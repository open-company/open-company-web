(ns oc.web.components.activity-modal
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as string]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.mixins.ui :as mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.ui.activity-move :refer (activity-move)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.rich-body-editor :refer (rich-body-editor)]
            [oc.web.components.ui.activity-attachments :refer (activity-attachments)]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.comments :refer (comments)]))

;; Unsaved edits handling

(defn autosave []
  (when-let [body-el (sel1 [:div.rich-body-editor])]
    (let [cleaned-body (when body-el
                        (utils/clean-body-html (.-innerHTML body-el)))]
      (dis/dispatch! [:entry-save-on-exit :modal-editing-data cleaned-body]))))

(defn save-on-exit?
  "Locally save the current outstanding edits if needed."
  [s]
  (when (:entry-save-on-exit @(drv/get-ref s :modal-data))
    (autosave)))

(defn toggle-save-on-exit
  "Enable and disable save current edit."
  [s turn-on?]
  (dis/dispatch! [:entry-toggle-save-on-exit turn-on?]))

;; Modal dismiss handling

(defn dismiss-modal [s]
  (let [org (router/current-org-slug)
        board (router/current-board-slug)
        modal-data @(drv/get-ref s :modal-data)]
    (router/nav!
      (if (:from-all-posts @router/path)
        (oc-urls/all-posts org)
        (oc-urls/board org board)))))

(defn close-clicked [s]
  (let [ap-initial-at (:ap-initial-at @(drv/get-ref s :modal-data))]
    (if (:from-all-posts @router/path)
      ;; Remove AP data from the DB to avoid showing results before loading and results again
      (when ap-initial-at
        (dis/dispatch! [:all-posts-reset]))
      ;; Make sure the seen-at is not reset when navigating back to the board so NEW is still visible
      (dis/dispatch! [:input [:no-reset-seen-at] true])))
  (dis/dispatch! [:input [:dismiss-modal-on-editing-stop] false])
  (reset! (::dismiss s) true)
  (utils/after 180 #(dismiss-modal s)))

;; Delete handling

(defn delete-clicked [e activity-data]
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :action "delete-entry"
                    :message (str "Delete this update?")
                    :link-button-title "No"
                    :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                    :solid-button-title "Yes"
                    :solid-button-cb #(let [org-slug (router/current-org-slug)
                                            board-slug (router/current-board-slug)
                                            board-url (oc-urls/board org-slug board-slug)]
                                       (router/nav! board-url)
                                       (dis/dispatch! [:activity-delete activity-data])
                                       (dis/dispatch! [:alert-modal-hide]))
                    }]
    (dis/dispatch! [:alert-modal-show alert-data])))

;; Editing

(defn body-on-change [state]
  (toggle-save-on-exit state true)
  (dis/dispatch! [:input [:modal-editing-data :has-changes] true]))

(defn- headline-on-change [state]
  (toggle-save-on-exit state true)
  (when-let [headline (rum/ref-node state "edit-headline")]
    (let [emojied-headline (utils/emoji-images-to-unicode
                            (gobj/get (utils/emojify (.-innerHTML headline)) "__html"))]
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
    (when-let [headline-el   (rum/ref-node state "edit-headline")]
      ; move cursor at the end
      (utils/to-end-of-content-editable headline-el))))

(defn- add-emoji-cb [state]
  (headline-on-change state)
  (when-let [body (sel1 [:div.rich-body-editor])]
    (body-on-change state)))

(defn- real-start-editing [state & [focus]]
  (when-not (responsive/is-tablet-or-mobile?)
    (dis/dispatch! [:activity-modal-edit (:activity-data @(drv/get-ref state :modal-data)) true])
    (utils/after 100 #(setup-headline state))
    (reset! (::autosave-timer state) (utils/every 5000 autosave))
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
             (utils/to-end-of-content-editable headline-el)))))))

