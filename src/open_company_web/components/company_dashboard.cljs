(ns open-company-web.components.company-dashboard
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [cljs.core.async :refer (chan <!)]
            [open-company-web.components.company-header :refer [company-header]]
            [open-company-web.components.topic-list :refer [topic-list]]
            [open-company-web.components.navbar :refer (navbar)]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))

(defonce default-category "progress")

(defn set-save-bt-active [owner active]
  (om/set-state! owner :save-bt-active active))

(defn set-navbar-editing [owner editing]
  (when-not editing
    (set-save-bt-active owner false))
  (om/set-state! owner :navbar-editing editing))

(defn switch-tab-cb [owner new-tab]
  (om/set-state! owner :active-category new-tab))

(defcomponent company-dashboard [data owner]

  (init-state [_]
    (let [url-hash (.. js/window -location -hash)
          url-tab (subs url-hash 1 (count url-hash))
          active-tab (if (pos? (count url-tab))
                       url-tab
                       default-category)]
      {:active-category active-tab
       :navbar-editing false
       :save-bt-active false}))

  (render-state [_ {:keys [navbar-editing] :as state}]
    (let [slug (:slug @router/path)
          company-data ((keyword slug) data)]
      (dom/div {:class "company-dashboard row-fluid"}

        ;; navbar
        (om/build navbar (merge data {:show-share true
                                      :save-bt-active (om/get-state owner :save-bt-active)
                                      :edit-mode (om/get-state owner :navbar-editing)}))

        ;; company header
        (om/build company-header {:loading (:loading company-data)
                                  :company-data company-data
                                  :navbar-editing navbar-editing
                                  :switch-tab-cb (partial switch-tab-cb owner)
                                  :active-category (:active-category state)})

        ;; topic list
        (om/build topic-list
                  {:loading (:loading company-data)
                   :company-data company-data
                   :active-category (:active-category state)}
                  {:opts {:navbar-editing-cb (partial set-navbar-editing owner)
                          :save-bt-active-cb (partial set-save-bt-active owner)}})))))