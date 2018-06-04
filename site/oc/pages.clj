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

(def mobile-horizontal-carousell
  [:div.horizontal-carousell
    [:div.horizontal-carousell-inner
      [:img.horizontal-carousell-1
        {:src (cdn "/img/ML/homepage_mobile_screenshot_1.png")
         :src-set (str (cdn "/img/ML/homepage_mobile_screenshot_1@2x.png") " 2x")}]
      [:img.horizontal-carousell-2
        {:src (cdn "/img/ML/homepage_mobile_screenshot_2.png")
         :src-set (str (cdn "/img/ML/homepage_mobile_screenshot_2@2x.png") " 2x")}]
      [:img.horizontal-carousell-3
        {:src (cdn "/img/ML/homepage_mobile_screenshot_3.png")
         :src-set (str (cdn "/img/ML/homepage_mobile_screenshot_3@2x.png") " 2x")}]]])

(def desktop-video
  [:div.main-animation-container
    [:video.main-animation
      {:controls false
       :autoPlay true
       :poster (cdn "/img/ML/new_homepage_screenshot.png")
       :onClick "this.play();"
       :loop true}
      [:source
        {:src (cdn "/img/ML/animation.webm")
         :type "video/webm"}]
      [:source
        {:src (cdn "/img/ML/animation.mp4")
         :type "video/mp4"}]
      [:div.fallback
        "Your browser doesn´t support this video format."]]])

(def carrot-cards
  [:div.cards-container
    [:div.cards-row.group
      [:div.card.card-1
        [:div.card-icon]
        [:div.card-title
          "Say what’s important "]
        [:div.card-content
          (str
            "Space to write more "
            "than a quick chat - like "
            "key updates, announcements, "
            "plans, and stories.")]]
      [:div.card.card-2
        [:div.card-icon]
        [:div.card-title
          "Read without interruptions"]
        [:div.card-content
          (str
            "Stay in sync without worrying "
            "you missed something important "
            "in a fast-moving conversation.")]]
      [:div.card.card-3
        [:div.card-icon]
        [:div.card-title
          "Focused, topic-based discussion"]
        [:div.card-content
          (str
            "Keep team reactions, comments "
            "and questions together in one "
            "place for better context.")]]]
    [:div.cards-row.group
      [:div.card.card-4
        [:div.card-icon]
        [:div.card-title
          "Find anything fast"]
        [:div.card-content
          (str
            "Get up to speed quickly with the "
            "full picture in one place. Great "
            "for distributed teams, too.")]]
      [:div.card.card-5
        [:div.card-icon]
        [:div.card-title
          "See who’s engaged"]
        [:div.card-content
          (str
            "Carrot shows you who's reading "
            "what so leaders can see if "
            "their teams are aligned.")]]
      [:div.card.card-6
        [:div.card-icon]
        [:div.card-title
          (str
            "In sync "
            "with Slack")]
        [:div.card-content
          (str
            "Discussions are automatically "
            "shared to the right channels. ")
          [:a
            {:href "/slack"}
            "Learn more"]]]]])

