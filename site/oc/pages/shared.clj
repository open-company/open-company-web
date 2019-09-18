(ns oc.pages.shared
  (:require [environ.core :refer (env)]))

(defn cdn [img-src]
  (str (when (env :oc-web-cdn-url) (str (env :oc-web-cdn-url) "/" (env :oc-deploy-key))) img-src))

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

(def testimonials-logos-line
  [:div.homepage-testimonials-container.group
    [:div.homepage-testimonials-copy
      "Remote teams around the world ❤️ Carrot"]
    [:div.homepage-testimonials-logos
      [:div.homepage-testimonials-logo.logo-ifttt]
      [:div.homepage-testimonials-logo.logo-hopper]
      [:div.homepage-testimonials-logo.logo-primary]
      [:div.homepage-testimonials-logo.logo-hinge]
      [:div.homepage-testimonials-logo.logo-resy]
      [:div.homepage-testimonials-logo.logo-flyt]
      [:div.homepage-testimonials-logo.logo-weblify]
      [:div.homepage-testimonials-logo.logo-novo]
      [:div.homepage-testimonials-logo.logo-gamercraft]]])

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
                "Create your team. It’s free!"]]]
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
                "Create your team. It’s free!"]]
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
                "Create your team. It’s free!"]]]
          [:div.keep-aligned-section-row-right
            [:img.keep-aligned-section-screenshot.screenshot-2.big-web-tablet-only
              {:src (cdn "/img/ML/homepage_screenshots_second_row.png")
               :srcSet (str (cdn "/img/ML/homepage_screenshots_second_row@2x.png") " 2x")}]
            [:img.keep-aligned-section-screenshot.screenshot-2.mobile-only
              {:src (cdn "/img/ML/homepage_screenshots_second_row_mobile.png")
               :srcSet (str (cdn "/img/ML/homepage_screenshots_second_row_mobile@2x.png") " 2x")}]]]]]])

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
      "Create your team. It’s free!"]])

(defn dashed-string [num & [responsive-class]]
  [:div.dashed-string
    {:class (str "dashed-string-" num " " responsive-class)}])

(defn testimonial-block [slug & [responsive-class]]
  (let [testimonial-copy (cond
                         (= slug :ifttt)
                         (str
                          "\"Carrot helps us communicate efficiently across time zones. "
                          "It minimizes FOMO from missed Slack conversations, and cuts out "
                          "the \"Did you see my message?\" nagging.\"")
                         (= slug :blend-labs)
                         (str
                          "“Carrot is a perfect compliment for Slack. We use it for longer-form "
                          "weekly updates no one should miss.”")
                         (= slug :bank-novo)
                         (str
                          "“Carrot keeps everyone across our global offices up to date. It "
                          "helps us share big wins, and key information across our growing family.”"))
        footer-copy (cond
                      (= slug :ifttt)
                      "Kevin Ebaugh, Senior Platform Community Manager"
                      (= slug :blend-labs)
                      "Sara Vienna, Head of Design"
                      (= slug :bank-novo)
                      "Tyler McIntyre, CEO")
        testimonial-website (cond
                             (= slug :ifttt)
                             "https://ifttt.com/"
                             (= slug :blend-labs)
                             "https://bl3ndlabs.com/"
                             (= slug :bank-novo)
                             "https://banknovo.com/")
        testimonial-company (cond
                             (= slug :ifttt)
                             "IFTTT"
                             (= slug :blend-labs)
                             "Bl3NDlabs"
                             (= slug :bank-novo)
                             "Banknovo.com")]
    [:div.testimonials-block.group
      {:class (str (name slug) " " responsive-class)}
      [:div.testimonial-copy
        testimonial-copy]
      [:div.testimonial-copy-footer
        [:div.testimonial-author-pic]
        [:div.testimonial-copy-footer-right
          footer-copy
          [:a.testimonial-copy-link
            {:href testimonial-website
             :target "_blank"}
            testimonial-company]]]]))

