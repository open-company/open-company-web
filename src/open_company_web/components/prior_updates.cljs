(ns open-company-web.components.prior-updates
  (:require [rum.core :as rum]
            [cljs-time.format :as f]
            [org.martinklepsch.derivatives :as drv]
            [open-company-web.urls :as oc-urls]
            [open-company-web.components.ui.icon :as i]
            [open-company-web.components.ui.popover :as popover]
            [open-company-web.api :as api]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.lib.utils :as utils]
            [open-company-web.urls :as urls]
            [open-company-web.lib.responsive :as responsive]))

(def link-formatter (f/formatter "yyyy-MM-dd"))
(def human-formatter (f/formatter "MMMM d, yyyy"))

(def desktop-width 500)

(defn- half-offset [pixels]
  (str "-" (Math/round (* 0.5 pixels)) "px"))

(rum/defcs prior-updates
  < {:after-render (fn [s]
                     (when (and (dispatcher/company-data)
                                (not (:su-list-loading @dispatcher/app-state))
                                (not (get-in @dispatcher/app-state (dispatcher/su-list-key (router/current-company-slug)))))
                       (dispatcher/dispatch! [:get-su-list]))
                   s)}
    (drv/drv :su-list)
    (drv/drv :jwt)
    rum/reactive
  [s]
  (let [company-slug (router/current-company-slug)
        updates (reverse (drv/react s :su-list))
        none? (empty? updates)
        mobile? (responsive/is-mobile-size?)
        klass (if mobile? "" "oc-popover") ; desktop is in a popover, mobile is full page
        window-height (when mobile? (.-clientHeight (.-documentElement js/document)))
        window-width (when mobile? (.-clientWidth (.-documentElement js/document)))
        width (if mobile? window-width desktop-width) ; full-width on mobile
        right-padding (if mobile? "pr2" "pr3")
        height (cond ; 4 possible heights
                  mobile? window-height
                  none? 200 ; just a message
                  (<= (count updates) 3) 325 ; small # of updates
                  :else 450) ; lots of updates
        margin-left (if mobile? "0" (half-offset width)) ; desktop is centered
        margin-top (if mobile? "0" (half-offset height))] ; desktop is centered

    [:div {:class klass
           :style {:height (str height "px")
                   :width (str width "px")
                   :padding 0
                   :margin-left margin-left
                   :margin-top margin-top}}
      
      (when-not mobile? ; floating close X
        [:button {:class "absolute top-0 btn-reset" :style {:left "100%"}
                  :on-click (fn [e] (popover/hide-popover e "prior-updates-dialog"))}
          (i/icon :simple-remove {:class "inline mr1" :stroke "4"
                                  :color "white" :accent-color "white"})])

      [:div.prior-updates-container {:style {:max-height (str (- height 2) "px") :overflow-y "scroll"}}

        [:h3.m0.pl3.py25.gray5.domine {:style {:class right-padding :border-bottom "solid 1px rgba(78, 90, 107, 0.1)"}}
          "Prior Updates"
          (when mobile? ; inside the header close X
            [:button {:class "top-0 btn-reset" :style {:float "right" :padding-top "0" :margin-top "-5px"}
                  :on-click #(router/nav! (oc-urls/company))}
              (i/icon :simple-remove {:class "inline mr1" :stroke "4" :vertical-align "top"
                                      :color "grey" :accent-color "grey"})])]
        
        (if (empty? updates)
          
          [:div.message-container.pt2
            [:p.message "There's nothing to see here."]
            [:p.message "Start sharing!"]]

          [:div.update-container.px3.pt2
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
                                [:div.medium "a " (i/icon :link-72 {:size 13 :class "inline"})])
                    link-map (if mobile?
                                {:href link}
                                {:href link :target (str "_update_" update-slug)})]
                [:div.update {:key update-slug}
                  [:div.update-title.domine [:a link-map title]]
                  [:div.update-details.domine author " shared " medium " on " human-date]]))])]]))