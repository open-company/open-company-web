(ns oc.web.components.ui.slack-users-dropdown
  (:require [rum.core :as rum]
            [dommy.core :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as string]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.team :as team-actions]
            [oc.web.mixins.ui :refer (on-window-click-mixin)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn check-user [user s]
  (or (string/includes? (string/lower (:name user)) s)
      (string/includes? (string/lower (:first-name user)) s)
      (string/includes? (string/lower (:last-name user)) s)
      (string/includes? (string/lower (:email user)) s)))

(defn filter-users [users s]
  (let [look-for (string/lower s)]
    (filterv #(check-user % look-for) users)))

(defn- setup-sorted-users
  "Read the roster users, group them by slack org and sort by slack org and name/email.
   Save the result in the local component state to be used during render."
  [s]
  (let [roster-data @(drv/get-ref s :team-roster)
        all-users (filterv #(= (:status %) "uninvited") (:users roster-data))
        team-roster (vals (group-by :slack-org-id all-users))
        sorted-team-roster (map
                              (fn [team]
                                (sort-by #(str (:first-name %) " " (:last-name %)) team)) team-roster)
        all-sorted-users (vec (apply concat sorted-team-roster))]
    (when (not= @(::all-sorted-users s) all-sorted-users)
      (reset! (::all-sorted-users s) all-sorted-users))))

(defn- get-filtered-sorted-users
  "Filter ::all-sorted-users on the fly with the passed filter-fn if present."
  [s]
  (let [filter-fn (:filter-fn (first (:rum/args s)))
        all-sorted-users @(::all-sorted-users s)]
    (if (fn? filter-fn)
      (filter filter-fn all-sorted-users)
      all-sorted-users)))

(rum/defcs slack-users-dropdown   <  (rum/local nil ::show-users-dropdown)
                                     (rum/local nil ::field-value)
                                     (rum/local "" ::slack-user)
                                     (rum/local false ::typing)
                                     (rum/local [] ::all-sorted-users)
                                     rum/reactive
                                     (on-window-click-mixin (fn [s e]
                                      (when (and @(::show-users-dropdown s)
                                                ; (not (utils/event-inside? e
                                                ;       (sel1 [:div.board-edit-slack-channels-dropdown])))
                                                (not (utils/event-inside? e
                                                      (.-parentElement (sel1 [:input.slack-users-dropdown])))))
                                       (reset! (::show-users-dropdown s) false)
                                       (let [{:keys [on-blur]} (first (:rum/args s))]
                                         (when (fn? on-blur)
                                           (on-blur))))))
                                     (drv/drv :team-data)
                                     (drv/drv :team-roster)
                                     {:will-mount (fn [s]
                                       (let [initial-value (:initial-value (first (:rum/args s)))]
                                         (reset! (::slack-user s) (or initial-value "")))
                                       (setup-sorted-users s)
                                       s)
                                      :will-update (fn [s]
                                       (setup-sorted-users s)
                                       s)
                                      :before-render (fn [s]
                                       (team-actions/teams-get-if-needed)
                                       s)}
  [s {:keys [disabled initial-value on-change on-intermediate-change on-focus on-blur filter-fn] :as data}]
  (let [_ (drv/react s :team-roster) ; Make sure the component is re-rendered when roster changes
        all-sorted-users (get-filtered-sorted-users s)
        slack-orgs (:slack-orgs (drv/react s :team-data))
        slack-orgs-map (zipmap (map :slack-org-id slack-orgs) (map :name slack-orgs))]
    [:div.slack-users-dropdown
      {:class (if disabled "disabled" "")
       :key (str "slack-users-dropdown-" (count all-sorted-users))}
      [:input.slack-users-dropdown
        {:value @(::slack-user s)
         :on-focus (fn []
                      (when (fn? on-focus) (on-focus))
                      (utils/after
                       100
                       #(do (reset! (::typing s) false) (reset! (::show-users-dropdown s) true))))
         :on-blur #(when (fn? on-blur) (on-blur))
         :on-change #(do
                       (reset! (::typing s) true)
                       (when (fn? on-intermediate-change)
                         (on-intermediate-change (.. % -target -value)))
                       (reset! (::slack-user s) (.. % -target -value)))
         :disabled disabled
         :placeholder (if (pos? (count all-sorted-users)) "Select a person to invite..." "No more members to add")}]
      (when-not disabled
        [:div.arrows
          {:on-click #(when-not disabled
                        (let [next-value (not @(::show-users-dropdown s))]
                          (reset! (::typing s) false)
                          (reset! (::show-users-dropdown s) next-value)
                          (when (and next-value
                                     (fn? on-focus))
                            (on-focus))
                          (when (and (not next-value)
                                     (fn? on-blur))
                            (on-blur)))
                        (utils/event-stop %))}])
      (when @(::show-users-dropdown s)
        [:div.slack-users-dropdown-list
          (for [user (if (and @(::typing s) @(::slack-user s))
                        (filter-users all-sorted-users @(::slack-user s))
                        all-sorted-users)]
            [:div.user.group
              {:key (str "slack-users-dd-" (:slack-org-id user) "-" (:slack-id user))
               :on-click #(do
                           (on-change user)
                           (reset! (::slack-user s) (utils/name-or-email user))
                           (reset! (::show-users-dropdown s) false)
                           (when (fn? on-blur)
                              (on-blur))
                           (reset! (::typing s) false))}
              (user-avatar-image user)
              [:div.user-name
                (utils/name-or-email user)
                [:span.slack-org (get slack-orgs-map (:slack-org-id user))]]])])]))