(defn testimonials-screenshot-block [block & [responsive-class]]
  (let [header (cond
                (= block :thoughtful-communication)
                "Thoughtful communication"
                (= block :conversation)
                "Clear, organized discussions"
                (= block :analytics)
                "Know who saw your update"
                (= block :follow-ups)
                "Request a follow-up"
                (= block :stay-in-sync)
                "Daily digest to stay in sync"
                (= block :share-to-slack)
                "Auto-share posts to Slack")
        subline (cond
                 (= block :thoughtful-communication)
                 "Space to write longer updates that convey more information"
                 (= block :conversation)
                 (str
                  "Threaded comments make it easy for your team to stay engaged "
                  "asynchronously. Ideal for remote teams.")
                 (= block :analytics)
                 "Carrot works in the background to make sure everyone sees what matters"
                 (= block :follow-ups)
                 "When you need a reply or feedback from your team, Carrot makes sure they'll follow up."
                 (= block :stay-in-sync)
                 "Everyone gets a daily, personalized summary of what's important."
                 (= block :share-to-slack)
                 "Your Carrot posts are automatically shared to the right Slack #channel")
        screenshot-num (cond
                        (= block :thoughtful-communication)
                        1
                        (= block :conversation)
                        2
                        (= block :analytics)
                        3
                        (= block :follow-ups)
                        4
                        (= block :stay-in-sync)
                        2
                        (= block :share-to-slack)
                        6)]
    [:div.testimonials-screenshot-block
      {:class responsive-class}
      [:div.testimonials-screenshot-header
        header]
      [:div.testimonials-screenshot-subheader
        subline]
      [:img.testimonials-screenshot.mobile-only
        {:src (cdn (str "/img/ML/testimonials_screenshot_" screenshot-num ".png"))
         :srcSet (str
                  (cdn (str "/img/ML/testimonials_screenshot_mobile_" screenshot-num "@2x.png")) " 2x, "
                  (cdn (str "/img/ML/testimonials_screenshot_mobile_" screenshot-num "@3x.png")) " 3x, "
                  (cdn (str "/img/ML/testimonials_screenshot_mobile_" screenshot-num "@4x.png")) " 4x")}]
      [:img.testimonials-screenshot.big-web-tablet-only
        {:src (cdn (str "/img/ML/testimonials_screenshot_" screenshot-num ".png"))
         :srcSet (str
                  (cdn (str "/img/ML/testimonials_screenshot_" screenshot-num "@2x.png")) " 2x, "
                  (cdn (str "/img/ML/testimonials_screenshot_" screenshot-num "@3x.png")) " 3x, "
                  (cdn (str "/img/ML/testimonials_screenshot_" screenshot-num "@4x.png")) " 4x")}]]))

(def testimonials-section
  [:section.testimonials
    (dashed-string 1)
    (testimonial-block :ifttt "big-web-tablet-only")
    (testimonials-screenshot-block :thoughtful-communication "mobile-only")
    (dashed-string 2)
    (testimonials-screenshot-block :thoughtful-communication "big-web-tablet-only")
    (testimonial-block :ifttt "mobile-only")
    (dashed-string 3)
    (testimonial-block :blend-labs "big-web-tablet-only")
    (testimonials-screenshot-block :conversation "mobile-only")
    (dashed-string 4)
    [:div.testimonials-floated-block.big-web-tablet-only
      [:div.testimonials-floated-block-inner.left-block.group
        [:img.testimonials-floated-screenshot
          {:src (cdn "/img/ML/testimonials_floated_screenshot_1.png")
           :srcSet (str (cdn "/img/ML/testimonials_floated_screenshot_1@2x.png") " 2x")}]
        [:div.testimonials-floated-copy
          [:div.testimonials-floated-header
            "Clear, organized discussions"]
          [:div.testimonials-floated-subheader
            "Threaded comments make it easy for your team to stay engaged asynchronously. Ideal for remote teams."]]]

      [:div.testimonials-floated-block-inner.right-block.group
        [:img.testimonials-floated-screenshot
          {:src (cdn "/img/ML/testimonials_floated_screenshot_2.png")
           :srcSet (str (cdn "/img/ML/testimonials_floated_screenshot_2@2x.png") " 2x")}]
        [:div.testimonials-floated-copy
          [:div.testimonials-floated-header
            "Request a follow-up"]
          [:div.testimonials-floated-subheader
            "When you need a reply or feedback from your team, Carrot makes sure they'll follow up."]]]

      [:div.testimonials-floated-block-inner.left-block.group
        [:img.testimonials-floated-screenshot
          {:src (cdn "/img/ML/testimonials_floated_screenshot_3.png")
           :srcSet (str (cdn "/img/ML/testimonials_floated_screenshot_3@2x.png") " 2x")}]
        [:div.testimonials-floated-copy
          [:div.testimonials-floated-header
            "Know who saw your update"]
          [:div.testimonials-floated-subheader
            "Carrot works in the background to make sure everyone sees what matters"]]]]
    (testimonials-screenshot-block :analytics "mobile-only")
    (dashed-string 5)
    [:div.testimonials-commgaps-block.big-web-tablet-only
      [:div.testimonals-commgaps-header
        "Close communication gaps"]
      [:div.testimonals-commgaps-subheader
        "Carrot makes sure everyone will see what matters."]
      [:div.testimonials.commgaps-block-inner.group
        [:div.testimonials-commgaps-column.left-column
          [:img.testimonials-commgaps-column-screenshot
            {:src (cdn "/img/ML/testimonials_commgaps_email.png")
             :srcSet (str (cdn "/img/ML/testimonials_commgaps_email@2x.png") " 2x")}]
          [:div.testimonials-commgaps-column-header
            "Daily digest to stay in sync"]
          [:div.testimonials-commgaps-column-subheader
            "Everyone gets a daily, personalized summary of what's important."]]
        [:div.testimonials-commgaps-column.right-column
          [:img.testimonials-commgaps-column-screenshot
            {:src (cdn "/img/ML/testimonials_commgaps_slack.png")
             :srcSet (str (cdn "/img/ML/testimonials_commgaps_slack@2x.png") " 2x")}]
          [:div.testimonials-commgaps-column-header
            "Auto-share posts to Slack"]
          [:div.testimonials-commgaps-column-subheader
            "Your Carrot posts are automatically shared to the right Slack #channel"]]]]
    (testimonial-block :blend-labs "mobile-only")
    (dashed-string 6)
    (testimonials-screenshot-block :follow-ups "mobile-only")
    (dashed-string 5 "mobile-only")
    (testimonials-screenshot-block :stay-in-sync "mobile-only")
    (dashed-string 2 "mobile-only")
    (testimonials-screenshot-block :share-to-slack "mobile-only")
    (dashed-string 3 "mobile-only")])

