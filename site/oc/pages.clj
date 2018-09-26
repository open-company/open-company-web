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
      "Don’t take our word for it"]
    [:div.testimonials-section-subtitle
      "Here’s how we’re helping teams like yours."]
    [:div.testimonials-cards-container.group
      [:div.testimonial-card
        [:div.testimonial-image]
        [:div.testimonial-name
          "CHRIS CAIRNS"]
        [:div.testimonial-role
          "Managing Partner"]
        [:div.testimonial-quote
          (str
           "As a busy leader it's hard to keep everyone up to date. "
           "I use Carrot to record a quick video update and it "
           "lets me know that everyone's seen it.")]
        [:div.testimonial-footer.group
          [:a
            {:href "https://skylight.digital/"
             :target "_blank"}
            [:div.testimonial-logo]]]]
      [:div.testimonial-card
        [:div.testimonial-image]
        [:div.testimonial-name
          "Tom Hadfield"]
        [:div.testimonial-role
          "CEO"]
        [:div.testimonial-quote
          "On Carrot, my updates get noticed and get the team talking. I love that."]
        [:div.testimonial-footer.group
          [:a
            {:href "https://m.io/"
             :target "_blank"}
            [:div.testimonial-logo]]]]
      [:div.testimonial-card
        [:div.testimonial-image]
        [:div.testimonial-name
          "Nick DeNardis"]
        [:div.testimonial-role
          "Director of Digital Communications"]
        [:div.testimonial-quote
          (str
           "Carrot helps me share things the entire team needs to know "
           "about - instead of burying it somewhere it won’t get noticed.")]
        [:div.testimonial-footer.group
          [:a
            {:href "https://wayne.edu/"
             :target "_blank"}
            [:div.testimonial-logo]]]]]])

(def no-credit-card
  [:div.no-credit-card
    "No credit card required&nbsp;&nbsp;•&nbsp;&nbsp;Works with Slack"])

(def keep-aligned-bottom
  [:section.keep-aligned
    [:div.keep-aligned-title
      "It’s never been easier to keep everyone on the same page"]
    [:button.mlb-reset.get-started-button
      "Get started for free"]
    no-credit-card])

(def keep-aligned-section
  [:section.home-keep-aligned
    [:h2.keep-aligned-title
      "Carrot keeps leaders and teams aligned"]

    [:div.keep-aligned-section
      [:div.keep-aligned-section-row.first-row.group
        [:img.keep-aligned-section-screenshot.screenshot-1.big-web-only
          {:src (cdn "/img/ML/homepage_screenshots_update_team.png")
           :srcSet (str (cdn "/img/ML/homepage_screenshots_update_team@2x.png") " 2x")}]
        [:img.keep-aligned-section-screenshot.screenshot-1.mobile-only
          {:src (cdn "/img/ML/homepage_screenshots_update_team_mobile.png")
           :srcSet (str (cdn "/img/ML/homepage_screenshots_update_team_mobile@2x.png") " 2x")}]
        [:div.keep-aligned-section-copy
          [:div.keep-aligned-section-copy-title
            "Update your team in seconds"]
          [:div.keep-aligned-section-list-item.purple-checkmark
            "Create compelling updates people want to read"]
          [:div.keep-aligned-section-list-item.purple-checkmark
            "Capture video updates to add a human touch"]
          [:div.keep-aligned-section-list-item.purple-checkmark
            "Link to Google, Dropbox, wikis and others"]
          [:div.keep-aligned-section-list-item.purple-checkmark
          "Automate recurring team updates"]]]

      [:div.keep-aligned-section-row.second-row.group
        [:img.keep-aligned-section-screenshot.screenshot-2.big-web-only
          {:src (cdn "/img/ML/homepage_screenshots_keep_informed.png")
           :srcSet (str (cdn "/img/ML/homepage_screenshots_keep_informed@2x.png") " 2x")}]
        [:img.keep-aligned-section-screenshot.screenshot-2.mobile-only
          {:src (cdn "/img/ML/homepage_screenshots_keep_informed_mobile.png")
           :srcSet (str (cdn "/img/ML/homepage_screenshots_keep_informed_mobile@2x.png") " 2x")}]
        [:div.keep-aligned-section-copy
          [:div.keep-aligned-section-copy-title
            "Keep everyone informed"]
          [:div.keep-aligned-section-list-item.green-checkmark
            "“Must see” updates rise to the top"]
          [:div.keep-aligned-section-list-item.green-checkmark
            "See what’s trending to stay in the loop"]
          [:div.keep-aligned-section-list-item.green-checkmark
            "Organized for easy browsing and search"]
          [:div.keep-aligned-section-list-item.green-checkmark
            "A morning digest summarizes what’s new"]]]

      [:div.keep-aligned-section-row.third-row.group
        [:img.keep-aligned-section-screenshot.screenshot-3.big-web-only
          {:src (cdn "/img/ML/homepage_screenshots_better_discussions.png")
           :srcSet (str (cdn "/img/ML/homepage_screenshots_better_discussions@2x.png") " 2x")}]
        [:img.keep-aligned-section-screenshot.screenshot-3.mobile-only
          {:src (cdn "/img/ML/homepage_screenshots_better_discussions_mobile.png")
           :srcSet (str (cdn "/img/ML/homepage_screenshots_better_discussions_mobile@2x.png") " 2x")}]
        [:div.keep-aligned-section-copy
          [:div.keep-aligned-section-copy-title
            "Spark better follow-on discussions"]
          [:div.keep-aligned-section-list-item.red-checkmark
            "Encourage thoughtful comments and questions"]
          [:div.keep-aligned-section-list-item.red-checkmark
            "Keep interactions together for greater context"]
          [:div.keep-aligned-section-list-item.red-checkmark
            "Syncs with Slack so discussions can happen anywhere"]]]

      [:div.keep-aligned-section-row.fourth-row.group
        [:img.keep-aligned-section-screenshot.screenshot-4.big-web-only
          {:src (cdn "/img/ML/homepage_screenshots_being_heard.png")
           :srcSet (str (cdn "/img/ML/homepage_screenshots_being_heard@2x.png") " 2x")}]
        [:img.keep-aligned-section-screenshot.screenshot-4.mobile-only
          {:src (cdn "/img/ML/homepage_screenshots_being_heard_mobile.png")
           :srcSet (str (cdn "/img/ML/homepage_screenshots_being_heard_mobile@2x.png") " 2x")}]
        [:div.keep-aligned-section-copy
          [:div.keep-aligned-section-copy-title
            "Make sure you're being heard"]
          [:div.keep-aligned-section-list-item.blue-checkmark
            "Know who's seen your post"]
          [:div.keep-aligned-section-list-item.blue-checkmark
            "Send reminders with a single click"]
          [:div.keep-aligned-section-list-item.blue-checkmark
            "Eliminate communication gaps"]
          [:div.keep-aligned-section-list-item.blue-checkmark
            "Ensure alignment on important items"]]]]])

