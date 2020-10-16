(ns oc.pages.shared
  (:require [environ.core :refer (env)]))

(defn cdn [img-src]
  (str (when (env :oc-web-cdn-url) (str (env :oc-web-cdn-url) "/" (env :oc-deploy-key))) img-src))

(def testimonials-logos-line
  [:div.homepage-testimonials-container.group
    ; [:div.homepage-testimonials-copy
    ;   "Growing and distributed teams around the world ❤️ Carrot"]
    [:div.homepage-testimonials-logos
      ; [:div.homepage-testimonials-logo.logo-ifttt]
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
                         (= slug :hopper)
                         (str
                          "“We use Carrot to make sure team updates and announcements don't get "
                          "lost in Slack conversations. It keeps our remote teams in sync.”")
                         (= slug :blend-labs)
                         (str
                          "“Carrot is a perfect compliment for Slack. We use it for longer-form "
                          "weekly updates no one should miss.”")
                         (= slug :bank-novo)
                         (str
                          "“Carrot keeps everyone across our global offices up to date. It "
                          "helps us share big wins and key information across our growing family.”"))
        footer-copy (cond
                      (= slug :hopper)
                      "Camilo Alvarez, Operations Lead"
                      (= slug :blend-labs)
                      "Sara Vienna, Head of Design"
                      (= slug :bank-novo)
                      "Tyler McIntyre, CEO")
        testimonial-website (cond
                             (= slug :hopper)
                             "https://hopper.com/"
                             (= slug :blend-labs)
                             "https://bl3ndlabs.com/"
                             (= slug :bank-novo)
                             "https://banknovo.com/")
        testimonial-company (cond
                             (= slug :hopper)
                             "Hopper.com"
                             (= slug :blend-labs)
                             "Bl3NDlabs.com"
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

(defn- block-data [block]
  (let [header (cond
                 (= block :stay-focused)
                 "Stay focused with less noise"
                 (= block :reduce-interruptions)
                 "Reduce interruptions"
                 (= block :analytics)
                 "Know who saw your update"
                 (= block :stay-in-sync)
                 "Daily newsletter to stay in sync"
                 (= block :stay-in-sync-slack)
                 "Daily digest to stay in sync"
                 (= block :share-to-slack)
                 "Auto-share posts to Slack")
        subline (cond
                  (= block :stay-focused)
                  "Personalize your news feed to filter out topics you don't care about. Less noise saves you time."
                  (= block :reduce-interruptions)
                  "Carrot batches updates together in a daily digest so it's easy to get caught up all at once — when it’s more convenient for you."
                  (= block :analytics)
                  "Curious if anyone heard you? It’s easy to see who’s on the same page, and easy to remind someone that missed it, too."
                  (= block :stay-in-sync)
                 "Everyone gets a daily, personalized summary of what's important."
                 (= block :stay-in-sync-slack)
                 "Everyone gets a daily, personalized summary of what's important."
                 (= block :share-to-slack)
                 "Your Carrot posts are automatically shared to the right Slack #channel.")
        screenshot-num (case block
                         :stay-focused         1
                         :reduce-interruptions 2
                         :analytics            3
                         :stay-in-sync         5
                         :share-to-slack       6
                         :stay-in-sync-slack   7)
        block-float (case block
                      (:stay-focused :analytics :share-to-slack)                :left-block
                      (:reduce-interruptions :stay-in-sync :stay-in-sync-slack) :right-block)]
    [header subline screenshot-num block-float]))

(defn- testimonials-desktop-block [block]
  (let [[header subline screenshot-num block-float] (block-data block)]
    [:div.testimonials-floated-block-inner.group
     {:class (name block-float)}
     [:img.testimonials-floated-screenshot
      {:src (cdn (str "/img/ML/testimonials_screenshot_" screenshot-num ".png"))
       :srcSet (str
                (cdn (str "/img/ML/testimonials_screenshot_" screenshot-num "@2x.png")) " 2x, "
                (cdn (str "/img/ML/testimonials_screenshot_" screenshot-num "@3x.png")) " 3x")}]
     [:div.testimonials-floated-copy
      [:div.testimonials-floated-header
       header]
      [:div.testimonials-floated-subheader
       subline]]]))

(defn testimonials-desktop-blocks []
  [:div.testimonials-floated-block.big-web-tablet-only
   (testimonials-desktop-block :stay-focused)

   (testimonials-desktop-block :reduce-interruptions)

   (testimonials-desktop-block :analytics)])

(defn testimonials-mobile-block [block & [responsive-class]]
  (let [[header subline screenshot-num] (block-data block)]
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
                  (cdn (str "/img/ML/testimonials_screenshot_mobile_" screenshot-num "@3x.png")) " 3x")}]
      [:img.testimonials-screenshot.big-web-tablet-only
        {:src (cdn (str "/img/ML/testimonials_screenshot_" screenshot-num ".png"))
         :srcSet (str
                  (cdn (str "/img/ML/testimonials_screenshot_" screenshot-num "@2x.png")) " 2x, "
                  (cdn (str "/img/ML/testimonials_screenshot_" screenshot-num "@3x.png")) " 3x")}]]))

