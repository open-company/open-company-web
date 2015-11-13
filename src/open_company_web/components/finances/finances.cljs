(ns open-company-web.components.finances.finances
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
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
            [open-company-web.components.editable-title :refer [editable-title]]
            [open-company-web.components.utility-components :refer [editable-pen]]
            [cljs.core.async :refer [chan <!]]))

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
    (let [finances-data (:section-data data)
          notes-data (:notes finances-data)]
      {:focus "cash"
       :as-of (:updated-at finances-data)}))
  (will-mount [_]
    (let [save-change (utils/get-channel "save-section-finances")]
        (go (loop []
          (let [change (<! save-change)
                cursor @dispatcher/app-state
                slug (:slug @router/path)
                company-data ((keyword slug) cursor)
                section (:section data)
                section-data (section company-data)]
            (api/save-or-create-section section-data)
            (recur)))))
    (let [save-notes-change (utils/get-channel "save-finances-notes")]
        (go (loop []
          (let [change (<! save-notes-change)
                cursor @dispatcher/app-state
                slug (:slug @router/path)
                company-data ((keyword slug) cursor)
                section (:section data)
                section-data (section company-data)]
            (api/patch-section-notes (:notes section-data) (:links section-data) section)
            (recur))))))
  (render [_]
    (let [showing-revision (om/get-state owner :as-of)
          focus (om/get-state owner :focus)
          classes "composed-section-link"
          finances-data (:section-data data)
          notes-data (:notes finances-data)
          cash-classes (str classes (when (= focus "cash") " active"))
          cash-flow-classes (str classes (when (= focus "cash-flow") " active"))
          revenue-classes (str classes (when (= focus "revenue") " active"))
          costs-classes (str classes (when (= focus "costs") " active"))
          runway-classes (str classes (when (= focus "runway") " active"))
          read-only (:read-only data)
          cursor @dispatcher/app-state
          slug (:slug @router/path)
          company-data ((keyword slug) cursor)
          subsection-data {:section-data finances-data
                           :read-only read-only
                           :currency (:currency company-data)
                           :editable-click-callback (:editable-click-callback data)}
          finances-row-data (:data finances-data)
          sum-revenues (apply + (map #(:revenue %) finances-row-data))
          first-title (if (pos? sum-revenues) "Cash flow" "Burn rate")
          needs-runway (some #(contains? % :runway) finances-row-data)]
      (dom/div {:class "section-container" :id "section-finances"}
        (dom/div {:class "composed-section finances"}
          (om/build editable-title {:read-only read-only
                                    :section-data finances-data
                                    :section :finances
                                    :save-channel "save-section-finances"})
          (dom/div {:class (utils/class-set {:link-bar true
                                             :editable (not read-only)})}
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
            (when needs-runway
              (dom/a {:href "#"
                      :class runway-classes
                      :title "Runway"
                      :data-tab "runway"
                      :on-click #(subsection-click % owner)} "Runway"))
            (om/build editable-pen {:click-callback (:editable-click-callback data)}))
          (dom/div {:class (utils/class-set {:composed-section-body true})}
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
            (when (or (not (empty? (:body notes-data))) (not read-only))
              (om/build rich-editor {:read-only read-only
                                     :section-data notes-data
                                     :section :finances
                                     :save-channel "save-finances-notes"
                                     :notes true}))
            (om/build revisions-navigator {:section-data finances-data
                                           :section :finances
                                           :actual-as-of (:actual-as-of data)
                                           :navigate-cb (:revisions-navigation-cb data)})))))))