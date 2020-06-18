(ns oc.web.utils.activity
  (:require [cuerdas.core :as s]
            [defun.core :refer (defun)]
            [cljs-time.format :as time-format]
            [oc.web.api :as api]
            [oc.web.lib.jwt :as jwt]
            [oc.web.utils.org :as ou]
            [oc.web.urls :as oc-urls]
            [oc.lib.user :as user-lib]
            [oc.lib.html :as html-lib]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.utils.comment :as cu]
            [oc.web.local-settings :as ls]
            [oc.web.utils.user :as user-utils]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.lib.responsive :as responsive]))

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
        (#{:all-posts :following :unfollowing :contributions} (keyword container-slug)))))

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
       :resource-type :separator
       :date last-monday}
      (> d two-weeks-ago)
      {:label "Last week"
       :resource-type :separator
       :date two-weeks-ago}
      (> d first-month)
      {:label "2 weeks ago"
       :resource-type :separator
       :date first-month}
      (and (= (.getMonth now) (.getMonth d))
           (= (.getFullYear now) (.getFullYear d)))
      {:label "This month"
       :resource-type :separator
       :date (post-month-date-from-date d)}
      (= (.getFullYear now) (.getFullYear d))
      {:label month-string
       :resource-type :separator
       :date (post-month-date-from-date d)}
      :else
      {:label (str month-string ", " (.getFullYear d))
       :resource-type :separator
       :date (post-month-date-from-date d)})))

(def preserved-keys [:resource-type :uuid :sort-value :unread])

