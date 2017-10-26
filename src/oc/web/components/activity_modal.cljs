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
            [oc.web.components.ui.mixins :as mixins]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.ui.activity-move :refer (activity-move)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.rich-body-editor :refer (rich-body-editor)]
            [oc.web.components.ui.carrot-close-bt :refer (carrot-close-bt)]
            [oc.web.components.ui.topics-dropdown :refer (topics-dropdown)]
            [oc.web.components.ui.activity-attachments :refer (activity-attachments)]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.comments :refer (comments)]))

(defn dismiss-modal [s board-filters]
  (let [org (router/current-org-slug)
        board (router/current-board-slug)
        modal-data @(drv/get-ref s :modal-data)
        current-board-filters (:board-filters modal-data)]
    (router/nav!
      (if (string? board-filters)
        (oc-urls/board-filter-by-topic org board board-filters)
        (if (:from-all-posts @router/path)
          (oc-urls/all-posts org)
          (if (string? current-board-filters)
            (oc-urls/board-filter-by-topic org board current-board-filters)
            (if (= current-board-filters :by-topic)
              (oc-urls/board-sort-by-topic org board)
              (oc-urls/board org board))))))))

(defn close-clicked [s & [board-filters]]
  (when-not (:from-all-posts @router/path)
    ;; Make sure the seen-at is not reset when navigating back to the board so NEW is still visible
    (dis/dispatch! [:input [:no-reset-seen-at] true]))
  (dis/dispatch! [:input [:dismiss-modal-on-editing-stop] false])
  (reset! (::dismiss s) true)
  (utils/after 180 #(dismiss-modal s board-filters)))

(defn delete-clicked [e activity-data]
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :action "delete-entry"
                    :message (str "Delete this update?")
                    :link-button-title "No"
                    :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                    :solid-button-title "Yes"
                    :solid-button-cb #(do
                                       (let [org-slug (router/current-org-slug)
                                             board-slug (router/current-board-slug)
                                             last-filter (keyword (cook/get-cookie (router/last-board-filter-cookie org-slug board-slug)))]
                                         (if (= last-filter :by-topic)
                                           (router/nav! (oc-urls/board-sort-by-topic))
                                           (router/nav! (oc-urls/board))))
                                       (dis/dispatch! [:activity-delete activity-data])
                                       (dis/dispatch! [:alert-modal-hide]))
                    }]
    (dis/dispatch! [:alert-modal-show alert-data])))

;; Editing

(defn body-on-change [state]
  (dis/dispatch! [:input [:modal-editing-data :has-changes] true]))

(defn- headline-on-change [state]
  (when-let [headline (sel1 [:div.activity-modal-content-headline])]
    (let [emojied-headline   (utils/emoji-images-to-unicode (gobj/get (utils/emojify (.-innerHTML headline)) "__html"))]
      (dis/dispatch! [:input [:modal-editing-data :headline] emojied-headline])
      (dis/dispatch! [:input [:modal-editing-data :has-changes] true]))))

(defn- setup-headline [state]
  (let [headline-el  (rum/ref-node state "edit-headline")]
    (reset! (::headline-input-listener state)
     (events/listen headline-el EventType/INPUT #(headline-on-change state)))
    (js/emojiAutocomplete)))

(defn headline-on-paste
  "Avoid to paste rich text into headline, replace it with the plain text clipboard data."
  [state e]
  ; Prevent the normal paste behaviour
  (utils/event-stop e)
  (let [clipboardData (or (.-clipboardData e) (.-clipboardData js/window))
        pasted-data   (.getData clipboardData "text/plain")
        headline-el   (rum/ref-node state "edit-headline")]
    ; replace the selected text of headline with the text/plain data of the clipboard
    (js/replaceSelectedText pasted-data)
    ; call the headline-on-change to check for content length
    (headline-on-change state)
    ; move cursor at the end
    (utils/to-end-of-content-editable headline-el)))

(defn- add-emoji-cb [state]
  (headline-on-change state)
  (when-let [body (sel1 [:div.rich-body-editor])]
    (body-on-change state)))

(defn- real-start-editing [state & [focus]]
  (reset! (::editing state) true)
  (let [activity-data (first (:rum/args state))]
    (dis/dispatch! [:input [:modal-editing-data] activity-data]))
  (dis/dispatch! [:activity-modal-edit true])
  (utils/after 100 #(setup-headline state))
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
         (utils/to-end-of-content-editable (rum/ref-node state "edit-headline"))))))

