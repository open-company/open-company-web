(ns oc.web.components.activity-card
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.oc-colors :refer (get-color-by-kw)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.reactions :refer (reactions)]
            [oc.web.components.ui.interactions-summary :refer (interactions-summary)]
            [goog.object :as gobj]))

(rum/defc activity-card-empty
  [read-only?]
  [:div.activity-card.empty-state.group
    (when-not read-only?
      [:div.empty-state-content
        [:img {:src (utils/cdn "/img/ML/entry_empty_state.svg")}]
        [:div.activity-card-title
          "This topic’s a little sparse. "
          [:button.mlb-reset
            {:on-click #(dis/dispatch! [:entry-edit {}])}
            "Add an update?"]]])])

(defn delete-clicked [e activity-data]
  (utils/event-stop e)
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :message (str "Delete this " (if (= (:type activity-data) "story") "journal" "update") "?")
                    :link-button-title "No"
                    :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                    :solid-button-title "Yes"
                    :solid-button-cb #(do
                                        (dis/dispatch! [:activity-delete activity-data])
                                        (dis/dispatch! [:alert-modal-hide]))
                    }]
    (dis/dispatch! [:alert-modal-show alert-data])))

(defn truncate-body [body-sel is-all-activity]
  (.dotdotdot (js/$ body-sel)
   #js {:height (* 24 (if is-all-activity 4 3))
        :wrap "word"
        :watch true
        :ellipsis "... "
        :after (when-not is-all-activity "a.read-more")}))

(defn get-first-body-thumbnail [body]
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
                   :thumbnail (if (.data $el "thumbnail")
                                (.data $el "thumbnail")
                                (.attr $el "src"))})))
            (reset! found {:type (.data $el "media-type") :thumbnail (.data $el "thumbnail")})))))
    @found))

