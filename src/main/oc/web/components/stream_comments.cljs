(ns oc.web.components.stream-comments
  (:require [rum.core :as rum]
            [clojure.data :refer (diff)]
            [goog.object :as gobj]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
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
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.info-hover-views :refer (user-info-hover)]
            ["emoji-mart" :as emoji-mart :refer (Picker)]))

(def max-reactions-count 5)

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
        activity-data (-> s :rum/args first :activity-data)]
    (comment-actions/edit-comment (:uuid activity-data) comment-data)
    (reset! (::show-more-menu s) nil)
    (reset! (::editing? s) (:uuid comment-data))))

(defn delete-clicked [e activity-data comment-data]
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :action "delete-comment"
                    :message (if (seq (:thread-children comment-data))
                               "Delete this comment thread?"
                               "Delete this comment?")
                    :link-button-title "No"
                    :link-button-cb #(alert-modal/hide-alert)
                    :solid-button-style :red
                    :solid-button-title "Yes"
                    :solid-button-cb (fn [_]
                                       (comment-actions/delete-comment activity-data comment-data)
                                       (alert-modal/hide-alert))
                    }]
    (alert-modal/show-alert alert-data)))

(defn scroll-to-bottom [s]
  (let [scrolling-node (rum/dom-node s)]
    (set! (.-scrollTop scrolling-node) (.-scrollHeight scrolling-node))))

(defn emoji-picked-cb [s comment-data emoji]
  (comment-actions/react-from-picker (-> s :rum/args first :activity-data) comment-data (get emoji "native")))

(def add-comment-prefix "edit-comment")

(defn- copy-comment-url [comment-url]
  (let [input-field (.createElement js/document "input")]
    (set! (.-style input-field) "position:absolute;top:-999999px;left:-999999px;")
    (set! (.-value input-field) comment-url)
    (.appendChild (.-body js/document) input-field)
    (.select input-field)
    (utils/copy-to-clipboard input-field)
    (.removeChild (.-body js/document) input-field)))

