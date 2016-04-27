(ns open-company-web.components.su-list
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.router :as router]
            [open-company-web.local-settings :as ls]
            [open-company-web.api :as api]
            [open-company-web.caches :as caches]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.navbar :refer (navbar)]
            [open-company-web.components.topic-body :refer (topic-body)]
            [open-company-web.components.company-header :refer [company-header]]
            [open-company-web.components.section-selector :refer (section-selector)]
            [open-company-web.components.ui.link :refer (link)]
            [open-company-web.components.ui.side-drawer :refer (side-drawer)]
            [open-company-web.components.ui.drawer-toggler :refer (drawer-toggler)]
            [clojure.string :as str]
            [goog.style :refer (setStyle)]))

(defcomponent su-update [data owner]
  (render [_]
    (let [slug (name (:slug @router/path))
          update (:update data)
          js-date-upat (utils/js-date (:created-at update))
          month-string (utils/month-string-int (inc (.getMonth js-date-upat)))
          topic-updated-at (str month-string " " (.getDate js-date-upat) ", " (.getFullYear js-date-upat))
          intro (:intro update)
          update-slug (:slug update)
          links (:links update)
          update-get-link (utils/link-for links "self" "GET")]
      (dom/div {:class "su-update"
                :on-click #(router/nav! (str "/" slug "/updates/" update-slug))}
        (dom/div {:class "su-title"} (:title update))
        (dom/div {:class "su-date"} topic-updated-at)
        (dom/div {:class "su-body"
                  :dangerouslySetInnerHTML (clj->js {"__html" (:body intro)})})))))

(defcomponent stakeholder-updates [data owner]

  (init-state [_] {
    :drawer-open false})

  (render-state [_ {:keys [drawer-open]}]
    (let [su-updates (:stakeholder-updates (:collection (:su-list data)))]
      (dom/div {:class "updates"}

        (dom/div {:class "updates-internal"}
          (for [update su-updates]
            (om/build su-update {:update update})))))))

(defn load-su-list-if-needed [owner]
  ;; request the live SU if necessary
  (let [slug (keyword (:slug @router/path))
        data (om/get-props owner)
        company-data (get data slug)]
    (when (and (not (om/get-state owner :su-requested)) (contains? company-data :links))
      (api/get-su-list)
      (om/set-state! owner :su-requested true))))

(defcomponent su-list [data owner]

  (init-state [_]
    {:drawer-open false
     :toggler-top-margin false
     :su-requested false})

  (did-mount [_]
    (load-su-list-if-needed owner))

  (did-update [_ _ _]
    (load-su-list-if-needed owner))

  (render-state [_ {:keys [drawer-open]}]
    (let [slug (keyword (:slug @router/path))
          su-list-key (dispatcher/su-list-key slug)
          company-data (get data slug)
          su-list (get data su-list-key)
          stakeholder-update-data (:stakeholder-update company-data)]

      (utils/update-page-title (str "OpenCompany - " (:name company-data)))

      (cond

        ;; The data is still loading
        (:loading data)
        (dom/div
          (dom/h4 "Loading data..."))

        ;; Stakeholder update
        (and (not (contains? data :loading)) (contains? data slug))
        (dom/div {:class (utils/class-set {:stakeholder-update true
                                           :navbar-offset (not (utils/is-mobile))})}
          ;; Company / user header
          (when-not (utils/is-mobile)
            (om/build navbar data))
            
          ;; Company header
          (om/build company-header {
              :editing-topic true ; no category nav
              :company-data company-data
              :stakeholder-update true})
          
          (dom/div {:class "update-internal"}
          
            (dom/div {:class "sections"}; col-md-9 col-sm-12"}
              ;; Stakeholder update topics
              (om/build stakeholder-updates {:company-data company-data
                                             :su-list su-list})
              ;; Dashboard link
              (when (utils/is-mobile)
                (dom/div {:class "dashboard-link"}
                  (om/build link {:href (str "/" (:slug company-data)) :name "View Dashboard"}))))))


        ;; Error fallback
        :else
        (dom/div
          (dom/h2 (str (name slug) " not found"))
          (om/build link {:href "/" :name "Back home"}))))))