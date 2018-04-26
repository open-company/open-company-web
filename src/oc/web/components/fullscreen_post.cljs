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
            [oc.web.components.ui.stream-view-attachments :refer (stream-view-attachments)]))

;; Unsaved edits handling

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
  (let [ap-initial-at (:ap-initial-at @(drv/get-ref s :fullscreen-post-data))]
    (when-not (:from-all-posts @router/path)
      ;; Make sure the seen-at is not reset when navigating back to the board so NEW is still visible
      (dis/dispatch! [:input [:no-reset-seen-at] true])))
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
  (when @(::autosave-timer state)
    (.clearInterval js/window @(::autosave-timer state)))
  (reset! (::autosave-timer state) (utils/every 5000 #(autosave state)))
  (.click (js/$ "div.rich-body-editor a") #(.stopPropagation %))
  (when focus
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
  (.clearInterval js/window @(::autosave-timer state))
  (reset! (::autosave-timer state) nil)
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
                     (activity-actions/entry-clear-local-cache (:uuid (:modal-editing-data modal-data))
                      :modal-editing-data)
                     (stop-editing state)
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

(rum/defcs fullscreen-post < rum/reactive
                             ;; Derivatives
                             (drv/drv :fullscreen-post-data)
                             ;; Locals
                             (rum/local false ::dismiss)
                             (rum/local false ::animate)
                             (rum/local false ::showing-dropdown)
                             (rum/local false ::move-activity)
                             (rum/local nil ::window-click)
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
                               s)
                              :will-unmount (fn [s]
                               (when @(::window-click s)
                                 (events/unlistenByKey @(::window-click s))
                                 (reset! (::window-click s) nil))
                               (when @(::headline-input-listener s)
                                 (events/unlistenByKey @(::headline-input-listener s))
                                 (reset! (::headline-input-listener s) nil))
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
        show-sections-picker (and editing (:show-sections-picker modal-data))]
    [:div.fullscreen-post-container.group
      {:class (utils/class-set {:will-appear (or @(::dismiss s)
                                                 (and @(::animate s)
                                                      (not @(:first-render-done s))))
                                :appear (and (not @(::dismiss s)) @(:first-render-done s))
                                :editing editing
                                :no-comments (not (:has-comments activity-data))})}
      [:div.fullscreen-post-header
        [:button.mlb-reset.mobile-modal-close-bt
          {:on-click #(if editing
                        (dismiss-editing? s (:dismiss-modal-on-editing-stop modal-data))
                        (close-clicked s))}]
        [:div.header-title-editing-post
          [:div.header-title-container-edit-pen]
          [:div.header-title-container-edit-title
            "Edit post"]]
        [:div.header-title-container.group.fs-hide
          {:style {:opacity (if (and (not editing) (:first-render-done s)) "1" "0")
                   :margin-left (when (and (not is-mobile?) (:first-render-done s))
                                  (str "-" (/ (.width (js/$ "div.header-title-container")) 2) "px"))}}
          (user-avatar-image (:publisher activity-data))
          [:div.header-title
            {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]
          [:div.header-timing
            [:time
              {:date-time (:published-at activity-data)
               :data-toggle "tooltip"
               :data-placement "top"
               :data-title (utils/activity-date-tooltip activity-data)}
              (utils/time-since (:published-at activity-data))]]]
        [:div.fullscreen-post-header-right
          (if editing
            [:button.mlb-reset.post-publish-bt
              {:on-click (fn [] (utils/after 1000 #(save-editing? s)))
               :disabled (zero? (count (:headline activity-editing)))
               :class (when @(::entry-saving s) "loading")}
              "Post changes"]
            (more-menu activity-data))]]
      [:div.fullscreen-post.group
        {:ref "fullscreen-post"}
        (when is-mobile?
          [:div.fullscreen-post-author-header.group
            (user-avatar-image (:publisher activity-data))
            [:div.fullscreen-post-author-header-title
              (:name (:publisher activity-data))]
            [:div.fullscreen-post-author-header-subtitle
              [:time
                {:date-time (:published-at activity-data)
                 :title (utils/activity-date-tooltip activity-data)}
                (utils/time-since (:published-at activity-data))]]])
        ;; Left column
        [:div.fullscreen-post-left-column
          [:div.fullscreen-post-left-column-content.group
            (if editing
              [:div.fullscreen-post-box-content-board.section-editing.group
                [:span.posting-in-span
                  "Posted in "]
                [:div.boards-dropdown-caret
                  [:div.board-name
                    {:on-click #(dis/dispatch! [:input [:show-sections-picker] (not show-sections-picker)])}
                    (:board-name activity-editing)]
                  (when show-sections-picker
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
                                                  :invite-note note})])))))]]
              [:div.fullscreen-post-box-content-board
                {:dangerouslySetInnerHTML (utils/emojify (str "Posted in " (:board-name activity-data)))}])
            [:div.fullscreen-post-box-content-headline.fs-hide
              {:content-editable editing
               :ref "edit-headline"
               :class (utils/class-set {:emoji-autocomplete editing
                                        :emojiable editing})
               :placeholder utils/default-headline
               :on-paste    #(headline-on-paste s %)
               :on-key-down #(headline-on-change s)
               :on-click    #(headline-on-change s)
               :on-key-press (fn [e]
                             (when (= (.-key e) "Enter")
                               (utils/event-stop e)
                               (utils/to-end-of-content-editable (sel1 [:div.rich-body-editor]))))
               :dangerouslySetInnerHTML @(::initial-headline s)}]
            (if editing
              (rich-body-editor {:on-change #(body-on-change s)
                                 :initial-body @(::initial-body s)
                                 :show-placeholder true
                                 :dispatch-input-key :modal-editing-data
                                 :upload-progress-cb #(reset! (::uploading-media s) %)
                                 :media-config ["photo" "video"]
                                 :classes "emoji-autocomplete emojiable"
                                 :use-inline-media-picker false
                                 :multi-picker-container-selector "div#fullscreen-post-box-footer-multi-picker"})
              [:div.fullscreen-post-box-content-body.fs-hide
                {:dangerouslySetInnerHTML (utils/emojify (:body activity-data))
                 :content-editable false
                 :class (when (empty? (:headline activity-data)) "no-headline")}])
            (stream-view-attachments activity-attachments
             (when editing #(activity-actions/remove-attachment :modal-editing-data %)))
            [:div.fullscreen-post-box-footer.group
              (if editing
                [:div.fullscreen-post-box-footer-editing
                  [:div.fullscreen-post-box-footer-multi-picker
                    {:id "fullscreen-post-box-footer-multi-picker"}]
                  (emoji-picker {:add-emoji-cb (partial add-emoji-cb s)
                                 :width 20
                                 :height 20
                                 :position "bottom"
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
                      [:div.fullscreen-post-box-footer-legend-image])]]
                (reactions activity-data))]]]
        ;; Right column
        (when (:has-comments activity-data)
          (let [activity-comments (-> modal-data
                                      :comments-data
                                      (get (:uuid activity-data))
                                      :sorted-comments)
                comments-data (or activity-comments (:comments activity-data))]
            [:div.fullscreen-post-right-column.group
              {:class (utils/class-set {:add-comment-focused (:add-comment-focus modal-data)
                                        :no-comments (zero? (count comments-data))})
               :style {:right (when-not is-mobile? (str (/ (- (.-clientWidth (.-body js/document)) 1060) 2) "px"))}}
              (when (:can-comment activity-data)
                (add-comment activity-data))
              (stream-comments activity-data comments-data)]))]]))