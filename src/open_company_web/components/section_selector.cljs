(ns open-company-web.components.section-selector
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.components.finances.finances :refer (finances)]
            [open-company-web.components.growth.growth :refer (growth)]
            [open-company-web.components.finances.finances-edit :refer (finances-edit)]
            [open-company-web.components.simple-section :refer (simple-section)]
            [open-company-web.lib.utils :as utils]))

(defn editing! [owner v]
  (om/update-state! owner :editing (fn [_]v)))

(defn section-component [section-name editable]
  (cond
    (and (= :finances (keyword section-name)) editable)
    finances-edit

    (= :finances (keyword section-name))
    finances

    (= :growth (keyword section-name))
    growth

    :else
    simple-section))

(defn revisions-navigation-cb [owner section-name as-of]
  (let [showing-revision (om/get-state owner :as-of)]
    (when (not (= as-of showing-revision))
      (om/update-state! owner :animating (fn [_]true))))
  (om/update-state! owner :as-of (fn [_]as-of)))

(def anim-d 800)

(defn box-height [section-name]
  (let [new-box (.$ js/window (str "#sec-box-new-" section-name))
        new-box-height (.height new-box)]
    (.height new-box)))

(defn animate-section-translation [owner section-name]
  (let [old-box (.$ js/window (str "#sec-box-old-" section-name))
        new-box (.$ js/window (str "#sec-box-new-" section-name))
        new-box-height (box-height section-name)
        box (.$ js/window (str "#sec-box-" section-name))]
    (.animate box #js {"height" (str new-box-height "px")} anim-d)
    (.fadeTo old-box anim-d 0)
    (.fadeTo new-box anim-d 1 (fn []
                                (let [as-of (om/get-state owner :as-of)]
                                  (om/update-state! owner :old-as-of (fn [_]as-of))
                                  (om/update-state! owner :animating (fn [_]false)))))
    (utils/scroll-to-id (str "sec-box-" section-name) anim-d)))

(defn setup-box-height [section-name owner]
  (when (not (om/get-state owner :animating))
    (let [container-id (str "#sec-box-" section-name)
          box-id (str "#sec-box-new-" section-name)
          height (.height (.$ js/window box-id))]
      (.css (.$ js/window container-id) "height" (str height "px")))))

(defn calc-height-imgs-onload [section-name owner]
  (-> (.$ js/window (str "#sec-box-" section-name))
      (.bind "DOMSubtreeModified"
             (fn [] (setup-box-height section-name owner)
                    (-> (.$ js/window (str "#sec-box-" section-name " img"))
                        (.unbind "load")
                        (.bind "load" #(setup-box-height section-name owner)))))))

(defcomponent section-selector [data owner]
  (init-state [_]
    (let [as-of (:updated-at (:section-data data))]
      {:editing false
       :old-as-of as-of
       :as-of as-of
       :animating false}))
  (did-mount [_]
    (let [section-name (name (:section data))]
      (setup-box-height section-name owner)
      ; listens for dom changes and recalculate the height when img tags load
      (calc-height-imgs-onload section-name owner)))
  (render [_]
    (let [showing-revision (om/get-state owner :as-of)
          old-revision (om/get-state owner :old-as-of)
          section (:section data)
          actual-data (:section-data data)
          section-data (utils/select-section-data actual-data section showing-revision)
          old-section-data (utils/select-section-data actual-data section old-revision)
          read-only (or (:loading section-data) (not (= showing-revision (:updated-at actual-data))))
          editing (om/get-state owner :editing)
          animating (not (= showing-revision old-revision))]
      (dom/div #js {:className "section-selector" :ref "section-selector"}
        (dom/div {:class "section-animator-box" :id (str "sec-box-" (name section))}
          (when animating
            (dom/div {:class "section-box-old"
                      :id (str "sec-box-old-" (name section))
                      :style {:opacity 1}}
              (om/build (section-component section editing) {:section-data old-section-data
                                                             :section section
                                                             :actual-as-of (:updated-at actual-data)
                                                             :loading (:loading data)
                                                             :revisions-navigation-cb #(revisions-navigation-cb owner section %)
                                                             :close-edit-cb #(editing! owner false)
                                                             :editable-click-callback #(editing! owner true)
                                                             :read-only (not (= (:updated-at old-section-data) (:updated-at actual-data)))})))
          (dom/div {:class "section-box-new"
                    :id (str "sec-box-new-" (name section))
                    :style {:opacity (if animating 0 1)}}
            (om/build (section-component section editing) {:section-data section-data
                                                           :section section
                                                           :actual-as-of (:updated-at actual-data)
                                                           :loading (:loading data)
                                                           :revisions-navigation-cb #(revisions-navigation-cb owner section %)
                                                           :close-edit-cb #(editing! owner false)
                                                           :editable-click-callback #(editing! owner true)
                                                           :read-only (not (= (:updated-at section-data) (:updated-at actual-data)))}))
          (when animating
            (.setTimeout js/window #(animate-section-translation owner (name section)) 1)))))))