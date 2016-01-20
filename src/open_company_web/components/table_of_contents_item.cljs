(ns open-company-web.components.table-of-contents-item
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.api :as api]
            [open-company-web.lib.section-utils :as section-utils]
            [open-company-web.components.popover :refer (add-popover hide-popover)]))

(defn show-delete-confirm-popover [section-title section-name]
  (add-popover {:container-id "delete-section-confirm"
                :title (str "Delete " section-title)
                :message "Are you sure you want to delete this section?"
                :cancel-title "CANCEL"
                :cancel-cb #(hide-popover nil "delete-section-confirm")
                :success-title "DELETE"
                :success-cb #(section-utils/remove-section section-name)
                :success-color-class "red"}))

(defcomponent table-of-contents-item [data owner]

  (render [_]
    (let [category (:category data)
          section (:section data)]
      (dom/div {:class (str "category-sortable category-" category)
                :id (str "section-sort--" (name section))}
        (dom/div {:class "category-section"}
          (dom/div {:class (utils/class-set {:category-section-close true
                                             :read-only (:read-only data)})
                    :on-click (fn [e]
                                (when-not (:read-only data)
                                  (show-delete-confirm-popover (:title data) (name section))))})
          (dom/a {:href "#"
                  :on-click (fn [e]
                              (.preventDefault e)
                              (utils/scroll-to-section (name section)))}
            (dom/span {:class "section-title"} (:title data))
            (dom/span {:class "section-date"} (utils/time-since (:updated-at data))))
          (dom/div {:class (utils/class-set {:category-section-sortable true
                                             :read-only (:read-only data)})}))))))