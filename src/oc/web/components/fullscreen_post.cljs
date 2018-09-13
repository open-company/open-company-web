(ns oc.web.components.fullscreen-post
  (:require [rum.core :as rum]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.mixins.ui :as mixins]
            [oc.web.local-settings :as ls]
            [oc.web.utils.activity :as au]
            [oc.web.utils.ui :as ui-utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.wrt :refer (wrt)]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.stream-comments :refer (stream-comments)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.rich-body-editor :refer (rich-body-editor)]
            [oc.web.components.ui.sections-picker :refer (sections-picker)]
            [oc.web.components.ui.comments-summary :refer (comments-summary)]
            [oc.web.components.ui.ziggeo :refer (ziggeo-player ziggeo-recorder)]
            [oc.web.components.ui.stream-attachments :refer (stream-attachments)]))

;; Unsaved edits handling

(defn remove-autosave [s]
  (when @(::autosave-timer s)
    (.clearInterval js/window @(::autosave-timer s))
    (reset! (::autosave-timer s) nil)))

(defn autosave [s]
  (when s
    (when-let [body-el (sel1 [:div.rich-body-editor])]
      (let [modal-data @(drv/get-ref s :fullscreen-post-data)
            section-editing (:section-editing modal-data)
            save-on-exit (:entry-save-on-exit modal-data)
            activity-data (:modal-editing-data modal-data)
            cleaned-body (when body-el
                          (utils/clean-body-html (.-innerHTML body-el)))]
        (when save-on-exit
          (activity-actions/entry-save-on-exit :modal-editing-data
                                               activity-data
                                               cleaned-body
                                               section-editing))))))

(defn save-on-exit?
  "Locally save the current outstanding edits if needed."
  [s]
  (when (:entry-save-on-exit @(drv/get-ref s :fullscreen-post-data))
    (autosave s)))

(defn toggle-save-on-exit
  "Enable and disable save current edit."
  [s turn-on?]
  (activity-actions/entry-toggle-save-on-exit turn-on?))

;; Modal dismiss handling

(defn dismiss-modal [s]
  (let [modal-data @(drv/get-ref s :fullscreen-post-data)
        edited-data (:modal-editing-data modal-data)]
    (activity-actions/activity-modal-fade-out (:board-slug edited-data))))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 #(dismiss-modal s)))

;; Editing

(defn body-on-change [state]
  (toggle-save-on-exit state true)
  (dis/dispatch! [:input [:modal-editing-data :has-changes] true]))

(defn- headline-on-change [state]
  (toggle-save-on-exit state true)
  (when-let [headline (rum/ref-node state "edit-headline")]
    (let [emojied-headline (.-innerHTML headline)]
      (dis/dispatch! [:update [:modal-editing-data] #(merge % {:headline emojied-headline
                                                               :has-changes true})]))))

(defn- setup-headline [state]
  (when-let [headline-el  (rum/ref-node state "edit-headline")]
    (reset! (::headline-input-listener state)
     (events/listen headline-el EventType/INPUT #(headline-on-change state)))
    (js/emojiAutocomplete)))

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
    (when (= (.-activeElement js/document) (.-body js/document))
      (when-let [headline-el   (rum/ref-node state "edit-headline")]
        ; move cursor at the end
        (utils/to-end-of-content-editable headline-el)))))

(defn- add-emoji-cb [state]
  (headline-on-change state)
  (when-let [body (sel1 [:div.rich-body-editor])]
    (body-on-change state)))

