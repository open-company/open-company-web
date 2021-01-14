(ns oc.web.actions.nux
  (:require [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.lib.json :refer (json->cljs cljs->json)]))

(def expand-cmail #(cmail-actions/cmail-expand (dis/cmail-data) (dis/cmail-state)))

(def collapse-cmail #(cmail-actions/cmail-collapse (dis/cmail-state)))

(defn step-intro [steps _viewer?]
  {:title "Welcome!"
   :description "When you’re ready to add an update or some news for your team, click here anytime."
   :steps (str "1 of " steps)
   :back-title nil
   :scroll :top
   :arrow-position :top
   :position :bottom
   :next-title "Next"
   :sel [:div.cmail-outer]})

(defn step-news [steps viewer?]
  {:title (if viewer? "Welcome!" "Your news feed")
   :description "Updates and discussions from your team will show up here in your feed."
   :steps (if viewer?
            (str "1 of " steps)
            (str "2 of " steps))
   :back-title (when-not viewer? "Back")
   :scroll :top
   :arrow-position :left-top
   :next-title "Next"
   :position :right-top
   :sel [:div.left-navigation-sidebar :a.nav-link.home]})

(defn step-feed [steps viewer?]
  {:title "Personalize your feed"
   :description [[:span "Add topics, such as Design, Marketing, HR to organize your updates into groups."]
                 [:br]
                 [:span "Your teammates will be able to subscribe to topics they want to follow."]]
   :steps (if viewer?
            (str "2 of " steps)
            (str "3 of " steps))
   :back-title "Back"
   :scroll :top
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
   :scroll :top
   :arrow-position :top-right
   :next-title "Next"
   :position :bottom-left
   :sel [:nav.oc-navbar :div.user-menu]})

(defn step-invite [steps _viewer?]
  {:title "Invite your team"
   :description "Carrot is built for teams. Send an invite link with an email or Slack to get started."
   :steps (str "5 of " steps)
   :back-title "Back"
   :scroll :element
   :arrow-position :left
   :next-title "Next"
   :position :right
   :sel [:div.left-navigation-sidebar :div.invite-people-box]})

(defn step-ready [steps viewer?]
  {:title "You’re ready to go! 🎉"
   :description (if viewer?
                  "Check the latest update from your team."
                  "Give it a try. Add an update or start a discussion to get started.")
   :steps (if viewer? (str "4 of " steps) (str "6 of " steps))
   :back-title "Back"
   :scroll :top
   :arrow-position :top
   :next-title "Done"
   :position :bottom
   :post-next-cb (when-not viewer? cmail-actions/cmail-hide)
   :post-dismiss-cb (when-not viewer? cmail-actions/cmail-hide)
   :sel (if viewer?
          [:div.paginated-stream-cards :div.virtualized-list-item:first-child]
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

(defn- get-user-role []
  (some-> (dis/current-user-data) :role keyword))

(defn get-nux-cookie
  "Read the cookie from the document only if the nux-cookie-value atom is nil.
  In all the other cases return the read value in the atom."
  []
  (some-> (jwt/user-id)
          router/nux-cookie
          cook/get-cookie
          json->cljs
          (update :key #(when % (keyword %)))
          (assoc :user-type (get-user-role))))

(defn set-nux-cookie
  "Create a map for the new user cookie and save it. Also update the value of
  the nux-cookie-value atom."
  [value-map]
  (let [old-nux-cookie (get-nux-cookie)
        value-map (merge old-nux-cookie value-map)
        json-map (cljs->json value-map)
        json-string (.stringify js/JSON json-map)]
    (cook/set-cookie! (router/nux-cookie (jwt/user-id))
                      json-string
                      (* 60 60 24 7))))

(defn ^:export end-nux
  "NUX completed for the current user, remove the cookie and update the nux-cookie-value."
  []
  (dis/dispatch! [:input [:nux] nil])
  (cook/remove-cookie! (router/nux-cookie (jwt/user-id))))

(defn check-nux
  [& [force?]]
  (when (and (or force?
                 (not (contains? @dis/app-state :nux)))
             (not (responsive/is-mobile-size?))
             (dis/current-org-slug)
             (router/is-home?))
    (let [nux-state (get-nux-cookie)]
      (if (= (:key nux-state) :done)
        (end-nux)
        (dis/dispatch! [:input [:nux] (get-nux-cookie)])))))

(defn- calc-next-step
  ([] (calc-next-step (get-user-role) {}))
  ([user-type] (calc-next-step user-type {}))
  ([user-type {:keys [key]}]
   (if (= user-type :viewer)
     :done
     (case key
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
       nil       (if (= user-type :viewer)
                   :done ;; Let's turn off NUX for viewers
                   :intro)
       key))))

(defn next-step []
  (let [user-type (get-user-role)
        current-value (get @dis/app-state :nux)
        next-step (calc-next-step user-type current-value)
        same-step? (= (:key current-value) next-step)
        prepare-cb (when (and (= next-step :ready)
                              (not same-step?))
                     expand-cmail)
        delay (if (fn? prepare-cb)
                280
                0)]
    ;; In case we are stalled on a value it means NUX is finished/dismissed
    (when (:key current-value)
      (if same-step?
        (end-nux)
        (set-nux-cookie {:key next-step})))
    (when (fn? prepare-cb)
      (prepare-cb))
    (utils/maybe-after delay #(dis/dispatch! [:input [:nux :key] next-step]))))

(defn- calc-prev-step [user-type {:keys [key]}]
  (case key
    :news     :intro
    :feed     (if (= user-type :viewer) :intro :news)
    :settings :feed
    :invite   :settings
    :ready    (if (or (= user-type :viewer)
                      (not (get @dis/app-state dis/show-invite-box-key)))
                :settings
                :invite)
    nil))

(defn prev-step []
  (let [user-type (get-user-role)
        current-value (get @dis/app-state :nux)
        prev-step (calc-prev-step user-type current-value)
        same-step? (= (:key current-value) prev-step)
        prepare-cb (when (and (= current-value :ready)
                              (not same-step?))
                     collapse-cmail)
        delay (if (fn? prepare-cb)
                280
                0)]
    ;; In case we are stalled on a value it means NUX is finished/dismissed
    (when (:key current-value)
      (when-not same-step?
        (set-nux-cookie {:key prev-step})))
    (when (fn? prepare-cb)
      (prepare-cb))
    (utils/maybe-after delay #(dis/dispatch! [:input [:nux :key] prev-step]))))

(defn dismiss-nux []
  (dis/dispatch! [:input [:nux :key] :done])
  (end-nux))

(defn new-user-registered [medium-type & [cb]]
  (cook/set-cookie! (router/first-ever-landing-cookie (jwt/user-id))
                    true (* 60 60 24 7))
  (when (not= (get-user-role) :viewer)
    (cook/set-cookie! (router/show-invite-box-cookie (jwt/user-id)) true))
  (set-nux-cookie {:key (calc-next-step) :medium medium-type})
  (when (fn? cb)
    (utils/after 100 cb)))

(defn ^:export restart-nux [& [key]]
  (set-nux-cookie {:key (if key (keyword key) (calc-next-step))})
  (utils/after 280 #(check-nux true)))