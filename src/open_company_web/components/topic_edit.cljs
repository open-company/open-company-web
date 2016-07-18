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
(def snippet-max-length 500)
(def title-alert-limit 3)
(def headline-alert-limit 10)
(def snippet-alert-limit 50)

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
  (when-let* [section-name (name (om/get-props owner :section))
              snippet-el (sel1 [(str "div#foce-snippet-" section-name)])]
    (let [topic-data (om/get-props owner :topic-data)
          topic (om/get-props owner :section)]
      (let [snippet-placeholder (if (:placeholder topic-data) (:snippet topic-data) "")
            snippet-editor      (new js/MediumEditor snippet-el (clj->js (utils/medium-editor-options snippet-placeholder)))]
        (.subscribe snippet-editor
                    "editableInput"
                    (fn [event editable]
                      (dis/dispatch! [:foce-input {:snippet (.-innerHTML snippet-el)}])
                      (let [v (.-innerText snippet-el)
                            remaining-chars (- snippet-max-length (count v))]
                        (om/set-state! owner :char-count remaining-chars)
                        (om/set-state! owner :char-count-alert (< remaining-chars snippet-alert-limit))))))
      (js/emojiAutocomplete))))

(defn headline-on-change [owner]
  (when-let [headline (sel1 (str "div#foce-headline-" (name (dis/foce-section-key))))]
    (dis/dispatch! [:foce-input {:headline (.-innerHTML headline)}])
    (let [headline-text   (.-innerText headline)
          remaining-chars (- headline-max-length (count headline-text))]
      (om/set-state! owner :char-count remaining-chars)
      (om/set-state! owner :char-count-alert (< remaining-chars headline-alert-limit)))))

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

(defn check-snippet-count [owner e]
  (when-let [snippet (sel1 (str "div#foce-snippet-" (name (dis/foce-section-key))))]
    (let [snippet-value (.-innerText snippet)]
      (when (and (not= (.-keyCode e) 8)
                 (not= (.-keyCode e) 16)
                 (not= (.-keyCode e) 17)
                 (not= (.-keyCode e) 40)
                 (not= (.-keyCode e) 38)
                 (not= (.-keyCode e) 13)
                 (not= (.-keyCode e) 27)
                 (not= (.-keyCode e) 37)
                 (not= (.-keyCode e) 39)
                 (>= (count snippet-value) snippet-max-length))
        (.preventDefault e)))))

(defn img-on-load [img]
  (dis/dispatch! [:foce-input {:image-width (.-clientHeight img)
                               :image-height (.-clientWidth img)}])
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

(defcomponent topic-edit [{:keys [currency
                                  prev-rev
                                  next-rev]} owner options]

  (init-state [_]
    (let [topic-data (dis/foce-section-data)]
      {:initial-headline (:headline topic-data)
       :initial-snippet  (if (:placeholder topic-data) "" (:snippet topic-data))
       :char-count nil
       :char-count-alert false
       :file-upload-state nil
       :file-upload-progress 0}))

  (did-mount [_]
    (js/filepicker.setKey ls/filestack-key)
    (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
    (setup-edit owner)
    (utils/after 100 #(focus-headline)))

  (render-state [_ {:keys [initial-headline initial-snippet char-count char-count-alert file-upload-state file-upload-progress upload-remote-url]}]
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
          (dom/div #js {:className "topic-body topic-snippet emoji-autocomplete"
                        :id (str "foce-snippet-" (name section))
                        :key "foce-snippet"
                        :ref "topic-snippet"
                        :contentEditable true
                        :style #js {:minHeight (if (:placeholder topic-data) "100px" "0px")}
                        :onKeyUp   #(check-snippet-count owner %)
                        :onKeyDown #(check-snippet-count owner %)
                        :onFocus   #(check-snippet-count owner %)
                        :onBlur #(do
                                   (check-snippet-count owner %)
                                   (om/set-state! owner :char-count nil))
                        :dangerouslySetInnerHTML #js {"__html" initial-snippet}})
          (dom/div {:class "topic-foce-buttons group"}
            (dom/input {:id "foce-file-upload-ui--select-trigger"
                        :style {:display "none"}
                        :type "file"
                        :on-change #(upload-file! owner (-> % .-target .-files (aget 0)))})
            (dom/button {:class "btn-reset camera left"
                         :title (if (not image-header) "Add an image" "Replace image")
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
            (dom/div {:class (str "upload-remote-url-container left" (when-not (= file-upload-state :show-url-field) " hidden"))}
                (dom/input {:type "text"
                            :auto-focus true
                            :on-change #(om/set-state! owner :upload-remote-url (-> % .-target .-value))
                            :value upload-remote-url})
                (dom/button {:style {:font-size "14px" :margin-left "1rem"}
                             :class "underline btn-reset p0"
                             :on-click #(upload-file! owner (om/get-state owner :upload-remote-url))}
                  "add")
                (dom/button {:style {:font-size "14px" :margin-left "1rem" :opacity "0.5"}
                             :class "underline btn-reset p0"
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
                           :disabled (= file-upload-state :show-progress)
                           :on-click #(dis/dispatch! [:foce-save])} "SAVE")
              (dom/button {:class "btn-reset btn-outline"
                           :on-click #(do
                                        (utils/event-stop %)
                                        (if (:placeholder topic-data)
                                          (dis/dispatch! [:topic-archive (name section)])
                                          (dis/dispatch! [:start-foce nil])))} "CANCEL"))))))))