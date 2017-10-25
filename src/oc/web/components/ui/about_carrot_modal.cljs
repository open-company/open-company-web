(ns oc.web.components.ui.about-carrot-modal
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.mixins :as mixins]
            [oc.web.components.ui.all-caught-up :refer (all-caught-up)]))

(defn dismiss-modal []
  (dis/dispatch! [:about-carrot-modal-hide]))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 dismiss-modal))

(def news
  [{:created-at "2017-09-22T12:38:30.021Z"
    :title "New updates to Boards. Now faster and more beautiful than ever before"
    :body "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec rhoncus, lectus sed vehicula auctor, orci eros ultrices augue, vitae tincidunt sem turpis nec massa. Integer lacinia dignissim ante, et feugiat enim fringilla aliquet..."}
   {:created-at "2017-09-01T14:38:30.021Z"
    :title "Donec pellentesque sollicitudin turpis, non bibendum ligula viverra non. Praesent vehicula, nibh nec bibendum"
    :body "Aliquam eget porttitor ex. Nam pellentesque vitae nunc eget ultrices. Proin fermentum elit id tortor viverra aliquam. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nulla dolor ligula..."}])

(rum/defcs about-carrot-modal < rum/static
                                ;; Locals
                                (rum/local false ::dismiss)
                                ;; Mixins
                                mixins/no-scroll-mixin
                                mixins/first-render-mixin
  [s]
  [:div.about-carrot-modal-container
    {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(:first-render-done s)))
                              :appear (and (not @(::dismiss s)) @(:first-render-done s))})}
    [:div.modal-wrapper
      [:button.carrot-modal-close.mlb-reset
        {:on-click #(close-clicked s)}]
      [:div.about-carrot-modal
        [:div.carrot-logo]
        [:div.about-title
          "What’s New with Carrot"]
        [:div.about-links
          [:a.about-link
            {:href oc-urls/about
             :on-click #(do (utils/event-stop %) (dis/dispatch! [:about-carrot-modal-hide]) (router/nav! oc-urls/about))}
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
        [:div.news-list-container
          [:div.news-list
            (for [n news]
              [:div.news
                {:key (str "about-news-" (:created-at n))}
                [:div.news-date (utils/time-since (utils/js-date (:created-at n)))]
                [:div.news-title (:title n)]
                [:div.news-body (:body n)]])]
          (all-caught-up)]]]])