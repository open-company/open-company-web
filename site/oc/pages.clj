(ns oc.pages
  (:require [oc.terms :as terms]
            [oc.privacy :as privacy]
            [environ.core :refer (env)]))

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
      [:a.carrot-early-access-link.hidden {:href "/"} "/"]]])

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
          "Where leaders speak"]
        [:div.subheadline.big-web-only
          "Leadership updates that keep everyone aligned"]
        [:div.subheadline.second-line.big-web-only
          "around what matters most"]
        [:div.subheadline.mobile-only
          (str
           "Leadership updates that keep everyone aligned "
           "around what matters most")]
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

        [:div.horizontal-carousell
          [:div.left-gradient]
          [:div.right-gradient]
          [:div.horizontal-carousell-inner
            [:img.horizontal-carousell-1
              {:src (cdn "/img/ML/homepage_mobile_screenshot_1.png")
               :src-set (str (cdn "/img/ML/homepage_mobile_screenshot_1@2x.png") " 2x")}]
            [:img.horizontal-carousell-2
              {:src (cdn "/img/ML/homepage_mobile_screenshot_2.png")
               :src-set (str (cdn "/img/ML/homepage_mobile_screenshot_2@2x.png") " 2x")}]
            [:img.horizontal-carousell-3
              {:src (cdn "/img/ML/homepage_mobile_screenshot_3.png")
               :src-set (str (cdn "/img/ML/homepage_mobile_screenshot_3@2x.png") " 2x")}]]]

        [:video.homepage-main-animation
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
            "Your browser doesn´t support this video format."]]

        [:div.stay-aligned-container
          [:div.stay-aligned-icon]
          [:div.stay-aligned-message
            "Stay aligned around topics that matter."]]

        [:div.cards-container
          [:div.cards-row.group
            [:div.card.card-1
              [:div.card-icon]
              [:div.card-title
                (str
                 "Highlight what’s "
                 "important")]
              [:div.card-content
                (str
                  "Elevate key updates above "
                  "the noise so they won’t be "
                  "missed. It’s perfect for "
                  "distributed teams, too.")]]
            [:div.card.card-2
              [:div.card-icon]
              [:div.card-title
                (str
                  "Cross-team "
                  "awareness")]
              [:div.card-content
                (str
                  "Keep teams in sync with "
                  "each other so you can see "
                  "what’s happening across "
                  "the company.")]]
            [:div.card.card-3
              [:div.card-icon]
              [:div.card-title
                (str
                  "Focused, topic-based "
                  "conversations")]
              [:div.card-content
                (str
                  "Capture team reactions, "
                  "comments and questions "
                  "together in one place.")]]]
          [:div.cards-row.group
            [:div.card.card-4
              [:div.card-icon]
              [:div.card-title
                (str
                  "The whole "
                  "story")]
              [:div.card-content
                (str
                  "New employees get up to "
                  "speed quickly with the full "
                  "picture in one place.")]]
            [:div.card.card-5
              [:div.card-icon]
              [:div.card-title
                (str
                  "Visible "
                  "engagement")]
              [:div.card-content
                (str
                  "Carrot measures team "
                  "engagement so leaders "
                  "can actually see if their "
                  "teams are aligned or not.")]]
            [:div.card.card-6
              [:div.card-icon]
              [:div.card-title
                (str
                  "In sync "
                  "with Slack")]
              [:div.card-content
                (str
                  "Communication is "
                  "automatically shared to the "
                  "right channel. ")
                [:a
                  {:href "/slack"}
                  "Learn more"]]]]]]

      [:section.second-section
        [:div.balloon.big-yellow-1]
        [:div.balloon.small-blue-1]
        [:div.second-section-title
          "Don’t take our word for it"]
        [:div.second-section-subtitle
          "Here’s how we’re helping teams like yours."]
        [:div.cards-container.group
          [:div.card
            [:div.card-content
              (str
               "We love Slack for spontaneous "
               "stuff, but Carrot for more "
               "focused, topic-based "
               "conversations.")]
            [:div.card-author
              [:img.card-avatar
                {:src (cdn "/img/ML/happy_face_blue.svg")}]
              [:div.author-name
                "John Smith"]
              [:div.author-company
                "Acme Co."]]]
          [:div.card
            [:div.card-content
              (str
               "Crucial updates get drowned "
               "out by \"taco "
               "Tuesday?\" and silly memes. "
               "Carrot makes sure the "
               "important stuff is always there.")]
            [:div.card-author
              [:img.card-avatar
                {:src (cdn "/img/ML/happy_face_purple.svg")}]
              [:div.author-name
                "John Smith"]
              [:div.author-company
                "Acme Co."]]]
          [:div.card
            [:div.card-content
              (str
               "RIP team email! 👻  With "
               "Carrot, email is finally "
               "obsolete for team updates "
               "and the rest.")]
            [:div.card-author
              [:img.card-avatar
                {:src (cdn "/img/ML/happy_face_red.svg")}]
              [:div.author-name
                "John Smith"]
              [:div.author-company
                "Acme Co."]]]
          [:div.card
            [:div.card-content
              (str
               "I never knew if anyone even "
               "saw my updates! Carrot shows "
               "me who's viewed what so I know "
               "who’s engaged and aligned. 🙏")]
            [:div.card-author
              [:img.card-avatar
                {:src (cdn "/img/ML/happy_face_yellow.svg")}]
              [:div.author-name
                "John Smith"]
              [:div.author-company
                "Acme Co."]]]]]

      [:section.third-section
        [:div.third-section-title
          "Keep everyone aligned around what matters most."]
        [:button.mlb-reset.get-started-button
          "Get started for free"]]
      ]])

