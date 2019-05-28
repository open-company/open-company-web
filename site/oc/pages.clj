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
        ;; First column
        [:div.testimonals-cards-column.group
          [:div.testimonial-card.peak-support
            [:div.testimonial-quote
              (str
               "“Carrot fixed our issue of "
               "email overload. We use it "
               "for important company "
               "communications, shout- "
               "outs, and announcements "
               "so they won’t get buried in "
               "everyone’s inbox. It’s been "
               "a big win for us.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Jon Steiman"]
              [:div.testimonial-role
                [:a
                  {:href "https://www.peaksupport.io/"
                   :target "_blank"}
                  "Peak Support, CEO"]]]]
            [:div.testimonial-card.wayne-state-univerity
              [:div.testimonial-quote
                (str
                 "“Carrot helps me share "
                 "things the entire team "
                 "needs to know - instead of "
                 "burying it somewhere it "
                 "won’t get noticed.”")]
              [:div.testimonial-footer.group
                [:div.testimonial-image]
                [:div.testimonial-name
                  "Nick DeNardis"]
                [:div.testimonial-role
                  [:a
                    {:href "https://wayne.edu/"
                     :targe "_blank"}
                    "Wayne State University, Director of Communications"]]]]]
        ;; Second column
        [:div.testimonals-cards-column.group
          [:div.testimonial-card.oval-money
            [:div.testimonial-quote
              (str
               "“Carrot keeps our "
               "distributed team informed "
               "about what matters most.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Edoardo Benedetto"]
              [:div.testimonial-role
                [:a
                  {:href "https://ovalmoney.com/"
                   :target "_blank"}
                  "Oval Money, Head of Design"]]]]
          [:div.testimonial-card.helloticket
            [:div.testimonial-quote
              (str
               "“Staying aligned across two "
               "offices is hard. Slack feels "
               "too ‘light’ and crazy for "
               "important information; and "
               "no one wants to read "
               "weekly emails. With Carrot, "
               "we have a knowledge base "
               "of what's going on with the "
               "company that’s made it "
               "easy to stay aligned.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Alberto Martinez"]
              [:div.testimonial-role
                [:a
                  {:href "https://ovalmoney.com/"
                   :target "_blank"}
                  "Hellotickets, CEO"]]]]]
        ;; Third column
        [:div.testimonals-cards-column.group
          [:div.testimonial-card.m-io
            [:div.testimonial-quote
              (str
               "“On Carrot, my updates "
               "get noticed and get the "
               "team talking. I love that.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Tom Hadfield"]
              [:div.testimonial-role
                [:a
                  {:href "https://m.io/"
                   :target "_blank"}
                  "M.io, CEO"]]]]
          [:div.testimonial-card.partner-hero
            [:div.testimonial-quote
              (str
               "“Carrot is where we "
               "communicate important "
               "information when we need "
               "everyone to see it - "
               "regardless of their time "
               "zone.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Andrew Love"]
              [:div.testimonial-role
                [:a
                  {:href "https://partnerhero.com/"
                   :targe "_blank"}
                  "PartnerHero, Director of R&D"]]]]
          [:div.testimonial-card.blend-labs
            [:div.testimonial-quote
              (str
               "“Carrot reminds us when "
               "it’s time to share a weekly "
               "update - so it’s easier for "
               "everyone to stay in sync.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Sara Vienna"]
              [:div.testimonial-role
                [:a
                  {:href "https://bl3ndlabs.com/"
                   :target "_blank"}
                  "BL3NDlabs, Head of Design"]]]]]
        ;; Fourth column
        [:div.testimonals-cards-column.group
          [:div.testimonial-card.novo
            [:div.testimonial-quote
              (str
               "“We use Carrot when we "
               "need to make sure "
               "everyone is on the same "
               "page across all our offices "
               "here and abroad. It helps us "
               "share BIG wins, and in a "
               "fast-growing startup "
               "keeping that family vibe, "
               "its awesome.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Tyler McIntyre"]
              [:div.testimonial-role
                [:a
                  {:href "https://banknovo.com/"
                   :target "_blank"}
                  "Novo, CEO"]]]]
          [:div.testimonial-card.skylight-digital
            [:div.testimonial-quote
              (str
               "“Carrot is a perfect "
               "complement for Slack. We "
               "use it for longer-form "
               "weekly updates no one "
               "should miss.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Chris Cairns"]
              [:div.testimonial-role
                [:a
                  {:href "https://skylight.digital/"
                   :target "_blank"}
                  "Skylight Digital, Managing Director"]]]]]]]])

