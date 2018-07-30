(ns oc.web.components.ui.ziggeo
  (:require [rum.core :as rum]
            [taoensso.timbre :as timbre]
            [oc.web.local-settings :as ls]
            [oc.web.actions.notifications :as na]
            [clojure.contrib.humanize :refer (filesize)]))

(rum/defcs ziggeo-player < (rum/local nil ::player-instance)
                           {:will-unmount (fn [s]
                             (when-let [player-instance @(::player-instance s)]
                               (.destroy player-instance))
                             s)
                            :did-mount (fn [s]
                            (let [args (into [] (:rum/args s))
                                  video-id (get args 0)
                                  width (get args 2 640)
                                  height (get args 3 480)
                                  player-el (rum/ref-node s :ziggeo-player)]
                              (let [config {:element player-el
                                            :attrs #js {:width width
                                                        :height height
                                                        :theme "modern"
                                                        :themecolor "red"
                                                        :video video-id}}
                                    Player (.. js/ZiggeoApi -V2 -Player)
                                    player-instance (Player. (clj->js config))]
                                (reset! (::player-instance s) player-instance)
                                (.activate player-instance)))
                            s)} 
  [s video-id remove-video-cb width height]
  [:div.ziggeo-player
    (when (fn? remove-video-cb)
      [:button.mlb-reset.remove-video-bt
        {:on-click (fn [] (when (fn? remove-video-cb)
                            (remove-video-cb video-id)))
         :data-toggle "tooltip"
         :data-placement "top"
         :data-container "body"
         :title "Remove video"}])
    [:div.ziggeo-player-embed
      {:ref :ziggeo-player}]])

(rum/defcs ziggeo-recorder <  (rum/local nil ::recorder-instance)
                              (rum/local false ::uploading)
                              {:will-unmount (fn [s]
                                (when-let [recorder-instance @(::recorder-instance s)]
                                  (when-not @(::uploading s)
                                    (.destroy recorder-instance)))
                                s)
                               :did-mount (fn [s]
                               (let [args (into [] (:rum/args s))
                                     {:keys [submit-cb start-cb cancel-cb width height
                                             pick-cover-start-cb pick-cover-end-cb upload-started-cb
                                             rerecord-cb]
                                      :or {width 640
                                           height 480}} (first (:rum/args s))
                                     recorder-el (rum/ref-node s :ziggeo-recorder)]
                                 (let [attrs {:width width
                                              :height height
                                              :theme "carrot"
                                              :themecolor "green"
                                              :meta-profile ls/oc-ziggeo-profiles}
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
                                     (when (and (fn? upload-started-cb)
                                                (not @(::uploading s)))
                                       (reset! (::uploading s) true)
                                       (upload-started-cb))))
                                   (.on recorder-instance "recording"
                                    (fn []
                                     (timbre/debug "recording")
                                     (when (fn? start-cb)
                                       (start-cb (.get recorder-instance "video")))))
                                   (.on recorder-instance "processing"
                                    (fn [a]
                                     (timbre/debug "processing" a)))
                                   (.on recorder-instance "error"
                                    (fn []
                                     (timbre/debug "error")
                                     (reset! (::uploading s) false)
                                     (na/show-notification {:title "Error processing your video."
                                                            :description "An error occurred while processing your video, please try again."
                                                            :id :ziggeo-video-error
                                                            :expire 10})
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
                                     (timbre/debug "processed" (.get recorder-instance "video"))
                                     (reset! (::uploading s) false)
                                     (na/show-notification {:title "Video uploaded and processed!"
                                                            :id :ziggeo-video-processed
                                                            :expire 5})
                                     (submit-cb (.get recorder-instance "video"))))))
                               s)} 
  [s {:keys [submit-cb start-cb cancel-cb width height pick-cover-start-cb
             pick-cover-end-cb upload-started-cb rerecord-cb]
      :or {width 640
           height 480}}]
  [:div.ziggeo-recorder
    {:ref :ziggeo-recorder
     :style {:width (str (or width 640) "px")
             :height (str (+ (or height 480) 96) "px")}}])