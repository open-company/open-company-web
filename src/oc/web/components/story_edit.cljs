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
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.medium-editor-exts :as editor]
            [oc.web.components.ui.alert-modal :refer (alert-modal)]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.ui.media-picker :refer (media-picker)]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.rich-body-editor :refer (rich-body-editor)]
            [oc.web.components.ui.media-video-modal :refer (media-video-modal)]
            [oc.web.components.ui.media-chart-modal :refer (media-chart-modal)]
            [oc.web.components.ui.story-publish-modal :refer (story-publish-modal)]
            [goog.dom :as gdom]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

;; Update story

(def default-story-title "Untitled story")
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
      (reset! (::central-message s) "saving...")))))

(defn update-story-editing [s new-data]
  (dis/dispatch! [:input [:story-editing] (merge @(drv/get-ref s :story-editing) new-data {:has-changes true})])
  (story-autosave s))

;; Body change handling

(defn body-on-change [s body]
  (update-story-editing s {:body body}))

(defn- title-on-change [state]
  (when-let [title (sel1 [:div.story-edit-title])]
    (let [emojied-title   (utils/emoji-images-to-unicode (gobj/get (utils/emojify (.-innerHTML title)) "__html"))]
      (update-story-editing state {:title emojied-title}))))

(defn- setup-headline [state]
  (let [title-el  (rum/ref-node state "title")]
    (events/listen title-el EventType/INPUT #(title-on-change state))
    ;; Make sure the jss lib is loaded before calling it
    (utils/after 2500 #(js/emojiAutocomplete))))

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

(defn delete-clicked [e story-data]
  (utils/event-stop e)
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :message "Delete this journal entry?"
                    :link-button-title "No"
                    :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                    :solid-button-title "Yes"
                    :solid-button-cb #(do
                                        (dis/dispatch! [:activity-delete story-data])
                                        (dis/dispatch! [:alert-modal-hide]))
                    }]
    (dis/dispatch! [:alert-modal-show alert-data])))

(defn did-select-storyboard-cb [s storyboard]
  (let [story-data @(drv/get-ref s :story-editing)]
    (when (not= (:value storyboard) (:board-slug story-data))
      (update-story-editing s {:board-slug (:value storyboard) :storyboard-name (:label storyboard) :redirect true})
      (reset! (::show-storyboards-list s) false))))

(defn add-emoji-cb [s]
  (title-on-change s)
  (let [body (sel1 [:div.rich-body-editor])
        emojied-body (utils/emoji-images-to-unicode (gobj/get (utils/emojify (.-innerHTML body)) "__html"))]
    (body-on-change s emojied-body)))

