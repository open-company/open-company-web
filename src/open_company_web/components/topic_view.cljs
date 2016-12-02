(ns open-company-web.components.topic-view
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.api :as api]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.topic :refer (topic)]
            [open-company-web.components.topic-edit :refer (topic-edit)]
            [cuerdas.core :as s]))

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
                                  company-data
                                  foce-key
                                  foce-data
                                  foce-data-editing?] :as data} owner options]
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
          topic-card-width (responsive/calc-update-width columns-num)
          topic-data (->> selected-topic-view keyword (get company-data))
          revisions (rest (:revisions-data topic-data))]
      (dom/div {:class "topic-view"
                :style {:width (str topic-card-width "px")
                        :margin-right (str (max 0 (- topic-view-width topic-card-width 50)) "px")}
                :key (str "topic-view-inner-" selected-topic-view)}
        (dom/div {:class "topic-view-internal"
                  :style {:width (str topic-card-width "px")}}
          (dom/div {:class "fake-textarea"}
            (if foce-key
              (dom/div {:class "topic topic-edit"
                        :style {:width (str (- topic-card-width 60) "px")}}
                (om/build topic-edit {:section selected-topic-view
                                      :topic-data topic-data
                                      :is-stakeholder-update false
                                      :currency (:currency company-data)
                                      :card-width (- card-width 60)
                                      :foce-data-editing? foce-data-editing?
                                      :read-only-company (:read-only-company data)
                                      :foce-key foce-key
                                      :foce-data foce-data}
                                     {:opts options
                                      :key (str "topic-foce-" selected-topic-view)}))
              (dom/div {:class "fake-textarea-internal"
                        :on-click #(dis/dispatch! [:start-foce (keyword selected-topic-view) {:title "" :body-placeholder (str "Write something new about " (s/capital selected-topic-view) ".") :data [] :headline ""}])
                        :style {:width (str (- topic-card-width 100) "px")}}
                "Write something new about " (s/capital selected-topic-view) ".")))
          (dom/div {:class "revision-container group"}
            (dom/hr {:class "separator-line"
                     :style {:width (str (- topic-card-width 100) "px")}})
            (om/build topic {:section selected-topic-view
                             :section-data topic-data
                             :card-width (- topic-card-width 60)
                             :foce-data-editing? (:foce-data-editing? data)
                             :read-only-company (:read-only company-data)
                             :currency (:currency company-data)
                             :is-topic-view true}
                             {:opts {:section-name selected-topic-view}}))
          (for [rev revisions]
            (when rev
              (dom/div {:class "revision-container group"}
                (dom/hr {:class "separator-line"
                         :style {:width (str (- topic-card-width 60) "px")}})
                (om/build topic {:section selected-topic-view
                                 :section-data rev
                                 :card-width (- topic-card-width 60)
                                 :foce-data-editing? (:foce-data-editing? data)
                                 :read-only-company (:read-only company-data)
                                 :currency (:currency company-data)
                                 :is-topic-view true}
                                 {:opts {:section-name selected-topic-view}})))))))))