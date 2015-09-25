(ns open-company-web.components.section-selector
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.components.finances-pieces.finances :refer (finances finances-edit)]
            [open-company-web.components.simple-section :refer (simple-section)]))

(defcomponent section-selector [data owner]
  (render [_]
    (let [slug (:slug @router/path)
          section (:section @router/path)
          tab (:tab @router/path)
          company-data ((keyword slug) data)]
      (cond
        ; finances edit
        (and (= section "finances") (= tab "edit"))
        (om/build finances-edit data)
        ; finances
        (= section "finances")
        (om/build finances data)
        ; else it is a simple section
        (contains? company-data (keyword section))
        (om/build simple-section {:section section :company-data ((keyword slug) data)})
        ; section not found
        :else
        (dom/h4 {} (str "Section " section " not found"))))))