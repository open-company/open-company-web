(ns open-company-web.components.topic-view
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.api :as api]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.tooltip :as t]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.topic :refer (topic)]
            [open-company-web.components.topic-edit :refer (topic-edit)]
            [open-company-web.components.ui.popover :refer (add-popover hide-popover)]
            [open-company-web.components.ui.loading :refer (loading)]
            [cuerdas.core :as s]))

(defn- remove-topic-click [owner e]
  (add-popover {:container-id "archive-topic-confirm"
                :message utils/before-archive-message
                :height "170px"
                :cancel-title "KEEP IT"
                :cancel-cb #(hide-popover nil "archive-topic-confirm")
                :success-title "ARCHIVE"
                :success-cb #(let [section (om/get-props owner :selected-topic-view)]
                               (dis/dispatch! [:topic-archive section])
                               (utils/after 1 (fn [] (router/nav! (oc-urls/company))))
                               (hide-popover nil "archive-topic-confirm"))}))

(defn load-revisions-if-needed [owner]
  (when (not (om/get-state owner :revisions-requested))
    (when-let* [topic-name (om/get-props owner :selected-topic-view)
                company-data (om/get-props owner :company-data)
                topic-data (->> topic-name keyword (get company-data))
                revisions-link (utils/link-for (:links topic-data) "revisions" "GET")]
      ; Do not request old revisions if there is the :new key
      ; since it was newly added from the add topic component
      ; and also it's not an archived topic being reactivated
      (when-not (:placeholder topic-data)
        (om/set-state! owner :revisions-requested true)
        (api/load-revisions (router/current-company-slug) topic-name revisions-link)))))

