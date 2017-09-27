(ns oc.web.components.media-picker-test
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.image-upload :as iu]
            [oc.web.components.ui.alert-modal :refer (alert-modal)]
            [oc.web.components.ui.media-video-modal :refer (media-video-modal)]
            [oc.web.components.ui.media-chart-modal :refer (media-chart-modal)]
            [goog.dom :as gdom]
            [goog.Uri :as guri]
            [goog.object :as gobj]))

;; Chart

(defn get-chart-thumbnail [chart-id oid]
  (str "https://docs.google.com/spreadsheets/d/" chart-id "/embed/oimg?id=" chart-id "&oid=" oid "&disposition=ATTACHMENT&bo=false&zx=sohupy30u1p"))

(defn media-chart-add [s editable chart-url]
  (if (= :dismiss chart-url)
    (.addChart editable nil nil nil)
    (let [url-fragment (last (clojure.string/split chart-url #"/spreadsheets/d/"))
          chart-proxy-uri (str "/_/sheets-proxy/spreadsheets/d/" url-fragment)
          parsed-uri (guri/parse chart-url)
          oid (.get (.getQueryData parsed-uri) "oid")
          splitted-path (clojure.string/split (.getPath parsed-uri) #"/")
          chart-id (nth splitted-path (- (count splitted-path) 2))
          chart-thumbnail (get-chart-thumbnail chart-id oid)]
      (.addChart editable chart-proxy-uri chart-id chart-thumbnail))))

;; Video

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
  (if (= :dismiss video-data)
    (.addVideo editable nil nil nil nil)
    (.addVideo editable (get-video-src video-data) (:type video-data) (:id video-data) (get-video-thumbnail video-data))))

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

(defn media-photo-add-if-finished [s editable]
  (let [image @(::media-photo s)]
    (when (and (contains? image :url)
               (contains? image :width)
               (contains? image :height)
               (contains? image :thumbnail))
      (.addPhoto editable (:url image) (:thumbnail image) (:width image) (:height image))
      (reset! (::media-photo s) nil)
      (reset! (::media-photo-did-success s) false))))

(defn img-on-load [s editable url img]
  (reset! (::media-photo s) (merge @(::media-photo s) {:width (.-width img) :height (.-height img)}))
  (gdom/removeNode img)
  (media-photo-add-if-finished s editable))

(defn media-photo-dismiss-picker
  "Called every time the image picke close, reset to inital state."
  [s editable]
  (when-not @(::media-photo-did-success s)
    (reset! (::media-photo s) false)
    (.addPhoto editable nil nil nil nil)))

(defn add-photo [s editable]
  (reset! (::media-photo s) true)
  (iu/upload! {:accept "image/*"}
   (fn [res]
     (reset! (::media-photo-did-success s) true)
     (let [url (gobj/get res "url")
           img   (gdom/createDom "img")]
       (set! (.-onload img) #(img-on-load s editable url img))
       (set! (.-className img) "hidden")
       (gdom/append (.-body js/document) img)
       (set! (.-src img) url)
       (reset! (::media-photo s) {:res res :url url})
       (iu/thumbnail! url
        (fn [thumbnail-url]
         (reset! (::media-photo s) (assoc @(::media-photo s) :thumbnail thumbnail-url))
         (media-photo-add-if-finished s editable)))))
   nil
   (fn [err]
     (media-photo-add-error))
   (fn []
     ;; Delay the check because this is called on cancel but also on success
     (utils/after 1000 #(media-photo-dismiss-picker s editable)))))

;; Picker cb

(defn on-picker-click [s editable type]
  (cond
    (= type "photo")
    (add-photo s editable)
    (= type "video")
    (reset! (::media-video s) true)
    (= type "chart")
    (reset! (::media-chart s) true)))

(defn- setup-editor [s]
  (let [body-el (rum/ref-node s "body-el")
        media-picker-opts {:buttons #js ["picture" "video" "chart" "divider-line"];["picture" "video" "chart" "attachment" "divider-line"]
                           :delegateMethods #js {:onPickerClick (partial on-picker-click s)}}
        media-picker-ext (js/MediaPicker. (clj->js media-picker-opts))
        options {:toolbar #js {:buttons #js ["bold" "italic" "unorderedlist" "anchor"]}
                 :buttonLabels "fontawesome"
                 :anchorPreview #js {:hideDelay 500, :previewValueSelector "a"}
                 :extensions #js {:autolist (js/AutoList.)
                                  :media-picker media-picker-ext}
                 :autoLink true
                 :anchor #js {:customClassOption nil
                              :customClassOptionText "Button"
                              :linkValidation true
                              :placeholderText "Paste or type a link"
                              :targetCheckbox false
                              :targetCheckboxText "Open in new window"}
                 :paste #js {:forcePlainText false
                             :cleanPastedHTML false}
                 :placeholder #js {:text "Placeholder text test"
                                   :hideOnClick true}}
        body-editor  (new js/MediumEditor body-el (clj->js options))]
    (reset! (::editable-ext s) media-picker-ext)
    (.subscribe body-editor
                "editableInput"
                (fn [event editable]
                  (js/console.log "media-picker-test/editableInput")))
    (reset! (::editor s) body-editor)))

(rum/defcs media-picker-test < rum/reactive
                               (rum/local nil ::editor)
                               (rum/local nil ::editable-ext)
                               (rum/local nil ::media-photo)
                               (rum/local nil ::media-video)
                               (rum/local nil ::media-chart)
                               (rum/local false ::media-photo-did-success)
                               (drv/drv :alert-modal)
                               (drv/drv :picker-data)
                               {:did-mount (fn [s]
                                             (utils/after 1000 #(setup-editor s))
                                             s)
                                :will-update (fn [s]
                                               (let [picker-data @(drv/get-ref s :picker-data)
                                                     video-data (:media-video picker-data)
                                                     chart-data (:media-chart picker-data)]
                                                  (when (and @(::media-video s)
                                                             (not (nil? video-data)))
                                                    (reset! (::media-video s) nil)
                                                    (dis/dispatch! [:input [:picker-data :media-video] nil])
                                                    (media-video-add s @(::editable-ext s) video-data))
                                                  (when (and @(::media-chart s)
                                                             (not (nil? chart-data)))
                                                    (reset! (::media-chart s) nil)
                                                    (dis/dispatch! [:input [:picker-data :media-chart] nil])
                                                    (media-chart-add s @(::editable-ext s) chart-data)))
                                               s)}
  [s]
  (let [_ (drv/react s :picker-data)]
    [:div.media-picker-test
      (when (drv/react s :alert-modal)
        (alert-modal))
      (when @(::media-video s)
        (media-video-modal :picker-data))
      (when @(::media-chart s)
        (media-chart-modal :picker-data))
      [:div.media-picker-inner
        [:div.media-picker-body
          {:ref "body-el"
           :dangerouslySetInnerHTML #js {"__html" ""}}]]]))