(defn pricing
  "Pricing page. This is a copy of oc.web.components.pricing and every change here should be reflected there and vice versa."
  [options]
   [:div.container.outer.sector.content
    [:div.row
     [:div.col-md-12.pricing-header
      [:h2 "Simple Pricing"]
      [:p "Transparent prices for any need."]]]
    [:div.row
     "<!-- Pricing Item -->"
     [:div.col-md-4
      [:div.pricing.hover-effect
       [:div.pricing-name
        [:h3 "Team"
         [:span "Internal Slack distrbution"]]]
       [:div.pricing-price [:h4 [:i "$"] "25" [:span "Per Month"]]]
       [:ul.pricing-content.list-unstyled
        [:li "Stakeholder dashboard"]
        [:li "Rich Slack integration"]]
       [:div.pricing-footer
        [:p "Perfect for keeping your team members informed."]
        [:p "Optionally involve the crowd with a public stakeholder dashboard."]]]]
     [:div.col-md-4
      [:div.pricing.hover-effect
       [:div.pricing-name
        [:h3 "Stakeholders"
         [:span "Periodic stakeholder updates"]]]
       [:div.pricing-price [:h4 [:i "$"] "50" [:span "Per Month"]]]
       [:ul.pricing-content.list-unstyled
        [:li "Stakeholder dashboard"]
        [:li "Rich Slack integration"]
        [:li [:b "Periodic stakeholder updates"]]]
       [:div.pricing-footer
        [:p "Keep your busy investors and advisors informed with periodic updates that follow best practices."]]]]
     [:div.col-md-4
      [:div.pricing.hover-effect
       [:div.pricing-name
        [:h3 "Concierge"
         [:span "Beautifully designed content"]]]
       [:div.pricing-price [:h4 [:i "$"] "250" [:span "Per Month"]]]
       [:ul.pricing-content.list-unstyled
        [:li "Stakeholder dashboard"]
        [:li "Rich Slack integration"]
        [:li "Periodic stakeholder updates"]
        [:li [:b "Concierge support to desgin custom stakeholder content *"]]]
       [:div.pricing-footer
        [:p "Impress your investors and advisors with beautful, concise and meaningful updates."]]]]
     ]])

