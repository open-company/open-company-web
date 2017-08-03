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
  (let [look-for (if (string/starts-with? s "#")
                   (string/strip-prefix s "#")
                   s)]
    (vec (filter #(string/includes? (:name %) look-for) channels))))

(rum/defcs board-edit < rum/reactive
                        (drv/drv :board-editing)
                        (drv/drv :current-user-data)
                        (drv/drv :org-data)
                        (drv/drv :team-data)
                        (drv/drv :team-channels)
                        (rum/local false ::first-render-done)
                        (rum/local false ::dismiss)
                        (rum/local false ::team-channels-requested)
                        (rum/local "" ::slack-channel)
                        (rum/local [] ::channels)
                        (rum/local false ::show-channels-dropdown)
                        (rum/local nil ::window-click)
                        (rum/local true ::slack-enabled)
                        {:will-mount (fn [s]
                                      (dis/dispatch! [:teams-get])
                                      s)
                         :did-remount (fn [s]
                                        (when (and (not @(drv/get-ref s :team-channels))
                                                   (not @(::team-channels-requested s)))
                                          (when-let [team-data @(drv/get-ref s :team-data)]
                                            (reset! (::team-channels-requested s) true)
                                            (dis/dispatch! [:channels-enumerate (:team-id team-data)])))
                                        s)
                         :did-mount (fn [s]
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
                         :before-render (fn [s]
                                          (let [chs (:channels (first @(drv/get-ref s :team-channels)))]
                                            (if (empty? @(::slack-channel s))
                                              (assoc s ::channels (atom chs))
                                              (assoc s ::channels (atom (filter-team-channels chs @(::slack-channel s)))))))
                         :will-unmount (fn [s]
                                         ;; Remove no-scroll class from the body tag
                                         (dommy/remove-class! (sel1 [:body]) :no-scroll)
                                         (events/unlistenByKey @(::window-click s))
                                         s)}
  [s]
  (let [current-user-data (drv/react s :current-user-data)
        board-editing (drv/react s :board-editing)
        new-board? (not (contains? board-editing :links))
        show-slack-channels? (not (empty? (:slug board-editing)))]
    [:div.board-edit-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(::first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(::first-render-done s))})}
      (when @(::show-channels-dropdown s)
        [:div.board-edit-slack-channels-dropdown
          (for [c @(::channels s)]
            [:div.channel
              {:value (:id c)
               :on-click #(do
                            (dis/dispatch! [:input [:board-editing :channel] c])
                            (reset! (::slack-channel s) (str "#" (:name c)))
                            (reset! (::show-channels-dropdown s) false))}
              [:span.ch "#"]
              (:name c)])])
      [:div.board-edit
        {:class (when show-slack-channels? "show-slack-channels")}
        [:div.board-edit-header.group
          (user-avatar-image current-user-data)
          (if new-board?
            [:div.title "Creating a new Board"]
            [:div.title "Editing " [:span.board-name (:name board-editing)]])]
        [:div.board-edit-divider]
        [:div.board-edit-body
          [:div.board-edit-name-label-container.group
            [:div.board-edit-label.board-edit-name-label "BOARD NAME"]
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
          [:div.board-edit-label.board-edit-access-label "BOARD PERMISSIONS"]
          [:div.board-edit-access-field.group
            [:div.board-edit-access-bt.board-edit-access-team-bt
              {:class (when (= (:access board-editing) "team") "selected")
               :on-click #(dis/dispatch! [:input [:board-editing :access] "team"])
               :data-toggle "tooltip"
               :data-placement "top"
               :title "All team members can view this board. Authors can edit and share."}
              [:span.board-edit-access-title "Team" [:span.more-info]]]
            [:div.board-edit-access-bt.board-edit-access-private-bt
              {:class (when (= (:access board-editing) "private") "selected")
               :on-click #(dis/dispatch! [:input [:board-editing :access] "private"])
               :data-toggle "tooltip"
               :data-placement "top"
               :title "Only invited team members can view, edit and share this board."}
              [:span.board-edit-access-title "Private" [:span.more-info]]]
            [:div.board-edit-access-bt.board-edit-access-public-bt
              {:class (when (= (:access board-editing) "public") "selected")
               :on-click #(dis/dispatch! [:input [:board-editing :access] "public"])
               :data-toggle "tooltip"
               :data-placement "top"
               :title "This board is public to everyone and could show up in search engines like Google. Team authors can edit and share information."}
              [:span.board-edit-access-title "Public" [:span.more-info]]]]]
        [:div.board-edit-divider]
        (when show-slack-channels?
          [:div.board-edit-slack-channels-container
            [:div.board-edit-slack-channels-label.group
              [:div.title
                "Connect comments to Slack"
                [:span.more-info]]
              (carrot-checkbox {:selected @(::slack-enabled s)
                                :did-change-cb #(reset! (::slack-enabled s) %)})]
            [:div.board-edit-slack-channels-field
              [:input.board-edit-slack-channel
                {:value @(::slack-channel s)
                 :on-focus (fn [] (utils/after 100 #(reset! (::show-channels-dropdown s) true)))
                 :on-change #(reset! (::slack-channel s) (.. % -target -value))
                 :disabled (not @(::slack-enabled s))
                 :placeholder "Select Channel..."}]]])
        [:div.board-edit-footer
          [:div.board-edit-footer-left
            (when (and (not (empty? (:slug board-editing)))
                       (utils/link-for (:links board-editing) "delete"))
              [:button.mlb-reset.mlb-link-black
                {:on-click (fn []
                            (dis/dispatch! [:alert-modal-show {:icon "/img/ML/trash.svg"
                                                               :message "Delete this board?"
                                                               :first-button-title "No"
                                                               :first-button-cb #(dis/dispatch! [:alert-modal-hide])
                                                               :second-button-title "Yes"
                                                               :second-button-cb #(do
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