(ns oc.web.components.stream-collapsed-item
  (:require [rum.core :as rum]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [dommy.core :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.face-pile :refer (face-pile)]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.mixins.gestures :refer (swipe-gesture-manager)]
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

(defn- dismiss-swipe-button [s & [e ref-kw]]
  (when e
    (utils/event-stop e))
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
                                   (drv/drv :activity-share-container)
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
                                                                               (not (utils/link-for (:links ad) "follow"))
                                                                               (not (utils/link-for (:links ad) "unfollow"))))}))
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
  [s {:keys [activity-data read-data comments-data]}]
  (let [is-mobile? (responsive/is-mobile-size?)
        current-board-slug (drv/react s :board-slug)
        current-activity-id (drv/react s :activity-uuid)
        is-drafts-board (= current-board-slug utils/default-drafts-board-slug)
        is-inbox? (= current-board-slug "inbox")
        dom-element-id (str "stream-collapsed-item-" (:uuid activity-data))
        dom-node-class (str "stream-collapsed-item-" (:uuid activity-data))
        is-published? (au/is-published? activity-data)
        has-zero-comments? (and (-> activity-data :comments count zero?)
                                (-> comments-data (get (:uuid activity-data)) :sorted-comments count zero?))
        follow-link (utils/link-for (:links activity-data) "follow")
        unfollow-link (utils/link-for (:links activity-data) "unfollow")
        mobile-swipe-menu-uuid (drv/react s :mobile-swipe-menu)]
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
                   (cond is-drafts-board
                         (activity-actions/activity-edit activity-data)
                         (seq mobile-swipe-menu-uuid)
                         (dismiss-swipe-button s e)
                         :else
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
                                  :muted-item follow-link
                                  :new-item (pos? (:unseen-comments activity-data))
                                  :no-comments has-zero-comments?})}
        (when (or follow-link
                  unfollow-link)
          [:button.mlb-reset.mobile-follow-bt
            {:class (utils/class-set {:visible @(::show-mobile-follow-bt s)
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
                :class utils/hide-class
                :dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]]]
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
               :class utils/hide-class
               :dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]
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
      [:div.activity-share-container]
      (when (and is-published?
                 (not is-mobile?))
        (more-menu {:entity-data activity-data
                    :share-container-id dom-element-id
                    :hide-bookmark? true
                    :hide-share? true
                    :external-follow true}))]))