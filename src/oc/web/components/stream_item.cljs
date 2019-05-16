(ns oc.web.components.stream-item
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :refer-macros (sel1)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.utils.activity :as au]
            [oc.web.mixins.activity :as am]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.utils.user :as user-utils]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.utils.draft :as draft-utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.mixins.mention :as mention-mixins]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.actions.routing :as routing-actions]
            [oc.web.components.ui.wrt :refer (wrt-count)]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.components.ui.ziggeo :refer (ziggeo-player)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.comments-summary :refer (comments-summary)]
            [oc.web.components.ui.stream-attachments :refer (stream-attachments)]))

(defn- check-item-ready
  "After component is mounted/re-mounted "
  [s]
  (let [activity-data (first (:rum/args s))
        $item-body (js/$ (rum/ref-node s :abstract))
        comments-data (au/get-comments activity-data @(drv/get-ref s :comments-data))]
    (when (or (.hasClass $item-body "ddd-truncated")
              (pos? (count (:attachments activity-data)))
              (pos? (count comments-data))
              (:body-thumbnail activity-data)
              (:fixed-video-id activity-data))
      (reset! (::truncated s) true))
    (reset! (::item-ready s) true)))

(defn win-width []
  (or (.-clientWidth (.-documentElement js/document))
      (.-innerWidth js/window)))

(defn calc-video-height [s]
  (when (responsive/is-tablet-or-mobile?)
    (reset! (::mobile-video-height s) (utils/calc-video-height (win-width)))))

(defn- item-mounted [s]
  (let [activity-data (first (:rum/args s))
        comments-data @(drv/get-ref s :comments-data)]
    (comment-actions/get-comments-if-needed activity-data comments-data)))

