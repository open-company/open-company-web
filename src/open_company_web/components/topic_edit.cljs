(ns open-company-web.components.topic-edit
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.dispatcher :as dis]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-colors :as oc-colors]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.lib.medium-editor-exts :as editor]
            [open-company-web.components.growth.utils :as growth-utils]
            [open-company-web.components.growth.topic-growth :refer (topic-growth)]
            [open-company-web.components.finances.topic-finances :refer (topic-finances)]
            [open-company-web.components.ui.icon :as i]
            [open-company-web.components.ui.filestack-uploader :refer (filestack-uploader)]
            [cljsjs.medium-editor] ; pulled in for cljsjs externs
            [goog.dom :as gdom]
            [clojure.string :as string]))

(def title-max-length 20)
(def headline-max-length 100)
(def title-alert-limit 3)
(def headline-alert-limit 10)

(defn scroll-to-topic-top [topic]
  (let [body-scroll (.-scrollTop (.-body js/document))
        topic-scroll-top (utils/offset-top topic)]
    (utils/scroll-to-y (- (+ topic-scroll-top body-scroll) 90))))

(defn start-fullscreen-editing-click [owner options e]
  (utils/event-stop e)
  (let [section (om/get-props owner :section)
        topic-click-cb (:topic-click options)]
    (topic-click-cb nil true)))

