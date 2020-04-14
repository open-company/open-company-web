(ns oc.web.components.ui.follow-picker
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :refer (strict-refresh-tooltips-mixin)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]
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
  (sort-users current-user-id (filterv #(filter-user s % (string/lower q)) users)))

(defn- follow! [s]
  (reset! (::saving s) true)
  (user-actions/follow-publishers @(::users s))
  (nav-actions/close-all-panels))

(defn- close-follow-picker [s]
  (if (not= @(::initial-users s) @(::users s))
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

 (drv/drv :org-data)
 (drv/drv :active-users)
 (drv/drv :publishers-list)
 (drv/drv :current-user-data)
 (rum/local #{} ::users)
 (rum/local #{} ::initial-users)
 (rum/local "" ::query)
 (rum/local false ::saving)
 (rum/local nil ::existing-board)
 strict-refresh-tooltips-mixin
 {:init (fn [s]
   ;; Refresh the following list
   (user-actions/load-publishers-list)
   s)
  :will-mount (fn [s]
   ;; setup the currently followed users
   (let [users (set (map :user-id @(drv/get-ref s :publishers-list)))]
     (reset! (::users s) users)
     (reset! (::initial-users s) users))
   s)
  :did-mount (fn [s]
   (when-let [query-field (rum/ref-node s :query)]
     (.focus query-field))
   s)}

  [s]
  (let [org-data (drv/react s :org-data)
        publishers-list (drv/react s :publishers-list)
        current-user-data (drv/react s :current-user-data)
        all-active-users (drv/react s :active-users)
        authors-uuids (->> org-data :authors (map :user-id) set)
        all-authors (filter #(and (authors-uuids (:user-id %))
                                  (not= (:user-id current-user-data) (:user-id %)))
                     (vals all-active-users))
        sorted-users (filter-sort-users s (:user-id current-user-data) all-authors @(::query s))
        is-mobile? (responsive/is-mobile-size?)]
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
          (if (zero? (count all-authors))
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
                  {:on-click #(follow! s)
                   :disabled (or (= @(::users s) @(::initial-users s))
                                 @(::saving s))
                   :data-toggle (when-not is-mobile? "tooltip")
                   :data-placement "top"
                   :title "Save & close"}
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
                      (:name u)]])

                (for [u sorted-users
                      :let [selected? (utils/in? @(::users s) (:user-id u))]]
                  [:button.mlb-reset.follow-picker-user-row.group
                    {:key (str "follow-picker-1" (:user-id u))
                     :class (when (utils/in? @(::users s) (:user-id u)) "selected")
                     :on-click #(do
                                  (toggle-user s u)
                                  (utils/event-stop %))}
                    (carrot-checkbox {:selected selected?})
                    (user-avatar-image u)
                    [:span.follow-picker-user
                      (:name u)]])

                (for [u sorted-users
                      :let [selected? (utils/in? @(::users s) (:user-id u))]]
                  [:button.mlb-reset.follow-picker-user-row.group
                    {:key (str "follow-picker-2" (:user-id u))
                     :class (when (utils/in? @(::users s) (:user-id u)) "selected")
                     :on-click #(do
                                  (toggle-user s u)
                                  (utils/event-stop %))}
                    (carrot-checkbox {:selected selected?})
                    (user-avatar-image u)
                    [:span.follow-picker-user
                      (:name u)]])

                (for [u sorted-users
                      :let [selected? (utils/in? @(::users s) (:user-id u))]]
                  [:button.mlb-reset.follow-picker-user-row.group
                    {:key (str "follow-picker-3" (:user-id u))
                     :class (when (utils/in? @(::users s) (:user-id u)) "selected")
                     :on-click #(do
                                  (toggle-user s u)
                                  (utils/event-stop %))}
                    (carrot-checkbox {:selected selected?})
                    (user-avatar-image u)
                    [:span.follow-picker-user
                      (:name u)]])]])]]]))
