(ns open-company-web.components.sidebar
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros (defcomponent)]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.components.ui.link :refer (link)]
              [open-company-web.router :as router]
              [open-company-web.lib.utils :as utils]))

(defcomponent sidebar-item [data owner]
  (render [_]
    (let [section (:section data)
          slug (:slug @router/path)
          selected-section (:section @router/path)]
      (dom/li {:class (if (= section selected-section) "active" "")}
        (om/build link {:href (str "/" slug "/" section) :name (utils/camel-case-str section)})))))

(defcomponent sidebar [data owner]
  (render [_]
    (let [slug (:slug @router/path)
          company-data ((keyword slug) data)
          sections (:sections company-data)
          sections-data (map #(merge {:section % :active false} {}) sections)]
      (dom/div {:class "col-mid-1 sidebar"}
        (dom/ul {:class "nav nav-sidebar"}
          (om/build-all sidebar-item sections-data))))))
