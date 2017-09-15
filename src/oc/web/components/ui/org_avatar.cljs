(ns oc.web.components.ui.org-avatar
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

(rum/defcs org-avatar < rum/static
                        (rum/local false ::img-load-failed)
  [s org-data should-show-link & [show-avatar-and-name]]
  (when org-data
    (let [org-slug (:slug org-data)
          has-name (not (empty? (:name org-data)))
          org-name (if has-name
                      (:name org-data)
                      (utils/camel-case-str org-slug))
          org-logo (or (:logo-url org-data) (utils/cdn "/img/ML/happy_face_red.svg"))
          img-load-failed @(::img-load-failed s)
          show-org-avatar? (and (not img-load-failed)
                                (not (clojure.string/blank? org-logo)))
          avatar-link (if should-show-link
                        (if (and (= org-slug (router/current-org-slug))
                                 (router/current-board-slug))
                          (oc-urls/board org-slug (router/current-board-slug))
                          (oc-urls/org org-slug))
                        "")]
      [:div.org-avatar
        [:a
          {:href avatar-link
           :style {:curstor (if should-show-link "pointer" "default")}
           :on-click (fn [e]
                       (.preventDefault e)
                       (when should-show-link
                         (router/redirect! avatar-link)))}
          [:div.org-avatar-container.group
            (when show-org-avatar?
              [:div.org-avatar-border
                [:span.helper]
                [:img.org-avatar-img
                  {:src org-logo
                   :title org-name
                   :on-error #(reset! (::img-load-failed s) true)}]])
            (when (or (not show-org-avatar?)
                      show-avatar-and-name)
              [:span.org-name
                {:class (when-not show-org-avatar? "no-logo")}
                org-name])]]])))