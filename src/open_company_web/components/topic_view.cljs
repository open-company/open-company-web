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
    (when (or (not= (:selected-topic-view next-props) selected-topic-view)
              (and (nil? (:foce-key next-props))
                   (not (nil? foce-data))))
      (js/console.log "Reload revisions")
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
          (when-not (:read-only company-data)
            (dom/div {:class "fake-textarea"}
              (if foce-key
                (dom/div {:class "topic topic-edit"
                          :style {:width (str (- topic-card-width 120) "px")}}
                  (om/build topic-edit {:section selected-topic-view
                                        :topic-data topic-data
                                        :is-stakeholder-update false
                                        :currency (:currency company-data)
                                        :card-width (- topic-card-width 120)
                                        :foce-data-editing? foce-data-editing?
                                        :foce-key foce-key
                                        :foce-data foce-data}
                                       {:opts options
                                        :key (str "topic-foce-" selected-topic-view)}))
                (let [section-kw (keyword selected-topic-view)
                      initial-data {:section section-kw
                                    :title (s/capital selected-topic-view)
                                    :body-placeholder (str "Write something new about " (s/capital selected-topic-view) ".")
                                    :data (when (contains? topic-data :data) (:data topic-data))
                                    :metrics (when (contains? topic-data :metrics) (:metrics topic-data))
                                    :headline ""}
                      with-data (if (#{:growth :finances} section-kw) (assoc initial-data :data (:data topic-data)) initial-data)
                      with-metrics (if (= :growth section-kw) (assoc with-data :metrics (:metrics topic-data)) with-data)]
                  (dom/div {:class "fake-textarea-internal"
                            :on-click #(dis/dispatch! [:start-foce section-kw with-metrics])
                            :style {:width (str (- topic-card-width 100) "px")}}
                    "Write something new about " (s/capital selected-topic-view) ".")))))
          (dom/div {:class "revision-container group"}
            (when-not (:read-only company-data)
              (dom/hr {:class "separator-line"
                       :style {:width (str (- topic-card-width 100) "px")}}))
            (om/build topic {:section selected-topic-view
                             :section-data topic-data
                             :card-width (- topic-card-width 60)
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
                                 :read-only-company (:read-only company-data)
                                 :currency (:currency company-data)
                                 :is-topic-view true}
                                 {:opts {:section-name selected-topic-view}})))))))))