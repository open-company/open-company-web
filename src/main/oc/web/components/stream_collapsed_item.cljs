(ns oc.web.components.stream-collapsed-item
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.components.ui.face-pile :refer (face-pile)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.comments-summary :refer (comments-summary)]
            [oc.web.components.ui.info-hover-views :refer (user-info-hover)]))

(defn- prefixed-html
  "Safari is showing the full body in a tooltip as a feature when text-overflow is ellipsis.
   To prevent it we need to add an empty div as first element of the body."
  [html-string]
  (str "<div class=\"hide-tooltip\"></div>" html-string))

(defn- stream-item-summary [activity-data]
  [:div.stream-item-body.oc-mentions
    {:data-itemuuid (:uuid activity-data)
     :dangerouslySetInnerHTML {:__html (prefixed-html (:body activity-data))}}])

(rum/defcs stream-collapsed-item < rum/static
                                   rum/reactive
                                   (drv/drv :activity-share-container)
                                   (drv/drv :board-slug)
                                   (drv/drv :activity-uuid)
  [s {:keys [activity-data read-data comments-data editable-boards current-user-data member?]}]
  (let [is-mobile? (responsive/is-mobile-size?)
        current-board-slug (drv/react s :board-slug)
        current-activity-id (drv/react s :activity-uuid)
        is-drafts-board (= current-board-slug utils/default-drafts-board-slug)
        is-inbox? (= current-board-slug "inbox")
        dom-element-id (str "stream-collapsed-item-" (:uuid activity-data))
        dom-node-class (str "stream-collapsed-item-" (:uuid activity-data))
        is-published? (au/is-published? activity-data)
        publisher (if is-published?
                    (:publisher activity-data)
                    (first (:author activity-data)))
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
       :data-last-activity-at (:last-activity-at activity-data)
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
                                  :new-item (pos? (:unseen-comments activity-data))
                                  :no-comments has-zero-comments?})}
        (if false ;is-mobile?
          [:div.stream-collapsed-item-fillers
           [:div.stream-collapsed-item-fill
            [:div.stream-collapsed-item-avatar
             (face-pile {:width 24 :faces (:authors (:for-you-context activity-data))})]
            [:div.stream-item-context
             (-> activity-data :for-you-context :label)]
            [:div.stream-collapsed-item-dot.muted-dot]
            [:div.new-item-tag]
            [:div.bookmark-tag-small]
            [:div.muted-activity]
            [:div.collapsed-time
             (let [t (:timestamp (:for-you-context activity-data))]
               [:time
                {:date-time t
                 :data-toggle (when-not is-mobile? "tooltip")
                 :data-placement "top"
                 :data-container "body"
                 :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                 :data-title (utils/activity-date-tooltip activity-data)}
                (utils/foc-date-time t)])]]
            [:div.stream-collapsed-item-fill
             [:div.stream-item-arrow]
              [:div.stream-item-headline.ap-seen-item-headline
               {:ref "activity-headline"
                :data-itemuuid (:uuid activity-data)
                :dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]]]
          [:div.stream-collapsed-item-fill
            [:div.stream-collapsed-item-avatar
              (face-pile {:width 24 :faces (:authors (:for-you-context activity-data))})]
            [:div.stream-item-context
            (-> activity-data :for-you-context :label)]
            ;; Needed to wrap mobile on a new line
            [:div.stream-item-break]
            [:div.stream-item-arrow]
            [:div.stream-item-headline.ap-seen-item-headline
              {:ref "activity-headline"
              :data-itemuuid (:uuid activity-data)
              :dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]
            [:div.stream-collapsed-item-dot.muted-dot]
            [:div.new-item-tag]
            [:div.bookmark-tag-small]
            [:div.muted-activity]
            [:div.collapsed-time
              (let [t (:timestamp (:for-you-context activity-data))]
                [:time
                {:date-time t
                  :data-toggle (when-not is-mobile? "tooltip")
                  :data-placement "top"
                  :data-container "body"
                  :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                  :data-title (utils/activity-date-tooltip activity-data)}
                (utils/foc-date-time t)])]])]
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
                  :show-inbox? is-inbox?})
      [:div.activity-share-container]]))