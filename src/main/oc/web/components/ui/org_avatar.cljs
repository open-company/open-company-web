(ns oc.web.components.ui.org-avatar
  (:require [rum.core :as rum]
            [clojure.string :as string]
            [oc.web.urls :as oc-urls]
            [oc.web.local-settings :as ls]
            [oc.web.images :as img]
            [oc.web.dispatcher :as dis]
            [oc.web.utils.theme :as theme-utils]
            [oc.web.utils.org :as org-utils]
            [oc.web.lib.utils :as utils]))

(def default-max-logo-height 96) ;; 32 * 3 for retina

(rum/defcs internal-org-avatar < rum/static
  (rum/local false ::img-load-failed)
  {:did-mount (fn [s]
                (js/console.log "DBG internal-org-avatar")
                 (let [brand-color (-> s :rum/args first :brand-color)
                       theme-key (theme-utils/computed-value (get-in @dis/app-state dis/theme-key))
                       primary-color-map (or (get-in brand-color [theme-key :primary])
                                             (get-in ls/default-brand-color [theme-key :primary]))
                       secondary-color-map (or (get-in brand-color [theme-key :secondary])
                                               (get-in ls/default-brand-color [theme-key :secondary]))
                       org-primary-color (org-utils/css-color primary-color-map)
                       org-secondary-color (org-utils/css-color secondary-color-map)]
                   (.. (rum/dom-node s) -style (setProperty "--primary-color" org-primary-color))
                   (.. (rum/dom-node s) -style (setProperty "--secondary-color" (or org-secondary-color (get-in ls/default-brand-color [theme-key :secondary])))))
                 s)}
  [s org-data show-org-avatar? show-org-name?]
  [:div.org-avatar-container.group
    {:class (utils/class-set {:no-avatar (or (not show-org-avatar?)
                                             @(::img-load-failed s))})
     :data-first-letter (first (:name org-data))}
    (when show-org-avatar?
      [:img.org-avatar-img
       {:src (-> org-data :logo-url (img/optimize-org-avatar-url default-max-logo-height))
        :on-error #(reset! (::img-load-failed s) true)}])
    (when show-org-name?
      [:span.org-name
        {:dangerouslySetInnerHTML (utils/emojify (:name org-data))}])])

(rum/defcs org-avatar
  "Org avatar component, params:
   - should-show-link: add anchor tag around the avatar linked to the company page
   - show-org-name: possible values:
       * :always
       * :never
       * :auto (default)
      auto means that it's shown if the org logo is empty."
  < rum/static
  [s org-data should-show-link & [show-org-name]]
  (let [org-logo (:logo-url org-data)]
    [:div.org-avatar
      {:class (when (string/blank? org-logo) "missing-logo")}
      (when org-data
        (let [org-slug (:slug org-data)
              has-name (seq (:name org-data))
              org-name (if has-name
                          (:name org-data)
                          (utils/camel-case-str org-slug))
              show-org-avatar? (not (string/blank? org-logo))
              show-org-name? (case show-org-name
                               :always
                               true
                               :never
                               false
                               ;; else
                               (not show-org-avatar?))
              avatar-link (when should-show-link
                            (oc-urls/default-landing org-slug))]
          (if should-show-link
            [:a.org-link
              {:href avatar-link}
              (internal-org-avatar org-data show-org-avatar? show-org-name?)]
            (internal-org-avatar org-data show-org-avatar? show-org-name?))))]))
