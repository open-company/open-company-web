(ns oc.web.components.org-editor
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.oc-colors :as occ]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.navbar :refer (navbar)]
            [oc.web.components.ui.small-loading :as loading]
            [oc.web.components.ui.alert-modal :as alert-modal]))

(defn- create-org-alert []
  (let [alert-data {:icon "/img/ML/error_icon.png"
                    :action "create-org-alert"
                    :title "Please enter a company name."
                    :message ""
                    :solid-button-title "OK"
                    :solid-button-cb #(alert-modal/hide-alert)}]
    (alert-modal/show-alert alert-data)))

(defn create-org-clicked [s e]
  (utils/event-stop e)
  (when-not @(::loading s) ;(om/get-state owner :loading)
    (let [org-name (:name @(drv/get-ref s :org-editing))]
      (if (clojure.string/blank? org-name)
        (create-org-alert)
        (do
          (reset! (::loading s) true)
          (dis/dispatch! [:org-create]))))))

(defn setup-org-name [s]
  (when-let [team-data (first (:teams @(drv/get-ref s :teams-data)))]
    ;; using utils/after here because we can't dispatch inside another dispatch.
    ;; ultimately we should switch to some event-loop impl that works like a proper queue
    ;; and does not have these limitations
    (utils/after 1 #(dis/dispatch!
                     [:input
                      [:org-editing]
                      {:name (or (:name team-data) "")
                       :logo-url (or (:logo-url team-data) "")}]))
    (when (seq (:name team-data))
      (reset! (::message s) "Is this the organization name you’d like to use?"))))

(rum/defcs org-editor < (drv/drv :org-editing)
                        (drv/drv :teams-data)
                        (rum/local false ::loading)
                        (rum/local "What's the name of your organization?" ::message)
                        (rum/local false ::name-did-change)
                        {:did-mount (fn [s]
                          (when-not @(drv/get-ref s :org-editing)
                            (setup-org-name s))
                          s)
                         :did-remount (fn [_ s]
                          (when-not @(::name-did-change s)
                            (setup-org-name s))
                          s)}
  [s]
  (let [org-editing (drv/react s :org-editing)]
    [:div.org-editor
      [:div.fullscreen-page
        (navbar true)
        [:div.org-editor-container
          [:div.org-editor-box
            [:form
              {:on-submit (partial create-org-clicked s)}
              [:div.form-group
                (when (and (jwt/jwt) (jwt/get-key :first-name))
                  [:label.org-editor-message
                    (str "Hi " (string/capital (jwt/get-key :first-name)) "!")])
                [:label.org-editor-message
                  @(::message s)]
                [:input.org-editor-input.h4
                  {:type "text"
                   :style #js {:width "100%"}
                   :placeholder "Simple name without the Inc., LLC, etc."
                   :value (or (:name org-editing) "")
                   :on-change #(let [v (.. % -target -value)]
                                 (reset! (::name-did-change s) true)
                                 (dis/dispatch! [:input [:org-editing :name] v]))}]]]
            [:div.center
              [:button.mlb-reset.mlb-default
                {:disabled (not (pos? (count (:name org-editing))))
                 :on-click (partial create-org-clicked s)}]]]]]]))