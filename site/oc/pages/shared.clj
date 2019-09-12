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
      "Growing and distributed teams around the world use Carrot."]
    [:div.homepage-testimonials-logo.logo-ifttt]
    [:div.homepage-testimonials-logo.logo-hopper]
    [:div.homepage-testimonials-logo.logo-hinge]
    [:div.homepage-testimonials-logo.logo-resy]
    [:div.homepage-testimonials-logo.logo-flyt]
    [:div.homepage-testimonials-logo.logo-oval]
    [:div.homepage-testimonials-logo.logo-novo]])

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

(def dashed-string
  [:div.dashed-string])

(defn testimonial-block [slug]
  (let [testimonial-copy (cond
                         (= slug :ifttt)
                         (str
                          "Carrot helps us communicate more efficiently across multiple "
                          "time zones and working hours. It minimizes the FOMO that can "
                          "result from missed Slack conversations, and cuts out the ‘Did "
                          "you see my message?’ nagging.")
                         (= slug :blend-labs)
                         (str
                          "Carrot is a perfect compliment for Slack. We use it for "
                          "longer-form weekly updates no one should miss.")
                         (= slug :novo)
                         (str
                          "Carrot keeps everyone across our global offices up to date. "
                          "It helps us share big wins, and key information across our growing family.")
                         (= slug :hello-tickets)
                         "Love being able to quickly see who read my post and when.")
        footer-copy (cond
                      (= slug :ifttt)
                      "— Kevin Ebaugh, Senior Platform Community Manager at "
                      (= slug :blend-labs)
                      "— Sara Vienna, Head of Design at "
                      (= slug :novo)
                      "— Tyler McIntyre, CEO at "
                      (= slug :hello-tickets)
                      "— Alberto Martinez, CEO at ")
        testimonial-website (cond
                             (= slug :ifttt)
                             "https://ifttt.com/"
                             (= slug :blend-labs)
                             "https://bl3ndlabs.com/"
                             (= slug :novo)
                             "https://banknovo.com/"
                             (= slug :hello-tickets)
                             "https://hellotickets.com/")
        testimonial-company (cond
                             (= slug :ifttt)
                             "IFTTT"
                             (= slug :blend-labs)
                             "Bl3NDlabs"
                             (= slug :novo)
                             "Novobank"
                             (= slug :hello-tickets)
                             "Hello Tickets")]
    [:div.testimonials-block.group
      {:class (name slug)}
      [:div.testimonial-author-pic]
      [:div.testimonial-copy-bubble
        [:div.testimonial-copy
          testimonial-copy]
        [:div.testimonial-copy-footer
          footer-copy
          [:a.testimonial-copy-link
            {:href testimonial-website
             :target "_blank"}
            testimonial-company]]]]))

(defn- testimonial-carousel [slug]
  (let [images-prefix (cond
                        (= slug :orange)
                        "1"
                        (= slug :blue)
                        "2"
                        (= slug :purple)
                        "3")
        headline (cond
                  (= slug :orange)
                  "Share what matters"
                  (= slug :blue)
                  "Organized, topic-based discussions"
                  (= slug :purple)
                  "Close communication gaps")
        subheadline (cond
                     (= slug :orange)
                     "Thoughtful communication should never be lost in the noise."
                     (= slug :blue)
                     "Bring your team closer together despite location and timezone differences."
                     (= slug :purple)
                     "Make sure your team knows what matters most.")
        footer-copy-1 (cond
                       (= slug :orange)
                       "Longer-form posts"
                       (= slug :blue)
                       "Reply later"
                       (= slug :purple)
                       "Analytics")
        footer-copy-2 (cond
                       (= slug :orange)
                       "At-a-glance summary"
                       (= slug :blue)
                       "Threads"
                       (= slug :purple)
                       "Daily digest")
        footer-copy-3 (cond
                       (= slug :orange)
                       "Follow-ups"
                       (= slug :blue)
                       "Stay current"
                       (= slug :purple)
                       "Integrations")]
    [:div.testimonial-carousel-block
      {:class (name slug)}
      [:div.testimonial-carousel-headline
        headline]
      [:div.testimonial-carousel-subheadline
        subheadline]
      [:div.testimonial-carousel-images
        [:div.testimonial-carousel-images-inner
          [:img.testimonial-carousel-image.image-1
            {:src (cdn (str "/img/ML/testimonial_carousel_" images-prefix "_1.png"))
             :srcSet (str (cdn (str "/img/ML/testimonial_carousel_" images-prefix "_1@2x.png")) " 2x")}]
          [:img.testimonial-carousel-image.image-2
            {:src (cdn (str "/img/ML/testimonial_carousel_" images-prefix "_2.png"))
             :srcSet (str (cdn (str "/img/ML/testimonial_carousel_" images-prefix "_2@2x.png")) " 2x")}]
          [:img.testimonial-carousel-image.image-3
            {:src (cdn (str "/img/ML/testimonial_carousel_" images-prefix "_3.png"))
             :srcSet (str (cdn (str "/img/ML/testimonial_carousel_" images-prefix "_3@2x.png")) " 2x")}]]]
      [:div.testimonial-carousel-footers.group
        [:div.testimonial-carousel-footer.footer-1
          {:class (name slug)
           :onClick (str "OCCarousel.carouselClicked(\"" (name slug) "\", \"1\");")
           :onMouseLeave (str "OCCarousel.maybeRestartTimeout(\"" (name slug) "\", \"1\");")}
          [:div.testimonial-carousel-footer-progress]
          [:div.testimonial-carousel-footer-block
            [:div.testimonial-carousel-footer-icon]
            [:div.testimonial-carousel-footer-copy
              footer-copy-1]]]
        [:div.testimonial-carousel-footer.footer-2
          {:class (name slug)
           :onClick (str "OCCarousel.carouselClicked(\"" (name slug) "\", \"2\");")
           :onMouseLeave (str "OCCarousel.maybeRestartTimeout(\"" (name slug) "\", \"2\");")}
          [:div.testimonial-carousel-footer-progress]
          [:div.testimonial-carousel-footer-block
            [:div.testimonial-carousel-footer-icon]
            [:div.testimonial-carousel-footer-copy
              footer-copy-2]]]
        [:div.testimonial-carousel-footer.footer-3
          {:class (name slug)
           :onClick (str "OCCarousel.carouselClicked(\"" (name slug) "\", \"3\");")
           :onMouseLeave (str "OCCarousel.maybeRestartTimeout(\"" (name slug) "\", \"3\");")}
          [:div.testimonial-carousel-footer-progress]
          [:div.testimonial-carousel-footer-block
            [:div.testimonial-carousel-footer-icon]
            [:div.testimonial-carousel-footer-copy
              footer-copy-3]]]]]))

