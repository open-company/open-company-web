(ns open-company-web.components.topic-edit
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
  (let [section-name (name (om/get-props owner :section))
        topic-data (om/get-props owner :topic-data)
        topic (om/get-props owner :section)]
    (when-let [headline-el    (sel1 [(str "div#foce-headline-" section-name)])]
      (let [headline-editor (new js/MediumEditor headline-el (clj->js (utils/medium-editor-options "Headline")))]
        (.subscribe headline-editor
                    "editableInput"
                    (fn [event editable]
                      (dis/dispatch! [:foce-input {:headline (.-innerHTML headline-el)}])
                      (let [v (.-innerText headline-el)
                            remaining-chars (- headline-max-length (count v))]
                        (om/set-state! owner :char-count remaining-chars)
                        (om/set-state! owner :char-count-alert (< remaining-chars headline-alert-limit)))))))
    (when-let [snippet-el (sel1 [(str "div#foce-snippet-" section-name)])]
      (let [snippet-placeholder (if (:placeholder topic-data) (utils/get-topic-body topic-data topic) "")
            snippet-editor      (new js/MediumEditor snippet-el (clj->js (utils/medium-editor-options snippet-placeholder)))]
        (.subscribe snippet-editor
                    "editableInput"
                    (fn [event editable]
                      (dis/dispatch! [:foce-input {:snippet (.-innerHTML snippet-el)}])
                      (let [v (.-innerText snippet-el)
                            remaining-chars (- snippet-max-length (count v))]
                        (om/set-state! owner :char-count remaining-chars)
                        (om/set-state! owner :char-count-alert (< remaining-chars snippet-alert-limit)))))))
    (js/emojiAutocomplete)))

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
        (.preventDefault e)))))

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
                      (om/set-state! owner :file-upload-state nil)
                      (om/set-state! owner :file-upload-progress 0))
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

(defcomponent topic-edit [{:keys [currency
                                  prev-rev
                                  next-rev]} owner options]

  (init-state [_]
    (let [topic-data          (dis/foce-section-data)]
      {:initial-headline (:headline topic-data)
       :initial-snippet  (:snippet topic-data)
       :char-count nil
       :char-count-alert false
       :file-upload-state nil
       :file-upload-progress 0}))

  (did-mount [_]
    (js/filepicker.setKey ls/filestack-key)
    (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
    (setup-edit owner))

  (render-state [_ {:keys [initial-headline initial-snippet char-count char-count-alert file-upload-state file-upload-progress]}]
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
                      :onKeyUp   #(check-snippet-count owner %)
                      :onKeyDown #(check-snippet-count owner %)
                      :onFocus    #(check-snippet-count owner %)
                      :onBlur #(do
                                 (check-snippet-count owner %)
                                 (om/set-state! owner :char-count nil))
                      :dangerouslySetInnerHTML #js {"__html" initial-snippet}})
        (dom/div {:class "topic-foce-buttons group"}
          (dom/button {:class "btn-reset archive-topic right"
                       :title "Archive this topic"
                       :type "button"
                       :data-toggle "tooltip"
                       :data-placement "top"
                       :on-click #(when (js/confirm "Archiving removes the topic from the dashboard, but you won’t lose prior updates if you add it again later. Are you sure you want to archive this topic?")
                                    (dis/dispatch! [:topic-archive section]))}
            (dom/i {:class "fa fa-archive"}))
          (dom/input {:id "file-upload-ui--select-trigger"
                      :style {:display "none"}
                      :type "file"
                      :on-change #(upload-file! owner (-> % .-target .-files (aget 0)))})
          (dom/button {:class "btn-reset camera left"
                       :title "Add an image"
                       :type "button"
                       :data-toggle "tooltip"
                       :data-placement "top"
                       :style {:display (if (and (not image-header) (nil? file-upload-state)) "block" "none")}
                       :on-click #(.click (sel1 [:input#file-upload-ui--select-trigger]))}
            (dom/i {:class "fa fa-camera"}))
          (when (= file-upload-state :show-progress)
            (dom/span {:class "file-upload-progress left"} (str file-upload-progress "%")))
          (dom/button {:class "btn-reset add-content left"
                       :title (if (clojure.string/blank? topic-body)
                                "Add supporting content"
                                "Edit supporting content")
                       :type "button"
                       :data-toggle "tooltip"
                       :data-placement "top"
                       :on-click (partial start-fullscreen-editing-click owner options)}
            (dom/i {:class "fa fa-plus-square"})))
        (dom/div {:class "topic-foce-footer group"}
          (dom/div {:class "divider"})
          (dom/div {:class "topic-foce-footer-left"}
            (dom/label {:class (str "char-counter" (when char-count-alert " char-count-alert"))} char-count))
          (dom/div {:class "topic-foce-footer-right"}
            (dom/button {:class "btn-reset btn-solid"
                         :disabled (= file-upload-state :show-progress)
                         :on-click #(dis/dispatch! [:foce-save])} "SAVE")
            (dom/button {:class "btn-reset btn-outline"
                         :on-click #(do (utils/event-stop %) (dis/dispatch! [:start-foce nil]))} "CANCEL")))))))