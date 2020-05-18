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
            [oc.web.utils.comment :as comment-utils]
            [oc.web.mixins.mention :as mention-mixins]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.loading :refer (loading)]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.poll :refer (polls-wrapper)]
            [oc.web.components.ui.ziggeo :refer (ziggeo-player)]
            [oc.web.components.ui.org-avatar :refer (org-avatar)]
            [oc.web.components.ui.add-comment :refer (add-comment)]
            [oc.web.components.ui.alert-modal :refer (alert-modal)]
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

(defn- maybe-load-comments [s]
  (let [activity-data (:activity-data @(drv/get-ref s :secure-activity-data))
        comments-data @(drv/get-ref s :comments-data)]
    (comment-utils/get-comments-if-needed activity-data comments-data)))

(rum/defcs secure-activity < rum/reactive
                             ;; Derivatives
                             (drv/drv :secure-activity-data)
                             (drv/drv :id-token)
                             (drv/drv :comments-data)
                             ;; Locals
                             (rum/local 0 ::mobile-video-height)
                             ;; Mixins
                             (mention-mixins/oc-mentions-hover)
                             ui-mixins/refresh-tooltips-mixin
                             {:did-mount (fn [s]
                               (maybe-load-comments s)
                               s)
                              :will-update (fn [s]
                               (maybe-load-comments s)
                               s)
                              :after-render (fn [s]
                               ;; Delay to make sure the change socket was initialized
                               (utils/after 2000 #(activity-actions/send-secure-item-seen-read))
                               s)}
  [s]
  (let [{:keys [activity-data is-showing-alert]} (drv/react s :secure-activity-data)
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
        activity-link (utils/link-for (:links org-data) "entries")]
    [:div.secure-activity-container
      (login-overlays-handler)
      (when is-showing-alert
        (alert-modal))
      (when activity-data
        [:div.activity-header.group
          (org-avatar org-data activity-link (if is-mobile? :never :always))
          (if id-token
            [:div.activity-header-right
              [:button.mlb-reset.login-as-bt
                {:on-click #(user-actions/show-login :login-with-email)
                 :data-toggle (when-not is-mobile? "tooltip")
                 :data-placement "bottom"
                 :data-container "body"
                 :title "Log in to view all posts"}
                [:span.login-as
                  "Log in as " (:first-name id-token)]
                (user-avatar-image id-token)]]
            [:div.activity-header-right
              [:button.mlb-reset.learn-more-bt
                {:on-click #(router/redirect! oc-urls/home)}
                (str "learn more"
                 (when-not is-mobile?
                   " about Wut"))]
              [:span.or " or "]
              [:button.mlb-reset.login-bt
                {:on-click #(user-actions/show-login :login-with-email)}
                "Login"]])])
      (when activity-data
        [:div.activity-content-outer
          [:div.activity-content
            (when (:headline activity-data)
              [:div.activity-title
                {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))
                 :class utils/hide-class}])
            [:div.activity-content-author
              (user-avatar-image (:publisher activity-data))
              [:div.name
                {:class utils/hide-class}
                (str (:name (:publisher activity-data)) " in "
                 (:board-name activity-data) " on ")
                 [:time
                   {:date-time (:published-at activity-data)
                    :data-toggle (when-not is-mobile? "tooltip")
                    :data-placement "top"
                    :data-container "body"
                    :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
                    :data-title (utils/activity-date-tooltip activity-data)}
                   (utils/date-string (utils/js-date (:published-at activity-data)) [:year])]]]
            (when video-id
              (ziggeo-player {:video-id video-id
                              :width (:width video-size)
                              :height (:height video-size)
                              :video-processed (:video-processed activity-data)}))
            (when (:abstract activity-data)
              [:div.activity-abstract
                {:class utils/hide-class
                 :dangerouslySetInnerHTML {:__html (:abstract activity-data)}}])
            (when (:body activity-data)
              [:div.activity-body.oc-mentions.oc-mentions-hover
                {:dangerouslySetInnerHTML (utils/emojify (:body activity-data))
                 :class utils/hide-class}])
            (when (seq (:polls activity-data))
              (polls-wrapper {:polls-data (:polls activity-data)
                              :container-selector "div.secure-activity-container"}))
            (stream-attachments (:attachments activity-data))
            [:div.activity-content-footer.group
              (comments-summary {:activity-data activity-data :comments-data comments-drv})
              (reactions {:entity-data activity-data})]
            (when (or (pos? (count comments-data))
                      (:can-comment activity-data))
              [:div.comments-separator])
            (when (:can-comment activity-data)
              (rum/with-key (add-comment {:activity-data activity-data}) (str "add-comment-" (:uuid activity-data))))
            (when comments-data
              (stream-comments {:activity-data activity-data
                                :comments-data comments-data
                                :current-user-id (:user-id id-token)}))]])
      [:div.secure-activity-footer
        (if id-token
          [:button.mlb-reset.secure-activity-footer-bt
            {:on-click #(user-actions/show-login :login-with-email)}
            "Log in required to access all posts"]
          [:a.sent-via-carrot
            {:href oc-urls/home}
            [:div.sent-via-carrot-copy
              "Sent with Wut"]])]
      (when-not activity-data
        [:div.secure-activity-container
          (loading {:loading true})])]))