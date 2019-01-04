(ns oc.pages
  (:require [oc.terms :as terms]
            [oc.privacy :as privacy]
            [environ.core :refer (env)]))

(def bootstrap-css
  ;; Bootstrap CSS //getbootstrap.com/
  [:link
    {:rel "stylesheet"
     :href "//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
     :integrity "sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
     :crossorigin "anonymous"}])

(def bootstrap-js
  ;; Bootstrap JavaScript //getf.com/
  [:script
    {:src "//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
     :type "text/javascript"
     :integrity "sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
     :crossorigin "anonymous"}])

(def font-awesome
  ;; Font Awesome icon fonts //fortawesome.github.io/Font-Awesome/cheatsheet/
  [:link
    {:rel "stylesheet"
     :href "//maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css"}])

(def jquery
  [:script
    {:src "//ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"
     :crossorigin "anonymous"}])

(def ziggeo-css
  [:link {:rel "stylesheet" :href "/lib/ziggeo/ziggeo.css"}])
  ; [:link {:rel "stylesheet" :href "https://assets-cdn.ziggeo.com/v2-stable/ziggeo.css"}])

(def ziggeo-js
  [:script {:src "/lib/ziggeo/ziggeo.js"}])
  ; [:script {:src "https://assets-cdn.ziggeo.com/v2-stable/ziggeo.js"}])

(def google-fonts
  ;; Google fonts Muli
  [:link {:href "https://fonts.googleapis.com/css?family=Muli|PT+Serif:700" :rel "stylesheet"}])

(defn google-analytics-init []
  [:script (let [ga-version (if (env :ga-version)
                              (str "'" (env :ga-version) "'")
                              false)
                 ga-tracking-id (if (env :ga-tracking-id)
                                  (str "'" (env :ga-tracking-id) "'")
                                  false)]
             (str "CarrotGA.init(" ga-version "," ga-tracking-id ");"))])

(defn fullstory-init []
  [:script (str "init_fullstory();")])

(defn cdn [img-src]
  (str (when (env :oc-web-cdn-url) (str (env :oc-web-cdn-url) "/" (env :oc-deploy-key))) img-src))

(defn terms [options]
  (terms/terms options))

(defn privacy [options]
  (privacy/privacy options))

(defn carrot-box-thanks [carrot-box-class]
  [:div.carrot-box-container.group
    {:class carrot-box-class
     :style {:display "none"}}
    ; [:img.carrot-box {:src (cdn "/img/ML/carrot_box.svg")}]
    [:div.carrot-box-thanks
      [:div.thanks-headline "Thanks!"]
      "We’ve sent you an email to confirm."
      [:div.carrot-early-access-top.hidden "Get earlier access when your friends sign up with this link:"]
      [:a.carrot-early-access-link.hidden {:href "/?no_redirect=1"} "/"]]])

(defn try-it-form [form-id try-it-class]
  [:form.validate
    {:action (or
              (env :oc-mailchimp-api-endpoint)
              "https://onhq6jg245.execute-api.us-east-1.amazonaws.com/dev/subscribe")
     :method "post"
     :id form-id
     :class "mailchimp-api-subscribe-form"
     :no-validate true}
    [:div.try-it-combo-field
      {:class try-it-class}
      [:div.mc-field-group
        [:input.mail.required
          {:type "text"
           :value ""
           :id "mce-EMAIL"
           :class (str form-id "-input")
           :name "email"
           :placeholder "Email address"}]]
      [:button.mlb-reset.try-it-get-started
        {:type "submit"
         :id "mc-embedded-subscribe"}
        "Get Early Access"]]])

(def desktop-video
  [:div.main-animation-container
    [:img.main-animation
      {:src (cdn "/img/ML/homepage_screenshot.png")
       :srcSet (str (cdn "/img/ML/homepage_screenshot@2x.png") " 2x")}]])

(def testimonials-section
  [:section.testimonials-section
    [:div.testimonials-section-title
      "Don’t take our word for it."]
    [:div.testimonials-section-subtitle
      "We’re helping teams like yours."]
    [:div.testimonials-cards-container.group
      [:div.testimonials-cards-inner.group
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
              "CEO, Founder"]]]
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
              "Digital Communications"]]]]]])

(def keep-aligned-bottom
  [:section.keep-aligned
    [:div.keep-aligned-title
      "Company updates that rise above the noise"]
    [:div.keep-aligned-values-line.big-web-tablet-only
      [:img.keep-aligned-value.value-announcements
        {:src (cdn "/img/ML/homepage_bottom_section_announcements.png")
         :srcSet (str (cdn "/img/ML/homepage_bottom_section_announcements@2x.png") " 2x")}]
      [:img.keep-aligned-value.value-decisions
        {:src (cdn "/img/ML/homepage_bottom_section_decisions.png")
         :srcSet (str (cdn "/img/ML/homepage_bottom_section_decisions@2x.png") " 2x")}]
      [:img.keep-aligned-value.value-team-updates
        {:src (cdn "/img/ML/homepage_bottom_section_team_updates.png")
         :srcSet (str (cdn "/img/ML/homepage_bottom_section_team_updates@2x.png") " 2x")}]
      [:img.keep-aligned-value.value-news
        {:src (cdn "/img/ML/homepage_bottom_section_news.png")
         :srcSet (str (cdn "/img/ML/homepage_bottom_section_news@2x.png") " 2x")}]]
    [:button.mlb-reset.get-started-button
      "Get started - It's free"]])