(def pricing-table
  [:div.pricing-table.group
    [:div.pricing-table-left
      [:div.pricing-table-left-price
        "$5"]
      [:div.pricing-table-left-subprice
        "/month per user"]]
    [:div.pricing-table-right.group
      [:div.pricing-table-right-copy
        (str
         "Carrot starts with a 14-day free trial, no credit card required. "
         "After that, monthly pricing starts at $60, which includes your first "
         "12 members. Then it’s just $5 a user after that.")]
      [:a.pricing-table-right-link
        {:href "/sign-up"}
        "Start your 14 day free trial"]]])

(def pricing-table-footer
  [:div.pricing-header-footer
    [:div.pricing-header-footer-logo]
    [:div.pricing-header-footer-subheadline
      "Have a team of 250+? Learn more about our "
      [:a
        {:href "/pricing"}
        "Enterprise plan"]
      "."]])

(def pricing-footer
  [:section.pricing-footer

    [:h1.pricing-headline
      "Simple pricing"]

    [:div.pricing-subheadline
      "Questions? "
      [:a.chat-with-us
        {:class "intercom-chat-link"
         :href "mailto:zcwtlybw@carrot-test-28eb3360a1a3.intercom-mail.com"}
        "Chat with us about Carrot"]]

    pricing-table
    
    pricing-table-footer])

(def testimonials-section-old
  [:section.testimonials-section-old.big-web-tablet-only
    [:div.testimonials-cards-container.group
      [:div.testimonials-cards-inner.group
        ;; First column
        [:div.testimonals-cards-column.group
          [:div.testimonial-card.peak-support
            [:div.testimonial-quote
              (str
               "“Carrot fixed our issue of email overload. "
               "We use it for important company communications, "
               "shout- outs, and announcements so they won’t get "
               "buried in everyone’s inbox. It’s been a big win for us.”")]
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
                 "“Carrot helps me share things "
                 "the entire team needs to know "
                 "- instead of burying it somewhere "
                 "it won’t get noticed.”")]
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
               "“Carrot keeps our distributed teams "
               "informed about what matters most.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Edoardo Benedetto"]
              [:div.testimonial-role
                [:a
                  {:href "https://ovalmoney.com/"
                   :target "_blank"}
                  "Oval Money, Head of Design"]]]]
          [:div.testimonial-card.hellotickets
            [:div.testimonial-quote
              (str
               "“Staying aligned across two offices "
               "is hard. Slack feels too ‘light’ and "
               "crazy for important information; and "
               "no one wants to read weekly emails. With "
               "Carrot, we have a knowledge base of what's "
               "going on with the company that’s made it "
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
               "“On Carrot, my updates get noticed "
               "and get the team talking. I love that.”")]
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
               "“Carrot is where we communicate important "
               "information when we need everyone to see it "
               "- regardless of their time zone.”")]
            [:div.testimonial-footer.group
              [:div.testimonial-image]
              [:div.testimonial-name
                "Andrew Love"]
              [:div.testimonial-role
                [:a
                  {:href "https://partnerhero.com/"
                   :targe "_blank"}
                  "PartnerHero, Director of R&D"]]]]]
        ;; Fourth column
        [:div.testimonals-cards-column.group
          [:div.testimonial-card.novo
            [:div.testimonial-quote
              (str
               "“We use Carrot when we need to make sure everyone "
               "is on the same page across all our offices here "
               "and abroad. It helps us share BIG wins, and in a "
               "fast-growing startup keeping that family vibe, "
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
               "“Carrot is a perfect compliment for Slack. We "
               "use it for longer-form weekly updates no one "
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