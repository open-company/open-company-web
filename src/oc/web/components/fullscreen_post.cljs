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
            [oc.web.utils.activity :as au]
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
            activity-data (:modal-editing-data modal-data)
            cleaned-body (when body-el
                          (utils/clean-body-html (.-innerHTML body-el)))]
        (activity-actions/entry-save-on-exit :modal-editing-data activity-data cleaned-body)))))

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
  (remove-autosave state)
  (activity-actions/activity-modal-edit (:activity-data @(drv/get-ref state :fullscreen-post-data)) false)
  (when @(::headline-input-listener state)
    (events/unlistenByKey @(::headline-input-listener state))
    (reset! (::headline-input-listener state) nil)))

(defn- clean-body []
  (when-let [body-el (sel1 [:div.rich-body-editor])]
    (let [raw-html (.-innerHTML body-el)]
      (dis/dispatch! [:update [:modal-editing-data] #(merge % {:body (utils/clean-body-html raw-html)
                                                               :has-changes true})]))))

(defn- save-editing? [state]
  (clean-body)
  (let [modal-data @(drv/get-ref state :fullscreen-post-data)
        section-editing (:section-editing modal-data)
        edited-data (:modal-editing-data modal-data)]
    (when (and (:has-changes edited-data)
               (pos? (count (:headline edited-data))))
      (reset! (::entry-saving state) true)
      (activity-actions/entry-modal-save edited-data (router/current-board-slug) section-editing))))

(defn- dismiss-editing? [state dismiss-modal?]
  (let [modal-data @(drv/get-ref state :fullscreen-post-data)
        dismiss-fn (fn [dismiss-alert?]
                     (when dismiss-alert?
                       (alert-modal/hide-alert))
                     (stop-editing state)
                     (activity-actions/entry-clear-local-cache (:uuid (:modal-editing-data modal-data))
                      :modal-editing-data)
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
                             ;; Mixins
                             (when-not (responsive/is-mobile-size?)
                               mixins/no-scroll-mixin)
                             (mixins/render-on-resize nil)
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
                                         (activity-actions/entry-clear-local-cache (:uuid activity-data)
                                          :modal-editing-data)
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
        delete-link (utils/link-for (:links activity-data) "delete")
        edit-link (utils/link-for (:links activity-data) "partial-update")
        share-link (utils/link-for (:links activity-data) "share")
        editing (:modal-editing modal-data)
        activity-editing (:modal-editing-data modal-data)
        activity-attachments (if editing (:attachments activity-editing) (:attachments activity-data))
        show-sections-picker (and editing (:show-sections-picker modal-data))
        dom-element-id (str "fullscreen-post-" (:uuid activity-data))
        activity-comments (-> modal-data
                              :comments-data
                              (get (:uuid activity-data))
                              :sorted-comments)
        comments-data (or activity-comments (:comments activity-data))
        read-data (:read-data modal-data)]
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
          {:key (:updated-at activity-data)
           :dangerouslySetInnerHTML (utils/emojify (:headline (if editing activity-editing activity-data)))}]
        [:div.fullscreen-post-header-right
          [:div.activity-share-container]
          (if editing
            [:button.mlb-reset.post-publish-bt
              {:on-click (fn [] (utils/after 1000 #(save-editing? s)))
               :disabled (zero? (count (:headline activity-editing)))
               :class (when @(::entry-saving s) "loading")}
              "SAVE"]
            (more-menu activity-data dom-element-id {:tooltip-position "left" :external-share true}))]]
      [:div.fullscreen-post.group
        {:ref "fullscreen-post"}
        (if editing
          [:div.fullscreen-post-author-header.section-editing.group
            [:div.fullscreen-post-author-header-author
              {:on-click #(when-not (utils/event-inside? % (rum/ref-node s :picker-container))
                            (dis/dispatch! [:input [:show-sections-picker] (not show-sections-picker)]))}
              (user-avatar-image (:publisher activity-data))
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
                                                  :invite-note note})]))))])]]]
          [:div.fullscreen-post-author-header.group
            [:div.fullscreen-post-author-header-author
              (user-avatar-image (:publisher activity-data))
              [:div.name.fs-hide
                (str (:name (:publisher activity-data))
                 " in "
                 (:board-name activity-data))]
              [:div.fullscreen-post-author-header-sub
                [:div.time-since
                  (let [t (or (:published-at activity-data) (:created-at activity-data))]
                    [:time
                      {:date-time t
                       :data-toggle (when-not is-mobile? "tooltip")
                       :data-placement "top"
                       :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
                       :data-title (utils/activity-date-tooltip activity-data)}
                      (utils/time-since t)])]
                [:div.separator]
                [:div.fullscreen-post-wrt
                  (wrt activity-data read-data)]]]
            [:div.fullscreen-post-author-header-right
              (when (:new activity-data)
                [:div.new-tag
                  "New"])]])
        ;; Left column
        [:div.fullscreen-post-left-column
          [:div.fullscreen-post-left-column-content.group
            (if editing
              [:div.fullscreen-post-box-content-headline.emoji-autocomplete.emojiable.fs-hide
                {:content-editable true
                 :ref "edit-headline"
                 :key (str "fullscreen-post-headline-edit-" (:updated-at activity-data))
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
            (when (and (:must-see activity-data)
                       (not editing))
              [:div.must-see
               {:class (utils/class-set {:must-see-on (:must-see activity-data)})}])
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
                {:key (str "fullscreen-post-body-" (:updated-at activity-data))
                 :ref :fullscreen-post-box-content-body
                 :dangerouslySetInnerHTML (utils/emojify (:body activity-data))}])
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
                                     (> (count (:reactions activity-data)) 2))
                            "wrap-reactions")}
                  (comments-summary activity-data)
                  (reactions activity-data)])]]
        ;; Right column
        (when (:has-comments activity-data)
          [:div.fullscreen-post-right-column.group
            {:class (utils/class-set {:add-comment-focused (:add-comment-focus modal-data)
                                      :no-comments (zero? (count comments-data))})}
            (when (:can-comment activity-data)
              (add-comment activity-data))
            (stream-comments activity-data comments-data)])]]))