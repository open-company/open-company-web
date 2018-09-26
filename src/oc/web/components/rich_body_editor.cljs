(ns oc.web.components.rich-body-editor
  (:require [rum.core :as rum]
            [dommy.core :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as string]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [oc.web.lib.image-upload :as iu]
            [oc.web.lib.responsive :as responsive]
            [oc.web.utils.mention :as mention-utils]
            [oc.web.mixins.mention :as mention-mixins]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.multi-picker :refer (multi-picker)]
            [cljsjs.medium-editor]
            [goog.dom :as gdom]
            [goog.Uri :as guri]
            [goog.object :as gobj]
            [clojure.contrib.humanize :refer (filesize)]))

;; Attachment

(defn media-attachment-dismiss-picker
  "Called every time the image picke close, reset to inital state."
  [s editable]
  (when-not @(::media-attachment-did-success s)
    (reset! (::media-attachment s) false)))

(defn attachment-upload-failed-cb [state editable]
  (let [alert-data {:icon "/img/ML/error_icon.png"
                    :action "attachment-upload-error"
                    :title "Sorry!"
                    :message "An error occurred with your file."
                    :solid-button-title "OK"
                    :solid-button-cb #(alert-modal/hide-alert)}]
    (alert-modal/show-alert alert-data)
    (utils/after 10 #(do
                       (reset! (::media-attachment-did-success state) false)
                       (media-attachment-dismiss-picker state editable)))))

(defn attachment-upload-success-cb [state editable res]
  (reset! (::media-attachment-did-success state) true)
  (let [url (gobj/get res "url")]
    (if-not url
      (attachment-upload-failed-cb state editable)
      (let [size (gobj/get res "size")
            mimetype (gobj/get res "mimetype")
            filename (gobj/get res "filename")
            createdat (utils/js-date)
            prefix (str "Uploaded by " (jwt/get-key :name) " on " (utils/date-string createdat [:year]) " - ")
            subtitle (str prefix (filesize size :binary false :format "%.2f" ))
            icon (au/icon-for-mimetype mimetype)
            attachment-data {:file-name filename
                             :file-type mimetype
                             :file-size size
                             :file-url url}
            dispatch-input-key (:dispatch-input-key (first (:rum/args state)))]
        (reset! (::media-attachment state) false)
        (activity-actions/add-attachment dispatch-input-key attachment-data)
        (utils/after 1000 #(reset! (::media-attachment-did-success state) false))))))

(defn attachment-upload-error-cb [state editable res error]
  (attachment-upload-failed-cb state editable))

(defn get-media-picker-extension [s]
  (let [body-el (rum/ref-node s "body")
        editor (js/MediumEditor.getEditorFromElement body-el)
        media-picker-ext (.getExtensionByName editor "media-picker")]
    media-picker-ext))

(defn add-attachment [s editable]
  (let [editable (or editable (get-media-picker-extension s))]
    (.saveSelection editable)
    (reset! (::media-attachment s) true)
    (iu/upload!
     nil
     (partial attachment-upload-success-cb s editable)
     nil
     (partial attachment-upload-error-cb s editable)
     (fn []
       (utils/after 400 #(media-attachment-dismiss-picker s editable))))))

;; Video

(defn add-video [s editable]
  (let [editable (or editable (get-media-picker-extension s))]
    (.saveSelection editable))
  (dis/dispatch! [:input [:media-input :media-video] true])
  (reset! (::media-video s) true))

(defn get-video-thumbnail [video]
  (cond
    (= (:type video) :youtube)
    (str "https://img.youtube.com/vi/" (:id video) "/0.jpg")
    (= (:type video) :vimeo)
    (:thumbnail video)))

(defn get-video-src [video]
  (cond
   (= (:type video) :youtube)
   (str "https://www.youtube.com/embed/" (:id video))
   (= (:type video) :vimeo)
   (str "https://player.vimeo.com/video/" (:id video))))

(defn media-video-add [s editable video-data]
  (if (nil? video-data)
    (.addVideo editable nil nil nil nil)
    (.addVideo
     editable
     (get-video-src video-data)
     (name (:type video-data))
     (:id video-data)
     (get-video-thumbnail video-data))))

;; Photo

(defn media-photo-add-error
  "Show an error alert view for failed uploads."
  [s]
  (let [alert-data {:icon "/img/ML/error_icon.png"
                    :action "media-photo-upload-error"
                    :title "Sorry!"
                    :message "An error occurred with your image."
                    :solid-button-title "OK"
                    :solid-button-cb #(alert-modal/hide-alert)}
        upload-progress-cb (:upload-progress-cb (first (:rum/args s)))]
    (upload-progress-cb false)
    (reset! (::upload-lock s) false)
    (alert-modal/show-alert alert-data)))

(defn media-photo-add-if-finished [s editable]
  (let [image @(::media-photo s)
        upload-progress-cb (:upload-progress-cb (first (:rum/args s)))]
    (when (and (contains? image :url)
               (contains? image :width)
               (contains? image :height)
               (contains? image :thumbnail))
      (.addPhoto editable (:url image) (:thumbnail image) (:width image) (:height image))
      (reset! (::media-photo s) nil)
      (reset! (::media-photo-did-success s) false)
      (reset! (::upload-lock s) false)
      (upload-progress-cb false))))

(defn img-on-load [s editable url img]
  (if (and url img)
    (do
      (reset! (::media-photo s) (merge @(::media-photo s) {:width (.-width img) :height (.-height img)}))
      (gdom/removeNode img)
      (media-photo-add-if-finished s editable))
    (media-photo-add-error s)))

(defn media-photo-dismiss-picker
  "Called every time the image picke close, reset to inital state."
  [s editable]
  (when-not @(::media-photo-did-success s)
    (reset! (::media-photo s) false)
    (.addPhoto editable nil nil nil nil)))

(defn add-photo [s editable]
  (let [editable (or editable (get-media-picker-extension s))]
    (.saveSelection editable)
    (reset! (::media-photo s) true)
    (let [props (first (:rum/args s))
          upload-progress-cb (:upload-progress-cb props)]
      (iu/upload! {:accept "image/*"}
       ;; success-cb
       (fn [res]
         (reset! (::media-photo-did-success s) true)
         (let [url (gobj/get res "url")
               img   (gdom/createDom "img")]
           (set! (.-onload img) #(img-on-load s editable url img))
           (set! (.-onerror img) #(img-on-load s editable nil nil))
           (set! (.-className img) "hidden")
           (gdom/append (.-body js/document) img)
           (set! (.-src img) url)
           (reset! (::media-photo s) {:res res :url url})
           ;; if the image is a vector image
           (if (or (= (string/lower (gobj/get res "mimetype")) "image/svg+xml")
                   (string/ends-with? (string/lower (gobj/get res "filename")) ".svg"))
             ;l use the same url for the thumbnail since the size doesn't matter
             (do
               (reset! (::media-photo s) (assoc @(::media-photo s) :thumbnail url))
               (media-photo-add-if-finished s editable))
             ;; else create the thumbnail
             (iu/thumbnail! url
              (fn [thumbnail-url]
                (reset! (::media-photo s) (assoc @(::media-photo s) :thumbnail thumbnail-url))
                (media-photo-add-if-finished s editable))
              (fn [res progress])
              (fn [res err]
                (media-photo-add-error s))))))
       ;; progress-cb
       (fn [res progress])
       ;; error-cb
       (fn [err]
         (media-photo-add-error s))
       ;; close-cb
       (fn []
         ;; Delay the check because this is called on cancel but also on success
         (utils/after 1000 #(media-photo-dismiss-picker s editable)))
       ;; finished-cb
       (fn [res])
       ;; selected-cb
       (fn [res]
         (reset! (::upload-lock s) true)
         (upload-progress-cb true))
       ;; started-cb
       (fn [res]
         (reset! (::upload-lock s) true)
         (upload-progress-cb true))))))

;; Picker cb

(defn on-picker-click [s editable type]
  (cond
    (= type "photo")
    (add-photo s editable)
    (= type "video")
    (add-video s editable)
    (= type "attachment")
    (add-attachment s editable)))

(defn body-on-change [state]
  (when-not @(::did-change state)
    (reset! (::did-change state) true))
  (let [options (first (:rum/args state))
        on-change (:on-change options)]
    (on-change)))

(def default-mutli-picker-button-id "entry-edit-multi-picker-bt")

(defn- file-dnd-handler [s editor-ext file]
  (if (< (gobj/get file "size") (* 5 1000 1000))
    (if (.match (.-type file) "image")
      (iu/upload-file! file
        (fn [url]
          (.insertImageFile editor-ext file url nil)
          (utils/after 500 #(utils/to-end-of-content-editable (rum/ref-node s "body")))))
      (iu/upload-file! file
        (fn [url]
          (let [size (gobj/get file "size")
                mimetype (gobj/get file "type")
                filename (gobj/get file "name")
                createdat (utils/js-date)
                prefix (str "Uploaded by " (jwt/get-key :name) " on " (utils/date-string createdat [:year]) " - ")
                subtitle (str prefix (filesize size :binary false :format "%.2f" ))
                icon (au/icon-for-mimetype mimetype)
                attachment-data {:file-name filename
                                 :file-type mimetype
                                 :file-size size
                                 :file-url url}
                dispatch-input-key (:dispatch-input-key (first (:rum/args s)))]
            (activity-actions/add-attachment dispatch-input-key attachment-data)))))
    (let [alert-data {:icon "/img/ML/error_icon.png"
                    :action "dnd-file-too-big"
                    :title "Sorry!"
                    :message "Error, please use files smaller than 5MB."
                    :solid-button-title "OK"
                    :solid-button-cb #(alert-modal/hide-alert)}]
      (alert-modal/show-alert alert-data))))

(defn- setup-editor [s]
  (let [options (first (:rum/args s))
        mobile-editor (responsive/is-tablet-or-mobile?)
        show-subtitle (:show-h2 options)
        media-config (:media-config options)
        placeholder (or (:placeholder options) "What would you like to share?")
        body-el (rum/ref-node s "body")
        media-picker-opts {:buttons (clj->js media-config)
                           :useInlinePlusButton (:use-inline-media-picker options)
                           :saveSelectionClickElementId default-mutli-picker-button-id
                           :delegateMethods #js {:onPickerClick (partial on-picker-click s)
                                                 :willExpand #(reset! (::did-change s) true)}}
        media-picker-ext (when-not mobile-editor (js/MediaPicker. (clj->js media-picker-opts)))
        file-dragging-ext (when-not mobile-editor (js/CarrotFileDragging. (clj->js {:uploadHandler (partial file-dnd-handler s)})))
        buttons (if show-subtitle
                  ["bold" "italic" "unorderedlist" "anchor" "h2"]
                  ["bold" "italic" "unorderedlist" "anchor"])
        users-list (:mention-users @(drv/get-ref s :team-roster))
        extensions (if mobile-editor
                      #js {"autolist" (js/AutoList.)
                           "mention" (mention-utils/mention-ext users-list)}
                      #js {"autolist" (js/AutoList.)
                           "mention" (mention-utils/mention-ext users-list)
                           "media-picker" media-picker-ext
                           "fileDragging" false
                           "carrotFileDragging" file-dragging-ext})
        options {:toolbar (if mobile-editor false #js {:buttons (clj->js buttons)})
                 :buttonLabels "fontawesome"
                 :anchorPreview (if mobile-editor false #js {:hideDelay 500, :previewValueSelector "a"})
                 :extensions extensions
                 :imageDragging false
                 :targetBlank true
                 :autoLink true
                 :anchor #js {:customClassOption nil
                              :customClassOptionText "Button"
                              :linkValidation true
                              :placeholderText "Paste or type a link"
                              :targetCheckbox false
                              :targetCheckboxText "Open in new window"}
                 :paste #js {:forcePlainText false
                             :cleanPastedHTML true
                             :cleanAttrs #js ["style" "alt" "dir" "size" "face" "color" "itemprop" "name" "id"]
                             :cleanTags #js ["meta" "video" "audio" "img" "button" "svg" "canvas" "figure" "input" "textarea"]
                             :unwrapTags (clj->js (remove nil? ["div" "label" "font" "h1"
                                                   (when-not show-subtitle "h2") "h3" "h4" "h5"
                                                   "h6" "strong" "section" "time" "em" "main" "u" "form" "header" "footer"
                                                   "details" "summary" "nav" "abbr"]))}
                 :placeholder #js {:text placeholder
                                   :hideOnClick true}
                 :keyboardCommands #js {:commands #js [
                                    #js {
                                      :command "bold"
                                      :key "B"
                                      :meta true
                                      :shift false
                                      :alt false
                                    }
                                    #js {
                                      :command "italic"
                                      :key "I"
                                      :meta true
                                      :shift false
                                      :alt false
                                    }
                                    #js {
                                      :command false
                                      :key "U"
                                      :meta true
                                      :shift false
                                      :alt false
                                    }]}}
        body-editor  (new js/MediumEditor body-el (clj->js options))]
    (reset! (::media-picker-ext s) media-picker-ext)
    (.subscribe body-editor
                "editableInput"
                (fn [event editable]
                  (body-on-change s)))
    (reset! (::editor s) body-editor)))

(defn toggle-menu-cb [s show?]
  (when-let [editable @(::editor s)]
    (if show?
      (.saveSelection editable)
      (.removeSelection editable))))

(rum/defcs rich-body-editor  < rum/reactive
                               (rum/local false ::did-change)
                               (rum/local nil ::editor)
                               (rum/local nil ::media-picker-ext)
                               (rum/local false ::media-photo)
                               (rum/local false ::media-video)
                               (rum/local false ::media-attachment)
                               (rum/local false ::media-photo-did-success)
                               (rum/local false ::media-attachment-did-success)
                               ;; Image upload lock
                               (rum/local false ::upload-lock)
                               (drv/drv :media-input)
                               (drv/drv :team-roster)
                               (mention-mixins/oc-mentions-hover)
                               {:did-mount (fn [s]
                                 (let [props (first (:rum/args s))]
                                   (when-not (:nux props)
                                     (utils/after 300 #(do
                                      (setup-editor s)
                                      (let [classes (:classes (first (:rum/args s)))]
                                        (when (string/includes? classes "emoji-autocomplete")
                                          (js/emojiAutocomplete)))))))
                                 s)
                                :will-update (fn [s]
                                 (let [data @(drv/get-ref s :media-input)
                                       video-data (:media-video data)]
                                    (when (and @(::media-video s)
                                               (or (= video-data :dismiss)
                                                   (map? video-data)))
                                      (when (or (= video-data :dismiss)
                                                (map? video-data))
                                        (reset! (::media-video s) false)
                                        (dis/dispatch! [:input [:media-input :media-video] nil]))
                                      (if (map? video-data)
                                        (media-video-add s @(::media-picker-ext s) video-data)
                                        (media-video-add s @(::media-picker-ext s) nil))))
                                 s)
                                :will-unmount (fn [s]
                                 (when @(::editor s)
                                   (.destroy @(::editor s)))
                                 s)}
  [s {:keys [initial-body
             nux
             on-change
             classes
             show-placeholder
             upload-progress-cb
             multi-picker-container-selector
             dispatch-input-key]}]
  [:div.rich-body-editor-container
    (when multi-picker-container-selector
      (when-let [multi-picker-container (.querySelector js/document multi-picker-container-selector)]
        (rum/portal
         (multi-picker
          {:toggle-button-id default-mutli-picker-button-id
           :add-photo-cb #(add-photo s nil)
           :add-video-cb #(add-video s nil)
           :add-attachment-cb #(add-attachment s nil)})
         multi-picker-container)))
    [:div.rich-body-editor.oc-mentions.oc-mentions-hover
      {:ref "body"
       :content-editable (not nux)
       :class (str classes
               (utils/class-set {:medium-editor-placeholder-hidden (or (not show-placeholder) @(::did-change s))
                                 :uploading @(::upload-lock s)}))
       :dangerouslySetInnerHTML (utils/emojify initial-body)}]])