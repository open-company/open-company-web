(ns oc.web.components.topic-edit
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.local-settings :as ls]
            [oc.web.lib.utils :as utils]
            ; [oc.web.lib.tooltip :as t]
            [oc.web.lib.oc-colors :as oc-colors]
            [oc.web.lib.image-upload :as iu]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.medium-editor-exts :as editor]
            [oc.web.lib.prevent-route-dispatch :refer (prevent-route-dispatch)]
            [oc.web.lib.growth-utils :as growth-utils]
            [oc.web.components.chart :refer (chart)]
            [oc.web.components.topic-attachments :refer (topic-attachments)]
            [oc.web.components.growth.topic-growth :refer (topic-growth)]
            [oc.web.components.finances.topic-finances :refer (topic-finances)]
            [oc.web.components.ui.icon :as i]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.ui.popover :refer (add-popover-with-rum-component add-popover hide-popover)]
            [oc.web.components.ui.collect-chart-popover :refer (collect-chart-popover)]
            [cljsjs.medium-editor] ; pulled in for cljsjs externs
            [goog.dom :as gdom]
            [goog.object :as googobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.history.EventType :as HistoryEventType]
            [clojure.string :as string]))

(def title-max-length 20)
(def headline-max-length 100)
(def title-alert-limit 3)
(def headline-alert-limit 10)

(def before-unload-message "You have unsaved edits. Are you sure you want to leave this topic?")

(defn focus-body []
  (when-let* [topic-kw (dis/foce-topic-key)
              body (sel1 [(str "div#foce-body-" (name topic-kw))])]
    (.focus body)
    (utils/to-end-of-content-editable body)))

(defn- headline-on-change [owner]
  (when-let* [topic-kw     (dis/foce-topic-key)
              headline       (sel1 (str "div#foce-headline-" (name topic-kw)))]
    (let [headline-innerHTML (.-innerHTML headline)
          emojied-headline   (utils/emoji-images-to-unicode (googobj/get (utils/emojify (.-innerHTML headline)) "__html"))
          remaining-chars    (- headline-max-length (count (.-innerText headline)))]
      (dis/dispatch! [:foce-input {:headline emojied-headline}])
      (om/update-state! owner #(merge % {:char-count remaining-chars
                                         :char-count-alert (< remaining-chars headline-alert-limit)
                                         :headline-exceeds (neg? remaining-chars)
                                         :has-changes true})))))

(defn- check-headline-count [owner e has-changes]
  (when-let* [topic-kw   (dis/foce-topic-key)
              headline (sel1 (str "div#foce-headline-" (name topic-kw)))]
    (let [headline-value (.-innerText headline)]
      (when (and e
                 (not= (.-keyCode e) 8)
                 (not= (.-keyCode e) 16)
                 (not= (.-keyCode e) 17)
                 (not= (.-keyCode e) 40)
                 (not= (.-keyCode e) 38)
                 (not= (.-keyCode e) 13)
                 (not= (.-keyCode e) 27)
                 (not= (.-keyCode e) 37)
                 (not= (.-keyCode e) 39)
                 (>= (count headline-value) headline-max-length))
        (.preventDefault e))))
  (when has-changes
    (headline-on-change owner)))

(defn body-on-change [owner]
  (when-let* [topic-kw   (dis/foce-topic-key)
              topic-name (name topic-kw)
              body-el      (sel1 [(str "div#foce-body-" topic-name)])]
    ; Attach paste listener to the body and all its children
    (js/recursiveAttachPasteListener body-el (comp #(utils/medium-editor-hide-placeholder (om/get-state owner :body-editor) body-el) #(body-on-change owner)))
    (let [emojied-body (utils/emoji-images-to-unicode (googobj/get (utils/emojify (.-innerHTML body-el)) "__html"))]
      (dis/dispatch! [:foce-input {:body emojied-body}]))
    (om/update-state! owner #(merge % {:char-count nil
                                       :has-changes true}))))

