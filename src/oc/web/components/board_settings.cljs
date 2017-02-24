(ns oc.web.components.board-settings
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [rum.core :as rum]
            [clojure.string :as string]
            [oc.web.local-settings :as ls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.small-loading :as loading]
            [oc.web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]
            [oc.web.components.ui.footer :as footer]
            [oc.web.components.ui.login-required :refer (login-required)]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.urls :as oc-urls]
            [oc.web.api :as api]
            [oc.web.dispatcher :as dis]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.iso4217 :as iso4217]
            [goog.events :as events]
            [goog.fx.dom :refer (Fade)]
            [goog.fx.Animation.EventType :as AnimationEventType]))

(defn- save-board-data [owner]
  (let [board-slug (router/current-board-slug)]
    (api/patch-board {:name (om/get-state owner :board-name)
                      :slug board-slug
                      :access (om/get-state owner :access)})))

(defn get-state [data current-state]
  (let [board-data (dis/board-data data)]
    {:board-slug (:slug board-data)
     :board-name (or (:board-name current-state) (:name board-data))
     :access (or (:access current-state) (:access board-data))
     :loading false
     :has-changes (or (:has-changes current-state) false)
     :show-save-successful (or (:show-save-successful current-state) false)}))

(defn cancel-clicked [owner]
  (om/set-state! owner (get-state (om/get-props owner) nil)))

(defcomponent board-settings-form [data owner]

  (init-state [_]
    (get-state data nil))

  (will-receive-props [_ next-props]
    (when (om/get-state owner :loading)
      (utils/after 1500 (fn []
                          (let [fade-animation (new Fade (sel1 [:div#board-settings-save-successful]) 1 0 utils/oc-animation-duration)]
                            (doto fade-animation
                              (.listen AnimationEventType/FINISH #(om/set-state! owner :show-save-successful false))
                              (.play))))))
    (om/set-state! owner (get-state next-props {:show-save-successful (om/get-state owner :loading)})))

  (did-mount [_]
    (when (and (not (utils/is-test-env?))
               (not (responsive/is-tablet-or-mobile?)))
      (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))))

  (render-state [_ {board-slug :board-slug board-name :board-name
                    access :access loading :loading
                    has-changes :has-changes show-save-successful :show-save-successful}]
    (let [org-data (dis/org-data data)]
      (utils/update-page-title (str "OpenCompany - " (:name org-data) " " board-name " settings"))

      (dom/div {:class "mx-auto my4 settings-container group"}
        
        (when-not board-name
          (dom/div {:class "settings-form-label board-settings"}
            (loading/small-loading)))
        
        (dom/div {:class "settings-form p3"}

          ;; Board name
          (dom/div {:class "settings-form-input-label"} "BOARD NAME")
          (dom/input {:class "npt col-8 p1 mb3"
                      :type "text"
                      :id "name"
                      :value (or board-name "")
                      :on-change #(do
                                   (om/set-state! owner :has-changes true)
                                   (om/set-state! owner :board-name (.. % -target -value)))})

          ;; Visibility
          (dom/div {:class "settings-form-input-label"} "VISIBILITY")
          (dom/div {:class "settings-form-input visibility"}
            ;; Public
            (dom/div {:class "visibility-value"
                      :on-click #(do
                                  (om/set-state! owner :has-changes true)
                                  (om/set-state! owner :access "public"))}
              (dom/h3 {:class "mr1"} "Public"
                (when (= access "public")
                  (dom/i {:class "ml1 fa fa-check-square-o"})))
              (dom/p {:class (str (when (= access "public") "bold"))} "This board is public to everyone and will show up in search engines like Google. Only designed authors can edit and share information."))
            ;; Private choice
            (dom/div {:class "visibility-value"}
              (dom/div {:on-click #(when (= access "public")
                                    (om/set-state! owner :has-changes true)
                                    (om/set-state! owner :access "team"))}
                (dom/h3 {} "Private"
                  (when (or (= access "team") (= access "private"))
                    (dom/i {:class "ml1 fa fa-check-square-o"})))
                (dom/p {} "Only designed people can view, edit and share."))
              ;; Team
              (when (or (= access "team") (= access "private"))
                (dom/div {:class "visibility-value ml2"
                          :on-click #(do
                                      (om/set-state! owner :has-changes true)
                                      (om/set-state! owner :access "team"))}
                  (dom/h3 {} "Team"
                    (when (= access "team")
                      (dom/i {:class "ml1 fa fa-check-square-o"})))
                  (dom/p {:class (str (when (= access "team") "bold"))} "All team members can view this board. Only designed authors can edit and share.")))
              ;; Private
              (when (or (= access "team") (= access "private"))
                (dom/div {:class "visibility-value ml2"
                          :on-click #(do
                                      (om/set-state! owner :has-changes true)
                                      (om/set-state! owner :access "private"))}
                  (dom/h3 {} "Invite-Only"
                    (when (= access "private")
                      (dom/i {:class "ml1 fa fa-check-square-o"})))
                  (dom/p {:class (str (when (= access "private") "bold"))} "Only invited team members can view, edit and share this board.")))))

          ; Slug
          (dom/div {:class "settings-form-input-label"} "BOARD URL")
          (dom/div {:class "dashboard-slug"} (str "http" (when ls/jwt-cookie-secure "s") "://" (oc-urls/board (:slug org-data) board-slug)))

          ;; Save button
          (dom/div {:class "mt2 right-align group"}
            (dom/button {:class "btn-reset btn-solid right"
                         :on-click #(save-board-data owner)
                         :disabled (not has-changes)}
              (if loading
                (loading/small-loading)
                "SAVE"))
            (dom/button {:class "btn-reset btn-outline right mr2"
                         :on-click #(cancel-clicked owner)
                         :disabled (not has-changes)}
              "CANCEL")
            (dom/div {:style {:margin-top "5px"
                              :opacity (if show-save-successful "1" "0")}
                      :class "mr2 right"
                      :id "board-settings-save-successful"}
              "Save successful")))))))

(defcomponent board-settings [data owner]

  (render [_]
    (let [org-data (dis/org-data data)
          board-data (dis/board-data data)]
      (when (:read-only board-data)
        (router/redirect! (oc-urls/board)))

      (dom/div {:class "main-board-settings fullscreen-page"}

        (cond
          ;; the data is still loading
          (:loading data)
          (dom/div (dom/h4 "Loading data..."))

          (get-in data [(keyword (router/current-org-slug)) (keyword (router/current-board-slug)) :error])
          (login-required)

          ;; Org profile
          :else
          (dom/div {}
            (back-to-dashboard-btn {:title "Board Settings"})
            (dom/div {:class "board-settings-container"}
              (om/build board-settings-form data))))

        (let [columns-num (responsive/columns-num)
              card-width (responsive/calc-card-width)
              footer-width (responsive/total-layout-width-int card-width columns-num)]
          (footer/footer footer-width))))))