(def keep-aligned-section
  [:section.home-keep-aligned.group

    [:div.keep-aligned-section
      [:div.keep-aligned-section-row.first-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-left.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Stay in sync"]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "Carrot reminds you when it’s time to update "
               "your team. Consistent communication builds "
               "trust and transparency.")
              [:br][:br]
              (str
               "For longer-form updates, you have space to "
               "share more context for what’s happening "
               "and why.")]]
          [:div.keep-aligned-section-row-right
            [:img.keep-aligned-section-screenshot.screenshot-1.big-web-tablet-only
              {:src (cdn "/img/ML/homepage_screenshots_first_row.png")
               :srcSet (str (cdn "/img/ML/homepage_screenshots_first_row@2x.png") " 2x")}]
            [:img.keep-aligned-section-screenshot.screenshot-1.mobile-only
              {:src (cdn "/img/ML/homepage_screenshots_first_row_mobile.png")
               :srcSet (str (cdn "/img/ML/homepage_screenshots_first_row_mobile@2x.png") " 2x")}]]]]

      [:div.keep-aligned-section-row.second-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-right.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Lower the noise level"]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "Carrot shares updates together in the "
               "morning newsletter to increase visibility with "
               "fewer interruptions.")]
            [:div.slack-email-container.group.big-web-tablet-only
              [:div.slack-email-title.tablet-only
                "Sent daily on Slack or email"]
              [:div.slack-email-switch-container
                [:button.mlb-reset.slack-email-switch-bt.email-bt.active
                  [:div.email-logo]
                  "Email"]
                [:button.mlb-reset.slack-email-switch-bt.slack-bt
                  [:div.slack-logo]
                  "Slack"]]
              [:div.slack-email-title.big-web-only
                "Sent daily on Slack or email"]]
            [:div.slack-email-mobile-title.mobile-only
              "Automatically sent daily on email or Slack..."]]
          [:div.keep-aligned-section-row-left
            [:div.keep-aligned-section-carion-container
              [:div.keep-aligned-section-carion-inner
                [:img.keep-aligned-section-screenshot.screenshot-2.carion-1
                  {:src (cdn "/img/ML/homepage_screenshots_second_row.png")
                   :srcSet (str (cdn "/img/ML/homepage_screenshots_second_row@2x.png") " 2x")}]
                [:img.keep-aligned-section-screenshot.screenshot-2.carion-1-alt
                  {:src (cdn "/img/ML/homepage_screenshots_second_row_slack.png")
                   :srcSet (str (cdn "/img/ML/homepage_screenshots_second_row_slack@2x.png") " 2x")}]]
              [:button.keep-aligned-section-next-bt.mlb-reset]]]]]

      [:div.keep-aligned-section-row.third-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-left.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Know who’s engaged"]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "With Carrot, you know who saw your "
               "update. If it’s missed, Carrot works in the "
               "background to remind them for you.")
              [:br]
              [:br]
              (str
               "See which updates create energy and "
               "engagement, and which ones aren’t getting "
               "through.")]]
          [:div.keep-aligned-section-row-right
            [:img.keep-aligned-section-screenshot.screenshot-3.big-web-tablet-only
              {:src (cdn "/img/ML/homepage_screenshots_third_row.png")
               :srcSet (str (cdn "/img/ML/homepage_screenshots_third_row@2x.png") " 2x")}]
            [:img.keep-aligned-section-screenshot.screenshot-3.mobile-only
              {:src (cdn "/img/ML/homepage_screenshots_third_row_mobile.png")
               :srcSet (str (cdn "/img/ML/homepage_screenshots_third_row_mobile@2x.png") " 2x")}]]]]]])

(def animation-lightbox
  [:div.animation-lightbox-container
    {:onClick "OCStaticHideAnimationLightbox();"}
    [:div.animation-lightbox
      [:div {:id "youtube-player"}]
      [:button.settings-modal-close.mlb-reset
        {;:onClick "OCStaticHideAnimationLightbox();"
         :onMouseDown "OCStaticHideAnimationLightbox();"
         :ontouchstart "OCStaticHideAnimationLightbox();"}]]])

(def show-animation-button
  [:button.mlb-reset.show-animation-bt
    {:onClick "OCStaticShowAnimationLightbox();"}
    [:div.green-play]
    "Carrot in 60 seconds"])