(defn- setup-body-editor [owner]
  (when-let* [topic-kw   (dis/foce-topic-key)
              body-el      (sel1 [(str "div#foce-body-" (name topic-kw))])
              headline-el  (sel1 [(str "div#foce-headline-" (name topic-kw))])]
    (let [body-editor      (new js/MediumEditor body-el (clj->js (utils/medium-editor-options "" false)))]
      (.subscribe body-editor
                  "editableInput"
                  (fn [event editable]
                    (body-on-change owner)))
      (om/set-state! owner :body-editor body-editor)
      (js/recursiveAttachPasteListener body-el (comp #(utils/medium-editor-hide-placeholder body-editor body-el) #(body-on-change owner))))
    (events/listen headline-el EventType/INPUT #(headline-on-change owner))
    (js/emojiAutocomplete)))

(defn- img-on-load [owner img]
  (om/set-state! owner :has-changes true)
  (dis/dispatch! [:foce-input {:image-width (.-clientWidth img)
                               :image-height (.-clientHeight img)}])
  (gdom/removeNode img))

(defn img-upload-success-cb [owner res]
  (let [url    (googobj/get res "url")
        node   (gdom/createDom "img")]
    (if-not url
      (dis/dispatch! [:error-banner-show "An error has occurred while processing the image URL. Please try again." 5000])
      (do
        (set! (.-onload node) #(img-on-load owner node))
        (gdom/append (.-body js/document) node)
        (set! (.-src node) url)))
    (dis/dispatch! [:foce-input {:image-url url}])
    (om/set-state! owner (merge (om/get-state owner) {:file-upload-state nil
                                                      :file-upload-progress nil
                                                      :has-changes true}))))

(defn img-upload-error-cb [owner res error]
  (dis/dispatch! [:error-banner-show "An error has occurred while processing the image URL. Please try again." 5000])
  (om/update-state! owner #(merge % {:file-upload-state nil
                                     :file-upload-progress nil
                                     :has-changes true})))

(defn img-upload-progress-cb [owner res progress]
  (om/update-state! owner #(merge % {:file-upload-state :show-progress
                                     :file-upload-progress progress})))

(defn attachment-upload-success-cb [owner res]
  (let [url    (googobj/get res "url")]
    (if-not url
      (dis/dispatch! [:error-banner-show "An error has occurred while processing the file URL. Please try again." 5000])
      (let [current-foce-data (dis/foce-topic-data)
            attachments (or (:attachments current-foce-data) [])
            attachment-data {:file-name (googobj/get res "filename")
                             :file-type (googobj/get res "mimetype")
                             :file-size (googobj/get res "size")
                             :file-url url}]
        (om/set-state! owner :has-changes true)
        (dis/dispatch! [:foce-input {:attachments (conj attachments attachment-data)}])))))

(defn attachment-upload-error-cb [owner res error]
  (dis/dispatch! [:error-banner-show "An error has occurred while processing the file. Please try again." 5000]))

(defn- dismiss-editing [topic]
  (dis/dispatch! [:add-topic-rollback (keyword topic)]))

(defn handle-navigate-event [current-token owner e]
    ;; only when the URL is changing
    (when-not (= (.-token e) current-token)
      ;; check if there are unsaved changes
      (if (om/get-state owner :has-changes)
        
        ;; confirmation dialog
        (add-popover {:container-id "leave-topic-confirm"
                      :height "150px"
                      :message before-unload-message
                      :cancel-title "STAY"
                      :cancel-cb (fn [_]
                                    ;; Go back to the previous token
                                    (.setToken @router/history current-token)
                                    (hide-popover nil "leave-topic-confirm"))
                      :success-title "LEAVE"
                      :success-cb (fn [_]
                                    (hide-popover nil "leave-topic-confirm")
                                    ;; cancel any FoCE
                                    (if (:new (dis/foce-topic-data))
                                      (dismiss-editing (dis/foce-topic-key))
                                      (dis/dispatch! [:foce-start nil]))
                                    ;; Dispatch the current url
                                    (@router/route-dispatcher (router/get-token)))})
        
        ; no changes, so dispatch the current url
        (@router/route-dispatcher (router/get-token)))))

(defn- delete-entry-click [owner e]
  (add-popover {:container-id "delete-entry-confirm"
                :message utils/before-removing-entry-message
                :height "130px"
                :cancel-title "KEEP IT"
                :cancel-cb #(hide-popover nil "delete-entry-confirm")
                :success-title "DELETE"
                :success-cb #(let [topic (dis/foce-topic-key)
                                   entries (dis/topic-entries-data topic)]
                               (dis/dispatch! [:entry-delete topic (:created-at (dis/foce-topic-data))])
                               (hide-popover nil "delete-entry-confirm"))}))