(defn- stop-editing [state]
  (save-on-exit? state)
  (toggle-save-on-exit state false)
  (reset! (::edited-data-loaded state) false)
  (js/clearInterval @(::autosave-timer state))
  (reset! (::autosave-timer state) nil)
  (dis/dispatch! [:activity-modal-edit (:activity-data @(drv/get-ref state :modal-data)) false])
  (when @(::headline-input-listener state)
    (events/unlistenByKey @(::headline-input-listener state))
    (reset! (::headline-input-listener state) nil)))

(defn- clean-body []
  (when-let [body-el (sel1 [:div.rich-body-editor])]
    (let [raw-html (.-innerHTML body-el)]
      (dis/dispatch! [:update [:modal-editing-data] #(merge % {:body (utils/clean-body-html raw-html)
                                                               :has-changes true})]))))

(defn- save-editing? [state]
  (let [modal-data @(drv/get-ref state :modal-data)
        edited-data (:modal-editing-data modal-data)]
    (when (:has-changes edited-data)
      (reset! (::entry-saving state) true)
      (clean-body)
      (dis/dispatch! [:entry-modal-save (router/current-board-slug)]))))

(defn- dismiss-editing? [state dismiss-modal?]
  (let [modal-data @(drv/get-ref state :modal-data)
        dismiss-fn (fn []
                     (dis/dispatch! [:entry-clear-local-cache :modal-editing-data])
                     (stop-editing state)
                     (when (or dismiss-modal?)
                       (close-clicked state)))]
  (if @(::uploading-media state)
    (let [alert-data {:icon "/img/ML/trash.svg"
                      :action "dismiss-edit-uploading-media"
                      :message (str "Leave before finishing upload?")
                      :link-button-title "Stay"
                      :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                      :solid-button-style :red
                      :solid-button-title "Cancel upload"
                      :solid-button-cb #(do
                                          (dis/dispatch! [:alert-modal-hide])
                                          (dismiss-fn))
                      }]
      (dis/dispatch! [:alert-modal-show alert-data]))
    (if (:has-changes (:modal-editing-data modal-data))
      (let [alert-data {:icon "/img/ML/trash.svg"
                        :action "dismiss-edit-dirty-data"
                        :message (str "Leave without saving your changes?")
                        :link-button-title "Stay"
                        :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                        :solid-button-style :red
                        :solid-button-title "Lose changes"
                        :solid-button-cb #(do
                                            (dis/dispatch! [:alert-modal-hide])
                                            (dismiss-fn))
                        }]
        (dis/dispatch! [:alert-modal-show alert-data]))
      (dismiss-fn)))))

(defn modal-height-did-change
  "Save the height of the activity modal in the local component state
   so the top margin is moved accordigly on the next render."
  [s & [force-reload]]
  (when-let [activity-modal (rum/ref-node s "activity-modal")]
    (when (or force-reload
              (not= @(::activity-modal-height s) (.-clientHeight activity-modal)))
      (reset! (::activity-modal-height s) (.-clientHeight activity-modal)))))

(def default-min-modal-height 450)

(defn setup-editing-data [s]
  (let [modal-data @(drv/get-ref s :modal-data)]
    (when (and (not @(::edited-data-loaded s))
               (:modal-editing modal-data))
      (let [activity-data (:modal-editing-data modal-data)
            initial-body (:body activity-data)
            initial-headline (utils/emojify (:headline activity-data))]
        (reset! (::initial-body s) initial-body)
        (reset! (::initial-headline s) initial-headline)
        (reset! (::edited-data-loaded s) true)))))

