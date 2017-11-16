(ns oc.web.components.ui.whats-new-modal
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.components.ui.all-caught-up :refer (all-caught-up)]))

(defn dismiss-modal []
  (dis/dispatch! [:whats-new-modal-hide]))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 dismiss-modal))

(rum/defcs whats-new-modal < rum/static
                             rum/reactive
                             (drv/drv :whats-new-data)
                             ;; Locals
                             (rum/local false ::dismiss)
                             ;; Mixins
                             mixins/no-scroll-mixin
                             mixins/first-render-mixin
  [s]

  (let [whats-new-data (drv/react s :whats-new-data)]

    [:div.whats-new-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(:first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(:first-render-done s))})}
      [:div.modal-wrapper
        [:button.carrot-modal-close.mlb-reset
          {:on-click #(close-clicked s)}]
        [:div.whats-new-modal
          [:div.carrot-logo]
          [:div.about-title
            (if (empty? whats-new-data)
              "About Carrot"
              "What’s New with Carrot")]
          [:div.about-links
            [:a.about-link
              {:href oc-urls/about
               :on-click #(do (utils/event-stop %) (router/nav! oc-urls/about))}
              "Company"]
            [:a.about-link
              {:href oc-urls/help}
              "Help"]
            [:a.about-link
              {:href oc-urls/contact-mail-to}
              "Contact"]
            [:a.about-link.twitter
              {:href oc-urls/oc-twitter}
              ""]
            [:a.about-link.medium
              {:href oc-urls/blog}
              ""]]
          (when (seq whats-new-data)
            [:div.news-list-container
              [:div.news-list
                  (let [sorted-whats-new (reverse (sort-by :published-at (vals whats-new-data)))]
                    (for [n sorted-whats-new
                          :let [self-link (utils/link-for (:links n) "self")
                                secure-url (oc-urls/secure-activity
                                            (utils/storage-url-org-slug (:href self-link)) (:secure-uuid n))]]
                      [:div.news
                        {:key (str "about-news-" (:uuid n))}
                        [:div.news-date
                          (utils/time-since (utils/js-date (:published-at n)))]
                          [:div.news-headline
                            {:data-secureurl secure-url
                             :dangerouslySetInnerHTML (utils/emojify (:headline n))}]
                        [:div.news-body
                          {:dangerouslySetInnerHTML (utils/emojify (:body n))}]]))]
              (all-caught-up)])]]]))