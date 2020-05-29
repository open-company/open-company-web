(ns oc.web.components.ui.all-caught-up
  (:require [rum.core :as rum]))

(rum/defc all-caught-up
  [message]
  [:div.all-caught-up
    [:div.all-caught-up-inner
      [:div.message
        (or message "That’s all for now!")]]])


(rum/defc caught-up-line
  [{:keys [message gray-style]}]
  [:div.caught-up-line
    {:class (when gray-style "gray-style")}
    (all-caught-up message)])