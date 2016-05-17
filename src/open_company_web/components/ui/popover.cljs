(ns open-company-web.components.ui.popover
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))

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
        (dom/button {:class (str "oc-btn oc-success " (:success-color-class data))
                     :on-click (:success-cb data)} (:success-title data))
        (dom/button {:class "oc-btn oc-cancel"
                     :on-click (:cancel-cb data)} (:cancel-title data))))))

(defn hide-popover [e container-id]
  (when (.-$ js/window)
    (try
      (.css (js/$ "body") #js {"overflow" ""})
      (let [popover-ct (js/$ (str "#" container-id))]
        (.fadeOut popover-ct 400 #(.css popover-ct #js {"display" "none"})))
      (.setTimeout js/window #(try
                                (let [container (sel1 (str "#" container-id))
                                      $container (js/$ container)] 
                                  (om/detach-root container)
                                  (.remove $container))
                                (catch :default e)) 1500)
      (catch :default e))))

(defn add-popover [data]
  (let [container-id (:container-id data)]
    (when (.-$ js/window) ; avoid tests crash
      (let [popover-ct (js/$ (str "<div class='oc-popover-container' id='" container-id "'></div>"))
            body (js/$ (.-body js/document))]
        ; add the div to the body
        (.append body popover-ct)
        ; if the component has not been mount, render it
        (.setTimeout js/window
                     (fn []
                       ; render the popover component
                       (om/root popover data {:target (sel1 (str "#" container-id))})
                       ; add the close action
                       (.click popover-ct #(hide-popover % container-id))
                       ; show the popover
                       (.setTimeout js/window #(.fadeIn popover-ct 400) 0))
                     1)))))