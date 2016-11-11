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
            [open-company-web.components.su-snapshot :refer (su-snapshot)]
            [open-company-web.components.prior-updates :refer (prior-updates)]
            [open-company-web.components.topics-columns :refer (topics-columns)]
            [open-company-web.components.ui.small-loading :refer (small-loading)]))

(def topic-total-x-padding 20)
(def updates-content-list-width 260)
(def updates-content-cards-right-margin 40)
(def updates-content-cards-max-width 560)
(def updates-content-cards-min-width 250)

(defn load-latest-su [owner data]
  (when (and (dis/stakeholder-update-list-data data)
             (not (dis/latest-stakeholder-update data)))
    (let [su-list (:collection (dis/stakeholder-update-list-data data))
          last-su (last (:stakeholder-updates su-list))]
      (when (not (om/get-state owner :selected-su))
        (om/set-state! owner :selected-su (:slug last-su))
        (api/get-stakeholder-update (router/current-company-slug) (:slug last-su) false)))))

(defn load-su-list-if-needed [owner data]
  (when (and (dis/company-data data)
             (not (:su-list-loading data))
             (not (dis/stakeholder-update-list-data data (router/current-company-slug))))
    (dis/dispatch! [:get-su-list])))

(defcomponent updates [data owner]

  (init-state [_]
    (load-su-list-if-needed owner data)
    (let [current-update-slug (router/current-stakeholder-update-slug)]
      (when current-update-slug
        (api/get-stakeholder-update (router/current-company-slug) current-update-slug false))
      {:columns-num (responsive/columns-num)
       :su-list (dis/stakeholder-update-list-data)
       :selected-su current-update-slug}))

  (will-receive-props [_ next-props]
    (load-su-list-if-needed owner next-props)
    (load-latest-su owner next-props)
    (om/update-state! owner #(merge % {:su-list (dis/stakeholder-update-list-data)})))

  (did-mount [_]
    (load-su-list-if-needed owner data)
    (load-latest-su owner data)
    (om/set-state! owner :resize-listener
      (events/listen js/window EventType/RESIZE #(om/set-state! owner :columns-num (responsive/columns-num)))))

  (will-unmount [_]
    (when-let [resize-listener (om/get-state owner :resize-listener)]
      (events/unlistenByKey resize-listener)))

  (did-update [_ _ prev-state]
    (when (not= (:selected-su prev-state) (om/get-state owner :selected-su))
      (api/get-stakeholder-update (router/current-company-slug) (om/get-state owner :selected-su) false)))

  (render-state [_ {:keys [columns-num su-list selected-su]}]
    (let [company-data (dis/company-data data)
          card-width   (responsive/calc-card-width)
          ww           (.-clientWidth (.-body js/document))
          total-width-int (- (* (+ card-width topic-total-x-padding) columns-num) ; width of each column less
                             20                                                   ; the container padding
                             40)                                                  ; the distance btw the columns
          total-width  (str total-width-int "px")
          fixed-total-width-int (if (<= total-width-int (+ updates-content-cards-min-width updates-content-cards-right-margin updates-content-list-width))
                                  (+ updates-content-cards-min-width updates-content-cards-right-margin updates-content-list-width)
                                  total-width-int)
          total-width  (str fixed-total-width-int "px")
          fixed-card-width (if (>= (- fixed-total-width-int updates-content-list-width updates-content-cards-right-margin) updates-content-cards-max-width)
                              updates-content-cards-max-width
                              (- fixed-total-width-int updates-content-list-width updates-content-cards-right-margin))
          su-data (dis/stakeholder-update-data data (:slug company-data) selected-su)
          update-title (if su-data
                          (if (clojure.string/blank? (:title su-data))
                            (str (:name (dis/company-data)) " Update")
                            (:title su-data))
                          "")]
      (if-not (dis/stakeholder-update-list-data data)
        (dom/div {:id "oc-loading"})
        (dom/div {:class "updates main-scroll group"}
          (dom/div {:class "page"}
            (om/build navbar {:card-width card-width
                              :columns-num columns-num
                              :company-data company-data
                              :foce-key (:foce-key data)
                              :active :updates
                              :mobile-menu-open (:mobile-menu-open data)
                              :auth-settings (:auth-settings data)})
            (dom/div {:class "updates-inner group navbar-offset"}
              (dom/div {:class "updates-content group"
                        :style {:width total-width}}
                (dom/div {:class "updates-content-list group right"
                          :style {:width (str updates-content-list-width "px")}}
                  (prior-updates false selected-su))
                (dom/div {:class "updates-content-cards right"
                          :style {:width (str fixed-card-width "px")}}
                  (when (and selected-su
                             (not su-data))
                    (dom/div {:class "center"}
                      (small-loading)))
                  (when su-data
                    (dom/h3 {:class "updates-content-cards-title"} update-title))
                  (when su-data
                    (om/build topics-columns {:columns-num 1
                                              :card-width (- fixed-card-width 10) ; remove 10 padding on the right
                                              :total-width (- fixed-card-width 10)
                                              :is-stakeholder-update true
                                              :content-loaded (not (:loading data))
                                              :topics (:sections su-data)
                                              :topics-data su-data
                                              :company-data company-data
                                              :hide-add-topic true
                                              :show-share-remove false})))))
            (om/build footer {:card-width card-width
                              :columns-num columns-num
                              :company-data company-data})))))))

(defcomponent updates-responsive-switcher [data owner]

  (init-state [_]
    {:mobile-size (responsive/is-mobile-size?)})

  (did-mount [_]
    (om/set-state! owner :resize-listener
      (events/listen js/window EventType/RESIZE #(om/set-state! owner :mobile-size (responsive/is-mobile-size?)))))

  (will-unmount [_]
    (when-let [resize-listener (om/get-state owner :resize-listener)]
      (events/unlistenByKey resize-listener)))

  (render-state [_ {:keys [mobile-size]}]
    (if mobile-size
      (if (router/current-stakeholder-update-slug)
        (om/build su-snapshot data)
        (prior-updates true nil))
      (om/build updates data))))