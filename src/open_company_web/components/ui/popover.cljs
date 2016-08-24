(ns open-company-web.components.ui.popover
  (:require [rum.core :as rum]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))

(rum/defc popover [data]

  (let [title (:title data)
        width (:width data)
        height (:height data)
        style {}
        w-style (if width (assoc style :width width) style)
        h-style (if height (assoc style :height height) style)]
    [:div.oc-popover {:style h-style}
      (when title
        [:div.title-container
          [:h3.title title]])
      [:div.message-container
        [:p.message (:message data)]]
      [:div.buttons-container
        [:button.btn-reset.btn-outline.mr1.secondary-button {:on-click (:cancel-cb data)} 
          (:cancel-title data)]
        [:button.btn-reset.btn-solid.mr1.primary-button {:on-click (:success-cb data)} 
          (:success-title data)]]]))

(defn hide-popover [e container-id]
  (when (.-$ js/window)
    (try
      (.css (js/$ "body") #js {"overflow" ""})
      (let [popover-ct (js/$ (str "#" container-id))]
        (.fadeOut popover-ct 400 #(.css popover-ct #js {"display" "none"})))
      (.setTimeout js/window #(try
                                (let [container (sel1 (str "#" container-id))
                                      $container (js/$ container)] 
                                  (rum/unmount container)
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
        ; if the component has not been mounted, render it
        (.setTimeout js/window
                     (fn []
                       ; render the popover component
                       (rum/mount (popover data) (sel1 (str "#" container-id)))
                       ; add the close action
                       (.click popover-ct #(hide-popover % container-id))
                       ; show the popover
                       (.setTimeout js/window #(.fadeIn popover-ct 300) 0))
                     1)))))