(ns open-company-web.components.updates
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [open-company-web.api :as api]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.footer :refer (footer)]
            [open-company-web.components.ui.navbar :refer (navbar)]
            [open-company-web.components.prior-updates :refer (prior-updates)]
            [open-company-web.components.topics-columns :refer (topics-columns)]))

(def topic-total-x-padding 20)
(def updates-content-list-width 280)
(def updates-content-cards-max-width 670)

(defn load-latest-su [owner]
  (when-not (dis/latest-stakeholder-update)
    (let [su-list (:collection (dis/stakeholder-update-list-data))
          last-su (last (:stakeholder-updates su-list))]
      (when (not (om/get-state owner :selected-su))
        (om/set-state! owner :selected-su (:slug last-su))
        (api/get-stakeholder-update (router/current-company-slug) (:slug last-su))))))

(defcomponent updates [data owner]

  (init-state [_]
    {:columns-num (responsive/columns-num)
     :su-list (dis/stakeholder-update-list-data)
     :selected-su nil})

  (will-receive-props [_ _]
    (load-latest-su owner)
    (om/update-state! owner #(merge % {:su-list (dis/stakeholder-update-list-data)})))

  (did-mount [_]
    (load-latest-su owner)
    (events/listen js/window EventType/RESIZE #(om/set-state! owner :columns-num (responsive/columns-num))))

  (did-update [_ _ prev-state]
    (when (not= (:selected-su prev-state) (om/get-state owner :selected-su))
      (api/get-stakeholder-update (router/current-company-slug) (om/get-state owner :selected-su))))

  (render-state [_ {:keys [columns-num su-list selected-su]}]
    (let [company-data (dis/company-data data)
          card-width   (responsive/calc-card-width)
          ww           (.-clientWidth (.-body js/document))
          total-width-int (if (< ww card-width)
                            ww
                            (- (* (+ card-width topic-total-x-padding) columns-num) ; width of each column less
                               20))                                                 ; the container padding
          total-width  (if (>= ww responsive/c1-min-win-width)
                          (str total-width-int "px")
                          "auto")
          fixed-card-width (if (>= (- total-width-int updates-content-list-width) updates-content-cards-max-width)
                              updates-content-cards-max-width
                              (- total-width-int updates-content-list-width))
          su-data (dis/stakeholder-update-data data (:slug company-data) selected-su)
          update-title (if su-data
                          (if (clojure.string/blank? (:title su-data))
                            (str (:name (dis/company-data)) " Update")
                            (:title su-data))
                          "")]
      (dom/div {:class "updates main-scroll group"}
        (dom/div {:class "page"}
          (om/build navbar {:card-width card-width
                            :columns-num columns-num
                            :company-data company-data
                            :foce-key (:foce-key data)
                            :active :updates
                            :auth-settings (:auth-settings data)})
          (dom/div {:class "updates-inner group navbar-offset"}
            (dom/div {:class "updates-content group"
                      :style {:width total-width}}
              (dom/div {:class "updates-content-list group right"
                        :style {:width (str updates-content-list-width "px")}}
                (dom/div {:class "center"}
                  (dom/button {:class "new-update-btn btn-reset btn-outline"} "SHARE A NEW UPDATE"))
                (prior-updates selected-su #(om/set-state! owner :selected-su %)))
              (dom/div {:class "updates-content-cards right"
                        :style {:width (str fixed-card-width "px")}}
                (dom/h3 {:class "updates-content-cards-title"} update-title)
                (om/build topics-columns {:columns-num 1
                                          :card-width (- fixed-card-width 10) ; remove 10 padding on the right
                                          :total-width (- fixed-card-width 10)
                                          :content-loaded (not (:loading data))
                                          :topics (:sections su-data)
                                          :topics-data su-data
                                          :company-data company-data
                                          :hide-add-topic true}))))
          (om/build footer {:card-width card-width
                            :columns-num columns-num
                            :company-data company-data}))))))