(ns oc.web.components.stream-item
  (:require [rum.core :as rum]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [dommy.core :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [clojure.contrib.humanize :refer (filesize)]
            [oc.web.images :as img]
            [oc.web.lib.jwt :as jwt]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.shared.useragent :as ua]
            [oc.web.utils.activity :as au]
            [oc.web.mixins.activity :as am]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.utils.draft :as draft-utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.wrt :refer (wrt-count)]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.components.ui.ziggeo :refer (ziggeo-player)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.comments-summary :refer (comments-summary)]
            [cljsjs.hammer]))

(defn- stream-item-summary [activity-data]
  (if (seq (:abstract activity-data))
    [:div.stream-item-body.oc-mentions
      {:data-itemuuid (:uuid activity-data)
       :dangerouslySetInnerHTML {:__html (:abstract activity-data)}}]
    [:div.stream-item-body.no-abstract.oc-mentions
      {:data-itemuuid (:uuid activity-data)
       :dangerouslySetInnerHTML {:__html (:body activity-data)}}]))

(defn win-width []
  (or (.-clientWidth (.-documentElement js/document))
      (.-innerWidth js/window)))

(defn calc-video-height [s]
  (when (responsive/is-tablet-or-mobile?)
    (reset! (::mobile-video-height s) (utils/calc-video-height (win-width)))))

(defn- swipe-left-handler [s]
  (reset! (::show-mobile-more-bt s) false)
  (swap! (::show-mobile-dismiss-bt s) not))

(defn- swipe-right-handler [s]
  (reset! (::show-mobile-dismiss-bt s) false)
  (swap! (::show-mobile-more-bt s) not))

(defn- swipe-gesture-manager [swipe-handlers]
  {:did-mount (fn [s]
    (let [el (rum/dom-node s)
          hr (js/Hammer. el)]
      (when (= (router/current-board-slug) "inbox")
        (.on hr "swipeleft" (partial (:swipe-left swipe-handlers) s)))
      (.on hr "swiperight" (partial (:swipe-right swipe-handlers) s))
      (reset! (::hammer-recognizer s) hr)
      s))
   :will-unmount (fn [s]
    (when @(::hammer-recognizer s)
      (.remove @(::hammer-recognizer s) "swipeleft")
      (.remove @(::hammer-recognizer s) "swiperight")
      (.destroy @(::hammer-recognizer s)))
    s)})

(defn- on-scroll [s]
  (reset! (::show-mobile-dismiss-bt s) false)
  (reset! (::show-mobile-more-bt s) false))

