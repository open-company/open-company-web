(ns open-company-web.components.su-preview
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1)]
            [open-company-web.api :as api]
            [open-company-web.dispatcher :as dis]
            [open-company-web.urls :as oc-urls]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.icon :refer (icon)]
            [cljsjs.react.dom]
            [cljsjs.clipboard]))

(defn patch-stakeholder-update [owner]
  (let [title (om/get-state owner :title)
        topics (om/get-props owner :selected-topics)]
    (api/patch-stakeholder-update {:title title
                                   :sections topics})))

(defn post-stakeholder-update [owner]
  (om/set-state! owner :posting-su true)
  (api/share-stakeholder-update))

(defn su-default-title []
  (let [js-date (utils/js-date)
        month (utils/month-string (utils/add-zero (.getMonth js-date)))
        year (.getFullYear js-date)]
    (str month " " year " Update")))

(defn preview-clicked [owner]
  (om/set-state! owner :preview-loading true)
  (patch-stakeholder-update owner))

(defn slack-clicked [owner]
  (om/set-state! owner :slack-loading true)
  (patch-stakeholder-update owner))

(defn email-clicked [owner]
  (om/set-state! owner :email-loading true)
  (patch-stakeholder-update owner))

(defn copy-clicked [owner]
  (om/set-state! owner :share-link-copied true))

(defn focus-title [owner]
  (let [title (.findDOMNode js/ReactDOM (om/get-ref owner "title"))
        title-val (.-value title)]
    (.focus title)
    (set! (.-value title) title-val)))

(defn cancel-clicked [owner options]
  (if (om/get-state owner :slack-patched)
   (do
      (om/set-state! owner :slack-patched false)
      (om/set-state! owner :slack-loading false))
   ((:dismiss-su-preview options))))

