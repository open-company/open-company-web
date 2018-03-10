(ns oc.web.components.ui.sections-picker
  (:require [rum.core :as rum]
            [goog.object :as gobj]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

;; Private section users search helpers

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
  [section-editing users]
  (let [already-in-ids (concat (:viewers section-editing) (:authors section-editing))
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
                            (rum/local false ::show-search-results)
                            (rum/local nil ::show-edit-user-dropdown)
                            (rum/local nil ::show-edit-user-top)
                            (rum/local "" ::initial-section-name)
                            ;; Derivatives
                            (drv/drv :org-data)
                            (drv/drv :board-data)
                            (drv/drv :section-editing)
                            (drv/drv :team-data)
                            (drv/drv :team-channels)
                            (drv/drv :team-roster)
                            {:will-mount (fn [s]
                             (let [initial-section-data (first (:rum/args s))
                                   fixed-section-data (if (nil? initial-section-data)
                                                       utils/default-section
                                                       initial-section-data)]
                               (when-not (string/blank? (:name fixed-section-data))
                                 (reset! (::initial-section-name s) (:name fixed-section-data)))
                               (dis/dispatch! [:input [:section-editing] fixed-section-data]))
                             s)}
  [s initial-section-data on-change]
  (let [org-data (drv/react s :org-data)
        section-editing (drv/react s :section-editing)
        section-data (if (seq (:slug section-editing)) (drv/react s :board-data) section-editing)
        team-data (drv/react s :team-data)
        roster (drv/react s :team-roster)
        current-user-id (jwt/user-id)
        ;; user can edit the private section users if
        ;; he's creating a new section
        ;; or if he's in the authors list of the existing section
        can-change (or (not (seq (:slug section-editing)))
                       (some #{current-user-id} (map :user-id (:authors section-editing))))]
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
         :placeholder "Section name"
         :ref "section-name"
         :on-paste #(js/OnPaste_StripFormatting (rum/ref-node s "section-name") %)
         :on-key-up #(when (:board-name-error section-editing)
                       (dis/dispatch! [:input [:section-editing :board-name-error] nil]))
         :on-key-press (fn [e]
                         (when (or (>= (count (.. e -target -innerText)) 50)
                                  (= (.-key e) "Enter"))
                          (utils/event-stop e)))
         :dangerouslySetInnerHTML (utils/emojify @(::initial-section-name s))}]
      [:div.sections-picker-add-label
        "Who can view this section?"]
      [:div.sections-picker-add-access
        {:class (when @(::show-access-list s) "expanded")
         :on-click #(reset! (::show-access-list s) (not @(::show-access-list s)))}
        (case (:access section-editing)
          "private" private-access
          "public" public-access
          team-access)]
      (when @(::show-access-list s)
        [:div.sections-picker-add-access-list
          [:div.access-list-row
            {:on-click #(do
                          (utils/event-stop %)
                          (reset! (::show-access-list s) false)
                          (dis/dispatch! [:input [:section-editing :access] "team"]))}
            team-access]
          [:div.access-list-row
            {:on-click #(do
                          (utils/event-stop %)
                          (reset! (::show-access-list s) false)
                          (dis/dispatch! [:input [:section-editing :access] "private"]))}
            private-access]
          [:div.access-list-row
            {:on-click #(do
                          (utils/event-stop %)
                          (reset! (::show-access-list s) false)
                          (dis/dispatch! [:input [:section-editing :access] "public"]))}
            public-access]])
      (when (= (:access section-editing) "private")
        [:div.sections-picker-add-label
          "Invite member"])
      (when (= (:access section-editing) "private")
        (let [query  (::query s)
              available-users (filter :user-id (:users roster))
              addable-users (get-addable-users section-editing available-users)
              filtered-users (filter-users addable-users @query)]
          (when (and can-change
                     (pos? (count addable-users)))
            [:div.sections-picker-private-users-search
              {:ref "private-users-search"}
              [:input
                {:value @query
                 :type "text"
                 :placeholder "Add a team member to this section"
                 :on-focus #(reset! (::show-search-results s) true)
                 :on-change #(let [q (.. % -target -value)]
                              (reset! (::show-search-results s) (seq q))
                              (reset! query q))}]
              (when @(::show-search-results s)
                [:div.sections-picker-private-users-results
                  (for [u filtered-users
                        :let [team-user (some #(when (= (:user-id %) (:user-id u)) %) (:users roster))
                              user (merge u team-user)
                              user-type (utils/get-user-type user org-data section-editing)]]
                    [:div.sections-picker-private-users-result
                      {:on-click #(do
                                    (reset! query "")
                                    (reset! (::show-search-results s) false)
                                    (dis/dispatch! [:private-board-user-add user user-type]))
                       :ref (str "add-user-" (:user-id user))}
                      (user-avatar-image user)
                      [:div.name
                        (utils/name-or-email user)]])])])))
      (when (and (= (:access section-editing) "private")
                 (pos? (+ (count (:authors section-editing))
                          (count (:viewers section-editing)))))
        [:div.sections-picker-add-label.group
          [:span.main-label
            "Section members"]
          [:span.role-header
            "Role"]])
      (when (and (= (:access section-editing) "private")
                 (pos? (+ (count (:authors section-editing))
                          (count (:viewers section-editing)))))
        [:div.sections-picker-add-private-users
          {:ref "sections-picker-add-private-users"}
          (let [user-id @(::show-edit-user-dropdown s)
                user-type (if (utils/in? (:viewers section-editing) user-id)
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
            (let [author-ids (set (:authors section-editing))
                  viewer-ids (set (:viewers section-editing))
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
                    (if (and (seq (:slug section-editing))
                             (> (count (:authors section-data)) 1))
                      [:div.user-type.remove-link
                        {:on-click (fn []
                          (let [authors (:authors section-data)
                                self-data (first (filter #(= (:user-id %) current-user-id) authors))]
                            (dis/dispatch! [:alert-modal-show
                             {:icon "/img/ML/error_icon.png"
                              :action "remove-self-user-from-private-section"
                              :message "Are you sure you want to leave this section?"
                              :link-button-title "No"
                              :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                              :solid-button-title "Yes"
                              :solid-button-cb (fn []
                               (dis/dispatch! [:private-board-kick-out-self self-data])
                               (dis/dispatch! [:alert-modal-hide]))}])))}
                        "Leave section"]
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
          {:on-click #(let [section-node (rum/ref-node s "section-name")
                            inner-html (.-innerHTML section-node)
                            section-name (utils/strip-HTML-tags
                                          (utils/emoji-images-to-unicode
                                           (gobj/get (utils/emojify inner-html) "__html")))
                            next-section-editing (merge section-editing {:slug utils/default-section-slug
                                                                         :name section-name})]
                        (dis/dispatch! [:input [:section-editing] next-section-editing])
                        (on-change next-section-editing))}
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
                             (drv/drv :section-editing)
                             (rum/local false ::show-add-section)
  [s active-slug on-change]
  (let [section-editing (drv/react s :section-editing)
        all-sections (vals (drv/react s :editable-boards))
        team-sections (filterv #(= (:access %) "team") all-sections)
        fixed-team-sections (if (= (:access section-editing) "team")
                              (vec (concat [section-editing] team-sections))
                              team-sections)
        public-sections (filterv #(= (:access %) "public") all-sections)
        fixed-public-sections (if (= (:access section-editing) "public")
                                (vec (concat [section-editing] public-sections))
                                public-sections)
        private-sections (filterv #(= (:access %) "private") all-sections)
        fixed-private-sections (if (= (:access section-editing) "private")
                                 (vec (concat [section-editing] private-sections))
                                 private-sections)
        cfn #(pos? (count %))
        ;; Check if at least 2 groups are shown
        ;; logic: a? (b || c) : (b && c)
        should-show-headers? (if (cfn fixed-team-sections)
                                (or (cfn fixed-private-sections)
                                    (cfn fixed-public-sections))
                                (and (cfn fixed-private-sections)
                                     (cfn fixed-public-sections)))]
    [:div.sections-picker.group
      {:class (when-not @(::show-add-section s) "sections-list")}
      [:div.sections-picker-header
        [:div.sections-picker-header-left
          "Post to..."]
        [:div.sections-picker-header-right
          (when-not @(::show-add-section s)
            [:button.mlb-reset.add-new-section-bt
              {:on-click #(reset! (::show-add-section s) true)}
              "Add new section"])]]
      (if @(::show-add-section s)
        (section-editor nil on-change)
        [:div.sections-picker-content
          (sections-group s fixed-team-sections should-show-headers?)
          (sections-group s fixed-public-sections should-show-headers?)
          (sections-group s fixed-private-sections should-show-headers?)])]))