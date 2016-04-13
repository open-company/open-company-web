(ns open-company-web.components.company-dashboard
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [cljs.core.async :refer (chan <!)]
            [open-company-web.components.navbar :refer [navbar]]
            [open-company-web.components.company-header :refer [company-header]]
            [open-company-web.components.topic-list :refer [topic-list]]
            [open-company-web.components.navbar :refer (navbar)]
            [open-company-web.components.edit-topic :refer (edit-topic)]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))

(defonce default-category (if (utils/is-mobile) "progress" "all"))

(defn set-save-bt-active [owner active]
  (om/set-state! owner :save-bt-active active))

(defn set-navbar-editing [owner data editing & [title]]
  (if editing
    ; if ALL is selected switch to the first available category
    ; for editing purpose
    (do
      (om/set-state! owner :last-active-category (om/get-state owner :active-category))
      (when (= (om/get-state owner :active-category) "all")
        (let [slug (:slug @router/path)
              company-data ((keyword slug) data)]
          (om/set-state! owner :active-category (first (:categories company-data))))))
    (set-save-bt-active owner false))
  (let [fixed-title (or title "")]
    (om/set-state! owner :navbar-editing editing)
    (om/set-state! owner :navbar-title fixed-title)))

(defn switch-category-cb [owner new-category]
  ; reset the last edited topic when switching category
  (om/set-state! owner :last-editing-topic nil)
  (om/set-state! owner :active-category new-category))

(defn topic-edit-cb [owner section]
  (om/set-state! owner :editing-topic section)
  (utils/scroll-to-y 0))

(defn dismiss-topic-editing-cb [owner did-save]
  (let [state (om/get-state owner)]
    (om/set-state! owner :active-category (:last-active-category state))
    (om/set-state! owner :last-editing-topic (:editing-topic state))
    (om/set-state! owner :editing-topic nil)
    (om/set-state! owner :navbar-editing false)
    (om/set-state! owner :last-active-category nil)))

(defcomponent company-dashboard [data owner]

  (init-state [_]
    (let [url-hash (.. js/window -location -hash)
          url-tab (subs url-hash 1 (count url-hash))
          active-tab (if (pos? (count url-tab))
                       url-tab
                       default-category)
          fix-active-tab (if (and (utils/is-mobile) (= active-tab "all")) default-category active-tab)]
      {:active-category fix-active-tab
       :navbar-editing false
       :editing-topic false
       :save-bt-active false}))

  (render-state [_ {:keys [editing-topic navbar-editing save-bt-active active-category] :as state}]
    (let [slug (:slug @router/path)
          company-data ((keyword slug) data)
          navbar-editing-cb (partial set-navbar-editing owner data)]
      (dom/div {:class (utils/class-set {:company-dashboard true
                                         :navbar-offset (not (utils/is-mobile))})}

       (when-not (utils/is-mobile)
          (om/build navbar (merge data {:edit-mode navbar-editing
                                        :save-bt-active save-bt-active})))

        ;; company header
        (om/build company-header {:loading (:loading company-data)
                                  :company-data company-data
                                  :navbar-editing navbar-editing
                                  :editing-topic editing-topic
                                  :switch-category-cb (partial switch-category-cb owner)
                                  :active-category active-category
                                  :save-bt-active save-bt-active})

        (if-not editing-topic
          ;; topic list
          (om/build topic-list
                    {:loading (or (:loading company-data) (:loading data))
                     :company-data company-data
                     :active-category (:active-category state)
                     :expanded-topics (:expanded-topics data)}
                    {:opts {:navbar-editing-cb navbar-editing-cb
                            :topic-edit-cb (partial topic-edit-cb owner)
                            :switch-category-cb (partial switch-category-cb owner)
                            :save-bt-active-cb (partial set-save-bt-active owner)}})
          ;; topic edit
          (om/build edit-topic {:section editing-topic
                                :section-data (get company-data (keyword editing-topic))}
                    {:opts {:navbar-editing-cb navbar-editing-cb
                            :save-bt-active-cb (partial set-save-bt-active owner)
                            :dismiss-topic-editing-cb (partial dismiss-topic-editing-cb owner)}}))))))