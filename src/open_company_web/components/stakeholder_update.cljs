(ns open-company-web.components.stakeholder-update
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.router :as router]
            [open-company-web.local-settings :as ls]
            [open-company-web.api :as api]
            [open-company-web.caches :as caches]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.navbar :refer (navbar)]
            [open-company-web.components.topic-body :refer (topic-body)]
            [open-company-web.components.company-header :refer [company-header]]
            [open-company-web.components.section-selector :refer (section-selector)]
            [open-company-web.components.ui.link :refer (link)]
            [open-company-web.components.ui.side-drawer :refer (side-drawer)]
            [open-company-web.components.ui.drawer-toggler :refer (drawer-toggler)]
            [clojure.string :as str]
            [goog.style :refer (setStyle)]))

(defn get-key-from-sections [sections]
  (clojure.string/join
    (map #(str (name (get % 0)) (clojure.string/join (get % 1))) sections)))

(defcomponent prior-updates [data owner]
  (render [_]
    (dom/div "")))

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

(defcomponent selected-topics [data owner]
  (render [_]
    (let [stakeholder-update (:stakeholder-update data)
          section-keys (map keyword (:sections stakeholder-update))
          all-sections-key (get-key-from-sections (:sections data))]
      (dom/div {:class "update-sections"
                :key all-sections-key}
        (for [section section-keys]
          (let [section-data (section data)]
            (when-not (and (:read-only section-data) (:placeholder section-data))
              (om/build stakeholder-update-topic {
                                      :section-data section-data
                                      :section section
                                      :currency (:currency data)
                                      :loading (:loading data)}))))))))

(defcomponent stakeholder-update-intro [data owner]
  (render [_]
    (let [intro (:intro data)
          body (:body intro)
          title (:title intro)]
      (when-not (str/blank? body)
        (dom/div {:class "update-intro"}
          (dom/div {:class "intro-title"} (if (str/blank? title) "Current Update" title))
          (dom/div {:class "topic-body"}
            (dom/div {:class "topic-body-inner group"} body)))))))

(defn save-stakeholder-update [old-stakeholder-update active-topics]
  (let [new-stakeholder-update (assoc old-stakeholder-update :sections (vec active-topics))]
    (api/patch-stakeholder-update new-stakeholder-update)))

(defn fix-drawer-toggler-position [owner]
  (when-let [update-internal (om/get-ref owner "update-internal")]
    (let [offset (utils/absolute-offset update-internal)]
      (setStyle (sel1 [:div.drawer-toggler]) #js {:marginTop (str (:top offset) "px")})
      (om/set-state! owner :toggler-top-margin (:top offset)))))

(defcomponent stakeholder-update [data owner]

  (init-state [_]
    {:drawer-open false
     :toggler-top-margin false})

  (did-update [_ _ _]
    (when-not (om/get-state owner :toggler-top-margin)
      (fix-drawer-toggler-position owner)))

  (render-state [_ {:keys [drawer-open]}]
    (let [slug (keyword (:slug @router/path))
          company-data (get data slug)
          stakeholder-update-data (:stakeholder-update company-data)]

      (utils/update-page-title (str "OpenCompany - " (:name company-data)))

      (cond

        ;; The data is still loading
        (:loading data)
        (dom/div
          (dom/h4 "Loading data..."))

        ;; Stakeholder update
        (and (not (contains? data :loading)) (contains? data slug))
        (dom/div {:class (utils/class-set {:stakeholder-update true
                                           :navbar-offset (not (utils/is-mobile))})}
          ;; Company / user header
          (when-not (utils/is-mobile)
            (om/build navbar data))

          (dom/div {:class "stakeholder-update-drawer"}
            (when (and (not (:read-only company-data))
                       (not (utils/is-mobile))
                       (not (:loading data)))
              ;; drawer toggler
              (om/build drawer-toggler {:close (not drawer-open)
                                        :click-cb #(om/update-state! owner :drawer-open not)}))
            (when-not (or (:read-only company-data)
                          (utils/is-mobile)
                          (:loading data)))
            ;; side drawer
            (let [all-sections (vec (flatten (vals (:sections company-data))))
                  all-section-keys (map keyword all-sections)
                  list-data (merge data {:active true
                                         :all-topics (select-keys company-data all-section-keys)
                                         :active-topics-list (:sections stakeholder-update-data)})
                  list-opts {:did-change-active-topics (partial save-stakeholder-update stakeholder-update-data)}]
              (om/build side-drawer {:open drawer-open
                                     :list-key "su-update"
                                     :list-data list-data}
                                    {:opts {:list-opts list-opts
                                            :bg-click-cb #(om/set-state! owner :drawer-open false)}})))
            
          ;; Company header
          (om/build company-header {
              :editing-topic true ; no category nav
              :company-data company-data
              :stakeholder-update true})
          
          (dom/div #js {:className "update-internal row"
                        :ref "update-internal"}
          
            (dom/div {:class "sections col-md-9 col-sm-12"}
              ;; Stakeholder update intro
              (om/build stakeholder-update-intro stakeholder-update-data)
              ;; Stakeholder update topics
              (om/build selected-topics company-data)
              ;; Dashboard link
              (when (utils/is-mobile)
                (dom/div {:class "dashboard-link"}
                  (om/build link {:href (str "/" (:slug company-data)) :name "View Dashboard"}))))
            
            (dom/div {:class "col-md-3 col-sm-0"} 
              (om/build prior-updates company-data))))


        ;; Error fallback
        :else
        (dom/div
          (dom/h2 (str (name slug) " not found"))
          (om/build link {:href "/" :name "Back home"}))))))