(defn- start-editing? [state & [focus]]
  (let [activity-data (first (:rum/args state))]
    (when (and (utils/link-for (:links activity-data) "partial-update")
               (not @(::showing-dropdown state))
               (not @(::move-activity state))
               (not @(::share-dropdown state))
               (not (.contains (.-classList (.-activeElement js/document)) "add-comment")))
      (real-start-editing state focus))))

(defn- stop-editing [state]
  (reset! (::editing state) false)
  (dis/dispatch! [:activity-modal-edit false])
  (when @(::headline-input-listener state)
    (events/unlistenByKey @(::headline-input-listener state))
    (reset! (::headline-input-listener state) nil)))

(defn- clean-body []
  (when-let [body-el (sel1 [:div.rich-body-editor])]
    (let [raw-html (.-innerHTML body-el)]
      (dis/dispatch! [:input [:modal-editing-data :body] (utils/clean-body-html raw-html)]))))

(defn- save-editing? [state]
  (let [modal-data @(drv/get-ref state :modal-data)
        edit-data (:modal-editing-data modal-data)]
    (when (:has-changes edit-data)
      (reset! (::entry-saving state) true)
      (clean-body)
      (dis/dispatch! [:entry-modal-save (router/current-board-slug)]))))

(defn- dismiss-editing? [state dismiss-modal?]
  (let [modal-data @(drv/get-ref state :modal-data)
        dismiss-fn (fn []
                     (stop-editing state)
                     (when (or dismiss-modal?)
                       (close-clicked state)))]
  (if @(::uploading-media state)
    (let [alert-data {:icon "/img/ML/trash.svg"
                      :action "dismiss-edit-uploading-media"
                      :message (str "Cancel before finishing upload?")
                      :link-button-title "No"
                      :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                      :solid-button-title "Yes"
                      :solid-button-cb #(do
                                          (dis/dispatch! [:alert-modal-hide])
                                          (dismiss-fn))
                      }]
      (dis/dispatch! [:alert-modal-show alert-data]))
    (if (:has-changes (:modal-editing-data modal-data))
      (let [alert-data {:icon "/img/ML/trash.svg"
                        :action "dismiss-edit-dirty-data"
                        :message (str "Cancel without saving your changes?")
                        :link-button-title "No"
                        :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                        :solid-button-title "Yes"
                        :solid-button-cb #(do
                                            (dis/dispatch! [:alert-modal-hide])
                                            (dismiss-fn))
                        }]
        (dis/dispatch! [:alert-modal-show alert-data]))
      (dismiss-fn)))))

(def default-min-modal-height 450)

