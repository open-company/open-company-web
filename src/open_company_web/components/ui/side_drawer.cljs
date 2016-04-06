(ns open-company-web.components.ui.side-drawer
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy]
            [open-company-web.router :as router]
            [open-company-web.caches :as caches]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.topic-list-edit :refer (topic-list-edit)]))

(defn get-active-topics [company-data category]
  (if (= category "all")
    (apply concat (vals (:sections company-data)))
    (get-in company-data [:sections (keyword category)])))

(defn open-value [open]
  (if open "open" "close"))

(defcomponent side-drawer [data owner options]

  (init-state [_]
    {:open nil})

  (will-receive-props [_ next-props]
    (when-not (= (:open next-props) (:open data))
      (om/set-state! owner :open (open-value (:open next-props)))))

  (render-state [_ {:keys [open]}]
    (let [cat "company"
          slug (keyword (:slug @router/path))
          company-data (:company-data data)
          categories (:categories company-data)
          active-topics (apply merge (map #(hash-map (keyword %) (get-active-topics company-data %)) categories))]
      (dom/div {:class (utils/class-set {:side-drawer true
                                         :group true
                                         :open-drawer (= open "open")
                                         :close-drawer (= open "close")})}
        (dom/div {:class "side-drawer-internal"}
          (when (and open (slug @caches/new-sections))
            (om/build topic-list-edit
                      (merge data {:active true
                                   :category cat
                                   :new-sections (slug @caches/new-sections)
                                   :active-topics (get (:active-topics data) (keyword cat))})
                      {:key cat
                       :opts {:active-category (:active-category data)
                              :did-change-sort #()}}))))))) ; (partial update-active-topics owner options (keyword cat))