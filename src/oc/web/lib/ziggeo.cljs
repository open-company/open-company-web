(ns oc.web.lib.ziggeo
  (:require [oc.web.local-settings :as ls]))

(defonce ziggeo-initialized (atom false))

(defn init-ziggeo [& [debug]]
  (when (compare-and-set! ziggeo-initialized false true)
    (let [config {:token ls/oc-ziggeo-app
                  :debug debug
                  :webrtc_streaming true
                  :webrtc_streaming_if_necessary true
                  :webrtc_on_mobile true}
          ZiggeoApplication (.. js/ZiggeoApi -V2 -Application)]
      (ZiggeoApplication. (clj->js config)))))