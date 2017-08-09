(ns oc.web.components.entry-modal
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as string]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.components.entry-attachments :refer (entry-attachments)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.comments :refer (comments)]))

(defn dismiss-modal [board-filters]
  (if (:from-all-activity @router/path)
    (dis/dispatch! [:all-activity-nav])
    (dis/dispatch! [:board-nav (router/current-board-slug) board-filters])))

(defn close-clicked [s & [board-filters]]
  (reset! (::dismiss s) true)
  (utils/after 180 #(dismiss-modal board-filters)))

(defn delete-clicked [e entry-data]
  (utils/event-stop e)
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :message "Delete this entry?"
                    :first-button-title "No"
                    :first-button-cb #(dis/dispatch! [:alert-modal-hide])
                    :second-button-title "Yes"
                    :second-button-cb #(do
                                        (let [org-slug (router/current-org-slug)
                                              board-slug (router/current-board-slug)
                                              last-filter (keyword (cook/get-cookie (router/last-board-filter-cookie org-slug board-slug)))]
                                          (if (= last-filter :by-topic)
                                            (router/nav! (oc-urls/board-sort-by-topic))
                                            (router/nav! (oc-urls/board))))
                                        (dis/dispatch! [:entry-delete entry-data])
                                        (dis/dispatch! [:alert-modal-hide]))
                    }]
    (dis/dispatch! [:alert-modal-show alert-data])))

(rum/defcs entry-modal < (rum/local false ::first-render-done)
                         (rum/local false ::dismiss)
                         (rum/local false ::animate)
                         (rum/local false ::hovering-card)
                         (rum/local false ::showing-dropdown)
                         (rum/local nil ::column-height)
                         (rum/local nil ::window-resize-listener)
                         rum/reactive
                         (drv/drv :entry-modal-fade-in)
                         {:before-render (fn [s]
                                           (when (and (not @(::animate s))
                                                    (= @(drv/get-ref s :entry-modal-fade-in) (:uuid (first (:rum/args s)))))
                                             (reset! (::animate s) true))
                                           s)
                          :will-mount (fn [s]
                                        (reset! (::window-resize-listener s)
                                         (events/listen js/window EventType/RESIZE #(reset! (::column-height s) nil)))
                                        s)
                          :did-mount (fn [s]
                                       ;; Add no-scroll to the body to avoid scrolling while showing this modal
                                       (dommy/add-class! (sel1 [:body]) :no-scroll)
                                       ;; Scroll to the bottom of the comments box
                                       (let [el (sel1 [:div.entry-right-column-content])]
                                          (set! (.-scrollTop el) (.-scrollHeight el)))
                                       (let [entry-data (first (:rum/args s))]
                                        (.on (js/$ (str "div.entry-modal-" (:uuid entry-data)))
                                         "show.bs.dropdown"
                                         (fn [e]
                                           (reset! (::showing-dropdown s) true)))
                                        (.on (js/$ (str "div.entry-modal-" (:uuid entry-data)))
                                         "hidden.bs.dropdown"
                                         (fn [e]
                                           (reset! (::showing-dropdown s) false))))
                                       s)
                          :after-render (fn [s]
                                          (when (not @(::first-render-done s))
                                            (reset! (::first-render-done s) true))
                                          (when-not @(::column-height s)
                                            (reset! (::column-height s) (max 284 (.height (js/$ ".entry-left-column"))))
                                            (.load (js/$ ".entry-modal-content-body img")
                                             #(reset! (::column-height s) (max 284 (.height (js/$ ".entry-left-column"))))))
                                          s)
                          :will-unmount (fn [s]
                                          ;; Remove no-scroll class from the body tag
                                          (dommy/remove-class! (sel1 [:body]) :no-scroll)
                                          ;; Remove window resize listener
                                          (when @(::window-resize-listener s)
                                            (events/unlistenByKey @(::window-resize-listener s))
                                            (reset! (::window-resize-listener s) nil))
                                          s)}
  [s entry-data]
  (let [column-height @(::column-height s)]
    [:div.entry-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (and @(::animate s) (not @(::first-render-done s))))
                                :appear (and (not @(::dismiss s)) @(::first-render-done s))})}
      [:div.entry-modal.group
        {:class (str "entry-modal-" (:uuid entry-data))
         :on-mouse-over #(reset! (::hovering-card s) true)
         :on-mouse-leave #(reset! (::hovering-card s) false)}
        [:button.close-entry-modal.mlb-reset
          {:on-click #(close-clicked s)}]
        [:div.entry-modal-inner.group
          [:div.entry-left-column
            {:style (when column-height {:minHeight (str column-height "px")})}
            [:div.entry-left-column-content
              {:style (when column-height {:minHeight (str (- column-height 40) "px")})}
              [:div.entry-modal-head.group
                [:div.entry-modal-head-left
                  (user-avatar-image (first (:author entry-data)))
                  [:div.name (:name (first (:author entry-data)))]
                  [:div.time-since
                    [:time
                      {:date-time (:created-at entry-data)
                       :data-toggle "tooltip"
                       :data-placement "top"
                       :data-container "body"
                       :title (let [js-date (utils/js-date (:updated-at entry-data))] (str (.toDateString js-date) " at " (utils/get-time js-date)))}
                      (utils/time-since (:created-at entry-data))]]]
                [:div.entry-modal-head-right
                  (when (:topic-slug entry-data)
                    (let [topic-name (or (:topic-name entry-data) (string/upper (:topic-slug entry-data)))]
                      [:div.topic-tag
                        {:on-click #(close-clicked s (:topic-slug entry-data))}
                        topic-name]))]]
              [:div.entry-modal-content
                [:div.entry-modal-content-headline
                  {:dangerouslySetInnerHTML (utils/emojify (:headline entry-data))}]
                [:div.entry-modal-content-body
                  {:dangerouslySetInnerHTML (utils/emojify (:body entry-data))
                   :class (when (empty? (:headline entry-data)) "no-headline")}]
                (entry-attachments (:attachments entry-data))
                [:div.entry-modal-footer.group
                  (reactions entry-data)
                  [:div.entry-modal-footer-right
                    [:div.more-dropdown.dropdown
                      [:button.mlb-reset.entry-modal-more.dropdown-toggle
                        {:type "button"
                         :class (utils/class-set {:hidden (and (not @(::hovering-card s)) (not @(::showing-dropdown s)))})
                         :id (str "entry-modal-more-" (router/current-board-slug) "-" (:uuid entry-data))
                         :data-toggle "dropdown"
                         :aria-haspopup true
                         :aria-expanded false
                         :title "More"}]
                      [:div.dropdown-menu
                        {:aria-labelledby (str "entry-modal-more-" (router/current-board-slug) "-" (:uuid entry-data))}
                        [:div.triangle]
                        [:ul.entry-modal-more-menu
                          [:li
                            {:on-click (fn [e]
                                         (utils/event-stop e)
                                         (dis/dispatch! [:entry-edit entry-data]))}
                            "Edit"]
                          [:li
                            {:on-click #(delete-clicked % entry-data)}
                            "Delete"]]]]]]]]]
          [:div.entry-right-column
            {:style (when column-height {:minHeight (str column-height "px")})}
            [:div.entry-right-column-content
              (comments (:uuid entry-data))]]]]]))