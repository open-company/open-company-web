(ns oc.web.components.threads-list
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [goog.object :as gobj]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.comment :as cu]
            [oc.web.utils.activity :as au]
            [oc.web.mixins.activity :as am]
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
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.ui.all-caught-up :refer (all-caught-up)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.info-hover-views :refer (user-info-hover board-info-hover)]))

(defn- reply-to [s add-comment-focus-key]
  (reset! (::replying s) true)
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

(defn- emoji-picker-container [s activity-data comment-data]
  (let [showing-picker? (and (seq @(::show-picker s))
                             (= @(::show-picker s) (:uuid comment-data)))]
    (when showing-picker?
      (emoji-picker {:dismiss-cb #(reset! (::show-picker s) nil)
                     :add-emoji-cb (fn [emoji]
                                     (when (reaction-utils/can-pick-reaction? (gobj/get emoji "native") (:reactions comment-data))
                                       (comment-actions/react-from-picker activity-data comment-data
                                        (gobj/get emoji "native")))
                                     (reset! (::show-picker s) nil))}))))

(rum/defc thread-comment < rum/static
  [{:keys [activity-data comment-data
           is-indented-comment? mouse-leave-cb
           react-cb reply-cb emoji-picker
           is-mobile? member?
           showing-picker? did-react-cb new-thread?
           current-user-id replies-count]}]
  [:div.thread-comment-outer
    {:key (str "thread-comment-" (:created-at comment-data))
     :data-comment-uuid (:uuid comment-data)
     :class (utils/class-set {:open-thread (not is-indented-comment?)
                              :new-comment (:unread comment-data)
                              :indented-comment is-indented-comment?
                              :showing-picker showing-picker?
                              :no-replies (zero? replies-count)})}
    [:div.thread-comment
      {:ref (str "thread-comment-" (:uuid comment-data))
       :on-mouse-leave mouse-leave-cb}
      [:div.thread-comment-inner
        (when is-mobile?
          [:div.thread-comment-mobile-menu
            (more-menu {:entity-data comment-data
                        :external-share false
                        :entity-type "comment"
                        :can-react? true
                        :react-cb react-cb
                        :can-reply? true
                        :reply-cb reply-cb})
            emoji-picker])
        [:div.thread-comment-right
          [:div.thread-comment-header.group
            {:class utils/hide-class}
            [:div.thread-comment-author-right
              [:div.thread-comment-author-right-group
                {:class (when (:unread comment-data) "new-comment")}
                [:div.thread-comment-author-name-container
                  (user-info-hover {:user-data (:author comment-data) :current-user-id current-user-id :leave-delay? true})
                  [:div.thread-comment-author-avatar
                    (user-avatar-image (:author comment-data))]
                  [:div.thread-comment-author-name
                    {:class (when (:user-id (:author comment-data)) "clickable-name")}
                    (:name (:author comment-data))]]
                [:div.thread-comment-author-timestamp
                  [:time
                    {:date-time (:created-at comment-data)
                     :data-toggle (when-not is-mobile? "tooltip")
                     :data-placement "top"
                     :data-container "body"
                     :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                     :data-title (utils/activity-date-tooltip comment-data)}
                    (utils/foc-date-time (:created-at comment-data))]]]
              (when (and (:unread comment-data)
                         (or (and is-indented-comment?
                                  (not new-thread?))
                             (not is-indented-comment?)))
                [:div.new-comment-tag])
              (if (responsive/is-mobile-size?)
                [:div.thread-comment-mobile-menu
                  (more-menu comment-data nil {:external-share false
                                               :entity-type "comment"
                                               :can-react? true
                                               :react-cb react-cb
                                               :can-reply? true
                                               :reply-cb reply-cb})
                  emoji-picker]
                [:div.thread-comment-floating-buttons
                  ;; Reply to comment
                  (when (:reply-parent comment-data)
                    [:button.mlb-reset.floating-bt.reply-bt.separator-bt
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
          [:div.thread-comment-content
            [:div.thread-comment-body.oc-mentions.oc-mentions-hover
              {:dangerouslySetInnerHTML (utils/emojify (:body comment-data))
               :ref (str "comment-body-" (:uuid comment-data))
               :class (utils/class-set {:emoji-comment (:is-emoji comment-data)
                                        utils/hide-class true})}]]
          (when (seq (:reactions comment-data))
            [:div.thread-comment-reactions-footer.group
              (reactions {:entity-data comment-data
                          :hide-picker (zero? (count (:reactions comment-data)))
                          :did-react-cb did-react-cb
                          :optional-activity-data activity-data})])]]]])

(rum/defc thread-header
  [{last-activity-at :last-activity-at {:keys [board-name published-at] :as activity-data} :activity-data}]
  [:div.thread-item-header
    [:div.thread-item-board-name-container
      (board-info-hover {:activity-data activity-data})
      [:span.board-name
        board-name]]
    [:div.separator-dot]
    [:span.time-since
      [:time
        {:date-time published-at}
        (utils/tooltip-date published-at)]]])

(rum/defcs thread-item < rum/static
                         (rum/local nil ::show-picker)
                         (rum/local false ::replying)
                         ui-mixins/refresh-tooltips-mixin
                         (mention-mixins/oc-mentions-hover {:click? true})
                         (ui-mixins/interactive-images-mixin "div.thread-comment-body")
                         (ui-mixins/on-window-click-mixin (fn [s e]
                          (when (and @(::show-picker s)
                                     (not (utils/event-inside? e
                                      (.get (js/$ "div.emoji-mart" (rum/dom-node s)) 0))))
                            (reset! (::show-picker s) nil))))
                           (rum/local false ::expanded)
  [s
   {current-user-id  :current-user-id
    resource-uuid    :resource-uuid
    comment-uuid     :uuid
    last-activity-at :last-activity-at
    activity-data    :activity-data
    replies          :thread-children
    author           :author
    unread           :unread
    created-at       :created-at
    open-item        :open-item
    close-item       :close-item
    member?          :member?
    :as n}]
  (let [is-mobile? (responsive/is-mobile-size?)
        showing-picker? (and (seq @(::show-picker s))
                             (= @(::show-picker s) comment-uuid))
        add-comment-focus-prefix "thread-comment"
        replies-count (count replies)
        read-count (count (filter (comp not :unread) replies))
        collapsed-count (when (and (not @(::expanded s))
                                   (not (:unread n)))
                          ;; Count the read comments and remove one since last is always rendered
                          (if (= read-count replies-count)
                            (dec read-count)
                            read-count))]
    [:div.thread-item.group
      {:class    (utils/class-set {:unread unread
                                   :close-item close-item
                                   :open-item open-item})
       :ref :thread
       :on-click (fn [e]
                   (let [thread-el (rum/ref-node s :thread)]
                     (when (and (not (utils/button-clicked? e))
                                (not (utils/input-clicked? e))
                                (not (utils/anchor-clicked? e))
                                (not (utils/event-inside? e (.querySelector thread-el "div.add-comment-box-container"))))
                       (nav-actions/open-post-modal activity-data false comment-uuid))))}
      (when open-item
        (thread-header n))
      (when open-item
        [:div.thread-item-title
          (:headline activity-data)])
      [:div.thread-item-blocks.group
        [:div.thread-item-block.vertical-line.group
          (thread-comment {:activity-data activity-data
                           :comment-data n
                           :is-mobile? is-mobile?
                           :react-cb #(reset! (::show-picker s) comment-uuid)
                           :reply-cb #(reply-to s (cu/add-comment-focus-value add-comment-focus-prefix resource-uuid comment-uuid))
                           :emoji-picker (when showing-picker?
                                           (emoji-picker-container s activity-data n))
                           :showing-picker? showing-picker?
                           :new-thread? unread
                           :unread unread
                           :member? member?
                           :replies-count replies-count
                           :current-user-id current-user-id})
          (when-not (contains? n :thread-children)
            [:div.thread-item-loading
              (small-loading)
              "Loading thread..."])
          (when (and (not @(::expanded s))
                     (pos? collapsed-count))
            [:button.mlb-reset.expand-thead-bt
              {:on-click #(reset! (::expanded s) true)}
              (str "View " collapsed-count " older repl" (if (not= collapsed-count 1) "ies" "y"))])
          (for [idx (range (count replies))
                :let [r (get replies idx)
                      ind-showing-picker? (and (seq @(::show-picker s))
                                               (= @(::show-picker s) (:uuid r)))
                      unread-reply? (au/comment-unread? (:created-at r) (:last-read-at activity-data))
                      reply-data (assoc r :unread unread-reply?)]
                :when (or (< collapsed-count 1)
                          unread-reply?
                          (= (dec (count replies)) idx))]
            [:div.thread-item-block.horizontal-line.group
              {:key (str "unir-" (:created-at r) "-" (:uuid r))}
              (thread-comment {:activity-data activity-data
                               :comment-data reply-data
                               :is-indented-comment? true
                               :is-mobile? is-mobile?
                               :react-cb #(reset! (::show-picker s) (:uuid r))
                               :reply-cb #(reply-to s (cu/add-comment-focus-value add-comment-focus-prefix resource-uuid comment-uuid))
                               :emoji-picker (when ind-showing-picker?
                                               (emoji-picker-container s activity-data r))
                               :showing-picker? ind-showing-picker?
                               :new-thread? unread
                               :member? member?
                               :current-user-id current-user-id})])]]

      (rum/with-key (add-comment {:activity-data activity-data
                                  :parent-comment-uuid comment-uuid
                                  :collapsed? true
                                  :add-comment-placeholder "Reply..."
                                  :add-comment-cb (partial user-actions/activity-reply-inline n)
                                  :add-comment-focus-prefix add-comment-focus-prefix
                                  :dismiss-reply-cb #(reset! (::replying s) false)})
       (str "adc-" resource-uuid  last-activity-at))]))

(defn- expand-thread [s comment-data]
  (let [threads @(::threads s)
        idx (utils/index-of threads #(= (:uuid %) (:uuid comment-data)))]
    (swap! (::threads s) (fn [thread]
                           (-> thread
                             (assoc-in [idx :collapsed-count] 0)
                             (update-in [idx :thread-children]
                              (fn [children]
                                (map #(assoc % :expanded true) children))))))))

(rum/defcs threads-list <
  ui-mixins/refresh-tooltips-mixin
  [s {:keys [items-to-render current-user-data member? loading-more]}]
  (let [is-mobile? (responsive/is-mobile-size?)]
    [:div.threads-list
      (if (empty? items-to-render)
        [:div.threads-list-empty
          (all-caught-up)]
        [:div.threads-list-container
          (for [item* items-to-render
                :let [caught-up? (= (:content-type item*) :caught-up)
                      item (assoc item* :current-user-data current-user-data :member? member?)]]
            (if caught-up?
              [:div.threads-list-caught-up
                {:key (str "threads-caught-up-" (:last-activity-at item))}
                (all-caught-up "You’re all caught up")]
              (rum/with-key
               (thread-item item)
               (str "thread-" (:resource-uuid item) "-" (:uuid item)))))
          (when loading-more
            [:div.loading-updates.bottom-loading
              "Loading more posts..."])])]))