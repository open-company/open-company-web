(ns oc.web.components.ui.ziggeo
  (:require [rum.core :as rum]
            [oc.web.actions.notifications :as na]
            [clojure.contrib.humanize :refer (filesize)]))

(rum/defcs ziggeo-player < {:did-mount (fn [s]
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

(rum/defcs ziggeo-recorder < {:did-mount (fn [s]
                               (let [args (into [] (:rum/args s))
                                     {:keys [submit-cb start-cb cancel-cb width height
                                             pick-cover-start-cb pick-cover-end-cb]
                                      :or {width 640
                                           height 480}} (first (:rum/args s))
                                     recorder-el (rum/ref-node s :ziggeo-recorder)]
                                 (let [config {:element recorder-el
                                               :attrs #js {:width width
                                                           :height height
                                                           :theme "carrot"
                                                           :themecolor "green"}}
                                       Recorder (.. js/ZiggeoApi -V2 -Recorder)
                                       recorder-instance (Recorder. (clj->js config))]
                                   (.activate recorder-instance)
                                   (.on recorder-instance "upload_selected"
                                    (fn []
                                     (js/console.log "XXX upload_selected")
                                     (when (fn? start-cb)
                                       (start-cb (.get recorder-instance "video")))))
                                   (.on recorder-instance "upload_progress"
                                    (fn [a b]
                                     (js/console.log "XXX upload_progress" a b)
                                     (na/show-notification {:title "Video is uploading."
                                                            :description (str "Progress: " (filesize a :binary false :format "%.2f") " of " (filesize b :binary false :format "%.2f") ".")
                                                            :id :ziggeo-video-upload
                                                            :expire (if (< (- b a) 1000) 5 0)})))
                                   (.on recorder-instance "recording"
                                    (fn []
                                     (js/console.log "XXX recording")
                                     (when (fn? start-cb)
                                       (start-cb (.get recorder-instance "video")))))
                                   (.on recorder-instance "processing"
                                    (fn [a]
                                     (js/console.log "XXX processing")
                                     (na/remove-notification-by-id :ziggeo-video-upload)
                                     (na/show-notification {:title "Video is processing."
                                                            :description (str "Progress: " (int a) "%.")
                                                            :id :ziggeo-video-processing
                                                            :expire (if (> a 99) 5 0)})))
                                   (.on recorder-instance "error"
                                    (fn []
                                     (js/console.log "XXX error")
                                     (na/remove-notification-by-id :ziggeo-video-upload)
                                     (na/remove-notification-by-id :ziggeo-video-processing)
                                     (na/show-notification {:title "Error processing your video."
                                                            :description "An error occurred while processing your video, please try again."
                                                            :id :ziggeo-video-error
                                                            :expire 10})
                                     (when (fn? cancel-cb)
                                       (cancel-cb (.get recorder-instance "video")))))
                                   (.on recorder-instance "pick_cover_start"
                                    (fn []
                                      (js/console.log "XXX picking_cover")
                                      (when (fn? pick-cover-start-cb)
                                        (pick-cover-start-cb))))
                                   (.on recorder-instance "pick_cover_end"
                                    (fn [a]
                                      (js/console.log "XXX picking_cover_end" a)
                                      (when (fn? pick-cover-end-cb)
                                        (pick-cover-end-cb a))))
                                   (.on recorder-instance "processed"
                                    (fn []
                                     (js/console.log "XXX processed")
                                     (na/remove-notification-by-id :ziggeo-video-upload)
                                     (na/remove-notification-by-id :ziggeo-video-processing)
                                     (submit-cb (.get recorder-instance "video"))))))
                               s)} 
  [s {:keys [submit-cb start-cb cancel-cb width height pick-cover-start-cb pick-cover-end-cb]
      :or {width 640
           height 480}}]
  [:div.ziggeo-recorder
    {:ref :ziggeo-recorder
     :style {:width (str (or width 640) "px")
             :height (str (+ (or height 480) 96) "px")}}])