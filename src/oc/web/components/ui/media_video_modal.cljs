(ns oc.web.components.ui.media-video-modal
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :refer (first-render-mixin)]))

(defn dismiss-modal [s]
  (dis/dispatch! [:input [:media-input :media-video] :dismiss]))

(defn close-clicked [s & [no-dismiss]]
  (reset! (::dismiss s) true)
  (when-not no-dismiss
    (utils/after 180 #(dismiss-modal s))))

(def youtube-regexp
 "https?://(?:www\\.|m\\.)*(?:youtube\\.com|youtu\\.be)/watch/?\\?((time_continue|t)=\\d+&)?v=([a-zA-Z0-9_-]{11}).*")
(def youtube-short-regexp
 "https?://(?:www\\.|m\\.)*(?:youtube\\.com|youtu\\.be)/([a-zA-Z0-9_-]{11}/?)")

; https://vimeo.com/223518754 https://vimeo.com/groups/asd/223518754 https://vimeo.com/channels/asd/223518754
(def vimeo-regexp
 (str
  "(?:http|https)?:\\/\\/(?:www\\.)?vimeo.com\\/"
  "(?:channels\\/(?:\\w+\\/)?|groups\\/(?:[?:^\\/]*)"
  "\\/videos\\/|)(\\d+)(?:|\\/\\?)"))

(def loom-regexp
 (str
  "(?:http|https)?:\\/\\/(?:www\\.)?useloom.com\\/share\\/"
  "([a-zA-Z0-9_-]*/?)"))

(defn get-video-data [url]
  (let [yr (js/RegExp youtube-regexp "ig")
        yr2 (js/RegExp youtube-short-regexp "ig")
        vr (js/RegExp vimeo-regexp "ig")
        loomr (js/RegExp loom-regexp "ig")
        y-groups (.exec yr url)
        y2-groups (.exec yr2 url)
        v-groups (.exec vr url)
        loom-groups (.exec loomr url)]
    {:id (cond
          (nth y-groups 1)
          (nth y-groups 1)

          (nth y2-groups 1)
          (nth y2-groups 1)

          (nth v-groups 1)
          (nth v-groups 1)

          (nth loom-groups 1)
          (nth loom-groups 1))
     :type (cond
            (or (nth y-groups 1) (nth y2-groups 1))
            :youtube
            (nth v-groups 1)
            :vimeo
            (nth loom-groups 1)
            :loom)}))

(defn- get-vimeo-thumbnail-success [s video res]
  (let [resp (aget res 0)
        thumbnail (aget resp "thumbnail_medium")
        video-data (assoc video :thumbnail thumbnail)]
    (dis/dispatch! [:input [:media-input :media-video] video-data])
    (close-clicked s true)))

(def _retry (atom 0))

(declare get-vimeo-thumbnail)

(defn- get-vimeo-thumbnail-retry [s video]
  ;; Increment the retry count
  (if (< (swap! _retry inc) 3)
    ; Retry at most 3 times to load the video details
    (get-vimeo-thumbnail s video)
    ;; Add the video without thumbnail
    (do
      (dis/dispatch! [:input [:media-input :media-video] video])
      (close-clicked s true))))

(defn- get-vimeo-thumbnail [s video]
  (.ajax js/$
    #js {
      :method "GET"
      :url (str "https://vimeo.com/api/v2/video/" (:id video) ".json")
      :success #(get-vimeo-thumbnail-success s video %)
      :error #(get-vimeo-thumbnail-retry s video)}))

(defn valid-video-url? [url]
  (let [trimmed-url (string/trim url)
        yr (js/RegExp youtube-regexp "ig")
        yr2 (js/RegExp youtube-short-regexp "ig")
        vr (js/RegExp vimeo-regexp "ig")
        loomr (js/RegExp loom-regexp "ig")]
    (when (seq trimmed-url)
      (or (.match trimmed-url yr)
          (.match trimmed-url yr2)
          (.match trimmed-url vr)
          (.match trimmed-url loomr)))))

(defn video-add-click [s]
  (when (valid-video-url? @(::video-url s))
    (let [video-data (get-video-data @(::video-url s))]
      (if (= :vimeo (:type video-data))
        (get-vimeo-thumbnail s video-data)
        (do
          (dis/dispatch! [:input [:media-input :media-video] video-data])
          (close-clicked s true))))))

(rum/defcs media-video-modal < rum/reactive
                               ;; Locals
                               (rum/local false ::dismiss)
                               (rum/local "" ::video-url)
                               ;; Derivatives
                               (drv/drv :current-user-data)
                               ;; Mixins
                               first-render-mixin
                               {:did-mount (fn [s]
                                            (utils/after 100
                                             #(when-let [input-field (rum/ref-node s "video-input")]
                                                (.focus input-field)))
                                            s)}
  [s]
  (let [current-user-data (drv/react s :current-user-data)]
    [:div.media-video-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(:first-render-done s)))
       :appear (and (not @(::dismiss s)) @(:first-render-done s))})}
      [:div.modal-wrapper
        [:button.settings-modal-close.mlb-reset
          {:on-click #(close-clicked s)}]
        [:div.media-video-modal
          [:div.media-video-modal-header.group
            [:div.title "Adding a video"]]
          [:div.media-video-modal-divider]
          [:div.media-video-modal-content
            [:div.content-title "VIDEO LINK"]
            [:input.media-video-modal-input
              {:type "text"
               :value @(::video-url s)
               :ref "video-input"
               :on-change #(reset! (::video-url s) (.. % -target -value))
               :placeholder "Link from Loom, YouTube or Vimeo"}]]
          [:div.media-video-modal-buttons.group
            [:button.mlb-reset.mlb-default
              {:on-click #(video-add-click s)
               :disabled (not (valid-video-url? @(::video-url s)))}
              "Add"]
            [:button.mlb-reset.mlb-link-black
              {:on-click #(close-clicked s)}
              "Cancel"]]]]]))