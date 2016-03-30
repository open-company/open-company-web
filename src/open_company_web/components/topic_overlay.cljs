(ns open-company-web.components.topic-overlay
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel sel1)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [open-company-web.caches :as cache]
            [open-company-web.api :as api]
            [open-company-web.components.growth.topic-growth :refer (topic-growth)]
            [open-company-web.components.finances.topic-finances :refer (topic-finances)]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [goog.fx.dom :refer (Fade)]
            [cljs-dynamic-resources.core :as cdr]
            [cljsjs.medium-editor]))

(defonce max-win-height 670)
(defonce overlay-top-margin 100)

(defn pencil-click [options topic e focus-field]
  (.stopPropagation e)
  ; remove the overlay
  ((:edit-topic-cb options) focus-field))

(defn circle-remove-click [options e]
  (.stopPropagation e)
  ((:close-overlay-cb options)))

(defcomponent topic-overlay-internal [{:keys [read-only as-of topic topic-data prev-rev next-rev currency selected-metric] :as data} owner options]

  (render [_]
    (let [topic-kw (keyword topic)
          js-date-upat (utils/js-date (:updated-at topic-data))
          month-string (utils/month-string-int (inc (.getMonth js-date-upat)))
          topic-updated-at (str month-string " " (.getDate js-date-upat))
          subtitle-string (str (:name (:author topic-data)) " on " topic-updated-at)
          section-body (utils/get-topic-body topic-data topic-kw)
          win-height (.-clientHeight (.-body js/document))
          needs-fix? (< win-height max-win-height)
          max-height (min (- 650 126) (- win-height 126))]
      (dom/div {:class "topic-overlay-internal"
                :on-click #(.stopPropagation %)}
        (dom/button {:class "circle-remove"
                     :on-click #(circle-remove-click options %)})
        (when-not read-only
          (dom/button {:class "pencil"
                       :on-click #(pencil-click options topic % "title")}))
        (dom/div {:class "topic-overlay-header"}
          (dom/div {:class "topic-overlay-title"
                    :on-click #(when-not read-only
                                (pencil-click options topic % "title"))} (:title topic-data))
          (dom/div {:class "topic-overlay-date"} subtitle-string))
        (dom/div #js {:className "topic-overlay-content"
                      :ref "topic-overlay-content"
                      :style #js {:maxHeight (str max-height "px")}}
          (dom/div {:class "topic-overlay-headline"
                    :on-click #(when-not read-only
                                (pencil-click options topic % "headline"))} (:headline topic-data))
          (dom/div {:on-click #(when-not read-only
                                 (pencil-click options topic % "body"))}
            (when (= topic "finances")
              (om/build topic-finances {:section-data topic-data
                                        :section topic-kw
                                        :currency currency
                                        :selected-metric selected-metric
                                        :read-only true}
                                       {:opts {:show-title false
                                               :show-revisions-navigation false
                                               :chart-size {:height (if (utils/is-mobile) 200 290)
                                                            :width (if (utils/is-mobile) 320 480)}}}))
            (when (= topic "growth")
              (om/build topic-growth   {:section-data topic-data
                                        :section topic-kw
                                        :currency currency
                                        :selected-metric selected-metric
                                        :read-only true}
                                       {:opts {:show-title false
                                               :show-revisions-navigation false
                                               :chart-size {:height (if (utils/is-mobile) 200 290)
                                                            :width (if (utils/is-mobile) 320 480)}}})))
          (dom/div {:class "topic-overlay-body"
                    :on-click #(when-not read-only
                                (pencil-click options topic % "body"))
                    :dangerouslySetInnerHTML (clj->js {"__html" section-body})})
          (dom/div {:class "topic-overlay-navigation topic-navigation group"}
            (when prev-rev
              (dom/div {:class "arrow previous"}
                (dom/button {:on-click (fn [e]
                                    (let [bt (.-target e)]
                                      (set! (.-disabled bt) "disabled")
                                      (.stopPropagation e)
                                      ((:prev-cb options) (:updated-at prev-rev))
                                      (.setTimeout js/window
                                        #(set! (.-disabled bt) false) 1000)))} "< Previous")))
            (when next-rev
              (dom/div {:class "arrow next"}
                (dom/button {:on-click (fn [e]
                                        (let [bt (.-target e)]
                                          (set! (.-disabled bt) "disabled")
                                          (.stopPropagation e)
                                          ((:next-cb options) (:updated-at next-rev))
                                          (.setTimeout js/window
                                            #(set! (.-disabled bt) false) 1000)))} "Next >")))))
        (dom/div {:class "gradient"})))))

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

(defn animate-topic-overlay [owner show]
  (when-not (utils/is-test-env?)
    (when-let [topic-overlay (om/get-ref owner "topic-overlay")]
      (.play
        (new Fade
             topic-overlay
             (if show 0 1)
             (if show 1 0)
             utils/oc-animation-duration)))))

(defn close-overlay [owner options]
  (animate-topic-overlay owner false)
  (.setTimeout js/window
    #((:close-overlay-cb options)) utils/oc-animation-duration))

(defn start-editing [owner focus]
  (om/set-state! owner :focus focus)
  (om/set-state! owner :editing true))

(defcomponent topic-overlay [{:keys [section section-data currency selected-metric force-editing] :as data} owner options]

  (init-state [_]
    {:as-of (:updated-at section-data)
     :focus nil
     :editing force-editing})

  (did-mount [_]
    (animate-topic-overlay owner true)
    ; prevent the window from scrolling
    (dommy/add-class! (sel1 [:body]) "no-scroll"))

  (did-update [_ old-props _]
    (when-not (= old-props data)
      (om/set-state! owner :as-of (:updated-at section-data))))

  (will-unmount [_]
    ; let the window scroll
    (dommy/remove-class! (sel1 [:body]) "no-scroll"))

  (render-state [_ {:keys [as-of editing focus]}]
    (let [section-kw (keyword section)
          revisions (utils/sort-revisions (:revisions section-data))
          slug (keyword (:slug @router/path))
          revisions-list (section-kw (@cache/revisions slug))
          topic-data (utils/select-section-data section-data section-kw as-of)
          prev-rev (utils/revision-prev revisions as-of)
          next-rev (utils/revision-next revisions as-of)
          actual-as-of (:updated-at section-data)
          win-height (.-clientHeight (.-body js/document))
          content-max-height (if (< win-height max-win-height)
                               (- win-height 20)
                               (- max-win-height 20))
          needs-fix? (< win-height (+ content-max-height (* overlay-top-margin 2)))
          calc-top-margin (+ (/ (- content-max-height (min win-height max-win-height)) 2) 10)
          top-margin (if needs-fix?
                       (max 10 calc-top-margin)
                       100)
          max-height (if needs-fix?
                       (- win-height 20)
                       650)]
      ; preload previous revision
      (when (and prev-rev (not (contains? revisions-list (:updated-at prev-rev))))
        (api/load-revision prev-rev slug section-kw))
      ; preload next revision as it can be that it's missing (ie: user jumped to the first rev then went forward)
      (when (and (not= (:updated-at next-rev) actual-as-of)
                  next-rev
                  (not (contains? revisions-list (:updated-at next-rev))))
        (api/load-revision next-rev slug section-kw))
      (dom/div #js {:className "topic-overlay"
                    :ref "topic-overlay"
                    :onClick #(when-not editing
                                (close-overlay owner options))
                    :key (name section)}
        (dom/div #js {:className "topic-overlay-box"
                      :ref "topic-overlay-box"
                      :style #js {:marginTop (str top-margin "px")
                                  :maxHeight (str max-height "px")}
                      :on-click #(.stopPropagation %)}
          (if-not editing
            (om/build topic-overlay-internal {:topic-data topic-data
                                              :as-of as-of
                                              :topic section
                                              :currency currency
                                              :selected-metric selected-metric
                                              :read-only (or (:read-only section-data) (not= as-of (:updated-at section-data)))
                                              :prev-rev prev-rev
                                              :next-rev next-rev}
                                             {:opts {:close-overlay-cb #(close-overlay owner options)
                                                     :edit-topic-cb #(start-editing owner %)
                                                     :prev-cb #(om/set-state! owner :as-of %)
                                                     :next-cb #(om/set-state! owner :as-of %)}})
            (om/build topic-overlay-edit {:topic-data section-data
                                          :topic section
                                          :focus focus
                                          :currency currency}
                                         {:opts {:dismiss-editing-cb #(om/set-state! owner :editing false)}})))))))