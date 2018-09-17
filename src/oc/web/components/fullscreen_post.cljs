(ns oc.web.components.fullscreen-post
  (:require [rum.core :as rum]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.mixins.ui :as mixins]
            [oc.web.local-settings :as ls]
            [oc.web.utils.activity :as au]
            [oc.web.utils.ui :as ui-utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.wrt :refer (wrt)]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.more-menu :refer (more-menu)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.stream-comments :refer (stream-comments)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.rich-body-editor :refer (rich-body-editor)]
            [oc.web.components.ui.sections-picker :refer (sections-picker)]
            [oc.web.components.ui.comments-summary :refer (comments-summary)]
            [oc.web.components.ui.ziggeo :refer (ziggeo-player ziggeo-recorder)]
            [oc.web.components.ui.stream-attachments :refer (stream-attachments)]))

;; Modal dismiss handling

(defn dismiss-modal [s]
  (let [modal-data @(drv/get-ref s :fullscreen-post-data)]
    (activity-actions/activity-modal-fade-out (:board-slug (:activity-data modal-data)))))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 #(dismiss-modal s)))

(defn show-post-error [s message]
  (when-let [$post-btn (js/$ (rum/ref-node s "mobile-post-btn"))]
    (if-not (.data $post-btn "bs.tooltip")
      (.tooltip $post-btn
       (clj->js {:container "body"
                 :placement "bottom"
                 :trigger "manual"
                 :template (str "<div class=\"tooltip post-btn-tooltip\">"
                                  "<div class=\"tooltip-arrow\"></div>"
                                  "<div class=\"tooltip-inner\"></div>"
                                "</div>")
                 :title message}))
      (doto $post-btn
        (.attr "data-original-title" message)
        (.tooltip "fixTitle")))
    (utils/after 10 #(.tooltip $post-btn "show"))
    (utils/after 5000 #(.tooltip $post-btn "hide"))))

(defn send-item-read-if-needed [s]
  (let [post-data @(drv/get-ref s :fullscreen-post-data)
        activity-data (:activity-data post-data)]
    (when (= (:status activity-data) "published")
      ;; Check if the page is at the bottom of the scroll
      (let [body-el (rum/ref-node s :fullscreen-post-box-content-body)]
        (when (au/is-element-bottom-visible? body-el)
          (activity-actions/wrt-events-gate (:uuid activity-data)))))))

(defn win-width []
  (or (.-innerWidth js/window)
      (.-clientWidth (.-documentElement js/document))))

(defn calc-video-height [s]
  (when (responsive/is-tablet-or-mobile?)
    (reset! (::mobile-video-height s) (utils/calc-video-height (win-width)))))

(rum/defcs fullscreen-post < rum/reactive
                             ;; Derivatives
                             (drv/drv :fullscreen-post-data)
                             ;; Locals
                             (rum/local false ::dismiss)
                             (rum/local false ::animate)
                             (rum/local nil ::window-scroll)
                             (rum/local 0 ::mobile-video-height)
                             ;; Mixins
                             (when-not (responsive/is-mobile-size?)
                               mixins/no-scroll-mixin)
                             (mixins/render-on-resize calc-video-height)
                             mixins/first-render-mixin

                             {:before-render (fn [s]
                               (let [modal-data @(drv/get-ref s :fullscreen-post-data)]
                                 ;; Animate the view if needed
                                 (when (and (not @(::animate s))
                                          (= (:activity-modal-fade-in modal-data) (:uuid (:activity-data modal-data))))
                                   (reset! (::animate s) true)))
                               s)
                              :will-mount (fn [s]
                               (let [modal-data @(drv/get-ref s :fullscreen-post-data)]
                                 ;; Force comments reload
                                 (comment-actions/get-comments (:activity-data modal-data)))
                               s)
                              :did-mount (fn [s]
                               (reset! (::window-scroll s)
                                (events/listen
                                 (rum/ref-node s :fullscreen-post-container)
                                 EventType/SCROLL
                                 #(send-item-read-if-needed s)))
                               (send-item-read-if-needed s)
                               (calc-video-height s)
                               (when ls/oc-enable-transcriptions
                                 (ui-utils/resize-textarea (rum/ref-node s "transcript-edit")))
                               s)
                              :did-remount (fn [_ s]
                               (when ls/oc-enable-transcriptions
                                 (ui-utils/resize-textarea (rum/ref-node s "transcript-edit")))
                               s)
                              :will-unmount (fn [s]
                               (when @(::window-scroll s)
                                 (events/unlistenByKey @(::window-scroll s))
                                 (reset! (::window-scroll s) nil))
                               (set! (.-onbeforeunload js/window) nil)
                               s)}
  [s]
  (let [modal-data (drv/react s :fullscreen-post-data)
        activity-data (:activity-data modal-data)
        is-mobile? (responsive/is-tablet-or-mobile?)
        dom-element-id (str "fullscreen-post-" (:uuid activity-data))
        activity-comments (-> modal-data
                              :comments-data
                              (get (:uuid activity-data))
                              :sorted-comments)
        comments-data (or activity-comments (:comments activity-data))
        read-data (:read-data modal-data)
        video-id (:fixed-video-id activity-data)
        activity-attachments (:attachments activity-data)
        video-size (if is-mobile?
                     {:width (win-width)
                      :height @(::mobile-video-height s)}
                     {:width 640
                      :height (utils/calc-video-height 640)})]
    [:div.fullscreen-post-container.group
      {:class (utils/class-set {:will-appear (or @(::dismiss s)
                                                 (and @(::animate s)
                                                      (not @(:first-render-done s))))
                                :appear (and (not @(::dismiss s)) @(:first-render-done s))
                                :no-comments (not (:has-comments activity-data))})
       :ref :fullscreen-post-container
       :id dom-element-id}
      [:div.fullscreen-post-header
        [:button.mlb-reset.mobile-modal-close-bt
          {:on-click #(close-clicked s)}]
        [:div.header-title-container.group.fs-hide
          {:key (:updated-at activity-data)
           :dangerouslySetInnerHTML #js {:__html (if (seq (:headline activity-data))
                                                   (:headline activity-data)
                                                   utils/default-headline)}}]
        [:div.fullscreen-post-header-right
          [:div.activity-share-container]
          (more-menu activity-data dom-element-id {:tooltip-position "left" :external-share true})]]
      [:div.fullscreen-post.group
        {:ref "fullscreen-post"}
        [:div.fullscreen-post-author-header.group
          [:div.fullscreen-post-author-header-author
            (user-avatar-image (:publisher activity-data))
            [:div.name-container.group
              [:div.name.fs-hide
                (str (:name (:publisher activity-data))
                 " in "
                 (:board-name activity-data))]
              (when (:new activity-data)
                [:div.new-tag
                  "New"])]
            [:div.fullscreen-post-author-header-sub
              [:div.time-since
                (let [t (or (:published-at activity-data) (:created-at activity-data))]
                  [:time
                    {:date-time t
                     :data-toggle (when-not is-mobile? "tooltip")
                     :data-placement "top"
                     :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
                     :data-title (utils/activity-date-tooltip activity-data)}
                    (utils/time-since t)])]
              [:div.separator]
              [:div.fullscreen-post-wrt
                (wrt activity-data read-data)]]]]
        ;; Left column
        [:div.fullscreen-post-left-column
          [:div.fullscreen-post-left-column-content.group
            ;; Video element
            (when video-id
              (ziggeo-player {:video-id video-id
                              :width (:width video-size)
                              :height (:height video-size)
                              :video-processed (:video-processed activity-data)}))
            [:div.fullscreen-post-box-content-headline.fs-hide
              {:key (str "fullscreen-post-headline-" (:updated-at activity-data))
               :dangerouslySetInnerHTML #js {:__html (:headline activity-data)}}]
            (when (:must-see activity-data)
              [:div.must-see
               {:class (utils/class-set {:must-see-on (:must-see activity-data)})}])
            [:div.fullscreen-post-box-content-body.fs-hide
              {:key (str "fullscreen-post-body-" (:updated-at activity-data))
               :ref :fullscreen-post-box-content-body
               :dangerouslySetInnerHTML #js {:__html (:body activity-data)}}]
            (when (and ls/oc-enable-transcriptions
                       (:video-transcript activity-data)
                       (:video-processed activity-data))
              [:div.fullscreen-post-transcript
                [:div.fullscreen-post-transcript-header
                  "This transcript was automatically generated and may not be accurate"]
                [:div.fullscreen-post-transcript-content
                  (:video-transcript activity-data)]])
            (stream-attachments activity-attachments)
            [:div.fullscreen-post-box-footer.group
              {:class (when (and (pos? (count comments-data))
                                 (> (count (:reactions activity-data)) 2))
                        "wrap-reactions")}
              (comments-summary activity-data)
              (reactions activity-data)]]]
        ;; Right column
        (when (:has-comments activity-data)
          [:div.fullscreen-post-right-column.group
            {:class (utils/class-set {:add-comment-focused (:add-comment-focus modal-data)
                                      :no-comments (zero? (count comments-data))})}
            (when (:can-comment activity-data)
              (add-comment activity-data))
            (stream-comments activity-data comments-data)])]]))