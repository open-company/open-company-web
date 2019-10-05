(ns oc.web.components.stream-comments
  (:require [rum.core :as rum]
            [cljsjs.emoji-mart]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.comment :as cu]
            [oc.web.utils.activity :as au]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.react-utils :as react-utils]
            [oc.web.mixins.mention :as mention-mixins]
            [oc.web.utils.reaction :as reaction-utils]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn stop-editing [s comment-data]
  (reset! (::editing? s) nil))

(defn cancel-edit
  [s comment-data]
  (stop-editing s comment-data))

(defn finish-edit [s comment-data cancel?]
  (if cancel?
    (cancel-edit s comment-data)
    (stop-editing s comment-data)))

(defn start-editing [s comment-data]
  (let [comment-node (rum/ref-node s (str "comment-body-" (:uuid comment-data)))
        activity-data (first (:rum/args s))]
    (comment-actions/edit-comment (:uuid activity-data) comment-data)
    (reset! (::show-more-menu s) nil)
    (reset! (::editing? s) (:uuid comment-data))))

(defn delete-clicked [e activity-data comment-data]
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :action "delete-comment"
                    :message (str "Delete this comment?")
                    :link-button-title "No"
                    :link-button-cb #(alert-modal/hide-alert)
                    :solid-button-style :red
                    :solid-button-title "Yes"
                    :solid-button-cb (fn []
                                       (comment-actions/delete-comment activity-data comment-data)
                                       (alert-modal/hide-alert))
                    }]
    (alert-modal/show-alert alert-data)))

(defn scroll-to-bottom [s]
  (let [scrolling-node (rum/dom-node s)]
    (set! (.-scrollTop scrolling-node) (.-scrollHeight scrolling-node))))

(defn emoji-picked-cb [s comment-data emoji]
  (comment-actions/react-from-picker (first (:rum/args s)) comment-data (get emoji "native")))

