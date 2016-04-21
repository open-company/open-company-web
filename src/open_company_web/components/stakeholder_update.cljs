(ns open-company-web.components.stakeholder-update
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]))

(defcomponent stakeholder-update [data owner]
  (render [_]
    (let [slug (keyword (:slug @router/path))
          su-slug (keyword (:update-slug @router/path))
          su-key (dispatcher/stakeholder-update-key slug)
          su-data (get-in data [su-key su-slug])]
      (if (:loading su-data)
        (dom/h2 {} "Loading...")
        (dom/div {:class "stakeholder-update"}
          (dom/h5 {} (:title su-data)))))))