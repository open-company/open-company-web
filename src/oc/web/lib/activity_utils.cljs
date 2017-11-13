(ns oc.web.lib.activity-utils
  (:require [cuerdas.core :as s]
            [oc.web.lib.utils :as utils]))

(defn get-first-body-thumbnail
  "Given an entry body get the first thumbnail available.
  Thumbnail type: image, video or chart."
  [activity-body & [is-ap]]
  (let [$body (js/$ (str "<div>" activity-body "</div>"))
        thumb-els (js->clj (js/$ "img:not(.emojione), iframe" $body))
        found (atom nil)]
    (dotimes [el-num (.-length thumb-els)]
      (let [el (aget thumb-els el-num)
            $el (js/$ el)]
        (when-not @found
          (if (= (s/lower (.-tagName el)) "img")
            (let [width (.attr $el "width")
                  height (.attr $el "height")]
              (when (and (not @found)
                         (or (<= width (* height 2))
                             (<= height (* width 2))))
                (reset! found
                  {:type "image"
                   :thumbnail (if (and (not is-ap) (.data $el "thumbnail"))
                                (.data $el "thumbnail")
                                (.attr $el "src"))})))
            (reset! found {:type (.data $el "media-type") :thumbnail (.data $el "thumbnail")})))))
    @found))

(def default-body-height 72)
(def default-all-posts-body-height 144)

(defn- truncate-body [body-sel is-all-posts]
  (let [$body-els (js/$ (str body-sel ">*"))
        partial-heights (atom [])
        found (atom false)]
    (js/console.log "au/truncate-body $body-els" $body-els)
    (.each $body-els (fn [idx el]
     (when-not @found
       (this-as this
         (let [$this (js/$ this)
               el-h (.outerHeight $this true) ;; Include margins in height calculation
               prev-height (apply + @partial-heights)
               actual-height (+ prev-height el-h)
               max-height (if is-all-posts default-all-posts-body-height default-body-height)
               truncate-height  (cond
                                  (zero? (- max-height prev-height))
                                  0
                                  (<= (- max-height prev-height) 24)
                                  24
                                  (<= (- max-height prev-height) (* 24 2))
                                  (* 24 2)
                                  (<= (- max-height prev-height) (* 24 3))
                                  (* 24 3)
                                  (< (- max-height prev-height) (* 24 4))
                                  (* 24 4)
                                  (< (- max-height prev-height) (* 24 5))
                                  (* 24 5)
                                  (< (- max-height prev-height) (* 24 6))
                                  (* 24 6))]
           (js/console.log "   - $this" $this "el-h" el-h "actual-height" actual-height "max-height" max-height "prev-height" prev-height)
           (swap! partial-heights #(vec (conj % el-h)))
           (when (>= actual-height max-height)
             (js/console.log "      apply dotdotdot!" truncate-height)
             (reset! found true)
             (.dotdotdot $this
               #js {:height truncate-height
                    :wrap "word"
                    :watch true
                    :ellipsis "..."})))))))))