(defn start-foce-if-needed [owner]
  (let [{:keys [foce-key
                company-data
                selected-topic-view
                new-sections]} (om/get-props owner)]
    (when (nil? foce-key)
      (let [section-kw (keyword selected-topic-view)
            sections-contains-topic (utils/in? (:sections company-data) selected-topic-view)]
        (when (not sections-contains-topic)
          (if (:read-only company-data)
            (router/redirect! (oc-urls/company (:slug company-data)))
            ; look for the urls in the new sections
            (when new-sections
              (let [new-section (first (filter #(= (:section-name %) selected-topic-view) new-sections))
                    new-section-data (utils/new-section-initial-data section-kw (:title new-section) {:links (:links new-section)})]
                (if (and new-section (contains? new-section :links))
                  (dis/dispatch! [:add-topic section-kw (assoc new-section-data :new true)])
                  (router/redirect! (oc-urls/company (:slug company-data))))))))))))

(defcomponent topic-view [{:keys [card-width
                                  columns-num
                                  selected-topic-view
                                  company-data
                                  foce-key
                                  foce-data
                                  foce-data-editing?] :as data} owner options]
  (init-state [_]
    {:revisions-requested false})

  (did-mount [_]
    (dis/dispatch! [:show-add-topic false])
    (load-revisions-if-needed owner)
    (start-foce-if-needed owner)
    (when (and (= (count (:sections company-data)) 1)
               (= (count (:archived company-data)) 0))
      (utils/after 500
        #(let [first-foce (str "first-foce-" (:slug company-data))]
          (t/tooltip (.querySelector js/document "div.topic-view") {:desktop "Enter your information. You can select text for easy formatting options, and jazz it up with a headline, emoji or image."
                                                                    :id first-foce
                                                                    :once-only true
                                                                    :config {:place "right-bottom"}})
          (t/show first-foce)))))

  (will-receive-props [_ next-props]
    (when (and (:foce-key data)
               (nil? (:foce-key next-props)))
      (t/hide (str "first-foce-" (:slug (:company-data next-props))))))

  (will-unmount [_]
    (t/hide (str "first-foce-" (:slug company-data))))

  (will-update [_ next-props _]
    (when (not= (:selected-topic-view next-props) selected-topic-view)
      (om/set-state! owner :revisions-requested false)))

  (did-update [_ _ _]
    (load-revisions-if-needed owner)
    (start-foce-if-needed owner))

  (render [_]
    (let [section-kw (keyword selected-topic-view)
          topic-view-width (responsive/topic-view-width card-width columns-num)
          topic-card-width (responsive/calc-update-width columns-num)
          topic-data (get company-data section-kw)
          is-custom-section (s/starts-with? (:section topic-data) "custom-")
          revisions (:revisions-data topic-data)
          is-new-foce (and (= foce-key section-kw) (nil? (:created-at foce-data)))
          is-another-foce (and (not (nil? foce-key)) (not (nil? (:created-at foce-data))))
          loading-topic-data (and (contains? company-data section-kw)
                                  (:loading topic-data))]
      (dom/div {:class "topic-view-container group"
                :style {:width (if (responsive/is-tablet-or-mobile?) "100%" (str topic-card-width "px"))
                        :margin-right (if (responsive/is-tablet-or-mobile?) "0px" (str (max 0 (- topic-view-width topic-card-width 50)) "px"))}
                :key (str "topic-view-inner-" selected-topic-view)}
        (dom/div {:class (utils/class-set {:topic-view true
                                           :group true
                                           :tablet-view (responsive/is-tablet-or-mobile?)
                                           :topic-404 (nil? topic-data)})}
          (when (responsive/is-tablet-or-mobile?)
            (dom/div {:class "topic-view-navbar"}
              (dom/div {:class "topic-view-navbar-close left"
                        :on-click #(router/nav! (oc-urls/company))} "<")
              (dom/div {:class "topic-view-navbar-title left"} (:title topic-data))))
          (if (or (:loading company-data)
                  loading-topic-data)
            (dom/div {:class "topic-view-internal loading group"}
              (om/build loading {:loading true}))
            (dom/div {:class "topic-view-internal"
                      :style {:width (if (responsive/is-tablet-or-mobile?) "auto" (str topic-card-width "px"))}}
              (when (and (not (:read-only company-data))
                         (not (responsive/is-tablet-or-mobile?)))
                (dom/div {:class (str "fake-textarea " (when is-another-foce "disabled"))}
                  (if is-new-foce
                    (dom/div {:class "topic topic-edit"
                              :style {:width (str (- topic-card-width 120) "px")}}
                      (om/build topic-edit {:is-stakeholder-update false
                                            :currency (:currency company-data)
                                            :card-width (- topic-card-width 120)
                                            :columns-num columns-num
                                            :is-topic-view true
                                            :foce-data-editing? foce-data-editing?
                                            :foce-key foce-key
                                            :show-archive-button false
                                            :foce-data foce-data}
                                           {:opts options
                                            :key (str "topic-foce-" selected-topic-view "-new")}))
                    (let [initial-data (utils/new-section-initial-data selected-topic-view (:title topic-data) topic-data)
                          with-data (if (#{:growth :finances} section-kw) (assoc initial-data :data (:data topic-data)) initial-data)
                          with-metrics (if (= :growth section-kw) (assoc with-data :metrics (:metrics topic-data)) with-data)]
                      (dom/div {:class "fake-textarea-internal"
                                :on-click #(dis/dispatch! [:start-foce section-kw with-metrics])
                                :style {:width (str (- topic-card-width 100) "px")}}
                        "Start a new entry...")))))
              ;; Render the topic from the company data only until the revisions are loaded.
              (when (and (not foce-key)
                         (not revisions)
                         (not (:placeholder topic-data)))
                (dom/div {:class "revision-container group"}
                  (when (and (not (:read-only company-data))
                             (not (responsive/is-tablet-or-mobile?)))
                    (dom/hr {:class "separator-line"
                             :style {:width (if (responsive/is-tablet-or-mobile?) "auto" (str (- topic-card-width 80) "px"))}}))
                  (om/build topic {:section selected-topic-view
                                   :section-data topic-data
                                   :card-width (- topic-card-width 60)
                                   :is-stakeholder-update false
                                   :read-only-company (:read-only company-data)
                                   :currency (:currency company-data)
                                   :is-topic-view true
                                   :foce-data-editing? foce-data-editing?
                                   :foce-key foce-key
                                   :foce-data foce-data
                                   :show-editing false}
                                   {:opts {:section-name selected-topic-view}})))
              (for [idx (range (count revisions))
                    :let [rev (get revisions idx)]]
                (when rev
                  (dom/div {:class "revision-container group"}
                    (when-not (and (= idx 0)
                                   (or (responsive/is-tablet-or-mobile?)
                                       (:read-only company-data)))
                      (dom/hr {:class "separator-line"
                               :style {:width (if (responsive/is-tablet-or-mobile?) "auto" (str (- topic-card-width 60) "px"))}}))
                    (om/build topic {:section selected-topic-view
                                     :section-data rev
                                     :card-width (- topic-card-width 60)
                                     :is-stakeholder-update false
                                     :read-only-company (:read-only company-data)
                                     :currency (:currency company-data)
                                     :is-topic-view true
                                     :foce-data-editing? foce-data-editing?
                                     :foce-key foce-key
                                     :foce-data foce-data
                                     :show-archive-button (= (count revisions) 1)
                                     :show-editing true}
                                     {:opts {:section-name selected-topic-view}
                                      :key (str "topic-"
                                            (when foce-key
                                              "foce-")
                                            selected-topic-view "-" (:updated-at rev))})))))))
        (when (and (not loading-topic-data)
                   (not (responsive/is-tablet-or-mobile?))
                   (not (:read-only company-data))
                   (> (count revisions) 1))
          (dom/button {:class "btn-reset btn-link archive-topic"
                       :on-click (partial remove-topic-click owner)} "Archive this topic"))))))