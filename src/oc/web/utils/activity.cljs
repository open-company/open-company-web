(ns oc.web.utils.activity
  (:require [cuerdas.core :as s]
            [cljs-time.format :as f]
            [cljs-time.core :as time]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

(defn get-first-body-thumbnail
  "Given an entry body get the first thumbnail available.
  Thumbnail type: image, video or chart."
  [activity-body]
  (let [is-ap (= (router/current-board-slug) "all-posts")
        $body (js/$ (str "<div>" activity-body "</div>"))
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

(defn get-sorted-activities [all-posts-data]
  (vec (sort compare-activities (vals (:fixed-items all-posts-data)))))

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
        board-unseen (:unseen board-change-data)]
    (utils/in? board-unseen (:uuid entry))))

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
        [has-images stream-view-body] (body-for-stream-view (:body entry-data))]
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
      (assoc :body-has-images has-images))))

(defn fix-board
  "Add `:read-only` and fix each entry of the board, then create a :fixed-entries map with the entry UUID."
  ([board-data] (fix-board board-data {}))

  ([board-data changes]
     (let [links (:links board-data)
           read-only (readonly-board? links)
           with-read-only (assoc board-data :read-only read-only)
           fixed-entries (zipmap
                          (map :uuid (:entries board-data))
                          (map #(fix-entry % board-data changes) (:entries board-data)))
           with-fixed-entries (assoc with-read-only :fixed-items fixed-entries)]
       with-fixed-entries)))

(defn fix-all-posts
  "Fix org data coming from the API."
  ([all-posts-data]
   (fix-all-posts all-posts-data {}))
  ([all-posts-data change-data]
    (let [fixed-activities-list (map
                                 #(fix-entry % {:slug (:board-slug %) :name (:board-name %)} change-data)
                                 (:items all-posts-data))
          fixed-activities (zipmap (map :uuid fixed-activities-list) fixed-activities-list)
          with-fixed-activities (assoc all-posts-data :fixed-items fixed-activities)]
      with-fixed-activities)))

(defn get-comments [activity-data comments-data]
  (or (-> comments-data
          (get (:uuid activity-data))
          :sorted-comments)
      (:comments activity-data)))