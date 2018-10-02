(ns oc.web.utils.activity
  (:require [cuerdas.core :as s]
            [cljs-time.format :as f]
            [cljs-time.core :as time]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.responsive :as responsive]))

(defn board-by-uuid [board-uuid]
  (let [org-data (dis/org-data)
        boards (:boards org-data)]
    (first (filter #(= (:uuid %) board-uuid) boards))))

(defn get-first-body-thumbnail
  "Given an entry body get the first thumbnail available.
  Thumbnail type: image, video or chart."
  [activity-body]
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
                   :thumbnail (if (.data $el "thumbnail")
                                (.data $el "thumbnail")
                                (.attr $el "src"))})))
            (reset! found {:type (.data $el "media-type") :thumbnail (.data $el "thumbnail")})))))
    @found))

(defn reset-truncate-body
  "Reset dotdotdot for the give body element."
  [body-el]
  (let [$body-els (js/$ ">*" body-el)]
    (.each $body-els (fn [idx el]
      (this-as this
        (.trigger (js/$ this) "destroy"))))))

(def default-body-height 72)
(def default-all-posts-body-height 144)
(def default-draft-body-height 48)

(defn truncate-body
  "Given a body element truncate the body. It iterate on the elements
  of the body and truncate the first exceeded element found.
  This is to avoid truncating a DIV with multiple spaced P inside,
  since this is a problem for the dotdotdot library that we are using."
  [body-el height]
  (reset-truncate-body body-el)
  (.dotdotdot (js/$ body-el)
    #js {:height height
         :wrap "word"
         :watch true
         :ellipsis "..."}))

(defn icon-for-mimetype
  "Thanks to https://gist.github.com/colemanw/9c9a12aae16a4bfe2678de86b661d922"
  [mimetype]
  (case (s/lower mimetype)
    ;; Media
    "image" "fa-file-image-o"
    "image/png" "fa-file-image-o"
    "image/bmp" "fa-file-image-o"
    "image/jpg" "fa-file-image-o"
    "image/jpeg" "fa-file-image-o"
    "image/gif" "fa-file-image-o"
    ".jpg" "fa-file-image-o"
    "audio" "fa-file-audio-o"
    "video" "fa-file-video-o"
    ;; Documents
    "application/pdf" "fa-file-pdf-o"
    "application/msword" "fa-file-word-o",
    "application/vnd.ms-word" "fa-file-word-o",
    "application/vnd.oasis.opendocument.text" "fa-file-word-o",
    "application/vnd.openxmlformats-officedocument.wordprocessingml" "fa-file-word-o",
    "application/vnd.ms-excel" "fa-file-excel-o",
    "application/vnd.openxmlformats-officedocument.spreadsheetml" "fa-file-excel-o",
    "application/vnd.oasis.opendocument.spreadsheet" "fa-file-excel-o",
    "application/vnd.ms-powerpoint" "fa-file-powerpoint-o",
    "application/vnd.openxmlformats-officedocument.presentationml" "fa-file-powerpoint-o",
    "application/vnd.oasis.opendocument.presentation" "fa-file-powerpoint-o",
    "text/plain" "fa-file-text-o",
    "text/html" "fa-file-code-o",
    "application/json" "fa-file-code-o",
    ;; Archives
    "application/gzip" "fa-file-archive-o",
    "application/zip" "fa-file-archive-o",
    ;; Code
    "text/css" "fa-file-code-o"
    "text/php" "fa-file-code-o"
    ;; Generic case
    "fa-file"))

(defn get-activity-date [activity]
  (or (:published-at activity) (:created-at activity)))

(defn compare-activities [act-1 act-2]
  (let [time-1 (get-activity-date act-1)
        time-2 (get-activity-date act-2)]
    (compare time-2 time-1)))

(defn get-sorted-activities [posts-data]
  (vec (sort compare-activities (vals posts-data))))

(defn readonly-board? [links]
  (let [new-link (utils/link-for links "create")
        update-link (utils/link-for links "partial-update")
        delete-link (utils/link-for links "delete")]
    (and (nil? new-link)
         (nil? update-link)
         (nil? delete-link))))

