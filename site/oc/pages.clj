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
     :style #js {:display "none"}}
    ; [:img.carrot-box {:src (cdn "/img/ML/carrot_box.svg")}]
    [:div.carrot-box-thanks
      [:div.thanks-headline "Thanks!"]
      "We’ve sent you an email to confirm."
      [:div.carrot-early-access-top.hidden "Get earlier access when your friends sign up with this link:"]
      [:a.carrot-early-access-link.hidden {:href "/"} "/"]]])

(defn try-it-form [form-id try-it-class]
  [:form.validate
    {:action (or (env :oc-mailchimp-api-endpoint) "https://onhq6jg245.execute-api.us-east-1.amazonaws.com/dev/subscribe")
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
      [:div.cta
        [:h1.headline "Grow Together"]
        [:div.subheadline
          "Announcements, updates and stories that bring teams closer"]
        ; (try-it-form "try-it-form-central" "try-it-combo-field-top")
        [:button.mlb-reset.get-started-centred-bt
          {:id "get-started-centred-bt"}
          "Get started for free"]
        [:div.small-teams
          {:id "easy-setup-label"}
          "Easy set-up • Free for small teams"]
        (carrot-box-thanks "carrot-box-thanks-top")
        [:div.carrot-box-container.confirm-thanks.group
          {:style #js {:display "none"}}
          [:div.carrot-box-thanks
            [:div.thanks-headline "You are Confirmed!"]
            [:div.thanks-subheadline "Thank you for subscribing."]]]

        ;; FIXME: Remove the carrot screenshot for the initial onboarding period
        (comment
          [:img.homepage-screenshot
            {:src (cdn "/img/ML/home_page_screenshot.png")
             :width 756
             :height 511}])]


      [:div.illustrations.group

        [:div.illustration.illustration-1.group
          [:img {:src (cdn "/img/ML/home_page_il_1_412_385.svg")}]
          [:div.description.group
            [:div.title
              "Get your team aligned"]
            [:div.subtitle
              "Whether your team is local or distributed, keep everyone focused on what’s most important."]]]

        [:div.illustration.illustration-2.group
          [:img {:src (cdn "/img/ML/home_page_il_2_444_414.svg")}]
          [:div.description.group
            [:div.title
              "Simplify stakeholder updates"]
            [:div.subtitle
              "Create beautiful updates for your company, investors, and advisors in less time. It’s never been easier to keep all stakeholders in the loop."]]]

        [:div.illustration.illustration-3.group
          [:img {:src (cdn "/img/ML/home_page_il_3_355_350.svg")}]
          [:div.description.group
            [:div.title
              "Expand your network"]
            [:div.subtitle
              "Share news with recruits, potential investors and customers to keep them engaged and supportive. It’s an easy way to build trust and grow your business."]]]]

      (comment
        [:div.customers
          [:div.customers-title
            [:img {:src (cdn "/img/ML/user_avatar_yellow.svg")}]
            "Our happy clients"]
          [:div.customers-cards.group
            [:div.left-arrow
              [:button.mlb-reset.left-arrow-bt
                {:disabled true}]]
            [:div.customers-cards-scroll
              [:div.customers-card]
              [:div.customers-card]
              [:div.customers-card]]
            [:div.right-arrow
              [:button.mlb-reset.right-arrow-bt
                {:disabled true}]]]])

      ; [:div.try-it
      ;   {:id "mc_embed_signup"}
      ;   [:div.try-it-title
      ;     {:id "thank-you-bottom"}
      ;     "Request early access"]
      ;   [:div.try-it-subtitle
      ;     "Easy set-up • Free for small teams"]
      ;   [:div
      ;     (try-it-form "try-it-form-bottom" "try-it-combo-field-bottom")]
      ;   (carrot-box-thanks "carrot-box-thanks-bottom")]
        ]])

(defn features [options]
  [:div.container.main.features
    ; Hope page header
    [:h1.features "Features"]

    [:div.divider-line]


    [:div.illustrations.group

      [:div.illustration.illustration-1.group
        [:img {:src (cdn "/img/ML/features_il_1_608_544.svg")}]
        [:div.description.group
          [:div.title
            "Simplicity"]
          [:div.subtitle
            "Whether you’re adding a quick team update about one topic, or writing a regular stakeholder update that covers many, getting started is fast and simple."]]]

      [:div.illustration.illustration-2.group
        [:img {:src (cdn "/img/ML/features_il_2_465_408.svg")}]
        [:div.description.group
          [:div.title
            "Company timeline"]
          [:div.subtitle
            "It’s easy to catch up if you missed something or want more context. Great for getting new employees up to speed, too."]]]

      [:div.illustration.illustration-3.group
        [:img {:src (cdn "/img/ML/features_il_3_443_269.svg")}]
        [:div.description.group
          [:div.title
            "Feedback loops"]
          [:div.subtitle
            "Company updates are best when they trigger conversation. Comments and reactions keep everyone engaged and in sync - great for distributed teams."]]]

      [:div.illustration.illustration-4.group
        [:img {:src (cdn "/img/ML/features_il_4_346_321.svg")}]
        [:div.description.group
          [:div.title
            "Works with Slack"]
          [:div.subtitle
            "With Slack single sign-on and our Slack bot, updates are automatically shared to the right channels. Discussions about updates can happen within Slack or Carrot - everything is kept in sync."]]]

      [:div.illustration.illustration-5.group
        [:img {:src (cdn "/img/ML/features_il_5_333_274.svg")}]
        [:div.description.group
          [:div.title
            "Go public"]
          [:div.subtitle
            "Updates can also be made public - ideal for crowdfunded ventures, social enterprises, and startups interested in full transparency."]]]]])

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
     ;; "<!--         <div class=\"col-md-4 sm-12\">\n          <h2>Team</h2>\n          <p>Internal distribution with Slack.</p>\n        </div>\n        <div class=\"col-md-4 sm-12\">\n          <h2>Stakeholders</h2>\n          <p>Periodic stakeholder updates distributed automatically.</p>\n        </div>\n        <div class=\"col-md-4 sm-12\">\n          <h2>Concierge</h2>\n          <p>Beautiful stakeholder updates, hand-crafted by content creation professionals.</p>\n        </div>\n -->"
     ]])