(rum/defcs activity-modal < rum/reactive
                            ;; Derivatives
                            (drv/drv :modal-data)
                            ;; Locals
                            (rum/local false ::dismiss)
                            (rum/local false ::animate)
                            (rum/local false ::showing-dropdown)
                            (rum/local nil ::esc-key-listener)
                            (rum/local false ::move-activity)
                            (rum/local default-min-modal-height ::activity-modal-height)
                            (rum/local nil ::window-click)
                            (rum/local false ::show-bottom-border)
                            (rum/local nil ::window-resize-listener)
                            ;; Editing locals
                            (rum/local "" ::initial-headline)
                            (rum/local "" ::initial-body)
                            (rum/local nil ::headline-input-listener)
                            (rum/local false ::entry-saving)
                            (rum/local nil ::uploading-media)
                            (rum/local false ::save-on-exit)
                            (rum/local false ::edited-data-loaded)
                            (rum/local nil ::autosave-timer)
                            ;; Mixins
                            (when-not (responsive/is-mobile-size?)
                              mixins/no-scroll-mixin)
                            mixins/first-render-mixin

                            {:before-render (fn [s]
                              (let [modal-data @(drv/get-ref s :modal-data)]
                                (when (and (not @(::animate s))
                                         (= (:activity-modal-fade-in modal-data) (:uuid (:activity-data modal-data))))
                                  (reset! (::animate s) true)))
                              (modal-height-did-change s)
                              (setup-editing-data s)
                              (let [modal-data @(drv/get-ref s :modal-data)]
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
                                (when (and (:modal-editing modal-data)
                                           @(::entry-saving s))
                                  (let [entry-edit (:modal-editing-data modal-data)
                                        initial-body (:body entry-edit)
                                        initial-headline (utils/emojify (:headline entry-edit))]
                                    (when-not (:loading entry-edit)
                                      (when-not (:error entry-edit)
                                        (reset! (::initial-headline s) initial-headline)
                                        (reset! (::initial-body s) initial-body)
                                        (stop-editing s))
                                      (dis/dispatch! [:input [:dismiss-modal-on-editing-stop] false])
                                      (reset! (::entry-saving s) false)))))
                              s)
                             :will-mount (fn [s]
                              (reset! (::esc-key-listener s)
                               (events/listen
                                js/window
                                EventType/KEYDOWN
                                #(let [modal-data @(drv/get-ref s :modal-data)
                                       add-comment-focus (:add-comment-focus modal-data)
                                       comment-edit (:comment-edit modal-data)]
                                   (when (and (= (.-key %) "Escape")
                                              (not add-comment-focus)
                                              (not comment-edit))
                                     (let [modal-data @(drv/get-ref s :modal-data)]
                                       (if (and (:modal-editing modal-data)
                                                (not @(::uploading-media s)))
                                         (dismiss-editing? s true)
                                         (close-clicked s)))))))
                              (setup-editing-data s)
                              s)
                             :did-mount (fn [s]
                              (reset! (::window-click s)
                               (events/listen
                                js/window
                                EventType/CLICK
                                (fn [e]
                                  (when (and (not
                                              (utils/event-inside? e (sel1 [:div.activity-modal :div.more-dropdown])))
                                             (not
                                              (utils/event-inside? e (sel1 [:div.activity-modal :div.activity-move]))))
                                    (reset! (::showing-dropdown s) false)))))
                              (reset! (::window-resize-listener s)
                               (events/listen
                                js/window
                                EventType/RESIZE
                                #(modal-height-did-change s true)))
                              (let [modal-data @(drv/get-ref s :modal-data)]
                                (when (:modal-editing modal-data)
                                  (utils/after 1000
                                    #(real-start-editing s :headline))))
                              (setup-editing-data s)
                              s)
                             :after-render (fn [s]
                              (when @(:first-render-done s)
                                (let [activity-modal-content (sel1 [:div.activity-modal-content])
                                      scroll-height (.-scrollHeight activity-modal-content)
                                      client-height (.-clientHeight activity-modal-content)
                                      next-show-bottom-border (> scroll-height client-height)]
                                  (when (not= @(::show-bottom-border s) next-show-bottom-border)
                                    (reset! (::show-bottom-border s) next-show-bottom-border))))
                              s)
                             :will-unmount (fn [s]
                              (when @(::window-click s)
                                (events/unlistenByKey @(::window-click s))
                                (reset! (::window-click s) nil))
                              (when @(::esc-key-listener s)
                                (events/unlistenByKey @(::esc-key-listener s))
                                (reset! (::esc-key-listener s) nil))
                              (when @(::window-resize-listener s)
                                (events/unlistenByKey @(::window-resize-listener s))
                                (reset! (::window-resize-listener s) nil))
                              (set! (.-onbeforeunload js/window) nil)
                              s)}
  [s]
  (let [fixed-activity-modal-height (max @(::activity-modal-height s) default-min-modal-height)
        wh (.-innerHeight js/window)
        modal-data (drv/react s :modal-data)
        activity-data (:activity-data modal-data)
        editing (:modal-editing modal-data)
        is-mobile? (responsive/is-tablet-or-mobile?)
        show-comments? (and (not is-mobile?)
                            (utils/link-for (:links activity-data) "comments"))
        delete-link (utils/link-for (:links activity-data) "delete")
        edit-link (utils/link-for (:links activity-data) "partial-update")
        share-link (utils/link-for (:links activity-data) "share")]
    [:div.activity-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s)
                                                 (and @(::animate s)
                                                      (not @(:first-render-done s))))
                                :appear (and (not @(::dismiss s)) @(:first-render-done s))
                                :no-comments (not show-comments?)
                                :editing editing})
       :on-click #(when (and (not editing)
                             (not (utils/event-inside? % (sel1 [:div.activity-modal])))
                             (not (utils/event-inside? % (sel1 [:button.carrot-modal-close]))))
                    (close-clicked s))}
      [:div.modal-wrapper
        {:style {:margin-top (when-not is-mobile? (str (max 0 (/ (- wh fixed-activity-modal-height) 2)) "px"))}}
        (when (and (not is-mobile?)
                   (not (:activity-share modal-data)))
          [:button.carrot-modal-close.mlb-reset
            {:on-click #(if editing
                          (dismiss-editing? s true)
                          (close-clicked s))}])
        [:div.activity-modal.group
          {:ref "activity-modal"
           :class (str "activity-modal-" (:uuid activity-data))}
          (when is-mobile?
            [:div.activity-modal-mobile-header
              [:button.mlb-reset.mobile-modal-close-bt
                {:on-click #(close-clicked s)}]])
          [:div.activity-modal-header.group
            [:div.activity-modal-header-left
              (user-avatar-image (first (:author activity-data)))
              [:div.name (:name (first (:author activity-data)))]
              [:div.time-since
                [:time
                  {:date-time (:published-at activity-data)
                   :data-toggle "tooltip"
                   :data-placement "top"
                   :title (utils/activity-date-tooltip activity-data)}
                  (utils/time-since (:published-at activity-data))]]]
            [:div.activity-modal-header-right
              (when (or edit-link
                        share-link
                        delete-link)
                (let [all-boards (filter
                                  #(not= (:slug %) utils/default-drafts-board-slug)
                                  (:boards (:org-data modal-data)))]
                  [:div.more-dropdown
                    [:button.mlb-reset.activity-modal-more.dropdown-toggle
                      {:type "button"
                       :on-click (fn [e]
                                   (utils/remove-tooltips)
                                   (reset! (::showing-dropdown s) (not @(::showing-dropdown s)))
                                   (reset! (::move-activity s) false))
                       :data-toggle "tooltip"
                       :data-placement "right"
                       :data-container "body"
                       :title "More"}]
                    (when @(::showing-dropdown s)
                      [:div.activity-modal-dropdown-menu
                        [:div.triangle]
                        [:ul.activity-modal-more-menu
                          (when (and is-mobile?
                                     edit-link)
                           [:li.no-editing
                             {:on-click #(do
                                          (reset! (::showing-dropdown s) false)
                                          (dis/dispatch! [:activity-edit activity-data]))}
                             "Edit"])
                          (when (and is-mobile?
                                     share-link)
                           [:li.no-editing
                             {:on-click #(do
                                          (reset! (::showing-dropdown s) false)
                                          (dis/dispatch! [:activity-share-show activity-data]))}
                             "Share"])
                          (when edit-link
                            [:li
                              {:class (if editing "disabled" "no-editing")
                               :on-click #(when-not editing
                                           (reset! (::showing-dropdown s) false)
                                           (reset! (::move-activity s) true))}
                              "Move"])
                          (when delete-link
                            [:li
                              {:on-click #(do
                                            (reset! (::showing-dropdown s) false)
                                            (delete-clicked % activity-data))}
                              "Delete"])]])
                    (when @(::move-activity s)
                      (activity-move {:activity-data activity-data
                                      :boards-list all-boards
                                      :dismiss-cb #(reset! (::move-activity s) false)
                                      :on-change #(close-clicked s)}))]))
              (when-not is-mobile?
                (activity-attachments activity-data false))]]
          [:div.activity-modal-columns
            ;; Left column
            [:div.activity-left-column
              [:div.activity-left-column-content
                (if editing
                  [:div.activity-modal-content
                    {:key "activity-modal-content-editing"}
                    [:div.activity-modal-content-headline.emoji-autocomplete.emojiable
                      {:content-editable true
                       :ref "edit-headline"
                       :placeholder utils/default-headline
                       :on-paste    #(headline-on-paste s %)
                       :on-key-down #(headline-on-change s)
                       :on-click    #(headline-on-change s)
                       :on-key-press (fn [e]
                                     (when (= (.-key e) "Enter")
                                       (utils/event-stop e)
                                       (utils/to-end-of-content-editable (sel1 [:div.rich-body-editor]))))
                       :dangerouslySetInnerHTML @(::initial-headline s)}]
                    (rich-body-editor {:on-change #(do
                                                    (body-on-change s)
                                                    (modal-height-did-change s))
                                       :initial-body @(::initial-body s)
                                       :show-placeholder false
                                       :show-h2 true
                                       :dispatch-input-key :modal-editing-data
                                       :upload-progress-cb (fn [is-uploading?]
                                                             (reset! (::uploading-media s) is-uploading?))
                                       :media-config ["photo" "video" "chart" "attachment"]
                                       :classes "emoji-autocomplete emojiable"})]
                  [:div.activity-modal-content
                    {:key "activity-modal-content"}
                    [:div.activity-modal-content-headline
                      {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]
                    [:div.activity-modal-content-body
                      {:dangerouslySetInnerHTML (utils/emojify (:body activity-data))
                       :class (when (empty? (:headline activity-data)) "no-headline")}]
                    (when is-mobile?
                      (reactions activity-data))
                    (when is-mobile?
                      (comments activity-data))])
                (when-not is-mobile?
                  (if editing
                    [:div.activity-modal-footer.group
                      {:class (when @(::show-bottom-border s) "scrolling-content")}
                      (when (and (not (js/isIE))
                                 (not is-mobile?))
                        (emoji-picker {:add-emoji-cb (partial add-emoji-cb s)
                                       :container-selector "div.activity-modal-content"}))
                      [:div.activity-modal-footer-right
                        [:button.mlb-reset.mlb-link-black.cancel-edit
                          {:on-click #(dismiss-editing? s (:dismiss-modal-on-editing-stop modal-data))}
                          "Cancel"]
                        [:button.mlb-reset.mlb-default.save-edit
                          {:on-click #(save-editing? s)
                           :disabled (or (not (:has-changes (:modal-editing-data modal-data)))
                                         (not (seq (:headline (:modal-editing-data modal-data)))))}
                          (when (:loading (:modal-editing-data modal-data))
                            (small-loading))
                          "Save"]]]
                    [:div.activity-modal-footer.group
                      {:class (when @(::show-bottom-border s) "scrolling-content")}
                      (reactions activity-data)
                      [:div.activity-modal-footer-right
                        (when (and (not is-mobile?)
                                   edit-link)
                          [:button.mlb-reset.post-edit
                            {:class (utils/class-set {:not-hover (and (not @(::move-activity s))
                                                                      (not @(::showing-dropdown s)))})
                             :on-click (fn [e]
                                         (utils/remove-tooltips)
                                         (real-start-editing s :headline))}
                            "Edit"])
                        (when (and (not is-mobile?)
                                   (utils/link-for (:links activity-data) "share"))
                          [:div.activity-modal-share
                            [:button.mlb-reset.share-button
                              {:on-click #(dis/dispatch! [:activity-share-show activity-data])}
                              "Share"]])]]))]]
            ;; Right column
            (when show-comments?
              [:div.activity-right-column
                [:div.activity-right-column-content
                  (comments activity-data)]])]]]]))