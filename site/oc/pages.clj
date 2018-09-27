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
      [:div.keep-aligned-section-row.first-row.group
        [:div.keep-aligned-section-copy
          [:div.keep-aligned-section-copy-title
            "Keep it"
            [:br]
            "visible."]
          [:div.keep-aligned-section-copy-subtitle
            (str
             "Key information stays organized and visible so your team can get caught "
             "up anytime without worrying they missed something in fast-moving conversations.")]
          [:img.keep-aligned-section-copy-values-list
            {:src (cdn "/img/ML/homepage_vertical_values_list.png")
             :srcSet (str (cdn "/img/ML/homepage_vertical_values_list@2x.png") " 2x")}]]
        [:img.keep-aligned-section-screenshot.screenshot-1
          {:src (cdn "/img/ML/homepage_screenshots_first_row.png")
           :srcSet (str (cdn "/img/ML/homepage_screenshots_first_row@2x.png") " 2x")}]]

      [:div.keep-aligned-section-row.second-row.group
        [:div.keep-aligned-section-copy
          [:div.keep-aligned-section-copy-title
            "Spark"
            [:br]
            "meaningful"
            [:br]
            "discussions."]
          [:div.keep-aligned-section-copy-subtitle
            (str
             "Reactions and comments stay together with "
             "the post to provide greater context for "
             "what’s happening and why.")
            [:br][:br]
            (str
             "Your team can join the discussion from Slack, "
             "too -- Carrot keeps it all in sync.")]]
        [:img.keep-aligned-section-screenshot.screenshot-2
          {:src (cdn "/img/ML/homepage_screenshots_second_row.png")
           :srcSet (str (cdn "/img/ML/homepage_screenshots_second_row@2x.png") " 2x")}]]

      [:div.keep-aligned-section-row.third-row.group
        [:div.keep-aligned-section-copy
          [:div.keep-aligned-section-copy-title
            "Make sure"
            [:br]
            "you're heard."]
          [:div.keep-aligned-section-copy-subtitle
            (str
             "When you post something new, you’ll always know "
             "who’s seen it. If they haven’t, Carrot reminds them "
             "for you.")
            [:br][:br]
            (str
             "Carrot AI works in the background to keep everyone "
             "on your team looped in to what matters.")]]
        [:img.keep-aligned-section-screenshot.screenshot-3
          {:src (cdn "/img/ML/homepage_screenshots_third_row.png")
           :srcSet (str (cdn "/img/ML/homepage_screenshots_third_row@2x.png") " 2x")}]]

      [:div.keep-aligned-section-row.fourth-row.group
        [:div.keep-aligned-section-copy
          [:div.keep-aligned-section-copy-title
            "Update your"
            [:br]
            "team in"
            [:br]
            "seconds."]
          [:div.keep-aligned-section-copy-subtitle
            (str
             "Starting a new post is fast - with text or video "
             "- so it’s easy to create consistent "
             "communication that builds transparency and "
             "trust. Set up recurring updates, too.")]]
        [:img.keep-aligned-section-screenshot.screenshot-4
          {:src (cdn "/img/ML/homepage_screenshots_fourth_row.png")
           :srcSet (str (cdn "/img/ML/homepage_screenshots_fourth_row@2x.png") " 2x")}]]

      [:div.keep-aligned-section-row.fifth-row.group
        [:div.keep-aligned-section-copy
          [:div.keep-aligned-section-copy-title
            "Perfect for"
            [:br]
            "Slack teams."]
          [:div.keep-aligned-section-copy-subtitle
            (str
             "New posts automatically shared to the "
             "appropriate Slack channel. Join the "
             "discussion right from Slack — Carrot keeps "
             "it all in sync.")]
          [:button.mlb-reset.keep-aligned-section-copy-button
            {:onClick "window.location='/slack';"}]]
        [:img.keep-aligned-section-screenshot.screenshot-5
          {:src (cdn "/img/ML/homepage_screenshots_fifth_row.png")
           :srcSet (str (cdn "/img/ML/homepage_screenshots_fifth_row@2x.png") " 2x")}]]]])

