(ns oc.web.components.reactions
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]
            [oc.lib.cljs.useragent  :as ua]
            [oc.web.lib.responsive :as responsive]
            [oc.web.utils.rum :as rutils]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.utils.reaction :as reaction-utils]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.actions.reaction :as reaction-actions]
            [oc.web.mixins.ui :as ui-mixins]
            ["emoji-mart" :as emoji-mart :refer (Picker)]))

(def emoji-mart-picker (partial rutils/build Picker))

(def default-reaction-number 3)

(defn- thumb-reaction? [r]
  (= (:reaction r) "👍"))

(rum/defcs reactions < (rum/local false ::show-picker)
                       (rum/local false ::mounted)
                       ui-mixins/refresh-tooltips-mixin
                       (ui-mixins/on-click-out #(reset! (::show-picker %1) false))
  [s {:keys [entity-data hide-picker optional-activity-data max-reactions did-react-cb only-thumb? thumb-first?]}]
  ;; optional-activity-data: is passed only when rendering the list of reactions for a comment
  ;; in that case entity-data is the comment-data. When optional-activity-data is nil it means
  ;; entity-data is the activity-data
  (let [first-reactions (when (or thumb-first? only-thumb?)
                          (filter thumb-reaction? (:reactions entity-data)))
        rest-reactions (cond thumb-first?
                             (filter (comp not thumb-reaction?) (:reactions entity-data))
                             only-thumb?
                             []
                             :else
                             (:reactions entity-data))
        all-reactions (vec (remove nil? (concat first-reactions rest-reactions)))
        reactions-max-count (if only-thumb? 1 (or max-reactions default-reaction-number))
        reactions-data (vec (take reactions-max-count all-reactions))
        reactions-loading (:reactions-loading entity-data)
        react-link (utils/link-for (:links entity-data) "react")
        should-show-picker? (and (not only-thumb?)
                                 (not thumb-first?)
                                 (not hide-picker)
                                 react-link
                                 (< (count reactions-data) reactions-max-count))
        is-mobile? (responsive/is-tablet-or-mobile?)]
    ;; If there are reactions to render or there is at least the link to add a reaction from the picker
    (when (or (seq reactions-data)
              should-show-picker?)
      [:div.reactions
        [:div.reactions-list.group
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
                            reaction-data)
                        thumb? (and (or only-thumb? thumb-first?) (thumb-reaction? r))]]

              [:button.reaction-btn.btn-reset
                {:key (str "reaction-" (:uuid entity-data) "-" idx)
                 :class (utils/class-set {:reacted (:reacted r)
                                          :can-react (not read-only-reaction)
                                          :has-reactions (pos? (:count r))
                                          :only-thumb thumb?
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
                  {:class (utils/class-set {:has-count (pos? (:count r))
                                            :safari ua/safari?})}
                  (if thumb?
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
             (emoji-mart-picker
               {:native true
                :autoFocus true
                :onClick (fn [emoji _event]
                           (when (reaction-utils/can-pick-reaction? (dom-utils/get-native-emoji emoji) reactions-data)
                             (when (fn? did-react-cb)
                               (did-react-cb))
                             (if optional-activity-data
                               (comment-actions/react-from-picker optional-activity-data entity-data (dom-utils/get-native-emoji emoji))
                               (reaction-actions/react-from-picker entity-data (dom-utils/get-native-emoji emoji))))
                           (reset! (::show-picker s) false))}))])])))
