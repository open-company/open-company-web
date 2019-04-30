(ns oc.web.components.cmail
  (:require [rum.core :as rum]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [taoensso.timbre :as timbre]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.utils.activity :as au]
            [oc.web.utils.ui :as ui-utils]
            [oc.web.local-settings :as ls]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.actions.qsg :as qsg-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.rich-body-editor :refer (rich-body-editor)]
            [oc.web.components.ui.sections-picker :refer (sections-picker)]
            [oc.web.components.ui.ziggeo :refer (ziggeo-player ziggeo-recorder)]
            [oc.web.components.ui.stream-attachments :refer (stream-attachments)]))

(defn- body-element []
  (sel1 [:div.rich-body-editor]))

(defn- cleaned-body []
  (when-let [body-el (body-element)]
    (utils/clean-body-html (.-innerHTML body-el))))

(defn real-close []
  (activity-actions/cmail-hide))

;; Local cache for outstanding edits

(defn remove-autosave [s]
  (when @(::autosave-timer s)
    (.clearInterval js/window @(::autosave-timer s))
    (reset! (::autosave-timer s) nil)))

(defn autosave [s]
  (let [cmail-data @(drv/get-ref s :cmail-data)
        section-editing @(drv/get-ref s :section-editing)]
    (activity-actions/entry-save-on-exit :cmail-data cmail-data (cleaned-body) section-editing)))

;; Close dismiss handling

(defn cancel-clicked [s]
  (let [cmail-data @(drv/get-ref s :cmail-data)
        clean-fn (fn [dismiss-modal?]
                    (remove-autosave s)
                    (activity-actions/entry-clear-local-cache (:uuid cmail-data) :cmail-data cmail-data)
                    (when dismiss-modal?
                      (alert-modal/hide-alert))
                    (real-close))]
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
      (if (:has-changes cmail-data)
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
  (dis/dispatch! [:input [:cmail-data :has-changes] true]))

(defn- headline-on-change [state]
  (when-let [headline (rum/ref-node state "headline")]
    (let [emojied-headline (.-innerText headline)]
      (dis/dispatch! [:update [:cmail-data] #(merge % {:headline emojied-headline
                                                       :has-changes true})]))))

(defn- abstract-on-change [state]
  (let [abstract (rum/ref-node state "abstract")]
    (dis/dispatch! [:update [:cmail-data] #(merge % {:abstract (.-value abstract)
                                                     :has-changes true})])))

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
    (when (= (.-activeElement js/document) (.-body js/document))
      (when-let [headline-el (rum/ref-node state "headline")]
        ; move cursor at the end
        (utils/to-end-of-content-editable headline-el)))))

(defn add-emoji-cb [s]
  (headline-on-change s)
  (abstract-on-change s)
  (body-on-change s))

