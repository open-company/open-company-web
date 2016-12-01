(ns open-company-web.components.updates
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [open-company-web.api :as api]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.footer :refer (footer)]
            [open-company-web.components.ui.navbar :refer (navbar)]
            [open-company-web.components.ui.oc-switch :refer (oc-switch)]
            [open-company-web.components.su-snapshot :refer (su-snapshot)]
            [open-company-web.components.prior-updates :refer (prior-updates)]
            [open-company-web.components.topics-columns :refer (topics-columns)]
            [open-company-web.components.ui.loading :refer (loading)]))

(defn load-latest-su [owner data]
  (when (and (:su-list-loaded data)
             (not (om/get-state owner :selected-su)))
    (let [su-list (:collection (dis/stakeholder-update-list-data data))
          last-su (last (:stakeholder-updates su-list))]
      (om/set-state! owner :selected-su (:slug last-su)))))

(defn load-su-list-if-needed [owner data]
  (when (and (dis/company-data (om/get-props owner))
             (not (:su-list-loading data))
             (not (:su-list-loaded data))
             (not (om/get-state owner :su-list-loading)))
    (om/set-state! owner :su-list-loading true)
    (dis/dispatch! [:get-su-list])))

(defcomponent updates [data owner]

  (init-state [_]
    (let [current-update-slug (router/current-stakeholder-update-slug)]
      (when current-update-slug
        (api/get-stakeholder-update (router/current-company-slug) current-update-slug false))
      {:columns-num (responsive/columns-num)
       :card-width (responsive/calc-card-width)
       :su-list (dis/stakeholder-update-list-data)
       :su-list-loading false
       :selected-su current-update-slug}))

  (will-receive-props [_ next-props]
    (load-su-list-if-needed owner next-props)
    (when (:su-list-loaded next-props)
      (load-latest-su owner next-props))
    (om/update-state! owner #(merge % {:su-list (dis/stakeholder-update-list-data)})))

  (did-mount [_]
    (when (:su-list-loaded data)
      (load-latest-su owner data))
    (om/set-state! owner :resize-listener
      (events/listen js/window EventType/RESIZE (fn [] (om/update-state! owner #(merge % {:columns-num (responsive/columns-num)
                                                                                          :card-width (responsive/calc-card-width)}))))))

  (will-unmount [_]
    (when-let [resize-listener (om/get-state owner :resize-listener)]
      (events/unlistenByKey resize-listener)))

  (did-update [_ _ prev-state]
    ; if we had a selected-su and it changed let's load the new update data
    ; if selected-su was empty there is no need to load
    (when (not= (:selected-su prev-state) (om/get-state owner :selected-su))
      (api/get-stakeholder-update (router/current-company-slug) (om/get-state owner :selected-su) false)))

  (render-state [_ {:keys [columns-num card-width su-list selected-su]}]
    (let [company-data (dis/company-data data)
          total-width-int (:total-width-int data)
          total-width (str total-width-int "px")
          fixed-card-width (responsive/calc-update-width columns-num)
          su-data (dis/stakeholder-update-data data (:slug company-data) selected-su)
          update-title (if su-data
                          (if (clojure.string/blank? (:title su-data))
                            (str (:name (dis/company-data)) " Update")
                            (:title su-data))
                          "")]
      (dom/div {:class "updates-inner group navbar-offset"}
        (dom/div {:class "updates-content group"
                  :style {:width total-width}}
          (dom/div {:class "updates-content-list group right"
                    :style {:width (str responsive/updates-content-list-width "px")}}
            (prior-updates false selected-su))
          (dom/div {:class "updates-content-cards right"
                    :style {:width (str fixed-card-width "px")}}
            (when (and selected-su
                       (not su-data))
              ; if a SU was selected but the data are not loaded yet show a spinner
              (dom/div {:class "loading-container"}
                (om/build loading {:loading true})))
            (when su-data
              (dom/h3 {:class "updates-content-cards-title"} update-title))
            (when su-data
              (om/build topics-columns {:columns-num 1
                                        :card-width (- fixed-card-width 60) ; remove 60 padding
                                        :total-width (- fixed-card-width 60)
                                        :is-stakeholder-update true
                                        :content-loaded (not (:loading data))
                                        :topics (:sections su-data)
                                        :topics-data su-data
                                        :company-data company-data
                                        :hide-add-topic true}))))))))

(defcomponent updates-responsive-switcher [data owner]

  (init-state [_]
    (dis/dispatch! [:start-foce nil])
    {:columns-num (responsive/columns-num)
     :card-width (responsive/calc-card-width)})

  (did-mount [_]
    (om/set-state! owner :resize-listener
      (events/listen js/window EventType/RESIZE (fn [_] (om/update-state! owner #(merge % {:columns-num (responsive/columns-num)
                                                                                           :card-width (responsive/calc-card-width)}))))))

  (will-unmount [_]
    (when-let [resize-listener (om/get-state owner :resize-listener)]
      (events/unlistenByKey resize-listener)))

  (render-state [_ {:keys [columns-num card-width]}]
    (let [company-data (dis/company-data data)
          total-width-int (responsive/total-layout-width-int card-width columns-num)]
      (if-not (dis/stakeholder-update-list-data data)
        (dom/div {:class "oc-loading active"} (dom/i {:class "fa fa-circle-o-notch fa-spin"}))
        (dom/div {:class "updates main-scroll group"}
          (dom/div {:class "page"}
            (om/build navbar (merge data {:card-width card-width
                                          :columns-num columns-num
                                          :header-width total-width-int
                                          :company-data company-data
                                          :show-share-su-button (utils/can-edit-sections? company-data)
                                          :show-navigation-bar (utils/can-edit-sections? company-data)
                                          :active :updates}))
            (when (responsive/is-mobile-size?)
              (oc-switch :updates))
            (if (responsive/is-mobile-size?)
              (prior-updates true nil)
              (om/build updates (merge data {:total-width-int total-width-int}))))
          (om/build footer {:footer-width total-width-int}))))))