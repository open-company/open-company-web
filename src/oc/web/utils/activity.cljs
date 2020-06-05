(ns oc.web.utils.activity
  (:require [cuerdas.core :as s]
            [defun.core :refer (defun)]
            [cljs-time.format :as time-format]
            [oc.web.lib.jwt :as jwt]
            [oc.lib.user :as user-lib]
            [oc.lib.html :as html-lib]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.utils.user :as user-utils]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.responsive :as responsive]
            [oc.web.utils.comment :as comment-utils]))

(def headline-placeholder "Add a title")

(def empty-body-html "<p><br></p>")

;; Posts separators

(defn show-separators?

  ([container-slug] (show-separators? container-slug (router/current-sort-type)))

  ([container-slug sort-type]
   (and ;; only on board/containers/contributions pages
        (not (s/blank? container-slug))
        ;; never on mobile
        (not (responsive/is-mobile-size?))
        ;; only on recently posted sorting
        (= sort-type dis/recently-posted-sort)
        ;; on All posts, Following and Unfollowing only
        (#{:all-posts :following :unfollowing} (keyword container-slug)))))

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
       :content-type :separator
       :date last-monday}
      (> d two-weeks-ago)
      {:label "Last week"
       :content-type :separator
       :date two-weeks-ago}
      (> d first-month)
      {:label "2 weeks ago"
       :content-type :separator
       :date first-month}
      (and (= (.getMonth now) (.getMonth d))
           (= (.getFullYear now) (.getFullYear d)))
      {:label "This month"
       :content-type :separator
       :date (post-month-date-from-date d)}
      (= (.getFullYear now) (.getFullYear d))
      {:label month-string
       :content-type :separator
       :date (post-month-date-from-date d)}
      :else
      {:label (str month-string ", " (.getFullYear d))
       :content-type :separator
       :date (post-month-date-from-date d)})))

