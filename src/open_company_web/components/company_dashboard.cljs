(ns open-company-web.components.company-dashboard
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [cljs.core.async :refer (chan <!)]
            [open-company-web.components.company-header :refer [company-header]]
            [open-company-web.components.topic-list :refer [topic-list]]
            [open-company-web.components.navbar :refer (navbar)]
            [open-company-web.components.edit-topic :refer (edit-topic)]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))

(defonce default-category "progress")

(defn set-navbar-editing [owner editing & [title]]
  (let [fixed-title (or title "")]
    (om/set-state! owner :navbar-editing editing)
    (om/set-state! owner :navbar-title fixed-title)))

(defn switch-tab-cb [owner new-tab]
  (om/set-state! owner :active-category new-tab))

(defn topic-edit-cb [owner section]
  (om/set-state! owner :editing-topic section)
  (utils/scroll-to-y 0))

(defn dismiss-topic-editing-cb [owner did-save]
  (om/set-state! owner {:editing-topic nil
                        :navbar-editing false
                        :active-category (om/get-state owner :active-category)}))

(defcomponent company-dashboard [data owner]

  (init-state [_]
    (let [url-hash (.. js/window -location -hash)
          url-tab (subs url-hash 1 (count url-hash))
          active-tab (if (pos? (count url-tab))
                       url-tab
                       default-category)]
      {:active-category active-tab
       :navbar-editing false
       :editing-topic false}))

  (render-state [_ {:keys [editing-topic] :as state}]
    (let [slug (:slug @router/path)
          company-data ((keyword slug) data)
          navbar-editing-cb (partial set-navbar-editing owner)]
      (dom/div {:class "company-dashboard row-fluid"}

        ;; navbar
        (om/build navbar (merge data {:show-share true
                                      :edit-mode (:navbar-editing state)
                                      :edit-title (:navbar-title state)}))
        (if-not editing-topic
          (dom/div {}
            ;; company header
            (om/build company-header {:loading (:loading company-data)
                                      :company-data company-data
                                      :switch-tab-cb (partial switch-tab-cb owner)
                                      :active-category (:active-category state)})

            ;; topic list
            (om/build topic-list
                      {:loading (:loading company-data)
                       :company-data company-data
                       :active-category (:active-category state)}
                      {:opts {:navbar-editing-cb navbar-editing-cb
                              :topic-edit-cb #(topic-edit-cb owner %)}}))
          ;; topic edit
          (om/build edit-topic {:section editing-topic
                                :section-data (get company-data (keyword editing-topic))}
                    {:opts {:navbar-editing-cb navbar-editing-cb
                            :dismiss-topic-editing-cb (partial dismiss-topic-editing-cb owner)}}))))))