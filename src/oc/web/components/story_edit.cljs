(ns oc.web.components.story-edit
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.local-settings :as ls]
            [oc.web.lib.image-upload :as iu]
            [oc.web.lib.medium-editor-exts :as editor]
            [oc.web.components.ui.alert-modal :refer (alert-modal)]
            [oc.web.components.ui.media-picker :refer (media-picker)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.media-video-modal :refer (media-video-modal)]
            [oc.web.components.ui.media-chart-modal :refer (media-chart-modal)]
            [goog.dom :as gdom]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

;; Update story

(def default-story-title "Untitled Story")
(def default-save-wait 2000)
(def default-save-message-show 2000)

(defn story-autosave [s]
  (reset! (::central-message s) nil)
  (when @(::last-timeout s)
    (js/clearTimeout @(::last-timeout s)))
  (reset! (::last-timeout s)
   (utils/after default-save-wait
    (fn []
      (dis/dispatch! [:draft-autosave])
      (reset! (::central-message s) "Saving")))))

(defn update-story-editing [s new-data]
  (dis/dispatch! [:input [:story-editing] (merge @(drv/get-ref s :story-editing) new-data {:has-changes true})])
  (story-autosave s))

;; Body change handling

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
      (update-story-editing state {:body emojied-body}))))

(defn- title-on-change [state]
  (when-let [title (sel1 [:div.story-edit-title])]
    (let [emojied-title   (utils/emoji-images-to-unicode (gobj/get (utils/emojify (.-innerHTML title)) "__html"))]
      (update-story-editing state {:title emojied-title}))))