(defn close-communication-gaps
  [active-page]
  [:div.testimonials-commgaps-block.big-web-tablet-only
   [:div.testimonals-commgaps-header
    "Close communication gaps"]
   [:div.testimonals-commgaps-subheader
    "Automatically share posts to Slack and email to increase coverage."]
   [:div.testimonials.commgaps-block-inner.group
    [:div.testimonials-commgaps-column.left-column
     (if (= active-page :slack)
       [:img.testimonials-commgaps-column-screenshot
        {:src (cdn "/img/ML/testimonials_commgaps_slack_digest.png")
         :srcSet (str (cdn "/img/ML/testimonials_commgaps_slack_digest@2x.png") " 2x, "
                      (cdn "/img/ML/testimonials_commgaps_slack_digest@3x.png") " 3x, "
                      (cdn "/img/ML/testimonials_commgaps_slack_digest@4x.png") " 4x")}]
       [:img.testimonials-commgaps-column-screenshot
        {:src (cdn "/img/ML/testimonials_commgaps_email.png")
         :srcSet (str (cdn "/img/ML/testimonials_commgaps_email@2x.png") " 2x, "
                      (cdn "/img/ML/testimonials_commgaps_email@3x.png") " 3x, "
                      (cdn "/img/ML/testimonials_commgaps_email@4x.png") " 4x")}])
     [:div.testimonials-commgaps-column-header
      "Daily newsletter to stay in sync"]
     [:div.testimonials-commgaps-column-subheader
      "Everyone gets a daily, personalized summary of what's important."]]
    [:div.testimonials-commgaps-column.right-column
     [:img.testimonials-commgaps-column-screenshot
      {:src (cdn "/img/ML/testimonials_commgaps_slack.png")
       :srcSet (str (cdn "/img/ML/testimonials_commgaps_slack@2x.png") " 2x, "
                    (cdn "/img/ML/testimonials_commgaps_slack@3x.png") " 3x, "
                    (cdn "/img/ML/testimonials_commgaps_slack@4x.png") " 4x")}]
     [:div.testimonials-commgaps-column-header
      "Auto-share posts to Slack"]
     [:div.testimonials-commgaps-column-subheader
      "Your Carrot posts are automatically shared to the right Slack #channel."]]]])

(defn testimonials-section [page]
  [:section.testimonials

   (dashed-string 1)

   (testimonial-block :hopper)

   (dashed-string 2)

   (if (= page :slack)
     (testimonials-mobile-block :stay-in-sync-slack "mobile-only")
     (testimonials-mobile-block :stay-focused "mobile-only"))

   (dashed-string 3 "mobile-only")

   (testimonials-mobile-block :reduce-interruptions "mobile-only")

   (dashed-string 4 "mobile-only")

   (testimonials-mobile-block :analytics "mobile-only")

   (testimonials-desktop-blocks)

   (dashed-string 5)

   (close-communication-gaps page)

   (when-not (= page :slack)
     (testimonials-mobile-block :stay-in-sync "mobile-only"))

   (when-not (= page :slack)
     (dashed-string 1 "mobile-only"))

   (testimonials-mobile-block :share-to-slack "mobile-only")

   (dashed-string 6)

  ;;  (testimonial-block :blend-labs)

  ;;  (dashed-string 1)
   ])

