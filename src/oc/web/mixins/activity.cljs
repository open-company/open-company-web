(ns oc.web.mixins.activity
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]))

(defn truncate-element-mixin [element-class height]
  (letfn [(truncate-fn [s]
            ; Truncate body text with dotdotdot
            (when-let [dom-node (rum/dom-node s)]
              (let [$dom-node (js/$ element-class dom-node)]
                (.dotdotdot $dom-node
                 #js {:height height
                      :wrap "word"
                      :watch false
                      :ellipsis "..."}))))]
    {:did-mount (fn [s]
      (truncate-fn s)
      s)
     :did-remount (fn [o s]
      (when-not (= (rum/dom-node o) (rum/dom-node s))
        (truncate-fn s))
      s)}))

(defn foc-truncate-element-mixin [element-ref height-or-calc-fn]
  {:pre [(or (string? element-ref) (keyword? element-ref))
         (or (integer? height-or-calc-fn) (fn? height-or-calc-fn))]}
  (letfn [(truncate-fn [s]
            ; Truncate body text with dotdotdot
            (when-let [dom-node (rum/ref-node s element-ref)]
              (.dotdotdot (js/$ dom-node)
               #js {:height (cond (number? height-or-calc-fn)
                                  height-or-calc-fn
                                  (fn? height-or-calc-fn)
                                  (height-or-calc-fn s))
                    :wrap "word"
                    :watch false
                    :ellipsis "..."})))]
    {:did-mount (fn [s]
      (truncate-fn s)
      s)
     :did-remount (fn [o s]
      (when-not (= (rum/dom-node o) (rum/dom-node s))
        (truncate-fn s))
      s)}))

(def truncate-comments-mixin
  {:init (fn [s]
    (assoc s :comments-truncated (atom false)))
   :after-render (fn [s]
    ; Truncate body text with dotdotdot
    (when (compare-and-set! (:comments-truncated s) false true)
      (let [dom-node (rum/dom-node s)
            $collapsed-comments (.find (js/$ dom-node) "div.stream-comment-body:not(.expanded)")]
        (.dotdotdot $collapsed-comments
          #js {:height (* 22 5)
               :wrap "word"
               :watch false
               :ellipsis "..."
               :callback (fn [is-truncated]
                (this-as this
                  (when is-truncated
                    (.append (js/$ this) "<p><a class=\"read-more\">Read More</a></p>"))))})))
    s)
   :did-remount (fn [o s]
    (reset! (:comments-truncated s) false)
    s)})
