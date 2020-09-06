(ns oc.web.lib.react-utils
  (:require ["react" :as react]
            [oops.core :refer (oset! oget)]
            [cljs.compiler :as compiler]
            [cljs.core :as cljs]))

(defn build [component props & children]
  (apply react/createElement component (clj->js props) children))

;; (defn pure-component []
;;   ;; (this-as this
;;   ;;          (.call (.-constructor (js/Object.getPrototypeOf (.-prototype react/PureComponent))) this))
;;   (this-as this
;;            (.contructor react/PureComponent this)))

;; (set! (.-prototype pure-component)
;;       (js/Object.create (.-prototype react/PureComponent)
;;                         #js {:keyPress (fn [e]
;;                                          )}))

;; Inspired by https://github.com/lilactown/hx/blob/ac33fb468e8daec98e1a87b1b3e24cfcb1949e58/src/hx/react.cljs

;; (defn create-class [super-class init-fn static-properties method-names]
;;   (let [ctor (fn [props]
;;                (this-as this
;;                         (js/console.log "DBG inside ctor")
;;                         ;; auto-bind methods
;;                         (doseq [method method-names]
;;                           (oset! this (munge method)
;;                                  (.bind (oget this (munge method)) this)))

;;                         (init-fn this props)))
;;         inherited-ctor (inherits ctor super-class)]
;;     (js/console.log "DBG goog" goog)
;;     (js/console.log "DBG    inherits" inherits)
;;     ;; set static properties on prototype
;;     (js/console.log "DBG    inherited" inherited-ctor)
;;     ;; (js/console.log "DBG    static-properties" static-properties)
;;     (doseq [[k v] static-properties]
;;       (js/console.log "DBG     k" k)
;;       (js/console.log "DBG     v" v)
;;       (oset! inherited-ctor k v))
;;     inherited-ctor)
;;   )

;; (defn create-component [init-fn static-properties method-names]
;;   (create-class react/Component init-fn static-properties method-names))

;; (defn create-pure-component [init-fn static-properties method-names]
;;   (js/console.log "DBG create-pure-component")
;;   (js/console.log "DBG    static-properties:" static-properties)
;;   (js/console.log "DBG    method-names:" method-names)
;;   (create-class react/PureComponent init-fn static-properties method-names))