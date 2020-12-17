(ns oc.web.actions.nux
  (:require [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.json :refer (json->cljs cljs->json)]))

(defn get-tooltip-data [step user-type]
  (let [viewer? (= user-type :viewer)
        steps (if viewer? 5 6)]
    (case step
      :intro    {:title "A few quick tips"
                 :description "When you’re ready to add an update or some news for your team, click here anytime."
                 :steps (str "1 of " steps)
                 :arrow-position :top
                 :position :bottom
                 :next-title "Next"
                 :sel [:div.cmail-outer]}
      :news     {:title "Your news feed"
                 :description "Updates from your team will show up here in your feed."
                 :steps (str "2 of " steps)
                 :arrow-position :top
                 :position :bottom
                 :next-title "Next"
                 :sel [:div.stream-item]}
      :feed     {:title "Personalize your feed"
                 :description ["Add topics, such as Design, Marketing, HR to organize your updates into groups."
                               [:br]
                               "Your teammates will be able to subsctibe to topics they want to follow."]
                 :steps (str "3 of " steps)
                 :arrow-position :left-top
                 :next-title "Next"
                 :position :right-top
                 :sel [:div.left-navigation-sidebar :a.nav-link.topics]}
      :settings {:title "Settings"
                 :description ["Click your avatar to access your profile, team settings, and more."
                               (when-not viewer?
                                 [:br])
                               (when-not viewer?
                                 "Using Slack? Set up auto-sharing to specific #Slack channel, too.")]
                 :steps (str "4 of " steps)
                 :arrow-position :top-right
                 :next-title "Next"
                 :position :bottom-left
                 :sel [:nav.oc-navbar :div.user-menu]}
      :invite   {:title "Invite your team"
                 :description "Carrot is built for teams. Send an invite link with an emial or Slack to get started."
                 :steps (str "5 of " steps)
                 :arrow-position :left
                 :next-title "Next"
                 :position :right
                 :sel [:div.left-navigation-sidebar :div.invite-people-box]}
      :ready    {:title "You’re ready to go!"
                 :description (if viewer?
                                "Check the latest update from your team."
                                "Give it a try. Add an update or news to get started.")
                 :steps (str (if viewer? 5 6) " of " steps)
                 :arrow-position :top
                 :next-title "Done"
                 :position :bottom
                 :sel [:div.cmail-outer]}
      nil)))

(defn get-nux-cookie
  "Read the cookie from the document only if the nux-cookie-value atom is nil.
  In all the other cases return the read value in the atom."
  []
  (some-> (jwt/user-id)
          router/nux-cookie
          cook/get-cookie
          json->cljs
          (update :step keyword)
          (update :user-type keyword)))

(defn set-nux-cookie
  "Create a map for the new user cookie and save it. Also update the value of
  the nux-cookie-value atom."
  [user-type value-map]
  (let [old-nux-cookie (get-nux-cookie)
        value-map (merge {:user-type user-type} old-nux-cookie value-map)
        json-map (cljs->json value-map)
        json-string (.stringify js/JSON json-map)]
    (cook/set-cookie!
     (router/nux-cookie (jwt/user-id))
      json-string
      (* 60 60 24 7))))

(defn new-user-registered [user-type]
  (cook/set-cookie! (router/first-ever-landing-cookie (jwt/user-id))
                    true (* 60 60 24 7))
  (when (not= user-type :viewer)
    (cook/set-cookie! (router/show-invite-box-cookie (jwt/user-id)) true))
  (set-nux-cookie user-type {:step :intro}))

(defn check-nux
  [& [force?]]
  (when (or force?
            (not (contains? @dis/app-state :nux)))
    (dis/dispatch! [:input [:nux] (get-nux-cookie)])))

(defn ^:export end-nux
  "NUX completed for the current user, remove the cookie and update the nux-cookie-value."
  []
  (dis/dispatch! [:input [:nux] nil])
  (cook/remove-cookie! (router/nux-cookie (jwt/user-id))))

(defn ^:export restart-nux []
  (set-nux-cookie (:role (dis/current-user-data)) {:step :intro})
  (check-nux true))

(defn- calc-step [{:keys [step user-type]}]
  (case step
    :intro    :news
    :news     :feed
    :feed     :settings
    :settings (if (or (= user-type :viewer)
                      (not (get @dis/app-state dis/show-invite-box-key)))
                :ready
                :invite)
    :invite   :ready
    :ready    :done
    nil       (if (= user-type :viewer) :news :intro)
    step))

(defn next-step []
  (let [current-value (get @dis/app-state :nux)
        next-step (calc-step current-value)]
    ;; In case we are stalled on a value it means NUX is finished/dismissed
    (when (:step current-value)
      (if (= (:step current-value) next-step)
        (end-nux)
        (set-nux-cookie (:user-type current-value) {:step next-step})))
    (dis/dispatch! [:input [:nux :step] next-step])))

(defn dismiss-nux []
  (dis/dispatch! [:input [:nux :step] :dismiss])
  (end-nux))