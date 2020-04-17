(ns oc.web.router
  (:require [secretary.core :as secretary]
            [taoensso.timbre :as timbre]
            [goog.history.Html5History :as history5]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.history.EventType :as HistoryEventType]
            [oc.web.lib.sentry :as sentry]
            [oc.web.lib.jwt :as jwt]
            [clojure.string :as cstr]))

(def ^{:export true} path (atom {}))

(defn set-route! [route parts]
  (timbre/info "set-route!" route parts)
  ;; Keep the ap-redirect-done flag
  (reset! path {:ap-redirect-done (:ap-redirect-done @path)})
  (swap! path assoc :route route)
  (doseq [[k v] parts] (swap! path assoc k v)))

(defn get-token []
  (when (or (not js/window.location.pathname)
            (not js/window.location.search))
    (sentry/capture-message! (str "Window.location problem:"
                                " windown.location.pathname:" js/window.location.pathname
                                " window.location.search:" js/window.location.search
                                " return:" (str js/window.location.pathname js/window.location.search))))
  (str js/window.location.pathname js/window.location.search))

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
    (.setPathPrefix (str js/window.location.protocol
                         "//"
                         js/window.location.host))
    (.setUseFragment false)))

(def history (atom nil))

; FIXME: remove the warning of history not found
(defn nav! [token]
  (timbre/info "nav!" token)
  (timbre/debug "history:" @history)
  (.setToken @history token))

(defn rewrite-org-uuid-as-slug
  [org-uuid org-slug]
  (timbre/info "Navigate from org" org-uuid "to slug:" org-slug)
  (nav! (cstr/replace (get-token) (re-pattern org-uuid) org-slug)))

(defn rewrite-board-uuid-as-slug
  [board-uuid board-slug]
  (timbre/info "Rewrite URL from board" board-uuid "to slug:" board-slug)
  (let [new-path (cstr/replace (get-token) (re-pattern board-uuid) board-slug)]
    (swap! path assoc :board board-slug)
    (.replaceState js/window.history #js {} js/window.title new-path)))

(defn redirect! [loc]
  (timbre/info "redirect!" loc)
  (set! (.-location js/window) loc))

(defn redirect-404! []
  (let [win-location (.-location js/window)
        pathname (.-pathname win-location)
        search (.-search win-location)
        hash-string (.-hash win-location)
        encoded-url (js/encodeURIComponent (str pathname search hash-string))]
    (timbre/info "redirect-404!" encoded-url)
    ;; FIXME: can't use oc-urls/not-found because importing the ns create a circular deps
    (.replace (.-location js/window) (str "/404?path=" encoded-url))))

(defn redirect-500! []
  (let [win-location (.-location js/window)
        pathname (.-pathname win-location)
        search (.-search win-location)
        hash-string (.-hash win-location)
        encoded-url (js/encodeURIComponent (str pathname search hash-string))]
    (timbre/info "redirect-500!" encoded-url)
    ;; FIXME: can't use oc-urls/not-found because importing the ns create a circular deps
    (.replace (.-location js/window) (str "/500?path=" encoded-url))))

(defn history-back! []
  (timbre/info "history-back!")
  (.go (.-history js/window) -1))

(defn setup-navigation! [cb-fn]
  (let [h (doto (make-history)
            (events/listen HistoryEventType/NAVIGATE
              ;; wrap in a fn to allow live reloading
              cb-fn)
            (.setEnabled true))]
    (reset! history h)))

;; Path components retrieve
(defn current-org-slug []
  (:org @path))

(defn current-board-slug []
  (:board @path))

(defn current-posts-filter []
  (:board @path))

(defn current-activity-id []
  (:activity @path))

(defn current-secure-activity-id []
  (:secure-id @path))

(defn current-comment-id []
  (:comment @path))

(defn current-contributions-id []
  (:contributions @path))

(defn query-params []
  (:query-params @path))

(defn query-param [k]
  (get (:query-params @path) k nil))

(defn ap-redirect []
  (:ap-redirect-done @path))

(defn ap-redirect-done! []
  (swap! path merge {:ap-redirect-done true}))

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

(defn collapse-sections-list-cookie
  "Cookie used to remember if the sections list was collapsed or not."
  []
  (str "collapse-sections-list-" (jwt/user-id)))

(defn collapse-users-list-cookie
  "Cookie used to remember if the users list was collapsed or not."
  []
  (str "collapse-users-list-" (jwt/user-id)))

(def login-redirect-cookie "login-redirect")

(def expo-push-token-cookie "expo-push-token")

;; Debug

(defn print-router-path []
  @path)

(set! (.-OCWebPrintRouterPath js/window) print-router-path)
