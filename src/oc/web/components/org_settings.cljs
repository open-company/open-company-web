(ns oc.web.components.org-settings
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [rum.core :as rum]
            [clojure.string :as string]
            [oc.web.local-settings :as ls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.small-loading :as loading]
            [oc.web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]
            [oc.web.components.ui.footer :as footer]
            [oc.web.components.ui.login-required :refer (login-required)]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.urls :as oc-urls]
            [oc.web.api :as api]
            [oc.web.dispatcher :as dis]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.image-upload :as iu]
            [goog.events :as events]
            [goog.fx.dom :refer (Fade)]
            [goog.fx.Animation.EventType :as AnimationEventType]))

(rum/defcs thanks-for-subscribing
  < {:will-unmount (fn [s] (cook/remove-cookie! :subscription-callback-slug) s)}
  []
  [:div.p3.border.mb3.rounded.success
   [:h2.m0.mb2 "Thank you!"]
   [:span "If you have any questions you can always reach us "
    [:a
     {:href oc-urls/contact-mail-to}
     "via email."]]])

(rum/defcs subscription-info
  < {:did-mount (fn [s] (api/get-subscription (last (:rum/args s))) s)}
    (drv/drv :subscription)
    (drv/drv :jwt)
    rum/reactive
  [s slug org-slug]
  [:div
    ;; Thank you message when they have a cookie and a subscription
    (when (and (cook/get-cookie :subscription-callback-slug)
               (drv/react s :subscription))
      (thanks-for-subscribing))
   [:div.small-caps.bold.mb1 "Subscription Plan"]
   (if (drv/react s :subscription)
     [:div
      [:p.mb2 "You are on the Carrot Beta Plan."]
      [:p.mb2 "To cancel your plan, "
        [:a {:href oc-urls/contact-mail-to} "contact us"]
        "."]]
      ; [:a.btn-reset.btn-solid
      ;  {:href (get-in (drv/react s :subscription) [org-slug :account-url])}
      ;  "Manage your subscription"]]
     [:div
      [:p.mb2 "You are on a 14-day trial."]
      (let [[fn ln] (-> (drv/react s :jwt) :real-name (string/split #"\s" 2))]
        [:a.btn-reset.btn-solid
         {:on-click #(cook/set-cookie! :subscription-callback-slug slug (* 60 60 24))
          :href (str "https://" ls/recurly-id ".recurly.com/subscribe/" ls/recurly-plan "/" org-slug
                     "?email=" (:email (drv/react s :jwt)) "&first_name=" fn "&last_name=" ln)}
         "Subscribe"])])])

(defn- save-org-data [org-data logo-url logo-width logo-height]
  (let [slug (router/current-org-slug)
        fixed-logo-url (or logo-url "")
        fixed-logo-width (or logo-width 0)
        fixed-logo-height (or logo-height 0)]
    (api/patch-org {:name (:name org-data)
                    :slug slug
                    :logo-width (js/parseInt fixed-logo-width)
                    :logo-height (js/parseInt fixed-logo-height)
                    :logo-url fixed-logo-url})))

(defn- check-img-cb [owner data img result]
  (if-not result
    ; there was an error loading the logo, could be an invalid URL
    ; or the link doesn't contain an image
    (do
      (dis/dispatch! [:error-banner-show "Invalid image url." 5000])
      (om/set-state! owner :logo-url (om/get-state owner :initial-logo))
      (om/set-state! owner :loading false))
    (save-org-data data (om/get-state owner :logo-url) (.-width img) (.-height img))))

(defn- check-image [url owner data]
  (let [img (new js/Image)]
    (set! (.-onload img) #(check-img-cb owner data img true))
    (set! (.-onerror img) #(check-img-cb owner data img false))
    (set! (.-src img) url)))

(defn save-org-clicked [owner]
  (let [logo-url     (om/get-state owner :logo-url)
        old-org-data (dis/org-data (om/get-props owner))
        new-org-data {:name (om/get-state owner :org-name)}]
    (om/set-state! owner :loading true)
    ; if the log has changed
    (if (not= logo-url (om/get-state owner :initial-logo))
      ; and it's empty
      (if (clojure.string/blank? logo-url)
        ; save the data w/o a logo
        (save-org-data new-org-data "" 0 0)
        ; else check the logo
        (check-image logo-url owner new-org-data))
      ; else save the org datas
      (save-org-data new-org-data (:logo-url old-org-data) (:logo-width old-org-data) (:logo-height old-org-data)))))

(defn- success-cb [owner res]
  (let [url (.-url res)]
    (om/update-state! owner #(merge % {:file-upload-state nil
                                       :file-upload-progress nil
                                       :logo-did-change true
                                       :state-logo url}))))

(defn- error-cb [owner res error]
  (dis/dispatch! [:error-banner-show "An error occurred while uploading the image. Please retry." 5000])
  (om/update-state! owner #(merge % {:file-upload-state nil
                                     :file-upload-progress nil})))

