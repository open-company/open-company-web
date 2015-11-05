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
  (om/update-state! owner :as-of (fn [_]as-of)))

(def anim-d 1000)

(defn animate-section-translation [owner section-name]
  (let [old-box (.$ js/window (str "#sec-box-old-" section-name))
        new-box (.$ js/window (str "#sec-box-new-" section-name))]
    (.fadeTo old-box anim-d 0)
    (.fadeTo new-box anim-d 1
             #(om/update-state! owner :old-as-of (fn [_](om/get-state owner :as-of))))
    (utils/scroll-to-id (str "sec-box-" section-name) anim-d)))

(defn prepare-animation [owner section-name]
  (let [old-box (.$ js/window (str "#sec-box-old-" section-name))
        new-box (.$ js/window (str "#sec-box-new-" section-name))
        new-box-height (.height new-box)]
    (.css old-box "opacity" 1)
    (.css new-box "opacity" 0)
    (.setTimeout js/window #(animate-section-translation owner section-name) 1)))

(defn setup-box-height [section-name]
  (let [container-id (str "#sec-box-" section-name)
        box-id (str "#sec-box-new-" section-name)
        height (.height (.$ js/window box-id))]
    (.css (.$ js/window container-id) "height" (str height "px"))))

(defn calc-height-imgs-onload [section-name]
  (-> (.$ js/window (str "#sec-box-" section-name))
      (.bind "DOMSubtreeModified"
             (fn [] (setup-box-height section-name)
                    (-> (.$ js/window (str "#sec-box-" section-name " img"))
                        (.unbind "load")
                        (.bind "load" #(setup-box-height section-name)))))))

(defcomponent section-selector [data owner]
  (init-state [_]
    (let [as-of (:updated-at (:section-data data))]
      {:editing false
       :old-as-of as-of
       :as-of as-of}))
  (did-mount [_]
    (let [section-name (name (:section data))]
      (setup-box-height section-name)
      ; listens for dom changes and recalculate the height when img tags load
      (calc-height-imgs-onload section-name)))
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
      ; (when (= :update section)
      ;   (console/log "section-selector render" showing-revision (= showing-revision old-revision)))
      (dom/div #js {:className "section-selector" :ref "section-selector"}
        (dom/div {:class "section-animator-box" :id (str "sec-box-" (name section))}
          (when animating
            ; (.setTimeout js/window #(prepare-animation (name section)) 1)
            (prepare-animation owner (name section))
            (dom/div {:class "section-box-old" :id (str "sec-box-old-" (name section))}
              (om/build (section-component section editing) {:section-data old-section-data
                                                             :section section
                                                             :actual-as-of (:updated-at actual-data)
                                                             :loading (:loading data)
                                                             :revisions-navigation-cb #(revisions-navigation-cb owner section %)
                                                             :close-edit-cb #(editing! owner false)
                                                             :editable-click-callback #(editing! owner true)
                                                             :read-only read-only})))
          (dom/div {:class "section-box-new" :id (str "sec-box-new-" (name section))}
            (om/build (section-component section editing) {:section-data section-data
                                                           :section section
                                                           :actual-as-of (:updated-at actual-data)
                                                           :loading (:loading data)
                                                           :revisions-navigation-cb #(revisions-navigation-cb owner section %)
                                                           :close-edit-cb #(editing! owner false)
                                                           :editable-click-callback #(editing! owner true)
                                                           :read-only read-only})))))))