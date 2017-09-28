(ns oc.web.components.entry-edit
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.medium-editor-exts :as editor]
            [oc.web.lib.image-upload :as iu]
            [oc.web.lib.medium-editor-exts :as editor]
            [oc.web.components.ui.media-picker :refer (media-picker)]
            [oc.web.components.ui.alert-modal :refer (alert-modal)]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.rich-body-editor :refer (rich-body-editor)]
            [cljsjs.medium-editor]
            [cljsjs.rangy-selectionsaverestore]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn dismiss-modal []
  (dis/dispatch! [:entry-edit/dismiss]))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 #(dismiss-modal)))

(defn unique-slug [topics topic-name]
  (let [slug (atom (s/slug topic-name))]
    (while (seq (filter #(= (:slug %) @slug) topics))
      (reset! slug (str (s/slug topic-name) "-" (int (rand 1000)))))
    @slug))

(defn toggle-topics-dd []
  (.dropdown (js/$ "div.entry-card-dd-container button.dropdown-toggle") "toggle"))

(defn body-on-change [body]
  (dis/dispatch! [:input [:entry-editing :body] body])
  (dis/dispatch! [:input [:entry-editing :has-changes] true]))

(defn- headline-on-change [state]
  (when-let [headline (sel1 [:div.entry-edit-headline])]
    (let [emojied-headline   (utils/emoji-images-to-unicode (gobj/get (utils/emojify (.-innerHTML headline)) "__html"))]
      (dis/dispatch! [:input [:entry-editing :headline] emojied-headline])
      (dis/dispatch! [:input [:entry-editing :has-changes] true]))))

(defn- setup-headline [state]
  (let [headline-el  (rum/ref-node state "headline")]
    (events/listen headline-el EventType/INPUT #(headline-on-change state))
    (js/emojiAutocomplete)))

(defn headline-on-paste
  "Avoid to paste rich text into headline, replace it with the plain text clipboard data."
  [state e]
  ; Prevent the normal paste behaviour
  (utils/event-stop e)
  (let [clipboardData (or (.-clipboardData e) (.-clipboardData js/window))
        pasted-data (.getData clipboardData "text/plain")
        headline-el     (.querySelector js/document "div.entry-edit-headline")]
    ; replace the selected text of headline with the text/plain data of the clipboard
    (js/replaceSelectedText pasted-data)
    ; call the headline-on-change to check for content length
    (headline-on-change state)
    ; move cursor at the end
    (utils/to-end-of-content-editable headline-el)))

(defn create-new-topic [s]
  (when-not (empty? @(::new-topic s))
    (let [topics @(drv/get-ref s :entry-edit-topics)
          topic-name (s/trim @(::new-topic s))
          topic-slug (unique-slug topics topic-name)]
      (dis/dispatch! [:topic-add {:name topic-name :slug topic-slug} true])
      (reset! (::new-topic s) ""))))

(defn media-picker-did-change [s]
  (body-on-change s)
  (utils/after 100 
    #(do
       (utils/to-end-of-content-editable (sel1 [:div.entry-edit-body]))
       (utils/scroll-to-bottom (sel1 [:div.entry-edit-modal-container]) true))))

(rum/defcs entry-edit < rum/reactive
                        (drv/drv :entry-edit-topics)
                        (drv/drv :current-user-data)
                        (drv/drv :entry-editing)
                        (drv/drv :board-filters)
                        (drv/drv :alert-modal)
                        (rum/local false ::first-render-done)
                        (rum/local false ::dismiss)
                        (rum/local nil ::body-editor)
                        (rum/local "" ::initial-body)
                        (rum/local "" ::initial-headline)
                        (rum/local "" ::new-topic)
                        (rum/local false ::focusing-create-topic)
                        (rum/local false ::remove-no-scroll)
                        (rum/local "entry-edit-media-picker" ::media-picker-id)
                        (rum/local 330 ::entry-edit-modal-height)
                        (rum/local false ::media-picker-expanded)
                        {:will-mount (fn [s]
                                       (let [entry-editing @(drv/get-ref s :entry-editing)
                                             board-filters @(drv/get-ref s :board-filters)
                                             initial-body (if (contains? entry-editing :links) (:body entry-editing) utils/default-body)
                                             initial-headline (utils/emojify (if (contains? entry-editing :links) (:headline entry-editing) ""))]
                                         ;; Load board if it's not already
                                         (when-not @(drv/get-ref s :entry-edit-topics)
                                           (dis/dispatch! [:board-get (utils/link-for (:links entry-editing) "up")]))
                                         (reset! (::initial-body s) initial-body)
                                         (reset! (::initial-headline s) initial-headline)
                                         (when (and (string? board-filters)
                                                    (nil? (:topic-slug entry-editing)))
                                            (let [topics @(drv/get-ref s :entry-edit-topics)
                                                  topic (first (filter #(= (:slug %) board-filters) topics))]
                                              (when topic
                                                (dis/dispatch! [:input [:entry-editing :topic-slug] (:slug topic)])
                                                (dis/dispatch! [:input [:entry-editing :topic-name] (:name topic)])))))
                                       s)
                         :did-mount (fn [s]
                                      ;; Add no-scroll to the body to avoid scrolling while showing this modal
                                      (let [body (sel1 [:body])]
                                        (when-not (dommy/has-class? body :no-scroll)
                                          (reset! (::remove-no-scroll s) true)
                                          (dommy/add-class! (sel1 [:body]) :no-scroll)))
                                      (setup-headline s)
                                      (utils/to-end-of-content-editable (rum/ref-node s "headline"))
                                      s)
                         :before-render (fn [s]
                                          (when-let [entry-edit-modal (sel1 [:div.entry-edit-modal])]
                                            (when (not= @(::entry-edit-modal-height s) (.-clientHeight entry-edit-modal))
                                              (reset! (::entry-edit-modal-height s) (.-clientHeight entry-edit-modal))))
                                          s)
                         :after-render (fn [s]
                                         (when (not @(::first-render-done s))
                                           (reset! (::first-render-done s) true))
                                         s)
                         :will-unmount (fn [s]
                                         ;; Remove no-scroll class from the body tag
                                         (when @(::remove-no-scroll s)
                                           (dommy/remove-class! (sel1 [:body]) :no-scroll))
                                         (when @(::body-editor s)
                                           (.destroy @(::body-editor s))
                                           (reset! (::body-editor s) nil))
                                         s)}
  [s]
  (let [topics            (distinct (drv/react s :entry-edit-topics))
        current-user-data (drv/react s :current-user-data)
        entry-editing     (drv/react s :entry-editing)
        alert-modal       (drv/react s :alert-modal)
        new-entry?        (empty? (:uuid entry-editing))
        fixed-entry-edit-modal-height (max @(::entry-edit-modal-height s) 330)
        wh (.-innerHeight js/window)]
    [:div.entry-edit-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(::first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(::first-render-done s))})
       :on-click #(when (and (empty? (:body entry-editing))
                             (empty? (:headline entry-editing))
                             (not (utils/event-inside? % (sel1 [:div.entry-edit-modal]))))
                    (close-clicked s))}
      [:div.modal-wrapper
        {:style {:margin-top (str (max 0 (/ (- wh fixed-entry-edit-modal-height) 2)) "px")}}
        ;; Show the close button only when there are no modals shown
        (when (and (not (:media-video entry-editing))
                   (not (:media-chart entry-editing))
                   (not alert-modal))
          [:button.carrot-modal-close.mlb-reset
            {:on-click #(close-clicked s)}])
        [:div.entry-edit-modal.group
          [:div.entry-edit-modal-header.group
            (user-avatar-image current-user-data)
            [:div.posting-in (if new-entry? "Posting" "Posted") " in " [:span (:board-name entry-editing)]]
            [:div.entry-card-dd-container
              (if (:topic-name entry-editing)
                [:button.mlb-reset.dropdown-toggle.has-topic
                  {:type "button"
                   :id "entry-edit-dd-btn"
                   :data-toggle "dropdown"
                   :aria-haspopup true
                   :aria-expanded false}
                  [:div.activity-tag
                    (:topic-name entry-editing)]]
                [:button.mlb-reset.dropdown-toggle
                  {:type "button"
                   :id "entry-edit-dd-btn"
                   :data-toggle "dropdown"
                   :aria-haspopup true
                   :aria-expanded false}
                  "+ Add a topic"])
              [:div.entry-edit-topics-dd.dropdown-menu
                {:aria-labelledby "entry-edit-dd-btn"}
                [:div.triangle]
                [:div.entry-dropdown-list-content
                  [:ul
                    (for [t (sort #(compare (:name %1) (:name %2)) topics)
                          :let [selected (= (:topic-name entry-editing) (:name t))]]
                      [:li.selectable.group
                        {:key (str "entry-edit-dd-" (:slug t))
                         :on-click #(dis/dispatch! [:input [:entry-editing] (merge entry-editing {:topic-name (:name t) :has-changes true})])
                         :class (when selected "select")}
                        [:button.mlb-reset
                          (:name t)]
                        (when selected
                          [:button.mlb-reset.mlb-link.remove
                            {:on-click (fn [e]
                                         (utils/event-stop e)
                                         (dis/dispatch! [:input [:entry-editing] (merge entry-editing {:topic-slug nil :topic-name nil :has-changes true})]))}
                            "Remove"])])
                    [:li.divider]
                    [:li.entry-edit-new-topic.group
                      ; {:on-click #(do (utils/event-stop %) (toggle-topics-dd))}
                      (when-not @(::focusing-create-topic s)
                        [:button.mlb-reset.entry-edit-new-topic-plus
                          {:on-click (fn [e]
                                       (utils/event-stop e)
                                       (toggle-topics-dd)
                                       (.focus (js/$ "input.entry-edit-new-topic-field")))
                           :title "Create a new topic"}])
                      [:input.entry-edit-new-topic-field
                        {:type "text"
                         :value @(::new-topic s)
                         :on-focus #(reset! (::focusing-create-topic s) true)
                         :on-blur (fn [e] (utils/after 100 #(reset! (::focusing-create-topic s) false)))
                         :on-key-up (fn [e]
                                      (cond
                                        (= "Enter" (.-key e))
                                        (create-new-topic s)))
                         :on-change #(reset! (::new-topic s) (.. % -target -value))
                         :placeholder "Create New Topic"}]
                      (when @(::focusing-create-topic s)
                        [:button.mlb-reset.mlb-default.entry-edit-new-topic-create
                          {:on-click (fn [e]
                                       (utils/event-stop e)
                                       (create-new-topic s))
                           :disabled (empty? (s/trim @(::new-topic s)))}
                          "Apply"])]]]]]]
        [:div.entry-edit-modal-body
          ; Headline element
          [:div.entry-edit-headline.emoji-autocomplete.emojiable
            {:content-editable true
             :ref "headline"
             :placeholder "Title this (if you like)"
             :on-paste    #(headline-on-paste s %)
             :on-key-Up   #(headline-on-change s)
             :on-key-down #(headline-on-change s)
             :on-focus    #(headline-on-change s)
             :on-blur     #(headline-on-change s)
             :auto-focus true
             :dangerouslySetInnerHTML @(::initial-headline s)}]
          (rich-body-editor {:on-change body-on-change
                             :initial-body @(::initial-body s)
                             :show-placeholder (not (contains? entry-editing :links))
                             :dispatch-input-key :entry-editing
                             :media-config ["photo" "video" "chart" "attachment"]
                             :classes "emoji-autocomplete emojiable"})
          [:div.entry-edit-controls-right]]
          ; Bottom controls
          [:div.entry-edit-controls.group]
        [:div.entry-edit-modal-divider]
        [:div.entry-edit-modal-footer.group
          [:button.mlb-reset.mlb-default.form-action-bt
            {:on-click #(do
                          (dis/dispatch! [:entry-save])
                          (close-clicked s))
             :disabled (not (:has-changes entry-editing))}
            (if new-entry? "Post" "Save")]
          [:button.mlb-reset.mlb-link-black.form-action-bt
            {:on-click #(close-clicked s)}
            "Cancel"]]]]]))