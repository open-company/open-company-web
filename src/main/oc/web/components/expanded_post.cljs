(ns oc.web.components.expanded-post
  (:require [rum.core :as rum]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [dommy.core :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.lib.cljs.useragent :as ua]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.utils.activity :as au]
            [oc.web.utils.comment :as cu]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.mixins.mention :as mention-mixins]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.components.ui.wrt :refer (wrt-count)]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.actions.foc-menu :as foc-menu-actions]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.image-modal :as image-modal]
            [oc.web.components.ui.labels :refer (labels-list)]
            [oc.web.components.ui.poll :refer (polls-wrapper)]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.stream-comments :refer (stream-comments)]
            [oc.web.components.ui.post-authorship :refer (post-authorship)]
            [oc.web.components.ui.comments-summary :refer (foc-comments-summary)]
            [oc.web.components.ui.stream-attachments :refer (stream-attachments)]))

(defn close-expanded-post [e]
  (nav-actions/dismiss-post-modal e))

(defn save-fixed-comment-height! [s]
  (let [cur-height (.outerHeight (js/$ (rum/ref-node s :expanded-post-fixed-add-comment)))]
    (when-not (= @(::comment-height s) cur-height)
      (reset! (::comment-height s) cur-height))))

(defn- load-comments [s force?]
  (let [activity-data @(drv/get-ref s :activity-data)]
    (if force?
      (au/get-comments activity-data)
      (au/get-comments-if-needed activity-data @(drv/get-ref s :comments-data)))))

(defn- save-initial-read-data [s]
  (let [activity-data @(drv/get-ref s :activity-data)]
    (when (and (nil? @(::initial-last-read-at s))
               (or (:last-read-at activity-data)
                   (:unread activity-data)))
      (reset! (::initial-last-read-at s) (or (:last-read-at activity-data) "")))))

(defn- mark-read [s]
  (when @(::mark-as-read? s)
    (activity-actions/mark-read @(::activity-uuid s))))

(def add-comment-prefix "main-comment")

(defn- can-dismiss? [s]
  (and (not (:foc-show-menu @(drv/get-ref s :foc-menu)))
       (not (seq @(drv/get-ref s :panel-stack)))
       (not (seq @(drv/get-ref s :expand-image-src)))))

