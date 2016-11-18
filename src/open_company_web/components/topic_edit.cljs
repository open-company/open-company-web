(ns open-company-web.components.topic-edit
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-colors :as oc-colors]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.lib.medium-editor-exts :as editor]
            [open-company-web.lib.prevent-route-dispatch :refer (prevent-route-dispatch)]
            [open-company-web.lib.growth-utils :as growth-utils]
            [open-company-web.components.growth.topic-growth :refer (topic-growth)]
            [open-company-web.components.finances.topic-finances :refer (topic-finances)]
            [open-company-web.components.ui.icon :as i]
            [open-company-web.components.ui.filestack-uploader :refer (filestack-uploader)]
            [open-company-web.components.ui.emoji-picker :refer (emoji-picker)]
            [open-company-web.components.ui.onboard-tip :refer (onboard-tip)]
            [open-company-web.components.ui.popover :refer (add-popover hide-popover)]
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
(def body-max-length 500)
(def body-alert-limit 50)

(def before-unload-message "You have unsaved edits. Are you sure you want to leave this topic?")
(def before-archive-message "Archiving removes this topic from the dashboard, but it's saved so you can add it back later. Are you sure you want to archive?")

(defn focus-headline []
  (when-let [headline (sel1 [(str "div#foce-headline-" (name (dis/foce-section-key)))])]
    (.focus headline)
    (utils/to-end-of-content-editable headline)))

(defn- scroll-to-topic-top [topic]
  (let [body-scroll (.-scrollTop (.-body js/document))
        topic-scroll-top (utils/offset-top topic)]
    (utils/scroll-to-y (- (+ topic-scroll-top body-scroll) 90))))

(defn- force-hide-placeholder [owner]
  (let [editor       (om/get-state owner :body-editor)
        section-name (name (om/get-props owner :section))
        body-el      (sel1 [(str "div#foce-body-" section-name)])]
    (utils/medium-editor-hide-placeholder editor body-el)))

(defn body-on-change [owner]
  (when-let* [section-kw   (keyword (om/get-props owner :section))
              section-name (name section-kw)
              body-el      (sel1 [(str "div#foce-body-" section-name)])]
    (let [emojied-body (utils/emoji-images-to-unicode (googobj/get (utils/emojify (.-innerHTML body-el)) "__html"))]
      (dis/dispatch! [:foce-input {:body emojied-body}]))
    (let [inner-text-count (count (.-innerText body-el))
          remaining-chars (- body-max-length inner-text-count)]
      ; restore the previous body if the new one exceeds the limit
      ; and the count is greater than the old, if it's less we let it update
      ; to let the user cancel content to get to the limit
      (om/update-state! owner #(merge % {:has-changes true
                                         :char-count remaining-chars
                                         :char-count-alert (< remaining-chars body-alert-limit)
                                         :body-exceeds (neg? remaining-chars)})))))

(defn- setup-edit [owner]
  (when-let* [section-kw   (keyword (om/get-props owner :section))
              section-name (name section-kw)
              body-id      (str "div#foce-body-" section-name)
              body-el      (sel1 [body-id])]
    (let [body-editor      (new js/MediumEditor body-el (clj->js (utils/medium-editor-options "" false)))]
      (.subscribe body-editor
                  "editableInput"
                  (fn [event editable]
                    (body-on-change owner)))
      (om/set-state! owner :body-editor body-editor))
    (js/emojiAutocomplete)))

(defn- headline-on-change [owner]
  (om/set-state! owner :has-changes true)
  (when-let [headline (sel1 (str "div#foce-headline-" (name (dis/foce-section-key))))]
    (let [headline-innerHTML (.-innerHTML headline)
          emojied-headline (utils/emoji-images-to-unicode (googobj/get (utils/emojify (.-innerHTML headline)) "__html"))]
      (dis/dispatch! [:foce-input {:headline emojied-headline}])
      (let [headline-text   (.-innerText headline)
            remaining-chars (- headline-max-length (count headline-text))]
        (om/update-state! owner #(merge % {:char-count remaining-chars
                                           :char-count-alert (< remaining-chars headline-alert-limit)}))))))

