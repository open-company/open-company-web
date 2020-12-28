(ns oc.web.actions.nux
  (:require [oops.core :refer (oget)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.lib.json :refer (json->cljs cljs->json)]))

(def expand-cmail #(cmail-actions/cmail-expand (dis/cmail-data) (dis/cmail-state)))

(def collapse-cmail #(cmail-actions/cmail-collapse (dis/cmail-state)))

(defn step-intro [steps _viewer?]
  {:title "Welcome!"
   :description "When you’re ready to add an update or some news for your team, click here anytime."
   :steps (str "1 of " steps)
   :back-title nil
   :arrow-position :top
   :position :bottom
   :next-title "Next"
   :sel [:div.cmail-outer]})

(defn step-news [steps viewer?]
  {:title (if viewer? "Welcome!" "Your news feed")
   :description "Updates and async discussions from your team will show up here in your feed."
   :steps (if viewer?
            (str "1 of " steps)
            (str "2 of " steps))
   :back-title (when-not viewer? "Back")
   :arrow-position :left-top
   :next-title "Next"
   :position :right-top
   :sel [:div.left-navigation-sidebar :a.nav-link.home]})

(defn step-feed [steps viewer?]
  {:title "Personalize your feed"
   :description [[:span "Add topics, such as Design, Marketing, HR to organize your updates into groups."]
                 [:br]
                 [:span "Your teammates will be able to subsctibe to topics they want to follow."]]
   :steps (if viewer?
            (str "2 of " steps)
            (str "3 of " steps))
   :back-title "Back"
   :arrow-position :left-top
   :next-title "Next"
   :position :right-top
   :sel [:div.left-navigation-sidebar :a.nav-link.topics]})

(defn step-settings [steps viewer?]
  {:title "Settings"
   :description [[:span "Click your avatar to access your profile, team settings, and more."]
                 (when-not viewer?
                   [:br])
                 (when-not viewer?
                   [:span "Using Slack? Set up auto-sharing to specific #Slack channel, too."])]
   :steps (if viewer?
            (str "3 of " steps)
            (str "4 of " steps))
   :back-title "Back"
   :arrow-position :top-right
   :next-title "Next"
   :position :bottom-left
   :sel [:nav.oc-navbar :div.user-menu]})

(defn step-invite [steps _viewer?]
  {:title "Invite your team"
   :description "Carrot is built for teams. Send an invite link with an email or Slack to get started."
   :steps (str "5 of " steps)
   :back-title "Back"
   :arrow-position :left
   :next-title "Next"
   :position :right
   :sel [:div.left-navigation-sidebar :div.invite-people-box]})

(defn step-ready [steps viewer?]
  {:title "You’re ready to go! 🎉"
   :description (if viewer?
                  "Check the latest update from your team."
                  "Give it a try. Add an update or start an async discussion to get started.")
   :steps (if viewer? (str "4 of " steps) (str "6 of " steps))
   :back-title "Back"
   :arrow-position :top
   :next-title "Done"
   :position :bottom
   :post-next-cb (when-not viewer? cmail-actions/cmail-hide)
   :post-dismiss-cb (when-not viewer? cmail-actions/cmail-hide)
   :sel (if viewer?
          [:div.paginated-stream-cards :div.virtualized-list-row:first-child :div.stream-item]
          [:div.cmail-outer])})

(defn get-tooltip-data [step user-type]
  (let [viewer? (= user-type :viewer)
        steps (if viewer? 4 6)]
    (case step
      :intro    (if viewer?
                  (step-news steps viewer?)
                  (step-intro steps viewer?))
      :news     (step-news steps viewer?)
      :feed     (step-feed steps viewer?)
      :settings (step-settings steps viewer?)
      :invite   (step-invite steps viewer?)
      :ready    (step-ready steps viewer?)
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
  (when (and (or force?
                 (not (contains? @dis/app-state :nux)))
             (dis/current-org-slug)
             (= (oget js/window "location.pathname") (oc-urls/following)))
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
    :intro    (if (= user-type :viewer)
                :feed
                :news)
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
        next-step (calc-step current-value)
        same-step? (= (:step current-value) next-step)
        prepare-cb (when (and (= next-step :ready)
                              (not same-step?))
                     expand-cmail)
        delay (if (fn? prepare-cb)
                280
                0)]
    ;; In case we are stalled on a value it means NUX is finished/dismissed
    (when (:step current-value)
      (if same-step?
        (end-nux)
        (set-nux-cookie (:user-type current-value) {:step next-step})))
    (when (fn? prepare-cb)
      (prepare-cb))
    (utils/maybe-after delay #(dis/dispatch! [:input [:nux :step] next-step]))))

(defn- calc-prev-step [{:keys [step user-type]}]
  (case step
    :news     :intro
    :feed     :news
    :settings :feed
    :invite   :settings
    :ready    (if (or (= user-type :viewer)
                      (not (get @dis/app-state dis/show-invite-box-key)))
                :settings
                :invite)
    nil))

(defn prev-step []
  (let [current-value (get @dis/app-state :nux)
        prev-step (calc-prev-step current-value)
        same-step? (= (:step current-value) prev-step)
        prepare-cb (when (and (= current-value :ready)
                              (not same-step?))
                     collapse-cmail)
        delay (if (fn? prepare-cb)
                280
                0)]
    ;; In case we are stalled on a value it means NUX is finished/dismissed
    (when (:step current-value)
      (when-not same-step?
        (set-nux-cookie (:user-type current-value) {:step prev-step})))
    (when (fn? prepare-cb)
      (prepare-cb))
    (utils/maybe-after delay #(dis/dispatch! [:input [:nux :step] prev-step]))))

(defn dismiss-nux []
  (dis/dispatch! [:input [:nux :step] :dismiss])
  (end-nux))