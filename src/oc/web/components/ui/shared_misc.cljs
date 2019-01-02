(ns oc.web.components.ui.shared-misc
  (:require [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]))

(def homepage-testimonials
  [:div.homepage-testimonials-container
    [:img.homepage-testimonials
      {:src (utils/cdn "/img/ML/homepage_testimonials.png")
       :srcSet (str (utils/cdn "/img/ML/homepage_testimonials@2x.png") " 2x")}]])

(def testimonials-section
  [:section.testimonials-section
    [:div.testimonials-section-title
      "Don’t take our word for it."]
    [:div.testimonials-section-subtitle
      "We’re helping teams like yours."]
    [:div.testimonials-cards-container.group
      [:div.testimonial-card.skylight
        [:div.testimonial-header.group
          [:a
            {:href "https://skylight.digital/"
             :target "_blank"}
            [:div.testimonial-logo]]]
        [:div.testimonial-quote
          (str
           "“As a busy leader it's hard to keep "
           "everyone up to date. I use Carrot to "
           "record a quick video update and it lets "
           "me know that everyone's seen it.”")]
        [:div.testimonial-footer.group
          [:div.testimonial-image]
          [:div.testimonial-name
            "Chris Cairns"]
          [:div.testimonial-role
            "Managing Partner"]]]
      [:div.testimonial-card.m-io
        [:div.testimonial-header.group
          [:a
            {:href "https://m.io/"
             :target "_blank"}
            [:div.testimonial-logo]]]
        [:div.testimonial-quote
          (str
           "“On Carrot, my updates get noticed and "
           "get the team talking. I love that.”")]
        [:div.testimonial-footer.group
          [:div.testimonial-image]
          [:div.testimonial-name
            "Tom Hadfield"]
          [:div.testimonial-role
            "CEO"]]]
      [:div.testimonial-card.wayne-edu
        [:div.testimonial-header.group
          [:a
            {:href "https://wayne.edu/"
             :target "_blank"}
            [:div.testimonial-logo]]]
        [:div.testimonial-quote
          (str
           "“Carrot helps me share things the entire "
           "team needs to know about - instead of "
           "burying it somewhere it won’t get "
           "noticed.”")]
        [:div.testimonial-footer.group
          [:div.testimonial-image]
          [:div.testimonial-name
            "Nick DeNardis"]
          [:div.testimonial-role
            "Director of Digital Communications"]]]]])

(def keep-aligned
  [:section.keep-aligned
    [:div.keep-aligned-title
      "It’s never been easier to keep everyone on the same page"]
    [:button.mlb-reset.get-started-button
      "Get started - It's free"]])

(def keep-aligned-bottom
  [:section.keep-aligned
    [:div.keep-aligned-title
      "Company updates that rise above the noise."]
    [:button.mlb-reset.get-started-button
      "Get started - It's free"]])

(def keep-aligned-section
  [:section.home-keep-aligned.group

    [:div.keep-aligned-section
      [:div.keep-aligned-section-row.first-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-right.keep-aligned-section-copy
            [:div.slack-email-container.group.big-web-only
              [:div.slack-email-title
                "Automatically sent to your team on"]
              [:div.slack-email-switch-container
                [:button.mlb-reset.slack-email-switch-bt.email-bt.active
                  [:div.email-logo]
                  "Email"]
                [:button.mlb-reset.slack-email-switch-bt.slack-bt
                  [:div.slack-logo]
                  "Slack"]]]
            [:div.keep-aligned-section-copy-title
              "Your morning digest."]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "Protect your team’s focus (and sanity). Carrot shares your updates together "
               "in a morning newsletter to reduce interruptions and increase engagement.")]
            [:div.slack-email-container.group.tablet-mobile-only
              [:div.slack-email-title
                "Automatically sent to your team on"]
              [:div.slack-email-switch-container
                [:button.mlb-reset.slack-email-switch-bt.email-bt.active
                  [:div.email-logo]
                  "Email"]
                [:button.mlb-reset.slack-email-switch-bt.slack-bt
                  [:div.slack-logo]
                  "Slack"]]]]
          [:div.keep-aligned-section-row-left
            [:div.keep-aligned-section-carion-container
              [:img.keep-aligned-section-screenshot.screenshot-1.carion-1
                {:src (utils/cdn "/img/ML/homepage_screenshots_first_row.png")
                 :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_first_row@2x.png") " 2x")}]
              [:button.keep-aligned-section-next-bt.mlb-reset]
              [:img.keep-aligned-section-screenshot.screenshot-1.carion-1-alt
                {:src (utils/cdn "/img/ML/homepage_screenshots_first_row_slack.png")
                 :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_first_row_slack@2x.png") " 2x")}]]]]]

      [:div.keep-aligned-section-row.second-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-left.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Know who’s engaged."]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "With Carrot, you know who saw your update. "
               "If it’s missed, Carrot works in the background "
               "to remind them for you.")
              [:br][:br]
              (str
               "See which updates create energy and engagement, "
               "and which ones aren’t getting through.")]]
          [:div.keep-aligned-section-row-right
            [:img.keep-aligned-section-screenshot.screenshot-2
              {:src (utils/cdn "/img/ML/homepage_screenshots_second_row.png")
               :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_second_row@2x.png") " 2x")}]]]]

      [:div.keep-aligned-section-row.third-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-right.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Stay consistent with reminders."]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "Carrot reminds you when it’s time to update "
               "your team. Consistent communication builds "
               "trust and transparency.")]
            [:img.keep-aligned-section-copy-values-list
              {:src (utils/cdn "/img/ML/homepage_vertical_values_list.png")
               :srcSet (str (utils/cdn "/img/ML/homepage_vertical_values_list@2x.png") " 2x")}]]
          [:div.keep-aligned-section-row-left
            [:img.keep-aligned-section-screenshot.screenshot-3
              {:src (utils/cdn "/img/ML/homepage_screenshots_third_row.png")
               :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_third_row@2x.png") " 2x")}]]]]

      [:div.keep-aligned-section-row.fourth-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-left.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Space to be thoughtful."]
            [:div.keep-aligned-section-copy-subtitle
              "Carrot gives you room to share more context for what’s happening and why."]
            [:img.keep-aligned-section-copy-interactions-list
              {:src (utils/cdn "/img/ML/homepage_vertical_interactions_list.png")
               :srcSet (str (utils/cdn "/img/ML/homepage_vertical_interactions_list@2x.png") " 2x")}]]
          [:div.keep-aligned-section-row-right
            [:img.keep-aligned-section-screenshot.screenshot-4
              {:src (utils/cdn "/img/ML/homepage_screenshots_fourth_row.png")
               :srcSet (str (utils/cdn "/img/ML/homepage_screenshots_fourth_row@2x.png") " 2x")}]]]]]])

(def animation-lightbox
  [:div.animation-lightbox-container
    {:on-click #(js/OCStaticHideAnimationLightbox)}
    [:div.animation-lightbox
      [:div {:id "youtube-player"}]
      [:button.settings-modal-close.mlb-reset
        {:on-mouse-down #(js/OCStaticHideAnimationLightbox)
         :on-touch-start #(js/OCStaticHideAnimationLightbox)}]]])

(def show-animation-button
  [:button.mlb-reset.show-animation-bt
    {:on-click #(js/OCStaticShowAnimationLightbox)}])