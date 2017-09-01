(ns oc.web.components.ui.org-settings-main-panel
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.image-upload :as iu]
            [goog.object :as gobj]
            [goog.dom :as gdom]))

(defn logo-on-load [org-data url img]
  (dis/dispatch! [:input [:org-editing] (merge org-data {:has-changes true
                                                         :logo-url url
                                                         :logo-width (.-width img)
                                                         :logo-height (.-height img)})])
  (gdom/removeNode img))

(defn logo-add-error
  "Show an error alert view for failed uploads."
  []
  (let [alert-data {:icon "/img/ML/error_icon.png"
                    :title "Sorry!"
                    :message "An error occurred with your image."
                    :solid-button-title "OK"
                    :solid-button-cb #(dis/dispatch! [:alert-modal-hide])}]
    (dis/dispatch! [:alert-modal-show alert-data])))

(rum/defc org-settings-main-panel
  < rum/static
    {:after-render (fn [s]
                     (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
                     s)}
  [org-data]
  [:div.org-settings-panel
    ;; Panel rows
    [:div.org-settings-main
      ;; Org name row
      [:div.org-settings-panel-row.group
        [:div.org-settings-label
          [:label "Team Name"]
          (when false
            [:label.error "Name can't be empty"])]
        [:div.org-settings-field
          [:input
            {:type "text"
             :value (:name org-data)
             :on-change (fn [e]
                          (dis/dispatch! [:input [:org-editing] (merge org-data {:name (.. e -target -value)
                                                                                 :has-changes true})]))}]]]
      ;; Org logo row
      [:div.org-settings-panel-row.org-logo-row.group
        {:on-click (fn [_]
                    (iu/upload! {:accept "image/*"
                                 :transformations {
                                   :crop {
                                     :aspectRatio 1}}}
                    (fn [res]
                      (let [url (gobj/get res "url")
                            img (gdom/createDom "img")]
                        (set! (.-onload img) #(logo-on-load org-data url img))
                        (set! (.-className img) "hidden")
                        (gdom/append (.-body js/document) img)
                        (set! (.-src img) url)))
                    nil
                    (fn [err]
                      (logo-add-error))))}
        [:div.org-logo-container
          {:title (if (empty? (:logo-url org-data))
                    "Add a logo"
                    "Change logo")
           :data-toggle "tooltip"
           :data-container "body"
           :data-position "top"
           :class (when (empty? (:logo-url org-data)) "no-logo")}
          [:img.org-logo
            {:src (if (not (empty? (:logo-url org-data)))
                    (:logo-url org-data)
                    (utils/cdn "/img/ML/carrot_grey.svg"))}]]
        [:div.org-logo-label
          [:div.cta "Change logo"]
          [:div.description "A 160x160 transparent PNG works best"]]]]
    ;; Save and cancel buttons
    [:div.org-settings-footer.group
      [:button.mlb-reset.mlb-default.save-btn
        {:disabled (not (:has-changes org-data))
         :on-click #(dis/dispatch! [:org-edit-save])}
        "Save"]
      [:button.mlb-reset.mlb-link-black.cancel-btn
        {:on-click #(dis/dispatch! [:org-edit (dis/org-data) false])}
        "Cancel"]]])