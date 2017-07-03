(ns oc.web.components.entry-modal
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [cuerdas.core :as s]
            [oc.web.router :as router]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.comments :refer (comments)]))

(def lorem-gen "<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a,</p>")

(defn close-clicked [_e]
  (if (= (keyword (cook/get-cookie (router/last-board-filter-cookie (router/current-org-slug) (router/current-board-slug)))) :by-topic)
    (router/nav! (oc-urls/board-sort-by-topic))
    (router/nav! (oc-urls/board))))

(rum/defcs entry-modal < {:did-mount (fn [s]
                                       ;; Add no-scroll to the body to avoid scrolling while showing this modal
                                       (dommy/add-class! (sel1 [:body]) :no-scroll)
                                       ;; Scroll to the bottom of the comments box
                                       (let [el (sel1 [:div.entry-right-column-content])]
                                          (set! (.-scrollTop el) (.-scrollHeight el)))
                                       s)
                          :will-unmount (fn [s]
                                          ;; Remove no-scroll class from the body tag
                                          (dommy/remove-class! (sel1 [:body]) :no-scroll)
                                          s)}
  [s entry-data]
  [:div.entry-modal-container
    [:div.entry-modal.group
      [:button.close-entry-modal.mlb-reset
        {:on-click close-clicked}]
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
                    (utils/time-since (:updated-at entry-data))]
                  (when (or (:topic-name entry-data) (:topic-slug entry-data))
                    (str " · " (or (:topic-name entry-data) (s/capital (:topic-slug entry-data)))))]]
              [:div.entry-card-head-right
                [:button.mlb-reset.entry-modal-more
                  {:on-click #()}]
                [:div.new "NEW"]]]
            [:div.entry-card-content
              [:div.entry-card-content-headline (:headline entry-data)]
              [:div.entry-card-content-body {:dangerouslySetInnerHTML #js {:__html (:body entry-data)}}]]
            [:div.entry-card-footer
              (reactions (:topic-slug entry-data) (:uuid entry-data) entry-data)]]]
        [:div.entry-right-column
          [:div.entry-right-column-content
            (comments)]]]]])