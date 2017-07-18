(ns oc.web.components.entry-create
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [cuerdas.core :as s]
            [oc.web.lib.medium-editor-exts :as editor]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [cljsjs.medium-editor]
            [goog.object :as googobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn dismiss-modal []
  (dis/dispatch! [:new-entry-toggle false]))

(defn close-clicked [s & [saving?]]
  (when-not saving?
    (dis/dispatch! [:input [:new-entry-edit] nil]))
  (reset! (::dismiss s) true)
  (utils/after 180 dismiss-modal))

(defn unique-slug [topics topic-name]
  (let [slug (atom (s/slug topic-name))]
    (while (seq (filter #(= (:slug %) @slug) topics))
      (reset! slug (str (s/slug topic-name) "-" (int (rand 1000)))))
    @slug))

(defn toggle-topics-dd []
  (.dropdown (js/$ "div.entry-card-dd-container button.dropdown-toggle") "toggle"))

(defn add-topic [state]
  (let [topics (:topics @(drv/get-ref state :board-data))
        topic-name @(::new-topic state)]
    (if (seq (filter #(= (s/lower (:name %)) (s/lower topic-name)) topics))
      (js/alert "Please choose another topic name.")
      (let [topic-slug (unique-slug topics topic-name)]
        (dis/dispatch! [:topic-add {:slug topic-slug :name topic-name} true])
        (reset! (::new-topic state) "")
        ;; Dismiss the dropdown:
        (toggle-topics-dd)))))

(defn body-on-change [state]
  (when-let [body-el (sel1 [:div.entry-create-body])]
    ; Attach paste listener to the body and all its children
    (js/recursiveAttachPasteListener body-el (comp #(utils/medium-editor-hide-placeholder @(::body-editor state) body-el) #(body-on-change state)))
    (let [emojied-body (utils/emoji-images-to-unicode (googobj/get (utils/emojify (.-innerHTML body-el)) "__html"))]
      (dis/dispatch! [:input [:new-entry-edit :body] emojied-body]))))

(defn- headline-on-change [state]
  (when-let [headline (sel1 [:div.entry-create-headline])]
    (let [emojied-headline   (utils/emoji-images-to-unicode (googobj/get (utils/emojify (.-innerHTML headline)) "__html"))]
      (dis/dispatch! [:input [:new-entry-edit :headline] emojied-headline]))))

(defn- setup-body-editor [state]
  (let [headline-el  (sel1 [:div.entry-create-headline])
        body-el      (sel1 [:div.entry-create-body])
        body-editor  (new js/MediumEditor body-el (clj->js (utils/medium-editor-options "What's new?" false)))]
    (.subscribe body-editor
                "editableInput"
                (fn [event editable]
                  (body-on-change state)))
    (reset! (::body-editor state) body-editor)
    (js/recursiveAttachPasteListener body-el (comp #(utils/medium-editor-hide-placeholder @(::body-editor state) body-el) #(body-on-change state)))
    (events/listen headline-el EventType/INPUT #(headline-on-change state))
    (js/emojiAutocomplete)))

(defn headline-on-paste
  "Avoid to paste rich text into headline, replace it with the plain text clipboard data."
  [state e]
  ; Prevent the normal paste behaviour
  (utils/event-stop e)
  (let [clipboardData (or (.-clipboardData e) (.-clipboardData js/window))
        pasted-data (.getData clipboardData "text/plain")
        headline-el     (.querySelector js/document "div.entry-create-headline")]
    ; replace the selected text of headline with the text/plain data of the clipboard
    ; (set! (.-innerText headline-el) pasted-data)
    (js/replaceSelectedText pasted-data)
    ; call the headline-on-change to check for content length
    (headline-on-change state)
    ; move cursor at the end
    (utils/to-end-of-content-editable headline-el)))

(rum/defcs entry-create < rum/reactive
                          (drv/drv :board-data)
                          (drv/drv :current-user-data)
                          (drv/drv :new-entry-edit)
                          (drv/drv :board-filters)
                          (rum/local false ::first-render-done)
                          (rum/local false ::dismiss)
                          (rum/local nil ::body-editor)
                          (rum/local "" ::new-topic)
                          {:will-mount (fn [s]
                                         (let [new-entry-edit @(drv/get-ref s :new-entry-edit)
                                               board-filters @(drv/get-ref s :board-filters)]
                                           (when (and (string? board-filters)
                                                      (nil? (:topic-slug new-entry-edit)))
                                              (let [topics (:topics @(drv/get-ref s :board-data))
                                                    topic (first (filter #(= (:slug %) board-filters) topics))]
                                                (when topic
                                                  (dis/dispatch! [:input [:new-entry-edit :topic-slug] (:slug topic)])))))
                                         s)
                           :did-mount (fn [s]
                                        ;; Add no-scroll to the body to avoid scrolling while showing this modal
                                        (dommy/add-class! (sel1 [:body]) :no-scroll)
                                        (setup-body-editor s)
                                        s)
                           :after-render (fn [s]
                                           (when (not @(::first-render-done s))
                                             (reset! (::first-render-done s) true))
                                           s)
                           :will-unmount (fn [s]
                                           ;; Remove no-scroll class from the body tag
                                           (dommy/remove-class! (sel1 [:body]) :no-scroll)
                                           s)}
  [s]
  (let [board-data (drv/react s :board-data)
        topics (:topics board-data)
        current-user-data (drv/react s :current-user-data)
        new-entry-edit (drv/react s :new-entry-edit)
        topic (first (filter #(= (:slug %) (:topic-slug new-entry-edit)) topics))]
    [:div.entry-create-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(::first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(::first-render-done s))})
       :on-click #(when (and (empty? (:body new-entry-edit))
                             (empty? (:headline new-entry-edit)))
                    (close-clicked s))}
      [:div.entry-create-modal.group
        {:on-click #(utils/event-stop %)}
        [:div.entry-create-modal-header.group
          (user-avatar-image current-user-data)
          [:div.posting-in "Posting in " [:span (:name board-data)]]
          [:div.arrow [:i.fa.fa-angle-right]]
          [:div.select-topic (if topic (:name topic) "Select a topic")]
          [:div.entry-card-dd-container
            [:button.mlb-reset.dropdown-toggle
              {:type "button"
               :id "entry-create-dd-btn"
               :data-toggle "dropdown"
               :aria-haspopup true
               :aria-expanded false}
              [:i.fa.fa-caret-down]]
            [:div.entry-create-topics-dd.dropdown-menu
              {:aria-labelledby "entry-create-dd-btn"}
              [:div.triangle]
              [:div.entry-dropdown-list-content
                [:ul
                  (for [t topics]
                    [:li
                      {:data-topic-slug (:slug t)
                       :key (str "entry-create-dd-" (:slug t))
                       :on-click #(dis/dispatch! [:input [:new-entry-edit :topic-slug] (:slug t)])
                       :class (when (= (:topic-slug new-entry-edit) (:slug t)) "select")}
                      (:name t)])
                  [:li.divider]
                  [:li.entry-create-new-topic
                    {:on-click #(utils/event-stop %)}
                    [:button.mlb-reset.entry-create-new-topic-plus
                      {:on-click (fn [e]
                                   (if (empty? @(::new-topic s))
                                     (do
                                       (toggle-topics-dd)
                                       (.focus (sel1 [:input.entry-create-new-topic-field])))
                                     (add-topic s)))
                       :title "Create a new topic"}]
                    [:input.entry-create-new-topic-field
                      {:type "text"
                       :value @(::new-topic s)
                       :on-key-up (fn [e]
                                    (cond
                                      (= "Enter" (.-key e))
                                      (when-not (empty? @(::new-topic s))
                                        (add-topic s))))
                       :on-change #(reset! (::new-topic s) (.. % -target -value))
                       :placeholder "Create New Topic"}]]]]]]]
      [:div.entry-create-modal-divider]
      [:div.entry-create-modal-body
        [:div.entry-create-headline.emoji-autocomplete.emojiable
          {:content-editable true
           :placeholder "Title this (if you like)"
           ; :on-change #(dis/dispatch! [:input [:new-entry-edit :headline] (.. % -target -value)])
           :on-paste    #(headline-on-paste s %)
           :on-key-Up   #(headline-on-change s)
           :on-key-down #(headline-on-change s)
           :on-focus    #(headline-on-change s)
           :on-blur     #(headline-on-change s)
           :dangerouslySetInnerHTML #js {"__html" ""}}]
        [:div.entry-create-body.emoji-autocomplete.emojiable
          {:placeholder "What's new?"
           :data-placeholder "What's new?"
           :role "textbox"
           :aria-multiline true
           :contentEditable true
           :dangerouslySetInnerHTML #js {"__html" ""}}]]
      [:div.entry-create-modal-divider]
      [:div.entry-create-modal-footer.group
        [:button.mlb-reset.mlb-default
          {:on-click #(do
                        (dis/dispatch! [:new-entry-add])
                        (close-clicked %))
           :disabled (and (empty? (:body new-entry-edit))
                          (empty? (:headline new-entry-edit)))}
          "Post"]
        [:button.mlb-reset.mlb-link-black
          {:on-click #(close-clicked s)}
          "Cancel"]]]]))

