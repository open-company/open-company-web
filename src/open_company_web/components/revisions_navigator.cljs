(ns open-company-web.components.revisions-navigator
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]))

(defcomponent revisions-navigator [data owner]
  (render [_]
    (let [as-of (:as-of @router/path)
          rev-first (utils/revision-first data as-of)
          rev-prev (utils/revision-prev data as-of)
          rev-first (if (= rev-prev rev-first) nil rev-first)
          rev-next (utils/revision-next data as-of)
          rev-last (utils/revision-last data as-of)
          rev-last (if (= rev-next rev-last) nil rev-last)]
      (println "revision-navigator next:" rev-next)
      (println "revision-navigator prev:" rev-prev)
      (dom/div {:class "revisions-navigator"}
        (dom/div {:class "revisions-navigator-left"}
          (when rev-first
            (dom/a {:href rev-first}
              (dom/div {:class "double-prev"}
                (dom/i {:class "fa fa-backward"}))))
          (when rev-prev
            (dom/a {:href rev-prev}
              (dom/div {:class "single-prev"}
                (dom/i {:class "fa fa-caret-left"})))))
        (dom/div {:class "revisions-navigator-right"}
          (when rev-last
            (dom/a {:href rev-last}
              (dom/div {:class "double-next"}
                (dom/i {:class "fa fa-forward"}))))
          (when rev-next
            (dom/a {:href rev-next}
              (dom/div {:class "single-next"}
                (dom/i {:class "fa fa-caret-right"})))))))))