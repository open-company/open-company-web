(ns oc.web.components.ui.made-with-carrot-modal
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]))

(defn show-modal []
  (dis/dispatch! [:input [:made-with-carrot-modal] true]))

(defn dismiss-modal []
  (dis/dispatch! [:input [:made-with-carrot-modal] false]))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 dismiss-modal))

(rum/defcs made-with-carrot-modal < rum/static
                                    ;; Locals
                                    (rum/local false ::dismiss)
                                    ;; Mixins
                                    mixins/no-scroll-mixin
                                    mixins/first-render-mixin
  [s]
  [:div.made-with-carrot-modal-container
    {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(:first-render-done s)))
                              :appear (and (not @(::dismiss s)) @(:first-render-done s))})}
    [:div.modal-wrapper
      [:button.settings-modal-close.mlb-reset
        {:on-click #(close-clicked s)}]
      [:div.made-with-carrot-modal
        [:div.carrot-logo]
        [:div.made-wih-carrot-title
          "Waaaaait a darn minute... What’s Carrot?"]
        [:div.made-wih-carrot-subtitle
          " Carrot highlights team news, ideas and stories that create "
          "real transparency and alignment for stakeholders."]
        [:button.mlb-reset.mlb-default.learn-more
          {:on-click #(router/redirect! "https://carrot.io")}
          "Learn More"]]]])
