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
  [:link {:href "https://fonts.googleapis.com/css?family=Muli|PT+Serif" :rel "stylesheet"}])

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

(def core-values-list
  [:div.core-values-list.group
    [:div.core-value-container.key-announcement
      [:div.core-value-header.group
        [:div.core-value-icon]
        [:div.core-value
          "Announcements"]]
      [:div.core-value-white-box
        [:div.core-value-box-header
          "Product "
          [:span.dot "•"]
          " 45 views"]
        [:div.core-value-box-title
          "Updates to billing & subscriptions (beta edition)"]]]
    [:div.core-value-container.team-updates
      [:div.core-value-header.group
        [:div.core-value-icon]
        [:div.core-value
          "Team updates"]]
      [:div.core-value-white-box
        [:div.core-value-box-header
          "General "
          [:span.dot "•"]
          " 22 views"]
        [:div.core-value-box-title
          "June 25, 2018 all hands video highlights"]]]
    [:div.core-value-container.strategic-plans
      [:div.core-value-header.group
        [:div.core-value-icon]
        [:div.core-value
          "Decisions"]]
      [:div.core-value-white-box
        [:div.core-value-box-header
          "Strategy "
          [:span.dot "•"]
          " 67 views"]
        [:div.core-value-box-title
          "Product roadmap review presentation and PDF"]]]
    [:div.core-value-container.stories
      [:div.core-value-header.group
        [:div.core-value-icon]
        [:div.core-value
          "Stories"]]
      [:div.core-value-white-box
        [:div.core-value-box-header
          "Design "
          [:span.dot "•"]
          " 34 views"]
        [:div.core-value-box-title
          "How we pulled off our biggest launch ever 🤩"]]]])

(def testimonials-section
  [:section.testimonials-section
    [:div.testimonials-section-title
      "Don’t take our word for it."]
    [:div.testimonials-section-subtitle
      "We’re helping teams like yours."]
    [:div.testimonials-cards-container.group
      [:div.testimonial-card
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
            "CHRIS CAIRNS"]
          [:div.testimonial-role
            "Managing Partner"]]]
      [:div.testimonial-card
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
      [:div.testimonial-card
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

(def no-credit-card
  [:div.no-credit-card
    "No credit card required&nbsp;&nbsp;•&nbsp;&nbsp;Works with Slack"])

(def keep-aligned-bottom
  [:section.keep-aligned
    [:div.keep-aligned-title
      "Keep everyone aligned around"
      [:br]
      "what matters most."]
    [:button.mlb-reset.get-started-button
      "Get started for free"]
    no-credit-card])

(def keep-aligned-section
  [:section.home-keep-aligned.group

    [:div.keep-aligned-section
      [:div.keep-aligned-section-row.first-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-left.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Provide clarity."]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "Key information stays organized and visible so your team can get caught "
               "up anytime without worrying they missed something in fast-moving conversations.")]
            [:img.keep-aligned-section-copy-values-list
              {:src (cdn "/img/ML/homepage_vertical_values_list.png")
               :srcSet (str (cdn "/img/ML/homepage_vertical_values_list@2x.png") " 2x")}]]
          [:div.keep-aligned-section-row-right
            [:img.keep-aligned-section-screenshot.screenshot-1
              {:src (cdn "/img/ML/homepage_screenshots_first_row.png")
               :srcSet (str (cdn "/img/ML/homepage_screenshots_first_row@2x.png") " 2x")}]]]]

      [:div.keep-aligned-section-row.second-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-right.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Spark meaningful discussions."]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "Reactions and comments stay together with "
               "the post to provide greater context for "
               "what’s happening and why.")
              [:br][:br]
              (str
               "Your team can join the discussion from Slack, "
               "too -- Carrot keeps it all in sync.")]]
          [:div.keep-aligned-section-row-left
            [:img.keep-aligned-section-screenshot.screenshot-2
              {:src (cdn "/img/ML/homepage_screenshots_second_row.png")
               :srcSet (str (cdn "/img/ML/homepage_screenshots_second_row@2x.png") " 2x")}]]]]

      [:div.keep-aligned-section-row.third-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-left.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Make sure you're heard."]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "When you post something new, you’ll always know "
               "who’s seen it. If they haven’t, Carrot reminds them "
               "for you.")
              [:br][:br]
              (str
               "Carrot AI works in the background to keep everyone "
               "on your team looped in to what matters.")]]
          [:div.keep-aligned-section-row-right
            [:img.keep-aligned-section-screenshot.screenshot-3
              {:src (cdn "/img/ML/homepage_screenshots_third_row.png")
               :srcSet (str (cdn "/img/ML/homepage_screenshots_third_row@2x.png") " 2x")}]]]]

      [:div.keep-aligned-section-row.fourth-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-right.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Update your team in seconds."]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "Starting a new post is fast - with text or video "
               "- so it’s easy to create consistent "
               "communication that builds transparency and "
               "trust. Set up recurring updates, too.")]]
          [:div.keep-aligned-section-row-left
            [:img.keep-aligned-section-screenshot.screenshot-4
              {:src (cdn "/img/ML/homepage_screenshots_fourth_row.png")
               :srcSet (str (cdn "/img/ML/homepage_screenshots_fourth_row@2x.png") " 2x")}]]]]

      [:div.keep-aligned-section-row.fifth-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-left.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Perfect for Slack teams."]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "New posts automatically shared to the "
               "appropriate Slack channel. Join the "
               "discussion right from Slack — Carrot keeps "
               "it all in sync.")]
            [:button.mlb-reset.keep-aligned-section-copy-button
              {:onClick "window.location='/slack';"}]]
          [:div.keep-aligned-section-row-right
            [:img.keep-aligned-section-screenshot.screenshot-5
              {:src (cdn "/img/ML/homepage_screenshots_fifth_row.png")
               :srcSet (str (cdn "/img/ML/homepage_screenshots_fifth_row@2x.png") " 2x")}]]]]]])

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
    {:onClick "OCStaticShowAnimationLightbox();"}])

