(ns open-company-web.components.popover
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]))

(defcomponent popover [data owner]
  (render [_]
    (dom/div {:class "oc-popover"
              :width (:width data)
              :height (:height data)}
      (dom/div {:class "title-container"}
        (dom/h3 {:class "title"} (:title data)))
      (dom/div {:class "message-container"}
        (dom/p {:class "message"} (:message data)))
      (dom/div {:class "buttons-container"}
        (dom/button {:class "oc-btn oc-cancel"
                     :on-click (:cancel-cb data)} (:cancel-title data))
        (dom/button {:class (str "oc-btn oc-success " (:success-color-class data))
                     :on-click (:success-cb data)} (:success-title data))))))

(defn hide-popover [e container-id]
  (when (.-$ js/window)
    (try
      (.css (.$ js/window "body") #js {"overflow" ""})
      (let [popover-ct (.$ js/window (str "#" container-id))]
        (.fadeOut popover-ct 400 #(.css popover-ct #js {"display" "none"})))
      (.setTimeout js/window #(try
                                (let [container (.getElementById js/document container-id)
                                      $container (.$ js/window container)] 
                                  (om/detach-root container)
                                  (.remove $container))
                                (catch :default e)) 1500)
      (catch :default e))))

(defn add-popover [data]
  (let [container-id (:container-id data)]
    (when (.-$ js/window) ; avoid tests crash
      (let [popover-ct (.$ js/window (str "<div class='oc-popover-container' id='" container-id "'></div>"))
            body (.$ js/window (.-body js/document))
            slug (keyword (:slug @router/path))]
        ; add the div to the body
        (.append body popover-ct)
        ; if the component has not been mount, render it
        (.setTimeout js/window
                     (fn []
                       ; render the popover component
                       (om/root popover data {:target (.getElementById js/document container-id)})
                       ; add the close action
                       (.click popover-ct #(hide-popover % container-id))
                       ; show the popover
                       (.setTimeout js/window #(.fadeIn popover-ct 400) 0))
                     1)))))


