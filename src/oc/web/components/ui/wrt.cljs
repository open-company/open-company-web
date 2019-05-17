(ns oc.web.components.ui.wrt
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.actions.notifications :as notifications-actions]
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

                 mixins/no-scroll-mixin

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
        is-mobile? (responsive/is-tablet-or-mobile?)]
    [:div.wrt-popup-container
       {:on-click nav-actions/hide-wrt}
      [:button.mlb-reset.modal-close-bt
        {:on-click nav-actions/hide-wrt}]
      [:div.wrt-popup
        {:class (utils/class-set {:loading (not (:reads read-data))})
         :on-click #(.stopPropagation %)}
        [:div.wrt-popup-header
          [:div.wrt-popup-header-title
            "Who viewed this post"]]
        ;; Show a spinner on mobile if no data is loaded yet
        (if-not (:reads read-data)
          (small-loading)
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
                    (nav-actions/show-wrt item-id))
         :class (when (pos? (count (:reads read-data))) "has-read-list")}
        (if read-count
          (str read-count " Viewer" (when (not= read-count 1) "s"))
          "0 Viewers")]]))