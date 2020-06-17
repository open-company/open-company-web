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
            [oc.web.utils.ui :refer (ui-compose)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]
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
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.all-caught-up :refer (all-caught-up caught-up-line)]
            [oc.web.components.ui.info-hover-views :refer (user-info-hover board-info-hover)]))

(defn- reply-to [s parent-uuid add-comment-focus-key]
  (swap! (::replying s) conj parent-uuid)
  (comment-actions/add-comment-focus add-comment-focus-key))

(defn- copy-comment-url [comment-url]
  (let [input-field (.createElement js/document "input")]
    (set! (.-style input-field) "position:absolute;top:-999999px;left:-999999px;")
    (set! (.-value input-field) comment-url)
    (.appendChild (.-body js/document) input-field)
    (.select input-field)
    (utils/copy-to-clipboard input-field)
    (.removeChild (.-body js/document) input-field)))

(rum/defc emoji-picker < (when (responsive/is-mobile-size?)
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

(defn- emoji-picker-container [s activity-data comment-data read-comment-cb]
  (let [showing-picker? (and (seq @(::show-picker s))
                             (= @(::show-picker s) (:uuid comment-data)))]
    (when showing-picker?
      (emoji-picker {:dismiss-cb #(reset! (::show-picker s) nil)
                     :add-emoji-cb (fn [emoji]
                                     (when (reaction-utils/can-pick-reaction? (gobj/get emoji "native") (:reactions comment-data))
                                       (read-comment-cb comment-data)
                                       (comment-actions/react-from-picker activity-data comment-data
                                        (gobj/get emoji "native")))
                                     (reset! (::show-picker s) nil))}))))

(rum/defcs reply-comment <
  rum/static
  rum/reactive
  (drv/drv :users-info-hover)
  [s {:keys [activity-data comment-data
             is-indented-comment? mouse-leave-cb
             react-cb reply-cb emoji-picker
             is-mobile? member? showing-picker?
             did-react-cb new-thread? current-user-id
             replies-count replying-to truncated-body?]}]
  (let [_users-info-hover (drv/react s :users-info-hover)
        show-new-comment-tag (and (:unread comment-data)
                                  (or (and is-indented-comment?
                                           (not new-thread?))
                                      (not is-indented-comment?)))]
    [:div.reply-comment-outer
      {:key (str "reply-comment-" (:created-at comment-data))
       :data-comment-uuid (:uuid comment-data)
       :class (utils/class-set {:open-reply (not is-indented-comment?)
                                :new-comment (:unread comment-data)
                                :indented-comment is-indented-comment?
                                :showing-picker showing-picker?
                                :no-replies (zero? replies-count)
                                :truncated-body truncated-body?})}
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
                  {:class (when (:unread comment-data) "new-comment")}
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
                    (when (:reply-parent comment-data)
                      [:button.mlb-reset.floating-bt.reply-bt
                        {:data-toggle "tooltip"
                         :data-placement "top"
                         :on-click reply-cb
                         :title "Reply"}
                        "Reply"])
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
                 :ref (str "comment-body-" (:uuid comment-data))
                 :class (utils/class-set {:emoji-comment (:is-emoji comment-data)
                                          utils/hide-class true})}]]
            (when (seq (:reactions comment-data))
              [:div.reply-comment-reactions-footer.group
                (reactions {:entity-data comment-data
                            :hide-picker (zero? (count (:reactions comment-data)))
                            :did-react-cb did-react-cb
                            :optional-activity-data activity-data})])]]]]))

