(ns oc.web.components.comment-reactions
  (:require-macros [dommy.core :refer (sel1)]
                   [if-let.core :refer (when-let*)])
  (:require [rum.core :as rum]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.comment :as cu]
            [oc.web.components.reactions :as reactions]
            [oc.web.actions.comment :as comment-actions]
            [cljsjs.web-animations]))

(rum/defcs comment-reactions
  [s activity-data item-data]
  (when (seq (:reactions item-data))
    (let [reactions-data (:reactions item-data)
          reactions-loading (:reactions-loading item-data)
          reaction-data (last reactions-data)
          is-loading (utils/in? reactions-loading (:reaction reaction-data))
          reacted (:reacted reaction-data)
          read-only-reaction (cu/is-own-comment? item-data)
          r (if is-loading
              (merge reaction-data {:count (if reacted
                                            (dec (:count reaction-data))
                                            (inc (:count reaction-data)))
                                    :reacted (not reacted)})
              reaction-data)]
      [:div.comment-reactions
        [:button.comment-reaction-btn.btn-reset
          {:key (str "comment-reactions-" (:uuid item-data))
           :class (utils/class-set {:reacted (:reacted r)
                                    :can-react (not read-only-reaction)})
           :on-click (fn [e]
                       (when (and (not is-loading) (not read-only-reaction))
                         (when (and (not (:reacted r))
                                    (not (js/isSafari))
                                    (not (js/isEdge))
                                    (not (js/isIE)))
                           (reactions/animate-reaction e s))
                         (comment-actions/comment-reaction-toggle activity-data item-data r (not reacted))))}
          [:div.reaction
            {:class (utils/class-set {:no-reactions (not (pos? (:count r)))})}
            (:reaction r)]
          [:div.count
            (when (pos? (:count r))
              (:count r))]]])))