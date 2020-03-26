(ns oc.web.components.direct-picker
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.lib.user :as user-lib]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.section :as section-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.notifications :as notification-actions]
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
  (or (not (seq q))
      (and (not (utils/in? @(::users s) (:user-id user)))
           (or (search-user user q)
               (some (partial search-user user)
               (string/split q #"\s"))))))

(defn- filter-sort-users [s current-user-id users q]
  (sort-users current-user-id (filterv #(filter-user s % q) users)))

(defn- create-section [s]
  (reset! (::saving s) true)
  (let [all-users (:users @(drv/get-ref s :team-roster))
        current-user-data @(drv/get-ref s :current-user-data)
        users (concat [current-user-data] (mapv #(some (fn [user] (when (= % (:user-id user)) user)) all-users) @(::users s)))
        direct-name (str (clojure.string/join ", " (mapv user-lib/name-for (butlast users))) " and " (user-lib/name-for (last users)))]
    (section-actions/section-save
      {:name direct-name
       :access :private
       :authors (vec @(::users s))
       :direct true}
      ""
      (fn [board-data]
        (reset! (::saving s) false)
        (notification-actions/show-notification {:title "Direct created"
                                                 :description (str "Direct with " direct-name " creation succeded.")
                                                 :dismiss true
                                                 :expire 3
                                                 :id :direct-create-success})
        (nav-actions/nav-to-url! nil (:slug board-data) (oc-urls/board (router/current-org-slug) (:slug board-data))))
      (fn []
       (reset! (::saving s) false)
       (notification-actions/show-notification {:title "An error occurred"
                                                :description "Please try again"
                                                :dismiss true
                                                :expire 3
                                                :id :direct-create-error})))))

(rum/defcs direct-picker < rum/reactive
                           (drv/drv :editable-boards)
                           (drv/drv :team-roster)
                           (drv/drv :current-user-data)
                           (rum/local #{} ::users)
                           (rum/local "" ::query)
                           (rum/local false ::saving)
                           (rum/local nil ::existing-board)
  [s]
  (let [roster (drv/react s :team-roster)
        editable-boards (drv/react s :editable-boards)
        current-user-data (drv/react s :current-user-data)
        active-users (filterv #(and (#{"active" "unverified"} (:status %))
                                    (not= (:user-id current-user-data) (:user-id %))) (:users roster))
        sorted-users (filter-sort-users s (:user-id current-user-data) active-users @(::query s))
        existing-board (some #(when (and (:direct %)
                                         (= (set (:authors %)) @(::users s)))
                                %)
                        editable-boards)]
    [:div.direct-picker
      [:h3.direct-picker-title
        "Direct messages"]
      [:div.direct-picker-subtitle
        "Send messages privately to a person or a group."]
      [:div.direct-picker-selected-users
        [:div.direct-picker-selected-users-list
          {:on-click #(when-let [q-el (rum/ref-node s :query)]
                        (when-not (utils/event-inside? % q-el)
                          (.focus q-el)))}
          (for [user-id @(::users s)
                :let [u (some #(when (= (:user-id %) user-id) %) (:users roster))]]
            [:button.mlb-reset.direct-picker-selected-user
              {:on-click #(swap! (::users s) disj user-id)
               :key (str "direct-picker-selected-" user-id "-" (rand 100))}
              (user-avatar-image u)
              [:span.direct-picker-selected-user-name
                (user-lib/name-for u)]])
          (when (not= (count @(::users s)) (count active-users))
            [:input.direct-picker-search-field-input
              {:class (when-not (seq @(::users s)) "empty")
               :value @(::query s)
               :type "text"
               :ref :query
               :placeholder (if (seq @(::users s))
                              "Search for teammates..."
                              "Search for teammates or make your selection below...")
               :on-change #(reset! (::query s) (.. % -target -value))}])]
        [:button.mlb-reset.direct-picker-create-bt
          {:class (when-not (seq @(::users s)))
           :on-click #(if existing-board
                        (nav-actions/nav-to-url! % (:slug existing-board) (oc-urls/board (router/current-org-slug) (:slug existing-board)))
                        (create-section s))
           :disabled (or (not (<= 1 (count @(::users s)) 5))
                         @(::saving s))}
          (if existing-board
            "Open"
            "Create")]]
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