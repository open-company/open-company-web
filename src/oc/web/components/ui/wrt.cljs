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
    (remove nil? (concat self-user sorted-other-users))))

(defn dropdown-label [val total]
  (case val
    :all (str "Everyone (" total ")")
    :seen "Viewed"
    :unseen "Unopened"))

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
        read-data (drv/react s :wrt-read-data)
        item-id (:uuid activity-data)
        seen-users (vec (sort-by user-lib/name-for (:reads read-data)))
        seen-ids (set (map :user-id seen-users))
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
        current-user-data (drv/react s :current-user-data)
        sorted-filtered-users (sort-users (:user-id current-user-data) filtered-users)
        is-mobile? (responsive/is-tablet-or-mobile?)
        seen-percent (int (* (/ (count seen-users) (count all-users)) 100))
        team-id (:team-id org-data)
        slack-bot-data (first (jwt/team-has-bot? team-id))]
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
         :on-click #(.stopPropagation %)}
        [:div.wrt-popup-header
          [:button.mlb-reset.mobile-close-bt
            {:on-click nav-actions/hide-wrt}]
          [:div.wrt-popup-header-title
            "Who viewed this post"]]
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
                     :stroke "#ECECEC"
                     :stroke-width "16px"}]
                  [:circle.wrt-donut-segment
                    {:cx "58"
                     :cy "58"
                     :r "50"
                     :fill "transparent"
                     :stroke "#3FBD7C"
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
                    "Manage section members?"])]]
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
                          slack-user          (get (:slack-users u) (keyword (:slack-org-id slack-bot-data)))
                          follow-up           (first (filterv #(= (-> % :assignee :user-id) (:user-id u)) (:follow-ups activity-data)))
                          follow-up-string    (when (and follow-up
                                                         (not (:completed? follow-up)))
                                                ", marked for follow-up")]]
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
                      (str "Opened " (string/lower (utils/time-since (:read-at u))))
                      (if user-sending-notice
                        (if (= user-sending-notice :loading)
                          "Sending..."
                          user-sending-notice)
                        "Unopened"))
                    follow-up-string]
                  ;; Send reminder button
                  (when (and (not (:seen u))
                             (not is-self-user?)
                             (not user-sending-notice))
                    [:button.mlb-reset.send-reminder-bt
                      {:on-click (fn [_]
                                   (let [user-payload (if (and slack-user
                                                               (= (:notification-medium u) "slack"))
                                                        {:medium "slack"
                                                         :channel {:slack-org-id (:slack-org-id slack-user)
                                                                   :channel-id (:id slack-user)
                                                                   :channel-name "Carrot"
                                                                   :type "user"}}
                                                        {:medium "email"
                                                         :to [(:email u)]})
                                         wrt-share (merge user-payload
                                                    {:note "When you have a moment, please check out this post."
                                                     :subject (str "You may have missed: " (:headline activity-data))})]
                                     (swap! (::sending-notice s) assoc (:user-id u) :loading)
                                     ;; Show the share popup
                                     (activity-actions/activity-share activity-data [wrt-share]
                                      (fn [{:keys [success body]}]
                                        (if success
                                          (let [resp (first body)
                                                user-label (if (= (:medium wrt-share) "email")
                                                             (str "Sent to: " (:email u))
                                                             (if (and slack-user
                                                                      (seq (:display-name slack-user))
                                                                      (not= (:display-name slack-user) "-"))
                                                               (str "Sent to: @" (:display-name slack-user) " (Slack)")
                                                               (str "Sent via Slack")))]
                                            (swap! (::sending-notice s) assoc (:user-id u) user-label))
                                          (do
                                            (swap! (::sending-notice s) assoc (:user-id u) "An error occurred, please retry...")
                                            (utils/after 5000 #(swap! (::sending-notice s) dissoc (:user-id u)))))))))}
                      "Send"])])]])]]))

(defn- under-middle-screen? [el]
  (let [el-offset-top (aget (.offset (js/$ el)) "top")
        fixed-top-position (- el-offset-top (.-scrollTop (.-scrollingElement js/document)))
        win-height (.-innerHeight js/window)]
    (>= fixed-top-position (/ win-height 2))))

(rum/defc wrt-count < rum/static
  [{:keys [activity-data reads-data hide-label?]}]
  (let [item-id (:uuid activity-data)
        reads-count (:count reads-data)]
    [:div.wrt-count-container
      [:div.wrt-count
        {:ref :wrt-count
         :on-click #(nav-actions/show-wrt item-id)
         :class (when (pos? (count (:reads reads-data))) "has-read-list")}
        (if reads-count
          (str reads-count
           (when-not hide-label?
             (str " viewer" (when (not= reads-count 1) "s"))))
          (if-not hide-label?
            "0 viewers"
            "0"))]]))
