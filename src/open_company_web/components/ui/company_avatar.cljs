(ns open-company-web.components.ui.company-avatar
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]))

(def df-company-avatar-size 40)

(defcomponent company-avatar [data owner]
  (render [_]
    (when (:company-data data)
      (let [company-data (:company-data data)
            slug (:slug @router/path)
            company-name (if (contains? company-data :name)
                           (:name company-data)
                           (utils/camel-case-str slug))
            first-letter (first (clojure.string/upper-case company-name))
            company-home (str "/companies/" slug "/profile")
            av-size (if (:size data)
                      (:size data)
                      df-company-avatar-size)
            px-size (utils/px av-size)
            bd-radius (utils/px (int (/ av-size 2)))
            company-logo (:logo company-data)]
        (dom/div {:class (utils/class-set {:company-avatar true
                                           :navbar-brand (:navbar-brand data)})}
          (dom/a {:href company-home
                  :on-click (fn [e]
                              (.preventDefault e)
                              (router/nav! company-home))}
            (if-not (clojure.string/blank? company-logo)
              (dom/img {:src company-logo
                        :class "company-avatar-img"
                        :title company-name})
              (dom/div {:class (utils/class-set {:company-avatar-img true
                                                 :no-image (clojure.string/blank? company-logo)})
                        :style {:width px-size
                                :height px-size
                                :border-radius bd-radius}}
                (dom/span {:class "company-avatar-initial"} (clojure.string/upper-case first-letter))))
            (dom/div {:class "company-avatar-name"} company-name)))))))