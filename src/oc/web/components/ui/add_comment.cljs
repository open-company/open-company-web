(ns oc.web.components.ui.add-comment
  (:require [rum.core :as rum]
            [dommy.core :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.comment :as cu]
            [oc.web.utils.activity :as au]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.utils.mention :as mention-utils]
            [oc.web.mixins.mention :as mention-mixins]
            [oc.web.actions.comment :as comment-actions]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.utils.medium-editor-media :as me-media-utils]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.ui.giphy-picker :refer (giphy-picker)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.media-video-modal :refer (media-video-modal)]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)])
  (:import [goog.async Throttle]))

(defn- focus-value [s]
  (let [{:keys [activity-data parent-comment-uuid
         add-comment-focus-prefix edit-comment-data]} (-> s :rum/args first)]
    (cu/add-comment-focus-value add-comment-focus-prefix (:uuid activity-data) parent-comment-uuid (:uuid edit-comment-data))))

(defn- add-comment-field [s]
  (rum/ref-node s "editor-node"))

(defn- add-comment-body [s]
  (when-let [field (add-comment-field s)]
    (.-innerHTML field)))

(defn- add-comment-data
  ([s]
   (add-comment-data s @(::add-comment-key s)))
  ([s add-comment-key]
   (let [add-comment-data @(drv/get-ref s :add-comment-data)]
     (get add-comment-data add-comment-key))))

(defn- enable-post-button? [s]
  (let [{:keys [edit-comment-data]} (first (:rum/args s))
        activity-add-comment-data (add-comment-data s)
        comment-text (add-comment-body s)]
    (boolean (and (not= comment-text (:body edit-comment-data))
                  (not (au/empty-body? comment-text))))))

(defn- toggle-post-button [s]
  (let [enabled? (enable-post-button? s)]
    (compare-and-set! (::post-enabled s) (not enabled?) enabled?)
    enabled?))

;; Add commnet handling
(defn- maybe-expand [s]
  (when @(::collapsed s)
    (when (add-comment-field s)
      (compare-and-set! (::collapsed s) true false))))

(defn- multiple-lines? [s]
  (when (and (not @(::collapsed s))
             (not @(::multiple-lines s)))
    (when-let [f (add-comment-field s)]
      (reset! (::multiple-lines s) (or (> (.-scrollWidth f) @(::inline-reply-max-width s))
                                       (> (.-scrollHeight f) 18))))))

(defn- focus [s]
  (maybe-expand s)
  (multiple-lines? s)
  (toggle-post-button s))

(defn- blur [s]
  (comment-actions/add-comment-blur (focus-value s))
  (let [toggle (toggle-post-button s)]
    ;; In case post button is being disabled let's collapse
    (when (and (not toggle)
               (-> s :rum/args first :collapse?))
      (reset! (::collapsed s) true))))

