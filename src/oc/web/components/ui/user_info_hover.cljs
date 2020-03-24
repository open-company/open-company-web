(ns oc.web.components.ui.user-info-hover
  (:require [rum.core :as rum]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [oc.web.urls :as oc-urls]
            [oc.lib.user :as user-lib]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(def ^:private default-positioning {:vertical-position nil :horizontal-position nil})

(def ^:private popup-size
  {:width 296
   :height 96})

(def ^:private padding 24)

(def ^:private popup-offset
  {:x padding
   :y (+ padding responsive/navbar-height)})

(defn- check-hover [s parent-el]
  (let [viewport-size (dom-utils/viewport-size)
        pos (dom-utils/viewport-offset parent-el)
        vertical-position (if (> (- (:y pos) (:y popup-offset)) (:height popup-size))
                            :top
                            :bottom)
        ; vertical-offset (if (= vertical-position :top)
        ;                   (* (:height popup-size) -1)
        ;                   0)
        horizontal-offset (if (> (:x pos) (:x popup-offset))
                            0
                            (if (neg? (:x pos))
                              (+ (* (:x pos) -1) (:x popup-offset))
                              (- (:x popup-offset) (:x pos))))]
    {:vertical-position vertical-position
     ; :vertical-offset vertical-offset
     :horizontal-offset horizontal-offset}))

(defn- enter! [s parent-el]
  (reset! (::enter-timeout s) nil)
  (when (compare-and-set! (::hovering s) false true)
    (reset! (::positioning s) (check-hover s parent-el))))

(defn- leave! [s]
  (when (compare-and-set! (::hovering s) true false)
    (reset! (::positioning s) default-positioning)))

(defn- enter-ev [s parent-el]
  (when-not (-> s :rum/args first :disabled)
    (.clearTimeout js/window @(::enter-timeout s))
    (reset! (::enter-timeout s) (.setTimeout js/window #(enter! s parent-el) 500))))

(defn- leave-ev [s]
  (when-not (-> s :rum/args first :disabled)
    (.clearTimeout js/window @(::enter-timeout s))
    (leave! s)))

(defn- click [s]
  (nav-actions/show-user-info (-> s :rum/args first :user-data :user-id)))

(rum/defcs user-info-hover <
  rum/static
  (rum/local nil ::mouse-enter)
  (rum/local nil ::mouse-leave)
  (rum/local nil ::click)
  (rum/local default-positioning ::positioning)
  (rum/local false ::hovering)
  (rum/local nil ::enter-timeout)
  {:did-mount (fn [s]
   (let [el (rum/dom-node s)
         parent-el (.-parentElement el)]
    (when parent-el
      (if (responsive/is-mobile-size?)
        (reset! (::click s) (events/listen parent-el EventType/CLICK
         (partial click s)))
        (do
          (reset! (::mouse-enter s) (events/listen parent-el EventType/MOUSEENTER
           #(enter-ev s parent-el)))
          (reset! (::mouse-leave s) (events/listen parent-el EventType/MOUSELEAVE
           #(leave-ev s)))))))
   s)
   :will-unmount (fn [s]
    (when @(::mouse-enter s)
      (events/unlistenByKey @(::mouse-enter s))
      (reset! (::mouse-enter s) nil))
    (when @(::mouse-leave s)
      (events/unlistenByKey @(::mouse-leave s))
      (reset! (::mouse-leave s) nil))
    (when @(::click s)
      (events/unlistenByKey @(::click s))
      (reset! (::click s) nil))
    s)}
  [s {:keys [disabled user-data current-user-id]}]
  (let [my-profile? (= (:user-id user-data) current-user-id)
        pos @(::positioning s)]
    [:div.user-info-hover
      {:class (utils/class-set {:show @(::hovering s)
                                (:vertical-position pos) true
                                :left true})
       :style {:margin-left (str (:horizontal-offset pos) "px")
               ; :margin-top (str (:vertical-offset pos) "px")
               }}
      [:div.user-info-inner
        [:div.user-info-header
          (user-avatar-image user-data)
          [:div.user-info-title
          (user-lib/name-for user-data)]]
        [:div.user-info-buttons.group
          [:button.mlb-reset.profile-bt
            {:on-click #(nav-actions/show-user-info (:user-id user-data))}
            (if my-profile?
              "My profile"
              "Profile")]
          [:button.mlb-reset.posts-bt
            {:on-click #(nav-actions/nav-to-author! % (:user-id user-data) (oc-urls/contributor (:user-id user-data)))}
            (if my-profile?
              "View my posts"
              "View posts")]]]]))