(def animation-lightbox
  [:div.animation-lightbox-container
    {:onClick "OCStaticHideAnimationLightbox(event);"}
    [:div.animation-lightbox
      [:div {:id "youtube-player"}]
      [:button.settings-modal-close.mlb-reset
        {;:onClick "OCStaticHideAnimationLightbox(event);"
         :onMouseDown "OCStaticHideAnimationLightbox(event);"
         :ontouchstart "OCStaticHideAnimationLightbox(event);"}]]])

(def show-animation-button
  [:button.mlb-reset.show-animation-bt
    {:onClick "OCStaticShowAnimationLightbox();"}
    [:div.green-play]
    "Carrot in 60 seconds"])

(def carrot-in-action
  [:section.carrot-in-action
    [:div.carrot-in-action-title
      "Want to see Carrot in action?"]
    show-animation-button])

(def keep-aligned-bottom
  [:section.keep-aligned
    [:div.keep-aligned-title
      "Never miss what matters most"]
    [:div.keep-aligned-values-line.big-web-tablet-only
      [:img.keep-aligned-value.value-team-updates
        {:src (cdn "/img/ML/homepage_bottom_section_team_updates.png")
         :srcSet (str (cdn "/img/ML/homepage_bottom_section_team_updates@2x.png") " 2x")}]
      [:img.keep-aligned-value.value-news
        {:src (cdn "/img/ML/homepage_bottom_section_news.png")
         :srcSet (str (cdn "/img/ML/homepage_bottom_section_news@2x.png") " 2x")}]
      [:img.keep-aligned-value.value-announcements
        {:src (cdn "/img/ML/homepage_bottom_section_announcements.png")
         :srcSet (str (cdn "/img/ML/homepage_bottom_section_announcements@2x.png") " 2x")}]
      [:img.keep-aligned-value.value-decisions
        {:src (cdn "/img/ML/homepage_bottom_section_decisions.png")
         :srcSet (str (cdn "/img/ML/homepage_bottom_section_decisions@2x.png") " 2x")}]]
    [:button.mlb-reset.get-started-button.get-started-action
      "Create your team - It's free"]])

(defn slack-email-switch [slack?]
  [:div.slack-email-container.group.big-web-only
    [:div.slack-email-switch-container.group
      {:class (if slack? "show-slack" "show-email")}
      (when slack?
        [:button.mlb-reset.slack-email-switch-bt.slack-bt
          [:div.slack-logo]])
      [:button.mlb-reset.slack-email-switch-bt.email-bt
        [:div.email-logo]]
      (when-not slack?
        [:button.mlb-reset.slack-email-switch-bt.slack-bt
          [:div.slack-logo]])
      (when slack?
        [:div.slack-email-switch-bottom-panel.slack-panel
          [:div.slack-email-switch-bottom-panel-copy
            (str
             "“Our remote teams are ecstatic about not having to read "
             "hundreds of Slack messages each morning to make sense of "
             "the previous day.”")]
          [:div.slack-email-switch-bottom-panel-img]
          [:div.slack-email-switch-bottom-panel-name
            "Andrew Love"]
          [:div.slack-email-switch-bottom-panel-role
            "PartnerHero, Director of R&D"]])
      [:div.slack-email-switch-bottom-panel.email-panel
        [:div.slack-email-switch-bottom-panel-copy
          (str
           "“The morning digest is how we know everyone in our "
           "offices is up to date on what matters.”")]
        [:div.slack-email-switch-bottom-panel-img]
        [:div.slack-email-switch-bottom-panel-name
          "Chris Cairns"]
        [:div.slack-email-switch-bottom-panel-role
          "Skylight Digital, Managing Director"]]
      (when-not slack?
        [:div.slack-email-switch-bottom-panel.slack-panel
          [:div.slack-email-switch-bottom-panel-copy
            (str
             "“Our remote teams are ecstatic about not having to read "
             "hundreds of Slack messages each morning to make sense of "
             "the previous day.”")]
          [:div.slack-email-switch-bottom-panel-img]
          [:div.slack-email-switch-bottom-panel-name
            "Andrew Love"]
          [:div.slack-email-switch-bottom-panel-role
            "PartnerHero, Director of R&D"]])]])