(defn- send-clicked [event s]
  (reset! (::collapsed s) true)
  (let [add-comment-div (add-comment-field s)
        comment-body (cu/add-comment-content add-comment-div)
        {:keys [activity-data parent-comment-uuid dismiss-reply-cb
         edit-comment-data scroll-after-posting? add-comment-cb]} (first (:rum/args s))
        save-done-cb (fn [success]
                      (if success
                        (when-let [el (add-comment-field s)]
                          (set! (.-innerHTML el) @(::initial-add-comment s)))
                        (notification-actions/show-notification
                         {:title "An error occurred while saving your comment."
                          :description "Please try again"
                          :dismiss true
                          :expire 3
                          :id (if edit-comment-data :update-comment-error :add-comment-error)})))]
    (swap! (::initial-add-comment s) #(if edit-comment-data % au/empty-body-html))
    (when add-comment-div
      (set! (.-innerHTML add-comment-div) @(::initial-add-comment s)))
    (let [updated-comment (if edit-comment-data
                            (comment-actions/save-comment activity-data edit-comment-data comment-body save-done-cb)
                            (comment-actions/add-comment activity-data comment-body parent-comment-uuid save-done-cb))]
      (when (and (not (responsive/is-mobile-size?))
                 (not edit-comment-data)
                 (not dismiss-reply-cb)
                 scroll-after-posting?
                 (not (dom-utils/is-element-top-in-viewport? (sel1 [:div.stream-comments]) -40)))
        (when-let [vertical-offset (-> s (rum/dom-node) (.-offsetTop) (- 72))]
          (utils/after 10
           #(.scrollTo js/window 0 vertical-offset))))
      (when (fn? dismiss-reply-cb)
        (dismiss-reply-cb false))
      (when (fn? add-comment-cb)
        (add-comment-cb updated-comment)))))

(defn- add-comment-unique-class [s]
  (str "add-comment-box-container-" @(::add-comment-id s)))

(defn- me-options [s parent-uuid placeholder]
  {:media-config ["gif"]
   :comment-parent-uuid parent-uuid
   :placeholder (or placeholder (if parent-uuid "Reply…" "Add a comment…"))
   :use-inline-media-picker true
   :static-positioned-media-picker true
   :media-picker-initially-visible false
   :media-picker-container-selector (str "div." (add-comment-unique-class s) " div.add-comment-box div.add-comment-internal div.add-comment-footer-media-picker")})

(defn- did-change [s]
  (let [post-enabled (enable-post-button? s)
        {:keys [activity-data parent-comment-uuid edit-comment-data]} (-> s :rum/args first)]
    (multiple-lines? s)
    (comment-actions/add-comment-change activity-data parent-comment-uuid (:uuid edit-comment-data) (add-comment-body s))
    (compare-and-set! (::post-enabled s) (not post-enabled) post-enabled)
    (when-let [throttled-did-change @(::did-change-throttled s)]
      (.fire throttled-did-change))))

(defn- should-focus? [s]
  (let [add-comment-focus @(drv/get-ref s :add-comment-focus)
        component-focus-id (focus-value s)]
    (= add-comment-focus component-focus-id)))

(defn- maybe-focus [s]
  (when (should-focus? s)
    (when-let [field (add-comment-field s)]
      (.focus field)
      (utils/after 0 #(utils/to-end-of-content-editable field)))))

(defn- close-reply-clicked [s]
  (let [{:keys [activity-data parent-comment-uuid add-comment-focus-prefix
         dismiss-reply-cb edit-comment-data]} (-> s :rum/args first)
        dismiss-fn (fn []
                    (comment-actions/add-comment-reset add-comment-focus-prefix (:uuid activity-data) parent-comment-uuid (:uuid edit-comment-data))
                    (when-let [el (add-comment-field s)]
                      (set! (.-innerHTML el)
                       (or (-> s :rum/args first :edit-comment-data :body) au/empty-body-html)))
                    (reset! (::collapsed s) true)
                    (when (fn? dismiss-reply-cb)
                      (dismiss-reply-cb true)))]
    (if @(::post-enabled s)
      (let [alert-data {:icon "/img/ML/trash.svg"
                        :action "cancel-comment-edit"
                        :message "Are you sure you want to cancel? Your comment will be lost."
                        :link-button-title "Keep"
                        :link-button-cb #(alert-modal/hide-alert)
                        :solid-button-style :red
                        :solid-button-title "Yes"
                        :solid-button-cb (fn []
                                          (dismiss-fn)
                                          (alert-modal/hide-alert))}]
        (alert-modal/show-alert alert-data))
      (dismiss-fn))))

(rum/defcs add-comment < rum/static
                         rum/reactive
                         ;; Locals
                         (rum/local nil ::me-media-utils/editor)
                         (rum/local nil ::me-media-utils/media-picker-ext)
                         (rum/local false ::me-media-utils/media-photo)
                         (rum/local false ::me-media-utils/media-video)
                         (rum/local false ::me-media-utils/media-attachment)
                         (rum/local false ::me-media-utils/media-photo-did-success)
                         (rum/local false ::me-media-utils/media-attachment-did-success)
                         (rum/local false ::me-media-utils/showing-media-video-modal)
                         (rum/local false ::me-media-utils/showing-gif-selector)
                         ;; Image upload lock
                         (rum/local false ::me-media-utils/upload-lock)
                         (rum/local "" ::add-comment-id)
                         (rum/local false ::comment-reply-to-reset)
                         ;; Derivatives
                         (drv/drv :media-input)
                         (drv/drv :add-comment-focus)
                         (drv/drv :add-comment-data)
                         (drv/drv :mention-users)
                         (drv/drv :attachment-uploading)
                         (drv/drv :users-info-hover)
                         (drv/drv :current-user-data)
                         (drv/drv :follow-publishers-list)
                         (drv/drv :followers-publishers-count)
                         (drv/drv :comment-reply-to)
                         ;; Locals
                         (rum/local nil ::add-comment-key)
                         (rum/local true ::collapsed)
                         (rum/local false ::multiple-lines)
                         (rum/local false ::post-enabled)
                         (rum/local au/empty-body-html ::initial-add-comment)
                         ; (rum/local false ::did-change)
                         (rum/local false ::last-add-comment-focus)
                         (rum/local 10000 ::inline-reply-max-width)
                         (rum/local nil ::did-change-throttled)
                         ;; Mixins
                         ui-mixins/first-render-mixin
                         (mention-mixins/oc-mentions-hover)

                         (ui-mixins/on-window-click-mixin (fn [s e]
                          (when (and @(::me-media-utils/showing-media-video-modal s)
                                     (not (.contains (.-classList (.-target e)) "media-video"))
                                     (not (utils/event-inside? e (rum/ref-node s :video-container))))
                            (me-media-utils/media-video-add s @(::me-media-utils/media-picker-ext s) nil)
                            (reset! (::me-media-utils/showing-media-video-modal s) false))
                          (when (and @(::me-media-utils/showing-gif-selector s)
                                     (not (.contains (.-classList (.-target e)) "media-gif"))
                                     (not (utils/event-inside? e (sel1 [:div.giphy-picker]))))
                            (me-media-utils/media-gif-add s @(::me-media-utils/media-picker-ext s) nil)
                            (reset! (::me-media-utils/showing-gif-selector s) false))))
                         {:will-mount (fn [s]
                          (reset! (::add-comment-id s) (utils/activity-uuid))
                          (let [{:keys [activity-data parent-comment-uuid edit-comment-data collapse? add-comment-did-change]} (first (:rum/args s))
                                add-comment-key (dis/add-comment-string-key (:uuid activity-data) parent-comment-uuid (:uuid edit-comment-data))
                                activity-add-comment-data (add-comment-data s add-comment-key)
                                initial-body (or activity-add-comment-data
                                                 (:body edit-comment-data)
                                                 au/empty-body-html)]
                            (reset! (::add-comment-key s) add-comment-key)
                            (reset! (::initial-add-comment s) initial-body)
                            (reset! (::collapsed s) (and collapse?
                                                         (au/empty-body? initial-body)))
                            (reset! (::post-enabled s) (boolean (and (seq activity-add-comment-data)
                                                                     (not= activity-add-comment-data (:body edit-comment-data))
                                                                     (not (au/empty-body? activity-add-comment-data)))))
                            (when (fn? add-comment-did-change)
                              (reset! (::did-change-throttled s) (Throttle. add-comment-did-change 2000))))
                          s)
                          :did-mount (fn [s]
                           (let [props (first (:rum/args s))
                                 me-opts (me-options s (:parent-comment-uuid props) (:add-comment-placeholder props))
                                 add-comment-internal (rum/ref-node s :add-comment-internal)
                                 bounding-box (.getBoundingClientRect add-comment-internal)
                                 computed-style (.getComputedStyle js/window add-comment-internal)
                                 max-width (- (:internal-max-width props) 132)]
                             (me-media-utils/setup-editor s did-change me-opts)
                             (reset! (::inline-reply-max-width s) max-width))
                           (maybe-focus s)
                           (multiple-lines? s)
                           (utils/after 2500 #(js/emojiAutocomplete))
                           s)
                          :will-update (fn [s]
                           (let [props (-> s :rum/args first)
                                 reply-to @(drv/get-ref s :comment-reply-to)
                                 focus-val (focus-value s)
                                 {:keys [focus body] :as r} (get reply-to focus-val)]
                             (when (string? body)
                               (let [body-field (add-comment-field s)
                                     current-body (.-innerHTML body-field)
                                     is-empty? (au/empty-body? current-body)
                                     quoted-body (str "<blockquote>" body "</blockquote>" au/empty-body-html)
                                     last-element (when-not is-empty?
                                                    (.-lastElementChild body-field))
                                     last-element-tag (when last-element
                                                        (.toLowerCase (.-nodeName last-element)))
                                     next-body (cond is-empty?
                                                     quoted-body
                                                     (and last-element
                                                          (or (= last-element-tag "blockquote")
                                                              (and (= last-element-tag "blockquote")
                                                                   (not (.-isContentEditable last-element)))))
                                                     (str current-body au/empty-body-html quoted-body)
                                                     :else
                                                     (str current-body quoted-body))]
                                 ;; If focus is required let's make sure the component force the focus
                                 ;; in did-update
                                 (when focus
                                   (reset! (::last-add-comment-focus s) nil))
                                 (set! (.-innerHTML body-field) next-body)
                                 (multiple-lines? s)
                                 (comment-actions/add-comment-change (:activity-data props) (:parent-comment-uuid props) (:uuid (:edit-comment-data props)) (add-comment-body s))
                                 (reset! (::collapsed s) false)
                                 (reset! (::post-enabled s) true)
                                 (comment-actions/reset-reply-to focus-val))))
                           (let [props (first (:rum/args s))]
                             (let [data @(drv/get-ref s :media-input)
                                   video-data (:media-video data)]
                                (when (and @(::me-media-utils/media-video s)
                                           (or (= video-data :dismiss)
                                               (map? video-data)))
                                  (when (or (= video-data :dismiss)
                                            (map? video-data))
                                    (reset! (::me-media-utils/media-video s) false)
                                    (dis/dispatch! [:update [:media-input] #(dissoc % :media-video)]))
                                  (if (map? video-data)
                                    (me-media-utils/media-video-add s @(::me-media-utils/media-picker-ext s) video-data)
                                    (me-media-utils/media-video-add s @(::me-media-utils/media-picker-ext s) nil)))))
                           s)
                          :did-update (fn [s]
                           (let [add-comment-focus @(drv/get-ref s :add-comment-focus)]
                             (when (not= @(::last-add-comment-focus s) add-comment-focus)
                               (maybe-focus s)
                               (reset! (::last-add-comment-focus s) add-comment-focus)))
                           s)
                          :did-remount (fn [o s]
                           (let [props (first (:rum/args s))
                                 me-opts (me-options s (:parent-comment-uuid props) (:add-comment-placeholder props))]
                             (me-media-utils/setup-editor s did-change me-opts))
                           s)
                          :will-unmount (fn [s]
                           (when @(::me-media-utils/editor s)
                             (.destroy @(::me-media-utils/editor s))
                             (reset! (::me-media-utils/editor s) nil))
                           (when-let [throttled-did-change @(::did-change-throttled s)]
                             (.dispose throttled-did-change))
                           s)}
  [s {:keys [activity-data parent-comment-uuid dismiss-reply-cb add-comment-focus-prefix internal-max-width
             edit-comment-data scroll-after-posting? add-comment-cb collapse? add-comment-did-change]}]
  (let [_add-comment-data (drv/react s :add-comment-data)
        _media-input (drv/react s :media-input)
        _mention-users (drv/react s :mention-users)
        _users-info-hover (drv/react s :users-info-hover)
        _current-user-data (drv/react s :current-user-data)
        _follow-publishers-list (drv/react s :follow-publishers-list)
        _followers-publishers-count (drv/react s :followers-publishers-count)
        _reply-to (drv/react s :comment-reply-to)
        current-user-data (drv/react s :current-user-data)
        container-class (add-comment-unique-class s)
        is-mobile? (responsive/is-mobile-size?)
        attachment-uploading (drv/react s :attachment-uploading)
        uploading? (and attachment-uploading
                        (= (:comment-parent-uuid attachment-uploading) parent-comment-uuid))
        add-comment-class (str "add-comment-" @(::add-comment-id s))]
    [:div.add-comment-box-container
      {:class (utils/class-set {container-class true
                                (str "add-comment-box-" add-comment-focus-prefix) true
                                :collapsed-box @(::collapsed s)
                                :inline-reply (not @(::multiple-lines s))})
       :on-click (when @(::collapsed s)
                   #(.focus (rum/ref-node s "editor-node")))}
      [:div.add-comment-box
        [:div.add-comment-internal
          {:ref :add-comment-internal}
          [:div.add-comment.emoji-autocomplete.emojiable.oc-mentions.oc-mentions-hover.editing
           {:ref "editor-node"
            :class (utils/class-set {add-comment-class true
                                     :medium-editor-placeholder-hidden (and collapse?
                                                                            (not @(::collapsed s)))
                                     :medium-editor-placeholder-relative true
                                     :medium-editor-element true
                                     utils/hide-class true})
            :on-focus #(focus s)
            :on-blur #(blur s)
            :on-key-down (fn [e]
                          (let [add-comment-node (rum/ref-node s "editor-node")]
                            (when (and (= (.-key e) "Escape")
                                       (= (.-activeElement js/document) add-comment-node))
                              (if edit-comment-data
                                (when (fn? dismiss-reply-cb)
                                  (dismiss-reply-cb true))
                                (.blur add-comment-node)))
                            (when (and (= (.-activeElement js/document) add-comment-node)
                                       (= (.-key e) "Enter"))
                              (if (.-metaKey e)
                                (send-clicked e s)
                                (compare-and-set! (::multiple-lines s) false true)))))
            :content-editable true
            :dangerouslySetInnerHTML #js {"__html" @(::initial-add-comment s)}}]
          [:div.add-comment-footer.group
            [:button.mlb-reset.close-reply-bt
              {:on-click #(close-reply-clicked s)
               :data-toggle (if (responsive/is-tablet-or-mobile?) "" "tooltip")
               :data-placement "top"
               :data-container "body"
               :title (if edit-comment-data "Cancel edit" "Cancel")}]
            (when uploading?
              [:div.upload-progress
                (small-loading)
                [:span.attachment-uploading
                  (str "Uploading " (or (:progress attachment-uploading) 0) "%...")]])
            (emoji-picker {:add-emoji-cb #(did-change s)
                           :width 24
                           :height 24
                           :position "top"
                           :default-field-selector (str "div." add-comment-class)
                           :container-selector (str "div." add-comment-class)})
            [:div.add-comment-footer-media-picker.group]
            [:button.mlb-reset.send-btn
              {:on-click #(send-clicked % s)
               :disabled (not @(::post-enabled s))
               :class (when uploading? "separator-line")}
              (if edit-comment-data
                "Save"
                "Reply")]]]
        (when @(::me-media-utils/showing-media-video-modal s)
          [:div.video-container
            {:ref :video-container}
            (media-video-modal {:fullscreen false
                                :dismiss-cb #(do
                                              (me-media-utils/media-video-add s @(::me-media-utils/media-picker-ext s) nil)
                                              (reset! (::me-media-utils/showing-media-video-modal s) false))
                                :offset-element-selector [(keyword (str "div." container-class))]
                                :outer-container-selector [(keyword (str "div." container-class))]})])
        (when @(::me-media-utils/showing-gif-selector s)
          (giphy-picker {:fullscreen false
                         :pick-emoji-cb (fn [gif-obj]
                                         (reset! (::me-media-utils/showing-gif-selector s) false)
                                         (me-media-utils/media-gif-add s @(::me-media-utils/media-picker-ext s) gif-obj))
                         :offset-element-selector [(keyword (str "div." container-class))]
                         :outer-container-selector [(keyword (str "div." container-class))]}))]]))
