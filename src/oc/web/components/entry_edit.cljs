(ns oc.web.components.entry-edit
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [cuerdas.core :as s]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.medium-editor-exts :as editor]
            [oc.web.lib.image-upload :as iu]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [cljsjs.medium-editor]
            [cljsjs.rangy-selectionsaverestore]
            [goog.object :as gobj]
            [goog.dom :as gdom]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn dismiss-modal [saving?]
  (dis/dispatch! [:entry-edit/dismiss])
  (when-not saving?
    (dis/dispatch! [:input [:entry-editing] nil])))

(defn close-clicked [s & [saving?]]
  (reset! (::dismiss s) true)
  (utils/after 180 #(dismiss-modal saving?)))

(defn unique-slug [topics topic-name]
  (let [slug (atom (s/slug topic-name))]
    (while (seq (filter #(= (:slug %) @slug) topics))
      (reset! slug (str (s/slug topic-name) "-" (int (rand 1000)))))
    @slug))

(defn toggle-topics-dd []
  (.dropdown (js/$ "div.entry-card-dd-container button.dropdown-toggle") "toggle"))

(defn body-on-change [state]
  (when-let [body-el (sel1 [:div.entry-edit-body])]
    ; Attach paste listener to the body and all its children
    (js/recursiveAttachPasteListener body-el (comp #(utils/medium-editor-hide-placeholder @(::body-editor state) body-el) #(body-on-change state)))
    (let [emojied-body (utils/emoji-images-to-unicode (gobj/get (utils/emojify (.-innerHTML body-el)) "__html"))]
      (dis/dispatch! [:input [:entry-editing :body] emojied-body])
      (dis/dispatch! [:input [:entry-editing :has-changes] true]))))

(defn- headline-on-change [state]
  (when-let [headline (sel1 [:div.entry-edit-headline])]
    (let [emojied-headline   (utils/emoji-images-to-unicode (gobj/get (utils/emojify (.-innerHTML headline)) "__html"))]
      (dis/dispatch! [:input [:entry-editing :headline] emojied-headline])
      (dis/dispatch! [:input [:entry-editing :has-changes] true]))))

(defn body-placeholder []
  (let [first-name (jwt/get-key :first-name)]
    (if-not (empty? first-name)
      (str "What's new, " first-name "?")
      "What's new?")))

(defn- setup-body-editor [state]
  (let [headline-el  (sel1 [:div.entry-edit-headline])
        body-el      (sel1 [:div.entry-edit-body])
        body-editor  (new js/MediumEditor body-el (clj->js (utils/medium-editor-options (body-placeholder) false)))]
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
        headline-el     (.querySelector js/document "div.entry-edit-headline")]
    ; replace the selected text of headline with the text/plain data of the clipboard
    (js/replaceSelectedText pasted-data)
    ; call the headline-on-change to check for content length
    (headline-on-change state)
    ; move cursor at the end
    (utils/to-end-of-content-editable headline-el)))

(defn media-image-add-if-finished [s]
  (let [image @(::media-photo s)]
    (when (and (contains? image :url)
               (contains? image :width)
               (contains? image :height)
               (contains? image :thumbnail))
      (.restoreSelection js/rangy @(::last-selection s))
      (let [image-html (str "<img class=\"carrot-no-preview\" src=\"" (:url image) "\" data-thumbnail=\"" (:thumbnail image) "\" data-width=\"" (:width image) "\" data-height=\"" (:height image) "\" /><br/><br/>")]
        (js/pasteHtmlAtCaret image-html (.getSelection js/rangy js/window) false))
      (reset! (::last-selection s) nil)
      (reset! (::media-photo s) nil)
      (body-on-change s)
      (utils/to-end-of-content-editable (sel1 [:div.entry-edit-body]))
      (utils/scroll-to-bottom (sel1 [:div.entry-edit-modal-container]) true))))

(defn img-on-load [s url img]
  (reset! (::media-photo s) (merge @(::media-photo s) {:width (.-width img) :height (.-height img)}))
  (gdom/removeNode img)
  (media-image-add-if-finished s))

(defn create-new-topic [s]
  (when-not (empty? @(::new-topic s))
    (let [topics (:topics @(drv/get-ref s :board-data))
          topic-name (s/trim @(::new-topic s))
          topic-slug (unique-slug topics topic-name)]
      (dis/dispatch! [:topic-add {:name topic-name :slug topic-slug} true])
      (reset! (::new-topic s) ""))))

(defn get-video-html [video]
  (cond
    (= (:type video) :youtube)
    (str "<iframe class=\"carrot-no-preview\" width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/" (:id video) "\" frameborder=\"0\" allowfullscreen></iframe><br/><br/>")
    (= (:type video) :vimeo)
    (str "<iframe class=\"carrot-no-preview\" src=\"https://player.vimeo.com/video/" (:id video) "\" width=\"560\" height=\"315\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe><br/><br/>")))

(defn media-video-add [s video-data]
  (js/console.log "media-video-add:" video-data)
  (let [video-html (get-video-html video-data)]
    (js/console.log "media-video-add:" video-html)
    (when video-html
      (.restoreSelection js/rangy @(::last-selection s))
      (js/pasteHtmlAtCaret video-html (.getSelection js/rangy js/window) false)
      (reset! (::last-selection s) nil)
      (reset! (::media-video s) false)
      (body-on-change s)
      (utils/after 100
        #(do
           (utils/to-end-of-content-editable (sel1 [:div.entry-edit-body]))
           (utils/scroll-to-bottom (sel1 [:div.entry-edit-modal-container]) true))))))

(rum/defcs entry-edit < rum/reactive
                        (drv/drv :board-data)
                        (drv/drv :current-user-data)
                        (drv/drv :entry-editing)
                        (drv/drv :board-filters)
                        (rum/local false ::first-render-done)
                        (rum/local false ::dismiss)
                        (rum/local nil ::body-editor)
                        (rum/local false ::body-focused)
                        (rum/local "" ::initial-body)
                        (rum/local "" ::initial-headline)
                        (rum/local "" ::new-topic)
                        (rum/local false ::focusing-create-topic)
                        (rum/local false ::media-expanded)
                        (rum/local false ::media-photo)
                        (rum/local false ::media-video)
                        (rum/local false ::media-chart)
                        (rum/local nil ::window-click-listener)
                        (rum/local nil ::last-selection)
                        {:will-mount (fn [s]
                                       (let [entry-editing @(drv/get-ref s :entry-editing)
                                             board-filters @(drv/get-ref s :board-filters)
                                             initial-body (utils/emojify (if (contains? entry-editing :links) (:body entry-editing) ""))
                                             initial-headline (utils/emojify (if (contains? entry-editing :links) (:headline entry-editing) ""))]
                                         (reset! (::initial-body s) initial-body)
                                         (reset! (::initial-headline s) initial-headline)
                                         (when (and (string? board-filters)
                                                    (nil? (:topic-slug entry-editing)))
                                            (let [topics (:topics @(drv/get-ref s :board-data))
                                                  topic (first (filter #(= (:slug %) board-filters) topics))]
                                              (when topic
                                                (dis/dispatch! [:input [:entry-editing :topic-slug] (:slug topic)])
                                                (dis/dispatch! [:input [:entry-editing :topic-name] (:name topic)])))))
                                       s)
                         :did-mount (fn [s]
                                      ;; Add no-scroll to the body to avoid scrolling while showing this modal
                                      (dommy/add-class! (sel1 [:body]) :no-scroll)
                                      (setup-body-editor s)
                                      (reset! (::window-click-listener s)
                                       (events/listen js/window EventType/CLICK
                                        #(when-not (utils/event-inside? % (sel1 [:div.entry-edit-controls-medias-container]))
                                           (when-not (utils/event-inside? % (sel1 [:div.entry-edit-body]))
                                              (reset! (::body-focused s) false))
                                           (reset! (::media-expanded s) false)
                                           ; If there was a last selection saved
                                           (when (and (not @(::media-photo s))
                                                      (not @(::media-video s))
                                                      (not @(::media-chart s))
                                                      @(::last-selection s))
                                             ; remove the markers
                                             (.removeMarkers js/rangy @(::last-selection s))
                                             (reset! (::last-selection s) nil)))))
                                      (utils/to-end-of-content-editable (sel1 [:div.entry-edit-body]))
                                      s)
                         :after-render (fn [s]
                                         (when (not @(::first-render-done s))
                                           (reset! (::first-render-done s) true))
                                         s)
                         :did-remount (fn [o s]
                                        (let [entry-editing @(drv/get-ref s :entry-editing)]
                                          (js/console.log "entry-edit did-remount" (:temp-video entry-editing))
                                          (when (map? (:temp-video entry-editing))
                                            (dis/dispatch! [:input [:entry-editing :temp-video] nil])
                                            (media-video-add s (:temp-video entry-editing))))
                                        s)
                         :will-unmount (fn [s]
                                         ;; Remove no-scroll class from the body tag
                                         (dommy/remove-class! (sel1 [:body]) :no-scroll)
                                         (events/unlistenByKey @(::window-click-listener s))
                                         s)}
  [s]
  (let [board-data (drv/react s :board-data)
        topics (:topics board-data)
        current-user-data (drv/react s :current-user-data)
        entry-editing (drv/react s :entry-editing)
        new-entry? (empty? (:uuid entry-editing))]
    [:div.entry-edit-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(::first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(::first-render-done s))})
       :on-click #(when (and (empty? (:body entry-editing))
                             (empty? (:headline entry-editing))
                             (not (utils/event-inside? % (sel1 [:div.entry-edit-modal]))))
                    (close-clicked s))}
      [:div.entry-edit-modal.group
        [:div.entry-edit-modal-header.group
          (user-avatar-image current-user-data)
          [:div.posting-in (if new-entry? "Posting" "Posted") " in " [:span (:name board-data)]]
          [:div.arrow " · "]
          [:div.entry-card-dd-container
            [:button.mlb-reset.dropdown-toggle
              {:class (when-not (:topic-name entry-editing) "select-a-topic")
               :type "button"
               :id "entry-edit-dd-btn"
               :data-toggle "dropdown"
               :aria-haspopup true
               :aria-expanded false}
              (if (:topic-name entry-editing) (:topic-name entry-editing) "Add a topic")
              [:i.fa.fa-caret-down]]
            [:div.entry-edit-topics-dd.dropdown-menu
              {:aria-labelledby "entry-edit-dd-btn"}
              [:div.triangle]
              [:div.entry-dropdown-list-content
                [:ul
                  (for [t (sort #(compare (:name %1) (:name %2)) topics)
                        :let [selected (= (:topic-name entry-editing) (:name t))]]
                    [:li.group
                      {:key (str "entry-edit-dd-" (:slug t))}
                      [:button.mlb-reset.selectable
                        {:data-topic-slug (:slug t)
                         :on-click #(dis/dispatch! [:input [:entry-editing :topic-name] (:name t)])
                         :class (when selected "select")}
                        (:name t)]
                      (when selected
                        [:button.mlb-reset.mlb-link.remove
                          {:on-click (fn [e]
                                       (utils/event-stop e)
                                       (dis/dispatch! [:input [:entry-editing :topic-slug] nil])
                                       (dis/dispatch! [:input [:entry-editing :topic-name] nil]))}
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
                                     (when @(::last-selection s)
                                        (.removeMarkers js/rangy @(::last-selection s))
                                        (reset! (::last-selection s) nil))
                                     (utils/event-stop e)
                                     (create-new-topic s))
                         :disabled (empty? (s/trim @(::new-topic s)))}
                        "Create"])]]]]]]
      [:div.entry-edit-modal-divider]
      [:div.entry-edit-modal-body
        ; Headline element
        [:div.entry-edit-headline.emoji-autocomplete.emojiable
          {:content-editable true
           :placeholder "Title this (if you like)"
           :on-paste    #(headline-on-paste s %)
           :on-key-Up   #(headline-on-change s)
           :on-key-down #(headline-on-change s)
           :on-focus    #(headline-on-change s)
           :on-blur     #(headline-on-change s)
           :dangerouslySetInnerHTML @(::initial-headline s)}]
        ; Body element
        [:div.entry-edit-body.emoji-autocomplete.emojiable
          {:role "textbox"
           :aria-multiline true
           :contentEditable true
           :on-focus #(reset! (::body-focused s) true)
           :dangerouslySetInnerHTML @(::initial-body s)}]
        ; Bottom controls
        [:div.entry-edit-controls.group
          ; Media handling
          [:div.entry-edit-controls-medias
            ; Add media button
            [:button.mlb-reset.media.add-media-bt
              {:title "Insert media"
               :class (utils/class-set {:expanded @(::media-expanded s)
                                        :disabled (not @(::body-focused s))})
               :data-toggle "tooltip"
               :data-placement "top"
               :data-container "body"
               :on-click (fn [e]
                           (when @(::body-focused s)
                             (utils/event-stop e)
                             (reset! (::last-selection s) (.saveSelection js/rangy js/window))
                             (reset! (::media-expanded s) (not @(::media-expanded s)))
                             (utils/after 1 #(utils/remove-tooltips))
                             (utils/after 100 #(.tooltip (js/$ "[data-toggle=\"tooltip\"]")))))}]

            [:div.entry-edit-controls-medias-container
              {:class (when @(::media-expanded s) "expanded")}
              ; Add a picture button
              [:button.mlb-reset.media.media-photo
                {:class (when @(::media-photo s) "active")
                 :title "Add a picture"
                 :data-toggle "tooltip"
                 :data-placement "top"
                 :data-container "body"
                 :on-click (fn []
                             (reset! (::media-photo s) true)
                             (iu/upload! "image/*"
                              (fn [res]
                                (let [url (gobj/get res "url")
                                      img   (gdom/createDom "img")]
                                  (set! (.-onload img) #(img-on-load s url img))
                                  (set! (.-className img) "hidden")
                                  (gdom/append (.-body js/document) img)
                                  (set! (.-src img) url)
                                  (reset! (::media-photo s) {:res res :url url})
                                  (iu/thumbnail! url
                                   (fn [thumbnail-url]
                                    (reset! (::media-photo s) (assoc @(::media-photo s) :thumbnail thumbnail-url))
                                    (media-image-add-if-finished s)))))
                              (fn [res prog])
                              (fn [err]
                                (js/console.log "err" err))))}]
              ; Add a video button
              [:button.mlb-reset.media.media-video
                {:class (when @(::media-video s) "active")
                 :data-toggle "tooltip"
                 :data-placement "top"
                 :data-container "body"
                 :title "Add a video"
                 :on-click (fn []
                             (reset! (::media-video s) true)
                             (dis/dispatch! [:input [:entry-editing :media-video] true]))}]
              ; Add a chart button
              [:button.mlb-reset.media.media-chart
                {:class (when @(::media-chart s) "active")
                 :title "Add a Google Sheet chart"
                 :data-toggle "tooltip"
                 :data-placement "top"
                 :data-container "body"
                 :on-click (fn []
                             (reset! (::media-chart s) true))}]]]
          ; Emoji picker
          (emoji-picker {:add-emoji-cb (fn [editor emoji]
                                         (let [headline (sel1 [:div.entry-edit-headline])
                                               body     (sel1 [:div.entry-edit-body])]
                                           (when (= (.-activeElement js/document) headline)
                                             (headline-on-change s))
                                           (when (= (.-activeElement js/document) body)
                                             (body-on-change s))))
                         :disabled (let [headline (sel1 [:div.entry-edit-headline])
                                         body     (sel1 [:div.entry-edit-body])]
                                     (not (or (= (.-activeElement js/document) headline)
                                              (= (.-activeElement js/document) body))))})]]
      [:div.entry-edit-modal-divider]
      [:div.entry-edit-modal-footer.group
        [:button.mlb-reset.mlb-default
          {:on-click #(do
                        (dis/dispatch! [:entry-save])
                        (close-clicked s))
           :disabled (not (:has-changes entry-editing))}
          (if new-entry? "Post" "Save")]
        [:button.mlb-reset.mlb-link-black
          {:on-click #(close-clicked s)}
          "Cancel"]]]]))