(def testimonials-section
  [:section.testimonials-section
    [:div.balloon.big-yellow-1]
    [:div.balloon.small-blue-1]
    [:div.testimonials-section-title
      "Don’t take our word for it"]
    [:div.testimonials-section-subtitle
      "Here’s how we’re helping teams like yours."]
    [:div.testimonials-cards-container.group
      [:div.card
        [:div.card-content
          (str
           "We love Slack for spontaneous "
           "stuff, but when it’s time to "
           "post key updates that can’t "
           "be missed, Carrot is awesome.")]
        [:div.card-author.group
          [:img.card-avatar
            {:src (cdn "/img/ML/happy_face_blue.svg")}]
          [:div.author-name
            ""]
          [:div.author-company
            ""]]]
      [:div.card
        [:div.card-content
          (str
           "Carrot makes sure our crucial "
           "updates aren’t drowned out "
           "by \"taco Tuesday?\" and "
           "silly memes!")]
        [:div.card-author.group
          [:img.card-avatar
            {:src (cdn "/img/ML/happy_face_purple.svg")}]
          [:div.author-name
            ""]
          [:div.author-company
            ""]]]
      [:div.card
        [:div.card-content
          (str
           "RIP team email! 👻  With "
           "Carrot, email is finally "
           "obsolete for team updates "
           "and the rest.")]
        [:div.card-author.group
          [:img.card-avatar
            {:src (cdn "/img/ML/happy_face_red.svg")}]
          [:div.author-name
            ""]
          [:div.author-company
            ""]]]
      [:div.card
        [:div.card-content
          (str
           "Before Carrot, I never knew "
           "if anyone saw my updates! "
           "Now I know who's engaged "
           "and aligned.  🙏")]
        [:div.card-author.group
          [:img.card-avatar
            {:src (cdn "/img/ML/happy_face_yellow.svg")}]
          [:div.author-name
            ""]
          [:div.author-company
            ""]]]]])

(def keep-aligned
  [:section.keep-aligned
    [:div.keep-aligned-title
      "Keep everyone aligned around what matters most."]
    [:button.mlb-reset.get-started-button
      "Get started for free"]])

(defn index [options]
  [:div
    {:id "wrap"}
    [:div.main.home-page
      ; Hope page header
      [:section.cta.group
        [:div.balloon.big-yellow]
        [:div.balloon.big-red]
        [:div.balloon.big-purple]
        [:div.balloon.big-green]
        [:div.balloon.small-blue]
        [:div.balloon.small-yellow-face]
        [:div.balloon.small-purple]
        [:div.balloon.small-red]
        [:div.balloon.small-purple-2]
        [:div.balloon.big-green-2]
        [:div.balloon.small-yellow]

        [:h1.headline
          "Better informed, less noise."]
        [:div.subheadline
          (str
            "Your company digest keeps everyone aligned "
            "around what matters most.")]
        ; (try-it-form "try-it-form-central" "try-it-combo-field-top")
        [:div.get-started-button-container
          [:button.mlb-reset.get-started-button
            {:id "get-started-centred-bt"}
            "Get started for free"]]
        (carrot-box-thanks "carrot-box-thanks-top")
        [:div.carrot-box-container.confirm-thanks.group
          {:style {:display "none"}}
          [:div.carrot-box-thanks
            [:div.thanks-headline "You are Confirmed!"]
            [:div.thanks-subheadline "Thank you for subscribing."]]]

        mobile-horizontal-carousell

        desktop-video

        [:div.stay-aligned-container
          [:div.stay-aligned-icon]
          [:div.stay-aligned-message
            "Rise above the noise."]]

        carrot-cards]

      testimonials-section

      keep-aligned
      ]])

