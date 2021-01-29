(ns oc.web.components.ui.wrt
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.utils.dom :as du]
            [oc.web.utils.org :as ou]
            [oc.web.utils.wrt :as wu]
            [oc.web.utils.user :as uu]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.local-settings :as ls]
            [oc.web.utils.theme :as theme-utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.activity :as activity-actions]
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
        sorted-other-users (sort-by :name other-users)]
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
                                   :channel-name ls/product-name
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

(def premium-remind-all-tooltip "Please upgrade to premium for Analytics and “nudges”. Know who saw what, and have one-click reminders so important updates aren’t missed.")
(def premium-remind-tooltip "Please upgrade to premium for Analytics and “nudges”. Know who saw what, and have one-click reminders so important updates aren’t missed.")
(def premium-download-csv-tooltip "Please upgrade to premium to download your team's Analytics data.")

(rum/defcs wrt < rum/reactive
                 ;; Derivatives
                 (drv/drv :wrt-read-data)
                 (drv/drv :wrt-activity-data)
                 (drv/drv :current-user-data)
                 (drv/drv :org-slug)
                 (drv/drv :theme)
                 ;; Locals
                 (rum/local false ::search-active)
                 (rum/local false ::search-focused)
                 (rum/local "" ::query)
                 (rum/local false ::list-view-dropdown-open)
                 (rum/local :all ::list-view) ;; :seen :unseen
                 (rum/local {} ::sending-notice)
                 (rum/local true ::show-remind-all-bt)
                 (rum/local nil ::jelly-head)

                 mixins/no-scroll-mixin
                 mixins/first-render-mixin
                 mixins/strict-refresh-tooltips-mixin

                 {:will-mount (fn [s]
                   (reset! (::jelly-head s) (uu/random-avatar))
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
        theme-data (drv/react s :theme)
        current-theme (theme-utils/computed-value theme-data)
        current-user-data (drv/react s :current-user-data)
        current-org-slug (drv/react s :org-slug)
        is-author? (:publisher? activity-data)
        read-data* (drv/react s :wrt-read-data)
        read-data (as-> read-data* rd
                    (update rd :unreads (fn [unreads]
                                          (if is-author?
                                            (filter #(not= (:user-id %) (:user-id current-user-data)) unreads)
                                            unreads)))
                    (update rd :reads (fn [reads]
                                        (if is-author?
                                          (filter #(not= (:user-id %) (:user-id current-user-data)) reads)
                                          reads))))
        seen-users (vec (sort-by :name (:reads read-data)))
        unseen-users (vec (sort-by :name (:unreads read-data)))
        all-users (sort-by :name (concat seen-users unseen-users))
        query (::query s)
        lower-query (string/lower (or @query ""))
        list-view (::list-view s)
        filtered-users (case @list-view
                        :all (filterv #(filter-by-query % lower-query) all-users)
                        :seen seen-users
                        :unseen unseen-users)
        sorted-filtered-users (sort-users (:user-id current-user-data) filtered-users)
        sort-all-users (sort-users (:user-id current-user-data) all-users)
        all-users-list (map #(if (:premium? org-data)
                               %
                               (-> %
                                   (assoc :avatar-url @(::jelly-head s))
                                   (assoc :name "Team member")))
                             sort-all-users)
        is-mobile? (responsive/is-tablet-or-mobile?)
        seen-percent (int (* (/ (count seen-users) (count all-users)) 100))
        team-id (:team-id org-data)
        slack-bot-data (first (jwt/team-has-bot? team-id))
        remind-all-users (filterv #(and (not (get @(::sending-notice s) (:user-id %)))
                                        (not= (:user-id %) (:user-id current-user-data)))
                                  unseen-users)
        remind-all-cb (if (:premium? org-data)
                        (fn []
                          (remind-to-all s {:activity-data activity-data
                                            :current-user-data current-user-data
                                            :users-list remind-all-users
                                            :slack-bot-data slack-bot-data
                                            :unopened-count (count unseen-users)}))
                        #(nav-actions/toggle-premium-picker! premium-remind-all-tooltip))
        remind-all-tooltip (if (:premium? org-data)
                             "Send a reminder to everyone that hasn’t opened it"
                             (str premium-remind-all-tooltip " Click for details."))
        remind-tooltip (when-not (:premium? org-data)
                         (str premium-remind-tooltip " Click for details."))
        show-remind-all-bt? (and (> (count remind-all-users) 1)
                                 @(::show-remind-all-bt s)
                                 (or (not is-mobile?)
                                     (:premium? org-data)))
        users-list (map #(if (:premium? org-data)
                           %
                           (-> %
                               (assoc :avatar-url @(::jelly-head s))
                               (assoc :name "Team member")))
                        sorted-filtered-users)
        download-csv-tooltip (when-not (:premium? org-data)
                               (str premium-download-csv-tooltip " Click for details"))]
    [:div.wrt-popup-container
      {:on-click #(if @(::list-view-dropdown-open s)
                    (when-not (du/event-inside? % (rum/ref-node s :wrt-pop-up-tabs))
                      (reset! (::list-view-dropdown-open s) false))
                    (when-not (du/event-inside? % (rum/ref-node s :wrt-popup))
                      (nav-actions/hide-wrt)))}
      [:button.mlb-reset.modal-close-bt
        {:on-click nav-actions/hide-wrt}]
      [:div.wrt-popup
        {:class (utils/class-set {:loading (not (:reads read-data))})
        :ref :wrt-popup
        :on-click #(when @(::list-view-dropdown-open s)
                      (when-not (du/event-inside? % (rum/ref-node s :wrt-pop-up-tabs))
                        (reset! (::list-view-dropdown-open s) false))
                        (du/prevent-default! %))}
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
                    :stroke (if (= current-theme :dark) "#DDDDDD" "#ECECEC")
                    :stroke-width "16px"}]
                  [:circle.wrt-donut-segment
                    {:cx "58"
                    :cy "58"
                    :r "50"
                    :fill "transparent"
                    :stroke (-> (ou/current-brand-color) :primary :hex)
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
                  "post."))]
              (when (and (:private-access? read-data)
                        (dis/board-data current-org-slug (:board-slug activity-data)))
                [:button.mlb-reset.manage-section-bt
                  {:on-click #(nav-actions/show-section-editor (:board-slug activity-data))}
                  "Manage team members?"])
              (when show-remind-all-bt?
                [:button.mlb-reset.send-to-all-bt
                  {:on-click remind-all-cb
                    :data-toggle (when-not is-mobile? "tooltip")
                    :data-placement "top"
                    :title remind-all-tooltip}
                  "Send reminders"])]
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
            (when (and (not is-mobile?)
                       (not (get-in org-data [:content-visibility :disallow-wrt-download])))
              [:div.wrt-download-csv-container.group
                [:a.download-csv-bt
                 {:href (if (:premium? org-data)
                          (wu/encoded-csv activity-data ["Name" "Email" "Read"] all-users-list)
                          "#")
                  :on-click (when-not (:premium? org-data)
                              #(do
                                 (du/prevent-default! %)
                                 (nav-actions/toggle-premium-picker! download-csv-tooltip)))
                  :download (when (:premium? org-data)
                              (wu/csv-filename activity-data))
                  :data-toggle (when-not is-mobile? "tooltip")
                  :data-placement "top"
                  :data-container "body"
                  :title (if (:premium? org-data)
                          (str "Download analytics data in excel compatible format.")
                          premium-download-csv-tooltip)}
                "Download CSV"]])
            [:div.wrt-popup-list
              (for [u users-list
                    :let [user-sending-notice (get @(::sending-notice s) (:user-id u))
                          is-self-user?       (= (:user-id current-user-data) (:user-id u))]]
                [:div.wrt-popup-list-row
                  {:key (str "wrt-popup-row-" (:user-id u))
                  :class (utils/class-set {:seen (and (:seen u) (= @list-view :all))
                                           :sent user-sending-notice})}
                  [:div.wrt-popup-list-row-avatar
                    {:class (when (:seen u) "seen")}
                    (user-avatar-image u)]
                  [:div.wrt-popup-list-row-name
                    (:name u)
                    (when (and (:premium? org-data)
                               is-self-user?)
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
                      {:on-click #(if (:premium? org-data)
                                    (remind-to s {:activity-data activity-data
                                                  :current-user-data current-user-data
                                                  :users-list [u]
                                                  :slack-bot-data slack-bot-data})
                                    (nav-actions/toggle-premium-picker! premium-remind-tooltip))
                       :data-toggle (when (and (not (:premium? org-data))
                                               (not is-mobile?))
                                      "tooltip")
                       :data-placement "top"
                       :title remind-tooltip}
                      "Remind"])])]])]]))

(rum/defc wrt-count < rum/static
  [{:keys [activity-data read-data]}]
  (let [item-id (:uuid activity-data)
        is-author? (:publisher? activity-data)
        reads-count (if (and is-author?
                             (:last-read-at activity-data))
                     (dec (:count read-data))
                     (:count read-data))
        is-mobile? (responsive/is-tablet-or-mobile?)]
    (when (map? read-data)
      [:div.wrt-count-container
        {:data-toggle (when-not is-mobile? "tooltip")
         :data-placement "top"
         :data-container "body"
         :title (cond
                  (= reads-count 1)
                  "1 person viewed"
                  (pos? reads-count)
                  (str reads-count " people viewed")
                  :else
                  "No views yet")}
        [:div.wrt-count
          {:ref :wrt-count
           :on-click #(nav-actions/show-wrt item-id)}
          reads-count]])))
