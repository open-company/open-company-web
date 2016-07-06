(ns open-company-web.components.su-edit
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.api :as api]
            [open-company-web.urls :as oc-urls]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.lib.prevent-route-dispatch :refer (prevent-route-dispatch)]
            [open-company-web.components.navbar :refer (navbar)]
            [open-company-web.components.topic-body :refer (topic-body)]
            [open-company-web.components.company-header :refer [company-header]]
            [open-company-web.components.su-edit-header :refer (su-edit-header)]
            [open-company-web.components.su-edit-footer :refer (su-edit-footer)]
            [open-company-web.components.ui.link :refer (link)]
            [goog.style :refer (setStyle)]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [clojure.string :as clj-string]))

(defn get-key-from-sections [sections]
  (clojure.string/join
    (map #(str (name (get % 0)) (clojure.string/join (get % 1))) sections)))

(defcomponent stakeholder-update-topic [data owner]
  (render [_]
    (let [section (:section data)
          section-data (:section-data data)
          headline (:headline section-data)]
      (dom/div {:class "update-topic"}

        ;; topic title
        (dom/div {:class "topic-title"} (:title section-data))

        ;; topic headline
        (when headline
          (dom/div {:class "topic-headline"} headline))
        
        ;; topic body
        (om/build topic-body {:section section
                              :section-data section-data
                              :currency (:currency data)
                              :expanded true})))))

(defn save-stakeholder-update [old-stakeholder-update active-topics]
  (let [new-stakeholder-update (assoc old-stakeholder-update :sections (vec active-topics))]
    (api/patch-stakeholder-update new-stakeholder-update)))

(defcomponent selected-topics [data owner]

  (render [_]
    (let [company-data (:company-data data)
          stakeholder-update (:stakeholder-update company-data)
          section-keys (map keyword (:sections stakeholder-update))
          all-sections-key (get-key-from-sections (:sections company-data))]
      (dom/div {:class "update-sections"
                :key all-sections-key}

        (dom/div {:class "update-sections-internal"}
          (dom/div {:class "update-sections-internal-width"}
            (dom/div {:class "overlay"})
            (for [section section-keys]
              (let [section-data (section company-data)]
                (when-not (and (:read-only section-data) (:placeholder section-data))
                  (om/build stakeholder-update-topic {
                                          :section-data section-data
                                          :section section
                                          :currency (:currency company-data)
                                          :loading (:loading data)}))))))))))

(defn fix-buttons-position [owner]
  (when-not (om/get-state owner :fixed-buttons-position)
    (when-let [su-header (om/get-ref owner "stakeholder-update-header")]
      (when-let [buttons (om/get-ref owner "floating-buttons")]
        (let [offset-top (utils/offset-top su-header)]
          (om/set-state! owner :fixed-buttons-position offset-top)
          (setStyle buttons #js {:top (str offset-top "px")}))))))

(defn- get-title [data]
  (let [title (:title data)]
    (if (clj-string/blank? title)
      (let [js-date (utils/js-date)
            month (utils/month-string (utils/add-zero (.getMonth js-date)))
            year (.getFullYear js-date)]
        (str month " " year " Update"))
      title)))

(def before-unload-message "You have unsaved changes.")

(defn save-click [owner]
  (let [title (om/get-state owner :title)
        intro-body (or (om/get-state owner :out-intro) (om/get-state owner :intro) "")
        outro-body (or (om/get-state owner :out-outro) (om/get-state owner :outro) "")]
    (api/patch-stakeholder-update {:title title
                                   :intro {:body intro-body}
                                   :sections (om/get-state owner :sections)
                                   :outro {:body outro-body}})))

(defn cancel-click [owner]
  (om/set-state! owner :has-changes false)
  (let [current-state (om/get-state owner)]
    (om/set-state! owner :title (:initial-title current-state))
    (om/set-state! owner :intro (:initial-intro current-state))
    (om/set-state! owner :outro (:initial-outro current-state))
    (om/set-state! owner :force-content-update true)
    (utils/after 100 #(om/set-state! owner :force-content-update false)))
  (router/nav! (oc-urls/stakeholder-update-list)))

(defn get-state [data current-state]
  (let [company-data (dispatcher/company-data data)
        su-data (:stakeholder-update company-data)
        title (:title su-data)
        sections (:sections su-data)
        intro (:body (:intro su-data))
        outro (:body (:outro su-data))
        fixed-title (get-title su-data)]
    {:title (get-title su-data)
     :initial-title title
     :history-listener-id (or (:history-listener-id current-state) nil)
     :has-changes (not= fixed-title title)
     :sections sections
     :initial-intro intro
     :intro intro
     :initial-outro outro
     :outro outro
     :force-content-update false
     :fixed-buttons-position (or (:fixed-buttons-position current-state) false)}))

(defn change-cb [owner k v]
  (om/set-state! owner :has-changes true)
  (cond
    (= k :outro)
    (om/set-state! owner :out-outro v)

    (= k :intro)
    (om/set-state! owner :out-intro v)

    :else
    (om/set-state! owner k v)))

(defn share-click [owner]
  (api/share-stakeholder-update))

(defcomponent su-edit [data owner]

  (init-state [_]
    (get-state data nil))

  (will-unmount [_]
    (when-not (utils/is-test-env?)
      ; re enable the route dispatcher
      (reset! prevent-route-dispatch false)
      ; remove the onbeforeunload handler
      (set! (.-onbeforeunload js/window) nil)
      ; remove history change listener
      (events/unlistenByKey (om/get-state owner :history-listener-id))))

  (did-mount [_]
    (fix-buttons-position owner)
    (when-not (utils/is-test-env?)
      (utils/after 100 (fn []
        (reset! prevent-route-dispatch true)
        (let [win-location (.-location js/window)
              current-token (str (.-pathname win-location) (.-search win-location) (.-hash win-location))
              listener (events/listen @router/history EventType/NAVIGATE
                         #(when-not (= (.-token %) current-token)
                            (if (om/get-state owner :has-changes)
                              (if (js/confirm (str before-unload-message " Are you sure you want to leave this page?"))
                                ; dispatch the current url
                                (@router/route-dispatcher (router/get-token))
                                ; go back to the previous token
                                (.setToken @router/history current-token))
                              ; dispatch the current url
                              (@router/route-dispatcher (router/get-token)))))]
          (om/set-state! owner :history-listener-id listener))))))

  (did-update [_ _ _]
    (fix-buttons-position owner))

  (will-receive-props [_ next-props]
    (om/set-state! owner (get-state next-props (om/get-state owner)))
    (when (:su-edit next-props)
      (router/nav! (oc-urls/stakeholder-update-list))))

  (render-state [_ {:keys [has-changes title intro sections outro force-content-update]}]
    ; set the onbeforeunload handler only if there are changes
    (let [onbeforeunload-cb (when has-changes #(str before-unload-message))]
      (set! (.-onbeforeunload js/window) onbeforeunload-cb))
    (let [company-data (dispatcher/company-data data)
          su-data (:stakeholder-update company-data)]

      (utils/update-page-title (str "OpenCompany - Stakeholder Update Edit - " (:name company-data)))

      (cond

        ;; The data is still loading
        (:loading data)
        (dom/div
          (dom/h4 "Loading data..."))

        ;; Stakeholder update
        (and (not (contains? data :loading)) company-data)
        (dom/div {:class (utils/class-set {:su-edit true
                                           :navbar-offset (not (responsive/is-mobile))})}
          ;; Company / user header
          (when-not (responsive/is-mobile)
            (om/build navbar data))
            
          ;; Company header
          (om/build company-header {
              :editing-topic true ; no category nav
              :company-data company-data
              :stakeholder-update true})
          
          (dom/div #js {:className "update-internal"
                        :ref "update-internal"}
          
            (dom/div {:class "sections group"}; col-md-9 col-sm-12"}
              (dom/div #js {:className "floating-buttons group"
                            :ref "floating-buttons"}
                (when has-changes
                  (dom/button {:class "cancel"
                               :on-click #(cancel-click owner)} "Cancel"))
                (when has-changes
                  (dom/button {:class "save"
                               :on-click #(save-click owner)} "Save")))
              ;; share button
              (dom/div {:class "share-su"}
                (dom/button {:class "share"
                             :disabled (or (clj-string/blank? (:title su-data))
                                           (empty? sections))
                             :on-click #(share-click owner)} "Share"))
              ;; Stakeholder update header
              (dom/div #js {:className "stakeholder-update-header"
                            :ref "stakeholder-update-header"}
                (om/build su-edit-header {:title title
                                                     :intro intro
                                                     :update-content force-content-update}
                                                    {:opts {:change-cb (partial change-cb owner)}}))
              ;; Stakeholder update topics
              (om/build selected-topics {:company-data company-data
                                         :loading (:loading data)})
              ;; Stakeholder update footer
              (om/build su-edit-footer {:outro outro
                                                   :update-content force-content-update}
                                                  {:opts {:change-cb (partial change-cb owner)}})
              ;; Dashboard link
              (when (responsive/is-mobile)
                (dom/div {:class "dashboard-link"}
                  (om/build link {:href (oc-urls/company) :name "View Dashboard"}))))))


        ;; Error fallback
        :else
        (dom/div
          (dom/h2 "Company not found")
          (om/build link {:href oc-urls/companies :name "Back home"}))))))