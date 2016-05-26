(ns open-company-web.components.login
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.footer :refer (footer)]
            [open-company-web.components.ui.login-required :refer (login-required)]))

(defcomponent login [data owner]
  (render [_]
    (let [card-width  (responsive/calc-card-width)]
      (dom/div {:class "login fullscreen-page"}
        (dom/div {:class "login-internal"}
          (om/build login-required data))
        (om/build footer {:su-preview false
                          :card-width card-width
                          :columns-num (responsive/columns-num)})))))
