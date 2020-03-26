(ns oc.web.components.direct-picker
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.lib.user :as user-lib]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)]))

(defn- sort-users [user-id users]
  (let [{:keys [self-user other-users]}
         (group-by #(if (= (:user-id %) user-id) :self-user :other-users) users)
        sorted-other-users (sort-by user-lib/name-for other-users)]
    (remove nil? (concat self-user sorted-other-users))))

(defn- toggle-user [s u]
  (swap! (::users s)
   #(if (utils/in? % (:user-id u))
     (disj % (:user-id u))
     (conj % (:user-id u)))))

(defn- search-string [v q]
  (-> v string/lower (string/includes? q)))

(defn- search-user [user q]
  (or (-> user :first-name (search-string q))
      (-> user :last-name (search-string q))
      (-> user :name (search-string q))
      (-> user :email (search-string q))
      (-> user :title (search-string q))))

(defn- filter-user [s user q]
  (and (#{"active" "unverified"} (:status user))
       (or (not (seq q))
           (and (not (utils/in? @(::users s) (:user-id user)))
                (or (search-user user q)
                    (some (partial search-user user)
                     (string/split q #"\s")))))))

(defn- filter-sort-users [s current-user-id users q]
  (sort-users current-user-id (filterv #(filter-user s % q) users)))

(rum/defcs direct-picker < rum/reactive
                           (drv/drv :team-roster)
                           (drv/drv :current-user-data)
                           (rum/local #{} ::users)
                           (rum/local "" ::query)
  [s]
  (let [roster (drv/react s :team-roster)
        current-user-data (drv/react s :current-user-data)
        sorted-users (filter-sort-users s (:user-id current-user-data) (:users roster) @(::query s))]
    [:div.direct-picker
      [:h3.direct-picker-title
        "Direct messages"]
      [:div.direct-picker-subtitle
        "Send messages privately to a person or a group."]
      [:div.direct-picker-selected-users.group
        [:div.direct-picker-selected-users-list
          {:on-click #(when-let [q-el (rum/ref-node s :query)]
                        (when-not (utils/event-inside? % q-el)
                          (.focus q-el)))}
          (for [user-id @(::users s)
                :let [u (some #(when (= (:user-id %) user-id) %) (:users roster))]]
            [:div.direct-picker-user.group
              {:key (str "direct-picker-selected-" user-id "-" (rand 100))}
              (user-avatar-image u)
              [:span.direct-picker-user-name
                (user-lib/name-for u)]
              [:button.mlb-reset.remove-user-bt
                {:on-click #(do
                              (utils/event-stop %)
                              (swap! (::users s) disj (:user-id u)))}]])
          [:input.direct-picker-search-field-input
            {:class (when-not (seq @(::users s)) "empty")
             :value @(::query s)
             :type "text"
             :ref :query
             :placeholder (if (seq @(::users s))
                            ""
                            "Search for teammates or make your selection below...")
             :on-change #(reset! (::query s) (.. % -target -value))}]]
        [:button.mlb-reset.direct-picker-create-bt
          {:class (when-not (seq @(::users s)))}
          "Create"]]
      [:div.direct-picker-users-list
        (for [u sorted-users
              :let [selected? (utils/in? @(::users s) (:user-id u))]]
          [:button.mlb-reset.direct-picker-user-row.group
            {:key (str "direct-picker-" (:user-id u))
             :class (when (utils/in? @(::users s) (:user-id u)) "selected")
             :on-click #(do
                          (toggle-user s u)
                          (utils/event-stop %))}
            (carrot-checkbox {:selected selected?
                              :did-change-cb #(toggle-user s u)})
            (user-avatar-image u)
            [:span.direct-picker-user
              (user-lib/name-for u)]])]]))