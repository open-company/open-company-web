(ns oc.web.components.blog
  (:require [rum.core :as rum]
            [oc.web.components.ui.site-header :refer (site-header)]
            [oc.web.components.ui.site-footer :refer (site-footer)]))

(rum/defc blog []
  [:div.blog-wrap {:id "wrap"}
    (site-header)

    [:div.blog
      [:div.blog-head
        [:h1 "Blog"]
        [:div.divider-line]]

      [:div.blog-body.group
        [:div.post.group
          [:div.post-column
            [:div.post-title "The inevitable future: startup transparency"]
            [:div.post-author "By Stuart Levinson • April 12th • 10 min read"]]
          [:div.post-column
            [:div.post-body "Sometimes you want to do more than update a topic. You want to cover a range of topics or you want to be more expressive. Perhaps it’s for an all-hands update, or maybe it’s for investors and advisors. Maybe you want to pull together information …
  potential recruit or new investor? It’s simple to curate a story and share it. Stories can be invite-only, shared with the team, or made public."]]]

        [:div.post.group
          [:div.post-column
            [:div.post-title "The inevitable future: startup transparency"]
            [:div.post-author "By Stuart Levinson • April 12th • 10 min read"]]
          [:div.post-column
            [:div.post-body "Sometimes you want to do more than update a topic. You want to cover a range of topics or you want to be more expressive. Perhaps it’s for an all-hands update, or maybe it’s for investors and advisors. Maybe you want to pull together information …
potential recruit or new investor? It’s simple to curate a story and share it. Stories can be invite-only, shared with the team, or made public."]]]

        [:div.post.group
          [:div.post-column
            [:div.post-title "The inevitable future: startup transparency"]
            [:div.post-author "By Stuart Levinson • April 12th • 10 min read"]
            [:div.post-body "Sometimes you want to do more than update a topic. You want to cover a range of topics or you want to be more expressive. Perhaps it’s for an all-hands update, or maybe it’s for investors and advisors. Maybe you want to pull together information …
potential recruit or new investor? It’s simple to curate a story and share it. Stories can be invite-only, shared with the team, or made public."]]
          [:div.post-column
            [:div.post-image]]]]]

    (site-footer)])