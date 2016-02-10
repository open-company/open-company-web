(ns open-company-web.components.ui.sortable.utils
  (:require [cljs.core.async :as async :refer (put!)]
            [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [goog.style :as gstyle])
  (:import [goog.ui IdGenerator]))

;; =============================================================================
;; Utilities

(def spacer ::spacer)

(defn guid []
  (.getNextUniqueId (.getInstance IdGenerator)))

(defn gsize->vec [size]
  [(.-width size) (.-height size)])

(defn to? [owner next-props next-state k]
  (or (and (not (om/get-render-state owner k))
           (k next-state))
      (and (not (k (om/get-props owner)))
           (k next-props))))

(defn from? [owner next-props next-state k]
  (or (and (om/get-render-state owner k)
           (not (k next-state)))
      (and (k (om/get-props owner))
           (not (k next-props)))))

(defn location [e]
  [(.-clientX e) (.-clientY e)])

(defn element-offset [el]
  (let [offset (gstyle/getPageOffset el)]
    [(.-x offset) (.-y offset)]))

;; =============================================================================
;; Generic Draggable

(defn dragging? [owner]
  (om/get-state owner :dragging))

(defn drag-start [e item owner]
  (when-not (dragging? owner)
    (let [el          (om/get-ref owner "draggable")
          state       (om/get-state owner)
          drag-start  (location e)
          el-offset   (element-offset el)
          drag-offset (vec (map - el-offset drag-start))]
      ;; if in a sortable need to wait for sortable to
      ;; initiate dragging
      (when-not (:delegate state)
        (om/set-state! owner :dragging true))
      (doto owner
        (om/set-state! :location
          ((or (:constrain state) identity) el-offset))
        (om/set-state! :drag-offset drag-offset))
      (when-let [c (:chan state)]
        (put! c
          {:event :drag-start
           :id (:id item)
           :location (vec (map + drag-start drag-offset))})))))

(defn drag-stop [e item owner]
  (let [state (om/get-state owner)]
    (when (:dragging state)
      (om/set-state! owner :dragging false))
    ;; rendering order issues otherwise
    (when-not (:delegate state)
      (doto owner
        (om/set-state! :location nil)
        (om/set-state! :drag-offset nil)))
    (when-let [c (:chan state)]
      (put! c {:event :drag-stop :id (:id item)}))))

(defn drag [e item owner]
  (let [state (om/get-state owner)]
    (when (dragging? owner)
      (let [loc ((or (:constrain state) identity)
                  (vec (map + (location e) (:drag-offset state))))]
        (om/set-state! owner :location loc)
        (when-let [c (:chan state)]
          (put! c {:event :drag :location loc :id (:id item)}))))))

;; =============================================================================
;; Generic Sortable

(defn from-loc [v1 v2]
  (vec (map - v2 v1)))

(defn sortable-spacer [height]
  (dom/li
    #js {:key "spacer-cell"
         :style #js {:height height}}))

(defn index-of [x v]
  (loop [i 0 v (seq v)]
    (if v
      (if (= x (first v))
        i
        (recur (inc i) (next v)))
      -1)))

(defn insert-at [x idx ignore v]
  (let [len (count v)]
    (loop [i 0 v v ret []]
      (if (>= i len)
        (conj ret x)
        (let [y (first v)]
          (if (= y ignore)
            (recur i (next v) (conj ret y))
            (if (== i idx)
              (into (conj ret x) v)
              (recur (inc i) (next v) (conj ret y)))))))))

(defn sorting? [owner]
  (om/get-state owner :sorting))

(defn start-sort [owner e]
  (let [state (om/get-state owner)
        sort  (:sort state)
        idx   (index-of (:id e) sort)]
    (doto owner
      (om/set-state! :sorting (:id e))
      (om/set-state! :real-sort sort)
      (om/set-state! :drop-index idx)
      (om/set-state! :sort (insert-at spacer idx (:id e) sort)))))

(defn handle-drop [owner e]
  (when (sorting? owner)
    (let [{:keys [sort drop-index]} (om/get-state owner)
           idx (index-of spacer sort)
           sort (->> sort
                  (remove #{(:id e)})
                  (replace {spacer (:id e)})
                  vec)]
      (doto owner
        (om/set-state! :sorting nil)
        (om/set-state! :drop-index nil)
        (om/set-state! :real-sort nil)
        (om/set-state! :sort sort)))))

(defn update-drop [owner e]
  (when (sorting? owner)
    (let [loc    (:location e)
          state  (om/get-state owner)
          [_ y]  (from-loc (:location state) loc)
          [_ ch] (:cell-dimensions state)
          drop-index (js/Math.round (/ y ch))]
      (when (not= (:drop-index state) drop-index)
        (doto owner
          (om/set-state! :drop-index drop-index)
          (om/set-state! :sort
            (insert-at spacer drop-index (:id e) (:real-sort state))))))))

(defn bound [n lb ub]
  (cond
    (< n lb) lb
    (> n ub) ub
    :else n))

(defn handle-drag-event [owner e]
  (case (:event e)
    :drag-start (start-sort owner e) 
    :drag-stop  (handle-drop owner e)
    :drag       (update-drop owner e)
    nil))