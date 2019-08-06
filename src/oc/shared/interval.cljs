(ns oc.shared.interval)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Constructor

(defn make-interval
  [{:keys [fn ms]}]
  (atom {::fn fn
         ::ms ms
         ;; ::js-interval  <-- the result of js/setInterval
         }))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Internal implementation

(defn- stop-interval*
  [{::keys [fn ms js-interval] :as interval}]
  (when js-interval
    (js/clearInterval js-interval))
  (dissoc interval ::js-interval))

(defn- start-interval*
  [{::keys [fn ms js-interval] :as interval}]
  (if-not js-interval
    (let [new-js-interval (js/setInterval fn ms)]
      (assoc interval ::js-interval new-js-interval))
    interval))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Public API

(defn start-interval!
  [interval-atom]
  (swap! interval-atom start-interval*))

(defn stop-interval!
  [interval-atom]
  (swap! interval-atom stop-interval*))

(defn restart-interval!
  ([interval-atom]
   (restart-interval! interval-atom (::ms @interval-atom)))
  ([interval-atom new-ms]
   (stop-interval! interval-atom)
   (swap! interval-atom #(start-interval* (assoc % ::ms new-ms)))))
