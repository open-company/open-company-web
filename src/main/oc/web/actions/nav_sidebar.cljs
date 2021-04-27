(ns oc.web.actions.nav-sidebar
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [oc.web.urls :as oc-urls]
            [oc.web.dispatcher :as dis]
            [oc.lib.cljs.useragent :as ua]
            [oc.web.lib.utils :as utils]
            [oc.web.expo :as expo]
            [oc.web.local-settings :as ls]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.actions.routing :as routing-actions]
            [oc.web.actions.section :as section-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.actions.notifications :as notif-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]))

;; Panels
;; :menu
;; :org
;; :integrations
;; :team
;; :invite-picker
;; :invite-email
;; :invite-slack
;; :payments
;; :premium-picker
;; :profile
;; :notifications
;; :reminders
;; :reminder-{uuid}/:reminder-new
;; :section-add
;; :section-edit
;; :wrt-{uuid}
;; :theme
;; :user-info-{uuid}
;; :follow-picker

(def feed-render-delay 100)

(defn- reset-cmail-component! [to-slug]
  ;; Let's change the QP section if it's not active and going to an editable section
  (when (and (:collapsed (dis/cmail-state))
             (not= to-slug utils/default-drafts-board-slug))
    (utils/after (/ feed-render-delay 2)
     #(when-let* [nav-to-board-data (dis/org-board-data to-slug)
                  edit-link (utils/link-for (:links nav-to-board-data) "create" "POST")]
       (dis/dispatch! [:input dis/cmail-data-key {:board-slug (:slug nav-to-board-data)
                                                  :board-name (:name nav-to-board-data)}])
       (dis/dispatch! [:input (conj dis/cmail-state-key :key) (utils/activity-uuid)])))))

(def ^:private click-throttle-ms (* 1000 15))

(defonce refresh-delays (atom {}))

