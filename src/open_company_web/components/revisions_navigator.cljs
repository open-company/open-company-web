(ns open-company-web.components.revisions-navigator
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]))

(defn nav-revision! [e rev]
  (.preventDefault e)
  (router/nav! rev))

(defcomponent revisions-navigator [data owner]
  (render [_]
    (let [revisions (:revisions data)
          as-of (:as-of (:query-params @router/path))
          as-of (if (nil? as-of) (utils/as-of-now) as-of)
          actual-revision (:actual-revision data)
          rev-first (utils/revision-first revisions as-of)
          rev-prev  (utils/revision-prev revisions as-of)
          rev-next  (utils/revision-next revisions as-of actual-revision)
          rev-last  (if rev-next (utils/revision-last revisions as-of actual-revision) nil)]
      (dom/div {:class "revisions-navigator"}
        (dom/div {:class "revisions-navigator-left"}
          (when rev-first
            (dom/a {:href rev-first
                    :on-click #(nav-revision! % rev-first)}
              (dom/div {:class "double-prev"}
                (dom/i {:class "fa fa-backward"}))))
          (when rev-prev
            (dom/a {:href rev-prev
                    :on-click #(nav-revision! % rev-prev)}
              (dom/div {:class "single-prev"}
                (dom/i {:class "fa fa-caret-left"})))))
        (dom/div {:class "revisions-navigator-right"}
          (when rev-last
            (dom/a {:href rev-last
                    :on-click #(nav-revision! % rev-last)}
              (dom/div {:class "double-next"}
                (dom/i {:class "fa fa-forward"}))))
          (when rev-next
            (dom/a {:href rev-next
                    :on-click #(nav-revision! % rev-next)}
              (dom/div {:class "single-next"}
                (dom/i {:class "fa fa-caret-right"})))))))))