(defn index [options]
  [:div.home-wrap
    {:id "wrap"}
    [:div.main.home-page
      animation-lightbox
      ; Hope page header
      [:section.cta.group

        [:a.carrot-logo
          {:href "/?no_redirect=1"}]

        [:h1.headline
          "Communicate "
          [:br.big-web-only]
          "what matters."]
        [:div.subheadline
          (str
           "Leaders struggle to communicate a clear direction that keeps everyone "
           "aligned because chat and email are noisy and overwhelming. Carrot rises "
           "above the noise to keep distributed teams up to date and focused.")]
        ; (try-it-form "try-it-form-central" "try-it-combo-field-top")
        [:div.get-started-button-container.group
          show-animation-button
          [:button.mlb-reset.get-started-button
            {:id "get-started-centred-bt"}
            "Get started for free"]]
        no-credit-card
        (carrot-box-thanks "carrot-box-thanks-top")
        [:div.carrot-box-container.confirm-thanks.group
          {:style {:display "none"}}
          [:div.carrot-box-thanks
            [:div.thanks-headline "You are Confirmed!"]
            [:div.thanks-subheadline "Thank you for subscribing."]]]

        [:div.main-animation-container
          [:img.main-animation
            {:src (cdn "/img/ML/homepage_screenshot.png")
             :srcSet (str (cdn "/img/ML/homepage_screenshot@2x.png") " 2x")}]]]

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

        [:a.carrot-logo
          {:href "/?no_redirect=1"}]

        [:h1.pricing-headline
          "Pricing"]

        [:div.pricing-subheadline
          "Keep everyone aligned around what matters most"]

        [:div.pricing-three-columns.group
          ;; Free
          [:div.pricing-column.free-column
            [:h2.tear-title
              "Free"]
            [:h3.tear-price
              "$0"]
            [:h5.tear-period
              "/month"]
            [:div.tear-subtitle
              "Free for small teams up to 10 users."]
            [:a.tear-start-bt
              {:href "/sign-up"}
              "Start Free"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              {:data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "Attachments up to 20MB"}
              [:span "20MB upload max"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "1TB storage"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              {:data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "Browse and search posts from the previous 6 months"}
              [:span "6 months history"]]]
          ;; Team
          [:div.pricing-column.team-column
            [:h2.tear-title
              "Team"]
            [:h3.tear-price
              "$65"]
            [:h5.tear-period
              "/month"]
            [:div.tear-subtitle
              "Includes 15 users, additional users are $4 /mo."]
            [:a.tear-start-bt
              {:href "/sign-up"}
              "Start Free"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              {:data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "Attachments up to 100MB"}
              [:span "100MB file upload"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "Unlimited storage"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              {:data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "No limits on history maintained in Carrot"}
              [:span "Unlimited history"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              {:data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "Support via chat and email"}
              [:span "Priority support"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              {:data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "Don’t feel like writing? Record a quick video"}
              [:span "Video updates"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              {:data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "Schedule and assign weekly and monthly updates to build consistency."}
              [:span "Recurring updates"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              {:data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "Add private sections for invited team members only"}
              [:span "Advanced permissions"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              {:data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
               :title "Make sure you’re being heard, and know who’s seen your post"}
              [:span "Team viewership"]]]
          ;; Enterprise
          [:div.pricing-column.enterprise-column
            [:h2.tear-title
              "Enterprise"]
            [:div.tear-price]
            [:div.tear-subtitle
              "A team of more than 100? Let's create a custom plan."]
            [:a.tear-start-bt
              {:href "mailto:hello@carrot.io"}
              "Contact Us"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span
                "Includes everything in the "
                [:span.heavy "Team plan"]
                ", plus:"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
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
             "Absolutely! You can use Carrot for free with teams of up to 10 people. "
             "The storage limit is 1TB and there’s a maximum upload of 20mbs. When you "
             "sign up for the free plan you’ll get to try a fully-featured Team plan "
             "(incl in-app video recording, recurring updates and advanced permissions) for "
             "30 days. It’s fast to sign up, and no credit card is required.")]]

        [:div.faq-row
          [:div.faq-row-question
            "How is the Team plan different from the Free plan?"]
          [:div.faq-row-answer
            "With the Team plan you get unlimited  upload file size and storage, and premium features, like:"
            [:ul
              [:li [:span.heavy "In-app video recording:"] " Don’t feel like writing? Record a quick video instead."]
              [:li [:span.heavy "Recurring updates:"] " Schedule weekly and monthly updates to build consistency."]
              [:li [:span.heavy "Advanced permissions:"] " Add private sections for invited members only."]
              [:li [:span.heavy "Viewership: Make sure"] " you’re being heard, and know who’s seen your post."]]]]

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
             "You can still read, write, edit, and organize existing content, "
             "but you won't be able to add new attachments and videos.")]]

        [:div.faq-row
          [:div.faq-row-question
            "How is pricing calculated for the Team plan?"]
          [:div.faq-row-answer
            (str
             "When you upgrade to the Team plan, you will be charged a base fee of $65 per "
             "month that includes up to 15 team members. Beyond the initial 15 members, you "
             "will be charged a fee of $4 per additional member per month.")
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
             "We use Stripe to process your payment. It's the same payment provider used in products "
             "such as Slack, Pinterest, and Lyft. We do not handle your credit card information directly.")]]

        [:div.faq-row
          [:div.faq-row-question
            "Are discounts available for nonprofits?"]
          [:div.faq-row-answer
            "Yes! We offer eligible nonprofit organizations a 50% discount. "
            [:a
              {:href "mailto:hello@carrot.io"
               :target "_blank"}
              "Contact us"]
            " to see if your organization is eligible."]]

        [:div.faq-row
          [:div.faq-row-question
            "Still have more questions?"
            [:a.chat-with-us
              {:href "mailto:hello@carrot.io"
               :target "_blank"}
              "Chat with us about carrot"]]]]

      testimonials-section
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

        [:a.carrot-logo
          {:href "/?no_redirect=1"}]

        [:h1.slack
          "Designed for Slack teams."]

        [:div.slack-subline
          (str
           "Carrot makes sure key leadership communication "
           "doesn't get lost in fast-moving conversations.")]

        ; (try-it-form "try-it-form-central" "try-it-combo-field-top")
        [:div.slack-button-container.group
            show-animation-button
            [:button.mlb-reset.add-to-slack-button
              {:id "get-started-centred-bt"}]]
        no-credit-card
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

(defn about
  "About page. This is a copy of oc.web.components.about and
   every change here should be reflected there and vice versa."
  [options]
  [:div.about-wrap
    {:id "wrap"}
    [:div.main.about
      [:section.about-header
        [:a.carrot-logo
          {:href "/?no_redirect=1"}]
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
            [:div.about-copy-title
              (str
               "In the age of Slack, the lack of clear leadership "
               "is the single, greatest problem in the workplace.")]
            [:p
              (str
               "Sure, teams are more connected than ever with chat apps like "
               "Slack - and yet, it has become even more difficult for those same "
               "teams to stay aligned around what matters most. ")
              [:span.oblique "How can that be?"]]
            [:p
              "This is the question that led us to build Carrot. What we found is that "
              [:span.heavy "leadership is getting lost in the noise."]]
            [:p
              (str
               "Leadership in the age of Slack isn’t easy. Sharing key information "
               "everyone needs alongside other random chats just increases the likelihood "
               "it will scroll by without getting noticed or read. Nor are people motivated "
               "to react, comment or ask questions when the conversation is hard to follow. "
               "Above all, leaders have no idea if anyone is paying attention to what they’ve said.")]
            [:p
              (str
               "Heavy Slack users ourselves, we wanted to design a Slack-friendly approach to "
               "handle leadership communication. We wanted this “non-chat” communication to be "
               "as fun, delightful and interactive as chat; but we also wanted it to be asynchronous "
               "so people could get caught up on their own time.")]
            [:p
              (str
               "The result is Carrot - a platform for leadership communication that keeps everyone "
               "focused on what matters to build transparency, trust, and stronger teams.")]]

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
                  {:href "mailto:hello@carrot.io"
                   :target "_blank"}
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
          google-fonts
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
          ;; Compiled oc.min.js from our CDN
          [:script {:src (cdn "/oc.js")}]
          ;; Compiled assets
          [:script {:src (cdn "/oc_assets.js")}]
          (when (= (env :fullstory) "true")
            (fullstory-init))
          (google-analytics-init)]})