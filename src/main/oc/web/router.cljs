(ns oc.web.router
  (:require [secretary.core :as secretary]
            [taoensso.timbre :as timbre]
            [goog.history.Html5History :as history5]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.history.EventType :as HistoryEventType]
            [oc.web.lib.sentry :as sentry]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.jwt :as jwt]
            [clojure.string :as cstr]
            [oops.core :refer (oget ocall oset!)]))

(defonce ^:export history (atom nil))

(defn ^:export get-token []
  (let [loc-pathname (oget js/window "location.pathname")
        loc-search (oget js/window "location.search")
        loc-string (str loc-pathname loc-search)]
    (when (or (not loc-pathname)
              (not loc-search))
      (sentry/capture-message! (str "Window.location problem:"
                                    " windown.location.pathname:" loc-pathname
                                    " window.location.search:" loc-search
                                    " return:" loc-string)))
    loc-string))

; this is needed as of this
; https://code.google.com/p/closure-library/source/detail?spec=svn
; 88dc096badf091f380b4c2b4a6514184511de657&r=88dc096badf091f380b4c2b4a6514184511de657
; setToken doen't replace the query string, it only attach it at the end
; solution here: https://github.com/Sparrho/supper/blob/master/src-cljs/supper/history.cljs
(defn build-transformer
  "Custom transformer is needed to replace query parameters, rather
  than adding to them.
  See: https://gist.github.com/pleasetrythisathome/d1d9b1d74705b6771c20"
  []
  (let [transformer (goog.history.Html5History.TokenTransformer.)]
    (set! (.. transformer -retrieveToken)
          (fn [path-prefix location]
            (str (.-pathname location) (.-search location))))
    (set! (.. transformer -createUrl)
          (fn [token path-prefix location]
            (str path-prefix token)))
    transformer))

(defn make-history []
  (doto (goog.history.Html5History. js/window (build-transformer))
    (.setPathPrefix (oget js/window "location.origin"))
    (.setUseFragment false)))

; FIXME: remove the warning of history not found
(defn ^:export nav! [token]
  (timbre/info "nav!" token)
  (timbre/debug "history:" @history)
  (.setToken ^js @history token))

(defn ^:export rewrite-org-uuid-as-slug
  [org-uuid org-slug]
  (timbre/info "Navigate from org" org-uuid "to slug:" org-slug)
  (nav! (cstr/replace (get-token) (re-pattern org-uuid) org-slug)))

(defn ^:export rewrite-board-uuid-as-slug
  [board-uuid board-slug]
  (timbre/info "Rewrite URL from board" board-uuid "to slug:" board-slug)
  (let [new-path (cstr/replace (get-token) (re-pattern board-uuid) board-slug)]
    (dis/dispatch! [:route/rewrite :board board-slug])
    (ocall js/window "history.replaceState" #js {} (oget js/window "title") new-path)))

(defn ^:export redirect! [loc]
  (timbre/info "redirect!" loc)
  (oset! js/window "location" loc))

(defn ^:export redirect-404! []
  (let [loc-pathname (oget js/window "location.pathname")
        loc-search (oget js/window "location.search")
        loc-hash (oget js/window "location.hash")
        encoded-url (ocall js/window "encodeURIComponent" (str loc-pathname loc-search loc-hash))]
    (timbre/info "redirect-404!" encoded-url)
    ;; FIXME: can't use oc-urls/not-found because importing the ns create a circular deps
    (ocall js/window "location.replace" (str "/404?path=" encoded-url))))

(defn ^:export redirect-500! []
  (let [loc-pathname (oget js/window "location.pathname")
        loc-search (oget js/window "location.search")
        loc-hash (oget js/window "location.hash")
        encoded-url (ocall js/window "encodeURIComponent" (str loc-pathname loc-search loc-hash))]
    (timbre/info "redirect-500!" encoded-url)
    ;; FIXME: can't use oc-urls/not-found because importing the ns create a circular deps
    (ocall js/window "location.replace" (str "/500?path=" encoded-url))))

(defn history-back! []
  (timbre/info "history-back!")
  (call js/window "history.go" -1))

(defn setup-navigation! [cb-fn]
  (let [h (doto (make-history)
            (events/listen HistoryEventType/NAVIGATE
              ;; wrap in a fn to allow live reloading
                           cb-fn)
            (.setEnabled true))]
    (reset! history h)))

(defn last-org-cookie
  "Cookie to save the last accessed org"
  []
  (str "last-org-" (when (jwt/jwt) (jwt/user-id))))

(defn last-board-cookie
  "Cookie to save the last accessed board"
  [org-slug]
  (str "last-board-" (when (jwt/jwt) (str (jwt/user-id) "-")) (name org-slug)))

(defn last-board-view-cookie
  "Cookie to save the last view used: grid or stream"
  [org-slug]
  (str "last-board-view-" (when (jwt/jwt) (str (jwt/get-key :user-id) "-")) (name org-slug)))

(defn last-used-board-slug-cookie
  "Cookie to save the last board slug used in a post creation"
  [org-slug]
  (str "last-used-board-slug-" (jwt/user-id) "-" (name org-slug)))

(defn last-home-cookie
  "Cookie to save the last selection in home: Following or All updates"
  [org-slug]
  (str "last-used-home-slug-" (jwt/user-id) "-" (name org-slug)))

(defn last-sort-cookie
  "Cookie to save the last sort selected"
  [org-slug]
  (str "last-sort-" (jwt/user-id) "-" (name org-slug)))

(defn last-foc-layout-cookie
  "Cookie to save the last FOC layout used"
  [org-slug]
  (str "last-foc-layout-" (jwt/user-id) "-" (name org-slug)))

(defn nux-cookie
  "Cookie to remember if the boards and journals tooltips where shown."
  [user-id]
  (str "nux-" user-id))

(defn first-ever-landing-cookie
  "Cookie used to land the user to a special URL only the first time."
  [user-id]
  (str "first-ever-ap-land-" user-id))

(defn show-add-post-tooltip-cookie
  "Cookie to check if the add first post tooltip shuold be visible."
  []
  (str "add-post-tooltip-" (jwt/user-id)))

(defn show-invite-people-tooltip-cookie
  "Cookie to check if the invite people tooltip shuold be visible."
  []
  (str "invite-people-tooltip-" (jwt/user-id)))

(defn show-invite-box-cookie
  "Cookie to check if the invite people tooltip shuold be visible."
  [user-id]
  (str "invite-box-" user-id))

(defn collapse-boards-list-cookie
  "Cookie used to remember if the sections list was collapsed or not."
  []
  (str "collapse-sections-list-" (jwt/user-id)))

(defn collapse-users-list-cookie
  "Cookie used to remember if the users list was collapsed or not."
  []
  (str "collapse-users-list-" (jwt/user-id)))

(def login-redirect-cookie "login-redirect")

(def expo-push-token-cookie "expo-push-token")