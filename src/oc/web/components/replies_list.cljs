(ns oc.web.components.replies-list
  (:require [rum.core :as rum]
            [goog.object :as gobj]
            [clojure.data :as clj-data]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.comment :as cu]
            [oc.web.utils.activity :as au]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.mixins.seen :as seen-mixins]
            [oc.web.utils.ui :refer (ui-compose)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]
            [oc.web.actions.reply :as reply-actions]
            [oc.web.lib.react-utils :as react-utils]
            [oc.web.mixins.mention :as mention-mixins]
            [oc.web.utils.reaction :as reaction-utils]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.refresh-button :refer (refresh-button)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.all-caught-up :refer (all-caught-up caught-up-line)]
            [oc.web.components.ui.info-hover-views :refer (user-info-hover board-info-hover)]))

(defn- reply-to [comment-data add-comment-focus-key]
  (comment-actions/reply-to add-comment-focus-key (str "<span class=\"oc-replying-to\" contenteditable=\"false\">↩︎ Replying to " (-> comment-data :author :name) "</span><br>" (:body comment-data)) true))

(defn- copy-comment-url [comment-url]
  (let [input-field (.createElement js/document "input")]
    (set! (.-style input-field) "position:absolute;top:-999999px;left:-999999px;")
    (set! (.-value input-field) comment-url)
    (.appendChild (.-body js/document) input-field)
    (.select input-field)
    (utils/copy-to-clipboard input-field)
    (.removeChild (.-body js/document) input-field)))

(rum/defc emoji-picker < rum/static
                         (when (responsive/is-mobile-size?)
                           ui-mixins/no-scroll-mixin)
  [{:keys [add-emoji-cb dismiss-cb]}]
  [:div.emoji-picker-container
    [:button.mlb-reset.close-bt
      {:on-click dismiss-cb}
      "Cancel"]
    (react-utils/build (.-Picker js/EmojiMart)
      {:native true
       :autoFocus true
       :onClick (fn [emoji _]
                  (add-emoji-cb emoji))})])

(defn- emoji-picker-container [s entry-data reply-data seen-reply-cb]
  (let [showing-picker? (and (seq @(::show-picker s))
                             (= @(::show-picker s) (:uuid reply-data)))]
    (when showing-picker?
      (emoji-picker {:dismiss-cb #(reset! (::show-picker s) nil)
                     :add-emoji-cb (fn [emoji]
                                     (when (reaction-utils/can-pick-reaction? (gobj/get emoji "native") (:reactions reply-data))
                                       (seen-reply-cb (:uuid reply-data))
                                       (comment-actions/react-from-picker entry-data reply-data
                                        (gobj/get emoji "native")))
                                     (reset! (::show-picker s) nil))}))))

