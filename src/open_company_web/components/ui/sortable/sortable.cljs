(ns open-company-web.components.ui.sortable.sortable
  (:require-macros [cljs.core.async.macros :refer (go alt!)])
  (:require [cljs.core.async :as async :refer (put! chan dropping-buffer)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [goog.events :as events]
            [goog.style :as gstyle]
            [open-company-web.components.ui.sortable.utils :as su])
  (:import [goog.events EventType]))

(defcomponent draggable [data owner options]

  (did-mount [_]
    ;; capture the cell dimensions when it becomes available
    (let [dims (-> (om/get-ref owner "draggable")
                   gstyle/getSize su/gsize->vec)]
      (om/set-state! owner :dimensions dims)
      ;; let cell dimension listeners know
      (when-let [dims-chan (:dims-chan (om/get-state owner))]
        (put! dims-chan dims))))

  (will-update [_ next-props next-state]
    ;; begin dragging, need to track events on window
    (when (or (su/to? owner next-props next-state :dragging))
      (let [mouse-up   #(su/drag-stop % next-props owner)
            mouse-move #(su/drag % next-props owner)]
        (om/set-state! owner :window-listeners
          [mouse-up mouse-move])
        (doto js/window
          (events/listen EventType.MOUSEUP mouse-up)
          (events/listen EventType.MOUSEMOVE mouse-move))))
    ;; end dragging, cleanup window event listeners
    (when (su/from? owner next-props next-state :dragging)
      (let [[mouse-up mouse-move]
            (om/get-state owner :window-listeners)]
        (doto js/window
          (events/unlisten EventType.MOUSEUP mouse-up)
          (events/unlisten EventType.MOUSEMOVE mouse-move)))))

  (render-state [_ state]
    (let [style (cond
                  (su/dragging? owner)
                  (let [[x y] (:location state)
                        [w h] (:dimensions state)]
                    #js {:position "absolute"
                         :top y :left x :zIndex 1
                         :width w :height h})
                  :else
                  #js {:position "static" :zIndex 0})]
      (dom/li
        #js {:className (when (su/dragging? owner) "dragging")
             :style style
             :ref "draggable"
             :onMouseDown #(su/drag-start % data owner)
             :onMouseUp   #(su/drag-stop % data owner)
             :onMouseMove #(su/drag % data owner)}
        (om/build (:item data) (dissoc data :item) {:opts options})))))

(defcomponent sortable [{:keys [items sort container-classes] :as data} owner options]

  (init-state [_]
    {:sort (om/value sort)})

  (will-mount [_]
    (let [drag-chan (chan)
          dims-chan (chan (dropping-buffer 1))]
      (om/set-state! owner :chans
        {:drag-chan drag-chan
         :dims-chan dims-chan})
      (go
        (while true
          (alt!
            drag-chan ([e c] (su/handle-drag-event owner e))
            dims-chan ([e c] (om/set-state! owner :cell-dimensions e)))))))

  ;; doesn't account for change in number of items in sortable
  ;; nor changes in sort - exercise for the reader 
  (will-update [_ next-props next-state]
    ;; calculate constraints from cell-dimensions when we receive them
    (when (su/to? owner next-props next-state :cell-dimensions)
      (let [node   (om/get-ref owner "sortable") 
            [w h]  (su/gsize->vec (gstyle/getSize node))
            [x y]  (su/element-offset node)
            [_ ch] (:cell-dimensions next-state)]
        (om/set-state! owner :constrain
          (fn [[_ cy]] [(inc x) (su/bound cy y (- (+ y h) ch))])))))

  (did-mount [_]
    (when-not (om/get-state owner :location)
      (om/set-state! owner :location
        (su/element-offset (om/get-ref owner "sortable")))))

  (render-state [_ state]
    (println "sortable render-state")
    (apply dom/ul #js {:className (str container-classes " sortable")
                       :ref "sortable"}
      (map
        (fn [id]
          (println "   map:" (keyword id))
          (if-not (= id ::spacer)
            (om/build draggable {:section-data ((keyword id) (:sections data))
                                 :active-sections (:active-sections data)
                                 :item (:item data)
                                 :id id}
              (let [{:keys [constrain chans]} state]
                {:key :id
                 :init-state {:chan      (:drag-chan chans)
                              :dims-chan (:dims-chan chans)
                              :delegate  true}
                 :opts options
                 :state {:constrain constrain
                         :dragging  (= id (:sorting state))}}))
            (su/sortable-spacer (second (:cell-dimensions state)))))
        (:sort state)))))

;; =============================================================================
;; Example

; (defn item [the-item owner]
;   (om/component (dom/span nil (str "Item " (:title the-item)))))

; (def app-state
;   (let [items (->> (take 10 (map vector (repeatedly su/guid) (range)))
;                 (map (fn [[id n]] [id {:id id :title n :item item}]))
;                 (into {}))]
;     (atom {:items items
;            :sort (into [] (keys items))})))

; (defn sortable-view [app owner]
;   (om/component
;     (dom/div nil
;       (dom/h2 nil "Sortable example")
;       (om/build sortable app))))