(defn index [options]
  [:div.home-wrap
    {:id "wrap"}
    [:div.main.home-page
      animation-lightbox
      ; Hope page header
      [:section.cta.group

        [:h1.headline
          "Lead with clarity"]
        [:div.subheadline
          (str
           "Leaders struggle to communicate effectively with "
           "fast-growing and distributed teams. Carrot helps "
           "leaders rise above the noise to keep teams focused.")]
        ; (try-it-form "try-it-form-central" "try-it-combo-field-top")
        [:div.get-started-button-container.group
          show-animation-button
          [:button.mlb-reset.get-started-button
            {:id "get-started-centred-bt"}
            "Get started - It's free"]]
        (carrot-box-thanks "carrot-box-thanks-top")
        [:div.carrot-box-container.confirm-thanks.group
          {:style {:display "none"}}
          [:div.carrot-box-thanks
            [:div.thanks-headline "You are Confirmed!"]
            [:div.thanks-subheadline "Thank you for subscribing."]]]

        [:div.main-animation-container
          [:img.main-animation
            {:src (cdn "/img/ML/homepage_screenshot.png")
             :srcSet (str (cdn "/img/ML/homepage_screenshot@2x.png") " 2x")}]]

        [:div.homepage-testimonials-container
          [:img.homepage-testimonials.big-web-tablet-only
            {:src (cdn "/img/ML/homepage_testimonials.png")
             :srcSet (str (cdn "/img/ML/homepage_testimonials@2x.png") " 2x")}]
          [:img.homepage-testimonials.mobile-only
            {:src (cdn "/img/ML/homepage_testimonials_mobile.png")
             :srcSet (str (cdn "/img/ML/homepage_testimonials_mobile@2x.png") " 2x")}]]]

      keep-aligned-section

      testimonials-section

      keep-aligned-bottom
      ]])

(defn pricing
  "Pricing page. This is a copy of oc.web.components.pricing and every change here should be reflected there and vice versa."
  [options]
  [:div.pricing-wrap
    {:id "wrap"}
    [:div.main.pricing
      [:section.pricing-header

        [:h1.pricing-headline
          "Find a plan"]

        [:div.pricing-subheadline
          "Simple plans to stay aligned. Get started for free."]

        [:div.pricing-toggle-line.monthly
          [:span.pricing-toggle-pre "Pay annually (20% off)"]
          [:div.pricing-toggle
            [:span.pricing-toggle-dot]]
          [:span.pricing-toggle-post.post-monthly
            "Pay monthly"]
          [:span.pricing-toggle-post.post-yearly
            "Pay yearly"]]

        [:div.pricing-three-columns.group
          ;; Free
          [:div.pricing-column.free-column
            [:h2.tear-title
              "Free"]
            [:h3.tear-price
              "$0"]
            [:div.tear-subtitle
              [:span.bold
                "Free for small teams"]
              " up to 10 users."]
            [:a.tear-start-bt
              {:href "/sign-up"}
              "Start for free"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "500Mmb storage"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              {:data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "Browse and search posts from the previous 6 months"}
              [:span "3 months history"]]]
          ;; Team
          [:div.pricing-column.team-column
            [:h2.tear-title
              "Standard"]
            [:div.tear-price-select-container
              [:button.mlb-reset.tear-price-select
                "Up to 25 users"]
              [:div.tear-price-select-values
                [:div.tear-price-select-value
                  {:data-value "25"}
                  "Up to 25 users"]
                [:div.tear-price-select-value
                  {:data-value "50"}
                  "Up to 50 users"]
                [:div.tear-price-select-value
                  {:data-value "100"}
                  "Up to 100 users"]]]
            [:h3.tear-price
              "$45"]
            [:div.tear-subtitle
              "Per month, billed annually"]
            [:a.tear-start-bt
              {:href "/sign-up"}
              "Try free for 14 days"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "Unlimited storage"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "Unlimited history"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "Priority support"]]]
          ;; Enterprise
          [:div.pricing-column.enterprise-column
            [:h2.tear-title
              "Enterprise"]
            [:div.tear-price]
            [:div.tear-subtitle
              "A team of more than 250? Let's create a custom plan."]
            [:a.tear-start-bt
              {:href "#"
               :onclick "drift.api.startInteraction({ interactionId: 43235 }); return false;"}
              "Contact Us"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span
                "Everything in the "
                [:span.heavy "Team plan"]
                ", plus:"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              {:data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "Make sure everyone sees what matters most."}
              [:span "Carrot AI"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "On premise option"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "Uptime SLA"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "Premium support"]]]]]

      [:section.pricing-faq
        [:h2.faq-header
          "Frequently asked questions."]

        [:div.faq-row
          [:div.faq-row-question
            "Can I use Carrot for free?"]
          [:div.faq-row-answer
            (str
             "Absolutely! Carrot is free for teams of up to 10 people. The Free plan has a storage "
             "limit, but includes most features. It’s fast to sign up, and no credit card is required.")]]

        [:div.faq-row
          [:div.faq-row-question
            "How is the Standard plan different from the Free plan?"]
          [:div.faq-row-answer
            "The Team plan doesn’t limit file sizes or storage, and includes some premium features, such as:"
            [:ul
              [:li "In-app video recording: Don’t feel like writing? Record a quick video instead."]
              [:li "Recurring updates: Schedule weekly and monthly updates to build consistency."]
              [:li "Advanced permissions: Add private sections for invited members only."]
              [:li "Who saw that: Make sure you’re being heard, and know who’s seen your post."]]]]

        [:div.faq-row
          [:div.faq-row-question
            "Do I need a credit card to sign up?"]
          [:div.faq-row-answer
            "No! You can use Carrot right away without a credit card."]]

        [:div.faq-row
          [:div.faq-row-question
            "What happens if we go over our storage limit in the Free plan?"]
          [:div.faq-row-answer
            (str
             "You can still read, write, edit, and organize existing content, but you won't be able "
             "to add new attachments and videos.")]]

        [:div.faq-row
          [:div.faq-row-question
            "How is pricing calculated for the Team plan?"]
          [:div.faq-row-answer
            (str
             "When you upgrade to the Team plan, you will be charged a base fee of $65 per month "
             "that includes up to 15 team members. Beyond the initial 15 members, you will be "
             "charged a fee of $4 per additional member per month.")
            [:br]
            [:br]
            (str
             "For example, if you have 25 team members, you would pay $65 for the first 15 "
             "members, and $4 for each of the additional 10 members, for a total of $105 per month.")]]

        [:div.faq-row
          [:div.faq-row-question
            "How is the payment being processed?"]
          [:div.faq-row-answer
            (str
             "We use Stripe to process your payment. It's the same payment provider used in "
             "products such as Slack, Pinterest, and Lyft. We do not handle your credit card "
             "information directly.")]]

        [:div.faq-row
          [:div.faq-row-question
            "Are discounts available for nonprofits?"]
          [:div.faq-row-answer
            "Yes! We offer eligible nonprofit organizations a 50% discount. "
            [:a
              {:href "#"
               :onclick "drift.api.startInteraction({ interactionId: 43239 }); return false;"}
              "Contact us"]
            " to see if your organization is eligible."]]

        [:div.faq-row
          [:div.faq-row-question
            "Still have more questions?"]
          [:div.faq-row-answer
            [:a.chat-with-us
              {:href "#"
               :onclick "drift.api.startInteraction({ interactionId: 43234 }); return false;"}
              "Get in touch with us"]]]]

      testimonials-section

      keep-aligned-bottom

    ]])

