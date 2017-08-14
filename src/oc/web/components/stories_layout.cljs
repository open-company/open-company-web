(ns oc.web.components.stories-layout
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [cuerdas.core :as s]
            [oc.web.router :as router]
            [oc.web.urls :as oc-urls]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.entry-card :refer (entry-card entry-card-empty)]))

(rum/defc stories-layout < rum/static
                           {:after-render (fn [s]
                                            (dommy/set-style! (sel1 [:div.stories-vertical-line]) :height (str (+ (.-clientHeight (sel1 [:div.story-cards-container])) 30) "px"))
                                           s)}
  [story-data]
  [:div.stories-layout
    (let [sorted-stories (vec (reverse (sort-by :created-at (:entries story-data))))]
      [:div.story-cards-container.group
        (for [story sorted-stories
              :let [has-headline (not (empty? (:headline story)))
                    has-body (not (empty? (:body story)))]]
          [:div.story-card-row.group
            {:key (str "story-latest-" (:uuid story))}
            [:div.story-card-row-left.group
              (user-avatar-image (first (:author story)))]
            (entry-card story has-headline has-body)])
        [:div.stories-vertical-line]])])