(defn- progress-cb [owner res progress]
  (let [state (om/get-state owner)]
    (om/set-state! owner (merge state {:file-upload-state :show-progress
                                       :file-upload-progress progress}))))

(defcomponent org-logo-setup [{:keys [logo-url logo-did-change-cb logo-did-load-cb] :as data} owner]
  (init-state [_]
    {:file-upload-state nil
     :file-upload-progress nil
     :logo-did-change false
     :state-logo logo-url
     :upload-remote-url nil})

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))))

  (will-receive-props [_ next-props]
    (when (and (not= (:logo-url next-props) (:logo-url data))
               (nil? (om/get-state owner :file-upload-state))
               (nil? (om/get-state owner :upload-remote-url)))
      (om/set-state! owner :state-logo (:logo-url next-props))))

  (did-update [_ _ prev-state]
    (when (and (not= (:state-logo prev-state) (om/get-state owner :state-logo))
               (not= (om/get-state owner :state-logo) (:logo-url data)))
      (when (fn? logo-did-change-cb)
        (logo-did-change-cb (om/get-state owner :state-logo)))))

  (render-state [_ {:keys [file-upload-state file-upload-progress upload-remote-url state-logo logo-did-change]}]
    (dom/div {}
      (when-not (empty? state-logo)
        (dom/div {:class "org-logo-container"}
          (dom/img {:src state-logo
                    :class "org-logo"
                    :on-load #(when (and logo-did-change (fn? logo-did-load-cb))
                                (logo-did-load-cb state-logo (.width (js/$ "img.org-logo")) (.height (js/$ "img.org-logo"))))
                    :on-error #(when (and logo-did-change (fn? logo-did-load-cb))
                                (logo-did-load-cb state-logo nil nil))})))
      (dom/div {:class "group"
                :style {:margin-bottom "2rem"}}
        (dom/button {:class "btn-reset camera left"
                     :title "Upload a logo"
                     :type "button"
                     :data-toggle "tooltip"
                     :data-container "body"
                     :data-placement "bottom"
                     :style {:display (if (nil? file-upload-state) "block" "none")}
                     :on-click #(iu/upload!
                                 {:accept "image/*"}
                                 (partial success-cb owner)
                                 (partial progress-cb owner)
                                 (partial error-cb owner))}
          (dom/i {:class "fa fa-camera"}))
        (dom/button {:class "btn-reset image-url left"
                     :title "Provide a link to your logo"
                     :type "button"
                     :data-toggle "tooltip"
                     :data-container "body"
                     :data-placement "bottom"
                     :style {:display (if (nil? file-upload-state) "block" "none")}
                     :on-click #(om/set-state! owner :file-upload-state :show-url-field)}
          (dom/i {:class "fa fa-link"}))
        (dom/div {:class "left"
                  :style {:display (if (= file-upload-state :show-progress) "block" "none")
                          :color "rgba(78, 90, 107, 0.5)"}}
          (str file-upload-progress "%"))))))

(defn get-state [data current-state]
  (let [org-data (dis/org-data data)]
    {:slug (:slug org-data)
     :initial-logo (:logo-url org-data)
     :logo-url (or (:logo-url current-state) (:logo-url org-data))
     :org-name (or (:org-name current-state) (:name org-data))
     :loading false
     :has-changes (or (:has-changes current-state) false)
     :show-save-successful (or (:show-save-successful current-state) false)}))

(defn cancel-clicked [owner]
  (om/set-state! owner (get-state (om/get-props owner) nil)))

