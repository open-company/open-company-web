(ns oc.web.components.ui.section-editor
  (:require [rum.core :as rum]
            [goog.object :as gobj]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.jwt :as jwt]
            [oc.lib.user :as user-lib]
            [oc.web.router :as router]
            [oc.web.utils.user :as uu]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.actions.org :as org-actions]
            [oc.web.actions.team :as team-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.section :as section-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.carrot-switch :refer (carrot-switch)]
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
  (compare (string/lower (user-lib/name-for user-1))
           (string/lower (user-lib/name-for user-2))))

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
    "Team members you invite"])

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

(defn check-section-name-error [s]
  (let [section-editing @(drv/get-ref s :section-editing)
        org-data @(drv/get-ref s :org-data)
        boards (filter #(not= (:slug %) (:slug section-editing)) (:boards org-data))
        sec-name @(::section-name s)
        equal-names (filter #(.match sec-name (js/RegExp. (str "^" (:name %) "$") "ig")) boards)]
    (reset! (::pre-flight-check s) false)
    (reset! (::pre-flight-ok s) false)
    (if (pos? (count equal-names))
      (dis/dispatch! [:update [:section-editing] #(-> %
                                                    (assoc :section-name-error utils/section-name-exists-error)
                                                    (dissoc :loading))])
      (do
        (when (:section-name-error section-editing)
          (dis/dispatch! [:input [:section-editing :section-name-error] nil]))
        (if (>= (count sec-name) section-actions/min-section-name-length)
          (do
            (section-actions/pre-flight-check (when @(::editing-existing-section s) (:slug section-editing))
             sec-name)
            (reset! (::pre-flight-check s) true))
          (reset! (::pre-flight-check s) false))))))

(rum/defcs section-editor <
  ;; Mixins
  mixins/refresh-tooltips-mixin
  rum/reactive
  ;; Locals
  (rum/local "" ::query)
  (rum/local false ::show-access-list)
  (rum/local false ::show-search-results)
  (rum/local nil ::show-edit-user-dropdown)
  (rum/local nil ::show-edit-user-top)
  (rum/local "" ::initial-section-name)
  (rum/local false ::editing-existing-section)
  (rum/local false ::slack-enabled)
  (rum/local "" ::section-name)
  (rum/local false ::pre-flight-check)
  (rum/local false ::pre-flight-ok)
  (rum/local nil ::section-name-check-timeout)
  (rum/local false ::saving)
  ;; Derivatives
  (drv/drv :org-data)
  (drv/drv :board-data)
  (drv/drv :section-editing)
  (drv/drv :team-data)
  (drv/drv :team-channels)
  (drv/drv :team-roster)
  (drv/drv :current-user-data)
  {:will-mount (fn [s]
   (team-actions/teams-get)
   (let [initial-section-data (first (:rum/args s))
         new-section (nil? initial-section-data)
         fixed-section-data (if new-section
                            utils/default-section
                            (section-for-editing initial-section-data))]
     (when (string? (:name fixed-section-data))
       (reset! (::section-name s) (clojure.string/trim
        (.text (js/$ (str "<div>" (:name fixed-section-data) "</div>"))))))
     (reset! (::editing-existing-section s) (not new-section))
     (when (seq (:name fixed-section-data))
       (reset! (::initial-section-name s) (:name fixed-section-data)))
     (dis/dispatch! [:input [:section-editing] fixed-section-data])
     (reset! (::slack-enabled s)
      (-> fixed-section-data :slack-mirror :channel-id seq)))
  s)
  :will-update (fn [s]
   (let [section-editing @(drv/get-ref s :section-editing)]
     (when @(::pre-flight-check s)
       (when-not (:pre-flight-loading section-editing)
         (reset! (::pre-flight-check s) false)
         (when (and (not (:section-name-error section-editing))
                    (not (:section-error section-editing)))
           (reset! (::pre-flight-ok s) true))))
     ;; Re-enable the save button after a save failure
     (when (and @(::saving s)
                (not (:loading section-editing)))
       (reset! (::saving s) false)))
   s)}
  [s initial-section-data on-change from-section-picker]
  (let [org-data (drv/react s :org-data)
        no-drafts-boards (filter #(and (not (:draft %)) (not= (:slug %) utils/default-drafts-board-slug))
                          (:boards org-data))
        section-editing (drv/react s :section-editing)
        section-data (if (seq (:slug section-editing)) (drv/react s :board-data) section-editing)
        team-data (drv/react s :team-data)
        slack-teams (drv/react s :team-channels)
        show-slack-channels? (pos? (apply + (map #(-> % :channels count) slack-teams)))
        channel-name (when @(::editing-existing-section s) (:channel-name (:slack-mirror section-data)))
        roster (drv/react s :team-roster)
        all-users-data (if team-data (:users team-data) (:users roster))
        slack-orgs (:slack-orgs team-data)
        cur-user-data (drv/react s :current-user-data)
        slack-users (:slack-users cur-user-data)
        current-user-id (jwt/user-id)
        ;; user can edit the private section users if
        ;; he's creating a new section
        ;; or if he's in the authors list of the existing section
        can-change (or (= (:slug section-editing) utils/default-section-slug)
                       (some #{current-user-id} (:authors section-editing))
                       (jwt/is-admin? (:team-id org-data)))
        last-section-standing (= (count no-drafts-boards) 1)
        disallow-public-board? (and (:content-visibility org-data)
                                    (:disallow-public-board (:content-visibility org-data)))
        wrapped-on-change (fn [exit-cb]
                            (if (:has-changes @(drv/get-ref s :section-editing))
                              (alert-modal/show-alert
                               {:icon "/img/ML/trash.svg"
                                :action "section-settings-unsaved-edits"
                                :message "Leave without saving your changes?"
                                :link-button-title "Stay"
                                :link-button-cb #(alert-modal/hide-alert)
                                :solid-button-style :red
                                :solid-button-title "Lose changes"
                                :solid-button-cb #(do
                                                    (alert-modal/hide-alert)
                                                    (on-change nil nil exit-cb))})
                              (on-change nil nil exit-cb)))]
    [:div.section-editor-container
      {:on-click #(when-not (utils/event-inside? % (rum/ref-node s :section-editor))
                    (wrapped-on-change nav-actions/close-all-panels))}
      [:button.mlb-reset.modal-close-bt
        {:on-click #(wrapped-on-change nav-actions/close-all-panels)}]
      [:div.section-editor.group
        {:ref :section-editor
         :on-click (fn [e]
                     (when-not (utils/event-inside? e (rum/ref-node s "section-editor-add-access-list"))
                       (reset! (::show-access-list s) false))
                     (when-not (utils/event-inside? e (rum/ref-node s "private-users-search"))
                       (reset! (::show-search-results s) false))
                     (when-not (utils/event-inside? e (rum/ref-node s "section-editor-add-private-users"))
                       (reset! (::show-edit-user-dropdown s) nil)))}
        [:div.section-editor-header
          [:div.section-editor-header-title
            {:dangerouslySetInnerHTML
              (utils/emojify
               (if @(::editing-existing-section s)
                 "Team settings"
                 "Create topic"))}]
          (let [disable-bt (or @(::saving s)
                               (< (count @(::section-name s)) section-actions/min-section-name-length)
                               @(::pre-flight-check s)
                               (:pre-flight-loading section-editing)
                               (seq (:section-name-error section-editing)))]
            [:button.mlb-reset.save-bt
              {:on-click (fn [_]
                          (when (and (not disable-bt)
                                     (compare-and-set! (::saving s) false true))
                            (let [section-node (rum/ref-node s "section-name")
                                  section-name (clojure.string/trim (.-value section-node))
                                  personal-note-node (rum/ref-node s "personal-note")
                                  personal-note (when personal-note-node (.-innerText personal-note-node))
                                  success-cb #(when (fn? on-change)
                                                (on-change % personal-note nav-actions/hide-section-editor))]
                              (section-actions/section-save-create section-editing section-name success-cb))))
               :class (when disable-bt "disabled")}
              "Save"])
          [:button.mlb-reset.cancel-bt
            {:on-click #(wrapped-on-change nav-actions/hide-section-editor)}
            "Back"]]
        [:div.section-editor-add
          [:div.section-editor-add-label
            [:span.section-name "Topic name"]]
          [:input.section-editor-add-name.oc-input
            {:value @(::section-name s)
             :placeholder "Topic name"
             :ref "section-name"
             :class  (utils/class-set {:preflight-ok @(::pre-flight-ok s)
                                       :preflight-error (:section-name-error section-editing)})
             :max-length 50
             :on-change (fn [e]
                          (let [next-section-name (.. e -target -value)]
                            (when (not= @(::section-name s) next-section-name)
                              (reset! (::section-name s) next-section-name)
                              (when @(::section-name-check-timeout s)
                                (.clearTimeout js/window @(::section-name-check-timeout s)))
                              (reset! (::section-name-check-timeout s)
                               (utils/after 500
                                #(check-section-name-error s))))))}]
          (when (or (:section-name-error section-editing)
                    (:section-error section-editing))
            [:div.section-editor-error-label
              (str (or (:section-name-error section-editing)
                       (:section-error section-editing)))])
          [:div.section-editor-add-label
            "Topic security"]
          [:div.section-editor-add-access.oc-input
            {:class (when @(::show-access-list s) "active")
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
                {:on-click (fn [e]
                             (utils/event-stop e)
                             (reset! (::show-access-list s) false)
                             (dis/dispatch! [:update [:section-editing] #(merge % {:access "team"
                                                                                   :has-changes true})]))}
                team-access]
              [:div.access-list-row
                {:on-click (fn [e]
                              (utils/event-stop e)
                              (reset! (::show-access-list s) false)
                              (when show-slack-channels?
                                (reset! (::slack-enabled s) false))
                              (dis/dispatch! [:update [:section-editing]
                                              #(merge % {:access "private"
                                                         :has-changes true
                                                         :authors (conj (set (:authors section-editing)) current-user-id)
                                                         :slack-mirror (if show-slack-channels?
                                                                         nil
                                                                         (:slack-mirror section-editor))})]))}
                private-access]
              (when-not disallow-public-board?
                [:div.access-list-row
                  {:on-click (fn [e]
                                (utils/event-stop e)
                                (reset! (::show-access-list s) false)
                                (dis/dispatch! [:update [:section-editing] #(merge % {:access "public"
                                                                                      :has-changes true})]))}
                  public-access])])
          (when show-slack-channels?
            [:div.section-editor-add-label.top-separator
              "Auto-share to Slack"
              (when show-slack-channels?
                [:span.info])
              (when show-slack-channels?
                (carrot-switch {:selected @(::slack-enabled s)
                                :did-change-cb (fn [v]
                                                 (reset! (::slack-enabled s) v)
                                                  (when-not v
                                                    (dis/dispatch! [:update [:section-editing]
                                                     #(merge % {:slack-mirror nil
                                                                :has-changes true})])))}))])
          (if show-slack-channels?
            [:div.section-editor-add-slack-channel.group
              {:class (when-not @(::slack-enabled s) "disabled")}
              (slack-channels-dropdown {:initial-value (when channel-name (str "#" channel-name))
                                        :on-change (fn [team channel]
                                                     (dis/dispatch!
                                                      [:update
                                                       [:section-editing]
                                                       #(merge %
                                                         {:slack-mirror
                                                           {:channel-id (:id channel)
                                                            :channel-name (:name channel)
                                                            :slack-org-id (:slack-org-id team)}
                                                          :has-changes true})]))})]
            ;; If they don't have bot installed already but have slack org associated to the team
            ;; and user has a slack user (if not they can't add the bot) let's prompt to add the bot
            (when (and (not (jwt/team-has-bot? (:team-id team-data)))
                       (pos? (count slack-users))
                       (pos? (count slack-orgs)))
              [:div.section-editor-enable-slack-bot.group
                "Automatically share updates to Slack? "
                [:button.mlb-reset.enable-slack-bot-bt
                  {:on-click (fn [_]
                               (org-actions/bot-auth team-data cur-user-data (router/get-token)))}
                  "Add Wut bot"]]))
          (when (= (:access section-editing) "public")
            [:div.section-editor-access-public-description
              "Public topics are visible to the world, including search engines."])
          (when (= (:access section-editing) "private")
            [:div.section-editor-add-label.top-separator
              "Add members to this private topic"])
          (when (= (:access section-editing) "private")
            (let [query  (::query s)
                  available-users (uu/filter-active-users all-users-data)
                  addable-users (get-addable-users section-editing available-users)
                  filtered-users (filter-users addable-users @query)]
              (when can-change
                [:div.section-editor-private-users-search
                  {:ref "private-users-search"}
                  [:input.oc-input
                    {:class utils/hide-class
                     :value @query
                     :type "text"
                     :placeholder "Select a member..."
                     :on-focus #(reset! (::show-search-results s) true)
                     :on-change #(let [q (.. % -target -value)]
                                  (reset! (::show-search-results s) (seq q))
                                  (reset! query q))}]
                  (when @(::show-search-results s)
                    [:div.section-editor-private-users-results
                      (if (pos? (count filtered-users))
                        (for [u filtered-users
                              :let [team-user (some #(when (= (:user-id %) (:user-id u)) %) (:users roster))
                                    user (merge u team-user)
                                    user-type (utils/get-user-type user org-data section-editing)]]
                          [:div.section-editor-private-users-result
                            {:class utils/hide-class
                             :on-click #(do
                                          (reset! query "")
                                          (reset! (::show-search-results s) false)
                                          (section-actions/private-section-user-add user user-type))
                             :ref (str "add-user-" (:user-id user))}
                            (user-avatar-image user)
                            [:div.name
                              (user-lib/name-for user)]])
                        [:div.section-editor-private-users-result.no-more-invites
                          [:div.name
                            (str
                             "Looks like you'll need to invite more people before you can add them. "
                             "You can do that in ")
                            [:a
                              {:on-click #(nav-actions/show-org-settings :invite)}
                              "Wut topic settings"]
                            "."]])])])))
          (when (and (= (:access section-editing) "private")
                     (pos? (+ (count (:authors section-editing))
                              (count (:viewers section-editing)))))
            [:div.section-editor-add-label.group
              [:span.main-label
                "Team members"]
              [:span.role-header
                "Access"]])
          (when (and (= (:access section-editing) "private")
                     (pos? (+ (count (:authors section-editing))
                              (count (:viewers section-editing)))))
            [:div.section-editor-add-private-users
              {:ref "section-editor-add-private-users"}
              (let [user-id @(::show-edit-user-dropdown s)
                    user-type (if (utils/in? (:viewers section-editing) user-id)
                                :viewer
                                :author)
                    team-user (some #(when (= (:user-id %) user-id) %) all-users-data)]
                [:div.section-editor-add-private-users-dropdown-container
                  {:style {:top (str (+ @(::show-edit-user-top s) -114) "px")
                           :display (if @(::show-edit-user-dropdown s) "block" "none")}}
                  (dropdown-list {:items [{:value :viewer :label "View"}
                                          {:value :author :label "Edit"}
                                          {:value :remove :label "Remove"}]
                                  :value user-type
                                  :on-change (fn [item]
                                   (reset! (::show-edit-user-dropdown s) nil)
                                   (if (= (:value item) :remove)
                                     (section-actions/private-section-user-remove team-user)
                                     (section-actions/private-section-user-add team-user (:value item))))})])
              [:div.section-editor-add-private-users-list.group
                {:class utils/hide-class
                 :on-scroll #(do
                              (reset! (::show-edit-user-dropdown s) nil)
                              (reset! (::show-edit-user-top s) nil))
                 :ref "edit-users-scroll"}
                (let [author-ids (set (:authors section-editing))
                      viewer-ids (set (:viewers section-editing))
                      authors (filter (comp author-ids :user-id) all-users-data)
                      viewers (filter (comp viewer-ids :user-id) all-users-data)
                      complete-authors (map
                                        #(merge % {:type :author :display-name (user-lib/name-for %)})
                                        authors)
                      complete-viewers (map
                                        #(merge % {:type :viewer :display-name (user-lib/name-for %)})
                                        viewers)
                      all-users (concat complete-authors complete-viewers)
                      self-user (first (filter #(= (:user-id %) current-user-id) all-users))
                      rest-users (filter #(not= (:user-id %) current-user-id) all-users)
                      sorted-users (concat [self-user] (sort compare-users rest-users))]
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
                        (user-lib/name-for user)
                        (when (= (:user-id user) current-user-id)
                          " (you)")]
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
                                  :message "Are you sure you want to leave this topic?"
                                  :link-button-title "No"
                                  :link-button-cb #(alert-modal/hide-alert)
                                  :solid-button-title "Yes"
                                  :solid-button-cb (fn []
                                   (section-actions/private-section-kick-out-self self-data)
                                   (alert-modal/hide-alert))})))}
                            "Leave topic"]
                          [:div.user-type.no-dropdown
                            "Edit"])
                        [:div.user-type
                          {:class (utils/class-set {:no-dropdown (not can-change)
                                                    :active showing-dropdown})}
                          (if (= user-type :author)
                            "Edit"
                            "View")])]))]])
          (when (= (:access section-editing) "private")
            [:div.section-editor-add-label
              "Personal note"])
          (when (= (:access section-editing) "private")
            [:div.section-editor-add-personal-note.oc-input
              {:class utils/hide-class
               :content-editable true
               :placeholder "Add a personal note to your invitation..."
               :ref "personal-note"
               :on-paste #(js/OnPaste_StripFormatting (rum/ref-node s "personal-note") %)
               :on-key-press (fn [e]
                               (when (or (>= (count (.. e -target -innerText)) 500)
                                        (= (.-key e) "Enter"))
                                (utils/event-stop e)))
               :dangerouslySetInnerHTML {:__html ""}}])
          [:div.section-editor-add-footer
            (when (and @(::editing-existing-section s)
                       (utils/link-for (:links section-data) "delete"))
              [:button.mlb-reset.delete-bt
                {:on-click (fn []
                            (when-not last-section-standing
                              (alert-modal/show-alert
                               {:icon "/img/ML/trash.svg"
                                :action "delete-section"
                                :message [:span
                                           [:span "Are you sure?"]
                                           (when (-> section-data :entry-count pos?)
                                             [:span
                                               " This will delete the topic and "
                                               [:strong "all"]
                                               " its updates, too."])]
                                :link-button-title "No"
                                :link-button-cb #(alert-modal/hide-alert)
                                :solid-button-style :red
                                :solid-button-title "Yes, I'm sure"
                                :solid-button-cb (fn []
                                                   (section-actions/section-delete
                                                     (:slug section-data)
                                                     (notification-actions/show-notification
                                                      {:title "Topic deleted"
                                                       :dismiss true
                                                       :expire 3
                                                       :id :section-deleted}))
                                                   (alert-modal/hide-alert)
                                                   (nav-actions/hide-section-editor))})))
                 :data-toggle "tooltip"
                 :data-placement "top"
                 :data-container "body"
                 :title (if last-section-standing
                         "You cannot delete the last remaining topic."
                         "Delete this topic and all its updates.")
                 :class (when last-section-standing "disabled")}
                "Delete topic"])]]]]))