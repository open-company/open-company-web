(ns open-company-web.components.recent-updates
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]))

(defcomponent recent-updates-item [data owner]
  (render [_]
    (let [section (:section data)
          section-title (if (= section "finances") "Finances" (:title data))
          href (str "#" (name section))]
      (dom/li {}
        (dom/a {:href href
                :on-click (fn [e]
                            (.preventDefault e)
                            (let [section-el (.$ js/window (str "#section-" (name section)))
                                  section-offset (.offset section-el)
                                  top (- (.-top section-offset) 60)]
                              (.scrollTo js/$ #js {"top" (str top "px") "left" "0px"})))}
          (dom/div {:class "recent-updates-item"}
            (dom/p {:class "title"} section-title)
            (dom/span {:class "updated-at"} (utils/time-since (:updated-at data)))))))))

(defcomponent recent-updates [data owner]
  (render [_]
    (dom/div {:class "recent-updates"}
      (dom/button {:class "btn btn-default dropdown-toggle"
                   :type "button"
                   :data-toggle "dropdown"
                   :aria-haspopup true
                   :aria-expanded true}
                  "Recent updates"
        (dom/span {:class "caret"}))
      (dom/ul {:class "dropdown-menu recent-updates-list" :aria-labelledby "dropdownMenu1"}
        (let [sections (utils/sort-sections data)]
          (om/build-all recent-updates-item sections))))))