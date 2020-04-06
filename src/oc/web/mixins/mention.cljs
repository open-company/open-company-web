(ns oc.web.mixins.mention
  (:require-macros [if-let.core :refer (if-let*)])
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oops.core :refer (oget oset!)]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.user-info-hover :refer (user-info-otf)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

; (defn- add-popup-on-hover [mention-el]
;   (let [$mention-el (js/$ mention-el)
;         user-avatar-url (.data $mention-el "avatar-url")
;         user-name (or (.data $mention-el "name") (str (.data $mention-el "first-name") " " (.data $mention-el "last-name")))
;         has-slack-username (seq (.data $mention-el "slack-username"))
;         user-subline (if has-slack-username
;                       (.data $mention-el "slack-username")
;                       (.data $mention-el "email"))
;         $win (js/$ js/window)
;         mention-offset (.offset $mention-el)
;         win-width (.width $win)
;         win-height (.height $win)
;         left-pos (- (.-left mention-offset) (.scrollLeft $win))
;         top-pos (- (+ (.-top mention-offset) 16) (.scrollTop $win))
;                        ;; If left positoin plus the maximum width of the screen less 20px padding
;         fixed-left-pos (if (> (+ left-pos 280) (- win-width 20))
;                           (- left-pos (- (+ left-pos 280) (- win-width 20)))
;                           left-pos)
;         fixed-top-pos (if (> (+ top-pos 74) (- win-height 20))
;                         (- (.-top mention-offset) (.scrollTop $win) 56 8)
;                         top-pos)
;         avatar-div (when user-avatar-url
;                     (str "<div class=\"oc-mention-popup-avatar\" style=\"background-image: url('" user-avatar-url "');\"></div>"))
;         name-div (when (and user-name (not= user-name " "))
;                   (str "<div class=\"oc-mention-popup-name\">" user-name "</div>"))
;         subline-div (when user-subline
;                      (str "<div class=\"oc-mention-popup-subline" (when has-slack-username " slack-icon")
;                           "\">" user-subline "</div>"))
;         format-str (str "<div class=\"oc-mention-popup-inner\">"
;                         avatar-div
;                         name-div
;                         subline-div
;                         "</div>")
;         popup-node (.html (js/$ "<div contenteditable=\"false\" class=\"oc-mention-popup\"></div>") format-str)]
;     (.append $mention-el (.css popup-node #js {:left (str fixed-left-pos "px")
;                                                :top (str fixed-top-pos "px")}))))

(defn- remove-events [s events-list]
  (doseq [hover-ev @events-list]
    (events/unlistenByKey hover-ev))
  (reset! events-list []))

(defn- add-events [s events-list click?]
  (let [dom-node (rum/dom-node s)
        searching-node (.find (js/$ dom-node)".oc-mentions.oc-mentions-hover")
        all-mentions (.find searching-node ".oc-mention[data-found]")]
    (.each all-mentions
     #(this-as this
       (let [enter-ev (events/listen this EventType/MOUSEENTER
                        (fn [e]
                          (when-let [mount-el (::mount-el s)]
                            (if-let* [_false false
                                      active-users (when click?
                                                     @(drv/get-ref s :users-info-hover))
                                      current-user-data (when click?
                                                          @(drv/get-ref s :current-user-data))
                                      user-id (oget this "dataset" "?userId")
                                      user-data (get active-users user-id)]
                              (rum/mount
                               (user-info-otf {:user-data user-data
                                               :portal-el this
                                               :my-profile (= (:user-id current-user-data) user-id)})
                               mount-el)
                              (let [slack-username (oget this "dataset" "?slackUsername")
                                    user-data {:first-name (oget this "dataset" "?firstName")
                                               :last-name (oget this "dataset" "?lastName")
                                               :avatar-url (oget this "dataset" "?avatarUrl")
                                               :title (if (seq slack-username)
                                                        slack-username
                                                        (oget this "dataset" "?email"))
                                               :slack-icon (seq slack-username)}]
                                (rum/mount
                                 (user-info-otf {:user-data user-data
                                                 :portal-el this
                                                 :hide-buttons true
                                                 ; :my-profile false
                                                 })
                                 mount-el)))
                            (reset! (::portal-mounted s) true))))
                            ; (when (zero? (.-length (.find (js/$ this) ".oc-mention-popup")))
                            ;   (add-popup-on-hover this)))))
             leave-ev (events/listen this EventType/MOUSELEAVE
                        (fn [e]
                          (.remove (.find (js/$ this) ".oc-mention-popup"))
                          (when @(::portal-mounted s)
                            (rum/unmount (::mount-el s)))
                          (reset! (::portal-mounted s) false)))
             click-ev (when click?
                        (events/listen this EventType/CLICK
                         (fn [e]
                           (when-let [user-id (oget this "dataset.?userId")]
                             (nav-actions/show-user-info user-id)))))]
         (swap! events-list concat [enter-ev leave-ev click-ev]))
       (when click?
         (.add (.-classList this) "expand-profile"))))))

(defn- mount-div []
  (let [d (.createElement js/document "div")
        _ (oset! d "id" (int (rand 10000)))
        _ (.appendChild (.-body js/document) d)]
    d))

(defn oc-mentions-hover
  "Mixin to show a popup when hovering a span.oc-mention with the user's info.
   If click? is true it shows the user's info popup with buttons to open profile and posts list.
   To use click? it needs the following mixins:
   - rum/reactive
   - (drv/drv :current-user-data)
   - (drv/drv :users-info-hover)"
  [& [{:keys [click?]}]]
  (let [events-list (atom [])]
    {:did-mount (fn [s]
      (add-events s events-list click?)
      (-> s
       (assoc ::mount-el (mount-div))
       (assoc ::portal-mounted (atom false))))
     :after-render (fn [s]
      (remove-events s events-list)
      (add-events s events-list click?)
      s)
     :will-unmount (fn [s]
      (remove-events s events-list)
      (if-let [mount-el (::mount-el s)]
        (do
          (when @(::portal-mounted s)
            (rum/unmount mount-el))
          (when (and mount-el
                     (.-parentElement mount-el))
            (.removeChild (.-body js/document) mount-el))
          (dissoc s ::mount-el))
        s))}))