(defn slack
  "Slack page. This is a copy of oc.web.components.slack and
   every change here should be reflected there and vice versa."
  [options]
  [:div.slack-wrap
    {:id "wrap"}
    [:div.main.slack
      ; Hope page header
      [:section.carrot-plus-slack.group
        animation-lightbox

        [:h1.slack-headline
          "Lead with clarity"]

        [:div.slack-subline
          (str
           "Carrot makes sure key leadership communication "
           "doesn't get lost in fast-moving conversations.")]

        ; (try-it-form "try-it-form-central" "try-it-combo-field-top")
        [:div.slack-button-container.group
            show-animation-button
            [:a.add-to-slack-button
              {:id "get-started-centred-bt"
               :href (env :slack-signup-url)}]]
        (carrot-box-thanks "carrot-box-thanks-top")
        [:div.carrot-box-container.confirm-thanks.group
          {:style {:display "none"}}
          [:div.carrot-box-thanks
            [:div.thanks-headline "You are Confirmed!"]
            [:div.thanks-subheadline "Thank you for subscribing."]]]

        [:div.main-animation-container
          [:img.main-animation
            {:src (cdn "/img/ML/slack_screenshot.png")
             :srcSet (str (cdn "/img/ML/slack_screenshot@2x.png") " 2x")}]]]

      keep-aligned-section

      testimonials-section

      keep-aligned-bottom
      ]])

(defn slack-lander
  "Slack lander page. This is a copy of oc.web.components.slack-lander and
   every change here should be reflected there and vice versa."
  [options]
  [:div.slack-lander-wrap
    {:id "wrap"}
    [:div.main.slack-lander
      ; Hope page header
      [:section.cta.group

        [:h1.headline
          "Join your team on Carrot"]

        [:div.subheadline
          "Carrot makes it simple for Slack teams to stay aligned around what matters most."]
        ; (try-it-form "try-it-form-central" "try-it-combo-field-top")
        [:a.continue-with-slack-bt
          {:href (env :slack-signup-url)}]

        [:div.main-animation-container
          [:img.main-animation
            {:src (cdn "/img/ML/slack_screenshot.png")
             :src-set (str (cdn "/img/ML/slack_screenshot@2x.png") " 2x")}]]]]])

