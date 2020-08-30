(ns oc.web.components.ui.small-loading
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(rum/defc small-loading < rum/static
  []
  [:div.small-loading
    [:div.small-loading-inner]])
