(ns oc.web.components.entry-edit
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.image-upload :as iu]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.medium-editor-exts :as editor]
            [oc.web.components.ui.mixins :as mixins]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.rich-body-editor :refer (rich-body-editor)]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn calc-edit-entry-modal-height [s]
  (when @(:first-render-done s)
    (when-let [entry-edit-modal (rum/ref-node s "entry-edit-modal")]
      (when (not= @(::entry-edit-modal-height s) (.-clientHeight entry-edit-modal))
        (reset! (::entry-edit-modal-height s) (.-clientHeight entry-edit-modal))))))

(defn dismiss-modal []
  (dis/dispatch! [:entry-edit/dismiss]))

(defn real-close [s]
  (reset! (::dismiss s) true)
  (utils/after 180 #(dismiss-modal)))

(defn cancel-clicked [s]
  (if (:has-changes @(drv/get-ref s :entry-editing))
    (let [alert-data {:icon "/img/ML/trash.svg"
                      :action "dismiss-edit-dirty-data"
                      :message (str "Cancel without saving your changes?")
                      :link-button-title "No"
                      :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                      :solid-button-title "Yes"
                      :solid-button-cb #(do
                                          (dis/dispatch! [:alert-modal-hide])
                                          (real-close s))
                      }]
      (dis/dispatch! [:alert-modal-show alert-data]))
    (real-close s)))

(defn unique-slug [topics topic-name]
  (let [slug (atom (s/slug topic-name))]
    (while (seq (filter #(= (:slug %) @slug) topics))
      (reset! slug (str (s/slug topic-name) "-" (int (rand 1000)))))
    @slug))

(defn toggle-topics-dd []
  (.dropdown (js/$ "div.entry-card-dd-container button.dropdown-toggle") "toggle"))

(defn body-on-change [state]
  (dis/dispatch! [:input [:entry-editing :has-changes] true])
  (calc-edit-entry-modal-height state))

(defn- headline-on-change [state]
  (when-let [headline (sel1 [:div.entry-edit-headline])]
    (let [emojied-headline   (utils/emoji-images-to-unicode (gobj/get (utils/emojify (.-innerHTML headline)) "__html"))]
      (dis/dispatch! [:input [:entry-editing :headline] emojied-headline])
      (dis/dispatch! [:input [:entry-editing :has-changes] true]))))

(defn- setup-headline [state]
  (let [headline-el  (rum/ref-node state "headline")]
    (reset! (::headline-input-listener state) (events/listen headline-el EventType/INPUT #(headline-on-change state)))
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

(defn add-emoji-cb [s]
  (headline-on-change s)
  (when-let [body (sel1 [:div.rich-body-editor])]
    (body-on-change s)))

(defn clean-body []
  (when-let [body-el (sel1 [:div.rich-body-editor])]
    (let [raw-html (.-innerHTML body-el)]
      (dis/dispatch! [:input [:entry-editing :body] (utils/clean-body-html raw-html)]))))

(rum/defcs entry-edit < rum/reactive
                        ;; Derivatives
                        (drv/drv :entry-edit-topics)
                        (drv/drv :current-user-data)
                        (drv/drv :entry-editing)
                        (drv/drv :board-filters)
                        (drv/drv :alert-modal)
                        (drv/drv :media-input)
                        ;; Locals
                        (rum/local false ::dismiss)
                        (rum/local nil ::body-editor)
                        (rum/local "" ::initial-body)
                        (rum/local "" ::initial-headline)
                        (rum/local "" ::new-topic)
                        (rum/local false ::focusing-create-topic)
                        (rum/local 330 ::entry-edit-modal-height)
                        (rum/local nil ::headline-input-listener)
                        ;; Mixins
                        mixins/no-scroll-mixin
                        mixins/first-render-mixin

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
                                      (utils/after 300 #(setup-headline s))
                                      (utils/to-end-of-content-editable (rum/ref-node s "headline"))
                                      s)
                         :before-render (fn [s]
                                          (calc-edit-entry-modal-height s)
                                          s)
                         :will-unmount (fn [s]
                                         (when @(::body-editor s)
                                           (.destroy @(::body-editor s))
                                           (reset! (::body-editor s) nil))
                                         (when @(::headline-input-listener s)
                                           (events/unlistenByKey @(::headline-input-listener s))
                                           (reset! (::headline-input-listener s) nil))
                                         s)}
  [s]
  (let [topics            (distinct (drv/react s :entry-edit-topics))
        current-user-data (drv/react s :current-user-data)
        entry-editing     (drv/react s :entry-editing)
        alert-modal       (drv/react s :alert-modal)
        new-entry?        (empty? (:uuid entry-editing))
        fixed-entry-edit-modal-height (max @(::entry-edit-modal-height s) 330)
        wh (.-innerHeight js/window)
        media-input (drv/react s :media-input)]
    [:div.entry-edit-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(:first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(:first-render-done s))})
       :on-click #(when (and (not (:has-changes entry-editing))
                             (not (utils/event-inside? % (sel1 [:div.entry-edit-modal]))))
                    (cancel-clicked s))}
      [:div.modal-wrapper
        {:style {:margin-top (str (max 0 (/ (- wh fixed-entry-edit-modal-height) 2)) "px")}}
        ;; Show the close button only when there are no modals shown
        (when (and (not (:media-video media-input))
                   (not (:media-chart media-input))
                   (not alert-modal))
          [:button.carrot-modal-close.mlb-reset
            {:on-click #(cancel-clicked s)}])
        [:div.entry-edit-modal.group
          {:ref "entry-edit-modal"}
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
             :on-key-down #(headline-on-change s)
             :on-click    #(headline-on-change s)
             :on-key-press (fn [e]
                           (when (= (.-key e) "Enter")
                             (utils/event-stop e)
                             (utils/to-end-of-content-editable (sel1 [:div.rich-body-editor]))))
             :dangerouslySetInnerHTML @(::initial-headline s)}]
          (rich-body-editor {:on-change (partial body-on-change s)
                             :initial-body @(::initial-body s)
                             :show-placeholder (not (contains? entry-editing :links))
                             :show-h2 true
                             :dispatch-input-key :entry-editing
                             :media-config ["photo" "video" "chart" "attachment" "divider-line"]
                             :classes "emoji-autocomplete emojiable"})
          [:div.entry-edit-controls-right]]
          ; Bottom controls
          [:div.entry-edit-controls.group]
        [:div.entry-edit-modal-divider]
        [:div.entry-edit-modal-footer.group
          (emoji-picker {:add-emoji-cb (partial add-emoji-cb s)})
          [:button.mlb-reset.mlb-default.form-action-bt
            {:on-click #(do
                          (clean-body)
                          (dis/dispatch! [:entry-save])
                          (real-close s))
             :disabled (not (:has-changes entry-editing))}
            (if new-entry? "Post" "Save")]
          [:button.mlb-reset.mlb-link-black.form-action-bt
            {:on-click #(cancel-clicked s)}
            "Cancel"]]]]]))