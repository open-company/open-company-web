(ns oc.web.components.capture-video
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
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.ziggeo :refer (ziggeo-recorder)]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.rich-body-editor :refer (rich-body-editor)]
            [oc.web.components.ui.sections-picker :refer (sections-picker)]
            [oc.web.components.ui.stream-attachments :refer (stream-attachments)]))

(defn real-close [s]
  (utils/after 180 activity-actions/capture-video-dismiss))

(defn cancel-clicked [s]
  (real-close s))

;; Data handling

(defn remove-autosave [s]
  (when @(::autosave-timer s)
    (.clearInterval js/window @(::autosave-timer s))
    (reset! (::autosave-timer s) nil)))

(defn autosave [s]
  (let [entry-editing @(drv/get-ref s :capture-video)
        body-el (sel1 [:div.rich-body-editor])
        cleaned-body (when body-el
                      (utils/clean-body-html (.-innerHTML body-el)))]
    (activity-actions/entry-save-on-exit :capture-video entry-editing cleaned-body)))

(defn save-on-exit?
  "Locally save the current outstanding edits if needed."
  [s]
  (when @(drv/get-ref s :entry-save-on-exit)
    (autosave s)))

(defn toggle-save-on-exit
  "Enable and disable save current edit."
  [s turn-on?]
  (activity-actions/entry-toggle-save-on-exit turn-on?))

(defn body-on-change [state]
  (toggle-save-on-exit state true)
  (dis/dispatch! [:input [:capture-video :has-changes] true]))

(def headline-placeholder "Untitled video post")

(defn- headline-on-change [state]
  (toggle-save-on-exit state true)
  (utils/after 100
   (fn []
    (when-let [headline (rum/ref-node state "headline")]
     (let [emojied-headline (.-innerText headline)]
       (dis/dispatch! [:update [:capture-video] #(merge % {:headline emojied-headline
                                                           :has-changes true})])
       (reset! (::title state) (if (seq emojied-headline)
                                 emojied-headline
                                 headline-placeholder)))))))

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

(defn- clean-body []
  (when-let [body-el (sel1 [:div.rich-body-editor])]
    (dis/dispatch! [:input [:capture-video :body] (utils/clean-body-html (.-innerHTML body-el))])))

(defn- is-publishable? [entry-editing]
  (seq (:board-slug entry-editing)))


;; Video callbacks

(defn update-video-token [video-token]
  (dis/dispatch! [:update [:capture-video] #(merge % {:video-id video-token
                                                      :has-changes true})]))

(defn video-record-started [s video-token]
  (reset! (::title s) [:div.rec [:span.recording] "Recording"])
  (update-video-token video-token))

(defn video-record-stopped [s]
  (reset! (::title s) [:span "Pick a cover shot..."]))true
