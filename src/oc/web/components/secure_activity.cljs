(ns oc.web.components.secure-activity
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.utils :as utils]
            [oc.web.router :as router]
            [oc.web.utils.activity :as au]
            [oc.web.local-settings :as ls]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]
            [oc.web.mixins.mention :as mention-mixins]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.ziggeo :refer (ziggeo-player)]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.components.ui.user-avatar :refer (user-avatar)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.stream-comments :refer (stream-comments)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.comments-summary :refer (comments-summary)]
            [oc.web.components.ui.login-overlay :refer (login-overlays-handler)]
            [oc.web.components.ui.stream-attachments :refer (stream-attachments)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn win-width []
  (or (.-clientWidth (.-documentElement js/document))
      (.-innerWidth js/window)))

(rum/defcs secure-activity < rum/reactive
                             ;; Derivatives
                             (drv/drv :secure-activity-data)
                             (drv/drv :id-token)
                             (drv/drv :comments-data)
                             ;; Locals
                             (rum/local 0 ::mobile-video-height)
                             ;; Mixins
                             (mention-mixins/oc-mentions-hover)
                             {:did-mount (fn [s]
                               (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
                               s)
                              :after-render (fn [s]
                               ;; Delay to make sure the change socket was initialized
                               (utils/after 2000 #(activity-actions/send-secure-item-seen-read))
                               s)}
  [s]
  (let [activity-data (drv/react s :secure-activity-data)
        activity-author (:publisher activity-data)
        is-mobile? (responsive/is-tablet-or-mobile?)
        video-size (when (:fixed-video-id activity-data)
                    (if is-mobile?
                      {:width (win-width)
                       :height @(::mobile-video-height s)}
                      {:width 640
                       :height (utils/calc-video-height 640)}))
        video-id (:fixed-video-id activity-data)
        id-token (drv/react s :id-token)
        org-data (-> activity-data
                  (select-keys [:org-slug :org-name :org-logo-url :org-logo-width :org-logo-height])
                  (clojure.set/rename-keys {:org-slug :slug
                                            :org-name :name
                                            :org-logo-url :logo-url
                                            :org-logo-width :logo-width
                                            :org-logo-height :logo-height}))
        comments-drv (drv/react s :comments-data)
        comments-data (au/get-comments activity-data comments-drv)
        activity-link (utils/link-for (:links org-data) "activity")]
    [:div.secure-activity-container
      (login-overlays-handler)
      (when activity-data
        [:div.activity-header.group
          (org-avatar org-data activity-link (if is-mobile? :never :always) (not is-mobile?))
          (if id-token
            [:div.activity-header-right
              [:button.mlb-reset.login-as-bt
                {:on-click #(user-actions/show-login :login-with-email)
                 :data-toggle (when-not is-mobile? "tooltip")
                 :data-placement "bottom"
                 :data-container "body"
                 :title "Log in to view all posts"}
                [:span
                  [:span.login-as
                    "Login as " (:first-name id-token)]
                  (user-avatar-image id-token)]]]
            [:div.activity-header-right
              [:button.mlb-reset.learn-more-bt
                {:on-click #(router/nav! oc-urls/home)}
                "Learn more"]
              [:button.mlb-reset.login-bt
                {:on-click #(user-actions/show-login :login-with-email)}
                "Login"]])])
      (when activity-data
        [:div.activity-content-outer
          [:div.activity-content-header
            [:div.activity-content-header-author
              (user-avatar-image (:publisher activity-data))
              [:div.name
                {:class utils/hide-class}
                (:name (:publisher activity-data))]
              [:div.time-since
                (let [t (or (:published-at activity-data) (:created-at activity-data))]
                  [:time
                    {:date-time t
                     :data-toggle (when-not is-mobile? "tooltip")
                     :data-placement "top"
                     :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
                     :data-title (utils/activity-date-tooltip activity-data)}
                    (utils/time-since t)])]]]
          [:div.activity-content
            (when (:headline activity-data)
              [:div.activity-title
                {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))
                 :class utils/hide-class}])
            (when video-id
              (ziggeo-player {:video-id video-id
                              :width (:width video-size)
                              :height (:height video-size)
                              :video-processed (:video-processed activity-data)}))
            (when (:body activity-data)
              [:div.activity-body.oc-mentions.oc-mentions-hover
                {:dangerouslySetInnerHTML (utils/emojify (:body activity-data))
                 :class utils/hide-class}])
            (when (and ls/oc-enable-transcriptions
                       (:video-transcript activity-data)
                       (:video-processed activity-data))
              [:div.activity-video-transcript
                [:div.activity-video-transcript-header
                  "This transcript was automatically generated and may not be accurate"]
                [:div.activity-video-transcript-content
                  (:video-transcript activity-data)]])
            (stream-attachments (:attachments activity-data))
            [:div.secure-activity-comments-summary
              (comments-summary activity-data true)]
            (when-not is-mobile?
                (reactions activity-data))
            (when comments-data
              (stream-comments activity-data comments-data true))
            (when (:can-comment activity-data)
              (rum/with-key (add-comment activity-data) (str "add-comment-" (:uuid activity-data))))]])
      (when-not activity-data
        [:div.secure-activity-container
          (loading {:loading true})])
      [:div.secure-activity-footer
        [:button.mlb-reset.secure-activity-footer-bt
          {:on-click #(if id-token
                        (user-actions/show-login :login-with-email)
                        (router/nav! oc-urls/home))}
          [:span
            (if id-token
              "Log in to view all posts"
              "Learn more about Carrot")
            [:div.right-arrow]]]]]))