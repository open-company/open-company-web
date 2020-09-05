(ns oc.web.utils.mention
  (:require [oc.web.lib.react-utils :as react-utils]
            ["react" :as react]
            ["react-dom" :as react-dom]
            ["medium-editor-tc-mention" :as tc-mention]))

;; (defn tc-mention-comp (react-utils/build tc-mention/TCMention))

(defn mention-ext [users-list]
  (let [mention-props {:tagName "span"
                       :extraPanelClassName "oc-mention-panel"
                       :extraTriggerClassNameMap {"@" "oc-mention"}
                       :renderPanelContent (fn [panel-el current-mention-text select-mention-callback]
                                            (.render react-dom/ReactDOM
                                             (.createElement react/React
                                              tc-mention/CustomizedTagComponent
                                              (clj->js {:currentMentionText current-mention-text
                                                        :users (clj->js users-list)
                                                        :selectMentionCallback select-mention-callback}))
                                             panel-el))
                       :activeTriggerList ["@"]}]
    (js/console.log "DBG TCMention" tc-mention/TCMention)
    (react-utils/build-class tc-mention/TCMention (clj->js mention-props))))

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