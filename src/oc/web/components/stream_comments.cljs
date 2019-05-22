(ns oc.web.components.stream-comments
  (:require [rum.core :as rum]
            [cljsjs.emoji-mart]
            [goog.object :as gobj]
            [goog.events :as events]
            [taoensso.timbre :as timbre]
            [goog.events.EventType :as EventType]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.comment :as cu]
            [oc.web.utils.activity :as au]
            [oc.web.lib.react-utils :as react-utils]
            [oc.web.mixins.mention :as mention-mixins]
            [oc.web.utils.reaction :as reaction-utils]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.mixins.ui :refer (on-window-click-mixin)]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn stop-editing [s]
  (let [medium-editor @(::medium-editor s)]
    (.destroy medium-editor)
    (when @(::esc-key-listener s)
      (events/unlistenByKey @(::esc-key-listener s))
      (reset! (::esc-key-listener s) nil))
    (reset! (::medium-editor s) nil)
    (reset! (::editing? s) false)))

(defn cancel-edit
  [e s c]
  (.stopPropagation e)
  (let [comment-field (rum/ref-node s (str "comment-body-" (:uuid c)))]
    (set! (.-innerHTML comment-field) (:body c))
    (stop-editing s)))

(defn edit-finished
  [e s c]
  (let [activity-data (first (:rum/args s))
        new-comment (rum/ref-node s (str "comment-body-" (:uuid c)))
        comment-text (cu/add-comment-content new-comment)]
    (if (pos? (count comment-text))
      (do
        (stop-editing s)
        (set! (.-innerHTML new-comment) comment-text)
        (comment-actions/save-comment (:uuid activity-data) c comment-text))
      (cancel-edit e s c))))

(defn start-editing [s comment-data]
  (let [comment-node (rum/ref-node s (str "comment-body-" (:uuid comment-data)))
        users-list (:mention-users @(drv/get-ref s :team-roster))
        medium-editor (cu/setup-medium-editor comment-node users-list)]
    (reset! (::esc-key-listener s)
     (events/listen
      js/window
      EventType/KEYDOWN
      (fn [e]
        (when (and (.-metaKey e)
                   (= "Enter" (.-key e)))
          (edit-finished e s comment-data)
          (.preventDefault e))
        (when (= "Escape" (.-key e))
          (cancel-edit e s comment-data)))))
    (reset! (::showing-menu s) false)
    (reset! (::medium-editor s) medium-editor)
    (reset! (::editing? s) (:uuid comment-data))
    (utils/after 600 #(utils/to-end-of-content-editable (rum/ref-node s (str "comment-body-" (:uuid comment-data)))))))

(defn delete-clicked [e activity-data comment-data]
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :action "delete-comment"
                    :message (str "Delete this comment?")
                    :link-button-title "No"
                    :link-button-cb #(alert-modal/hide-alert)
                    :solid-button-style :red
                    :solid-button-title "Yes"
                    :solid-button-cb (fn []
                                       (comment-actions/delete-comment activity-data comment-data)
                                       (alert-modal/hide-alert))
                    }]
    (alert-modal/show-alert alert-data)))

(defn scroll-to-bottom [s]
  (let [scrolling-node (rum/dom-node s)]
    (set! (.-scrollTop scrolling-node) (.-scrollHeight scrolling-node))))

(defn emoji-picked-cb [s comment-data emoji]
  (comment-actions/react-from-picker (first (:rum/args s)) comment-data (get emoji "native")))

(defn- copy-comment-url [comment-url]
  (let [input-field (.createElement js/document "input")]
    (set! (.-style input-field) "position:absolute;top:-999999px;left:-999999px;")
    (set! (.-value input-field) comment-url)
    (.appendChild (.-body js/document) input-field)
    (.select input-field)
    (utils/copy-to-clipboard input-field)
    (.removeChild (.-body js/document) input-field)))