(def access-anywhere-section
  [:section.access-anywhere-section
    [:div.access-anywhere-section-container
      [:div.access-anywhere-copy
        [:div.access-anywhere-copy-title
          "Stay up to date on the go."]
        [:div.access-anywhere-copy-title.big-web-only
          "Access Carrot from anywhere."]
        [:div.access-anywhere-copy-title.mobile-only
          "Access Carrot anywhere."]
        [:div.access-anywhere-copy-subtitle.big-web-only
          "Fully responsive web app. No app install required."]
        [:div.access-anywhere-copy-subtitle.mobile-only
          "Fully responsive web app."]
        [:div.access-anywhere-copy-subtitle.mobile-only
          "No app install required."]]
      [:div.access-anywhere-screenshot]]])

(defn slack-comparison-section
  [& [slack-version?]]
  [:section.slack-comparison
    [:div.slack-comparison-headline
      "PERFECT FOR SLACK TEAMS"]
    (when-not slack-version?
      [:div.slack-comparison-headline-1
        "Slack keeps your team connected in the moment."])
    (if slack-version? 
      [:div.slack-comparison-headline-2
        "Keep your team informed without distractions."]
      [:div.slack-comparison-headline-2
        "Carrot keeps it aligned over time."])
    [:img.slack-comparison-screenshot.big-web-only
      {:src (cdn "/img/ML/slack_comparison_screenshot.png")
       :srcSet (str (cdn "/img/ML/slack_comparison_screenshot@2x.png") " 2x")}]
    [:img.slack-comparison-screenshot.mobile-only
      {:src (cdn "/img/ML/slack_comparison_screenshot_mobile.png")
       :srcSet (str (cdn "/img/ML/slack_comparison_screenshot_mobile@2x.png") " 2x")}]])

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
          "Rise above the noise of chat and email"
          [:br]
          "to keep your distributed team aligned."]
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

      access-anywhere-section

      (slack-comparison-section)

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
          "Pricing guide"]

        [:table.pricing-table.big-web-only
          [:thead
            [:tr
              [:th]
              [:th
                [:div.tire-title
                  "Free"]
                [:div.tire-price
                  "$0"]
                [:button.mlb-reset.price-button
                  "Get started"]]
              [:th
                [:div.tire-title
                  "Team"]
                [:div.tire-price
                  "$45"
                  [:span.per-month
                    "per month"]]
                [:button.mlb-reset.price-button
                  "Try for free"]]
              [:th
                [:div.tire-title
                  "Enterprise"]
                [:div.tire-price
                  "$125"
                  [:span.per-month
                    "per month"]]
                [:button.mlb-reset.price-button
                  "Try for free"]]]]
          [:tbody
            [:tr
              [:td.pricing-description
                "Number of users included"]
              [:td.pricing-value
                "10"]
              [:td.pricing-value
                "25"]
              [:td.pricing-value
                "50"]]

            [:tr
              [:td.pricing-description
                "Additional users"]
              [:td.pricing-value
                "—"]
              [:td.pricing-value
                "—"]
              [:td.pricing-value
                "$2 per user"]]

            [:tr
              [:td.pricing-description
                "Number of new posts"
                [:span.info
                  {:data-toggle "tooltip"
                   :data-placement "top"
                   :data-container "body"
                   :title "Number of new posts"
                   :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]
              [:td.pricing-value
                "50"]
              [:td.pricing-value
                "Unlimited"]
              [:td.pricing-value
                "Unlimited"]]

            [:tr
              [:td.pricing-description
                "History retained"
                [:span.info
                  {:data-toggle "tooltip"
                   :data-placement "top"
                   :data-container "body"
                   :title "History retained"
                   :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]
              [:td.pricing-value
                "6 months"]
              [:td.pricing-value
                "Unlimited"]
              [:td.pricing-value
                "Unlimited"]]

            [:tr
              [:td.pricing-description
                "File upload"]
              [:td.pricing-value
                "25 MB"]
              [:td.pricing-value
                "Unlimited"]
              [:td.pricing-value
                "Unlimited"]]

            [:tr
              [:td.pricing-description
                "File storage"]
              [:td.pricing-value
                "500 MB"]
              [:td.pricing-value
                "5 TB"]
              [:td.pricing-value
                "Unlimited"]]

            [:tr
              [:td.pricing-description
                "G suite single sign-on"]
              [:td.pricing-value
                [:div.price-checkmark]]
              [:td.pricing-value
                [:div.price-checkmark]]
              [:td.pricing-value
                [:div.price-checkmark]]]

            [:tr
              [:td.pricing-description
                "Slack single sign-on"]
              [:td.pricing-value
                [:div.price-checkmark]]
              [:td.pricing-value
                [:div.price-checkmark]]
              [:td.pricing-value
                [:div.price-checkmark]]]

            [:tr
              [:td.pricing-description
                "Sync with Slack"
                [:span.info
                  {:data-toggle "tooltip"
                   :data-placement "top"
                   :data-container "body"
                   :title "Sync with Slack"
                   :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]
              [:td.pricing-value
                [:div.price-checkmark]]
              [:td.pricing-value
                [:div.price-checkmark]]
              [:td.pricing-value
                [:div.price-checkmark]]]

            [:tr
              [:td.pricing-description
                "Dropbox, Google Drive and other integrations"]
              [:td.pricing-value
                [:div.price-checkmark]]
              [:td.pricing-value
                [:div.price-checkmark]]
              [:td.pricing-value
                [:div.price-checkmark]]]

            [:tr
              [:td.pricing-description
                "Advanced permissions"
                [:span.info
                  {:data-toggle "tooltip"
                   :data-placement "top"
                   :data-container "body"
                   :title "Advanced permissions"
                   :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]
              [:td.pricing-value]
              [:td.pricing-value
                [:div.price-checkmark]]
              [:td.pricing-value
                [:div.price-checkmark]]]

            [:tr
              [:td.pricing-description
                "Priority support"
                [:span.info
                  {:data-toggle "tooltip"
                   :data-placement "top"
                   :data-container "body"
                   :title "Priority support"
                   :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]
              [:td.pricing-value]
              [:td.pricing-value
                [:div.price-checkmark]]
              [:td.pricing-value
                [:div.price-checkmark]]]

            [:tr
              [:td.pricing-description
                "Analytics"]
              [:td.pricing-value]
              [:td.pricing-value]
              [:td.pricing-value
                [:div.price-checkmark]]]

            [:tr
              [:td.pricing-description
                "Uptime SLA"]
              [:td.pricing-value]
              [:td.pricing-value]
              [:td.pricing-value
                [:div.price-checkmark]]]

            [:tr
              [:td.pricing-description
                "On premise"]
              [:td.pricing-value]
              [:td.pricing-value]
              [:td.pricing-value
                [:div.price-checkmark]]]]
          [:thead
            [:tr
              [:th]
              [:th
                [:div.tire-title
                  "Free"]
                [:button.mlb-reset.price-button
                  "Get started"]]
              [:th
                [:div.tire-title
                  "Team"]
                [:button.mlb-reset.price-button
                  "Try for free"]]
              [:th
                [:div.tire-title
                  "Enterprise"]
                [:button.mlb-reset.price-button
                  "Try for free"]]]]]

        [:table.pricing-table.mobile-only
          [:thead
            [:tr
              [:th
                [:div.tire-title
                  "FREE"]
                [:div.tire-price
                  [:span.dollar "$"]
                  "0"]
                [:button.mlb-reset.price-button
                  "Get started"]]]]
          [:tbody
            [:tr
              [:td.pricing-description
                "Up to 10 users"]]
            [:tr
              [:td.pricing-description
                "50 new post cap"
                [:span.info
                  {:data-toggle "tooltip"
                   :data-placement "top"
                   :data-container "body"
                   :title "50 new post cap"
                   :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]]
            [:tr
              [:td.pricing-description
                "6 months of history retained"
                [:span.info
                  {:data-toggle "tooltip"
                   :data-placement "top"
                   :data-container "body"
                   :title "6 months of history retained"
                   :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]]
            [:tr
              [:td.pricing-description
                "25 MB upload limit"]]
            [:tr
              [:td.pricing-description
                "500 MB file storage"]]
            [:tr
              [:td.pricing-description
                "G suite single sign-on"]]
            [:tr
              [:td.pricing-description
                "Slack single sign-on"]]
            [:tr
              [:td.pricing-description
                "Sync with Slack"
                [:span.info
                  {:data-toggle "tooltip"
                   :data-placement "top"
                   :data-container "body"
                   :title "Sync with Slack"
                   :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]]
            [:tr
              [:td.pricing-description
                "Dropbox, Google Drive, and other integrations"]]]]

        [:table.pricing-table.mobile-only
          [:thead
            [:tr
              [:th
                [:div.tire-title
                  "TEAM"]
                [:div.tire-price
                  [:span.dollar "$"]
                  "45"
                  [:span.per-month\
                    "per month"]]
                [:button.mlb-reset.price-button
                  "Get started"]]]]
          [:tbody
            [:tr
              [:td.pricing-description
                "Up to 25 users"]]
            [:tr
              [:td.pricing-description
                "Unlimited posts"
                [:span.info
                  {:data-toggle "tooltip"
                   :data-placement "top"
                   :data-container "body"
                   :title "Unlimited posts"
                   :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]]
            [:tr
              [:td.pricing-description
                "No history limit"
                [:span.info
                  {:data-toggle "tooltip"
                   :data-placement "top"
                   :data-container "body"
                   :title "No history limit"
                   :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]]
            [:tr
              [:td.pricing-description
                "No file size upload limit"]]
            [:tr
              [:td.pricing-description
                "5 TB file storage"]]
            [:tr
              [:td.pricing-description
                "G suite single sign-on"]]
            [:tr
              [:td.pricing-description
                "Slack single sign-on"]]
            [:tr
              [:td.pricing-description
                "Sync with Slack"
                [:span.info
                  {:data-toggle "tooltip"
                   :data-placement "top"
                   :data-container "body"
                   :title "Sync with Slack"
                   :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]]
            [:tr
              [:td.pricing-description
                "Dropbox, Google Drive, and other integrations"]]
            [:tr
              [:td.pricing-description
                "Advanced permissions"]]
            [:tr
              [:td.pricing-description
                "Priority support"
                [:span.info
                  {:data-toggle "tooltip"
                   :data-placement "top"
                   :data-container "body"
                   :title "Priority support"
                   :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]]]]

        [:table.pricing-table.mobile-only
          [:thead
            [:tr
              [:th
                [:div.tire-title
                  "ENTERPRISE"]
                [:div.tire-price
                  [:span.dollar "$"]
                  "125"
                  [:span.per-month\
                    "per month"]]
                [:button.mlb-reset.price-button
                  "Get started"]]]]
          [:tbody
            [:tr
              [:td.pricing-description
                "Includes everthing in "
                [:span.strong "team"]
                " and:"]]
            [:tr
              [:td.pricing-description
                "Up to 50 users"]]
            [:tr
              [:td.pricing-description
                "$2 per additional user "]]
            [:tr
              [:td.pricing-description
                "Analytics"
                [:span.info
                  {:data-toggle "tooltip"
                   :data-placement "top"
                   :data-container "body"
                   :title "Analytics"
                   :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]]
            [:tr
              [:td.pricing-description
                "Uptime SLA"
                [:span.info
                  {:data-toggle "tooltip"
                   :data-placement "top"
                   :data-container "body"
                   :title "Uptime SLA"
                   :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"}]]]
            [:tr
              [:td.pricing-description
                "On premise"]]]]

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

      (slack-comparison-section true)

      keep-aligned-section

      access-anywhere-section

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
          ;; Google fonts Muli
          [:link {:href "https://fonts.googleapis.com/css?family=Muli" :rel "stylesheet"}]
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