(defn slack-send-clicked [owner options]
  (if (om/get-state owner :slack-patched-posted)
    ((:dismiss-su-preview options))
    (let [slack-message-ta (.findDOMNode js/ReactDOM (om/get-ref owner "slack-share-textarea"))
          slack-message (.-value slack-message-ta)]
      (om/set-state! owner :slack-patched-posting true)
      (utils/after 1000 #(api/share-stakeholder-update (or slack-message ""))))))

(defcomponent su-preview [data owner options]

  (init-state [_]
    (let [title (:title (:stakeholder-update (:company-data data)))]
      {:title (if (clojure.string/blank? title)
                (su-default-title)
                title)
       :preview-loading false
       :slack-loading false
       :slack-patched false
       :slack-patched-posting false
       :slack-patched-posted false
       :email-loading false
       :share-link-copied false
       :posting-su false
       :su-posted false}))

  (did-mount [_]
    (utils/disable-scroll)
    (utils/after 600 #(focus-title owner)))

  (will-unmount [_]
    (utils/enable-scroll))

  (will-receive-props [_ next-props]
    (om/set-state! owner :preview-loading false)
    (cond
      ; email clicked, patch done, going to post
      (om/get-state owner :email-loading)
      (do
        (om/set-state! owner :email-loading false)
        (post-stakeholder-update owner))
      ; email clicked, patch done, post done too
      (om/get-state owner :posting-su)
      (do
        (om/set-state! owner :email-loading false)
        (om/set-state! owner :posting-su false)
        (om/set-state! owner :su-posted true)
        (utils/after 600
          #(let [protocol           (.. js/document -location -protocol)
                 host               (.. js/document -location -host)
                 latest-update-slug (dis/latest-stakeholder-update)
                 fixed-su-slug      (subs latest-update-slug 10)
                 su-url             (str protocol "//" host fixed-su-slug)]
             (om/set-state! owner :share-link su-url))))
      ; slack button pushed
      (and (om/get-state owner :slack-loading)
           (not (om/get-state owner :slack-patched))
           (not (om/get-state owner :slack-patched-posting)))
      (do
        (om/set-state! owner :slack-loading false)
        (om/set-state! owner :slack-patched true))
      ; slack SU posted
      (and (om/get-state owner :slack-patched)
           (om/get-state owner :slack-patched-posting))
      (om/set-state! owner :slack-patched-posted true)))

  (did-update [_ _ _]
    (when (om/get-state owner :share-link)
      (js/Clipboard. ".share-link-button")))

  (render-state [_ {:keys [title
                           preview-loading
                           slack-loading
                           email-loading
                           su-posted
                           share-link
                           share-link-copied
                           slack-patched
                           slack-patched-posting
                           slack-patched-posted]}]
    (let [company-data (:company-data data)]
      (dom/div {:class "su-preview"}
        (dom/div {:class (utils/class-set {:su-close-window true
                                           :share-copy-window su-posted
                                           :slack-patched-window slack-patched})}
          (dom/button {:on-click #(cancel-clicked owner options)}
            (icon :simple-remove {:stroke "4" :accent-color "white"})))
        (dom/div {:class (utils/class-set {:su-preview-window true
                                           :share-link-copy su-posted
                                           :slack-message slack-patched})}
          (when (and (not su-posted)
                     (not slack-patched))
            (dom/input #js {:className "su-title"
                            :ref "title"
                            :placeholder "Update title"
                            :type "text"
                            :onChange #(om/set-state! owner :title (.. % -target -value))
                            :value title}))
          (when (and (not su-posted)
                     (not slack-patched))
            (dom/div {:class "su-preview-box"}
              (dom/div {:class "separator top-separator"})
              (dom/div {:class "su-preview-title"} "PREVIEW")
              (dom/a {:class "preview-button"
                      :href (oc-urls/stakeholder-update-preview)
                      :target "_blank"
                      :on-click #(preview-clicked owner)}
                (dom/div {:class "link-circle"}
                  (if preview-loading
                    (dom/img {:class "small-loading" :src "/img/small_loading.gif"})
                    (icon :link-72 {:size 20 :accent-color "rgba(78, 90, 107, 0.5)"})))
                (dom/label {} "VIEW PREVIEW"))
              (dom/div {:class "separator"})
              (dom/div {:class "su-preview-ready"} "READY TO SHARE?")
              (dom/button {:class "ready-slack-button"
                           :on-click #(slack-clicked owner)}
                (dom/div {:class "slack-circle"}
                  (if slack-loading
                    (dom/img {:class "small-loading" :src "/img/small_loading.gif"})
                    (dom/img {:class "slack-icon" :src "/img/Slack_Icon.png"})))
                (dom/label {} "SHARE ON SLACK"))
              (dom/button {:class "ready-mail-button"
                           :on-click #(email-clicked owner)}
                (dom/div {:class "mail-circle"}
                  (if email-loading
                    (dom/img {:class "small-loading" :src "/img/small_loading.gif"})
                    (icon :email-84 {:size 20 :accent-color "rgba(78, 90, 107, 0.5)"})))
                (dom/label {} "SHARE URL"))))
          (when su-posted
            (dom/div {:class "su-preview-box"}
              (dom/label {:class "share-link-cta"} "SHARE THIS PRIVATE URL")
              (dom/div {:class "share-link-box"}
                (dom/input #js {:type "text"
                                :className "share-link-input"
                                :id "share-link-input"
                                :on-change #(om/set-state! owner :share-link share-link)
                                :value share-link
                                :ref "share-link-input"})
                (dom/button {:class "share-link-button"
                             :data-clipboard-target "#share-link-input"
                             :on-click #(copy-clicked owner)} (if share-link-copied "COPIED ✓" "COPY")))
              (dom/a {:class "share-link-new-win" :href share-link :target "_blank"} "Open in new window")))
          (when slack-patched
            (dom/div {:class "su-preview-box"}
              (dom/label {:class "slack-share-cta"} "YOUR SNAPSHOT WILL BE SHARED WITH ALL THE MEMBERS OF YOUR SLACK ORGANIZATION")
              (dom/textarea #js {:className "slack-share-textarea"
                                 :ref "slack-share-textarea"
                                 :placeholder "Add a note"})))
          (when slack-patched
            (dom/button {:class "slack-send-button"
                         :on-click #(slack-send-clicked owner options)}
              (cond
                (and slack-patched
                     slack-patched-posting
                     (not slack-patched-posted))
                  (dom/img {:class "small-loading" :src "/img/small_loading.gif"})
                (and slack-patched
                     slack-patched-posting
                     slack-patched-posted)
                  "SENT ✓"
                :else
                  "SEND")))
          (dom/button {:class "cancel-button"
                       :on-click #(cancel-clicked owner options)} (if su-posted "DONE" "CANCEL")))))))