(ns oc.web.components.ui.sections-picker
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

;; Private board users search helpers

(defn filter-user-by-query
  "Given a user and a query string, return true if first-name, last-name or email contains it."
  [user query]
  (let [lower-query (string/lower query)
        first-name (:first-name user)
        last-name (:last-name user)
        lower-name (string/lower (str first-name " " last-name))
        lower-email (string/lower (:email user))]
    (or (>= (.search lower-name lower-query) 0)
        (>= (.search lower-email lower-query) 0))))

(defn compare-users [user-1 user-2]
  (compare (string/lower (utils/name-or-email user-1))
           (string/lower (utils/name-or-email user-2))))

(defn filter-users
  "Given a list of users and a query string, return those that match the given query."
  [users-list query]
  (let [filtered-users (filter #(filter-user-by-query % query) users-list)]
    (sort compare-users filtered-users)))

(defn get-addable-users
  "Filter users that are not arleady viewer or author users."
  [board-editing users]
  (let [already-in-ids (concat (:viewers board-editing) (:authors board-editing))
        without-self (remove #(= (:user-id %) (jwt/user-id)) users)]
    (remove #(some #{(:user-id %)} already-in-ids) without-self)))

(defn get-section-name [group]
  (case (-> group first :access)
    "team" "TEAM"
    "public" "PUBLIC"
    "private" "PRIVATE"))

(def private-access
  [:div.access-item.private-access
    "Only team members you invite"])

(def team-access
  [:div.access-item.team-access
    "Anyone on the team"])

(def public-access
  [:div.access-item.public-access
    "Open for the public"])

(rum/defcs section-editor < rum/reactive
                            ;; Locals
                            (rum/local "" ::query)
                            (rum/local false ::show-access-list)
                            (rum/local :team ::access)
                            (rum/local false ::show-search-results)
                            (rum/local nil ::show-edit-user-dropdown)
                            (rum/local nil ::show-edit-user-top)
                            ;; Derivatives
                            (drv/drv :org-data)
                            (drv/drv :board-data)
                            (drv/drv :board-editing)
                            (drv/drv :team-data)
                            (drv/drv :team-channels)
                            (drv/drv :team-roster)
  [s]
  (let [org-data (drv/react s :org-data)
        board-editing (drv/react s :board-editing)
        board-data (if (seq (:slug board-editing)) (drv/react s :board-data) board-editing)
        team-data (drv/react s :team-data)
        roster (drv/react s :team-roster)
        current-user-id (jwt/user-id)
        ;; user can edit the private board users if
        ;; he's creating a new board
        ;; or if he's in the authors list of the existing board
        can-change (or (not (seq (:slug board-editing)))
                       (some #{current-user-id} (map :user-id (:authors board-editing))))]
    (js/console.log "show dropdown?" @(::show-edit-user-dropdown s) "top" @(::show-edit-user-top s))
    [:div.sections-picker-add
      {:on-click #(do
                    (when-not (utils/event-inside? % (rum/ref-node s "private-users-search"))
                      (reset! (::show-search-results s) false))
                    (when-not (utils/event-inside? % (rum/ref-node s "sections-picker-add-private-users"))
                      (reset! (::show-edit-user-dropdown s) nil)))}
      [:div.sections-picker-add-label
        "Add a new section"]
      [:div.sections-picker-add-name
        {:content-editable true
         :placeholder "Section name"}]
      [:div.sections-picker-add-label
        "Who can view this section?"]
      [:div.sections-picker-add-access
        {:class (when @(::show-access-list s) "expanded")
         :on-click #(reset! (::show-access-list s) (not @(::show-access-list s)))}
        (case @(::access s)
          :private private-access
          :team team-access
          :public public-access)]
      (when @(::show-access-list s)
        [:div.sections-picker-add-access-list
          [:div.access-list-row
            {:on-click #(do
                          (utils/event-stop %)
                          (reset! (::show-access-list s) false)
                          (reset! (::access s) :private))}
            private-access]
          [:div.access-list-row
            {:on-click #(do
                          (utils/event-stop %)
                          (reset! (::show-access-list s) false)
                          (reset! (::access s) :team))}
            team-access]
          [:div.access-list-row
            {:on-click #(do
                          (utils/event-stop %)
                          (reset! (::show-access-list s) false)
                          (reset! (::access s) :public))}
            public-access]])
      (when (= @(::access s) :private)
        [:div.sections-picker-add-label
          "Invite member"])
      (when (= @(::access s) :private)
        (let [query  (::query s)
              available-users (filter :user-id (:users roster))
              addable-users (get-addable-users board-editing available-users)
              filtered-users (filter-users addable-users @query)]
          (when (and can-change
                     (pos? (count addable-users)))
            [:div.sections-picker-private-users-search
              {:ref "private-users-search"}
              [:input
                {:value @query
                 :type "text"
                 :placeholder "Add a team member to this board"
                 :on-focus #(reset! (::show-search-results s) true)
                 :on-change #(let [q (.. % -target -value)]
                              (reset! (::show-search-results s) (seq q))
                              (reset! query q))}]
              (when @(::show-search-results s)
                [:div.sections-picker-private-users-results
                  (for [u filtered-users
                        :let [team-user (some #(when (= (:user-id %) (:user-id u)) %) (:users roster))
                              user (merge u team-user)
                              user-type (utils/get-user-type user org-data board-editing)]]
                    [:div.sections-picker-private-users-result
                      {:on-click #(do
                                    (reset! query "")
                                    (reset! (::show-search-results s) false)
                                    (dis/dispatch! [:private-board-user-add user user-type]))
                       :ref (str "add-user-" (:user-id user))}
                      (user-avatar-image user)
                      [:div.name
                        (utils/name-or-email user)]])])])))
      (when (= @(::access s) :private)
        [:div.sections-picker-add-label.group
          [:span.main-label
            "Section members"]
          [:span.role-header
            "Role"]])
      (when (and (= @(::access s) :private)
                 (pos? (+ (count (:authors board-editing))
                          (count (:viewers board-editing)))))
        [:div.sections-picker-add-private-users
          {:ref "sections-picker-add-private-users"}
          (let [user-id @(::show-edit-user-dropdown s)
                user-type (if (utils/in? (:viewers board-editing) user-id)
                            :viewer
                            :author)
                team-user (some #(when (= (:user-id %) user-id) %) (:users team-data))]
            [:div.sections-picker-add-private-users-dropdown-container
              {:style {:top (str (+ @(::show-edit-user-top s) -114) "px")
                       :display (if @(::show-edit-user-dropdown s) "block" "none")}}
              (dropdown-list {:items [{:value :viewer :label "Viewer"}
                                      {:value :author :label "Full Access"}
                                      {:value nil :label :divider-line}
                                      {:value :remove :label "Remove User" :color "#FA6452"}]
                              :value user-type
                              :on-change (fn [item]
                               (reset! (::show-edit-user-dropdown s) nil)
                               (if (= (:value item) :remove)
                                 (dis/dispatch! [:private-board-user-remove team-user])
                                 (dis/dispatch! [:private-board-user-add team-user (:value item)])))})])
          [:div.sections-picker-add-private-users-list.group
            {:on-scroll #(do
                          (reset! (::show-edit-user-dropdown s) nil)
                          (reset! (::show-edit-user-top s) nil))
             :ref "edit-users-scroll"}
            (let [author-ids (set (:authors board-editing))
                  viewer-ids (set (:viewers board-editing))
                  authors (filter (comp author-ids :user-id) (:users team-data))
                  viewers (filter (comp viewer-ids :user-id) (:users team-data))
                  complete-authors (map
                                    #(merge % {:type :author :display-name (utils/name-or-email %)})
                                    authors)
                  complete-viewers (map
                                    #(merge % {:type :viewer :display-name (utils/name-or-email %)})
                                    viewers)
                  all-users (concat complete-authors complete-viewers)
                  sorted-users (sort compare-users all-users)]
              (for [user sorted-users
                    :let [user-type (:type user)
                          self (= (:user-id user) current-user-id)
                          showing-dropdown (= @(::show-edit-user-dropdown s) (:user-id user))]]
                [:div.sections-picker-add-private-user.group
                  {:ref (str "edit-user-" (:user-id user))
                   :on-click #(when (and can-change
                                         (not self))
                                (let [user-node (rum/ref-node s (str "edit-user-" (:user-id user)))
                                      top (- (.-offsetTop user-node) (.-scrollTop (.-parentElement user-node)))
                                      next-user-id (if showing-dropdown
                                                     nil
                                                     (:user-id user))]
                                  (reset! (::show-edit-user-top s) top)
                                  (reset! (::show-edit-user-dropdown s) next-user-id)))}
                  (user-avatar-image user)
                  [:div.name
                    (utils/name-or-email user)]
                  (if self
                    (if (and (seq (:slug board-editing))
                             (> (count (:authors board-data)) 1))
                      [:div.user-type.remove-link
                        {:on-click (fn []
                          (let [authors (:authors board-data)
                                self-data (first (filter #(= (:user-id %) current-user-id) authors))]
                            (dis/dispatch! [:alert-modal-show
                             {:icon "/img/ML/error_icon.png"
                              :action "remove-self-user-from-private-board"
                              :message "Are you sure you want to leave this board?"
                              :link-button-title "No"
                              :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                              :solid-button-title "Yes"
                              :solid-button-cb (fn []
                               (dis/dispatch! [:private-board-kick-out-self self-data])
                               (dis/dispatch! [:alert-modal-hide]))}])))}
                        "Leave Board"]
                      [:div.user-type.no-dropdown
                        "Full Access"])
                    [:div.user-type
                      {:class (utils/class-set {:no-dropdown (not can-change)
                                                :active showing-dropdown})}
                      (if (= user-type :author)
                        "Contributor"
                        "Viewer")])]))]])
      [:div.sections-picker-add-footer
        [:button.mlb-reset.create-bt
          "Create"]]]))

(defn sections-group
  [s group should-show-headers]
  (let [active-slug (first (:rum/args s))
        on-change (second (:rum/args s))]
    [:div.sections-picker-group
      (when (and should-show-headers
                 (pos? (count group)))
        [:div.sections-picker-group-header
          (get-section-name group)])
      (when (pos? (count group))
        (for [b group
              :let [active (= (:slug b) active-slug)]]
          [:div.sections-picker-section.group
            {:key (str "sections-picker-" (:uuid b))
             :class (when active "active")
             :on-click #(when (fn? on-change)
                          (on-change b))}
            [:div.sections-picker-circle]
            [:div.sections-picker-section-name
              (:name b)]
            [:div.sections-picker-section-posts
              (str (count (:fixed-items b)) " posts")]]))]))

(rum/defcs sections-picker < rum/reactive
                             (drv/drv :editable-boards)
                             (rum/local false ::show-add-board)
  [s active-slug on-change]
  (let [all-sections (vals (drv/react s :editable-boards))
        team-sections (filterv #(= (:access %) "team") all-sections)
        public-sections (filterv #(= (:access %) "public") all-sections)
        private-sections (filterv #(= (:access %) "private") all-sections)
        should-show-headers? (or (and (pos? (count private-sections))
                                      (pos? (count public-sections)))
                                 (and (pos? (count team-sections))
                                      (pos? (count public-sections)))
                                 (and (pos? (count team-sections))
                                      (pos? (count private-sections))))]
    [:div.sections-picker.group
      {:class (when-not @(::show-add-board s) "sections-list")}
      [:div.sections-picker-header
        [:div.sections-picker-header-left
          "Post to..."]
        [:div.sections-picker-header-right
          (when-not @(::show-add-board s)
            [:button.mlb-reset.add-new-section-bt
              {:on-click #(reset! (::show-add-board s) true)}
              "Add new section"])]]
      (if @(::show-add-board s)
        (section-editor)
        [:div.sections-picker-content
          (sections-group s team-sections should-show-headers?)
          (sections-group s public-sections should-show-headers?)
          (sections-group s private-sections should-show-headers?)])]))