(ns open-company-web.components.finances.finances
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.components.finances.cash :refer (cash)]
            [open-company-web.components.finances.cash-flow :refer (cash-flow)]
            [open-company-web.components.finances.revenue :refer (revenue)]
            [open-company-web.components.finances.costs :refer (costs)]
            [open-company-web.components.finances.runway :refer (runway)]
            [open-company-web.components.update-footer :refer (update-footer)]
            [open-company-web.components.rich-editor :refer (rich-editor)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.revisions-navigator :refer [revisions-navigator]]
            [open-company-web.api :as api]
            [open-company-web.dispatcher :refer [app-state]]
            [open-company-web.components.editable-title :refer [editable-title]]
            [cljs.core.async :refer [put! chan <!]]))

(defn subsection-click [e owner]
  (.preventDefault e)
  (let [tab  (.. e -target -dataset -tab)]
    (om/update-state! owner :focus (fn [] tab))))

(defcomponent finances [data owner]
  (init-state [_]
    (let [save-channel (chan)
          save-notes-channel (chan)]
      (utils/add-channel "save-section-finances" save-channel)
      (utils/add-channel "save-finances-notes" save-notes-channel))
    (let [company-data (:company-data data)
          finances-data (:finances company-data)
          notes-data (:notes finances-data)]
      {:focus "cash"
       :read-only false}))
  (will-mount [_]
    (let [save-change (utils/get-channel "save-section-finances")]
        (go (loop []
          (let [change (<! save-change)
                cursor @app-state
                slug (:slug @router/path)
                company-data ((keyword slug) cursor)
                section-data (:finances company-data)]
            (api/save-or-create-section section-data)
            (recur)))))
    (let [save-notes-change (utils/get-channel "save-finances-notes")]
        (go (loop []
          (let [change (<! save-notes-change)
                cursor @app-state
                slug (:slug @router/path)
                company-data ((keyword slug) cursor)
                section-data (:finances company-data)]
            (api/update-finances-notes (:notes section-data) (:links section-data))
            (recur))))))
  (render [_]
    (let [focus (om/get-state owner :focus)
          classes "finances-link"
          slug (:slug @router/path)
          company-data (:company-data data)
          finances-data (:finances company-data)
          notes-data (:notes finances-data)
          cash-classes (str classes (when (= focus "cash") " active"))
          cash-flow-classes (str classes (when (= focus "cash-flow") " active"))
          revenue-classes (str classes (when (= focus "revenue") " active"))
          costs-classes (str classes (when (= focus "costs") " active"))
          runway-classes (str classes (when (= focus "runway") " active"))
          read-only (or (:loading finances-data) (om/get-state owner :read-only))
          subsection-data {:company-data company-data
                           :read-only read-only
                           :editable-click-callback (:editable-click-callback data)}
          finances-row-data (:data finances-data)
          sum-revenues (apply + (map #(:revenue %) finances-row-data))
          first-title (if (pos? sum-revenues) "Cash flow" "Burn rate")
          need-runway (some #(contains? % :runway) finances-row-data)]
      (dom/div {:class "row" :id "section-finances"}
        (dom/div {:class "finances"}
          (om/build editable-title {:read-only read-only
                                    :section-data finances-data
                                    :section :finances
                                    :save-channel "save-section-finances"})
          (dom/div {:class "link-bar"}
            (dom/a {:href "#"
                    :class cash-classes
                    :title "Cash"
                    :data-tab "cash"
                    :on-click #(subsection-click % owner)} "Cash")
            (dom/a {:href "#"
                    :class cash-flow-classes
                    :title first-title
                    :data-tab "cash-flow"
                    :on-click #(subsection-click % owner)} first-title)
            (when need-runway
              (dom/a {:href "#"
                      :class runway-classes
                      :title "Runway"
                      :data-tab "runway"
                      :on-click #(subsection-click % owner)} "Runway")))
          (dom/div {:class (utils/class-set {:finances-body true
                                             :editable (not read-only)})}
            (case focus

              "cash"
              (om/build cash subsection-data)

              "cash-flow"
              (if (pos? sum-revenues)
                (om/build cash-flow subsection-data)
                (om/build costs subsection-data))

              "runway"
              (om/build runway subsection-data))
            (om/build update-footer {:updated-at (:updated-at finances-data)
                                     :author (:author finances-data)
                                     :section :finances})
            (om/build rich-editor {:read-only read-only
                                   :section-data notes-data
                                   :section :finances
                                   :save-channel "save-finances-notes"})
            (om/build revisions-navigator {:revisions (:revisions finances-data)
                                           :section :finances
                                           :updated-at (:updated-at finances-data)
                                           :loading (:loading finances-data)
                                           :navigate-cb (fn [read-only]
                                                          (utils/handle-change finances-data true :loading)
                                                          (om/update-state! owner :read-only (fn [_]read-only)))})))))))