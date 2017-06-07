(ns oc.web.components.trend-bar
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer (add-class! remove-class!) :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(def status-bar-values
  [:collapsed
   :expanded
   :trending])

(defn update-body-class [status]
  ; add the current trend class status to the body as a class in form of "trend-bar-{status}"
  (let [body (sel1 [:body])]
    (doseq [v status-bar-values]
      (if (= status v)
        (add-class! body (str "trend-bar-" (name v)))
        (remove-class! body (str "trend-bar-" (name v)))))))

(defn- scrolled [e s]
  ; Get the new scroll top value
  (let [trend-bar-status @(drv/get-ref s :trend-bar-status)
        next-scroll-top (or (.-pageYOffset js/window) (.. js/document -documentElement -scrollTop))]
      (cond
        ; if it's showing trend, there is enoug scroll to collapse and it's scrolling down
        (and (= trend-bar-status :trending)
             (>= (.-clientHeight (.-body js/document)) (+ (.-innerHeight js/window) 232))
             (> next-scroll-top @(::last-scroll-top s)))
        (do
          ; collapse the trendbar
          (dis/dispatch! [:trend-bar-status :collapsed])
          ; avoid to expand when the mouse move out of the trendbar
          (reset! (::hovering s) false))
        (and (= trend-bar-status :collapsed)
             (= next-scroll-top 0))
        ; if the scroll reaches the top of the page
        ; expand the trending bar
        (dis/dispatch! [:trend-bar-status :trending]))
    ; save the scroll top for the enxt scroll
    (reset! (::last-scroll-top s) next-scroll-top)))

(rum/defcs trend-bar < rum/static
                       rum/reactive
                       (drv/drv :trend-bar-status)
                       (rum/local false ::hovering)
                       (rum/local 0 ::last-scroll-top)
                       (rum/local nil ::scroll-listener)
                       {:did-mount (fn [s]
                                     ; start listenings for scrolling events
                                     (let [scroll-listener (events/listen js/window EventType/SCROLL #(scrolled % s))]
                                       (reset! (::scroll-listener s) scroll-listener))
                                     s)
                        :will-unmount (fn [s]
                                        ; remove the scroll listener when the components in unmounted
                                        (events/unlistenByKey @(::scroll-listener s))
                                        s)
                        :after-render (fn [s]
                                        ; update the body classes after the each render
                                        (update-body-class @(drv/get-ref s :trend-bar-status))
                                        s)}
  [s org-name]
  (let [trend-bar-status (drv/react s :trend-bar-status)]
    [:div.trend-bar.group
      {:class (name trend-bar-status)
       :on-mouse-over #(when (= trend-bar-status :collapsed)
                         (reset! (::hovering s) true)
                         (dis/dispatch! [:trend-bar-status :expanded]))
       :on-mouse-leave #(when @(::hovering s)
                        (reset! (::hovering s) false)
                        (dis/dispatch! [:trend-bar-status :collapsed]))}
      [:div.trend-bar-orange-box
        [:button.mlb-reset.toggle-trend-bt
          {:on-click #(do
                        (reset! (::hovering s) false)
                        (dis/dispatch! [:trend-bar-status (if (not= trend-bar-status :trending) :trending :expanded)]))}
          (if (not= trend-bar-status :trending) "Expand" "Hide")]
        [:div.trending-org
          (str "Trending in " org-name)]
        (let [card-number 5]
          [:div.trend-bar-cards
            [:div.trend-bar-cards-inner
              {:style {:width (str (+ (* 278 card-number) (* 16 (dec card-number))) "px")}}
              (for [c (range 5)]
                [:div.card
                  {:key (str "card-" (rand 5))}])]])]]))
