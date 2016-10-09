(ns open-company-web.components.company-settings
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [rum.core :as rum]
            [clojure.string :as string]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.small-loading :as loading]
            [open-company-web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]
            [open-company-web.components.ui.footer :as footer]
            [open-company-web.components.user-management :refer (user-management)]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.cookies :as cook]
            [open-company-web.urls :as oc-urls]
            [open-company-web.api :as api]
            [open-company-web.local-settings :as ls]
            [open-company-web.dispatcher :as dis]
            [org.martinklepsch.derivatives :as drv]
            [open-company-web.lib.iso4217 :as iso4217]
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
  [s slug company-uuid]
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
      ;  {:href (get-in (drv/react s :subscription) [company-uuid :account-url])}
      ;  "Manage your subscription"]]
     [:div
      [:p.mb2 "You are on a 14-day trial."]
      (let [[fn ln] (-> (drv/react s :jwt) :real-name (string/split #"\s" 2))]
        [:a.btn-reset.btn-solid
         {:on-click #(cook/set-cookie! :subscription-callback-slug slug (* 60 60 24))
          :href (str "https://" ls/recurly-id ".recurly.com/subscribe/" ls/recurly-plan "/" company-uuid
                     "?email=" (:email (drv/react s :jwt)) "&first_name=" fn "&last_name=" ln)}
         "Subscribe"])])])

(defn- save-company-data [company-data logo logo-width logo-height]
  (let [slug (router/current-company-slug)
        fixed-logo (or logo "")
        fixed-logo-width (or logo-width 0)
        fixed-logo-height (or logo-height 0)]
    (api/patch-company slug {:name (:name company-data)
                             :slug slug
                             :currency (:currency company-data)
                             :logo-width (js/parseInt fixed-logo-width)
                             :logo-height (js/parseInt fixed-logo-height)
                             :logo fixed-logo})))

(defn- check-img-cb [owner data img result]
 (if-not result
    ; there was an error loading the logo, could be an invalid URL
    ; or the link doesn't contain an image
    (do
      (js/alert "Invalid image url")
      (om/set-state! owner :logo (om/get-state owner :initial-logo))
      (om/set-state! owner :loading false))
    (save-company-data data (om/get-state owner :logo) (.-width img) (.-height img))))

