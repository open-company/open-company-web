(ns oc.web.components.ui.org-avatar
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.images :as img]
            [oc.web.lib.utils :as utils]))

(def default-max-logo-height 96) ;; 32 * 3 for retina

(defn internal-org-avatar
  [s org-data show-org-avatar? show-org-name?]
  [:div.org-avatar-container.group
    {:class (utils/class-set {:no-avatar (not show-org-avatar?)})
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
    (rum/local false ::img-load-failed)
  [s org-data should-show-link & [show-org-name]]
  (let [org-logo (:logo-url org-data)]
    [:div.org-avatar
      {:class (when (empty? org-logo) "missing-logo")}
      (when org-data
        (let [org-slug (:slug org-data)
              has-name (seq (:name org-data))
              org-name (if has-name
                          (:name org-data)
                          (utils/camel-case-str org-slug))
              img-load-failed @(::img-load-failed s)
              show-org-avatar? (and (not img-load-failed)
                                    (not (clojure.string/blank? org-logo))
                                    (pos? (:logo-height org-data))
                                    (pos? (:logo-width org-data)))
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
              (internal-org-avatar s org-data show-org-avatar? show-org-name?)]
            (internal-org-avatar s org-data show-org-avatar? show-org-name?))))]))
