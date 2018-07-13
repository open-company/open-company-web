(ns oc.web.components.ui.wrt
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.section :as su]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.actions.notifications :as notifications-actions]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn filter-by-query [user query]
  (or (string/includes? (string/lower (:email user)) query)
      (string/includes? (string/lower (:name user)) query)
      (string/includes? (string/lower (:first-name user)) query)
      (string/includes? (string/lower (:last-name user)) query)))

(defn under-middle-screen? [el]
  (let [el-offset-top (aget (.offset (js/$ el)) "top")
        fixed-top-position (- el-offset-top (.-scrollTop (.-scrollingElement js/document)))
        win-height (.-innerHeight js/window)]
    (>= fixed-top-position (/ win-height 2))))

(defn calc-left-position [el]
  (let [el-offset-left (aget (.offset (js/$ el)) "left")
        win-width (.-innerWidth js/window)]
    (if (> (+ el-offset-left 360) (- win-width 40))
      (- (- win-width 40) (+ el-offset-left 360))
      0)))

(def default-appear-delay 30)
(def default-disappear-delay 500)

(rum/defcs wrt < rum/reactive
                 ;; Derivatives
                 (drv/drv :team-roster)
                 ;; Locals
                 (rum/local "" ::query)
                 (rum/local false ::showing-popup)
                 (rum/local false ::under-middle-screen)
                 (rum/local 0 ::left-position)
                 (rum/local :seen ::list-view)
                 (rum/local nil ::show-delay)

                 {:after-render (fn [s] (.tooltip (js/$ "[data-toggle=\"tooltip\"]")) s)}

  [s activity-data read-data]
  (let [item-id (:uuid activity-data)
        team-roster (drv/react s :team-roster)
        team-users (filter #(= (:status %) "active") (:users team-roster))
        seen-users (into [] (sort-by utils/name-or-email (map #(assoc % :seen true) (:reads read-data))))
        seen-ids (set (map :user-id seen-users))
        all-ids (set (map :user-id team-users))
        unseen-ids (clojure.set/difference all-ids seen-ids)
        unseen-users (into [] (sort-by utils/name-or-email (map (fn [user-id] (first (filter #(= (:user-id %) user-id) team-users))) unseen-ids)))
        all-users (sort-by utils/name-or-email (concat seen-users unseen-users))
        read-count (:count read-data)
        query (::query s)
        lower-query (string/lower @query)
        list-view (::list-view s)
        unsorted-list (if (= @list-view :seen)
                        seen-users
                        unseen-users)
        filtered-users (if (seq @query)
                         (filterv #(filter-by-query % (string/lower @query)) unsorted-list)
                         unsorted-list)
        sorted-filtered-users (sort-by utils/name-or-email filtered-users)
        is-mobile? (responsive/is-tablet-or-mobile?)]
    [:div.wrt-container
      {:class (when seen-users "has-read-list")
       :on-mouse-over #(when (and (not is-mobile?)
                                  (not (:reads read-data)))
                        (su/request-reads-data item-id))
       :on-mouse-enter (fn [_]
                         (when-not is-mobile?
                           (reset! (::under-middle-screen s) (under-middle-screen? (rum/ref-node s :wrt-count)))
                           (reset! (::left-position s) (calc-left-position (rum/ref-node s :wrt-count)))
                           (if @(::show-delay s)
                             (do
                               (.clearInterval js/window @(::show-delay s))
                               (reset! (::show-delay s) nil))
                             (reset! (::show-delay s)
                              (utils/after default-appear-delay
                               #(do
                                 (reset! (::show-delay s) false)
                                 (reset! (::showing-popup s) true)))))))
       :on-mouse-leave (fn [_]
                         (if @(::show-delay s)
                          (do
                            (.clearInterval js/window @(::show-delay s))
                            (reset! (::show-delay s) nil))
                          (reset! (::show-delay s)
                           (utils/after default-disappear-delay
                            #(do
                              (reset! (::show-delay s) nil)
                              (reset! (::list-view s) :seen)
                              (reset! (::showing-popup s) false))))))}
      [:div.wrt-count
        {:ref :wrt-count}
        (if read-count
          (str read-count " view" (when (not= read-count 1) "s"))
          "Views")]
      (when (and @(::showing-popup s)
                 (:reads read-data))
        [:div.wrt-popup
          {:class (when @(::under-middle-screen s) "top")
           :style {:left (str @(::left-position s) "px")}}
          [:div.wrt-popup-title
            "Who saw this"]
          [:input.wrt-popup-query
            {:value @query
             :type "text"
             :placeholder "Find a person..."
             :on-change #(reset! query (.. % -target -value))}]
          [:div.wrt-popup-tabs
            [:button.mlb-reset.wrt-popup-tab.viewed
              {:class (when (= @list-view :seen) "active")
               :on-click #(reset! list-view :seen)}
              "Viewed"]
            [:button.mlb-reset.wrt-popup-tab.unseen
              {:class (when (= @list-view :unseen) "active")
               :on-click #(reset! list-view :unseen)}
              "NOT SEEN"]]
          [:div.wrt-popup-list
            (if (pos? (count sorted-filtered-users))
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
                      [:button.mlb-reset.button.send-reminder-bt
                        {:data-toggle "tooltip"
                         :data-placement "top"
                         :data-container "body"
                         :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
                         :title "Send a reminder"
                         :on-click #(let [email-share {:medium :email
                                                       :note "When you have a moment, please check out this post."
                                                       :subject (str "Just a reminder: " (:headline activity-data))
                                                       :to [(:email u)]}]
                                      ;; Hide the WRT popup
                                      (reset! (::showing-popup s) false)
                                      ;; Show the share popup
                                      (activity-actions/activity-share activity-data [email-share]
                                       (fn []
                                        (notifications-actions/show-notification
                                         {:title (str "Reminder sent to " (utils/name-or-email u) ".")
                                          :id :wrt-remind-default
                                          :dismiss true
                                          :expire 5}))))}])]])
              (when-not (seq @query)
                [:div.wrt-popup-list-row.empty-list
                  {:class (if (= @list-view :seen) "viewed" "unseen")}
                  (if (= @list-view :seen)
                    [:div.empty-copy.no-viewed "No one has seen this post yet…"]
                    [:div.empty-copy.no-unseen "Everyone has seen this post!"])]))]])]))