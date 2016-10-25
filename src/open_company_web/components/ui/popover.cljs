(ns open-company-web.components.ui.popover
  "
  Popover dialog with one or 2 buttons. Use `add-popover` and `hide-popover` functions rather than mounting
  this component directly.
  "
  (:require [rum.core :as rum]
            [om.core :as om :include-macros true]
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
      (.removeClass (js/$ "body") "no-scroll")
      (.setTimeout js/window #(try
                                (let [container (sel1 (str "#" container-id))
                                      $container (js/$ container)]
                                  (rum/unmount container)
                                  (.remove $container))
                                (catch :default e)) 1500)
      (catch :default e))))

(def default-z-index 1031)

;; Options passed as a map to add-popover -
;;
;; :container-id unique DOM ID for the container, pass the same ID hide-popover
;; :height optional height for the dialog, e.g. "150px"
;; :width optional width for the dialog, e.g. "300px"
;; :title optional H3 title text for the dialog
;; :message textual content of the dialog
;; :cancel-title optional title of the secondary styled cancel button, pass as false if you only want 1 button
;; :cancel-cb function to call when the user clicks the cancel button, required if you provide a cancel title,
;;            be sure to call hide-popover with the container ID
;; :success-title required title of the primary styled success button
;; :success-cb function to call when the user clicks the cancel button, required if you provide a cancel title,
;;             be sure to call hide-popover with the container ID
;; :hide-on-click-out true if you want to remove the popover on backgorund click
;; :z-index-offset an offset to add to the default z-index to make sure multiple popover stack as expeceted
;;
(defn add-popover-with-rum-component [component data]
  (let [container-id (:container-id data)]
    (when (.-$ js/window) ; avoid tests crash
      (let [popover-ct (js/$ (str "<div class='oc-popover-container' id='" container-id "'></div>"))
            body (js/$ (.-body js/document))]
        ; add the div to the body
        (.append body popover-ct)
        (let [z-index (or (:z-index-offset data) 0)]
          (.css popover-ct #js {:zIndex (+ default-z-index (* z-index 3))}))
        ; if the component has not been mounted, render it
        (.setTimeout js/window
                     (fn []
                       ; render the popover component
                       (rum/mount (component data) (sel1 (str "#" container-id)))
                       (.addClass body "no-scroll")
                       ; add the close action
                       (when (:hide-on-click-out data)
                         (.click popover-ct #(hide-popover % container-id)))
                       ; show the popover
                       (.setTimeout js/window #(.fadeIn popover-ct 300) 0))
                     1)))))

(defn add-popover-with-om-component [component data]
  (let [container-id (:container-id data)]
    (when (.-$ js/window) ; avoid tests crash
      (let [popover-ct (js/$ (str "<div class='oc-popover-container' id='" container-id "'></div>"))
            body (js/$ (.-body js/document))]
        ; add the div to the body
        (.append body popover-ct)
        (let [z-index (or (:z-index-offset data) 0)]
          (.css popover-ct #js {:zIndex (+ default-z-index (* z-index 3))}))
        ; if the component has not been mounted, render it
        (.setTimeout js/window
                     (fn []
                       ; render the popover component
                       (om/root component (:data data) {:target (sel1 (str "#" container-id))})
                       (.addClass body "no-scroll")
                       ; add the close action
                       (when (:hide-on-click-out data)
                         (.click popover-ct #(hide-popover % container-id)))
                       ; show the popover
                       (.setTimeout js/window #(.fadeIn popover-ct 300) 0))
                     1)))))

(defn add-popover [data]
  (add-popover-with-rum-component popover data))

(rum/defc popover < rum/static [{:keys [container-id
                                        height
                                        width
                                        title
                                        message
                                        cancel-title
                                        cancel-cb
                                        success-title
                                        success-cb
                                        z-index-offset]}]

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
  (assert (number? z-index-offset) "z-index-offset must be a number")

  (let [style {}
        w-style (if width
                  (assoc style :width width)
                  style)
        h-style (if height
                  (assoc w-style :height height)
                  w-style)
        z-style (if z-index-offset
                  (assoc h-style :z-index (+ default-z-index (* z-index-offset 3) 1))
                  (assoc h-style :z-index (+ default-z-index 1)))]

    [:div.oc-popover {:style z-style}
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