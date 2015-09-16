(ns open-company-web.components.finances.runaway
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.link :refer [link]]
            [open-company-web.router :as router]))

(defcomponent runaway [data owner]
  (render [_]
    (dom/h1 "Runaway")))