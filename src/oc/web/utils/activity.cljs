(ns oc.web.utils.activity
  (:require [cuerdas.core :as s]
            [defun.core :refer (defun)]
            [cljs-time.format :as time-format]
            [oc.web.lib.jwt :as jwt]
            [oc.lib.user :as user-lib]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.utils.user :as user-utils]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.ziggeo :as ziggeo]
            [oc.web.lib.responsive :as responsive]
            [oc.web.utils.comment :as comment-utils]))

(def headline-placeholder "Add a title")

(def empty-body-html "<p><br/></p>")

;; Posts separators

(defn show-separators?

  ([container-slug-or-href] (show-separators? container-slug-or-href (router/current-sort-type)))

  ([container-slug-or-href sort-type]
   (and ;; only on board/containers/contributions pages
        container-slug-or-href
        ;; never on mobile
        (not (responsive/is-mobile-size?))
        ;; on All posts and Following only on NOT recent activity sort
        (or (and (not= sort-type dis/recent-activity-sort)
                 (#{"all-posts" "following" "unfollowing"} (name container-slug-or-href)))
            (and (string? container-slug-or-href)
                 (not (.match container-slug-or-href #"(?i)/(entries|following|unfollowing)/?\?.*sort=activity"))))
        ;; Never on inbox and bookmarks
        (not (or (#{"inbox" "bookmarks" utils/default-drafts-board-slug} (name container-slug-or-href))
                 (and (string? container-slug-or-href)
                      (or (.match container-slug-or-href #"(?i)/(inbox|bookmarks)(/|$)")
                          (.match container-slug-or-href #"(?i)/u/(\d|[a-f]){4}-(\d|[a-f]){4}-(\d|[a-f]){4}(/|$)"))))))))

(defn- post-month-date-from-date [post-date]
  (doto post-date
    ;; Reset day to first of the month
    (.setDate 1)
    ;; Reset time to midnight
    (.setHours 0)
    (.setMinutes 0)
    (.setSeconds 0)
    (.setMilliseconds 0)))

(defn- separator-from-date [d last-monday two-weeks-ago first-month]
  (let [now (utils/js-date)
        month-string (utils/full-month-string (inc (.getMonth d)))]
    (cond
      (> d last-monday)
      {:label "Recent"
       :content-type "separator"
       :date last-monday}
      (> d two-weeks-ago)
      {:label "Last week"
       :content-type "separator"
       :date two-weeks-ago}
      (> d first-month)
      {:label "2 weeks ago"
       :content-type "separator"
       :date first-month}
      (and (= (.getMonth now) (.getMonth d))
           (= (.getFullYear now) (.getFullYear d)))
      {:label "This month"
       :content-type "separator"
       :date (post-month-date-from-date d)}
      (= (.getFullYear now) (.getFullYear d))
      {:label month-string
       :content-type "separator"
       :date (post-month-date-from-date d)}
      :else
      {:label (str month-string ", " (.getFullYear d))
       :content-type "separator"
       :date (post-month-date-from-date d)})))

(defn- add-post-to-separators [post-data separators-map last-monday two-weeks-ago first-month]
  (let [post-date (utils/js-date (:published-at post-data))]
    (if (and (seq separators-map)
             (> post-date (:date (last separators-map))))
      (update-in separators-map [(dec (count separators-map)) :posts-list] #(-> % (conj (:uuid post-data)) vec))
      (vec
       (conj separators-map
        (assoc (separator-from-date post-date last-monday two-weeks-ago first-month)
         :posts-list [(:uuid post-data)]))))))

(defn grouped-posts [container-data]
  (let [sorted-post-uuids (:posts-list container-data)
        sorted-posts-list (mapv #(or (get-in container-data [:fixed-items %])
                                     (dis/activity-data %))
                           sorted-post-uuids)

        last-monday (utils/js-date)
        _last-monday (doto last-monday
                       (.setDate (- (.getDate last-monday)
                                    ; First saturday before now
                                    (-> (.getDay last-monday) (+ 8) (mod 7))))
                       (.setHours 23)
                       (.setMinutes 59)
                       (.setSeconds 59)
                       (.setMilliseconds 999))

        two-weeks-ago (utils/js-date)
        _two-weeks-ago (doto two-weeks-ago
                         (.setDate (- (.getDate two-weeks-ago)
                                      ;; Saturday before last one
                                      (-> (.getDay two-weeks-ago) (+ 8) (mod 7) (+ 7))))
                         ;; Reset time to midnight
                         (.setHours 23)
                         (.setMinutes 59)
                         (.setSeconds 59)
                         (.setMilliseconds 999))

        first-month (utils/js-date)
        _first-month (doto first-month
                       (.setDate (- (.getDate first-month)
                                    (-> (.getDay first-month) (+ 8) (mod 7) (+ 14))))
                       ;; Reset time to midnight
                       (.setHours 23)
                       (.setMinutes 59)
                       (.setSeconds 59)
                       (.setMilliseconds 999))

        last-date (:published-at (last sorted-posts-list))
        separators-data (loop [separators []
                               posts sorted-posts-list]
                          (if (empty? posts)
                            separators
                            (recur (add-post-to-separators (first posts) separators last-monday two-weeks-ago first-month)
                                   (rest posts))))]
        (vec (rest ;; Always remove the first label
         (mapcat #(concat [(dissoc % :posts-list)] (remove nil? (:posts-list %))) separators-data)))))

;; 

(defn is-published? [entry-data]
  (= (:status entry-data) "published"))

(defun is-publisher?

  ([entry-data :guard map?]
   (when (jwt/jwt)
     (is-publisher? (jwt/user-id) entry-data)))

  ([user-data :guard :user-id entry-data :guard map?]
   (is-publisher? (:user-id user-data) entry-data))

  ([user-id :guard string? entry-data :guard map?]
   (= (jwt/user-id) (-> entry-data :publisher :user-id))))

(defn board-by-uuid [board-uuid]
  (let [org-data (dis/org-data)
        boards (:boards org-data)]
    (first (filter #(= (:uuid %) board-uuid) boards))))

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

(defn post-unseen?
  "An entry is new if its uuid is contained in container's unseen."
  [entry changes]
  (let [board-uuid (:board-uuid entry)
        board-change-data (get changes board-uuid {})
        board-unseen (:unseen board-change-data)
        user-id (jwt/user-id)]
    (and (utils/in? board-unseen (:uuid entry))
         (not= (:user-id (:publisher entry)) user-id))))

(defn post-unread?
  "An entry is new if its uuid is contained in container's unread."
  [entry changes]
  (let [board-uuid (:board-uuid entry)
        board-change-data (get changes board-uuid {})
        board-unread (:unread board-change-data)]
    (utils/in? board-unread (:uuid entry))))

(defun comment-unread?
  "An entry is new if its uuid is contained in container's unread."
  ([comment-data :guard map? last-read-at]
   (comment-unread? (:created-at comment-data) last-read-at))
  ([iso-date last-read-at]
   (letfn [(get-time [t] (.getTime (utils/js-date t)))]
     (< (get-time last-read-at)
        (get-time iso-date)))))

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

(defn has-attachments? [data]
  (seq (:attachments data)))

(defn has-headline? [data]
  (-> data :headline s/trim s/blank?))

(defn empty-body? [body]
  (boolean
   (or (s/blank? body)
       (re-matches #"(?i)^\s*<p[^>]*><br\s*/?></p>\s*$" body))))

(defn has-body? [data]
  (not (empty-body? (:body data))))

(defn has-text? [data]
  (or (has-headline? data)
      (has-body? data)))

(defn has-content? [data]
  (or (some? (:video-id data))
      (has-attachments? data)
      (has-text? data)))

(defn fix-entry
  "Add `:read-only`, `:board-slug`, `:board-name` and `:content-type` keys to the entry map."
  ([entry-data board-data changes]
   (fix-entry entry-data board-data changes (dis/active-users)))
  ([entry-data board-data changes active-users]
  (if (= entry-data :404)
    entry-data
    (let [comments-link (utils/link-for (:links entry-data) "comments")
          add-comment-link (utils/link-for (:links entry-data) "create" "POST")
          fixed-board-uuid (or (:board-uuid entry-data) (:uuid board-data))
          fixed-board-slug (or (:board-slug entry-data) (:slug board-data))
          fixed-board-name (or (:board-name entry-data) (:name board-data))
          fixed-board-access (if (is-published? entry-data)
                               (or (:board-access entry-data) (:access board-data))
                               "private")
          fixed-publisher-board (or (:publisher-board entry-data) (:publisher-board board-data) false)
          [has-images stream-view-body] (body-for-stream-view (:body entry-data))
          is-uploading-video? (dis/uploading-video-data (:video-id entry-data))
          fixed-video-id (:video-id entry-data)
          fixed-publisher (get active-users (-> entry-data :publisher :user-id))]
      (when (seq fixed-video-id)
        (ziggeo/init-ziggeo true))
      (-> entry-data
        (assoc :content-type "entry")
        (update :publisher merge fixed-publisher)
        (assoc :unseen (post-unseen? (assoc entry-data :board-uuid fixed-board-uuid) changes))
        (assoc :unread (post-unread? (assoc entry-data :board-uuid fixed-board-uuid) changes))
        (assoc :read-only (readonly-entry? (:links entry-data)))
        (assoc :board-uuid fixed-board-uuid)
        (assoc :board-slug fixed-board-slug)
        (assoc :board-name fixed-board-name)
        (assoc :publisher-board fixed-publisher-board)
        (assoc :board-access fixed-board-access)
        (assoc :has-comments (boolean comments-link))
        (assoc :can-comment (boolean add-comment-link))
        (assoc :stream-view-body stream-view-body)
        (assoc :body-has-images has-images)
        (assoc :fixed-video-id fixed-video-id)
        (assoc :has-headline (has-headline? entry-data))
        (assoc :comments (comment-utils/sort-comments (:comments entry-data))))))))

(defn fix-board
  "Parse board data coming from the API."
  ([board-data]
   (fix-board board-data {} (dis/active-users) (dis/follow-boards-list)))

  ([board-data change-data]
   (fix-board board-data change-data (dis/active-users) (dis/follow-boards-list)))

  ([board-data change-data active-users]
   (fix-board board-data change-data active-users (dis/follow-boards-list)))

  ([board-data change-data active-users follow-boards-list & [direction]]
    (let [links (:links board-data)
          with-fixed-activities (reduce #(assoc-in %1 [:fixed-items (:uuid %2)]
                                          (fix-entry %2 {:slug (:board-slug %2)
                                                         :name (:board-name %2)
                                                         :uuid (:board-uuid %2)
                                                         :publisher-board (:publisher-board %2)}
                                           change-data
                                           active-users))
                                 board-data
                                 (:entries board-data))
          next-links (when direction
                      (vec
                       (remove
                        #(if (= direction :down) (= (:rel %) "previous") (= (:rel %) "next"))
                        links)))
          link-to-move (when direction
                         (if (= direction :down)
                           (utils/link-for (:old-links board-data) "previous")
                           (utils/link-for (:old-links board-data) "next")))
          fixed-next-links (if direction
                             (if link-to-move
                               (vec (conj next-links link-to-move))
                               next-links)
                             links)
          items-list (if (contains? board-data :entries)
                       ;; In case we are parsing a fresh response from server
                       (map :uuid (:entries board-data))
                       ;; If we are re-parsing existing data for updated related data
                       ;; ie: change, org or active-users
                       (:posts-list board-data))
          follow-board-uuids (set (map :uuid follow-boards-list))]
      (as-> with-fixed-activities b
        (assoc b :read-only (readonly-board? links))
        (dissoc b :old-links :items)
        (assoc b :posts-list (vec (case direction
                                   :up (concat items-list (:posts-list board-data))
                                   :down (concat (:posts-list board-data) items-list)
                                   items-list)))
        (if direction
          (assoc b :saved-items (count (:posts-list board-data)))
          b)
        (if (show-separators? (:slug board-data))
          (update b :items-to-render grouped-posts)
          (assoc b :items-to-render (:posts-list b)))
        (assoc b :following (boolean (follow-board-uuids (:uuid board-data))))))))

(defn fix-contributions
  "Parse data coming from the API for a certain user's posts."
  ([contributions-data]
   (fix-contributions contributions-data {} (dis/org-data) (dis/active-users) (dis/follow-publishers-list)))

  ([contributions-data change-data]
   (fix-contributions contributions-data change-data (dis/org-data) (dis/active-users) (dis/follow-publishers-list)))

  ([contributions-data change-data org-data]
   (fix-contributions contributions-data change-data org-data (dis/active-users) (dis/follow-publishers-list)))

  ([contributions-data change-data org-data active-users]
   (fix-contributions contributions-data change-data org-data active-users (dis/follow-publishers-list)))

  ([contributions-data change-data org-data active-users follow-publishers-list & [direction]]
    (let [all-boards (:boards org-data)
          with-fixed-activities (reduce (fn [ret item]
                                          (let [board-data (first (filterv #(= (:slug %) (:board-slug item))
                                                            all-boards))]
                                            (assoc-in ret [:fixed-items (:uuid item)]
                                             (fix-entry item board-data change-data active-users))))
                                 contributions-data
                                 (:items contributions-data))
          next-links (when direction
                      (vec
                       (remove
                        #(if (= direction :down) (= (:rel %) "previous") (= (:rel %) "next"))
                        (:links contributions-data))))
          link-to-move (when direction
                         (if (= direction :down)
                           (utils/link-for (:old-links contributions-data) "previous")
                           (utils/link-for (:old-links contributions-data) "next")))
          fixed-next-links (if direction
                             (if link-to-move
                               (vec (conj next-links link-to-move))
                               next-links)
                             (:links contributions-data))
          items-list (if (contains? contributions-data :items)
                       ;; In case we are parsing a fresh response from server
                       (map :uuid (:items contributions-data))
                       ;; If we are re-parsing existing data for updated related data
                       ;; ie: change, org or active-users
                       (:posts-list contributions-data))
          follow-publishers-ids (set (map :user-id follow-publishers-list))]
      (as-> with-fixed-activities c
        (dissoc :old-links :items)
        (assoc c :links fixed-next-links)
        (assoc c :posts-list (vec (case direction
                                   :up (concat items-list (:posts-list contributions-data))
                                   :down (concat (:posts-list contributions-data) items-list)
                                   items-list)))
        (if direction
          (assoc c :saved-items (count (:posts-list contributions-data)))
          c)
        (if (show-separators? (:href contributions-data))
          (assoc c :items-to-render (grouped-posts c))
          (assoc c :items-to-render (:posts-list c)))
        (assoc c :following (boolean (follow-publishers-ids (:author-uuid contributions-data))))))))

(defn fix-container
  "Parse container data coming from the API, like All posts or Must see."
  ([container-data]
   (fix-container container-data {} (dis/org-data) (dis/active-users) nil))

  ([container-data change-data]
   (fix-container container-data change-data (dis/org-data) (dis/active-users) nil))

  ([container-data change-data org-data]
   (fix-container container-data change-data org-data (dis/active-users) nil))

  ([container-data change-data org-data active-users sort-type & [ direction]]
    (let [all-boards (:boards org-data)
          with-fixed-activities (reduce (fn [ret item]
                                          (let [board-data (some #(when (= (:slug %) (:board-slug item)) %)
                                                            all-boards)]
                                            (assoc-in ret [:fixed-items (:uuid item)]
                                             (fix-entry item board-data change-data active-users))))
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
          items-list (if (contains? container-data :items)
                       ;; In case we are parsing a fresh response from server
                       (map :uuid (:items container-data))
                       ;; If we are re-parsing existing data for updated related data
                       ;; ie: change, org or active-users
                       (:posts-list container-data))]
      (as-> with-fixed-activities c
       (dissoc c :old-links :items)
       (assoc c :links fixed-next-links)
       (assoc c :posts-list (vec (case direction
                                  :up (concat items-list (:posts-list container-data))
                                  :down (concat (:posts-list container-data) items-list)
                                  items-list)))
       (if direction
         (assoc c :saved-items (count (:posts-list container-data)))
         c)
       (if (show-separators? (:href container-data) sort-type)
         (assoc c :items-to-render (grouped-posts c))
         (assoc c :items-to-render (:posts-list c)))))))

(defn fix-thread [thread entry-data active-users]
  (let [fixed-author (get active-users (-> thread :author :user-id))
        comment-unread (comment-unread? thread (:last-read-at entry-data))
        thread-unread (comment-unread? (:last-activity-at thread) (:last-read-at entry-data))]
    (-> thread
      (assoc :content-type "comment")
      (assoc :entry entry-data)
      (update :author merge fixed-author)
      (assoc :unread comment-unread)
      (assoc :unread-thread thread-unread))))

(defn- caught-up-map
  ([] (caught-up-map nil))
  ([n]
   (let [t (if (:last-activity-at n)
             (-> n :last-activity-at utils/js-date .getTime inc utils/js-date .toISOString)
             (utils/as-of-now))]
     {:content-type :caught-up :last-activity-at t})))

(defn fix-threads
  "
  Threads data looks like this:
  {:total-count 100
   :entries [...List of entry maps...]
   :items [{...Comment map...
            :reply-count 1
            :resource-uuid uuid
            :last-activity-at most-recent-created-at}]
   :links []}
  "
  ([threads-data change-data org-data active-users sort-type]
   (fix-threads threads-data change-data org-data active-users sort-type nil))
  ([threads-data change-data org-data active-users sort-type direction]
    (let [all-boards (:boards org-data)
          entries (atom [])
          with-fixed-entries (reduce (fn [ret item]
                                       (let [board-data (some #(when (= (:slug %) (:board-slug item)) %)
                                                         all-boards)
                                             fixed-entry (fix-entry item board-data change-data active-users)]
                                         (swap! entries conj fixed-entry)
                                         (assoc-in ret [:fixed-entries (:uuid item)] fixed-entry)))
                              threads-data
                              (:entries threads-data))
          with-fixed-items (reduce (fn [ret item]
                                     (if-let [entry-data (get-in with-fixed-entries [:fixed-entries (:resource-uuid item)])]
                                       (let [fixed-thread (fix-thread item entry-data active-users)]
                                       (assoc-in ret [:fixed-items (:uuid item)]
                                        (fix-thread item entry-data active-users)))
                                       ret))
                            with-fixed-entries
                            (:items with-fixed-entries))
          next-links (when direction
                      (vec
                       (remove
                        #(if (= direction :down) (= (:rel %) "previous") (= (:rel %) "next"))
                        (:links threads-data))))
          link-to-move (when direction
                         (if (= direction :down)
                           (utils/link-for (:old-links threads-data) "previous")
                           (utils/link-for (:old-links threads-data) "next")))
          fixed-next-links (if direction
                             (if link-to-move
                               (vec (conj next-links link-to-move))
                               next-links)
                             (:links threads-data))
          items-list (if (contains? threads-data :items)
                       ;; In case we are parsing a fresh response from server
                       (remove nil? (map :uuid (:items threads-data)))
                       ;; If we are re-parsing existing data for updated related data
                       ;; ie: change, org or active-users
                       (filter string? (:threads-list threads-data)))
          threads-list (vec (case direction
                             :up (concat items-list (:threads-list threads-data))
                             :down (concat (:threads-list threads-data) items-list)
                             items-list))
          threads-with-separators (when (seq threads-list)
                                    (loop [to-items []
                                           from-items threads-list]
                                      (if (or (->> from-items first (get (:fixed-items with-fixed-items)) :unread-thread not)
                                              (not (seq from-items)))
                                        (concat to-items [(caught-up-map (last to-items))] from-items)
                                        (recur (vec (conj to-items (first from-items)))
                                               (rest from-items)))))]
      (doseq [e @entries] (utils/after 0 #(comment-utils/get-comments e)))
      (as-> with-fixed-items t
       (dissoc t :old-links :entries :items)
       (assoc t :links fixed-next-links)
       (assoc t :threads-list threads-list)
       (assoc t :no-virtualized-steam true)
       (update t :entries-list #(remove nil? (map :uuid (vals (:fixed-entries t)))))
       (if (seq direction)
         (assoc t :saved-items (count threads-list))
         t)
       (assoc t :items-to-render threads-with-separators)))))

(defn get-comments [activity-data comments-data]
  (or (-> comments-data
          (get (:uuid activity-data))
          :sorted-comments)
      (:comments activity-data)))

(defn is-element-visible?
   "Given a DOM element return true if it's actually visible in the viewport."
  [el]
  (let [rect (.getBoundingClientRect el)
        zero-pos? #(or (zero? %)
                       (pos? %))
        doc-element (.-documentElement js/document)
        win-height (or (.-clientHeight doc-element)
                       (.-innerHeight js/window))]
    (or      ;; Item top is more then the navbar height
        (and (>= (.-top rect) responsive/navbar-height)
             ;; and less than the screen height
             (< (.-top rect) win-height))
             ;; Item bottom is less than the screen height
        (and (<= (.-bottom rect) win-height)
         ;; and more than the navigation bar to
         (> (.-bottom rect) responsive/navbar-height)))))

(defn clean-who-reads-count-ids
  "Given a list of items we want to request the who reads count
   and the current read data, filter out the ids we already have data."
  [item-ids activities-read-data]
  (let [all-items (set (keys activities-read-data))
        request-set (set item-ids)
        diff-ids (clojure.set/difference request-set all-items)]
    (vec diff-ids)))

;; Last used section

(defn last-used-section []
  (when-let [org-slug (router/current-org-slug)]
    (let [cookie-name (router/last-used-board-slug-cookie org-slug)]
      (cook/get-cookie cookie-name))))

(defn save-last-used-section [section-slug]
  (let [org-slug (router/current-org-slug)
        last-board-cookie (router/last-used-board-slug-cookie org-slug)]
    (if section-slug
      (cook/set-cookie! last-board-cookie section-slug (* 60 60 24 365))
      (cook/remove-cookie! last-board-cookie))))

(def iso-format (time-format/formatters :date-time))

(def date-format (time-format/formatter "MMMM d"))

(def date-format-year (time-format/formatter "MMMM d YYYY"))

(defn post-date [timestamp & [force-year]]
  (let [d (time-format/parse iso-format timestamp)
        now-year (.getFullYear (utils/js-date))
        timestamp-year (.getFullYear (utils/js-date timestamp))
        show-year (or force-year (not= now-year timestamp-year))
        f (if show-year date-format-year date-format)]
    (time-format/unparse f d)))