(defn- real-start-editing [state & [focus]]
  (activity-actions/activity-modal-edit (:activity-data @(drv/get-ref state :fullscreen-post-data)) true)
  (utils/after 100 #(setup-headline state))
  (remove-autosave state)
  (reset! (::autosave-timer state) (utils/every 5000 #(autosave state)))
  (.click (js/$ "div.rich-body-editor a") #(.stopPropagation %))
  ; (ui-utils/resize-textarea (rum/ref-node state "transcript-edit"))
  (when (and focus
             (= (.-activeElement js/document) (.-body js/document)))
    (utils/after 1000
      #(cond
         (= focus :body)
         (let [body-el (sel1 [:div.rich-body-editor])
               scrolling-el (sel1 [:div.activity-modal-content])]
           (utils/to-end-of-content-editable body-el)
           (utils/scroll-to-bottom scrolling-el))
         (= focus :headline)
         (when-let [headline-el (rum/ref-node state "edit-headline")]
           (utils/to-end-of-content-editable headline-el))))))

(defn- stop-editing [state]
  (save-on-exit? state)
  (toggle-save-on-exit state false)
  (reset! (::edited-data-loaded state) false)
  (reset! (::record-video state) false)
  (reset! (::video-uploading state) false)
  (reset! (::video-picking-cover state) false)
  (remove-autosave state)
  (activity-actions/activity-modal-edit (:activity-data @(drv/get-ref state :fullscreen-post-data)) false)
  (when @(::headline-input-listener state)
    (events/unlistenByKey @(::headline-input-listener state))
    (reset! (::headline-input-listener state) nil)))

(defn- clean-body [state]
  (when-let [body-el (sel1 [:div.rich-body-editor])]
    (let [raw-html (.-innerHTML body-el)]
      (dis/dispatch! [:update [:modal-editing-data] #(merge % {:body (utils/clean-body-html raw-html)
                                                               :has-changes true})])))
  (when ls/oc-enable-transcriptions
    (let [editing-data (:modal-editing-data @(drv/get-ref state :fullscreen-post-data))]
      (when (:fixed-video-id editing-data)
        (when-let [transcription-el (rum/ref-node state "transcript-edit")]
          (dis/dispatch! [:update [:modal-editing-data] #(merge % {:video-transcript (.-value transcription-el)})]))))))

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

(defn- fix-headline [entry-editing]
  (utils/trim (:headline entry-editing)))

(defn- can-save? [s modal-editing-data]
  (and (seq (:board-slug modal-editing-data))
       (or (and @(::record-video s)
                @(::video-uploading s))
           (not @(::record-video s)))
       (not (zero? (count (fix-headline modal-editing-data))))))

(defn save-editing? [state]
  (clean-body state)
  (let [modal-data @(drv/get-ref state :fullscreen-post-data)
        section-editing (:section-editing modal-data)
        edited-data (:modal-editing-data modal-data)
        fixed-headline (fix-headline edited-data)]
    (if (can-save? state edited-data)
      (let [_ (dis/dispatch! [:input [:modal-editing-data :headline] fixed-headline])
            updated-edited-data (:modal-editing-data @(drv/get-ref state :fullscreen-post-data))]
        (reset! (::entry-saving state) true)
        (activity-actions/entry-modal-save edited-data section-editing))
      (cond
        ;; Missing headline error
        (zero? (count fixed-headline))
        (show-post-error state "A title is required in order to save or share this post.")
        ;; User needs to pick a cover shot
        (and @(::record-video state)
             (not @(::video-uploading state))
             @(::video-picking-cover state))
        (show-post-error state "Please pick a cover image for your video.")
        ;; Video still recording
        (and @(::record-video state)
             (not @(::video-uploading state)))
        (show-post-error state "Please finish video recording.")))))

(defn- dismiss-editing? [state dismiss-modal?]
  (let [modal-data @(drv/get-ref state :fullscreen-post-data)
        dismiss-fn (fn [dismiss-alert?]
                     (when dismiss-alert?
                       (alert-modal/hide-alert))
                     (stop-editing state)
                     (activity-actions/entry-clear-local-cache
                        (:uuid (:modal-editing-data modal-data))
                        :modal-editing-data
                        (:modal-editing-data modal-data))
                     (when dismiss-modal?
                       (close-clicked state)))]
  (if @(::uploading-media state)
    (let [alert-data {:icon "/img/ML/trash.svg"
                      :action "dismiss-edit-uploading-media"
                      :message (str "Leave before finishing upload?")
                      :link-button-title "Stay"
                      :link-button-cb #(alert-modal/hide-alert)
                      :solid-button-style :red
                      :solid-button-title "Cancel upload"
                      :solid-button-cb #(dismiss-fn true)}]
      (alert-modal/show-alert alert-data))
    (if (:has-changes (:modal-editing-data modal-data))
      (let [alert-data {:icon "/img/ML/trash.svg"
                        :action "dismiss-edit-dirty-data"
                        :message (str "Leave without saving your changes?")
                        :link-button-title "Stay"
                        :link-button-cb #(alert-modal/hide-alert)
                        :solid-button-style :red
                        :solid-button-title "Lose changes"
                        :solid-button-cb #(dismiss-fn true)}]
        (alert-modal/show-alert alert-data))
      (dismiss-fn false)))))

(defn setup-editing-data [s]
  (let [modal-data @(drv/get-ref s :fullscreen-post-data)]
    (when-not @(::edited-data-loaded s)
      (let [activity-data (:activity-data modal-data)
            initial-body (:body activity-data)
            initial-headline (utils/emojify (:headline activity-data))]
        (reset! (::initial-body s) initial-body)
        (reset! (::initial-headline s) initial-headline)
        (reset! (::edited-data-loaded s) true)))))

(defn video-record-clicked [s]
  (let [modal-data @(drv/get-ref s :fullscreen-post-data)
        activity-editing (:modal-editing-data modal-data)
        start-recording-fn #(do
                             (reset! (::record-video s) true)
                             (reset! (::video-picking-cover s) false)
                             (reset! (::video-uploading s) false))]
    (cond
      (:fixed-video-id activity-editing)
      (activity-actions/prompt-remove-video :modal-editing-data)
      @(::record-video s)
      (reset! (::record-video s) false)
      :else
      (start-recording-fn))))

(defn send-item-read-if-needed [s]
  (let [post-data @(drv/get-ref s :fullscreen-post-data)
        editing (:modal-editing post-data)
        activity-data (:activity-data post-data)]
    (when (and (= (:status activity-data) "published")
               (not editing))
      ;; Check if the page is at the bottom of the scroll
      (let [body-el (rum/ref-node s :fullscreen-post-box-content-body)]
        (when (au/is-element-bottom-visible? body-el)
          (activity-actions/wrt-events-gate (:uuid activity-data)))))))

(defn win-width []
  (or (.-innerWidth js/window)
      (.-clientWidth (.-documentElement js/document))))

(defn calc-video-height [s]
  (when (responsive/is-tablet-or-mobile?)
    (reset! (::mobile-video-height s) (* (win-width) (/ 377 640)))))

(rum/defcs fullscreen-post < rum/reactive
                             ;; Derivatives
                             (drv/drv :fullscreen-post-data)
                             ;; Locals
                             (rum/local false ::dismiss)
                             (rum/local false ::animate)
                             (rum/local false ::showing-dropdown)
                             (rum/local false ::move-activity)
                             (rum/local nil ::window-click)
                             (rum/local nil ::window-scroll)
                             ;; Editing locals
                             (rum/local "" ::initial-headline)
                             (rum/local "" ::initial-body)
                             (rum/local nil ::headline-input-listener)
                             (rum/local false ::entry-saving)
                             (rum/local nil ::uploading-media)
                             (rum/local false ::save-on-exit)
                             (rum/local false ::edited-data-loaded)
                             (rum/local nil ::autosave-timer)
                             (rum/local false ::show-legend)
                             (rum/local false ::record-video)
                             (rum/local 0 ::mobile-video-height)
                             (rum/local false ::video-uploading)
                             (rum/local false ::video-picking-cover)
                             ;; Mixins
                             (when-not (responsive/is-mobile-size?)
                               mixins/no-scroll-mixin)
                             (mixins/render-on-resize calc-video-height)
                             mixins/first-render-mixin

                             {:before-render (fn [s]
                               (setup-editing-data s)
                               (let [modal-data @(drv/get-ref s :fullscreen-post-data)]
                                 ;; Animate the view if needed
                                 (when (and (not @(::animate s))
                                          (= (:activity-modal-fade-in modal-data) (:uuid (:activity-data modal-data))))
                                   (reset! (::animate s) true))
                                 ;; Hanlde save on exit
                                 (let [save-on-exit (:entry-save-on-exit modal-data)]
                                   (set! (.-onbeforeunload js/window)
                                    (if save-on-exit
                                     #(do
                                       (save-on-exit? s)
                                       "Do you want to save before leaving?")
                                     nil)))
                                 (when (and (:modal-editing modal-data)
                                            (nil? @(::autosave-timer s)))
                                   (utils/after 1000 #(real-start-editing s :headline)))
                                 ;; Saves finished, dismiss the editing if it succeeded
                                 (when (and (:modal-editing modal-data)
                                            @(::entry-saving s))
                                   (let [entry-edit (:modal-editing-data modal-data)
                                         activity-data (:activity-data modal-data)
                                         dismiss-modal-on-editing-stop (:dismiss-modal-on-editing-stop modal-data)
                                         initial-body (:body entry-edit)
                                         initial-headline (utils/emojify (:headline entry-edit))]
                                     (when-not (:loading entry-edit)
                                       (when-not (:error entry-edit)
                                         (reset! (::initial-headline s) initial-headline)
                                         (reset! (::initial-body s) initial-body)
                                         (stop-editing s)
                                         (activity-actions/entry-clear-local-cache (:uuid activity-data) :modal-editing-data activity-data)
                                         (cond
                                           ;; If the board change redirect to the board since the url we have is
                                           ;; not correct anymore
                                           (not= (:board-slug entry-edit) (router/current-board-slug))
                                           (router/nav!
                                            (if (:from-all-posts @router/path)
                                              (oc-urls/all-posts)
                                              (oc-urls/board (:board-slug entry-edit))))
                                           ;; Dismiss editing if needed
                                           dismiss-modal-on-editing-stop
                                          (close-clicked s)))
                                       (dis/dispatch! [:input [:dismiss-modal-on-editing-stop] false])
                                       (reset! (::entry-saving s) false)))))
                               s)
                              :will-mount (fn [s]
                               (let [modal-data @(drv/get-ref s :fullscreen-post-data)]
                                 ;; Force comments reload
                                 (comment-actions/get-comments (:activity-data modal-data)))
                               s)
                              :did-mount (fn [s]
                               (reset! (::window-click s)
                                (events/listen
                                 js/window
                                 EventType/CLICK
                                 (fn [e]
                                   (when (and @(::show-legend s)
                                              (not (utils/event-inside? e (rum/ref-node s "legend-container"))))
                                      (reset! (::show-legend s) false)))))
                               (reset! (::window-scroll s)
                                (events/listen
                                 (rum/ref-node s :fullscreen-post-container)
                                 EventType/SCROLL
                                 #(send-item-read-if-needed s)))
                               (send-item-read-if-needed s)
                               (calc-video-height s)
                               (when ls/oc-enable-transcriptions
                                 (ui-utils/resize-textarea (rum/ref-node s "transcript-edit")))
                               s)
                              :did-remount (fn [_ s]
                               (when ls/oc-enable-transcriptions
                                 (ui-utils/resize-textarea (rum/ref-node s "transcript-edit")))
                               s)
                              :will-unmount (fn [s]
                               (when @(::window-click s)
                                 (events/unlistenByKey @(::window-click s))
                                 (reset! (::window-click s) nil))
                               (when @(::headline-input-listener s)
                                 (events/unlistenByKey @(::headline-input-listener s))
                                 (reset! (::headline-input-listener s) nil))
                               (when @(::window-scroll s)
                                 (events/unlistenByKey @(::window-scroll s))
                                 (reset! (::window-scroll s) nil))
                               (set! (.-onbeforeunload js/window) nil)
                               s)}
  [s]
  (let [modal-data (drv/react s :fullscreen-post-data)
        activity-data (:activity-data modal-data)
        is-mobile? (responsive/is-tablet-or-mobile?)
        edit-link (utils/link-for (:links activity-data) "partial-update")
        share-link (utils/link-for (:links activity-data) "share")
        editing (:modal-editing modal-data)
        activity-editing (:modal-editing-data modal-data)
        show-sections-picker (and editing (:show-sections-picker modal-data))
        dom-element-id (str "fullscreen-post-" (:uuid activity-data))
        activity-comments (-> modal-data
                              :comments-data
                              (get (:uuid activity-data))
                              :sorted-comments)
        comments-data (or activity-comments (:comments activity-data))
        read-data (:read-data modal-data)
        current-activity-data (if editing activity-editing activity-data)
        video-id (:fixed-video-id current-activity-data)
        activity-attachments (:attachments current-activity-data)
        video-size (if is-mobile?
                     {:width (win-width)
                      :height @(::mobile-video-height s)}
                     {:width 640
                      :height 377})]
    [:div.fullscreen-post-container.group
      {:class (utils/class-set {:will-appear (or @(::dismiss s)
                                                 (and @(::animate s)
                                                      (not @(:first-render-done s))))
                                :appear (and (not @(::dismiss s)) @(:first-render-done s))
                                :editing editing
                                :no-comments (not (:has-comments activity-data))})
       :ref :fullscreen-post-container
       :id dom-element-id}
      [:div.fullscreen-post-header
        [:button.mlb-reset.mobile-modal-close-bt
          {:on-click #(if editing
                        (dismiss-editing? s (:dismiss-modal-on-editing-stop modal-data))
                        (close-clicked s))}]
        [:div.header-title-container.group.fs-hide
          {:key (:updated-at activity-data)}
          (if (seq (:headline current-activity-data))
            (:headline current-activity-data)
            utils/default-headline)]
        [:div.fullscreen-post-header-right
          [:div.activity-share-container]
          (if editing
            (let [disabled? (or @(::entry-saving s)
                                (not (:has-changes activity-editing))
                                (and @(::record-video s)
                                     (not @(::video-uploading s))))
                  working? @(::entry-saving s)]
              [:button.mlb-reset.post-publish-bt
                {:ref "mobile-post-btn"
                 :on-click (fn [] (utils/after 1000 #(save-editing? s)))
                 :class (utils/class-set {:loading working?
                                          :disabled disabled?})}
                "SAVE"])
            (more-menu activity-data dom-element-id {:tooltip-position "left" :external-share true}))]]
      [:div.fullscreen-post.group
        {:ref "fullscreen-post"}
        (if editing
          [:div.fullscreen-post-author-header.section-editing.group
            [:div.fullscreen-post-author-header-author
              {:on-click #(when-not (utils/event-inside? % (rum/ref-node s :picker-container))
                            (dis/dispatch! [:input [:show-sections-picker] (not show-sections-picker)]))}
              (user-avatar-image (:publisher current-activity-data))
              [:div.fullscreen-post-box-content-board.group
                [:span.posting-in-span
                  "Posting in "]
                [:div.board-name
                  (:board-name activity-editing)]
                (when show-sections-picker
                  [:div
                    {:ref :picker-container}
                    (sections-picker (:board-slug activity-editing)
                     (fn [section-data note]
                       ;; Dismiss the picker
                       (dis/dispatch! [:input [:show-sections-picker] false])
                       ;; Update the post if the user picked a section
                       (when (and activity-editing
                                  (seq (:name section-data)))
                        (dis/dispatch! [:input [:modal-editing-data]
                         (merge activity-editing {:board-slug (:slug section-data)
                                                  :board-name (:name section-data)
                                                  :has-changes true
                                                  :invite-note note})]))))])]]
            ;; Add video button
            (when-not is-mobile?
              [:div.fullscreen-post-author-header-video-bt-container
                [:button.mlb-reset.video-record-bt
                  {:on-click #(video-record-clicked s)
                   :class (when (or (:fixed-video-id activity-editing)
                                    @(::record-video s))
                            "remove-video-bt")}
                  (if (or (:fixed-video-id activity-editing)
                          @(::record-video s))
                    "Remove video"
                    "Record video")]])]
          [:div.fullscreen-post-author-header.group
            [:div.fullscreen-post-author-header-author
              (user-avatar-image (:publisher current-activity-data))
              [:div.name-container.group
                [:div.name.fs-hide
                  (str (:name (:publisher current-activity-data))
                   " in "
                   (:board-name current-activity-data))]
                (when (:new current-activity-data)
                  [:div.new-tag
                    "New"])]
              [:div.fullscreen-post-author-header-sub
                [:div.time-since
                  (let [t (or (:published-at current-activity-data) (:created-at current-activity-data))]
                    [:time
                      {:date-time t
                       :data-toggle (when-not is-mobile? "tooltip")
                       :data-placement "top"
                       :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
                       :data-title (utils/activity-date-tooltip current-activity-data)}
                      (utils/time-since t)])]
                [:div.separator]
                [:div.fullscreen-post-wrt
                  (wrt current-activity-data read-data)]]]])
        ;; Left column
        [:div.fullscreen-post-left-column
          [:div.fullscreen-post-left-column-content.group
            ;; Video element
            (when (and video-id
                       (not @(::record-video s)))
              (ziggeo-player {:video-id video-id
                              :remove-video-cb (when editing #(activity-actions/prompt-remove-video :modal-editing-data))
                              :width (:width video-size)
                              :height (:height video-size)
                              :video-processed (:video-processed current-activity-data)}))
            (when @(::record-video s)
              (ziggeo-recorder {:start-cb (partial activity-actions/video-started-recording-cb :modal-editing-data)
                                :upload-started-cb #(do
                                                      (activity-actions/uploading-video %)
                                                      (reset! (::video-picking-cover s) false)
                                                      (reset! (::video-uploading s) true))
                                :pick-cover-start-cb #(reset! (::video-picking-cover s) true)
                                :pick-cover-end-cb #(reset! (::video-picking-cover s) false)
                                :width (:width video-size)
                                :height (:height video-size)
                                :submit-cb (partial activity-actions/video-processed-cb :modal-editing-data)
                                :remove-recorder-cb (fn []
                                  (activity-actions/remove-video :modal-editing-data)
                                  (reset! (::record-video s) false))}))
            (if editing
              [:div.fullscreen-post-box-content-headline.headline-edit.emoji-autocomplete.emojiable.fs-hide
                {:content-editable true
                 :ref "edit-headline"
                 :key (str "fullscreen-post-headline-edit-" (:updated-at current-activity-data))
                 :placeholder utils/default-headline
                 :on-paste    #(headline-on-paste s %)
                 :on-key-down #(headline-on-change s)
                 :on-click    #(headline-on-change s)
                 :on-key-press (fn [e]
                               (when (= (.-key e) "Enter")
                                 (utils/event-stop e)
                                 (utils/to-end-of-content-editable (sel1 [:div.rich-body-editor]))))
                 :dangerouslySetInnerHTML @(::initial-headline s)}]
              [:div.fullscreen-post-box-content-headline.fs-hide
                {:content-editable false
                 :ref "edit-headline"
                 :key (str "fullscreen-post-headline-" (:updated-at activity-data))
                 :dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}])
            (when (and (:must-see current-activity-data)
                       (not editing))
              [:div.must-see
               {:class (utils/class-set {:must-see-on (:must-see current-activity-data)})}])
            (if editing
              (rich-body-editor {:on-change #(body-on-change s)
                                 :initial-body @(::initial-body s)
                                 :show-placeholder true
                                 :show-h2 true
                                 :dispatch-input-key :modal-editing-data
                                 :upload-progress-cb #(reset! (::uploading-media s) %)
                                 :media-config ["photo" "video"]
                                 :classes "emoji-autocomplete emojiable"
                                 :use-inline-media-picker false
                                 :multi-picker-container-selector "div#fullscreen-post-box-footer-multi-picker"})
              [:div.fullscreen-post-box-content-body.fs-hide
                {:key (str "fullscreen-post-body-" (:updated-at current-activity-data))
                 :ref :fullscreen-post-box-content-body
                 :dangerouslySetInnerHTML (utils/emojify (:body current-activity-data))}])
            (when (and ls/oc-enable-transcriptions
                       (:video-transcript current-activity-data)
                       (:video-processed current-activity-data))
              (if editing
                [:div.fullscreen-post-transcript
                  [:textarea.fullscreen-post-transcript-edit
                    {:ref "transcript-edit"
                     :on-input #(ui-utils/resize-textarea (.-target %))
                     :default-value (:video-transcript current-activity-data)}]]
                [:div.fullscreen-post-transcript
                  [:div.fullscreen-post-transcript-header
                    "This transcript was automatically generated and may not be accurate"]
                  [:div.fullscreen-post-transcript-content
                    (:video-transcript current-activity-data)]]))
            (stream-attachments activity-attachments nil
             (when editing #(activity-actions/remove-attachment :modal-editing-data %)))
            (if editing
              [:div.fullscreen-post-box-footer.group
                [:div.fullscreen-post-box-footer-editing
                  [:div.fullscreen-post-box-footer-multi-picker
                    {:id "fullscreen-post-box-footer-multi-picker"}]
                  (emoji-picker {:add-emoji-cb (partial add-emoji-cb s)
                                 :width 20
                                 :height 20
                                 :position "top"
                                 :default-field-selector "div.fullscreen-post div.rich-body-editor"
                                 :container-selector "div.fullscreen-post"})
                  [:div.fullscreen-post-box-footer-legend-container
                    {:on-click #(reset! (::show-legend s) (not @(::show-legend s)))
                     :ref "legend-container"}
                    [:button.mlb-reset.fullscreen-post-box-footer-legend-trigger
                      {:aria-label "Keyboard shortcuts"
                       :title "Shortcuts"
                       :data-toggle "tooltip"
                       :data-placement "top"
                       :data-container "body"}]
                    (when @(::show-legend s)
                      [:div.fullscreen-post-box-footer-legend-image])]]]
                [:div.fullscreen-post-box-footer.group
                  {:class (when (and (pos? (count comments-data))
                                     (> (count (:reactions current-activity-data)) 2))
                            "wrap-reactions")}
                  (comments-summary current-activity-data)
                  (reactions current-activity-data)])]]
        ;; Right column
        (when (:has-comments activity-data)
          [:div.fullscreen-post-right-column.group
            {:class (utils/class-set {:add-comment-focused (:add-comment-focus modal-data)
                                      :no-comments (zero? (count comments-data))})}
            (when (:can-comment activity-data)
              (add-comment activity-data))
            (stream-comments activity-data comments-data)])]]))