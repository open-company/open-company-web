(ns oc.web.lib.whats-new)

(defn init [selector]
  (let [headway-config (clj->js {
                        :selector selector
                        :account "xGYD6J"
                        :position {:y "bottom"}
                        :translations {:title "What's New"
                                       :footer "👉 Show me more new stuff"}})]
    (.init js/Headway headway-config)))

(defn show []
  (.show js/Headway))