(defn about
  "About page. This is a copy of oc.web.components.about and
   every change here should be reflected there and vice versa."
  [options]
  [:div
    {:id "wrap"}
    [:div.main.about
      [:section.about-header
        [:div.balloon.big-yellow]
        [:div.balloon.big-red]
        [:div.balloon.big-purple]
        [:div.balloon.small-purple-face]
        [:div.balloon.small-red]
        [:div.balloon.small-yellow-face]
        [:div.balloon.small-yellow]
        [:div.balloon.big-purple-1]
        [:div.balloon.small-green]
        [:div.balloon.big-blue]
        [:div.balloon.small-red-2]

        [:h1.about "About"]
        [:div.about-subline
          (str
           "Growing companies struggle to keep everyone on the same page. "
           "Carrot provides the big picture that keeps them together.")]

        [:div.paragraphs-container.group
          [:p
            (str
             "Messaging apps are designed for real-time work. They’re great in the moment, "
             "but chat gets noisy and conversations disappear, making it easy to miss the important stuff.")]
          [:p
            (str
              "Carrot provides an easy to read view of the latest announcements, updates, and stories "
              "so you can always see what’s happening in context. A common, shared view of what’s "
              "important creates real transparency and alignment.")]
          [:p
            "It also brings teams closer so they can grow together."]
          [:p
            "Carrot on!"]

          [:div.principles-title
            "We designed Carrot based on three core principles:"]

          [:div.principles.group
            [:div.principle.left-principle
              [:div.principle-icon]
              [:div.principle-title
                (str
                 "Alignment should be "
                 "simple and fun.")]
              [:div.principle-description
                (str
                 "Alignment might be essential for success, "
                 "but achieving it has never been easy. "
                 "We’re changing that. With a simple "
                 "structure and beautiful writing experience, "
                 "it can’t be easier. Just say what’s going on, "
                 "we’ll take care of the rest.")]]

            [:div.principle.right-principle
              [:div.principle-icon]
              [:div.principle-title
                (str
                 "The “big picture” should "
                 "always be visible.")]
              [:div.principle-description
                (str
                 "No one wants to look through folders and "
                 "documents to understand what’s going on, "
                 "or search through chat messages to find "
                 "something. It should be easy to get an "
                 "instant, bird’s-eye view of what’s "
                 "happening across the company anytime.")]]]

          [:div.principle.center-principle
            [:div.principle-icon]
            [:div.principle-title
              (str
               "It should be easy to keep "
               "stakeholders in the loop, too.")]
            [:div.principle-description
              (str
               "Sharing the latest with stakeholders "
               "shouldn’t be a chore. Just give investors, "
               "customers and others their own big picture "
               "view. It’s the surest way to keep them "
               "engaged and supportive, and an easy way "
               "to grow your business.")]]]]

      [:section.about-team.group
        [:div.about-team-inner.group
          [:h1.team "Our team"]

          [:div.about-team-users.group
            [:div.column-left.group
              [:div.team-card.stuart-levinson
                [:div.team-avatar
                  [:img {:src "http://www.gravatar.com/avatar/99399ee082e57d67045cb005f9c2e4ef?s=100"}]]
                [:div.team-member
                  [:div.team-name "Stuart Levinson"]
                  [:div.team-title "CEO and founder"]
                  [:div.team-media-links
                    [:a.linkedin {:href "https://linkedin.com/in/stuartlevinson"}]]]]
              [:div.team-card.iacopo-carraro
                [:div.team-avatar
                  [:img {:src "http://www.gravatar.com/avatar/0224b757acf053e02d8cdf807620417c?s=100"}]]
                [:div.team-member
                  [:div.team-name "Iacopo Carraro"]
                  [:div.team-title "Software Engineer"]
                  [:div.team-media-links
                    [:a.linkedin {:href "https://www.linkedin.com/in/iacopocarraro"}]]]]]

            [:div.column-right.group
              [:div.team-card.sean-johnson
                [:div.team-avatar
                  [:img {:src "http://www.gravatar.com/avatar/f5b8fc1affa266c8072068f811f63e04?s=100"}]]
                [:div.team-member
                  [:div.team-name "Sean Johnson"]
                  [:div.team-title "CTO and founder"]
                  [:div.team-media-links
                    [:a.linkedin {:href "https://linkedin.com/in/snootymonkey"}]]]]
              [:div.team-card.nathan-zorn
                [:div.team-avatar
                  [:img {:src "https://s.gravatar.com/avatar/e7407a2aefa6b5a54a0af630a0a58210?s=100"}]]
                [:div.team-member
                  [:div.team-name "Nathan Zorn"]
                  [:div.team-title "Software Engineer"]
                  [:div.team-media-links
                    [:a.linkedin {:href "https://www.linkedin.com/in/nathanzorn"}]]]]]]

          [:div.about-team-users.group
            [:div.column-center.group
              [:div.team-card.new-member
                [:div.team-avatar]
                  [:div.team-member
                    [:div.team-name "You?"]
                    [:div.team-title "We’re always looking for talented"]
                    [:div.team-title "people to join us."]]]]]]]

      [:section.about-footer.group

        [:div.block.join-us
          [:div.block-title
            "Join Us"]
          [:div.block-description
            "Want to join us? We are always looking for amazing people no matter where they live."]
          [:a.link
            {:href (:contact-mail-to options)}
            "Say hello"]]

        [:div.block.open-source
          [:div.block-title
            "Open Source"]
          [:div.block-description
            "Have an idea you’d like to contribute? A new integration you’d like to see?"]
          [:a.link
            {:href "https://github.com/open-company"}
            "Build it with us on Github"]]]

      [:section.fourth-section.group
        [:div.above-noise-container
          [:div.above-noise-title
            "Rise above the noise"]
          [:div.above-noise-description
            "Give your team a clear view of what’s most important."]
          [:button.mlb-reset.get-started-button
            "Get started for free"]]]
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
          [:title "Carrot - Stay aligned"]
          ;; Reset IE
          "<!--[if lt IE 9]><script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script><![endif]-->"
          ;; Bootstrap CSS //getbootstrap.com/
          [:link
            {:rel "stylesheet"
             :href "//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
             :integrity "sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
             :crossorigin "anonymous"}]
          ;; Normalize.css //necolas.github.io/normalize.css/
          ;; TODO inline this into app.main.css
          [:link {:rel "stylesheet" :href "/css/normalize.css"}]
          ;; Font Awesome icon fonts //fortawesome.github.io/Font-Awesome/cheatsheet/
          [:link {:rel "stylesheet" :href "//maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css"}]
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
              ;; Top left corner
              [:div.balloon.big.big-yellow]
              [:div.balloon.small.small-red]
              [:div.balloon.small.face.small-face-purple]
              ;; Top right corner
              [:div.balloon.big.big-red]
              [:div.balloon.small.small-yellow]
              [:div.balloon.small.face.small-face-yellow]
              ;; Bottom left corner
              [:div.balloon.big.big-green]
              [:div.balloon.small.small-purple]
              ;; Bottom right corner
              [:div.balloon.big.big-purple]

              [:div.oc-loading-inner
                [:div.oc-loading-heart]
                [:div.oc-loading-body]]
              [:div.setup-cta
                "Here we go!"]]]
          [:div#oc-error-banner]
          [:div#oc-loading]
          ;; Static js files
          [:script {:type "text/javascript" :src (cdn "/js/static-js.js")}]
          ;; JWT decode library
          [:script {:src "/lib/jwt_decode/jwt-decode.min.js" :type "text/javascript"}]
          ;; Custom Tooltips
          [:script {:type "text/javascript" :src "/lib/tooltip/tooltip.js"}]
          ;; jQuery needed by Bootstrap JavaScript
          [:script {:src "//ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js" :type "text/javascript"}]
          ;; Truncate html string
          [:script {:type "text/javascript" :src "/lib/truncate/jquery.dotdotdot.js"}]
          ;; Rangy
          [:script {:type "text/javascript" :src "/lib/rangy/rangy-core.js"}]
          [:script {:type "text/javascript" :src "/lib/rangy/rangy-classapplier.js"}]
          [:script {:type "text/javascript" :src "/lib/rangy/rangy-selectionsaverestore.js"}]
          ;; jQuery textcomplete needed by Emoji One autocomplete
          [:script
            {:src "//cdnjs.cloudflare.com/ajax/libs/jquery.textcomplete/1.7.3/jquery.textcomplete.min.js"
             :type "text/javascript"}]
          ;; WURFL used for mobile/tablet detection
          [:script {:type "text/javascript" :src "//wurfl.io/wurfl.js"}]
          ;; jQuery scrollTo plugin
          [:script {:src "/lib/scrollTo/scrollTo.min.js" :type "text/javascript"}]
          ;; jQuery UI
          [:script {:src "//ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js" :type "text/javascript"}]
          ;; Resolve jQuery UI and Bootstrap tooltip conflict
          [:script "$.widget.bridge('uitooltip', $.ui.tooltip);"]
          ;; Bootstrap JavaScript //getf.com/
          [:script
            {:src "//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
             :type "text/javascript"
             :integrity "sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
             :crossorigin "anonymous"}]
          ;; Emoji One Autocomplete
          [:script {:src "/js/emojione/autocomplete.js" :type "text/javascript"}]
          ;; ClojureScript generated JavaScript
          [:script {:src "/oc.js" :type "text/javascript"}]
          ;; Utilities
          [:script {:type "text/javascript", :src "/lib/js-utils/svg-utils.js"}]
          [:script {:type "text/javascript", :src "/lib/js-utils/pasteHtmlAtCaret.js"}]
          ;; Clean HTML input
          [:script {:src "/lib/cleanHTML/cleanHTML.js" :type "text/javascript"}]
          ;; MediumEditorAutolist
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorAutolist/autolist.js"}]
          ;; MediumEditorMediaPicker
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorMediaPicker/MediaPicker.js"}]]})

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
          [:title "Carrot - Stay aligned"]
          ;; Reset IE
          "<!--[if lt IE 9]><script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script><![endif]-->"
          ;; Bootstrap CSS //getbootstrap.com/
          [:link
            {:rel "stylesheet"
             :href "//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
             :integrity "sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
             :crossorigin "anonymous"}]
          ;; Font Awesome icon fonts //fortawesome.github.io/Font-Awesome/cheatsheet/
          [:link {:rel "stylesheet" :href "//maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css"}]
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
          [:script {:src "//ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js" :type "text/javascript"}]
          ;; Automatically load the needed polyfill depending on
          ;; the browser user agent and the available features
          [:script {:src "https://cdn.polyfill.io/v2/polyfill.min.js"}]]
   :body [:body
          [:div#app
            [:div.oc-loading.active
              ;; Top left corner
              [:div.balloon.big.big-yellow]
              [:div.balloon.small.small-red]
              [:div.balloon.small.face.small-face-purple]
              ;; Top right corner
              [:div.balloon.big.big-red]
              [:div.balloon.small.small-yellow]
              [:div.balloon.small.face.small-face-yellow]
              ;; Bottom left corner
              [:div.balloon.big.big-green]
              [:div.balloon.small.small-purple]
              ;; Bottom right corner
              [:div.balloon.big.big-purple]

              [:div.oc-loading-inner
                [:div.oc-loading-heart]
                [:div.oc-loading-body]]
              [:div.setup-cta
                "Here we go!"]]]
          [:div#oc-error-banner]
          [:div#oc-loading]
          ;; Static js files
          [:script {:src (cdn "/js/static-js.js")}]
          ;; jQuery textcomplete needed by Emoji One autocomplete
          [:script
            {:src "//cdnjs.cloudflare.com/ajax/libs/jquery.textcomplete/1.7.3/jquery.textcomplete.min.js"
             :type "text/javascript"}]
          ;; WURFL used for mobile/tablet detection
          [:script {:type "text/javascript" :src "//wurfl.io/wurfl.js"}]
          ;; jQuery UI
          [:script {:src "//ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js" :type "text/javascript"}]
          ;; Resolve jQuery UI and Bootstrap tooltip conflict
          [:script "$.widget.bridge('uitooltip', $.ui.tooltip);"]
          ;; Bootstrap JavaScript //getf.com/
          [:script
            {:src "//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
             :type "text/javascript"
             :integrity "sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
             :crossorigin "anonymous"}]
          ;; Compiled oc.min.js from our CDN
          [:script {:src (cdn "/oc.js")}]
          ;; Compiled assents
          [:script {:src (cdn "/oc_assets.js")}]]})