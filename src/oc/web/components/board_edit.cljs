(ns oc.web.components.board-edit
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [cuerdas.core :as string]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)]
            [oc.web.components.ui.slack-channels-dropdown :refer (slack-channels-dropdown)]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn dismiss-modal []
  (dis/dispatch! [:board-edit/dismiss]))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 dismiss-modal))

(defn board-name-on-change [s board-name-node]
  (let [board-html (.-innerHTML board-name-node)
        cleaned-board-name (utils/strip-HTML-tags
                            (utils/emoji-images-to-unicode (gobj/get (utils/emojify board-html) "__html")))]
    (dis/dispatch! [:input [:board-editing :name] cleaned-board-name])))

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

(rum/defcs board-edit < rum/reactive
                        ;; Derivatives
                        (drv/drv :board-editing)
                        (drv/drv :current-user-data)
                        (drv/drv :org-data)
                        (drv/drv :board-data)
                        (drv/drv :team-data)
                        (drv/drv :team-channels)
                        (drv/drv :team-roster)
                        (drv/drv :alert-modal)
                        ;; Locals
                        (rum/local false ::dismiss)
                        (rum/local false ::team-channels-requested)
                        (rum/local false ::slack-enabled)
                        (rum/local nil ::input-event)
                        (rum/local nil ::dom-add-event)
                        (rum/local nil ::dom-remove-event)
                        (rum/local nil ::char-data-mod-event)
                        (rum/local nil ::initial-board-name)
                        (rum/local nil ::window-click-event)
                        (rum/local "" ::query)
                        (rum/local false ::show-search-results)
                        (rum/local nil ::show-edit-user-dropdown)
                        (rum/local nil ::show-edit-user-top)
                        ;; Mixins
                        mixins/no-scroll-mixin
                        mixins/first-render-mixin

                        {:will-mount (fn [s]
                          ;; Load team-data if not already loaded
                          (if-let [team-data @(drv/get-ref s :team-data)]
                            (when (and (not @(drv/get-ref s :team-channels))
                                       (not @(::team-channels-requested s)))
                              (reset! (::team-channels-requested s) true)
                              (dis/dispatch! [:channels-enumerate (:team-id team-data)]))
                            (dis/dispatch! [:teams-get]))
                          (let [board-data @(drv/get-ref s :board-editing)]
                            (when (some? (:slack-mirror board-data))
                              (reset! (::slack-enabled s) (:slack-mirror board-data)))
                            (reset! (::initial-board-name s) (:name board-data)))
                          s)
                         :did-mount (fn [s]
                          (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
                          (utils/after 100 #(utils/remove-tooltips))
                          (js/emojiAutocomplete)
                          (let [board-name-node (rum/ref-node s "board-name")]
                            (utils/to-end-of-content-editable board-name-node)
                            (reset! (::input-event s)
                             (events/listen board-name-node EventType/INPUT
                              #(board-name-on-change s board-name-node)))
                            (reset! (::dom-add-event s)
                             (events/listen board-name-node EventType/DOMNODEINSERTED
                              #(board-name-on-change s board-name-node)))
                            (reset! (::dom-remove-event s)
                             (events/listen board-name-node EventType/DOMNODEREMOVED
                              #(board-name-on-change s board-name-node)))
                            (reset! (::char-data-mod-event s)
                             (events/listen board-name-node EventType/DOMCHARACTERDATAMODIFIED
                              #(board-name-on-change s board-name-node))))
                          (reset! (::window-click-event s)
                            (events/listen js/window EventType/CLICK
                             #(do
                               (when-not (utils/event-inside? % (rum/ref-node s "private-users-search"))
                                 (reset! (::show-search-results s) false))
                               (when-not (utils/event-inside? % (rum/ref-node s "edit-users-scroll"))
                                 (reset! (::show-edit-user-dropdown s) nil)))))
                          s)
                         :did-remount (fn [o s]
                          ;; Dismiss animated since the board-editing was removed
                          (when (nil? @(drv/get-ref s :board-editing))
                            (close-clicked s))
                          (when (and (not @(drv/get-ref s :team-channels))
                                     (not @(::team-channels-requested s)))
                            (when-let [team-data @(drv/get-ref s :team-data)]
                              (reset! (::team-channels-requested s) true)
                              (dis/dispatch! [:channels-enumerate (:team-id team-data)])))
                          s)
                         :will-unmount (fn [s]
                          (when @(::input-event s)
                            (events/unlistenByKey @(::input-event s))
                            (reset! (::input-event s) nil))
                          (when @(::dom-add-event s)
                            (events/unlistenByKey @(::dom-add-event s))
                            (reset! (::dom-add-event s) nil))
                          (when @(::dom-remove-event s)
                            (events/unlistenByKey @(::dom-remove-event s))
                            (reset! (::dom-remove-event s) nil))
                          (when @(::char-data-mod-event s)
                            (events/unlistenByKey @(::char-data-mod-event s))
                            (reset! (::char-data-mod-event s) nil))
                          (when @(::window-click-event s)
                            (events/unlistenByKey @(::window-click-event s))
                            (reset! (::window-click-event s) nil))
                          s)}
  [s]
  (let [current-user-data (drv/react s :current-user-data)
        board-editing (drv/react s :board-editing)
        board-data (if (seq (:slug board-editing)) (drv/react s :board-data) board-editing)
        new-board? (not (contains? board-editing :links))
        slack-teams (drv/react s :team-channels)
        show-slack-channels? (pos? (apply + (map #(-> % :channels count) slack-teams)))
        channel-name (when-not new-board? (:channel-name (:slack-mirror board-data)))
        org-data (drv/react s :org-data)
        team-data (drv/react s :team-data)
        roster (drv/react s :team-roster)
        should-show-private-board-editing? (= (:access board-editing) "private")
        current-user-id (jwt/user-id)]
    [:div.board-edit-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(:first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(:first-render-done s))})}
      [:div.modal-wrapper
        (when (nil? (drv/react s :alert-modal))
          [:button.carrot-modal-close.mlb-reset
            {:on-click #(close-clicked s)}])
        [:div.board-edit
          {:class (when show-slack-channels? "show-slack-channels")}
          [:div.board-edit-header.group
            (user-avatar-image current-user-data)
            (if new-board?
              [:div.title
                [:span "Creating a new Board"]]
              [:div.title.group
                [:span "Editing "]
                [:span.board-name
                  {:dangerouslySetInnerHTML (utils/emojify (:name board-editing))}]])]
          [:div.board-edit-divider]
          [:div.board-edit-body
            [:div.board-edit-name-label-container.group
              [:div.board-edit-label.board-edit-name-label "Board name"]
              (when (:board-name-error board-editing)
                [:div.board-name-error (:board-name-error board-editing)])]
            [:div.board-edit-name-field.emoji-autocomplete
              {:class (when (:board-name-error board-editing) "board-name-error")
               :content-editable true
               :ref "board-name"
               :on-paste #(js/OnPaste_StripFormatting (rum/ref-node s "board-name") %)
               :on-key-up #(when (:board-name-error board-editing)
                             (dis/dispatch! [:input [:board-editing :board-name-error] nil]))
               :on-key-press (fn [e]
                               (when (or (>= (count (.. e -target -innerText)) 50)
                                        (= (.-key e) "Enter"))
                                (utils/event-stop e)))
               :placeholder "Announcements, Strategy, Who We Are, Marketing, Sales"
               :dangerouslySetInnerHTML (utils/emojify @(::initial-board-name s))}]]
          [:div.board-edit-divider]
          (when show-slack-channels?
            [:div.board-edit-slack-channels-container
              [:div.board-edit-slack-channels-label.group
                [:div.board-edit-label
                  "Send new posts and comments to Slack"]
                (carrot-checkbox {:selected @(::slack-enabled s)
                                  :did-change-cb #(do
                                                    (reset! (::slack-enabled s) %)
                                                    (when-not %
                                                      (dis/dispatch!
                                                       [:input
                                                        [:board-editing :slack-mirror]
                                                        nil])))})]
              (slack-channels-dropdown {:disabled (not @(::slack-enabled s))
                                        :initial-value (when channel-name (str "#" channel-name))
                                        :on-change (fn [team channel]
                                                     (dis/dispatch!
                                                      [:input
                                                       [:board-editing :slack-mirror]
                                                       {:channel-id (:id channel)
                                                        :channel-name (:name channel)
                                                        :slack-org-id (:slack-org-id team)}]))})])
          [:div.board-edit-divider]
          [:div.board-edit-body
            [:div.board-edit-label.board-edit-access-label "Board permissions"]
            [:div.board-edit-access-field.group
              [:div.board-edit-access-bt.board-edit-access-team-bt
                {:class (when (= (:access board-editing) "team") "selected")
                 :on-click #(dis/dispatch! [:input [:board-editing :access] "team"])
                 :data-toggle "tooltip"
                 :data-placement "top"
                 :title "Anyone on the team can see this board. Contributors can edit."}
                [:span.board-edit-access-title "Team"]]
              [:div.board-edit-access-bt.board-edit-access-private-bt
                {:class (when (= (:access board-editing) "private") "selected")
                 :on-click (fn [_]
                            (let [current-authors (:authors board-editing)
                                  next-authors (if (first (filter #(= % current-user-id) current-authors))
                                                 current-authors
                                                 (vec (concat [current-user-id] current-authors)))
                                  next-board-editing (merge board-editing {:authors next-authors
                                                                           :access "private"})]
                              (dis/dispatch! [:input [:board-editing] next-board-editing])))
                 :data-toggle "tooltip"
                 :data-placement "top"
                 :title "Only team members you invite can see or edit this board."}
                [:span.board-edit-access-title "Private"]]
              [:div.board-edit-access-bt.board-edit-access-public-bt
                {:class (when (= (:access board-editing) "public") "selected")
                 :on-click #(dis/dispatch! [:input [:board-editing :access] "public"])
                 :data-toggle "tooltip"
                 :data-placement "top"
                 :title "This board is open for the public to see. Contributors can edit."}
                [:span.board-edit-access-title "Public"]]]]
          [:div.board-edit-divider]
          (when should-show-private-board-editing?
            (let [query  (::query s)
                  available-users (filter :user-id (:users roster))
                  addable-users (get-addable-users board-editing available-users)
                  filtered-users (filter-users addable-users @query)
                  ;; user can edit the private board users if
                  ;; he's creating a new board
                  ;; or if he's in the authors list of the existing board
                  can-change (or (not (seq (:slug board-editing)))
                                 (some #{current-user-id} (map :user-id (:authors board-data))))]
              [:div.board-edit-private-users-container
                (when (and can-change
                           (pos? (count addable-users)))
                  [:div.board-edit-private-users-search
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
                      [:div.board-edit-private-users-results
                        (for [u filtered-users
                              :let [team-user (some #(when (= (:user-id %) (:user-id u)) %) (:users roster))
                                    user (merge u team-user)
                                    user-type (utils/get-user-type user org-data board-data)]]
                          [:div.board-edit-private-users-result
                            {:on-click #(do
                                          (reset! query "")
                                          (reset! (::show-search-results s) false)
                                          (dis/dispatch! [:private-board-user-add user user-type]))
                             :ref (str "add-user-" (:user-id user))}
                            (user-avatar-image user)
                            [:div.name
                              (utils/name-or-email user)]])])])
                [:div.board-edit-private-users
                  (let [user-id @(::show-edit-user-dropdown s)
                        user-type (if (utils/in? (:viewers board-editing) user-id)
                                    :viewer
                                    :author)
                        team-user (some #(when (= (:user-id %) user-id) %) (:users team-data))]
                    [:div.board-edit-private-users-dropdown-container
                      {:style {:top (str (+ @(::show-edit-user-top s) -114) "px")
                               :display (if (seq @(::show-edit-user-dropdown s)) "block" "none")}}
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
                  [:div.board-edit-private-users-list.group
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
                                  self (= (:user-id user) current-user-id)]]
                        [:div.board-edit-private-user.group
                          {:ref (str "edit-user-" (:user-id user))
                           :on-click #(when (and can-change
                                                 (not self))
                                        (let [user-node (rum/ref-node s (str "edit-user-" (:user-id user)))
                                              top (- (.-offsetTop user-node) (.-scrollTop (.-parentElement user-node)))
                                              next-user-id (if (= @(::show-edit-user-dropdown s) (:user-id user))
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
                              {:class (when (not can-change) "no-dropdown")}
                              (if (= user-type :author)
                                "Full Access"
                                "Viewer")])]))]]]))
          (when should-show-private-board-editing?
            [:div.board-edit-divider])
          [:div.board-edit-footer
            [:div.board-edit-footer-left
              (when (and (seq (:slug board-data))
                         (utils/link-for (:links board-data) "delete"))
                [:button.mlb-reset.mlb-link-black.delete-board
                  {:on-click (fn []
                              (dis/dispatch!
                               [:alert-modal-show
                                {:icon "/img/ML/trash.svg"
                                 :action "delete-board"
                                 :message (str
                                           "Are you sure? This will delete the board and ALL its posts, too.")
                                 :link-button-title "No"
                                 :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                                 :solid-button-style :red
                                 :solid-button-title "Yes, I'm sure"
                                 :solid-button-cb (fn []
                                                    (dis/dispatch!
                                                     [:board-delete
                                                     (:slug board-data)])
                                                    (dis/dispatch!
                                                     [:alert-modal-hide])
                                                    (close-clicked s))}]))}
                  "Delete"])]
            [:div.board-edit-footer-right.group
              [:button.mlb-reset.mlb-default
                {:type "button"
                 :disabled (or (empty? (:name board-editing))
                               (empty? (:access board-editing)))
                 :on-click #(let [board-node (rum/ref-node s "board-name")
                                  inner-html (.-innerHTML board-node)
                                  board-name (utils/strip-HTML-tags
                                              (utils/emoji-images-to-unicode
                                               (gobj/get (utils/emojify inner-html) "__html")))]
                              (dis/dispatch! [:input [:board-editing :name] board-name])
                              (dis/dispatch! [:board-edit-save]))}
                (if new-board? "Create" "Save")]
              [:button.mlb-reset.mlb-link-black.cancel-btn
                {:type "button"
                 :on-click #(close-clicked s)}
                "Cancel"]]]]]]))