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

(def before-unload-message "You have unsaved edits. Are you sure you want to leave this topic?")
(def before-archive-message "Archiving removes this topic from the dashboard, but it's saved so you can add it back later. Are you sure you want to archive?")

(defn- scroll-to-topic-top [topic]
  (let [body-scroll (.-scrollTop (.-body js/document))
        topic-scroll-top (utils/offset-top topic)]
    (utils/scroll-to-y (- (+ topic-scroll-top body-scroll) 90))))

(defn- start-fullscreen-editing-click [owner options e]
  (utils/event-stop e)
  (let [section (om/get-props owner :section)
        topic-click-cb (:topic-click options)]
    (topic-click-cb nil true)))

(defn- force-hide-placeholder [owner]
  (let [editor       (om/get-state owner :body-editor)
        section-name (name (om/get-props owner :section))
        body-el      (sel1 [(str "div#foce-body-" section-name)])]
    (utils/medium-editor-hide-placeholder editor body-el)))

(defn- setup-edit [owner]
  (when-let* [section-kw   (keyword (om/get-props owner :section))
              section-name (name section-kw)
              body-el      (sel1 [(str "div#foce-body-" section-name)])]
    (let [body-editor      (new js/MediumEditor body-el (clj->js (utils/medium-editor-options "" false)))]
      (.subscribe body-editor
                  "editableInput"
                  (fn [event editable]
                    (om/set-state! owner :has-changes true)
                    (let [emojied-body (utils/emoji-images-to-unicode (googobj/get (utils/emojify (.-innerHTML body-el)) "__html"))]
                      (dis/dispatch! [:foce-input {:body emojied-body}]))
                    (let [inner-text (.-innerText body-el)]
                      (om/set-state! owner :char-count (if (> (count inner-text) 500) "Extended\nlength" nil)))))
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
        (om/set-state! owner :char-count remaining-chars)
        (om/set-state! owner :char-count-alert (< remaining-chars headline-alert-limit))
        (om/set-state! owner :negative-headline-char-count (neg? remaining-chars))))))

