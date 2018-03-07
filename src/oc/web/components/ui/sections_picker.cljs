(ns oc.web.components.ui.sections-picker
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]))

(defn sections-group
  [sections-group active-slug sections-name should-show-headers]
  [:div.sections-picker-group
    (when (and should-show-headers
               (pos? (count sections-group)))
      [:div.sections-picker-group-header
        sections-name])
    (when (pos? (count sections-group))
      (for [b sections-group
            :let [active (= (:slug b) active-slug)]]
        [:div.sections-picker-section.group
          {:key (str "sections-picker-" (:uuid b))
           :class (when active "active")}
          [:div.sections-picker-circle]
          [:div.sections-picker-section-name
            (:name b)]
          [:div.sections-picker-section-posts
            (str (count (:fixed-items b)) " posts")]]))])

(rum/defcs sections-picker < rum/reactive
                             (drv/drv :editable-boards)
  [s view-title active-slug edit-input-key]
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
    (js/console.log "sections-picker/render" all-sections "-")
    [:div.sections-picker.group
      [:div.sections-picker-header
        [:div.sections-picker-header-left
          view-title]
        [:div.sections-picker-header-right
          [:button.mlb-reset.add-new-section-bt
            "Add new section"]]]
      [:div.sections-picker-content
        (sections-group team-sections active-slug "TEAM" should-show-headers?)
        (sections-group public-sections active-slug "PUBLIC" should-show-headers?)
        (sections-group private-sections active-slug "PRIVATE" should-show-headers?)]]))