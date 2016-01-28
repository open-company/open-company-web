(ns open-company-web.components.company-dashboard
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [cljs.core.async :refer (chan <!)]
            [open-company-web.components.company-header :refer [company-header]]
            [open-company-web.components.topic-list :refer [topic-list]]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))

(defonce default-category "progress")

(defcomponent company-dashboard [data owner]

  (init-state [_]
    ;; Create an async channel to receive changes to the active category
    (let [category-chan (chan)]
      (utils/add-channel "change-category" category-chan))
    ;; Initial active category as the default
    {:active-category default-category})

  (will-mount [_]
    ;; Update the active category state when we get a change event
    (let [change-category (utils/get-channel "change-category")]
      (go (loop []
        (let [category (<! change-category)]
          (om/set-state! owner :active-category category)
          (recur))))))

  (render-state [_ state]
    (let [slug (:slug @router/path)
          company-data ((keyword slug) data)]
      (dom/div {:class "company-dashboard"}
        (om/build company-header (assoc data :active-category (:active-category state)))
        (om/build topic-list {:loading (:loading company-data)
                              :company-data company-data
                              :active-category (:active-category state)})))))