(defn- check-headline-count [owner e has-changes]
  (when-let [headline (sel1 (str "div#foce-headline-" (name (dis/foce-section-key))))]
    (let [headline-value (.-innerText headline)]
      (when (and (not= (.-keyCode e) 8)
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
                      (let [url    (.-url success)
                            node   (gdom/createDom "img")]
                        (set! (.-onload node) #(img-on-load owner node))
                        (gdom/append (.-body js/document) node)
                        (set! (.-src node) url)
                        (dis/dispatch! [:foce-input {:image-url url}]))
                      (om/set-state! owner (merge (om/get-state owner) {:file-upload-state nil
                                                                        :file-upload-progress nil
                                                                        :has-changes true})))
        error-cb    (fn [error] (js/console.log "error" error))
        progress-cb (fn [progress]
                      (let [state (om/get-state owner)]
                        (om/set-state! owner (merge state {:file-upload-state :show-progress
                                                           :file-upload-progress progress}))))]
    (cond
      (and (string? file) (not (string/blank? file)))
      (js/filepicker.storeUrl file success-cb error-cb progress-cb)
      file
      (js/filepicker.store file #js {:name (.-name file)} success-cb error-cb progress-cb))))

(defn focus-headline []
  (when-let [headline (sel1 [(str "div#foce-headline-" (name (dis/foce-section-key)))])]
    (.focus headline)
    (utils/to-end-of-content-editable headline)))

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
  (when e
    (utils/event-stop e))
  (add-popover {:container-id "archive-topic-confirm"
                :message before-archive-message
                :height "170px"
                :cancel-title "KEEP"
                :cancel-cb #(hide-popover nil "archive-topic-confirm")
                :success-title "ARCHIVE"
                :success-cb #(do
                                (let [section (dis/foce-section-key)]
                                  (dis/dispatch! [:topic-archive section]))
                                (dis/dispatch! [:start-foce nil]))}))

(defn- add-image-tooltip [image-header]
  (if (or (not image-header) (string/blank? image-header))
    "Add an image"
    "Replace image"))

(defcomponent topic-edit [{:keys [show-first-edit-tip
                                  currency
                                  prev-rev
                                  next-rev]} owner options]

  (init-state [_]
    (let [topic      (dis/foce-section-key)
          topic-data (dis/foce-section-data)
          body       (:body topic-data)]
      {:initial-headline (utils/emojify (:headline topic-data))
       :body-placeholder (or (:body-placeholder topic-data) "")
       :initial-body  (utils/emojify (if (:placeholder topic-data) "" body))
       :char-count nil
       :char-count-alert false
       :has-changes false
       :file-upload-state nil
       :file-upload-progress 0}))

  (will-receive-props [_ next-props]
    ;; update body placeholder when receiving data from API
    (let [topic        (dis/foce-section-key)
          company-data (dis/company-data)
          topic-data   (get company-data (keyword topic))
          body         (:body topic-data)]
      (om/set-state! owner :body-placeholder (or (:body-placeholder topic-data) ""))))

  (will-unmount [_]
    (when-not (utils/is-test-env?)
      ; re enable the route dispatcher
      (reset! prevent-route-dispatch false)
      ; remove the onbeforeunload handler
      (set! (.-onbeforeunload js/window) nil)
      ; remove history change listener
      (events/unlistenByKey (om/get-state owner :history-listener-id))))

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (js/filepicker.setKey ls/filestack-key)
      (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
      (setup-edit owner)
      (utils/after 100 #(focus-headline))
      (reset! prevent-route-dispatch true)
      (let [win-location (.-location js/window)
            current-token (oc-urls/company (router/current-company-slug))
            listener (events/listen @router/history HistoryEventType/NAVIGATE
                      (partial handle-navigate-event current-token owner))]
        (om/set-state! owner :history-listener-id listener))))

  (did-update [_ _ _]
    (let [section           (dis/foce-section-key)
          topic-data        (dis/foce-section-data)
          image-header      (:image-url topic-data)
          add-image-tooltip (add-image-tooltip image-header)
          add-image-el      (js/$ (gdom/getElementByClass "camera"))]
      (doto add-image-el
        (.tooltip "hide")
        (.attr "data-original-title" add-image-tooltip)
        (.tooltip "fixTitle"))))

  (render-state [_ {:keys [initial-headline initial-body body-placeholder char-count char-count-alert
                           file-upload-state file-upload-progress upload-remote-url negative-headline-char-count
                           has-changes]}]

    (let [company-slug        (router/current-company-slug)
          section             (dis/foce-section-key)
          topic-data          (dis/foce-section-data)
          section-kw          (keyword section)
          chart-opts          {:chart-size {:width 230}
                               :hide-nav true
                               :topic-click (:topic-click options)}
          is-growth-finances? (#{:growth :finances} section-kw)
          gray-color          (oc-colors/get-color-by-kw :oc-gray-5)
          finances-row-data   (:data topic-data)
          growth-data         (growth-utils/growth-data-map (:data topic-data))
          no-data             (or (and (= section-kw :finances)
                                       (or (empty? finances-row-data)
                                        (utils/no-finances-data? finances-row-data)))
                                  (and (= section-kw :growth)
                                       (utils/no-growth-data? growth-data)))
          image-header        (:image-url topic-data)
          topic-body          (:body topic-data)]
      ; set the onbeforeunload handler only if there are changes
      (let [onbeforeunload-cb (when has-changes #(str before-unload-message))]
        (set! (.-onbeforeunload js/window) onbeforeunload-cb))
      (when section
        (dom/div #js {:className "topic-foce group"
                      :ref "topic-internal"}
          (when image-header
            (dom/div {:class "card-header card-image"}
              (when image-header
                (dom/img {:src image-header
                          :class "topic-header-img"}))
              (when image-header
                (dom/button {:class "btn-reset remove-header"
                             :on-click #(do
                                          (om/set-state! owner :has-changes true)
                                          (dis/dispatch! [:foce-input {:image-url nil :image-height 0 :image-width 0}]))}
                  (i/icon :simple-remove {:size 15
                                          :stroke 4
                                          :color "white"
                                          :accent-color "white"})))))
          ;; Topic title
          (dom/input {:class "topic-title"
                      :value (:title topic-data)
                      :max-length title-max-length
                      :placeholder (:name topic-data)
                      :type "text"
                      :on-blur #(om/set-state! owner :char-count nil)
                      :on-change #(let [v (.. % -target -value)
                                        remaining-chars (- title-max-length (count v))]
                                    (dis/dispatch! [:foce-input {:title v}])
                                    (om/set-state! owner :has-changes true)
                                    (om/set-state! owner :char-count remaining-chars)
                                    (om/set-state! owner :char-count-alert (< remaining-chars title-alert-limit)))})
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
          (dom/div #js {:className "topic-body emoji-autocomplete emojiable"
                        :id (str "foce-body-" (name section))
                        :key "foce-body"
                        :ref "topic-body"
                        :placeholder body-placeholder
                        :data-placeholder body-placeholder
                        :contentEditable true
                        :style #js {:minHeight (if (:placeholder topic-data) "110px" "0px")}
                        :onBlur #(om/set-state! owner :char-count nil)
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
                                               (force-hide-placeholder owner)))
                             :disabled (let [headline (sel1 (str "#foce-headline-" (name section)))
                                             body     (sel1 (str "#foce-body-" (name section)))]
                                         (not (or (= (.-activeElement js/document) headline)
                                                  (= (.-activeElement js/document) body))))}))
            (dom/button {:class "btn-reset camera left"
                         :title (add-image-tooltip image-header)
                         :type "button"
                         :data-toggle "tooltip"
                         :data-placement "top"
                         :style {:display (if (nil? file-upload-state) "block" "none")}
                         :on-click #(.click (sel1 [:input#foce-file-upload-ui--select-trigger]))}
                (dom/i {:class "fa fa-camera"}))
            (dom/button {:class "btn-reset image-url left"
                         :title "Provide an image link"
                         :type "button"
                         :data-toggle "tooltip"
                         :data-placement "top"
                         :style {:display (if (nil? file-upload-state) "block" "none")}
                         :on-click #(om/set-state! owner :file-upload-state :show-url-field)}
                (dom/i {:class "fa fa-code"}))
            (when-not (:placeholder topic-data)
              (dom/button {:class "btn-reset archive-button right"
                           :title "Archive this topic"
                           :type "button"
                           :data-toggle "tooltip"
                           :data-placement "top"
                           :style {:display (if (nil? file-upload-state) "block" "none")}
                           :on-click (partial remove-topic-click owner)}
                  (dom/i {:class "fa fa-archive"})))
            (dom/div {:class (str "upload-remote-url-container left" (when-not (= file-upload-state :show-url-field) " hidden"))}
                (dom/input {:type "text"
                            :style {:height "32px" :margin-top "1px" :outline "none" :border "1px solid rgba(78, 90, 107, 0.5)"}
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
            (dom/span {:class (str "file-upload-progress left" (when-not (= file-upload-state :show-progress) " hidden"))}
              (str file-upload-progress "%")))
          (dom/div {:class "topic-foce-footer group"}
            (dom/div {:class "divider"})
            (dom/div {:class "topic-foce-footer-left"}
              (dom/label {:class (str "char-counter" (when char-count-alert " char-count-alert"))} char-count))
            (dom/div {:class "topic-foce-footer-right"}
              (dom/button {:class "btn-reset btn-solid"
                           :disabled (or (= file-upload-state :show-progress) negative-headline-char-count)
                           :on-click #(dis/dispatch! [:foce-save])} "SAVE")
              (dom/button {:class "btn-reset btn-outline"
                           :on-click #(do
                                        (utils/event-stop %)
                                        (if (:placeholder topic-data)
                                          (dis/dispatch! [:topic-archive (name section)])
                                          (dis/dispatch! [:start-foce nil])))} "CANCEL")))

        ;; Onboarding toolip
        (when show-first-edit-tip
          (onboard-tip
            {:id (str "content-topic-add-" company-slug)
             :once-only true
             :mobile false
             :desktop "What would you like to say? You can add text, emoji and images."
             :dismiss-tip-fn focus-headline})))))))