(defn- add-post-to-separators [post-data separators-map last-monday two-weeks-ago first-month]
  (let [post-date (utils/js-date (:published-at post-data))
        item-data (select-keys post-data [:content-type :uuid :resource-uuid])]
    (if (and (seq separators-map)
             (> post-date (:date (last separators-map))))
      (update-in separators-map [(dec (count separators-map)) :posts-list] #(-> % (conj item-data) vec))
      (vec
       (conj separators-map
        (assoc (separator-from-date post-date last-monday two-weeks-ago first-month)
         :posts-list [item-data]))))))

(defn grouped-posts
  ([full-items-list]
   (let [items-list (map #(select-keys % [:content-type :uuid :resource-uuid]) full-items-list)
         items-map (zipmap (map :uuid full-items-list) full-items-list)]
     (grouped-posts items-list items-map)))
  ([items-list fixed-items]
   (let [sorted-posts-list (mapv #(or (get fixed-items (:uuid %))
                                      (dis/activity-data (:uuid %)))
                            items-list)

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
          (mapcat #(concat [(dissoc % :posts-list)] (remove nil? (:posts-list %))) separators-data))))))

(defn is-published? [entry-data]
  (= (:status entry-data) "published"))

(defun is-publisher?

  ([entry-data :guard map?]
   (when (jwt/jwt)
     (is-publisher? entry-data (jwt/user-id))))

  ([entry-author-id :guard string?]
   (when (jwt/jwt)
     (is-publisher? entry-author-id (jwt/user-id))))

  ([entry-data :guard map? user-data :guard :user-id]
   (is-publisher? entry-data (:user-id user-data)))

  ([entry-data :guard map? user-id :guard string?]
   (is-publisher? (-> entry-data :publisher :user-id)  user-id))

  ([publisher-id :guard string? user-id :guard string?]
   (= user-id publisher-id)))

(defun is-author?
  "Check if current user is the author of the entry/comment or whole thread."
  ([entity-data :guard :author]
   (when (jwt/jwt)
     (is-author? entity-data (jwt/user-id))))

  ([entity-data :guard :author user-data :guard :user-id]
   (is-author? (-> entity-data :author :user-id) (:user-id user-data)))

  ([entity-data :guard :author user-id :guard string?]
   (is-author? (-> entity-data :author :user-id) user-id))

  ([author-id :guard string? user-id :guard string?]
   (= user-id author-id)))

(defun is-monologue?
  ([thread-data :guard :author]
   (when (jwt/jwt)
    (is-monologue? thread-data (jwt/user-id))))
  ([thread-data :guard :author user-id :guard string?]
   (and (is-author? (-> thread-data :author :user-id) (jwt/user-id))
        (every? #(is-author? % user-id) (:reply-authors thread-data)))))

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
    (if board-unread
      (utils/in? board-unread (:uuid entry))
      (nil? (:last-read-at entry)))))

(defun comment-unread?
  "An entry is new if its uuid is contained in container's unread."
  ([comment-data :guard map? last-read-at]
   (comment-unread? (:created-at comment-data) last-read-at))
  ([iso-date last-read-at]
   (letfn [(get-time [t] (.getTime (new js/Date t)))]
     (< (get-time last-read-at)
        (get-time iso-date)))))

(defn has-attachments? [data]
  (seq (:attachments data)))

(defn has-headline? [data]
  (-> data :headline s/trim s/blank?))

(defn empty-body? [body]
  (boolean
   (or (s/blank? body)
       (re-matches #"(?i)^(\s*<p[^>]*>\s*(<br[^>]*/?>)*?\s*</p>\s*)*$" body))))

(defn has-body? [data]
  (not (empty-body? (:body data))))

(defn has-text? [data]
  (or (has-headline? data)
      (has-body? data)))

(defn has-content? [data]
  (or (some? (:video-id data))
      (has-attachments? data)
      (has-text? data)))

(defn- merge-items-lists
  "Given 2 list of reduced items (reduced since they contains only the uuid and content-type props)
   return a single list without duplicates depending on the direction"
  [new-items-list old-items-list direction]
  (if (seq new-items-list)
    (let [old-uuids (map :uuid old-items-list)
          new-uuids (map :uuid new-items-list)
          next-items-uuids (if (= direction :down)
                             (distinct (concat old-uuids new-uuids))
                             (reverse (distinct (concat (reverse old-uuids) (reverse new-uuids)))))
          all-items-list (concat old-items-list new-items-list)
          items-map (zipmap (map :uuid all-items-list) all-items-list)]
      (mapv items-map next-items-uuids))
    old-items-list))

(def default-caught-up-message "You’re up to date")

(defn- next-activity-timestamp [prev-item]
  (if (:last-activity-at prev-item)
   (-> prev-item :last-activity-at utils/js-date .getTime inc utils/js-date .toISOString)
   (utils/as-of-now)))

(defn- caught-up-map
  ([] (caught-up-map nil false default-caught-up-message))
  ([n] (caught-up-map n false default-caught-up-message))
  ([n gray-scale?] (caught-up-map n gray-scale? default-caught-up-message))
  ([n gray-scale? message] (caught-up-map n gray-scale? default-caught-up-message nil))
  ([n gray-scale? message opts]
   (let [t (next-activity-timestamp n)]
     {:content-type :caught-up
      :last-activity-at t
      :message message
      :opts opts
      :gray-style gray-scale?})))

(defn- insert-caught-up [items-list check-fn ignore-fn & [{:keys [hide-top-line has-next] :as opts}]]
  (let [index (loop [ret-idx 0
                     idx 0
                     items items-list]
                (let [item (first items)
                      next-items (rest items)]
                  (cond
                   ;; We reached the end, no more items, return last index
                   (nil? item)
                   ret-idx
                   ;; Found the first truthy item, return last index
                   (check-fn item)
                   idx
                   ;; Check if element needs to be ignored
                   (ignore-fn item)
                   (recur ret-idx
                          (inc idx)
                          next-items)
                   :else
                   (recur idx
                          (inc idx)
                          next-items))))
        [before after] (split-at index items-list)]
    (cond
      (and has-next
           (= index (count items-list)))
      (vec items-list)
      (= index (count items-list))
      (vec (concat items-list [(caught-up-map (last items-list) (zero? index) default-caught-up-message opts)]))
      (and hide-top-line
           (zero? index))
      (vec items-list)
      :else
      (vec (remove nil? (concat before
                                [(caught-up-map (last before) (zero? index) default-caught-up-message opts)]
                                after))))))

(defn- insert-open-close-item [items-list check-fn]
  (vec
   (remove nil?
    (map
     (fn [idx]
       (let [prev-item (get items-list (dec idx))
             item (get items-list idx)
             next-item (get items-list (inc idx))]
         (when item
           (assoc item :open-item (check-fn :open item prev-item)
                       :close-item (check-fn :close item next-item)))))
     (range (count items-list))))))

(defn- insert-ending-item [items-list has-next]
  (let [carrot-close? (> (count items-list 3))
        last-item (last items-list)]
    (cond
     (and (seq items-list) has-next)
     (vec (concat items-list [{:content-type :loading-more
                               :last-activity-at (next-activity-timestamp last-item)
                               :message (if (= (:content-type last-item) :thread)
                                          "Loading more threads..."
                                          "Loading more posts...")}]))
     carrot-close?
     (vec
      (concat items-list [{:content-type :carrot-close
                           :last-activity-at (next-activity-timestamp (last items-list))
                           :message "🤠 You've reached the end, partner"}]))
     :else
     (vec items-list))))

(defn fix-entry
  "Add `:read-only`, `:board-slug`, `:board-name` and `:content-type` keys to the entry map."
  ([entry-data board-data changes]
   (fix-entry entry-data board-data changes (dis/active-users)))
  ([entry-data board-data changes active-users]
  (if (= entry-data :404)
    entry-data
    (let [comments-link (utils/link-for (:links entry-data) "comments")
          add-comment-link (utils/link-for (:links entry-data) "create" "POST")
          published? (is-published? entry-data)
          fixed-board-uuid (or (:board-uuid entry-data) (:uuid board-data))
          fixed-board-slug (or (:board-slug entry-data) (:slug board-data))
          fixed-board-name (or (:board-name entry-data) (:name board-data))
          fixed-board-access (if published?
                               (or (:board-access entry-data) (:access board-data))
                               "private")
          fixed-publisher-board (or (:publisher-board entry-data) (:publisher-board board-data) false)
          is-uploading-video? (dis/uploading-video-data (:video-id entry-data))
          fixed-video-id (:video-id entry-data)
          fixed-publisher (when published?
                            (get active-users (-> entry-data :publisher :user-id)))]
      (-> entry-data
        (assoc :content-type :entry)
        (assoc :published? published?)
        (as-> e
          (if published?
            (update e :publisher merge fixed-publisher)
            e)
          (if published?
            (assoc e :publisher? (is-publisher? e))
            e))
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
        (assoc :fixed-video-id fixed-video-id)
        (assoc :has-headline (has-headline? entry-data))
        (assoc :body-thumbnail (when (seq (:body entry-data))
                                 (html-lib/first-body-thumbnail (:body entry-data))))
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
    (let [with-fixed-activities* (reduce (fn [ret item]
                                           (assoc-in ret [:fixed-items (:uuid item)]
                                            (fix-entry item {:slug (:board-slug item)
                                                             :name (:board-name item)
                                                             :uuid (:board-uuid item)
                                                             :publisher-board (:publisher-board item)}
                                             change-data
                                             active-users)))
                                  board-data
                                  (:entries board-data))
          with-fixed-activities (reduce (fn [ret item]
                                         (if (contains? (:fixed-items ret) (:uuid item))
                                           ret
                                           (assoc-in ret [:fixed-items (:uuid item)] (dis/activity-data (:uuid item)))))
                                 with-fixed-activities*
                                 (:posts-list board-data))
          keep-link-rel (if (= direction :down) "previous" "next")
          next-links (when direction
                      (vec (remove #(= (:rel %) keep-link-rel) (:links board-data))))
          link-to-move (when direction
                         (utils/link-for (:old-links board-data) keep-link-rel))
          fixed-next-links (if direction
                             (if link-to-move
                               (vec (conj next-links link-to-move))
                               next-links)
                             (:links board-data))
          items-list (when (contains? board-data :items)
                       ;; In case we are parsing a fresh response from server
                       (map #(hash-map :uuid (:uuid %) :content-type :entry) (:entries board-data)))
          full-items-list (merge-items-lists items-list (:posts-list board-data) direction)
          grouped-items (if (show-separators? (:slug board-data))
                          (grouped-posts full-items-list (:fixed-items with-fixed-activities))
                          full-items-list)
          with-open-close-items (insert-open-close-item grouped-items #(not= (:content-type %2) (:content-type %3)))
          with-ending-item (insert-ending-item with-open-close-items (utils/link-for fixed-next-links "next"))
          follow-board-uuids (set (map :uuid follow-boards-list))]
      (-> with-fixed-activities
        (assoc :read-only (readonly-board? (:links board-data)))
        (dissoc :old-links :items)
        (assoc :posts-list full-items-list)
        (assoc :items-to-render with-ending-item)
        (assoc :following (boolean (follow-board-uuids (:uuid board-data))))))))

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
          boards-map (zipmap (map :slug all-boards) all-boards)
          with-fixed-activities* (reduce (fn [ret item]
                                           (let [board-data (get boards-map (:board-slug item))
                                                 fixed-entry (fix-entry item board-data change-data active-users)]
                                             (assoc-in ret [:fixed-items (:uuid item)] fixed-entry)))
                                  contributions-data
                                  (:items contributions-data))
          with-fixed-activities (reduce (fn [ret item]
                                         (if (contains? (:fixed-items ret) (:uuid item))
                                           ret
                                           (assoc-in ret [:fixed-items (:uuid item)] (dis/activity-data (:uuid item)))))
                                 with-fixed-activities*
                                 (:posts-list contributions-data))
          keep-link-rel (if (= direction :down) "previous" "next")
          next-links (when direction
                      (vec (remove #(= (:rel %) keep-link-rel) (:links contributions-data))))
          link-to-move (when direction
                         (utils/link-for (:old-links contributions-data) keep-link-rel))
          fixed-next-links (if direction
                             (if link-to-move
                               (vec (conj next-links link-to-move))
                               next-links)
                             (:links contributions-data))
          items-list (when (contains? contributions-data :items)
                       ;; In case we are parsing a fresh response from server
                       (map #(hash-map :uuid (:uuid %) :content-type :entry) (:items contributions-data)))
          full-items-list (merge-items-lists items-list (:posts-list contributions-data) direction)
          grouped-items (if (show-separators? (:href contributions-data))
                          (grouped-posts full-items-list (:fixed-items with-fixed-activities))
                          full-items-list)
          with-open-close-items (insert-open-close-item grouped-items #(not= (:content-type %2) (:content-type %3)))
          with-ending-item (insert-ending-item with-open-close-items (utils/link-for fixed-next-links "next"))
          follow-publishers-ids (set (map :user-id follow-publishers-list))]
      (-> with-fixed-activities
        (dissoc :old-links :items)
        (assoc :links fixed-next-links)
        (assoc :posts-list full-items-list)
        (assoc :items-to-render with-ending-item)
        (assoc :following (boolean (follow-publishers-ids (:author-uuid contributions-data))))))))

(defn fix-container
  "Parse container data coming from the API, like All posts or Must see."
  ([container-data]
   (fix-container container-data {} (dis/org-data) (dis/active-users) nil))

  ([container-data change-data]
   (fix-container container-data change-data (dis/org-data) (dis/active-users) nil))

  ([container-data change-data org-data]
   (fix-container container-data change-data org-data (dis/active-users) nil))

  ([container-data change-data org-data active-users sort-type & [direction]]
    (let [all-boards (:boards org-data)
          boards-map (zipmap (map :slug all-boards) all-boards)
          with-fixed-activities* (reduce (fn [ret item]
                                           (let [board-data (get boards-map (:board-slug item))
                                                 fixed-entry (fix-entry item board-data change-data active-users)]
                                             (assoc-in ret [:fixed-items (:uuid item)] fixed-entry)))
                                  container-data
                                  (:items container-data))
          with-fixed-activities (reduce (fn [ret item]
                                         (if (contains? (:fixed-items ret) (:uuid item))
                                           ret
                                           (assoc-in ret [:fixed-items (:uuid item)] (dis/activity-data (:uuid item)))))
                                 with-fixed-activities*
                                 (:posts-list container-data))
          keep-link-rel (if (= direction :down) "previous" "next")
          next-links (when direction
                      (vec (remove #(= (:rel %) keep-link-rel) (:links container-data))))
          link-to-move (when direction
                         (utils/link-for (:old-links container-data) keep-link-rel))
          fixed-next-links (if direction
                             (if link-to-move
                               (vec (conj next-links link-to-move))
                               next-links)
                             (:links container-data))
          items-list (when (contains? container-data :items)
                       ;; In case we are parsing a fresh response from server
                       (map #(hash-map :uuid (:uuid %) :content-type :entry) (:items container-data)))
          full-items-list (merge-items-lists items-list (:posts-list container-data) direction)
          grouped-items (if (show-separators? (:container-slug container-data) sort-type)
                          (grouped-posts full-items-list (:fixed-items with-fixed-activities))
                          full-items-list)
          next-link (utils/link-for fixed-next-links "next")
          with-caught-up (if (= (:container-slug container-data) :following)
                           (let [enriched-items-list (map (comp (:fixed-items with-fixed-activities) :uuid) full-items-list)
                                 items-map (merge (:fixed-items with-fixed-activities) (zipmap (map :uuid enriched-items-list) enriched-items-list))]
                             (insert-caught-up grouped-items
                              #(->> % :uuid (get items-map) :unread not)
                              #(or (not= (:content-type %) :entry)
                                   (->> % :uuid (get items-map) :publisher?))
                              {:has-next next-link}))
                           grouped-items)
          with-open-close-items (insert-open-close-item with-caught-up #(not= (:content-type %2) (:content-type %3)))
          with-ending-item (insert-ending-item with-open-close-items next-link)]
      (-> with-fixed-activities
       (dissoc :old-links :items)
       (assoc :links fixed-next-links)
       (assoc :posts-list full-items-list)
       (assoc :items-to-render with-ending-item)))))

(defn fix-thread [thread entry-data active-users]
  (let [fixed-author (get active-users (-> thread :author :user-id))
        comment-unread (comment-unread? thread (:last-read-at entry-data))
        thread-unread (comment-unread? (:last-activity-at thread) (:last-read-at entry-data))]
    (-> thread
      (assoc :content-type :thread)
      (assoc :entry entry-data)
      (update :author merge fixed-author)
      (assoc :unread-thread (or comment-unread thread-unread))
      (as-> t (assoc t :author? (is-author? t))
              (assoc t :monologue? (is-monologue? t))))))

(defn fix-threads
  "
  Threads data looks like this:
  {:total-count 100
   :entries [...List of entry maps...]
   :items [{...Comment map...
            :reply-count 1
            :resource-uuid 1234-1234-1234
            :uuid 1234-1234-1235
            :last-activity-at most-recent-created-at}]
   :links []}
  "
  ([threads-data change-data org-data active-users sort-type]
   (fix-threads threads-data change-data org-data active-users sort-type nil))
  ([threads-data change-data org-data active-users sort-type direction]
    (let [all-boards (:boards org-data)
          boards-map (zipmap (map :slug all-boards) all-boards)
          with-fixed-entries (reduce (fn [ret entry]
                                       (let [board-data (get boards-map (:board-slug entry))
                                             fixed-entry (fix-entry entry board-data change-data active-users)]
                                         (assoc-in ret [:fixed-entries (:uuid entry)] fixed-entry)))
                              threads-data
                              (:entries threads-data))
          with-fixed-threads (reduce (fn [ret thread]
                                     (let [local-entry (get-in with-fixed-entries [:fixed-entries (:resource-uuid thread)])
                                           global-entry (when-not local-entry
                                                          (dis/activity-data (:resource-uuid thread)))
                                           fixed-thread (when (or local-entry global-entry)
                                                        (fix-thread thread (or local-entry global-entry) active-users))]
                                       (as-> ret next-ret
                                        (assoc-in next-ret [:fixed-items (:uuid fixed-thread)] fixed-thread)
                                        (if global-entry
                                          (assoc-in next-ret [:fixed-entries (:resource-uuid fixed-thread)] global-entry)
                                          next-ret))))
                            with-fixed-entries
                            (:items with-fixed-entries))
          keep-link-rel (if (= direction :down) "previous" "next")
          next-links (when direction
                      (vec (remove #(= (:rel %) keep-link-rel) (:links threads-data))))
          link-to-move (when direction
                         (utils/link-for (:old-links threads-data) keep-link-rel))
          fixed-next-links (if direction
                             (if link-to-move
                               (vec (conj next-links link-to-move))
                               next-links)
                             (:links threads-data))
          items-list (when (contains? threads-data :items)
                       ;; In case we are parsing a fresh response from server
                       (remove nil? (map #(hash-map :resource-uuid (:resource-uuid %) :content-type :thread :uuid (:uuid %)) (:items threads-data))))
          threads-list (merge-items-lists items-list (:threads-list threads-data) direction)
          get-thread-data (fn [thread-uuid]
                            (or (get-in with-fixed-threads [:fixed-items thread-uuid])
                                (dis/thread-data thread-uuid)))
          next-link (utils/link-for fixed-next-links "next")
          with-caught-up (insert-caught-up threads-list
                          #(-> % :uuid get-thread-data :unread-thread not)
                          #(or (not= (:content-type %) :thread)
                               (-> % :uuid get-thread-data :monologue?))
                          {:has-next next-link})
          with-open-close-items (insert-open-close-item with-caught-up #(not= (:resource-uuid %2) (:resource-uuid %3)))
          with-ending-item (insert-ending-item with-open-close-items next-link)]
      (doseq [e (vals (:fixed-entries with-fixed-entries))]
        (utils/after 0 #(comment-utils/get-comments e)))
      (-> with-fixed-threads
       (dissoc :old-links :entries :items)
       (assoc :links fixed-next-links)
       (assoc :threads-list threads-list)
       (assoc :no-virtualized-steam true)
       (assoc :items-to-render with-ending-item)))))

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