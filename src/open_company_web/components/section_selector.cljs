(ns open-company-web.components.section-selector
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.router :as router]
            [open-company-web.components.finances.finances :refer (finances)]
            [open-company-web.components.growth.growth :refer (growth)]
            [open-company-web.components.finances.finances-edit :refer (finances-edit)]
            [open-company-web.components.simple-section :refer (simple-section)]
            [open-company-web.lib.utils :as utils]
            [goog.fx.dom :refer (Resize)]
            [goog.fx.dom :refer (Fade)]
            [goog.fx.Animation.EventType :as EventType]
            [goog.events :as events]))

(defn section-component [section-name]
  (cond
    (= :finances (keyword section-name))
    finances

    (= :growth (keyword section-name))
    growth

    :else
    simple-section))

(defn revisions-navigation-cb [owner section-name as-of]
  (let [showing-revision (om/get-state owner :as-of)]
    (when-not (= as-of showing-revision)
      (om/update-state! owner :animating (fn [_]true))))
  (om/update-state! owner :next-as-of (fn [_]as-of)))

(def anim-d 800)

(defn box-height [box-id]
  (if-let [box (sel1 (str "#" box-id))]
    (.-offsetHeight box)
    0))

(defn next-box-height [owner section-name]
  (let [first-box (om/get-state owner :first-box)
        box-label (if first-box "b" "a")
        box-id (str "sec-box-" box-label "-" section-name)]
    (box-height box-id)))

(defn actual-box-height [owner section-name]
  (let [first-box (om/get-state owner :first-box)
        box-label (if first-box "a" "b")
        box-id (str "sec-box-" box-label "-" section-name)]
    (box-height box-id)))

(defn animate-section-translation [owner section-name]
  (let [first-box (om/get-state owner :first-box)
        a-box (sel1 (str "#sec-box-a-" section-name))
        b-box (sel1 (str "#sec-box-b-" section-name))
        next-box-height (min 724 (next-box-height owner section-name))
        box (sel1 (str "#sec-box-" section-name))]
    (.play
      (new Resize
           box
           (new js/Array (.-offsetWidth box) (.-offsetHeight box))
           (new js/Array (.-offsetWidth box) next-box-height)
           anim-d))
    (.play
      (new Fade
           a-box
           (if first-box 1 0)
           (if first-box 0 1)
           anim-d))
    (let [fade-b-box (new Fade
                          b-box
                          (if first-box 0 1)
                          (if first-box 1 0)
                          anim-d)]
      (events/listen fade-b-box
                     EventType/FINISH
                     (fn []
                       (let [next-as-of (om/get-state owner :next-as-of)
                             first-box (om/get-state owner :first-box)]
                         (om/update-state! owner :as-of (fn [_]next-as-of))
                         (om/update-state! owner :first-box (fn [_](not first-box)))
                         (om/update-state! owner :animating (fn [_]false))
                         (om/update-state! owner :next-as-of (fn [_]nil)))))
      (.play fade-b-box))
    (utils/scroll-to-id (str "sec-box-" section-name) anim-d)))

(defn setup-box-height [section-name owner]
  (when-not (om/get-state owner :animating)
    (let [container-id (str "#sec-box-" section-name)
          height (actual-box-height owner section-name)]
      (.css (.$ js/window container-id) "height" (str height "px")))))

(defn calc-height-imgs-onload [section-name owner]
  (.bind
    (.$ js/window (str "#sec-box-" section-name))
    "DOMSubtreeModified"
    (fn []
      (setup-box-height section-name owner)
      (.bind
        (.$ js/window (str "#sec-box-" section-name " img"))
        "load"
        #(setup-box-height section-name owner)))))

(defcomponent section-selector [data owner]

  (init-state [_]
    (let [as-of (:updated-at (:section-data data))]
      {:next-as-of nil
       :as-of as-of
       :animating false
       :first-box true}))

  (did-update [_ prev-props _]
    (when-not (= (:updated-at (:section-data data)) (:updated-at (:section-data prev-props)))
      (om/update-state! owner :as-of (fn [_](:updated-at (:section-data data))))))

  (did-mount [_]
    (when (.-$ js/window) ; to not crash tests
      (let [section-name (name (:section data))]
        (setup-box-height section-name owner)
        ; listens for dom changes and recalculate the height when img tags load
        (calc-height-imgs-onload section-name owner))))

  (render [_]
    (let [showing-revision (om/get-state owner :as-of)
          next-revision (om/get-state owner :next-as-of)
          section (:section data)
          actual-data (:section-data data)
          section-data (utils/select-section-data actual-data section showing-revision)
          next-section-data (utils/select-section-data actual-data section next-revision)
          animating (and next-revision (not= showing-revision next-revision))
          first-box (om/get-state owner :first-box)
          a-section-data (if first-box section-data next-section-data)
          b-section-data (if first-box next-section-data section-data)
          a-read-only (not= (:updated-at a-section-data) (:updated-at actual-data))
          b-read-only (not= (:updated-at b-section-data) (:updated-at actual-data))]
      (dom/div #js {:className "section-selector" :ref "section-selector"}
        (dom/div {:class "section-animator-box" :id (str "sec-box-" (name section))}
          (when a-section-data
            (dom/div #js {:className "section-box"
                          :id (str "sec-box-a-" (name section))
                          :ref "sec-box-a"
                          :style #js {:opacity (if first-box 1 0)}}
              (om/build (section-component section) {:section-data a-section-data
                                                     :section section
                                                     :currency (:currency data)
                                                     :actual-as-of (:updated-at actual-data)
                                                     :revisions-navigation-cb #(revisions-navigation-cb owner section %)
                                                     :read-only a-read-only})))
          (when b-section-data
            (dom/div #js {:className "section-box"
                          :id (str "sec-box-b-" (name section))
                          :ref "sec-box-b"
                          :style #js {:opacity (if first-box 0 1)}}
              (om/build (section-component section) {:section-data b-section-data
                                                     :section section
                                                     :actual-as-of (:updated-at actual-data)
                                                     :revisions-navigation-cb #(revisions-navigation-cb owner section %)
                                                     :read-only b-read-only})))
          (when animating
            (.setTimeout js/window #(animate-section-translation owner (name section)) 1)
            nil))))))