(rum/defcs reply-comment <
  rum/static
  [s {:keys [entry-data comment-data mouse-leave-cb
             react-cb reply-cb emoji-picker
             is-mobile? member? showing-picker?
             did-react-cb current-user-id reply-focus-value
             replies-count replying-to]}]
  (let [show-new-comment-tag (:unseen comment-data)]
    [:div.reply-comment-outer.open-reply
      {:key (str "reply-comment-" (:created-at comment-data))
       :data-comment-uuid (:uuid comment-data)
       :data-unseen (:unseen comment-data)
       :class (utils/class-set {:new-comment (:unseen comment-data)
                                :showing-picker showing-picker?
                                :no-replies (zero? replies-count)})}
      [:div.reply-comment
        {:ref (str "reply-comment-" (:uuid comment-data))
         :on-mouse-leave mouse-leave-cb}
        [:div.reply-comment-inner
          (when is-mobile?
            [:div.reply-comment-mobile-menu
              (more-menu {:entity-data comment-data
                          :external-share false
                          :entity-type "comment"
                          :can-react? true
                          :react-cb react-cb
                          :can-reply? true
                          :reply-cb reply-cb})
              emoji-picker])
          [:div.reply-comment-right
            [:div.reply-comment-header.group
              {:class utils/hide-class}
              [:div.reply-comment-author-right
                [:div.reply-comment-author-right-group
                  {:class (when (:unseen comment-data) "new-comment")}
                  [:div.reply-comment-author-name-container
                    (user-info-hover {:user-data (:author comment-data) :current-user-id current-user-id :leave-delay? true})
                    [:div.reply-comment-author-avatar
                      (user-avatar-image (:author comment-data))]
                    [:div.reply-comment-author-name
                      {:class (when (:user-id (:author comment-data)) "clickable-name")}
                      (:name (:author comment-data))]]
                  [:div.separator-dot]
                  [:div.reply-comment-author-timestamp
                    [:time
                      {:date-time (:created-at comment-data)
                       :data-toggle (when-not is-mobile? "tooltip")
                       :data-placement "top"
                       :data-container "body"
                       :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                       :data-title (utils/activity-date-tooltip comment-data)}
                      (utils/foc-date-time (:created-at comment-data))]]]
                (when show-new-comment-tag
                  [:div.separator-dot])
                (when show-new-comment-tag
                  [:div.new-comment-tag])
                (if (responsive/is-mobile-size?)
                  [:div.reply-comment-mobile-menu
                    (more-menu comment-data nil {:external-share false
                                                 :entity-type "comment"
                                                 :can-react? true
                                                 :react-cb react-cb
                                                 :can-reply? true
                                                 :reply-cb reply-cb})
                    emoji-picker]
                  [:div.reply-comment-floating-buttons
                    {:key "reply-comment-floating-buttons"}
                    ;; Reply to comment
                    [:button.mlb-reset.floating-bt.reply-bt
                      {:data-toggle "tooltip"
                       :data-placement "top"
                       :on-click reply-cb
                       :title "Reply"}
                      "Reply"]
                    ;; React container
                    [:div.react-bt-container.separator-bt
                      [:button.mlb-reset.floating-bt.react-bt
                        {:data-toggle "tooltip"
                         :data-placement "top"
                         :title "Add reaction"
                         :on-click react-cb}
                        "React"]
                      emoji-picker]])]]
            [:div.reply-comment-content
              [:div.reply-comment-body.oc-mentions.oc-mentions-hover
                {:dangerouslySetInnerHTML (utils/emojify (:body comment-data))
                 :ref :reply-comment-body
                 :class (utils/class-set {:emoji-comment (:is-emoji comment-data)
                                          utils/hide-class true})}]]
            (when (seq (:reactions comment-data))
              [:div.reply-comment-reactions-footer.group
                (reactions {:entity-data comment-data
                            :hide-picker (zero? (count (:reactions comment-data)))
                            :did-react-cb did-react-cb
                            :optional-activity-data entry-data})])]]]]))