(defn- setup-body-editor [state]
  (let [media-picker-id @(::media-picker-id state)
        title-el  (sel1 [:div.story-edit-title])
        body-el      (sel1 [:div.story-edit-body])
        body-editor  (new js/MediumEditor body-el (clj->js (-> "Say something..."
                                                            (utils/medium-editor-options false true)
                                                            (editor/inject-extension (editor/media-upload media-picker-id {:left 44 :top -102})))))]
    (.subscribe body-editor
                "editableInput"
                (fn [event editable]
                  (body-on-change state)))
    (reset! (::body-editor state) body-editor)
    (js/recursiveAttachPasteListener body-el (comp #(utils/medium-editor-hide-placeholder @(::body-editor state) body-el) #(body-on-change state)))
    (events/listen title-el EventType/INPUT #(title-on-change state))
    (js/emojiAutocomplete)))

;; Title handling

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

;; Banner handling

(defn banner-add-if-finished [s]
  (let [image @(::banner-url s)]
    (when (and (contains? image :url)
               (contains? image :width)
               (contains? image :height))
      (reset! (::banner-url s) nil)
      (reset! (::banner-add-did-success s) false)
      (update-story-editing s {:banner-url (:url image) :banner-width (:width image) :banner-height (:height image)}))))

(defn banner-add-dismiss-picker
  "Called every time the image picke close, reset to inital state."
  [s]
  (when-not @(::banner-add-did-success s)
    (reset! (::banner-url s) false)))

(defn banner-add-error
  "Show an error alert view for failed uploads."
  []
  (let [alert-data {:icon "/img/ML/error_icon.png"
                    :title "Sorry!"
                    :message "An error occurred with your image."
                    :solid-button-title "OK"
                    :solid-button-cb #(dis/dispatch! [:alert-modal-hide])}]
    (dis/dispatch! [:alert-modal-show alert-data])))

(defn banner-on-load [s url img]
  (reset! (::banner-url s) (merge @(::banner-url s) {:width (.-width img) :height (.-height img)}))
  (gdom/removeNode img)
  (banner-add-if-finished s))

;; Media picker handling

(defn media-picker-did-change [s]
  (body-on-change s)
  (story-autosave s)
  (utils/after 100
    #(do
       (utils/to-end-of-content-editable (sel1 [:div.story-edit-body]))
       (utils/scroll-to-bottom (.-body js/document) true))))

(defn complete-story-edit-url [uuid]
  (str "http" (when ls/jwt-cookie-secure "s") "://" ls/web-server (oc-urls/story-edit (router/current-org-slug) (router/current-board-slug) uuid)))

(defn share-draft-select-text []
  (.select (sel1 [:input#share-draft-copy-text])))

(defn copy-clicked [story-uuid]
  (share-draft-select-text)
  (utils/copy-to-clipboard (complete-story-edit-url story-uuid)))

(defn delete-clicked [e story-data]
  (utils/event-stop e)
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :message "Delete this?"
                    :link-button-title "No"
                    :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                    :solid-button-title "Yes"
                    :solid-button-cb #(do
                                        (dis/dispatch! [:activity-delete story-data])
                                        (dis/dispatch! [:alert-modal-hide]))
                    }]
    (dis/dispatch! [:alert-modal-show alert-data])))

(rum/defcs story-edit < rum/reactive
                        ;; Story edits
                        (drv/drv :story-editing)
                        (drv/drv :alert-modal)
                        ;; Medium editor
                        (rum/local nil ::body-editor)
                        ;; Initial data
                        (rum/local "" ::initial-title)
                        (rum/local "" ::initial-body)
                        ;; Autosave
                        (rum/local "" ::central-message)
                        (rum/local nil ::last-timeout)
                        ;; Needed to load the story data from the server
                        (rum/local nil ::activity-uuid)
                        ;; Banner url
                        (rum/local nil ::banner-url)
                        (rum/local false ::banner-add-did-success)
                        ;; Media picker
                        (rum/local "story-edit-media-picker" ::media-picker-id)
                        ;; Shaer draft
                        (rum/local false ::show-share-draft)
                        (rum/local nil ::window-click-listener)
                        {:will-mount (fn [s]
                                       (let [story-editing @(drv/get-ref s :story-editing)]
                                         (reset! (::initial-title s) (:title story-editing))
                                         (reset! (::initial-body s) (:body story-editing))
                                         (reset! (::activity-uuid s) (:uuid story-editing)))
                                       (reset! (::window-click-listener s)
                                         (events/listen js/window EventType/CLICK
                                          #(when (and @(::show-share-draft s)
                                                      (not (utils/event-inside? % (sel1 [:div.share-draft-container])))
                                                      (not (utils/event-inside? % (sel1 [:button.share-button]))))
                                             (reset! (::show-share-draft s) false))))
                                       s)
                         :did-mount (fn [s]
                                      (utils/after 1000 #(setup-body-editor s))
                                      s)
                         :did-remount (fn [o s]
                                        (when-let [story-editing @(drv/get-ref s :story-editing)]
                                          ;; Replace title and body only if the story wasn't loaded yet
                                          (when (nil? @(::activity-uuid s))
                                            (reset! (::initial-title s) (:title story-editing))
                                            (reset! (::initial-body s) (:body story-editing))
                                            (reset! (::activity-uuid s) (:uuid story-editing)))
                                          ;; If it's saving and there is no autosaving key in the story-editing it means saving ended
                                          (when (and (= @(::central-message s) "Saving")
                                                     (not (:autosaving story-editing)))
                                            (reset! (::central-message s) "Saved")
                                            (reset! (::last-timeout s) nil)))
                                        s)
                         :after-render (fn [s]
                                         (doto (js/$ "[data-toggle=\"tooltip\"]")
                                           (.tooltip "fixTitle")
                                           (.tooltip "hide"))
                                         s)
                         :will-unmount (fn [s]
                                        (events/unlistenByKey @(::window-click-listener s))
                                        s)}
  [s]
  (let [story-data (drv/react s :story-editing)
        story-author (if (map? (:author story-data))
                       (:author story-data)
                       (first (:author story-data)))]
    [:div.story-edit-container
      (when (drv/react s :alert-modal)
        (alert-modal))
      (when (:media-video story-data)
        (media-video-modal :story-editing))
      (when (:media-chart story-data)
        (media-chart-modal :story-editing))
      [:div.story-edit-header.group
        [:div.story-edit-header-left
          [:a.board-name
            {:href (oc-urls/board (router/current-board-slug))
             :on-click #(do (utils/event-stop %) (router/nav! (oc-urls/board (router/current-org-slug) (if (= (:status story-data) "draft") "drafts" (:board-slug story-data)))))}
            (if (or (not (:storyboard-name story-data))
                    (= (:type story-data) "draft"))
              "Drafts"
              (:storyboard-name story-data))]
          [:span.arrow ">"]
          [:span.story-edit-top-title
            {:dangerouslySetInnerHTML (utils/emojify (utils/strip-HTML-tags (if (empty? (:title story-data)) default-story-title (:title story-data))))}]
          [:button.mlb-reset.mlb-link-black.delete-link
            {:on-click #(delete-clicked % story-data)}
            "Delete"]]
        [:div.story-edit-header-center
          [:span.story-edit-header-left-save
            {:class (when-not (empty? @(::central-message s)) "showing")}
            @(::central-message s)]]
        [:div.story-edit-header-right
          [:button.mlb-reset.mlb-default.share-button
            {:on-click #(let [showing @(::show-share-draft s)]
                         (when-not showing
                           (utils/after 1000 share-draft-select-text))
                         (reset! (::show-share-draft s) (not showing)))}
            "Share Draft"]
          (when @(::show-share-draft s)
            [:div.share-draft-container
              [:div.share-draft-triangle]
              [:div.share-draft-title
                "Share a link to this draft"]
              [:div.share-draft-description
                "People with this link will be able to comment on your story, but not edit it."]
              [:div.share-draft-link-box
                [:input
                  {:type "text"
                   :id "share-draft-copy-text"
                   :readonly true
                   :value (complete-story-edit-url (:uuid story-data))}]
                [:button.mlb-reset.mlb-default.share-draft-copy-btn
                  {:on-click #(copy-clicked (:uuid story-data))}
                  "Copy"]]])
          [:button.mlb-reset.mlb-default.post-button
            {:on-click #(dis/dispatch! [:story-share])}
            "Post"]]]
      [:div.story-edit-content
        [:div.story-edit-author.group
          (user-avatar-image story-author)
          [:div.name (or (:name story-author) (str (:first-name story-author) " " (:last-name story-author)))]
          [:div.time-since
            (if (:created-at story-data)
              [:time
                {:date-time (:created-at story-data)
                 :data-toggle "tooltip"
                 :data-placement "top"
                 :title (utils/activity-date-tooltip story-data)}
                (utils/time-since (:created-at story-data))]
              "Draft")]]
        (if (:banner-url story-data)
          [:div.story-edit-banner
            {:style #js {:backgroundImage (str "url(" (:banner-url story-data) ")")
                         :height (str (min 200 (* (/ (:banner-height story-data) (:banner-width story-data)) 840)) "px")}}
            [:button.mlb-reset.mlb-default.remove-banner
              {:on-click #(update-story-editing s {:banner-url nil :banner-width 0 :banner-height 0})}
              "Remove Image"]]
          [:div.story-edit-add-banner
            {:on-click (fn []
                         (iu/upload! {:accept "image/*" ; :imageMin [840 200]
                                      :transformations {
                                        :crop {
                                          :aspectRatio (/ 840 200)}}}
                           (fn [res]
                             (reset! (::banner-add-did-success s) true)
                             (let [url (gobj/get res "url")
                                   img   (gdom/createDom "img")]
                               (set! (.-onload img) #(banner-on-load s url img))
                               (set! (.-className img) "hidden")
                               (gdom/append (.-body js/document) img)
                               (set! (.-src img) url)
                               (reset! (::banner-url s) {:res res :url url})))
                             nil
                             (fn [err]
                               (banner-add-error))
                             (fn []
                               ;; Delay the check because this is called on cancel but also on success
                               (utils/after 1000 #(banner-add-dismiss-picker s)))))}
            "Click here to upload your header image."])
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
          {:dangerouslySetInnerHTML (utils/emojify @(::initial-body s))}]
        (media-picker [:photo :video :chart :attachment :divider-line] @(::media-picker-id s) #(media-picker-did-change s) "div.story-edit-body" story-data :story-editing "<br />")]]))