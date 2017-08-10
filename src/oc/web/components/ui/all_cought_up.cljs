(ns oc.web.components.ui.all-cought-up
  (:require [rum.core :as rum]))

(rum/defc all-cought-up
  []
  [:div.all-cought-up
    [:div.all-cought-up-inner
      [:div.message
        "You’re all caught up!"]]])