(ns oc.web.components.entry-edit
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.utils.activity :as au]
            [oc.web.utils.ui :as ui-utils]
            [oc.web.local-settings :as ls]
            [oc.web.lib.image-upload :as iu]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.rich-body-editor :refer (rich-body-editor)]
            [oc.web.components.ui.sections-picker :refer (sections-picker)]
            [oc.web.components.ui.ziggeo :refer (ziggeo-player ziggeo-recorder)]
            [oc.web.components.ui.stream-attachments :refer (stream-attachments)]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn real-close [s]
  (reset! (::dismiss s) true)
  (utils/after 180 activity-actions/entry-edit-dismiss))

;; Local cache for outstanding edits

(defn remove-autosave [s]
  (when @(::autosave-timer s)
    (.clearInterval js/window @(::autosave-timer s))
    (reset! (::autosave-timer s) nil)))

(defn autosave [s]
  (let [entry-editing @(drv/get-ref s :entry-editing)
        body-el (sel1 [:div.rich-body-editor])
        cleaned-body (when body-el
                      (utils/clean-body-html (.-innerHTML body-el)))]
    (activity-actions/entry-save-on-exit :entry-editing entry-editing cleaned-body)))

(defn save-on-exit?
  "Locally save the current outstanding edits if needed."
  [s]
  (when @(drv/get-ref s :entry-save-on-exit)
    (autosave s)))

(defn toggle-save-on-exit
  "Enable and disable save current edit."
  [s turn-on?]
  (activity-actions/entry-toggle-save-on-exit turn-on?))

;; Close dismiss handling

