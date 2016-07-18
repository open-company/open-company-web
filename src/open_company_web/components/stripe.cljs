(ns open-company-web.components.stripe
  (:require [rum.core :as rum]
            [open-company-web.api :as api]
            [goog.net.jsloader :as jsl]))

;; Sripe JS loading via higher-order components ================================

(defn checkout-loader-mx [k]
  {:did-mount (fn [state]
                (.then (jsl/loadMany #js ["https://js.stripe.com/v2/" "https://checkout.stripe.com/checkout.js"])
                       (fn []
                         (js/console.info "Checkout.js loaded")
                         (reset! (get state k) true)))
                state)})

(rum/defcs checkout-loader
  < rum/static rum/reactive (rum/local nil ::loaded?) (checkout-loader-mx ::loaded?)
  [s loading loaded]
  (if (-> s ::loaded? rum/react)
    loaded
    loading))

(defn create-subscription! [token-object]
  (js/console.log token-object)
  (api/pay-post "/subscriptions"
                {:json-params {:token (.-id token-object)
                               :email (.-email token-object)}}
                #(js/console.log (pr-str %))))

(def opts
  {:key "pk_test_SFPBvun1IpNxuYzi9KWl6GNg"
   :image "/img/oc-logo-gold.svg"
   :token create-subscription!
   :locale "auto"
   :name "Premium"
   :description "Super Premium stuff"
   :amount 2500})

(rum/defcs with-checkout-handler
  < {:will-mount (fn [s] (assoc s ::handler (js/StripeCheckout.configure (clj->js opts))))
     :will-unmount (fn [s] (.close (::handler s)) s)}
  [s component-fn]
  (component-fn (::handler s)))

(rum/defc checkout-btn [handler]
  [:button.btn-reset.btn-outline
   {:on-click #(.open handler)}
   "Upgrade to premium"])

(rum/defc stripe-checkout []
  [:div
   (checkout-loader
    [:h1 "Checkout loading"]
    (with-checkout-handler checkout-btn))])