(defn- add-image-tooltip [image-header]
  (if (or (not image-header) (string/blank? image-header))
    "Add an image"
    "Replace image"))

(defn- add-attachment-tooltip [attachments]
  (str "Add " (when (pos? (count attachments)) "another ") "attachment"))

(defn remove-navigation-listener [owner]
  (when (om/get-state owner :history-listener-id)
    (events/unlistenByKey (om/get-state owner :history-listener-id))
    (om/set-state! owner :history-listener-id nil)))

(defn- save-topic [owner]
  (let [topic           (name (dis/foce-topic-key))
        body-el         (js/$ (str "#foce-body-" topic))
        headline-el     (js/$ (str "#foce-headline-" topic))]
    (cond
      ;; if the headline exceeds: focus on it with the cursor at the end, show the chart count
      (om/get-state owner :headline-exceeds)
      (do
        (.focus headline-el)
        (headline-on-change owner)
        (utils/to-end-of-content-editable (.get headline-el 0)))
      ;; body and headline have the right number of chars, moving on with save
      :else
      (do
        (remove-navigation-listener owner)
        (utils/remove-ending-empty-paragraph body-el)
        (let [topic-data   (dis/foce-topic-data)
              board-data   (dis/board-data)
              topics     (vec (:topics board-data))
              fixed-body   (utils/emoji-images-to-unicode (googobj/get (utils/emojify (.html body-el)) "__html"))
              data-to-save {:body fixed-body}]
          (dis/dispatch! [:foce-save topics data-to-save])
          ; go back to dashbaord if it's a brand new topic
          (when (:new topic-data)
            (reset! prevent-route-dispatch false)
            (router/nav! (oc-urls/board))))))))

(defn headline-on-paste
  "Avoid to paste rich text into headline, replace it with the plain text clipboard data."
  [owner e]
  ; Prevent the normal paste behaviour
  (utils/event-stop e)
  (let [clipboardData (or (.-clipboardData e) (.-clipboardData js/window))
        pasted-data (.getData clipboardData "text/plain")
        topic           (name (dis/foce-topic-key))
        headline-el     (.querySelector js/document (str "#foce-headline-" topic))]
    ; replace the selected text of headline with the text/plain data of the clipboard
    ; (set! (.-innerText headline-el) pasted-data)
    (js/replaceSelectedText pasted-data)
    ; call the headline-on-change to check for content length
    (headline-on-change owner)
    ; move cursor at the end
    (utils/to-end-of-content-editable headline-el)))

(defn- data-editing-cb [owner value]
  (dis/dispatch! [:foce-data-editing-start value])) ; global atom state

; (defn show-edit-tt [owner]
;   (let [board-data (dis/board-data)]
;     (when (and (not (om/get-state owner :first-foce-tt-shown))
;                (= (count (:topics board-data)) 1)
;                (= (count (:archived board-data)) 0)
;                (om/get-props owner :foce-key))
;       (om/set-state! owner :first-foce-tt-shown true)
;       (utils/after 500
;         #(let [first-foce (str "first-foce-" (:slug board-data))]
;           (t/tooltip (.querySelector js/document "div.topic-edit") {:desktop "Enter your information. You can select text for easy formatting options, and jazz it up with a headline, emoji or image."
;                                                                     :id first-foce
;                                                                     :once-only true
;                                                                     :config {:place "right-bottom"}})
;           (t/show first-foce))))))

