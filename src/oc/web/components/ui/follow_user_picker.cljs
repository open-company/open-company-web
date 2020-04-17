(ns oc.web.components.ui.follow-user-picker
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
  (compare-and-set! (::selecting-multiple s) false true)
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
      (search-user user q)
      (some (partial search-user user) (string/split q #"\s"))))

(defn- filter-sort-users [s current-user-id users q]
  (sort-users current-user-id (filterv #(filter-user s % (string/lower q)) users)))

(defn- follow! [s]
  (reset! (::saving s) true)
  (user-actions/follow-publishers @(::users s))
  (nav-actions/close-all-panels))

(defn- toggle-user-and-exit [s user]
  (let [users (swap! (::users s)
               #(if (utils/in? % (:user-id user))
                 (disj % (:user-id user))
                 (conj % (:user-id user))))]
    (follow! s)))

(defn- close-follow-user-picker [s]
  (if (not= @(::initial-users s) @(::users s))
    (let [alert-data {:icon "/img/ML/trash.svg"
                      :action "follow-user-picker-unsaved-exit"
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

(rum/defcs follow-user-picker < rum/reactive

 (drv/drv :org-data)
 (drv/drv :active-users)
 (drv/drv :follow-publishers-list)
 (drv/drv :current-user-data)
 (rum/local #{} ::users)
 (rum/local #{} ::initial-users)
 (rum/local "" ::query)
 (rum/local false ::saving)
 (rum/local false ::selecting-multiple)
 strict-refresh-tooltips-mixin
 {:init (fn [s]
   ;; Refresh the following list
   (user-actions/load-follow-list)
   s)
  :will-mount (fn [s]
   ;; setup the currently followed users
   (let [users (set (map :user-id @(drv/get-ref s :follow-publishers-list)))]
     (reset! (::users s) users)
     (reset! (::initial-users s) users))
   s)}

  [s]
  (let [org-data (drv/react s :org-data)
        follow-publishers-list (drv/react s :follow-publishers-list)
        current-user-data (drv/react s :current-user-data)
        all-active-users (drv/react s :active-users)
        authors-uuids (->> org-data :authors (map :user-id) set)
        all-authors (filter #(and (authors-uuids (:user-id %))
                                  (not= (:user-id current-user-data) (:user-id %)))
                     (vals all-active-users))
        sorted-users (filter-sort-users s (:user-id current-user-data) all-authors @(::query s))
        is-mobile? (responsive/is-mobile-size?)]
    [:div.follow-user-picker
      [:div.follow-user-picker-modal
        ; [:div.follow-user-picker-header]
        [:button.mlb-reset.modal-close-bt
          {:on-click #(close-follow-user-picker s)}]
        [:div.follow-user-picker-body
          [:h3.follow-user-picker-title
            "People"]
          [:div.follow-user-picker-subtitle
            "Select someone to follow their posts and comments more easily."]
          (if (zero? (count all-authors))
            [:div.follow-user-picker-empty-users
              [:div.follow-user-picker-empty-icon]
              [:div.follow-user-picker-empty-copy
                "There are no active team members yet. "
                [:button.mlb-reset.follow-user-picker-empty-invite-bt
                  {:on-click #(nav-actions/show-org-settings :invite-picker)}
                  "Invite your team"]
                " to get started."]]
            [:div.follow-user-picker-body-inner.group
              [:input.follow-user-picker-search-field-input.oc-input
                {:class (when-not (seq @(::users s)) "empty")
                 :value @(::query s)
                 :type "text"
                 :ref :query
                 :placeholder "Search for teammates or make your selection below..."
                 :on-change #(reset! (::query s) (.. % -target -value))}]
              [:div.follow-user-picker-users-list.group
                (for [u sorted-users
                      :let [selected? (utils/in? @(::users s) (:user-id u))]]
                  [:div.follow-user-picker-user-row.group
                    {:key (str "follow-user-picker-" (:user-id u))
                     :class (when (utils/in? @(::users s) (:user-id u)) "selected")}
                    (carrot-checkbox {:selected selected?
                                      :did-change-cb #(toggle-user s u)})
                    [:button.mlb-reset.follow-user-bt
                      {:on-click #(if @(::selecting-multiple s)
                                    (toggle-user s u)
                                    (toggle-user-and-exit s u))}
                      (user-avatar-image u)
                      [:span.follow-user-picker-user
                        (:name u)]]])]
              [:div.follow-user-picker-footer.group
                [:button.mlb-reset.invite-user-bt
                  {:on-click #(nav-actions/show-section-add)}
                  "Invite people"]
                [:button.mlb-reset.follow-user-picker-create-bt
                  {:on-click #(follow! s)
                   :disabled (or (= @(::users s) @(::initial-users s))
                                 @(::saving s))}
                  "Save"]]])]]]))
