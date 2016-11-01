(ns open-company-web.components.prior-updates
  (:require [rum.core :as rum]
            [cljs-time.format :as f]
            [org.martinklepsch.derivatives :as drv]
            [open-company-web.components.ui.icon :as i]
            [open-company-web.components.ui.popover :as popover]
            [open-company-web.api :as api]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.lib.utils :as utils]
            [open-company-web.urls :as urls]))

(def link-formatter (f/formatter "yyyy-MM-dd"))
(def human-formatter (f/formatter "MMMM d, yyyy"))

(rum/defcs prior-updates
  < {:did-mount (fn [s] 
                  (api/get-su-list) 
                  s)}
    (drv/drv :su-list)
    (drv/drv :jwt)
    rum/reactive
  [s]
  (let [company-slug (router/current-company-slug)]
  
    [:div.oc-popover {:style {:height "450px" :width "500px" } :on-click (fn [e] (.stopPropagation e))}
      
      [:button {:class "absolute top-0 btn-reset" :style {:left "100%"}
                :on-click (fn [e] (popover/hide-popover e "prior-updates-dialog"))}
            (i/icon :simple-remove {:class "inline mr1" :stroke "4" :color "white" :accent-color "white"})]

      [:h3.m0.px2.py25.gray5.domine
        {:style {:border-bottom  "solid 1px rgba(78, 90, 107, 0.1)"}}
        "Prior Updates"]
      
      (let [updates (reverse (drv/react s :su-list))]
        (if (empty? updates)
          
          [:div.message-container
            [:p.message "There's nothing to see here."]
            [:p.message "Start sharing!"]]

          [:div.update-container.px2.pt2
            (for [update updates]
              (let [user-id (:user-id (drv/react s :jwt))
                    author-id (-> update :author :user-id)
                    same-user? (= user-id author-id)
                    iso-date (:created-at update)
                    js-date (utils/js-date iso-date)
                    month (inc (.getMonth js-date))
                    day (.getDate js-date)
                    year (.getFullYear js-date)
                    month-string (utils/month-string-int month)
                    link-date (str year "-" (utils/add-zero month) "-" (utils/add-zero day))
                    human-date (str month-string " " day ", " year)
                    update-slug (:slug update)
                    link (urls/stakeholder-update company-slug link-date update-slug)
                    title (if (clojure.string/blank? (:title update))
                            (str (:name (dispatcher/company-data)) " Update")
                            (:title update))
                    author (if same-user? "You" (-> update :author :name))
                    medium (case (keyword (:medium update))
                                :email [:div.medium "by " (i/icon :email-84 {:size 13 :class "inline"})]
                                :slack [:span "to " [:i {:class "fa fa-slack"}]]
                                :legacy ""
                                [:div.medium "a " (i/icon :link-72 {:size 13 :class "inline"})])]
                [:div.update {:key update-slug}
                  [:div.update-title.domine [:a {:href link :target "_new"} title]]
                  [:div.update-details.domine author " shared " medium " on " human-date]]))]))]))