(ns oc.web.components.ui.carrot-box-thanks
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]))

(defn get-link []
  (int (rand 100000)))

(rum/defc carrot-box-thanks
  [& [email]]
  (let [thanks-message (if (seq email)
                          [:div.thanks-subheadline "We’ve sent an email to confirm to " [:span email] "."]
                          [:div.thanks-subheadline "We’ve sent you an email to confirm."])
        carrot-link (str "http" (when ls/jwt-cookie-secure "s") "://" ls/web-server "/" (get-link))]
    [:div.carrot-box-container.group
      ; [:img.carrot-box {:src (utils/cdn "/img/ML/carrot_box.svg")}]
      [:div.carrot-box-thanks
        [:div.thanks-headline "Thanks!"]
        thanks-message
        [:div.carrot-early-access-top.hidden "Get earlier access when your friends sign up with this link:"]
        [:a.carrot-early-access-link.hidden {:href carrot-link} carrot-link]]]))