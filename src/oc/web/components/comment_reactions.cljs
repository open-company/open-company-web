(ns oc.web.components.comment-reactions
  (:require-macros [dommy.core :refer (sel1)]
                   [if-let.core :refer (when-let*)])
  (:require [rum.core :as rum]
            [oc.web.components.reactions :as reactions]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.jwt :as jwt]
            [cljsjs.web-animations]))

(defn- read-only?
  [item-data]
  (= (jwt/user-id) (:user-id (:author item-data))))

(defn- reaction-class-helper
  [item-data r]
  (utils/class-set {:no-reactions (not (pos? (:count r)))
                    :comment true}))

(defn- reaction-display-helper
  "Display is different if reaction is on an entry vs a comment."
  [item-data r]
  (let [count (:count r)
        reacted (:reacted r)]
    (if (pos? count)
      (if reacted
        (str "You"
             (when (> count 1) (str " and +" (dec count)))
             " agreed")
        (str "+" count " agreed"))
      (str "Agree"))))

(defn- display-reactions?
  "
   We want to skip the reactions display if the data is a comment,
   and there are no reactions and the owner of the comment is the current user.
  "
  [item-data]
  (let [reactions-data (:reactions item-data)
        reaction (when (= (count reactions-data) 1)
                   (first reactions-data))
        owner? (= (jwt/user-id) (:user-id (:author item-data)))
        skip? (and owner? (zero? (:count reaction)))]
    (not skip?)))

(rum/defcs comment-reactions
  [s item-data]
  (when (seq (:reactions item-data))
    (let [reactions-data (:reactions item-data)
          reactions-loading (:reactions-loading item-data)]
      (when (display-reactions? item-data)
       [:div.reactions
        (for [idx (range (count reactions-data))
              :let [reaction-data (get reactions-data idx)
                    is-loading (utils/in? reactions-loading (:reaction reaction-data))
                    read-only-reaction (read-only? item-data)
                    r (if is-loading
                        (merge reaction-data {:count (if (:reacted reaction-data)
                                                      (dec (:count reaction-data))
                                                      (inc (:count reaction-data)))
                                              :reacted (not (:reacted reaction-data))})
                        reaction-data)]]
          [:button.reaction-btn.btn-reset
            {:key (str "-entry-" (:uuid item-data) "-" idx)
             :class (utils/class-set {:reacted (:reacted r)
                                      :can-react (not read-only-reaction)
                                      :has-reactions (pos? (:count r))
                                      :comment true})
             :on-click (fn [e]
                         (when (and (not is-loading) (not read-only-reaction))
                           (when (and (not (:reacted r))
                                      (not (js/isSafari))
                                      (not (js/isEdge))
                                      (not (js/isIE)))
                             (reactions/animate-reaction e s))
                           (dis/dispatch! [:reaction-toggle item-data r])))}
            [:span.reaction
              {:class (reaction-class-helper item-data r)}
              (:reaction r)]
            [:div.count
              (reaction-display-helper item-data r)]])]))))