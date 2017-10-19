(ns oc.web.components.activity-card
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [cuerdas.core :as s]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.cookies :as cook]
            [oc.web.lib.oc-colors :refer (get-color-by-kw)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.activity-move :refer (activity-move)]
            [oc.web.components.ui.interactions-summary :refer (interactions-summary)]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(rum/defc activity-card-empty
  [topic read-only?]
  [:div.activity-card.empty-state.group
    (when-not read-only?
      [:div.empty-state-content
        [:img {:src (utils/cdn "/img/ML/entry_empty_state.svg")}]
        [:div.activity-card-title
          "This topic’s a little sparse. "
          [:button.mlb-reset
            {:on-click #(dis/dispatch! [:entry-edit topic])}
            "Add an update?"]]])])

(defn- delete-clicked [e activity-data]
  (utils/event-stop e)
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :action "delete-entry"
                    :message "Delete this post?"
                    :link-button-title "No"
                    :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                    :solid-button-title "Yes"
                    :solid-button-cb #(do
                                        (dis/dispatch! [:activity-delete activity-data])
                                        (dis/dispatch! [:alert-modal-hide]))
                    }]
    (dis/dispatch! [:alert-modal-show alert-data])))

(defn- truncate-body [body-sel is-all-posts]
  (.dotdotdot (js/$ body-sel)
   #js {:height (* 24 (if is-all-posts 6 3))
        :wrap "word"
        :watch true
        :ellipsis "... "}))

(defn- get-first-body-thumbnail [body is-ap]
  (let [$body (js/$ (str "<div>" body "</div>"))
        thumb-els (js->clj (js/$ "img:not(.emojione), iframe" $body))
        found (atom nil)]
    (dotimes [el-num (.-length thumb-els)]
      (let [el (aget thumb-els el-num)
            $el (js/$ el)]
        (when-not @found
          (if (= (s/lower (.-tagName el)) "img")
            (let [width (.attr $el "width")
                  height (.attr $el "height")]
              (when (and (not @found)
                         (or (<= width (* height 2))
                             (<= height (* width 2))))
                (reset! found
                  {:type "image"
                   :thumbnail (if (and (not is-ap) (.data $el "thumbnail"))
                                (.data $el "thumbnail")
                                (.attr $el "src"))})))
            (reset! found {:type (.data $el "media-type") :thumbnail (.data $el "thumbnail")})))))
    @found))