(rum/defcs activity-modal < rum/reactive
                            ;; Derivatives
                            (drv/drv :modal-data)
                            ;; Locals
                            (rum/local false ::dismiss)
                            (rum/local false ::animate)
                            (rum/local false ::showing-dropdown)
                            (rum/local nil ::window-resize-listener)
                            (rum/local nil ::esc-key-listener)
                            (rum/local false ::move-activity)
                            (rum/local default-min-modal-height ::activity-modal-height)
                            (rum/local false ::share-dropdown)
                            (rum/local nil ::window-click)
                            (rum/local false ::show-bottom-border)
                            ;; Editing locals
                            (rum/local false ::editing)
                            (rum/local "" ::initial-headline)
                            (rum/local "" ::initial-body)
                            (rum/local nil ::headline-input-listener)
                            (rum/local false ::entry-saving)
                            (rum/local nil ::uploading-media)
                            ;; Mixins
                            mixins/no-scroll-mixin
                            mixins/first-render-mixin

                            {:before-render (fn [s]
                                              (let [modal-data @(drv/get-ref s :modal-data)]
                                                (when (and (not @(::animate s))
                                                         (= (:activity-modal-fade-in modal-data) (:uuid (first (:rum/args s)))))
                                                  (reset! (::animate s) true))
                                                (when-let [activity-modal (sel1 [:div.activity-modal])]
                                                  (when (not= @(::activity-modal-height s) (.-clientHeight activity-modal))
                                                    (reset! (::activity-modal-height s) (.-clientHeight activity-modal)))))
                                              s)
                             :will-mount (fn [s]
                                           (reset! (::esc-key-listener s)
                                            (events/listen js/window EventType/KEYDOWN #(when (= (.-key %) "Escape")
                                                                                          (let [modal-data @(drv/get-ref s :modal-data)]
                                                                                            (if (and (:modal-editing modal-data)
                                                                                                     (not @(::uploading-media s)))
                                                                                              (dismiss-editing? s true)
                                                                                              (close-clicked s))))))
                                           (let [modal-data @(drv/get-ref s :modal-data)
                                                 activity-data (first (:rum/args s))
                                                 initial-body (:body activity-data)
                                                 initial-headline (utils/emojify (:headline activity-data))]
                                             (reset! (::editing s) (:modal-editing modal-data))
                                             (reset! (::initial-body s) initial-body)
                                             (reset! (::initial-headline s) initial-headline))
                                           s)
                             :did-mount (fn [s]
                                          (reset! (::window-click s)
                                           (events/listen js/window EventType/CLICK (fn [e]
                                                                                      (when (and (not (utils/event-inside? e (sel1 [:div.activity-modal :div.more-dropdown])))
                                                                                                 (not (utils/event-inside? e (sel1 [:div.activity-modal :div.activity-move]))))
                                                                                        (reset! (::showing-dropdown s) false))
                                                                                      (when (not (utils/event-inside? e (sel1 [:div.activity-modal :div.activity-modal-share])))
                                                                                        (reset! (::share-dropdown s) false)))))
                                          (let [modal-data @(drv/get-ref s :modal-data)]
                                            (when (:modal-editing modal-data)
                                              (utils/after 1000
                                                #(real-start-editing s :headline))))
                                          s)
                            :after-render (fn [s]
                                            (when @(:first-render-done s)
                                              (let [wh (.-innerHeight js/window)
                                                    activity-modal (rum/ref-node s "activity-modal")
                                                    next-show-bottom-border (>= (.-clientHeight activity-modal) wh)]
                                                (when (not= @(::show-bottom-border s) next-show-bottom-border)
                                                  (reset! (::show-bottom-border s) next-show-bottom-border))))
                                            s)
                            :will-unmount (fn [s]
                                            ;; Remove window resize listener
                                            (when @(::window-resize-listener s)
                                              (events/unlistenByKey @(::window-resize-listener s))
                                              (reset! (::window-resize-listener s) nil))
                                            (when @(::window-click s)
                                              (events/unlistenByKey @(::window-click s))
                                              (reset! (::window-click s) nil))
                                            (when @(::esc-key-listener s)
                                              (events/unlistenByKey @(::esc-key-listener s))
                                              (reset! (::esc-key-listener s) nil))
                                            s)
                            :did-remount (fn [_ s]
                                           (let [activity-data (first (:rum/args s))
                                                 initial-body (:body activity-data)
                                                 initial-headline (utils/emojify (:headline activity-data))]
                                             (reset! (::initial-headline s) initial-headline)
                                             (reset! (::initial-body s) initial-body))
                                           (let [modal-data @(drv/get-ref s :modal-data)]
                                             (when (and (:modal-editing modal-data)
                                                        @(::entry-saving s))
                                               (let [entry-edit (:modal-editing-data modal-data)]
                                                 (when-not (:loading entry-edit)
                                                   (when-not (:error entry-edit)
                                                     (stop-editing s))
                                                   (dis/dispatch! [:input [:dismiss-modal-on-editing-stop] false])
                                                   (reset! (::entry-saving s) false)))))
                                           s)}
  [s activity-data]
  (let [show-comments? (utils/link-for (:links activity-data) "comments")
        fixed-activity-modal-height (max @(::activity-modal-height s) default-min-modal-height)
        wh (.-innerHeight js/window)
        modal-data (drv/react s :modal-data)
        editing @(::editing s)]
    [:div.activity-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (and @(::animate s) (not @(:first-render-done s))))
                                :appear (and (not @(::dismiss s)) @(:first-render-done s))
                                :no-comments (not show-comments?)
                                :editing editing})
       :on-click #(when (and (not editing)
                             (not (utils/event-inside? % (sel1 [:div.activity-modal])))
                             (not (utils/event-inside? % (sel1 [:button.carrot-modal-close]))))
                    (close-clicked s))}
      [:div.modal-wrapper
        {:style {:margin-top (str (max 0 (/ (- wh fixed-activity-modal-height) 2)) "px")}}
        [:button.carrot-modal-close.mlb-reset
          {:on-click #(if editing
                        (dismiss-editing? s true)
                        (close-clicked s))}]
        [:div.activity-modal.group
          {:ref "activity-modal"
           :class (str "activity-modal-" (:uuid activity-data))}
          [:div.activity-modal-header.group
            [:div.activity-modal-header-left
              (user-avatar-image (first (:author activity-data)))
              [:div.name (:name (first (:author activity-data)))]
              [:div.time-since
                [:time
                  {:date-time (:created-at activity-data)
                   :data-toggle "tooltip"
                   :data-placement "top"
                   :title (utils/activity-date-tooltip activity-data)}
                  (utils/time-since (:created-at activity-data))]]]
            [:div.activity-modal-header-right
              (when (or (utils/link-for (:links activity-data) "partial-update")
                        (utils/link-for (:links activity-data) "delete"))
                (let [all-boards (filter #(not= (:slug %) "drafts") (:boards (:org-data modal-data)))]
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
                          (when (utils/link-for (:links activity-data) "partial-update")
                            [:li
                              {:class (if editing "disabled" "no-editing")
                               :on-click #(when-not editing
                                           (reset! (::showing-dropdown s) false)
                                           (reset! (::move-activity s) true))}
                              "Move"])
                          (when (utils/link-for (:links activity-data) "delete")
                            [:li
                              {:on-click #(do
                                            (reset! (::showing-dropdown s) false)
                                            (delete-clicked % activity-data))}
                              "Delete"])]])
                    (when @(::move-activity s)
                      (activity-move {:activity-data activity-data :boards-list all-boards :dismiss-cb #(reset! (::move-activity s) false) :on-change #(close-clicked s nil)}))]))
              (activity-attachments activity-data false)
              (if editing
                (topics-dropdown (:modal-editing-data modal-data) :modal-editing-data)
                (when (:topic-slug activity-data)
                  (let [topic-name (or (:topic-name activity-data) (string/upper (:topic-slug activity-data)))]
                    [:div.activity-tag
                      {:on-click #(close-clicked s (:topic-slug activity-data))}
                      topic-name])))]]
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
                       :placeholder "Untitled post"
                       :on-paste    #(headline-on-paste s %)
                       :on-key-down #(headline-on-change s)
                       :on-click    #(headline-on-change s)
                       :on-key-press (fn [e]
                                     (when (= (.-key e) "Enter")
                                       (utils/event-stop e)
                                       (utils/to-end-of-content-editable (sel1 [:div.rich-body-editor]))))
                       :dangerouslySetInnerHTML @(::initial-headline s)}]
                    (rich-body-editor {:on-change (partial body-on-change s)
                                       :initial-body @(::initial-body s)
                                       :show-placeholder false
                                       :show-h2 true
                                       :dispatch-input-key :modal-editing-data
                                       :upload-progress-cb (fn [is-uploading?]
                                                             (reset! (::uploading-media s) is-uploading?))
                                       :media-config ["photo" "video" "chart" "attachment" "divider-line"]
                                       :classes "emoji-autocomplete emojiable"})]
                  [:div.activity-modal-content
                    {:key "activity-modal-content"}
                    [:div.activity-modal-content-headline
                      {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))
                       :on-click #(start-editing? s :headline)}]
                    [:div.activity-modal-content-body
                      {:dangerouslySetInnerHTML (utils/emojify (:body activity-data))
                       :on-click #(start-editing? s :body)
                       :class (when (empty? (:headline activity-data)) "no-headline")}]])
                (if editing
                  [:div.activity-modal-footer.group
                    {:class (when @(::show-bottom-border s) "scrolling-content")}
                    (emoji-picker {:add-emoji-cb (partial add-emoji-cb s)})
                    [:div.activity-modal-footer-right
                      [:button.mlb-reset.mlb-link-black.cancel-edit
                        {:on-click #(dismiss-editing? s (:dismiss-modal-on-editing-stop modal-data))}
                        "Cancel"]
                      [:button.mlb-reset.mlb-default.save-edit
                        {:on-click #(save-editing? s)
                         :disabled (not (:has-changes (:modal-editing-data modal-data)))}
                        (when (:loading (:modal-editing-data modal-data))
                          (small-loading))
                        "Save"]]]
                  [:div.activity-modal-footer.group
                    {:class (when @(::show-bottom-border s) "scrolling-content")}
                    (reactions activity-data)
                    [:div.activity-modal-footer-right
                      (when (utils/link-for (:links activity-data) "partial-update")
                        [:button.mlb-reset.post-edit
                          {:class (utils/class-set {:not-hover (and (not @(::move-activity s))
                                                                    (not @(::showing-dropdown s))
                                                                    (not @(::share-dropdown s)))})
                           :on-click (fn [e]
                                       (utils/remove-tooltips)
                                       (real-start-editing s :headline))}
                          "Edit"])
                      (when (utils/link-for (:links activity-data) "share")
                        [:div.activity-modal-share
                          (when @(::share-dropdown s)
                            [:div.share-dropdown
                              [:div.triangle]
                              [:ul.share-dropdown-menu
                                (when (jwt/team-has-bot? (:team-id (dis/org-data)))
                                  [:li.share-dropdown-item
                                    {:on-click (fn [e]
                                                 (reset! (::share-dropdown s) false)
                                                 ; open the activity-share-modal component
                                                 (dis/dispatch! [:activity-share-show :slack activity-data]))}
                                    "Slack"])
                                [:li.share-dropdown-item
                                  {:on-click (fn [e]
                                               (reset! (::share-dropdown s) false)
                                               ; open the activity-share-modal component
                                               (dis/dispatch! [:activity-share-show :email activity-data]))}
                                  "Email"]
                                [:li.share-dropdown-item
                                  {:on-click (fn [e]
                                               (reset! (::share-dropdown s) false)
                                               ; open the activity-share-modal component
                                               (dis/dispatch! [:activity-share-show :link activity-data]))}
                                  "Link"]]])
                          [:button.mlb-reset.share-button
                            {:on-click #(do
                                         (reset! (::share-dropdown s) (not @(::share-dropdown s))))}
                            "Share"]])]])]]
            ;; Right column
            (when show-comments?
              [:div.activity-right-column
                [:div.activity-right-column-content
                  (comments activity-data)]])]]]]))