(defn press-kit
  "Press kit page."
  [options]
  [:div.press-kit-wrap
    {:id "wrap"}
    [:div.main.press-kit
      [:section.cta.group
        animation-lightbox

        [:h1.headline
          "Press kit"]

        [:div.press-kit-intro-container
          [:div.press-kit-intro
            [:div.press-kit-intro-title
              "An introduction to Carrot"]
            [:div.press-kit-intro-description
              [:p
                (str
                 "When teamwork gets too noisy and overwhelming, "
                 "leaders struggle to communicate effectively with their teams. "
                 "With Carrot, leaders rise above the noise to communicate what matters.")]

              [:p
                (str
                 "It’s especially tough for leaders to be heard in the "
                 "age of Slack because key announcements, updates, and "
                 "decisions get lost in fast-moving conversations.")]

              [:p
                (str
                 "Carrot makes leadership communication stand out so "
                 "no one misses it, sparks more meaningful discussions, "
                 "and makes sure your team is listening and looped in.")]

              [:p
                (str
                 "With Carrot, leaders are sure their teams are empowered "
                 "with the information they need to stay focused and make better decisions.")]]
            [:div.press-kit-intro-footer
              [:div.press-kit-intro-footer-left
                "Questions?"]
              [:div.press-kit-intro-footer-right
                "We’re always happy to talk about Carrot."
                [:a
                  {:href "#"
                   :onclick "drift.api.startInteraction({ interactionId: 43229 }); return false;"}
                  "SAY HELLO"]]]]]

        [:div.core-ft
          [:div.core-ft-title
            "Core features"]
          [:div.core-ft-content
            [:div.core-ft-grid
              [:div.core-ft-content-title
                "Leadership visibility"]
              [:div.core-ft-content-description
                [:p
                 (str
                  "Key communications stay visible and organized outside of noisy channels, so "
                  "it’s easier for distributed teams to get caught up on their own time without "
                  "worrying they missed something important in a fast-moving conversation. "
                  "It’s also perfect for new employees getting up to speed quickly. With greater "
                  "transparency, your team stays focused and makes better decisions.")]]]
            [:div.core-ft-grid
              [:div.core-ft-content-title
                "Meaningful discussions"]
              [:div.core-ft-content-description
                [:p
                  (str
                    "Carrot gives everyone the time and space they need to add reactions and "
                    "thoughtful comments to a post, and then makes it easy for everyone to see as "
                    "part of the original post. Better discussions provide greater insight into "
                    "what your team is thinking, and give everyone more context about what’s happening.")]]]
            [:div.core-ft-grid
              [:div.core-ft-content-title
                "Make sure you're being heard"]
              [:div.core-ft-content-description
                [:p
                 (str
                  "Too often leaders want to be transparent, but they communicate in ways that aren’t. "
                  "Slack is too noisy, and email is ignored, and above all leaders have no idea if "
                  "anyone is paying attention.")]
                [:p
                 (str
                  "When you share something important in Carrot, you’ll know who’s seen it. If "
                  "people haven’t seen it, Carrot reminds them to make sure they’re looped in. "
                  "That eliminates communication gaps and keeps everyone on the same page.")]]]
            [:div.core-ft-grid
              [:div.core-ft-content-title
                "Communicate more effectively"]
              [:div.core-ft-content-description
                [:p
                  "With Carrot, leaders can rise above the noise to reach their teams more effectively."]
                [:ul
                  [:li
                   "Beautifully formatted updates, including images and video, motivate engagement"]
                  [:li
                   "Video updates add fun and a human touch ‒ great for distributed teams"]
                  [:li
                   "Space for medium- and long-form storytelling, e.g., Lessons Learned, Success Stories, and other stories that unify your team"]
                  [:li
                   "Recurring updates that build consistent communication and build trust"]]]]]]

        [:div.media-res-container
          [:div.media-res
            [:div.media-res-title
              "Media resources"]
            [:div.media-res-content
              [:button.mlb-reset.media-res-content-video
                {:onClick "OCStaticShowAnimationLightbox();"}
                [:img
                  {:src (cdn "/img/ML/press_kit_homepage.png")
                   :srcSet (str (cdn "/img/ML/press_kit_homepage.png") " 2x")}]
                [:div.play-button]]

              [:a.media-res-content-video-download
                {:href "https://carrot-press-kit.s3.amazonaws.com/Carrot-intro-video.mp4"}
                "Download marketing video"]

              [:div.media-res-footer
                [:div.media-res-footer-left
                  "Carrot logo pack"]
                [:div.media-res-footer-right
                  "Carrot logo assets. Light, dark, and the Carrot icon."
                  [:div.media-res-download
                    [:a
                      {:href "https://carrot-press-kit.s3.amazonaws.com/Carrot-Logo.zip"}
                      "Download"]]]]

              [:div.media-res-footer
                [:div.media-res-footer-left
                  "Product screenshots"]
                [:div.media-res-footer-right
                  "Press-friendly product screenshots."
                  [:div.media-res-download
                    [:a
                      {:href "https://carrot-press-kit.s3.amazonaws.com/Carrot-Screenshots.zip"}
                      "Download"]]]]]]]

        [:div.odds-ends
          [:div.odds-ends-title
            "Odd & Ends"]

          [:div.odds-ends-description
            (str
             "Here are a few more details about Carrot and where to find us. "
             "For a quote or to talk more about leadership communication, contact us anytime.")]

          [:div.odds-ends-content
            [:div.odds-ends-content-left
              "Other facts"]
            [:div.odds-ends-content-right
              [:ul
                [:li "Our team is distributed by design"]
                [:li "Carrot is open source. " [:a {:href "https://github.com/open-company" :target "_blank"} "Visit us on Github"]]
                [:li "Carrot has a free plan for small teams, and offers 50% off for approved nonprofits."]
                [:li "Press contact: "
                    [:a {:href "mailto:stuart.levinson@carrot.io"}
                      "Stuart Levinson"]]]]]
          [:div.odds-ends-content
            [:div.odds-ends-content-left
              "Find us online"]
            [:div.odds-ends-content-right
              [:ul
                [:li "Website: "
                     [:a {:href "https://carrot.io"} "https://carrot.io"]]
                [:li "Email: "
                     [:a {:href "mailto:hello@carrot.io"} "hello@carrot.io"]]
                [:li "Chat: "
                     [:a {:onclick "drift.api.startInteraction({ interactionId: 43229 }); return false;"
                          :href "#"}
                      "Say hello"]]
                [:li "Social: "
                     [:a {:href "https://twitter.com/carrot_hq" :target "_blank"} "Twitter"]
                     ", "
                     [:a {:href "https://github.com/open-company" :target "_blank"} "Github"]
                     ", and "
                     [:a {:href "https://blog.carrot.io" :target "_blank"} "Medium"]
                     " (our blog)"]]]]]]

      testimonials-section

      keep-aligned-bottom]])

