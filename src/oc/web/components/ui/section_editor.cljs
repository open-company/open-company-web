(ns oc.web.components.ui.section-editor
  (:require [rum.core :as rum]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.section :as section-actions]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)]
            [oc.web.components.ui.slack-channels-dropdown :refer (slack-channels-dropdown)]))

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

(def private-access
  [:div.access-item.private-access
    "Only team members you invite"])

(def team-access
  [:div.access-item.team-access
    "Anyone on the team"])

(def public-access
  [:div.access-item.public-access
    "Open for the public"])

(defn section-for-editing [section-data]
  (-> section-data
    (assoc :authors (map #(if (map? %) (:user-id %) %) (:authors section-data)))
    (assoc :viewers (map #(if (map? %) (:user-id %) %) (:viewers section-data)))))

(defn dismiss []
  (dis/dispatch! [:input [:show-section-editor] false])
  (dis/dispatch! [:input [:show-section-add] false]))

(rum/defcs section-editor < rum/reactive
                            ;; Locals
                            (rum/local "" ::query)
                            (rum/local false ::show-access-list)
                            (rum/local false ::show-search-results)
                            (rum/local nil ::show-edit-user-dropdown)
                            (rum/local nil ::show-edit-user-top)
                            (rum/local "" ::initial-section-name)
                            (rum/local false ::editing-existing-section)
                            (rum/local nil ::click-listener)
                            (rum/local false ::slack-enabled)
                            ;; Derivatives
                            (drv/drv :org-data)
                            (drv/drv :board-data)
                            (drv/drv :section-editing)
                            (drv/drv :team-data)
                            (drv/drv :team-channels)
                            (drv/drv :team-roster)
                            {:will-mount (fn [s]
                              (let [initial-section-data (first (:rum/args s))
                                    new-section (nil? initial-section-data)
                                    fixed-section-data (if new-section
                                                        utils/default-section
                                                        (section-for-editing initial-section-data))]
                                (reset! (::editing-existing-section s) (not new-section))
                                (when-not (empty? (:name fixed-section-data))
                                  (reset! (::initial-section-name s) (:name fixed-section-data)))
                                (dis/dispatch! [:input [:section-editing] fixed-section-data])
                                (reset! (::slack-enabled s)
                                 (not (empty? (:channel-id (:slack-mirror fixed-section-data))))))
                              (reset! (::click-listener s)
                               (events/listen js/window EventType/CLICK
                                #(when-not (utils/event-inside? % (rum/dom-node s))
                                   (dismiss))))
                              s)
                             :will-unmount (fn [s]
                              (when @(::click-listener s)
                                (events/unlistenByKey @(::click-listener s))
                                (reset! (::click-listener s) nil))
                              s)}
  [s initial-section-data on-change]
  (let [org-data (drv/react s :org-data)
        section-editing (drv/react s :section-editing)
        section-data (if (seq (:slug section-editing)) (drv/react s :board-data) section-editing)
        team-data (drv/react s :team-data)
        slack-teams (drv/react s :team-channels)
        show-slack-channels? (pos? (apply + (map #(-> % :channels count) slack-teams)))
        channel-name (when @(::editing-existing-section s) (:channel-name (:slack-mirror section-data)))
        roster (drv/react s :team-roster)
        current-user-id (jwt/user-id)
        ;; user can edit the private section users if
        ;; he's creating a new section
        ;; or if he's in the authors list of the existing section
        can-change (or (= (:slug section-editing) utils/default-section-slug)
                       (some #{current-user-id} (:authors section-editing)))]
    [:div.section-editor.group
      {:on-click (fn [e]
                   (when-not (utils/event-inside? e (rum/ref-node s "section-editor-add-access-list"))
                     (reset! (::show-access-list s) false))
                   (when-not (utils/event-inside? e (rum/ref-node s "private-users-search"))
                     (reset! (::show-search-results s) false))
                   (when-not (utils/event-inside? e (rum/ref-node s "section-editor-add-private-users"))
                     (reset! (::show-edit-user-dropdown s) nil)))}
      [:button.mlb-reset.mobile-modal-close-bt
        {:on-click #(on-change nil)}]
      [:div.section-editor-header
        [:div.section-editor-header-left
          {:dangerouslySetInnerHTML
            (utils/emojify
             (if @(::editing-existing-section s)
               "Section settings"
               "Create a new section"))}]]
      [:div.section-editor-add
        [:div.section-editor-add-label
          "Section name"]
        [:div.section-editor-add-name
          {:content-editable true
           :placeholder "Section name"
           :ref "section-name"
           :on-paste #(js/OnPaste_StripFormatting (rum/ref-node s "section-name") %)
           :on-key-up #(when (:section-name-error section-editing)
                         (dis/dispatch! [:input [:section-editing :section-name-error] nil]))
           :on-key-press (fn [e]
                           (when (or (>= (count (.. e -target -innerText)) 50)
                                    (= (.-key e) "Enter"))
                            (utils/event-stop e)))
           :dangerouslySetInnerHTML (utils/emojify @(::initial-section-name s))}]
        (when show-slack-channels?
          [:div.section-editor-add-label
            "Auto-share to Slack"
            [:span.info]])
        (when show-slack-channels?
        [:div.section-editor-add-slack-channel.group
          (carrot-checkbox {:selected @(::slack-enabled s)
                            :did-change-cb #(do
                                              (reset! (::slack-enabled s) %)
                                              (when-not %
                                                (dis/dispatch!
                                                 [:input
                                                  [:section-editing :slack-mirror]
                                                  nil])))})
          (when @(::slack-enabled s)
            (slack-channels-dropdown {:initial-value (when channel-name (str "#" channel-name))
                                      :on-change (fn [team channel]
                                                   (dis/dispatch!
                                                    [:input
                                                     [:section-editing :slack-mirror]
                                                     {:channel-id (:id channel)
                                                      :channel-name (:name channel)
                                                      :slack-org-id (:slack-org-id team)}]))}))])
        [:div.section-editor-add-label
          "Who can view this section?"]
        [:div.section-editor-add-access
          {:class (when @(::show-access-list s) "expanded")
           :on-click #(do
                        (utils/event-stop %)
                        (reset! (::show-access-list s) (not @(::show-access-list s))))}
          (case (:access section-editing)
            "private" private-access
            "public" public-access
            team-access)]
        (when @(::show-access-list s)
          [:div.section-editor-add-access-list
            {:ref "section-editor-add-access-list"}
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
          [:div.section-editor-add-label
            "Invite member"])
        (when (= (:access section-editing) "private")
          (let [query  (::query s)
                available-users (filter :user-id (:users roster))
                addable-users (get-addable-users section-editing available-users)
                filtered-users (filter-users addable-users @query)]
            (when (and can-change
                       (pos? (count addable-users)))
              [:div.section-editor-private-users-search
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
                  [:div.section-editor-private-users-results
                    (for [u filtered-users
                          :let [team-user (some #(when (= (:user-id %) (:user-id u)) %) (:users roster))
                                user (merge u team-user)
                                user-type (utils/get-user-type user org-data section-editing)]]
                      [:div.section-editor-private-users-result
                        {:on-click #(do
                                      (reset! query "")
                                      (reset! (::show-search-results s) false)
                                      (section-actions/private-section-user-add user user-type))
                         :ref (str "add-user-" (:user-id user))}
                        (user-avatar-image user)
                        [:div.name
                          (utils/name-or-email user)]])])])))
        (when (and (= (:access section-editing) "private")
                   (pos? (+ (count (:authors section-editing))
                            (count (:viewers section-editing)))))
          [:div.section-editor-add-label.group
            [:span.main-label
              "Section members"]
            [:span.role-header
              "Role"]])
        (when (and (= (:access section-editing) "private")
                   (pos? (+ (count (:authors section-editing))
                            (count (:viewers section-editing)))))
          [:div.section-editor-add-private-users
            {:ref "section-editor-add-private-users"}
            (let [user-id @(::show-edit-user-dropdown s)
                  user-type (if (utils/in? (:viewers section-editing) user-id)
                              :viewer
                              :author)
                  team-user (some #(when (= (:user-id %) user-id) %) (:users team-data))]
              [:div.section-editor-add-private-users-dropdown-container
                {:style {:top (str (+ @(::show-edit-user-top s) -114) "px")
                         :display (if @(::show-edit-user-dropdown s) "block" "none")}}
                (dropdown-list {:items [{:value :viewer :label "Viewer"}
                                        {:value :author :label "Contributor"}
                                        {:value :remove :label "Remove"}]
                                :value user-type
                                :on-change (fn [item]
                                 (reset! (::show-edit-user-dropdown s) nil)
                                 (if (= (:value item) :remove)
                                   (section-actions/private-section-user-remove team-user)
                                   (section-actions/private-section-user-add team-user (:value item))))})])
            [:div.section-editor-add-private-users-list.group
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
                  [:div.section-editor-add-private-user.group
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
                              (alert-modal/show-alert
                               {:icon "/img/ML/error_icon.png"
                                :action "remove-self-user-from-private-section"
                                :message "Are you sure you want to leave this section?"
                                :link-button-title "No"
                                :link-button-cb #(alert-modal/hide-alert)
                                :solid-button-title "Yes"
                                :solid-button-cb (fn []
                                 (section-actions/private-section-kick-out-self self-data)
                                 (alert-modal/hide-alert))})))}
                          "Leave section"]
                        [:div.user-type.no-dropdown
                          "Contributor"])
                      [:div.user-type
                        {:class (utils/class-set {:no-dropdown (not can-change)
                                                  :active showing-dropdown})}
                        (if (= user-type :author)
                          "Contributor"
                          "Viewer")])]))]])
        [:div.section-editor-add-footer
          (when (and @(::editing-existing-section s)
                     (utils/link-for (:links section-data) "delete"))
            [:button.mlb-reset.delete-bt
              {:on-click (fn []
                          (alert-modal/show-alert
                           {:icon "/img/ML/trash.svg"
                            :action "delete-section"
                            :message [:span
                                       [:span "Are you sure?"]
                                       (when (-> section-data :fixed-items count pos?)
                                         [:span
                                           " This will delete the section and "
                                           [:strong "all"]
                                           " its posts, too."])]
                            :link-button-title "No"
                            :link-button-cb #(alert-modal/hide-alert)
                            :solid-button-style :red
                            :solid-button-title "Yes, I'm sure"
                            :solid-button-cb (fn []
                                               (section-actions/section-delete
                                                 (:slug section-data))
                                               (alert-modal/hide-alert)
                                               (dismiss))}))}
              "Delete section"])
          [:button.mlb-reset.create-bt
            {:on-click #(let [section-node (rum/ref-node s "section-name")
                              inner-html (.-innerHTML section-node)
                              section-name (utils/strip-HTML-tags inner-html)
                              next-section-editing (merge section-editing {:slug utils/default-section-slug
                                                                           :name section-name})]
                          (dis/dispatch! [:input [:section-editing] next-section-editing])
                          (when (fn? on-change)
                            (on-change next-section-editing)))}
            (if @(::editing-existing-section s)
              "Save"
              "Create")]]]]))