(defn cancel-clicked [s]
  (let [entry-editing @(drv/get-ref s :entry-editing)
        clean-fn (fn [dismiss-modal?]
                    (remove-autosave s)
                    (activity-actions/entry-clear-local-cache (:uuid entry-editing) :entry-editing)
                    (when dismiss-modal?
                      (alert-modal/hide-alert))
                    (real-close s))]
    (if @(::uploading-media s)
      (let [alert-data {:icon "/img/ML/trash.svg"
                        :action "dismiss-edit-uploading-media"
                        :message (str "Leave before finishing upload?")
                        :link-button-title "Stay"
                        :link-button-cb #(alert-modal/hide-alert)
                        :solid-button-style :red
                        :solid-button-title "Cancel upload"
                        :solid-button-cb #(clean-fn true)}]
        (alert-modal/show-alert alert-data))
      (if (:has-changes entry-editing)
        (let [alert-data {:icon "/img/ML/trash.svg"
                          :action "dismiss-edit-dirty-data"
                          :message (str "Leave without saving your changes?")
                          :link-button-title "Stay"
                          :link-button-cb #(alert-modal/hide-alert)
                          :solid-button-style :red
                          :solid-button-title "Lose changes"
                          :solid-button-cb #(clean-fn true)}]
          (alert-modal/show-alert alert-data))
        (clean-fn false)))))

;; Data change handling

(defn body-on-change [state]
  (toggle-save-on-exit state true)
  (dis/dispatch! [:input [:entry-editing :has-changes] true]))

(defn- headline-on-change [state]
  (toggle-save-on-exit state true)
  (when-let [headline (rum/ref-node state "headline")]
    (let [emojied-headline (.-innerText headline)]
      (dis/dispatch! [:update [:entry-editing] #(merge % {:headline emojied-headline
                                                          :has-changes true})]))))

;; Headline setup and paste handler

(defn- setup-headline [state]
  (when-let [headline-el  (rum/ref-node state "headline")]
    (reset! (::headline-input-listener state) (events/listen headline-el EventType/INPUT #(headline-on-change state))))
  (js/emojiAutocomplete))

(defn headline-on-paste
  "Avoid to paste rich text into headline, replace it with the plain text clipboard data."
  [state e]
  ; Prevent the normal paste behaviour
  (utils/event-stop e)
  (let [clipboardData (or (.-clipboardData e) (.-clipboardData js/window))
        pasted-data   (.getData clipboardData "text/plain")]
    ; replace the selected text of headline with the text/plain data of the clipboard
    (js/replaceSelectedText pasted-data)
    ; call the headline-on-change to check for content length
    (headline-on-change state)
    (when (and (not (responsive/is-tablet-or-mobile?))
               (= (.-activeElement js/document) (.-body js/document)))
      (when-let [headline-el (rum/ref-node state "headline")]
        ; move cursor at the end
        (utils/to-end-of-content-editable headline-el)))))

(defn add-emoji-cb [s]
  (headline-on-change s)
  (body-on-change s))

(defn- clean-body [s]
  (when-let [body-el (sel1 [:div.rich-body-editor])]
    (dis/dispatch! [:input [:entry-editing :body] (utils/clean-body-html (.-innerHTML body-el))]))
  (when ls/oc-enable-transcriptions
    (let [editing-data @(drv/get-ref s :entry-editing)]
      (when (:fixed-video-id editing-data)
        (when-let [transcription-el (rum/ref-node s "transcript-edit")]
          (dis/dispatch! [:update [:entry-editing] #(merge % {:video-transcript (.-value transcription-el)})]))))))

(defn- fix-headline [entry-editing]
  (utils/trim (:headline entry-editing)))

(defn- is-publishable? [s entry-editing]
  (and (seq (:board-slug entry-editing))
       (or (and @(::record-video s)
                @(::video-uploading s))
           (not @(::record-video s)))
       (not (zero? (count (fix-headline entry-editing))))))

(defn video-record-clicked [s]
  (let [entry-editing @(drv/get-ref s :entry-editing)
        start-recording-fn #(do
                              (reset! (::record-video s) true)
                              (reset! (::video-picking-cover s) false)
                              (reset! (::video-uploading s) false))]
    (cond
      (:fixed-video-id entry-editing)
      (activity-actions/prompt-remove-video :entry-editing)
      @(::record-video s)
      (reset! (::record-video s) false)
      :else
      (start-recording-fn))))

(defn win-width []
  (or (.-innerWidth js/window)
      (.-clientWidth (.-documentElement js/document))))

(defn calc-video-height [s]
  (when (responsive/is-tablet-or-mobile?)
    (reset! (::mobile-video-height s) (* (win-width) (/ 377 640)))))

(defn show-post-error [s message]
  (when-let [$post-btn (js/$ (rum/ref-node s "mobile-post-btn"))]
    (if-not (.data $post-btn "bs.tooltip")
      (.tooltip $post-btn
       (clj->js {:container "body"
                 :placement "bottom"
                 :trigger "manual"
                 :template (str "<div class=\"tooltip post-btn-tooltip\">"
                                  "<div class=\"tooltip-arrow\"></div>"
                                  "<div class=\"tooltip-inner\"></div>"
                                "</div>")
                 :title message}))
      (doto $post-btn
        (.attr "data-original-title" message)
        (.tooltip "fixTitle")))
    (utils/after 10 #(.tooltip $post-btn "show"))
    (utils/after 5000 #(.tooltip $post-btn "hide"))))

(defn post-clicked [s]
  (clean-body s)
  (let [entry-editing @(drv/get-ref s :entry-editing)
        fixed-headline (fix-headline entry-editing)
        published? (= (:status entry-editing) "published")]
    (if (is-publishable? s entry-editing)
      (let [_ (dis/dispatch! [:input [:entry-editing :headline] fixed-headline])
            updated-entry-editing @(drv/get-ref s :entry-editing)
            section-editing @(drv/get-ref s :section-editing)]
        (remove-autosave s)
        (if published?
          (do
            (reset! (::saving s) true)
            (activity-actions/entry-save updated-entry-editing section-editing))
          (do
            (reset! (::publishing s) true)
            (activity-actions/entry-publish (dissoc updated-entry-editing :status) section-editing))))
      (cond
        ;; Missing headline error
        (zero? (count fixed-headline))
        (show-post-error s "A title is required in order to save or share this post.")
        ;; User needs to pick a cover shot
        (and @(::record-video s)
             (not @(::video-uploading s))
             @(::video-picking-cover s))
        (show-post-error s "Please pick a cover image for your video.")
        ;; Video still recording
        (and @(::record-video s)
             (not @(::video-uploading s)))
        (show-post-error s "Please finish video recording.")))))

(rum/defcs entry-edit < rum/reactive
                        ;; Derivatives
                        (drv/drv :org-data)
                        (drv/drv :current-user-data)
                        (drv/drv :entry-editing)
                        (drv/drv :section-editing)
                        (drv/drv :editable-boards)
                        (drv/drv :entry-save-on-exit)
                        (drv/drv :show-sections-picker)
                        ;; Locals
                        (rum/local false ::dismiss)
                        (rum/local "" ::initial-body)
                        (rum/local "" ::initial-headline)
                        (rum/local nil ::headline-input-listener)
                        (rum/local nil ::uploading-media)
                        (rum/local false ::saving)
                        (rum/local false ::publishing)
                        (rum/local false ::window-click-listener)
                        (rum/local nil ::autosave-timer)
                        (rum/local false ::show-legend)
                        (rum/local false ::record-video)
                        (rum/local 0 ::mobile-video-height)
                        (rum/local false ::video-uploading)
                        (rum/local false ::video-picking-cover)
                        ;; Mixins
                        mixins/no-scroll-mixin
                        mixins/first-render-mixin
                        (mixins/render-on-resize calc-video-height)

                        {:will-mount (fn [s]
                          (let [entry-editing @(drv/get-ref s :entry-editing)
                                initial-body (if (seq (:body entry-editing))
                                               (:body entry-editing)
                                               "")
                                initial-headline (utils/emojify
                                                   (if (seq (:headline entry-editing))
                                                     (:headline entry-editing)
                                                     ""))]
                            (reset! (::initial-body s) initial-body)
                            (reset! (::initial-headline s) initial-headline))
                          s)
                         :did-mount (fn [s]
                          (utils/after 300 #(setup-headline s))
                          (when-not (responsive/is-tablet-or-mobile?)
                            (when-let [headline-el (rum/ref-node s "headline")]
                              (utils/to-end-of-content-editable headline-el)))
                          (reset! (::window-click-listener s)
                           (events/listen js/window EventType/CLICK
                            #(when (and @(::show-legend s)
                                      (not (utils/event-inside? % (rum/ref-node s "legend-container"))))
                               (reset! (::show-legend s) false))))
                          (reset! (::autosave-timer s) (utils/every 5000 #(autosave s)))
                          (when (responsive/is-tablet-or-mobile?)
                            (set! (.-scrollTop (.-body js/document)) 0))
                          (calc-video-height s)
                          (when ls/oc-enable-transcriptions
                            (ui-utils/resize-textarea (rum/ref-node s "transcript-edit")))
                          s)
                         :did-remount (fn [_ s]
                          (when ls/oc-enable-transcriptions
                            (ui-utils/resize-textarea (rum/ref-node s "transcript-edit")))
                          s)
                         :before-render (fn [s]
                          ;; Handle saving/publishing states to dismiss the component
                          (let [entry-editing @(drv/get-ref s :entry-editing)]
                            ;; Entry is saving
                            (when @(::saving s)
                              ;: Save request finished
                              (when-not (:loading entry-editing)
                                (reset! (::saving s) false)
                                (when-not (:error entry-editing)
                                  (real-close s)
                                  (let [to-draft? (not= (:status entry-editing) "published")]
                                    ;; If it's not published already redirect to drafts board
                                    (utils/after 180
                                     #(router/nav!
                                       (if to-draft?
                                         (oc-urls/drafts (router/current-org-slug))
                                         (oc-urls/board (:board-slug entry-editing)))))))))
                            (when @(::publishing s)
                              (when-not (:publishing entry-editing)
                                (reset! (::publishing s) false)
                                (when-not (:error entry-editing)
                                  (let [redirect? (seq (:board-slug entry-editing))]
                                    ;; Redirect to the publishing board if the slug is available
                                    (when redirect?
                                      (real-close s)
                                      (utils/after
                                       180
                                       #(let [from-ap (or (:from-all-posts @router/path)
                                                          (= (router/current-board-slug) "all-posts"))
                                              go-to-ap (and (not (:new-section entry-editing))
                                                            from-ap)]
                                          ;; Redirect to AP if coming from it or if the post is not published
                                          (router/nav!
                                            (if go-to-ap
                                              (oc-urls/all-posts (router/current-org-slug))
                                              (oc-urls/board (router/current-org-slug)
                                               (:board-slug entry-editing))))))))))))
                          s)
                         :will-unmount (fn [s]
                          (when @(::headline-input-listener s)
                            (events/unlistenByKey @(::headline-input-listener s))
                            (reset! (::headline-input-listener s) nil))
                          (when @(::window-click-listener s)
                            (events/unlistenByKey @(::window-click-listener s))
                            (reset! (::window-click-listener s) nil))
                          (remove-autosave s)
                          s)}
  [s]
  (let [org-data          (drv/react s :org-data)
        current-user-data (drv/react s :current-user-data)
        entry-editing     (drv/react s :entry-editing)
        is-mobile? (responsive/is-tablet-or-mobile?)
        published? (= (:status entry-editing) "published")
        show-sections-picker (drv/react s :show-sections-picker)
        posting-title (if (:uuid entry-editing)
                        (if (= (:status entry-editing) "published")
                          "Posted to "
                          "Draft for ")
                        "Posting to ")
        video-size (if is-mobile?
                     {:width (win-width)
                      :height @(::mobile-video-height s)}
                     {:width 640
                      :height 377})]
    [:div.entry-edit-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(:first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(:first-render-done s))})}
      [:div.entry-edit-modal-header
        [:button.mlb-reset.modal-close-bt
          {:on-click #(cancel-clicked s)}
          ""]
        [:div.entry-edit-modal-header-title
          {:dangerouslySetInnerHTML (utils/emojify (if (seq (:headline entry-editing)) (:headline entry-editing) utils/default-headline))}]
        (let [should-show-save-button? (and (not @(::publishing s))
                                            (not published?))]
          [:div.entry-edit-modal-header-right
            (let [disabled? (or @(::publishing s)
                                (not (is-publishable? s entry-editing)))
                  working? (or (and published?
                                    @(::saving s))
                               (and (not published?)
                                    @(::publishing s)))]
              [:button.mlb-reset.header-buttons.post-button
                {:ref "mobile-post-btn"
                 :on-click #(post-clicked s)
                 :class (utils/class-set {:disabled disabled?
                                          :loading working?})}
                (when working?
                  (small-loading))
                (if published?
                  "SAVE"
                  "POST")])
            (when should-show-save-button?
              [:div.mobile-buttons-divider-line])
            (when should-show-save-button?
              (let [disabled? (or @(::saving s)
                                  (not (:has-changes entry-editing))
                                  (and @(::record-video s)
                                       (not @(::video-uploading s))))
                    working? @(::saving s)]
                [:button.mlb-reset.header-buttons.save-button
                  {:class (utils/class-set {:disabled disabled?
                                            :loading working?})
                   :on-click (fn [_]
                              (when-not disabled?
                                (remove-autosave s)
                                (clean-body s)
                                (reset! (::saving s) true)
                                (activity-actions/entry-save (assoc @(drv/get-ref s :entry-editing) :status "draft") @(drv/get-ref s :section-editing))))}
                  (when working?
                    (small-loading))
                  "Save draft"]))])]
      [:div.entry-edit-modal.group
        {:ref "entry-edit-modal"}
        [:div.entry-edit-modal-section.group
          [:div.posting-in
            {:on-click #(when-not (utils/event-inside? % (rum/ref-node s :picker-container))
                          (dis/dispatch! [:input [:show-sections-picker] (not show-sections-picker)]))}
            (user-avatar-image current-user-data)
            [:span.posting-in-span
              posting-title]
            [:div.board-name
              (:board-name entry-editing)]
            (when show-sections-picker
              [:div
                {:ref :picker-container}
                (sections-picker (:board-slug entry-editing)
                 (fn [board-data note]
                   (dis/dispatch! [:input [:show-sections-picker] false])
                   (when (and board-data
                              (seq (:name board-data)))
                    (dis/dispatch! [:input [:entry-editing]
                     (merge entry-editing {:board-slug (:slug board-data)
                                           :board-name (:name board-data)
                                           :has-changes true
                                           :invite-note note})]))))])]
          ;; Add video button
          (when-not is-mobile?
            [:div.entry-edit-modal-video-bt-container
              [:button.mlb-reset.video-record-bt
                {:on-click #(video-record-clicked s)
                 :class (when (or (:fixed-video-id entry-editing)
                                  @(::record-video s))
                          "remove-video-bt")}
                (if (or (:fixed-video-id entry-editing)
                        @(::record-video s))
                  "Remove video"
                  "Record video")]])]
        [:div.entry-edit-modal-separator]
        [:div.entry-edit-modal-body
          {:ref "entry-edit-modal-body"}
          ;; Video elements
          (when (and (not is-mobile?)
                     (:fixed-video-id entry-editing)
                     (not @(::record-video s)))
            (ziggeo-player {:video-id (:fixed-video-id entry-editing)
                            :remove-video-cb #(activity-actions/prompt-remove-video :entry-editing)
                            :width (:width video-size)
                            :height (:height video-size)
                            :video-processed (:video-processed entry-editing)}))
          (when (and (not is-mobile?)
                     @(::record-video s))
            (ziggeo-recorder {:start-cb (partial activity-actions/video-started-recording-cb :entry-editing)
                              :upload-started-cb #(do
                                                    (activity-actions/uploading-video %)
                                                    (reset! (::video-picking-cover s) false)
                                                    (reset! (::video-uploading s) true))
                              :pick-cover-start-cb #(reset! (::video-picking-cover s) true)
                              :pick-cover-end-cb #(reset! (::video-picking-cover s) false)
                              :submit-cb (partial activity-actions/video-processed-cb :entry-editing)
                              :width (:width video-size)
                              :height (:height video-size)
                              :remove-recorder-cb (fn []
                                (activity-actions/remove-video :entry-editing)
                                (reset! (::record-video s) false))}))
          ; Headline element
          [:div.entry-edit-headline.emoji-autocomplete.emojiable.group.fs-hide
            {:content-editable true
             :ref "headline"
             :placeholder utils/default-headline
             :on-paste    #(headline-on-paste s %)
             :on-key-down #(headline-on-change s)
             :on-click    #(headline-on-change s)
             :on-key-press (fn [e]
                           (when (= (.-key e) "Enter")
                             (utils/event-stop e)
                             (utils/to-end-of-content-editable (sel1 [:div.rich-body-editor]))))
             :dangerouslySetInnerHTML @(::initial-headline s)}]
          [:div.rich-body-editor-wrapper
            (rich-body-editor {:on-change (partial body-on-change s)
                               :use-inline-media-picker false
                               :multi-picker-container-selector "div#entry-edit-footer-multi-picker"
                               :initial-body @(::initial-body s)
                               :show-placeholder (not (contains? entry-editing :links))
                               :show-h2 true
                               :dispatch-input-key :entry-editing
                               :upload-progress-cb (fn [is-uploading?]
                                                     (reset! (::uploading-media s) is-uploading?))
                               :media-config ["photo" "video"]
                               :classes "emoji-autocomplete emojiable fs-hide"})]
          (when (and ls/oc-enable-transcriptions
                     (:fixed-video-id entry-editing)
                     (:video-processed entry-editing))
            [:div.entry-edit-transcript
              [:textarea.video-transcript
                {:ref "transcript-edit"
                 :on-input #(ui-utils/resize-textarea (.-target %))
                 :default-value (:video-transcript entry-editing)}]])
          ; Attachments
          (stream-attachments (:attachments entry-editing) nil
           #(activity-actions/remove-attachment :entry-editing %))]
        [:div.entry-edit-modal-footer.group
          [:div.entry-edit-footer-multi-picker
            {:id "entry-edit-footer-multi-picker"}]
          (emoji-picker {:add-emoji-cb (partial add-emoji-cb s)
                         :width 20
                         :height 20
                         :position "top"
                         :default-field-selector "div.entry-edit-modal div.rich-body-editor"
                         :container-selector "div.entry-edit-modal"})
          [:div.entry-edit-legend-container
            {:on-click #(reset! (::show-legend s) (not @(::show-legend s)))
             :ref "legend-container"}
            [:button.mlb-reset.entry-edit-legend-trigger
              {:aria-label "Keyboard shortcuts"
               :title "Shortcuts"
               :data-toggle "tooltip"
               :data-placement "top"
               :data-container "body"}]
            (when @(::show-legend s)
              [:div.entry-edit-legend-image])]]]]))