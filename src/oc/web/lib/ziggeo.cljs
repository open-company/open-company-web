(ns oc.web.lib.ziggeo
  (:require [oc.web.local-settings :as ls]))

(defn init-ziggeo [& [debug]]
  (let [config {:token ls/oc-ziggeo-app
                :debug debug
                :webrtc_streaming true
                :webrtc_streaming_if_necessary true
                :webrtc_on_mobile true}
        ZiggeoApplication (.. js/ZiggeoApi -V2 -Application)]
    (ZiggeoApplication. (clj->js config))))