(ns open-company-web.components.ui.popover
  "
  Popover dialog with one or 2 buttons. Use `add-popover` and `hide-popover` functions rather than mounting
  this component directly.
  "
  (:require [rum.core :as rum]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))


(declare popover)

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

;; Options passed as a map to add-popover -
;;
;; :container-id unique DOM ID for the container, pass the same ID hide-popover
;; :height optional height for the dialog, e.g. "150px"
;; :width optional width for the dialog, e.g. "300px"
;; :title optional H3 title text for the dialog
;; :message textual content of the dialog
;; :cancel-title optional title of the secondary styled cancel button, pass as false if you only want 1 button
;; :cancel-cb function to call when the user clicks the cancel button, required if you provide a cancel title,
;;            be sure to call hide-popever with the container ID
;; :success-title required title of the primary styled success button
;; :success-cb function to call when the user clicks the cancel button, required if you provide a cancel title,
;;             be sure to call hide-popever with the container ID
;;
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

(rum/defc popover < rum/static [{:keys [container-id
                                        height
                                        width
                                        title
                                        message
                                        cancel-title
                                        cancel-cb
                                        success-title
                                        success-cb]}]

  ;; assertions of proper usage
  (assert (string? container-id) "popover container-id is not a string")
  (assert (if height (string? height) true) "popover height is not a string")
  (assert (if width (string? width) true) "popover width is not a string")
  (assert (if title (string? title) true) "popover title is not a string")
  (assert (string? message) "popover message is not a string")
  (assert (if cancel-title (string? cancel-title) true) "popover cancel-title is not a string")
  (assert (if cancel-cb (fn? cancel-cb) true) "popover cancel-cb is not a function")
  (assert (string? success-title) "popover success-title is not a string")
  (assert (fn? success-cb) "popover success-cb is not a function")

  (let [style {}
        w-style (if width (assoc style :width width) style)
        h-style (if height (assoc style :height height) style)]

    [:div.oc-popover {:style h-style}
      (when title
        [:div.title-container
          [:h3.title title]])
      [:div.message-container
        [:p.message message]]
      [:div.buttons-container
        (when cancel-title
          [:button.btn-reset.btn-outline.mr1.secondary-button {:on-click cancel-cb} 
            cancel-title])
        [:button.btn-reset.btn-solid.mr1.primary-button {:on-click success-cb} 
          success-title]]]))