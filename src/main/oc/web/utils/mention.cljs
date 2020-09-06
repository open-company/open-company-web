(ns oc.web.utils.mention
  (:require [oc.web.lib.react-utils :as react-utils]
            ["react" :as react]
            ["react-dom" :as react-dom]
            ["medium-editor-tc-mention" :as tc-mention]
            [oc.web.components.ui.custom-tag :refer (CustomizedTagComponent)]))

(defn mention-ext [users-list]
  (let [mention-props {:tagName "span"
                       :extraPanelClassName "oc-mention-panel"
                       :extraTriggerClassNameMap {"@" "oc-mention"}
                       :renderPanelContent (fn [panel-el current-mention-text select-mention-callback]
                                             (js/console.log "DBG panel-el childs:" (.-children panel-el))
                                             (react-dom/render
                                              (react/createElement CustomizedTagComponent
                                                                   (clj->js {"currentMentionText" current-mention-text
                                                                             "users" (clj->js users-list)
                                                                             "selectMentionCallback" select-mention-callback}))
                                              panel-el))
                       :activeTriggerList ["@"]}]
    (tc-mention/TCMention. (clj->js mention-props))))

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