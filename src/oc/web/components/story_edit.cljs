(ns oc.web.components.story-edit
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.image-upload :as iu]
            [oc.web.lib.medium-editor-exts :as editor]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(def default-story-title "Untitled Story")

(defn body-on-change [state]
  (when-let [body-el (sel1 [:div.story-edit-body])]
    ; Hide/show placeholder capturing the situation medium-editor ignores:
    ; multiple empty Ps, img or iframes
    (utils/after 1000
     #(when-let [$body-el (js/$ "div.story-edit-body.medium-editor-placeholder")]
        (if (and (empty? (.text $body-el))
                 (<= (.-length (.find $body-el "div, p")) 1)
                 (zero? (.-length (.find $body-el "img, iframe"))))
          (.removeClass $body-el "hide-placeholder")
          (.addClass $body-el "hide-placeholder"))))
    ; Attach paste listener to the body and all its children
    (js/recursiveAttachPasteListener body-el (comp #(utils/medium-editor-hide-placeholder @(::body-editor state) body-el) #(body-on-change state)))
    (let [emojied-body (utils/emoji-images-to-unicode (gobj/get (utils/emojify (.-innerHTML body-el)) "__html"))]
      (dis/dispatch! [:input [:story-editing :body] emojied-body])
      (dis/dispatch! [:input [:story-editing :has-changes] true]))))

(defn- title-on-change [state]
  (when-let [title (sel1 [:div.story-edit-title])]
    (let [emojied-title   (utils/emoji-images-to-unicode (gobj/get (utils/emojify (.-innerHTML title)) "__html"))]
      (dis/dispatch! [:input [:story-editing :title] emojied-title])
      (dis/dispatch! [:input [:story-editing :has-changes] true]))))

(defn- setup-body-editor [state]
  (let [title-el  (sel1 [:div.story-edit-title])
        body-el      (sel1 [:div.story-edit-body])
        body-editor  (new js/MediumEditor body-el (clj->js (-> "Say something..."
                                                            (utils/medium-editor-options false)
                                                            (editor/inject-extension editor/file-upload))))]
    (.subscribe body-editor
                "editableInput"
                (fn [event editable]
                  (body-on-change state)))
    (reset! (::body-editor state) body-editor)
    (js/recursiveAttachPasteListener body-el (comp #(utils/medium-editor-hide-placeholder @(::body-editor state) body-el) #(body-on-change state)))
    (events/listen title-el EventType/INPUT #(title-on-change state))
    (js/emojiAutocomplete)))

(defn title-on-paste
  "Avoid to paste rich text into title, replace it with the plain text clipboard data."
  [state e]
  ; Prevent the normal paste behaviour
  (utils/event-stop e)
  (let [clipboardData (or (.-clipboardData e) (.-clipboardData js/window))
        pasted-data (.getData clipboardData "text/plain")
        title-el     (.querySelector js/document "div.story-edit-title")]
    ; replace the selected text of title with the text/plain data of the clipboard
    (js/replaceSelectedText pasted-data)
    ; call the title-on-change to check for content length
    (title-on-change state)
    ; move cursor at the end
    (utils/to-end-of-content-editable title-el)))

(rum/defcs story-edit < rum/reactive
                        (drv/drv :story-data)
                        (drv/drv :story-editing)
                        (drv/drv :current-user-data)
                        (rum/local nil ::window-resize)
                        (rum/local nil ::body-editor)
                        (rum/local "" ::initial-title)
                        (rum/local "" ::initial-body)
                        (rum/local nil ::initial-story-data)
                        {:will-mount (fn [s]
                                       (let [story-data @(drv/get-ref s :story-data)
                                             story-editing @(drv/get-ref s :story-editing)
                                             initial-title (if (contains? story-data :links) (:title story-data) "")
                                             initial-body (if (contains? story-data :links) (:body story-data) "")]
                                         (reset! (::initial-title s) initial-title)
                                         (reset! (::initial-body s) initial-body)
                                         (reset! (::initial-story-data s) story-data)
                                         (if (router/current-activity-id)
                                           (dis/dispatch! [:story-get])
                                           (dis/dispatch! [:input [:story-editing] {:org-slug (router/current-org-slug)
                                                                                    :board-slug (router/current-board-slug)
                                                                                    :type "story"
                                                                                    :title initial-title
                                                                                    :body initial-body}])))
                                       s)
                         :did-mount (fn [s]
                                      (utils/after 1000 #(setup-body-editor s))
                                      s)
                         :did-remount (fn [o s]
                                        (when-let [story-data @(drv/get-ref s :story-data)]
                                          (when (not= @(::initial-story-data s) story-data)
                                            (let [story-editing @(drv/get-ref s :story-editing)
                                                  initial-title (if (contains? story-data :links) (:title story-data) (:title story-editing))
                                                  initial-body (if (contains? story-data :links) (:body story-data) (:body story-editing))]
                                              (dis/dispatch! [:input [:story-editing] (merge story-editing {:title initial-title
                                                                                                            :body initial-body})])
                                              (when (not= initial-title @(::initial-title s))
                                                (reset! (::initial-title s) initial-title))
                                              (when (not= initial-body @(::initial-body s))
                                                (reset! (::initial-body s) initial-body)))))
                                        s)
                         :after-render (fn [s]
                                         (doto (js/$ "[data-toggle=\"tooltip\"]")
                                           (.tooltip "fixTitle")
                                           (.tooltip "hide"))
                                         s)}
  [s]
  (let [story-data (drv/react s :story-editing)
        story-author (drv/react s :current-user-data)]
    [:div.story-edit-container
      [:div.story-edit-header.group
        [:div.story-edit-header-left
          [:a.board-name
            {:href (oc-urls/board (router/current-board-slug))
             :on-click #(utils/event-stop %)}
            (or (:storyboard-name story-data) "Draft")]
          [:span.arrow ">"]
          [:span.story-edit-top-title
            {:dangerouslySetInnerHTML (utils/emojify (or (:title story-data) default-story-title))}]]
        [:div.story-edit-header-right
          [:button.mlb-reset.mlb-link.share-button
            {:on-click #()}
            "Share Draft"]
          [:button.mlb-reset.mlb-default.post-button
            {:on-click #()}
            "Post"]]]
      [:div.story-edit-content
        (when (:banner-url story-data)
          [:div.story-edit-banner
            {:style #js {:backgroundImage (str "url(" (:banner-url story-data) ")")
                         :height (str (min 200 (* (/ (:banner-height story-data) (:banner-width story-data)) 840)) "px")}}])
        [:div.story-edit-title.emoji-autocomplete
          {:content-editable true
           :placeholder default-story-title
           :on-paste    #(title-on-paste s %)
           :on-key-Up   #(title-on-change s)
           :on-key-down #(title-on-change s)
           :on-focus    #(title-on-change s)
           :on-blur     #(title-on-change s)
           :dangerouslySetInnerHTML (utils/emojify @(::initial-title s))}]
        [:div.story-edit-body.emoji-autocomplete
          {:dangerouslySetInnerHTML (utils/emojify @(::initial-body s))}]]]))

