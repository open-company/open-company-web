(ns oc.web.utils.reminder
  (:require [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

(def sample-list
  [{:uuid "1234-1234-1234"
    :title "A simple reminder"
    :description "Monthly post needed to remember where we are and what we are doing"
    :org-uuid "93d8-47ba-bacd"
    :author "7e80-4d3c-830d" ;; Another admin
    ; :author {:user-id "7e80-4d3c-830d"
    ;          :first-name "IacAdmin"
    ;          :last-name "14Nov"
    ;          :name "IacAdmin 14Nov"
    ;          :avatar-url "/img/ML/happy_face_blue.svg"}
    :assignee "773e-4258-915e" ;; My self
    ; :assignee {:user-id "773e-4258-915e"
    ;            :first-name "Iacopo"
    ;            :last-name "Carraro"
    ;            :name "Iacopo Carraro"
    ;            :avatar-url "https://avatars.slack-edge.com/2017-02-02/136114833346_3758034af26a3b4998f4_512.jpg"}
    :start-date "2019-01-04T14:15:02Z"
    :frequency "Month"
    :on "Friday"
    :last-sent nil
    :assignee-tz "Europe/Amsterdam"
    :links [{:href "/blah/blah/blah"
             :rel "update"
             :method "PATCH"}]
   }
   {:uuid "4321-4321-4321"
    :title "Quarterly All-Hands"
    :description "Quarterly All-Hands post used to start the usual conversation around what's going on."
    :org-uuid "93d8-47ba-bacd"
    :author "773e-4258-915e" ;; My self
    ; :author {:user-id "773e-4258-915e"
    ;          :first-name "Iacopo"
    ;          :last-name "Carraro"
    ;          :name "Iacopo Carraro"
    ;          :avatar-url "https://avatars.slack-edge.com/2017-02-02/136114833346_3758034af26a3b4998f4_512.jpg"}
    :assignee "7e80-4d3c-830d" ;; Another admin
    ; :assignee {:user-id "7e80-4d3c-830d"
    ;            :first-name "IacAdmin"
    ;            :last-name "14Nov"
    ;            :name "IacAdmin 14Nov"
    ;            :avatar-url "/img/ML/happy_face_blue.svg"}
    :start-date "2018-12-31T12:45:02Z"
    :frequency "Quarter"
    :on "Last day of the quarter"
    :last-sent nil
    :assignee-tz "Europe/Amsterdam"
    :links [{:href "/blah/blah/blah"
             :rel "update"
             :method "PATCH"}]}])

(defn reminders-sample-list []
  (let [current-user-data (dis/current-user-data)
        team-data (dis/team-data)
        users-no-self (filterv #(not= (:user-id %) (:user-id current-user-data)) (:users team-data))
        other-user (first (shuffle  users-no-self))
        first-reminder (-> (first sample-list)
                        (assoc :assignee (:user-id current-user-data))
                        (assoc :author (:user-id other-user)))
        second-reminder (-> (second sample-list)
                         (assoc :assignee (:user-id other-user))
                         (assoc :author (:user-id current-user-data)))]
    [first-reminder second-reminder]))

(defn parse-reminder
  "Given the map of a reminder denormalize it with the assignee and author map."
  [reminder-data org-data team-data]
  (let [assignee-data (first (filter #(= (:user-id %) (:assignee reminder-data)) (:users team-data)))
        author-data (first (filter #(= (:user-id %) (:author reminder-data)) (:users team-data)))
        js-date (utils/js-date (:start-date reminder-data))
        now-year (.getFullYear (utils/js-date))
        show-year? (not= (.getFullYear js-date) now-year)
        parsed-date (utils/date-string js-date [:short (when show-year? :year)])]
    (-> reminder-data
      (assoc :assignee assignee-data)
      (assoc :author author-data)
      (assoc :parsed-start-date parsed-date))))

(defn parse-reminders [reminders-data]
  (let [org-data (dis/org-data)
        team-data (dis/team-data (:team-id org-data))]
    (vec (map #(parse-reminder % org-data team-data) reminders-data))))

(defn new-reminder-data []
  (let [org-data (dis/org-data)
        current-user-data (dis/current-user-data)]
    {:title ""
     :description ""
     :org-uuid (:uuid org-data)
     :author current-user-data
     :assignee current-user-data
     :on "Monday"
     :frequency "Week"
     :last-sent nil
     :assignee-tz (:timezone current-user-data)}))

(defn- user-is-allowed? [org-data user]
  (when (or (= (:status user) "active")
            (= (:status user) "unverified"))
    (utils/get-author (:user-id user) (:authors org-data))))

(defn users-for-reminders [org-data team-data]
  (let [all-users (:users team-data)]
    (filterv #(user-is-allowed? org-data %) all-users)))