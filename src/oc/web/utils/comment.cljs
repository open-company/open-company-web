(ns oc.web.utils.comment
  (:require [cljsjs.medium-editor]
            [defun.core :refer (defun defun-)]
            [goog.object :as gobj]
            [cuerdas.core :as string]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
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

(defn add-comment-content [comment-node & [print?]]
  (let [comment-html (.-innerHTML comment-node)
        $comment-node (.html (js/$ "<div/>") comment-html)
        _remove-mentions-popup (.remove $comment-node ".oc-mention-popup")]
    (.html $comment-node)))

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
  (get collapsed-map (:uuid item)))

(defn is-collapsed? [item collapsed-map]
  (not (:expanded (get-collapsed-item item collapsed-map))))

(defn is-unread? [item collapsed-map]
  (not (:unread (get-collapsed-item item collapsed-map))))

(defn unread?
  "If unread was already set let's reuse it."
  [last-read-at comment-data]
  (if (contains? comment-data :unread)
    (:unread comment-data)
    (and (not (:author? comment-data))
         (< (.getTime (utils/js-date last-read-at))
            (.getTime (utils/js-date (:created-at comment-data)))))))

(defn- unread-comment [comment-data last-read-at collapsed-map]
  (if-not last-read-at
    ;; User has never read the post, so comment is new
    (assoc comment-data :unread true)
    (assoc comment-data :unread
     (unread? last-read-at (merge comment-data (get-collapsed-item comment-data collapsed-map))))))

(defn- expanded-comment [comment-data last-read-at collapsed-map & [last-comment?]]
  (assoc comment-data :expanded (or ;; comment is unread
                                    (:unread comment-data)
                                    ;; Keep the comment expanded if it was already
                                    (not (is-collapsed? comment-data collapsed-map))
                                    ;; Do not collapse root comments
                                    ; (not (seq (:parent-uuid comment-data)))
                                    ;; User has not read the post yet
                                    (not (seq last-read-at))
                                    ;; Do not collapse last comments
                                    last-comment?)))

(defn- enrich-comment [comment-data last-read-at last-comment? collapsed-map]
  (as-> comment-data c
   (unread-comment c last-read-at collapsed-map)
   (expanded-comment c last-read-at collapsed-map last-comment?)))

(defun collapse-comments
  "Add a collapsed flag to every comment that is a reply and is not unread.
   Also add unread? flag to every unread one. Add a count of the collapsed
   comments to each root comment."

  ([last-read-at :guard #(or (nil? %) (string? %))
    comments :guard coll?]
    (collapse-comments last-read-at comments {}))

  ([last-read-at :guard #(or (nil? %) (string? %))
    comments :guard #(and (coll? %)
                          (<= (count %) 3))
    collapsed-map :guard map?]
   (mapv
    #(-> %
      (unread-comment last-read-at collapsed-map)
      (assoc :expanded true))
    comments))

  ([last-read-at :guard #(or (nil? %) (string? %))
    comments :guard #(and (coll? %)
                          (> (count %) 3))
    collapsed-map :guard map?]
   (let [with-unread (mapv #(unread-comment % last-read-at collapsed-map) comments)
         has-unread-comments? (some :unread with-unread)]
     (if has-unread-comments?
       (concat
        [(assoc (first with-unread) :expanded (not (is-collapsed? (first with-unread) collapsed-map)))]
        (mapv #(expanded-comment % last-read-at collapsed-map) (butlast (rest with-unread)))
        [(assoc (last with-unread) :expanded (not (is-collapsed? (last with-unread) collapsed-map)))])
       (collapse-comments last-read-at with-unread [] collapsed-map))))

  ;; Recursive step: unread has been set, let's add expand now
  ([last-read-at :guard #(or (nil? %) (string? %))
    in-comments :guard coll?
    out-comments :guard coll?
    collapsed-map :guard map?]
    (let [read-items (vec (take-while #(let [item (get-collapsed-item % collapsed-map)]
                                         (and (not (:unread item))
                                              (not (:expanded item))))
                           in-comments))
          next-in-comments (if (seq read-items)
                             (subvec in-comments (count read-items))
                             (vec (rest in-comments)))
          should-add-collapsed-item? (> (count read-items) 1)
          first-expanded? (not (seq out-comments))
          fixed-read-items (if first-expanded?
                             (rest read-items)
                             read-items)
          collapsed-item (when should-add-collapsed-item?
                           {:resource-type :collapsed-comments
                            :collapsed-count (count fixed-read-items)
                            :collapse-id (clojure.string/join "-" (map :uuid fixed-read-items))
                            :expanded true
                            :unread false
                            :comment-uuids (set (map :uuid fixed-read-items))})
          next-out-comments (if should-add-collapsed-item?
                              ;; In case there are at least 2 read and not expanded items in a row
                              ;; set the 
                              (vec (remove nil?
                                    (concat out-comments
                                            [(when first-expanded?
                                               (assoc (first read-items) :expanded true))]
                                            fixed-read-items
                                            [collapsed-item])))
                              (vec (conj out-comments
                                         (assoc (first in-comments) :expanded true))))]
      (if (seq next-in-comments)
        (recur last-read-at next-in-comments next-out-comments collapsed-map)
        next-out-comments))))

(defun add-comment-focus-value
  ([prefix :guard string? comment-data :guard map?]
   (add-comment-focus-value prefix (:resource-uuid comment-data) (:parent-uuid comment-data) (:uuid comment-data)))

  ([prefix :guard string? entry-uuid :guard string?]
   (add-comment-focus-value prefix entry-uuid nil nil))

  ([prefix :guard string? entry-uuid :guard string? parent-comment-uuid]
   (add-comment-focus-value prefix entry-uuid parent-comment-uuid nil))

  ([prefix :guard string? entry-uuid :guard string? parent-comment-uuid edit-comment-uuid]
   (str prefix "-" (dis/add-comment-string-key entry-uuid parent-comment-uuid edit-comment-uuid))))