(defn- check-image [url owner data]
  (let [img (new js/Image)]
    (set! (.-onload img) #(check-img-cb owner data img true))
    (set! (.-onerror img) #(check-img-cb owner data img false))
    (set! (.-src img) url)))

(defn save-company-clicked [owner]
  (let [logo         (om/get-state owner :logo)
        old-company-data (dis/company-data (om/get-props owner))
        new-company-data {:name (om/get-state owner :company-name)
                          :currency (om/get-state owner :currency)}]
    (om/set-state! owner :loading true)
    ; if the log has changed
    (if (not= logo (om/get-state owner :initial-logo))
      ; and it's empty
      (if (clojure.string/blank? logo)
        ; save the data w/o a logo
        (save-company-data new-company-data "" 0 0)
        ; else check the logo
        (check-image logo owner new-company-data))
      ; else save the company datas
      (save-company-data new-company-data (:logo old-company-data) (:logo-width old-company-data) (:logo-height old-company-data)))))

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
        error-cb    (fn [error] (js/console.log "error" error))
        progress-cb (fn [progress]
                      (let [state (om/get-state owner)]
                        (om/set-state! owner (merge state {:file-upload-state :show-progress
                                                           :file-upload-progress progress}))))]
    (cond
      (and (string? file) (not (string/blank? file)))
      (js/filepicker.storeUrl file success-cb error-cb progress-cb)
      file
      (js/filepicker.store file #js {:name (.-name file)} success-cb error-cb progress-cb))))

(defcomponent company-logo-setup [{:keys [logo logo-did-change-cb logo-did-load-cb] :as data} owner]
  (init-state [_]
    {:file-upload-state nil
     :file-upload-progress nil
     :logo-did-change false
     :state-logo logo
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
          (dom/div {:class "company-logo-container"}
            (when-not (string/blank? state-logo)
              (dom/img {:src state-logo
                        :class "company-logo"
                        :data-test "aaa"
                        :on-load #(when (and logo-did-change (fn? logo-did-load-cb))
                                    (logo-did-load-cb state-logo (.width (js/$ "img.company-logo")) (.height (js/$ "img.company-logo"))))
                        :on-error #(when (and logo-did-change (fn? logo-did-load-cb))
                                    (logo-did-load-cb state-logo nil nil))})))
          (dom/div {:class (str "upload-remote-url-container left" (when-not (= file-upload-state :show-url-field) " hidden"))}
              (dom/input {:type "text"
                          :class "npt col-7 p1 mb3"
                          :on-change #(om/set-state! owner :upload-remote-url (-> % .-target .-value))
                          :placeholder "http://site.com/img.png"
                          :value upload-remote-url})
              (dom/button {:style {:font-size "14px" :margin-left "5px" :padding "0.3rem"}
                           :class "btn-reset btn-solid"
                           :disabled (string/blank? upload-remote-url)
                           :on-click #(upload-file! owner (om/get-state owner :upload-remote-url))}
                "add")
              (dom/button {:style {:font-size "14px" :margin-left "5px" :padding "0.3rem"}
                           :class "btn-reset btn-outline"
                           :on-click #(om/set-state! owner :file-upload-state nil)}
                "cancel"))))
      (dom/div {:class "group"
                :style {:margin-bottom "2rem"}}
        (dom/input {:id "foce-file-upload-ui--select-trigger"
                    :style {:display "none"}
                    :type "file"
                    :on-change #(upload-file! owner (-> % .-target .-files (aget 0)))})
        (dom/button {:class "btn-reset camera left"
                     :title "Upload a logo"
                     :type "button"
                     :data-toggle "tooltip"
                     :data-placement "bottom"
                     :data-container "body"
                     :style {:display (if (nil? file-upload-state) "block" "none")}
                     :on-click #(.click (sel1 [:input#foce-file-upload-ui--select-trigger]))}
          (dom/i {:class "fa fa-camera"}))
        (dom/button {:class "btn-reset image-url left"
                     :title "Provide a link to your logo"
                     :type "button"
                     :data-toggle "tooltip"
                     :data-placement "bottom"
                     :data-container "body"
                     :style {:display (if (nil? file-upload-state) "block" "none")}
                     :on-click #(om/set-state! owner :file-upload-state :show-url-field)}
          (dom/i {:class "fa fa-link"}))
        (dom/div {:class "left"
                  :style {:display (if (= file-upload-state :show-progress) "block" "none")
                          :color "rgba(78, 90, 107, 0.5)"}}
          (str file-upload-progress "%"))))))

(defn get-state [data current-state]
  (let [company-data (dis/company-data data)]
    {:uuid (:uuid company-data)
     :initial-logo (:logo data)
     :logo (or (:logo current-state) (:logo company-data))
     :company-name (or (:company-name current-state) (:name company-data))
     :currency (or (:currency current-state) (:currency company-data))
     :loading false
     :show-save-successful (or (:show-save-successful current-state) false)}))

(defcomponent company-settings-form [data owner]

  (init-state [_]
    (get-state data nil))

  (will-receive-props [_ next-props]
    (when (om/get-state owner :loading)
      (utils/after 1500 (fn []
                          (let [fade-animation (new Fade (sel1 [:div#company-settings-save-successful]) 1 0 utils/oc-animation-duration)]
                            (doto fade-animation
                              (.listen AnimationEventType/FINISH #(om/set-state! owner :show-save-successful false))
                              (.play))))))
    (om/set-state! owner (get-state next-props {:show-save-successful (om/get-state owner :loading)})))

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (js/filepicker.setKey ls/filestack-key)
      (when-not (responsive/is-tablet-or-mobile?)
        (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))))

  (render-state [_ {company-uuid :uuid company-name :company-name logo :logo
                    currency :currency loading :loading
                    file-upload-state :file-upload-state upload-remote-url :upload-remote-url
                    file-upload-progress :file-upload-progress
                    show-save-successful :show-save-successful}]
    (let [slug (keyword (router/current-company-slug))]

      (utils/update-page-title (str "OpenCompany - " company-name))

      (dom/div {:class "lg-col-5 md-col-7 col-11 mx-auto mt4 mb4 settings-container group"}
        
        (dom/div {:class "settings-form-label company-settings"}
          (dom/span {} "Company Settings")
          (when-not company-name
            (loading/small-loading)))
        
        (dom/div {:class "settings-form p3"}

          ;; Company name
          (dom/div {:class "settings-form-input-label"} "COMPANY NAME")
          (dom/input {:class "npt col-8 p1 mb3"
                      :type "text"
                      :id "name"
                      :value company-name
                      :on-change #(om/set-state! owner :company-name (.. % -target -value))})
          ; Slug
          (dom/div {:class "settings-form-input-label"} "DASHBOARD URL")
          (dom/div {:class "npt npt-disabled col-11 p1 mb3"} (str ls/web-server "/" (name slug)))

          ;; Company logo
          (dom/div {:class "settings-form-input-label"} "A SQUARE COMPANY LOGO URL (approx. 160px per side)")
          (om/build company-logo-setup {:logo logo
                                        :logo-did-change-cb #(om/set-state! owner :logo %)})

          ;; Currency
          (dom/div {:class "settings-form-input-label"} "DISPLAY FINANCE & GROWTH CHART CURRENCY AS")
          (dom/select {:id "currency"
                       :value currency
                       :on-change #(om/set-state! owner :currency (.. % -target -value))
                       :class "npt col-8 p1 mb3 company-currency"}
            (for [currency (iso4217/sorted-iso4217)]
              (let [symbol (:symbol currency)
                    display-symbol (or symbol (:code currency))
                    label (str (:text currency) " " display-symbol)]
                (om/build currency-option
                          {:value (:code currency) :text label}
                          {:react-key (:code currency)}))))

          ;; Save button
          (dom/div {:class "mt2 right-align group"}
            (dom/button {:class "btn-reset btn-solid right"
                         :on-click #(save-company-clicked owner)}
              (if loading
                (loading/small-loading)
                "SAVE"))
            (dom/div {:style {:float "right"
                              :margin-right "20px"
                              :color "rgba(78, 90, 107, 0.5)"
                              :margin-top "5px"
                              :opacity (if show-save-successful "1" "0")}
                      :id "company-settings-save-successful"}
              "Save successful")))

        (when false ; hide until Stripe/Recurly accounts are live

          (dom/div {:class "settings-form-label subscription-settings-label"}
            (dom/span {} "Account")
            (when-not company-uuid
              (loading/small-loading)))

          (when company-uuid
              (dom/div {:class "settings-form p3"}
                (subscription-info (name slug) company-uuid))))))))

(defcomponent company-settings [data owner]

  (render [_]
    (let [company-data (dis/company-data data)]

      (when (:read-only company-data)
        (router/redirect! (oc-urls/company)))

      (dom/div {:class "main-company-settings fullscreen-page"}

        (back-to-dashboard-btn {})

        (if (:loading data)

          ;; The data is still loading
          (dom/div (dom/h4 "Loading data..."))

          ;; Company profile
          (dom/div {:class "company-settings-container"}
            (om/build company-settings-form data)
            (user-management)))

        (om/build footer/footer data)))))