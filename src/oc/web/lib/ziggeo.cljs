(ns oc.web.lib.ziggeo)

(defn init-ziggeo [& [debug]]
  (let [config {:token "c9b611b2b996ee5a1f318d3bacc36b27"
                :debug debug
                :webrtc_streaming true
                :webrtc_streaming_if_necessary true
                :webrtc_on_mobile true
                :record_screen true
                :screen true}
        ZiggeoApplication (.. js/ZiggeoApi -V2 -Application)]
    (ZiggeoApplication. (clj->js config))))