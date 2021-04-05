(ns oc.web.components.stream-item
  (:require [rum.core :as rum]
            [oops.core :refer (ocall)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [dommy.core :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [clojure.contrib.humanize :refer (filesize)]
            [oc.web.images :as img]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.utils.activity :as au]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.utils.draft :as draft-utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.wrt :refer (wrt-count)]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.mixins.gestures :refer (swipe-gesture-manager)]
            [oc.web.components.ui.labels :refer (labels-list)]
            [oc.web.components.ui.post-authorship :refer (post-authorship)]
            [oc.web.components.ui.comments-summary :refer (foc-comments-summary)]))

(defn- stream-item-summary [activity-data]
  [:div.stream-item-body.oc-mentions
    {:data-itemuuid (:uuid activity-data)
     :ref :item-body
     :class utils/hide-class
     :dangerouslySetInnerHTML {:__html (:body activity-data)}}])

(rum/defc stream-item-draft-footer <
  rum/static
  [activity-data]
  [:div.stream-item-footer.group
   [:div.stream-body-draft-edit
    [:button.mlb-reset.edit-draft-bt
     {:on-click #(activity-actions/activity-edit activity-data)}
     "Continue editing"]]
   [:div.stream-body-draft-delete
    [:button.mlb-reset.delete-draft-bt
     {:on-click #(draft-utils/delete-draft-clicked activity-data %)}
     "Delete draft"]]])

(rum/defc stream-item-attachments <
  rum/static
  [{:keys [activity-attachments is-mobile?]}]
  (when (seq activity-attachments)
    (if-not is-mobile?
      [:div.stream-item-attachments.foc-click-stop
       [:div.stream-item-attachments-count
        {:data-toggle (when-not is-mobile? "tooltip")
        :data-placement "top"
        :data-container "body"
        :title (str (count activity-attachments)
                    " attachment"
                    (when (> (count activity-attachments) 1) "s"))}
        (count activity-attachments)]
       [:div.stream-item-attachments-list
        (for [atc activity-attachments]
          [:a.stream-item-attachments-item
          {:href (:file-url atc)
            :target "_blank"}
          [:div.stream-item-attachments-item-desc
            [:span.file-name
            (:file-name atc)]
            [:span.file-size
            (str "(" (filesize (:file-size atc) :binary false :format "%.2f") ")")]]])]]
      [:div.stream-item-mobile-attachments
       [:span.mobile-attachments-icon]
       [:span.mobile-attachments-count
        (count activity-attachments)]])))

(rum/defc stream-item-footer <
  rum/static
  [s {:keys [activity-data show-wrt? member? current-activity-id
             show-new-comments? activity-attachments is-mobile?
             premium? read-data show-view-more?]}]
  [:div.stream-item-footer.group
   {:ref "stream-item-reactions"}
   (when member?
     [:div.foc-click-stop
      (reactions {:entity-data activity-data
                  :only-thumb? true
                  :hide-picker true})])
   [:div.stream-item-footer-mobile-group
    (when member?
      [:div.stream-item-comments-summary.foc-click-stop
       (foc-comments-summary {:entry-data activity-data
                              :add-comment-focus-prefix "main-comment"
                              :current-activity-id current-activity-id
                              :new-comments-count (when show-new-comments? (:unseen-comments activity-data))})])
    (when show-wrt?
      [:div.stream-item-wrt.foc-click-stop
       (wrt-count {:activity-data activity-data
                   :premium? premium?
                   :read-data read-data})])
    (stream-item-attachments {:activity-attachments activity-attachments
                              :is-mobile? is-mobile?})
    (when (seq (:labels activity-data))
      [:div.stream-item-labels.foc-click-stop
       (labels-list (:labels activity-data))])]
   (when show-view-more?
     [:div.stream-item-mobile-view-more
      "View more"])])

(defn win-width []
  (or (.-clientWidth (.-documentElement js/document))
      (.-innerWidth js/window)))

(defn calc-video-height [s]
  (when (responsive/is-tablet-or-mobile?)
    (reset! (::mobile-video-height s) (utils/calc-video-height (win-width)))))

(defn- set-foc-menu-open [s open?]
  (activity-actions/foc-menu-open (when open? (-> s :rum/args first :activity-data :uuid))))

(defn- show-mobile-menu [s]
  (set-foc-menu-open s true))

(defn- dismiss-swipe-button [s & [e ref-kw]]
  (when e
    (utils/event-stop e))
  (when (or (not ref-kw)
            (= ref-kw ::show-mobile-more-bt))
    (reset! (::show-mobile-more-bt s) false))
  (reset! (::last-mobile-swipe-menu s) nil)
  (dis/dispatch! [:input [:mobile-swipe-menu] nil]))

(defn- swipe-left-handler [s _]
  (dis/dispatch! [:input [:mobile-swipe-menu] (-> s :rum/args first :activity-data :uuid)])
  (swap! (::show-mobile-more-bt s) not))

(defn- long-press-handler [s _]
  (dismiss-swipe-button s)
  (utils/after 180 #(show-mobile-menu s)))

(defn- on-scroll [s]
  (reset! (::show-mobile-more-bt s) false))

(rum/defcs stream-item < rum/static
                         rum/reactive
                         ;; Derivatives
                         (drv/drv :activity-share-container)
                         (drv/drv :mobile-swipe-menu)
                         (drv/drv :board-slug)
                         (drv/drv :activity-uuid)
                         ; (drv/drv :show-post-added-tooltip)
                         ;; Locals
                         (rum/local 0 ::mobile-video-height)
                         (rum/local false ::show-mobile-more-bt)
                         (rum/local false ::on-scroll)
                         (rum/local nil ::last-mobile-swipe-menu)
                         (rum/local false ::show-view-more)
                         ;; Mixins
                         (ui-mixins/render-on-resize calc-video-height)
                         (when (responsive/is-mobile-size?)
                           (swipe-gesture-manager {:swipe-left swipe-left-handler
                                                   :long-press long-press-handler
                                                   :disabled #(not (au/is-published? (-> % :rum/args first :activity-data)))}))
                         ui-mixins/strict-refresh-tooltips-mixin
                         {:will-mount (fn [s]
                           (calc-video-height s)
                           (when (responsive/is-mobile-size?)
                             (reset! (::on-scroll s)
                              (events/listen js/window EventType/SCROLL (partial on-scroll s))))
                           (let [activity-data (-> s :rum/args first :activity-data)]
                             (reset! (::show-view-more s) (or (seq (:polls activity-data))
                                                              (seq (:fixed-video-id activity-data))
                                                              (seq (:thumbnail (:body-thumbnail activity-data)))
                                                              (and (seq (:body activity-data))
                                                                   (> (.-length (.text (.html (js/$ "<div/>") (:body activity-data)))) 110))))) ;; ~ 2 lines
                           s)
                          :did-update (fn [s]
                           (when (responsive/is-mobile-size?)
                             (let [mobile-swipe-menu @(drv/get-ref s :mobile-swipe-menu)
                                   activity-uuid (-> s :rum/args first :activity-data :uuid)]
                               (when (not= @(::last-mobile-swipe-menu s) mobile-swipe-menu)
                                 (reset! (::last-mobile-swipe-menu s) mobile-swipe-menu)
                                 (when (not= activity-uuid mobile-swipe-menu)
                                   (compare-and-set! (::show-mobile-more-bt s) true false)))))
                           s)
                          :will-unmount (fn [s]
                           (when @(::on-scroll s)
                             (events/unlistenByKey @(::on-scroll s))
                             (reset! (::on-scroll s) nil))
                           s)}
  [s {:keys [activity-data read-data show-wrt? editable-boards member? boards-count foc-board
             current-user-data container-slug show-new-comments? foc-menu-open foc-labels-picker
             clear-cell-measure-cb premium?]}]
  (let [is-mobile? (responsive/is-mobile-size?)
        current-user-id (:user-id current-user-data)
        activity-attachments (:attachments activity-data)
        current-board-slug (drv/react s :board-slug)
        current-activity-id (drv/react s :activity-uuid)
        dom-element-id (str "stream-item-" (:uuid activity-data))
        is-published? (au/is-published? activity-data)
        dom-node-class (str "stream-item-" (:uuid activity-data))
        ; post-added-tooltip (drv/react s :show-post-added-tooltip)
        ; show-post-added-tooltip? (and post-added-tooltip
        ;                               (= post-added-tooltip (:uuid activity-data)))
        mobile-more-menu-el (sel1 [:div.mobile-more-menu])
        mobile-more-menu? (and is-mobile?
                               mobile-more-menu-el)
        is-home? (-> container-slug keyword (= :following))
        is-entry-board? (= (dis/current-board-slug) (:board-slug activity-data))
        sharing-entry? (= (drv/react s :activity-share-container) dom-element-id)
        menu-open? (or foc-menu-open
                       (and (not is-mobile?)
                            sharing-entry?))
        more-menu-comp ;;(fn []
                         (partial more-menu
                                  {:entity-data activity-data
                                   :share-container-id dom-element-id
                                   :editable-boards editable-boards
                                   :external-share (not is-mobile?)
                                   :external-bookmark (not is-mobile?)
                                   :external-follow (not is-mobile?)
                                   :show-home-pin is-home?
                                   :show-board-pin is-entry-board?
                                   :show-edit? true
                                   :show-delete? true
                                   :show-move? (not is-mobile?)
                                   :will-open (fn [] (set-foc-menu-open s true))
                                   :will-close (fn [] (set-foc-menu-open s false))
                                   :force-show-menu menu-open?
                                   :show-labels-picker foc-labels-picker
                                   :mobile-tray-menu mobile-more-menu?
                                   :current-user-data current-user-data
                                   :external-labels true
                                   :custom-class "foc-click-stop"}) ;)
        mobile-swipe-menu-uuid (drv/react s :mobile-swipe-menu)
        show-new-item-tag (and is-home?
                               (:unseen activity-data)
                               (not (:publisher? activity-data)))
        show-body-thumbnail? (:body-thumbnail activity-data)]
    [:div.stream-item
      {:class (utils/class-set {dom-node-class true
                                :draft (not is-published?)
                                :bookmark-item (:bookmarked-at activity-data)
                                :unseen-item (:unseen activity-data)
                                :expandable is-published?
                                :muted-item (utils/link-for (:links activity-data) "follow")
                                :pinned-item (:pinned-at activity-data)
                                :show-mobile-more-bt true
                                :showing-share sharing-entry?})
       :data-last-activity-at (::last-activity-at activity-data)
       :data-last-read-at (:last-read-at activity-data)
       ;; click on the whole tile only for draft editing
       :on-click (cond (seq mobile-swipe-menu-uuid)
                       #(dismiss-swipe-button s %)
                       (not is-published?)
                       #(activity-actions/activity-edit activity-data)
                       :else
                       #(when-not (dom-utils/event-container-matches % "input, button, a, .foc-click-stop")
                          (nav-actions/open-post-modal activity-data false)))
       :id dom-element-id}
      [:button.mlb-reset.mobile-more-bt
        {:class (when @(::show-mobile-more-bt s) "visible")
         :on-click (fn [e]
                    (dismiss-swipe-button s e ::show-mobile-more-bt)
                    (show-mobile-menu s))}
        [:span.mobile-more-bt-icon]
        [:span.mobile-more-bt-text "More"]]
      [:div.stream-item-header.group
       {:class (when (seq (:labels activity-data))
                 "has-labels")}
       [:div.stream-item-header-head-author
        (post-authorship {:activity-data activity-data
                          :user-avatar? true
                          :user-hover? true
                          :board-hover? true
                          :short-name? is-mobile?
                            ; :show-board? foc-board
                          :activity-board? (and (not (:publisher-board activity-data))
                                                (not= (:board-slug activity-data) current-board-slug)
                                                (> boards-count 1))
                          :current-user-id current-user-id})
        [:div.separator-dot]
        (let [t (or (:published-at activity-data) (:created-at activity-data))]
          [:span.time-since
           {:data-toggle (when-not is-mobile? "tooltip")
            :data-placement "top"
            :data-container "body"
            :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
            :title (utils/activity-date-tooltip activity-data)}
           [:time
            {:date-time t}
            (utils/time-since t [:short :lower-case])]])
        [:div.muted-activity
         {:data-toggle (when-not is-mobile? "tooltip")
          :data-placement "top"
          :title "Muted"}]
        (when show-new-item-tag
          [:div.new-item-tag])
        [:div.bookmark-tag-small.mobile-only]
        [:div.bookmark-tag.big-web-tablet-only]
        [:div.pinned-tag]]
       (when is-published?
        (if is-mobile?
          (when mobile-more-menu?
            (rum/portal (more-menu-comp) mobile-more-menu-el))
          (more-menu-comp)))
       [:div.activity-share-container]]
      [:div.stream-item-content
        {:class (when show-body-thumbnail? "has-preview")}
        [:div.stream-item-headline.ap-seen-item-headline
          {:ref "activity-headline"
           :data-itemuuid (:uuid activity-data)
           :class utils/hide-class
           :dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]
        (stream-item-summary activity-data)
        (when show-body-thumbnail?
          [:div.stream-item-preview-container
            [:img.stream-item-preview
             {:data-image (:thumbnail (:body-thumbnail activity-data))
              :on-load clear-cell-measure-cb
              :class utils/hide-class
              :src (-> activity-data
                       :body-thumbnail
                       :thumbnail
                       (img/optimize-image-url (* 102 3)))}]])]
        (if-not is-published?
          (stream-item-draft-footer activity-data)
          (stream-item-footer s {:activity-data activity-data
                                 :show-wrt? show-wrt?
                                 :member? member?
                                 :current-activity-id current-activity-id
                                 :show-new-comments? show-new-comments?
                                 :activity-attachments activity-attachments
                                 :is-mobile? is-mobile?
                                 :premium? premium?
                                 :read-data read-data
                                 :show-view-more? @(::show-view-more s)}))]))
