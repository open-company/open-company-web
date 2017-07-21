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

(defn delete-clicked [e entry-data]
  (utils/event-stop e)
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :message "Delete this entry?"
                    :first-button-title "No"
                    :first-button-cb #(dis/dispatch! [:alert-modal-hide])
                    :second-button-title "Yes"
                    :second-button-cb #(do
                                        (dis/dispatch! [:entry-delete entry-data])
                                        (dis/dispatch! [:alert-modal-hide]))
                    }]
    (dis/dispatch! [:alert-modal-show alert-data])))

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
  (let [column-height (str (max 284 (.height (js/$ ".entry-left-column"))) "px")]
    [:div.entry-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (and @(::animate s) (not @(::first-render-done s))))
                                :appear (and (not @(::dismiss s)) @(::first-render-done s))})}
      [:div.entry-modal.group
        [:button.close-entry-modal.mlb-reset
          {:on-click #(close-clicked s)}]
        [:div.entry-modal-inner.group
          [:div.entry-left-column
            {:style #js {:minHeight column-height}}
            [:div.entry-left-column-content
              {:style #js {:minHeight column-height}}
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
                  (when (:topic-slug entry-data)
                    (let [topic-name (s/upper (or (:topic-name entry-data) (:topic-slug entry-data)))
                          topic-color (utils/rgb-with-opacity (:topic-color entry-data) "1")
                          topic-border (utils/rgb-with-opacity (:topic-color entry-data) "0.4")]
                      [:div.new
                        {:style {:color topic-color
                                 :border (str "1px solid " topic-border)}}
                        topic-name]))]]
              [:div.entry-card-content
                [:div.entry-card-content-headline (:headline entry-data)]
                [:div.entry-card-content-body {:dangerouslySetInnerHTML #js {:__html (:body entry-data)}}]]
              [:div.entry-card-footer
                (reactions (:topic-slug entry-data) (:uuid entry-data) entry-data)
                [:div.entry-card-footer-right
                  [:div.more-dropdown.dropdown
                    [:button.mlb-reset.entry-modal-more.dropdown-toggle
                      {:type "button"
                       :id (str "entry-modal-more-" (router/current-board-slug) "-" (:uuid entry-data))
                       :data-toggle "dropdown"
                       :aria-haspopup true
                       :aria-expanded false
                       :title "More"}]
                    [:div.dropdown-menu
                      {:aria-labelledby (str "entry-modal-more-" (router/current-board-slug) "-" (:uuid entry-data))}
                      [:div.triangle]
                      [:ul.entry-card-more-menu
                        [:li
                          {:on-click (fn [e]
                                       (utils/event-stop e)
                                       (dis/dispatch! [:entry-edit entry-data]))}
                          "Edit"]
                        [:li
                          {:on-click #(delete-clicked % entry-data)}
                          "Delete"]]]]]]]]
          [:div.entry-right-column
            {:style #js {:minHeight column-height}}
            [:div.entry-right-column-content
              (comments (:uuid entry-data))]]]]]))