(defcomponent org-settings-form [data owner]

  (init-state [_]
    (get-state data nil))

  (will-receive-props [_ next-props]
    (when (om/get-state owner :loading)
      (utils/after 1500 (fn []
                          (when-let [save-successful-el (sel1 [:div#org-settings-save-successful])]
                            (let [fade-animation (new Fade save-successful-el 1 0 utils/oc-animation-duration)]
                              (doto fade-animation
                                (.listen AnimationEventType/FINISH #(om/set-state! owner :show-save-successful false))
                                (.play))))))
      (om/set-state! owner (get-state next-props {:show-save-successful (om/get-state owner :loading)})))
    (when-not (om/get-state owner :loading)
      (om/set-state! owner (get-state next-props (om/get-state owner)))))

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (when-not (responsive/is-tablet-or-mobile?)
        (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))))

  (render-state [_ {org-slug :slug org-name :org-name logo-url :logo-url loading :loading
                    file-upload-state :file-upload-state upload-remote-url :upload-remote-url
                    file-upload-progress :file-upload-progress
                    show-save-successful :show-save-successful
                    has-changes :has-changes}]
    (let [slug (keyword (router/current-org-slug))]

      (utils/update-page-title (str "Carrot - " org-name))

      (dom/div {:class "mx-auto my4 settings-container group"}
        
        (when-not org-slug
          (dom/div {:class "settings-form-label org-settings"}
            (loading/small-loading)))
        
        (dom/div {:class "settings-form p3"}

          ;; Org name
          (dom/div {:class "settings-form-input-label"} "COMPANY NAME")
          (dom/input {:class "npt col-8 p1 mb3"
                      :type "text"
                      :id "name"
                      :value (or org-name "")
                      :on-change #(do
                                   (om/set-state! owner :has-changes true)
                                   (om/set-state! owner :org-name (.. % -target -value)))})

          ; Slug
          (dom/div {:class "settings-form-input-label"} "DASHBOARD URL")
          (dom/div {:class "dashboard-slug"} (str "http" (when ls/jwt-cookie-secure "s") "://" ls/web-server (oc-urls/org slug)))

          ;; org logo
          (dom/div {:class "settings-form-input-label"} "COMPANY LOGO (square works best, approx. 160px per side)")
          (om/build org-logo-setup {:logo-url logo-url
                                    :logo-did-load-cb #(do
                                                        (om/set-state! owner :has-changes true)
                                                        (om/set-state! owner :logo-url %))})

          ;; Save button
          (dom/div {:class "mt2 right-align group"}
            (dom/button {:class "btn-reset btn-solid right"
                         :on-click #(save-org-clicked owner)
                         :disabled (not has-changes)}
              (if loading
                (loading/small-loading)
                "SAVE"))
            (dom/button {:class "btn-reset btn-outline right mr2"
                         :on-click #(cancel-clicked owner)
                         :disabled (not has-changes)}
              "CANCEL")
            (dom/div {:style {:margin-top "5px"
                              :opacity (if show-save-successful "1" "0")}
                      :class "mr2 right"
                      :id "org-settings-save-successful"}
              "Save successful")))

        (when false ; hide until Stripe/Recurly accounts are live

          (dom/div {:class "settings-form-label subscription-settings-label"}
            (dom/span {} "Account")
            (when-not org-slug
              (loading/small-loading)))

          (when org-slug
              (dom/div {:class "settings-form p3"}
                (subscription-info (name org-slug) org-slug))))))))

(defcomponent org-settings [data owner]

  (render [_]
    (let [org-data (dis/org-data data)]
      (when (:read-only org-data)
        (router/redirect! (oc-urls/org)))

      (dom/div {:class "main-org-settings fullscreen-page"}

        (cond
          ;; the data is still loading
          (:loading data)
          (dom/div (dom/h4 "Loading data..."))

          (get-in data [(keyword (router/current-org-slug)) :error])
          (login-required)

          ;; Org profile
          :else
          (dom/div {}
            (back-to-dashboard-btn {:title "Company Settings"})
            (dom/div {:class "org-settings-container"}
              (om/build org-settings-form data))))

        (let [columns-num (responsive/columns-num)
              card-width responsive/card-width
              footer-width (responsive/total-layout-width-int card-width columns-num)]
          (footer/footer footer-width))))))