(rum/defcs capture-video < rum/reactive
                           (drv/drv :current-user-data)
                           (drv/drv :show-sections-picker)
                           (drv/drv :capture-video)
                           (drv/drv :entry-save-on-exit)
                           (drv/drv :section-editing)
                           (rum/local "" ::initial-body)
                           (rum/local "" ::initial-headline)
                           (rum/local [:span "New video post"] ::title)
                           (rum/local false ::show-good-human-banner)
                           (rum/local false ::show-post-editor)
                           (rum/local false ::show-legend)
                           (rum/local nil ::autosave-timer)
                           (rum/local nil ::window-click-listener)
                           (rum/local nil ::headline-input-listener)
                           (rum/local nil ::uploading-media)
                           (rum/local false ::saving)
                           (rum/local false ::publishing)
                           (rum/local false ::blue-header)
                           {:will-mount (fn [s]
                            (let [entry-editing @(drv/get-ref s :capture-video)
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
                            s)
                           :before-render (fn [s]
                            ;; Set or remove the onBeforeUnload prompt
                            (let [save-on-exit @(drv/get-ref s :entry-save-on-exit)]
                              (set! (.-onbeforeunload js/window)
                               (if save-on-exit
                                #(do
                                  (save-on-exit? s)
                                  "Do you want to save before leaving?")
                                nil)))
                            ;; Handle saving/publishing states to dismiss the component
                            (let [entry-editing @(drv/get-ref s :capture-video)]
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
                            (set! (.-onbeforeunload js/window) nil)
                            s)}
  [s]
  (let [current-user-data (drv/react s :current-user-data)
        show-sections-picker (drv/react s :show-sections-picker)
        entry-editing (drv/react s :capture-video)
        posting-title (if (:uuid entry-editing)
                        (if (= (:status entry-editing) "published")
                          "Posted to: "
                          "Draft for: ")
                        "Posting to: ")
        published? (= (:status entry-editing) "published")]
    [:div.capture-video
      [:div.capture-video-header
        {:class (when @(::blue-header s) "editing")}
        [:button.mlb-reset.modal-close-bt
          {:on-click #(cancel-clicked s)}]
        [:div.capture-video-title
          @(::title s)]
        (when @(::blue-header s)
          (let [should-show-save-button? (and (not @(::publishing s))
                                              (not published?))]
            [:div.capture-video-header-right
              (let [fixed-headline (utils/trim (:headline entry-editing))
                    disabled? (or @(::publishing s)
                                  (not (is-publishable? entry-editing))
                                  (zero? (count fixed-headline)))
                    working? (or (and published?
                                      @(::saving s))
                                 (and (not published?)
                                      @(::publishing s)))]
                [:button.mlb-reset.header-buttons.post-button
                  {:ref "mobile-post-btn"
                   :on-click (fn [_]
                               (clean-body)
                               (if (and (is-publishable? entry-editing)
                                        (not (zero? (count fixed-headline))))
                                  (let [_ (dis/dispatch! [:input [:capture-video :headline] fixed-headline])
                                        updated-entry-editing @(drv/get-ref s :capture-video)
                                        section-editing @(drv/get-ref s :section-editing)]
                                    (remove-autosave s)
                                    (if published?
                                      (do
                                        (reset! (::saving s) true)
                                        (activity-actions/entry-save updated-entry-editing section-editing :capture-video))
                                      (do
                                        (reset! (::publishing s) true)
                                        (activity-actions/entry-publish (dissoc updated-entry-editing :status) section-editing :capture-video))))
                                  (when (zero? (count fixed-headline))
                                    (when-let [$post-btn (js/$ (rum/ref-node s "mobile-post-btn"))]
                                      (when-not (.data $post-btn "bs.tooltip")
                                        (.tooltip $post-btn
                                         (clj->js {:container "body"
                                                   :placement "bottom"
                                                   :trigger "manual"
                                                   :template (str "<div class=\"tooltip post-btn-tooltip\">"
                                                                    "<div class=\"tooltip-arrow\"></div>"
                                                                    "<div class=\"tooltip-inner\"></div>"
                                                                  "</div>")
                                                   :title "A title is required in order to save or share this post."})))
                                      (utils/after 10 #(.tooltip $post-btn "show"))
                                      (utils/after 5000 #(.tooltip $post-btn "hide"))))))
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
                                    (not (:has-changes entry-editing)))
                      working? @(::saving s)]
                  [:button.mlb-reset.header-buttons.save-button
                    {:class (utils/class-set {:disabled disabled?
                                              :loading working?})
                     :on-click (fn [_]
                                (when-not disabled?
                                  (remove-autosave s)
                                  (clean-body)
                                  (reset! (::saving s) true)
                                  (activity-actions/entry-save (assoc @(drv/get-ref s :capture-video) :status "draft") @(drv/get-ref s :section-editing))))}
                    (when working?
                      (small-loading))
                    "Save to draft"]))]))]
      [:div.capture-video-body
        (ziggeo-recorder {:start-cb (partial video-record-started s)
                          :pick-cover-start-cb (partial video-record-stopped s)
                          :rerecord-cb #(do
                                         (reset! (::show-good-human-banner s) false)
                                         (reset! (::show-post-editor s) false)
                                         (reset! (::blue-header s) false)
                                         (reset! (::title s) [:span "Rerecord video post"]))
                          :upload-started-cb #(do
                                                (when (activity-actions/should-show-good-human-banner?)
                                                  (reset! (::show-good-human-banner s) true))
                                                (reset! (::show-post-editor s) true)
                                                (reset! (::blue-header s) true)
                                                (reset! (::title s) (if (seq @(::initial-body s))
                                                                      (gobj/get @(::initial-headline s) "__html")
                                                                      headline-placeholder)))})
        (when @(::show-good-human-banner s)
          [:div.good-human-banner.group
            [:div.good-human-banner-title
              [:span.smile-emo]
              "Be a good person and provide a summary for your video k?"]
            [:button.remove-button.mlb-reset
              {:on-click #(do
                            (activity-actions/hide-good-human-banner!)
                            (reset! (::show-good-human-banner s) false))}]])
        (when @(::show-post-editor s)
          [:div.post-editor.group
            {:class (utils/class-set {:move-up (not @(::show-good-human-banner s))})}
            [:div.post-editor-header.group
              (user-avatar-image current-user-data)
              [:div.posting-in
                {:on-click #(when-not (utils/event-inside? % (rum/ref-node s :picker-container))
                              (dis/dispatch! [:input [:show-sections-picker] (not show-sections-picker)]))}
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
                        (dis/dispatch! [:input [:capture-video]
                         (merge entry-editing {:board-slug (:slug board-data)
                                               :board-name (:name board-data)
                                               :has-changes true
                                               :invite-note note})]))))])]]
            [:div.post-editor-headline.emoji-autocomplete.emojiable.group.fs-hide
              {:content-editable true
               :ref "headline"
               :placeholder headline-placeholder
               :on-paste    #(headline-on-paste s %)
               :on-key-down #(headline-on-change s)
               :on-click    #(headline-on-change s)
               :on-key-press (fn [e]
                             (when (= (.-key e) "Enter")
                               (utils/event-stop e)
                               (utils/to-end-of-content-editable (sel1 [:div.rich-body-editor]))))
               :dangerouslySetInnerHTML @(::initial-headline s)}]
            (rich-body-editor {:on-change (partial body-on-change s)
                               :use-inline-media-picker false
                               :multi-picker-container-selector "div#post-editor-footer-multi-picker"
                               :initial-body @(::initial-body s)
                               :show-placeholder (not (contains? entry-editing :links))
                               :placeholder "Use this to provide depth to your video content, or hyperlink related resources…"
                               :show-h2 true
                               :dispatch-input-key :capture-video
                               :upload-progress-cb (fn [is-uploading?]
                                                     (reset! (::uploading-media s) is-uploading?))
                               :media-config ["photo" "video"]
                               :classes "emoji-autocomplete emojiable fs-hide"})
            ; Attachments
            (stream-attachments (:attachments entry-editing) nil
             #(activity-actions/remove-attachment :capture-video %))
            [:div.post-editor-footer.group
              [:div.post-editor-footer-multi-picker
                {:id "post-editor-footer-multi-picker"}]
              (emoji-picker {:add-emoji-cb (partial add-emoji-cb s)
                             :width 20
                             :height 20
                             :position "top"
                             :default-field-selector "div.post-editor div.rich-body-editor"
                             :container-selector "div.post-editor"})
              [:div.post-editor-legend-container
                {:on-click #(reset! (::show-legend s) (not @(::show-legend s)))
                 :ref "legend-container"}
                [:button.mlb-reset.post-editor-legend-trigger
                  {:aria-label "Keyboard shortcuts"
                   :title "Shortcuts"
                   :data-toggle "tooltip"
                   :data-placement "top"
                   :data-container "body"}]
                (when @(::show-legend s)
                  [:div.post-editor-legend-image])]]])]]))