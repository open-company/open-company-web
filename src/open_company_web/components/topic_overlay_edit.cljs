(ns open-company-web.components.topic-overlay-edit
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [open-company-web.api :as api]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [cljs-dynamic-resources.core :as cdr]
            [cljsjs.medium-editor]))

(defn change-value [owner k e]
  (let [target (.-target e)
        value (.-value target)]
    (om/set-state! owner :has-changes true)
    (om/set-state! owner k value)))

(def medium-editor-options {
  :toolbar {
    :buttons #js ["bold" "italic" "underline" "strikethrough" "h2" "orderedlist" "unorderedlist"]
  }})

(defn focus-field [topic field]
  (let [topic-field (.getElementById js/document (str "topic-edit-" field "-" (name topic)))
        field-value (.-value topic-field)]
    (.focus topic-field)
    (when (or (= field "headline") (= field "title"))
      (set! (.-value topic-field) field-value))))

(def before-unload-message "You have unsaved changes to the topic.")

(defcomponent topic-overlay-edit [{:keys [topic topic-data currency focus] :as data} owner options]

  (init-state [_]
    (cdr/add-style! "/css/medium-editor/medium-editor.css")
    (cdr/add-style! "/css/medium-editor/beagle.css")
    {:has-changes false
     :title (:title topic-data)
     :headline (:headline topic-data)
     :body (utils/get-topic-body topic-data topic)
     :medium-editor nil
     :history-listener-id nil})

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
      (let [body-el (om/get-ref owner "topic-overlay-edit-body")
            med-ed (new js/MediumEditor body-el (clj->js medium-editor-options))]
        (.subscribe med-ed "editableInput" (fn [event editable]
                                             (om/set-state! owner :has-changes true)))
        (om/set-state! owner :initial-body (.-innerHTML body-el))
        (om/set-state! owner :medium-editor med-ed))
      (when focus
        (focus-field topic focus))
      (let [win-location (.-location js/window)
            current-token (str (.-pathname win-location) (.-search win-location) (.-hash win-location))
            listener (events/listen open-company-web.core/history EventType/NAVIGATE
                       #(when-not (= (.-token %) current-token)
                          (if (js/confirm (str before-unload-message " Are you sure you want to leave this page?"))
                            ; dispatch the current url
                            (open-company-web.core/route-dispatch! (router/get-token))
                            ; go back to the previous token
                            (.setToken open-company-web.core/history current-token))
                          (.log js/console %)))]
        (om/set-state! owner :history-listener-id listener))))

  (render-state [_ {:keys [has-changes title headline body]}]
    (let [topic-kw (keyword topic)
          js-date-upat (utils/js-date (:updated-at topic-data))
          month-string (utils/month-string-int (inc (.getMonth js-date-upat)))
          topic-updated-at (str month-string " " (.getDate js-date-upat))
          subtitle-string (str (:name (:author topic-data)) " on " topic-updated-at)
          section-body (utils/get-topic-body topic-data topic-kw)
          win-height (.-clientHeight (.-body js/document))
          needs-fix? (< win-height max-win-height)
          max-height (- win-height 126)]
      ; set the onbeforeunload handler only if there are changes
      (let [onbeforeunload-cb (when has-changes #(str before-unload-message))]
        (set! (.-onbeforeunload js/window) onbeforeunload-cb))
      (dom/div {:class "topic-overlay-edit"
                :on-click #(.stopPropagation %)}
        (when has-changes
          (dom/button {:class "save-topic"
                       :on-click #(let [section-data {:title (om/get-state owner :title)
                                                      :headline (om/get-state owner :headline)
                                                      :body (.-innerHTML (om/get-ref owner "topic-overlay-edit-body"))}]
                                    (.stopPropagation %)
                                    (api/partial-update-section topic section-data)
                                    ((:dismiss-editing-cb options)))} "Save Topic"))
        (dom/button {:class "cancel"
                     :on-click #((:dismiss-editing-cb options))} "Cancel")
        (dom/div {:class "topic-overlay-edit-header"}
          (dom/input {:class "topic-overlay-edit-title"
                      :id (str "topic-edit-title-" (name topic))
                      :type "text"
                      :placeholder "Type your title here"
                      :max-length 100
                      :value title
                      :on-change #(change-value owner :headline %)})
          (dom/div {:class "topic-overlay-date"} subtitle-string))
        (dom/div #js {:className "topic-overlay-edit-content"
                      :ref "topic-overlay-edit-content"
                      :style #js {:maxHeight (str max-height "px")}}
          (dom/div {}
            (dom/div {:class "topic-overlay-edit-headline-count"}
              (dom/label {:class "bold"} (- 100 (count headline))) "/100"))
          (dom/input {:class "topic-overlay-edit-headline"
                      :id (str "topic-edit-headline-" (name topic))
                      :type "text"
                      :placeholder "Type your headline here"
                      :max-length 100
                      :value headline
                      :on-change #(change-value owner :headline %)})
          (dom/div #js {:className "topic-overlay-edit-body"
                        :ref "topic-overlay-edit-body"
                        :id (str "topic-edit-body-" (name topic))
                        :dangerouslySetInnerHTML (clj->js {"__html" section-body})}))
        (dom/div {:class "gradient"})))))