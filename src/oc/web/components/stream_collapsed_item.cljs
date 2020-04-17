(ns oc.web.components.stream-collapsed-item
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.comments-summary :refer (comments-summary)]
            [oc.web.components.ui.user-info-hover :refer (user-info-hover)]))

(defn- prefixed-html
  "Safari is showing the full body in a tooltip as a feature when text-overflow is ellipsis.
   To prevent it we need to add an empty div as first element of the body."
  [html-string]
  (str "<div class=\"hide-tooltip\"></div>" html-string))

(defn- stream-item-summary [activity-data]
  (if (seq (:abstract activity-data))
    [:div.stream-item-body.oc-mentions
      {:data-itemuuid (:uuid activity-data)
       :dangerouslySetInnerHTML {:__html (prefixed-html (:abstract activity-data))}}]
    [:div.stream-item-body.no-abstract.oc-mentions
      {:data-itemuuid (:uuid activity-data)
       :dangerouslySetInnerHTML {:__html (prefixed-html (:body activity-data))}}]))

(rum/defcs stream-collapsed-item < rum/static
                                   rum/reactive
                                   (drv/drv :activity-share-container)
  [s {:keys [activity-data read-data comments-data editable-boards :member? member?]}]
  (let [is-mobile? (responsive/is-mobile-size?)
        is-drafts-board (= (router/current-board-slug) utils/default-drafts-board-slug)
        is-inbox? (= (router/current-board-slug) "inbox")
        dom-element-id (str "stream-collapsed-item-" (:uuid activity-data))
        dom-node-class (str "stream-collapsed-item-" (:uuid activity-data))
        current-user-id (jwt/user-id)
        is-published? (au/is-published? activity-data)
        publisher (if is-published?
                    (:publisher activity-data)
                    (first (:author activity-data)))
        ;; Add NEW tag besides comment summary
        has-new-comments? ;; if the post has a last comment timestamp (a comment not from current user)
                          (and (:new-at activity-data)
                               ;; and that's after the user last read
                               (< (.getTime (utils/js-date (:last-read-at activity-data)))
                                  (.getTime (utils/js-date (:new-at activity-data)))))
        has-zero-comments? (and (-> activity-data :comments count zero?)
                                (-> comments-data (get (:uuid activity-data)) :sorted-comments count zero?))]
    [:div.stream-collapsed-item
      {:class (utils/class-set {dom-node-class true
                                :draft (not is-published?)
                                :unseen-item (:unseen activity-data)
                                :unread-item (or (pos? (:new-comments-count activity-data))
                                                 (:unread activity-data))
                                :expandable is-published?
                                :showing-share (= (drv/react s :activity-share-container) dom-element-id)})
       :data-new-at (:new-at activity-data)
       :data-last-read-at (:last-read-at activity-data)
       ;; click on the whole tile only for draft editing
       :on-click (fn [e]
                   (if is-drafts-board
                     (activity-actions/activity-edit activity-data)
                     (let [more-menu-el (.get (js/$ (str "#" dom-element-id " div.more-menu")) 0)
                           comments-summary-el (.get (js/$ (str "#" dom-element-id " div.is-comments")) 0)]
                       (when (and ;; More menu wasn't clicked
                                  (not (utils/event-inside? e more-menu-el))
                                  ;; Comments summary wasn't clicked
                                  (not (utils/event-inside? e comments-summary-el))
                                  ;; a button wasn't clicked
                                  (not (utils/button-clicked? e))
                                  ;; No input field clicked
                                  (not (utils/input-clicked? e))
                                  ;; No body link was clicked
                                  (not (utils/anchor-clicked? e)))
                         (nav-actions/open-post-modal activity-data false)))))
       :id dom-element-id}
      [:div.stream-collapsed-item-inner
        {:class (utils/class-set {:must-see-item (:must-see activity-data)
                                  :bookmark-item (:bookmarked-at activity-data)
                                  :muted-item (utils/link-for (:links activity-data) "follow")
                                  :no-comments has-zero-comments?})}
        [:div.stream-collapsed-item-avatar-container
          (user-info-hover {:user-data publisher :current-user-id current-user-id})
          [:div.stream-collapsed-item-avatar
            (user-avatar-image publisher)]]
        [:div.stream-collapsed-item-fill
          [:div.stream-item-headline.ap-seen-item-headline
            {:ref "activity-headline"
             :data-itemuuid (:uuid activity-data)
             :dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]
          [:div.stream-collapsed-item-dot.muted-dot]
          [:div.muted-activity]
          [:div.must-see-tag]
          [:div.bookmark-tag-small]
          (stream-item-summary activity-data)]
        (when-not has-zero-comments?
          (comments-summary {:entry-data activity-data
                             :comments-data comments-data
                             :new-comments-count (:new-comments-count activity-data)
                             :hide-label? is-mobile?}))
        [:div.collapsed-time
          (let [t (or (:published-at activity-data) (:created-at activity-data))]
            [:time
              {:date-time t
               :data-toggle (when-not is-mobile? "tooltip")
               :data-placement "top"
               :data-container "body"
               :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
               :data-title (utils/activity-date-tooltip activity-data)}
              (utils/foc-date-time t)])]]
      (more-menu {:entity-data activity-data
                  :share-container-id dom-element-id
                  :editable-boards editable-boards
                  :external-share (not is-mobile?)
                  :external-bookmark (not is-mobile?)
                  :external-follow (not is-mobile?)
                  :show-edit? true
                  :show-delete? true
                  :show-unread (not (:unread activity-data))
                  :show-move? (not is-mobile?)
                  :show-inbox? is-inbox?})]))