(defn about
  "About page. This is a copy of oc.web.components.about and
   every change here should be reflected there and vice versa."
  [options]
  [:div.about-wrap
    {:id "wrap"}
    [:div.main.about
      [:section.about-header

        [:h1.about
          "Meet the team"]

        [:div.team-container
          [:div.team-row.group.three-cards
            [:div.team-card.iacopo-carraro
              [:div.user-avatar]
              [:div.user-name
                "Iacopo Carraro"]
              [:div.user-position
                "Software Engineer"
                [:br]
                "in Livorno, Italy"]
              [:div.user-links
                [:a.twitter-link
                  {:href "https://twitter.com/bago2k4"
                   :target "_blank"}]
                [:a.linkedin-link
                  {:href "https://linkedin.com/in/iacopocarraro/"
                   :target "_blank"}]]]
            [:div.team-card.sean-johnson
              [:div.user-avatar]
              [:div.user-name
                "Sean Johnson"]
              [:div.user-position
                "CTO & Founder"
                [:br]
                "in Chapel Hill, USA"]
              [:div.user-links
                [:a.twitter-link
                  {:href "https://twitter.com/belucid"
                   :target "_blank"}]
                [:a.linkedin-link
                  {:href "https://linkedin.com/in/snootymonkey/"
                   :target "_blank"}]]]
            [:div.team-card.georgiana-laudi
              [:div.user-avatar]
              [:div.user-name
                "Georgiana Laudi"]
              [:div.user-position
                "Marketing & CX Advisor"
                [:br]
                "in Montreal, Canada"]
              [:div.user-links
                [:a.twitter-link
                  {:href "https://twitter.com/ggiiaa"
                   :target "_blank"}]
                [:a.linkedin-link
                  {:href "https://linkedin.com/in/georgianalaudi/"
                   :target "_blank"}]]]
            [:div.team-card.stuart-levinson
              [:div.user-avatar]
              [:div.user-name
                "Stuart Levinson"]
              [:div.user-position
                "CEO & Founder"
                [:br]
                "in Cambridge, USA"]
              [:div.user-links
                [:a.twitter-link
                  {:href "https://twitter.com/stuartlevinson"
                   :target "_blank"}]
                [:a.linkedin-link
                  {:href "https://linkedin.com/in/stuartlevinson/"
                   :target "_blank"}]]]
            [:div.team-card.ryan-le-roux
              [:div.user-avatar]
              [:div.user-name
                "Ryan Le Roux"]
              [:div.user-position
                "Head of Design"
                [:br]
                "in Vancouver, Canada"]
              [:div.user-links
                [:a.twitter-link
                  {:href "https://twitter.com/ryanleroux"
                   :target "_blank"}]
                [:a.linkedin-link
                  {:href "https://linkedin.com/in/ryanleroux/"
                   :target "_blank"}]]]
            [:div.team-card.nathan-zorn
              [:div.user-avatar]
              [:div.user-name
                "Nathan Zorn"]
              [:div.user-position
                "Software Engineer"
                [:br]
                "in Charleston, USA"]
              [:div.user-links
                [:a.twitter-link
                  {:href "https://twitter.com/thepug"
                   :target "_blank"}]
                [:a.linkedin-link
                  {:href "https://linkedin.com/in/nathanzorn/"
                   :target "_blank"}]]]]]

        [:div.about-copy
          [:div.about-copy-inner
            [:h3.about-copy-header
              "Why we built Carrot"]
            [:p
              (str
               "In the age of Slack and fast-moving conversations, the lack of "
               "focus and clarity have become a huge problem in the workplace.")]
            [:p
              (str
               "Chat apps keep everyone connected throughout the day, and yet "
               "it's become even more difficult for teams to stay "
               "aligned around what matters most. ")
              [:span.oblique "How can that be?"]]
            [:p
              "This is the question that led us to build Carrot. What we "
              "found is that "
              [:span.heavy "leadership is getting lost in the noise."]]
            [:p
              (str
               "Sharing key information everyone needs alongside random chats just increases "
               "the likelihood it will scroll by without being noticed. It’s difficult for the "
               "team to know what matters, and leaders have no idea if anyone even heard what they said.")]
            [:p
              (str
               "Heavy Slack users ourselves, we wanted to design a Slack-friendly approach "
               "to handle leadership communication. We wanted this “non-chat” communication "
               "to be as fun, delightful and interactive as chat; but we also wanted it to be "
               "asynchronous so people could get caught up on their own time.")]
            [:p
              (str
               "The result is Carrot - a platform for leadership communication that keeps "
               "everyone focused on what matters to build transparency, trust, and stronger teams.")]]

          [:div.about-bottom-copy
            [:div.about-bottom-copy-row.group
              [:div.about-bottom-copy-left
                "Distributed by design."]
              [:div.about-bottom-copy-right
                [:div.about-bottom-copy-description
                  (str
                   "Want to join us? We are always looking for "
                   "amazing people regardless of where they "
                   "call home.")]
                [:a
                  {:href "#"
                   :onclick "drift.api.startInteraction({ interactionId: 43229 }); return false;"}
                   "Say hello"]]]
            [:div.about-bottom-copy-row.group
              [:div.about-bottom-copy-left
                "Crazy for open source."]
              [:div.about-bottom-copy-right
                [:div.about-bottom-copy-description
                  (str
                   "Have an idea you’d like to contribute? A "
                   "new integration you’d like to see?")]
                [:a
                  {:href "https://github.com/open-company"
                   :target "_blank"}
                  "Build with us on git"]]]]]]

      keep-aligned-bottom
    ] ;<!-- main -->
  ])

