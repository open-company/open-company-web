(ns oc.web.components.ui.popover
  "
  Popover dialog with one or 2 buttons. Use `add-popover` and `hide-popover` functions rather than mounting
  this component directly.
  "
  (:require [org.martinklepsch.derivatives :as drv]
            [rum.core :as rum]
            [om.core :as om :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.rum-utils :as ru]
            [oc.web.lib.utils :as utils]))


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
                                  (.remove $container)
                                  (swap! dis/app-state dissoc :rum-popover-data))
                                (catch :default e)) 1500)
      (catch :default e))))

(def default-z-index 1031)

;; Options passed as a map to add-popover -
;;
;; :container-id unique DOM ID for the container, pass the same ID hide-popover
;; :hide-on-click-out true if you want to remove the popover on backgorund click
;; :z-index-offset an offset to add to the default z-index to make sure multiple popover stack as expeceted
;;
;; The following are only for adding a popover via data, not via a component:
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
                       (ru/drv-root {:state dis/app-state
                                     :component #(om/component (component (merge data {:hide-popover-cb (fn []
                                                                                                          (when (:hide-popover-cb data)
                                                                                                            ((:hide-popover-cb data)))
                                                                                                          (hide-popover nil container-id))})))
                                     :drv-spec (dis/drv-spec dis/app-state router/path)
                                     :target (sel1 (str "#" container-id))})
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
                       (om/root component data {:target (sel1 (str "#" container-id))})
                       (.addClass body "no-scroll")
                       ; add the close action
                       (when (:hide-on-click-out data)
                         (.click popover-ct #(hide-popover % container-id)))
                       ; show the popover
                       (.setTimeout js/window #(.fadeIn popover-ct 300) 0))
                     1)))))

(defn add-popover [data]
  (swap! dis/app-state assoc :rum-popover-data data)
  (add-popover-with-rum-component popover data))

(defn assert-param [v f msg]
  (assert (if v (f v) true) msg))

(rum/defcs popover < rum/static
                     rum/reactive
                     (drv/drv :rum-popover-data)
  [state]
  (let [{:keys [container-id
                height
                width
                title
                message
                cancel-title
                cancel-cb
                success-title
                success-cb
                z-index-offset] :as rum-popover} (drv/react state :rum-popover-data)]
    ;; assertions of proper usage
    (assert-param height string?  "popover height is not a string")
    (assert-param width string? "popover width is not a string")
    (assert-param title string? "popover title is not a string")
    (assert-param message string? "popover message is not a string")
    (assert-param cancel-title string? "popover cancel-title is not a string")
    (assert-param cancel-cb fn? "popover cancel-cb is not a function")
    (assert-param success-title string? "popover success-title is not a string")
    (assert-param success-cb fn? "popover success-cb is not a function")
    (assert-param z-index-offset number? "z-index-offset must be a number")
    (let [style {}
          w-style (if width
                    (assoc style :width width)
                    style)
          h-style (if height
                    (assoc w-style :height height)
                    w-style)
          z-style (if z-index-offset
                    (assoc h-style :zIndex (+ default-z-index (* z-index-offset 3) 1))
                    (assoc h-style :zIndex (+ default-z-index 1)))]

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
            success-title]]])
  ))