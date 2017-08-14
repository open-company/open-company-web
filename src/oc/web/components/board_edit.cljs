(ns oc.web.components.board-edit
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [cuerdas.core :as string]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn dismiss-modal []
  (dis/dispatch! [:board-edit/dismiss]))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 #(dismiss-modal)))

(defn filter-team-channels [channels s]
  (let [look-for (string/lower
                   (if (string/starts-with? s "#")
                     (string/strip-prefix s "#")
                     s))]
    (vec (filter #(string/includes? (string/lower (:name %)) look-for) channels))))

(rum/defcs board-edit < rum/reactive
                        (drv/drv :board-editing)
                        (drv/drv :current-user-data)
                        (drv/drv :org-data)
                        (drv/drv :board-data)
                        (drv/drv :team-data)
                        (drv/drv :team-channels)
                        (rum/local false ::first-render-done)
                        (rum/local false ::dismiss)
                        (rum/local false ::team-channels-requested)
                        (rum/local "" ::slack-channel)
                        (rum/local false ::show-channels-dropdown)
                        (rum/local nil ::window-click)
                        (rum/local true ::slack-enabled)
                        {:will-mount (fn [s]
                                      (dis/dispatch! [:teams-get])
                                      (let [board-data @(drv/get-ref s :board-data)]
                                        (when (:channel-id (:slack-mirror board-data))
                                          (reset! (::slack-channel s) (or (str "#" (:channel-name (:slack-mirror board-data))) ""))))
                                      s)
                         :did-mount (fn [s]
                                      (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
                                      ;; Add no-scroll to the body to avoid scrolling while showing this modal
                                      (dommy/add-class! (sel1 [:body]) :no-scroll)
                                      (when (and (not @(drv/get-ref s :team-channels))
                                                 (not @(::team-channels-requested s)))
                                          (when-let [team-data @(drv/get-ref s :team-data)]
                                            (reset! (::team-channels-requested s) true)
                                            (dis/dispatch! [:channels-enumerate (:team-id team-data)])))
                                      (reset! (::window-click s)
                                        (events/listen js/window EventType/CLICK
                                          #(when (and @(::show-channels-dropdown s)
                                                      (not (utils/event-inside? % (sel1 [:div.board-edit-slack-channels-dropdown])))
                                                      (not (utils/event-inside? % (sel1 [:input.board-edit-slack-channel]))))
                                             (reset! (::show-channels-dropdown s) false))))
                                      s)
                         :after-render (fn [s]
                                         (when (not @(::first-render-done s))
                                           (reset! (::first-render-done s) true))
                                         s)
                         :did-remount (fn [s]
                                        (when (and (not @(drv/get-ref s :team-channels))
                                                   (not @(::team-channels-requested s)))
                                          (when-let [team-data @(drv/get-ref s :team-data)]
                                            (reset! (::team-channels-requested s) true)
                                            (dis/dispatch! [:channels-enumerate (:team-id team-data)])))
                                        s)
                         :will-unmount (fn [s]
                                         ;; Remove no-scroll class from the body tag
                                         (dommy/remove-class! (sel1 [:body]) :no-scroll)
                                         (events/unlistenByKey @(::window-click s))
                                         s)}
  [s]
  (let [current-user-data (drv/react s :current-user-data)
        board-editing (drv/react s :board-editing)
        new-board? (not (contains? board-editing :links))
        slack-teams (drv/react s :team-channels)
        show-slack-channels? (and (not (empty? (:slug board-editing)))
                                  (pos? (apply + (map #(-> % :channels count) slack-teams))))
        label (if (= (:type board-editing) "story") "storyboard" "board")]
    [:div.board-edit-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(::first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(::first-render-done s))})}
      (when @(::show-channels-dropdown s)
        [:div.board-edit-slack-channels-dropdown
          (for [t slack-teams
                :let [chs (if @(::slack-channel s)
                            (filter-team-channels (:channels t) @(::slack-channel s))
                            (:channels t))
                      show-slack-team-name (and (> (count slack-teams) 1)
                                                (pos? (count chs)))]]
            [:div.slack-team
              {:class (when show-slack-team-name "show-slack-name")
               :key (str "slack-chs-dd-" (:slack-org-id t))}
              (when show-slack-team-name
                [:div.slack-team-name (:name t)])
              (for [c chs]
               [:div.channel
                 {:value (:id c)
                  :key (str "slack-chs-dd-" (:slack-org-id t) "-" (:id c))
                  :on-click #(do
                               (dis/dispatch! [:input [:board-editing :slack-mirror :channel-id] (:id c)])
                               (dis/dispatch! [:input [:board-editing :slack-mirror :channel-name] (:name c)])
                               (dis/dispatch! [:input [:board-editing :slack-mirror :slack-org-id] (:slack-org-id t)])
                               (reset! (::slack-channel s) (str "#" (:name c)))
                               (reset! (::show-channels-dropdown s) false))}
                  [:span.ch-prefix "#"]
                  [:span.ch (:name c)]])])])
      [:div.board-edit
        {:class (when show-slack-channels? "show-slack-channels")}
        [:div.board-edit-header.group
          (user-avatar-image current-user-data)
          (if new-board?
            [:div.title (str "Creating a new " (string/capital label))]
            [:div.title "Editing " [:span.board-name (:name board-editing)]])]
        [:div.board-edit-divider]
        [:div.board-edit-body
          [:div.board-edit-name-label-container.group
            [:div.board-edit-label.board-edit-name-label (str (string/upper label) " NAME")]
            (when (:board-name-error board-editing)
              [:div.board-name-error (:board-name-error board-editing)])]
          [:input.board-edit-name-field
            {:type "text"
             :class (when (:board-name-error board-editing) "board-name-error")
             :value (:name board-editing)
             :on-change #(do
                          (dis/dispatch! [:input [:board-editing :name] (.. % -target -value)])
                          (dis/dispatch! [:input [:board-editing :board-name-error] nil]))
             :placeholder "Product, Development, Finance, Operations, etc."}]
          [:div.board-edit-label.board-edit-access-label (str (string/upper label) " PERMISSIONS")]
          [:div.board-edit-access-field.group
            [:div.board-edit-access-bt.board-edit-access-team-bt
              {:class (when (= (:access board-editing) "team") "selected")
               :on-click #(dis/dispatch! [:input [:board-editing :access] "team"])
               :data-toggle "tooltip"
               :data-placement "top"
               :title (str "All team members can view this " label ". Authors can edit and share.")}
              [:span.board-edit-access-title "Team"]]
            [:div.board-edit-access-bt.board-edit-access-private-bt
              {:class (when (= (:access board-editing) "private") "selected")
               :on-click #(dis/dispatch! [:input [:board-editing :access] "private"])
               :data-toggle "tooltip"
               :data-placement "top"
               :title (str "Only invited team members can view, edit and share this " label ".")}
              [:span.board-edit-access-title "Private"]]
            [:div.board-edit-access-bt.board-edit-access-public-bt
              {:class (when (= (:access board-editing) "public") "selected")
               :on-click #(dis/dispatch! [:input [:board-editing :access] "public"])
               :data-toggle "tooltip"
               :data-placement "top"
               :title (str "This " label " is public to everyone and could show up in search engines like Google. Team authors can edit and share information.")}
              [:span.board-edit-access-title "Public"]]]]
        [:div.board-edit-divider]
        (when show-slack-channels?
          [:div.board-edit-slack-channels-container
            [:div.board-edit-slack-channels-label.group
              [:div.title
                "Connect comments to Slack"
                [:span.more-info]]
              (carrot-checkbox {:selected @(::slack-enabled s)
                                :did-change-cb #(do
                                                  (reset! (::slack-enabled s) %)
                                                  (when-not %
                                                    (dis/dispatch! [:input [:board-editing :slack-mirror] nil])))})]
            [:div.board-edit-slack-channels-field
              {:class (when (not @(::slack-enabled s)) "disabled")}
              [:input.board-edit-slack-channel
                {:value @(::slack-channel s)
                 :on-focus (fn [] (utils/after 100 #(reset! (::show-channels-dropdown s) true)))
                 :on-change #(reset! (::slack-channel s) (.. % -target -value))
                 :disabled (not @(::slack-enabled s))
                 :placeholder "Select Channel..."}]
              [:i.fa
                {:class (utils/class-set {:fa-angle-down (not @(::show-channels-dropdown s))
                                          :fa-angle-up @(::show-channels-dropdown s)})
                 :on-click #(do
                              (reset! (::show-channels-dropdown s) (not @(::show-channels-dropdown s)))
                              (utils/event-stop %))}]]])
        [:div.board-edit-footer
          [:div.board-edit-footer-left
            (when (and (not (empty? (:slug board-editing)))
                       (utils/link-for (:links board-editing) "delete"))
              [:button.mlb-reset.mlb-link-black
                {:on-click (fn []
                            (dis/dispatch! [:alert-modal-show {:icon "/img/ML/trash.svg"
                                                               :message (str "Delete this " label "?")
                                                               :link-button-title "No"
                                                               :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                                                               :solid-button-title "Yes"
                                                               :solid-button-cb (fn []
                                                                                  (dis/dispatch! [:board-delete (:slug board-editing)])
                                                                                  (dis/dispatch! [:alert-modal-hide]))}]))}
                "Delete"])]
          [:div.board-edit-footer-right.group
            [:button.mlb-reset.mlb-default
              {:type "button"
               :disabled (or (empty? (:name board-editing))
                             (empty? (:access board-editing)))
               :on-click #(dis/dispatch! [:board-edit-save])}
              (if new-board? "Create" "Save")]
            [:button.mlb-reset.mlb-link-black
              {:type "button"
               :on-click #(close-clicked s)}
              "Cancel"]]]]]))