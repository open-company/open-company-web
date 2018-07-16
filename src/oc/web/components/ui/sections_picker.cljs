(ns oc.web.components.ui.sections-picker
  (:require [rum.core :as rum]
            [goog.events :as events]
            [cuerdas.core :as string]
            [goog.events.EventType :as EventType]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.section-editor :refer (section-editor)]))

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
        (for [b (sort-by :name group)
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
              (str (:entry-count b) " posts")]]))]))

(rum/defcs sections-picker < rum/reactive
                             (drv/drv :editable-boards)
                             (drv/drv :section-editing)
                             (rum/local false ::show-add-section)
                             (rum/local nil ::click-listener)
                             {:will-mount (fn [s]
                               (reset! (::click-listener s)
                                (events/listen js/window EventType/CLICK
                                 #(when-not (utils/event-inside? % (rum/dom-node s))
                                    (dis/dispatch! [:input [:show-sections-picker] false]))))
                               s)
                              :will-unmount (fn [s]
                               (when @(::click-listener s)
                                 (events/unlistenByKey @(::click-listener s))
                                 (reset! (::click-listener s) nil))
                               s)}
  [s active-slug on-change]
  (let [section-editing (drv/react s :section-editing)
        fixed-section-editing (when-not (string/blank? (:name section-editing))
                                section-editing)
        all-sections (filterv #(not= (:slug fixed-section-editing) (:slug %)) (vals (drv/react s :editable-boards)))
        team-sections (filterv #(= (:access %) "team") all-sections)
        fixed-team-sections (if (= (:access fixed-section-editing) "team")
                              (vec (concat [fixed-section-editing] team-sections))
                              team-sections)
        public-sections (filterv #(= (:access %) "public") all-sections)
        fixed-public-sections (if (= (:access fixed-section-editing) "public")
                                (vec (concat [fixed-section-editing] public-sections))
                                public-sections)
        private-sections (filterv #(= (:access %) "private") all-sections)
        fixed-private-sections (if (= (:access fixed-section-editing) "private")
                                 (vec (concat [fixed-section-editing] private-sections))
                                 private-sections)
        cfn #(pos? (count %))
        ;; Check if at least 2 groups are shown
        ;; logic: a? (b || c) : (b && c)
        should-show-headers? (if (cfn fixed-team-sections)
                                (or (cfn fixed-private-sections)
                                    (cfn fixed-public-sections))
                                (and (cfn fixed-private-sections)
                                     (cfn fixed-public-sections)))]
    [:div.sections-picker-container
      [:button.mlb-reset.mobile-modal-close-bt
        {:on-click #(on-change nil)}]
      (if @(::show-add-section s)
        (section-editor nil (fn [sec-data note]
                              (reset! (::show-add-section s) false)
                              (on-change sec-data note))
         true)
        [:div.sections-picker.group
          {:class (when should-show-headers? "show-headers")}
          [:div.sections-picker-header
            [:div.sections-picker-header-left
              "Sections"]
            [:div.sections-picker-header-right
              [:button.mlb-reset.add-new-section-bt
                {:on-click #(reset! (::show-add-section s) true)}
                "Add new section"]]]
          [:div.sections-picker-content
            (sections-group s fixed-team-sections should-show-headers?)
            (sections-group s fixed-public-sections should-show-headers?)
            (sections-group s fixed-private-sections should-show-headers?)
            (when (and (= (count all-sections) 1)
                       (= (:slug (first all-sections)) "general"))
              [:div.add-section-tooltip-container
                [:div.add-section-tooltip-arrow]
                [:div.add-section-tooltip
                  (str
                   "Keep posts organized by sections, e.g., "
                   "Announcements, and Design, Sales, and Marketing.")]])]])]))