(rum/defcs expanded-post <
  rum/reactive
  (drv/drv :route)
  (drv/drv :org-data)
  (drv/drv :panel-stack)
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
  (drv/drv :foc-menu)
  ;; Locals
  (rum/local nil ::wh)
  (rum/local nil ::comment-height)
  (rum/local nil ::initial-last-read-at)
  (rum/local nil ::activity-uuid)
  (rum/local true ::mark-as-read?)
  (rum/local nil ::collapse-post)
  (rum/local nil ::esc-listener)
  ;; Mixins
  (mention-mixins/oc-mentions-hover {:click? true})
  (mixins/interactive-images-mixin "div.expanded-post-body")
  mixins/no-scroll-mixin
  (mixins/on-key-press ["Escape"]
                       (fn [s e]
                         (when (can-dismiss? s)
                           (close-expanded-post e))))
  (mixins/on-window-click-mixin (fn [s e]
                                  (when (and (can-dismiss? s)
                                             (not (dom-utils/event-container-matches e (str ".expanded-post-container, "
                                                                                            ".exp-click-stop, "
                                                                                            ".modal-wrapper, "
                                                                                            ".label-modal-view, "
                                                                                            ".labels-picker, "
                                                                                            ".oc-labels-modal-wrapper, "
                                                                                            ".emoji-autocomplete-menu, "
                                                                                            ".more-menu"))))
                                    (close-expanded-post e))))
  {:will-mount (fn [s]
                 (reset! (::collapse-post s) (-> s (drv/get-ref :activity-data) deref :collapse-body?))
                 (save-initial-read-data s)
                 s)
   :did-mount (fn [s]
                (save-fixed-comment-height! s)
                (reset! (::activity-uuid s) (-> s (drv/get-ref :activity-data) deref :uuid))
                (load-comments s true)
                (mark-read s)
                s)
   :did-remount (fn [_ s]
                  (save-initial-read-data s)
                  (load-comments s false)
                  s)
   :will-unmount (fn [s]
                   (mark-read s)
                   (comment-actions/add-comment-blur (cu/add-comment-focus-value add-comment-prefix @(::activity-uuid s)))
                   (reset! (::activity-uuid s) nil)
                   (when @(::esc-listener s)
                     (events/unlistenByKey @(::esc-listener s))
                     (reset! (::esc-listener s) nil))
                   s)}
  [s]
  (let [activity-data (drv/react s :activity-data)
        comments-drv (drv/react s :comments-data)
        _panel-stack (drv/react s :panel-stack)
        _add-comment-focus (drv/react s :add-comment-focus)
        _users-info-hover (drv/react s :users-info-hover)
        _follow-publishers-list (drv/react s :follow-publishers-list)
        _followers-publishers-count (drv/react s :followers-publishers-count)
        activities-read (drv/react s :activities-read)
        activity-uuid (:uuid activity-data)
        read-data (get activities-read activity-uuid)
        editable-boards (drv/react s :editable-boards)
        comments-data (au/activity-comments activity-data comments-drv)
        is-mobile? (responsive/is-mobile-size?)
        _route (drv/react s :route)
        org-data (drv/react s :org-data)
        current-user-data (drv/react s :current-user-data)
        current-user-id (:user-id current-user-data)
        expand-image-src (drv/react s :expand-image-src)
        add-comment-force-update* (drv/react s :add-comment-force-update)
        add-comment-force-update (get add-comment-force-update* (dis/add-comment-string-key activity-uuid))
        mobile-more-menu-el (sel1 [:div.mobile-more-menu])
        foc-menu (drv/react s :foc-menu)
        foc-show-menu (= (:foc-show-menu foc-menu) activity-uuid)
        foc-menu-open (= (:foc-menu-open foc-menu) activity-uuid)
        foc-activity-move (= (:foc-activity-move foc-menu) activity-uuid)
        foc-labels-picker (= (:foc-labels-picker foc-menu) activity-uuid)
        foc-share-entry (= (:foc-share-entry foc-menu) activity-uuid)
        show-mobile-menu? (and is-mobile?
                               mobile-more-menu-el
                               (or foc-menu-open
                                   foc-share-entry
                                   foc-labels-picker
                                   foc-activity-move))
        more-menu-comp (fn []
                        (more-menu {:entity-data activity-data
                                    :editable-boards editable-boards
                                    :external-share (not is-mobile?)
                                    :external-bookmark (not is-mobile?)
                                    :external-follow (not is-mobile?)
                                    :show-home-pin true
                                    :show-board-pin true
                                    :show-edit? true
                                    :show-delete? true
                                    :show-move? (not is-mobile?)
                                    :tooltip-position "top"
                                    :mobile-tray-menu show-mobile-menu?
                                    :current-user-data current-user-data
                                    :external-labels true
                                    :foc-show-menu foc-show-menu
                                    :foc-menu-open foc-menu-open
                                    :foc-activity-move foc-activity-move
                                    :foc-labels-picker foc-labels-picker
                                    :foc-share-entry foc-share-entry
                                    :custom-class "exp-click-stop"}))
        muted-post? (map? (utils/link-for (:links activity-data) "follow"))
        comments-link (utils/link-for (:links activity-data) "comments")]
    [:div.expanded-post
      {:class (utils/class-set {:bookmark-item (:bookmarked-at activity-data)
                                :muted-item muted-post?})
       :style {:padding-bottom (str @(::comment-height s) "px")}}
      (image-modal/image-modal {:src expand-image-src})
      [:div.expanded-post-container.exp-click-stop
        {:ref :expanded-post-container}
        [:div.expanded-post-header.group
          [:div.back-to-board-container
            [:button.mlb-reset.back-to-board
              {:on-click close-expanded-post
               :data-toggle (when-not is-mobile? "tooltip")
               :data-placement "top"
               :title "Close or press Esc"}]]
          [:div.expanded-post-header-center
            (post-authorship {:activity-data activity-data
                              :user-avatar? true
                              :activity-board? true
                              :user-hover? true
                              :board-hover? true
                              :current-user-id current-user-id})
            [:time
              {:date-time (:published-at activity-data)
               :data-toggle (when-not is-mobile? "tooltip")
               :data-placement "top"
               :data-container "body"
               :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
               :title (utils/activity-date-tooltip activity-data)}
              (utils/foc-date-time (:published-at activity-data))]
            [:div.bookmark-tag.big-web-tablet-only]
            [:div.bookmark-tag-small.mobile-only]
            [:div.muted-activity
             {:data-toggle (when-not is-mobile? "tooltip")
              :data-placement "top"
              :title "Muted"}]]
          [:div.more-menu-container
            (if show-mobile-menu?
              (rum/portal (more-menu-comp) mobile-more-menu-el)
              (more-menu-comp))
            [:button.mlb-reset.mobile-more-bt.foc-menu-event-stop
              {:on-click #(foc-menu-actions/toggle-foc-menu-open (:uuid activity-data))}]]]
        (if-not activity-data
          (small-loading)
          [:div.expanded-post-container-inner
            [:div.expanded-post-headline
              {:class utils/hide-class}
              (:headline activity-data)]
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
            [:div.expanded-post-footer.group
              (reactions {:entity-data activity-data
                          :thumb-first? true
                          :hide-picker true})
              (when (:member? org-data)
                (foc-comments-summary {:entry-data activity-data
                                       :add-comment-focus-prefix "main-comment"
                                       :current-activity-id (:uuid activity-data)}))
              (when (:member? org-data)
                (wrt-count {:activity-data activity-data
                            :read-data read-data}))
              (when (seq (:labels activity-data))
                [:div.expanded-post-labels
                  [:div.separator-dot]
                  (labels-list {:labels (:labels activity-data)
                                :tooltip? (not is-mobile?)})])]
            [:div.expanded-post-comments.group
              {:class (when ua/android? "android")}
              (stream-comments {:activity-data activity-data
                                :comments-data comments-data
                                :loading-comments-count (when-not (seq (get-in comments-drv [(:uuid activity-data) :sorted-comments]))
                                                          (- (:count comments-link) (count comments-data)))
                                :member? (:member? org-data)
                                :last-read-at @(::initial-last-read-at s)
                                :reply-add-comment-prefix add-comment-prefix
                                :current-user-id current-user-id
                                :foc-menu-open (:foc-menu-open foc-menu)
                                :foc-show-menu (:foc-show-menu foc-menu)
                                :foc-share-entry (:foc-share-entry foc-menu)})
              (when (:can-comment activity-data)
                (rum/with-key (add-comment {:activity-data activity-data
                                            :scroll-after-posting? true
                                            :collapse? true
                                            :internal-max-width (if is-mobile? (- (dom-utils/viewport-width) (* (+ 24 1) 2)) 606) ;; On mobile is screen width less the padding and border on both sides
                                            :add-comment-focus-prefix "main-comment"})
                 (str "expanded-post-add-comment-" (:uuid activity-data) "-" add-comment-force-update)))]])]]))
