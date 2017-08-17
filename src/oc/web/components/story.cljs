(ns oc.web.components.story
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.interactions-summary :refer (comments-summary)]))

(rum/defcs story < rum/reactive
                   (drv/drv :board-data)
                   (drv/drv :story-data)
                   {:will-mount (fn [s]
                                  (dis/dispatch! [:story-get (first (:rum/args s))])
                                  s)
                    :after-render (fn [s]
                                    (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
                                    s)}
  [s]
  (let [board-data (drv/react s :board-data)
        story-data (drv/react s :story-data)
        test-story-data {:title "This is my title"
                         :created-at (utils/as-of-now)
                         :updated-at (utils/as-of-now)
                         :banner-url "http://lorempixel.com/630/480/animals/"
                         :banner-width 640
                         :banner-height 480
                         :reactions [{:reaction "♥️" :count 20 :reacted false} {:reaction "♠️" :count 10 :reacted true} {:reaction "💣" :count 12 :reacted false}]
                         :links [{:rel "comments" :href "/" :count "10" :authors [{:name "Iacopo Carraro" :avatar-url "https://cdn.filestackcontent.com/odtNwsWFQlK385Mb3jeQ"}]}]
                         :author [{:name "Iacopo Carraro"
                                   :avatar-url "https://cdn.filestackcontent.com/odtNwsWFQlK385Mb3jeQ"}]
                         :body "<p>This is some body text</p><p>Another paragraph</p><ul><li>First list item</li><li>Second list item</li><li>Third list item</li></ul>"}]
    [:div.story-container
      [:div.story-header.group
        [:div.story-header-left
          [:span.board-name (:name board-data)]
          [:span.arrow ">"]
          [:span.story-title (:title story-data)]]
        [:div.story-header-right
          (reactions story-data)
          (comments-summary story-data)
          [:button.mlb-reset.mlb-link.share-button
            {:on-click #()}
            "Share"]]]
      [:div.story-content
        (when (:banner-url story-data)
          [:div.story-banner
            {:style #js {:backgroundImage (str "url(" (:banner-url story-data) ")")}}])
        [:div.story-author.group
          (user-avatar-image (first (:author story-data)))
          [:div.name (:name (first (:author story-data)))]
          [:div.time-since
            [:time
              {:date-time (:created-at story-data)
               :data-toggle "tooltip"
               :data-placement "top"
               :title (utils/activity-date-tooltip story-data)}
              (utils/time-since (:created-at story-data))]]]
        [:div.story-tags
          [:div.activity-tag
            {:on-click #(router/nav! (oc-urls/board (router/current-org-slug) (:board-slug story-data)))}
            (:name board-data)]]
        (when (:title story-data)
          [:div.story-title
            {:dangerouslySetInnerHTML (utils/emojify (:title story-data))}])
        (when (:body story-data)
          [:div.story-body
            {:dangerouslySetInnerHTML (utils/emojify (:body story-data))}])]]))