(defn- check-headline-count [owner e has-changes]
  (when-let [headline (sel1 (str "div#foce-headline-" (name (dis/foce-section-key))))]
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

(defn- img-on-load [owner img]
  (om/set-state! owner :has-changes true)
  (dis/dispatch! [:foce-input {:image-width (.-clientWidth img)
                               :image-height (.-clientHeight img)}])
  (gdom/removeNode img))

(defn- upload-file! [owner file]
  (let [success-cb  (fn [success]
                      (let [url    (googobj/get success "url")
                            node   (gdom/createDom "img")]
                        (if-not url
                          (js/alert "An error has occurred while processing the image URL. Please try again.")
                          (do
                            (set! (.-onload node) #(img-on-load owner node))
                            (gdom/append (.-body js/document) node)
                            (set! (.-src node) url)))
                        (dis/dispatch! [:foce-input {:image-url url}])
                        (om/set-state! owner (merge (om/get-state owner) {:file-upload-state nil
                                                                          :file-upload-progress nil
                                                                          :has-changes true}))))
        error-cb    (fn [error] (js/alert "An error has occurred while processing the image URL. Please try again.")
                                (om/set-state! owner (merge (om/get-state owner) {:file-upload-state nil
                                                                                  :file-upload-progress nil
                                                                                  :has-changes true})))
        progress-cb (fn [progress]
                      (let [state (om/get-state owner)]
                        (om/set-state! owner (merge state {:file-upload-state :show-progress
                                                           :file-upload-progress progress}))))]
    (cond
      (and (string? file) (not (string/blank? file)))
      (js/filepicker.storeUrl file success-cb error-cb progress-cb)
      file
      (js/filepicker.store file #js {:name (.-name file)} success-cb error-cb progress-cb))))

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
                      :cancel-cb #(do
                                    ;; Go back to the previous token
                                    (.setToken @router/history current-token)
                                    (hide-popover nil "leave-topic-confirm"))
                      :success-title "LEAVE"
                      :success-cb #(do
                                    (hide-popover nil "leave-topic-confirm")
                                    ;; cancel any FoCE
                                    (dis/dispatch! [:start-foce nil])
                                    ;; Dispatch the current url
                                    (@router/route-dispatcher (router/get-token)))})
        
        ; no changes, so dispatch the current url
        (@router/route-dispatcher (router/get-token)))))

(defn- remove-topic-click [owner e]
  (add-popover {:container-id "archive-topic-confirm"
                :message before-archive-message
                :height "170px"
                :cancel-title "KEEP"
                :cancel-cb #(hide-popover nil "archive-topic-confirm")
                :success-title "ARCHIVE"
                :success-cb #(let [section (dis/foce-section-key)]
                               (dis/dispatch! [:topic-archive section])
                               (hide-popover nil "archive-topic-confirm"))}))

(defn- add-image-tooltip [image-header]
  (if (or (not image-header) (string/blank? image-header))
    "Add an image"
    "Replace image"))

(defn- pin-tooltip [pinned]
  (if pinned
    "Unpin this topic"
    "Pin this topic"))

(defn- save-topic [owner]
  (let [topic           (name (dis/foce-section-key))
        body-el         (js/$ (str "#foce-body-" (name topic)))]
    (if (om/get-state owner :body-exceeds)
      (do
        (.focus body-el)
        (body-on-change owner)
        (utils/to-end-of-content-editable (.get body-el 0)))
      (do
        (utils/remove-ending-empty-paragraph body-el)
        (let [topic-data   (dis/foce-section-data)
              company-data (dis/company-data)
              sections     (vec (:sections company-data))
              fixed-body   (utils/emoji-images-to-unicode (googobj/get (utils/emojify (.html body-el)) "__html"))
              data-to-save {:body fixed-body}]
          (cond
            (and (not (om/get-state owner :initially-pinned))
                 (:pin topic-data))
            ; needs to PATCH :sections to move the topic at the top of the unpinned topics
            (let [without-topic (utils/vec-dissoc sections topic)
                  {:keys [pinned other]} (utils/get-pinned-other-keys without-topic company-data)
                  with-pinned-topic (let [[before after] (split-at (count pinned) without-topic)]
                                      (vec (concat before [topic] after)))]
              (dis/dispatch! [:foce-save with-pinned-topic data-to-save]))
            (and (om/get-state owner :initially-pinned)
                 (not (:pin topic-data)))
            ; needs to PATCH :sections to move the topic at the top of the unpinned topics
            (let [without-topic (utils/vec-dissoc sections topic)
                  {:keys [pinned other]} (utils/get-pinned-other-keys without-topic company-data)
                  with-unpinned-topic (let [[before after] (split-at (inc (count pinned)) without-topic)]
                                      (vec (concat before [topic] after)))]
              (dis/dispatch! [:foce-save with-unpinned-topic data-to-save]))
            :else
            (dis/dispatch! [:foce-save sections data-to-save])))))))

(defn- data-editing-cb [owner value]
  (dis/dispatch! [:start-foce-data-editing value])) ; global atom state

