(ns oc.web.components.updates
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [oc.web.api :as api]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.footer :refer (footer)]
            [oc.web.components.ui.navbar :refer (navbar)]
            [oc.web.components.su-snapshot :refer (su-snapshot)]
            [oc.web.components.prior-updates :refer (prior-updates)]
            [oc.web.components.topics-columns :refer (topics-columns)]
            [oc.web.components.ui.loading :refer (loading)]))

(defn load-latest-su [owner data]
  (when (and (:updates-list-loaded data)
             (not (om/get-state owner :selected-su)))
    (let [updates-list (:collection (dis/updates-list-data data))
          last-su (last (:stakeholder-updates updates-list))]
      (om/set-state! owner :selected-su (:slug last-su)))))

(defn load-updates-list-if-needed [owner data]
  (when (and (dis/org-data (om/get-props owner))
             (not (:updates-list-loading data))
             (not (:updates-list-loaded data))
             (not (om/get-state owner :updates-list-loading)))
    (om/set-state! owner :updates-list-loading true)
    (dis/dispatch! [:get-updates-list])))

(defcomponent updates [data owner]

  (init-state [_]
    (let [current-update-slug (router/current-stakeholder-update-slug)]
      (when current-update-slug
        (api/get-stakeholder-update (router/current-board-slug) current-update-slug false))
      {:columns-num (responsive/columns-num)
       :card-width (responsive/calc-card-width)
       :updates-list (dis/updates-list-data)
       :updates-list-loading false
       :selected-su current-update-slug}))

  (will-receive-props [_ next-props]
    (load-updates-list-if-needed owner next-props)
    (when (:updates-list-loaded next-props)
      (load-latest-su owner next-props))
    (om/update-state! owner #(merge % {:updates-list (dis/updates-list-data)})))

  (did-mount [_]
    (when (:updates-list-loaded data)
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
      (api/get-stakeholder-update (router/current-board-slug) (om/get-state owner :selected-su) false)))

  (render-state [_ {:keys [columns-num card-width updates-list selected-su]}]
    (let [org-data (dis/org-data data)
          total-width-int (:total-width-int data)
          total-width (str total-width-int "px")
          fixed-card-width (responsive/calc-update-width columns-num)
          su-data (dis/stakeholder-update-data data (:slug org-data) selected-su)
          update-title (if su-data
                          (if (clojure.string/blank? (:title su-data))
                            (str (:name org-data) " Update")
                            (:title su-data))
                          "")]
      (dom/div {:class "updates-inner group navbar-offset"}
        (dom/div {:class "updates-content group"
                  :style {:width total-width}}
          (dom/div {:class "updates-content-list group left"
                    :style {:width (str responsive/updates-content-list-width "px")}}
            (prior-updates false selected-su))
          (dom/div {:class (str "updates-content-cards left" (when su-data " has-content"))
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
                                        :org-data org-data
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
    (let [org-data (dis/org-data data)
          total-width-int (responsive/total-layout-width-int card-width columns-num)
          mobile-layout (<= (responsive/ww) responsive/updates-list-breakpoint)]
      (if-not (dis/updates-list-data data)
        (dom/div {:class "oc-loading active"} (dom/i {:class "fa fa-circle-o-notch fa-spin"}))
        (dom/div {:class (str "updates main-scroll group" (when mobile-layout " mobile-layout"))}
          (dom/div {:class "page"}
            (om/build navbar (merge data {:card-width card-width
                                          :columns-num columns-num
                                          :header-width total-width-int
                                          :org-data org-data
                                          :show-share-su-button false
                                          :active :updates}))
            (if mobile-layout
              (prior-updates true nil)
              (om/build updates (merge data {:total-width-int total-width-int
                                             :mobile-layout mobile-layout}))))
          (footer total-width-int))))))