(defn- comment-mark-read [s comment-uuid]
  (let [threads @(::threads s)
        idx (utils/index-of threads #(= (:uuid %) comment-uuid))]
    (swap! (::threads s) (fn [threads]
                           (assoc-in threads [idx :unread] false)))))

(rum/defc emoji-picker < (when (responsive/is-mobile-size?)
                           ui-mixins/no-scroll-mixin)
  [{:keys [add-emoji-cb dismiss-cb]}]
  [:div.emoji-picker-container
    [:button.mlb-reset.close-bt
      {:on-click dismiss-cb}
      "Cancel"]
    (react-utils/build Picker
      {:native true
       :autoFocus true
       :onClick (fn [emoji _]
                  (add-emoji-cb emoji))})])

(defn- emoji-picker-container [s comment-data]
  (let [activity-data (-> s :rum/args first :activity-data)
        showing-picker? (and (seq @(::show-picker s))
                             (= @(::show-picker s) (:uuid comment-data)))]
    (when showing-picker?
      (emoji-picker {:dismiss-cb #(reset! (::show-picker s) nil)
                     :add-emoji-cb (fn [emoji]
       (when (reaction-utils/can-pick-reaction? (gobj/get emoji "native") (:reactions comment-data))
         (comment-mark-read s (:uuid comment-data))
         (comment-actions/react-from-picker activity-data comment-data
          (gobj/get emoji "native")))
       (reset! (::show-picker s) nil))}))))

(rum/defc edit-comment < rum/static
  [{:keys [activity-data comment-data dismiss-reply-cb
           edit-comment-key add-comment-cb add-comment-did-change]}]
  [:div.stream-comment-outer.open-thread
    {:key (str "stream-comment-" (:created-at comment-data))
     :data-comment-uuid (:uuid comment-data)}
    [:div.stream-comment
      (rum/with-key
       (add-comment {:activity-data activity-data
                     :parent-comment-uuid (:reply-parent comment-data)
                     :dismiss-reply-cb dismiss-reply-cb
                     :add-comment-cb add-comment-cb
                     :add-comment-did-change add-comment-did-change
                     :edit-comment-data comment-data
                     :add-comment-focus-prefix add-comment-prefix})
       (str "edit-comment-" edit-comment-key))]])


(rum/defc read-comment < rum/static
  [{:keys [activity-data comment-data editing?
           edit-comment-key mouse-leave-cb
           edit-cb delete-cb react-cb reply-cb emoji-picker
           is-mobile? can-show-edit-bt? can-show-delete-bt? member?
           show-more-menu showing-picker? did-react-cb
           current-user-id]}]
  [:div.stream-comment-outer.open-thread
    {:key (str "stream-comment-" (:created-at comment-data))
     :data-comment-uuid (:uuid comment-data)
     :class (utils/class-set {:new-comment (:unread comment-data)
                              :showing-picker showing-picker?})}
    [:div.stream-comment
      {:ref (str "stream-comment-" (:uuid comment-data))
       :class (utils/class-set {:editing-other-comment editing?})
       :on-mouse-leave mouse-leave-cb}
      [:div.stream-comment-inner
        (when is-mobile?
          [:div.stream-comment-mobile-menu
            (more-menu {:entity-data comment-data
                        :entity-type "comment"
                        :show-edit? true
                        :edit-cb edit-cb
                        :show-delete? true
                        :delete-cb delete-cb
                        :can-react? true
                        :react-cb react-cb
                        :can-reply? true
                        :reply-cb reply-cb})
            emoji-picker])
        [:div.stream-comment-right
          [:div.stream-comment-header.group
            {:class utils/hide-class}
            [:div.stream-comment-author-right
              [:div.stream-comment-author-right-group
                {:class (when (:unread comment-data) "new-comment")}
                [:div.stream-comment-author-name-container
                  (user-info-hover {:user-data (:author comment-data) :current-user-id current-user-id :leave-delay? true})
                  [:div.stream-comment-author-avatar
                    (user-avatar-image (:author comment-data))]
                  [:div.stream-comment-author-name
                    (:name (:author comment-data))]]
                [:div.stream-comment-author-timestamp
                  [:time
                    {:date-time (:created-at comment-data)
                     :data-toggle (when-not is-mobile? "tooltip")
                     :data-placement "top"
                     :data-container "body"
                     :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                     :title (utils/activity-date-tooltip comment-data)}
                    (utils/foc-date-time (:created-at comment-data))]]]
              (when (:unread comment-data)
                [:div.new-comment-tag])
              (if (responsive/is-mobile-size?)
                [:div.stream-comment-mobile-menu
                  (more-menu comment-data nil {:entity-type "comment"
                                               :show-edit? true
                                               :edit-cb edit-cb
                                               :show-delete? true
                                               :delete-cb delete-cb
                                               :can-react? (fn? did-react-cb)
                                               :react-cb react-cb
                                               :can-reply? true
                                               :reply-cb reply-cb})
                  emoji-picker]
                [:div.stream-comment-floating-buttons
                  {:key (str "stream-comment-floating-buttons"
                         (when can-show-edit-bt?
                           "-edit")
                         (when can-show-delete-bt?
                           "-delete"))}
                  (when (or can-show-delete-bt?
                            can-show-edit-bt?)
                    [:div.more-bt-container
                      [:button.mlb-reset.floating-bt.more-bt
                        {:on-click #(reset! show-more-menu (:uuid comment-data))}]
                      (when (= @show-more-menu (:uuid comment-data))
                        [:div.comment-more-menu-container
                          (when can-show-delete-bt?
                            [:button.mlb-reset.delete-bt
                              {:on-click #(delete-cb comment-data)}
                              "Delete"])
                          (when can-show-edit-bt?
                            [:button.mlb-reset.edit-bt
                              {:on-click #(edit-cb comment-data)}
                              "Edit"])])])
                  ;; Reply to comment
                  (when (:reply-parent comment-data)
                    [:button.mlb-reset.floating-bt.reply-bt
                      {:data-toggle "tooltip"
                       :data-placement "top"
                       :data-container "body"
                       :on-click reply-cb
                       :title "Reply"}])
                  ;; React container
                  (when did-react-cb
                    [:div.react-bt-container
                      [:button.mlb-reset.floating-bt.react-bt
                        {:data-toggle "tooltip"
                        :data-placement "top"
                        :data-container "body"
                        :title "Add reaction"
                        :on-click react-cb}]
                      emoji-picker])])]]
          [:div.stream-comment-content
            [:div.stream-comment-body.oc-mentions.oc-mentions-hover
              {:dangerouslySetInnerHTML (utils/emojify (:body comment-data))
               :ref (str "comment-body-" (:uuid comment-data))
               :class (utils/class-set {:emoji-comment (:is-emoji comment-data)
                                        utils/hide-class true})}]]
          (when (seq (:reactions comment-data))
            [:div.stream-comment-reactions-footer.group
              (reactions {:entity-data comment-data
                          :hide-picker (zero? (count (:reactions comment-data)))
                          :did-react-cb did-react-cb
                          :max-reactions max-reactions-count
                          :optional-activity-data activity-data})])]]]])

(defn- quoted-reply-header [comment-data]
  (str "<div class=\"oc-replying-to\" contenteditable=\"false\">↩︎ Replying to " (-> comment-data :author :name) "</div>"))

(defn- reply-to [comment-data add-comment-focus-key]
  (comment-actions/reply-to add-comment-focus-key (str (quoted-reply-header comment-data) (:body comment-data)) true))

(rum/defcs stream-comments < rum/reactive
                             (drv/drv :add-comment-focus)
                             (drv/drv :add-comment-data)
                             (drv/drv :users-info-hover)
                             (drv/drv :current-user-data)
                             (drv/drv :follow-publishers-list)
                             (drv/drv :followers-publishers-count)
                             (rum/local false ::last-focused-state)
                             (rum/local nil ::editing?)
                             (rum/local nil ::show-picker)
                             (rum/local {} ::comment-url-copy)
                             (rum/local false ::initial-comment-scroll)
                             (rum/local nil ::show-more-menu)
                             (rum/local [] ::threads)
                             (drv/drv :add-comment-force-update)
                             ;; Mixins
                             (mention-mixins/oc-mentions-hover {:click? true})
                             ui-mixins/refresh-tooltips-mixin
                             (ui-mixins/interactive-images-mixin "div.stream-comment-body")
                             (ui-mixins/on-window-click-mixin (fn [s e]
                              (when (and @(::show-picker s)
                                         (not (utils/event-inside? e
                                          (.get (js/$ "div.emoji-mart" (rum/dom-node s)) 0))))
                                (reset! (::show-picker s) nil))))
                             {:after-render (fn [s]
                               (let [activity-uuid (-> s :rum/args first :activity-data :uuid)
                                     focused-uuid @(drv/get-ref s :add-comment-focus)
                                     current-local-state @(::last-focused-state s)
                                     is-self-focused? (= focused-uuid activity-uuid)]
                                  (when (not= current-local-state is-self-focused?)
                                    (reset! (::last-focused-state s) is-self-focused?)
                                    (when is-self-focused?
                                      (scroll-to-bottom s))))
                               s)
                             :will-mount (fn [s]
                              (let [{:keys [last-read-at comments-data]} (-> s :rum/args first)
                                    threads (cu/collapse-comments comments-data last-read-at)
                                    fixed-threads (filterv #(not (au/resource-type? % :collapsed-comments)) threads)]
                                (reset! (::threads s) fixed-threads))
                              s)
                             :did-remount (fn [o s]
                              (let [{:keys [comments-data last-read-at]} (-> s :rum/args first)
                                    comments-diff (diff (-> o :rum/args first :comments-data) comments-data)]
                                (when (or (seq (first comments-diff))
                                          (seq (second comments-diff))
                                          (not= last-read-at (-> o :rum/args first :last-read-at)))
                                  (let [all-comments (vec (mapcat #(concat [%] (:thread-children %)) @(::threads s)))
                                        collapsed-map (zipmap (map :uuid all-comments) (map #(select-keys % [:expanded :unread]) all-comments))
                                        updated-threads (cu/collapse-comments comments-data last-read-at)
                                        fixed-updated-threads (filterv #(not (au/resource-type? % :collapsed-comments)) updated-threads)]
                                    (reset! (::threads s) fixed-updated-threads))))
                              s)}
  [s {:keys [activity-data comments-data last-read-at current-user-id member? reply-add-comment-prefix loading-comments-count]}]
  (let [_users-info-hover (drv/react s :users-info-hover)
        _current-user-data (drv/react s :current-user-data)
        _follow-publishers-list (drv/react s :follow-publishers-list)
        _followers-publishers-count (drv/react s :followers-publishers-count)
        add-comment-force-update* (drv/react s :add-comment-force-update)
        is-mobile? (responsive/is-mobile-size?)
        threads (filter au/comment? @(::threads s))
        all-comments (vec (mapcat #(concat [%] (:thread-children %)) threads))
        reply-focus-value (cu/add-comment-focus-value reply-add-comment-prefix (:uuid activity-data))]
    [:div.stream-comments
      {:class (when (seq @(::editing? s)) "editing")}
      (if (pos? (count threads))
        [:div.stream-comments-list
          (when (pos? loading-comments-count)
            [:div.stream-comments-list-loading
              (small-loading)
              [:span.span.stream-comments-loading-inner
                "Loading " loading-comments-count " more comments..."]])
          (for [idx (range (count threads))
                :let [root-comment-data (nth threads idx)
                      is-editing? (and (seq @(::editing? s))
                                       (= @(::editing? s) (:uuid root-comment-data)))
                      add-comment-string-key (dis/add-comment-string-key (:uuid activity-data) (:reply-parent root-comment-data)
                                              (:uuid root-comment-data))
                      edit-comment-key (get-in add-comment-force-update* add-comment-string-key)
                      can-react? (< (count (:reactions root-comment-data)) max-reactions-count)
                      showing-picker? (and (seq @(::show-picker s))
                                           (= @(::show-picker s) (:uuid root-comment-data)))]]
            [:div.stream-comment-thread
              {:key (str "stream-comments-thread-" (:uuid root-comment-data))}
              (if is-editing?
                (edit-comment {:activity-data activity-data
                               :comment-data root-comment-data
                               :dismiss-reply-cb (partial finish-edit s root-comment-data)
                               :edit-comment-key edit-comment-key})
                (read-comment {:activity-data activity-data
                               :comment-data root-comment-data
                               :editing? (not (nil? @(::editing? s)))
                               :mouse-leave-cb #(compare-and-set! (::show-more-menu s) (:uuid root-comment-data) nil)
                               :is-mobile? is-mobile?
                               :can-show-edit-bt? (and (:can-edit root-comment-data)
                                                       (not (:is-emoji root-comment-data)))
                               :can-show-delete-bt? (:can-delete root-comment-data)
                               :edit-cb (partial start-editing s)
                               :delete-cb (partial delete-clicked s activity-data)
                               :react-cb (when can-react? #(reset! (::show-picker s) (:uuid root-comment-data)))
                               :reply-cb #(reply-to root-comment-data reply-focus-value)
                               :did-react-cb (when can-react?
                                               #(comment-mark-read s (:uuid root-comment-data)))
                               :emoji-picker (when showing-picker?
                                               (emoji-picker-container s root-comment-data))
                               :showing-picker? showing-picker?
                               :show-more-menu (::show-more-menu s)
                               :dismiss-reply-cb (partial finish-edit s root-comment-data)
                               :edit-comment-key edit-comment-key
                               :member? member?
                               :current-user-id current-user-id}))])])]))