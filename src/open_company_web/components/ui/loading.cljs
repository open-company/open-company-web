(ns open-company-web.components.ui.loading
  (:require [om.core :as om]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]))

(defcomponent loading [data owner]
  (render [_]
    (dom/div {:class (utils/class-set {:oc-loading true
                                       :active (:loading data)})}
      (dom/div {:class "oc-loading-internal"}))))