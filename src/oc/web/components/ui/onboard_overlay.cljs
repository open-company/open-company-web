(ns oc.web.components.ui.onboard-overlay
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

(defn dismiss-modal []
  (dis/dispatch! [:onboard-overlay-hide]))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 dismiss-modal))

(rum/defcs onboard-overlay < (rum/local 1 ::step)
                             (rum/local false ::remove-no-scroll)
                             (rum/local false ::first-render-done)
                             (rum/local false ::dismiss)
                             {:did-mount (fn [s]
                                            ;; Add no-scroll to the body if it doesn't has it already
                                            ;; to avoid scrolling while showing this modal
                                            (let [body (sel1 [:body])]
                                              (when-not (dommy/has-class? body :no-scroll)
                                                (reset! (::remove-no-scroll s) true)
                                                (dommy/add-class! (sel1 [:body]) :no-scroll)))
                                            s)
                              :after-render (fn [s]
                                              (when (not @(::first-render-done s))
                                                (reset! (::first-render-done s) true))
                                              s)
                              :will-unmount (fn [s]
                                              ;; Remove no-scroll class from the body tag
                                              ;; if it wasn't already there
                                              (when @(::remove-no-scroll s)
                                                (dommy/remove-class! (sel1 [:body]) :no-scroll))
                                              s)}
  [s]
  [:div.onboard-overlay-container
    {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(::first-render-done s)))
                              :appear (and (not @(::dismiss s)) @(::first-render-done s))})}
    [:div.onboard-overlay
      (case @(::step s)
        1
        [:div.onboard-overlay-step.step-1
          [:img.step-illustration
            {:src (utils/cdn "/img/carrot_logo.png")
             :height 101}]
          [:div.onboard-overlay-step-title
            "Wellcome to Carrot"]
          [:div.onboard-overlay-step-description
            "Where companies find alignment"]]
        2
        [:div.onboard-overlay-step.step-2
          [:div.empty-line]
          [:div.onboard-overlay-step-title
            "Organize topics with boards"]
          [:img.step-illustration
            {:src (utils/cdn "/img/ML/onboard_overlay_il_2.png")
             :width 389
             :height 150}]
          [:div.onboard-overlay-step-description
            "Boards keep everything organized: Boards keep everyone up to date on related topics."]]
        3
        [:div.onboard-overlay-step.step-3
          [:div.empty-line]
          [:div.onboard-overlay-step-title
            "See what's hot"]
          [:img.step-illustration
            {:src (utils/cdn "/img/ML/onboard_overlay_il_3.png")
             :width 404
             :height 181}]
          [:div.onboard-overlay-step-description
            "Check out what’s trending to get an at a glance view of the hottest topics in your company"]]
        4
        [:div.onboard-overlay-step.step-4
          [:div.empty-line]
          [:div.onboard-overlay-step-title
            "Gather, create, and reflect, with stories"]
          [:img.step-illustration
            {:src (utils/cdn "/img/ML/onboard_overlay_il_4.png")
             :width 403
             :height 164}]
          [:div.onboard-overlay-step-description
            "Gather, create and reflect, with Stories: Stories are great for regular updates that cover multiple topics."]])
      [:div.onboard-overlay-footer
        [:div.onboard-overlay-footer-left
          [:button.mlb-reset.skip-button
            {:on-click #(do (utils/event-stop %) (close-clicked s))}
            "Skip"]]
        [:div.onboard-overlay-footer-steps
          [:div.dot-step
            {:class (when (= @(::step s) 1) "active")}]
          [:div.dot-step
            {:class (when (= @(::step s) 2) "active")}]
          [:div.dot-step
            {:class (when (= @(::step s) 3) "active")}]
          [:div.dot-step
            {:class (when (= @(::step s) 4) "active")}]]
        [:div.onboard-overlay-footer-right
          [:button.mlb-reset.mlb-default.next-button
            {:on-click #(if (< @(::step s) 4)
                          (reset! (::step s) (inc @(::step s)))
                          (close-clicked s))}
            (if (= @(::step s) 4)
              "Let's go! "
              "Next ")
            [:img
              {:src (utils/cdn "/img/ML/next_arrow.png")
               :width 20
               :height 20}]]]]]])