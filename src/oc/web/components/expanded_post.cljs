(ns oc.web.components.expanded-post
  (:require [rum.core :as rum]
            [dommy.core :as dom]
            [dommy.core :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.shared.useragent :as ua]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.utils.activity :as au]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.mixins.mention :as mention-mixins]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.components.ui.wrt :refer (wrt-count)]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.image-modal :as image-modal]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.components.ui.poll :refer (polls-wrapper)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.stream-comments :refer (stream-comments)]
            [oc.web.components.ui.post-authorship :refer (post-authorship)]
            [oc.web.components.ui.comments-summary :refer (comments-summary)]
            [oc.web.components.ui.stream-attachments :refer (stream-attachments)]))

(defn close-expanded-post [e]
  (nav-actions/dismiss-post-modal e))

(defn save-fixed-comment-height! [s]
  (let [cur-height (.outerHeight (js/$ (rum/ref-node s :expanded-post-fixed-add-comment)))]
    (when-not (= @(::comment-height s) cur-height)
      (reset! (::comment-height s) cur-height))))

(defn win-width []
  (or (.-clientWidth (.-documentElement js/document))
      (.-innerWidth js/window)))

(defn set-mobile-video-height! [s]
  (when (responsive/is-tablet-or-mobile?)
    (reset! (::mobile-video-height s) (utils/calc-video-height (win-width)))))

(defn- load-comments [s force?]
  (let [activity-data @(drv/get-ref s :activity-data)]
    (if force?
      (comment-actions/get-comments activity-data)
      (comment-actions/get-comments-if-needed activity-data @(drv/get-ref s :comments-data)))))

(defn- save-initial-read-data [s]
  (let [activity-data @(drv/get-ref s :activity-data)]
    (when (and (nil? @(::initial-last-read-at s))
               (or (:last-read-at activity-data)
                   (:unread activity-data)))
      (reset! (::initial-last-read-at s) (or (:last-read-at activity-data) "")))))

(defn- mark-read [s]
  (when @(::mark-as-read? s)
    (activity-actions/mark-read @(::activity-uuid s))))

(def big-web-collapse-min-height 134)
(def mobile-collapse-min-height 160)
(def min-body-length-for-truncation 150)

(defn- check-collapse-post [s]
  (when (nil? @(::collapse-post s))
    (let [is-mobile? (responsive/is-mobile-size?)
          comparing-height (if is-mobile? mobile-collapse-min-height big-web-collapse-min-height)
          activity-data @(drv/get-ref s :activity-data)
          comments-count (-> activity-data :links (utils/link-for "comments") :count)]
      (reset! (::collapse-post s) (and ;; Truncate posts with a minimum of body length
                                       (> (count (:body activity-data)) min-body-length-for-truncation)
                                       ;; Never if they have polls
                                       (not (seq (:polls activity-data)))
                                       ;; Only for users we can know if they read it or not
                                       (jwt/user-is-part-of-the-team (:team-id (dis/org-data)))
                                       ;; Only when they are read
                                       (not (:unread activity-data))
                                       ;; And only when there is at least a comment
                                       (pos? comments-count))))))