(def pricing-table
  [:div.pricing-table.group
   ;; Free
   [:div.pricing-table-column
    [:div.pricing-table-column-inner
     [:div.pricing-column-header
      "Free"]
     [:div.pricing-column-value
      [:div.price-value
       "$0"]
      [:div.price-desc
       "per user"
       [:br]
       "per month"]]
     [:a.pricing-column-right-link
      {:href "/sign-up"}
      "Try for free"]
     [:div.pricing-column-row
      "Unlimited users"]
     [:div.pricing-column-row
      "Unlimited posts"]
     [:div.pricing-column-row
      "Team-level access to all updates"]
     [:div.pricing-column-row
      "Everyone can add an update"]]
    [:div.pricing-column-footer-note.big-web-tablet-only
     "Nonprofits and K-12 education are always free."]]
   ;; Premium
   [:div.pricing-table-column
    [:div.pricing-table-column-inner
     [:div.pricing-column-header
      "Premium"]
     [:div.pricing-column-value
      [:div.price-value
       "$5"]
      [:div.price-desc
       "per user"
       [:br]
       "per month"]]
     [:a.pricing-column-right-link
      {:href "/sign-up"}
      "Try for free"]
     [:div.pricing-column-row
      "Unlimited users"]
     [:div.pricing-column-row
      "Unlimited posts"]
     [:div.pricing-column-row.has-tooltip
      [:span.pricing-column-copy
       {:title "Create updates that are private for select users, or public for people outside the team."
        :data-toggle "tooltip"
        :data-placement "top"}
       "Team, private and public updates"]]
     [:div.pricing-column-row.has-tooltip
      [:span.pricing-column-copy
       {:title "In the free version, everyone on the team can add updates. With premium, admins can assign editor and view-only roles."
        :data-toggle "tooltip"
        :data-placement "top"}
       "View-only users"]]
     [:div.pricing-column-row.has-tooltip
      [:span.pricing-column-copy
       {:title "In the free version, everyone receives a morning digest at 7AM. With premium, users can add periodic digests at noon and 5PM to stay in sync through the day."
        :data-toggle "tooltip"
        :data-placement "top"}
       "Custom digest schedule"]]
     [:div.pricing-column-row.has-tooltip
      [:span.pricing-column-copy
       {:title "Know who saw your update, and easily remind those that missed it."
        :data-toggle "tooltip"
        :data-placement "top"}
       "Analytics"]]
     [:div.pricing-column-row
      "Custom colors and branding"]
    ;;  [:div.pricing-column-row.coming-feature
    ;;   "Assign roles for team onboarding (coming)"]
     ]]
   [:div.pricing-column-footer-note.mobile-only
    "Nonprofits and K-12 education are always free."]])

(def pricing-table-footer
  [:div.pricing-header-footer
    [:div.pricing-header-footer-logo]
    [:div.pricing-header-footer-subheadline
      "Have a team of 250+? "
      [:a.chat-with-us
        {:class "intercom-chat-link"
         :href "mailto:hello@carrot.io"}
        "Let’s chat about our Enterprise plan."]]])

(def pricing-headline "Open source and free")

(def pricing-chat
  [:div.pricing-subheadline
    "Questions? "
    [:a.chat-with-us
      {:class "intercom-chat-link"
       :href "mailto:hello@carrot.io"}
      "Let's chat"]])

(def pricing-footer
  [:section.pricing-footer

    [:h1.pricing-headline
      pricing-headline]    

    pricing-table
    
    ; pricing-table-footer

    pricing-chat])

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
               "and abroad. It helps us share BIG wins and in a "
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
                (cdn "/img/ML/slack_screenshot@3x.png") " 3x")}]])