(ns oc.web.components.ui.ziggeo
  (:require [rum.core :as rum]))

(rum/defcs ziggeo-player < {:did-mount (fn [s]
                            (let [args (into [] (:rum/args s))
                                  video-id (get args 0)
                                  width (get args 2 640)
                                  height (get args 3 360)
                                  player-el (rum/ref-node s :ziggeo-player)]
                              (js/console.log "XXX ziggeo-player player-el" player-el "args" video-id width height)
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
                               (let [args (:rum/args s)
                                     submit-cb (get args 0)
                                     start-cb (get args 1)
                                     cancel-cb (get args 2)
                                     width (get args 3 640)
                                     height (get args 4 360)
                                     recorder-el (rum/ref-node s :ziggeo-recorder)]
                                 (js/console.log "XXX ziggeo-recorder recorder-el" recorder-el "args" submit-cb width height)
                                 (let [config {:element recorder-el
                                               :attrs #js {:width width
                                                           :height height
                                                           :theme "modern"
                                                           :themecolor "red"}}
                                       Recorder (.. js/ZiggeoApi -V2 -Recorder)
                                       recorder-instance (Recorder. (clj->js config))]
                                   (.activate recorder-instance)
                                   (.on recorder-instance "upload_selected"
                                    (fn []
                                     (js/console.log "XXX ziggeo-recorder upload_selected fired" (.get recorder-instance "video"))
                                     (when (fn? start-cb)
                                       (start-cb (.get recorder-instance "video")))))
                                   (.on recorder-instance "recording"
                                    (fn []
                                     (js/console.log "XXX ziggeo-recorder recording fired" (.get recorder-instance "video"))
                                     (when (fn? start-cb)
                                       (start-cb (.get recorder-instance "video")))))
                                   (.on recorder-instance "error"
                                    (fn []
                                     (js/console.log "XXX ziggeo-recorder error fired" (.get recorder-instance "video"))
                                     (when (fn? cancel-cb)
                                       (cancel-cb (.get recorder-instance "video")))))
                                   (.on recorder-instance "processed"
                                    (fn []
                                     (js/console.log "XXX ziggeo-recorder processed fired" (.get recorder-instance "video") recorder-instance)
                                     (submit-cb (.get recorder-instance "video"))))))
                               s)} 
  [s submit-cb start-cb cancel-cb width height]
  [:div.ziggeo-recorder
    {:ref :ziggeo-recorder}])