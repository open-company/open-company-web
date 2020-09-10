(ns oc.web.lib.logging
  (:require [taoensso.timbre :as timbre]
            [cuerdas.core :as s]))

(def carrot-log-level (atom nil))

(defn config-log-level! [level]
  (let [level-kw (keyword (s/lower level))]
    (when (#{:trace :debug :info :warn :error :fatal :report} level-kw)
      (timbre/info "Log level:" level-kw)
      (reset! carrot-log-level level-kw)
      (timbre/merge-config! {:level level-kw}))))

(defn dbg [& args]
  (when (= @carrot-log-level :debug)
    (apply js/console.log args)))

(set! (.-OCWebConfigLogLevel js/window) config-log-level!)