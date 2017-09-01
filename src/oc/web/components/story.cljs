(ns oc.web.components.story
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.comments :refer (comments)]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.carrot-close-bt :refer (carrot-close-bt)]
            [oc.web.components.ui.story-publish-modal :refer (story-publish-modal)]
            [oc.web.components.ui.interactions-summary :refer (interactions-summary comments-summary)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(rum/defc related-story < rum/static
  [story-data single-related]
  (let [author (if (map? (:author story-data)) (:author story-data) (first (:author story-data)))]
    [:div.related-story
      {:class (when single-related "centered")
       :on-click #(when story-data (router/nav! (oc-urls/story (router/current-org-slug) (:board-slug story-data) (:uuid story-data))))}
      [:div.related-story-header
        (user-avatar-image author)
        [:div.name (:name author)]
          [:div.time-since
            [:time
              {:date-time (:created-at story-data)
               :data-toggle "tooltip"
               :data-placement "top"
               :title (utils/activity-date-tooltip story-data)}
              (utils/time-since (:created-at story-data))]]]
      [:div.related-story-title
        {:dangerouslySetInnerHTML (utils/emojify (utils/strip-HTML-tags (:title story-data)))}]
      [:div.related-story-footer.group
        (interactions-summary story-data false)]]))

(def default-comments-total-width 492)

(defn should-show-you-did-it [s]
  (reset! (::show-you-did-it s) (> (+ (.height (js/$ ".story-content")) 68 32 104) (.-innerHeight js/window))))

(rum/defcs story < rum/reactive
                   (drv/drv :activity-data)
                   (rum/local false ::comments-expanded)
                   (rum/local nil ::window-resize)
                   (rum/local false ::show-you-did-it)
                   (rum/local false ::show-publish-modal)
                   {:will-mount (fn [s]
                                  (utils/after 100 #(dis/dispatch! [:story-get]))
                                  (reset! (::window-resize s)
                                    (events/listen js/window EventType/RESIZE #(reset! (::comments-expanded s) @(::comments-expanded s))))
                                  s)
                    :did-mount (fn [s]
                                ;; Show the you did it message only if the story-content div is taller than the window
                                (utils/after 1000 #(should-show-you-did-it s))
                                s)
                    :after-render (fn [s]
                                    (doto (js/$ "[data-toggle=\"tooltip\"]")
                                      (.tooltip "fixTitle")
                                      (.tooltip "hide"))
                                    s)
                    :will-unmount (fn [s]
                                    (events/unlistenByKey @(::window-resize s))
                                    s)}
  [s]
  (let [story-data (drv/react s :activity-data)
        story-author (if (map? (:author story-data)) (:author story-data) (first (:author story-data)))
        left-space (/ (- (.-innerWidth js/window) 840) 2)
        offset (if (> left-space default-comments-total-width)
                  0
                  (min (- left-space 24) (- default-comments-total-width left-space)))
        margin-left (- left-space (when @(::comments-expanded s) offset))]
    [:div.story-container
      (when @(::show-publish-modal s)
        (story-publish-modal story-data #(reset! (::show-publish-modal s) (not @(::show-publish-modal s)))))
      [:div.story-header.group
        [:div.story-header-left
          [:a.board-name
            {:href (when story-data (oc-urls/board (:board-slug story-data)))
             :on-click #(do (utils/event-stop %) (when story-data (router/nav! (oc-urls/board (:board-slug story-data)))))}
            (:storyboard-name story-data)]
          [:span.arrow ">"]
          [:span.story-title
            {:dangerouslySetInnerHTML (utils/emojify (utils/strip-HTML-tags (:title story-data)))}]]
        [:div.story-header-right
          (reactions story-data)
          (when (utils/link-for (:links story-data) "comments")
            [:div.comments-summary-container.group
              {:on-click #(reset! (::comments-expanded s) (not @(::comments-expanded s)))}
              (comments-summary story-data true)])
          (when (utils/link-for (:links story-data) "share")
            [:button.mlb-reset.mlb-link.share-button
              {:on-click #(reset! (::show-publish-modal s) true)}
              "Share"])]]
      [:div.story-content-outer
        {:style #js {:marginLeft (str (int margin-left) "px")}}
        [:div.story-content
          [:div.story-author.group
            (user-avatar-image story-author)
            [:div.name (:name story-author)]
            [:div.time-since
              [:time
                {:date-time (:created-at story-data)
                 :data-toggle "tooltip"
                 :data-placement "top"
                 :title (utils/activity-date-tooltip story-data)}
                (utils/time-since (:created-at story-data))]]]
          (when (:banner-url story-data)
            [:div.story-banner
              {:style #js {:backgroundImage (str "url(" (:banner-url story-data) ")")
                           :height (str (min 200 (* (/ (:banner-height story-data) (:banner-width story-data)) 840)) "px")}}])
          (when (:title story-data)
            [:div.story-title
              {:dangerouslySetInnerHTML (utils/emojify (:title story-data))}])
          (when (:body story-data)
            [:div.story-body
              {:dangerouslySetInnerHTML (utils/emojify (:body story-data))}])
          [:div.story-content-footer.group
            {:class (when-not @(::show-you-did-it s) "hidden")}
            [:div.you-did-it "The End. You did it!"]
            [:div.caught-up]]]
        (when (pos? (count (:related story-data)))
          [:div.related-container.group
            [:div.related-title (str "Related Stories in " (:storyboard-name story-data))]
            [:div.related-stories
              {:class (when (= (count (:related story-data)) 1) "single-related")}
              (for [story (:related story-data)]
                (rum/with-key (related-story story (= (count (:related story-data)) 1)) (str "related-story-" (:uuid story))))]])]
      [:div.story-comments-container
        {:class (when @(::comments-expanded s) "comments-expanded")}
        (carrot-close-bt {:on-click #(reset! (::comments-expanded s) false)})
        [:div.story-comments-container-inner
          (comments story-data)]]]))