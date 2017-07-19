(ns oc.web.components.entry-modal
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as s]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.comments :refer (comments)]))

(defn dismiss-modal []
  (dis/dispatch! [:board-nav (router/current-board-slug)]))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 dismiss-modal))

(rum/defcs entry-modal < (rum/local false ::first-render-done)
                         (rum/local false ::dismiss)
                         (rum/local false ::animate)
                         rum/reactive
                         (drv/drv :entry-modal-fade-in)
                         {:before-render (fn [s]
                                           (when (and (not @(::animate s))
                                                    (= @(drv/get-ref s :entry-modal-fade-in) (:uuid (first (:rum/args s)))))
                                             (reset! (::animate s) true))
                                           s)
                          :did-mount (fn [s]
                                       ;; Add no-scroll to the body to avoid scrolling while showing this modal
                                       (dommy/add-class! (sel1 [:body]) :no-scroll)
                                       ;; Scroll to the bottom of the comments box
                                       (let [el (sel1 [:div.entry-right-column-content])]
                                          (set! (.-scrollTop el) (.-scrollHeight el)))
                                       s)
                          :after-render (fn [s]
                                          (when (not @(::first-render-done s))
                                            (reset! (::first-render-done s) true))
                                          s)
                          :will-unmount (fn [s]
                                          ;; Remove no-scroll class from the body tag
                                          (dommy/remove-class! (sel1 [:body]) :no-scroll)
                                          s)}
  [s entry-data]
  [:div.entry-modal-container
    {:class (utils/class-set {:will-appear (or @(::dismiss s) (and @(::animate s) (not @(::first-render-done s))))
                              :appear (and (not @(::dismiss s)) @(::first-render-done s))})}
    [:div.entry-modal.group
      [:button.close-entry-modal.mlb-reset
        {:on-click #(close-clicked s)}]
      [:div.entry-modal-inner.group
        [:div.entry-left-column
          [:div.entry-left-column-content
            [:div.entry-card-head.group
              [:div.entry-card-head-left
                (user-avatar-image (first (:author entry-data)))
                [:div.name (:name (first (:author entry-data)))]
                [:div.time-since
                  [:time
                    {:date-time (:updated-at entry-data)
                     :data-toggle "tooltip"
                     :data-placement "top"
                     :data-container "body"
                     :title (let [js-date (utils/js-date (:updated-at entry-data))] (str (.toDateString js-date) " at " (.toLocaleTimeString js-date)))}
                    (utils/time-since (:updated-at entry-data))]]]
              [:div.entry-card-head-right
                [:button.mlb-reset.entry-modal-more
                  {:on-click #()}]
                [:div.new (s/upper (or (:topic-name entry-data) (:topic-slug entry-data)))]]]
            [:div.entry-card-content
              [:div.entry-card-content-headline (:headline entry-data)]
              [:div.entry-card-content-body {:dangerouslySetInnerHTML #js {:__html (:body entry-data)}}]]
            [:div.entry-card-footer
              (reactions (:topic-slug entry-data) (:uuid entry-data) entry-data)]]]
        [:div.entry-right-column
          {:style #js {:minHeight (str (.height (js/$ ".entry-left-column")) "px")}}
          [:div.entry-right-column-content
            (comments (:uuid entry-data))]]]]])