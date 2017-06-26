(ns oc.web.components.ui.carrot-box-thanks
  (:require [rum.core :as rum]
            [oc.web.local-settings :as ls]))

(defn get-link []
  (int (rand 100000)))

(rum/defc carrot-box-thanks
  [& [email]]
  (let [thanks-message (if-not (empty? email)
                          [:div.thanks-subheadline "We've sent you a link to " [:span email] "."]
                          [:div.thanks-subheadline "we've sent an email with a link."])
        carrot-link (str "http" (when ls/jwt-cookie-secure "s") "://" ls/web-server "/" (get-link))]
    [:div.carrot-box-container.group
      [:img.carrot-box {:src "/img/ML/carrot_box.svg"}]
      [:div.carrot-box-thanks
        [:div.thanks-headline "Thanks!"]
        thanks-message
        [:div.carrot-early-access-top "Get earlier access when your friends sign up with this link:"]
        [:a.carrot-early-access-link {:href carrot-link} carrot-link]]]))