(rum/defc reply-top
  [{:keys [current-user-id publisher board-name published-at headline links] :as activity-data}]
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
        ;   (board-info-hover {:activity-data activity-data})
        ;   [:span.board-name
        ;     board-name]]
        [:div.separator-dot]
        [:span.time-since
          [:time
            {:date-time published-at}
            (utils/tooltip-date published-at)]]
        [:div.reply-item-title
          (str "→ " headline)]
        (when (or follow-link unfollow-link)
          [:button.mlb-reset.mute-bt
            {:title (if follow-link
                      "Get notified about new activity"
                      "Don't show future replies to this update")
             :class (if follow-link "unfollowing" "following")
             :data-toggle (when-not (responsive/is-mobile-size?) "tooltip")
             :data-placement "top"
             :data-container "body"
             :on-click #(activity-actions/inbox-unfollow (:uuid activity-data))}])]]))

(defn- reply-item-unique-class [{:keys [uuid]}]
  (str "reply-item-" uuid))

(def add-comment-focus-prefix "reply-comment")

(defn- expand-reply [s reply-data]
  (let [replies @(::replies s)
        idx (utils/index-of replies #(= (:uuid %) (:uuid reply-data)))]
    (swap! (::replies s) (fn [reply]
                           (-> reply
                             (assoc-in [idx :collapsed-count] 0)
                             (update-in [idx :thread-children]
                              (fn [children]
                                (map #(assoc % :expanded true) children))))))))

(defn- reply-mark-read [s reply-uuid]
  (let [replies @(::replies s)
        idx (utils/index-of replies #(= (:uuid %) reply-uuid))]
    (swap! (::replies s) (fn [replies]
                           (-> replies
                             (assoc-in [idx :unread] false)
                             (update-in [idx :thread-children]
                              (fn [children]
                                (map #(assoc % :unread false) children))))))))

(defn- comment-mark-read [s reply-data]
  (let [replies @(::replies s)]
    (if-not (seq (:parent-uuid reply-data))
      (reply-mark-read s (:uuid reply-data))
      (let [idx (utils/index-of replies #(= (:uuid %) (:parent-uuid reply-data)))]
        (swap! (::replies s) (fn [replies]
                               (update-in replies [idx :thread-children]
                                (fn [children]
                                  (map #(if (= (:uuid %) (:uuid reply-data))
                                          (assoc % :unread false)
                                          %)
                                   children)))))))))

(defn- reply-thread-item
  [s {:keys [activity-data reply-data is-mobile? read-reply-cb member?
             current-user-id read-comment-cb]}]
  (let [showing-picker? (and (seq @(::show-picker s))
                             (= @(::show-picker s) (:uuid reply-data)))
        reply-focus-value (cu/add-comment-focus-value add-comment-focus-prefix (:uuid activity-data) (:uuid reply-data))
        collapsed-count (:collapsed-count reply-data)
        show-more-bt? (and (:expanded reply-data)
                           (pos? collapsed-count))
        replying-to (@(::replying s) (:uuid reply-data))
        has-expanded? (< collapsed-count (count (:thread-children reply-data)))]
    [:div.reply-item-block.vertical-line.group
      {:key (str "reply-thread-item-" (:uuid reply-data))}
      (when (or (:expanded reply-data)
                has-expanded?)
        (reply-comment {:activity-data activity-data
                        :comment-data reply-data
                        :truncated-body? (and (not (:expanded reply-data))
                                              has-expanded?)
                        :is-mobile? is-mobile?
                        :react-cb #(reset! (::show-picker s) (:uuid reply-data))
                        :reply-cb #(reply-to s (:uuid reply-data) reply-focus-value)
                        :did-react-cb #(read-reply-cb (:uuid reply-data))
                        :emoji-picker (when showing-picker?
                                        (emoji-picker-container s activity-data reply-data read-comment-cb))
                        :showing-picker? showing-picker?
                        :new-thread? (:unread reply-data)
                        :unread (:unread reply-data)
                        :member? member?
                        :replies-count (:replies-count reply-data)
                        :current-user-id current-user-id
                        :replying-to replying-to}))
      (when show-more-bt?
        [:button.mlb-reset.expand-thead-bt
          {:on-click #(expand-reply s reply-data)}
          (str "View " collapsed-count " older repl" (if (not= collapsed-count 1) "ies" "y"))])
      (for [inner-reply-data (:thread-children reply-data)
            :let [ind-showing-picker? (and (seq @(::show-picker s))
                                           (= @(::show-picker s) (:uuid inner-reply-data)))]
            :when (:expanded inner-reply-data)]
        [:div.reply-item-block.horizontal-line.group
          {:key (str "unir-" (:uuid inner-reply-data) "-" (:created-at inner-reply-data))
           :data-resource-uuid (:resource-uuid inner-reply-data)
           :data-comment-uuid (:uuid inner-reply-data)
           :data-parent-uuid (:parent-uuid inner-reply-data)}
          (reply-comment {:activity-data activity-data
                          :comment-data inner-reply-data
                          :is-indented-comment? true
                          :is-mobile? is-mobile?
                          :react-cb #(reset! (::show-picker s) (:uuid inner-reply-data))
                          :reply-cb #(reply-to s (:uuid reply-data) reply-focus-value)
                          :did-react-cb #(read-comment-cb inner-reply-data)
                          :emoji-picker (when ind-showing-picker?
                                          (emoji-picker-container s activity-data inner-reply-data read-comment-cb))
                          :showing-picker? ind-showing-picker?
                          :new-thread? (:unread reply-data)
                          :member? member?
                          :current-user-id current-user-id}
                          :replying-to replying-to)])
      (if (@(::replying s) (:uuid reply-data))
        (rum/with-key (add-comment {:activity-data activity-data
                                    :parent-comment-uuid (:uuid reply-data)
                                    :collapse? true
                                    :add-comment-placeholder "Reply..."
                                    :add-comment-cb (fn [new-comment-data]
                                                      (read-reply-cb (:uuid reply-data)))
                                    :add-comment-focus-prefix add-comment-focus-prefix
                                    :dismiss-reply-cb #(swap! (::replying s) disj (:uuid reply-data))})
         (str "adc-" (:uuid reply-data))))]))

(defn- update-replies [s]
  (let [props (-> s :rum/args first)
        all-comments (cu/ungroup-comments @(::replies s))
        expanded-unread-map (map #(select-keys % [:expanded :unread]) all-comments)
        collapsed-map (zipmap (map :uuid all-comments) expanded-unread-map)
        strict-collapse? @(::strict-collapse s)
        collapse-comments-fn (if strict-collapse? cu/strict-collapse-comments cu/collapse-comments)
        collapsed-comments (collapse-comments-fn (:initial-last-read-at props) (:comments-data props) collapsed-map)
        all-comments-after (cu/ungroup-comments collapsed-comments)
        total-collapsed-count (if strict-collapse?
                                (count (filter (comp not :expanded) all-comments-after))
                                0)]
    (reset! (::replies s) collapsed-comments)
    (reset! (::total-collapsed-count s) total-collapsed-count)))

(rum/defcs reply-item < rum/static
                        (rum/local nil ::show-picker)
                        (rum/local #{} ::replying)
                        (rum/local nil ::replies)
                        (rum/local nil ::total-collapsed-count)
                        (rum/local true ::strict-collapse)
                        ui-mixins/refresh-tooltips-mixin
                        (mention-mixins/oc-mentions-hover {:click? true})
                        (ui-mixins/interactive-images-mixin "div.reply-comment-body")
                        (ui-mixins/on-window-click-mixin (fn [s e]
                         (when (and @(::show-picker s)
                                    (not (utils/event-inside? e
                                     (.get (js/$ "div.emoji-mart" (rum/dom-node s)) 0))))
                           (reset! (::show-picker s) nil))))
                        {:will-mount (fn [s]
                           (update-replies s)
                         s)
                         :did-remount (fn [o s]
                           (let [items (-> s :rum/args first :comments-data)
                                 items-changed (clj-data/diff (-> o :rum/args first :comments-data) items)]
                              (when (or (seq (first items-changed))
                                        (seq (second items-changed)))
                                (update-replies s)))
                            s)}
  [s {current-user-id  :current-user-id
      uuid             :uuid
      publisher        :publisher
      unread           :unread
      published-at     :published-at
      member?          :member?
      comments-data    :comments-data
      initial-last-read-at :initial-last-read-at
      :as activity-data}]
  (let [is-mobile? (responsive/is-mobile-size?)
        reply-item-class (reply-item-unique-class activity-data)
        replies @(::replies s)
        comments-loaded? (not (seq replies))]
    [:div.reply-item.group
      {:class (utils/class-set {:unread unread
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
                                (not (utils/event-inside? e (.querySelector reply-el "div.emoji-mart")))
                                (not (utils/event-inside? e (.querySelector reply-el "div.add-comment-box-container"))))
                       (nav-actions/open-post-modal activity-data false))))}
      (reply-top activity-data)
      (if comments-loaded?
        [:div.reply-item-blocks.group
          [:div.reply-item-loading.group
            (small-loading)
            [:span.reply-item-loading-inner
              "Loading replies..."]]]
        [:div.reply-item-blocks.group
          (for [reply @(::replies s)
                :let [payload {:activity-data activity-data
                               :reply-data reply
                               :is-mobile? is-mobile?
                               :read-reply-cb (partial reply-mark-read s)
                               :member? member?
                               :current-user-id current-user-id
                               :read-comment-cb (partial comment-mark-read s)}]]
            (reply-thread-item s payload))
          (when (and @(::strict-collapse s)
                     (pos? @(::total-collapsed-count s)))
            [:button.mlb-reset.expand-all-bt
              {:on-click (fn [_e]
                           (reset! (::strict-collapse s) false)
                           (update-replies s))}
              (str "View " @(::total-collapsed-count s) " older repl" (if (not= @(::total-collapsed-count s) 1) "ies" "y"))])])]))

(defn- mark-read-if-needed [s items-container offset-top item]
  (when-let [item-node (.querySelector items-container (str "div." (reply-item-unique-class item)))]
    (when (dom-utils/is-element-top-in-viewport? item-node offset-top)
      (let [read (activity-actions/mark-read (:resource-uuid item))]
        (when read
          (swap! (::read-items s) conj (:resource-uuid item)))))))

(defn- did-scroll [s _scroll-event]
  (when @(::has-unread-items s)
    (when-let [items-container (rum/ref-node s :entries-list)]
      (let [items @(::entries s)
            offset-top (if (responsive/is-mobile-size?) responsive/mobile-navbar-height responsive/navbar-height)]
        (doseq [item items
                :when (and (= (:resource-type item) :entry)
                           (pos? (:unread-count item))
                           (not (@(::read-items s) (:resource-uuid item))))]
          (mark-read-if-needed s items-container offset-top item))
        (when-not (some (comp pos? :unread-count) @(::entries s))
          (reset! (::has-unread-items s) false))))))

(rum/defcs replies-list <
  rum/reactive
  (drv/drv :users-info-hover)
  (drv/drv :comments-data)
  ui-mixins/refresh-tooltips-mixin
  (rum/local #{} ::read-items)
  (rum/local false ::has-unread-items)
  (rum/local [] ::entries)
  (rum/local nil ::initial-last-read-at)
  (ui-mixins/on-window-scroll-mixin did-scroll)
  {:will-mount (fn [s]
    (let [entries (-> s :rum/args first :items-to-render)
          last-read-at (reduce (fn [r t]
                                 (update r (:uuid t) #(if (pos? (compare % (:last-read-at t)))
                                                        %
                                                        (:last-read-at t))))
                        {}
                        entries)]
     (reset! (::entries s) entries)
     (reset! (::has-unread-items s) (some :unread entries))
     (reset! (::initial-last-read-at s) last-read-at))
    s)
   :did-mount (fn [s]
     (did-scroll s nil)
   s)
   :did-remount (fn [o s]
   (let [entries (-> s :rum/args first :items-to-render)
         items-changed (clj-data/diff (-> o :rum/args first :items-to-render) entries)]
     (when (or (seq (first items-changed))
               (seq (second items-changed)))
       (let [last-read-at (reduce (fn [r t]
                                    (update r (:uuid t) #(if (pos? (compare % (:last-read-at t)))
                                                           %
                                                           (:last-read-at t))))
                            @(::initial-last-read-at s)
                            entries)]
         (reset! (::entries s) entries)
         (reset! (::has-unread-items s) (some :unread entries))
         (reset! (::initial-last-read-at s) last-read-at)
         (utils/after 0 #(did-scroll s nil)))))
   s)}
  [s {:keys [items-to-render last-read-at current-user-data member?]}]
  (let [is-mobile? (responsive/is-mobile-size?)
        items @(::entries s)
        _users-info-hover (drv/react s :users-info-hover)
        comments-drv (drv/react s :comments-data)]
    [:div.replies-list
      (if (empty? items)
        [:div.replies-list-empty
          (all-caught-up)]
        [:div.replies-list-container
          {:ref :entries-list}
          (for [item* items
                :let [caught-up? (= (:resource-type item*) :caught-up)
                      loading-more? (= (:resource-type item*) :loading-more)
                      closing-item? (= (:resource-type item*) :closing-item)
                      item (assoc item* :current-user-data current-user-data
                                        :member? member?
                                        :comments-data (au/activity-comments item* comments-drv)
                                        :initial-last-read-at (get @(::initial-last-read-at s) (:uuid item*)))]]
            (cond
              caught-up?
              (rum/with-key
               (caught-up-line item)
               (str "reply-caught-up-" (:last-activity-at item)))
              loading-more?
              [:div.loading-updates.bottom-loading
                {:key (str "reply-loading-more-" (:last-activity-at item))}
                (:message item)]
              closing-item?
              [:div.closing-item
                {:key (str "reply-closing-item-" (:last-activity-at item))}
                (:message item)]
              :else
              (rum/with-key
               (reply-item item)
               (str "reply-" (:resource-uuid item) "-" (:uuid item)))))])]))