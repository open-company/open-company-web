(ns oc.web.components.ui.alert-modal
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]))

(defn dismiss-modal []
  (dis/dispatch! [:alert-modal-hide-done]))

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
   :link-button-title The title for the first button, it's black link styled.
   :link-button-cb The function to execute when the first button is clicked.
   :solid-button-title The title for the second button, it's green solid styled.
   :solid-button-cb The function to execute when the second button is clicked."
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
                    (dis/dispatch! [:alert-modal-hide]))}
      [:div.modal-wrapper
        [:button.carrot-modal-close.mlb-reset
          {:on-click #(if (fn? (:link-button-cb alert-modal)) (link-button-clicked alert-modal %) (close-clicked s))}]
        [:div.alert-modal
          {:class (when has-buttons "has-buttons")}
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
              {:class (when (or (not (:link-button-title alert-modal))
                                (not (:solid-button-title alert-modal))) "single-button")}
              (when (:link-button-title alert-modal)
                [:button.mlb-reset.mlb-link-black
                  {:on-click #(link-button-clicked alert-modal %)}
                  (:link-button-title alert-modal)])
              (when (:solid-button-title alert-modal)
                [:button.mlb-reset.mlb-default
                  {:on-click #(solid-button-clicked alert-modal %)}
                  (:solid-button-title alert-modal)])])]]]))