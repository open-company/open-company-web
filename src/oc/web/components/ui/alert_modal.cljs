(ns oc.web.components.ui.alert-modal
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]))

(defn show-alert [modal-data]
  (dis/dispatch! [:input [:alert-modal] modal-data]))

(defn hide-alert []
  (dis/dispatch! [:input [:alert-modal :dismiss] true]))

(defn dismiss-modal []
  (dis/dispatch! [:input [:alert-modal] nil]))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 dismiss-modal))

(defn link-button-clicked [alert-modal e]
  (utils/event-stop e)
  (when (fn? (:link-button-cb alert-modal))
    ((:link-button-cb alert-modal))))

(defn solid-button-clicked [alert-modal e]
  (utils/event-stop e)
  (when (fn? (:solid-button-cb alert-modal))
    ((:solid-button-cb alert-modal))))

(defn bottom-button-clicked [alert-modal e]
  (utils/event-stop e)
  (when (fn? (:bottom-button-cb alert-modal))
    ((:bottom-button-cb alert-modal))))

(rum/defcs alert-modal < rum/reactive
                         ;; Derivatives
                         (drv/drv :alert-modal)
                         ;; Locals
                         (rum/local false ::dismiss)
                         ;; Mixins
                         mixins/no-scroll-mixin
                         mixins/first-render-mixin

                         {:after-render (fn [s]
                                          (let [alert-modal @(drv/get-ref s :alert-modal)]
                                            (when (and (not @(::dismiss s))
                                                       (:dismiss alert-modal))
                                              (close-clicked s)))
                                          s)}
  "Customizable alert modal. It gets the following property from the :alert-modal derivative:
   :icon The src to use for an image, it's encapsulated in utils/cdn.
   :title The title of the view.
   :message A description message to show in the view.
   :link-button-style The color of the font for the link button
   :link-button-title The title for the first button, it's black link styled.
   :link-button-cb The function to execute when the first button is clicked.
   :solid-button-style The style of the button: default green, :red.
   :solid-button-title The title for the second button, it's green solid styled.
   :solid-button-cb The function to execute when the second button is clicked.
   :bottom-button-style The style for the button at the bottom of the view.
   :bottom-button-title The title for the bottom button, it's green solid styled.
   :bottom-button-cb The function to execute when the bottom button is clicked."
  [s]
  (let [alert-modal (drv/react s :alert-modal)
        action (if (empty? (:action alert-modal)) "no-action" (:action alert-modal))
        has-buttons (or (:link-button-title alert-modal)
                        (:solid-button-title alert-modal))]
    [:div.alert-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(:first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(:first-render-done s))
                                action true})
       :on-click #(when-not has-buttons
                    (hide-alert))}
      [:div.modal-wrapper
        [:button.settings-modal-close.mlb-reset
          {:on-click #(if (fn? (:link-button-cb alert-modal)) (link-button-clicked alert-modal %) (close-clicked s))}]
        [:div.alert-modal
          {:class (utils/class-set {:has-buttons has-buttons
                                    :has-bottom-button (seq (:bottom-button-title alert-modal))})}
          (when (:icon alert-modal)
            [:img.alert-modal-icon {:src (utils/cdn (:icon alert-modal))}])
          (when (:title alert-modal)
            [:div.alert-modal-title
              (:title alert-modal)])
          (when (:message alert-modal)
            [:div.alert-modal-message
              (:message alert-modal)])
          (when has-buttons
            [:div.alert-modal-buttons.group
              {:class (utils/class-set {:single-button (or (not (:link-button-title alert-modal))
                                                           (not (:solid-button-title alert-modal)))})}
              (when (:link-button-title alert-modal)
                [:button.mlb-reset.mlb-link-black
                  {:on-click #(link-button-clicked alert-modal %)
                   :class (when (:link-button-style alert-modal) (name (:link-button-style alert-modal)))}
                  (:link-button-title alert-modal)])
              (when (:solid-button-title alert-modal)
                [:button.mlb-reset.mlb-default
                  {:on-click #(solid-button-clicked alert-modal %)
                   :class (when (:solid-button-style alert-modal) (name (:solid-button-style alert-modal)))}
                  (:solid-button-title alert-modal)])])
          (when (seq (:bottom-button-title alert-modal))
            [:button.mlb-reset.bottom-button
              {:on-click #(bottom-button-clicked alert-modal %)
               :class (when (:bottom-button-style alert-modal) (name (:bottom-button-style alert-modal)))}
              (:bottom-button-title alert-modal)])]]]))