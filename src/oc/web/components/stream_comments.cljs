(ns oc.web.components.stream-comments
  (:require [rum.core :as rum]
            [cljsjs.emoji-mart]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
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
    (utils/after 10 (fn [_]
     (swap! (::highlighting-comments s) #(conj % comment-id)
     (when scroll?
       (.scrollIntoView comment-node #js {:behavior "smooth" :block "center"}))
     (utils/after 1000 (fn [_] (swap! (::highlighting-comments s) #(disj % comment-id)))))))))

(defn- maybe-highlight-comment [s]
  (let [comments-data (-> s :rum/args first :comments-data)]
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

(defn- emoji-picker-container [s comment-data]
  (let [activity-data (-> s :rum/args first :activity-data)
        showing-picker? (and (seq @(::show-picker s))
                             (= @(::show-picker s) (:uuid comment-data)))]
    (when showing-picker?
      (emoji-picker {:dismiss-cb #(reset! (::show-picker s) nil)
                     :add-emoji-cb (fn [emoji]
       (when (reaction-utils/can-pick-reaction? (gobj/get emoji "native") (:reactions comment-data))
         (comment-actions/react-from-picker activity-data comment-data
          (gobj/get emoji "native")))
       (reset! (::show-picker s) nil))}))))

(rum/defc edit-comment < rum/static
  [{:keys [activity-data comment-data highlighted? closing-thread
           dismiss-reply-cb edit-comment-key is-indented-comment?
           add-comment-cb]}]
  [:div.stream-comment-outer
    {:key (str "stream-comment-" (:created-at comment-data))
     :data-comment-uuid (:uuid comment-data)
     :class (utils/class-set {:not-highlighted (not highlighted?)
                              :highlighted highlighted?
                              :open-thread (not is-indented-comment?)
                              :closing-thread closing-thread})}
    [:div.stream-comment
      {:class (utils/class-set {:indented-comment is-indented-comment?})}
      (rum/with-key
       (add-comment {:activity-data activity-data
                     :parent-comment-uuid (:reply-parent comment-data)
                     :dismiss-reply-cb dismiss-reply-cb
                     :add-comment-cb add-comment-cb
                     :edit-comment-data comment-data})
       (str "edit-comment-" edit-comment-key))]])


(rum/defc read-comment < rum/static
  [{:keys [activity-data comment-data highlighted? closing-thread
           editing? edit-comment-key is-indented-comment?
           mouse-leave-cb edit-cb delete-cb share-cb react-cb
           reply-cb emoji-picker is-mobile? can-show-edit-bt?
           can-show-delete-bt? show-more-menu showing-picker?]}]
  [:div.stream-comment-outer
    {:key (str "stream-comment-" (:created-at comment-data))
     :data-comment-uuid (:uuid comment-data)
     :class (utils/class-set {:not-highlighted (not highlighted?)
                              :highlighted highlighted?
                              :open-thread (not is-indented-comment?)
                              :closing-thread closing-thread})}
    [:div.stream-comment
      {:ref (str "stream-comment-" (:uuid comment-data))
       :class (utils/class-set {:editing-other-comment editing?
                                :showing-picker showing-picker?
                                :indented-comment is-indented-comment?})
       :on-mouse-leave mouse-leave-cb}
      [:div.stream-comment-inner
        (when is-mobile?
          [:div.stream-comment-mobile-menu
            (more-menu {:entity-data comment-data
                        :external-share false
                        :entity-type "comment"
                        :show-edit? true
                        :edit-cb edit-cb
                        :show-delete? true
                        :delete-cb delete-cb
                        :can-comment-share? true
                        :comment-share-cb share-cb
                        :can-react? true
                        :react-cb react-cb
                        :can-reply? true
                        :reply-cb reply-cb})
            emoji-picker])
        [:div.stream-comment-author-avatar
          (user-avatar-image (:author comment-data))]

        [:div.stream-comment-right
          [:div.stream-comment-header.group
            {:class utils/hide-class}
            [:div.stream-comment-author-right
              [:div.stream-comment-author-right-group
                {:class (when (:new comment-data) "new-comment")}
                [:div.stream-comment-author-name
                  (:name (:author comment-data))]
                [:div.stream-comment-author-timestamp
                  [:time
                    {:date-time (:created-at comment-data)
                     :data-toggle (when-not is-mobile? "tooltip")
                     :data-placement "top"
                     :data-container "body"
                     :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                     :data-title (utils/activity-date-tooltip comment-data)}
                    (utils/foc-date-time (:created-at comment-data))]]]
              (when (:new comment-data)
                [:div.new-comment-tag])
              (if (responsive/is-mobile-size?)
                [:div.stream-comment-mobile-menu
                  (more-menu comment-data nil {:external-share false
                                               :entity-type "comment"
                                               :show-edit? true
                                               :edit-cb edit-cb
                                               :show-delete? true
                                               :delete-cb delete-cb
                                               :can-comment-share? true
                                               :comment-share-cb share-cb
                                               :can-react? true
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
                  (if (or can-show-delete-bt?
                          can-show-edit-bt?)
                    [:div.more-bt-container.separator-bt
                      [:button.mlb-reset.floating-bt.more-bt.separator-bt
                        {:on-click #(reset! show-more-menu (:uuid comment-data))}
                        "More"]
                      (when (= @show-more-menu (:uuid comment-data))
                        [:div.comment-more-menu-container
                          [:button.mlb-reset.share-bt
                            {:on-click share-cb}
                            "Share"]
                          (when can-show-delete-bt?
                            [:button.mlb-reset.delete-bt
                              {:on-click #(delete-cb comment-data)}
                              "Delete"])
                          (when can-show-edit-bt?
                            [:button.mlb-reset.edit-bt
                              {:on-click #(edit-cb comment-data)}
                              "Edit"])])]
                    [:button.mlb-reset.floating-bt.share-bt.separator-bt
                      {:data-toggle "tooltip"
                       :data-placement "top"
                       :on-click share-cb
                       :title "Copy link"}
                      "Share"])
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
                          :optional-activity-data activity-data})])]]]])

(defn- expand-thread [s comment-data]
  (let [threads @(::threads s)
        idx (utils/index-of threads #(= (:uuid %) (:uuid comment-data)))]
    (swap! (::threads s) (fn [thread]
                           (-> thread
                             (assoc-in [idx :collapsed-count] 0)
                             (update-in [idx :thread-children]
                              (fn [children]
                                (map #(assoc % :expanded true) children))))))))

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
                             (rum/local [] ::threads)
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
                              (let [{:keys [current-user-id last-read-at comments-data]} (-> s :rum/args first)
                                    threads (cu/collapsed-comments current-user-id last-read-at comments-data)]
                                (reset! (::threads s) threads))
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
                              (when-let [new-added-comment (-> s :rum/args first :new-added-comment)]
                                (utils/after 180 #(highlight-comment s new-added-comment false))
                                (comment-actions/add-comment-highlight-reset))
                              (let [{:keys [comments-data current-user-id last-read-at]} (-> s :rum/args first)
                                    comments-diff (clojure.data/diff (-> o :rum/args first :comments-data) comments-data)]
                                (when (or (seq (first comments-diff))
                                          (seq (second comments-diff))
                                          (not= last-read-at (-> o :rum/args first :last-read-at)))
                                  (let [all-comments (vec (mapcat #(concat [%] (:thread-children %)) @(::threads s)))
                                        collapsed-map (zipmap (map :uuid all-comments) (map :expanded all-comments))]
                                    (reset! (::replying-to s) #{})
                                    (reset! (::threads s) (cu/collapsed-comments current-user-id last-read-at comments-data collapsed-map)))))
                              (try (js/emojiAutocomplete)
                                (catch :default e false))
                              s)}
  [s {:keys [activity-data comments-data new-added-comment last-read-at current-user-id add-comment-cb]}]
  (let [add-comment-force-update* (drv/react s :add-comment-force-update)
        is-mobile? (responsive/is-mobile-size?)
        threads @(::threads s)
        all-comments (vec (mapcat #(concat [%] (:thread-children %)) threads))
        collapsed-map (zipmap (map :uuid all-comments) (map :expanded all-comments))]
    [:div.stream-comments
      {:class (when (seq @(::editing? s)) "editing")}
      (if (pos? (count threads))
        [:div.stream-comments-list
          (for [idx (range (count threads))
                :let [root-comment-data (nth threads idx)
                      expanded-thread? (zero? (:collapsed-count root-comment-data))
                      highlighted? (utils/in? @(::highlighting-comments s) (:uuid root-comment-data))
                      is-editing? (and (seq @(::editing? s))
                                       (= @(::editing? s) (:uuid root-comment-data)))
                      add-comment-string-key (dis/add-comment-string-key (:uuid activity-data) (:replay-parent root-comment-data)
                                              (:uuid root-comment-data))
                      edit-comment-key (get-in add-comment-force-update* add-comment-string-key)
                      show-add-comment? (utils/in? @(::replying-to s) (:uuid root-comment-data))
                      closing-thread (and (-> root-comment-data :thread-children count zero?)
                                          (not show-add-comment?))
                      showing-picker? (and (seq @(::show-picker s))
                                           (= @(::show-picker s) (:uuid root-comment-data)))]]
            [:div.stream-comment-thread
              {:class (utils/class-set {:left-border (and (-> root-comment-data :thread-children count pos?)
                                                          (or (pos? (:new-count root-comment-data))
                                                              expanded-thread?))
                                        :has-new (pos? (:new-count root-comment-data))})
               :key (str "stream-comments-thread-" (:uuid root-comment-data))}
              (if is-editing?
                (edit-comment {:activity-data activity-data
                               :comment-data root-comment-data
                               :highlighted? highlighted?
                               :closing-thread closing-thread
                               :dismiss-reply-cb (partial finish-edit s root-comment-data)
                               :edit-comment-key edit-comment-key
                               :add-comment-cb add-comment-cb})
                (read-comment {:activity-data activity-data
                               :comment-data root-comment-data
                               :highlighted? highlighted?
                               :closing-thread closing-thread
                               :editing? (not (nil? @(::editing? s)))
                               :mouse-leave-cb #(compare-and-set! (::show-more-menu s) (:uuid root-comment-data) nil)
                               :is-mobile? is-mobile?
                               :can-show-edit-bt? (and (:can-edit root-comment-data)
                                                       (not (:is-emoji root-comment-data)))
                               :can-show-delete-bt? (:can-delete root-comment-data)
                               :edit-cb (partial start-editing s)
                               :delete-cb (partial delete-clicked s activity-data)
                               :share-cb #(share-clicked root-comment-data)
                               :react-cb #(reset! (::show-picker s) (:uuid root-comment-data))
                               :reply-cb #(reply-to s (:reply-parent root-comment-data))
                               :emoji-picker (when showing-picker?
                                               (emoji-picker-container s root-comment-data))
                               :showing-picker? showing-picker?
                               :show-more-menu (::show-more-menu s)
                               :dismiss-reply-cb (partial finish-edit s root-comment-data)
                               :edit-comment-key edit-comment-key}))
               (when (and (not expanded-thread?)
                          (pos? (:collapsed-count root-comment-data)))
                 [:button.mlb-reset.expand-thead-bt
                   {:on-click #(expand-thread s root-comment-data)}
                   (str "View " (:collapsed-count root-comment-data) " older repl" (if (not= (:collapsed-count root-comment-data) 1) "ies" "y"))])
               (for [idx (range (count (:thread-children root-comment-data)))
                     :let [comment-data (nth (:thread-children root-comment-data) idx)
                           ind-highlighted? (utils/in? @(::highlighting-comments s) (:uuid comment-data))
                           ind-is-editing? (and (seq @(::editing? s))
                                            (= @(::editing? s) (:uuid comment-data)))
                           ind-closing-thread (and (= idx (-> root-comment-data :thread-children count dec))
                                                   (not show-add-comment?))
                           ind-add-comment-string-key (dis/add-comment-string-key (:uuid activity-data) (:replay-parent comment-data)
                                                   (:uuid comment-data))
                           ind-edit-comment-key (get-in add-comment-force-update* add-comment-string-key)
                           ind-showing-picker? (and (seq @(::show-picker s))
                                                    (= @(::show-picker s) (:uuid comment-data)))]
                    :when (or expanded-thread?
                              (:expanded comment-data))]
                 [:div.stream-comment-child
                   {:key (str "stream-comments-thread-" (:uuid root-comment-data) "-child-" (:uuid comment-data))}
                   (if is-editing?
                     (edit-comment {:activity-data activity-data
                                    :comment-data comment-data
                                    :highlighted? ind-highlighted?
                                    :is-indented-comment? true
                                    :closing-thread ind-closing-thread
                                    :dismiss-reply-cb (partial finish-edit s comment-data)
                                    :edit-comment-key ind-edit-comment-key
                                    :add-comment-cb add-comment-cb})
                     (read-comment {:activity-data activity-data
                                    :comment-data comment-data
                                    :is-indented-comment? true
                                    :highlighted? ind-highlighted?
                                    :closing-thread ind-closing-thread
                                    :editing? (not (nil? @(::editing? s)))
                                    :mouse-leave-cb #(compare-and-set! (::show-more-menu s) (:uuid comment-data) nil)
                                    :is-mobile? is-mobile?
                                    :can-show-edit-bt? (and (:can-edit comment-data)
                                                            (not (:is-emoji comment-data)))
                                    :can-show-delete-bt? (:can-delete comment-data)
                                    :edit-cb (partial start-editing s)
                                    :delete-cb (partial delete-clicked s activity-data)
                                    :share-cb #(share-clicked comment-data)
                                    :react-cb #(reset! (::show-picker s) (:uuid comment-data))
                                    :reply-cb #(reply-to s (:reply-parent comment-data))
                                    :emoji-picker (when ind-showing-picker?
                                                    (emoji-picker-container s comment-data))
                                    :showing-picker? ind-showing-picker?
                                    :show-more-menu (::show-more-menu s)
                                    :dismiss-reply-cb (partial finish-edit s comment-data)
                                    :edit-comment-key ind-edit-comment-key}))])
          (when show-add-comment?
            [:div.stream-comment-outer
              {:key (str "stream-comment-add-" (:uuid root-comment-data))
               :class (utils/class-set {:not-highlighted true
                                        :open-thread false
                                        :closing-thread true})}
              [:div.stream-comment
                {:class (utils/class-set {:indented-comment true
                                          :add-comment-container true})}
                (rum/with-key (add-comment {:activity-data activity-data
                                            :parent-comment-uuid (:reply-parent root-comment-data)
                                            :add-comment-cb add-comment-cb
                                            :dismiss-reply-cb (fn [_ _]
                                                               (swap! (::replying-to s) #(disj % (:reply-parent root-comment-data))))})
                 (str "add-comment-" edit-comment-key))]])])])]))
