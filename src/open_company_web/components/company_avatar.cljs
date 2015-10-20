(ns open-company-web.components.company-avatar
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]))

(def df-company-avatar-size 40)

(defcomponent company-avatar [data owner]
  (render [_]
    (when (:company-data data)
      (let [slug (:slug @router/path)
            first-letter (if (contains? (:company-data data) :name)
                           (first (:name (:company-data data)))
                           (first slug))
            company-home (str "/companies/" slug)
            av-size (if (:size data)
                      (:size data)
                      df-company-avatar-size)
            px-size (utils/px av-size)
            bd-radius (utils/px (int (/ av-size 2)))]
        (dom/div {:class (utils/class-set {:company-avatar true
                                           :navbar-brand (:navbar-brand data)})}
          (dom/a {:href company-home
                  :on-click (fn [e]
                              (.preventDefault e)
                              (router/nav! company-home))}
            (dom/div {:class "company-avatar-img"
                      :style {:width px-size
                              :height px-size
                              :border-radius bd-radius}
                      :src (jwt/get-key :avatar)
                      :title (jwt/get-key :real-name)}
              (dom/span {:class "company-avatar-name"} (clojure.string/upper-case first-letter)))))))))