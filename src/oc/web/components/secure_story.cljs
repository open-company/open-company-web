(ns oc.web.components.secure-story
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(rum/defcs secure-story < rum/reactive
                          (drv/drv :activity-data)
                          {:will-mount (fn [s]
                                         (utils/after 100 #(dis/dispatch! [:story-get]))
                                         s)}
  [s]
  (let [story-data (drv/react s :activity-data)
        story-author (if (map? (:author story-data)) (:author story-data) (first (:author story-data)))]
    [:div.secure-story-container
      [:div.story-header.group
        (org-avatar (clojure.set/rename-keys story-data {:org-logo-height :logo-height :org-logo-width :logo-width :org-logo-url :logo-url :org-name :name :org-slug :slug}))]
      [:div.story-content-outer
        [:div.story-content
          (when (:banner-url story-data)
            [:div.story-banner
              {:style #js {:backgroundImage (str "url(" (:banner-url story-data) ")")
                           :height (str (min 200 (* (/ (:banner-height story-data) (:banner-width story-data)) 840)) "px")}}])
          (when (:title story-data)
            [:div.story-title
              {:dangerouslySetInnerHTML (utils/emojify (:title story-data))}])
          [:div.story-author.group
            [:div.story-author-inner.group
              (user-avatar-image story-author)
              [:div.posted-by
                (str "Posted by " (:name story-author) " on " (utils/date-string (utils/js-date (:published-at story-data))))]]]
          (when (:body story-data)
            [:div.story-body
              {:dangerouslySetInnerHTML (utils/emojify (:body story-data))}])]]
    [:div.story-content-footer
      [:div.story-content-footer-inner
        [:div.carrot-logo]
        [:div.you-did-it "Published using carrot!"]]]]))