(def testimonials-section
  [:section.testimonials
    dashed-string
    (testimonial-block :ifttt)
    dashed-string
    (testimonial-carousel :orange)
    dashed-string
    (testimonial-block :blend-labs)
    dashed-string
    (testimonial-carousel :blue)
    dashed-string
    (testimonial-block :novo)
    dashed-string
    (testimonial-carousel :purple)
    dashed-string
    (testimonial-block :hello-tickets)
    dashed-string])

(def pricing-table-section
  [:section.pricing-header

    [:h1.pricing-headline
      "Free for unlimited users"]

    [:div.pricing-subheadline
      "Carrot is open source and always free to use. Questions? "
      [:a.chat-with-us
        {:class "intercom-chat-link"
         :href "mailto:zcwtlybw@carrot-test-28eb3360a1a3.intercom-mail.com"}
        "Chat with us!"]]

    [:div.pricing-three-columns.group
      ;; Free
      [:div.pricing-column.free-column
        [:h2.tear-title
          "Free"]
        [:div.tear-subtitle
          "Perfect for any size team"]
        [:h3.tear-price
          "$0"]
        [:div.tear-price-billing
          "FREE for unlimited users"]
        [:a.tear-start-bt
          {:href "/sign-up"}
          "Create your team"]
        [:div.tear-feature
          [:span "Unlimited users"]]
        [:div.tear-feature-separator]
        [:div.tear-feature
          [:span "Unlimited posts"]]
        [:div.tear-feature-separator]
        [:div.tear-feature
          [:span "2 GB storage"]]]
      ;; Premium
      [:div.pricing-column.team-column
        [:h2.tear-title
          "Premium"]
        [:div.tear-subtitle
          "Advanced permissions and analytics to boost engagement"]
        [:h3.tear-price
          "$4"]
        [:div.tear-price-billing
          "Per user, per month, billed annually"]
        [:a.tear-start-bt
          {:href "/sign-up"}
          "Create your team"]
        [:div.tear-feature-summary
          "Everything in FREE plus:"]
        [:div.tear-feature-separator]
        [:div.tear-feature
          {:data-toggle "tooltip"
           :data-placement "top"
           :title "Advanced permissions"}
          [:span "Advanced permissions"]]
        [:div.tear-feature-separator]
        [:div.tear-feature
          {:data-toggle "tooltip"
           :data-placement "top"
           :title "Follow-ups"}
          [:span "Follow-ups"]]
        [:div.tear-feature-separator]
        [:div.tear-feature
          {:data-toggle "tooltip"
           :data-placement "top"
           :title "Analytics"}
          [:span "Analytics"]]
        [:div.tear-feature-separator]
        [:div.tear-feature
          [:span "Scheduling"]
          [:span.soon "SOON"]]
        [:div.tear-feature-separator]
        [:div.tear-feature
          [:span "Free fuest users"]
          [:span.soon "SOON"]]
        [:div.tear-feature-separator]
        [:div.tear-feature
          [:span "Unlimited storage"]]
        [:div.tear-feature-separator]
        [:div.tear-feature
          [:span "Premium support"]]]
      ;; Enterprise
      [:div.pricing-column.enterprise-column
        [:h2.tear-title
          "Enterprise"]
        [:div.tear-subtitle
          "Tailored for corporate comms and HR departments"]
        [:div.tear-price]
        [:div.tear-price-billing
          "Ideal for teams 250+"]
        [:a.chat-with-us
          {:class "intercom-chat-link"
           :href "mailto:zcwtlybw@carrot-test-28eb3360a1a3.intercom-mail.com"}
          "Chat with us"]]]
    [:div.pricing-header-footer
      [:div.pricing-header-footer-headline
        "Non-profits and educational institutions save 50% on premium plans."]
      [:div.pricing-header-footer-subheadline
        (str
         "Prices shown are for annual billing. When billed month-to-month, "
         "the Premium plan is $5 per user per month.")]]])