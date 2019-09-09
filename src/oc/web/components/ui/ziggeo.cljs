(ns oc.web.components.ui.ziggeo
  (:require [rum.core :as rum]
            [taoensso.timbre :as timbre]
            [oc.web.local-settings :as ls]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.notifications :as na]
            [oc.web.lib.responsive :as responsive]
            [clojure.contrib.humanize :refer (filesize)]))

(rum/defcs ziggeo-player < (rum/local nil ::player-instance)
                           (rum/local false ::video-show-player)
                           {:will-unmount (fn [s]
                             (when-let [player-instance @(::player-instance s)]
                               (.destroy player-instance))
                             s)
                            :did-mount (fn [s]
                              (let [{:keys [video-id width height video-processed autoplay playing-cb]
                                     :or {width 640
                                          height 480}} (first (:rum/args s))]
                              (when video-processed
                                (let [player-el (rum/ref-node s :ziggeo-player)
                                      config {:element player-el
                                              :attrs #js {:width width
                                                          :height height
                                                          :autoplay autoplay
                                                          :theme "carrot"
                                                          :themecolor "white"
                                                          :video video-id}}
                                      Player (.. js/ZiggeoApi -V2 -Player)
                                      player-instance (Player. (clj->js config))]
                                  (reset! (::player-instance s) player-instance)
                                  (.activate player-instance)
                                  (.on player-instance "playing"
                                    (fn []
                                     (timbre/debug "playing")
                                     (when (fn? playing-cb)
                                       (playing-cb (.get player-instance "video"))))))))
                            s)} 
  [s {:keys [video-id remove-video-cb width height video-processed autoplay lazy video-image]
      :or {width 640
           height 480}}]
  [:div.ziggeo-player
    (when lazy
      [:div.video-play-image
        {:style {:width (str (or width 640) "px")
                 :height (str (or height 480) "px")}
         :class (utils/class-set {:clicked @(::video-show-player s)
                                  :loading (not video-processed)})
         :on-click #(do
                      (reset! (::video-show-player s) true)
                      (utils/after 100
                                   (fn [] (.play @(::player-instance s)))))}
        [:div.play {:class (when (not video-processed) "loading")}]
        [:img.video-image {
                           :style {:width (str (or width 640) "px")
                                   :height (str (or height 480) "px")}
                           :class (when (not video-processed) "loading")
                           :src (str "https://" video-image)}]])
    (when (fn? remove-video-cb)
      [:button.mlb-reset.remove-video-bt
        {:on-click (fn [] (when (fn? remove-video-cb)
                            (remove-video-cb video-id)))
         :class  (when (and lazy (not @(::video-show-player s)))
                   "hide")
         :data-toggle "tooltip"
         :data-placement "top"
         :data-container "body"
         :title "Remove video"}])
    (if-not video-processed
      [:div.ziggeo-player-not-processed
        {:style {:width (str (or width 640) "px")
                 :height (str (or height 480) "px")}
         :class  (when (and lazy (not @(::video-show-player s)))
                   "hide")}
       [:span "Preparing video…"]]
      [:div.ziggeo-player-embed
       {:ref :ziggeo-player
        :class  (when (and lazy (not @(::video-show-player s)))
                  "hide")}])])