(defn- slack-panel [first-panel?]
  [:div.keep-aligned-section-carion-block
    [:div.keep-aligned-section-header.tablet-mobile-only
      [:span.slack-logo]]
    [:img.keep-aligned-section-screenshot.screenshot-1
      {:class (if first-panel? "carion-1" "carion-1-alt")
       :src (cdn "/img/ML/homepage_screenshots_first_row_slack.png")
       :srcSet (str (cdn "/img/ML/homepage_screenshots_first_row_slack@2x.png") " 2x")}]
    [:div.slack-email-switch-bottom-panel.slack-panel
      [:div.slack-email-switch-bottom-panel-copy
        (str
         "“Our remote teams are ecstatic about not having to read "
         "hundreds of Slack messages each morning to make sense of "
         "the previous day.”")]
      [:div.slack-email-switch-bottom-panel-img]
      [:div.slack-email-switch-bottom-panel-name
        "Andrew Love"]
      [:div.slack-email-switch-bottom-panel-role
        "PartnerHero, Director of R&D"]]])

(defn- email-panel [first-panel?]
  [:div.keep-aligned-section-carion-block
    [:div.keep-aligned-section-header.tablet-mobile-only
      [:span.email-logo]]
    [:img.keep-aligned-section-screenshot.screenshot-1
      {:class (if first-panel? "carion-1" "carion-1-alt")
       :src (cdn "/img/ML/homepage_screenshots_first_row.png")
       :srcSet (str (cdn "/img/ML/homepage_screenshots_first_row@2x.png") " 2x")}]
    [:div.slack-email-switch-bottom-panel.email-panel
      [:div.slack-email-switch-bottom-panel-copy
        (str
         "“The morning digest is how we know everyone in our "
         "offices is up to date on what matters.”")]
      [:div.slack-email-switch-bottom-panel-img]
      [:div.slack-email-switch-bottom-panel-name
        "Chris Cairns"]
      [:div.slack-email-switch-bottom-panel-role
        "Skylight Digital, Managing Director"]]])