(rum/defc reply-top <
  rum/static
  ui-mixins/refresh-tooltips-mixin
  [{:keys [current-user-id publisher board-name published-at headline links] :as entry-data}]
  (let [follow-link (utils/link-for links "follow")
        unfollow-link (utils/link-for links "unfollow")]
    [:div.reply-item-top
      [:div.reply-item-header
        [:div.reply-item-author-container
          (user-info-hover {:user-data publisher :current-user-id current-user-id :leave-delay? true})
          (user-avatar-image publisher)
          [:span.author-name
            (:name publisher)]]
        ; [:span.in "in"]
        ; [:div.reply-item-board-container
        ;   (board-info-hover {:activity-data entry-data})
        ;   [:span.board-name
        ;     board-name]]
        [:div.separator-dot]
        [:span.time-since
          [:time
            {:date-time published-at
             :data-toggle (when-not (responsive/is-mobile-size?) "tooltip")
             :data-placement "top"
             :data-container "body"
             :title (utils/activity-date-tooltip (utils/js-date published-at))}
            (utils/time-since published-at [:short :lower-case])]]
        (when (or follow-link unfollow-link)
          [:button.mlb-reset.mute-bt
            {:title (if follow-link
                      "Get notified about new activity"
                      "Don't show future replies to this update")
             :class (if follow-link "unfollowing" "following")
             :data-toggle (when-not (responsive/is-mobile-size?) "tooltip")
             :data-placement "top"
             :data-container "body"
             :on-click #(activity-actions/inbox-unfollow (:uuid entry-data))}])]
      [:div.reply-item-title
        headline]]))

(defn- reply-item-unique-class [{:keys [uuid]}]
  (str "reply-item-" uuid))

(defn- add-comment-focus-prefix []
  (str "reply-comment-" (int (rand 10000)) "-prefix"))

(defn- reply-expand [entry-data reply-data]
  (reply-actions/reply-expand entry-data reply-data))

(defn- reply-mark-seen [entry-data reply-data]
  (reply-actions/reply-mark-seen entry-data reply-data))

(defn- replies-mark-seen [entry-data]
  (reply-actions/replies-mark-seen entry-data))

(defn reply-unwrap-body [entry-data reply-data]
  (reply-actions/reply-unwrap-body entry-data reply-data))

(defn replies-expand [entry-data]
  (reply-actions/replies-expand entry-data))

(defn- comment-item
  [s {:keys [entry-data reply-data is-mobile? seen-reply-cb member?
             current-user-id reply-focus-value]}]
  (let [showing-picker? (and (seq @(::show-picker s))
                             (= @(::show-picker s) (:uuid reply-data)))
        replying-to (@(::replying s) (:uuid reply-data))]
    [:div.reply-item-block.vertical-line.group
      {:key (str "reply-thread-item-" (:uuid reply-data))}
      (reply-comment {:entry-data entry-data
                      :comment-data reply-data
                      :reply-focus-value reply-focus-value
                      :is-mobile? is-mobile?
                      :react-cb #(reset! (::show-picker s) (:uuid reply-data))
                      :reply-cb #(reply-to reply-data reply-focus-value)
                      :did-react-cb #(seen-reply-cb (:uuid reply-data))
                      :emoji-picker (when showing-picker?
                                      (emoji-picker-container s entry-data reply-data seen-reply-cb))
                      :showing-picker? showing-picker?
                      :member? member?
                      :replies-count (:replies-count reply-data)
                      :current-user-id current-user-id
                      :replying-to replying-to})]))

(rum/defc collapsed-comments-button <
  rum/static
  [{:keys [message expand-cb]}]
  [:button.mlb-reset.view-more-bt
    {:on-click expand-cb}
    message])

(rum/defcs reply-item < rum/static
                        rum/reactive
                        (rum/local nil ::show-picker)
                        (rum/local #{} ::replying)
                        (rum/local nil ::add-comment-focus-prefix)
                        ui-mixins/refresh-tooltips-mixin
                        (ui-mixins/interactive-images-mixin "div.reply-comment-body")
                        (ui-mixins/on-window-click-mixin (fn [s e]
                         (when (and @(::show-picker s)
                                    (not (utils/event-inside? e
                                     (.get (js/$ "div.emoji-mart" (rum/dom-node s)) 0))))
                           (reset! (::show-picker s) nil))))
                        ;; Mentions:
                        (drv/drv :users-info-hover)
                        (drv/drv :current-user-data)
                        (drv/drv :follow-publishers-list)
                        (drv/drv :followers-publishers-count)
                        (mention-mixins/oc-mentions-hover {:click? true})
                        {:will-mount (fn [s]
                           (reset! (::add-comment-focus-prefix s) (add-comment-focus-prefix))
                         s)
                         :did-remount (fn [o s]
                           ; (let [items (-> s :rum/args first :replies-data)
                           ;       items-changed (clj-data/diff (-> o :rum/args first :replies-data) items)]
                           ;    (when (or (seq (first items-changed))
                           ;              (seq (second items-changed)))
                           ;      (update-replies s)))
                            s)}
  [s {uuid              :uuid
      publisher         :publisher
      unseen            :unseen
      published-at      :published-at
      member?           :member?
      replies-data      :replies-data
      expanded-replies  :expanded-replies
      loading-comments? :loading-comments?
      :as entry-data}]
  (let [_users-info-hover (drv/react s :users-info-hover)
        _follow-publishers-list (drv/react s :follow-publishers-list)
        _followers-publishers-count (drv/react s :followers-publishers-count)
        current-user-data (drv/react s :current-user-data)
        is-mobile? (responsive/is-mobile-size?)
        reply-item-class (reply-item-unique-class entry-data)
        add-comment-focus-value (cu/add-comment-focus-value @(::add-comment-focus-prefix s) uuid)
        show-expand-replies? (and (not expanded-replies)
                                  (seq (filter :collapsed replies-data)))]
    [:div.reply-item.group
      {:class (utils/class-set {:unseen unseen
                                :open-item true
                                :close-item true
                                reply-item-class true})
       :data-activity-uuid uuid
       :ref :reply-item
       :on-click (fn [e]
                   (let [reply-el (rum/ref-node s :reply-item)]
                     (when (and (not (utils/button-clicked? e))
                                (not (utils/input-clicked? e))
                                (not (utils/anchor-clicked? e))
                                (not (utils/content-editable-clicked? e))
                                (not (dom-utils/event-inside? e (.querySelector reply-el "div.emoji-mart")))
                                (not (dom-utils/event-inside? e (.querySelector reply-el "div.add-comment-box-container")))
                                (not (dom-utils/event-cotainer-has-class e "reply-comment-body")))
                       (nav-actions/open-post-modal entry-data false))))}
      (reply-top (assoc entry-data :current-user-id (:user-id current-user-data)))
      (when loading-comments?
        [:div.reply-item-blocks.group
          [:div.reply-item-loading.group
            (small-loading)
            [:span.reply-item-loading-inner
              "Loading replies..."]]])
      [:div.reply-item-blocks.group
        (when show-expand-replies?
          (rum/with-key
           (collapsed-comments-button {:expand-cb #(replies-expand entry-data)
                                       :message (str "View all " (count replies-data) " comments")})
           (str "collapsed-comments-bt-" uuid "-" (count replies-data))))
        (for [reply replies-data
              :when (or expanded-replies
                        (not (:collapsed reply)))]
          (comment-item s {:entry-data entry-data
                           :reply-data reply
                           :is-mobile? is-mobile?
                           :seen-reply-cb #(reply-mark-seen entry-data reply)
                           :member? member?
                           :reply-focus-value add-comment-focus-value
                           :current-user-id (:user-id current-user-data)}))
        (rum/with-key
         (add-comment {:activity-data entry-data
                       :collapse? true
                       :add-comment-placeholder "Reply..."
                       :add-comment-cb (fn [new-comment-data]
                                          (reply-actions/replies-add entry-data new-comment-data))
                       :add-comment-focus-prefix @(::add-comment-focus-prefix s)})
         (str "add-comment-" @(::add-comment-focus-prefix s) "-" uuid))]]))

(defn- count-unseen-comments [items]
  (reduce (fn [c item]
            (+ c (count (filter :unseen (:replies-data item)))))
   0
   items))

(rum/defcs replies-refresh-button < rum/reactive
  (drv/drv :replies-badge)
  (rum/local 0 ::initial-unseen-comments)
  (rum/local nil ::last-force-list-update)
  {:will-update (fn [s]
    (let [props (-> s :rum/args first)
          initial-unseen-comments (::initial-unseen-comments s)
          last-force-list-update (::last-force-list-update s)
          force-list-update (:force-list-update s)]
      (when-not (= @last-force-list-update force-list-update)
        (reset! initial-unseen-comments (count-unseen-comments (:items-to-render props)))
        (reset! (::last-force-list-update s) force-list-update)))
    s)}
  [s {:keys [items-to-render]}]
  (let [replies-badge (drv/react s :replies-badge)
        delta-new-comments (- (count-unseen-comments items-to-render) @(::initial-unseen-comments s))
        show-refresh-button? (and replies-badge
                                  (pos? delta-new-comments))]
    (when show-refresh-button?
      (refresh-button {:message (if (pos? delta-new-comments)
                                  (str delta-new-comments " unread comment" (when-not (= delta-new-comments 1) "s"))
                                  "New replies available")
                       :visible show-refresh-button?
                       :class-name :replies-refresh-button-container}))))

(def replies-empty-message "When someone replies to you, or mentions your name, you'll see it here.")

(rum/defc replies-empty-list < rum/static
  []
  [:div.replies-list-empty
    (all-caught-up replies-empty-message)])