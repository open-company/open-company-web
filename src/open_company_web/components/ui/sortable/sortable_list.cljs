(ns open-company-web.components.ui.sortable.sortable-b
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]))

(defn replace-item-in-vec [coll old-item new-item]
  (let [fix-coll (to-array coll)
        idx (.indexOf fix-coll old-item)]
    (when-not (neg? idx)
      (let [before (.slice fix-coll 0 idx)
            after (.slice fix-coll (inc idx) (count coll))]
        (concat before [new-item] after)))))

(defn insert-at-index [coll item idx]
  (let [fix-coll (to-array coll)
        before (.slice fix-coll 0 idx)
        after (.slice fix-coll (inc idx) (count coll))]
    (concat before [item] after)))

(defn drag-start [e owner]
  (println "drag-start" e)
  (om/set-state! owner :dragged (.-currentTarget e))
  (set! (.-effectAllowed (.-dataTransfer e)) "move")
  
  ; Firenfox fix
  (.setData (.-dataTransfer e) "text/html" (.-currentTarget e))
  
  ; this.dragged = e.currentTarget
  ; e.dataTransfer.effectAllowed = 'move';

  ;  // Firefox requires calling dataTransfer.setData
  ;  // for the drag to properly work
  ;  e.dataTransfer.setData("text/html", e.currentTarget)
)

(defn drag-end [e owner]
  (let [dragged (om/get-state owner :dragged)]
    (set! (.-display (.-style dragged)) "block")
    ; (.removeChild (.-parentNode dragged) (placeholder))
  
    (let [dragged-id (.-id (.-dataset dragged))
          items (om/get-state owner :items)
          plc-idx (.indexOf (to-array items) "placeholder")
          new-items (insert-at-index items dragged-id plc-idx)]
      (println "drag-end" new-items)
      (doto owner
        (om/set-state! :items new-items)
        (om/set-state! :dragged nil)))))

  ;// Update state
  ;var data = this.state.data;
  ; this.dragged.style.display = "block"
  ; this.dragged.parentNode.removeChild(placeholder)
  ; var from = Number(this.dragged.dataset.id);
  ; var to = Number(this.over.dataset.id);
  ; if(from < to) to--;
  ; data.splice(to, 0, data.splice(from, 1)[0]);
  ; this.setState({data: data});)
  
(defn drag-over [e owner]
  (.preventDefault e)
  (set! (.-display (.-style (om/get-state owner :dragged))) "none")
  (when-not (= (.-className (.-target e)) "placeholder")
    (let [over (.-id (.-dataset (.-target e)))]
      (when (= over "placeholder") (println "over placeholder"))
      (when-not (= over "placeholder")
        (let [items (om/get-state owner :items)
              plc-idx (.indexOf (to-array items) "placeholder")
              over-idx (.indexOf (to-array items) over)
              new-items (if (neg? plc-idx) items (insert-at-index items over plc-idx))
              new-items (insert-at-index new-items "placeholder" over-idx)]
          (println "drag-over" new-items)
          (om/set-state! owner :items new-items)
        
          ; (.insertBefore (.-parentNode (.-target e)) (placeholder) (.-target e))
          )))))
  ; e.preventDefault();
  ; this.dragged.style.display = "none";
  ; if(e.target.className == "placeholder") return;
  ; this.over = e.target;
  ; e.target.parentNode.insertBefore(placeholder, e.target);)

(defcomponent sortable-list [data owner]

  (init-state [_]
    {:items (take 5 (:items data))})

  (render [_]
    (dom/ul #js {:onDragOver #(drag-over % owner)
                 :onDrop #(drag-end % owner)}
      (for [item (om/get-state owner :items)]
        (dom/li #js {:data-id item
                     :key item
                     :draggable true
                     :onDragStart #(drag-start % owner)
                     :style #js {:display "block"}} item)))))