(defn- add-posts-to-separator [post-data separators-map last-monday two-weeks-ago first-month]
  (let [post-date (utils/js-date (:published-at post-data))
        item-data (select-keys post-data preserved-keys)]
    (if (and (seq separators-map)
             (> post-date (:date (last separators-map))))
      (update-in separators-map [(dec (count separators-map)) :posts-list] #(-> % (conj item-data) vec))
      (vec
       (conj separators-map
        (assoc (separator-from-date post-date last-monday two-weeks-ago first-month)
         :posts-list [item-data]))))))

(defn grouped-posts
  ([full-items-list]
   (let [items-list (map #(select-keys % preserved-keys) full-items-list)
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
                             (recur (add-posts-to-separator (first posts) separators last-monday two-weeks-ago first-month)
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
  "Check if current user is the author of the entry/comment."
  ([entity-data :guard :author]
   (when (jwt/jwt)
     (is-author? entity-data (jwt/user-id))))

  ([entity-data :guard :author user-data :guard :user-id]
   (is-author? (-> entity-data :author :user-id) (:user-id user-data)))

  ([entity-data :guard :author user-id :guard string?]
   (is-author? (-> entity-data :author :user-id) user-id))

  ([author-id :guard string? user-id :guard string?]
   (= user-id author-id)))

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

(defn readonly-org? [links]
  (let [update-link (utils/link-for links "partial-update")]
    (nil? update-link)))

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
  "Given 2 list of reduced items (reduced since they contains only the uuid and resource-type props)
   return a single list without duplicates depending on the direction.
   This is necessary since the lists could contain duplicates because they are loaded in 2 different
   moments and new activity could lead to changes in the sort."
  [new-items-list old-items-list direction]
  (cond
    (and direction
         (seq old-items-list)
         (seq new-items-list))
    (let [old-uuids (map :uuid old-items-list)
          new-uuids (map :uuid new-items-list)
          next-items-uuids (if (= direction :down)
                             (vec (distinct (concat old-uuids new-uuids)))
                             (vec (reverse (distinct (concat (reverse old-uuids) (reverse new-uuids))))))
          all-items-list (concat old-items-list new-items-list)
          items-map (zipmap (map :uuid all-items-list) all-items-list)]
      (mapv items-map next-items-uuids))
    (seq old-items-list)
    (vec old-items-list)
    :else
    (vec new-items-list)))

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
     {:resource-type :caught-up
      :last-activity-at t
      :message message
      :opts opts
      :gray-style gray-scale?})))

(defn- insert-caught-up [items-list check-fn & [{:keys [hide-top-line has-next] :as opts}]]

  (if (seq items-list)
    (let [reversed-list (vec (reverse items-list))
          reversed-idx (utils/index-of reversed-list check-fn)
          last-idx (dec (count items-list))
          idx (when (number? reversed-idx) (- last-idx reversed-idx))
          [before after] (when (number? idx) (split-at idx items-list))]
      (cond
        ;; No unread items, add caught up at the top
        (nil? idx)
        (if hide-top-line
          items-list
          (vec (cons (caught-up-map nil true) items-list)))
        ;; Last item is unread
        (= idx last-idx)
        (if has-next
          items-list
          (vec (conj items-list (caught-up-map (last items-list) false default-caught-up-message opts))))
        ;; Found an unread item
        :else
        (vec (remove nil?
         (concat before
          [(caught-up-map (last before) (zero? idx) default-caught-up-message opts)]
          after)))))
    items-list))

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
  (let [closing-item? (> (count items-list) 3)
        last-item (last items-list)]
    (cond
     (and (seq items-list) has-next)
     (vec (concat items-list [{:resource-type :loading-more
                               :last-activity-at (next-activity-timestamp last-item)
                               :message "Loading more updates..."}]))
     closing-item?
     (vec
      (concat items-list [{:resource-type :closing-item
                           :last-activity-at (next-activity-timestamp (last items-list))
                           :message "🤠 You've reached the end, partner"}]))
     :else
     (vec items-list))))

(defn get-comments-finished
  [comments-key activity-data {:keys [status success body]}]
  (when success
    (dis/dispatch! [:comments-get/finish {:success success
                                          :error (when-not success body)
                                          :comments-key comments-key
                                          :body (when (seq body) (json->cljs body))
                                          :activity-uuid (:uuid activity-data)
                                          :secure-activity-uuid (router/current-secure-activity-id)}])))

(defn get-comments [activity-data]
  (when activity-data
    (let [comments-key (dis/activity-comments-key (router/current-org-slug) (:uuid activity-data))
          comments-link (utils/link-for (:links activity-data) "comments")]
      (when comments-link
        (dis/dispatch! [:comments-get
                        comments-key
                        activity-data])
        (api/get-comments comments-link #(get-comments-finished comments-key activity-data %))))))

(defn get-comments-if-needed [activity-data all-comments-data]
  (let [comments-link (utils/link-for (:links activity-data) "comments")
        activity-uuid (:uuid activity-data)
        comments-data (get all-comments-data activity-uuid)
        should-load-comments? (and ;; there is a comments link
                                   (map? comments-link)
                                   ;; there are comments to load,
                                   (pos? (:count comments-link))
                                   ;; they are not already loading,
                                   (not (:loading comments-data))
                                   ;; and they are not loaded already
                                   (not (contains? comments-data :sorted-comments)))]
    ;; Load the whole list of comments if..
    (when should-load-comments?
      (get-comments activity-data))))

(defn- is-emoji
  [body]
  (let [plain-text (.text (js/$ (str "<div>" body "</div>")))
        is-emoji? (js/RegExp "^([\ud800-\udbff])([\udc00-\udfff])" "g")
        is-text-message? (js/RegExp "[a-zA-Z0-9\\s!?@#\\$%\\^&(())_=\\-<>,\\.\\*;':\"]" "g")]
    (and ;; emojis can have up to 11 codepoints
         (<= (count plain-text) 11)
         (.match plain-text is-emoji?)
         (not (.match plain-text is-text-message?)))))

(defun parse-comment
  ([org-data activity-data comments :guard sequential?]
    (map #(parse-comment org-data activity-data %) comments))
  ([org-data activity-data comment-map :guard nil?]
    {})
  ([org-data :guard map? activity-data :guard map? comment-map :guard map?]
    (let [edit-comment-link (utils/link-for (:links comment-map) "partial-update")
          delete-comment-link (utils/link-for (:links comment-map) "delete")
          can-react? (utils/link-for (:links comment-map) "react"  "POST")
          reply-parent (or (:parent-uuid comment-map) (:uuid comment-map))
          is-root-comment (empty? (:parent-uuid comment-map))
          author? (is-author? comment-map)
          unread? (and (not author?)
                       (comment-unread? comment-map (:last-read-at activity-data)))]
      (-> comment-map
        (assoc :resource-type :comment)
        (assoc :author? author?)
        (assoc :unread unread?)
        (assoc :is-emoji (is-emoji (:body comment-map)))
        (assoc :can-edit (boolean edit-comment-link))
        (assoc :can-delete (boolean delete-comment-link))
        (assoc :can-react can-react?)
        (assoc :reply-parent reply-parent)
        (assoc :resource-uuid (:uuid activity-data))
        (assoc :url (str ls/web-server-domain (oc-urls/comment-url (:slug org-data) (:board-slug activity-data)
                                               (:uuid activity-data) (:uuid comment-map))))))))

(defn parse-entry
  "Add `:read-only`, `:board-slug`, `:board-name` and `:resource-type` keys to the entry map."
  ([entry-data board-data changes]
   (parse-entry entry-data board-data changes (dis/active-users)))
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
        (assoc :resource-type :entry)
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
        (assoc :comments (cu/sort-comments (:comments entry-data))))))))

(defn parse-org
  "Fix org data coming from the API."
  [db org-data]
  (when org-data
    (let [fixed-boards (mapv #(assoc % :read-only (-> % :links readonly-board?)) (:boards org-data))
          drafts-board (some #(when (= (:slug %) utils/default-drafts-board-slug) %) (:boards org-data))
          drafts-link (when drafts-board
                        (utils/link-for (:links drafts-board) ["item" "self"] "GET"))
          previous-org-drafts-count (get-in db (conj (dis/org-data-key (:slug org-data)) :drafts-count))
          previous-bookmarks-count (get-in db (conj (dis/org-data-key (:slug org-data)) :bookmarks-count))]
      (-> org-data
       (assoc :read-only (readonly-org? (:links org-data)))
       (assoc :boards fixed-boards)
       (assoc :author? (is-author? org-data))
       (assoc :member? (jwt/user-is-part-of-the-team (:team-id org-data)))
       (assoc :drafts-count (ou/disappearing-count-value previous-org-drafts-count (:count drafts-link)))
       (assoc :bookmarks-count (ou/disappearing-count-value previous-bookmarks-count (:bookmarks-count org-data)))
       (assoc :unfollowing-count (ou/disappearing-count-value previous-bookmarks-count (:unfollowing-count org-data)))))))

(defn parse-board
  "Parse board data coming from the API."
  ([board-data]
   (parse-board board-data {} (dis/active-users) (dis/follow-boards-list) dis/recently-posted-sort))

  ([board-data change-data]
   (parse-board board-data change-data (dis/active-users) (dis/follow-boards-list) dis/recently-posted-sort))

  ([board-data change-data active-users]
   (parse-board board-data change-data active-users (dis/follow-boards-list) dis/recently-posted-sort))

  ([board-data change-data active-users follow-boards-list]
   (parse-board board-data change-data active-users follow-boards-list dis/recently-posted-sort))

  ([board-data change-data active-users follow-boards-list sort-type & [direction]]
    (when board-data
      (let [with-fixed-activities* (reduce (fn [ret item]
                                             (assoc-in ret [:fixed-items (:uuid item)]
                                              (parse-entry item {:slug (:board-slug item)
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
                         (map #(hash-map :uuid (:uuid %) :resource-type :entry) (:entries board-data)))
            full-items-list (merge-items-lists items-list (:posts-list board-data) direction)
            grouped-items (if (show-separators? (:slug board-data) sort-type)
                            (grouped-posts full-items-list (:fixed-items with-fixed-activities))
                            full-items-list)
            with-open-close-items (insert-open-close-item grouped-items #(not= (:resource-type %2) (:resource-type %3)))
            with-ending-item (insert-ending-item with-open-close-items (utils/link-for fixed-next-links "next"))
            follow-board-uuids (set (map :uuid follow-boards-list))]
        (-> with-fixed-activities
          (assoc :read-only (readonly-board? (:links board-data)))
          (dissoc :old-links :items)
          (assoc :posts-list full-items-list)
          (assoc :items-to-render with-ending-item)
          (assoc :following (boolean (follow-board-uuids (:uuid board-data)))))))))

(defn parse-contributions
  "Parse data coming from the API for a certain user's posts."
  ([contributions-data]
   (parse-contributions contributions-data {} (dis/org-data) (dis/active-users) (dis/follow-publishers-list) dis/recently-posted-sort))

  ([contributions-data change-data]
   (parse-contributions contributions-data change-data (dis/org-data) (dis/active-users) (dis/follow-publishers-list) dis/recently-posted-sort))

  ([contributions-data change-data org-data]
   (parse-contributions contributions-data change-data org-data (dis/active-users) (dis/follow-publishers-list) dis/recently-posted-sort))

  ([contributions-data change-data org-data active-users]
   (parse-contributions contributions-data change-data org-data active-users (dis/follow-publishers-list) dis/recently-posted-sort))

  ([contributions-data change-data org-data active-users follow-publishers-list]
   (parse-contributions contributions-data change-data org-data active-users follow-publishers-list dis/recently-posted-sort))

  ([contributions-data change-data org-data active-users follow-publishers-list sort-type & [direction]]
    (when contributions-data
      (let [all-boards (:boards org-data)
            boards-map (zipmap (map :slug all-boards) all-boards)
            with-fixed-activities* (reduce (fn [ret item]
                                             (let [board-data (get boards-map (:board-slug item))
                                                   fixed-entry (parse-entry item board-data change-data active-users)]
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
                         (map #(hash-map :uuid (:uuid %) :resource-type :entry) (:items contributions-data)))
            full-items-list (merge-items-lists items-list (:posts-list contributions-data) direction)
            grouped-items (if (show-separators? :contributions sort-type)
                            (grouped-posts full-items-list (:fixed-items with-fixed-activities))
                            full-items-list)
            next-link (utils/link-for fixed-next-links "next")
            with-open-close-items (insert-open-close-item grouped-items #(not= (:resource-type %2) (:resource-type %3)))
            with-ending-item (insert-ending-item with-open-close-items next-link)
            follow-publishers-ids (set (map :user-id follow-publishers-list))]
        (-> with-fixed-activities
          (dissoc :old-links :items)
          (assoc :links fixed-next-links)
          (assoc :self? (is-author? (:author-uuid contributions-data) (jwt/user-id)))
          (assoc :posts-list full-items-list)
          (assoc :items-to-render with-ending-item)
          (assoc :following (boolean (follow-publishers-ids (:author-uuid contributions-data)))))))))

(defn parse-container
  "Parse container data coming from the API, like All posts or Must see."
  ([container-data]
   (parse-container container-data {} (dis/org-data) (dis/active-users) nil))

  ([container-data change-data]
   (parse-container container-data change-data (dis/org-data) (dis/active-users) nil))

  ([container-data change-data org-data]
   (parse-container container-data change-data org-data (dis/active-users) nil))

  ([container-data change-data org-data active-users sort-type & [direction]]
    (when container-data
      (let [all-boards (:boards org-data)
            boards-map (zipmap (map :slug all-boards) all-boards)
            with-fixed-activities* (reduce (fn [ret item]
                                             (let [board-data (get boards-map (:board-slug item))
                                                   fixed-entry (parse-entry item board-data change-data active-users)]
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
                         (map #(hash-map :uuid (:uuid %) :resource-type :entry) (:items container-data)))
            full-items-list (merge-items-lists items-list (:posts-list container-data) direction)
            grouped-items (if (show-separators? (:container-slug container-data) sort-type)
                            (grouped-posts full-items-list (:fixed-items with-fixed-activities))
                            full-items-list)
            next-link (utils/link-for fixed-next-links "next")
            following-or-replies? (#{:following :replies} (:container-slug container-data))
            items-map (when following-or-replies?
                        (let [enriched-items-list (map (comp (:fixed-items with-fixed-activities) :uuid) full-items-list)]
                          (merge (:fixed-items with-fixed-activities) (zipmap (map :uuid enriched-items-list) enriched-items-list))))
            with-caught-up (if following-or-replies?
                            (insert-caught-up grouped-items :unread {:has-next next-link})
                            grouped-items)
            with-open-close-items (insert-open-close-item with-caught-up #(not= (:resource-type %2) (:resource-type %3)))
            with-ending-item (insert-ending-item with-open-close-items next-link)
            replies? (= (-> container-data :container-slug keyword) :replies)]
        (when replies?
          (doseq [e (vals (:fixed-items with-fixed-activities))]
            (utils/after 0 #(get-comments e))))
        (-> with-fixed-activities
         (assoc :no-virtualized-steam replies?)
         (dissoc :old-links :items)
         (assoc :links fixed-next-links)
         (assoc :posts-list full-items-list)
         (assoc :items-to-render with-ending-item))))))

(defn activity-comments [activity-data comments-data]
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