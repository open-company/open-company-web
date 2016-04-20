(ns open-company-web.components.stakeholder-update-header
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.local-settings :as ls]
            [open-company-web.api :as api]
            [open-company-web.lib.utils :as utils]
            [clojure.string :as clj-string]
            [goog.style :refer (setStyle)]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [cljs-dynamic-resources.core :as cdr]
            [cljsjs.medium-editor]))

(defn- get-title [data]
  (let [title (:title data)]
    (if (clj-string/blank? title)
      (let [js-date (utils/js-date)
            month (utils/month-string (utils/add-zero (.getMonth js-date)))
            year (.getFullYear js-date)]
        (str month " " year " Update"))
      title)))

(def before-unload-message "You have unsaved changes.")

(defn save-title-intro [owner]
  (let [title (om/get-state owner :title)
        intro-body (.-innerHTML (om/get-ref owner "intro-body"))]
    (api/patch-stakeholder-update {:title title
                                   :intro {:body intro-body}
                                   :sections (:sections (om/get-props owner))
                                   :outro (:outro (om/get-props owner))})))

(defn cancel-title-intro [owner]
  (om/set-state! owner :has-changes false)
  (let [intro-body (om/get-ref owner "intro-body")
        current-state (om/get-state owner)]
    (om/set-state! owner :title (:initial-title current-state))
    (set! (.-innerHTML intro-body) (:initial-intro current-state))
    (.focus intro-body)
    (.blur intro-body)))

(defn get-state [data current-state]
  (let [title (get-title data)
       intro (:body (:intro data))]
    {:title title
     :initial-title title
     :history-listener-id (or (:history-listener-id current-state) nil)
     :has-changes false
     :initial-intro intro
     :intro intro}))

(defcomponent stakeholder-update-header [data owner]

  (init-state [_]
    (cdr/add-style! "/css/medium-editor/medium-editor.css")
    (cdr/add-style! "/css/medium-editor/default.css")
    (get-state data nil))

  (will-receive-props [_ next-props]
    (get-state next-props (om/get-state owner)))

  (will-unmount [_]
    (when-not (utils/is-test-env?)
      ; re enable the route dispatcher
      (reset! open-company-web.core/prevent-route-dispatch false)
      ; remove the onbeforeunload handler
      (set! (.-onbeforeunload js/window) nil)
      ; remove history change listener
      (events/unlistenByKey (om/get-state owner :history-listener-id))))

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (reset! open-company-web.core/prevent-route-dispatch true)
      ; save initial innerHTML and setup MediumEditor
      (let [body-el (om/get-ref owner "intro-body")
            med-ed (new js/MediumEditor body-el (clj->js (utils/medium-editor-options "Add an introduction (optional).")))]
        (.subscribe med-ed "editableInput" (fn [event editable]
                                             (om/set-state! owner :has-changes true)))
        (om/set-state! owner :initial-body (.-innerHTML body-el))
        (om/set-state! owner :medium-editor med-ed))
      (let [win-location (.-location js/window)
            current-token (str (.-pathname win-location) (.-search win-location) (.-hash win-location))
            listener (events/listen open-company-web.core/history EventType/NAVIGATE
                       #(when-not (= (.-token %) current-token)
                          (if (om/get-state owner :has-changes)
                            (if (js/confirm (str before-unload-message " Are you sure you want to leave this page?"))
                              ; dispatch the current url
                              (open-company-web.core/route-dispatch! (router/get-token))
                              ; go back to the previous token
                              (.setToken open-company-web.core/history current-token))
                            ; dispatch the current url
                            (open-company-web.core/route-dispatch! (router/get-token)))))]
        (om/set-state! owner :history-listener-id listener))))

  (render-state [_ {:keys [title intro has-changes]}]
    (dom/div {:class "update-header"}
      (when has-changes
        (dom/div {}
          (dom/button {:class "cancel"
                       :on-click #(cancel-title-intro owner)} "cancel")
          (dom/button {:class "save"
                       :on-click #(save-title-intro owner)} "Save")))
      (dom/div {:class "update-header-internal"}
        (dom/div {:class "update-title"}
          (dom/input {:class "update-title-input"
                      :value title
                      :max-length 100
                      :on-change (fn [e]
                                  (om/set-state! owner :has-changes true)
                                  (om/set-state! owner :title (.. e -target -value)))}))
        (dom/div #js {:className "intro-body"
                      :ref "intro-body"
                      :dangerouslySetInnerHTML (clj->js {"__html" intro})})
        (dom/hr {:class "divider"})))))