(defn keep-aligned-section [slack?]
  [:section.home-keep-aligned.group

    [:div.keep-aligned-section

      [:div.keep-aligned-section-row.first-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-left.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Share what matters"]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "Carrot gives you the space you need to "
               "update your team, and even reminds you "
               "when it's time to share the latest.")
              [:br]
              [:br]
              (str
               "Your team can view and comment on posts "
               "in the app, or directly from Slack and email. "
               "Rest assured, Carrot updates never get lost "
               "in the noise.")]
            [:div.keep-aligned-section-footer
              [:button.mlb-reset.create-team-bt.get-started-action
                "Create your team - it’s free"]]]
          [:div.keep-aligned-section-row-right
            [:img.keep-aligned-section-screenshot.screenshot-3.big-web-tablet-only
              {:src (cdn (str "/img/ML/homepage_screenshots_third_row" (if slack? "_slack" "") ".png"))
               :srcSet (str (cdn (str "/img/ML/homepage_screenshots_third_row" (if slack? "_slack" "") "@2x.png")) " 2x")}]
            [:img.keep-aligned-section-screenshot.screenshot-3.mobile-only
              {:src (cdn (str "/img/ML/homepage_screenshots_third_row" (if slack? "_slack" "") "_mobile.png"))
               :srcSet (str (cdn (str "/img/ML/homepage_screenshots_third_row" (if slack? "_slack" "") "_mobile@2x.png")) " 2x")}]]]]

      [:div.keep-aligned-section-row.second-row
        [:div.keep-aligned-section-row-inner.group
          [:div.keep-aligned-section-row-right.keep-aligned-section-copy
            [:div.keep-aligned-section-copy-title
              "Start every day in sync"]
            [:div.keep-aligned-section-copy-subtitle
              (str
               "Your team wakes up to a daily digest of what's "
               "important. This curated summary makes it easy "
               "to get caught up fast - perfect for remote teams.")]
            [:div.keep-aligned-section-footer
              [:button.mlb-reset.create-team-bt.get-started-action
                "Create your team - it’s free"]]
            (slack-email-switch slack?)]
          [:div.keep-aligned-section-row-left
            [:div.keep-aligned-section-carion-container
              [:div.keep-aligned-section-carion-inner
                (when slack?
                  (slack-panel true))
                (email-panel (not slack?))
                (when-not slack?
                  (slack-panel false))]
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
               "through.")]
            [:div.keep-aligned-section-footer
              [:button.mlb-reset.create-team-bt.get-started-action
                "Create your team - it’s free"]]]
          [:div.keep-aligned-section-row-right
            [:img.keep-aligned-section-screenshot.screenshot-2.big-web-tablet-only
              {:src (cdn "/img/ML/homepage_screenshots_second_row.png")
               :srcSet (str (cdn "/img/ML/homepage_screenshots_second_row@2x.png") " 2x")}]
            [:img.keep-aligned-section-screenshot.screenshot-2.mobile-only
              {:src (cdn "/img/ML/homepage_screenshots_second_row_mobile.png")
               :srcSet (str (cdn "/img/ML/homepage_screenshots_second_row_mobile@2x.png") " 2x")}]]]]]])

(def testimonials-logos-line
  [:div.homepage-testimonials-container.group
    [:div.homepage-testimonials-copy
      "Fast-growing and remote teams around the world use Carrot"]
    [:div.homepage-testimonials-logo.logo-novo]
    [:div.homepage-testimonials-logo.logo-ph]
    [:div.homepage-testimonials-logo.logo-wsu]
    [:div.homepage-testimonials-logo.logo-om]
    [:div.homepage-testimonials-logo.logo-mio]
    [:div.homepage-testimonials-logo.logo-sd]])

