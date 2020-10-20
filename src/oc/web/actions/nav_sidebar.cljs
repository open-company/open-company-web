(ns oc.web.actions.nav-sidebar
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [oc.web.urls :as oc-urls]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.actions.routing :as routing-actions]
            [oc.web.actions.section :as section-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.actions.notifications :as notif-actions]
            [oc.web.actions.contributions :as contributions-actions]
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

(defn- refresh-contributions-data [author-uuid]
  (when author-uuid
    (contributions-actions/contributions-get author-uuid)))

(defn nav-to-author!
  ([e author-uuid url]
  (nav-to-author! e author-uuid url (or (dis/route-param :back-y) (utils/page-scroll-top)) true))

  ([e author-uuid url back-y refresh?]
  (when (and e
             (.-preventDefault e))
    (.preventDefault e))
  (when (responsive/is-mobile-size?)
    (dis/dispatch! [:input [:mobile-navigation-sidebar] false])
    (notif-actions/hide-mobile-user-notifications))
  (utils/after 0 (fn []
   (let [current-path (str (.. js/window -location -pathname) (.. js/window -location -search))
         org-slug (dis/current-org-slug)
         sort-type (activity-actions/saved-sort-type org-slug author-uuid)
         org-data (dis/org-data)]
     (if (= current-path url)
       (do ;; In case user is clicking on the currently highlighted section
           ;; let's refresh the posts list only
         (routing-actions/post-routing)
         (user-actions/initial-loading refresh?))
       (do ;; If user clicked on a different section/container
           ;; let's switch to it using pushState and changing
           ;; the internal router state
         (routing-actions/routing! {:org org-slug
                                    :contributions author-uuid
                                    :sort-type sort-type
                                    :scroll-y back-y
                                    :query-params (dis/query-params)
                                    :route [org-slug author-uuid sort-type "dashboard"]})
         (.pushState (.-history js/window) #js {} (.-title js/document) url)
         (set! (.. js/document -scrollingElement -scrollTop) (utils/page-scroll-top))
         (when refresh?
           (refresh-contributions-data author-uuid)))))))))

(defn nav-to-url!
  ([e board-slug url]
  (nav-to-url! e board-slug url (or (dis/route-param :back-y) (utils/page-scroll-top)) true))

  ([e board-slug url back-y refresh?]
  (when (and e
             (.-preventDefault e))
    (.preventDefault e))
  (when (responsive/is-mobile-size?)
    (dis/dispatch! [:input [:mobile-navigation-sidebar] false])
    (notif-actions/hide-mobile-user-notifications))
  (utils/after 0 (fn []
   (let [current-path (str (.. js/window -location -pathname) (.. js/window -location -search))
         org-slug (dis/current-org-slug)
         sort-type (activity-actions/saved-sort-type org-slug board-slug)
         is-drafts-board? (= board-slug utils/default-drafts-board-slug)
         is-container? (dis/is-container? board-slug)
         org-data (dis/org-data)
         current-activity-id (dis/current-activity-id)]
     (if (= current-path url)
       (do ;; In case user clicked on the current location let's refresh it
         (routing-actions/post-routing)
         (user-actions/initial-loading refresh?))
       (do ;; If user clicked on a different section/container
           ;; let's switch to it using pushState and changing
           ;; the internal router state
         (routing-actions/routing! {:org org-slug
                                    :board board-slug
                                    :sort-type sort-type
                                    :scroll-y back-y
                                    :query-params (dis/query-params)
                                    :route [org-slug (if is-container? "dashboard" board-slug) sort-type]})
         (.pushState (.-history js/window) #js {} (.-title js/document) url)
         (when refresh?
           (activity-actions/reload-current-container))
         ;; Let's change the QP section if it's not active and going to an editable section
         (when (and (not is-container?)
                    (not is-drafts-board?)
                    (:collapsed (dis/cmail-state)))
           (when-let* [nav-to-board-data (dis/org-board-data org-data board-slug)
                       edit-link (utils/link-for (:links nav-to-board-data) "create" "POST")]
             (dis/dispatch! [:input dis/cmail-data-key {:board-slug (:slug nav-to-board-data)
                                                        :board-name (:name nav-to-board-data)
                                                        :publisher-board (:publisher-board nav-to-board-data)}])
             (dis/dispatch! [:input (conj dis/cmail-state-key :key) (utils/activity-uuid)]))))))))))

(defn dismiss-post-modal [e]
  (let [org-data (dis/org-data)
        route (dis/route)
        board (dis/current-board-slug)
        contributions-id (dis/current-contributions-id)
        is-contributions? (seq contributions-id)
        to-url (or (:back-to route) (oc-urls/following))
        cont-data (dis/current-container-data)
        should-refresh-data? (or ; Force refresh of activities if user did an action that can resort posts
                                 (:refresh route)
                                 (not cont-data))
        ;; Get the previous scroll top position
        default-back-y (or (:back-y route) (utils/page-scroll-top))
        ;; Scroll back to the previous scroll position only if the posts are
        ;; not going to refresh, if they refresh the old scroll position won't be right anymore
        back-y (if (contains? route :back-to) (.. js/document -scrollingElement -scrollTop) (utils/page-scroll-top))]
    (if is-contributions?
      (nav-to-author! e contributions-id to-url back-y false)
      (nav-to-url! e board to-url back-y false))
    (when should-refresh-data?
      (utils/after 180 activity-actions/refresh-current-container))))

(defn open-post-modal
  ([entry-data dont-scroll]
   (open-post-modal entry-data dont-scroll nil))

  ([entry-data dont-scroll comment-uuid]
  (let [org (dis/current-org-slug)
        entry-board-slug (:board-slug entry-data)
        current-sort-type (dis/current-sort-type)
        current-contributions-id (dis/current-contributions-id)
        current-board-slug (dis/current-board-slug)
        back-to (cond
                  (and (seq current-contributions-id)
                       (not current-board-slug))
                  (oc-urls/contributions current-contributions-id)
                  (= (keyword current-board-slug) :replies)
                  (oc-urls/replies)
                  (= (keyword current-board-slug) :topics)
                  (oc-urls/topics)
                  (= (keyword current-board-slug) :following)
                  (oc-urls/following)
                  :else
                  (oc-urls/board entry-board-slug))
        entry-uuid (:uuid entry-data)
        post-url (if comment-uuid
                   (oc-urls/comment-url org entry-board-slug entry-uuid comment-uuid)
                   (oc-urls/entry org entry-board-slug entry-uuid))
        query-params (dis/query-params)
        route (vec (remove nil? [org current-board-slug current-contributions-id current-sort-type entry-uuid "activity"]))
        route-path* {:org org
                     :board current-board-slug
                     :entry-board entry-board-slug
                     :contributions (dis/current-contributions-id)
                     :sort-type current-sort-type
                     :activity entry-uuid
                     :back-to back-to
                     :query-params query-params
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
    (when (or (not= panel :payments)
              ls/payments-enabled)
      (push-panel panel))
    (pop-panel)))

(defn show-user-settings [panel]
  (if panel
    (push-panel panel)
    (pop-panel)))

;; WRT

(defn show-wrt [activity-uuid]
  (push-panel (keyword (str "wrt-" activity-uuid))))

(defn hide-wrt []
  (pop-panel))

;; Integrations

(defn open-integrations-panel [e]
  (when e
    (.preventDefault e)
    (.stopPropagation e))
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

;; User info modal

(defn show-user-info [user-id]
  (push-panel (str "user-info-" user-id)))

(defn hide-user-info []
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