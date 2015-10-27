(ns open-company-web.components.growth.growth
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.components.update-footer :refer (update-footer)]
            [open-company-web.components.rich-editor :refer (rich-editor)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.revisions-navigator :refer [revisions-navigator]]
            [open-company-web.api :as api]
            [open-company-web.dispatcher :refer [app-state]]
            [open-company-web.components.editable-title :refer [editable-title]]
            [open-company-web.components.growth.growth-metric :refer [growth-metric]]))

(defn subsection-click [e owner]
  (.preventDefault e)
  (let [tab  (.. e -target -dataset -tab)]
    (om/update-state! owner :focus (fn [] tab))))

(defcomponent growth [data owner]
  (init-state [_]
    (let [company-data (:company-data data)
          metrics-data (:metrics (:growth company-data))]
      {:focus (:slug (first metrics-data))
       :read-only false}))
  (render [_]
    (let [focus (om/get-state owner :focus)
          growth-link-class :composed-section-link
          slug (:slug @router/path)
          company-data (:company-data data)
          growth-section (:growth company-data)
          metrics-data (:metrics growth-section)
          growth-data (:data growth-section)
          notes-data (:notes growth-section)
          read-only (or (:loading growth-section) (om/get-state owner :read-only))
          focus-metric-data (filter #(= (:slug %) focus) growth-data)
          focus-metric-info (first (filter #(= (:slug %) focus) metrics-data))
          subsection-data {:metric-data focus-metric-data
                           :metric-info focus-metric-info
                           :read-only read-only}]
      (dom/div {:class "row" :id "section-growth"}
        (dom/div {:class "growth composed-section"}
          (om/build editable-title {:read-only read-only
                                    :section-data growth-section
                                    :section :growth
                                    :save-channel "save-section-growth"})
          (dom/div {:class "link-bar"}
            (for [metric metrics-data]
              (let [mslug (:slug metric)
                    mname (:name metric)
                    metric-classes (utils/class-set {growth-link-class true
                                                     mslug true
                                                     :active (= focus mslug)})]
                (dom/a {:href "#"
                    :class metric-classes
                    :title mname
                    :data-tab mslug
                    :on-click #(subsection-click % owner)} mname))))
          (dom/div {:class (utils/class-set {:composed-section-body true
                                             :editable (not read-only)})}
            ;; growth metric currently shown
            (om/build growth-metric subsection-data)
            (om/build update-footer {:updated-at (:updated-at growth-section)
                                     :author (:author growth-section)
                                     :section :growth})
            (when (and (not read-only) (not (nil? notes-data)) (not (nil? (:body notes-data))))
              (om/build rich-editor {:read-only read-only
                                     :section-data notes-data
                                     :section :growth
                                     :save-channel "save-growth-notes"}))
            (om/build revisions-navigator {:revisions (:revisions growth-section)
                                           :section :growth
                                           :updated-at (:updated-at growth-section)
                                           :loading (:loading growth-section)
                                           :navigate-cb (fn [read-only]
                                                          (utils/handle-change growth-section true :loading)
                                                          (om/update-state! owner :read-only (fn [_]read-only)))})))))))