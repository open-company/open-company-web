(ns open-company-web.components.topic-view
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.api :as api]
            [open-company-web.urls :as oc-urls]
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
      ; Do not request old revisions if there is the :new key
      ; since it was newly added from the add topic component
      ; and also it's not an archived topic being reactivated
      (when-not (:placeholder topic-data)
        (om/set-state! owner :revisions-requested true)
        (api/load-revisions (router/current-company-slug) topic-name revisions-link)))))

(defn start-foce-if-needed [owner]
  (let [{:keys [foce-key
                company-data
                selected-topic-view]} (om/get-props owner)]
    (when (and (nil? foce-key)
               (not (:read-only company-data)))
      (let [section-kw (keyword selected-topic-view)
            topic-data (get company-data section-kw)]
        (when (:placeholder topic-data)
          (dis/dispatch! [:start-foce section-kw (dissoc topic-data :updated-at)]))))))

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
    (start-foce-if-needed owner))

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
          revisions (if is-custom-section (vec (butlast (:revisions-data topic-data))) (:revisions-data topic-data))
          is-new-foce (and (= foce-key section-kw) (nil? (:updated-at foce-data)))
          is-another-foce (and (not (nil? foce-key)) (not (nil? (:updated-at foce-data))))]
      (dom/div {:class (str "topic-view" (when (responsive/is-tablet-or-mobile?) " tablet-view"))
                :style {:width (if (responsive/is-tablet-or-mobile?) "100%" (str topic-card-width "px"))
                        :margin-right (if (responsive/is-tablet-or-mobile?) "0px" (str (max 0 (- topic-view-width topic-card-width 50)) "px"))}
                :key (str "topic-view-inner-" selected-topic-view)}
        (when (responsive/is-tablet-or-mobile?)
          (dom/div {:class "topic-view-navbar"}
            (dom/div {:class "topic-view-navbar-close left"
                      :on-click #(router/nav! (oc-urls/company))} "<")
            (dom/div {:class "topic-view-navbar-title left"} (:title topic-data))))
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
                                        :foce-data foce-data}
                                       {:opts options
                                        :key (str "topic-foce-" selected-topic-view "-new")}))
                (let [initial-data (utils/new-section-initial-data selected-topic-view (:title topic-data) topic-data)
                      with-data (if (#{:growth :finances} section-kw) (assoc initial-data :data (:data topic-data)) initial-data)
                      with-metrics (if (= :growth section-kw) (assoc with-data :metrics (:metrics topic-data)) with-data)]
                  (dom/div {:class "fake-textarea-internal"
                            :on-click #(dis/dispatch! [:start-foce section-kw with-metrics])
                            :style {:width (str (- topic-card-width 100) "px")}}
                    "Write something new here...")))))
          ;; Render the topic from the company data only until the revisions are loaded.
          (when (and (not revisions)
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
                                 :show-editing true}
                                 {:opts {:section-name selected-topic-view}
                                  :key (str "topic-"
                                        (when foce-key
                                          "foce-")
                                        selected-topic-view "-" (:updated-at rev))})))))))))