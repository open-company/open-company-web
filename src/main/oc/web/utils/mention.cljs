(ns oc.web.utils.mention
  (:require [oops.core :refer (ocall)]
            ["react" :as react :refer (createElement)]
            [oc.web.utils.rum :refer (create-element)]
            ["react-dom" :as react-dom]
            ["medium-editor" :as medium-editor]
            ["@open-company/medium-editor-extentions" :refer (TCMention)]
            [oc.web.components.ui.custom-tag :refer (CustomizedTagComponent)]))

(defn- destroy [editor-node]
  (let [me-editor (.getEditorFromElement ^js medium-editor editor-node)
        mention-extension (ocall me-editor "getExtensionByName" "mention")]
    (when mention-extension
      (.hidePanel ^js mention-extension))))

(defn mention-ext [editor-node users-list]
  (let [mention-props {:tagName "span"
                       :extraPanelClassName "oc-mention-panel"
                       :extraTriggerClassNameMap {"@" "oc-mention"}
                       :renderPanelContent (fn [panel-el current-mention-text select-mention-callback]
                                             (react-dom/render
                                              (create-element CustomizedTagComponent
                                                              {"currentMentionText" current-mention-text
                                                               "users" (clj->js users-list)
                                                               "selectMentionCallback" select-mention-callback
                                                               "hidePanel" (fn [] (destroy editor-node))})
                                             panel-el))
                       :activeTriggerList ["@"]}]
    (TCMention. (clj->js mention-props))))

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