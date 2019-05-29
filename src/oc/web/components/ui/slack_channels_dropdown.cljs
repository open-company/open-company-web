(ns oc.web.components.ui.slack-channels-dropdown
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as string]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :refer (on-window-click-mixin)]))

(defn filter-team-channels [channels s]
  (let [look-for (string/lower
                   (if (string/starts-with? s "#")
                     (string/strip-prefix s "#")
                     s))]
    (filterv #(string/includes? (string/lower (:name %)) look-for) channels)))

(rum/defcs slack-channels-dropdown < (rum/local nil ::show-channels-dropdown)
                                     (rum/local nil ::field-value)
                                     (rum/local nil ::team-channels-requested)
                                     (rum/local "" ::slack-channel)
                                     (rum/local false ::typing)
                                     rum/reactive
                                     (on-window-click-mixin (fn [s e]
                                      (when (and @(::show-channels-dropdown s)
                                                (not (utils/event-inside? e (rum/dom-node s))))
                                       (reset! (::show-channels-dropdown s) false))))
                                     (drv/drv :team-data)
                                     (drv/drv :team-channels)
                                     {:will-mount (fn [s]
                                       (let [initial-value (:initial-value (nth (:rum/args s) 0))]
                                          (reset! (::slack-channel s) initial-value))
                                       (when (and (not @(drv/get-ref s :team-channels))
                                                  (not @(::team-channels-requested s)))
                                         (when-let [team-data @(drv/get-ref s :team-data)]
                                           (reset! (::team-channels-requested s) true)
                                           (dis/dispatch! [:channels-enumerate (:team-id team-data)])))
                                       s)
                                      :did-remount (fn [o s]
                                       (when (and (not @(drv/get-ref s :team-channels))
                                                  (not @(::team-channels-requested s)))
                                         (when-let [team-data @(drv/get-ref s :team-data)]
                                           (reset! (::team-channels-requested s) true)
                                           (dis/dispatch! [:channels-enumerate (:team-id team-data)])))
                                       s)}
  [s {:keys [disabled initial-value on-change on-intermediate-change] :as data}]
  (let [slack-teams (drv/react s :team-channels)]
    [:div.slack-channels-dropdown
      {:class (if disabled "disabled" "")
       :on-click #(when-not disabled
                    (reset! (::typing s) false)
                    (reset! (::show-channels-dropdown s) (not @(::show-channels-dropdown s)))
                    (utils/event-stop %))}
      [:input.board-edit-slack-channel.oc-input
        {:value (or @(::slack-channel s) "")
         :placeholder (if disabled "Not connected" "Select a channel...")
         :on-focus (fn []
                    (utils/after
                     100
                     #(do (reset! (::typing s) false) (reset! (::show-channels-dropdown s) true))))
         :on-change #(do
                       (reset! (::typing s) true)
                       (when (fn? on-intermediate-change)
                         (on-intermediate-change (.. % -target -value)))
                       (reset! (::slack-channel s) (.. % -target -value)))
         :disabled disabled}]
      (when @(::show-channels-dropdown s)
        [:div.slack-channels-dropdown-list
          (for [t slack-teams
                :let [chs (if (and @(::typing s) @(::slack-channel s))
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
               [:div.channel.group
                 {:value (:id c)
                  :key (str "slack-chs-dd-" (:slack-org-id t) "-" (:id c))
                  :on-click #(do
                                (on-change t c)
                                (utils/event-stop %)
                                (reset! (::slack-channel s) (str "#" (:name c)))
                                (reset! (::show-channels-dropdown s) false)
                                (reset! (::typing s) false))}
                  [:span.ch-prefix "#"]
                  [:span.ch (:name c)]])])])]))