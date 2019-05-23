(ns oc.web.components.ui.wrt
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
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
        sorted-other-users (sort-by utils/name-or-email other-users)]
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

                 {:after-render (fn [s]
                   (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
                   (when @(::search-active s)
                      (when (compare-and-set! (::search-focused s) false true)
                        (.focus (rum/ref-node s :search-field))))
                   s)}

  [s]
  (let [activity-data (drv/react s :wrt-activity-data)
        read-data (drv/react s :wrt-read-data)
        item-id (:uuid activity-data)
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
        seen-percent (int (* (/ (count seen-users) (count all-users)) 100))]
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
                (case (count seen-users)
                  (count all-users)
                  "👏 Everyone has seen this post!"
                  1
                  "1 person has viewed this post."
                  0
                  "No one has viewed this post."
                  ; else
                  (str (count seen-users)
                   " of "
                   (count all-users)
                   " people viewed this "
                   (when (:private-access? read-data)
                     "private ")
                   "post."))
                (when (:private-access? read-data)
                  [:button.mlb-reset.manage-section-bt
                    {:on-click #(nav-actions/show-section-editor)}
                    "Manage section members?"])]]
            [:div.wrt-popup-tabs
              {:ref :wrt-pop-up-tabs}
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
              (for [u sorted-filtered-users
                    :let [user-sending-notice (get @(::sending-notice s) (:user-id u))
                          is-self-user?       (= (:user-id current-user-data) (:user-id u))]]
                [:div.wrt-popup-list-row
                  {:key (str "wrt-popup-row-" (:user-id u))
                   :class (when (:seen u) "seen")}
                  [:div.wrt-popup-list-row-avatar
                    {:class (when (:seen u) "seen")}
                    (user-avatar-image u)]
                  [:div.wrt-popup-list-row-name
                    (utils/name-or-email u)
                    (when is-self-user?
                      " (you)")]
                  [:div.wrt-popup-list-row-seen
                    (if (:seen u)
                      ;; Show time the read happened
                      (str "Viewed " (string/lower (utils/time-since (:read-at u))))
                      (if (and user-sending-notice
                               (not= user-sending-notice :loading))
                        user-sending-notice
                        "Unopened"))]
                  ;; Send reminder button
                  (when (and (not (:seen u))
                             (not user-sending-notice))
                    [:button.mlb-reset.send-reminder-bt
                      {:on-click (fn [_]
                                   (let [wrt-share {:note "When you have a moment, please check out this post."
                                                    :subject (str "You may have missed: " (:headline activity-data))
                                                    :user-id (:user-id u)}]
                                     (swap! (::sending-notice s) assoc (:user-id u) :loading)
                                     ;; Show the share popup
                                     (activity-actions/activity-share activity-data [wrt-share]
                                      (fn [{:keys [body]}]
                                        (let [resp (first body)
                                              medium (:medium resp)
                                              slack-org (when (= medium "slack")
                                                          (:slack-org-id (:channel resp)))
                                              slack-user (when slack-org
                                                           (get (:slack-users u) (keyword slack-org)))
                                              user-label (if (= medium "email")
                                                           (str "Sent to: " (first (:to resp)))
                                                           (if (and slack-user
                                                                    (seq (:display-name slack-user))
                                                                    (not= (:display-name slack-user) "-"))
                                                             (str "Sent to: @" (:display-name slack-user))
                                                             (str "Sent via Slack")))]
                                          (swap! (::sending-notice s) assoc (:user-id u) user-label)
                                          (utils/after 5000 #(swap! (::sending-notice s) dissoc (:user-id u))))))))}
                      (if is-self-user?
                        "Remind me"
                        "Notify")])])]])]]))

(defn- under-middle-screen? [el]
  (let [el-offset-top (aget (.offset (js/$ el)) "top")
        fixed-top-position (- el-offset-top (.-scrollTop (.-scrollingElement js/document)))
        win-height (.-innerHeight js/window)]
    (>= fixed-top-position (/ win-height 2))))

(rum/defcs wrt-count < rum/reactive
                       ;; Derivatives
                       (drv/drv :wrt-show)

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
                    (nav-actions/show-wrt item-id))
         :class (when (pos? (count (:reads read-data))) "has-read-list")}
        (if read-count
          (str read-count " Viewer" (when (not= read-count 1) "s"))
          "0 Viewers")]]))
