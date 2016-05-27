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
    (api/patch-stakeholder-update {:title (or title "")
                                   :sections topics})))

(defn post-stakeholder-update [owner]
  (om/set-state! owner :su-posting true)
  (api/share-stakeholder-update))

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
  ((:dismiss-su-preview options)))

(defn slack-send-clicked [owner options]
  (if (om/get-state owner :slack-posted)
    ((:dismiss-su-preview options))
    (let [slack-message-ta (.findDOMNode js/ReactDOM (om/get-ref owner "slack-share-textarea"))
          slack-message (.-value slack-message-ta)]
      (om/set-state! owner :slack-posting true)
      (utils/after 1000 #(api/share-stakeholder-update (or slack-message ""))))))

(defn select-share-link [owner]
  (when-let [input (.findDOMNode js/ReactDOM (om/get-ref owner "share-link-input"))]
    (.setSelectionRange input 0 (count (.-value input)))))

(defcomponent su-preview [data owner options]

  (init-state [_]
    (let [title          (:su-title data)
          old-title      (:title (:stakeholder-update (:company-data data)))
          fixed-title    (if (or (:share-slack data)
                                 (:share-email data))
                          title
                          (or old-title (utils/su-default-title)))]
      {:title fixed-title
       :preview-loading false
       :slack-loading (:share-slack data)
       :slack-patched false
       :slack-posting false
       :slack-posted false
       :email-loading (:share-email data)
       :share-link-copied false
       :su-posting false
       :su-posted false}))

  (did-mount [_]
    (utils/disable-scroll)
    (when-not (or (:share-slack data)
                  (:share-email data))
      (utils/after 600 #(focus-title owner)))
    (when (or (:share-slack data)
              (:share-email data))
      (patch-stakeholder-update owner)))

  (will-unmount [_]
    (utils/enable-scroll))

  (will-receive-props [_ next-props]
    (om/set-state! owner :preview-loading false)
    (cond
      ; email clicked, patch done, going to post
      (and (om/get-state owner :email-loading)
           (not (om/get-state owner :su-posting))
           (not (om/get-state owner :su-posted)))
      (post-stakeholder-update owner)
      ; email clicked, patch done, post done too
      (om/get-state owner :su-posting)
      (do
        (om/set-state! owner :su-posted true)
        ((:share-done-cb options))
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
           (not (om/get-state owner :slack-posting)))
      (om/set-state! owner :slack-patched true)
      ; slack SU posted
      (and (om/get-state owner :slack-patched)
           (om/get-state owner :slack-posting))
      (do
        (om/set-state! owner :slack-posted true)
        ((:share-done-cb options)))))

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
                           slack-posting
                           slack-posted]}]
    (let [company-data (:company-data data)]
      (dom/div {:class "su-preview"}
        (dom/div {:class (utils/class-set {:su-close-window true
                                           :share-copy-window email-loading
                                           :slack-patched-window slack-loading})}
          (dom/button {:on-click #(cancel-clicked owner options)}
            (icon :simple-remove {:stroke "4" :color "white" :accent-color "white"})))
        (dom/div {:class (utils/class-set {:su-preview-window true
                                           :share-link-copy email-loading
                                           :slack-message slack-loading})}
          (when (and (not email-loading)
                     (not slack-loading))
            (dom/input #js {:className "su-title"
                            :ref "title"
                            :placeholder "Update title"
                            :type "text"
                            :onChange #(om/set-state! owner :title (.. % -target -value))
                            :value title}))
          (when (and (not email-loading)
                     (not slack-loading))
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
                    (icon :link-72 {:size 20 :color "rgba(78, 90, 107, 0.5)" :accent-color "rgba(78, 90, 107, 0.5)"})))
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
                    (icon :email-84 {:size 20 :color "rgba(78, 90, 107, 0.5)" :accent-color "rgba(78, 90, 107, 0.5)"})))
                (dom/label {} "SHARE URL"))))
          (when email-loading
            (dom/div {:class "su-preview-box"}
              (dom/label {:class "share-link-cta"} "SHARE THIS PRIVATE URL")
              (dom/div {:class "share-link-box"}
                (dom/input #js {:type "text"
                                :className "share-link-input"
                                :id "share-link-input"
                                :onFocus #(select-share-link owner)
                                :onKeyUp #(select-share-link owner)
                                :value share-link
                                :ref "share-link-input"})
                (dom/button {:class "share-link-button"
                             :data-clipboard-target "#share-link-input"
                             :on-click #(copy-clicked owner)} (if share-link-copied "COPIED ✓" "COPY")))
              (dom/a {:class "share-link-new-win" :href share-link :target "_blank"} "Open in new window")))
          (when slack-loading
            (dom/div {:class "su-preview-box"}
              (dom/label {:class "slack-share-cta"} "SHARE SNAPSHOT WITH THE MEMBERS OF YOUR SLACK ORGANIZATION")
              (dom/textarea #js {:className "slack-share-textarea"
                                 :ref "slack-share-textarea"
                                 :placeholder "Add a note"})))
          (when slack-patched
            (dom/button {:class "slack-send-button"
                         :on-click #(slack-send-clicked owner options)}
              (cond
                (and slack-patched
                     slack-posting
                     (not slack-posted))
                  (dom/img {:class "small-loading" :src "/img/small_loading.gif"})
                (and slack-patched
                     slack-posting
                     slack-posted)
                  "SENT ✓"
                :else
                  "SEND")))
          (dom/button {:class "cancel-button"
                       :on-click #(cancel-clicked owner options)} (if su-posted "DONE" "CANCEL")))))))