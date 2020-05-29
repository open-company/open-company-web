(ns oc.web.utils.medium-editor-media
  (:require [rum.core :as rum]
            [goog.dom :as gdom]
            [goog.Uri :as guri]
            [cljsjs.medium-editor]
            [cuerdas.core :as string]
            [cljsjs.react-giphy-selector]
            [oops.core :refer (oget)]
            [org.martinklepsch.derivatives :as drv]
            [clojure.contrib.humanize :refer (filesize)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.poll :as poll-utils]
            [oc.web.actions.poll :as poll-actions]
            [oc.web.utils.activity :as au]
            [oc.web.local-settings :as ls]
            [oc.web.lib.image-upload :as iu]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.utils.mention :as mention-utils]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]))

(defn get-media-picker-extension [s]
  (let [body-el (rum/ref-node s "editor-node")
        editor (js/MediumEditor.getEditorFromElement body-el)
        media-picker-ext (.getExtensionByName editor "media-picker")]
    media-picker-ext))

;; Polls

(defn add-poll [s options editable]
  (let [delay (if (:collapsed (:cmail-state @dis/app-state)) 500 0)]
    (utils/maybe-after delay #(do
     (when-not (:use-inline-media-picker options)
       (let [editable (or editable (get-media-picker-extension s))]
         (.saveSelection editable)))
       (let [poll-id (poll-utils/new-poll-id)
             dispatch-input-key (:dispatch-input-key options)]
         (.addPoll editable poll-id)
         (poll-actions/add-poll dispatch-input-key poll-id))))))

;; Gif handling

(defn add-gif [s editable]
  (reset! (:me/showing-gif-selector s) true))

(defn media-gif-add [s editable gif-data]
  (if (nil? gif-data)
    (.addGIF editable nil nil nil nil)
    (let [original (oget gif-data ["images" "original"])
          original-url (or (oget original "?url")
                           (oget original "?gif_url"))
          fixed-width-still (oget gif-data ["images" "fixed_width_still"])
          fixed-width-still-url (or (oget fixed-width-still "?url")
                                    (oget fixed-width-still "?gif_url"))
          original-width (oget original "width")
          original-height (oget original "height")]
      (.addGIF
       editable
       original-url
       fixed-width-still-url
       original-width
       original-height))))

;; Attachment

(defn media-attachment-dismiss-picker
  "Called every time the image picke close, reset to inital state."
  [s editable]
  (when-not @(:me/media-attachment-did-success s)
    (reset! (:me/media-attachment s) false)))

