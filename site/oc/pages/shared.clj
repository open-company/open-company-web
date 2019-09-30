(ns oc.pages.shared
  (:require [environ.core :refer (env)]))

(defn cdn [img-src]
  (str (when (env :oc-web-cdn-url) (str (env :oc-web-cdn-url) "/" (env :oc-deploy-key))) img-src))

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
                (= block :stay-in-sync-slack)
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
                 "Carrot works in the background to make sure everyone sees what matters."
                 (= block :follow-ups)
                 (str
                  "When you’re looking for a reply, or want to guarantee nobody misses your post, "
                  "request a follow-up. Carrot reminds anyone that misses it to improve engagement.")
                 (= block :stay-in-sync)
                 "Everyone gets a daily, personalized summary of what's important."
                 (= block :stay-in-sync-slack)
                 "Everyone gets a daily, personalized summary of what's important."
                 (= block :share-to-slack)
                 "Your Carrot posts are automatically shared to the right Slack #channel.")
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
                        5
                        (= block :stay-in-sync-slack)
                        7
                        (= block :share-to-slack)
                        6)]
    [:div.testimonials-screenshot-block
      {:class responsive-class}
      [:div.testimonials-screenshot-header
        header]
      [:div.testimonials-screenshot-subheader
        subline]
      [:img.testimonials-screenshot.mobile-only
        {:src (cdn (str "/img/ML/testimonials_screenshot_mobile_" screenshot-num ".png"))
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

(defn testimonials-section [page]
  [:section.testimonials

    (dashed-string 1)

    (testimonial-block :ifttt "big-web-tablet-only")
    (testimonials-screenshot-block :thoughtful-communication "mobile-only")

    (dashed-string 2)

    (testimonials-screenshot-block :thoughtful-communication "big-web-tablet-only")
    (testimonial-block :ifttt "mobile-only")

    (dashed-string 3)

    (if (= page :slack)
      (testimonials-screenshot-block :stay-in-sync-slack "mobile-only")
      (testimonials-screenshot-block :conversation "mobile-only"))

    (dashed-string 4 "mobile-only")

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
            (str
             "When you’re looking for a reply, or want to guarantee nobody misses your post, "
             "request a follow-up. Carrot reminds anyone that misses it to improve engagement.")]]]

      [:div.testimonials-floated-block-inner.left-block.group
        [:img.testimonials-floated-screenshot
          {:src (cdn "/img/ML/testimonials_floated_screenshot_3.png")
           :srcSet (str (cdn "/img/ML/testimonials_floated_screenshot_3@2x.png") " 2x")}]
        [:div.testimonials-floated-copy
          [:div.testimonials-floated-header
            "Know who saw your update"]
          [:div.testimonials-floated-subheader
            "Carrot works in the background to make sure everyone sees what matters."]]]]
    (if (= page :slack)
      (testimonials-screenshot-block :conversation "mobile-only")
      (testimonials-screenshot-block :analytics "mobile-only"))

    (dashed-string 5)

    [:div.testimonials-commgaps-block.big-web-tablet-only
      [:div.testimonals-commgaps-header
        "Close communication gaps"]
      [:div.testimonals-commgaps-subheader
        "Your team stays engaged and informed no matter where they are."]
      [:div.testimonials.commgaps-block-inner.group
        [:div.testimonials-commgaps-column.left-column
          (if (= page :slack)
            [:img.testimonials-commgaps-column-screenshot
              {:src (cdn "/img/ML/testimonials_commgaps_slack_digest.png")
               :srcSet (str (cdn "/img/ML/testimonials_commgaps_slack_digest@2x.png") " 2x")}]
            [:img.testimonials-commgaps-column-screenshot
              {:src (cdn "/img/ML/testimonials_commgaps_email.png")
               :srcSet (str (cdn "/img/ML/testimonials_commgaps_email@2x.png") " 2x")}])
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
            "Your Carrot posts are automatically shared to the right Slack #channel."]]]]
    (testimonial-block :blend-labs "mobile-only")

    (dashed-string 6)

    (testimonial-block :blend-labs "big-web-tablet-only")

    (dashed-string 1 "big-web-tablet-only")

    (if (= page :slack)
      (testimonials-screenshot-block :analytics "mobile-only")
      (testimonials-screenshot-block :follow-ups "mobile-only"))

    (dashed-string 5 "mobile-only")

    (if (= page :slack)
      (testimonials-screenshot-block :follow-ups "mobile-only")
      (testimonials-screenshot-block :stay-in-sync "mobile-only"))

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
      "Have a team of 250+? "
      [:a.chat-with-us
        {:class "intercom-chat-link"
         :href "mailto:zcwtlybw@carrot-test-28eb3360a1a3.intercom-mail.com"}
        "Let’s chat about our Enterprise plan."]]])

(def pricing-footer
  [:section.pricing-footer

    [:h1.pricing-headline
      "Simple pricing for remote team communication"]

    [:div.pricing-subheadline
      "Questions? "
      [:a.chat-with-us
        {:class "intercom-chat-link"
         :href "mailto:zcwtlybw@carrot-test-28eb3360a1a3.intercom-mail.com"}
        "Let's chat"]]

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

(def slack-hero-screenshot
  [:div.main-animation-container
    [:img.main-animation
      {:src (cdn "/img/ML/slack_screenshot.png")
       :srcSet (str
                (cdn "/img/ML/slack_screenshot@2x.png") " 2x, "
                (cdn "/img/ML/slack_screenshot@3x.png") " 3x, "
                (cdn "/img/ML/slack_screenshot@4x.png") " 4x")}]])