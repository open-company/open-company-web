(ns oc.web.components.entry-create
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn dismiss-modal []
  (dis/dispatch! [:new-entry-toggle false]))

(defn close-clicked [s]
  (dis/dispatch! [:input [:new-entry-edit] nil])
  (reset! (::dismiss s) true)
  (utils/after 180 dismiss-modal))

(rum/defcs entry-create < rum/reactive
                          (drv/drv :board-topics)
                          (drv/drv :current-user-data)
                          (drv/drv :new-entry-edit)
                          (drv/drv :board-filters)
                          (rum/local false ::first-render-done)
                          (rum/local false ::dismiss)
                          {:will-mount (fn [s]
                                         (when (nil? (:topic-slug @(drv/get-ref s :new-entry-edit)))
                                            (let [board-filters @(drv/get-ref s :board-filters)
                                                  topics @(drv/get-ref s :board-topics)
                                                  topic (if (string? board-filters) (first (filter #(= (:slug %) board-filters) topics)) (first topics))]
                                              (dis/dispatch! [:input [:new-entry-edit :topic-slug] (:slug topic)])))
                                         s)
                           :did-mount (fn [s]
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
  (let [topics (drv/react s :board-topics)
        current-user-data (drv/react s :current-user-data)
        new-entry-edit (drv/react s :new-entry-edit)
        topic (first (filter #(= (:slug %) (:topic-slug new-entry-edit)) topics))]
    [:div.entry-create-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(::first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(::first-render-done s))})
       :on-click #(close-clicked s)}
      [:div.entry-create-modal.group
        {:on-click #(utils/event-stop %)}
        (user-avatar-image current-user-data)
        [:div.posting-in "Posting in " [:span (:name topic)]]
        [:div.arrow [:i.fa.fa-angle-right]]
        [:div.select-topic "Select a topic" [:i.fa.fa-caret-down]]]]))


