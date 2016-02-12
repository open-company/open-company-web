(ns open-company-web.components.edit-topic
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.lib.utils :as utils]))

(defcomponent edit-topic [data owner options]
  (render [_]
    (dom/div {} (str "Editing topic" (:section data)))))