(rum/defcs expanded-post <
  rum/reactive
  (drv/drv :route)
  (drv/drv :activity-data)
  (drv/drv :comments-data)
  (drv/drv :add-comment-focus)
  (drv/drv :activities-read)
  (drv/drv :expand-image-src)
  (drv/drv :add-comment-force-update)
  (drv/drv :editable-boards)
  (drv/drv :users-info-hover)
  (drv/drv :current-user-data)
  (drv/drv :follow-publishers-list)
  (drv/drv :followers-publishers-count)
  ;; Locals
  (rum/local nil ::wh)
  (rum/local nil ::comment-height)
  (rum/local 0 ::mobile-video-height)
  (rum/local nil ::initial-last-read-at)
  (rum/local nil ::activity-uuid)
  (rum/local false ::force-show-menu)
  (rum/local true ::mark-as-read?)
  (rum/local nil ::collapse-post)
  ;; Mixins
  (mention-mixins/oc-mentions-hover {:click? true})
  (mixins/interactive-images-mixin "div.expanded-post-body")
  {:will-mount (fn [s]
    (check-collapse-post s)
    (save-initial-read-data s)
    s)
   :did-mount (fn [s]
    (save-fixed-comment-height! s)
    (reset! (::activity-uuid s) (:uuid @(drv/get-ref s :activity-data)))
    (load-comments s true)
    (mark-read s)
    s)
   :did-remount (fn [_ s]
    (save-initial-read-data s)
    (load-comments s false)
    s)
   :will-unmount (fn [s]
    (mark-read s)
    (reset! (::activity-uuid s) nil)
    (comment-actions/add-comment-blur)
    s)}
  [s]
  (let [activity-data (drv/react s :activity-data)
        comments-drv (drv/react s :comments-data)
        _add-comment-focus (drv/react s :add-comment-focus)
        _users-info-hover (drv/react s :users-info-hover)
        _follow-publishers-list (drv/react s :follow-publishers-list)
        _followers-publishers-count (drv/react s :followers-publishers-count)
        activities-read (drv/react s :activities-read)
        reads-data (get activities-read (:uuid activity-data))
        editable-boards (drv/react s :editable-boards)
        comments-data (au/get-comments activity-data comments-drv)
        dom-element-id (str "expanded-post-" (:uuid activity-data))
        dom-node-class (str "expanded-post-" (:uuid activity-data))
        publisher (:publisher activity-data)
        is-mobile? (responsive/is-mobile-size?)
        route (drv/react s :route)
        org-data (dis/org-data)
        has-video (seq (:fixed-video-id activity-data))
        uploading-video (dis/uploading-video-data (:video-id activity-data))
        current-user-data (drv/react s :current-user-data)
        current-user-id (:user-id current-user-data)
        is-publisher? (= (:user-id publisher) current-user-id)
        video-player-show (and is-publisher? uploading-video)
        video-size (when has-video
                     (if is-mobile?
                       {:width (win-width)
                        :height @(::mobile-video-height s)}
                       {:width 638
                        :height (utils/calc-video-height 638)}))
        user-is-part-of-the-team (jwt/user-is-part-of-the-team (:team-id org-data))
        expand-image-src (drv/react s :expand-image-src)
        assigned-follow-up-data (first (filter #(= (-> % :assignee :user-id) current-user-id) (:follow-ups activity-data)))
        add-comment-force-update* (drv/react s :add-comment-force-update)
        add-comment-force-update (get add-comment-force-update* (dis/add-comment-string-key (:uuid activity-data)))
        mobile-more-menu-el (sel1 [:div.mobile-more-menu])
        show-mobile-menu? (and is-mobile?
                                      mobile-more-menu-el)
        more-menu-comp (fn []
                        (more-menu {:entity-data activity-data
                                    :share-container-id dom-element-id
                                    :editable-boards editable-boards
                                    :external-share (not is-mobile?)
                                    :external-bookmark (not is-mobile?)
                                    :show-edit? is-publisher?
                                    :show-delete? is-publisher?
                                    :show-move? (and (not is-mobile?)
                                                     is-publisher?)
                                    :tooltip-position "top"
                                    :force-show-menu (and is-mobile? @(::force-show-menu s))
                                    :mobile-tray-menu show-mobile-menu?
                                    :will-close (when show-mobile-menu?
                                                  (fn [] (reset! (::force-show-menu s) false)))}))
        muted-post? (seq (utils/link-for (:links activity-data) "follow"))
        comments-link (utils/link-for (:links activity-data) "comments")]
    [:div.expanded-post
      {:class (utils/class-set {dom-node-class true
                                :android ua/android?})
       :id dom-element-id
       :style {:padding-bottom (str @(::comment-height s) "px")}
       :data-new-at (:new-at activity-data)
       :data-initial-last-read-at @(::initial-last-read-at s)
       :data-last-read-at (:last-read-at activity-data)
       :data-new-comments-count (:new-comments-count activity-data)}
      (image-modal/image-modal {:src expand-image-src})
      [:div.expanded-post-container
        [:div.activity-share-container]
        [:div.expanded-post-header.group
          [:button.mlb-reset.back-to-board
            {:on-click close-expanded-post
             :data-toggle (when-not is-mobile? "tooltip")
             :data-placement "top"
             :title "Close"}]
          [:div.expanded-post-header-center
            (post-authorship {:activity-data activity-data
                              :user-avatar? true
                              :user-hover? true
                              :board-hover? true
                              :current-user-id current-user-id})]
          (if show-mobile-menu?
            (rum/portal (more-menu-comp) mobile-more-menu-el)
            (more-menu-comp))
          [:button.mlb-reset.mobile-more-bt
            {:on-click #(swap! (::force-show-menu s) not)}]]
        (if-not activity-data
          (small-loading)
          [:div.expanded-post-container-inner
            [:div.expanded-post-headline
              {:class utils/hide-class}
              (:headline activity-data)]
            [:div.expanded-post-author.group
              [:div.expanded-post-author-inner
                {:class utils/hide-class}
                [:div.expanded-post-author-inner-label
                  [:time
                    {:date-time (:published-at activity-data)
                     :data-toggle (when-not is-mobile? "tooltip")
                     :data-placement "top"
                     :data-container "body"
                     :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                     :data-title (utils/activity-date-tooltip activity-data)}
                    (utils/foc-date-time (:published-at activity-data))]]
                (when muted-post?
                  [:div.expanded-post-author-dot.muted-dot])
                (when muted-post?
                  [:div.muted-activity
                    {:data-toggle (when-not is-mobile? "tooltip")
                     :data-placement "top"
                     :title "Muted"}])
                (when (or (:must-see activity-data)
                          (:bookmarked-at activity-data))
                  [:div.expanded-post-author-dot])
                (cond
                  (:bookmarked-at activity-data)
                  [:div.bookmark-tag]
                  (:must-see activity-data)
                  [:div.must-see-tag])]]
            (when (seq (:abstract activity-data))
              [:div.expanded-post-abstract.oc-mentions.oc-mentions-hover
                {:class utils/hide-class
                 :dangerouslySetInnerHTML {:__html (:abstract activity-data)}}])
            [:div.expanded-post-body.oc-mentions.oc-mentions-hover
              {:ref "post-body"
               :on-click (when @(::collapse-post s)
                           #(reset! (::collapse-post s) false))
               :class (utils/class-set {utils/hide-class true
                                        :collapsed @(::collapse-post s)})
               :dangerouslySetInnerHTML {:__html (:body activity-data)}}]
            (when @(::collapse-post s)
              [:button.mlb-reset.expand-button
                {:on-click #(reset! (::collapse-post s) false)}
                [:div.expand-button-inner
                  "View more"]])
            (when (seq (:polls activity-data))
              (polls-wrapper {:polls-data (:polls activity-data)
                              :editing? false
                              :current-user-id current-user-id
                              :container-selector "div.expanded-post"
                              :activity-data activity-data
                              :dispatch-key (dis/activity-key (:slug org-data) (:uuid activity-data))}))
            (stream-attachments (:attachments activity-data))
            ; (when is-mobile?
            ;   [:div.expanded-post-mobile-reactions
            ;     (reactions {:entity-data activity-data})])
            [:div.expanded-post-footer.group
              (reactions {:entity-data activity-data})
              [:div.expanded-post-footer-mobile-group
                (when (pos? (:count comments-link))
                  (comments-summary {:entry-data activity-data
                                     :comments-data comments-drv
                                     :new-comments-count 0
                                     :hide-face-pile? true
                                     :publisher? is-publisher?}))]
              (when is-publisher?
                (wrt-count {:activity-data activity-data
                            :reads-data reads-data}))]
            [:div.expanded-post-comments.group
              (when (:can-comment activity-data)
                (rum/with-key (add-comment {:activity-data activity-data
                                            :scroll-after-posting? true
                                            :add-comment-focus-prefix "main-comment"})
                 (str "expanded-post-add-comment-" (:uuid activity-data) "-" add-comment-force-update)))
              (stream-comments {:activity-data activity-data
                                :comments-data comments-data
                                :member? user-is-part-of-the-team
                                :last-read-at @(::initial-last-read-at s)
                                :current-user-id current-user-id})]])]]))