(defn pricing
  "Pricing page. This is a copy of oc.web.components.pricing and every change here should be reflected there and vice versa."
  [options]
  [:div
    {:id "wrap"}
    [:div.main.pricing
      [:section.pricing-header
        ;; Top left
        [:div.balloon.big-red]
        [:div.balloon.big-green]
        [:div.balloon.small-blue]
        [:div.balloon.big-purple]

        [:h1.pricing-headline
          "Pricing guide"]

        [:div.pricing-block.group
          [:div.pricing-block-column.free-column
            [:div.price-column-title
              "Free"]
            [:div.price-column-price
              "0"]
            [:div.price-column-description
              "Free for small teams for an unlimited period of time"]
            [:button.mlb-reset.price-button
              {:onClick "CarrotGA.trackEvent({eventCategory: 'purchase-click', eventAction: 'click', eventLabel: 'Free'});"}
              "Create a digest"]]

          [:div.pricing-block-column.standard-column
            [:div.price-column-title
              "Standard"]
            [:div.price-column-price
              "8"]
            [:div.price-column-description
              "Per contributor per month billed annually"]
            [:div.price-column-description.second-line
              "Or $10 billed monthly"]
            [:div.price-column-description.more-info
              "Viewers are always free"]
            [:button.mlb-reset.price-button
              {:onClick "CarrotGA.trackEvent({eventCategory: 'purchase-click', eventAction: 'click', eventLabel: 'Standard'});"}
              "Buy standard"]]

          [:div.pricing-block-column.plus-column
            [:div.price-column-title
              "Plus"]
            [:div.price-column-price
              "12"]
            [:div.price-column-description
              "Per contributor per month billed annually"]
            [:div.price-column-description.second-line
              "Or $14 billed monthly"]
            [:div.price-column-description.more-info
              "Viewers are always free"]
            [:button.mlb-reset.price-button
              {:onClick "CarrotGA.trackEvent({eventCategory: 'purchase-click', eventAction: 'click', eventLabel: 'Plus'});"}
              "Buy plus"]]]
        [:div.enterprise-block
          [:span.enterprise-block-title
            "Enterprise Edition"]
          [:span.enterprise-block-copy
            "Let’s create a plan that’s right for your organization."]
          [:a.enterprise-block-link
            {:href (:contact-mail-to options)}
            "Contact us"]]]
      [:section.second-section
        [:h1.compare-plans
          "Compare Plans"]

        [:div.pricing-table
          [:table
            {:cellpadding "0"
             :cellspacing "0"}
            [:thead
              [:tr
                [:th]
                [:th
                  [:div
                    "Free"]]
                [:th
                  [:div
                    "Standard"]]
                [:th
                  [:div
                    "Plus"]]]]
            [:tbody
              [:tr
                [:td
                  [:div.more-info
                    "Searchable posts"
                    [:span.more-info-icon]
                    [:div.more-info-bubble
                      [:div.more-info-title
                        "Searchable posts"]
                      [:div.more-info-desc
                        (str
                          "Lorem ipsum dolor sit amet, consectetur adipiscing "
                          "elit. Vestibulum nisi augue, pharetra nec tempus ac, "
                          "rhoncus eu felis. Sed tempus massa a ipsum commodo, sed condimentum.")]]]]
                [:td
                  [:div "100"]]
                [:td
                  [:div "Unlimited"]]
                [:td
                  [:div "Unlimited"]]]
              [:tr
                [:td
                  [:div.more-info
                    "History kept"
                    [:span.more-info-icon]
                    [:div.more-info-bubble
                      [:div.more-info-title
                        "History kept"]
                      [:div.more-info-desc
                        (str
                          "Lorem ipsum dolor sit amet, consectetur adipiscing "
                          "elit. Vestibulum nisi augue, pharetra nec tempus ac, "
                          "rhoncus eu felis. Sed tempus massa a ipsum commodo, sed condimentum.")]]]]
                [:td
                  [:div "Last 12 months"]]
                [:td
                  [:div "Unlimited"]]
                [:td
                  [:div "Unlimited"]]]
              [:tr
                [:td
                  [:div "File storage"]]
                [:td
                  [:div "1 GB"]]
                [:td
                  [:div "10 GB"]]
                [:td
                  [:div "50 GB"]]]
              [:tr
                [:td
                  [:div "File upload"]]
                [:td
                  [:div "25 MB"]]
                [:td
                  [:div "50 MB"]]
                [:td
                  [:div "100 MB"]]]
              [:tr
                [:td
                  [:div "Slack single sign-on"]]
                [:td
                  [:div.check]]
                [:td
                  [:div.check]]
                [:td
                  [:div.check]]]
              [:tr
                [:td
                  [:div.more-info
                    "Slack sync"
                    [:span.more-info-icon]
                    [:div.more-info-bubble
                      [:div.more-info-title
                        "Slack sync"]
                      [:div.more-info-desc
                        (str
                          "Lorem ipsum dolor sit amet, consectetur adipiscing "
                          "elit. Vestibulum nisi augue, pharetra nec tempus ac, "
                          "rhoncus eu felis. Sed tempus massa a ipsum commodo, sed condimentum.")]]]]
                [:td
                  [:div.check]]
                [:td
                  [:div.check]]
                [:td
                  [:div.check]]]
              [:tr
                [:td
                  [:div.more-info
                    "Dropbox, Google Drive and other integrations"
                    [:span.more-info-icon]
                    [:div.more-info-bubble
                      [:div.more-info-title
                        "Dropbox, Google Drive and other integrations"]
                      [:div.more-info-desc
                        (str
                          "Lorem ipsum dolor sit amet, consectetur adipiscing "
                          "elit. Vestibulum nisi augue, pharetra nec tempus ac, "
                          "rhoncus eu felis. Sed tempus massa a ipsum commodo, sed condimentum.")]]]]
                [:td]
                [:td
                  [:div.check]]
                [:td
                  [:div.check]]]
              [:tr
                [:td
                  [:div.more-info
                    "OAuth with Google"
                    [:span.more-info-icon]
                    [:div.more-info-bubble
                      [:div.more-info-title
                        "OAuth with Google"]
                      [:div.more-info-desc
                        (str
                          "Lorem ipsum dolor sit amet, consectetur adipiscing "
                          "elit. Vestibulum nisi augue, pharetra nec tempus ac, "
                          "rhoncus eu felis. Sed tempus massa a ipsum commodo, sed condimentum.")]]]]
                [:td]
                [:td
                  [:div.check]]
                [:td
                  [:div.check]]]
              [:tr
                [:td
                  [:div.more-info
                    "Private and public visibility"
                    [:span.more-info-icon]
                    [:div.more-info-bubble
                      [:div.more-info-title
                        "Private and public visibility"]
                      [:div.more-info-desc
                        (str
                          "Lorem ipsum dolor sit amet, consectetur adipiscing "
                          "elit. Vestibulum nisi augue, pharetra nec tempus ac, "
                          "rhoncus eu felis. Sed tempus massa a ipsum commodo, sed condimentum.")]]]]
                [:td]
                [:td
                  [:div.check]]
                [:td
                  [:div.check]]]
              [:tr
                [:td
                  [:div.more-info
                    "Who read what"
                    [:span.more-info-icon]
                    [:div.more-info-bubble
                      [:div.more-info-title
                        "Who read what"]
                      [:div.more-info-desc
                        (str
                          "Lorem ipsum dolor sit amet, consectetur adipiscing "
                          "elit. Vestibulum nisi augue, pharetra nec tempus ac, "
                          "rhoncus eu felis. Sed tempus massa a ipsum commodo, sed condimentum.")]]]]
                [:td]
                [:td
                  [:div.check]]
                [:td
                  [:div.check]]]
              [:tr
                [:td
                  [:div "Analytics"]]
                [:td]
                [:td]
                [:td
                  [:div.check]]]
              [:tr
                [:td
                  [:div "Priority support"]]
                [:td]
                [:td]
                [:td
                  [:div.check]]]
              [:tr
                [:td
                  [:div "Uptime SLA"]]
                [:td]
                [:td]
                [:td
                  [:div.check]]]]]]]

      testimonials-section

      keep-aligned
    ]])

