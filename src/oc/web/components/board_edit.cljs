(ns oc.web.components.board-edit
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn dismiss-modal []
  (dis/dispatch! [:board-edit/dismiss]))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 #(dismiss-modal)))

(rum/defcs board-edit < rum/reactive
                        (drv/drv :board-editing)
                        (drv/drv :current-user-data)
                        (rum/local false ::first-render-done)
                        (rum/local false ::dismiss)
                        {:did-mount (fn [s]
                                      ;; Add no-scroll to the body to avoid scrolling while showing this modal
                                      (dommy/add-class! (sel1 [:body]) :no-scroll)
                                      s)
                         :after-render (fn [s]
                                         (when (not @(::first-render-done s))
                                           (reset! (::first-render-done s) true))
                                         s)
                         :will-unmount (fn [s]
                                         ;; Remove no-scroll class from the body tag
                                         (dommy/remove-class! (sel1 [:body]) :no-scroll)
                                         s)}
  [s]
  (let [current-user-data (drv/react s :current-user-data)
        board-editing (drv/react s :board-editing)
        new-board? (not (contains? board-editing :links))]
    [:div.board-edit-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(::first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(::first-render-done s))})}
      [:div.board-edit
        [:div.board-edit-header.group
          (user-avatar-image current-user-data)
          (if new-board?
            [:div.title "Creating a new Board"]
            [:div.title "Editing " [:span.board-name (:name board-editing)]])]
        [:div.board-edit-divider]
        [:div.board-edit-body
          [:div.board-edit-label.board-edit-name-label "BOARD NAME"]
          [:input.board-edit-name-field
            {:type "text"
             :value (:name board-editing)
             :on-change #(dis/dispatch! [:input [:board-editing :name] (.. % -target -value)])
             :placeholder "Product, Development, Finance, Operations, etc."}]
          [:div.board-edit-label.board-edit-access-label "BOARD PERMISSIONS" [:span.more-info]]
          [:div.board-edit-access-field.group
            [:div.board-edit-access-bt.board-edit-access-team-bt
              {:class (when (= (:access board-editing) "team") "selected")
               :on-click #(dis/dispatch! [:input [:board-editing :access] "team"])}
              [:span.board-edit-access-title "Team"]]
            [:div.board-edit-access-bt.board-edit-access-private-bt
              {:class (when (= (:access board-editing) "private") "selected")
               :on-click #(dis/dispatch! [:input [:board-editing :access] "private"])}
              [:span.board-edit-access-title "Private"]]
            [:div.board-edit-access-bt.board-edit-access-public-bt
              {:class (when (= (:access board-editing) "public") "selected")
               :on-click #(dis/dispatch! [:input [:board-editing :access] "public"])}
              [:span.board-edit-access-title "Public"]]]]
        [:div.board-edit-divider]
        [:div.board-edit-footer
          [:button.mlb-reset.mlb-default
            {:type "button"
             :on-click #(dis/dispatch! [:board-edit-save])}
            (if new-board? "Create" "Save")]
          [:button.mlb-reset.mlb-link-black
            {:type "button"
             :on-click #(close-clicked s)}
            "Cancel"]]]]))
