(ns open-company-web.components.recent-updates
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]))

(defcomponent recent-updates-item [data owner]
  (render [_]
    (let [section-title (if (= (:section data) "finances") "Finances" (:title data))
          href (str "/companies/" (:slug @router/path) "/" (:section data))]
      (dom/li {}
        (dom/a {:href href
                :on-click (fn [e]
                            (.preventDefault e)
                            (router/nav! href))}
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