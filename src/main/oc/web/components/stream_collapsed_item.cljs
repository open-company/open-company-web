(ns oc.web.components.stream-collapsed-item
  (:require [rum.core :as rum]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.lib.hateoas :as hateoas]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.utils.activity :as au]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.face-pile :refer (face-pile)]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.mixins.gestures :refer (swipe-gesture-manager)]))

(defn- dismiss-swipe-button [s & [e ref-kw]]
  (when e
    (dom-utils/event-stop! e))
  (when (or (not ref-kw)
            (= ref-kw ::show-mobile-follow-bt))
    (reset! (::show-mobile-follow-bt s) false))
  (reset! (::last-mobile-swipe-menu s) nil)
  (dis/dispatch! [:input [:mobile-swipe-menu] nil]))

(defn- swipe-left-handler [s _]
  (dis/dispatch! [:input [:mobile-swipe-menu] (-> s :rum/args first :activity-data :uuid)])
  (swap! (::show-mobile-follow-bt s) not))

(defn- on-scroll [s]
  (reset! (::show-mobile-follow-bt s) false))

(rum/defcs stream-collapsed-item < rum/static
                                   rum/reactive
                                   (drv/drv :board-slug)
                                   (drv/drv :activity-uuid)
                                   (drv/drv :mobile-swipe-menu)
                                   (rum/local false ::show-mobile-follow-bt)
                                   (rum/local false ::on-scroll)
                                   (rum/local nil ::last-mobile-swipe-menu)
                                   ;; Mixins
                                   (when (responsive/is-mobile-size?)
                                     (swipe-gesture-manager {:swipe-left swipe-left-handler
                                                             :disabled #(let [member? (-> % :rum/args first :member?)
                                                                              ad (-> % :rum/args first :activity-data)]
                                                                          (and member?
                                                                               (not (hateoas/link-for (:links ad) "follow"))
                                                                               (not (hateoas/link-for (:links ad) "unfollow"))))}))
                                   {:will-mount (fn [s]
                                                  (when (responsive/is-mobile-size?)
                                                    (reset! (::on-scroll s)
                                                            (events/listen js/window EventType/SCROLL (partial on-scroll s))))
                                                  s)
                                    :did-update (fn [s]
                                                  (when (responsive/is-mobile-size?)
                                                    (let [mobile-swipe-menu @(drv/get-ref s :mobile-swipe-menu)
                                                          activity-uuid (-> s :rum/args first :activity-data :uuid)]
                                                      (when (not= @(::last-mobile-swipe-menu s) mobile-swipe-menu)
                                                        (reset! (::last-mobile-swipe-menu s) mobile-swipe-menu)
                                                        (when (not= activity-uuid mobile-swipe-menu)
                                                          (compare-and-set! (::show-mobile-follow-bt s) true false)))))
                                                  s)
                                    :will-unmount (fn [s]
                                                    (when @(::on-scroll s)
                                                      (events/unlistenByKey @(::on-scroll s))
                                                      (reset! (::on-scroll s) nil))
                                                    s)}
  [s {:keys [activity-data read-data comments-data foc-show-menu foc-share-entry
             foc-menu-open foc-labels-picker foc-activity-move foc-other-menu]}]
  (let [is-mobile? (responsive/is-mobile-size?)
        current-board-slug (drv/react s :board-slug)
        current-activity-id (drv/react s :activity-uuid)
        is-drafts-board (= current-board-slug utils/default-drafts-board-slug)
        is-published? (au/is-published? activity-data)
        has-zero-comments? (and (-> activity-data :comments count zero?)
                                (-> comments-data (get (:uuid activity-data)) :sorted-comments count zero?))
        follow-link (hateoas/link-for (:links activity-data) "follow")
        unfollow-link (hateoas/link-for (:links activity-data) "unfollow")
        mobile-swipe-menu-uuid (drv/react s :mobile-swipe-menu)]
    [:div.stream-collapsed-item
      {:class (dom-utils/class-set {:draft (not is-published?)
                                    :unseen-item (:unseen activity-data)
                                    :unread-item (or (pos? (:new-comments-count activity-data))
                                                    (:unread activity-data))
                                    :expandable is-published?
                                    :showing-share foc-share-entry})
       :data-last-activity-at (:last-activity-at activity-data)
       :data-last-read-at (:last-read-at activity-data)
       ;; click on the whole tile only for draft editing
       :on-click (cond is-drafts-board
                       #(activity-actions/activity-edit activity-data)
                       (seq mobile-swipe-menu-uuid)
                       #(dismiss-swipe-button s %)
                       (or foc-show-menu foc-menu-open foc-activity-move foc-labels-picker foc-share-entry foc-other-menu)
                       identity
                       :else
                       (fn [e]
                         (when-not (dom-utils/event-container-matches e "input, button, a, .foc-collapsed-click-stop")
                           (nav-actions/open-post-modal activity-data false))))}
      [:div.stream-collapsed-item-inner
        {:class (dom-utils/class-set {:bookmark-item (:bookmarked-at activity-data)
                                      :muted-item follow-link
                                      :new-item (pos? (:unseen-comments activity-data))
                                      :no-comments has-zero-comments?
                                      :showing-share foc-share-entry})}
        (when (or follow-link
                  unfollow-link)
          [:button.mlb-reset.mobile-follow-bt
            {:class (dom-utils/class-set {:visible @(::show-mobile-follow-bt s)
                                          :follow follow-link})
             :on-click (fn [e]
                         (dismiss-swipe-button s e ::show-mobile-follow-bt)
                         (if follow-link
                           (activity-actions/entry-follow (:uuid activity-data))
                           (activity-actions/entry-unfollow (:uuid activity-data))))}
            [:span.mobile-follow-bt-icon]
            [:span.mobile-follow-bt-text
             (if follow-link
               "Follow"
               "Mute")]])
        (if is-mobile?
          [:div.stream-collapsed-item-fillers
           [:div.stream-collapsed-item-avatar
            (face-pile {:width 40 :max-faces 10 :faces (:authors (:for-you-context activity-data))})]
           [:div.stream-collapsed-item-fill
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
                 :title (utils/activity-date-tooltip activity-data)}
                (utils/foc-date-time t)])]]
            [:div.stream-collapsed-item-fill
              ;;  [:div.stream-item-arrow]
              [:div.stream-item-headline.ap-seen-item-headline
               {:ref "activity-headline"
                :data-itemuuid (:uuid activity-data)
                :class utils/hide-class}
               (:headline activity-data)]]]
          [:div.stream-collapsed-item-fill
            [:div.stream-collapsed-item-avatar
              {:class utils/hide-class}
              (face-pile {:width 24 :faces (:authors (:for-you-context activity-data))})]
            [:div.stream-item-context
             {:class utils/hide-class}
             (str (-> activity-data :for-you-context :label) " in:")]
            ;; Needed to wrap mobile on a new line
            [:div.stream-item-break]
            ;; [:div.stream-item-arrow]
            [:div.stream-item-headline.ap-seen-item-headline
              {:ref "activity-headline"
               :data-itemuuid (:uuid activity-data)
               :class utils/hide-class}
             (:headline activity-data)]
            [:div.stream-collapsed-item-dot.muted-dot]
            [:div.new-item-tag]
            [:div.bookmark-tag-small]
            [:div.muted-activity
             {:data-toggle (when-not is-mobile? "tooltip")
              :data-placement "top"
              :title "Muted"}]
            [:div.collapsed-time
              (let [t (:timestamp (:for-you-context activity-data))]
                [:time
                 {:date-time t
                  :data-toggle (when-not is-mobile? "tooltip")
                  :data-placement "top"
                  :data-container "body"
                  :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                  :title (utils/activity-date-tooltip activity-data)}
                (utils/foc-date-time t)])]])]
      (when (and is-published?
                 (not is-mobile?)
                 (not (seq current-activity-id)))
        (more-menu {:entity-data activity-data
                    :foc-share-entry foc-share-entry
                    :foc-show-menu foc-show-menu
                    :foc-labels-picker false ;; foc-labels-picker
                    :foc-menu-open foc-menu-open
                    :foc-activity-move false ;; foc-activity-move
                    :hide-bookmark? true
                    :hide-share? true
                    :hide-labels? true
                    :external-follow true
                    :show-edit? true
                    :show-delete? true
                    :show-move? true
                    :custom-class "foc-collapsed-click-stop"}))]))