(defn slack
  "Slack page. This is a copy of oc.web.components.slack and
   every change here should be reflected there and vice versa."
  [options]
  [:div
    {:id "wrap"}
    [:div.main.slack.group
      [:section.carrot-plus-slack.group
        ;; Top Left
        [:div.balloon.big-green]
        [:div.balloon.small-purple]
        [:div.balloon.small-yellow]
        ;; Top Right
        [:div.balloon.big-red]
        [:div.balloon.small-yellow-face]
        [:div.balloon.small-purple-1]
        ;; Center Left
        [:div.balloon.big-blue]
        [:div.balloon.small-purple-2]
        [:div.balloon.small-green]
        [:div.balloon.big-purple]
        [:div.balloon.small-red]


        [:div.carrot-plus-slack]

        [:h1.slack
          "Rise above the noise"]

        [:div.slack-subline
          (str
           "Slack keeps your team connected in the moment. "
           "Carrot keeps it aligned over time.")]

        [:div.sigin-with-slack-container
          [:button.signin-with-slack.mlb-reset
            {:onClick "javascript:window.location=\"/sign-up\";"}
            "Sign up with"
            [:div.slack-white-icon]]
          [:div.signin-with-slack-disclaimer
            [:div.signin-with-slack-description
              "By signing in, you agree to the "
              [:a
                {:href "/terms"}
                "Terms of Use"]
              " and "
              [:a
                {:href "/privacy"}
                "Privacy Policy."]]]]

        mobile-horizontal-carousell

        desktop-video        

        [:div.designed-for-container
          [:div.designed-for
            "Designed for Slack"]
          [:div.designed-for-content
            (str
             "Slack is fun and awesome for real-time work, "
             "but gets noisy. With Carrot, what matters most "
             "rises above the noise to keep everyone "
             "on the same page.")]]

        carrot-cards]

      testimonials-section

      [:section.keep-aligned
        [:div.keep-aligned-title
          "Keep everyone aligned around what matters most."]
        [:div.sigin-with-slack-container
          [:button.signin-with-slack.mlb-reset
            {:onClick "javascript:window.location=\"/sign-up\";"}
            "Sign up with"
            [:div.slack-white-icon]]]]

      ] ;<!-- main -->
  ])