(rum/defcs activity-card < rum/static
                        (rum/local false ::hovering-card)
                        (rum/local false ::showing-dropdown)
                        (rum/local false ::truncated)
                        (rum/local nil ::first-body-image)
                        {:after-render (fn [s]
                                         (let [activity-data (first (:rum/args s))
                                               body-sel (str "div.activity-card-" (:uuid activity-data) " div.activity-card-body")
                                               body-a-sel (str body-sel " a")
                                               read-more-sel (str body-a-sel ".read-more")
                                               is-all-activity (get (:rum/args s) 3)]
                                           ; Prevent body links in FoC
                                           (.click (js/$ body-a-sel) #(.preventDefault %))
                                           ; Prevent read more link to change directly the url
                                           (.click (js/$ read-more-sel) #(.preventDefault %))
                                           ; Truncate body text with dotdotdot
                                           (when (compare-and-set! (::truncated s) false true)
                                             (truncate-body body-sel is-all-activity)
                                             (utils/after 10 #(do
                                                                (.trigger (js/$ body-sel) "destroy")
                                                                (truncate-body body-sel is-all-activity)))))
                                         s)
                         :will-mount (fn [s]
                                       (let [activity-data (first (:rum/args s))]
                                         (when (= (:type activity-data) "entry")
                                          (reset! (::first-body-image s) (get-first-body-thumbnail (:body activity-data)))))
                                       s)
                         :did-remount (fn [o s]
                                        (let [old-activity-data (first (:rum/args o))
                                              new-activity-data (first (:rum/args s))]
                                          (when (not= (:body old-activity-data) (:body new-activity-data))
                                            (when (= (:type new-activity-data) "entry")
                                              (reset! (::first-body-image s) (get-first-body-thumbnail (:body new-activity-data))))
                                            (.trigger (js/$ (str "div.activity-card-" (:uuid old-activity-data) " div.activity-card-body")) "destroy")
                                            (reset! (::truncated s) false)))
                                        s)
                         :did-mount (fn [s]
                                      (let [activity-data (first (:rum/args s))]
                                        (.on (js/$ (str "div.activity-card-" (:uuid activity-data)))
                                         "show.bs.dropdown"
                                         (fn [e]
                                           (reset! (::showing-dropdown s) true)))
                                        (.on (js/$ (str "div.activity-card-" (:uuid activity-data)))
                                         "hidden.bs.dropdown"
                                         (fn [e]
                                           (reset! (::showing-dropdown s) false))))
                                      s)}
  [s activity-data has-headline has-body is-all-activity]
  [:div.activity-card
    {:class (utils/class-set {(str "activity-card-" (:uuid activity-data)) true
                              :all-activity-card is-all-activity
                              :story-card (= (:type activity-data) "story")})
     :on-click #(if (= (:type activity-data) "story")
                  (router/nav! (oc-urls/story (:board-slug activity-data) (:uuid activity-data)))
                  (dis/dispatch! [:activity-modal-fade-in (:board-slug activity-data) (:uuid activity-data) (:type activity-data)]))
     :on-mouse-enter #(when-not (:read-only activity-data) (reset! (::hovering-card s) true))
     :on-mouse-leave #(when-not (:read-only activity-data) (reset! (::hovering-card s) false))}
    (when (and (not is-all-activity)
               (= (:type activity-data) "story"))
      [:div.triangle])
    (when is-all-activity
      [:div.activity-card-breadcrumb
        "In " [:span.bold (if (= (:type activity-data) "entry") (:board-name activity-data) (:storyboard-name activity-data))]
        (when (:topic-slug activity-data)
          " → ")
        (when (:topic-slug activity-data)
          [:span.bold (:topic-name activity-data)])])
    ; Card header
    (when (or is-all-activity
              (= (:type activity-data) "entry"))
      [:div.activity-card-head.group
        ; Card author
        [:div.activity-card-head-author
          (user-avatar-image (first (:author activity-data)))
          [:div.name (:name (first (:author activity-data)))]
          [:div.time-since
            (let [t (if (= (:type activity-data) "story") (:published-at activity-data) (:created-at activity-data))]
              [:time
                {:date-time t
                 :data-toggle "tooltip"
                 :data-placement "top"
                 :title (utils/activity-date-tooltip activity-data)}
                (utils/time-since t)])]]
        ; Card labels
        [:div.activity-card-head-right
          ; Topic tag button
          (when (and (not is-all-activity)
                     (:topic-slug activity-data))
            (let [topic-name (or (:topic-name activity-data) (s/upper (:topic-slug activity-data)))]
              [:div.activity-tag
                {:on-click #(do
                              (utils/event-stop %)
                              (router/nav! (oc-urls/board-filter-by-topic (router/current-org-slug) (:board-slug activity-data) (:topic-slug activity-data))))}
                topic-name]))]])
    [:div.activity-card-content.group
      (when (= (:type activity-data) "story")
        [:div.activity-card-title
          {:dangerouslySetInnerHTML (utils/emojify (:title activity-data))}])
      ; Headline
      (when (= (:type activity-data) "entry")
        [:div.activity-card-headline
          {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))
           :class (when has-headline "has-headline")}])
      ; Body
      (let [body-without-preview (utils/body-without-preview (:body activity-data))
            activity-url (if (= (:type activity-data) "story")
                           (oc-urls/story (:board-slug activity-data) (:uuid activity-data))
                           (oc-urls/entry (:board-slug activity-data) (:uuid activity-data)))
            read-more-html (str "<a class=\"read-more\" href=\"" activity-url "\">Read more</a>")
            emojied-body (utils/emojify (str body-without-preview (if is-all-activity "" read-more-html)))]
        [:div.activity-card-body
          {:dangerouslySetInnerHTML emojied-body
           :class (utils/class-set {:has-body has-body
                                    :has-headline has-headline
                                    :has-media-preview @(::first-body-image s)})}])
      (when (and is-all-activity
                 has-body)
        [:div.read-more "Read Full Update"])
      ; Body preview
      (when @(::first-body-image s)
        [:div.activity-card-media-preview
          {:style #js {:backgroundImage (str "url(" (:thumbnail @(::first-body-image s)) ")")}
           :class (or (:type @(::first-body-image s)) "image")}])
      (when (and (= (:type activity-data) "story")
                 (not (empty? (:banner-url activity-data))))
        [:div.story-banner
          {:style #js {:backgroundImage (str "url(\"" (:banner-url activity-data) "\")")
                       :height (str (* (/ (:banner-height activity-data) (:banner-width activity-data)) 619) "px")}}])]
    [:div.activity-card-footer.group
      (interactions-summary activity-data)
      (when-not (:read-only activity-data)
        [:div.more-button.dropdown
          [:button.mlb-reset.more-ellipsis.dropdown-toggle
            {:type "button"
             :class (utils/class-set {:hidden (and (not @(::hovering-card s)) (not @(::showing-dropdown s)))})
             :id (str "activity-card-more-" (:board-slug activity-data) "-" (:uuid activity-data))
             :on-click #(utils/event-stop %)
             :title "More"
             :data-toggle "dropdown"
             :aria-haspopup true
             :aria-expanded false}]
          [:div.dropdown-menu
            {:aria-labelledby (str "activity-card-more-" (:board-slug activity-data) "-" (:uuid activity-data))}
            [:div.triangle]
            [:ul.activity-card-more-menu
              [:li
                {:on-click (fn [e]
                             (utils/event-stop e)
                             (if (= (:type activity-data) "story")
                               (router/nav! (oc-urls/story-edit (:board-slug activity-data) (:uuid activity-data)))
                               (dis/dispatch! [:entry-edit activity-data])))}
                "Edit"]
              [:li
                {:on-click #(delete-clicked % activity-data)}
                "Delete"]]]])]])