(defn- maybe-refresh-container [container-slug]
  (let [container-kw (keyword container-slug)]
    (when (-> refresh-delays deref container-kw not)
      ;; Set lock
      (swap! refresh-delays assoc container-kw true)
      ;; Pre-set unlock
      (utils/after click-throttle-ms #(swap! refresh-delays dissoc container-kw))
      ;; Do action
      (routing-actions/post-routing)
      ;; Refresh container and update the seen at since it's an explicit action from the user
      (activity-actions/refresh-current-container true))))

(defn- current-path []
  (str (.. js/window -location -pathname) (.. js/window -location -search)))

(defn feed-navigation!
  "Return true in case the pathname actually changed."
  [url route-map refresh?]
  (when (responsive/is-mobile-size?)
    (dis/dispatch! [:input [:mobile-navigation-sidebar] false])
    (notif-actions/hide-mobile-user-notifications))
  (if (= (current-path) url)
    (do
      (maybe-refresh-container url)
      false)
    (do
      (routing-actions/routing! route-map)
      (.pushState (.-history js/window) #js {} (.-title js/document) url)
      (when refresh?
        (utils/after feed-render-delay #(activity-actions/reload-current-container))))))

(defn nav-to-label!
  ([e label-slug url]
   (nav-to-label! e label-slug url (or (dis/route-param :back-y) (utils/page-scroll-top)) true))

  ([e label-slug url refresh?]
   (nav-to-label! e label-slug url (or (dis/route-param :back-y) (utils/page-scroll-top)) refresh?))

  ([e label-slug url back-y refresh?]
   (dom-utils/prevent-default! e)
   (when (responsive/is-mobile-size?)
     (dis/dispatch! [:input [:mobile-navigation-sidebar] false])
     (notif-actions/hide-mobile-user-notifications))
   (let [org-slug (dis/current-org-slug)
         sort-type (dis/current-sort-type)
         next-router-map {:org org-slug
                          :label label-slug
                          :sort-type sort-type
                          :scroll-y back-y
                          :query-params (dis/query-params)
                          :route [org-slug label-slug sort-type "dashboard"]
                          dis/router-opts-key [dis/router-dark-allowed-key]}]
     (feed-navigation! url next-router-map refresh?))))

(defn nav-to-author!
  ([e author-uuid url]
   (nav-to-author! e author-uuid url (or (dis/route-param :back-y) (utils/page-scroll-top)) true))

  ([e author-uuid url refresh?]
   (nav-to-author! e author-uuid url (or (dis/route-param :back-y) (utils/page-scroll-top)) refresh?))

  ([e author-uuid url back-y refresh?]
   (dom-utils/prevent-default! e)
   (let [org-slug (dis/current-org-slug)
         sort-type (dis/current-sort-type)
         next-router-map {:org org-slug
                          :contributions author-uuid
                          :sort-type sort-type
                          :scroll-y back-y
                          :query-params (dis/query-params)
                          :route [org-slug author-uuid sort-type "dashboard"]
                          dis/router-opts-key [dis/router-dark-allowed-key]}]
     (feed-navigation! url next-router-map refresh?))))

(defn nav-to-container!
  ([e board-slug url]
   (nav-to-container! e board-slug url (or (dis/route-param :back-y) (utils/page-scroll-top)) true))
  
  ([e board-slug url refresh?]
   (nav-to-container! e board-slug url (or (dis/route-param :back-y) (utils/page-scroll-top)) refresh?))

  ([e board-slug url back-y refresh?]
   (dom-utils/prevent-default! e)
   (let [org-slug (dis/current-org-slug)
         is-container? (dis/is-container? board-slug)
         sort-type (activity-actions/saved-sort-type org-slug board-slug)
         next-router-map {:org org-slug
                          :board board-slug
                          :sort-type sort-type
                          :scroll-y back-y
                          :query-params (dis/query-params)
                          :route [org-slug (if is-container? "dashboard" board-slug) sort-type]
                          dis/router-opts-key [dis/router-dark-allowed-key]}]
     (feed-navigation! url next-router-map refresh?))))

(defn dismiss-post-modal [e]
  (let [route (dis/route)
        board (dis/current-board-slug)
        contributions-id (dis/current-contributions-id)
        is-contributions? (seq contributions-id)
        label-slug (dis/current-label-slug)
        is-label? (seq label-slug)
        to-url (or (:back-to route) (oc-urls/following))
        cont-data (dis/current-container-data)
        should-refresh-data? (or ; Force refresh of activities if user did an action that can resort posts
                                 (:refresh route)
                                 (not cont-data))
        ;; Scroll back to the previous scroll position only if the posts are
        ;; not going to refresh, if they refresh the old scroll position won't be right anymore
        back-y (if (contains? route :back-to) (.. js/document -scrollingElement -scrollTop) (utils/page-scroll-top))]
    (cond is-label?
          (nav-to-label! e label-slug to-url back-y false)
          is-contributions?
          (nav-to-author! e contributions-id to-url back-y false)
          :else
          (nav-to-container! e board to-url back-y false))
    (when should-refresh-data?
      (utils/after 180 activity-actions/refresh-current-container))))

(defn open-post-modal
  ([entry-data dont-scroll]
   (open-post-modal entry-data dont-scroll nil))

  ([entry-data _dont-scroll comment-uuid]
  (let [org (dis/current-org-slug)
        entry-board-slug (:board-slug entry-data)
        current-sort-type (dis/current-sort-type)
        current-contributions-id (dis/current-contributions-id)
        current-board-slug (dis/current-board-slug)
        current-label-slug (dis/current-label-slug)
        back-to (cond (seq current-contributions-id)
                      (oc-urls/contributions current-contributions-id)
                      (seq current-label-slug)
                      (oc-urls/label current-label-slug)
                      (= (keyword current-board-slug) :replies)
                      (oc-urls/replies)
                      (= (keyword current-board-slug) :topics)
                      (oc-urls/topics)
                      (not (dis/is-container? current-board-slug))
                      (oc-urls/board current-board-slug)
                      :else
                      (oc-urls/following))
        entry-uuid (:uuid entry-data)
        post-url (if comment-uuid
                   (oc-urls/comment-url org entry-board-slug entry-uuid comment-uuid)
                   (oc-urls/entry org entry-board-slug entry-uuid))
        query-params (dis/query-params)
        route (vec (remove nil? [org current-board-slug current-contributions-id current-sort-type entry-uuid "activity"]))
        route-path* {:org org
                     :board current-board-slug
                     :entry-board entry-board-slug
                     :contributions current-contributions-id
                     :label current-label-slug
                     :sort-type current-sort-type
                     :activity entry-uuid
                     :back-to back-to
                     :query-params query-params
                     dis/router-opts-key [dis/router-dark-allowed-key]
                     :route route}
        route-path (if comment-uuid
                     (assoc route-path* :comment comment-uuid)
                     route-path*)]
    (routing-actions/routing! route-path)
    (cmail-actions/cmail-hide)
    (.pushState (.-history js/window) #js {} (.-title js/document) post-url)
    ;; Refresh the post data
    (cmail-actions/get-entry-with-uuid entry-board-slug entry-uuid))))

;; Push panel

(defn- push-panel
  "Push a panel at the top of the stack."
  [panel]
  (when (zero? (count (get @dis/app-state :panel-stack [])))
    (dom-utils/lock-page-scroll))
  (when (#{:invite-picker :invite-email :invite-slack} panel)
    (user-actions/dismiss-invite-box))
  (dis/dispatch! [:update [:panel-stack] #(vec (conj (or % []) panel))]))

(defn- pop-panel
  "Pop the panel at the top of stack from it and return it."
  []
  (let [panel-stack (:panel-stack @dis/app-state)]
    (when (pos? (count panel-stack))
      (dis/dispatch! [:update [:panel-stack] pop]))
    (when (= (count panel-stack) 1)
      (dom-utils/unlock-page-scroll))
    (peek panel-stack)))

(defn close-all-panels
  "Remove all the panels from the stack and return the stack itself.
   The return value is never used, but it's being returned to be consistent with
   the pop-panel function."
  []
  (let [panel-stack (:panel-stack @dis/app-state)]
    (dis/dispatch! [:input [:panel-stack] []])
    (dom-utils/unlock-page-scroll)
    panel-stack))

;; Section settings

(defn show-section-editor [section-slug]
  (section-actions/setup-section-editing section-slug)
  (push-panel :section-edit))

(defn hide-section-editor []
  (pop-panel))

(defn show-section-add []
  (dis/dispatch! [:input [:show-section-add-cb]
   (fn [sec-data note dismiss-action]
     (if sec-data
       (section-actions/section-save sec-data note dismiss-action)
       (when (fn? dismiss-action)
        (dismiss-action))))])
  (push-panel :section-add))

(defn show-section-add-with-callback [callback]
  (dis/dispatch! [:input [:show-section-add-cb]
   (fn [sec-data note dismiss-action]
     (callback sec-data note dismiss-action)
     (dis/dispatch! [:input [:show-section-add-cb] nil])
     (pop-panel))])
  (push-panel :section-add))

(defn hide-section-add []
  (pop-panel))

;; Reminders

(defn show-reminders []
  (push-panel :reminders))

(defn show-new-reminder []
  (push-panel :reminder-new))

(defn edit-reminder [reminder-uuid]
  (push-panel (keyword (str "reminder-" reminder-uuid))))

(defn close-reminders []
  (pop-panel))

;; Menu

(defn menu-toggle []
  (let [panel-stack (:panel-stack @dis/app-state)]
    (if (= (peek panel-stack) :menu)
      (pop-panel)
      (push-panel :menu))))

(defn menu-close []
  (pop-panel))

;; Show panels

(defn show-org-settings [panel]
  (if panel
    (push-panel panel)
    (pop-panel)))

(defn show-user-settings [panel]
  (if panel
    (push-panel panel)
    (pop-panel)))

;; WRT

(defn show-wrt [activity-uuid]
  (push-panel (keyword (str "wrt-" activity-uuid)))
  (when (responsive/is-mobile-size?)
    (set! (.. js/document -scrollingElement -scrollTop) (utils/page-scroll-top))))

(defn hide-wrt []
  (pop-panel))

;; Integrations

(defn open-integrations-panel [e]
  (dom-utils/event-stop! e)
  (if (responsive/is-mobile-size?)
    (let [alert-data {:action "mobile-integrations-link"
                      :message (str ls/product-name " integrations need to be configured in a desktop browser.")
                      :solid-button-style :green
                      :solid-button-title "OK, got it"
                      :solid-button-cb #(alert-modal/hide-alert)}]
      (alert-modal/show-alert alert-data))
    (show-org-settings :integrations)))

(set! (.-OCWebStaticOpenIntegrationsPanel js/window) open-integrations-panel)

;; Theme

(defn show-theme-settings []
  (push-panel :theme))

(defn hide-theme-settings []
  (pop-panel))

;; Follow picker

(defn show-follow-picker []
  (push-panel :follow-picker))

(defn hide-follow-picker []
  (pop-panel))

;; Invite picker

(defn ^:export open-invite-picker [e]
  (when e
    (utils/event-stop e))
  (push-panel :invite-picker))

;; Show premium picker

(defn prompt-open-pricing [msg]
  (let [alert-data {:action "mobile-prompt-open-pricing"
                    :title "Try Premium"
                    :message (or msg "This feature is available only to Premium accounts.")
                    :solid-button-style :green
                    :solid-button-title "Tell me more"
                    :solid-button-cb #(let [pricing-url (str ls/web-server-domain oc-urls/pricing)]
                                        (alert-modal/hide-alert)
                                        (if ua/mobile-app?
                                          (expo/open-in-browser pricing-url)
                                          (.open js/window pricing-url "_blank")))
                    :link-button-title "OK, got it"
                    :link-button-cb #(alert-modal/hide-alert)}]
    (alert-modal/show-alert alert-data)))

(defn ^:export toggle-premium-picker! [& [msg]]
  (when ls/payments-enabled
    (if (or (responsive/is-mobile-size?)
            ua/mobile-app?)
      (prompt-open-pricing msg)
      (dis/dispatch! [:toggle-premium-picker]))))