(ns oc.web.components.ui.org-avatar
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

(defn internal-org-avatar
  [s org-data show-org-avatar? show-org-name?]
  [:div.org-avatar-container.group
    (when show-org-avatar?
      [:img.org-avatar-img
        {:src (:logo-url org-data)
         :on-error #(reset! (::img-load-failed s) true)}])
    (when show-org-name?
      [:span.org-name
        {:class (when-not show-org-avatar? "no-logo")
         :dangerouslySetInnerHTML (utils/emojify (:name org-data))}])])

(rum/defcs org-avatar < rum/static
                        (rum/local false ::img-load-failed)
  [s org-data should-show-link & [hide-name]]
  (let [org-logo (:logo-url org-data)]
    [:div.org-avatar
      {:class (when (empty? org-logo) "missing-logo")}
      (when org-data
        (let [org-slug (:slug org-data)
              has-name (not (empty? (:name org-data)))
              org-name (if has-name
                          (:name org-data)
                          (utils/camel-case-str org-slug))
              img-load-failed @(::img-load-failed s)
              show-org-avatar? (and (not img-load-failed)
                                    (not (clojure.string/blank? org-logo))
                                    (pos? (:logo-height org-data))
                                    (pos? (:logo-width org-data)))
              avatar-link (if should-show-link
                            (if (and (= org-slug (router/current-org-slug))
                                     (router/current-board-slug))
                              (oc-urls/board org-slug (router/current-board-slug))
                              (oc-urls/org org-slug))
                            "")]
          (if should-show-link
            [:a
              {:href avatar-link
               :on-click (fn [e]
                           (.preventDefault e)
                           (when should-show-link
                             (router/redirect! avatar-link)))}
              (internal-org-avatar s org-data show-org-avatar? (not hide-name))]
            (internal-org-avatar s org-data show-org-avatar? (not hide-name)))))]))