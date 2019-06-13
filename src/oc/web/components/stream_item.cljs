(ns oc.web.components.stream-item
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [clojure.contrib.humanize :refer (filesize)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.utils.activity :as au]
            [oc.web.mixins.activity :as am]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.utils.org :as org-utils]
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
            [oc.web.components.ui.comments-summary :refer (comments-summary)]))

(defn- check-item-ready
  "After component is mounted/re-mounted "
  [s]
  (let [activity-data (first (:rum/args s))
        $item-body (js/$ (rum/ref-node s "activity-body"))
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
                         (drv/drv :activity-share-container)
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
                             is-published?)
        ;; Add NEW tag besides comment summary
        has-new-comments? ;; if the post has a last comment timestamp (a comment not from current user)
                          (and (:new-at activity-data)
                               ;; and that's after the user last read
                               (< (.getTime (utils/js-date (:last-read-at read-data)))
                                  (.getTime (utils/js-date (:new-at activity-data)))))]
    [:div.stream-item
      {:class (utils/class-set {dom-node-class true
                                :draft (not is-published?)
                                :must-see-item (:must-see activity-data)
                                :unseen-item (:unseen activity-data)
                                :unread-item (:unread activity-data)
                                :expandable is-published?
                                :showing-share (= (drv/react s :activity-share-container) dom-element-id)})
       ;; click on the whole tile only for draft editing
       :on-click (fn [e]
                   (if is-drafts-board
                     (activity-actions/activity-edit activity-data)
                     (let [more-menu-el (.get (js/$ (str "#" dom-element-id " div.more-menu")) 0)
                           stream-item-wrt-el (rum/ref-node s :stream-item-wrt)
                           emoji-picker (.get (js/$ (str "#" dom-element-id " div.emoji-mart")) 0)
                           attachments-el (rum/ref-node s :stream-item-attachments)]
                       (when (and ;; More menu wasn't clicked
                                  (not (utils/event-inside? e more-menu-el))
                                  ;; WRT wasn't clicked 
                                  (not (utils/event-inside? e stream-item-wrt-el))
                                  ;; Attachments wasn't clicked
                                  (not (utils/event-inside? e attachments-el))
                                  ;; Emoji picker wasn't clicked
                                  (not (utils/event-inside? e emoji-picker))
                                  ;; a button wasn't clicked
                                  (not (utils/button-clicked? e))
                                  ;; No input field clicked
                                  (not (utils/input-clicked? e))
                                  ;; No anchor clicked
                                  (not (utils/anchor-clicked? e)))
                         (routing-actions/open-post-modal activity-data)
                         (utils/scroll-to-y 0)))))
       :id dom-element-id}
      [:div.stream-item-inner
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
              [:div.must-see-tag.big-web-tablet-only]]]
          [:div.activity-share-container]
          (when is-published?
            (more-menu activity-data dom-element-id
             {:external-share (not is-mobile?)
              :show-edit? true
              :show-delete? true
              :show-move? (not is-mobile?)
              :show-unread (not (:unread activity-data))}))]
        [:div.must-see-tag.mobile-only]
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
                                                       (:body activity-data))}}])]]
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
                  (comments-summary activity-data true has-new-comments?)]
                (reactions activity-data)
                (when should-show-wrt
                  [:div.stream-item-wrt
                    {:ref :stream-item-wrt}
                    (wrt-count activity-data read-data)])
                (when (seq activity-attachments)
                  [:div.stream-item-attachments
                    {:ref :stream-item-attachments}
                    [:div.stream-item-attachments-count
                      (count activity-attachments) " Attachment" (when (> (count activity-attachments) 1) "s")]
                    [:div.stream-item-attachments-list
                      (for [atc activity-attachments]
                        [:a.stream-item-attachments-item
                          {:href (:file-url atc)
                           :target "_blank"}
                          [:div.stream-item-attachments-item-desc
                            [:span.file-name
                              (:file-name atc)]
                            [:span.file-size
                              (str "(" (filesize (:file-size atc) :binary false :format "%.2f") ")")]]])]])
                [:div.time-since
                  (let [t (or (:published-at activity-data) (:created-at activity-data))]
                    [:time
                      {:date-time t
                       :data-toggle (when-not is-mobile? "tooltip")
                       :data-placement "top"
                       :data-container "body"
                       :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                       :data-title (str "Posted on " (utils/tooltip-date (:published-at activity-data)))}
                      (utils/foc-date-time t)])]])]]]))