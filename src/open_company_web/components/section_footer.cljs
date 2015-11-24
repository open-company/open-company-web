(ns open-company-web.components.section-footer
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.revisions-navigator :refer (revisions-navigator)]))

(defcomponent section-footer [data owner]

  (render [_]
    (let [editing (:editing data)]
      (dom/div {:class "section-footer"}
        (dom/div {:class "save-section"}
          (dom/button {:class "oc-btn oc-cancel"
                       :on-click (:cancel-cb data)}
                      "CANCEL")
          (dom/button {:class "oc-btn oc-success"
                       :on-click (fn [e]
                                   ((:save-cb data)))}
                      "SAVE"))))))