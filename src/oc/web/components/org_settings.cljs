(ns oc.web.components.org-settings
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [taoensso.timbre :as timbre]
            [dommy.core :refer-macros (sel1)]
            [rum.core :as rum]
            [clojure.string :as string]
            [org.martinklepsch.derivatives :as drv]
            [goog.events :as events]
            [goog.fx.dom :refer (Fade)]
            [goog.fx.Animation.EventType :as AnimationEventType]
            [oc.web.api :as api]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.local-settings :as ls]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.iso4217 :as iso4217]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.small-loading :as loading]
            [oc.web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]
            [oc.web.components.ui.footer :as footer]
            [oc.web.components.ui.login-required :refer (login-required)]))

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
  [s slug org-uuid]
  [:div
    ;; Thank you message when they have a cookie and a subscription
    (when (and (cook/get-cookie :subscription-callback-slug)
               (drv/react s :subscription))
      (thanks-for-subscribing))
   [:div.small-caps.bold.mb1 "Subscription Plan"]
   (if (drv/react s :subscription)
     [:div
      [:p.mb2 "You are on the OpenCompany Beta Plan."]
      [:p.mb2 "To cancel your plan, "
        [:a {:href oc-urls/contact-mail-to} "contact us"]
        "."]]
      ; [:a.btn-reset.btn-solid
      ;  {:href (get-in (drv/react s :subscription) [org-uuid :account-url])}
      ;  "Manage your subscription"]]
     [:div
      [:p.mb2 "You are on a 14-day trial."]
      (let [[fn ln] (-> (drv/react s :jwt) :real-name (string/split #"\s" 2))]
        [:a.btn-reset.btn-solid
         {:on-click #(cook/set-cookie! :subscription-callback-slug slug (* 60 60 24))
          :href (str "https://" ls/recurly-id ".recurly.com/subscribe/" ls/recurly-plan "/" org-uuid
                     "?email=" (:email (drv/react s :jwt)) "&first_name=" fn "&last_name=" ln)}
         "Subscribe"])])])

(defn- save-org-data [org-data logo-url logo-width logo-height]
  (let [slug (router/current-board-slug)
        fixed-logo (or logo-url "")
        fixed-logo-width (or logo-width 0)
        fixed-logo-height (or logo-height 0)]
    (api/patch-org {:name (:name org-data)
                    :currency (:currency org-data)
                    ; :public (:public company-data)
                    :logo-width (js/parseInt fixed-logo-width)
                    :logo-height (js/parseInt fixed-logo-height)
                    :logo-url fixed-logo})))

(defn- check-img-cb [owner data img result]
 (if-not result
    ; there was an error loading the logo, could be an invalid URL
    ; or the link doesn't contain an image
    (do
      (js/alert "Invalid image url")
      (om/set-state! owner :logo-url (om/get-state owner :initial-logo-url))
      (om/set-state! owner :loading false))
    (save-org-data data (om/get-state owner :logo-url) (.-width img) (.-height img))))

(defn- check-image [url owner data]
  (let [img (new js/Image)]
    (set! (.-onload img) #(check-img-cb owner data img true))
    (set! (.-onerror img) #(check-img-cb owner data img false))
    (set! (.-src img) url)))

(defn save-org-clicked [owner]
  (let [logo-url     (om/get-state owner :logo-url)
        old-org-data (dis/board-data (om/get-props owner))
        new-org-data {:name (om/get-state owner :org-name)
                      :currency (om/get-state owner :currency)
                      ; :public (om/get-state owner :public)
                      }]
    (om/set-state! owner :loading true)
    ; if the log has changed
    (if (not= logo-url (om/get-state owner :initial-logo-url))
      ; and it's empty
      (if (clojure.string/blank? logo-url)
        ; save the data w/o a logo
        (save-org-data new-org-data "" 0 0)
        ; else check the logo
        (check-image logo-url owner new-org-data))
      ; else save the company datas
      (save-org-data new-org-data (:logo-url old-org-data) (:logo-width old-org-data) (:logo-height old-org-data)))))

(defcomponent currency-option [data owner]
  (render [_]
    (dom/option {:value (or (:value data) (:text data))
                 :disabled (and (contains? :value data) (= (count (:value data)) 0))}
      (:text data))))

(defn- upload-file! [owner file]
  (js/filepicker.setKey ls/filestack-key)
  (let [success-cb  (fn [success]
                      (let [url    (.-url success)]
                        (om/set-state! owner (merge (om/get-state owner) {:file-upload-state nil
                                                                          :file-upload-progress nil
                                                                          :logo-did-change true
                                                                          :state-logo url}))))
        error-cb    (fn [error]
                      (timbre/error "Error uploading image:" error)
                      (js/alert "An error occurred while uploading the file, please try again.")
                      (om/set-state! owner (merge state {:file-upload-state nil
                                                         :file-upload-progress nil})))
        progress-cb (fn [progress]
                      (let [state (om/get-state owner)]
                        (om/set-state! owner (merge state {:file-upload-state :show-progress
                                                           :file-upload-progress progress}))))]
    (cond
      (and (string? file) (not (string/blank? file)))
      (js/filepicker.storeUrl file success-cb error-cb progress-cb)
      file
      (js/filepicker.store file #js {:name (.-name file)} success-cb error-cb progress-cb))))

(defcomponent org-logo-setup [{:keys [read-only logo-url logo-did-change-cb logo-did-load-cb] :as data} owner]
  (init-state [_]
    {:file-upload-state nil
     :file-upload-progress nil
     :logo-did-change false
     :state-logo logo-url
     :upload-remote-url nil})

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (js/filepicker.setKey ls/filestack-key)
      (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))))

  (will-receive-props [_ next-props]
    (when (and (not= (:logo next-props) (:logo data))
               (nil? (om/get-state owner :file-upload-state))
               (nil? (om/get-state owner :upload-remote-url)))
      (om/set-state! owner :state-logo (:logo next-props))))

  (did-update [_ _ prev-state]
    (when (and (not= (:state-logo prev-state) (om/get-state owner :state-logo))
               (not= (om/get-state owner :state-logo) (:logo data)))
      (when (fn? logo-did-change-cb)
        (logo-did-change-cb (om/get-state owner :state-logo)))))

  (render-state [_ {:keys [file-upload-state file-upload-progress upload-remote-url state-logo logo-did-change]}]
    (dom/div {}
      (dom/div {}
        (if (not= file-upload-state :show-url-field)
          (dom/div {:class "org-logo-container"}
            (when-not (string/blank? state-logo)
              (dom/img {:src state-logo
                        :class "org-logo"
                        :data-test "aaa"
                        :on-load #(when (and logo-did-change (fn? logo-did-load-cb))
                                    (logo-did-load-cb state-logo (.width (js/$ "img.org-logo")) (.height (js/$ "img.org-logo"))))
                        :on-error #(when (and logo-did-change (fn? logo-did-load-cb))
                                    (logo-did-load-cb state-logo nil nil))})))
          (dom/div {:class (str "upload-remote-url-container left" (when-not (= file-upload-state :show-url-field) " hidden"))}
              (dom/input {:type "text"
                          :class "npt col-7 p1 mb3"
                          :on-change #(om/set-state! owner :upload-remote-url (-> % .-target .-value))
                          :placeholder "http://site.com/img.png"
                          :value (or upload-remote-url "")})
              (dom/button {:style {:font-size "14px" :margin-left "5px" :padding "0.3rem"}
                           :class "btn-reset btn-solid"
                           :disabled (string/blank? upload-remote-url)
                           :on-click #(upload-file! owner (om/get-state owner :upload-remote-url))}
                "add")
              (dom/button {:style {:font-size "14px" :margin-left "5px" :padding "0.3rem"}
                           :class "btn-reset btn-outline"
                           :on-click #(om/set-state! owner :file-upload-state nil)}
                "cancel"))))
      (when-not read-only
        (dom/div {:class "group"}
          (dom/input {:id "foce-file-upload-ui--select-trigger"
                      :style {:display "none"}
                      :type "file"
                      :accept "image/x-png, image/gif, image/jpeg"
                      :on-change #(upload-file! owner (-> % .-target .-files (aget 0)))})
          (dom/button {:class "btn-reset camera left"
                       :title "Upload a logo"
                       :type "button"
                       :data-toggle "tooltip"
                       :data-container "body"
                       :data-placement "bottom"
                       :style {:display (if (nil? file-upload-state) "block" "none")}
                       :on-click #(.click (sel1 [:input#foce-file-upload-ui--select-trigger]))}
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
            (str file-upload-progress "%")))))))

(defn get-state [data current-state]
  (let [org-data (dis/org-data data)]
    {:org-id (:org-id org-data)
     :initial-logo-url (:logo-url data)
     :logo-url (or (:logo-url current-state) (:logo-url org-data))
     :org-name (or (:org-name current-state) (:name org-data))
     :read-only (:read-only org-data)
     :currency (or (:currency current-state) (:currency org-data))
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
                          (let [fade-animation (new Fade (sel1 [:div#org-settings-save-successful]) 1 0 utils/oc-animation-duration)]
                            (doto fade-animation
                              (.listen AnimationEventType/FINISH #(om/set-state! owner :show-save-successful false))
                              (.play))))))
    (om/set-state! owner (get-state next-props {:show-save-successful (om/get-state owner :loading)})))

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (js/filepicker.setKey ls/filestack-key)
      (when-not (responsive/is-tablet-or-mobile?)
        (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))))

  (render-state [_ {org-id :org-id org-name :org-name logo-url :logo-url
                    currency :currency loading :loading
                    file-upload-state :file-upload-state
                    read-only :read-only
                    upload-remote-url :upload-remote-url
                    file-upload-progress :file-upload-progress
                    show-save-successful :show-save-successful
                    has-changes :has-changes}]
    (let [slug (keyword (router/current-org-slug))]

      (utils/update-page-title (str "OpenCompany - " org-name))

      (dom/div {:class "mx-auto my4 settings-container group"}
        
        (when-not org-name
          (dom/div {:class "settings-form-label org-settings"}
            (loading/small-loading)))
        
        (dom/div {:class "settings-form p3"}

          ;; Org name
          (dom/div {:class "settings-form-input-label"} "COMPANY NAME")
          (dom/input {:class "npt col-8 p1 mb3"
                      :type "text"
                      :id "name"
                      :disabled read-only
                      :value (or org-name "")
                      :on-change #(do
                                   (om/set-state! owner :has-changes true)
                                   (om/set-state! owner :org-name (.. % -target -value)))})

          ; ;; Visibility
          ; (dom/div {:class "settings-form-input-label"} "VISIBILITY")
          ; (dom/div {:class "settings-form-input visibility"}
          ;   (dom/div {:class "visibility-value"
          ;             :on-click #(do
          ;                         (om/set-state! owner :has-changes true)
          ;                         (om/set-state! owner :public false))}
          ;     (dom/h3 {} "Private "
          ;       (when (not public)
          ;         (dom/i {:class "fa fa-check"})))
          ;     (dom/p {} "Only team members can view, edit and share information."))
          ;   (dom/div {:class "visibility-value"
          ;             :on-click #(do
          ;                         (om/set-state! owner :has-changes true)
          ;                         (om/set-state! owner :public true))}
          ;     (dom/h3 {} "Public "
          ;       (when public
          ;         (dom/i {:class "fa fa-check"})))
          ;     (dom/p {} "Your information is public and will show up in search engines like Google. Only team members can edit and share information.")))

          ; Slug
          (dom/div {:class "settings-form-input-label"} "COMPANY URL")
          (dom/div {:class "dashboard-slug"} (str "http" (when ls/jwt-cookie-secure "s") "://" ls/web-server "/" (name slug)))

          ;; Company logo
          (dom/div {:class "settings-form-input-label"} "COMPANY LOGO (square works best, approx. 160px per side)")
          (om/build org-logo-setup {:logo-url logo-url
                                    :read-only read-only
                                    :logo-did-change-cb #(do
                                                          (om/set-state! owner :has-changes true)
                                                          (om/set-state! owner :logo-url %))})

          ;; Currency
          (dom/div {:class "settings-form-input-label mt3"} "DISPLAY CURRENCY IN CHARTS AS")
          (dom/select {:id "currency"
                       :value (or currency "")
                       :disabled read-only
                       :on-change #(do
                                    (om/set-state! owner :has-changes true)
                                    (om/set-state! owner :currency (.. % -target -value)))
                       :class "npt col-8 p1 mb3 org-currency"}
            (for [currency (iso4217/sorted-iso4217)]
              (let [symbol (:symbol currency)
                    display-symbol (or symbol (:code currency))
                    label (str (:text currency) " " display-symbol)]
                (om/build currency-option
                          {:value (:code currency) :text label}
                          {:react-key (:code currency)}))))

          ;; Save button
          (when-not read-only
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
                "Save successful"))))

        (when false ; hide until Stripe/Recurly accounts are live

          (dom/div {:class "settings-form-label subscription-settings-label"}
            (dom/span {} "Account")
            (when-not org-id
              (loading/small-loading)))

          (when org-id
              (dom/div {:class "settings-form p3"}
                (subscription-info (name slug) org-id))))))))

(defcomponent org-settings [data owner]

  (render [_]
    (let [org-data (dis/org-data data)
          ro (:read-only org-data)]

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
              card-width (responsive/calc-card-width)
              footer-width (responsive/total-layout-width-int card-width columns-num)]
          (footer/footer footer-width))))))