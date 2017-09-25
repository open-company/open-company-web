(ns oc.web.components.secure-story
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.about-carrot-modal :refer (about-carrot-modal)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(rum/defcs secure-story < rum/reactive
                          (drv/drv :activity-data)
                          (drv/drv :about-carrot-modal)
                          {:will-mount (fn [s]
                                         (utils/after 100 #(dis/dispatch! [:story-get]))
                                         s)}
  [s]
  (let [story-data (drv/react s :activity-data)
        story-author (if (map? (:author story-data)) (:author story-data) (first (:author story-data)))
        ww (min (responsive/ww) 840)]
    [:div.secure-story-container
      (when (drv/react s :about-carrot-modal)
        (about-carrot-modal))
      [:div.story-header.group
        (org-avatar (clojure.set/rename-keys story-data {:org-logo-height :logo-height :org-logo-width :logo-width :org-logo-url :logo-url :org-name :name :org-slug :slug}))]
      [:div.story-content-outer
        [:div.story-content
          (when (:banner-url story-data)
            [:div.story-banner
              {:style #js {:backgroundImage (str "url(" (:banner-url story-data) ")")
                           :height (str (if (pos? (:banner-height story-data)) (min 520 (* (/ (:banner-height story-data) (:banner-width story-data)) ww)) "1") "px")}}])
          (when (:title story-data)
            [:div.story-title
              {:dangerouslySetInnerHTML (utils/emojify (:title story-data))}])
          [:div.story-author.group
            [:div.story-author-inner.group
              (user-avatar-image story-author)
              [:div.posted-by
                (str "Posted by " (:name story-author) " " (utils/activity-date (utils/js-date (:published-at story-data)) true) ".")]]]
          (when (:body story-data)
            [:div.story-body
              {:dangerouslySetInnerHTML (utils/emojify (:body story-data))}])]]
    [:div.story-content-footer
      {:on-click #(dis/dispatch! [:about-carrot-modal-show])}
      [:div.story-content-footer-inner
        [:div.carrot-logo]
        [:div.you-did-it "Made with Carrot"]]]]))