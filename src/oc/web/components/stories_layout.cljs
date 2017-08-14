(ns oc.web.components.stories-layout
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [oc.web.router :as router]
            [oc.web.urls :as oc-urls]
            [oc.web.components.entry-card :refer (entry-card entry-card-empty)]))

(rum/defc stories-layout
  [story-data]
  [:div.stories-layout
    (let [sorted-stories (vec (reverse (sort-by :created-at (:entries story-data))))]
      [:div.story-cards-container.group
        (for [story sorted-stories
              :let [has-headline (not (empty? (:headline story)))
                    has-body (not (empty? (:body story)))]]
          [:div.story-cards-container-row.group
            {:key (str "stories-row-" idx)}
            (rum/with-key (entry-card story has-headline has-body) (str "story-latest-" (:uuid story)))])])])