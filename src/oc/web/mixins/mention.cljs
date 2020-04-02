(ns oc.web.mixins.mention
  (:require [rum.core :as rum]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn- add-popup-on-hover [mention-el]
  (let [$mention-el (js/$ mention-el)
        user-avatar-url (.data $mention-el "avatar-url")
        user-name (or (.data $mention-el "name") (str (.data $mention-el "first-name") " " (.data $mention-el "last-name")))
        has-slack-username (seq (.data $mention-el "slack-username"))
        user-subline (if has-slack-username
                      (.data $mention-el "slack-username")
                      (.data $mention-el "email"))
        $win (js/$ js/window)
        mention-offset (.offset $mention-el)
        win-width (.width $win)
        win-height (.height $win)
        left-pos (- (.-left mention-offset) (.scrollLeft $win))
        top-pos (- (+ (.-top mention-offset) 16) (.scrollTop $win))
                       ;; If left positoin plus the maximum width of the screen less 20px padding
        fixed-left-pos (if (> (+ left-pos 280) (- win-width 20))
                          (- left-pos (- (+ left-pos 280) (- win-width 20)))
                          left-pos)
        fixed-top-pos (if (> (+ top-pos 74) (- win-height 20))
                        (- (.-top mention-offset) (.scrollTop $win) 56 8)
                        top-pos)
        avatar-div (when user-avatar-url
                    (str "<div class=\"oc-mention-popup-avatar\" style=\"background-image: url('" user-avatar-url "');\"></div>"))
        name-div (when (and user-name (not= user-name " "))
                  (str "<div class=\"oc-mention-popup-name\">" user-name "</div>"))
        subline-div (when user-subline
                     (str "<div class=\"oc-mention-popup-subline" (when has-slack-username " slack-icon")
                          "\">" user-subline "</div>"))
        format-str (str "<div class=\"oc-mention-popup-inner\">"
                        avatar-div
                        name-div
                        subline-div
                        "</div>")
        popup-node (.html (js/$ "<div contenteditable=\"false\" class=\"oc-mention-popup\"></div>") format-str)]
    (.append $mention-el (.css popup-node #js {:left (str fixed-left-pos "px")
                                               :top (str fixed-top-pos "px")}))))

(defn- remove-events [s events-list]
  (doseq [hover-ev @events-list]
    (events/unlistenByKey hover-ev))
  (reset! events-list []))

(defn- add-events [s events-list click?]
  (let [searching-node (js/$ ".oc-mentions.oc-mentions-hover")
        all-mentions (.find searching-node ".oc-mention[data-found]")]
    (.each all-mentions
     #(this-as this
       (let [enter-ev (events/listen this EventType/MOUSEENTER
                        (fn [e]
                          (when (zero? (.-length (.find (js/$ this) ".oc-mention-popup")))
                            (add-popup-on-hover this))))
             leave-ev (events/listen this EventType/MOUSELEAVE
                        (fn [e]
                          (.remove (.find (js/$ this) ".oc-mention-popup"))))
             click-ev (when click?
                        (events/listen this EventType/CLICK
                         (fn [e]
                           (when-let [user-id (.. this -dataset -userId)]
                             (nav-actions/show-user-info user-id)))))]
         (swap! events-list concat [enter-ev leave-ev click-ev]))
       (when click?
         (.add (.-classList this) "expand-profile"))))))

(defn oc-mentions-hover [& [{:keys [click?]}]]
  (let [events-list (atom [])]
    {:did-mount (fn [s]
      (add-events s events-list click?)
      s)
     :after-render (fn [s]
      (remove-events s events-list)
      (add-events s events-list click?)
      s)
     :will-unmount (fn [s]
      (remove-events s events-list)
      s)}))