(defn refresh-chart-url [owner topic-data]
  (om/set-state! owner :has-changes true)
  (let [chart-url (:chart-url topic-data)
        chart-rg #"(?i)(cache-buster=[0-9a-f]{4})"
        new-uuid (utils/my-uuid)
        next-chart-url (if (.match chart-url chart-rg)
                          (.replace chart-url chart-rg (str "cache-buster=" new-uuid))
                          (str chart-url "&cache-buster=" new-uuid))]
    (dis/dispatch! [:foce-input [:chart-url next-chart-url]])))

(defcomponent topic-edit [{:keys [currency
                                  card-width
                                  columns-num
                                  prev-rev
                                  next-rev] :as data} owner options]

  (init-state [_]
    (let [topic      (dis/foce-topic-key)
          topic-data (dis/foce-topic-data)
          body       (:body topic-data)
          has-data?  (not-empty (:data topic-data))
          body-placeholder (:body-placeholder topic-data)
          fixed-body-placeholder (if (pos? (count body-placeholder)) (str (string/lower-case (first body-placeholder)) (subs body-placeholder 1)) "")]
      {:initial-headline (utils/emojify (:headline topic-data))
       :body-placeholder (if (string/starts-with? (name topic) "custom-")
                            body-placeholder
                            (if (:new topic-data) (str "What would you like to say? For example, " fixed-body-placeholder) utils/new-topic-body-placeholder))
       :initial-body (utils/emojify (if (and (:placeholder topic-data) (not has-data?)) "" body))
       :char-count nil
       :char-count-alert false
       :has-changes false
       :file-upload-state nil
       :file-upload-progress 0
       :headline-exceeds false
       :first-foce-tt-shown false
       :show-chart-url-input false}))

  (will-unmount [_]
    (when-not (utils/is-test-env?)
      ; if adding a :new topic or a topic that was archived
      ; restore the previous app-state when leaving the view
      (when (and (dis/foce-topic-key)
                 (or (:new (dis/foce-topic-data))
                     (:was-archvied (dis/foce-topic-data))))
        (dis/dispatch! [:add-topic-rollback (dis/foce-topic-key)]))
      ; ; hide FoCE editing tooltip
      ; (t/hide (str "first-foce-" (:slug (dis/board-data))))
      ; re enable the route dispatcher
      (reset! prevent-route-dispatch false)
      ; remove the onbeforeunload handler
      (set! (.-onbeforeunload js/window) nil)
      ; remove history change listener
      (remove-navigation-listener owner)))

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (when-not (responsive/is-tablet-or-mobile?)
        (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))
      (setup-body-editor owner)
      (utils/after 100 #(focus-body))
      (reset! prevent-route-dispatch true)
      (let [loc (.-location js/window)
            current-token (str (.-pathname loc) (.-search loc) (.-hash loc))
            listener (events/listen @router/history HistoryEventType/NAVIGATE
                      (partial handle-navigate-event current-token owner))]
        (om/set-state! owner :history-listener-id listener))
      ;; scroll to top of this div
      (utils/after 10 #(let [topic-edit-div (js/$ "div.topic-edit")]
                        (when (and topic-edit-div
                                   (.offset topic-edit-div)
                                   (.-top (.offset topic-edit-div)))
                          (.animate (js/$ "html, body")
                           #js {:scrollTop (- (.-top (.offset topic-edit-div)) 168)}))))
      ; (show-edit-tt owner)
      ))

  (did-update [_ _ prev-state]
    (when-not (responsive/is-tablet-or-mobile?)
      (let [topic           (dis/foce-topic-key)
            topic-data        (dis/foce-topic-data)
            image-header      (:image-url topic-data)
            add-image-tooltip (add-image-tooltip image-header)
            add-image-el      (js/$ (gdom/getElementByClass "camera"))
            add-chart-el      (js/$ (gdom/getElementByClass "chart-button"))
            show-chart-url-input (om/get-state owner :show-chart-url-input)]
        (when (not= (:show-chart-url-input prev-state) show-chart-url-input)
          (if show-chart-url-input
            (add-popover-with-rum-component collect-chart-popover {:hide-popover-cb #(om/set-state! owner :show-chart-url-input false)
                                                                   :collect-chart-cb #(om/set-state! owner :has-changes true)
                                                                   :container-id "add-chart-popover"
                                                                   :width 500
                                                                   :height 300})
            (hide-popover nil "add-chart-popover")))
        (doto add-image-el
          (.tooltip "hide")
          (.attr "data-original-title" add-image-tooltip)
          (.tooltip "fixTitle")
          (.tooltip "hide"))
        (doto add-chart-el
          (.tooltip "fixTitle")
          (.tooltip "hide")))
      ; (show-edit-tt owner)
      )
    (let [file-upload-state (om/get-state owner :file-upload-state)
          old-file-upload-state (:file-upload-state prev-state)]
      (when (and (= file-upload-state :show-url-field)
                 (not= old-file-upload-state :show-url-field))
        (.focus (sel1 [:input.upload-remote-url-field])))))

  (render-state [_ {:keys [initial-headline initial-body body-placeholder char-count char-count-alert
                           file-upload-state file-upload-progress upload-remote-url
                           show-chart-url-input headline-exceeds has-changes]}]
    (let [board-slug        (router/current-board-slug)
          topic             (dis/foce-topic-key)
          topic-kw          (keyword topic)
          topic-data          (dis/foce-topic-data)
          gray-color          (oc-colors/get-color-by-kw :oc-gray-5)
          image-header        (:image-url topic-data)
          attachments         (:attachments topic-data)
          is-data?            (#{:growth :finances} topic-kw)
          finances-data       (:data topic-data)
          growth-data         (growth-utils/growth-data-map (:data topic-data))
          no-data?            (or (and (= topic-kw :finances)
                                       (or (empty? finances-data)
                                        (utils/no-finances-data? finances-data)))
                                  (and (= topic-kw :growth)
                                       (utils/no-growth-data? growth-data)))
          chart-opts          {:chart-size {:width 230}
                               :hide-nav true
                               :topic-click (:topic-click options)}]
      ; set the onbeforeunload handler only if there are changes
      (let [onbeforeunload-cb (when has-changes #(str before-unload-message))]
        (set! (.-onbeforeunload js/window) onbeforeunload-cb))
      (when topic
        (dom/div #js {:className "topic-foce group"}
          (when (and (not is-data?)
                     image-header)
            (dom/div {:class "card-header card-image"}
              (dom/img {:src image-header
                          :class "topic-header-img"})
               (dom/button {:class "btn-reset remove-header"
                            :data-toggle "tooltip"
                            :data-placement "top"
                            :data-container "body"
                            :title "Remove this image"
                            :on-click (fn [_]
                                        (om/set-state! owner :has-changes true)
                                        (dis/dispatch! [:foce-input {:image-url nil :image-height 0 :image-width 0}]))}
                (i/icon :simple-remove {:size 15
                                        :stroke 4
                                        :color "white"
                                        :accent-color "white"}))))
          ;; Chart
          (dom/div {:class "topic-edit-chart-container"}
            (when-not (empty? (:chart-url topic-data))
              (dom/button {:class "btn-reset chart-btn refresh-chart-btn"
                           :title "Charts sync with Google every 10 min, or you can refresh now."
                           :data-toggle "tooltip"
                           :data-container "body"
                           :data-placement "top"
                           :on-click #(refresh-chart-url owner topic-data)}
                (dom/i {:class "fa fa-refresh"})))
            (when-not (empty? (:chart-url topic-data))
              (dom/button {:class "btn-reset chart-btn remove-chart-btn"
                           :title "Remove Chart from Google Sheets"
                           :data-toggle "tooltip"
                           :data-container "body"
                           :data-placement "top"
                           :on-click (fn [_]
                                        (om/set-state! owner :has-changes true)
                                        (dis/dispatch! [:foce-input [:chart-url nil]]))}
                (dom/i {:class "fa fa-times"})))
            (chart topic-data (- card-width (* 16 2))))
          ;; Topic title
          (dom/input {:class "topic-title"
                      :value (or (:title topic-data) "")
                      :max-length title-max-length
                      :placeholder (:name topic-data)
                      :type "text"
                      :on-blur #(om/set-state! owner :char-count nil)
                      :on-change (fn [e]
                                    (let [v (or (.. e -target -value) "")
                                          remaining-chars (- title-max-length (count v))]
                                      (dis/dispatch! [:foce-input {:title v}])
                                      (om/update-state! owner #(merge % {:has-changes true
                                                                         :char-count remaining-chars
                                                                         :char-count-alert (< remaining-chars title-alert-limit)}))))})
          ;; Topic data
          (when is-data?
            (dom/div {:class ""}
              (cond
                (= topic-kw :growth)
                (om/build topic-growth {:topic-data topic-data
                                        :topic topic-kw
                                        :currency currency
                                        :editable? true
                                        :card-width card-width
                                        :columns-num columns-num
                                        :data-topic-on-change #(om/set-state! owner :has-changes true)
                                        :foce-data-editing? (:foce-data-editing? data)
                                        :editing-cb (partial data-editing-cb owner)}
                                        {:opts chart-opts})
                (= topic-kw :finances)
                (om/build topic-finances {:topic-data (utils/fix-finances topic-data)
                                          :topic topic-kw
                                          :currency currency
                                          :editable? true
                                          :card-width card-width
                                          :columns-num columns-num
                                          :data-topic-on-change #(om/set-state! owner :has-changes true)
                                          :foce-data-editing? (:foce-data-editing? data)
                                          :editing-cb (partial data-editing-cb owner)}
                                          {:opts chart-opts}))))
          ;; Topic headline
          (dom/div #js {:className "topic-headline-inner emoji-autocomplete emojiable"
                        :id (str "foce-headline-" (name topic))
                        :key "foce-headline"
                        :placeholder "Optional headline"
                        :contentEditable true
                        :onPaste #(headline-on-paste owner %)
                        :onKeyUp   #(check-headline-count owner % true)
                        :onKeyDown #(check-headline-count owner % true)
                        :onFocus    #(check-headline-count owner % false)
                        :onBlur (fn [e]
                                    (check-headline-count owner e false)
                                    (om/set-state! owner :char-count nil))
                        :dangerouslySetInnerHTML initial-headline})
          ;; Topic body
          (dom/div #js {:className "topic-body emoji-autocomplete emojiable"
                        :id (str "foce-body-" (name topic))
                        :key (str "foce-body-" (name topic))
                        :role "textbox"
                        :aria-multiline true
                        :contentEditable true
                        :dangerouslySetInnerHTML initial-body})
          (dom/div {:class "topic-foce-buttons group"}
            (dom/div {:class "left mr2 group"
                      :style {:display (if (nil? file-upload-state) "block" "none")}}
              (emoji-picker {:add-emoji-cb (fn [editor emoji]
                                             (let [headline (sel1 (str "#foce-headline-" (name topic)))
                                                   body     (sel1 (str "#foce-body-" (name topic)))]
                                               (when (= (.-activeElement js/document) headline)
                                                 (check-headline-count owner nil true))
                                               (when (= (.-activeElement js/document) body)
                                                 (body-on-change owner))))
                             :disabled (let [headline (sel1 (str "#foce-headline-" (name topic)))
                                             body     (sel1 (str "#foce-body-" (name topic)))]
                                         (not (or (= (.-activeElement js/document) headline)
                                                  (= (.-activeElement js/document) body))))}))

            ;; Topic image button
            (when-not is-data?
              (dom/button {:class "btn-reset camera left"
                         :title (add-image-tooltip image-header)
                         :type "button"
                         :data-toggle "tooltip"
                         :data-container "body"
                         :data-placement "top"
                         :style {:display (if (nil? file-upload-state) "block" "none")}
                         :on-click (fn [e]
                                    (.blur (.-target e))
                                    (utils/after 100 #(.tooltip (js/$ "[data-toggle=\"tooltip\"]") "hide"))
                                    (iu/upload!
                                     "image/*"
                                     (partial img-upload-success-cb owner)
                                     (partial img-upload-progress-cb owner)
                                     (partial img-upload-error-cb owner)))}
                (dom/i {:class "fa fa-camera"})))
            (dom/button {:class "btn-reset attachment left"
                         :title (add-attachment-tooltip attachments)
                         :type "button"
                         :data-toggle "tooltip"
                         :data-container "body"
                         :data-placement "top"
                         :style {:display (if (nil? file-upload-state) "block" "none")}
                         :on-click (fn [e]
                                    (.blur (.-target e))
                                    (utils/after 100 #(.tooltip (js/$ "[data-toggle=\"tooltip\"]") "hide"))
                                    (iu/upload!
                                     nil
                                     (partial attachment-upload-success-cb owner)
                                     nil
                                     (partial attachment-upload-error-cb owner)))}
                (dom/i {:class "fa fa-paperclip"}))

            (when (empty? (:chart-url topic-data))
              (dom/button {:class "btn-reset chart-button left"
                           :title (if (empty? (:chart-url topic-data)) "Add Chart from Google Sheets" "Edit Chart from Google Sheets")
                           :type "button"
                           :data-toggle "tooltip"
                           :data-container "body"
                           :data-placement "top"
                           :on-click #(om/set-state! owner :show-chart-url-input true)}
                (dom/i {:class "fa fa-line-chart"})))
            
            ;; Hidden (initially) file upload progress
            (dom/span {:class (str "file-upload-progress left" (when-not (= file-upload-state :show-progress) " hidden"))}
              (str file-upload-progress "%"))

            (when (:chart-url topic-data)
              (dom/span {:class ""})))

          (topic-attachments attachments #(om/set-state! owner :has-changes true))
          
          ;; Hidden (initially) file upload UI
          (dom/div {:class "topic-foce-footer group"}
            (dom/div {:class "divider"})
            (dom/div {:class "topic-foce-footer-left"}
              (dom/label {:class (utils/class-set {:char-counter true
                                                   :char-count-alert char-count-alert})} char-count))
            (dom/div {:class "topic-foce-footer-right"
                      :style {:display (if (and (not show-chart-url-input) (nil? file-upload-state)) "block" "none")}}
              (dom/button {:class "btn-reset btn-solid"
                           :disabled (or (= file-upload-state :show-progress)
                                         (not has-changes)
                                         (dis/foce-topic-data-editing?))
                           :on-click #(save-topic owner)} "SAVE")
              (dom/button {:class "btn-reset btn-outline"
                           :disabled (dis/foce-topic-data-editing?)
                           :on-click #(if (:new topic-data)
                                        (do
                                          (dismiss-editing topic)
                                          (router/nav! (oc-urls/board)))
                                        (dis/dispatch! [:foce-start nil]))} "CANCEL")
              ;; Topic archive button
            (when (:show-delete-entry-button data)
              (dom/button {:class "btn-reset archive-button right"
                           :title "Delete this entry"
                           :type "button"
                           :data-toggle "tooltip"
                           :data-container "body"
                           :data-placement "top"
                           :style {:display (if (nil? file-upload-state) "block" "none")}
                           :on-click (partial delete-entry-click owner)}
                  (dom/i {:class "fa fa-trash"}))))))))))