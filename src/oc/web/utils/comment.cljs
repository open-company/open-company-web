(ns oc.web.utils.comment
  (:require [cljsjs.medium-editor]
            [defun.core :refer (defun defun-)]
            [goog.object :as gobj]
            [cuerdas.core :as string]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.mention :as mention-utils]))

(defn setup-medium-editor [comment-node users-list]
  (let [extentions (if (seq users-list)
                     #js {"mention" (mention-utils/mention-ext users-list)}
                     #js {})
        config {:toolbar false
                :anchorPreview false
                :imageDragging false
                :extensions extentions
                :autoLink true
                :anchor false
                :targetBlank true
                :paste #js {:forcePlainText false
                            :cleanPastedHTML true
                            :cleanAttrs #js ["style" "alt" "dir" "size" "face" "color" "itemprop" "name" "id"]
                            :cleanTags #js ["meta" "video" "audio" "img" "button" "svg" "canvas"
                                            "figure" "input" "textarea"]
                            :unwrapTags #js ["div" "label" "font" "h1" "h2" "h3" "h4" "h5" "div" "p" "ul" "ol" "li"
                                             "h6" "strong" "section" "time" "em" "main" "u" "form" "header" "footer"
                                             "details" "summary" "nav" "abbr" "a"]}
                :placeholder #js {:text "Add a reply…"
                                  :hideOnClick true}
               :keyboardCommands #js {:commands #js [
                                  #js {
                                    :command false
                                    :key "B"
                                    :meta true
                                    :shift false
                                    :alt false
                                  }
                                  #js {
                                    :command false
                                    :key "I"
                                    :meta true
                                    :shift false
                                    :alt false
                                  }
                                  #js {
                                    :command false
                                    :key "U"
                                    :meta true
                                    :shift false
                                    :alt false
                                  }]}}]
    (new js/MediumEditor comment-node (clj->js config))))

(defn add-comment-content [comment-node]
  (let [comment-html (.-innerHTML comment-node)
        $comment-node (.html (js/$ "<div/>") comment-html)
        _remove-mentions-popup (.remove $comment-node ".oc-mention-popup")]
    (utils/clean-body-html (.html $comment-node))))

(defn ungroup-comments [comments]
  (filterv #(#{:thread :comment} (:resource-type %)) comments))

(defun sort-comments
  ([comments :guard nil?]
   [])
  ([comments :guard map?]
   (sort-comments (vals comments)))
  ([comments :guard coll?]
   (vec (sort-by :created-at comments)))
  ([comments :guard coll? parent-uuid]
   (let [check-fn (if parent-uuid #(-> % :parent-uuid (= parent-uuid)) (comp empty? :parent-uuid))
         filtered-comments (filterv check-fn comments)
         sorted-comments (sort-by :created-at filtered-comments)]
     (if (nil? parent-uuid)
      (vec (reverse sorted-comments))
      (vec sorted-comments)))))

(defn- get-collapsed-item [item collapsed-map]
  (merge item (get collapsed-map (:uuid item))))

(defn is-collapsed? [item collapsed-map]
  (:collapsed (get-collapsed-item item collapsed-map)))

; (defn is-unread? [item collapsed-map]
;   (not (:unread (get-collapsed-item item collapsed-map))))

; (defn unread?
;   "If unread was already set let's reuse it."
;   [last-read-at comment-data]
;   (if (contains? comment-data :unread)
;     (:unread comment-data)
;     (and (not (:author? comment-data))
;          (< (.getTime (utils/js-date last-read-at))
;             (.getTime (utils/js-date (:created-at comment-data)))))))

; (defn- unread-comment [comment-data last-read-at collapsed-map]
;   (if-not last-read-at
;     ;; User has never read the post, so comment is new
;     (assoc comment-data :unread true)
;     (assoc comment-data :unread
;      (unread? last-read-at (get-collapsed-item comment-data collapsed-map)))))

(defun comment-unread?
  "A comment is unread if it's created-at is past the last seen-at of the contianer it belongs to."

  ([comment-data :guard map? last-read-at]
   (comment-unread? (:created-at comment-data) last-read-at))

  ([created-at last-read-at :guard #(or (nil? %) (string? %))]
   (pos? (compare created-at last-read-at))))

(defn is-unseen? [item collapsed-map]
  (not (:unseen (get-collapsed-item item collapsed-map))))

(defun comment-unseen?
  "A comment is unseen if it's created-at is later than the last seen-at of the container it belongs to."

  ([comment-data :guard map? container-seen-at]
   (comment-unseen? (:created-at comment-data) container-seen-at))

  ([created-at container-seen-at :guard #(or (nil? %) (string? %))]
   (pos? (compare created-at container-seen-at))))

(def default-expanded-comments-count 3)

(defun collapse-comments
  "Add a collapsed flag to every comment that is a reply and is not unseen.
   Also add unseen? flag to every unseen one. Add a count of the collapsed
   comments to each root comment."

   ([_comments :guard empty?
     _container-seen-at]
    [])
  ([comments :guard #(and (coll? %)
                          (<= (count %) default-expanded-comments-count))
    container-seen-at :guard #(or (nil? %) (string? %))]
   (map #(assoc % :collapsed false) comments))
  ;; When we have more than default-expanded-comments-count comments we always show the last default-expanded-comments-count plus all the unseen
  ([comments :guard (fn [cs] (and (coll? cs)
                                  (> (count cs) default-expanded-comments-count)))
    container-seen-at :guard #(or (nil? %) (string? %))]
   (let [comments-count (count comments)
         min-expanded-index (- comments-count default-expanded-comments-count)
         min-unseen-index (utils/index-of comments :unseen)
         first-expanded-index (min min-unseen-index min-expanded-index)
         collapsed-comments (subvec (vec comments) 0 first-expanded-index)
         expanded-comments (subvec (vec comments) first-expanded-index comments-count)]
     (vec (concat
      (map #(assoc % :collapsed true) collapsed-comments)
      (map #(assoc % :collapsed false) expanded-comments))))))

(defun add-comment-focus-value
  ([prefix :guard string? comment-data :guard map?]
   (add-comment-focus-value prefix (:resource-uuid comment-data) (:parent-uuid comment-data) (:uuid comment-data)))

  ([prefix :guard string? entry-uuid :guard string?]
   (add-comment-focus-value prefix entry-uuid nil nil))

  ([prefix :guard string? entry-uuid :guard string? parent-comment-uuid]
   (add-comment-focus-value prefix entry-uuid parent-comment-uuid nil))

  ([prefix :guard string? entry-uuid :guard string? parent-comment-uuid edit-comment-uuid]
   (str prefix "-" (dis/add-comment-string-key entry-uuid parent-comment-uuid edit-comment-uuid))))