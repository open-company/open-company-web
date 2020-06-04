(ns oc.web.stores.search
  (:require [taoensso.timbre :as timbre]
            [oc.web.lib.jwt :as jwt]
            [oc.web.utils.activity :as au]
            [oc.web.dispatcher :as dispatcher]))

(defonce search-limit 20)
(defonce savedsearch (atom ""))

(defonce search-key :search-results)
(defonce search-active? :search-active)

(defn search-results []
  (get-in @dispatcher/app-state search-key))

(defn saved-search []
  (let [tmp @savedsearch]
    (reset! savedsearch "")
    tmp))

(defn should-display []
  " If the user is anonymous or not part of the orginization
    don't display search component.
  "
  (if-not (jwt/jwt)
    false
    (if (:member? (dispatcher/org-data))
      true
      false)))

(defn board-slug-with-uuid [board-uuid]
  (when-let [uuid-board (au/board-by-uuid board-uuid)]
    (:board-slug uuid-board)))

(defn cleanup-result [result]
  (let [source (:_source result)
        fixed-entry-uuid (clojure.string/replace (:uuid source)
                          "entry-" "")
        fixed-board-slug (cond
                          (seq (:board-slug source))
                          (:board-slug source)
                          (seq (:board-uuid source))
                          (board-slug-with-uuid (:board-uuid source))
                          :else
                          nil)]
    (assoc result :_source (-> source
                            (assoc :uuid fixed-entry-uuid)
                            (assoc :board-slug fixed-board-slug)))))

(defn- cleanup-results
  "Entries have the uuid in this format: entry-0000-0000-0000-0000
   replace those with only the UUID and make sure they have a board-slug,
   if the slug is missing replace it with the board-uuid.
   Finally filter out all the results that still don't have a board-slug."
  [results]
  (->> results
   (mapv cleanup-result)
   (filterv #(-> % :_source :board-slug seq))))

(defmethod dispatcher/action :search-query/start
  [db [_ search-query]]
  (assoc-in db [search-key :loading] true))


(defmethod dispatcher/action :search-query/finish
  [db [_ {:keys [success error body query]}]]
  (let [total-hits (:total body)
        results (vec (sort-by #(:created-at (:_source %)) (:hits body)))]
    (when success
      (reset! savedsearch query))
    (if success
      (assoc db search-key {:count total-hits :loading false :results (cleanup-results results) :query query})
      (assoc db search-key {:failed true :loading false :query query}))))

(defmethod dispatcher/action :search-active
  [db [_]]
  (assoc db search-active? true))

(defmethod dispatcher/action :search-inactive
  [db [_]]
  (assoc db search-active? false))

(defmethod dispatcher/action :search-reset
  [db [_]]
  (dissoc db search-key))

(defmethod dispatcher/action :search-result-clicked
  [db [_]]
  (assoc db search-active? false))