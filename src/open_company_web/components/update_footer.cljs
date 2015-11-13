(ns open-company-web.components.update-footer
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]))

(defn get-footer-id [data]
  (let [section-name (name (:section data))
        notes (if (:notes data)
                "-notes"
                "")]
    (str "update-footer-" section-name notes)))

(defn setup-popover [data owner]
  (when (.-$ js/window)
    (let [component-id (get-footer-id data)
          timeago-width (.width (.$ js/window (str "#" component-id)))
          hover-div (.$ js/window (str "#" component-id "-hover"))]
      (.css hover-div #js {"left" (str (+ timeago-width 15) "px")})
      (.hover (.$ js/window (str "#" component-id ", #" component-id "-hover"))
              #(om/update-state! owner :hover (fn [_]true))
              #(om/update-state! owner :hover (fn [_]false))))))

(defcomponent update-footer [data owner]
  (init-state [_]
    {:hover false})
  (did-mount [_]
    (setup-popover data owner))
  (did-update [_ _ _]
    (setup-popover data owner))
  (render [_]
    (dom/div {:class "update-footer"}
      (dom/span {:class "timeago"
                :id (get-footer-id data)}
               (utils/time-since (:updated-at data)))
      (dom/div {:class (utils/class-set {:update-footer-hover true
                                         :show (om/get-state owner :hover)})
                :id (str (get-footer-id data) "-hover")}
        (dom/a {:href (str "/user/" (:user-id (:author data)))
                :on-click #(.preventDefault %)}
          (dom/img {:src (:image (:author data)) :title (:name (:author data)) :class "author-image"})
          (dom/div {:class "author"} (:name (:author data))))))))