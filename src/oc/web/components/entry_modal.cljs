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
                         (rum/local false ::hovering-card)
                         (rum/local false ::showing-dropdown)
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
        {:class (str "entry-modal-" (:uuid entry-data))
         :on-mouse-over #(reset! (::hovering-card s) true)
         :on-mouse-leave #(reset! (::hovering-card s) false)}
        [:button.close-entry-modal.mlb-reset
          {:on-click #(close-clicked s)}]
        [:div.entry-modal-inner.group
          [:div.entry-left-column
            {:style #js {:minHeight column-height}}
            [:div.entry-left-column-content
              {:style #js {:minHeight column-height}}
              [:div.entry-modal-head.group
                [:div.entry-modal-head-left
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
                [:div.entry-modal-head-right
                  (when (:topic-slug entry-data)
                    (let [topic-name (or (:topic-name entry-data) (s/upper (:topic-slug entry-data)))]
                      [:div.new
                        topic-name]))]]
              [:div.entry-modal-content
                [:div.entry-modal-content-headline
                  {:dangerouslySetInnerHTML (utils/emojify (:headline entry-data))}]
                [:div.entry-modal-content-body
                  {:dangerouslySetInnerHTML (utils/emojify (:body entry-data))
                   :class (when (empty? (:headline entry-data)) "no-headline")}]
                [:div.entry-modal-footer.group
                  (reactions (:topic-slug entry-data) (:uuid entry-data) entry-data)
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
            {:style #js {:minHeight column-height}}
            [:div.entry-right-column-content
              (comments (:uuid entry-data))]]]]]))