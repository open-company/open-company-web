(ns oc.web.components.board-editor
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [rum.core :as rum]
            [cuerdas.core :as s]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.oc-colors :as occ]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.popover :refer (add-popover hide-popover)]
            [oc.web.components.ui.navbar :refer (navbar)]
            [oc.web.components.ui.small-loading :as loading]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

(defn- create-board-alert []
  (add-popover {:container-id "create-board-alert"
                :message "Please enter a board name."
                :height "120px"
                :success-title "GOT IT"
                :success-cb #(hide-popover nil "create-board-alert")}))

(defn create-board-clicked [owner e]
  (utils/event-stop e)
  (when-not (om/get-state owner :loading)
    (let [data         (om/get-props owner)
          board-name (:create-board data)]
      (if (clojure.string/blank? board-name)
        (create-board-alert)
        (do
          (om/set-state! owner :loading true)
          (dis/dispatch! [:board-create board-name]))))))

(defcomponent board-editor [data owner]

  (init-state [_]
    {:loading false})

  (did-mount [_]
    (utils/update-page-title "OpenCompany - Setup A Board"))

  (render-state [_ {:keys [loading]}]
    (dom/div {:class "board-editor"}
      (dom/div {:class "fullscreen-page group"}
        (om/build navbar {:hide-right-menu true :show-navigation-bar true})
        (dom/div {:class "board-editor-box group navbar-offset"}
          (dom/form {:on-submit (partial create-board-clicked owner)}
            (dom/div {:class "form-group"}
              (when (and (jwt/jwt) (jwt/get-key :first-name))
                (dom/label {:class "board-editor-message"} (str "Hi " (s/capital (jwt/get-key :first-name)) "!")))
              (dom/label {:class "board-editor-message"} "You need to create a board to contain your topics, what should it be called?")
              (dom/input {:type "text"
                          :class "board-editor-input domine h4"
                          :style #js {:width "100%"}
                          :placeholder "Name of the board (e.g. \"Sales\")"
                          :value (or (:create-board data) "")
                          :on-change #(dis/dispatch! [:input [:create-board] (.. % -target -value)])})))
            (dom/div {:class "center"}
              (dom/button {:class "btn-reset btn-solid get-started-button"
                           :disabled (not (pos? (count (:create-board data))))
                           :on-click (partial create-board-clicked owner)}
                          (when loading
                            (loading/small-loading {:class "left mt1"}))
                          (dom/label {:class (str "pointer mt1" (when loading " ml2"))} "OK, LET’S GO"))))))))