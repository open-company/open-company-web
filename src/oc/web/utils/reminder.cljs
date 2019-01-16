(ns oc.web.utils.reminder
  (:require [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

; (def sample-list
;   [{:uuid "1234-1234-1234"
;     :headline "A simple reminder"
;     :org-uuid "93d8-47ba-bacd"
;     :author {:user-id "7e80-4d3c-830d"
;              :first-name "IacAdmin"
;              :last-name "14Nov"
;              :name "IacAdmin 14Nov"
;              :avatar-url "/img/ML/happy_face_blue.svg"}
;     :assignee {:user-id "773e-4258-915e"
;                :first-name "Iacopo"
;                :last-name "Carraro"
;                :name "Iacopo Carraro"
;                :avatar-url "https://avatars.slack-edge.com/2017-02-02/136114833346_3758034af26a3b4998f4_512.jpg"}
;     :frequency "weekly"
;     :week-occurence "friday"
;     :created-at "2019-01-13T13:12:12.342Z"
;     :updated-at "2019-01-13T13:12:12.342Z"
;     :next-send nil
;     :last-sent nil
;     :assignee-tz "Europe/Amsterdam"
;     :links [{:href "/blah/blah/blah"
;              :rel "update"
;              :method "PATCH"}]
;    }
;    {:uuid "4321-4321-4321"
;     :headline "Quarterly All-Hands"
;     :org-uuid "93d8-47ba-bacd"
;     :author {:user-id "773e-4258-915e"
;              :first-name "Iacopo"
;              :last-name "Carraro"
;              :name "Iacopo Carraro"
;              :avatar-url "https://avatars.slack-edge.com/2017-02-02/136114833346_3758034af26a3b4998f4_512.jpg"}
;     :assignee {:user-id "7e80-4d3c-830d"
;                :first-name "IacAdmin"
;                :last-name "14Nov"
;                :name "IacAdmin 14Nov"
;                :avatar-url "/img/ML/happy_face_blue.svg"}
;     :frequency "quarterly"
;     :period-occurence "last"
;     :created-at "2019-01-13T13:12:12.342Z"
;     :updated-at "2019-01-13T13:12:12.342Z"
;     :next-send nil
;     :last-sent nil
;     :assignee-tz "Europe/Amsterdam"
;     :links [{:href "/blah/blah/blah"
;              :rel "update"
;              :method "PATCH"}]}])

(def occurence-values
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
   :biweekly "Other week"
   :monthly "Month"
   :quarterly "Quarter"})

(def occurence-fields
  {:weekly :week-occurence
   :biweekly :week-occurence
   :monthly :period-occurence
   :quarterly :period-occurence})

; (defn reminders-sample-list []
;   (let [current-user-data (dis/current-user-data)
;         team-data (dis/team-data)
;         users-no-self (filterv #(not= (:user-id %) (:user-id current-user-data)) (:users team-data))
;         other-user (first (shuffle  users-no-self))
;         first-reminder (-> (first sample-list)
;                         (assoc :assignee current-user-data)
;                         (assoc :author other-user))
;         second-reminder (-> (second sample-list)
;                          (assoc :assignee other-user)
;                          (assoc :author current-user-data))]
;     [first-reminder second-reminder]))

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
        occurence-field (get occurence-fields frequency-kw)
        occurence-value (get reminder-data occurence-field)
        occurence-value-kw (keyword occurence-value)
        occurence-value (get-in occurence-values [frequency-kw occurence-value-kw])]
    (-> with-parsed-date
      ;; The freuqncy keyword
      (assoc :frequency frequency-kw)
      ;; The name of the field used for the occurence: :week-occurence or :period-occurence
      (assoc :occurence-label occurence-field)
      ;; The occurence field but in keyword
      (assoc occurence-field occurence-value-kw)
      ;; The value of the occurence field like it is visualized, not the keyword for it
      (assoc :occurence-value occurence-value))))

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
     :week-occurence "monday"
     :frequency "weekly"
     :next-send nil
     :last-sent nil
     :assignee-tz (:timezone current-user-data)}))

(defn- user-is-allowed? [org-data user]
  (when (or (= (:status user) "active")
            (= (:status user) "unverified"))
    (utils/get-author (:user-id user) (:authors org-data))))

(defn users-for-reminders [org-data team-data]
  (let [all-users (:users team-data)]
    (filterv #(user-is-allowed? org-data %) all-users)))