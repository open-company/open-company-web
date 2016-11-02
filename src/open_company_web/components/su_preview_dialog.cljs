(ns open-company-web.components.su-preview-dialog
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [rum.core :as rum]
            [goog.dom :as gdom]
            [goog.style :as gstyle]
            [open-company-web.dispatcher :as dis]
            [open-company-web.components.ui.icon :as i]
            [cljsjs.react.dom]
            [cljsjs.clipboard]))

(defn select-share-link [event]
  (when-let [input (.-target event)]
    (.setSelectionRange input 0 (count (.-value input)))))

;; Rum Mixins

(defn clipboard-mixin [btn-selector]
  {:did-mount    (fn [s] (assoc s ::clipboard (js/Clipboard. btn-selector)))
   :will-unmount (fn [s] (.destroy (::clipboard s)) s)})

(rum/defcs link-dialog < (rum/local false ::copied)
                         (rum/local false ::clipboard)
                         (clipboard-mixin ".js-copy-btn")
  [{:keys [::copied] :as _state} link]
  [:div.p3
    [:label.block.small-caps.bold.mb2 "Share this private link"]
    [:div.flex
     [:input.domine.npt.p1.flex-auto
      {:type "text"
       :id "share-link-input"
       :on-focus select-share-link
       :on-key-up select-share-link
       :value link}]
     [:button {:class "btn-reset btn-solid js-copy-btn"
               :data-clipboard-target "#share-link-input"
               :on-click #(reset! copied true)}
      (if @copied "COPIED ✓" "COPY")]]
    [:div.block.mt2
     [:a.small-caps.underline.bold.dimmed-gray
      {:href link :target "_blank"}
      "Open in New Window"]]])

(rum/defc modal-title < rum/static
  [title]
  [:h3.m0.mb2.gray5.domine
   title])

(rum/defc confirmation < rum/static
  [type cancel-fn]
  [:div
    [:div.p3
      (case type
        :email (modal-title "Email sent!")
        :slack (modal-title "Slack message shared!"))
      (case type
        :email [:div.domine "Recipients will get your update by email."]
        :slack [:div.domine "Members of your " [:i.fa.fa-slack] " Slack organization will get your update."])]])

(defn reset-scroll-height []
  (let [main-scroll (gdom/getElementByClass "main-scroll")]
    (gstyle/setStyle (.-body js/document) #js {:overflow "auto"})
    (gstyle/setStyle main-scroll #js {:height "auti"})))

(defn setup-scroll-height []
  (let [main-scroll       (gdom/getElementByClass "main-scroll")
        su-preview-window (js/$ ".su-preview-window")
        window-height     (max (+ (.height su-preview-window) (* (.-top (.offset su-preview-window)) 2))
                               (.height (js/$ js/window)))]
    (gstyle/setStyle (.-body js/document) #js {:overflow "hidden"})
    (gstyle/setStyle main-scroll #js {:height (str window-height "px")})))

(defcomponent su-preview-dialog [data owner options]

  (init-state [_]
    {:share-via (:share-via data)
     :share-link (:latest-su data)})

  (did-mount [_]
    (setup-scroll-height))

  (did-update [_ _ _]
    (setup-scroll-height))

  (will-unmount [_]
    (dis/dispatch! [:su-share/reset])
    (reset-scroll-height))

  (will-receive-props [_ next-props]
    ; slack SU posted
    (when (and (= (om/get-state owner :share-via) :link)
               (:latest-su next-props))
      (om/set-state! owner :share-link (:latest-su next-props))))

  (render-state [_ {:keys [share-via share-link] :as state}]
    (let [cancel-fn    (:dismiss-su-preview options)]
      (dom/div {:class "su-preview-dialog"}
        (dom/div {:class "su-preview-window"}
          (dom/button
              {:class "absolute top-0 btn-reset" :style {:left "100%"}
               :on-click #(cancel-fn)}
            (i/icon :simple-remove {:class "inline mr1" :stroke "4" :color "white" :accent-color "white"}))
          (dom/div {:class "su-preview-box"}
            (case share-via
              :link       (link-dialog share-link)
              (or :email
                  :slack) (confirmation share-via cancel-fn))))))))