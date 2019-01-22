(ns oc.web.utils.reminder
  (:require [clojure.set :refer (rename-keys)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

(def occurrence-values
  {:weekly {:monday "Monday"
            :tuesday "Tuesday"
            :wednesday "Wednesday"
            :thursday "Thursday"
            :friday "Friday"
            :saturday "Saturday"
            :sunday "Sunday"}
   :biweekly {:monday "Monday"
              :tuesday "Tuesday"
              :wednesday "Wednesday"
              :thursday "Thursday"
              :friday "Friday"
              :saturday "Saturday"
              :sunday "Sunday"}
   :monthly {:first "First day of the month"
             :first-monday "First Monday of the month"
             :last-friday "Last Friday of the month"
             :last "Last day of the month"}
   :quarterly {:first "First day of the quarter"
               :first-monday "First Monday of the quarter"
               :last-friday "Last Friday of the quarter"
               :last "Last day of the quarter"}})

(def frequency-values
  {:weekly "Week"
   :biweekly "Two weeks"
   :monthly "Month"
   :quarterly "Quarter"})

(def occurrence-fields
  {:weekly :week-occurrence
   :biweekly :week-occurrence
   :monthly :period-occurrence
   :quarterly :period-occurrence})

(defn parse-reminder
  "Given the map of a reminder denormalize it with the assignee and author map."
  [reminder-data]
  (let [js-date (utils/js-date (:next-send reminder-data))
        now-year (.getFullYear (utils/js-date))
        show-year? (not= (.getFullYear js-date) now-year)
        parsed-date (utils/date-string js-date [:short (when show-year? :year)])
        with-parsed-date (if (:next-send reminder-data)
                           (assoc reminder-data :parsed-next-send parsed-date)
                           reminder-data)
        frequency-kw (keyword (:frequency reminder-data))
        occurrence-field (get occurrence-fields frequency-kw)
        occurrence-value (get reminder-data occurrence-field)
        occurrence-value-kw (keyword occurrence-value)
        occurrence-value (get-in occurrence-values [frequency-kw occurrence-value-kw])
        assignee-map (:assignee reminder-data)
        assignee-name (or (:name assignee-map) (utils/name-or-email assignee-map))]
    (-> with-parsed-date
      ;; The freuqncy keyword
      (assoc :frequency frequency-kw)
      ;; The name of the field used for the occurrence: :week-occurrence or :period-occurrence
      (assoc :occurrence-label occurrence-field)
      ;; The occurrence field but in keyword
      (assoc occurrence-field occurrence-value-kw)
      ;; The value of the occurrence field like it is visualized, not the keyword for it
      (assoc :occurrence-value occurrence-value)
      ;; Make sure assignee has the :name key for sorting
      (assoc-in [:assignee :name] assignee-name))))

(defn parse-reminders [reminders-data]
  (let [parsed-reminders (vec (map parse-reminder (:items reminders-data)))]
    (assoc reminders-data :items parsed-reminders)))

(defn new-reminder-data []
  (let [org-data (dis/org-data)
        current-user-data (dis/current-user-data)]
    {:headline ""
     :org-uuid (:uuid org-data)
     :author (select-keys current-user-data [:user-id :avatar-url :name :first-name :last-name])
     :assignee (select-keys current-user-data [:user-id :avatar-url :name :first-name :last-name])
     :week-occurrence "monday"
     :frequency "weekly"
     :next-send nil
     :last-sent nil
     :assignee-tz (:timezone current-user-data)}))

(defn sort-users-fn [user-a user-b]
  (let [disabled-compare (compare (:disabled user-a) (:disabled user-b))]
    (if (= disabled-compare 0)
      (compare (:label user-b) (:label user-a))
      disabled-compare)))

(defn users-for-reminders [roster-data]
  (let [fixed-roster (map #(let [status (:status %)
                                 tooltip (case status
                                           ("pending" "unverified") "This user has an unverified email"
                                           nil)]
                             (merge % {:disabled (not= status "active")
                                       :tooltip tooltip}))
                      (:items roster-data))
        users-list (vec (map #(-> %
                                (assoc :name (utils/name-or-email %))
                                (select-keys [:name :user-id :disabled :tooltip])
                                (rename-keys {:name :label :user-id :value})
                                (assoc :user-map %))
                     fixed-roster))]
    (sort sort-users-fn users-list)))

(defn sort-fn [reminder-a reminder-b]
  (let [headline-compare (compare (:headline reminder-a) (:headline reminder-b))]
    (if (= headline-compare 0)
      (compare (:name (:assignee reminder-a)) (:name (:assignee reminder-b)))
      headline-compare)))

(defn sort-reminders [reminders-items]
  (sort sort-fn reminders-items))

(defn parse-reminders-roster [roster-data]
  (let [collection (:collection roster-data)]
    (assoc roster-data :users-list (users-for-reminders collection))))