(rum/defcs stream-item < rum/reactive
                         ;; Derivatives
                         (drv/drv :org-data)
                         (drv/drv :comments-data)
                         (drv/drv :show-post-added-tooltip)
                         ;; Locals
                         (rum/local false ::truncated)
                         (rum/local false ::item-ready)
                         (rum/local 0 ::mobile-video-height)
                         ;; Mixins
                         (ui-mixins/render-on-resize calc-video-height)
                         (am/truncate-element-mixin :abstract (* 24 3))
                         (mention-mixins/oc-mentions-hover)
                         {:will-mount (fn [s]
                           (calc-video-height s)
                           s)
                          :did-mount (fn [s]
                           (item-mounted s)
                           (check-item-ready s)
                           s)
                          :did-remount (fn [_ s]
                           (item-mounted s)
                           (check-item-ready s)
                           s)}
  [s activity-data read-data]
  (let [org-data (drv/react s :org-data)
        is-mobile? (responsive/is-tablet-or-mobile?)
        truncated? @(::truncated s)
        ;; Fallback to the activity inline comments if we didn't load
        ;; the full comments just yet
        _ (drv/react s :comments-data)
        activity-attachments (:attachments activity-data)
        is-drafts-board (= (router/current-board-slug) utils/default-drafts-board-slug)
        dom-element-id (str "stream-item-" (:uuid activity-data))
        is-published? (au/is-published? activity-data)
        publisher (if is-published?
                    (:publisher activity-data)
                    (first (:author activity-data)))
        is-publisher? (= (:user-id publisher) (jwt/user-id))
        dom-node-class (str "stream-item-" (:uuid activity-data))
        has-video (seq (:fixed-video-id activity-data))
        uploading-video (dis/uploading-video-data (:video-id activity-data))
        video-player-show (and is-publisher? uploading-video)
        video-size (when has-video
                     (if is-mobile?
                       {:width (win-width)
                        :height @(::mobile-video-height s)}
                       {:width 136
                        :height (utils/calc-video-height 136)}))
        user-is-part-of-the-team (jwt/user-is-part-of-the-team (:team-id org-data))
        should-show-wrt (and user-is-part-of-the-team
                             is-published?)]
    [:div.stream-item
      {:class (utils/class-set {dom-node-class true
                                :draft (not is-published?)
                                :must-see-item (:must-see activity-data)
                                :unseen-item (:unseen activity-data)
                                :unread-item (:unread activity-data)
                                :expandable is-published?})
       ;; click on the whole tile only for draft editing
       :on-click (fn [e]
                   (when-not is-mobile?
                     (if is-drafts-board
                       (activity-actions/activity-edit activity-data)
                       (let [more-menu-el (.get (js/$ (str "#" dom-element-id " div.more-menu")) 0)
                             stream-item-wrt-el (rum/ref-node s :stream-item-wrt)
                             emoji-picker (.get (js/$ (str "#" dom-element-id " div.emoji-mart")) 0)]
                         (when (and ;; More menu wasn't clicked
                                    (not (utils/event-inside? e more-menu-el))
                                    ;; WRT wasn't clicked 
                                    (not (utils/event-inside? e stream-item-wrt-el))
                                    ;; Emoji picker wasn't clicked
                                    (not (utils/event-inside? e emoji-picker))
                                    ;; a button wasn't clicked
                                    (not (utils/button-clicked? e))
                                    ;; No input field clicked
                                    (not (utils/input-clicked? e)))
                           (routing-actions/open-post-modal activity-data))))))
       :id dom-element-id}
      [:div.stream-item-header.group
        [:div.stream-header-head-author
          (user-avatar-image publisher)
          [:div.name
            [:div.name-inner
              {:class utils/hide-class}
              (str
               (:name publisher)
               " in "
               (:board-name activity-data))]
            [:div.must-see-tag.big-web-tablet-only "Must see"]]
          [:div.time-since
            (let [t (or (:published-at activity-data) (:created-at activity-data))]
              [:time
                {:date-time t
                 :data-toggle (when-not is-mobile? "tooltip")
                 :data-placement "top"
                 :data-container "body"
                 :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                 :data-title (str "Posted on " (utils/tooltip-date (:published-at activity-data)))}
                (utils/foc-date-time t)])]]
        (when (and is-published?
                   is-mobile?)
          (more-menu activity-data dom-element-id
           {:external-share (not is-mobile?)}))]
      [:div.must-see-tag.mobile-only "Must see"]
      [:div.new-tag.mobile-only "NEW"]
      [:div.stream-item-body-ext.group
        [:div.thumbnail-container.group
          (if has-video
            [:div.group
             {:key (str "ziggeo-player-" (:fixed-video-id activity-data))
              :ref :ziggeo-player}
             (ziggeo-player {:video-id (:fixed-video-id activity-data)
                             :width (:width video-size)
                             :height (:height video-size)
                             :lazy (not video-player-show)
                             :video-image (:video-image activity-data)
                             :video-processed (:video-processed activity-data)
                             :playing-cb #(activity-actions/send-item-read (:uuid activity-data))})]
            (when (:body-thumbnail activity-data)
              [:div.body-thumbnail-wrapper
                {:class (:type (:body-thumbnail activity-data))}
                [:img.body-thumbnail
                  {:data-image (:thumbnail (:body-thumbnail activity-data))
                   :src (:thumbnail (:body-thumbnail activity-data))}]]))
          [:div.stream-body-left.group
            {:class (utils/class-set {:has-thumbnail (:has-thumbnail activity-data)
                                      :has-video (:fixed-video-id activity-data)
                                      utils/hide-class true})}
            [:div.stream-item-headline.ap-seen-item-headline
              {:ref "activity-headline"
               :data-itemuuid (:uuid activity-data)
               :dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]
            (let [has-abstract (seq (:abstract activity-data))]
              [:div.stream-item-body
                {:class (utils/class-set {:no-abstract (not has-abstract)
                                          :item-ready @(::item-ready s)
                                          :truncated truncated?})
                 :data-itemuuid (:uuid activity-data)
                 :ref :abstract
                 :dangerouslySetInnerHTML {:__html (if has-abstract
                                                     (:abstract activity-data)
                                                     (:body activity-data))}}])]
          (when (and ls/oc-enable-transcriptions
                     (:video-transcript activity-data))
            [:div.stream-item-transcript
              [:div.stream-item-transcript-header
                "This transcript was automatically generated and may not be accurate"]
              [:div.stream-item-transcript-content
                (:video-transcript activity-data)]])]
          (stream-attachments activity-attachments)
          (if is-drafts-board
            [:div.stream-item-footer.group
              [:div.stream-body-draft-edit
                [:button.mlb-reset.edit-draft-bt
                  {:on-click #(activity-actions/activity-edit activity-data)}
                  "Continue editing"]]
              [:div.stream-body-draft-delete
                [:button.mlb-reset.delete-draft-bt
                  {:on-click #(draft-utils/delete-draft-clicked activity-data %)}
                  "Delete draft"]]]
            [:div.stream-item-footer.group
              {:ref "stream-item-reactions"}
              [:div.stream-item-comments-summary
                ; {:on-click #(expand s true true)}
                (comments-summary activity-data true)]
              (reactions activity-data)
              (when should-show-wrt
                [:div.stream-item-wrt
                  {:ref :stream-item-wrt}
                  (wrt-count activity-data read-data)
                  (when (and (not is-mobile?)
                             (= (drv/react s :show-post-added-tooltip) (:uuid activity-data)))
                    [:div.post-added-tooltip-container.group
                      [:div.post-added-tooltip-top-arrow]
                      [:button.mlb-reset.post-added-tooltip-dismiss
                        {:on-click #(nux-actions/dismiss-post-added-tooltip)}]
                      [:div.post-added-tooltips
                        [:div.post-added-tooltip
                          (if (user-utils/is-org-creator? org-data)
                            "After you invite your team, you'll know who saw this post."
                            "Here's where you'll know who saw this post.")]
                        [:button.mlb-reset.post-added-bt
                          {:on-click #(nux-actions/dismiss-post-added-tooltip)}
                          "OK, got it"]]])])
              [:div.menu-container
                [:div.activity-share-container]
                (when (and is-published?
                           (not is-mobile?))
                  (more-menu activity-data dom-element-id
                   {:external-share (not is-mobile?)}))]])]]))