(defn about
  "About page. This is a copy of oc.web.components.about and every change here should be reflected there and vice versa."
  [options]
  [:div
    [:div.container.main.about

      [:h1.about "About"]

      [:div.divider-line]

      [:div.ovals-container

        [:div.ovals-container-face.face-red]
        [:div.ovals-container-face.face-yellow]
        [:div.ovals-container-face.face-blue]
        [:div.ovals-container-face.face-green]
        [:div.ovals-container-face.face-purple]

        [:div.about-subline
          "People are hyper-connected in the moment, but still struggle to know what’s really happening across the company."]
        [:div.paragraphs-container.group
          [:div.mobile-only.happy-face.yellow-happy-face]
          [:div.mobile-only.happy-face.red-happy-face]
          [:div.paragraphs-bg-container.group
            [:div.paragraph
              "The solution is surprisingly simple and effective - great company updates that build transparency and alignment."]
            [:div.paragraph
              "With that in mind we designed Carrot based on three principles:"]]
          [:div.mobile-only.happy-face.blue-happy-face]
          [:div.mobile-only.happy-face.purple-happy-face]
          [:div.mobile-only.happy-face.green-happy-face]]]

      [:div.principles.group
        [:div.principle.principle-1
          [:div.principle-oval-bg]
          [:div.principle-logo]
          [:div.principle-title "It has to be easy or no one will play"]
          [:div.principle-description "Alignment might be essential for success, but achieving it has never been easy or fun. We’re changing that. With a simple structure and beautiful writing experience, it can’t be easier. Just say what’s going on, we’ll take care of the rest."]]

        [:div.principle.principle-2
          [:div.principle-oval-bg]
          [:div.principle-logo]
          [:div.principle-title "The “big picture” should always be visible"]
          [:div.principle-description "No one wants to look through folders and documents to understand what’s going on, or search through chat messages to find something. It should be easy to get an instant view of what’s happening across the company anytime."]]

        [:div.principle.principle-3
          [:div.principle-oval-bg]
          [:div.principle-logo]
          [:div.principle-title "Alignment is valuable beyond the team, too"]
          [:div.principle-description "Sharing beautiful updates with recruits, investors,  customers and other outside stakeholders is the surest way to keep them engaged and supportive. It’s an easy way to expand your network and grow your business."]]]

    ] ;<!-- main -->

    [:div.about-alignment
      [:div.quote]
      [:div.about-alignment-description "Company alignment requires real openness and transparency."]]

    [:div.about-team.group
      [:div.about-team-inner.group
        [:h1.team "Our team"]
        [:div.divider-line]

        [:div.group
          [:div.column-left.group
            ;; Member: Stuart Levinson
            [:div.team-card.stuart-levinson
              [:div.team-avatar
                [:img {:src "http://www.gravatar.com/avatar/99399ee082e57d67045cb005f9c2e4ef?s=64"}]]
              [:div.team-member
                [:div.team-name "Stuart Levinson"]
                [:div.team-title "CEO and cofounder"]
                [:div.team-description "Prior to Carrot, Stuart started two venture-backed startups - Venetica (acquired by IBM) and TalkTo (acquired by Path). Those experiences, pre- and post-acquisitions, inspired a passion for transparency and its effect on overall alignment."]
                [:div.team-media-links
                  [:a.linkedin {:href "https://linkedin.com/in/stuartlevinson"}]
                  [:a.twitter {:href "https://twitter.com/stuartlevinson"}]]]]
            ;; Member: Iacopo Carraro
            [:div.team-card.iacopo-carraro
              [:div.team-avatar
                [:img {:src "http://www.gravatar.com/avatar/0224b757acf053e02d8cdf807620417c?s=64"}]]
              [:div.team-member
                [:div.team-name "Iacopo Carraro"]
                [:div.team-description "Iacopo is a full-stack engineer with lots of remote team and startup experience."]
                [:div.team-media-links
                  [:a.linkedin {:href "https://www.linkedin.com/pub/iacopo-carraro/21/ba2/5ab"}]
                  [:a.twitter {:href "http://twitter.com/bago2k4"}]
                  [:a.github {:href "http://github.com/bago2k4"} [:i.fa.fa-github]]]]]]

          [:div.column-right.group
            ;; Member: Sean Johnson
            [:div.team-card.sean-johnson
              [:div.team-avatar
                [:img {:src "http://www.gravatar.com/avatar/f5b8fc1affa266c8072068f811f63e04?s=64"}]]
              [:div.team-member
                [:div.team-name "Sean Johnson"]
                [:div.team-title "CTO and cofounder"]
                [:div.team-description "As a serial startup CTO and engineer, Sean has over 20 years experience building products and startup engineering teams."]
                [:div.team-media-links
                  [:a.linkedin {:href "https://linkedin.com/in/snootymonkey"}]
                  [:a.twitter {:href "http://twitter.com/belucid"}]
                  [:a.github {:href "http://github.com/belucid"} [:i.fa.fa-github]]]]]
            ;; Member: new member
            [:div.team-card.new-member
              [:div.team-avatar]
                [:div.team-member
                  [:div.team-name "You?"]
                  [:div.team-description "We're always looking for talented individuals. Drop us a line if you share our mission."]]]]]]]

    [:div.about-footer.group

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
    ])

(defn not-found [{contact-mail-to :contact-mail-to contact-email :contact-email}]
  [:div.not-found
    [:div
      [:div.error-page
        [:img {:src (cdn "/img/ML/carrot_404.svg") :width 338 :height 189}]
        [:h2 "Page Not Found"]
        [:p "It seems we can't find what you're looking for."]
        [:p.last "The page may have been moved or removed."]
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
          [:meta {:content "width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no", :name "viewport"}]
          [:meta {:name "apple-mobile-web-app-capable" :content "yes"}]
          [:meta {:name "slack-app-id" :content (env :oc-slack-app-id)}]
          [:link {:rel "icon" :type "image/png" :href (cdn "/img/carrot_logo.png") :sizes "64x64"}]
          ;; The above 3 meta tags *must* come first in the head;
          ;; any other head content must come *after* these tags
          [:title "Carrot - Get everyone aligned"]
          ;; Reset IE
          "<!--[if lt IE 9]><script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script><![endif]-->"
          ;; Bootstrap CSS //getbootstrap.com/
          [:link {:rel "stylesheet" :href "//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" :integrity "sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" :crossorigin "anonymous"}]
          ;; Normalize.css //necolas.github.io/normalize.css/
          ;; TODO inline this into app.main.css
          [:link {:rel "stylesheet" :href "/css/normalize.css"}]
          ;; Font Awesome icon fonts //fortawesome.github.io/Font-Awesome/cheatsheet/
          [:link {:rel "stylesheet" :href "//maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css"}]
          ;; OpenCompany CSS
          [:link {:type "text/css" :rel "stylesheet" :href "/css/app.main.css"}]
          ;; jQuery UI CSS
          [:link {:rel "stylesheet" :href "//ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/themes/smoothness/jquery-ui.css"}]
          ;; Emoji One Autocomplete CSS
          [:link {:type "text/css" :rel "stylesheet" :href "/css/emojione/autocomplete.css"}]
          ;; Google fonts Muli
          [:link {:href "https://fonts.googleapis.com/css?family=Muli" :rel "stylesheet"}]
          ;;  Medium Editor css
          [:link {:type "text/css" :rel "stylesheet" :href "/css/medium-editor/medium-editor.css"}]
          [:link {:type "text/css" :rel "stylesheet" :href "/css/medium-editor/default.css"}]
          ;; Emojione CSS
          [:link {:type "text/css" :rel "stylesheet" :href "/css/emojione.css"}]
          ;; EmojionePicker css from cljsjs
          [:link {:type "text/css" :rel "stylesheet" :href "/css/emojione-picker.css"}]
          ;; Emojone Sprites CSS
          [:link {:type "text/css" :rel "stylesheet" :href "/css/emojione.sprites.css"}]
          ;; CarrotKit Font
          [:link {:type "text/css" :rel "stylesheet" :href "/css/fonts/CarrotKit.css"}]
          ;; MediumEditorMediaPicker
          [:link {:type "text/css" :rel "stylesheet" :href "/lib/MediumEditorExtensions/MediumEditorMediaPicker/MediaPicker.css"}]
          [:script {:type "text/javascript" :src "/lib/print_ascii.js"}]]
   :body [:body.small-footer
          [:div#app [:div.oc-loading.active [:div.oc-loading-inner [:div.oc-loading-heart] [:div.oc-loading-body]]]]
          [:div#oc-error-banner]
          [:div#oc-loading]
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
          [:script {:src "//cdnjs.cloudflare.com/ajax/libs/jquery.textcomplete/1.7.3/jquery.textcomplete.min.js" :type "text/javascript"}]
          ;; WURFL used for mobile/tablet detection
          [:script {:type "text/javascript" :src "//wurfl.io/wurfl.js"}]
          ;; jQuery scrollTo plugin
          [:script {:src "/lib/scrollTo/scrollTo.min.js" :type "text/javascript"}]
          ;; jQuery UI
          [:script {:src "//ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js" :type "text/javascript"}]
          ;; Resolve jQuery UI and Bootstrap tooltip conflict
          [:script "$.widget.bridge('uitooltip', $.ui.tooltip);"]
          ;; Bootstrap JavaScript //getf.com/
          [:script {:src "//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" :type "text/javascript" :integrity "sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" :crossorigin "anonymous"}]
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
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorMediaPicker/MediaPicker.js"}]
          ;; MediumEditorCustomBold
          [:script {:type "text/javascript" :src "/lib/MediumEditorExtensions/MediumEditorCustomBold/CustomBold.js"}]
          ;; Static js files
          [:script {:src (cdn "/js/static-js.js")}]
          [:div.hidden [:img {:src (cdn "/img/emojione.sprites.png")}]]]})

(def prod-app-shell
  {:head [:head
          [:meta {:charset "utf-8"}]
          [:meta {:content "IE=edge", :http-equiv "X-UA-Compatible"}]
          [:meta {:content "width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no", :name "viewport"}]
          [:meta {:name "apple-mobile-web-app-capable" :content "yes"}]
          [:meta {:name "slack-app-id" :content (env :oc-slack-app-id)}]
          [:link {:rel "icon" :type "image/png" :href (cdn "/img/carrot_logo.png") :sizes "64x64"}]
          ;; The above 3 meta tags *must* come first in the head;
          ;; any other head content must come *after* these tags
          [:title "Carrot - Get everyone aligned"]
          ;; Reset IE
          "<!--[if lt IE 9]><script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script><![endif]-->"
          ;; Bootstrap CSS //getbootstrap.com/
          [:link {:rel "stylesheet" :href "//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" :integrity "sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" :crossorigin "anonymous"}]
          ;; Font Awesome icon fonts //fortawesome.github.io/Font-Awesome/cheatsheet/
          [:link {:rel "stylesheet" :href "//maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css"}]
          ;; jQuery UI CSS
          [:link {:rel "stylesheet" :href "//ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/themes/smoothness/jquery-ui.css"}]
          ;; App single CSS
          [:link {:type "text/css" :rel "stylesheet" :href (cdn "/main.css")}]
          ;; Google fonts Muli
          [:link {:href "https://fonts.googleapis.com/css?family=Muli" :rel "stylesheet"}]
          ;; CarrotKit Font
          [:link {:type "text/css" :rel "stylesheet" :href (cdn "/css/fonts/CarrotKit.css")}]
          ;; jQuery needed by Bootstrap JavaScript
          [:script {:src "//ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js" :type "text/javascript"}]]
   :body [:body.small-footer
          [:div#app [:div.oc-loading.active [:div.oc-loading-inner [:div.oc-loading-heart] [:div.oc-loading-body]]]]
          [:div#oc-error-banner]
          [:div#oc-loading]
          
          ;; jQuery textcomplete needed by Emoji One autocomplete
          [:script {:src "//cdnjs.cloudflare.com/ajax/libs/jquery.textcomplete/1.7.3/jquery.textcomplete.min.js" :type "text/javascript"}]
          ;; WURFL used for mobile/tablet detection
          [:script {:type "text/javascript" :src "//wurfl.io/wurfl.js"}]
          ;; jQuery UI
          [:script {:src "//ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js" :type "text/javascript"}]
          ;; Resolve jQuery UI and Bootstrap tooltip conflict
          [:script "$.widget.bridge('uitooltip', $.ui.tooltip);"]
          ;; Bootstrap JavaScript //getf.com/
          [:script {:src "//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" :type "text/javascript" :integrity "sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" :crossorigin "anonymous"}]
          ;; Compiled oc.min.js from our CDN
          [:script {:src (cdn "/oc.js")}]
          ;; Compiled assents
          [:script {:src (cdn "/oc_assets.js")}]
          ;; Static js files
          [:script {:src (cdn "/js/static-js.js")}]
          [:div.hidden [:img {:src (cdn "/img/emojione.sprites.png")}]]]})