(def animation-lightbox
  [:div.animation-lightbox-container
    {:onClick "OCStaticHideAnimationLightbox();"}
    [:div..animation-lightbox
      [:iframe
        {:width 720 ; 1280
         :height 405 ; 720
         :src "https://www.youtube.com/embed/tAJnbC9_i7s?rel=0&enablejsapi=1"
         :frameborder 0
         :id "carrot-animation"
         :allow "autoplay; encrypted-media"
         :allowfullscreen true}]]])

(defn index [options]
  [:div.home-wrap
    {:id "wrap"}
    [:div.main.home-page
      animation-lightbox
      ; Hope page header
      [:section.cta.group

        [:a.carrot-logo]

        [:h1.headline
          "Communicate"
          [:br]
          "what matters."]
        [:div.subheadline
          "With Carrot, leaders rise above the noise"
          [:br.big-web-only]
          "to keep distributed teams focused and up to date."]
        ; (try-it-form "try-it-form-central" "try-it-combo-field-top")
        [:div.get-started-button-container
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
             :srcSet (str (cdn "/img/ML/homepage_screenshot@2x.png") " 2x")}]]

        [:div.cta-lightbox-starter
          [:h2 "Carrot keeps leaders and teams aligned."]
          [:button.mlb-reset.watch-video-bt
            {:onClick "OCStaticShowAnimationLightbox();"}]]]

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

        [:a.carrot-logo]

        [:h1.pricing-headline
          "Pricing"]

        [:div.pricing-three-columns
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
            [:button.mlb-reset.tear-start-bt
              "Get Started"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              "5MB upload max"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              "500 MB storage"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              "6 months history"]]
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
            [:button.mlb-reset.tear-start-bt
              "Try for Free"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              "No file upload limit"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              "Unlimited storage"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              "Unlimited history"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              "Priority support"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              "Video updates"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              "Recurring updates"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              "Advanced permissions"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              "Team viewership"]]
          ;; Enterprise
          [:div.pricing-column.enterprise-column
            [:h2.tear-title
              "Enterprise"]
            [:div.tear-price]
            [:div.tear-subtitle
              "A team of more than 100? Let's create a custom plan."]
            [:button.mlb-reset.tear-start-bt
              "Contact Us"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              "Includes everything in the "
              [:span.heavy "Team plan"]
              ", plus:"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              "On premise option"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              "Uptime SLA"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              "Premium support"]]]

        ; [:div.pricing-faq
        ;   [:h2.faq-header
        ;     "Frequently asked questions"]

        ;   [:div.faq-row
        ;     [:div.faq-question
        ;       "What are my payment options (credit card and/or invoicing)?"]
        ;     [:div.faq-response
        ;       (str
        ;        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
        ;        "Vestibulum nisi augue, pharetra nec tempus ac, rhoncus eu felis. Sed tempus"
        ;        " massa a ipsum commodo, sed condimentum odio viverra. Donec euismod "
        ;        "mauris et diam pellentesque porta. Donec et laoreet nunc. Maecenas ut leo vel "
        ;        "dui rutrum dapibus. Etiam viverra tortor quam, in fermentum ipsum rutrum sed. "
        ;        "Suspendisse risus eros, gravida vel placerat sit amet, viverra vitae massa.")]]
        ;   [:div.faq-row
        ;     [:div.faq-question
        ;       "My team has credits. How do we use them?"]
        ;     [:div.faq-response
        ;       (str
        ;        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
        ;        "Vestibulum nisi augue, pharetra nec tempus ac, rhoncus eu felis. Sed tempus"
        ;        " massa a ipsum commodo, sed condimentum odio viverra. Donec euismod "
        ;        "mauris et diam pellentesque porta. Donec et laoreet nunc. Maecenas ut leo vel "
        ;        "dui rutrum dapibus. Etiam viverra tortor quam, in fermentum ipsum rutrum sed. "
        ;        "Suspendisse risus eros, gravida vel placerat sit amet, viverra vitae massa.")]]
        ;   [:div.faq-row
        ;     [:div.faq-question
        ;       "We need to add new users to our team. How will that be billed?"]
        ;     [:div.faq-response
        ;       (str
        ;        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
        ;        "Vestibulum nisi augue, pharetra nec tempus ac, rhoncus eu felis. Sed tempus"
        ;        " massa a ipsum commodo, sed condimentum odio viverra. Donec euismod "
        ;        "mauris et diam pellentesque porta. Donec et laoreet nunc. Maecenas ut leo vel "
        ;        "dui rutrum dapibus. Etiam viverra tortor quam, in fermentum ipsum rutrum sed. "
        ;        "Suspendisse risus eros, gravida vel placerat sit amet, viverra vitae massa.")]]
        ;   [:div.faq-row
        ;     [:div.faq-question
        ;       "My team wasnts to cancel its subscription. How do we do that? Can we get a refund?"]
        ;     [:div.faq-response
        ;       (str
        ;        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
        ;        "Vestibulum nisi augue, pharetra nec tempus ac, rhoncus eu felis. Sed tempus"
        ;        " massa a ipsum commodo, sed condimentum odio viverra. Donec euismod "
        ;        "mauris et diam pellentesque porta. Donec et laoreet nunc. Maecenas ut leo vel "
        ;        "dui rutrum dapibus. Etiam viverra tortor quam, in fermentum ipsum rutrum sed. "
        ;        "Suspendisse risus eros, gravida vel placerat sit amet, viverra vitae massa.")]]
        ;   [:div.faq-row
        ;     [:div.faq-question
        ;       "What are my payment options (credit card and/or invoicing)?"]
        ;     [:div.faq-response
        ;       (str
        ;        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
        ;        "Vestibulum nisi augue, pharetra nec tempus ac, rhoncus eu felis. Sed tempus"
        ;        " massa a ipsum commodo, sed condimentum odio viverra. Donec euismod "
        ;        "mauris et diam pellentesque porta. Donec et laoreet nunc. Maecenas ut leo vel "
        ;        "dui rutrum dapibus. Etiam viverra tortor quam, in fermentum ipsum rutrum sed. "
        ;        "Suspendisse risus eros, gravida vel placerat sit amet, viverra vitae massa.")]]
        ;   [:div.contact-us-row
        ;     "If you have more questions, don’t hesitate to "
        ;     [:a.contact
        ;       {:href oc-urls/contact-mail-to}
        ;       "contact us"]]]
        ]

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
        [:div.balloon.big-blue]
        [:div.balloon.small-green]
        [:div.balloon.big-green]
        [:div.balloon.small-purple-face]
        [:div.balloon.big-yellow]
        [:div.balloon.small-purple]

        [:div.carrot-plus-slack]

        [:h3.slack
          "Slack keeps your team connected in the moment."]

        [:h1.slack
          "Carrot keeps it aligned over time."]

        [:div.slack-subline
          (str
           "Key updates and announcements get lost in fast-moving chat and stuffed inboxes. "
           "Carrot makes it simple for Slack teams to stay aligned around what matters most.")]

        ; (try-it-form "try-it-form-central" "try-it-combo-field-top")
        [:div.get-started-button-container
          [:button.mlb-reset.signin-with-slack
            {:id "get-started-centred-bt"}
            [:span.slack-white-icon]
            [:span.slack-copy "Add to Slack"]]]
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
             :srcSet (str (cdn "/img/ML/slack_screenshot@2x.png") " 2x")}]]

        core-values-list]

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
        [:h1.about "About us"]
        [:div.about-subline
          "We believe real transparency and alignment requires focused communication."]
        [:div.about-copy
          [:p
            (str
             "Workplace chat is everywhere, and "
             "yet teams are still struggling to "
             "stay on the same page - especially "
             "growing or distributed teams.")]
          [:p
            (str
             "Chat might be ideal for fast and "
             "spontaneous conversations in the moment, "
             "but it gets noisy and drowns out "
             "important information and follow-on "
             "discussions that teams need to stay "
             "aligned over time.")]
          [:p
            (str
             "Carrot is a company digest that gives "
             "everyone time to read and react to "
             "important information without worrying "
             "they missed it.")]
          [:p
            (str
             "When it’s this easy to see what matters "
             "most, busy teams stay informed and "
             "aligned with fewer distractions.")]]
        [:div.team-container
          [:div.team-row.group.three-cards
            [:div.team-card.iacopo-carraro
              [:div.user-avatar]
              [:div.user-name
                "Iacopo Carraro"]
              [:div.user-position
                "Software Engineer"]
              [:a.linkedin-link
                {:href "https://linkedin.com/in/iacopocarraro/"
                 :target "_blank"}]]
            [:div.team-card.sean-johnson
              [:div.user-avatar]
              [:div.user-name
                "Sean Johnson"]
              [:div.user-position
                "CTO and co-founder"]
              [:a.linkedin-link
                {:href "https://linkedin.com/in/snootymonkey/"
                 :target "_blank"}]]
            [:div.team-card.georgiana-laudi
              [:div.user-avatar]
              [:div.user-name
                "Georgiana Laudi"]
              [:div.user-position
                "Marketing and CX Advisor"]
              [:a.linkedin-link
                {:href "https://linkedin.com/in/georgianalaudi/"
                 :target "_blank"}]]]
          [:div.team-row.group.three-cards
            [:div.team-card.stuart-levinson
              [:div.user-avatar]
              [:div.user-name
                "Stuart Levinson"]
              [:div.user-position
                "CEO and co-founder"]
              [:a.linkedin-link
                {:href "https://linkedin.com/in/stuartlevinson/"
                 :target "_blank"}]]
            [:div.team-card.ryan-le-roux
              [:div.user-avatar]
              [:div.user-name
                "Ryan Le Roux"]
              [:div.user-position
                "Chief Design Officer"]
              [:a.linkedin-link
                {:href "https://linkedin.com/in/ryanleroux/"
                 :target "_blank"}]]
            [:div.team-card.nathan-zorn
              [:div.user-avatar]
              [:div.user-name
                "Nathan Zorn"]
              [:div.user-position
                "Software Engineer"]
              [:a.linkedin-link
                {:href "https://linkedin.com/in/nathanzorn/"
                 :target "_blank"}]]]]

        [:div.other-cards.group
          [:div.other-card.heart-card
            [:div.card-icon]
            [:div.card-title
              "Careers at Carrot"]
            [:div.card-content
              (str
               "Want to join us? We are always looking for "
               "amazing people no matter where they live. ")]
            [:a.card-button
              {:href (:contact-mail-to options)
               :onTouchStart ""}
              "Say hello!"]]
          [:div.other-card.oss-card
            [:div.card-icon]
            [:div.card-title
              "We’re Crazy for Open Source"]
            [:div.card-content
              (str
               "Have an idea you’d like to contribute? A new "
               "integration you’d like to see?")]
            [:a.card-button
              {:href "https://github.com/open-company"
               :onTouchStart ""
               :target "_blank"}
              "Build with us on GitHub"]]]

        [:div.about-bottom-get-started
          [:div.about-alignment
            "Keep everyone aligned around what matters most."]
          [:div.get-started-button-container
            [:button.mlb-reset.get-started-button.bottom-button
              {:id "get-started-bottom-bt"}
              "Get started for free"]]]]
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
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorFileDragging/filedragging.js"}]]})

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