(defn- reply-to [s parent-uuid]
  (swap! (::replying-to s) #(conj % parent-uuid))
  (comment-actions/add-comment-focus parent-uuid))

(defn- copy-comment-url [comment-url]
  (let [input-field (.createElement js/document "input")]
    (set! (.-style input-field) "position:absolute;top:-999999px;left:-999999px;")
    (set! (.-value input-field) comment-url)
    (.appendChild (.-body js/document) input-field)
    (.select input-field)
    (utils/copy-to-clipboard input-field)
    (.removeChild (.-body js/document) input-field)))

(defn- highlight-comment [s comment-id scroll?]
  (when-let [comment-node (rum/ref-node s (str "stream-comment-" comment-id))]
    (utils/after 10 (fn []
     (swap! (::highlighting-comments s) #(conj % comment-id)
     (when scroll?
       (.scrollIntoView comment-node #js {:behaviour "smooth" :block "center"}))
     (utils/after 1000(fn []
      (swap! (::highlighting-comments s) #(disj % comment-id)))))))))

(defn- maybe-highlight-comment [s]
  (let [comments-data (second (:rum/args s))]
    (when (and (seq comments-data)
               (router/current-comment-id)
               (not @(::initial-comment-scroll s))
               (rum/ref-node s (str "stream-comment-" (router/current-comment-id))))
      (reset! (::initial-comment-scroll s) true)
      (highlight-comment s (router/current-comment-id) true))))

(defn share-clicked [comment-data]
  (copy-comment-url (:url comment-data))
  (notification-actions/show-notification {:title "Share link copied to clipboard"
                                           :dismiss true
                                           :expire 3
                                           :id (keyword (str "comment-url-copied-"
                                            (:uuid comment-data)))}))

(rum/defc emoji-picker < ui-mixins/no-scroll-mixin
  [{:keys [add-emoji-cb dismiss-cb]}]
  [:div.emoji-picker-container
    [:button.mlb-reset.close-bt
      {:on-click dismiss-cb}
      "Cancel"]
    (react-utils/build (.-Picker js/EmojiMart)
      {:native true
       :autoFocus true
       :onClick (fn [emoji event]
                  (add-emoji-cb emoji))})])

(defn- emoji-picker-container [s comment-data]
  (let [activity-data (first (:rum/args s))
        showing-picker? (and (seq @(::show-picker s))
                             (= @(::show-picker s) (:uuid comment-data)))]
    (when showing-picker?
      (emoji-picker {:dismiss-cb (fn [_] (reset! (::show-picker s) nil))
                     :add-emoji-cb (fn [emoji]
       (when (reaction-utils/can-pick-reaction? (gobj/get emoji "native") (:reactions comment-data))
         (comment-actions/react-from-picker activity-data comment-data
          (gobj/get emoji "native")))
       (reset! (::show-picker s) nil))}))))

(rum/defcs stream-comments < rum/reactive
                             (drv/drv :add-comment-focus)
                             (drv/drv :team-roster)
                             (rum/local false ::last-focused-state)
                             (rum/local nil ::editing?)
                             (rum/local [] ::expanded-comments)
                             (rum/local nil ::show-picker)
                             (rum/local #{} ::replying-to)
                             (rum/local {} ::comment-url-copy)
                             (rum/local #{} ::highlighting-comments)
                             (rum/local false ::initial-comment-scroll)
                             (rum/local nil ::show-more-menu)
                             (drv/drv :add-comment-force-update)
                             ;; Mixins
                             (mention-mixins/oc-mentions-hover)
                             ui-mixins/refresh-tooltips-mixin
                             (ui-mixins/interactive-images-mixin "div.stream-comment-body")
                             (ui-mixins/on-window-click-mixin (fn [s e]
                              (when (and @(::show-picker s)
                                         (not (utils/event-inside? e
                                          (.get (js/$ "div.emoji-mart" (rum/dom-node s)) 0))))
                                (reset! (::show-picker s) nil))))
                             {:after-render (fn [s]
                               (let [activity-uuid (:uuid (first (:rum/args s)))
                                     focused-uuid @(drv/get-ref s :add-comment-focus)
                                     current-local-state @(::last-focused-state s)
                                     is-self-focused? (= focused-uuid activity-uuid)]
                                  (when (not= current-local-state is-self-focused?)
                                    (reset! (::last-focused-state s) is-self-focused?)
                                    (when is-self-focused?
                                      (scroll-to-bottom s))))
                               s)
                             :did-mount (fn [s]
                              (maybe-highlight-comment s)
                              (try (js/emojiAutocomplete)
                                (catch :default e false))
                              s)
                             :did-update (fn [s]
                              (maybe-highlight-comment s)
                              s)
                             :did-remount (fn [o s]
                              (when (not= (count (second (:rum/args o))) (count (second (:rum/args s))))
                                 (reset! (::replying-to s) #{}))
                              (let [args (vec (:rum/args s))
                                    new-added-comment (get args 2)]
                                (when new-added-comment
                                  (utils/after 180 #(highlight-comment s new-added-comment false))
                                  (comment-actions/add-comment-highlight-reset)))
                              (try (js/emojiAutocomplete)
                                (catch :default e false))
                              s)}
  [s {:keys [activity-data comments-data new-added-comment last-read-at current-user-id]}]
  (let [add-comment-force-update (drv/react s :add-comment-force-update)]
    [:div.stream-comments
      {:class (when (seq @(::editing? s)) "editing")}
      (if (pos? (count comments-data))
        [:div.stream-comments-list
          (for [idx (range (count comments-data))
                :let [comment-data (nth comments-data idx)
                      is-indented-comment? (seq (:parent-uuid comment-data))
                      next-comment-data (when (< idx (dec (count comments-data)))
                                          (nth comments-data (inc idx)))
                      should-show-add-comment? (or (and (not (:parent-uuid comment-data))
                                                        (not= (:parent-uuid next-comment-data) (:uuid comment-data))
                                                        (utils/in? @(::replying-to s) (:uuid comment-data)))
                                                   (and (not= (:parent-uuid next-comment-data) (:parent-uuid comment-data))
                                                        (utils/in? @(::replying-to s) (:parent-uuid comment-data))))
                      is-editing? (and (seq @(::editing? s))
                                       (= @(::editing? s) (:uuid comment-data)))
                      can-show-edit-bt? (and (:can-edit comment-data)
                                                 (not (:is-emoji comment-data)))
                      can-show-delete-bt? (:can-delete comment-data)
                      showing-picker? (and (seq @(::show-picker s))
                                           (= @(::show-picker s) (:uuid comment-data)))]]
            (if is-editing?
              [:div.stream-comment-outer
                {:key (str "stream-comment-" (:created-at comment-data))
                 :data-comment-uuid (:uuid comment-data)
                 :class (utils/class-set {:not-highlighted (not (utils/in? @(::highlighting-comments s) (:uuid comment-data)))
                                          :closing-thread (or (not next-comment-data)
                                                              (empty? (:parent-uuid next-comment-data)))})}
                [:div.stream-comment
                  {:class (utils/class-set {:indented-comment is-indented-comment?})}
                  (rum/with-key
                   (add-comment activity-data (:reply-parent comment-data)
                    (partial finish-edit s comment-data)
                    comment-data)
                   (str "add-comment-" (:reply-parent comment-data) "-" add-comment-force-update))]]
              [:div.stream-comment-outer
                {:key (str "stream-comment-" (:created-at comment-data))
                 :data-comment-uuid (:uuid comment-data)
                 :class (utils/class-set {:not-highlighted (not (utils/in? @(::highlighting-comments s) (:uuid comment-data)))
                                          :closing-thread (or (not next-comment-data)
                                                              (empty? (:parent-uuid next-comment-data)))})}
                [:div.stream-comment
                  {:ref (str "stream-comment-" (:uuid comment-data))
                   :class (utils/class-set {:editing is-editing?
                                            :editing-other-comment (not (nil? @(::editing? s)))
                                            :showing-picker showing-picker?
                                            :indented-comment is-indented-comment?})
                   :on-mouse-leave #(compare-and-set! (::show-more-menu s) (:uuid comment-data) nil)}
                  [:div.stream-comment-inner
                    (when-not is-editing?
                      (if (responsive/is-tablet-or-mobile?)
                        [:div.stream-comment-mobile-menu
                          (more-menu comment-data nil {:external-share false
                                                       :entity-type "comment"
                                                       :show-edit? true
                                                       :edit-cb (partial start-editing s)
                                                       :show-delete? true
                                                       :delete-cb (partial delete-clicked s activity-data)
                                                       :can-comment-share? true
                                                       :comment-share-cb #(share-clicked comment-data)
                                                       :can-react? true
                                                       :react-cb #(reset! (::show-picker s) (:uuid comment-data))
                                                       :can-reply? true
                                                       :reply-cb #(reply-to s (:reply-parent comment-data))})
                          (when showing-picker?
                            (emoji-picker-container s comment-data))]
                        [:div.stream-comment-floating-buttons
                          {:key (str "stream-comment-floating-buttons"
                                 (when can-show-edit-bt?
                                   "-edit")
                                 (when can-show-delete-bt?
                                   "-delete"))}
                          [:div.stream-comment-floating-buttons-inner
                            ;; Green buttons more menu
                            (when (= @(::show-more-menu s) (:uuid comment-data))
                              [:div.stream-comment-floating-buttons-more-menu
                                (when can-show-edit-bt?
                                  [:button.mlb-reset.edit-bt
                                    {:on-click (fn [_]
                                                (start-editing s comment-data))}
                                    "Edit"])
                                (when can-show-delete-bt?
                                  [:button.mlb-reset.delete-bt
                                    {:on-click (fn [_]
                                                (delete-clicked s activity-data comment-data))}
                                    "Delete"])
                                [:button.mlb-reset.share-bt
                                  {:on-click #(share-clicked comment-data)}
                                  "Copy link"]])
                            ;; More menu button or share button (depends if user is author of the comment)
                            (if (or can-show-edit-bt?
                                    can-show-delete-bt?)
                              [:button.mlb-reset.floating-bt.more-menu-bt
                                {:on-click (fn [_] (swap! (::show-more-menu s) #(if (= % (:uuid comment-data)) nil (:uuid comment-data))))
                                 :data-toggle "tooltip"
                                 :data-placement "top"
                                 :title "More"}]
                              [:button.mlb-reset.floating-bt.share-bt
                                {:data-toggle "tooltip"
                                 :data-placement "top"
                                 :on-click #(do
                                              (copy-comment-url (:url comment-data))
                                              (notification-actions/show-notification {:title "Share link copied to clipboard"
                                                                                       :dismiss true
                                                                                       :expire 3
                                                                                       :id (keyword (str "comment-url-copied-"
                                                                                        (:uuid comment-data)))}))
                                 :title "Copy link"}])
                            ;; Reply to comment
                            (when (:reply-parent comment-data)
                              [:button.mlb-reset.floating-bt.reply-bt
                                {:data-toggle "tooltip"
                                 :data-placement "top"
                                 :on-click #(reply-to s (:reply-parent comment-data))
                                 :title "Reply"}])
                            ;; React container
                            [:div.react-bt-container
                              [:button.mlb-reset.floating-bt.react-bt
                                {:data-toggle "tooltip"
                                 :data-placement "top"
                                 :title "Add reaction"
                                 :class (when (or can-show-edit-bt? can-show-delete-bt?) "has-more-menu")
                                 :on-click #(reset! (::show-picker s) (:uuid comment-data))}]
                              (when showing-picker?
                                (emoji-picker-container s comment-data))]]]))
                    [:div.stream-comment-author-avatar
                      (user-avatar-image (:author comment-data))]

                    [:div.stream-comment-right
                      [:div.stream-comment-header.group
                        {:class utils/hide-class}
                        [:div.stream-comment-author-right
                          [:div.stream-comment-author-name
                            (:name (:author comment-data))]
                          [:div.stream-comment-author-timestamp
                            (utils/foc-date-time (:created-at comment-data))]
                          (when (and (not= (-> comment-data :author :user-id) current-user-id)
                                     (< (.getTime (utils/js-date last-read-at))
                                        (.getTime (utils/js-date (:created-at comment-data)))))
                            [:div.new-comment-tag "(NEW)"])]]
                      [:div.stream-comment-content
                        [:div.stream-comment-body.oc-mentions.oc-mentions-hover
                          {:dangerouslySetInnerHTML (utils/emojify (:body comment-data))
                           :ref (str "comment-body-" (:uuid comment-data))
                           :class (utils/class-set {:emoji-comment (:is-emoji comment-data)
                                                    :expanded (utils/in? @(::expanded-comments s) (:uuid comment-data))
                                                    :emoji-autocomplete is-editing?
                                                    utils/hide-class true})}]]
                      (when (and (not is-editing?)
                                 (seq (:reactions comment-data)))
                       [:div.stream-comment-reactions-footer.group
                          (reactions comment-data (zero? (count (:reactions comment-data))) activity-data)])]]]
              (when should-show-add-comment?
                [:div.stream-comment
                  {:class (utils/class-set {:indented-comment true})}
                  (rum/with-key (add-comment activity-data (:reply-parent comment-data)
                   (fn [_ _](swap! (::replying-to s) #(disj % (:reply-parent comment-data)))))
                   (str "add-comment-" (:reply-parent comment-data) "-" add-comment-force-update))])]))]
        [:div.stream-comments-empty])]))
