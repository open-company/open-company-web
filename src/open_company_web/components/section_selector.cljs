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
  (om/update-state! owner :next-as-of (fn [_]as-of)))

(def anim-d 800)

(defn box-height [box-id]
  (let [new-box (.$ js/window box-id)
        new-box-height (.height new-box)]
    (.height new-box)))

(defn next-box-height [owner section-name]
  (let [first-box (om/get-state owner :first-box)
        box-label (if first-box "b" "a")
        box-id (str "#sec-box-" box-label"-" section-name)]
    (box-height box-id)))

(defn actual-box-height [owner section-name]
  (let [first-box (om/get-state owner :first-box)
        box-label (if first-box "a" "b")
        box-id (str "#sec-box-" box-label "-" section-name)]
    (box-height box-id)))

(defn animate-section-translation [owner section-name]
  (let [first-box (om/get-state owner :first-box)
        a-box (.$ js/window (str "#sec-box-a-" section-name))
        b-box (.$ js/window (str "#sec-box-b-" section-name))
        next-box-height (next-box-height owner section-name)
        box (.$ js/window (str "#sec-box-" section-name))]
    (.animate box #js {"height" (str next-box-height "px")} anim-d)
    (.fadeTo a-box anim-d (if first-box 0 1))
    (.fadeTo b-box anim-d (if first-box 1 0) (fn []
                                                (let [next-as-of (om/get-state owner :next-as-of)
                                                      first-box (om/get-state owner :first-box)]
                                                  (om/update-state! owner :as-of (fn [_]next-as-of))
                                                  (om/update-state! owner :first-box (fn [_](not first-box)))
                                                  (om/update-state! owner :animating (fn [_]false))
                                                  (om/update-state! owner :next-as-of (fn [_]nil)))))
    (utils/scroll-to-id (str "sec-box-" section-name) anim-d)))

(defn setup-box-height [section-name owner]
  (when (not (om/get-state owner :animating))
    (let [container-id (str "#sec-box-" section-name)
          height (actual-box-height owner section-name)]
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
       :next-as-of nil
       :as-of as-of
       :animating false
       :first-box true}))
  (did-mount [_]
    (let [section-name (name (:section data))]
      (setup-box-height section-name owner)
      ; listens for dom changes and recalculate the height when img tags load
      (calc-height-imgs-onload section-name owner)))
  (render [_]
    (let [showing-revision (om/get-state owner :as-of)
          next-revision (om/get-state owner :next-as-of)
          section (:section data)
          actual-data (:section-data data)
          section-data (utils/select-section-data actual-data section showing-revision)
          next-section-data (utils/select-section-data actual-data section next-revision)
          read-only (or (:loading section-data) (not (= showing-revision (:updated-at actual-data))))
          editing (om/get-state owner :editing)
          animating (and next-revision (not (= showing-revision next-revision)))
          first-box (om/get-state owner :first-box)
          a-section-data (if first-box section-data next-section-data)
          b-section-data (if first-box next-section-data section-data)]
      (dom/div #js {:className "section-selector" :ref "section-selector"}
        (dom/div {:class "section-animator-box" :id (str "sec-box-" (name section))}
          (when a-section-data
            (dom/div {:class "section-box"
                      :id (str "sec-box-a-" (name section))
                      :style {:opacity (if first-box 1 0)}}
              (om/build (section-component section editing) {:section-data a-section-data
                                                             :section section
                                                             :actual-as-of (:updated-at actual-data)
                                                             :loading (:loading data)
                                                             :revisions-navigation-cb #(revisions-navigation-cb owner section %)
                                                             :close-edit-cb #(editing! owner false)
                                                             :editable-click-callback #(editing! owner true)
                                                             :read-only (not (= (:updated-at a-section-data) (:updated-at actual-data)))})))
          (when b-section-data
            (dom/div {:class "section-box"
                      :id (str "sec-box-b-" (name section))
                      :style {:opacity (if first-box 0 1)}}
              (om/build (section-component section editing) {:section-data b-section-data
                                                             :section section
                                                             :actual-as-of (:updated-at actual-data)
                                                             :loading (:loading data)
                                                             :revisions-navigation-cb #(revisions-navigation-cb owner section %)
                                                             :close-edit-cb #(editing! owner false)
                                                             :editable-click-callback #(editing! owner true)
                                                             :read-only (not (= (:updated-at b-section-data) (:updated-at actual-data)))})))
          (when animating
            (.setTimeout js/window #(animate-section-translation owner (name section)) 1)
            nil))))))