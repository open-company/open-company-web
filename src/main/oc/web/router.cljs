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

(defn get-token []
  (let [win-loc (oget js/window "location")
        loc-pathname (oget win-loc "pathname")
        loc-search (oget win-loc "search")]
    (when (or (not loc-pathname)
              (not loc-search))
      (sentry/capture-message! (str "Window.location problem:"
                                  "\n windown.location.pathname:" loc-pathname
                                  "\n window.location.search:" loc-search
                                  "\n return:" (str loc-pathname loc-search))))
    (str loc-pathname loc-search)))

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
    (oset! transformer "retrieveToken" (fn [path-prefix location]
                                         (str (oget location "pathname") (oget location "search"))))
    (oset! transformer "createUrl" (fn [token path-prefix location]
                                     (str path-prefix token)))
    transformer))

(defn make-history []
  (doto (goog.history.Html5History. js/window (build-transformer))
    (ocall "setPathPrefix" (str (oget js/window "location.protocol") "//" (oget js/window "location.host")))
    (ocall "setUseFragment" false)))

(def history (atom nil))

; FIXME: remove the warning of history not found
(defn nav! [token]
  (timbre/info "nav!" token)
  (timbre/debug "history:" @history)
  (ocall @history "setToken" token))

(defn rewrite-org-uuid-as-slug
  [org-uuid org-slug]
  (timbre/info "Navigate from org" org-uuid "to slug:" org-slug)
  (nav! (cstr/replace (get-token) (re-pattern org-uuid) org-slug)))

(defn rewrite-board-uuid-as-slug
  [board-uuid board-slug]
  (timbre/info "Rewrite URL from board" board-uuid "to slug:" board-slug)
  (let [new-path (cstr/replace (get-token) (re-pattern board-uuid) board-slug)]
    ; (swap! path assoc :board board-slug)
    (dis/dispatch! [:route/rewrite :board board-slug])
    (ocall js/window "history.replaceState" #js {} (oget js/window "title") new-path)))

(defn redirect! [loc]
  (timbre/info "redirect!" loc)
  (oset! js/window "location" loc))

(defn redirect-404! []
  (let [win-location (oget js/window "location")
        pathname (oget win-location "pathname")
        search (oget win-location "search")
        hash-string (oget win-location "hash")
        encoded-url (js/encodeURIComponent (str pathname search hash-string))]
    (timbre/info "redirect-404!" encoded-url)
    ;; FIXME: can't use oc-urls/not-found because importing the ns create a circular deps
    (ocall js/window "location.replace" (str "/404?path=" encoded-url))))

(defn redirect-500! []
  (let [win-location (oget js/window "location")
        pathname (oget win-location "pathname")
        search (oget win-location "search")
        hash-string (oget win-location "hash")
        encoded-url (js/encodeURIComponent (str pathname search hash-string))]
    (timbre/info "redirect-500!" encoded-url)
    ;; FIXME: can't use oc-urls/not-found because importing the ns create a circular deps
    (ocall js/window "location.replace" (str "/500?path=" encoded-url))))

(defn history-back! []
  (timbre/info "history-back!")
  (ocall js/window "history.go" -1))

(defn setup-navigation! [cb-fn]
  (let [h (doto (make-history)
            (events/listen HistoryEventType/NAVIGATE cb-fn) ;; wrap in a fn to allow live reloading
            (ocall "setEnabled" true))]
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