(rum/defcs ziggeo-recorder <  (rum/local nil ::recorder-instance)
                              (rum/local false ::uploading)
                              (rum/local false ::mounted)
                              {:will-unmount (fn [s]
                                (reset! (::mounted s) false)
                                (when-let [recorder-instance @(::recorder-instance s)]
                                  (when-not @(::uploading s)
                                    (.destroy recorder-instance)))
                                s)
                               :will-mount (fn[s]
                                (reset! (::mounted s) true)
                                s)
                               :did-mount (fn [s]
                               (let [args (vec (:rum/args s))
                                     {:keys [submit-cb start-cb cancel-cb width height
                                             pick-cover-start-cb pick-cover-end-cb upload-started-cb
                                             rerecord-cb]
                                      :or {width 640
                                           height 480}} (first (:rum/args s))
                                     recorder-el (rum/ref-node s :ziggeo-recorder)]
                                 (let [attrs {:width width
                                              :height height
                                              :theme "carrot"
                                              :themecolor "white"
                                              :localplayback true
                                              :timelimit 300
                                              :meta-profile ls/oc-ziggeo-profiles
                                              :picksnapshots (not (responsive/is-tablet-or-mobile?))}
                                       config {:element recorder-el
                                               :attrs attrs}
                                       Recorder (.. js/ZiggeoApi -V2 -Recorder)
                                       recorder-instance (Recorder. (clj->js config))]
                                   (reset! (::recorder-instance s) recorder-instance)
                                   (.activate recorder-instance)
                                   (.on recorder-instance "upload_selected"
                                    (fn []
                                     (timbre/debug "upload_selected")
                                     (when (fn? start-cb)
                                       (start-cb (.get recorder-instance "video")))))
                                   (.on recorder-instance "upload_progress"
                                    (fn [a b]
                                     (timbre/debug "upload_progress" a b (fn? upload-started-cb))
                                     (when-not @(::uploading s)
                                       (reset! (::uploading s) true)
                                       (when (fn? upload-started-cb)
                                          (upload-started-cb (.get recorder-instance "video"))))
                                     (when-not @(::mounted s)
                                       (na/show-notification {:title "Video is uploading."
                                                              :description (str "Progress: " (filesize a :binary false :format "%.2f") " of " (filesize b :binary false :format "%.2f") ".")
                                                              :id :ziggeo-video-upload
                                                              :expire 3}))))
                                   (.on recorder-instance "recording"
                                    (fn []
                                     (timbre/debug "recording")
                                     (when (fn? start-cb)
                                       (start-cb (.get recorder-instance "video")))))
                                   (.on recorder-instance "processing"
                                    (fn [a]
                                     (timbre/debug "processing" a)
                                     (when-not @(::mounted s)
                                       (na/remove-notification-by-id :ziggeo-video-upload)
                                       (na/show-notification {:title "Video is processing."
                                                              :description (str "Progress: " (int (* a 100)) "%.")
                                                              :id :ziggeo-video-processing
                                                              :expire 3}))))
                                   (.on recorder-instance "error"
                                    (fn []
                                     (timbre/debug "error")
                                     (reset! (::uploading s) false)
                                     (na/remove-notification-by-id :ziggeo-video-upload)
                                     (na/remove-notification-by-id :ziggeo-video-processing)
                                     (na/show-notification {:title "Error processing your video."
                                                            :description "An error occurred while processing your video, please try again."
                                                            :id :ziggeo-video-error
                                                            :expire 3})
                                     (when (fn? cancel-cb)
                                       (cancel-cb (.get recorder-instance "video")))))
                                   (.on recorder-instance "pick_cover_start"
                                    (fn []
                                      (timbre/debug "picking_cover")
                                      (when (fn? pick-cover-start-cb)
                                        (pick-cover-start-cb))))
                                   (.on recorder-instance "upload_start"
                                    (fn [a]
                                      (timbre/debug "picking_cover_end" a)
                                      (when (fn? pick-cover-end-cb)
                                        (pick-cover-end-cb a))))
                                   (.on recorder-instance "rerecord"
                                    (fn []
                                     (reset! (::uploading s) false)
                                     (when (fn? rerecord-cb)
                                      (rerecord-cb))))
                                   (.on recorder-instance "processed"
                                    (fn []
                                     (timbre/debug "processed" (.get recorder-instance "video"))))
                                   (.on recorder-instance "verified"
                                    (fn []
                                     (timbre/debug "verified" (.get recorder-instance "video"))
                                     (reset! (::uploading s) false)
                                     (na/show-notification {:title "Video is processing and will be available soon"
                                                            :id :ziggeo-video-processed
                                                            :expire 3})
                                     (when (fn? submit-cb)
                                      (submit-cb (.get recorder-instance "video") (not @(::mounted s))))))))
                               s)} 
  [s {:keys [submit-cb start-cb cancel-cb width height pick-cover-start-cb
             pick-cover-end-cb upload-started-cb rerecord-cb remove-recorder-cb]
      :or {width 640
           height 480}}]
  [:div.ziggeo-recorder
    {:style {:width (str (or width 640) "px")
             :height (str (or height 480) "px")}}
    (when (fn? remove-recorder-cb)
      [:button.mlb-reset.remove-recorder-bt
        {:on-click (fn [] (when (fn? remove-recorder-cb)
                            (remove-recorder-cb)))
         :data-toggle "tooltip"
         :data-placement "top"
         :data-container "body"
         :title "Remove video"}])
    [:div.ziggeo-recorder-embed
      {:ref :ziggeo-recorder}]])