(defn index [options]
  [:div.home-wrap
    {:id "wrap"}
    [:div.main.home-page
      animation-lightbox
      ; Hope page header
      [:section.cta.group

        [:h1.headline
          "Where teams find clarity"]
        [:div.subheadline
          (str
           "Carrot is the platform for team communication that matters. "
           "Post important updates, news, decisions and stories "
           "that nobody should miss — perfect for remote teams.")]

        [:div.get-started-button-container.group
          [:button.mlb-reset.get-started-button.get-started-action
            {:id "get-started-centred-bt"}
            "Create your team - It's free"]]

        [:div.main-animation-container
          [:img.main-animation
            {:src (cdn "/img/ML/homepage_screenshot.png")
             :srcSet (str (cdn "/img/ML/homepage_screenshot@2x.png") " 2x")}]]

        testimonials-logos-line]

      (keep-aligned-section false)

      carrot-in-action

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
          "It’s free to get started"]

        [:div.pricing-subheadline
          "Carrot keeps fast-growing and remote teams focused on what matters."]

        [:div.pricing-three-columns.group
          ;; Free
          [:div.pricing-column.free-column
            [:h2.tear-title
              "Free"]
            [:h3.tear-price
              "$0"]
            [:div.tear-subtitle
              "Free for small teams up to 10 users"]
            [:a.tear-start-bt
              {:href "/sign-up"}
              "Start for free"]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "1gb storage"]]
            [:div.tear-feature-separator]
            [:div.tear-feature
              [:span "Access 3 months recent history"]]]
          ;; Team
          [:div.pricing-column.team-column.annual
            [:h2.tear-title
              "Standard"]
            [:div.pricing-toggle-line
              [:span.pricing-toggle-annual
                "Annual (20% off)"]
              [:div.pricing-toggle
                [:span.pricing-toggle-dot]]
              [:span.pricing-toggle-monthly
                "Monthly"]]
            [:h3.tear-price
              [:span.annual
                "$3.20"]
              [:span.monthly
                "$4"]]
            [:div.tear-subtitle
              [:span.billed-annually
                "Per user, per month, billed annually"]
              [:span.billed-monthly
                "Per user, per month"]]
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
              [:span "Custom plans"]]
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
             "Absolutely! Carrot is free for teams of up to 10 people. The Free plan "
             "has a storage limit, but includes all of the features of the Standard plan. "
             "It’s fast to sign up, and no credit card is required.")]]

        [:div.faq-row
          [:div.faq-row-question
            "How is the Standard plan different from the Free plan?"]
          [:div.faq-row-answer
            (str
             "The Standard plan includes unlimited storage and history. "
             "Choose the size of your team, and whether you’d like to pay "
             "monthly or annually. Annual plans paid in advance provide a 20% discount.")]]

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
            "Are custom plans available?"]
          [:div.faq-row-answer
            (str
             "Absolutely! Our Enterprise plan is 100% flexible and priced based on your "
             "unique needs. Please contact us to discuss further details.")]]

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
      animation-lightbox
      ; Hope page header
      [:section.carrot-plus-slack.group

        [:h1.slack-headline
          "Where teams find clarity"]
        
        [:div.slack-subline
          (str
            "Carrot is the platform for team communication that matters. "
            "Post important updates, news, decisions and stories "
            "that nobody should miss — perfect for remote teams.")]

        [:div.slack-button-container.group
            [:a.add-to-slack-button
              {:id "get-started-centred-bt"
               :href (env :slack-signup-url)}]]

        [:div.main-animation-container
          [:img.main-animation
            {:src (cdn "/img/ML/slack_screenshot.png")
             :srcSet (str (cdn "/img/ML/slack_screenshot@2x.png") " 2x")}]]

        testimonials-logos-line]

      (keep-aligned-section true)

      carrot-in-action

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

        [:a.continue-with-slack-bt
          {:href (env :slack-signup-url)}]

        [:div.main-animation-container
          [:img.main-animation
            {:src (cdn "/img/ML/slack_screenshot.png")
             :src-set (str (cdn "/img/ML/slack_screenshot@2x.png") " 2x")}]]

        testimonials-logos-line]]])

