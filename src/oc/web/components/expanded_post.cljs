(ns oc.web.components.expanded-post
  (:require [rum.core :as rum]
            [dommy.core :as dom]
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
            [oc.web.components.ui.ziggeo :refer (ziggeo-player)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.stream-comments :refer (stream-comments)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
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

(defn- save-initial-last-read-at [s]
  (when-not @(::initial-last-read-at s)
    (let [activity-data @(drv/get-ref s :activity-data)
          reads-data (get @(drv/get-ref s :activities-read) (:uuid activity-data))]
      (when (:last-read-at reads-data)
        (reset! (::initial-last-read-at s) (:last-read-at reads-data))))))

(rum/defcs expanded-post <
  rum/reactive
  (drv/drv :route)
  (drv/drv :activity-data)
  (drv/drv :comments-data)
  (drv/drv :hide-left-navbar)
  (drv/drv :add-comment-focus)
  (drv/drv :activities-read)
  (drv/drv :add-comment-highlight)
  (drv/drv :expand-image-src)
  (drv/drv :add-comment-force-update)
  (drv/drv :editable-boards)
  ;; Locals
  (rum/local nil ::wh)
  (rum/local nil ::comment-height)
  (rum/local 0 ::mobile-video-height)
  (rum/local nil ::initial-last-read-at)
  (rum/local nil ::activity-uuid)
  ;; Mixins
  (mention-mixins/oc-mentions-hover)
  (mixins/interactive-images-mixin "div.expanded-post-body")
  {:will-mount (fn [s]
    (save-initial-last-read-at s)
    s)
   :did-mount (fn [s]
    (save-fixed-comment-height! s)
    (let [activity-uuid (:uuid @(drv/get-ref s :activity-data))]
      (activity-actions/send-item-read activity-uuid)
      (reset! (::activity-uuid s) activity-uuid))
    (load-comments s true)
    s)
   :did-remount (fn [_ s]
    (load-comments s false)
    (save-initial-last-read-at s)
    s)
   :will-unmount (fn [s]
    (activity-actions/send-item-read @(::activity-uuid s))
    s)}
  [s]
  (let [activity-data (drv/react s :activity-data)
        comments-drv (drv/react s :comments-data)
        _add-comment-focus (drv/react s :add-comment-focus)
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
        current-user-id (jwt/user-id)
        is-publisher? (= (:user-id publisher) current-user-id)
        video-player-show (and is-publisher? uploading-video)
        video-size (when has-video
                     (if is-mobile?
                       {:width (win-width)
                        :height @(::mobile-video-height s)}
                       {:width 638
                        :height (utils/calc-video-height 638)}))
        user-is-part-of-the-team (jwt/user-is-part-of-the-team (:team-id org-data))
        add-comment-highlight (drv/react s :add-comment-highlight)
        expand-image-src (drv/react s :expand-image-src)
        assigned-follow-up-data (first (filter #(= (-> % :assignee :user-id) current-user-id) (:follow-ups activity-data)))
        add-comment-force-update (drv/react s :add-comment-force-update)
        has-new-comments? ;; if the post has a last comment timestamp (a comment not from current user)
                          (and (:new-at activity-data)
                               ;; and that's after the user last read
                               (< (.getTime (utils/js-date (:last-read-at reads-data)))
                                  (.getTime (utils/js-date (:new-at activity-data)))))]
    [:div.expanded-post
      {:class (utils/class-set {dom-node-class true
                                :android ua/android?})
       :id dom-element-id
       :style {:padding-bottom (str @(::comment-height s) "px")}}
      (image-modal/image-modal {:src expand-image-src})
      [:div.expanded-post-header.group
        [:button.mlb-reset.back-to-board
          {:on-click close-expanded-post}]
       [:div.expanded-post-header-center.group
         (user-avatar-image (:publisher activity-data))
         [:span.header-title
           (:headline activity-data)]
         (if (and assigned-follow-up-data
                  (not (:completed? assigned-follow-up-data)))
            [:div.follow-up-tag]
            (when (:must-see activity-data)
              [:div.must-see-tag]))]
       [:div.activity-share-container]
       (more-menu {:entity-data activity-data
                   :share-container-id dom-element-id
                   :editable-boards editable-boards
                   :external-share (not is-mobile?)
                   :external-follow-up true
                   :show-edit? true
                   :show-delete? true
                   :show-move? (not is-mobile?)
                   :tooltip-position "bottom"
                   :assigned-follow-up-data assigned-follow-up-data
                   :complete-follow-up-title "Complete follow-up"})]
      (when has-video
        [:div.group
          {:key (str "ziggeo-player-" (:fixed-video-id activity-data))
           :ref :ziggeo-player}
          (ziggeo-player {:video-id (:fixed-video-id activity-data)
                          :width (:width video-size)
                          :height (:height video-size)
                          :lazy (not video-player-show)
                          :video-image (:video-image activity-data)
                          :video-processed (:video-processed activity-data)})])
      [:div.expanded-post-headline
        {:class utils/hide-class}
        (:headline activity-data)]
      [:div.expanded-post-author.group
        (user-avatar-image publisher)
        [:div.expanded-post-author-inner
          {:data-toggle (when-not is-mobile? "tooltip")
           :data-placement "top"
           :data-container "body"
           :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
           :data-title (utils/activity-date-tooltip activity-data)
           :class utils/hide-class}
          [:span.expanded-post-author-inner-label
            (str (:name publisher) " in "
                 (:board-name activity-data)
                 (when (= (:board-access activity-data) "private")
                   " (private)")
                 (when (= (:board-access activity-data) "public")
                   " (public)")
                 " on "
                 (utils/tooltip-date (:published-at activity-data)))]
          (if (and assigned-follow-up-data
                   (not (:completed? assigned-follow-up-data)))
            [:div.follow-up-tag.mobile-only]
            (when (:must-see activity-data)
              [:div.must-see-tag.mobile-only]))]]
      (when (seq (:abstract activity-data))
        [:div.expanded-post-abstract.oc-mentions.oc-mentions-hover
          {:class utils/hide-class
           :dangerouslySetInnerHTML {:__html (:abstract activity-data)}}])
      [:div.expanded-post-body.oc-mentions.oc-mentions-hover
        {:ref "post-body"
         :class utils/hide-class
         :dangerouslySetInnerHTML {:__html (:body activity-data)}}]
      (stream-attachments (:attachments activity-data))
      ; (when is-mobile?
      ;   [:div.expanded-post-mobile-reactions
      ;     (reactions {:entity-data activity-data})])
      [:div.expanded-post-footer.group
        (reactions {:entity-data activity-data})
        [:div.expanded-post-footer-mobile-group
          (comments-summary {:entry-data activity-data
                             :comments-data comments-data
                             :show-new-tag? has-new-comments?})
          (when user-is-part-of-the-team
            [:div.expanded-post-wrt-container
              (wrt-count {:activity-data activity-data
                          :reads-data reads-data})])]]
      [:div.expanded-post-comments.group
        (when (:can-comment activity-data)
          (rum/with-key (add-comment activity-data) (str "expanded-post-add-comment-" (:uuid activity-data) "-" add-comment-force-update)))
        (stream-comments {:activity-data activity-data
                          :comments-data comments-data
                          :new-added-comment add-comment-highlight
                          :last-read-at @(::initial-last-read-at s)
                          :current-user-id current-user-id})]]))