(defn attachment-upload-failed-cb [state editable]
  (let [alert-data {:icon "/img/ML/error_icon.png"
                    :action "attachment-upload-error"
                    :title "Sorry!"
                    :message "An error occurred with your file."
                    :solid-button-title "OK"
                    :solid-button-cb #(alert-modal/hide-alert)}]
    (alert-modal/show-alert alert-data)
    (utils/after 10 #(do
                       (reset! (:me/media-attachment-did-success state) false)
                       (media-attachment-dismiss-picker state editable)))))

(defn attachment-upload-success-cb [state options editable res]
  (reset! (:me/media-attachment-did-success state) true)
  (let [url (oget res :url)]
    (if-not url
      (attachment-upload-failed-cb state editable)
      (let [size (oget res :size)
            mimetype (oget res :mimetype)
            filename (oget res :filename)
            createdat (utils/js-date)
            prefix (str "Uploaded by " (jwt/get-key :name) " on " (utils/date-string createdat [:year]) " - ")
            subtitle (str prefix (filesize size :binary false :format "%.2f" ))
            icon (au/icon-for-mimetype mimetype)
            attachment-data {:file-name filename
                             :file-type mimetype
                             :file-size size
                             :file-url url}
            dispatch-input-key (:dispatch-input-key options)]
        (reset! (:me/media-attachment state) false)
        (activity-actions/add-attachment dispatch-input-key attachment-data)
        (utils/after 1000 #(reset! (:me/media-attachment-did-success state) false))))))

(defn attachment-upload-error-cb [state editable res error]
  (attachment-upload-failed-cb state editable))

(defn add-attachment [s options editable]
  (let [editable (or editable (get-media-picker-extension s))]
    (.saveSelection editable)
    (reset! (:me/media-attachment s) true)
    (iu/upload!
     nil
     (partial attachment-upload-success-cb s options editable)
     nil
     (partial attachment-upload-error-cb s editable)
     (fn []
       (utils/after 400 #(media-attachment-dismiss-picker s editable))))))

;; Video

(defn add-video [s options editable]
  (when-not (:use-inline-media-picker options)
    (let [editable (or editable (get-media-picker-extension s))]
      (.saveSelection editable)))
  (dis/dispatch! [:input [:media-input :media-video] true])
  (reset! (:me/media-video s) true)
  (when (:use-inline-media-picker options)
    (reset! (:me/showing-media-video-modal s) true)))

(defn get-video-thumbnail [video]
  (cond
   (= (:type video) :loom)
   (str "https://cdn.loom.com/sessions/thumbnails/" (:id video) "-00001.jpg")
   (= (:type video) :youtube)
   (str "https://img.youtube.com/vi/" (:id video) "/0.jpg")
   (= (:type video) :vimeo)
   (:thumbnail video)))

(defn get-video-src [video]
  (cond
   (= (:type video) :loom)
   (str "https://www.loom.com/embed/" (:id video))
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
  [s options]
  (let [alert-data {:icon "/img/ML/error_icon.png"
                    :action "media-photo-upload-error"
                    :title "Sorry!"
                    :message "An error occurred with your image."
                    :solid-button-title "OK"
                    :solid-button-cb #(alert-modal/hide-alert)}
        upload-progress-cb (:upload-progress-cb options)]
    (when (fn? upload-progress-cb)
      (upload-progress-cb false))
    (reset! (:me/upload-lock s) false)
    (alert-modal/show-alert alert-data)))

(defn media-photo-add-if-finished [s options editable]
  (let [image @(:me/media-photo s)
        upload-progress-cb (:upload-progress-cb options)]
    (when (and (contains? image :url)
               (contains? image :width)
               (contains? image :height)
               (contains? image :thumbnail))
      (.addPhoto editable (:url image) (:thumbnail image) (:width image) (:height image))
      (reset! (:me/media-photo s) nil)
      (reset! (:me/media-photo-did-success s) false)
      (reset! (:me/upload-lock s) false)
      (when (fn? upload-progress-cb)
        (upload-progress-cb false)))))

(defn img-on-load [s options editable url img]
  (if (and url img)
    (do
      (reset! (:me/media-photo s) (merge @(:me/media-photo s) {:width (.-width img) :height (.-height img)}))
      (gdom/removeNode img)
      (media-photo-add-if-finished s options editable))
    (media-photo-add-error s options)))

(defn media-photo-dismiss-picker
  "Called every time the image picke close, reset to inital state."
  [s editable]
  (when-not @(:me/media-photo-did-success s)
    (reset! (:me/media-photo s) false)
    (.addPhoto editable nil nil nil nil)))

(defn add-photo [s options editable]
  (let [editable (or editable (get-media-picker-extension s))]
    (.saveSelection editable)
    (reset! (:me/media-photo s) true)
    (let [upload-progress-cb (:upload-progress-cb options)]
      (iu/upload! {:accept "image/*"}
       ;; success-cb
       (fn [res]
         (reset! (:me/media-photo-did-success s) true)
         (let [url (oget res :url)
               img   (gdom/createDom "img")]
           (set! (.-onload img) #(img-on-load s options editable url img))
           (set! (.-onerror img) #(img-on-load s options editable nil nil))
           (set! (.-className img) "hidden")
           (gdom/append (.-body js/document) img)
           (set! (.-src img) url)
           (reset! (:me/media-photo s) {:res res :url url})
           ;; if the image is a vector image
           (if (or (= (string/lower (oget res :mimetype)) "image/svg+xml")
                   (string/ends-with? (string/lower (oget res :filename)) ".svg"))
             ;l use the same url for the thumbnail since the size doesn't matter
             (do
               (reset! (:me/media-photo s) (assoc @(:me/media-photo s) :thumbnail url))
               (media-photo-add-if-finished s options editable))
             ;; else create the thumbnail
             (iu/thumbnail! url
              (fn [thumbnail-url]
                (reset! (:me/media-photo s) (assoc @(:me/media-photo s) :thumbnail thumbnail-url))
                (media-photo-add-if-finished s options editable))
              (fn [res progress])
              (fn [res err]
                (media-photo-add-error s options))))))
       ;; progress-cb
       (fn [res progress])
       ;; error-cb
       (fn [err]
         (media-photo-add-error s options))
       ;; close-cb
       (fn []
         ;; Delay the check because this is called on cancel but also on success
         (utils/after 1000 #(media-photo-dismiss-picker s editable)))
       ;; finished-cb
       (fn [res])
       ;; selected-cb
       (fn [res]
         (reset! (:me/upload-lock s) true)
         (when (fn? upload-progress-cb)
           (upload-progress-cb true)))
       ;; started-cb
       (fn [res]
         (reset! (:me/upload-lock s) true)
         (when (fn? upload-progress-cb)
           (upload-progress-cb true)))))))

;; Picker cb

(defn on-picker-click [s options editable type]
  (cond
    (= type "poll")
    (add-poll s options editable)
    (= type "gif")
    (add-gif s editable)
    (= type "photo")
    (add-photo s options editable)
    (= type "video")
    (add-video s options editable)
    (= type "attachment")
    (add-attachment s options editable)))

;; DND

(defn file-dnd-handler [s options editor-ext file]
  (if (< (oget file :size) ls/file-upload-size)
    (if (:attachment-uploading @dis/app-state)
      (let [alert-data {:icon "/img/ML/error_icon.png"
                      :action "dnd-already-running"
                      :title "Sorry!"
                      :message "You are already uploading a file, wait until it finishes to add another."
                      :solid-button-title "OK"
                      :solid-button-cb #(alert-modal/hide-alert)}]
                  (alert-modal/show-alert alert-data))
      (let [cmail-state (:cmail-state @dis/app-state)]
        ;; If Quick Post is still collapsed expand it
        (when (:collapsed cmail-state)
          (cmail-actions/cmail-show (cmail-actions/get-board-for-edit) {:collapsed false
                                                                        :fullscreen false
                                                                        :key (:key cmail-state)}))
        (if (.match (.-type file) "image")
          (do
            (.hide @(:me/media-picker-ext s))
            (dis/dispatch! [:input [:attachment-uploading]
             {:progress "0"
              :comment-parent-uuid (:comment-parent-uuid options)}])
            (iu/upload-file! file
              (fn [url]
                (.insertImageFile editor-ext file url nil)
                (utils/after 500
                 (fn []
                   (dis/dispatch! [:input [:attachment-uploading] nil])
                   (utils/to-end-of-content-editable (rum/ref-node s "editor-node"))
                   (utils/after 500 #(.togglePicker @(:me/media-picker-ext s))))))
              (fn []
               (dis/dispatch! [:input [:attachment-uploading] nil])
               (let [alert-data {:icon "/img/ML/error_icon.png"
                      :action "dnd-image-upload-error"
                      :title "Sorry!"
                      :message "An error occurred while uploading your file."
                      :solid-button-title "OK"
                      :solid-button-cb #(alert-modal/hide-alert)}]
                  (alert-modal/show-alert alert-data)))
              (fn [progress-percentage]
               (dis/dispatch! [:input [:attachment-uploading]
                {:progress progress-percentage
                 :comment-parent-uuid (:comment-parent-uuid options)}]))))
          (when (:attachments-enabled options)
            (do
              (dis/dispatch! [:input [:attachment-uploading]
               {:progress "0"
                :comment-parent-uuid (:comment-parent-uuid options)}])
              (iu/upload-file! file
                (fn [url]
                  (let [size (oget file :size)
                        mimetype (oget file :type)
                        filename (oget file :name)
                        createdat (utils/js-date)
                        prefix (str "Uploaded by " (jwt/get-key :name) " on " (utils/date-string createdat [:year]) " - ")
                        subtitle (str prefix (filesize size :binary false :format "%.2f" ))
                        icon (au/icon-for-mimetype mimetype)
                        attachment-data {:file-name filename
                                         :file-type mimetype
                                         :file-size size
                                         :file-url url}
                        dispatch-input-key (:dispatch-input-key options)]
                    (activity-actions/add-attachment dispatch-input-key attachment-data)
                    (dis/dispatch! [:input [:attachment-uploading] nil])))
                (fn []
                 (dis/dispatch! [:input [:attachment-uploading] nil])
                 (let [alert-data {:icon "/img/ML/error_icon.png"
                      :action "dnd-attachment-upload-error"
                      :title "Sorry!"
                      :message "An error occurred while uploading your file."
                      :solid-button-title "OK"
                      :solid-button-cb #(alert-modal/hide-alert)}]
                  (alert-modal/show-alert alert-data)))
                (fn [progress-percentage]
                 (dis/dispatch! [:input [:attachment-uploading]
                  {:progress progress-percentage
                   :comment-parent-uuid (:comment-parent-uuid options)}]))))))))
    (let [alert-data {:icon "/img/ML/error_icon.png"
                    :action "dnd-file-too-big"
                    :title "Sorry!"
                    :message "Error, please use files smaller than 20MB."
                    :solid-button-title "OK"
                    :solid-button-cb #(alert-modal/hide-alert)}]
      (alert-modal/show-alert alert-data))))

;; Setup ME

(defn setup-editor [s body-on-change options]
  (let [users-list @(drv/get-ref s :mention-users)]
    (when (and (seq users-list)
               (nil? @(:me/editor s)))
      (let [mobile-editor (responsive/is-tablet-or-mobile?)
            media-config (:media-config options)
            placeholder (or (:placeholder options) "What would you like to share?")
            body-el (rum/ref-node s "editor-node")
            media-picker-opts {:buttons (clj->js media-config)
                               :hidePlaceholderOnExpand false
                               :inlinePlusButtonOptions #js {:inlineButtons (:use-inline-media-picker options)
                                                             :staticPositioning (:static-positioned-media-picker options)
                                                             :mediaPickerContainerSelector (:media-picker-container-selector options)
                                                             :alwaysExpanded (:use-inline-media-picker options)
                                                             :initiallyVisible (:media-picker-initially-visible options)
                                                             :disableButtons (:paywall? options)
                                                             }
                               ; :saveSelectionClickElementId default-mutli-picker-button-id
                               :delegateMethods #js {:onPickerClick (partial on-picker-click s options)}}
            media-picker-ext (when-not mobile-editor (js/MediaPicker. (clj->js media-picker-opts)))
            file-dragging-ext (when-not mobile-editor
                                (js/CarrotFileDragging. (clj->js {:uploadHandler (partial file-dnd-handler s options)})))
            buttons ["bold" "italic" "unorderedlist" "anchor" "quote" "highlighter" "h1" "h2"]
            paste-ext-options #js {:forcePlainText false
                                   :cleanPastedHTML true
                                   :cleanAttrs #js ["style" "alt" "dir" "size" "face" "color" "itemprop" "name" "id"]
                                   :cleanTags #js ["meta" "video" "audio" "img" "button" "svg" "canvas" "figure" "input"
                                                   "textarea" "style" "javascript"]
                                   :unwrapTags (clj->js (remove nil?
                                                ["!doctype" "abbr" "acronym" "address" "applet" "area" "article"
                                                 "aside" "base" "basefont" "bb" "bdo" "big" "body" "br" "caption"
                                                 "center" "cite" "col" "colgroup" "command" "datagrid" "datalist"
                                                 "dd" "del" "details" "dfn" "dialog" "dir" "div" "dl" "dt" "em"
                                                 "embed" "eventsource" "fieldset" "figcaption" "font" "footer" "form"
                                                 "frame" "frameset" "h3" "h4" "h5"
                                                 "h6" "head" "header" "hgroup" "hr" "html" "iframe" "ins" "isindex"
                                                 "kbd" "keygen" "label" "legend" "link"  "main" "map" "mark" "menu" "meter"
                                                 "nav" "noframes" "noscript" "object" "ol" "optgroup" "option"
                                                 "output" "p" "param" "progress" "q" "rp" "rt" "ruby" "s" "samp"
                                                 "script" "section" "select" "small" "source" "span" "strike"
                                                 "strong" "sub" "summary" "sup" "table" "tbody" "td" "tfoot" "th"
                                                 "thead" "time" "title" "tr" "track" "tt" "u" "var" "wbr"]))}
            extensions (cond-> {"autolist" (js/AutoList.)
                                "mention" (mention-utils/mention-ext users-list)
                                "fileDragging" false}
                         (not mobile-editor) (assoc "media-picker" media-picker-ext
                                                    "autoquote" (js/AutoQuote.)
                                                    "autocode" (js/AutoCode.)
                                                    "autoinlinecode" (js/AutoInlinecode.)
                                                    "inlinecode" (js/InlineCodeButton.)
                                                    "highlighter" (js/HighlighterButton.)
                                                    "carrotFileDragging" file-dragging-ext)
                         true clj->js)
            options {:toolbar (if mobile-editor false #js {:buttons (clj->js buttons)
                                                           :allowMultiParagraphSelection false})
                     :buttonLabels "fontawesome"
                     :anchorPreview (if mobile-editor false #js {:hideDelay 500, :previewValueSelector "a"})
                     :extensions extensions
                     :disableEditing (:paywall? options)
                     :imageDragging false
                     :targetBlank true
                     :autoLink true
                     :spellcheck false
                     :anchor #js {:customClassOption nil
                                  :customClassOptionText "Button"
                                  :linkValidation true
                                  :placeholderText "Paste or type a link"
                                  :targetCheckbox false
                                  :targetCheckboxText "Open in new window"}
                     :placeholder #js {:text placeholder
                                       :hideOnClick false
                                       :hide-on-click false}
                     :paste paste-ext-options
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
        (reset! (:me/media-picker-ext s) media-picker-ext)
        (.subscribe body-editor
                    "editableInput"
                    (fn [event editable]
                      (body-on-change s)))
        (.subscribe body-editor
                    "editableKeydown"
                    (fn [e editable]
                      (when (and (fn? (:cmd-enter-cb options))
                                   (.-metaKey e)
                                   (= "Enter" (.-key e)))
                          ((:cmd-enter-cb options) e))))
        (reset! (:me/editor s) body-editor)
        ;; Setup autocomplete
        (let [classes (:classes options)]
          (when (and (string/includes? classes "emoji-autocomplete")
                     (not (:paywall? options)))
            (js/emojiAutocomplete)))))))