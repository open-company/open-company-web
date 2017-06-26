(ns oc.web.components.ui.try-it-form
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(rum/defcs try-it-form < (rum/local (int (rand 100)) ::input-class)
                         (rum/local "" ::email-value)
  [s try-it-input-class get-started-cb]
  (let [try-it-final-class (or try-it-input-class (str "try-it-class-" @(::input-class s)))]
    [:form.validate
      {:action "//opencompany.us11.list-manage.com/subscribe/post?u=16bbc69a5b39531f20233bd5f&amp;id=2ee535bf29"
       :method "post"
       :id "mc-embedded-subscribe-form"
       :name "mc-embedded-subscribe-form"
       :target "_blank"
       :no-validate true}
      [:div.try-it-combo-field
        {:id "mc_embed_signup_scroll"}
        [:div.hidden "real people should not fill this in and expect good things - do not remove this or risk form bot signups"]
        [:div.clear {:id "mce-responses"}
          [:div.response.hidden {:id "mce-error-response"}]
          [:div.response.hidden {:id "mce-success-response"}]]
        [:div.mc-field-group
          [:input.mail.required
            {:type "text"
             :value @(::email-value s)
             :class try-it-final-class
             :id "mce-EMAIL"
             :on-change #(reset! (::email-value s) (.. % -target -value))
             :name "EMAIL"
             :placeholder "Email address"}]]
        [:div {:style #js {:position "absolute" :left "-5000px"}
               :aria-hidden true}
          [:input {:type "text" :name "b_16bbc69a5b39531f20233bd5f_2ee535bf29" :tab-index "-1" :value ""}]]
        [:button.mlb-reset.try-it-get-started
          {:type "submit"
           :id "mc-embedded-subscribe"
           :disabled (not (utils/valid-email? @(::email-value s)))
           :on-click #(let [email-value @(::email-value s)]
                        (when (and (fn? get-started-cb)
                                   (utils/valid-email? email-value))
                          (get-started-cb email-value)))}
          "Get Started"]]]))