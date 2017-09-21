(ns oc.web.components.ui.media-picker
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [cljs-hash.goog :as gh]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.image-upload :as iu]
            [goog.dom :as gdom]
            [goog.Uri :as guri]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [clojure.contrib.humanize :refer (filesize)]))

(def separator "<br /></p>")

;; Photo

(defn media-photo-add-error
  "Show an error alert view for failed uploads."
  []
  (let [alert-data {:icon "/img/ML/error_icon.png"
                    :title "Sorry!"
                    :message "An error occurred with your image."
                    :solid-button-title "OK"
                    :solid-button-cb #(dis/dispatch! [:alert-modal-hide])}]
    (dis/dispatch! [:alert-modal-show alert-data])))

(defn media-photo-add-if-finished [s]
  (let [image @(::media-photo s)
        body-did-change-cb (nth (:rum/args s) 2)]
    (when (and (contains? image :url)
               (contains? image :width)
               (contains? image :height)
               (contains? image :thumbnail))
      (.restoreSelection js/rangy @(::last-selection s))
      (let [image-html (str "<img "
                             "class=\"carrot-no-preview\" "
                             "src=\"" (:url image) "\" "
                             "data-media-type=\"image\" "
                             "data-thumbnail=\"" (:thumbnail image) "\" "
                             "width=\"" (:width image) "\" "
                             "height=\"" (:height image) "\" "
                            "/>"
                            separator)]
        (js/pasteHtmlAtCaret image-html (.getSelection js/rangy js/window) false))
      (reset! (::last-selection s) nil)
      (reset! (::media-photo s) nil)
      (reset! (::media-photo-did-success s) false)
      (body-did-change-cb))))

(defn media-photo-dismiss-picker
  "Called every time the image picke close, reset to inital state."
  [s]
  (when-not @(::media-photo-did-success s)
    (reset! (::media-photo s) false)
    (when @(::last-selection s)
      (.removeMarkers js/rangy @(::last-selection s))
      (reset! (::last-selection s) nil))))

(defn img-on-load [s url img]
  (reset! (::media-photo s) (merge @(::media-photo s) {:width (.-width img) :height (.-height img)}))
  (gdom/removeNode img)
  (media-photo-add-if-finished s))

;; Video

(defn get-video-thumbnail [video]
  (cond
    (= (:type video) :youtube)
    (str "https://img.youtube.com/vi/" (:id video) "/0.jpg")
    (= (:type video) :vimeo)
    (:thumbnail video)))

(defn get-video-html [s video]
  (str "<iframe "
         "data-thumbnail=\"" (get-video-thumbnail video) "\" "
         "data-media-type=\"video\" "
         "data-video-type=\"" (name (:type video)) "\" "
         "data-video-id=\"" (:id video) "\" "
         "class=\"carrot-no-preview\" "
         "width=\"560\" "
         "height=\"315\" "
         (cond
           (= (:type video) :youtube)
           (str "src=\"https://www.youtube.com/embed/" (:id video) "\" ")
           (= (:type video) :vimeo)
           (str "src=\"https://player.vimeo.com/video/" (:id video) "\" "))
         "frameborder=\"0\" "
         "webkitallowfullscreen "
         "mozallowfullscreen "
         "allowfullscreen>"
       "</iframe>"
       separator))

(defn media-video-add [s video-data]
  (let [video-html (get-video-html s video-data)
        body-did-change-cb (nth (:rum/args s) 2)]
    (when video-html
      (.restoreSelection js/rangy @(::last-selection s))
      (js/pasteHtmlAtCaret video-html (.getSelection js/rangy js/window) false)
      (reset! (::last-selection s) nil)
      (reset! (::media-video s) false)
      (body-did-change-cb))))

;; Chart

(defn get-chart-thumbnail [chart-id oid]
  (str "https://docs.google.com/spreadsheets/d/" chart-id "/embed/oimg?id=" chart-id "&oid=" oid "&disposition=ATTACHMENT&bo=false&zx=sohupy30u1p"))

(defn get-chart-html [s chart-url]
 (let [entry-data (first (:rum/args s))
       chart-id (str (:uuid entry-data) "-" (gh/hash :md5 chart-url))
       url-fragment (last (clojure.string/split chart-url #"/spreadsheets/d/"))
       chart-proxy-uri (str "/_/sheets-proxy/spreadsheets/d/" url-fragment)
       parsed-uri (guri/parse chart-url)
       oid (.get (.getQueryData parsed-uri) "oid")
       splitted-path (clojure.string/split (.getPath parsed-uri) #"/")
       chart-id (nth splitted-path (- (count splitted-path) 2))]
  (str "<iframe "
        "data-thumbnail=\"" (get-chart-thumbnail chart-id oid) "\" "
        "data-media-type=\"chart\" "
        "data-chart-id=\"" chart-id "\" "
        "src=\"" chart-proxy-uri "\" "
        "class=\"carrot-no-preview\" "
        "width=\"550\" "
        "height=\"430\" "
        "frameborder=\"0\" "
        "webkitallowfullscreen "
        "mozallowfullscreen "
        "allowfullscreen>"
       "</iframe>"
       separator)))

(defn media-chart-add [s chart-url]
  (let [chart-html (get-chart-html s chart-url)
        body-did-change-cb (nth (:rum/args s) 2)]
    (when chart-html
      (.restoreSelection js/rangy @(::last-selection s))
      (js/pasteHtmlAtCaret chart-html (.getSelection js/rangy js/window) false)
      (reset! (::last-selection s) nil)
      (reset! (::media-chart s) false)
      (body-did-change-cb))))

;; Attachment

(defn get-attachment-html [s attachment]
  (let [prefix (str (utils/date-string (utils/js-date) [:year]) " - ")
        subtitle (str prefix (filesize (:file-size attachment) :binary false :format "%.2f" ))]
    (str "<a "
          "target=\"_blank\" "
          "contentEditable=\"false\" "
          "href=\"" (:file-url attachment) "\" "
          "class=\"carrot-no-preview media-attachment\" "
          "data-name=\"" (:file-name attachment) "\" "
          "data-mimetype=\"" (:file-type attachment) "\" "
          "data-name=\"" (:file-size attachment) "\" >"
           "<i contentEditable=\"false\" class=\"file-mimetype fa " (utils/icon-for-mimetype (:file-type attachment)) "\"></i>"
           "<label contentEditable=\"false\" class=\"media-attachment-title\">" (:file-name attachment) "</label>"
           "<label contentEditable=\"false\" class=\"media-attachment-subtitle\">" subtitle "</label>"
         "</a>"
         separator)))

(defn media-attachment-dismiss-picker
  "Called every time the image picke close, reset to inital state."
  [s]
  (when-not @(::media-attachment-did-success s)
    (reset! (::media-attachment s) false)
    (when @(::last-selection s)
      (.removeMarkers js/rangy @(::last-selection s))
      (reset! (::last-selection s) nil))))

(defn attachment-upload-failed-cb [state]
  (let [alert-data {:icon "/img/ML/error_icon.png"
                    :title "Sorry!"
                    :message "An error occurred with your file."
                    :solid-button-title "OK"
                    :solid-button-cb #(dis/dispatch! [:alert-modal-hide])}]
    (dis/dispatch! [:alert-modal-show alert-data])
    (utils/after 2000 #(do
                         (reset! (::media-attachment-did-success state) false)
                         (media-attachment-dismiss-picker state)))))

(defn attachment-upload-success-cb [state res]
  (reset! (::media-attachment-did-success state) true)
  (let [url (gobj/get res "url")]
    (if-not url
      (attachment-upload-failed-cb state)
      (let [attachment-data {:file-name (gobj/get res "filename")
                             :file-type (gobj/get res "mimetype")
                             :file-size (gobj/get res "size")
                             :file-url url}
            attachment-html (get-attachment-html state attachment-data)
            body-did-change-cb (nth (:rum/args state) 2)]
        (.restoreSelection js/rangy @(::last-selection state))
        (js/pasteHtmlAtCaret attachment-html (.getSelection js/rangy js/window) false)
        (reset! (::last-selection state) nil)
        (reset! (::media-attachment state) false)
        (utils/after 2000 #(reset! (::media-attachment-did-success state) false))
        (body-did-change-cb)))))

(defn attachment-upload-error-cb [state res error]
  (attachment-upload-failed-cb state))

;; Divider line

(defn get-divider-line-html [s]
  (str "<p><hr "
        "class=\"carrot-no-preview media-divider-line\" "
        "contentEditable=\"false\" /></p>" separator))

(defn media-divider-line-add [s]
  (let [divider-line-html (get-divider-line-html s)
        body-did-change-cb (nth (:rum/args s) 2)]
    (.restoreSelection js/rangy @(::last-selection s))
    (js/pasteHtmlAtCaret divider-line-html (.getSelection js/rangy js/window) false)
    (reset! (::last-selection s) nil)
    (reset! (::media-divider-line s) false)
    (body-did-change-cb)))

(rum/defcs media-picker < rum/reactive
                          (rum/local false ::media-expanded)
                          (rum/local false ::media-entry)
                          (rum/local false ::media-photo)
                          (rum/local false ::media-video)
                          (rum/local false ::media-chart)
                          (rum/local false ::media-attachment)
                          (rum/local false ::media-divider-line)
                          (rum/local false ::media-photo-did-success)
                          (rum/local false ::media-attachment-did-success)
                          (rum/local false ::body-focused)
                          (rum/local nil ::last-selection)
                          (rum/local nil ::window-click-listener)
                          (rum/local nil ::document-focus-in)
                          {:will-mount (fn [s]
                                         (let [body-sel (nth (:rum/args s) 3)]
                                           ;; Document focus in
                                           (reset! (::document-focus-in s)
                                            (events/listen js/document EventType/FOCUSIN
                                             #(let [body-el (sel1 [body-sel])]
                                               (when (= (.-activeElement js/document) body-el)
                                                  (reset! (::body-focused s) true)))))
                                           ;; Window click listener
                                           (reset! (::window-click-listener s)
                                            (events/listen js/window EventType/CLICK
                                             #(let [body-el (sel1 [body-sel])]
                                                (reset! (::body-focused s) (utils/event-inside? % body-el))
                                                (when-not (utils/event-inside? % body-el)
                                                  (reset! (::media-expanded s) false)
                                                  ; If there was a last selection saved
                                                  (when (and (not @(::media-entry s))
                                                             (not @(::media-photo s))
                                                             (not @(::media-video s))
                                                             (not @(::media-chart s))
                                                             (not @(::media-attachment s))
                                                             (not @(::media-divider-line s))
                                                             @(::last-selection s))
                                                    ; remove the markers
                                                    (.removeMarkers js/rangy @(::last-selection s))
                                                    (reset! (::last-selection s) nil))
                                                  ;; Reset the button tooltip
                                                  (doto (js/$ "button.add-media-bt")
                                                   (.tooltip "hide")
                                                   (.attr "data-original-title" "Insert media")
                                                   (.tooltip "fixTitle")
                                                   (.tooltip "hide")))))))
                                         s)
                           :did-mount (fn [s]
                                        (let [body-sel (nth (:rum/args s) 3)
                                              body-el (sel1 [body-sel])]
                                          (when (= (.-activeElement js/document) body-el)
                                            (reset! (::body-focused s) true)))
                                        s)
                           :did-remount (fn [o s]
                                          (let [data-editing (nth (:rum/args s) 4)
                                                dispatch-input-key (nth (:rum/args s) 5)]
                                            (when (map? (:temp-video data-editing))
                                              (dis/dispatch! [:input [dispatch-input-key :temp-video] nil])
                                              (media-video-add s (:temp-video data-editing)))
                                            (when-not (empty? (:temp-chart data-editing))
                                              (dis/dispatch! [:input [dispatch-input-key :temp-chart] nil])
                                              (media-chart-add s (:temp-chart data-editing))))
                                        s)
                           :will-unmount (fn [s]
                                           (events/unlistenByKey @(::window-click-listener s))
                                           (events/unlistenByKey @(::document-focus-in s))
                                           s)}
  [s media-config media-picker-id body-did-change-cb body-editor-sel data-editing dispatch-input-key]
  [:div.media-picker
    {:id media-picker-id
     :style {:display "none"}}
    ; Add media button
    [:button.mlb-reset.media.add-media-bt
      {:title (if @(::media-expanded s) "Close" "Insert media")
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
                     (utils/after 100 (fn []
                                        (-> (js/$ "button.add-media-bt")
                                          (.tooltip "hide")
                                          (.attr "data-original-title" "Close")
                                          (.tooltip "fixTitle")
                                          (.tooltip "hide"))))))}]

    [:div.media-picker-container
      {:class (utils/class-set {:expanded @(::media-expanded s)
                                (str "media-" (count media-config)) true})}
      ; Add an entry button
      (when (:entry (set media-config))
        [:button.mlb-reset.media.media-entry
          {:class (utils/class-set {:active @(::media-entry s)
                                    (str "media-" (.indexOf media-config :entry)) true})
           :title "Add update"
           :data-toggle "tooltip"
           :data-placement "top"
           :data-container "body"
           :on-click (fn []
                       (reset! (::media-entry s) (not @(::media-entry s))))}])
      ; Add a picture button
      (when (:photo (set media-config))
        [:button.mlb-reset.media.media-photo
          {:class (utils/class-set {:active @(::media-photo s)
                                    (str "media-" (.indexOf media-config :photo)) true})
           :title "Add picture"
           :data-toggle "tooltip"
           :data-placement "top"
           :data-container "body"
           :on-click (fn []
                       (reset! (::media-photo s) true)
                       (iu/upload! {:accept "image/*"}
                        (fn [res]
                          (reset! (::media-photo-did-success s) true)
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
                              (media-photo-add-if-finished s)))))
                        nil
                        (fn [err]
                          (media-photo-add-error))
                        (fn []
                          ;; Delay the check because this is called on cancel but also on success
                          (utils/after 1000 #(media-photo-dismiss-picker s)))))}])
      ; Add a video button
      (when (:video (set media-config))
        [:button.mlb-reset.media.media-video
          {:class (utils/class-set {:active @(::media-video s)
                                    (str "media-" (.indexOf media-config :video)) true})
           :data-toggle "tooltip"
           :data-placement "top"
           :data-container "body"
           :title "Add video"
           :on-click (fn []
                       (reset! (::media-video s) true)
                       (dis/dispatch! [:input [dispatch-input-key :media-video] true]))}])
      ; Add a chart button
      (when (:chart (set media-config))
        [:button.mlb-reset.media.media-chart
          {:class (utils/class-set {:active @(::media-chart s)
                                    (str "media-" (.indexOf media-config :chart)) true})
           :title "Add Google Sheet chart"
           :data-toggle "tooltip"
           :data-placement "top"
           :data-container "body"
           :on-click (fn []
                       (reset! (::media-chart s) true)
                       (dis/dispatch! [:input [dispatch-input-key :media-chart] true]))}])
      (when (:attachment (set media-config))
        [:button.mlb-reset.media.media-attachment
          {:class (utils/class-set {:active @(::media-attachment s)
                                    (str "media-" (.indexOf media-config :attachment)) true})
           :title "Add attachment"
           :data-toggle "tooltip"
           :data-placement "top"
           :data-container "body"
           :on-click (fn []
                       (reset! (::media-attachment s) true)
                       (iu/upload!
                        nil
                        (partial attachment-upload-success-cb s)
                        nil
                        (partial attachment-upload-error-cb s)
                        (fn []
                          (utils/after 1000 #(media-attachment-dismiss-picker s)))))}])
      (when (:divider-line (set media-config))
        [:button.mlb-reset.media.media-divider
          {:class (utils/class-set {:active @(::media-divider-line s)
                                    (str "media-" (.indexOf media-config :divider-line)) true})
           :title "Add divider line"
           :data-toggle "tooltip"
           :data-placement "top"
           :data-container "body"
           :on-click (fn []
                       (reset! (::media-divider-line s) true)
                       (media-divider-line-add s))}])]])