(defn readonly-entry? [links]
  (let [partial-update (utils/link-for links "partial-update")
        delete (utils/link-for links "delete")]
    (and (nil? partial-update) (nil? delete))))

(defn post-new?
  "An entry is new if its uuid is contained in container's unseen."
  [entry changes]
  (let [board-uuid (:board-uuid entry)
        board-change-data (get changes board-uuid {})
        board-unseen (:unseen board-change-data)
        user-id (jwt/user-id)]
    (and (utils/in? board-unseen (:uuid entry))
         (not= (:user-id (:publisher entry)) user-id))))

(defn body-for-stream-view [inner-html]
  (if (seq inner-html)
    (let [$container (.html (js/$ "<div/>") inner-html)
          _ (.append (js/$ (.-body js/document)) $container)
          has-images (pos? (.-length (.find $container "img")))
          _ (.remove (js/$ "img" $container))
          empty-paragraph-rx (js/RegExp "^(<br\\s*/?>)?$" "i")
          _ (.each (.find $container "p")
             #(this-as this
                (let [$this (js/$ this)]
                  (when (.match (.html $this) empty-paragraph-rx)
                    (.remove $this)))))
          cleaned-body (.html $container)
          _ (.detach $container)]
      [has-images cleaned-body])
    [false inner-html]))

(defn fix-entry
  "Add `:read-only`, `:board-slug`, `:board-name` and `:content-type` keys to the entry map."
  [entry-data board-data changes]
  (let [comments-link (utils/link-for (:links entry-data) "comments")
        add-comment-link (utils/link-for (:links entry-data) "create" "POST")
        fixed-board-uuid (or (:board-uuid entry-data) (:uuid board-data))
        fixed-board-slug (or (:board-slug entry-data) (:slug board-data))
        fixed-board-name (or (:board-name entry-data) (:name board-data))
        [has-images stream-view-body] (body-for-stream-view (:body entry-data))
        is-uploading-video? (dis/uploading-video-data (:video-id entry-data))
        fixed-video-id (when (or (not (:video-error entry-data))
                                 is-uploading-video?)
                         (:video-id entry-data))]
    (-> entry-data
      (assoc :content-type "entry")
      (assoc :new (post-new? (assoc entry-data :board-uuid fixed-board-uuid) changes))
      (assoc :read-only (readonly-entry? (:links entry-data)))
      (assoc :board-uuid fixed-board-uuid)
      (assoc :board-slug fixed-board-slug)
      (assoc :board-name fixed-board-name)
      (assoc :has-comments (boolean comments-link))
      (assoc :can-comment (boolean add-comment-link))
      (assoc :stream-view-body stream-view-body)
      (assoc :body-has-images has-images)
      (assoc :fixed-video-id fixed-video-id))))

(defn fix-board
  "Add `:read-only` and fix each entry of the board, then create a :fixed-items map with the entry UUID."
  ([board-data]
    (fix-board board-data {}))

  ([board-data changes]
    (let [links (:links board-data)
          read-only (readonly-board? links)
          with-read-only (assoc board-data :read-only read-only)
          with-fixed-entries (reduce #(assoc-in %1 [:fixed-items (:uuid %2)]
                                       (fix-entry %2 board-data changes))
                                       with-read-only (:entries board-data))
          with-entry-count (if (:entries board-data)
                             (assoc with-fixed-entries
                              :entry-count
                              (count (:fixed-items with-fixed-entries)))
                             with-fixed-entries)
          without-entries (dissoc with-entry-count :entries)]
      without-entries)))

