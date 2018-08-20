(ns oc.web.mixins.mention
  (:require [rum.core :as rum]
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
        top-pos (- (+ (.-top mention-offset) 24) (.scrollTop $win))
                       ;; If left positoin plus the maximum width of the screen less 20px padding
        fixed-left-pos (if (> (+ left-pos 280) (- win-width 20))
                          (- left-pos (- (+ left-pos 280) (- win-width 20)))
                          left-pos)
        fixed-top-pos (if (> (+ top-pos 56) (- win-height 20))
                        (- (.-top mention-offset) (.scrollTop $win) 56 8)
                        top-pos)
        format-str (str "<div class=\"oc-mention-popup-avatar\" style=\"background-image: url('" user-avatar-url "');\"></div>"
                        "<div class=\"oc-mention-popup-name\">" user-name "</div>"
                        "<div class=\"oc-mention-popup-subline"
                         (when has-slack-username
                           " slack-icon")
                         "\">" user-subline "</div>")
        popup-node (.html (js/$ "<div contenteditable=\"false\" class=\"oc-mention-popup\">") format-str)]
    (.append $mention-el (.css popup-node #js {:left (str fixed-left-pos "px")
                                               :top (str fixed-top-pos "px")}))))

(defn- remove-hover-events [s events-list]
  (doseq [hover-ev @events-list]
    (events/unlistenByKey hover-ev))
  (reset! events-list []))

(defn- add-hover-events [s events-list]
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
                          (.remove (.find (js/$ this) ".oc-mention-popup"))))]
         (swap! events-list concat [enter-ev leave-ev]))))))

(defn oc-mentions-hover []
  (let [events-list (atom [])]
    {:did-mount (fn [s]
      (add-hover-events s events-list)
      s)
     :after-render (fn [s]
      (remove-hover-events s events-list)
      (add-hover-events s events-list)
      s)
     :will-unmount (fn [s]
      (remove-hover-events s events-list)
      s)}))