(rum/defcs story-edit < rum/reactive
                        ;; Story edits
                        (drv/drv :story-editing)
                        (drv/drv :current-user-data)
                        (drv/drv :alert-modal)
                        (drv/drv :org-data)
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
                        (rum/local false ::media-picker-expanded)
                        ;; Storyboard tag
                        (rum/local nil ::show-storyboards-list)
                        ;; Publish dialog
                        (rum/local false ::show-publish-modal)
                        {:will-mount (fn [s]
                                       (utils/after 100 #(dis/dispatch! [:story-get]))
                                       (let [story-editing @(drv/get-ref s :story-editing)]
                                         (when (:uuid story-editing)
                                           (reset! (::initial-title s) (:title story-editing))
                                           (reset! (::initial-body s) (:body story-editing))
                                           (reset! (::activity-uuid s) (:uuid story-editing))))
                                       s)
                         :did-mount (fn [s]
                                      (setup-headline s)
                                      (.focus (rum/ref-node s "title"))
                                      s)
                         :did-remount (fn [o s]
                                        (when-let [story-editing @(drv/get-ref s :story-editing)]
                                          ;; Replace title and body only if the story wasn't loaded yet
                                          (when (nil? @(::activity-uuid s))
                                            (reset! (::initial-title s) (:title story-editing))
                                            (reset! (::initial-body s) (:body story-editing))
                                            (reset! (::activity-uuid s) (:uuid story-editing)))
                                          ;; If it's saving and there is no autosaving key in the story-editing it means saving ended
                                          (when (and (= @(::central-message s) "saving...")
                                                     (not (:autosaving story-editing)))
                                            (reset! (::central-message s) "saved")
                                            (reset! (::last-timeout s) nil)))
                                        s)
                         :after-render (fn [s]
                                         (doto (js/$ "[data-toggle=\"tooltip\"]")
                                           (.tooltip "fixTitle")
                                           (.tooltip "hide"))
                                         s)
                         :will-unmount (fn [s]
                                        (utils/after 100 #(dis/dispatch! [:input [:story-editing] nil]))
                                        (when @(::body-editor s)
                                          (.destroy @(::body-editor s))
                                          (reset! (::body-editor s) nil))
                                        s)}
  [s]
  (let [story-data (drv/react s :story-editing)
        story-author (if (:author story-data)
                       (if (map? (:author story-data))
                         (:author story-data)
                         (first (:author story-data)))
                       (drv/react s :current-user-data))
        ww (min (responsive/ww) 840)]
    [:div.story-edit-container
      (when (drv/react s :alert-modal)
        (alert-modal))
      (when (:media-video story-data)
        (media-video-modal :story-editing))
      (when (:media-chart story-data)
        (media-chart-modal :story-editing))
      (when @(::show-publish-modal s)
        (story-publish-modal story-data #(reset! (::show-publish-modal s) (not @(::show-publish-modal s)))))
      [:div.story-edit-header.group
        [:div.story-edit-header-left
          [:div.story-edit-header-back
            {:on-click #(router/history-back!)}
            [:span.back-arrow "<"]
            "Back"]]
        [:div.story-edit-header-center
          (let [title (if (empty? (:title story-data)) default-story-title (gobj/get (utils/emojify (:title story-data)) "__html"))
                with-save (str title "<span class=\"saving-message\">" @(::central-message s) "</span>")]
            [:div.story-edit-header-center-title
              {:dangerouslySetInnerHTML #js {"__html" with-save}}])]
        [:div.story-edit-header-right
          [:button.mlb-reset.mlb-link-black.delete-link
            {:on-click #(delete-clicked % story-data)}
            "Delete"]
          (when (= "draft" (:status story-data))
            [:button.mlb-reset.mlb-default.post-button
              {:on-click #(reset! (::show-publish-modal s) true)}
              "Post"])]]
      [:div.story-edit-content
        [:div.story-edit-content-authorship.group
          [:div.story-edit-content-authorship-left.group
            (user-avatar-image story-author)
            [:div.name (or (:name story-author) (str (:first-name story-author) " " (:last-name story-author)))]
            [:div.time-since
              (if (:published-at story-data)
                [:time
                  {:date-time (:published-at story-data)
                   :title (utils/activity-date-tooltip story-data)}
                  (utils/time-since (:published-at story-data))]
                "Draft")]]
            [:div.story-edit-content-authorship-right.group
              [:div.story-edit-tags
                [:div.activity-tag.storyboard-tag
                  {:on-click #(reset! (::show-storyboards-list s) (not @(::show-storyboards-list s)))}
                  (:storyboard-name story-data)]
                (when @(::show-storyboards-list s)
                  (let [org-data (drv/react s :org-data)
                        storyboards (filter #(and (= (:type %) "story") (not= (:slug %) "drafts")) (:boards org-data))
                        storyboards-list (map #(select-keys % [:name :slug :links]) storyboards)
                        fixed-storyboards (vec (map #(clojure.set/rename-keys % {:name :label :slug :value :links :links}) storyboards-list))]
                    (dropdown-list {:items fixed-storyboards
                                    :value (:board-slug story-data)
                                    :on-change (partial did-select-storyboard-cb s)
                                    :on-blur #(reset! (::show-storyboards-list s) false)})))]
              (when-not (:banner-url story-data)
                [:div.story-edit-add-banner
                  {:on-click (fn []
                               (iu/upload! {:accept "image/*" ; :imageMin [840 200]
                                            :transformations {
                                              :crop {
                                                :aspectRatio (/ ww 520)}}}
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
                  "+ Add cover image"])]]
        [:div.story-edit-banner
          {:style #js {:backgroundImage (str "url(" (:banner-url story-data) ")")
                       :marginLeft (str "-" (/ (- ww 780) 2) "px")
                       :width (str ww "px")
                       :height (str (if (pos? (:banner-height story-data)) (min 520 (* (/ (:banner-height story-data) (:banner-width story-data)) ww)) "1") "px")}}
          (when (:banner-url story-data)
            [:button.mlb-reset.mlb-default.remove-banner
              {:on-click #(update-story-editing s {:banner-url nil :banner-width 0 :banner-height 0})}
              "Remove Image"])]
        [:div.story-edit-title.emoji-autocomplete.emojiable
          {:content-editable true
           :ref "title"
           :placeholder default-story-title
           :on-paste    #(title-on-paste s %)
           :on-key-Up   #(title-on-change s)
           :on-key-down #(title-on-change s)
           :on-focus    #(title-on-change s)
           :on-blur     #(title-on-change s)
           :dangerouslySetInnerHTML (utils/emojify @(::initial-title s))}]
        (rich-body-editor {:on-change (partial body-on-change s)
                           :initial-body @(::initial-body s)
                           :dispatch-input-key :story-editing
                           :media-config ["photo" "video" "chart" "attachment" "divider-line"]
                           :classes "emoji-autocomplete emojiable"})]
      [:div.story-edit-footer.group
        [:div.story-edit-footer-inner
          (emoji-picker {:add-emoji-cb (partial add-emoji-cb s)})]]]))