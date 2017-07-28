(ns oc.web.components.ui.entry-video-modal
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn dismiss-modal []
  (dis/dispatch! [:input [:entry-editing :media-video] false]))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 #(dismiss-modal)))

(def youtube-regexp "https?://[www\\.|m\\.]?youtube.com/watch/?\\?v=([a-zA-Z0-9_-]{11})")

; https://vimeo.com/223518754 https://vimeo.com/groups/asd/223518754 https://vimeo.com/channels/asd/223518754
(def vimeo-regexp "(http|https)?:\\/\\/(www\\.)?vimeo.com\\/(?:channels\\/(?:\\w+\\/)?|groups\\/([^\\/]*)\\/videos\\/|)(\\d+)(?:|\\/\\?)")

(defn get-video-data [url]
  (let [yr (js/RegExp youtube-regexp "ig")
        vr (js/RegExp vimeo-regexp "ig")
        y-groups (.exec yr url)
        v-groups (.exec vr url)]
    {:id (if (nth y-groups 1) (nth y-groups 1) (nth v-groups 4))
     :type (if (nth y-groups 1) :youtuve :vimeo)}))
  

(defn valid-video-url? [url]
  (let [trimmed-url (string/trim url)
        yr (js/RegExp youtube-regexp "ig")
        vr (js/RegExp vimeo-regexp "ig")]
    (when-not (empty? trimmed-url)
      (or (.match trimmed-url yr)
          (.match trimmed-url vr)))))

(rum/defcs entry-video-modal < (rum/local false ::first-render-done)
                               (rum/local false ::dismiss)
                               (rum/local "" ::video-url)
                               rum/reactive
                               (drv/drv :current-user-data)
                               (drv/drv :entry-editing)
                               {:did-mount (fn [s]
                                             ;; Add no-scroll to the body to avoid scrolling while showing this modal
                                             (dommy/add-class! (sel1 [:body]) :no-scroll)
                                             s)
                                :after-render (fn [s]
                                                (when (not @(::first-render-done s))
                                                  (reset! (::first-render-done s) true))
                                                s)
                                :will-unmount (fn [s]
                                                ;; Remove no-scroll class from the body tag
                                                (dommy/remove-class! (sel1 [:body]) :no-scroll)
                                                s)}
  [s]
  (let [current-user-data (drv/react s :current-user-data)
        entry-editing (drv/react s :entry-editing)]
    [:div.entry-video-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(::first-render-done s)))
       :appear (and (not @(::dismiss s)) @(::first-render-done s))})}
      [:div.entry-video-modal
        [:div.entry-video-modal-header.group
          (user-avatar-image current-user-data)
          [:div.title "Adding a video"]]
        [:div.entry-video-modal-divider]
        [:div.entry-video-modal-content
          [:div.content-title "VIDEO LINK"]
          [:input
            {:type "text"
             :value @(::video-url s)
             :on-change #(reset! (::video-url s) (.. % -target -value))
             :placeholder "Link from YouTube or Vimeo"}]]
        [:div.entry-video-modal-buttons.group
          [:button.mlb-reset.mlb-default
            {:on-click #(when (valid-video-url? @(::video-url s))
                          (let [video-data (get-video-data @(::video-url s))]
                            (dis/dispatch! [:input [:entry-editing :temp-video] video-data]))
                          (close-clicked s))
             :disabled (not (valid-video-url? @(::video-url s)))}
            "Add"]
          [:button.mlb-reset.mlb-link-black
            {:on-click #(close-clicked s)}
            "Cancel"]]]]))