(defn not-found [{contact-mail-to :contact-mail-to contact-email :contact-email}]
  [:div.not-found
    [:div
      [:div.error-page.not-found-page
        [:img {:src (cdn "/img/ML/carrot_404.svg") :width 338 :height 189}]
        [:h2 "Page Not Found"]
        [:p "The page may have been moved or removed,"]
        [:p.not-logged-in.last "or you may need to " [:a.login {:href "/login"} "login"] "."]
        [:p.logged-in.last "or you may not have access with this account."]
        [:script {:src "/js/set-path.js"}]]]])

(defn server-error [{contact-mail-to :contact-mail-to contact-email :contact-email}]
  [:div.server-error
    [:div
      [:div.error-page
        [:h1 "500"]
        [:h2 "Internal Server Error"]
        [:p "We are sorry for the inconvenience."]
        [:p.last "Please try again later."]
        [:script {:src "/js/set-path.js"}]]]])

(def app-shell
  {:head [:head
          [:meta {:charset "utf-8"}]
          [:meta {:content "IE=edge", :http-equiv "X-UA-Compatible"}]
          [:meta
            {:content "width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"
             :name "viewport"}]
          [:meta {:name "apple-mobile-web-app-capable" :content "yes"}]
          [:meta {:name "slack-app-id" :content (env :oc-slack-app-id)}]
          [:link {:rel "icon" :type "image/png" :href (cdn "/img/carrot_logo.png") :sizes "64x64"}]
          ;; The above 3 meta tags *must* come first in the head;
          ;; any other head content must come *after* these tags
          [:title "Carrot | Leadership communication for growing and distributed teams"]
          ;; Reset IE
          "<!--[if lt IE 9]><script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script><![endif]-->"
          bootstrap-css
          ;; Normalize.css //necolas.github.io/normalize.css/
          ;; TODO inline this into app.main.css
          [:link {:rel "stylesheet" :href "/css/normalize.css"}]
          font-awesome
          ;; OpenCompany CSS
          [:link {:type "text/css" :rel "stylesheet" :href "/css/app.main.css"}]
          ;; jQuery UI CSS
          [:link
            {:rel "stylesheet"
             :href "//ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/themes/smoothness/jquery-ui.css"}]
          ;; Emoji One Autocomplete CSS
          [:link {:type "text/css" :rel "stylesheet" :href "/css/emojione/autocomplete.css"}]
          google-fonts
          ;;  Medium Editor css
          [:link {:type "text/css" :rel "stylesheet" :href "/css/medium-editor/medium-editor.css"}]
          [:link {:type "text/css" :rel "stylesheet" :href "/css/medium-editor/default.css"}]
          ;; Emojione CSS
          [:link {:type "text/css" :rel "stylesheet" :href "/css/emojione.css"}]
          ;; Emojone Sprites CSS
          [:link {:type "text/css" :rel "stylesheet" :href "/css/emoji-mart.css"}]
          ;; CarrotKit Font
          [:link {:type "text/css" :rel "stylesheet" :href "/css/fonts/CarrotKit.css"}]
          ;; MediumEditorTCMention css
          [:link {:type "text/css" :rel "stylesheet" :href "/lib/MediumEditorExtensions/MediumEditorTCMention/mention-panel.min.css"}]
          ;; MediumEditorMediaPicker
          [:link
            {:type "text/css"
             :rel "stylesheet"
             :href "/lib/MediumEditorExtensions/MediumEditorMediaPicker/MediaPicker.css"}]
          [:script {:type "text/javascript" :src "/lib/print_ascii.js"}]
          ;; Automatically load the needed polyfill depending on
          ;; the browser user agent and the available features
          [:script {:src "https://cdn.polyfill.io/v2/polyfill.js"}]
          ;; Ziggeo
          ziggeo-css
          ziggeo-js
          ;; Drift (Support): not enabled for local dev
          ;; [:script {:src (cdn "/js/drift.js")}]
          ;; Headway (What's New)
          [:script {:type "text/javascript" :src "//cdn.headwayapp.co/widget.js"}]]
   :body [:body
          [:div#app
            [:div.oc-loading.active
              [:div.oc-loading-inner
                [:div.oc-loading-heart]
                [:div.oc-loading-body]]]]
          [:div#oc-notifications-container]
          [:div#oc-loading]
          ;; Static js files
          [:script {:type "text/javascript" :src (cdn "/js/static-js.js")}]
          ;; Google Analytics
          [:script {:type "text/javascript" :src "https://www.google-analytics.com/analytics.js"}]
          [:script {:type "text/javascript" :src "/lib/autotrack/autotrack.js"}]
          [:script {:type "text/javascript" :src "/lib/autotrack/google-analytics.js"}]
          (google-analytics-init)
          ;; jQuery needed by Bootstrap JavaScript
          jquery
          ;; Truncate html string
          [:script {:type "text/javascript" :src "/lib/truncate/jquery.dotdotdot.js"}]
          ;; Rangy
          [:script {:type "text/javascript" :src "/lib/rangy/rangy-core.js"}]
          [:script {:type "text/javascript" :src "/lib/rangy/rangy-classapplier.js"}]
          [:script {:type "text/javascript" :src "/lib/rangy/rangy-selectionsaverestore.js"}]
          ;; jQuery textcomplete needed by Emoji One autocomplete
          [:script
            {:src "//cdnjs.cloudflare.com/ajax/libs/jquery.textcomplete/1.8.4/jquery.textcomplete.min.js"
             :type "text/javascript"}]
          ;; WURFL used for mobile/tablet detection
          [:script {:type "text/javascript" :src "//wurfl.io/wurfl.js"}]
          ;; jQuery scrollTo plugin
          [:script {:src "/lib/scrollTo/scrollTo.min.js" :type "text/javascript"}]
          ;; jQuery UI
          [:script {:src "//ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js" :type "text/javascript"}]
          ;; Resolve jQuery UI and Bootstrap tooltip conflict
          [:script "$.widget.bridge('uitooltip', $.ui.tooltip);"]
          bootstrap-js
          ;; Emoji One Autocomplete
          [:script {:src "/js/emojione/autocomplete.js" :type "text/javascript"}]
          ;; ClojureScript generated JavaScript
          [:script {:src "/oc.js" :type "text/javascript"}]
          ;; Utilities
          [:script {:type "text/javascript", :src "/lib/js-utils/pasteHtmlAtCaret.js"}]
          ;; Clean HTML input
          [:script {:src "/lib/cleanHTML/cleanHTML.js" :type "text/javascript"}]
          ;; MediumEditorAutolist
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorAutolist/autolist.js"}]
          ;; MediumEditorMediaPicker
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorMediaPicker/MediaPicker.js"}]
          ;; MediumEditorFileDragging
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorFileDragging/filedragging.js"}]
          ;; MediumEditorTCMention
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorTCMention/index.min.js"}]
          ;; MediumEditorTCMention Panel
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorTCMention/CustomizedTagComponent.js"}]]})

(def prod-app-shell
  {:head [:head
          [:meta {:charset "utf-8"}]
          [:meta {:content "IE=edge", :http-equiv "X-UA-Compatible"}]
          [:meta
            {:content "width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"
             :name "viewport"}]
          [:meta {:name "apple-mobile-web-app-capable" :content "yes"}]
          [:meta {:name "slack-app-id" :content (env :oc-slack-app-id)}]
          ;; Google fonts
          google-fonts
          [:link {:rel "icon" :type "image/png" :href (cdn "/img/carrot_logo.png") :sizes "64x64"}]
          ;; The above 3 meta tags *must* come first in the head;
          ;; any other head content must come *after* these tags
          [:title "Carrot | Leadership communication for growing and distributed teams"]
          ;; Reset IE
          "<!--[if lt IE 9]><script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script><![endif]-->"
          bootstrap-css
          font-awesome
          ;; jQuery UI CSS
          [:link
            {:rel "stylesheet"
             :href "//ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/themes/smoothness/jquery-ui.css"}]
          ;; App single CSS
          [:link {:type "text/css" :rel "stylesheet" :href (cdn "/main.css")}]
          ;; CarrotKit Font
          [:link {:type "text/css" :rel "stylesheet" :href (cdn "/css/fonts/CarrotKit.css")}]
          ;; jQuery needed by Bootstrap JavaScript
          jquery
          ;; Automatically load the needed polyfill depending on
          ;; the browser user agent and the available features
          [:script {:src "https://cdn.polyfill.io/v2/polyfill.min.js"}]
          ;; Ziggeo
          ziggeo-css
          ziggeo-js]
   :body [:body
          [:div#app
            [:div.oc-loading.active
              [:div.oc-loading-inner
                [:div.oc-loading-heart]
                [:div.oc-loading-body]]]]
          [:div#oc-notifications-container]
          [:div#oc-loading]
          ;; Static js files
          [:script {:src (cdn "/js/static-js.js")}]
          ;; jQuery textcomplete needed by Emoji One autocomplete
          [:script
            {:src "//cdnjs.cloudflare.com/ajax/libs/jquery.textcomplete/1.8.4/jquery.textcomplete.min.js"
             :type "text/javascript"}]
          ;; WURFL used for mobile/tablet detection
          [:script {:type "text/javascript" :src "//wurfl.io/wurfl.js"}]
          ;; jQuery UI
          [:script {:src "//ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js" :type "text/javascript"}]
          ;; Resolve jQuery UI and Bootstrap tooltip conflict
          [:script "$.widget.bridge('uitooltip', $.ui.tooltip);"]
          bootstrap-js
          ;; Google Analytics
          [:script {:type "text/javascript" :src "https://www.google-analytics.com/analytics.js" :async true}]
          ;; Drift (Support)
          [:script {:src (cdn "/js/drift.js")}]
          ;; Headway (What's New)
          [:script {:type "text/javascript" :src "//cdn.headwayapp.co/widget.js"}]
          ;; Compiled oc.min.js from our CDN
          [:script {:src (cdn "/oc.js")}]
          ;; Compiled assets
          [:script {:src (cdn "/oc_assets.js")}]
          (when (= (env :fullstory) "true")
            (fullstory-init))
          (google-analytics-init)]})