(defn setup-edit [owner]
  (when-let* [section-kw   (keyword (om/get-props owner :section))
              section-name (name section-kw)
              body-el      (sel1 [(str "div#foce-body-" section-name)])]
    (let [body-editor      (new js/MediumEditor body-el (clj->js (utils/medium-editor-options "" false)))]
      (.subscribe body-editor
                  "editableInput"
                  (fn [event editable]
                    (let [inner-html (.-innerHTML body-el)]
                      (dis/dispatch! [:foce-input (if (#{:finances :growth} section-kw)
                                                    {:notes {:body (.-innerHTML body-el)}}
                                                    {:body (.-innerHTML body-el)})]))
                    (let [inner-text (.-innerText body-el)]
                      (om/set-state! owner :char-count (if (> (count inner-text) 500) "Extended\nlength" nil))))))
    (js/emojiAutocomplete)))

(defn headline-on-change [owner]
  (when-let [headline (sel1 (str "div#foce-headline-" (name (dis/foce-section-key))))]
    (dis/dispatch! [:foce-input {:headline (.-innerHTML headline)}])
    (let [headline-text   (.-innerText headline)
          remaining-chars (- headline-max-length (count headline-text))]
      (om/set-state! owner :char-count remaining-chars)
      (om/set-state! owner :char-count-alert (< remaining-chars headline-alert-limit))
      (om/set-state! owner :negative-headline-char-count (neg? remaining-chars)))))

(defn check-headline-count [owner e]
  (when-let [headline (sel1 (str "div#foce-headline-" (name (dis/foce-section-key))))]
    (let [headline-value (.-innerText headline)]
      (when (and (not= (.-keyCode e) 8)
                 (not= (.-keyCode e) 16)
                 (not= (.-keyCode e) 17)
                 (not= (.-keyCode e) 40)
                 (not= (.-keyCode e) 38)
                 (not= (.-keyCode e) 13)
                 (not= (.-keyCode e) 27)
                 (not= (.-keyCode e) 37)
                 (not= (.-keyCode e) 39)
                 (>= (count headline-value) headline-max-length))
        (.preventDefault e))))
  (headline-on-change owner))

(defn img-on-load [img]
  (dis/dispatch! [:foce-input {:image-width (.-clientWidth img)
                               :image-height (.-clientHeight img)}])
  (gdom/removeNode img))

(defn upload-file! [owner file]
  (let [success-cb  (fn [success]
                      (let [url    (.-url success)
                            node   (gdom/createDom "img")]
                        (set! (.-onload node) #(img-on-load node))
                        (gdom/append (.-body js/document) node)
                        (set! (.-src node) url)
                        (dis/dispatch! [:foce-input {:image-url url}]))
                      (om/set-state! owner (merge (om/get-state owner) {:file-upload-state nil :file-upload-progress nil})))
        error-cb    (fn [error] (js/console.log "error" error))
        progress-cb (fn [progress]
                      (let [state (om/get-state owner)]
                        (om/set-state! owner (merge state {:file-upload-state :show-progress
                                                           :file-upload-progress progress}))))]
    (cond
      (and (string? file) (not (string/blank? file)))
      (js/filepicker.storeUrl file success-cb error-cb progress-cb)
      file
      (js/filepicker.store file #js {:name (.-name file)} success-cb error-cb progress-cb))))

(defn focus-headline []
  (when-let [headline (sel1 [(str "div#foce-headline-" (name (dis/foce-section-key)))])]
    (.focus headline)
    (utils/to-end-of-content-editable headline)))

(defn remove-topic-click [e]
  (when e
    (utils/event-stop e))
  (when (js/confirm "Archiving removes the topic from the dashboard, but you won’t lose prior updates if you add it again later. Are you sure you want to archive this topic?")
    (let [section (dis/foce-section-key)]
      (dis/dispatch! [:topic-archive section]))
    (dis/dispatch! [:start-foce nil])))

(defn- add-image-tooltip [image-header]
  (if (or (not image-header) (string/blank? image-header))
    "Add an image"
    "Replace image"))

(defcomponent topic-edit [{:keys [currency
                                  prev-rev
                                  next-rev]} owner options]

  (init-state [_]
    (let [topic      (dis/foce-section-key)
          topic-data (dis/foce-section-data)
          body       (utils/get-topic-body topic-data topic)]
      {:initial-headline (:headline topic-data)
       :body-placeholder (if (:placeholder topic-data) body "")
       :initial-body  (if (:placeholder topic-data) "" body)
       :char-count nil
       :char-count-alert false
       :file-upload-state nil
       :file-upload-progress 0}))

  (did-mount [_]
    (js/filepicker.setKey ls/filestack-key)
    (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
    (setup-edit owner)
    (utils/after 100 #(focus-headline)))

  (did-update [_ _ _]
    (let [section           (dis/foce-section-key)
          topic-data        (dis/foce-section-data)
          image-header      (:image-url topic-data)
          add-image-tooltip (add-image-tooltip image-header)
          add-image-el      (js/$ (gdom/getElementByClass "camera"))]
      (doto add-image-el
        (.tooltip "hide")
        (.attr "data-original-title" add-image-tooltip)
        (.tooltip "fixTitle"))))

  (render-state [_ {:keys [initial-headline initial-body body-placeholder char-count char-count-alert file-upload-state file-upload-progress upload-remote-url negative-headline-char-count]}]
    (let [section             (dis/foce-section-key)
          topic-data          (dis/foce-section-data)
          section-kw          (keyword section)
          chart-opts          {:chart-size {:width  260
                                            :height 196}
                               :hide-nav true
                               :pillboxes-first false
                               :topic-click (:topic-click options)}
          is-growth-finances? (#{:growth :finances} section-kw)
          gray-color          (oc-colors/get-color-by-kw :oc-gray-5)
          finances-row-data   (:data topic-data)
          growth-data         (growth-utils/growth-data-map (:data topic-data))
          no-data             (or (and (= section-kw :finances)
                                       (or (empty? finances-row-data)
                                        (utils/no-finances-data? finances-row-data)))
                                  (and (= section-kw :growth)
                                       (utils/no-growth-data? growth-data)))
          image-header        (:image-url topic-data)
          topic-body          (utils/get-topic-body topic-data section)]
      (when section
        (dom/div #js {:className "topic-foce group"
                      :ref "topic-internal"}
          (when image-header
            (dom/div {:class "card-header card-image"}
              (when image-header
                (dom/img {:src image-header
                          :class "topic-header-img"}))
              (when image-header
                (dom/button {:class "btn-reset remove-header"
                             :on-click #(do
                                          (dis/dispatch! [:foce-input {:image-url nil :image-height 0 :image-width 0}]))}
                  (i/icon :simple-remove {:size 15
                                          :stroke 4
                                          :color "white"
                                          :accent-color "white"})))))
          ;; Topic title
          (dom/input {:class "topic-title"
                      :value (:title topic-data)
                      :max-length title-max-length
                      :placeholder (:name topic-data)
                      :type "text"
                      :on-blur #(om/set-state! owner :char-count nil)
                      :on-change #(let [v (.. % -target -value)
                                        remaining-chars (- title-max-length (count v))]
                                    (dis/dispatch! [:foce-input {:title v}])
                                    (om/set-state! owner :char-count remaining-chars)
                                    (om/set-state! owner :char-count-alert (< remaining-chars title-alert-limit)))})
          ;; Topic headline
          (dom/div {:class "topic-headline-inner emoji-autocomplete"
                    :id (str "foce-headline-" (name section))
                    :key "foce-headline"
                    :placeholder "Headline"
                    :contentEditable true
                    :on-key-up   #(check-headline-count owner %)
                    :on-key-down #(check-headline-count owner %)
                    :on-focus    #(check-headline-count owner %)
                    :onBlur #(do
                               (check-headline-count owner %)
                               (om/set-state! owner :char-count nil))
                    :dangerouslySetInnerHTML #js {"__html" initial-headline}})
          (dom/div #js {:className "topic-body emoji-autocomplete"
                        :id (str "foce-body-" (name section))
                        :key "foce-body"
                        :ref "topic-body"
                        :data-placeholder body-placeholder
                        :placeholder body-placeholder
                        :contentEditable true
                        :style #js {:minHeight (if (:placeholder topic-data) "100px" "0px")}
                        :dangerouslySetInnerHTML #js {"__html" initial-body}})
          (dom/div {:class "topic-foce-buttons group"}
            (dom/input {:id "foce-file-upload-ui--select-trigger"
                        :style {:display "none"}
                        :type "file"
                        :on-change #(upload-file! owner (-> % .-target .-files (aget 0)))})
            (dom/button {:class "btn-reset camera left"
                         :title (add-image-tooltip image-header)
                         :type "button"
                         :data-toggle "tooltip"
                         :data-placement "top"
                         :style {:display (if (nil? file-upload-state) "block" "none")}
                         :on-click #(.click (sel1 [:input#foce-file-upload-ui--select-trigger]))}
                (dom/i {:class "fa fa-camera"}))
            (dom/button {:class "btn-reset image-url left"
                         :title "Provide an image link"
                         :type "button"
                         :data-toggle "tooltip"
                         :data-placement "top"
                         :style {:display (if (nil? file-upload-state) "block" "none")}
                         :on-click #(om/set-state! owner :file-upload-state :show-url-field)}
                (dom/i {:class "fa fa-code"}))
            (when-not (:placeholder topic-data)
              (dom/button {:class "btn-reset archive-button right"
                           :title "Archive this topic"
                           :type "button"
                           :data-toggle "tooltip"
                           :data-placement "top"
                           :style {:display (if (nil? file-upload-state) "block" "none")}
                           :on-click (partial remove-topic-click)}
                  (dom/i {:class "fa fa-archive"})))
            (dom/div {:class (str "upload-remote-url-container left" (when-not (= file-upload-state :show-url-field) " hidden"))}
                (dom/input {:type "text"
                            :style {:height "32px" :margin-top "1px" :outline "none" :border "1px solid rgba(78, 90, 107, 0.5)"}
                            :on-change #(om/set-state! owner :upload-remote-url (-> % .-target .-value))
                            :value upload-remote-url})
                (dom/button {:style {:font-size "14px" :margin-left "5px" :padding "0.3rem"}
                             :class "btn-reset btn-solid"
                             :disabled (string/blank? upload-remote-url)
                             :on-click #(upload-file! owner (om/get-state owner :upload-remote-url))}
                  "add")
                (dom/button {:style {:font-size "14px" :margin-left "5px" :padding "0.3rem"}
                             :class "btn-reset btn-outline"
                             :on-click #(om/set-state! owner :file-upload-state nil)}
                  "cancel"))
            (dom/span {:class (str "file-upload-progress left" (when-not (= file-upload-state :show-progress) " hidden"))}
              (str file-upload-progress "%"))
            (dom/button {:class "btn-reset add-content left"
                         :title "Expanded view"
                         :type "button"
                         :data-toggle "tooltip"
                         :data-placement "top"
                         :style {:display (if (nil? file-upload-state) "block" "none")}
                         :on-click (partial start-fullscreen-editing-click owner options)}
              (dom/i {:class "fa fa-expand"})))
          (dom/div {:class "topic-foce-footer group"}
            (dom/div {:class "divider"})
            (dom/div {:class "topic-foce-footer-left"}
              (dom/label {:class (str "char-counter" (when char-count-alert " char-count-alert"))} char-count))
            (dom/div {:class "topic-foce-footer-right"}
              (dom/button {:class "btn-reset btn-solid"
                           :disabled (or (= file-upload-state :show-progress) negative-headline-char-count)
                           :on-click #(dis/dispatch! [:foce-save])} "SAVE")
              (dom/button {:class "btn-reset btn-outline"
                           :on-click #(do
                                        (utils/event-stop %)
                                        (if (:placeholder topic-data)
                                          (dis/dispatch! [:topic-archive (name section)])
                                          (dis/dispatch! [:start-foce nil])))} "CANCEL"))))))))