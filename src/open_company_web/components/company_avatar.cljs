(ns open-company-web.components.company-avatar
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]))

(defn get-px [n]
  (str n "px"))

(defcomponent company-avatar [data owner]
  (render [_]
    (let [slug (:slug @router/path)
          first-letter (if (contains? (:company-data data) :name)
                         (first (:name (:company-data data)))
                         (first slug))
          company-home (str "/companies/" slug)
          width (if (:width data)
                  (:width data)
                  40)
          height (if (:height data)
                   (:height data)
                   40)
          bd-radius (int (/ width 2))]
      (dom/div {:class (utils/class-set {:company-avatar true
                                         :navbar-brand (:navbar-brand data)})}
        (dom/a {:href company-home
                :on-click (fn [e]
                            (.preventDefault e)
                            (router/nav! company-home))}
          (dom/div {:class "company-avatar-img"
                    :style {:width (get-px width)
                            :height (get-px height)
                            :border-radius (get-px bd-radius)}
                    :src (jwt/get-key :avatar)
                    :title (jwt/get-key :real-name)}
            (dom/span {:class "company-avatar-name"} (clojure.string/upper-case first-letter))))))))