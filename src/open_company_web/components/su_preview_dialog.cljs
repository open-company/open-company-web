(ns open-company-web.components.su-preview-dialog
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

(defn post-stakeholder-update [owner]
  (let [slack-textarea (.findDOMNode js/ReactDOM (om/get-ref owner "slack-share-textarea"))
        slack-message (.-value slack-textarea)]
    (api/share-stakeholder-update slack-message)))

(defn copy-clicked [owner]
  (om/set-state! owner :share-link-copied true))

(defn initial-state [owner]
  (let [data (om/get-props owner)]
    {:share-via-slack (:share-via-slack data)
     :share-via-link (:share-via-link data)
     :share-link-copied false
     :share-link (when (:latest-su data) (:latest-su data))
     :slack-sending false
     :slack-sent false}))

(defn cancel-clicked [owner options]
  (om/set-state! owner (initial-state owner))
  ((:dismiss-su-preview options)))

(defn slack-send-clicked [owner options]
  (if (om/get-state owner :slack-sent)
    (cancel-clicked owner options)
    (let [slack-message-ta (.findDOMNode js/ReactDOM (om/get-ref owner "slack-share-textarea"))
          slack-message (.-value slack-message-ta)]
      (om/set-state! owner :slack-sending true)
      (api/share-stakeholder-update (or slack-message "")))))

(defn select-share-link [owner]
  (when-let [input (.findDOMNode js/ReactDOM (om/get-ref owner "share-link-input"))]
    (.setSelectionRange input 0 (count (.-value input)))))

(defcomponent su-preview-dialog [data owner options]

  (init-state [_]
    {:share-via-slack (:share-via-slack data)
     :share-via-link (:share-via-link data)
     :share-link-copied false
     :share-link (when (:latest-su data) (:latest-su data))
     :slack-sending false
     :slack-sent false})

  (did-mount [_]
    (when (om/get-state owner :share-link)
      (js/Clipboard. ".share-link-button"))
    (utils/disable-scroll))

  (will-unmount [_]
    (utils/enable-scroll))

  (will-receive-props [_ next-props]
    ; slack SU posted
    (when (and (om/get-state owner :share-via-slack)
               (om/get-state owner :slack-sending)
               (not (om/get-state owner :slack-sent)))
      (om/set-state! owner :slack-sent true)))

  (did-update [_ _ _]
    (when (om/get-state owner :share-link)
      (js/Clipboard. ".share-link-button")))

  (render-state [_ {:keys [share-via-slack share-via-link share-link-copied share-link slack-sending slack-sent]}]
    (let [company-data (:company-data data)]
      (dom/div {:class "su-preview-dialog"}
        (dom/div {:class (utils/class-set {:su-close-window true
                                           :share-copy-window share-via-link
                                           :slack-patched-window share-via-slack})}
          (dom/button {:on-click #(cancel-clicked owner options)}
            (icon :simple-remove {:stroke "4" :color "white" :accent-color "white"})))
        (dom/div {:class (utils/class-set {:su-preview-window true
                                           :share-link-copy share-via-link
                                           :slack-message share-via-slack})}
          (when share-via-link
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
          (when share-via-slack
            (dom/div {:class "su-preview-box"}
              (dom/label {:class "slack-share-cta"} "SHARE THIS SNAPSHOT WITH THE MEMBERS OF YOUR SLACK TEAM")
              (dom/textarea #js {:className "slack-share-textarea"
                                 :ref "slack-share-textarea"
                                 :placeholder "Add a note"})))
          (when share-via-slack
            (dom/button {:class "slack-send-button"
                         :on-click #(slack-send-clicked owner options)}
              (cond
                (and slack-sending
                     (not slack-sent))
                (dom/img {:class "small-loading" :src "/img/small_loading.gif"})
                (and slack-sending
                     slack-sent)
                "SENT ✓"
                :else
                "SEND")))
          (dom/button {:class "cancel-button"
                       :on-click #(cancel-clicked owner options)} (if share-via-link "DONE" "CANCEL")))))))