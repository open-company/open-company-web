(ns oc.web.components.ui.custom-tag
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [oops.core :refer (oget oset!)]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.react-utils :as react-utils]
            ["react" :as react]
            ["react-dom" :as react-dom])
  (:use-macros [oc.shared.macros :only [goog-extend]]))

(defn- is-hidden [el]
  (when-let [style (.getComputedStyle js/window el)]
    (or (= (oget style :?display) "none")
        (= (oget style :?visibility) "hidden")
        (nil? (oget el :?offsetParent)))))

(defn- add-event [el e-name cb]
  (cond
    (fn? (oget el :?addEventListener))
    (.addEventListener el e-name cb false)
    (fn? (oget el :?attachEvent))
    (.attachEvent el (str "on" e-name) cb)
    :else
    (oset! el (str "on" e-name) cb)))

(defn- remove-event [el e-name cb]
  (cond
    (fn? (oget el :?addEventListener))
    (.removeEventListener el e-name cb false)
    (fn? (oget el :?attachEvent))
    (.detachEvent el (str "on" e-name) cb)
    :else
    (js-delete el (str "on" e-name))))

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
  (if (= (oget user "?selectedKey") "slack-username")
    (oget user "?slack-username")
    (first (oget user "?slack-usernames"))))

(defn list-item [props]
  (let [user (oget props "?user")
        avatar-style (clj->js {:backgroundImage (str "url(" (oget user "?avatar-url") ")")})
        display-name (user-display-name user)
        slack-username (user-slack-username user)]
    (react/createElement "div"
                         (clj->js {:key (str "user-" (oget user "?user-id") "-" (oget user "?email") "-" (rand 100))
                                   :className (string/join " " ["oc-mention-option"
                                                                 (when (= (oget props "?selectedIndex") (oget props "?index"))
                                                                   "active ")
                                                                 (when (seq (oget user "?avatar-url"))
                                                                   "has-avatar ")])
                                   :data-name display-name
                                   :data-user-id (oget user "?user-id")
                                   :data-slack-username slack-username
                                   :on-mouseenter (fn [e]
                                                     (when (fn? (oget props "?hoverItem"))
                                                       (props.hoverItem e (oget props "?index"))))
                                   :on-click (fn [_]
                                               (when (fn? (oget props "?clickCb"))
                                                 (props.clickCb user)))})
                         (clj->js [(react/createElement "div" (clj->js {:className "oc-mention-option-avatar"
                                                                        :style avatar-style}))
                                   (react/createElement "div" (clj->js {:className (string/join " "
                                                                                                 ["oc-mention-option-subline"
                                                                                                  slack-username])}))]))))

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

(defn- arrow-keys [that e options index-fn index-default]
  (if (.. that -state -selectedIndex)
    (that.setState #js {:selectedIndex (mod (index-fn (.. that -state -selectedIndex)) (count options))})
    (that.setState #js {:selectedIndex index-default}))
  (utils/event-stop e))

(defn- select-current [that e]
  (when (.. that -state -selectedIndex)
    (let [user (-> (that.filterUsers (.-props that))
                   vec
                   (nth (.. that -state -selectedIndex)))]
      (.selectItem that user)))
  (utils/event-stop e))

(goog-extend CustomizedTagComponent
             react/PureComponent
             ([props]
              (this-as this
                       (goog/base this props)
                       (aset this "state" (clj->js {"selectedIndex" 0}))
                       this))
             (render []
                     (this-as this
                              ;; (goog/base (js* "this") "render")
                              (let [filtered-users (this.filterUsers (oget this "props"))]
                                (react/createElement "div"
                                                     #js {:className "oc-mention-options"
                                                          :contentEditable false}
                                                     (react/createElement "div"
                                                                          #js {:className "oc-mention-options-list"}
                                                                          (clj->js (mapv (fn [idx]
                                                                                           (let [user (get filtered-users idx)]
                                                                                             (list-item #js {:user user
                                                                                                             :index idx
                                                                                                             :selectedIndex (oget this "state.?selectedIndex")
                                                                                                             :clickCb (.bind (oget this "?selectItem") this)
                                                                                                             :hoverItem (.bind (oget this "?hoverItem") this)})))
                                                                                         (range (count filtered-users)))))))))
             (filterUsers [properties]
                          (this-as this
                                  ;;  (goog/base (js* "this") "filterUsers" properties)
                                   (let [current-mention-text (.-currentMentionText properties)
                                         current-text (string/lower (subs current-mention-text 1 (count current-mention-text)))
                                         mapped-users (map (fn [user-index]
                                                             (let [user (oget properties (str "?users.?" user-index))]
                                                               (cond
                                                                 (value-lookup (oget user "?name") current-text)
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
                                                           (range (.. properties -users -length)))]
                                     (filterv (fn [user] (seq (oget user "?selectedKey"))) mapped-users))))
             (keyPress [e]
               (this-as this
                        ;; (goog/base (js* "this") "keyPress" e)
                        (let [event (or e (.-event js/window))
                              node (react-dom/findDOMNode this)
                              options (when node (.querySelectorAll node ".oc-mention-option"))]
                          (when (and (is-hidden node)
                                     (seq options))
                            (case (oget event "?-keyCode")
                              39 ;; Right arrpw
                              (arrow-keys this event options inc 0)
                              40 ;; Down arrow
                              (arrow-keys this event options inc 0)
                              37 ;; Left arrow
                              (arrow-keys this event options dec (dec (count options)))
                              38 ;; Up arrow
                              (arrow-keys this event options dec (dec (count options)))
                              13 ;; Enter
                              (select-current this event)
                              9 ;; Tab
                              (select-current this event)
                              ;; :else
                              this)))))

             (componentDidMount []
               (this-as this
                        ;; (goog/base (js* "this") "componentDidMount")
                        (add-event js/window "keydown" (.bind (.-keyPress this) this))))

             (componentWillUnmount []
               (this-as this
                        ;; (goog/base (js* "this") "componentWillUnmount")
                        (remove-event js/window "keydown" (.bind (.-keyPress this) this))))

             (hoverItem [e idx]
               (this-as this
                        ;; (goog/base (js* "this") "hoverItem" e idx)
                        (this.setState #js {:selectedIndex idx})))

             (selectItem [user]
               (this-as this
                        ;; (goog/base (js* "this") "selectItem" user)
                        (let [selected-value (user-selected-display-value user)]))))