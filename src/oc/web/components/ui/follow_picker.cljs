(ns oc.web.components.ui.follow-picker
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.section :as section-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)]))

(defn- sort-users [user-id users]
  (let [{:keys [self-user other-users]}
         (group-by #(if (= (:user-id %) user-id) :self-user :other-users) users)
        sorted-other-users (sort-by :short-name other-users)]
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
  (sort-users current-user-id (filterv #(filter-user s % (string/lower q)) (vals users))))

(defn- create-section [s]
  (reset! (::saving s) true)
  )

(defn- close-follow-picker [s]
  (if (seq @(::users s))
    (let [alert-data {:icon "/img/ML/trash.svg"
                      :action "follow-picker-unsaved-exit"
                      :message "Leave without saving your changes?"
                      :link-button-title "Stay"
                      :link-button-cb #(alert-modal/hide-alert)
                      :solid-button-style :red
                      :solid-button-title "Lose changes"
                      :solid-button-cb #(do
                                          (alert-modal/hide-alert)
                                          (nav-actions/close-all-panels))}]
      (alert-modal/show-alert alert-data))
    (nav-actions/close-all-panels)))

(rum/defcs follow-picker < rum/reactive
                           (drv/drv :editable-boards)
                           (drv/drv :active-users)
                           (drv/drv :current-user-data)
                           (rum/local #{} ::users)
                           (rum/local "" ::query)
                           (rum/local false ::saving)
                           (rum/local nil ::existing-board)
                           {:did-mount (fn [s]
                            (when-let [query-field (rum/ref-node s :query)]
                              (.focus query-field))
                            s)}
  [s]
  (let [editable-boards (vec (vals (drv/react s :editable-boards)))
        current-user-data (drv/react s :current-user-data)
        all-active-users (drv/react s :active-users)
        active-users (into {} (filter #(not= (:user-id current-user-data) (first %)) all-active-users))
        sorted-users (filter-sort-users s (:user-id current-user-data) active-users @(::query s))]
    [:div.follow-picker
      [:div.follow-picker-modal
        ; [:div.follow-picker-header]
        [:button.mlb-reset.modal-close-bt
          {:on-click #(close-follow-picker s)}]
        [:div.follow-picker-body
          [:h3.follow-picker-title
            "People"]
          [:div.follow-picker-subtitle
            "Select someone to follow their posts and comments more easily."]
          (if (zero? (count all-active-users))
            [:div.follow-picker-empty-users
              [:div.follow-picker-empty-icon]
              [:div.follow-picker-empty-copy
                "There are no active team members yet. "
                [:button.mlb-reset.follow-picker-empty-invite-bt
                  {:on-click #(nav-actions/show-org-settings :invite-picker)}
                  "Invite your team"]
                " to get started."]]
            [:div.follow-picker-body-inner
              [:div.follow-picker-selected-users
                [:input.follow-picker-search-field-input.oc-input
                  {:class (when-not (seq @(::users s)) "empty")
                   :value @(::query s)
                   :type "text"
                   :ref :query
                   :placeholder "Search for teammates or make your selection below..."
                   :on-change #(reset! (::query s) (.. % -target -value))}]
                [:button.mlb-reset.follow-picker-create-bt
                  {:class (when-not (seq @(::users s)))
                   :on-click #(create-section s)
                   :disabled (or (not (> (count @(::users s)) 0))
                                 @(::saving s))}
                  "Follow"]]
              [:div.follow-picker-users-list
                (for [u sorted-users
                      :let [selected? (utils/in? @(::users s) (:user-id u))]]
                  [:button.mlb-reset.follow-picker-user-row.group
                    {:key (str "follow-picker-" (:user-id u))
                     :class (when (utils/in? @(::users s) (:user-id u)) "selected")
                     :on-click #(do
                                  (toggle-user s u)
                                  (utils/event-stop %))}
                    (carrot-checkbox {:selected selected?})
                    (user-avatar-image u)
                    [:span.follow-picker-user
                      (:name u)]])]])]]])) 