(defcomponent topic-edit [{:keys [show-first-edit-tip
                                  currency
                                  card-width
                                  prev-rev
                                  next-rev] :as data} owner options]

  (init-state [_]
    (let [topic      (dis/foce-section-key)
          topic-data (dis/foce-section-data)
          body       (:body topic-data)
          has-data?  (not-empty (:data topic-data))]
      {:initial-headline (utils/emojify (:headline topic-data))
       :body-placeholder (or (:body-placeholder topic-data) "")
       :initial-body  (utils/emojify (if (and (:placeholder topic-data) (not has-data?)) "" body))
       :initially-pinned (:pin topic-data)
       :char-count nil
       :char-count-alert false
       :has-changes false
       :file-upload-state nil
       :file-upload-progress 0
       :body-exceeds false}))

  (will-receive-props [_ next-props]
    ;; update body placeholder when receiving data from API
    (let [topic        (dis/foce-section-key)
          company-data (dis/company-data)
          topic-data   (get company-data (keyword topic))]
      (om/set-state! owner :body-placeholder (or (:body-placeholder topic-data) ""))))

  (will-unmount [_]
    (when-not (utils/is-test-env?)
      ; re enable the route dispatcher
      (reset! prevent-route-dispatch false)
      ; remove the onbeforeunload handler
      (set! (.-onbeforeunload js/window) nil)
      ; remove history change listener
      (when (om/get-state owner :history-listener-id)
        (events/unlistenByKey (om/get-state owner :history-listener-id))
        (om/set-state! owner :history-listener-id nil))))

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (js/filepicker.setKey ls/filestack-key)
      (when-not (responsive/is-tablet-or-mobile?)
        (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))
      (setup-edit owner)
      (utils/after 100 #(focus-headline))
      (reset! prevent-route-dispatch true)
      (let [win-location (.-location js/window)
            current-token (oc-urls/company (router/current-company-slug))
            listener (events/listen @router/history HistoryEventType/NAVIGATE
                      (partial handle-navigate-event current-token owner))]
        (om/set-state! owner :history-listener-id listener))))

  (did-update [_ _ prev-state]
    (when-not (responsive/is-tablet-or-mobile?)
      (let [section           (dis/foce-section-key)
            topic-data        (dis/foce-section-data)
            image-header      (:image-url topic-data)
            add-image-tooltip (add-image-tooltip image-header)
            add-image-el      (js/$ (gdom/getElementByClass "camera"))
            pin-image         (js/$ (gdom/getElementByClass "pin-button"))
            add-chart-el      (js/$ (gdom/getElementByClass "chart-button"))]
        (doto add-image-el
          (.tooltip "hide")
          (.attr "data-original-title" add-image-tooltip)
          (.tooltip "fixTitle")
          (.tooltip "hide"))
        (doto pin-image
          (.tooltip "hide")
          (.attr "data-original-title" (pin-tooltip (:pin topic-data)))
          (.tooltip "fixTitle"))
        (doto add-chart-el
          (.tooltip "fixTitle")
          (.tooltip "hide"))))
    (let [file-upload-state (om/get-state owner :file-upload-state)
          old-file-upload-state (:file-upload-state prev-state)]
      (when (and (= file-upload-state :show-url-field)
                 (not= old-file-upload-state :show-url-field))
        (.focus (sel1 [:input.upload-remote-url-field])))))

  (render-state [_ {:keys [initial-headline initial-body body-placeholder char-count char-count-alert
                           file-upload-state file-upload-progress upload-remote-url body-exceeds
                           has-changes]}]

    (let [company-slug        (router/current-company-slug)
          section             (dis/foce-section-key)
          section-kw          (keyword section)
          topic-data          (dis/foce-section-data)
          gray-color          (oc-colors/get-color-by-kw :oc-gray-5)
          image-header        (:image-url topic-data)
          is-data?            (#{:growth :finances} section-kw)
          finances-data       (:data topic-data)
          growth-data         (growth-utils/growth-data-map (:data topic-data))
          no-data?            (or (and (= section-kw :finances)
                                       (or (empty? finances-data)
                                        (utils/no-finances-data? finances-data)))
                                  (and (= section-kw :growth)
                                       (utils/no-growth-data? growth-data)))
          chart-opts          {:chart-size {:width 230}
                               :hide-nav true
                               :topic-click (:topic-click options)}]
      ; set the onbeforeunload handler only if there are changes
      (let [onbeforeunload-cb (when has-changes #(str before-unload-message))]
        (set! (.-onbeforeunload js/window) onbeforeunload-cb))
      (when section
        (dom/div #js {:className "topic-foce group"
                      :ref "topic-internal"}
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
                            :on-click #(do
                                        (om/set-state! owner :has-changes true)
                                        (dis/dispatch! [:foce-input {:image-url nil :image-height 0 :image-width 0}]))}
                (i/icon :simple-remove {:size 15
                                        :stroke 4
                                        :color "white"
                                        :accent-color "white"}))))
          ;; Topic title
          (dom/input {:class "topic-title"
                      :value (:title topic-data)
                      :max-length title-max-length
                      :placeholder (:name topic-data)
                      :type "text"
                      :on-blur #(om/set-state! owner :char-count nil)
                      :on-change (fn [e]
                                    (let [v (.. e -target -value)
                                          remaining-chars (- title-max-length (count v))]
                                      (dis/dispatch! [:foce-input {:title v}])
                                      (om/update-state! owner #(merge % {:has-changes true
                                                                         :char-count remaining-chars
                                                                         :char-count-alert (< remaining-chars title-alert-limit)}))))})
          
          ;; Topic data
          (when is-data?
            (dom/div {:class ""}
              (cond
                (= section-kw :growth)
                (om/build topic-growth {:section-data topic-data
                                        :section section-kw
                                        :currency currency
                                        :editable? true
                                        :data-section-on-change #(om/set-state! owner :has-changes true)
                                        :foce-data-editing? (:foce-data-editing? data)
                                        :editing-cb (partial data-editing-cb owner)}
                                        {:opts chart-opts})
                (= section-kw :finances)
                (om/build topic-finances {:section-data (utils/fix-finances topic-data)
                                          :section section-kw
                                          :currency currency
                                          :editable? true
                                          :data-section-on-change #(om/set-state! owner :has-changes true)
                                          :foce-data-editing? (:foce-data-editing? data)
                                          :editing-cb (partial data-editing-cb owner)}
                                          {:opts chart-opts}))))
          ;; Topic headline
          (dom/div #js {:className "topic-headline-inner emoji-autocomplete emojiable"
                        :id (str "foce-headline-" (name section))
                        :key "foce-headline"
                        :placeholder "Headline"
                        :contentEditable true
                        :onKeyUp   #(check-headline-count owner % true)
                        :onKeyDown #(check-headline-count owner % true)
                        :onFocus    #(check-headline-count owner % false)
                        :onBlur #(do
                                    (check-headline-count owner % false)
                                    (om/set-state! owner :char-count nil))
                        :dangerouslySetInnerHTML initial-headline})
          ;; Topic body
          (dom/div #js {:className "topic-body emoji-autocomplete emojiable"
                        :id (str "foce-body-" (name section))
                        :key (str "foce-body-" (name section))
                        :ref "topic-body"
                        :role "textbox"
                        :aria-multiline true
                        :placeholder body-placeholder
                        :data-placeholder body-placeholder
                        :contentEditable true
                        :dangerouslySetInnerHTML initial-body})
          (dom/div {:class "topic-foce-buttons group"}
            (dom/input {:id "foce-file-upload-ui--select-trigger"
                        :style {:display "none"}
                        :type "file"
                        :on-change #(upload-file! owner (-> % .-target .-files (aget 0)))})
            (dom/div {:class "left mr2"
                      :style {:display (if (nil? file-upload-state) "block" "none")}}
              (emoji-picker {:add-emoji-cb (fn [editor emoji]
                                             (when (= editor (sel1 (str "div#foce-body-" (name section-kw))))
                                               (force-hide-placeholder owner))
                                             (let [headline (sel1 (str "#foce-headline-" (name section)))
                                                   body     (sel1 (str "#foce-body-" (name section)))]
                                               (when (= (.-activeElement js/document) headline)
                                                  (check-headline-count owner nil true))
                                               (when (= (.-activeElement js/document) body)
                                                  (body-on-change owner))))
                             :disabled (let [headline (sel1 (str "#foce-headline-" (name section)))
                                             body     (sel1 (str "#foce-body-" (name section)))]
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
                         :on-click #(.click (sel1 [:input#foce-file-upload-ui--select-trigger]))}
                (dom/i {:class "fa fa-camera"})))

            ;; Topic chart button
            (when (or (= is-data? :growth)
                      (and (= is-data? :finances)
                           (not (dis/foce-section-data-editing?))))
              (dom/button {:class "btn-reset chart-button left"
                           :title (if (and (= is-data? :growth)
                                           (pos? (count (:metrics topic-data))))
                                    "Add another chart"
                                    "Add a chart")
                           :type "button"
                           :data-toggle "tooltip"
                           :data-container "body"
                           :data-placement "top"
                           :style {:display (if (or (and (= is-data? :finances) no-data?) (= is-data? :growth)) "block" "none")}
                           :on-click #(dis/dispatch! [:start-foce-data-editing (if (= is-data? :growth) growth-utils/new-metric-slug-placeholder :new)])}
                (dom/i {:class "fa fa-line-chart"})))

            ;; Topic archive button
            (when-not (:placeholder topic-data)
              (dom/button {:class "btn-reset archive-button right"
                           :title "Archive this topic"
                           :type "button"
                           :data-toggle "tooltip"
                           :data-container "body"
                           :data-placement "top"
                           :style {:display (if (nil? file-upload-state) "block" "none")}
                           :on-click (partial remove-topic-click owner)}
                  (dom/i {:class "fa fa-archive"})))
            
            ;; Topic pin button
            (when (> (count (utils/get-section-keys (dis/company-data))) 1)
              (dom/button {:class "btn-reset pin-button right"
                           :title (pin-tooltip (:pin topic-data))
                           :type "button"
                           :data-toggle "tooltip"
                           :data-container "body"
                           :data-placement "top"
                           :style {:display (if (nil? file-upload-state) "block" "none")}
                           :on-click #(dis/dispatch! [:foce-input {:pin (not (:pin topic-data))}])}
                  (dom/i {:class (str "fa fa-thumb-tack" (if (:pin topic-data) " pinned" ""))})))
            
            ;; Hidden (initially) file upload progress
            (dom/span {:class (str "file-upload-progress left" (when-not (= file-upload-state :show-progress) " hidden"))}
              (str file-upload-progress "%")))
          
          ;; Hidden (initially) file upload UI
          (dom/div {:class "topic-foce-footer group"}
            (dom/div {:class "divider"})
            (dom/div {:class (str "upload-remote-url-container left" (when-not (= file-upload-state :show-url-field) " hidden"))
                      :style {:display (if file-upload-state "block" "none")}}
              (dom/input {:type "text"
                          :class "upload-remote-url-field"
                          :style {:width (str (- card-width 122 50) "px")}
                          :on-change #(om/set-state! owner :upload-remote-url (-> % .-target .-value))
                          :placeholder "http://site.com/img.png"
                          :value upload-remote-url})
              (dom/button {:style {:font-size "14px" :margin-left "5px" :padding "0.3rem"}
                           :class "btn-reset btn-solid"
                           :disabled (string/blank? upload-remote-url)
                           :on-click #(upload-file! owner (om/get-state owner :upload-remote-url))}
                "add")
              (dom/button {:style {:font-size "14px" :margin-left "5px" :padding "0.3rem"}
                           :class "btn-reset btn-outline"
                           :on-click #(om/set-state! owner :file-upload-state nil)}
                "cancel"))
            (dom/div {:class "topic-foce-footer-left"
                      :style {:display (if (nil? file-upload-state) "block" "none")}}
              (dom/label {:class (utils/class-set {:char-counter true
                                                   :char-count-alert char-count-alert})} char-count))
            (dom/div {:class "topic-foce-footer-right"
                      :style {:display (if (nil? file-upload-state) "block" "none")}}
              (dom/button {:class "btn-reset btn-solid"
                           :disabled (or (= file-upload-state :show-progress)
                                         (dis/foce-section-data-editing?))
                           :on-click #(save-topic owner)} "SAVE")
              (dom/button {:class "btn-reset btn-outline"
                           :disabled (dis/foce-section-data-editing?)
                           :on-click #(if (:placeholder topic-data)
                                        (dis/dispatch! [:topic-archive (name section)])
                                        (dis/dispatch! [:start-foce nil]))} "CANCEL")))

        ;; Onboarding toolips
        (when (and show-first-edit-tip (not is-data?))
          (onboard-tip
            {:id (str "content-topic-add-" company-slug)
             :once-only true
             :mobile false
             :desktop "What would you like to say? You can add text, emoji and images."
             :dismiss-tip-fn focus-headline}))

        (when (and (= section-kw :growth) no-data?)
          (onboard-tip
            {:id (str "growth-topic-add-" company-slug)
             :once-only true
             :mobile false
             :desktop "Add simple charts to share company performance."
             :dismiss-tip-fn focus-headline}))

        (when (and (= section-kw :finances) no-data?)
          (onboard-tip
            {:id (str "finance-topic-add-" company-slug)
             :once-only true
             :mobile false
             :desktop "Add a chart to share revenue, expenses and cash."
             :dismiss-tip-fn focus-headline})))))))