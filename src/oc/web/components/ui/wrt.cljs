(ns oc.web.components.ui.wrt
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.actions.notifications :as notifications-actions]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn show-wrt
  "Show WRT for the given activity item."
  [activity-uuid]
  (dis/dispatch! [:input [:wrt-show] activity-uuid]))

(defn hide-wrt
  "Hide WRT, if an activity id is passed hide only if
  that's the current activity we are showing WRT for,
  user to prevent race conditions with mouse over/enter/leave/click events.
  If nothing is passed hide WRT for any activity."
  [& [activity-uuid]]
  (if activity-uuid
    (when (= (:wrt-show @dis/app-state) activity-uuid)
      (dis/dispatch! [:input [:wrt-show] nil]))
    (dis/dispatch! [:input [:wrt-show] nil])))

(defn dismiss-modal [& [s]]
  (if s
    (reset! (::unmounting s) true)
    (hide-wrt)))

(defn- filter-by-query [user query]
  (let [complete-name (or (:name user) (str (:first-name user) " " (:last-name user)))]
    (or (string/includes? (string/lower (:email user)) query)
        (string/includes? (string/lower complete-name) query)
        (string/includes? (string/lower (:first-name user)) query)
        (string/includes? (string/lower (:last-name user)) query))))

(defn- reset-search [s]
  (reset! (::search-active s) false)
  (reset! (::query s) "")
  (reset! (::search-focused s) false))

