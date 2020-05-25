(ns oc.web.components.ui.wrt
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.lib.user :as user-lib]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.actions.ui-theme :as ui-theme]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

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
        sorted-other-users (sort-by user-lib/name-for other-users)]
    (vec (remove nil? (concat self-user sorted-other-users)))))

(defn dropdown-label [val total]
  (case val
    :all (str "Everyone (" total ")")
    :seen "Viewed"
    :unseen "Unopened"))

(defn- remind-to [s {:keys [current-user-data activity-data users-list slack-bot-data]}]
  (let [wrt-share-base {:note "When you have a moment, please check out this post."
                        :subject (str "You may have missed: " (:headline activity-data))}
        wrt-share (apply merge
                   (for [u users-list
                        :when (and (not (:seen u))
                                   (not= (:user-id current-user-data) (:user-id u))
                                   (not (get @(::sending-notice s) (:user-id u))))
                        :let [slack-user (get (:slack-users u) (keyword (:slack-org-id slack-bot-data)))]]
                    {(:user-id u)
                     (merge wrt-share-base
                      (if (and slack-user
                               (= (:notification-medium u) "slack"))
                        {:medium "slack"
                         :channel {:slack-org-id (:slack-org-id slack-user)
                                   :channel-id (:id slack-user)
                                   :channel-name "Wut"
                                   :type "user"}}
                        {:medium "email"
                         :to [(:email u)]}))}))]
    (swap! (::sending-notice s) merge (zipmap (keys wrt-share) (repeat (count wrt-share) :loading)))
    (reset! (::show-remind-all-bt s) false)
    (activity-actions/activity-share activity-data (vals wrt-share)
     (fn [{:keys [success body]}]
       (reset! (::show-remind-all-bt s) true)
       (if success
         (let [noticed-users (apply merge
                              (for [user-id (keys wrt-share)
                                    :let [u (some #(when (= (:user-id %) user-id) %) users-list)
                                          slack-user (get (:slack-users u) (keyword (:slack-org-id slack-bot-data)))]]
                               {(:user-id u)
                                (if (and slack-user
                                         (= (:notification-medium u) "slack"))
                                  (if (and slack-user
                                           (seq (:display-name slack-user))
                                           (not= (:display-name slack-user) "-"))
                                    (str "Sent to: @" (:display-name slack-user) " (Slack)")
                                    (str "Sent via Slack"))
                                  (str "Sent to: " (:email u)))}))]

           (swap! (::sending-notice s) merge noticed-users))
         (swap! (::sending-notice s) merge
          (apply merge
           (for [user-id (keys wrt-share)]
            (do
              (utils/after 5000 #(swap! (::sending-notice s) dissoc user-id))
              {user-id "An error occurred, please retry..."})))))))))

(defn- remind-to-all [s {:keys [unopened-count] :as props}]
  (let [alert-data {:emoji-icon "👌"
                    :action "remind-all-unseens"
                    :message (str "Do you want to send a reminder to everyone that hasn’t opened it? (" unopened-count " users)")
                    :link-button-title "No"
                    :link-button-cb #(alert-modal/hide-alert)
                    :solid-button-style :red
                    :solid-button-title "Yes, remind them"
                    :solid-button-cb (fn []
                                      (remind-to s props)
                                      (alert-modal/hide-alert))}]
    (alert-modal/show-alert alert-data)))

(rum/defcs wrt < rum/reactive
                 ;; Derivatives
                 (drv/drv :wrt-read-data)
                 (drv/drv :wrt-activity-data)
                 (drv/drv :current-user-data)
                 ;; Locals
                 (rum/local false ::search-active)
                 (rum/local false ::search-focused)
                 (rum/local "" ::query)
                 (rum/local false ::list-view-dropdown-open)
                 (rum/local :all ::list-view) ;; :seen :unseen
                 (rum/local {} ::sending-notice)
                 (rum/local true ::show-remind-all-bt)

                 mixins/no-scroll-mixin
                 mixins/first-render-mixin
                 mixins/refresh-tooltips-mixin

                 {:will-mount (fn [s]
                   (when-let [activity-data @(drv/get-ref s :wrt-activity-data)]
                     (activity-actions/request-reads-data (:uuid activity-data)))
                   s)
                  :after-render (fn [s]
                   (when @(::search-active s)
                      (when (compare-and-set! (::search-focused s) false true)
                        (.focus (rum/ref-node s :search-field))))
                   s)}
  [s org-data]
  (let [activity-data (drv/react s :wrt-activity-data)
        current-user-data (drv/react s :current-user-data)
        read-data (drv/react s :wrt-read-data)
        unread-data (filter #(not= (:user-id %) (:user-id current-user-data)) (:unread read-data))
        item-id (:uuid activity-data)
        seen-users (vec (sort-by user-lib/name-for unread-data))
        seen-ids (disj (set (map :user-id seen-users)) (:user-id current-user-data))
        unseen-users (vec (sort-by user-lib/name-for (:unreads read-data)))
        all-users (sort-by user-lib/name-for (concat seen-users unseen-users))
        read-count (:count read-data)
        query (::query s)
        lower-query (string/lower @query)
        list-view (::list-view s)
        filtered-users (case @list-view
                         :all (filterv #(filter-by-query % (string/lower (or @query ""))) all-users)
                         :seen seen-users
                         :unseen unseen-users)
        sorted-filtered-users (sort-users (:user-id current-user-data) filtered-users)
        is-mobile? (responsive/is-tablet-or-mobile?)
        seen-percent (int (* (/ (count seen-users) (count all-users)) 100))
        team-id (:team-id org-data)
        slack-bot-data (first (jwt/team-has-bot? team-id))
        remind-all-users (filterv #(and (not (get @(::sending-notice s) (:user-id %)))
                                        (not= (:user-id %) (:user-id current-user-data)))
                          unseen-users)]
    [:div.wrt-popup-container
      {:on-click #(if @(::list-view-dropdown-open s)
                    (when-not (utils/event-inside? % (rum/ref-node s :wrt-pop-up-tabs))
                      (reset! (::list-view-dropdown-open s) false))
                    (when-not (utils/event-inside? % (rum/ref-node s :wrt-popup))
                      (nav-actions/hide-wrt)))}
      [:button.mlb-reset.modal-close-bt
        {:on-click nav-actions/hide-wrt}]
      [:div.wrt-popup
        {:class (utils/class-set {:loading (not (:reads read-data))})
         :ref :wrt-popup
         :on-click #(when @(::list-view-dropdown-open s)
                      (when-not (utils/event-inside? % (rum/ref-node s :wrt-pop-up-tabs))
                        (reset! (::list-view-dropdown-open s) false))
                        (utils/event-stop %))}
        [:div.wrt-popup-header
          [:button.mlb-reset.mobile-close-bt
            {:on-click nav-actions/hide-wrt}]
          [:div.wrt-popup-header-title
            "Post analytics"]]
        ;; Show a spinner on mobile if no data is loaded yet
        (if-not (:reads read-data)
          (small-loading)
          [:div.wrt-popup-inner
            [:div.wrt-chart-container
              [:div.wrt-chart
                [:svg
                  {:width "116px"
                   :height "116px"
                   :viewBox "0 0 116 116"
                   :version "1.1"
                   :xmlns "http://www.w3.org/2000/svg"
                   :xmlnsXlink "http://www.w3.org/1999/xlink"}
                  [:circle.wrt-donut-ring
                    {:cx "58px"
                     :cy "58px"
                     :r "50px"
                     :fill "transparent"
                     :stroke (if (= (ui-theme/computed-value (ui-theme/get-ui-theme-setting)) :dark) "#DDDDDD" "#ECECEC")
                     :stroke-width "16px"}]
                  [:circle.wrt-donut-segment
                    {:cx "58"
                     :cy "58"
                     :r "50"
                     :fill "transparent"
                     :stroke "#6833F1"
                     :stroke-width "16"
                     :class (when @(:first-render-done s) (str "wrt-donut-segment-" seen-percent))}]
                  [:g.wrt-chart-text
                    [:text.wrt-chart-number
                      {:x "50%" :y "50%"}
                      (str seen-percent "%")]]]]
              [:div.wrt-chart-label
                (cond 
                  (= (count all-users) (count seen-users))
                  "👏 Everyone has seen this post!"
                  (= 1 (count seen-users))
                  "1 person has viewed this post."
                  (zero? (count seen-users))
                  "No one has viewed this post."
                  :else
                  (str (count seen-users)
                   " of "
                   (count all-users)
                   " people viewed this "
                   (when (:private-access? read-data)
                     "private ")
                   "post."))
                (when (and (:private-access? read-data)
                           (dis/board-data (router/current-org-slug) (:board-slug activity-data)))
                  [:button.mlb-reset.manage-section-bt
                    {:on-click #(nav-actions/show-section-editor (:board-slug activity-data))}
                    "Manage team members?"])
                (when (and (> (count remind-all-users) 1)
                           @(::show-remind-all-bt s))
                  [:button.mlb-reset.send-to-all-bt
                    {:on-click #(remind-to-all s {:activity-data activity-data
                                                  :current-user-data current-user-data
                                                  :users-list remind-all-users
                                                  :slack-bot-data slack-bot-data
                                                  :unopened-count (count unseen-users)})
                     :data-toggle (when-not is-mobile? "tooltip")
                     :data-placement "top"
                     :title "Send a reminder to everyone that hasn’t opened it"}
                    "Send reminders"])]]
            [:div.wrt-popup-tabs
              {:ref :wrt-pop-up-tabs}
              [:div.wrt-popup-tabs-select.oc-input
                {:on-click #(swap! (::list-view-dropdown-open s) not)
                 :class (when @(::list-view-dropdown-open s) "active")}
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
                [:input.wrt-popup-query.oc-input
                  {:value @query
                   :type "text"
                   :placeholder "Search by name..."
                   :ref :search-field
                   :on-key-up #(when (= (.-key %) "Escape")
                                 (reset-search s))
                   :on-change #(reset! query (.. % -target -value))}]])
            [:div.wrt-popup-list
              (for [u sorted-filtered-users
                    :let [user-sending-notice (get @(::sending-notice s) (:user-id u))
                          is-self-user?       (= (:user-id current-user-data) (:user-id u))
                          slack-user          (get (:slack-users u) (keyword (:slack-org-id slack-bot-data)))]]
                [:div.wrt-popup-list-row
                  {:key (str "wrt-popup-row-" (:user-id u))
                   :class (utils/class-set {:seen (and (:seen u) (= @list-view :all))
                                            :sent user-sending-notice})}
                  [:div.wrt-popup-list-row-avatar
                    {:class (when (:seen u) "seen")}
                    (user-avatar-image u)]
                  [:div.wrt-popup-list-row-name
                    (user-lib/name-for u)
                    (when is-self-user?
                      " (you)")]
                  [:div.wrt-popup-list-row-seen
                    (if (:seen u)
                      ;; Show time the read happened
                      (string/capital (string/lower (utils/time-since (:read-at u))))
                      (if user-sending-notice
                        (if (= user-sending-notice :loading)
                          "Sending..."
                          user-sending-notice)
                        "Unopened"))]
                  ;; Send reminder button
                  (when (and (not (:seen u))
                             (not is-self-user?)
                             (not user-sending-notice))
                    [:button.mlb-reset.send-reminder-bt
                      {:on-click #(remind-to s {:activity-data activity-data
                                                :current-user-data current-user-data
                                                :users-list [u]
                                                :slack-bot-data slack-bot-data})}
                      "Remind"])])]])]]))

(defn- under-middle-screen? [el]
  (let [el-offset-top (aget (.offset (js/$ el)) "top")
        fixed-top-position (- el-offset-top (.-scrollTop (.-scrollingElement js/document)))
        win-height (.-innerHeight js/window)]
    (>= fixed-top-position (/ win-height 2))))

(rum/defc wrt-count < rum/static
  [{:keys [activity-data reads-data]}]
  (let [item-id (:uuid activity-data)
        reads-count (:count reads-data)]
    [:div.wrt-count-container
      [:div.wrt-count
        {:ref :wrt-count
         :on-click #(nav-actions/show-wrt item-id)
         :class (when (pos? (count (:reads reads-data))) "has-read-list")}
        (cond
          (= reads-count 1)
          "1 person viewed"
          (pos? reads-count)
          (str reads-count " people viewed")
          :else
          "Viewers")]]))
