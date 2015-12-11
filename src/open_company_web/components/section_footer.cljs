(ns open-company-web.components.section-footer
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]))

(defcomponent section-footer [data owner]

  (render [_]
    (let [editing (:editing data)]
      (dom/div {:class "section-footer"}
        (dom/div {:class "save-section"}
          (dom/button {:class (utils/class-set {:oc-btn true
                                                :oc-success true
                                                :disabled (:save-disabled data)
                                                :green true})
                       :disabled (:save-disabled data)
                       :on-click (fn [e]
                                   ((:save-cb data)))}
                      (if (:is-new-section data)
                        "CREATE"
                        "SAVE"))
          (dom/button {:class "oc-btn oc-cancel gray"
             :on-click (:cancel-cb data)}
            (if (:is-new-section data)
              "DELETE"
              "CANCEL")))))))