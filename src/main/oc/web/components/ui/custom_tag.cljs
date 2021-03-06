(ns oc.web.components.ui.custom-tag
  (:require [cuerdas.core :as string]
            [oops.core :refer (oget oget+ ocall)]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.dom :as dom-utils]
            ["react-dom" :as react-dom :refer (findDOMNode)]
            ["react" :as react]
            [goog.events :as events]
            [goog.object :refer (extend)]
            [oc.web.utils.rum :refer (create-element)]
            [goog.events.EventType :as EventType])
  (:use-macros [oc.web.macros :only [goog-extend]]))

(defn- user-display-name [user]
  (if (seq (oget user "?name"))
    (oget user "?name")
    (str (oget user "?first-name") " " (oget user "?last-name"))))

(defn- user-selected-display-value [user]
  (cond
    (and (= (oget user "?selectedKey") "slack-username")
         (oget user "?slack-username"))
    (oget user "?slack-username")
    (oget user "?name")
    (oget user "?name")
    (and (oget user "?first-name")
         (oget user "?last-name"))
    (str (oget user "?first-name") " " (oget user "?last-name"))
    (oget user "?first-name")
    (oget user "?first-name")
    (oget user "?last-name")
    (oget user "?last-name")
    :else
    (oget user "?email")))

(defn- user-slack-username [user]
  (or (oget user "?slack-username")
      (oget user "?slack-usernames.?0")))

(defn- subline-text [user]
  (oget user "?email"))

(defn list-item [props]
  (let [user (oget props "?user")
        avatar-style (clj->js {:backgroundImage (str "url(" (oget user "?avatar-url") ")")})
        display-name (user-display-name user)
        slack-username (user-slack-username user)
        alt-subline (subline-text user)]
    (create-element "div"
                    {:key (str "user-" (oget user "?user-id"))
                     :className (string/join " " ["oc-mention-option"
                                                  (when (= (oget props "?selectedIndex") (oget props "?index"))
                                                    "active ")
                                                  (when (seq (oget user "?avatar-url"))
                                                    "has-avatar ")])
                     :data-name display-name
                     :data-user-id (oget user "?user-id")
                     :data-slack-username slack-username
                     :onMouseEnter (fn [e]
                                     (let [hover-item-fn (oget props "?hoverItem")]
                                       (when (fn? hover-item-fn)
                                         (hover-item-fn e (oget props "?index")))))
                     :onClick (fn [_]
                                (when (fn? (oget props "?clickCb"))
                                  (ocall props "clickCb" user)))}
                    (create-element "div"
                                    {:className "oc-mention-option-avatar"
                                     :style avatar-style})
                    (create-element "div"
                                    {:className "oc-mention-option-title"}
                                    display-name)
                    (create-element "div"
                                    {:className (string/join " " ["oc-mention-option-subline"
                                                                  (when (seq slack-username)
                                                                    "slack-icon")])}
                                    (or slack-username alt-subline)))))

