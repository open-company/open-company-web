(ns oc.web.lib.ziggeo)

(defn init-ziggeo [& [debug]]
  (let [config {:token "c9b611b2b996ee5a1f318d3bacc36b27"
                :debug debug}
        ZiggeoApplication (.. js/ZiggeoApi -V2 -Application)]
    (ZiggeoApplication. (clj->js config))))