(rum/defcs activity-card < rum/reactive
                        (rum/local false ::hovering-card)
                        (rum/local false ::showing-dropdown)
                        (rum/local false ::truncated)
                        (rum/local nil ::first-body-image)
                        (rum/local false ::move-activity)
                        (rum/local nil ::window-click)
                        (drv/drv :org-data)
                        {:after-render (fn [s]
                                         (let [activity-data (first (:rum/args s))
                                               body-sel (str "div.activity-card-" (:uuid activity-data) " div.activity-card-body")
                                               body-a-sel (str body-sel " a")
                                               is-all-posts (nth (:rum/args s) 4 false)]
                                           ; Prevent body links in FoC
                                           (.click (js/$ body-a-sel) #(.stopPropagation %))
                                           ; Truncate body text with dotdotdot
                                           (when (compare-and-set! (::truncated s) false true)
                                             (truncate-body body-sel is-all-posts)
                                             (utils/after 10 #(do
                                                                (.trigger (js/$ body-sel) "destroy")
                                                                (truncate-body body-sel is-all-posts)))))
                                         s)
                         :will-mount (fn [s]
                                       (let [activity-data (first (:rum/args s))
                                             is-all-posts (nth (:rum/args s) 4 false)]
                                         (reset! (::first-body-image s) (get-first-body-thumbnail (:body activity-data) is-all-posts)))
                                       s)
                         :did-remount (fn [o s]
                                        (let [old-activity-data (first (:rum/args o))
                                              new-activity-data (first (:rum/args s))
                                              is-all-posts (nth (:rum/args s) 4 false)]
                                          (when (not= (:body old-activity-data) (:body new-activity-data))
                                            (reset! (::first-body-image s) (get-first-body-thumbnail (:body new-activity-data) is-all-posts))
                                            (.trigger (js/$ (str "div.activity-card-" (:uuid old-activity-data) " div.activity-card-body")) "destroy")
                                            (reset! (::truncated s) false)))
                                        s)
                         :did-mount (fn [s]
                                      (reset! (::window-click s)
                                       (events/listen js/window EventType/CLICK #(when @(::showing-dropdown s)
                                                                                   (reset! (::showing-dropdown s) false))))
                                      s)
                         :will-unmount (fn [s]
                                         (events/unlistenByKey @(::window-click s))
                                         s)}
  [s activity-data has-headline has-body is-new is-all-posts]
  [:div.activity-card
    {:class (utils/class-set {(str "activity-card-" (:uuid activity-data)) true
                              :all-posts-card is-all-posts})
     :on-click #(dis/dispatch! [:activity-modal-fade-in (:board-slug activity-data) (:uuid activity-data) (:type activity-data)])
     :on-mouse-enter #(when-not (:read-only activity-data) (reset! (::hovering-card s) true))
     :on-mouse-leave #(when-not (:read-only activity-data) (reset! (::hovering-card s) false))}
    ; Card header
    [:div.activity-card-head.group
      {:class "entry-card"}
      ; Card author
      [:div.activity-card-head-author
        (user-avatar-image (first (:author activity-data)))
        [:div.name (:name (first (:author activity-data)))]
        [:div.time-since
          (let [t (or (:published-at activity-data) (:created-at activity-data))]
            [:time
              {:date-time t
               :data-toggle "tooltip"
               :data-placement "top"
               :data-delay "{\"show\":\"1000\", \"hide\":\"0\"}"
               :title (utils/activity-date-tooltip activity-data)}
              (utils/time-since t)])]]
      ; Card labels
      [:div.activity-card-head-right
        (when (or (utils/link-for (:links activity-data) "partial-update")
                  (utils/link-for (:links activity-data) "delete"))
          (let [all-boards (filter #(not= (:slug %) "drafts") (:boards (drv/react s :org-data)))
                same-type-boards (filter #(= (:type %) (:type activity-data)) all-boards)]
            [:div.more-button
              [:button.mlb-reset.more-ellipsis
                {:type "button"
                 :class (utils/class-set {:not-hover (and (not @(::move-activity s))
                                                          (not @(::hovering-card s))
                                                          (not @(::showing-dropdown s)))})
                 :on-click (fn [e]
                             (utils/event-stop e)
                             (utils/remove-tooltips)
                             (reset! (::showing-dropdown s) (not @(::showing-dropdown s)))
                             (reset! (::move-activity s) false))
                 :title "More"
                 :data-toggle "tooltip"
                 :data-placement "top"
                 :data-container "body"}]
              (when @(::showing-dropdown s)
                [:div.activity-more-dropdown-menu
                  [:div.triangle]
                  [:ul.activity-card-more-menu
                    (when (utils/link-for (:links activity-data) "share")
                      [:li
                        {:on-click (fn [e]
                                     (utils/event-stop e)
                                     (reset! (::showing-dropdown s) false)
                                     (reset! (::hovering-card s) false)
                                     ; open the activity-share-modal component
                                     (dis/dispatch! [:activity-share-show :link activity-data]))}
                        "Share Link"])
                    (when (utils/link-for (:links activity-data) "share")
                      [:li
                        {:on-click (fn [e]
                                     (utils/event-stop e)
                                     (reset! (::showing-dropdown s) false)
                                     (reset! (::hovering-card s) false)
                                     ; open the activity-share-modal component
                                     (dis/dispatch! [:activity-share-show :email activity-data]))}
                        "Share Email"])
                    (when (and (utils/link-for (:links activity-data) "share")
                               (jwt/team-has-bot? (:team-id (dis/org-data))))
                      [:li
                        {:on-click (fn [e]
                                     (utils/event-stop e)
                                     (reset! (::showing-dropdown s) false)
                                     (reset! (::hovering-card s) false)
                                     ; open the activity-share-modal component
                                     (dis/dispatch! [:activity-share-show :slack activity-data]))}
                        "Share Slack"])
                    (when (and (utils/link-for (:links activity-data) "partial-update")
                               (> (count same-type-boards) 1))
                      [:li
                        {:on-click #(do
                                      (utils/event-stop %)
                                      (reset! (::showing-dropdown s) false)
                                      (reset! (::move-activity s) true))}
                        "Move"])
                    (when (utils/link-for (:links activity-data) "delete")
                      [:li
                        {:on-click #(do
                                      (utils/event-stop %)
                                      (reset! (::showing-dropdown s) false)
                                      (reset! (::hovering-card s) false)
                                      (delete-clicked % activity-data))}
                        "Delete"])]])
              (when @(::move-activity s)
                (activity-move {:activity-data activity-data :boards-list same-type-boards :dismiss-cb #(reset! (::move-activity s) false)}))]))
        ; Topic tag button
        (when (:topic-slug activity-data)
          (let [topic-name (or (:topic-name activity-data) (s/upper (:topic-slug activity-data)))]
            [:div.activity-tag
              {:class (when is-all-posts "double-tag")
               :on-click #(do
                            (utils/event-stop %)
                            (router/nav! (oc-urls/board-filter-by-topic (router/current-org-slug) (:board-slug activity-data) (:topic-slug activity-data))))}
              topic-name]))
        (when is-all-posts
          [:div.activity-tag
            {:class (utils/class-set {:board-tag true
                                      :double-tag (:topic-slug activity-data)})
             :on-click #(do
                          (utils/event-stop %)
                          (router/nav!
                            (if (= (keyword (cook/get-cookie (router/last-board-filter-cookie (router/current-org-slug) (:board-slug activity-data)))) :by-topic)
                                (oc-urls/board-sort-by-topic (:board-slug activity-data))
                                (oc-urls/board (:board-slug activity-data)))))}
            (:board-name activity-data)])
              ;; TODO This will be replaced w/ new Ryan new design, be sure to clean up CSS too when this changes
              ;;(when is-new [:div.new-tag "New"])
              ]]
    [:div.activity-card-content.group
      ; Headline
      [:div.activity-card-headline
        {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))
         :class (when has-headline "has-headline")}]
      ; Body
      (let [body-without-preview (utils/body-without-preview (:body activity-data))
            activity-url (oc-urls/entry (:board-slug activity-data) (:uuid activity-data))
            emojied-body (utils/emojify body-without-preview)]
        [:div.activity-card-body
          {:dangerouslySetInnerHTML emojied-body
           :class (utils/class-set {:has-body has-body
                                    :has-headline has-headline
                                    :has-media-preview @(::first-body-image s)})}])
      ; Body preview
      (when @(::first-body-image s)
        [:div.activity-card-media-preview-container
          {:class (or (:type @(::first-body-image s)) "image")}
          [:img
            {:src (:thumbnail @(::first-body-image s))}]])]
    [:div.activity-card-footer.group
      (interactions-summary activity-data)
      (when (utils/link-for (:links activity-data) "partial-update")
        [:button.mlb-reset.post-edit
          {:title "Edit"
           :data-toggle "tooltip"
           :data-placement "top"
           :data-container "body"
           :class (utils/class-set {:not-hover (and (not @(::move-activity s))
                                                    (not @(::hovering-card s))
                                                    (not @(::showing-dropdown s)))})
           :on-click (fn [e]
                       (utils/event-stop e)
                       (utils/remove-tooltips)
                       (reset! (::hovering-card s) false)
                       (reset! (::showing-dropdown s) false)
                       (dis/dispatch! [:entry-edit activity-data]))}])]])