(defn about
  "About page. This is a copy of oc.web.components.about and
   every change here should be reflected there and vice versa."
  [options]
  [:div
    {:id "wrap"}
    [:div.main.about
      [:section.about-header
        [:div.balloon.big-red]
        [:div.balloon.big-green]
        [:div.balloon.small-green-face]
        [:div.balloon.small-blue]
        [:div.balloon.small-purple]
        [:div.balloon.big-purple]
        [:div.balloon.small-yellow]
        [:div.balloon.big-yellow]
        [:div.balloon.small-purple-1]
        [:div.balloon.small-purple-2]
        [:div.balloon.small-blue-1]

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
                {:href "https://www.linkedin.com/in/iacopocarraro/"
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
            [:div.team-card.stuart-levinson
              [:div.user-avatar]
              [:div.user-name
                "Stuart Levinson"]
              [:div.user-position
                "CEO and co-founder"]
              [:a.linkedin-link
                {:href "https://linkedin.com/in/stuartlevinson/"
                 :target "_blank"}]]]
          [:div.team-row.group.two-cards
            [:div.team-card.ryan-le-roux
              [:div.user-avatar]
              [:div.user-name
                "Ryan Le Roux"]
              [:div.user-position
                "CDO"]
              [:a.linkedin-link
                {:href "https://www.linkedin.com/in/ryanleroux/"
                 :target "_blank"}]]
            [:div.team-card.nathan-zorn
              [:div.user-avatar]
              [:div.user-name
                "Nathan Zorn"]
              [:div.user-position
                "Software Engineer"]
              [:a.linkedin-link
                {:href "https://www.linkedin.com/in/nathanzorn/"
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
          [:title "Carrot | Company digest"]
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
          ;; Google fonts Muli
          [:link {:href "https://fonts.googleapis.com/css?family=Muli" :rel "stylesheet"}]
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
          [:script {:src "https://cdn.polyfill.io/v2/polyfill.js"}]]
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
          [:title "Carrot | Company digest"]
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
          ;; Google fonts Muli
          [:link {:href "https://fonts.googleapis.com/css?family=Muli" :rel "stylesheet"}]
          ;; CarrotKit Font
          [:link {:type "text/css" :rel "stylesheet" :href (cdn "/css/fonts/CarrotKit.css")}]
          ;; jQuery needed by Bootstrap JavaScript
          jquery
          ;; Automatically load the needed polyfill depending on
          ;; the browser user agent and the available features
          [:script {:src "https://cdn.polyfill.io/v2/polyfill.min.js"}]]
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