(defn press-kit
  "Press kit page."
  [options]
  [:div.press-kit-wrap
    {:id "wrap"}
    [:div.main.press-kit
      animation-lightbox
      [:section.cta.group

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

        ; [:h1.about
        ;   "Meet the team"]

        ; [:div.team-container
        ;   [:div.team-row.group.three-cards
        ;     [:div.team-card.iacopo-carraro
        ;       [:div.user-avatar]
        ;       [:div.user-name
        ;         "Iacopo Carraro"]
        ;       [:div.user-position
        ;         "Software Engineer"
        ;         [:br]
        ;         "in Livorno, Italy"]
        ;       [:div.user-links
        ;         [:a.twitter-link
        ;           {:href "https://twitter.com/bago2k4"
        ;            :target "_blank"}]
        ;         [:a.linkedin-link
        ;           {:href "https://linkedin.com/in/iacopocarraro/"
        ;            :target "_blank"}]]]
        ;     [:div.team-card.sean-johnson
        ;       [:div.user-avatar]
        ;       [:div.user-name
        ;         "Sean Johnson"]
        ;       [:div.user-position
        ;         "CTO & Founder"
        ;         [:br]
        ;         "in Chapel Hill, USA"]
        ;       [:div.user-links
        ;         [:a.twitter-link
        ;           {:href "https://twitter.com/belucid"
        ;            :target "_blank"}]
        ;         [:a.linkedin-link
        ;           {:href "https://linkedin.com/in/snootymonkey/"
        ;            :target "_blank"}]]]
        ;     [:div.team-card.georgiana-laudi
        ;       [:div.user-avatar]
        ;       [:div.user-name
        ;         "Georgiana Laudi"]
        ;       [:div.user-position
        ;         "Marketing & CX Advisor"
        ;         [:br]
        ;         "in Montreal, Canada"]
        ;       [:div.user-links
        ;         [:a.twitter-link
        ;           {:href "https://twitter.com/ggiiaa"
        ;            :target "_blank"}]
        ;         [:a.linkedin-link
        ;           {:href "https://linkedin.com/in/georgianalaudi/"
        ;            :target "_blank"}]]]
        ;     [:div.team-card.stuart-levinson
        ;       [:div.user-avatar]
        ;       [:div.user-name
        ;         "Stuart Levinson"]
        ;       [:div.user-position
        ;         "CEO & Founder"
        ;         [:br]
        ;         "in Cambridge, USA"]
        ;       [:div.user-links
        ;         [:a.twitter-link
        ;           {:href "https://twitter.com/stuartlevinson"
        ;            :target "_blank"}]
        ;         [:a.linkedin-link
        ;           {:href "https://linkedin.com/in/stuartlevinson/"
        ;            :target "_blank"}]]]
        ;     [:div.team-card.ryan-le-roux
        ;       [:div.user-avatar]
        ;       [:div.user-name
        ;         "Ryan Le Roux"]
        ;       [:div.user-position
        ;         "Head of Design"
        ;         [:br]
        ;         "in Vancouver, Canada"]
        ;       [:div.user-links
        ;         [:a.twitter-link
        ;           {:href "https://twitter.com/ryanleroux"
        ;            :target "_blank"}]
        ;         [:a.linkedin-link
        ;           {:href "https://linkedin.com/in/ryanleroux/"
        ;            :target "_blank"}]]]
        ;     [:div.team-card.nathan-zorn
        ;       [:div.user-avatar]
        ;       [:div.user-name
        ;         "Nathan Zorn"]
        ;       [:div.user-position
        ;         "Software Engineer"
        ;         [:br]
        ;         "in Charleston, USA"]
        ;       [:div.user-links
        ;         [:a.twitter-link
        ;           {:href "https://twitter.com/thepug"
        ;            :target "_blank"}]
        ;         [:a.linkedin-link
        ;           {:href "https://linkedin.com/in/nathanzorn/"
        ;            :target "_blank"}]]]]]

        [:h3.about-copy-header
          "Why we built Carrot"]

        [:div.about-copy
          [:div.about-copy-inner
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
               "Sharing important team updates, news, and decisions alongside random chats "
               "just increases the likelihood it will scroll by without being noticed. "
               "It’s difficult for the team to know what matters, and leaders have no idea "
               "if anyone even heard what they said.")]
            [:p
              (str
               "Heavy Slack users ourselves, we wanted to design a Slack-friendly approach "
               "to handle must-see communication. We wanted this “non-chat” communication "
               "to be as fun, delightful and interactive as chat; but we also wanted it "
               "to be asynchronous so people could get caught up on their own time.")]
            [:p
              (str
               "The result is Carrot - a platform for must-see communication that keeps "
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
                  "Build with us on git"]
                [:span.link-or "or"]
                [:a
                  {:href "https://trello.com/b/eKs2LtLu"
                   :target "_blank"}
                  "VISIT OUR ROADMAP"]]]]]]

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