(defn- clean-body [s]
  (when-let [body-el (sel1 [:div.rich-body-editor])]
    (dis/dispatch! [:input [:cmail-data :body] (utils/clean-body-html (.-innerHTML body-el))]))
  (when ls/oc-enable-transcriptions
    (let [editing-data @(drv/get-ref s :cmail-data)]
      (when (:fixed-video-id editing-data)
        (when-let [transcription-el (rum/ref-node s "transcript-edit")]
          (dis/dispatch! [:update [:cmail-data] #(merge % {:video-transcript (.-value transcription-el)})]))))))

(defn- fix-headline [cmail-data]
  (utils/trim (:headline cmail-data)))

(defn- fix-abstract [cmail-data]
  (utils/trim (:abstract cmail-data)))

(defn- is-publishable? [s cmail-data]
  (and (seq (:board-slug cmail-data))
       (or (and @(::record-video s)
                @(::video-uploading s))
           (not @(::record-video s)))
       (not (zero? (count (fix-headline cmail-data))))))

(defn video-record-clicked [s]
  (nux-actions/dismiss-edit-tooltip)
  (let [cmail-data @(drv/get-ref s :cmail-data)
        start-recording-fn #(do
                              (reset! (::record-video s) true)
                              (reset! (::video-picking-cover s) false)
                              (reset! (::video-uploading s) false))]
    (cond
      (:fixed-video-id cmail-data)
      (activity-actions/prompt-remove-video :cmail-data cmail-data)
      @(::record-video s)
      (reset! (::record-video s) false)
      :else
      (start-recording-fn))))

(defn show-post-error [s message]
  (let [is-mobile? (responsive/is-tablet-or-mobile?)
        post-bt-ref (if is-mobile? "mobile-post-btn" "post-btn")]
    (when-let [$post-btn (js/$ (rum/ref-node s post-bt-ref))]
      (if-not (.data $post-btn "bs.tooltip")
        (.tooltip $post-btn
         (clj->js {:container "body"
                   :placement (if is-mobile? "bottom" "top")
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
      (utils/after 5000 #(.tooltip $post-btn "hide")))))

(defn real-post-action [s]
  (let [cmail-data @(drv/get-ref s :cmail-data)
        fixed-headline (fix-headline cmail-data)
        fixed-abstract (fix-abstract cmail-data)
        published? (= (:status cmail-data) "published")]
      (if (is-publishable? s cmail-data)
        (let [_ (dis/dispatch! [:update [:cmail-data] #(merge % {:headline fixed-headline :abstract fixed-abstract})])
              updated-cmail-data @(drv/get-ref s :cmail-data)
              section-editing @(drv/get-ref s :section-editing)]
          (qsg-actions/finish-create-post-trail)
          (qsg-actions/turn-on-show-guide)
          (remove-autosave s)
          (if published?
            (do
              (reset! (::saving s) true)
              (activity-actions/entry-save :cmail-data updated-cmail-data section-editing))
            (do
              (reset! (::publishing s) true)
              (activity-actions/entry-publish (dissoc updated-cmail-data :status) section-editing :cmail-data))))
        (do
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
            (show-post-error s "Please finish video recording."))
          (reset! (::disable-post s) false)))))

(defn- disable-post-bt? [s]
  (let [cmail-data @(drv/get-ref s :cmail-data)]
    (or @(::publishing s)
        (not (is-publishable? s cmail-data))
        @(::disable-post s))))

(defn post-clicked [s]
  (clean-body s)
  (let [disabled? (disable-post-bt? s)]
    (when-not disabled?
      (reset! (::disable-post s) true)
      (let [cmail-data @(drv/get-ref s :cmail-data)]
        (if (:auto-saving cmail-data)
          (reset! (::publish-after-autosave s) true)
          (real-post-action s))))))

(defn fix-tooltips [s]
  (doto (.find (js/$ (rum/dom-node s)) "[data-toggle=\"tooltip\"]")
    (.tooltip "hide")
    (.tooltip "fixTitle")))

;; Delete handling

(defn delete-clicked [s e activity-data]
  (let [post-type (if (= (:status activity-data) "published")
                    "post"
                    "draft")
        alert-data {:icon "/img/ML/trash.svg"
                    :action "delete-entry"
                    :message (str "Delete this " post-type "?")
                    :link-button-title "No"
                    :link-button-cb #(alert-modal/hide-alert)
                    :solid-button-style :red
                    :solid-button-title "Yes"
                    :solid-button-cb #(do
                                       (reset! (::deleting s) true)
                                       (activity-actions/activity-delete activity-data)
                                       (alert-modal/hide-alert)
                                       (real-close))
                    }]
    (alert-modal/show-alert alert-data)))

(defn win-width []
  (or (.-clientWidth (.-documentElement js/document))
      (.-innerWidth js/window)))

(defn calc-video-height [s]
  (when (responsive/is-tablet-or-mobile?)
    (reset! (::mobile-video-height s) (utils/calc-video-height (win-width)))))

(defn edit-tooltip [s]
  [:div.edit-tooltip-container.group
    [:button.mlb-reset.edit-tooltip-dismiss
      {:on-click #(nux-actions/dismiss-edit-tooltip)}]
    [:div.edit-tooltips
      [:div.edit-tooltip
        (str
         "Share something with your team, like an announcement, update, or decision. "
         (when-not (responsive/is-tablet-or-mobile?)
           "Use the buttons below to add images, video, or attachments."))]]])

(rum/defcs cmail < rum/reactive
                   ;; Derivatives
                   (drv/drv :qsg)
                   (drv/drv :cmail-state)
                   (drv/drv :cmail-data)
                   (drv/drv :show-sections-picker)
                   (drv/drv :section-editing)
                   (drv/drv :show-edit-tooltip)
                   ;; Locals
                   (rum/local "" ::initial-body)
                   (rum/local "" ::initial-headline)
                   (rum/local true ::show-placeholder)
                   (rum/local nil ::initial-uuid)
                   (rum/local nil ::headline-input-listener)
                   (rum/local nil ::abstract-input-listener)
                   (rum/local nil ::uploading-media)
                   (rum/local false ::saving)
                   (rum/local false ::publishing)
                   (rum/local false ::disable-post)
                   (rum/local false ::publish-after-autosave)
                   (rum/local nil ::autosave-timer)
                   (rum/local false ::record-video)
                   (rum/local false ::video-uploading)
                   (rum/local false ::video-picking-cover)
                   (rum/local 0 ::mobile-video-height)
                   (rum/local false ::deleting)
                   (rum/local false ::abstract-focused)
                   ;; Mixins
                   (mixins/render-on-resize calc-video-height)
                   (mixins/autoresize-textarea "abstract")

                   {:will-mount (fn [s]
                    (let [cmail-data @(drv/get-ref s :cmail-data)
                          initial-body (if (seq (:body cmail-data))
                                         (:body cmail-data)
                                         "")
                          initial-headline (utils/emojify
                                             (if (seq (:headline cmail-data))
                                               (:headline cmail-data)
                                               ""))]
                      (when-not (seq (:uuid cmail-data))
                        (nux-actions/dismiss-add-post-tooltip))
                      (reset! (::initial-body s) initial-body)
                      (reset! (::initial-headline s) initial-headline)
                      (reset! (::initial-uuid s) (:uuid cmail-data))
                      (reset! (::show-placeholder s) (not (.match initial-body #"(?i).*(<iframe\s?.*>).*"))))
                    s)
                   :did-mount (fn [s]
                    (calc-video-height s)
                    (utils/after 300 #(setup-headline s))
                    (when-let [headline-el (rum/ref-node s "headline")]
                      (utils/to-end-of-content-editable headline-el))
                    (reset! (::autosave-timer s) (utils/every 5000 #(autosave s)))
                    (when ls/oc-enable-transcriptions
                      (ui-utils/resize-textarea (rum/ref-node s "transcript-edit")))
                    s)
                   :did-remount (fn [_ s]
                    (when ls/oc-enable-transcriptions
                      (ui-utils/resize-textarea (rum/ref-node s "transcript-edit")))

                    s)
                   :before-render (fn [s]
                    ;; Handle saving/publishing states to dismiss the component
                    (let [cmail-data @(drv/get-ref s :cmail-data)]
                      ;; Did activity get removed in another client?
                      (when (and @(::deleting s)
                                 (:delete cmail-data))
                        (reset! (::deleting s) false)
                        (real-close))
                      ;; Entry is saving
                      ;: and save request finished
                      (when (and @(::publish-after-autosave s)
                                 (not (:auto-saving cmail-data)))
                        (reset! (::publish-after-autosave s) false)
                        (real-post-action s))
                      (when (and @(::saving s)
                                 (not (:loading cmail-data)))
                        (reset! (::saving s) false)
                        (if (:error cmail-data)
                          (reset! (::disable-post s) false)
                          (real-close)))
                      (when (and @(::publishing s)
                                 (not (:publishing cmail-data)))
                        (reset! (::publishing s) false)
                        (if (:error cmail-data)
                          (reset! (::disable-post s) false)
                          (let [redirect? (seq (:board-slug cmail-data))]
                            ;; Redirect to the publishing board if the slug is available
                            (if redirect?
                              (do
                                (real-close)
                                (utils/after
                                 180
                                 #(let [org-slug (router/current-org-slug)
                                        board-slug (:board-slug cmail-data)
                                        post-url (oc-urls/entry org-slug board-slug (:uuid cmail-data))]
                                    ;; Redirect to AP if coming from it or if the post is not published
                                    (router/nav! post-url))))
                              (reset! (::disable-post s) false))))))
                    s)
                   :after-render (fn [s]
                    (fix-tooltips s)
                    s)
                   :will-unmount (fn [s]
                    (nux-actions/dismiss-edit-tooltip)
                    (when @(::headline-input-listener s)
                      (events/unlistenByKey @(::headline-input-listener s))
                      (reset! (::headline-input-listener s) nil))
                    (when @(::abstract-input-listener s)
                      (events/unlistenByKey @(::abstract-input-listener s))
                      (reset! (::abstract-input-listener s) nil))
                    (remove-autosave s)
                    s)}
  [s]
  (let [is-mobile? (responsive/is-tablet-or-mobile?)
        cmail-state (drv/react s :cmail-state)
        cmail-data (drv/react s :cmail-data)
        show-sections-picker (drv/react s :show-sections-picker)
        published? (= (:status cmail-data) "published")
        video-size (if is-mobile?
                     {:width (win-width)
                      :height @(::mobile-video-height s)}
                     {:width 548
                      :height (utils/calc-video-height 548)})
        show-edit-tooltip (and (drv/react s :show-edit-tooltip)
                               (not (seq @(::initial-uuid s))))
        qsg-data (drv/react s :qsg)
        disabled? (disable-post-bt? s)
        working? (or (and published?
                          @(::saving s))
                     (and (not published?)
                          @(::publishing s)))]
    [:div.cmail-outer
      {:class (utils/class-set {:fullscreen (:fullscreen cmail-state)
                                :showing-qsg (:visible qsg-data)})}
      [:div.cmail-container
        [:div.cmail-header
          (let [long-tooltip (not= (:status cmail-data) "published")]
            [:div.close-bt-container
              {:class (when long-tooltip "long-tooltip")}
              [:button.mlb-reset.close-bt
                {:on-click #(do
                              (if (au/has-content? (assoc cmail-data
                                                     :body
                                                     (cleaned-body)))
                                (autosave s)
                                (do
                                  (reset! (::deleting s) true)
                                  (activity-actions/activity-delete cmail-data)))
                              (if (and (= (:status cmail-data) "published")
                                       (:has-changes cmail-data))
                                (cancel-clicked s)
                                (do
                                  (qsg-actions/turn-on-show-guide)
                                  (activity-actions/cmail-hide))))
                 :data-toggle (if is-mobile? "" "tooltip")
                 :data-placement "top"
                 :data-trigger "hover"
                 :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
                 :title (if long-tooltip
                          "Save & Close"
                          "Close")}]])
          [:div.fullscreen-bt-container
            [:button.mlb-reset.fullscreen-bt
              {:on-click #(activity-actions/cmail-toggle-fullscreen)
               :data-toggle "tooltip"
               :data-placement "top"
               :data-trigger "hover"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title (if (:fullscreen cmail-state)
                        "Shrink"
                        "Expand")}]]
          [:div.cmail-header-vertical-separator]
          (when (and (not= (:status cmail-data) "published")
                     is-mobile?)
            (if (or (:has-changes cmail-data)
                    (:auto-saving cmail-data))
              [:div.mobile-saving-saved "Saving..."]
              (when (false? (:auto-saving cmail-data))
                [:div.mobile-saving-saved "Saved"])))
          [:div.cmail-header-board-must-see-container.group
            {:class (when (:must-see cmail-data) "must-see-on")}
            [:div.board-name
              {:on-click #(when-not (utils/event-inside? % (rum/ref-node s :picker-container))
                            (dis/dispatch! [:input [:show-sections-picker] (not show-sections-picker)]))
               :class (when show-sections-picker "open")}
              [:div.board-name-inner
                (:board-name cmail-data)]]
            (when show-sections-picker
              [:div.section-picker-container
                {:ref :picker-container}
                (sections-picker (:board-slug cmail-data)
                 (fn [board-data note]
                   (dis/dispatch! [:input [:show-sections-picker] false])
                   (when (and board-data
                              (seq (:name board-data)))
                    (dis/dispatch! [:input [:cmail-data]
                     (merge cmail-data {:board-slug (:slug board-data)
                                        :board-name (:name board-data)
                                        :has-changes true
                                        :invite-note note})]))))])
            [:div.must-see-toggle-container
              {:class (when (:must-see cmail-data) "on")}
              [:div.must-see-toggle
                {:on-mouse-down #(activity-actions/cmail-toggle-must-see)
                 :data-toggle "tooltip"
                 :data-placement "top"
                 :data-trigger "hover"
                 :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
                 :title "Must See"}
                [:span.must-see-toggle-circle]]]
            (when (:must-see cmail-data)
              [:div.must-see-tag
                "Must see"])]
          [:button.mlb-reset.post-button
            {:ref "post-btn"
             :on-click #(post-clicked s)
             :class (utils/class-set {:disabled disabled?
                                      :loading working?})}
            (if (= (:status cmail-data) "published")
              "Save"
              "Post")]
          [:button.mlb-reset.mobile-post-button
            {:ref "mobile-post-btn"
             :on-click #(post-clicked s)
             :class (utils/class-set {:disabled disabled?
                                      :loading working?})}
            (when working?
              (small-loading))
            (if (= (:status cmail-data) "published")
              "SAVE"
              "POST")]]
        [:div.cmail-content
          {:class (when show-edit-tooltip "showing-edit-tooltip")}
          (when (and is-mobile?
                     show-edit-tooltip)
            (edit-tooltip s))
          ;; Video elements
          ; FIXME: disable video on mobile for now
          (when-not is-mobile?
            (when (and (:fixed-video-id cmail-data)
                       (not @(::record-video s)))
              (ziggeo-player {:video-id (:fixed-video-id cmail-data)
                              :remove-video-cb #(activity-actions/prompt-remove-video :cmail-data cmail-data)
                              :width (:width video-size)
                              :height (:height video-size)
                              :video-processed (:video-processed cmail-data)})))
          ; FIXME: disable video on mobile for now
          (when-not is-mobile?
            (when @(::record-video s)
              (ziggeo-recorder {:start-cb (partial activity-actions/video-started-recording-cb :cmail-data)
                                :upload-started-cb #(do
                                                      (activity-actions/uploading-video % :cmail-data)
                                                      (reset! (::video-picking-cover s) false)
                                                      (reset! (::video-uploading s) true))
                                :pick-cover-start-cb #(reset! (::video-picking-cover s) true)
                                :pick-cover-end-cb #(reset! (::video-picking-cover s) false)
                                :submit-cb (partial activity-actions/video-processed-cb :cmail-data)
                                :width (:width video-size)
                                :height (:height video-size)
                                :remove-recorder-cb (fn []
                                  (when (:video-id cmail-data)
                                    (activity-actions/remove-video :cmail-data cmail-data))
                                  (reset! (::record-video s) false))})))
          ; Headline element
          [:div.cmail-content-headline.emoji-autocomplete.emojiable.group
            {:class utils/hide-class
             :content-editable true
             :ref "headline"
             :placeholder utils/default-headline
             :on-paste    #(headline-on-paste s %)
             :on-key-down #(headline-on-change s)
             :on-click    #(headline-on-change s)
             :on-key-press (fn [e]
                           (when (= (.-key e) "Enter")
                             (utils/event-stop e)
                             (utils/to-end-of-content-editable (body-element))))
             :dangerouslySetInnerHTML @(::initial-headline s)}]
          ;; Abstract
          [:div.cmail-content-abstract-container
            [:div.cmail-content-abstract-counter
              {:class (when @(::abstract-focused s) "show-counter")}
              (str "Character limit " (count (or (:abstract cmail-data) "")) "/" utils/max-abstrct-length)]
            [:textarea.cmail-content-abstract.emoji-autocomplete.emojiable.group.oc-mentions.oc-mentions-hover
              {:class utils/hide-class
               :ref "abstract"
               :rows 1
               :placeholder utils/default-abstract
               :value (or (:abstract cmail-data) "")
               :max-length utils/max-abstrct-length
               :on-change #(abstract-on-change s)
               :on-focus #(reset! (::abstract-focused s) true)
               :on-blur #(reset! (::abstract-focused s) false)
               ; :on-click    #(abstract-on-change s)
               :on-key-press (fn [e]
                             (when (= (.-key e) "Enter")
                               (utils/event-stop e)
                               (utils/to-end-of-content-editable (sel1 [:div.rich-body-editor]))))}]]
          (rich-body-editor {:on-change (partial body-on-change s)
                             :use-inline-media-picker true
                             :multi-picker-container-selector "div#cmail-footer-multi-picker"
                             :initial-body @(::initial-body s)
                             :show-placeholder @(::show-placeholder s)
                             :show-h2 true
                             :dispatch-input-key :cmail-data
                             :start-video-recording-cb #(video-record-clicked s)
                             :upload-progress-cb (fn [is-uploading?]
                                                   (reset! (::uploading-media s) is-uploading?))
                             :media-config ["photo" "video"]
                             :classes (str "emoji-autocomplete emojiable " utils/hide-class)})
          (when (and ls/oc-enable-transcriptions
                     (:fixed-video-id cmail-data)
                     (:video-processed cmail-data))
            [:div.cmail-data-transcript
              [:textarea.video-transcript
                {:ref "transcript-edit"
                 :on-input #(ui-utils/resize-textarea (.-target %))
                 :default-value (:video-transcript cmail-data)}]])
          ; Attachments
          (stream-attachments (:attachments cmail-data) nil
           #(activity-actions/remove-attachment :cmail-data %))
          (when (and (not is-mobile?)
                     show-edit-tooltip)
            (edit-tooltip s))]
      [:div.cmail-footer
        [:div.cmail-footer-multi-picker
          {:id "cmail-footer-multi-picker"}]
        (emoji-picker {:add-emoji-cb (partial add-emoji-cb s)
                       :width 24
                       :height 24
                       :position "top"
                       :default-field-selector "div.cmail-content div.rich-body-editor"
                       :container-selector "div.cmail-content"})
        [:div.cmail-footer-right
          [:div.delete-button-container
            [:button.mlb-reset.delete-button
              {:title (if (= (:status cmail-data) "published") "Delete post" "Delete draft")
               :data-toggle "tooltip"
               :data-placement "top"
               :data-trigger "hover"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :on-click #(delete-clicked s % cmail-data)}]]]
        (when (and (not= (:status cmail-data) "published")
                   (not is-mobile?))
          (if (or (:has-changes cmail-data)
                  (:auto-saving cmail-data))
            [:div.saving-saved "Saving..."]
            (when (false? (:auto-saving cmail-data))
              [:div.saving-saved "Saved"])))]]]))