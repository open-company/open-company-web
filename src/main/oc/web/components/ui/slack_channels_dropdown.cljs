(ns oc.web.components.ui.slack-channels-dropdown
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.actions.team :as team-actions]
            [oc.web.utils.dom :as du]
            [oc.web.utils.board :as bu]
            [clojure.string :as cstr]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.mixins.ui :refer (on-click-out strict-refresh-tooltips-mixin)]))

(def premium-slack-channels-limit-copy "Premium accounts don't have limits on the number of Slack auto-shared channels.")

(defn- sort-team-channels [channels]
  (sort-by (juxt :channel-type :name) channels))

(defn- filter-team-channels [team channels search-string]
  (if (seq search-string)
    (let [prepare-search-string (cstr/replace search-string #"\s" ".")
          rx (re-pattern (str "(?i)" prepare-search-string))
          lookup #(or (re-find rx (:name %))
                      (re-find rx (:name team)))]
      (filterv lookup channels)
      (->> channels
          (filterv lookup)
          (sort-team-channels)))
    (sort-team-channels channels)))

(defn- channel-label [channel show-checkmark?]
  (let [prefix (bu/prefix-for-channel channel)]
    [:div.channel-inner
     {:class (when show-checkmark? "selected")}
     (when show-checkmark?
       [:i.mdi.mdi-check])
     [:span.ch-prefix
      prefix]
     [:span.ch
      (or (:name channel) (:id channel))]]))

(defn- dismiss-dropdown
  [s]
  (reset! (::show-channels-dropdown s) false)
  (when (fn? (-> s :rum/args first :on-dismiss))
    ((-> s :rum/args first :on-dismiss))))

(rum/defcs slack-channels-dropdown < (rum/local nil ::show-channels-dropdown)
                                     (rum/local "" ::search-str)
                                     rum/reactive
                                     strict-refresh-tooltips-mixin
                                     (on-click-out (fn [s e]
                                                     (when (and @(::show-channels-dropdown s)
                                                                (not (du/event-container-has-class e :section-editor-add-slack-channel)))
                                                       (dismiss-dropdown s))))
                                     (drv/drv :team-channels)
                                     {:will-mount (fn [s]
                                       (reset! (::search-str s) (-> s :rum/args first :initial-value))
                                       (team-actions/enumerate-channels)
                                       s)}
  [s {:keys [disabled initial-value on-change on-dismiss on-intermediate-change
             prevent-dismiss-on-change? hide-active-channels? selected-channels
             placeholder block-channel-add?]}]
  (let [slack-teams (drv/react s :team-channels)
        check-selected-fn (bu/contains-channel? selected-channels)]
    [:div.slack-channels-dropdown
      {:class (if disabled "disabled" "")
       :on-click #(reset! (::show-channels-dropdown s) true)}
      [:input.board-edit-slack-channel.oc-input
        {:value (or @(::search-str s) "")
         :placeholder (cond disabled
                            "Not connected"
                            (seq placeholder)
                            placeholder
                            :else
                            "Search channels or users...")
         :on-focus (fn []
                    (reset! (::show-channels-dropdown s) true))
         :on-change (fn [e]
                      (reset! (::show-channels-dropdown s) true)
                      (when (fn? on-intermediate-change)
                        (on-intermediate-change (.. e -target -value)))
                      (reset! (::search-str s) (.. e -target -value)))
         :disabled disabled}]
      (when @(::show-channels-dropdown s)
        [:div.slack-channels-dropdown-list
          (for [t slack-teams
                :let [chs (filter-team-channels t (:channels t) @(::search-str s))
                      show-slack-team-name true ;(and (> (count slack-teams) 1)
                                                ;(pos? (count chs)))
                ]]
            [:div.slack-team
              {:class (when show-slack-team-name "show-slack-name")
               :key (str "slack-chs-dd-" (:slack-org-id t))}
              (when show-slack-team-name
                [:div.slack-team-name (:name t)])
              (for [c chs
                    :let [selected? (when (fn? check-selected-fn) (check-selected-fn t c))
                          show-checkmark? (and (not hide-active-channels?)
                                               selected?)
                          label (channel-label c show-checkmark?)
                          block-ch? (and (not selected?)
                                         block-channel-add?)]
                    :when (or (not hide-active-channels?)
                              (not selected?))]
               [:div.channel.group
                 {:value (:id c)
                  :key (str "slack-chs-dd-" (when block-ch? "locked-") (:slack-org-id t) "-" (:id c))
                  :data-toggle (when block-ch? "tooltip")
                  :data-placement "top"
                  :data-container "body"
                  :title (when block-ch?
                           (str premium-slack-channels-limit-copy " Click for details"))
                  :class (du/class-set {:selected selected?
                                        :block-add block-ch?})
                  :on-click (fn [e]
                              (du/event-stop! e)
                              (if block-ch?
                                (nav-actions/toggle-premium-picker! premium-slack-channels-limit-copy)
                                (do
                                  (on-change t c)
                                  (when-not prevent-dismiss-on-change?
                                    (dismiss-dropdown s)))))}
                label])])])]))