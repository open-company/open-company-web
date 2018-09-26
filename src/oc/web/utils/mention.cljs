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