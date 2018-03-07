(ns oc.web.components.ui.sections-picker
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

(defn get-section-name [group]
  (case (-> group first :access)
    "team" "TEAM"
    "public" "PUBLIC"
    "private" "PRIVATE"))

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

(def private-access
  [:div.access-item.private-access
    "Only team members you invite"])

(def team-access
  [:div.access-item.team-access
    "Anyone on the team"])

(def public-access
  [:div.access-item.public-access
    "Open for the public"])

(rum/defcs sections-picker < rum/reactive
                             (drv/drv :editable-boards)
                             (rum/local :team ::access)
                             (rum/local false ::show-add-board)
                             (rum/local false ::show-access-list)
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
        [:div.sections-picker-add
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
          [:div.sections-picker-add-footer
            [:button.mlb-reset.create-bt
              "Create"]]]
        [:div.sections-picker-content
          (sections-group s team-sections should-show-headers?)
          (sections-group s public-sections should-show-headers?)
          (sections-group s private-sections should-show-headers?)])]))