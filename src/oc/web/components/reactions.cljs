(ns oc.web.components.reactions
  (:require-macros [dommy.core :refer (sel1)]
                   [if-let.core :refer (when-let*)])
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.react-utils :as react-utils]
            [oc.web.utils.reaction :as reaction-utils]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.actions.reaction :as reaction-actions]
            [oc.web.mixins.ui :as ui-mixins]
            [cljsjs.react]
            [cljsjs.react.dom]
            [cljsjs.emoji-mart]
            [goog.object :as gobj]))

(def default-reaction-number 3)

(defn- thumb-up-reaction-map [org-uuid board-uuid resource-uuid]
  {:reaction "👍"
   :count 0
   :authors []
   :author-ids []
   :reacted false
   :links [{:rel "react"
            :method "POST"
            :href (str "/orgs/" org-uuid "/boards/" board-uuid "/resources/" resource-uuid "/reactions/👍/on")}]
   })

(rum/defcs reactions < (rum/local false ::show-picker)
                       ui-mixins/refresh-tooltips-mixin
                       (ui-mixins/on-window-click-mixin (fn [s e]
                        (when-not (utils/event-inside? e (rum/dom-node s))
                          (reset! (::show-picker s) false))))
  [s {:keys [entity-data hide-picker optional-activity-data max-reactions did-react-cb only-thumb?]}]
  ;; optional-activity-data: is passed only when rendering the list of reactions for a comment
  ;; in that case entity-data is the comment-data. When optional-activity-data is nil it means
  ;; entity-data is the activity-data
  (let [filtered-reactions (if only-thumb?
                             (filter #(= (:reaction %) "👍") (:reactions entity-data))
                             (:reactions entity-data))
        reactions-max-count (if only-thumb? 1 (or max-reactions default-reaction-number))
        reactions-data (vec (take reactions-max-count filtered-reactions))
        reactions-loading (:reactions-loading entity-data)
        react-link (utils/link-for (:links entity-data) "react")
        should-show-picker? (and (not only-thumb?)
                                 (not hide-picker)
                                 react-link
                                 (< (count reactions-data) reactions-max-count))
        is-mobile? (responsive/is-tablet-or-mobile?)]
    ;; If there are reactions to render or there is at least the link to add a reaction from the picker
    (when (or (seq reactions-data)
              should-show-picker?)
      [:div.reactions.group
        [:div.reactions-list
          (when (seq reactions-data)
            (for [idx (range (count reactions-data))
                  :let [reaction-data (get reactions-data idx)
                        is-loading (utils/in? reactions-loading (:reaction reaction-data))
                        reacted (:reacted reaction-data)
                        reaction-authors (:authors reaction-data)
                        multiple-reaction-authors? (> (count reaction-authors) 1)
                        attribution-start (if multiple-reaction-authors? "Reactions" "Reaction")
                        attribution-end (if multiple-reaction-authors?
                                          (str (clojure.string/join ", " (butlast reaction-authors))
                                               " and "
                                               (last reaction-authors))
                                          (first reaction-authors))
                        reaction-attribution (if (empty? reaction-authors)
                                                ""
                                                (str attribution-start " by " attribution-end))
                        read-only-reaction (not (utils/link-for
                                                 (:links reaction-data)
                                                 "react"
                                                 (if reacted "DELETE" "PUT")))
                        r (if is-loading
                            (merge reaction-data {:count (if reacted
                                                          (dec (:count reaction-data))
                                                          (inc (:count reaction-data)))
                                                  :reacted (not reacted)})
                            reaction-data)]]

              [:button.reaction-btn.btn-reset
                {:key (str "reaction-" (:uuid entity-data) "-" idx)
                 :class (utils/class-set {:reacted (:reacted r)
                                          :can-react (not read-only-reaction)
                                          :has-reactions (pos? (:count r))
                                          :only-thumb only-thumb?
                                          utils/hide-class true})
                 :on-mouse-leave (when-not is-mobile?
                                   #(this-as this
                                     (utils/remove-tooltips)
                                     (.tooltip (js/$ this))))
                 :title reaction-attribution
                 :data-placement "top"
                 :data-container "body"
                 :data-toggle (when-not is-mobile? "tooltip")
                 :on-click (fn [e]
                             (when (and (not is-loading) (not read-only-reaction))
                               (when (fn? did-react-cb)
                                 (did-react-cb))
                               (if optional-activity-data
                                (comment-actions/comment-reaction-toggle optional-activity-data entity-data r (not reacted))
                                (reaction-actions/reaction-toggle entity-data r (not reacted)))))}
                [:span.reaction
                  {:class (utils/class-set {:has-count (pos? (:count r))})}
                  (if only-thumb?
                    [:span.thumb-up-icon]
                    (:reaction r))]
                [:div.count
                  (:count r)]]))
          (when should-show-picker?
            [:button.reaction-btn.btn-reset.can-react.reaction-picker
              {:key (str "reaction-" (:uuid entity-data) "-picker")
               :on-click #(reset! (::show-picker s) (not @(::show-picker s)))
               :data-toggle (when-not is-mobile? "tooltip")
               :data-placement "top"
               :data-container "body"
               :title "Pick a reaction"}
              [:span.reaction]])]
       (when @(::show-picker s)
         [:div.reactions-picker-container
           (when (responsive/is-tablet-or-mobile?)
             [:button.mlb-reset.dismiss-mobile-picker
               {:on-click #(reset! (::show-picker s) false)}
               "Cancel"])
           (when-not (utils/is-test-env?)
             (react-utils/build (.-Picker js/EmojiMart)
               {:native true
                :autoFocus true
                :onClick (fn [emoji event]
                           (when (reaction-utils/can-pick-reaction? (gobj/get emoji "native") reactions-data)
                             (when (fn? did-react-cb)
                               (did-react-cb))
                             (if optional-activity-data
                               (comment-actions/react-from-picker optional-activity-data entity-data (gobj/get emoji "native"))
                               (reaction-actions/react-from-picker entity-data (gobj/get emoji "native"))))
                           (reset! (::show-picker s) false))}))])])))
