(ns open-company-web.components.edit-user-profile
  (:require [dommy.core :refer-macros (sel1)]
            [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.ui.footer :refer (footer)]
            [open-company-web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]
            [open-company-web.components.ui.user-avatar :refer (user-avatar-image)]
            [goog.object :as googobj]
            [goog.dom :as gdom]))

(defn- img-on-load [owner img]
  (om/set-state! owner :has-changes true)
  (dis/dispatch! [:foce-input {:image-width (.-clientWidth img)
                               :image-height (.-clientHeight img)}])
  (gdom/removeNode img))

(defn- upload-file! [owner file]
  (let [success-cb  (fn [success]
                      (let [url    (googobj/get success "url")
                            node   (gdom/createDom "img")]
                        (if-not url
                          (js/alert "An error has occurred while processing the image URL. Please try again.")
                          (do
                            (set! (.-onload node) #(img-on-load owner node))
                            (gdom/append (.-body js/document) node)
                            (set! (.-src node) url)))
                        (dis/dispatch! [:input [:edit-user-profile :avatar-url] url])
                        (om/set-state! owner (merge (om/get-state owner) {:file-upload-state nil
                                                                          :file-upload-progress nil
                                                                          :has-changes true}))))
        error-cb    (fn [error] (js/alert "An error has occurred while processing the image URL. Please try again.")
                                (om/set-state! owner (merge (om/get-state owner) {:file-upload-state nil
                                                                                  :file-upload-progress nil})))
        progress-cb (fn [progress]
                      (let [state (om/get-state owner)]
                        (om/set-state! owner (merge state {:file-upload-state :show-progress
                                                           :file-upload-progress progress}))))]
    (cond
      (and (string? file) (not (clojure.string/blank? file)))
      (js/filepicker.storeUrl file success-cb error-cb progress-cb)
      file
      (js/filepicker.store file #js {:name (.-name file)} success-cb error-cb progress-cb))))

(defn change! [owner k v]
  (dis/dispatch! [:input [:edit-user-profile k] v])
  (om/set-state! owner :has-changes true))

(defn reset-user-profile-data [owner e]
  (.preventDefault e)
  (dis/dispatch! [:reset-user-profile])
  (om/set-state! owner {:has-changes false :file-upload-progress nil :file-upload-state nil :upload-remote-url ""}))

(defn save-user-profile-data [owner e]
  (.preventDefault e)
  (dis/dispatch! [:save-user-profile]))

(defcomponent edit-user-profile [data owner]

  (init-state [_]
    (dis/dispatch! [:reset-user-profile])
    {:file-upload-state nil
     :upload-remote-url ""
     :file-upload-progress nil
     :password-did-change false
     :has-changes false})

  (did-mount [_]
    (dis/dispatch! [:get-current-user]))

  (did-update [_ _ _]
    (when-not (utils/is-test-env?)
      (js/filepicker.setKey ls/filestack-key)))

  (render-state [_ {:keys [file-upload-progress file-upload-state upload-remote-url has-changes]}]
    (let [columns-num (responsive/columns-num)
          card-width (responsive/calc-card-width)]
      (dom/div {:class "edit-user-profile fullscreen-page"}
        (back-to-dashboard-btn {:title "User Profile"})
        (dom/div {:class "edit-user-profile-internal mx-auto my4"}
          (dom/form {:class ""}
            (dom/div {:class "edit-user-profile-content group"}
              (dom/div {}
                (dom/div {:class "left-column"}
                  (dom/div {:class "edit-user-profile-title data-title"} "FIRST NAME")
                  (dom/input {:class "edit-user-profile"
                              :name "first-name"
                              :on-change #(change! owner :first-name (.. % -target -value))
                              :value (or (:first-name (:edit-user-profile data)) "")})
                  (dom/div {:class "edit-user-profile-title data-title"} "LAST NAME")
                  (dom/input {:class "edit-user-profile"
                              :name "last-name"
                              :on-change #(change! owner :last-name (.. % -target -value))
                              :value (or (:last-name (:edit-user-profile data)) "")})
                  (dom/div {:class "edit-user-profile-title data-title"} "PASSWORD")
                  (dom/input {:class "edit-user-profile"
                              :name "password"
                              :on-change #(do
                                            (change! owner :password (.. % -target -value))
                                            (om/set-state! :password-did-change true))
                              :type "password"
                              :value (or (:password (:edit-user-profile data)) "********")})
                  (dom/div {:class "edit-user-profile-title data-title"} "EMAIL")
                  (dom/input {:class "edit-user-profile"
                              :name "email"
                              :on-change #(change! owner :email (.. % -target -value))
                              :value (or (:email (:edit-user-profile data)) "")}))
                (dom/div {:class "right-column"}
                  (dom/div {:class "edit-user-profile-title data-title"} "AVATAR")
                  (user-avatar-image (:avatar-url (:edit-user-profile data)))
                  (dom/button {:class "btn-reset camera left"
                               :title "Add a profile image"
                               :type "button"
                               :data-toggle "tooltip"
                               :data-container "body"
                               :data-placement "top"
                               :style {:display (if (nil? file-upload-state) "block" "none")}
                               :on-click #(.click (sel1 [:input#foce-file-upload-ui--select-trigger]))}
                      (dom/i {:class "fa fa-camera"}))
                  (dom/input {:id "foce-file-upload-ui--select-trigger"
                              :style {:display "none"}
                              :type "file"
                              :on-change #(upload-file! owner (-> % .-target .-files (aget 0)))})
                  (when (= file-upload-state :show-progress)
                    (dom/div {:class "uploading-progress"} (str "Uploading " file-upload-progress "%")))
                  (dom/div {:class (str "upload-remote-url-container left" (when-not (= file-upload-state :show-url-field) " hidden"))
                            :style {:display (if file-upload-state "block" "none")}}
                    (dom/input {:type "text"
                                :class "upload-remote-url-field"
                                :style {:width (str (- card-width 122 50) "px")}
                                :on-change #(om/set-state! owner :upload-remote-url (or (-> % .-target .-value) ""))
                                :placeholder "http://site.com/img.png"
                                :value (or upload-remote-url "")})
                    (dom/button {:style {:font-size "14px" :margin-left "5px" :padding "0.3rem"}
                                 :class "btn-reset btn-solid"
                                 :disabled (clojure.string/blank? upload-remote-url)
                                 :on-click #(upload-file! owner (om/get-state owner :upload-remote-url))}
                      "add")
                    (dom/button {:style {:font-size "14px" :margin-left "5px" :padding "0.3rem"}
                                 :class "btn-reset btn-outline"
                                 :on-click #(om/set-state! owner :file-upload-state nil)}
                      "cancel"))))
              (dom/div {:class "right mt2"}
                (dom/button {:class "btn-reset btn-outline mr2"
                             :on-click #(reset-user-profile-data owner %)} "CANCEL")
                (dom/button {:class "btn-reset btn-solid"
                             :disabled (not has-changes)
                             :on-click #(save-user-profile-data owner %)} "SAVE")))))
        (om/build footer (assoc data :footer-width (responsive/total-layout-width-int card-width columns-num)))))))