(defn fix-container
  "Fix container data coming from the API."
  ([container-data]
   (fix-container container-data {}))
  ([container-data change-data & [direction]]
    (let [with-fixed-activities (reduce #(assoc-in %1 [:fixed-items (:uuid %2)]
                                          (fix-entry %2 {:slug (:board-slug %2)
                                                         :name (:board-name %2)
                                                         :uuid (:board-uuid %2)}
                                           change-data))
                                 container-data
                                 (:items container-data))
          next-links (when direction
                      (vec
                       (remove
                        #(if (= direction :down) (= (:rel %) "previous") (= (:rel %) "next"))
                        (:links container-data))))
          link-to-move (when direction
                         (if (= direction :down)
                           (utils/link-for (:old-links container-data) "previous")
                           (utils/link-for (:old-links container-data) "next")))
          fixed-next-links (if direction
                             (if link-to-move
                               (vec (conj next-links link-to-move))
                               next-links)
                             (:links container-data))
          with-links (-> with-fixed-activities
                       (dissoc :old-links)
                       (assoc :links fixed-next-links))
          new-items (map :uuid (:items container-data))
          without-items (dissoc with-links :items)
          with-posts-list (assoc without-items :posts-list (into []
                                                             (case direction
                                                              :up (concat new-items (:posts-list container-data))
                                                              :down (concat (:posts-list container-data) new-items)
                                                              new-items)))
          with-saved-items (if direction
                             (assoc with-posts-list :saved-items (count (:posts-list container-data)))
                             with-posts-list)]
      with-saved-items)))

(defn get-comments [activity-data comments-data]
  (or (-> comments-data
          (get (:uuid activity-data))
          :sorted-comments)
      (:comments activity-data)))

(defn- is-element-visible?
   "Given a DOM element return true if it's actually visible in the viewport."
  [el]
  (let [rect (.getBoundingClientRect el)
        zero-pos? #(or (zero? %)
                       (pos? %))
        doc-element (.-documentElement js/document)
        win-height (or (.-clientHeight doc-element)
                       (.-innerHeight js/window))
        win-width (or (.-clientWidth doc-element)
                      (.-innerWidth js/window))]
    (and ;; Item starts below the navbar
         (>= (.-top rect) responsive/navbar-height)
         ;; Item left is not out of the screen
         (zero-pos? (.-left rect))
         ;; Item bottom is less than the screen height
         (<= (.-bottom rect) win-height)
         ;; Item right is less than the screen width
         (<= (.-right rect) win-width))))

(defn- is-element-bottom-visible?
   "Given a DOM element return true if it's actually visible in the viewport."
  [el]
  (let [rect (.getBoundingClientRect el)
        zero-pos? #(or (zero? %)
                       (pos? %))
        doc-element (.-documentElement js/document)
        win-height (or (.-innerHeight js/window)
                       (.-clientHeight doc-element))
        win-width (or (.-innerWidth js/window)
                      (.-clientWidth doc-element))]
    (and ;; Item left is not out of the screen
         (zero-pos? (.-left rect))
         ;; Item bottom is less than the screen height
         (and (<= (.-bottom rect) win-height)
              ;; and more than the navigation bar to
              ;; make sure it's not hidden behind it
              (> (.-bottom rect) responsive/navbar-height))
         ;; Item right is less than the screen width
         (<= (.-right rect) win-width))))

(defn clean-who-reads-count-ids
  "Given a list of items we want to request the who reads count
   and the current read data, filter out the ids we already have data."
  [item-ids activities-read-data]
  (let [all-items (set (keys activities-read-data))
        request-set (set item-ids)
        diff-ids (clojure.set/difference request-set all-items)]
    (into [] diff-ids)))

;; Last used section

(defn last-used-section []
  (let [org-slug (router/current-org-slug)
        cookie-name (router/last-used-board-slug-cookie org-slug)]
    (cook/get-cookie cookie-name)))

(defn save-last-used-section [section-slug]
  (let [org-slug (router/current-org-slug)
        last-board-cookie (router/last-used-board-slug-cookie org-slug)]
    (if section-slug
      (cook/set-cookie! last-board-cookie section-slug (* 60 60 24 365))
      (cook/remove-cookie! last-board-cookie))))

(defn has-attachments? [data]
  (seq (:attachments data)))

(defn has-text? [data]
  (if (and (clojure.string/blank? (:headline data))
           (clojure.string/blank? (:body data)))
    false
    true))

(defn has-content? [data]
  (or (some? (:video-id data))
      (has-attachments? data)
      (has-text? data)))