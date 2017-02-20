(ns oc.web.components.prior-updates
  (:require [rum.core :as rum]
            [cljs-time.format :as f]
            [org.martinklepsch.derivatives :as drv]
            [defun.core :refer (defun-)]
            [oc.web.urls :as oc-urls]
            [oc.web.components.ui.icon :as i]
            [oc.web.components.ui.popover :as popover]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.utils :as utils]
            [oc.web.urls :as urls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]))

(def link-formatter (f/formatter "yyyy-MM-dd"))
(def human-formatter (f/formatter "MMMM d, yyyy"))

(def desktop-width 500)

(defn- half-offset [pixels]
  (str "-" (Math/round (* 0.5 pixels)) "px"))

(defn- update-click [link e]
  (utils/event-stop e)
  (router/nav! link))

(defn load-prior-updates-if-needed []
  (when (and (dispatcher/org-data)
             (not (:updates-list-loading @dispatcher/app-state))
             (not (:updates-list-loaded @dispatcher/app-state)))
    (dispatcher/dispatch! [:get-updates-list])))

(defun- medium-for
  ;; one possible initial case, just one medium
  ([medium :guard string?] (medium-for [medium] []))

  ;; another possible initial case, multiple mediums
  ([mediums :guard sequential?] (medium-for mediums []))

  ;; completion case
  ([mediums :guard empty? html] html)

  ;; progress cases
  ([mediums :guard #(= "email" (first %)) html] 
    (medium-for (rest mediums) (conj html [:div.medium [:i.fa.fa-envelope-o]])))
  ([mediums :guard #(= "slack" (first %)) html] 
    (medium-for (rest mediums) (conj html [:div.medium [:i.fa.fa-slack]])))
  ([mediums :guard #(= "link" (first %)) html] 
    (medium-for (rest mediums) (conj html [:div.medium [:i.fa.fa-link]])))
  ([mediums html] ; legacy, unknown?
    (medium-for (rest mediums) html)))

(rum/defcs update-row < (drv/drv :current-user-data)
                        rum/reactive
                        rum/static
  [s update standalone-component current-update]
  (let [org-slug (router/current-org-slug)
        org-data (dispatcher/org-data)
        mobile? (responsive/is-mobile-size?)
        user-id (:user-id (drv/react s :current-user-data))
        author-id (-> update :author :user-id)
        same-user? (= user-id author-id)
        iso-date (:created-at update)
        js-date (utils/js-date iso-date)
        month (inc (.getMonth js-date))
        day (.getDate js-date)
        year (.getFullYear js-date)
        needs-year (> (- (.getTime (utils/js-date)) (.getTime js-date)) (* 1000 60 60 24 365))
        month-string (utils/month-string-int month [:short :capitalize])
        link-date (str year "-" (utils/add-zero month) "-" (utils/add-zero day))
        human-date (str month-string " " day (when needs-year (str ", " year)))
        update-slug (:slug update)
        list-link (urls/updates-list org-slug update-slug)
        link-url (urls/stakeholder-update org-slug link-date update-slug)
        link (if mobile? (str link-url "?list=true") link-url)
        title (if (clojure.string/blank? (:title update))
                (str (:name org-data) " Update")
                (:title update))
        author (if same-user? "You" (-> update :author :name))
        medium (medium-for (:medium update))]
    [:div.update {:key update-slug
                  :class (when (= current-update update-slug) "active")
                  :on-click #(when-not standalone-component (update-click list-link %))}
      [:div.update-title.domine
        [:a {:href link :on-click #(when-not standalone-component (.preventDefault %))}
          title]]
      [:div.update-details.domine author " shared"
                                  (when-not (empty? medium) " ") medium
                                  " on " human-date]]))

(rum/defcs prior-updates < (drv/drv :updates-list)
                           rum/static
                           rum/reactive
  [s standalone-component current-update]
  (let [org-slug (router/current-org-slug)
        updates (reverse (drv/react s :updates-list))
        none? (empty? updates)
        org-data (dispatcher/org-data)]
    (js/console.log "prior-updates/render" updates)
    (when standalone-component
      (load-prior-updates-if-needed))
    (if (and standalone-component
             (not (dispatcher/updates-list-data)))
      [:div.prior-updates
        [:div.oc-loading.active [:i.fa.fa-circle-o-notch.fa-spin]]]
      [:div.prior-updates

        [:div.prior-updates-container {}
          
          (if (empty? updates)

            [:div.message-container.pt2
              [:h3.message "No company updates have been shared."]
              (when-not (:read-only org-data)
                [:p.message
                  "As updates are shared by email, Slack or URL, they show up here so they are easy to find and read."])]

            [:div.update-container
              (for [update updates]
                (rum/with-key (update-row update standalone-component current-update) (:slug update)))])]])))