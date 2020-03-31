(ns oc.web.utils.mention)

(defn mention-ext [users-list]
  (let [mention-props {:tagName "span"
                       :extraPanelClassName "oc-mention-panel"
                       :extraTriggerClassNameMap {"@" "oc-mention"}
                       :renderPanelContent (fn [panel-el current-mention-text select-mention-callback]
                                            (.render js/ReactDOM
                                             (.createElement js/React
                                              js/CustomizedTagComponent
                                              (clj->js {:currentMentionText current-mention-text
                                                        :users (clj->js users-list)
                                                        :selectMentionCallback select-mention-callback}))
                                             panel-el))
                       :activeTriggerList ["@"]}]
    (js/TCMention. (clj->js mention-props))))

(defn- get-slack-usernames [user]
  (let [slack-display-name [(:slack-display-name user)]
        slack-users-usernames (vec (map :display-name (vals (:slack-users user))))]
    (remove #(or (nil? %) (= % "-")) (concat slack-users-usernames slack-display-name))))

(defn- compact-slack-usernames [users]
  (doall (map #(assoc % :slack-usernames (get-slack-usernames %)) users)))

(defn users-for-mentions [users]
  (let [fixed-users (if (map? users) (vals users) users)]
    (compact-slack-usernames (filterv #(and ;; is a carrot user
                                            (seq (:user-id %))
                                            ;; is active
                                            (or (= (:status %) "active")
                                                (= (:status %) "unverified")))
     fixed-users))))