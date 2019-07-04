(ns oc.web.components.ui.media-video-modal
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [oops.core :refer (oget+)]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :refer (first-render-mixin)]))

(def media-video-modal-height 153)

(def youtube-regexp
 "https?://(?:www\\.|m\\.)*(?:youtube\\.com|youtu\\.be)/watch/?\\?(?:(?:time_continue|t)=\\d+s?&)?v=([a-zA-Z0-9_-]{11}).*")
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
  "(?:http|https)?:\\/\\/(?:www\\.)?(?:useloom|loom).com\\/share\\/"
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
        video-data (assoc video :thumbnail thumbnail)
        dismiss-modal-cb (:dismiss-cb (first (:rum/args s)))]
    (dis/dispatch! [:input [:media-input :media-video] video-data])
    (dismiss-modal-cb)))

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
      (let [dismiss-modal-cb (:dismiss-cb (first (:rum/args s)))]
        (dismiss-modal-cb)))))

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
  (let [video-data (get-video-data @(::video-url s))
        dismiss-modal-cb (:dismiss-cb (first (:rum/args s)))]
    (if (= :vimeo (:type video-data))
      (get-vimeo-thumbnail s video-data)
      (do
        (dis/dispatch! [:input [:media-input :media-video] video-data])
        (dismiss-modal-cb)))))

(rum/defcs media-video-modal < rum/reactive
                               ;; Locals
                               (rum/local false ::dismiss)
                               (rum/local "" ::video-url)
                               (rum/local false ::video-url-focused)
                               (rum/local 0 ::offset-top)
                               ;; Derivatives
                               (drv/drv :current-user-data)
                               ;; Mixins
                               first-render-mixin
                               {:will-mount (fn [s]
                                 (when-let [picker-el (sel1 [:div.medium-editor-media-picker])]
                                   (reset! (::offset-top s) (.-offsetTop picker-el)))
                                 s)
                                :did-mount (fn [s]
                                 (when-let [video-field (rum/ref-node s "video-input")]
                                  (.focus video-field))
                                s)}
  [s {:keys [fullscreen dismiss-cb]}]
  (let [current-user-data (drv/react s :current-user-data)
        valid-url (valid-video-url? @(::video-url s))
        scrolling-element-selector (if fullscreen :div.cmail-content-outer :div.cmail-content)
        scrolling-element (sel1 [scrolling-element-selector])
        win-height (or (.-clientHeight (.. js/window -document -documentElement))
                       (.-innerHeight js/window))
        fixed-win-height (if fullscreen win-height (- win-height 52)) ;; Remove bottom padding of Cmail view
        top-offset-limit (.-offsetTop (sel1 [:div.rich-body-editor-outer-container]))
        top-limit (if fullscreen
                    0
                    ;; On Cmail the top limit is not 0 but it's the dark bar, so let's
                    ;; calculate the distance btw the rich body editor and the top of the bar
                    ;; and remove the bar height
                    (+ (* -1 top-offset-limit)
                       56))
        offset-top (if fullscreen
                    ;; On Bmail the offset is directly the top offset of the body container
                    top-offset-limit
                    ;; On cmail let's get the distance of Cmail from the top of the page
                    ;; and add the distance between the rich-body-editor container
                    ;; and the first fixed element (that is the limit to calculate the absolute offset)
                    (+ (oget+ (.offset (js/$ (sel1 [:div.cmail-outer]))) :top)
                       top-offset-limit))
        ; offset-height (.-scrollHeight scrolling-element)
        scroll-top (.-scrollTop scrolling-element)
        top-position (max 0 @(::offset-top s))
        relative-position (+ top-position
                             offset-top
                             (* scroll-top -1)
                             media-video-modal-height)
        adjusted-position (if (> relative-position fixed-win-height) ;; 286 is the top offset of the body
                            (max top-limit (- top-position (- relative-position fixed-win-height) 16))
                            top-position)]
    [:div.media-video-modal-container
      {:class (when @(::video-url-focused s) "video-url-focused")
       :style {:top (str adjusted-position "px")}}
      [:div.media-video-modal-title
        "Embed video"]
      [:input.media-video-modal-input.oc-input
          {:type "text"
           :value @(::video-url s)
           :ref "video-input"
           :on-change #(reset! (::video-url s) (.. % -target -value))
           :on-focus #(reset! (::video-url-focused s) true)
           :on-key-press (fn [e]
                           (when (and valid-url
                                      (= (.-key e) "Enter"))
                             (video-add-click s)))
           :on-blur #(when (clojure.string/blank? @(::video-url s))
                       (reset! (::video-url-focused s) false))
           :placeholder "Paste the video link…"}]
      [:button.mlb-reset.embed-video-bt
        {:class (when-not valid-url "disabled")
         :on-click #(when valid-url
                      (video-add-click s))}
        "Embed video"]
      [:div.media-video-description
        "Works with Loom, Youtube, and Vimeo"]]))