(defn- value-lookup [value search-value]
  (when (seq value)
    (some #(string/index-of % (string/lower search-value))
          (-> value
              (string/lower)
              (string/split #"\s")))))

(defn- check-slack-usernames [user current-text]
  (some (fn [slack-username]
          (when (value-lookup slack-username current-text)
            (js/Object.assign user #js {:selectedKey "slack-username"
                                        :slack-username slack-username})))
        (oget user "?slack-usernames")))

(def customized-tag-component-methods
  #js{:hidePanel (fn []
                   (this-as this
                            (let [hide-panel-fn (oget this "props.?hidePanel")]
                              (when (fn? hide-panel-fn)
                                (hide-panel-fn)))))
      :scrollActiveUserIntoView (fn []
                                  (this-as this
                                           (let [node (oget (findDOMNode this) "parentElement")
                                                 active-node (when node
                                                               (ocall node "querySelector" ".oc-mention-option.active"))]
                                             (when active-node
                                               (let [node-height (oget node "clientHeight")
                                                     node-scroll-top (oget node "scrollTop")
                                                     active-node-offset-top (oget active-node "offsetTop")
                                                     active-node-height (oget active-node "clientHeight")
                                                     active-node-scroll-bottom (+ active-node-offset-top active-node-height)
                                                     position (cond (> active-node-scroll-bottom (+ node-height node-scroll-top))
                                                                    :bottom
                                                                    (< active-node-offset-top node-scroll-top)
                                                                    :top
                                                                    :else
                                                                    :stale)
                                                     y-scroll-top (case position
                                                                    :bottom (- active-node-scroll-bottom node-height) ;; Mention is at the bottom, not visible or partially visible  
                                                                    :top    active-node-offset-top ;; Mention is at the top, not visible or partially visible
                                                                    :stale  node-scroll-top)] ;; Let it where it is, is already visible
                                                 (ocall node "scrollTo" 0 y-scroll-top)))))) ;; Stale
      :arrowKeys (fn [e options index-fn index-default]
                   (this-as this
                     (dom-utils/prevent-default! e)
                     (if-let [selected-index (oget this "?state.?selectedIndex")]
                       (.call (oget this "setState") this (clj->js {"selectedIndex" (mod (index-fn selected-index) (oget options "?length"))}))
                       (.call (oget this "setState") this (clj->js {"selectedIndex" index-default})))
                     ()
                     (.call (oget this "scrollActiveUserIntoView") this)))
      :selectCurrent (fn [e]
                       (this-as this
                                (when-let [selected-index (oget this "state.?selectedIndex")]
                                  (let [user (-> (.call (oget this "filterUsers") this (oget this "?props"))
                                                 vec
                                                 (nth selected-index))]
                                    (ocall this "selectItem" user)))
                                (utils/event-stop e)))
      :render (fn []
                (this-as this
                         (let [filtered-users (.call (oget this "filterUsers") this (oget this "props"))]
                           (create-element "div"
                                           {:className "oc-mention-options"
                                            :contentEditable false}
                                           (create-element "div"
                                                           {:className "oc-mention-options-list"}
                                                           (mapv (fn [idx]
                                                                   (let [user (get filtered-users idx)]
                                                                     (list-item #js {:user user
                                                                                     :index idx
                                                                                     :selectedIndex (oget this "?state.?selectedIndex")
                                                                                     :clickCb (.bind (oget this "?selectItem") this)
                                                                                     :hoverItem (.bind (oget this "?hoverItem") this)})))
                                                                 (range (count filtered-users))))))))
      :filterUsers (fn [properties]
                     (let [current-mention-text (oget properties "currentMentionText")
                           current-text (string/lower (subs current-mention-text 1 (count current-mention-text)))
                           mapped-users (map (fn [user-index]
                                               (let [user (oget+ properties (str "?users.?" user-index))]
                                                 (cond (value-lookup (oget user "?name") current-text)
                                                       (js/Object.assign user #js {:selectedKey "name"})
                                                                 ;;  (oset! user "selectedKey" "name")
                                                       (value-lookup (oget user "?first-name") current-text)
                                                       (js/Object.assign user #js {:selectedKey "first-name"})
                                                                 ;;  (oset! user "selectedKey" "first-name")
                                                       (value-lookup (oget user "?last-name") current-text)
                                                       (js/Object.assign user #js {:selectedKey "last-name"})
                                                                 ;;  (oset! user "selectedKey" "last-name")
                                                       (and (seq (oget user "?slack-usernames"))
                                                            (check-slack-usernames user current-text))
                                                       (check-slack-usernames user current-text)
                                                       (value-lookup (oget user "?email") current-text)
                                                       (js/Object.assign user #js {:selectedKey "email"})
                                                                 ;;  (oset! user "selectedKey" "email")
                                                       :else
                                                       user)))
                                             (range (oget properties "?users.?length")))]
                       (filterv (fn [user] (seq (oget user "?selectedKey"))) mapped-users)))
      :addBindedEvent (fn [el e-name cb]
                        (events/listen el e-name cb))
      :removeBindedEvent (fn [el e-name cb]
                           (events/unlisten el e-name cb))
      :keyPress (fn [e]
                  (this-as this
                           (let [event (or e (oget js/window "event"))
                                 node (findDOMNode this)
                                 options (when node (ocall node "querySelectorAll" ".oc-mention-option"))]
                             (when (and (not (dom-utils/is-hidden node))
                                        (seq options))
                               (case (oget event "?keyCode")
                                 (39 40) (.call (oget this "arrowKeys") this event options inc 0) ;; Right and down arrow
                                 (37 38) (.call (oget this "arrowKeys") this event options dec (dec (oget options "?length"))) ;; Left and up arrow
                                 13      (.call (oget this "selectCurrent") this event) ;; Enter
                                 9       (.call (oget this "selectCurrent") this event) ;; Tab
                                 27      (.call (oget this "hidePanel") this) ;; Esc
                                 this ;; :else
                                 )))))
      :componentDidMount (fn []
                           (this-as this
                                    (ocall this "addBindedEvent" js/window EventType/KEYDOWN (.bind (oget this "keyPress") this))))
      :componentWillUnmount (fn []
                              (this-as this
                                       (ocall this "removeBindedEvent" js/window EventType/KEYDOWN (.bind (oget this "keyPress") this))))
      :hoverItem (fn [e idx]
                   (this-as this
                            (.call (oget this "setState") this (clj->js {"selectedIndex" idx}))
                            (utils/event-stop e)))
      :selectItem (fn [user]
                    (this-as this
                             (let [select-mention-cb (.bind (oget this "props.selectMentionCallback") this)
                                   selected-value (user-selected-display-value user)]
                               (when (fn? select-mention-cb)
                                 (select-mention-cb (str "@" selected-value) user)))))})

(defn CustomizedTagComponent [props context updater]
  (this-as this
           (react/Component.call this props context updater)))

(extend (.-prototype CustomizedTagComponent)
  react/Component.prototype
  customized-tag-component-methods)