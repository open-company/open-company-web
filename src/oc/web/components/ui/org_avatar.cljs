(ns oc.web.components.ui.org-avatar
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

(def default-max-logo-height 42)

(defn internal-org-avatar
  [s org-data show-org-avatar? show-org-name? force-label]
  [:div.org-avatar-container.group
    {:class (utils/class-set {:no-avatar (not show-org-avatar?)
                              :force-label force-label})}
    (when show-org-avatar?
      [:img.org-avatar-img
        {:src (:logo-url org-data)
         :style #js {:height (str (:logo-height org-data) "px")
                     :marginTop (when (< (:logo-height org-data) default-max-logo-height)
                                 (str (/ (- default-max-logo-height (:logo-height org-data)) 2) "px"))}
         :on-error #(reset! (::img-load-failed s) true)}])
    (when show-org-name?
      [:span.org-name
        {:class (when-not show-org-avatar? "no-logo")
         :dangerouslySetInnerHTML (utils/emojify (:name org-data))}])])

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
  [s org-data should-show-link & [show-org-name force-label]]
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
                            (if (and (= org-slug (router/current-org-slug))
                                     (router/current-board-slug))
                              (oc-urls/board org-slug (router/current-board-slug))
                              (oc-urls/org org-slug)))]
          (if should-show-link
            [:a.org-link
              {:href avatar-link
               :on-click (fn [e]
                           (.preventDefault e)
                           (when should-show-link
                             (router/redirect! avatar-link)))}
              (internal-org-avatar s org-data show-org-avatar? show-org-name? force-label)]
            (internal-org-avatar s org-data show-org-avatar? show-org-name? force-label))))]))