(ns open-company-web.components.su-preview
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1)]
            [open-company-web.api :as api]
            [open-company-web.urls :as oc-urls]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.icon :refer (icon)]
            [cljsjs.react.dom]))

(defn patch-stakeholder-update [owner]
  (let [title (om/get-state owner :title)
        topics (om/get-props owner :selected-topics)]
    (api/patch-stakeholder-update {:title title
                                   :sections topics})))

(defn su-default-title []
  (let [js-date (utils/js-date)
        month (utils/month-string (utils/add-zero (.getMonth js-date)))
        year (.getFullYear js-date)]
    (str month " " year " Update")))

(defn preview-clicked [owner]
  (patch-stakeholder-update owner))

(defn focus-title [owner]
  (let [title (.findDOMNode js/ReactDOM (om/get-ref owner "title"))
        title-val (.-value title)]
    (.focus title)
    (set! (.-value title) title-val)))

(defcomponent su-preview [data owner options]

  (init-state [_]
    (let [title (:title (:stakeholder-update (:company-data data)))]
      {:title (if (clojure.string/blank? title)
                (su-default-title)
                title)}))

  (did-mount [_]
    (utils/disable-scroll)
    (utils/after 600 #(focus-title owner)))

  (will-unmount [_]
    (utils/enable-scroll))

  (render-state [_ {:keys [title]}]
    (let [company-data (:company-data data)]
      (dom/div {:class "su-preview"}
        (dom/div {:class "su-close-window"}
          (dom/button {:on-click #((:dismiss-su-preview options))}
            (icon :simple-remove {:stroke "4" :accent-color "white"})))
        (dom/div {:class "su-preview-window"}
          (dom/input #js {:className "su-title"
                          :ref "title"
                          :placeholder "Update title"
                          :type "text"
                          :on-change #(om/set-state! owner :title (.. % -target -value))
                          :value title})
          (dom/div {:class "su-preview-box"}
            (dom/div {:class "su-preview-title"} "PREVIEW")
            (dom/a {:class "preview-button"
                    :href (oc-urls/stakeholder-update-preview)
                    :target "_blank"
                    :on-click #(preview-clicked owner)}
              (dom/div {:class "link-circle"}
                (icon :link-72 {:size 20 :accent-color "rgba(78, 90, 107, 0.5)"}))
              (dom/label {} "VIEW PREVIEW"))
            (dom/div {:class "su-preview-ready"} "READY TO SHARE")
            (dom/button {:class "ready-slack-button"}
              (dom/div {:class "slack-circle"}
                (dom/img {:class "slack-icon" :src "/img/Slack_Icon.png"}))
              (dom/label {} "SHARE ON SLACK"))
            (dom/button {:class "ready-mail-button"}
              (dom/div {:class "mail-circle"}
                (icon :email-84 {:size 20 :accent-color "rgba(78, 90, 107, 0.5)"}))
              (dom/label {} "SHARE VIA EMAIL")))
          (dom/button {:class "cancel-button"
                       :on-click #((:dismiss-su-preview options))} "CANCEL"))))))