(rum/defcs stream-comments < rum/reactive
                             (drv/drv :add-comment-focus)
                             (drv/drv :team-roster)
                             (rum/local false ::last-focused-state)
                             (rum/local false ::showing-menu)
                             (rum/local false ::editing?)
                             (rum/local false ::medium-editor)
                             (rum/local nil ::esc-key-listener)
                             (rum/local [] ::expanded-comments)
                             (rum/local nil ::show-picker)
                             (rum/local {} ::comment-url-copy)
                             (rum/local false ::highlight-comment-url)
                             (rum/local false ::initial-comment-scroll)
                             ;; Mixins
                             (mention-mixins/oc-mentions-hover)
                             (on-window-click-mixin (fn [s e]
                              (when (and @(::show-picker s)
                                         (not (utils/event-inside? e
                                          (.get (js/$ "div.emoji-mart" (rum/dom-node s)) 0))))
                                (reset! (::show-picker s) nil))))
                             {:after-render (fn [s]
                               (let [activity-uuid (:uuid (first (:rum/args s)))
                                     focused-uuid @(drv/get-ref s :add-comment-focus)
                                     current-local-state @(::last-focused-state s)
                                     is-self-focused? (= focused-uuid activity-uuid)
                                     comments-data (second (:rum/args s))]
                                  (when (not= current-local-state is-self-focused?)
                                    (reset! (::last-focused-state s) is-self-focused?)
                                    (when is-self-focused?
                                      (scroll-to-bottom s)))
                                 (timbre/info "comment-scroll after-render comments-data" (boolean comments-data) "comment-id" (router/current-comment-id))
                                 (when (and comments-data
                                            (router/current-comment-id)
                                            (not @(::initial-comment-scroll s)))
                                   (timbre/info "comment-scroll   looking for dom node")
                                   (when-let [comment-node (rum/ref-node s (str "stream-comment-" (router/current-comment-id)))]
                                     (timbre/info "comment-scroll     comment-node" comment-node)
                                     (reset! (::initial-comment-scroll s) true)
                                     (utils/after 2000 (fn []
                                      (reset! (::highlight-comment-url s) true)
                                      (utils/scroll-to-element (rum/ref-node s (str "stream-comment-" (router/current-comment-id))))
                                      (utils/after 5000(fn []
                                       (timbre/info "comment-scroll        reset highlight")
                                       (reset! (::highlight-comment-url s) false))))))))
                               s)}
  [s activity-data comments-data collapse-comments]
  [:div.stream-comments
    (if (pos? (count comments-data))
      [:div.stream-comments-list
        (for [idx (range (count comments-data))
              :let [comment-data (nth comments-data idx)
                    is-editing? (= @(::editing? s) (:uuid comment-data))
                    can-show-edit-bt? (and (:can-edit comment-data)
                                               (not (:is-emoji comment-data)))
                    can-show-delete-bt? (:can-delete comment-data)
                    showing-picker? (and (seq @(::show-picker s))
                                         (= @(::show-picker s) (:uuid comment-data)))]]
          [:div.stream-comment
            {:key (str "stream-comment-" (:created-at comment-data))
             :ref (str "stream-comment-" (:uuid comment-data))
             :class (utils/class-set {:editing is-editing?
                                      :showing-picker showing-picker?
                                      :highlighted (and @(::highlight-comment-url s)
                                                        (= (:uuid comment-data) (router/current-comment-id)))})}
            [:div.stream-comment-inner
              (when-not is-editing?
                [:div.stream-comment-floating-buttons
                  {:class (utils/class-set {:can-edit can-show-edit-bt?
                                            :can-delete can-show-delete-bt?
                                            :can-share (seq (:url comment-data))})}
                  (when can-show-edit-bt?
                    [:button.mlb-reset.edit-bt
                      {:data-toggle "tooltip"
                       :data-placement "top"
                       :title "Edit"
                       :on-click (fn [_]
                                  (start-editing s comment-data))}])
                  (when can-show-delete-bt?
                    [:button.mlb-reset.delete-bt
                      {:data-toggle "tooltip"
                       :data-placement "top"
                       :title "Delete"
                       :on-click (fn [_]
                                  (delete-clicked s activity-data comment-data))}])
                  (when (:url comment-data)
                    [:button.mlb-reset.share-bt
                      {:data-toggle "tooltip"
                       :data-placement "top"
                       :on-click #(copy-comment-url (:url comment-data))
                       :title "Share"}])
                  [:button.mlb-reset.react-bt
                    {:data-toggle "tooltip"
                     :data-placement "top"
                     :title "Add reaction"
                     :on-click #(reset! (::show-picker s) (:uuid comment-data))}]
                  (when showing-picker?
                    (react-utils/build (.-Picker js/EmojiMart)
                     {:native true
                      :onClick (fn [emoji event]
                                 (when (reaction-utils/can-pick-reaction? (gobj/get emoji "native") (:reactions comment-data))
                                   (comment-actions/react-from-picker activity-data comment-data
                                    (gobj/get emoji "native")))
                                 (reset! (::show-picker s) nil))}))])
              [:div.stream-comment-author-avatar
                (user-avatar-image (:author comment-data))]
              [:div.stream-comment-right
                [:div.stream-comment-header.group
                  {:class utils/hide-class}
                  [:div.stream-comment-author-right
                    [:div.stream-comment-author-name
                      (:name (:author comment-data))]
                    [:div.stream-comment-author-timestamp
                      (utils/foc-date-time (:created-at comment-data))]]]
                [:div.stream-comment-content
                  [:div.stream-comment-body.oc-mentions.oc-mentions-hover
                    {:dangerouslySetInnerHTML (utils/emojify (:body comment-data))
                     :ref (str "comment-body-" (:uuid comment-data))
                     :content-editable is-editing?
                     :on-click #(when-let [$body (.closest (js/$ (.-target %)) ".stream-comment-body.ddd-truncated")]
                                  (when (> (.-length $body) 0)
                                    (.restore (.data $body "dotdotdot"))
                                    (reset! (::expanded-comments s) (vec (set (conj @(::expanded-comments s) (:uuid comment-data)))))))
                     :class (utils/class-set {:emoji-comment (:is-emoji comment-data)
                                              :expanded (utils/in? @(::expanded-comments s) (:uuid comment-data))
                                              utils/hide-class true})}]]
                (if is-editing?
                  [:div.stream-comment-footer.group
                    [:div.save-cancel-edit-buttons
                      [:button.mlb-reset.save-bt
                        {:on-click #(edit-finished % s comment-data)
                         :title "Save edit"}
                        "Save"]
                      [:button.mlb-reset.cancel-bt
                        {:on-click #(cancel-edit % s comment-data)
                         :title "Cancel edit"}
                        "Cancel"]]]
                  (when (and (:can-react comment-data)
                             (seq (:reactions comment-data)))
                    [:div.stream-comment-reactions-footer.group
                      (reactions comment-data false activity-data)]))]]])]
      [:div.stream-comments-empty])])
