(ns open-company-web.components.topic-view
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.api :as api]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.topic :refer (topic)]))

(defn load-revisions-if-needed [owner]
  (when (not (om/get-state owner :revisions-requested))
    (when-let* [topic-name (om/get-props owner :selected-topic-view)
                company-data (om/get-props owner :company-data)
                topic-data (->> topic-name keyword (get company-data))
                revisions-link (utils/link-for (:links topic-data) "revisions" "GET")]
      (om/set-state! owner :revisions-requested true)
      (api/load-revisions (router/current-company-slug) topic-name revisions-link))))

(defcomponent topic-view [{:keys [card-width
                                  columns-num
                                  selected-topic-view
                                  company-data] :as data} owner options]
  (init-state [_]
    {:revisions-requested false})

  (did-mount [_]
    (load-revisions-if-needed owner))

  (will-update [_ next-props _]
    (when (not= (:selected-topic-view next-props) selected-topic-view)
      (om/set-state! owner :revisions-requested false)))

  (did-update [_ _ _]
    (load-revisions-if-needed owner))

  (render [_]
    (let [topic-view-width (responsive/topic-view-width card-width columns-num)
          topic-data (->> selected-topic-view keyword (get company-data))
          revisions (rest (:revisions-data topic-data))]
      (dom/div {:class "topic-view"
                :style {:width (str card-width "px")
                        :margin-right (str (max 0 (- topic-view-width card-width 50)) "px")}
                :key (str "topic-view-inner-" selected-topic-view)}
        (dom/div {:class "topic-view-internal"
                  :style {:width (str card-width "px")}}
          (dom/div {:class "revision-container group"}
            (om/build topic {:section selected-topic-view
                             :section-data topic-data
                             :card-width (- card-width 60)
                             :foce-data-editing? (:foce-data-editing? data)
                             :read-only-company (:read-only company-data)
                             :currency (:currency company-data)
                             :is-topic-view true}
                             {:opts {:section-name selected-topic-view}}))
          (for [rev revisions]
            (when rev
              (dom/div {:class "revision-container group"}
                (dom/hr {:class "separator-line"
                         :style {:width (str (- card-width 60) "px")}})
                (om/build topic {:section selected-topic-view
                                 :section-data rev
                                 :card-width (- card-width 60)
                                 :foce-data-editing? (:foce-data-editing? data)
                                 :read-only-company (:read-only company-data)
                                 :currency (:currency company-data)
                                 :is-topic-view true}
                                 {:opts {:section-name selected-topic-view}})))))))))