(rum/defcs stream-item < rum/static
                         rum/reactive
                         ;; Derivatives
                         (drv/drv :activity-share-container)
                         ; (drv/drv :show-post-added-tooltip)
                         ;; Locals
                         (rum/local 0 ::mobile-video-height)
                         (rum/local nil ::hammer-recognizer)
                         (rum/local false ::force-show-menu)
                         (rum/local false ::show-mobile-dismiss-bt)
                         (rum/local false ::show-mobile-more-bt)
                         (rum/local false ::on-scroll)
                         ;; Mixins
                         (ui-mixins/render-on-resize calc-video-height)
                         (swipe-gesture-manager {:swipe-left swipe-left-handler
                                                 :swipe-right swipe-right-handler})
                         (when-not ua/edge?
                           (am/truncate-element-mixin "div.stream-item-body" (* 24 2)))
                         ui-mixins/strict-refresh-tooltips-mixin
                         {:will-mount (fn [s]
                           (calc-video-height s)
                           (when ua/mobile?
                             (reset! (::on-scroll s)
                              (events/listen js/window EventType/SCROLL (partial on-scroll s))))
                           s)
                          :will-unmount (fn [s]
                           (when @(::on-scroll s)
                             (events/unlistenByKey @(::on-scroll s))
                             (reset! (::on-scroll s) nil))
                           s)}
  [s {:keys [activity-data read-data comments-data show-wrt? editable-boards]}]
  (let [is-mobile? (responsive/is-mobile-size?)
        current-user-id (jwt/user-id)
        activity-attachments (:attachments activity-data)
        is-drafts-board (= (router/current-board-slug) utils/default-drafts-board-slug)
        is-inbox? (= (router/current-board-slug) "inbox")
        dom-element-id (str "stream-item-" (:uuid activity-data))
        is-published? (au/is-published? activity-data)
        publisher (if is-published?
                    (:publisher activity-data)
                    (first (:author activity-data)))
        is-publisher? (= (:user-id publisher) current-user-id)
        dom-node-class (str "stream-item-" (:uuid activity-data))
        has-video (seq (:fixed-video-id activity-data))
        uploading-video (dis/uploading-video-data (:video-id activity-data))
        video-player-show (and is-publisher? uploading-video)
        video-size (when has-video
                     (if is-mobile?
                       {:width (win-width)
                        :height @(::mobile-video-height s)}
                       {:width 136
                        :height (utils/calc-video-height 136)}))
        ;; Add NEW tag besides comment summary
        has-new-comments? ;; if the post has a last comment timestamp (a comment not from current user)
                          (and (:new-at activity-data)
                               ;; and that's after the user last read
                               (< (.getTime (utils/js-date (:last-read-at read-data)))
                                  (.getTime (utils/js-date (:new-at activity-data)))))
        ; post-added-tooltip (drv/react s :show-post-added-tooltip)
        ; show-post-added-tooltip? (and post-added-tooltip
        ;                               (= post-added-tooltip (:uuid activity-data)))
        mobile-more-menu-el (sel1 [:div.mobile-more-menu])
        show-mobile-menu? (and is-mobile?
                               mobile-more-menu-el)
        more-menu-comp #(more-menu
                          {:entity-data activity-data
                           :share-container-id dom-element-id
                           :editable-boards editable-boards
                           :external-share (not is-mobile?)
                           :external-bookmark (not is-mobile?)
                           :external-follow (not is-mobile?)
                           :show-edit? true
                           :show-delete? true
                           :show-move? (not is-mobile?)
                           :show-inbox? is-inbox?
                           :will-close (fn [] (reset! (::force-show-menu s) false))
                           :force-show-menu @(::force-show-menu s)
                           :mobile-tray-menu show-mobile-menu?})]
    [:div.stream-item
      {:class (utils/class-set {dom-node-class true
                                :draft (not is-published?)
                                :must-see-item (:must-see activity-data)
                                :bookmark-item (:bookmarked activity-data)
                                :unseen-item (:unseen activity-data)
                                :unread-item (:unread activity-data)
                                :expandable is-published?
                                :show-mobile-more-bt true
                                :show-mobile-dismiss-bt true
                                :showing-share (= (drv/react s :activity-share-container) dom-element-id)})
       :data-new-at (:new-at activity-data)
       :data-last-read-at (:last-read-at read-data)
       ;; click on the whole tile only for draft editing
       :on-click (fn [e]
                   (if is-drafts-board
                     (activity-actions/activity-edit activity-data)
                     (let [more-menu-el (.get (js/$ (str "#" dom-element-id " div.more-menu")) 0)
                           comments-summary-el (.get (js/$ (str "#" dom-element-id " div.is-comments")) 0)
                           stream-item-wrt-el (rum/ref-node s :stream-item-wrt)
                           emoji-picker (.get (js/$ (str "#" dom-element-id " div.emoji-mart")) 0)
                           attachments-el (rum/ref-node s :stream-item-attachments)]
                       (when (and ;; More menu wasn't clicked
                                  (not (utils/event-inside? e more-menu-el))
                                  ;; Comments summary wasn't clicked
                                  (not (utils/event-inside? e comments-summary-el))
                                  ;; WRT wasn't clicked 
                                  (not (utils/event-inside? e stream-item-wrt-el))
                                  ;; Attachments wasn't clicked
                                  (not (utils/event-inside? e attachments-el))
                                  ;; Emoji picker wasn't clicked
                                  (not (utils/event-inside? e emoji-picker))
                                  ;; a button wasn't clicked
                                  (not (utils/button-clicked? e))
                                  ;; No input field clicked
                                  (not (utils/input-clicked? e))
                                  ;; No body link was clicked
                                  (not (utils/anchor-clicked? e)))
                         (nux-actions/dismiss-post-added-tooltip)
                         (nav-actions/open-post-modal activity-data false)))))
       :id dom-element-id}
      [:button.mlb-reset.mobile-more-bt
        {:class (when @(::show-mobile-more-bt s) "visible")
         :on-click #(do
                      (reset! (::show-mobile-more-bt s) false)
                      (reset! (::force-show-menu s) true))}
        [:span "More"]]
      [:button.mlb-reset.mobile-dismiss-bt
        {:class (when @(::show-mobile-dismiss-bt s) "visible")
         :on-click #(do
                      (activity-actions/inbox-dismiss (:uuid activity-data))
                      (reset! (::show-mobile-dismiss-bt s) false))}
        [:span "Dismiss"]]
      [:div.stream-item-header.group
        [:div.stream-header-head-author
          (user-avatar-image publisher)
          [:div.name
            [:div.mobile-name
              [:div.name-inner
                {:class utils/hide-class
                 :data-toggle (when-not is-mobile? "tooltip")
                 :data-placement "top"
                 :data-container "body"
                 :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                 :data-title (utils/activity-date-tooltip activity-data)}
                (str
                 (:name publisher)
                 " in "
                 (:board-name activity-data)
                 (when (= (:board-access activity-data) "private")
                   " (private)")
                 (when (= (:board-access activity-data) "public")
                   " (public)"))]
              [:div.time-since
                (let [t (or (:published-at activity-data) (:created-at activity-data))]
                  [:time
                    {:date-time t
                     :data-toggle (when-not is-mobile? "tooltip")
                     :data-placement "top"
                     :data-container "body"
                     :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                     :data-title (utils/activity-date-tooltip activity-data)}
                    (utils/foc-date-time t)])]]
            [:div.must-see-tag]
            [:div.bookmark-tag-small.mobile-only]
            [:div.bookmark-tag.big-web-tablet-only]]]
        [:div.activity-share-container]
        (when is-published?
          (if (and is-mobile?
                   mobile-more-menu-el)
            (rum/portal (more-menu-comp) mobile-more-menu-el)
            (more-menu-comp)))]
      [:div.stream-item-body-ext.group
        [:div.thumbnail-container.group
          (if has-video
            [:div.group
             {:key (str "ziggeo-player-" (:fixed-video-id activity-data))
              :ref :ziggeo-player}
             (ziggeo-player {:video-id (:fixed-video-id activity-data)
                             :width (:width video-size)
                             :height (:height video-size)
                             :lazy (not video-player-show)
                             :video-image (:video-image activity-data)
                             :video-processed (:video-processed activity-data)
                             :playing-cb #(activity-actions/send-item-read (:uuid activity-data))})]
            (when (:body-thumbnail activity-data)
              [:div.body-thumbnail-wrapper
                {:class (:type (:body-thumbnail activity-data))}
                [:img.body-thumbnail
                  {:data-image (:thumbnail (:body-thumbnail activity-data))
                   :src (-> activity-data
                            :body-thumbnail
                            :thumbnail
                            (img/optimize-image-url (* 102 3)))}]]))
          [:div.stream-body-left.group
            {:class (utils/class-set {:has-thumbnail (:has-thumbnail activity-data)
                                      :has-video (:fixed-video-id activity-data)
                                      utils/hide-class true})}
            [:div.stream-item-headline.ap-seen-item-headline
              {:ref "activity-headline"
               :data-itemuuid (:uuid activity-data)
               :dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]
            (stream-item-summary activity-data)]]
          (if is-drafts-board
            [:div.stream-item-footer.group
              [:div.stream-body-draft-edit
                [:button.mlb-reset.edit-draft-bt
                  {:on-click #(activity-actions/activity-edit activity-data)}
                  "Continue editing"]]
              [:div.stream-body-draft-delete
                [:button.mlb-reset.delete-draft-bt
                  {:on-click #(draft-utils/delete-draft-clicked activity-data %)}
                  "Delete draft"]]]
            [:div.stream-item-footer.group
              {:ref "stream-item-reactions"}
              (reactions {:entity-data activity-data
                          :max-reactions (when is-mobile? 3)})
              [:div.stream-item-footer-mobile-group
                [:div.stream-item-comments-summary
                  ; {:on-click #(expand s true true)}
                  (comments-summary {:entry-data activity-data
                                     :comments-data comments-data
                                     :show-new-tag? has-new-comments?
                                     :hide-label? is-mobile?})]
                (when show-wrt?
                  [:div.stream-item-wrt
                    {:ref :stream-item-wrt}
                    ; (when show-post-added-tooltip?
                    ;   [:div.post-added-tooltip-container
                    ;     {:ref :post-added-tooltip}
                    ;     [:div.post-added-tooltip-title
                    ;       "Post analytics"]
                    ;     [:div.post-added-tooltip
                    ;       (str "Invite your team to Carrot so you can know who read your "
                    ;        "post and when. Click here to access your post analytics anytime.")]
                    ;     [:button.mlb-reset.post-added-tooltip-bt
                    ;       {:on-click #(nux-actions/dismiss-post-added-tooltip)}
                    ;       "OK, got it"]])
                    (wrt-count {:activity-data activity-data
                                :reads-data read-data})])
                (when (and (seq activity-attachments)
                           ;; Show attachments on FoC on mobile only if there are no reactions
                           (or (not is-mobile?)
                               (seq (:reactions activity-data))))
                  [:div.stream-item-attachments
                    {:ref :stream-item-attachments}
                    [:div.stream-item-attachments-count
                      (str (count activity-attachments)
                       " attachment" (when (> (count activity-attachments) 1) "s"))]
                    [:div.stream-item-attachments-list
                      (for [atc activity-attachments]
                        [:a.stream-item-attachments-item
                          {:href (:file-url atc)
                           :target "_blank"}
                          [:div.stream-item-attachments-item-desc
                            [:span.file-name
                              (:file-name atc)]
                            [:span.file-size
                              (str "(" (filesize (:file-size atc) :binary false :format "%.2f") ")")]]])]])]])]]))
