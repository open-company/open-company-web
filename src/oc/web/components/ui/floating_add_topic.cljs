(ns oc.web.components.ui.floating-add-topic
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]))

(rum/defc floating-add-topic < rum/static
  []
  [:div.floating-add-topic
    [:button.btn-reset.floating-add-topic-btn
      {:on-click #(dis/dispatch! [:add-topic-show true])}]])