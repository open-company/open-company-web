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
  (vec (mapcat #(concat [(dissoc % :thread-children)] (:thread-children %)) comments)))

(defun sort-comments
  ([comments :guard nil?]
   [])
  ([comments :guard map?]
   (sort-comments (vals comments)))
  ;; If we have already grouped  the comments we need to ungroup them to recalculate the grouping
  ([comments :guard (fn [c] (and (sequential? c)
                                 (some #(contains? % :thread-children) c)))]
    (sort-comments (ungroup-comments comments)))
  ([comments :guard sequential?]
   (let [root-comments (filterv (comp empty? :parent-uuid) comments)
         grouped-comments (mapv #(let [sorted-children (sort-comments comments (:uuid %))]
                                  (merge % {:thread-children sorted-children
                                            :last-activity-at (if (seq sorted-children)
                                                                (-> sorted-children last :created-at)
                                                                (:created-at %))}))
                           root-comments)]
     (vec (reverse (sort-by :created-at grouped-comments)))))
  ([comments :guard sequential? parent-uuid]
   (let [check-fn (if parent-uuid #(-> % :parent-uuid (= parent-uuid)) (comp empty? :parent-uuid))
         filtered-comments (filterv check-fn comments)
         sorted-comments (sort-by :created-at filtered-comments)]
     (if (nil? parent-uuid)
      (vec (reverse sorted-comments))
      (vec sorted-comments)))))

(defn unread?
  "If unread was already set let's reuse it."
  [last-read-at comment-data]
  (if (contains? comment-data :unread)
    (:unread comment-data)
    (and (not (:author? comment-data))
         (< (.getTime (utils/js-date last-read-at))
            (.getTime (utils/js-date (:created-at comment-data)))))))

(defn- unread-comment [comment-data last-read-at collapsed-map]
  (let [collapsed-comment-map (get collapsed-map (:uuid comment-data))]
   (assoc comment-data :unread
    (unread? last-read-at (merge comment-data collapsed-comment-map)))))

(defn- expanded-comment [comment-data last-read-at collapsed-map & [last-comment?]]
  (let [collapsed-comment-map (get collapsed-map (:uuid comment-data))]
    (assoc comment-data :expanded (or ;; comment is unread
                                      (:unread comment-data)
                                      ;; Keep the comment expanded if it was already
                                      (:expanded collapsed-comment-map)
                                      ;; Do not collapse root comments
                                      (not (seq (:parent-uuid comment-data)))
                                      ;; User has not read the post yet
                                      (not (seq last-read-at))
                                      ;; Do not collapse last comments
                                      last-comment?))))

(defn- enrich-comment [comment-data last-read-at last-comment? collapsed-map]
  (as-> comment-data c
   (unread-comment c last-read-at collapsed-map)
   (expanded-comment c last-read-at collapsed-map last-comment?)))

(defun collapse-comments
  "Add a collapsed flag to every comment that is a reply and is not unread.
   Also add unread? flag to every unread one. Add a count of the collapsed
   comments to each root comment."

  ([last-read-at :guard string? comments :guard sequential?]
    (collapse-comments last-read-at comments {}))

  ;; Root comments
  ([last-read-at :guard string? comments :guard sequential? collapsed-map :guard map?]
   (mapv
    (fn [comment]
     (if (#{:thread :comment} (:resource-type comment))
       (as-> comment c
        (enrich-comment c last-read-at false collapsed-map)
        (collapse-comments c last-read-at (:thread-children c) collapsed-map)
        (assoc c :unread-count (count (filter :unread (:thread-children c))))
        (assoc c :collapsed-count (count (filter (comp not :expanded) (:thread-children c)))))
       comment))
    comments))

  ;; Children comments
  ([root-comment :guard :thread-children last-read-at :guard string? comments :guard coll? collapsed-map :guard map?]
   (update root-comment :thread-children
    #(mapv
     (fn [comment]
      (if (#{:thread :comment} (:resource-type comment))
        (enrich-comment comment last-read-at (= (:uuid comment) (-> root-comment :thread-children last :uuid)) collapsed-map)
        comment))
     %))))

(defun- strict-collapse-comments-inner
  ;; Root comments strct mode
  ([last-read-at :guard string? comments :guard sequential? collapsed-map :guard map?]
   (mapv (fn [comment]
          (as-> comment c
           (strict-collapse-comments-inner c last-read-at (:thread-children c) collapsed-map)
           (unread-comment c last-read-at collapsed-map)
           (assoc c :expanded (or (some :unread (:thread-children c))
                                  (:unread c)))
           (assoc c :unread-count (count (filter :unread (:thread-children c))))))
    comments))
  ;; Children strict mode
  ([root-comment :guard :thread-children last-read-at :guard string? comments :guard sequential? collapsed-map :guard map?]
   (update root-comment :thread-children
    (fn [children]
     (mapv
       #(-> %
         (unread-comment last-read-at collapsed-map)
         (expanded-comment last-read-at collapsed-map))
       children)))))

(defn- collapsed-count [comment]
  (assoc comment :collapsed-count (count (filter (comp not :expanded) (:thread-children comment)))))

(defun strict-collapse-comments
  "Add a :expanded and an :unread flag to every comment that is not unread.
   General rules are:
   - all unread comments are expanded
   - all read comments are hidden except if:
     - the root comment of a thread that has unread comments
     - the most recent comment if none is unread"

  ([last-read-at :guard string? comments :guard sequential?]
    (strict-collapse-comments last-read-at comments {}))

  ;; Root comments strct mode
  ([last-read-at :guard string? comments :guard sequential? collapsed-map :guard map?]
   (let [cms (strict-collapse-comments-inner last-read-at comments collapsed-map)
         all-comments (ungroup-comments cms)
         has-expanded? (some :expanded all-comments)
         most-recent-comment (when-not has-expanded?
                               (->> all-comments (sort-by :created-at) last :uuid))
         check-fn #(assoc % :expanded (if (= (:uuid %) most-recent-comment) true (:expanded %)))]
     (mapv (fn [comment]
              (as-> comment c
               (if has-expanded?
                 c
                 (check-fn c))
               (if has-expanded?
                 c
                 (update c :thread-children #(mapv check-fn %)))
               (collapsed-count c)))
        cms))))

(defun add-comment-focus-value
  ([prefix :guard string? comment-data :guard map?]
   (add-comment-focus-value prefix (:resource-uuid comment-data) (:parent-uuid comment-data) (:uuid comment-data)))

  ([prefix :guard string? entry-uuid :guard string?]
   (add-comment-focus-value prefix entry-uuid nil nil))

  ([prefix :guard string? entry-uuid parent-comment-uuid]
   (add-comment-focus-value prefix entry-uuid parent-comment-uuid nil))

  ([prefix :guard string? entry-uuid :guard string? parent-comment-uuid edit-comment-uuid]
   (str prefix "-" (dis/add-comment-string-key entry-uuid parent-comment-uuid edit-comment-uuid))))