(defn- sort-users [user-id users]
  (let [{:keys [self-user other-users]}
         (group-by #(if (= (:user-id %) user-id) :self-user :other-users) users)
        sorted-other-users (sort-by utils/name-or-email other-users)]
    (remove nil? (concat self-user sorted-other-users))))

(defn dropdown-label [val total]
  (case val
    :all (str "Everyone (" total ")")
    :seen "Viewed"
    :unseen "Unopened"))

(rum/defcs wrt < rum/reactive
                 ;; Locals
                 (rum/local false ::search-active)
                 (rum/local false ::search-focused)
                 (rum/local "" ::query)
                 (rum/local false ::list-view-dropdown-open)
                 (rum/local :all ::list-view) ;; :seen :unseen
                 (rum/local false ::unmounting)
                 (rum/local false ::unmounted)
                 (drv/drv :current-user-data)

                 mixins/no-scroll-mixin
                 mixins/first-render-mixin

                 {:after-render (fn [s]
                   (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
                   (when @(::search-active s)
                      (when (compare-and-set! (::search-focused s) false true)
                        (.focus (rum/ref-node s :search-field))))
                   s)
                  :did-update (fn [s]
                   (when (and @(::unmounting s)
                              (compare-and-set! (::unmounted s) false true))
                     (utils/after 180 hide-wrt))
                   s)}

  [s activity-data read-data show-above]
  (let [item-id (:uuid activity-data)
        seen-users (vec (sort-by utils/name-or-email (:reads read-data)))
        seen-ids (set (map :user-id seen-users))
        unseen-users (vec (sort-by utils/name-or-email (:unreads read-data)))
        all-users (sort-by utils/name-or-email (concat seen-users unseen-users))
        read-count (:count read-data)
        query (::query s)
        lower-query (string/lower @query)
        list-view (::list-view s)
        filtered-users (case @list-view
                         :all (filterv #(filter-by-query % (string/lower (or @query ""))) all-users)
                         :seen seen-users
                         :unseen unseen-users)
        current-user-data (drv/react s :current-user-data)
        sorted-filtered-users (sort-users (:user-id current-user-data) filtered-users)
        is-mobile? (responsive/is-tablet-or-mobile?)
        appear-class (and @(:first-render-done s)
                          (not @(::unmounting s))
                          (not @(::unmounted s)))]
    [:div.wrt-popup-container
      {:class (utils/class-set {:appear appear-class})
       :on-click #(dismiss-modal s)}
      [:button.mlb-reset.modal-close-bt
        {:on-click #(dismiss-modal s)}]
      [:div.wrt-popup
        {:class (utils/class-set {:top show-above
                                  :loading (not (:reads read-data))})
         :on-click #(.stopPropagation %)}
        [:div.wrt-popup-header
          [:div.wrt-popup-header-title
            "Who viewed this post"]]
        ;; Show a spinner on mobile if no data is loaded yet
        (if-not (:reads read-data)
          (when is-mobile?
            (small-loading))
          [:div.wrt-popup-inner
            [:div.wrt-popup-tabs
              [:div.wrt-popup-tabs-select
                {:on-click #(swap! (::list-view-dropdown-open s) not)}
                (dropdown-label @list-view (count all-users))]
              (when @(::list-view-dropdown-open s)
                (dropdown-list {:items [{:value :all
                                         :label (dropdown-label :all (count all-users))}
                                        {:value :seen
                                         :label (dropdown-label :seen (count all-users))}
                                        {:value :unseen
                                         :label (dropdown-label :unseen (count all-users))}]
                                 :value @list-view
                                 :on-change #(do
                                              (reset! list-view (:value %))
                                              (reset! (::list-view-dropdown-open s) false)
                                              (reset! query ""))}))]
            (when (= @list-view :all)
              [:div.wrt-popup-search-container.group
                [:input.wrt-popup-query
                  {:value @query
                   :type "text"
                   :placeholder "Search by name..."
                   :ref :search-field
                   :on-key-up #(when (= (.-key %) "Escape")
                                 (reset-search s))
                   :on-change #(reset! query (.. % -target -value))}]])
            [:div.wrt-popup-list
              (for [u sorted-filtered-users]
                [:div.wrt-popup-list-row
                  {:key (str "wrt-popup-row-" (:user-id u))}
                  [:div.wrt-popup-list-row-avatar
                    {:class (when (:seen u) "seen")}
                    (user-avatar-image u)]
                  [:div.wrt-popup-list-row-name
                    (utils/name-or-email u)]
                  [:div.wrt-popup-list-row-seen
                    {:class (when (:seen u) "seen")}
                    (if (:seen u)
                      ;; Show time the read happened
                      (utils/time-since (:read-at u))
                      ;; Send reminder button
                      [:button.mlb-reset.send-reminder-bt
                        {:on-click #(let [email-share {:medium :email
                                                       :note "When you have a moment, please check out this post."
                                                       :subject (str "Just a reminder: " (:headline activity-data))
                                                       :to [(:email u)]}]
                                      ;; Show the share popup
                                      (activity-actions/activity-share activity-data [email-share]
                                       (fn []
                                        (notifications-actions/show-notification
                                         {:title (str "Reminder sent to " (utils/name-or-email u) ".")
                                          :id (str "wrt-share-" (utils/name-or-email u))
                                          :dismiss true
                                          :expire 3}))))}
                        "Send post"])]])]])]]))

(defn- under-middle-screen? [el]
  (let [el-offset-top (aget (.offset (js/$ el)) "top")
        fixed-top-position (- el-offset-top (.-scrollTop (.-scrollingElement js/document)))
        win-height (.-innerHeight js/window)]
    (>= fixed-top-position (/ win-height 2))))

(rum/defcs wrt-count < rum/reactive
                       ;; Locals
                       (rum/local false ::under-middle-screen)
                       ;; Derivatives
                       (drv/drv :wrt-show)
                       {:did-mount (fn [s]
                        (when-not (responsive/is-tablet-or-mobile?)
                          (reset! (::under-middle-screen s) (under-middle-screen? (rum/dom-node s))))
                        s)
                        :did-remount (fn [_ s]
                        (when-not (responsive/is-tablet-or-mobile?)
                          (reset! (::under-middle-screen s) (under-middle-screen? (rum/dom-node s))))
                        s)}

  [s activity-data read-data]
  (let [item-id (:uuid activity-data)
        read-count (:count read-data)
        wrt-show (drv/react s :wrt-show)
        is-mobile? (responsive/is-tablet-or-mobile?)]
    [:div.wrt-count-container
      {:on-mouse-over #(when (and (not is-mobile?)
                                  (not (:reads read-data)))
                        (activity-actions/request-reads-data item-id))}
      [:div.wrt-count
        {:ref :wrt-count
         :on-click #(do
                    (when (not (:reads read-data))
                      (activity-actions/request-reads-data item-id))
                    (show-wrt item-id))
         :class (when (pos? (count (:reads read-data))) "has-read-list")}
        (if read-count
          (str read-count " Viewer" (when (not= read-count 1) "s"))
          "0 Viewers")]
      (when (and (not is-mobile?)
                 (= wrt-show item-id))
        (wrt activity-data read-data @(::under-middle-screen s)))]))