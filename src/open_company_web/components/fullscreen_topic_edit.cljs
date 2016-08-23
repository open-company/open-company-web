(ns open-company-web.components.fullscreen-topic-edit
  (:require-macros [cljs.core.async.macros :refer (go)]
                   [if-let.core :refer (if-let* when-let*)])
  (:require [cljs.core.async :refer (chan <!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.api :as api]
            [open-company-web.urls :as oc-urls]
            [open-company-web.caches :as caches]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-colors :as oc-colors]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.lib.medium-editor-exts :as editor]
            [open-company-web.lib.prevent-route-dispatch :refer (prevent-route-dispatch)]
            [open-company-web.components.finances.finances-edit :refer (finances-edit)]
            [open-company-web.lib.finance-utils :as finance-utils]
            [open-company-web.components.growth.growth-edit :refer (growth-edit)]
            [open-company-web.lib.growth-utils :as growth-utils]
            [open-company-web.components.ui.icon :refer (icon)]
            [open-company-web.components.ui.emoji-picker :refer (emoji-picker)]
            [open-company-web.components.ui.filestack-uploader :refer (filestack-uploader)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.history.EventType :as HistoryEventType]
            [goog.dom :as gdom]
            [goog.object :as gobj]
            [cljsjs.medium-editor] ; pulled in for cljsjs externs
            [clojure.string :as string]
            [cljsjs.react.dom]))

(def before-unload-message "You have unsaved edits.")

(defn change-value [owner k e]
  (let [target (.-target e)
        value (.-value target)]
    (om/set-state! owner :has-changes true)
    (om/set-state! owner k value)))

(defn focus-headline [owner]
  (when-let [headline (sel1 [:div.topic-edit-headline])]
    (.focus headline)
    (utils/to-end-of-content-editable headline)))

(defn scroll-to-bottom []
  (let [fullscreen-topic (sel1 [:div.fullscreen-topic])]
    (set! (.-scrollTop fullscreen-topic) (.-scrollHeight fullscreen-topic))))

(defn focus-body [owner]
  (when-let [body (sel1 [:div.topic-body])]
    (.focus body)
    (utils/to-end-of-content-editable body)
    (scroll-to-bottom)))

(defn data-check-value [v]
  (and (not (= v ""))
       (not (nil? v))))

;; Finances helpers

(defn finances-get-value [v]
  (if (js/isNaN v)
    0
    v))

(defn finances-fix-row [row]
  (let [fixed-cash (update-in row [:cash] finances-get-value)
        fixed-revenue (assoc fixed-cash :revenue (finances-get-value (:revenue row)))
        fixed-costs (assoc fixed-revenue :costs (finances-get-value (:costs row)))
        fixed-burnrate (assoc fixed-costs :burn-rate (utils/calc-burn-rate (:revenue fixed-costs) (:costs fixed-costs)))
        fixed-runway (assoc fixed-burnrate :runway (utils/calc-runway (:cash fixed-burnrate) (:burn-rate fixed-burnrate)))]
    fixed-runway))

(defn finances-init-state [topic data]
  (when (= topic "finances")
    {:finances-data (finance-utils/finances-data-map data)}))

(defn finances-row-has-data [row]
  (or (data-check-value (:cash row))
      (data-check-value (:costs row))
      (data-check-value (:revenue row))))

(defn change-finances-data-cb [owner row]
  (when (finances-row-has-data row)
    (let [fixed-row (finances-fix-row row)
          period (:period fixed-row)
          finances-data (om/get-state owner :finances-data)
          fixed-data (assoc finances-data period fixed-row)]
      (om/set-state! owner :has-changes (or (om/get-state owner :has-changes) (not= finances-data fixed-data)))
      (om/set-state! owner :finances-data fixed-data))))

(defn finances-clean-row [data]
  ; a data entry is good if we have the period and one other value: cash, costs or revenue
  (when (and (not (nil? (:period data)))
             (or (not (nil? (:cash data)))
                 (not (nil? (:costs data)))
                 (not (nil? (:revenue data)))))
    (dissoc data :burn-rate :runway :new :value)))

(defn finances-clean-data [finances-data]
  (remove nil? (vec (map (fn [[_ v]] (finances-clean-row v)) finances-data))))

;; Growth helpers

(defn growth-get-value [v]
  (if (string/blank? v)
    ""
    (if (js/isNaN v)
      0
      v)))

(defn growth-fix-row [row]
  (let [fixed-value (growth-get-value (:value row))
        with-fixed-value (if (string/blank? fixed-value)
                           (dissoc row :value)
                           (assoc row :value fixed-value))
        fixed-target (growth-get-value (:target with-fixed-value))
        with-fixed-target (if (string/blank? fixed-target)
                           (dissoc with-fixed-value :target)
                           (assoc with-fixed-value :target fixed-target))]
    with-fixed-target))

(defn growth-metrics-map [metrics-coll]
  (apply merge (map #(hash-map (:slug %) %) (reverse metrics-coll))))

(defn growth-metrics-order [metrics-coll]
  (map :slug metrics-coll))

(defn growth-init-state [topic data current-state]
  (when (= topic "growth")
    (let [topic         (:topic data)
          topic-data    (if (= (dis/foce-section-key) (keyword topic)) (dis/foce-section-data) (:topic-data data))
          growth-metric-focus (:growth-metric-focus data)
          all-metrics (:metrics topic-data)
          focus-metric (or growth-metric-focus (:slug (first all-metrics)))]
      {:growth-focus (or (:growth-focus current-state) focus-metric growth-utils/new-metric-slug-placeholder)
       :growth-metadata-editing (:growth-metadata-editing current-state)
       :growth-data (growth-utils/growth-data-map (:data topic-data))
       :growth-metrics (growth-metrics-map all-metrics)
       :growth-metric-slugs (growth-metrics-order all-metrics)})))

(declare data-to-save)
(defn growth-save-metrics-metadata-cb [owner data metric-slug]
  (let [topic (om/get-props owner :topic)
        metrics (om/get-state owner :growth-metrics)
        metrics-order (om/get-state owner :growth-metric-slugs)
        new-metrics (vec (map #(metrics %) metrics-order))
        other-data (data-to-save owner topic)]
    ;; PATCH metrics, preserving title, headline and body w/ local state we have, not what the response has
    (api/partial-update-section "growth" {:metrics new-metrics} 
                                         {:title (:title other-data)
                                          :headline (:headline other-data)
                                          :body (:body other-data)})))

(defn growth-delete-metric-cb [owner data metric-slug]
  (let [topic (om/get-props owner :topic)
        all-metrics (vals (om/get-state owner :growth-metrics))
        new-metrics (vec (filter #(not= (:slug %) metric-slug) all-metrics))
        new-metrics-order (growth-metrics-order new-metrics)
        next-focus (if new-metrics-order
                      (first new-metrics-order)
                      growth-utils/new-metric-slug-placeholder)
        other-data (data-to-save owner topic)]
    (om/set-state! owner :growth-focus next-focus)
    (om/set-state! owner :growth-metric-slugs new-metrics-order)
    (om/set-state! owner :growth-metadata-editing false)
    ;; PATCH metrics, preserving title, headline and body w/ local state we have, not what the response has
    (api/partial-update-section "growth" {:metrics new-metrics}
                                         {:title (:title other-data)
                                          :headline (:headline other-data)
                                          :body (:body other-data)})))

(defn growth-metadata-edit-cb [owner editing]
  (om/set-state! owner :growth-metadata-editing editing))

(defn growth-row-has-data [row]
  (or (data-check-value (:value row))
      (data-check-value (:target row))))

(defn growth-change-data-cb
  "
  Callback for when data is changed in a growth table row.
  
  Updates the data, and enables the save button.
  "
  [owner row]
  (when (growth-row-has-data row)
    (let [{:keys [period slug] :as fixed-row} (growth-fix-row row)
          growth-data (om/get-state owner :growth-data)
          fixed-data (if (and (not (:target fixed-row))
                              (not (:value fixed-row)))
                       (dissoc growth-data (str period slug))
                       (assoc growth-data (str period slug) fixed-row))]
      (om/set-state! owner :has-changes true)
      (om/set-state! owner :growth-data fixed-data))))

(defn growth-change-metric-cb [owner data slug properties-map]
  (let [change-slug (and (contains? properties-map :slug)
                         (not= (:slug properties-map) slug))
        metrics (or (om/get-state owner :growth-metrics) {})
        metric (or (get metrics slug) {})
        new-metric (merge metric properties-map)
        ; the slug has changed, change the key of the map too
        new-metrics (if change-slug
                      (-> metrics
                          (dissoc slug)
                          (assoc (:slug properties-map) new-metric))
                      (assoc metrics slug new-metric))
        focus (om/get-state owner :growth-focus)]
    (when change-slug
      (let [slugs (om/get-state owner :growth-metric-slugs)
            remove-slug (vec (remove #(= % slug) slugs))
            add-slug (conj remove-slug (:slug properties-map))]
        ; switch the focus to the new metric-slug
        (om/set-state! owner :growth-focus (:slug properties-map))
        ; save the new metrics list
        (om/set-state! owner :growth-metric-slugs add-slug)))
    (om/set-state! owner :growth-metrics new-metrics)))

(defn growth-cancel-cb [owner data]
  (let [state (growth-init-state (:topic data) data (om/get-state owner))]
    ; reset the finances fields to the initial values
    (om/set-state! owner :growth-data (:growth-data state))
    (om/set-state! owner :growth-metrics (:growth-metrics state))
    (om/set-state! owner :growth-metric-slugs (:growth-metric-slugs state))))

(defn growth-clean-row [data]
  ; a data entry is good if we have the period and one other value: cash, costs or revenue
  (when (and (not (nil? (:period data)))
             (not (nil? (:slug data)))
             (or (not (nil? (:target data)))
                 (not (nil? (:value data)))))
    (dissoc data :new)))

(defn growth-clean-data [growth-data]
  (remove nil? (vec (map (fn [[_ v]] (growth-clean-row v)) growth-data))))

(defn growth-save-data [owner]
  (let [growth-data (om/get-state owner :growth-data)
        fixed-growth-data (growth-clean-data growth-data)]
    {:data fixed-growth-data}))

(defn data-to-save [owner topic]
  (when-let* [body-node (sel1 [(keyword (str "div#topic-edit-body-" (name topic)))])
              headline-node (sel1 [(keyword (str "div#topic-edit-headline-" (name topic)))])]
    (let [topic-kw (keyword topic)
         is-data-topic (#{:finances :growth} topic-kw)
         with-title {:title (om/get-state owner :title)}
         headline (utils/emoji-images-to-unicode (.-innerHTML headline-node))
         with-headline (merge with-title {:headline headline})
         with-header-image (merge with-headline {:image-url (om/get-state owner :image-url)
                                                 :image-width (or (om/get-state owner :image-width) 0)
                                                 :image-height (or (om/get-state owner :image-height) 0)})
         body (utils/emoji-images-to-unicode (.-innerHTML body-node))
         with-body (merge with-header-image {:body body})
         with-finances-data (if (= topic-kw :finances)
                              (merge with-body {:data (finances-clean-data (om/get-state owner :finances-data))})
                              with-body)
         with-growth-data (if (= topic-kw :growth)
                            (merge with-finances-data (growth-save-data owner))
                            with-finances-data)]
      with-growth-data)))

(def title-alert-limit 3)
(def headline-alert-limit 10)

(defn headline-on-change [owner]
  (let [headline-innerHTML (.-innerHTML (sel1 [:div.topic-edit-headline]))]
    (when (not= (gobj/get (om/get-state owner :headline) "__html") headline-innerHTML)
      (om/set-state! owner :has-changes true))))

(defn check-headline-count [owner headline-max-length e]
  (when-let [headline (sel1 [:div.topic-edit-headline])]
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
        (.preventDefault e))
      (let [remaining-chars (- headline-max-length (count headline-value))]
        (om/set-state! owner :char-count remaining-chars)
        (om/set-state! owner :char-count-alert (< remaining-chars headline-alert-limit))
        (om/set-state! owner :negative-headline-char-count (neg? remaining-chars)))
      (headline-on-change owner))))

(defn count-chars
  "A special variant of `count` that will count emoji strings (:smile:)
   and html spaces (&nbsp;) as single characters."
  [s]
  (-> (js/emojione.shortnameToUnicode (or s ""))
      (string/replace #"&nbsp;" " ")
      count))

(defn headline-count-chars []
  (when-let [headline-node (sel1 [:div.topic-edit-headline])]
    (count-chars (.-innerHTML headline-node))))

(defn top-position [el]
  (loop [yPos 0
         element el]
    (if (and element
         (gobj/get element "offsetTop")
         (gobj/get element "scrollTop")
         (gobj/get element "clientTop")
         (gobj/get element "offsetParent"))
      (recur (+ yPos (- (gobj/get element "offsetTop")
                        (gobj/get element "scrollTop"))
                     (gobj/get element "clientTop"))
             (gobj/get element "offsetParent"))
      yPos)))

(defn body-clicked [owner e]
  (when (om/get-props owner :visible)
    (when-let* [topic-body (sel1 [:div.topic-body])
                fullscreen-topic (sel1 [:div.fullscreen-topic])
                body-line (sel1 [:div.topic-body-line])]
      (let [file-upload-ui (sel1 [:div#file-upload-ui])
            add-image-btn (sel1 [:button.file-upload-btn])
            emoji-picker  (sel1 [:div.emoji-picker])]
        (when (and (>= (+ (.-clientY e) (.-scrollTop fullscreen-topic)) (- (top-position body-line) 24))
                   (not (utils/event-inside? e topic-body))
                   ; click is not on the add image button
                   (or (nil? add-image-btn)
                       (and add-image-btn
                            (not (utils/event-inside? e add-image-btn))))
                   ; click is not in the file upload ui
                   (or (nil? file-upload-ui)
                       (and file-upload-ui
                            (not (utils/event-inside? e file-upload-ui))))
                   ; click is not inside emojione-picker
                   (or (nil? emoji-picker)
                       (and emoji-picker
                            (not (utils/event-inside? e emoji-picker)))))
          (.focus topic-body)
          (utils/to-end-of-content-editable topic-body)
          (scroll-to-bottom))))))

(defn setup-body-listener [owner]
  (when-let [fullscreen-topic (sel1 [:div.fullscreen-topic])]
    (events/listen fullscreen-topic EventType/CLICK (partial body-clicked owner))))

(defn get-state [owner data current-state]
  (let [topic         (:topic data)
        current-topic-data (:topic-data data)
        topic-data    (if (= (dis/foce-section-key) (keyword topic)) (dis/foce-section-data) (:topic-data data))
        body-click    (if (and (nil? (:body-click current-state)) (:visible data))
                        (setup-body-listener owner)
                        (:body-click current-state))
        is-placeholder-topic (:placeholder current-topic-data)]
    (merge
      {:has-changes (or (not= (:image-url current-topic-data) (:image-url topic-data))
                        (not= (:title current-topic-data) (:title topic-data))
                        (not= (:headline current-topic-data) (:headline topic-data))
                        (not= (:body current-topic-data) (:body topic-data)))
       :title (:title topic-data)
       :headline (utils/emojify (:headline topic-data))
       :image-url (:image-url topic-data)
       :image-width (:image-width topic-data)
       :image-height (:image-height topic-data)
       :body (utils/emojify (:body topic-data))
       :show-title-counter (:show-title-counter current-state)
       :medium-editor (:medium-editor current-state)
       :history-listener-id (:history-listener-id current-state)
       :body-click body-click
       :char-count (:char-count current-state)
       :hide-placeholder false}
      (finances-init-state topic (:data topic-data))
      (growth-init-state topic data current-state))))

(defn reset-and-dismiss [owner options]
  (let [props (om/get-props owner)
        placeholder-section (:placeholder (:topic-data props))]
    (when placeholder-section
      (dis/dispatch! [:topic-archive (om/get-props owner :topic)]))
    ((:dismiss-editing options) placeholder-section)
    (om/set-state! owner (get-state owner props (om/get-state owner)))))

(defn force-hide-placeholder [owner]
  (let [editor       (om/get-state owner :body-medium-editor)
        body-el   (sel1 (str "div#topic-edit-body-" (name (om/get-props owner :topic))))]
    (utils/medium-editor-hide-placeholder editor body-el)))

(defn setup-medium-editor [owner {:keys [topic-data topic] :as data}]
  ; save initial innerHTML and setup MediumEditor and Emoji autocomplete
  (let [body-el (sel1 (str "div#topic-edit-body-" (name topic)))
        med-ed (new js/MediumEditor body-el (clj->js
                                             (->  (utils/medium-editor-options "" true)
                                                  (editor/inject-extension editor/file-upload))))]
    (.subscribe med-ed "editableInput" (fn [event editable]
                                         (om/set-state! owner :has-changes true)))
    (om/set-state! owner :initial-body #js {"__html" (.-innerHTML body-el)})
    (om/set-state! owner :body-medium-editor med-ed))
  (js/emojiAutocomplete)
  (utils/after 200 #(focus-headline owner)))

(defn save-data [owner options]
  (let [topic (om/get-props owner :topic)]
    (when-let [section-data (data-to-save owner topic)]
      (om/set-state! owner :has-changes false)
      (dis/dispatch! [:save-topic topic section-data])
      ((:dismiss-editing options)))))

(defn remove-topic-click [owner options e]
  (when e
    (utils/event-stop e))
  (when (js/confirm "Archiving removes the topic from the dashboard, but you won’t lose prior updates if you add it again later. Are you sure you want to archive this topic?")
    (let [section (om/get-props owner :topic)]
      (dis/dispatch! [:topic-archive section]))
    ((:dismiss-editing options) true)))

(defn img-on-load [owner img]
  (om/set-state! owner (merge (om/get-state owner) {:image-width (.-clientWidth img)
                                                    :image-height (.-clientHeight img)}))
  (gdom/removeNode img))

(defn upload-file! [owner file]
  (let [success-cb  (fn [success]
                      (let [url    (.-url success)
                            node   (gdom/createDom "img")]
                        (gdom/append (.-body js/document) node)
                        (set! (.-onload node) #(img-on-load owner node))
                        (set! (.-src node) url)
                      (om/set-state! owner (merge (om/get-state owner) {:image-url url
                                                                        :file-upload-state nil
                                                                        :file-upload-progress nil
                                                                        :has-changes true}))))
        error-cb    (fn [error]
                      (om/set-state! owner :file-upload-progress nil)
                      (js/console.log "error" error))
        progress-cb (fn [progress]
                      (let [state (om/get-state owner)]
                        (om/set-state! owner (merge state {:file-upload-state :show-progress
                                                           :file-upload-progress progress}))))]
    (cond
      (and (string? file) (not (string/blank? file)))
      (js/filepicker.storeUrl file success-cb error-cb progress-cb)
      file
      (js/filepicker.store file #js {:name (.-name file)} success-cb error-cb progress-cb))))

(defcomponent fullscreen-topic-edit [{:keys [card-width topic topic-data currency focus show-first-edit-tip] :as data} owner options]

  (init-state [_]
    (get-state owner data nil))

  (will-receive-props [_ next-props]
    ;become visible
    (when (and (:visible next-props)
               (not (:visible data)))
      (when-not (om/get-state owner :medium-editor)
        (setup-medium-editor owner data))
      (let [new-state (get-state owner next-props (om/get-state owner))
            headline-el (sel1 (str "div#topic-edit-headline-" (name (:topic next-props))))
            body-el (sel1 (str "div#topic-edit-body-" (name (:topic next-props))))
            body (:body new-state)]
        (set! (.-innerHTML headline-el) (gobj/get (:headline new-state) "__html"))
        (set! (.-innerHTML body-el) (gobj/get (:body new-state) "__html"))
        (om/set-state! owner new-state))
      (utils/after 200 #(focus-headline owner)))
    ; goes hidden
    (when (and (not (:visible next-props))
               (:visible data))
      (events/unlistenByKey (om/get-state owner :body-click)))
    (when (and (not (om/get-state owner :has-changes))
               (not= next-props data))
      (om/set-state! owner (get-state owner next-props (om/get-state owner)))))

  (will-unmount [_]
    (when-not (utils/is-test-env?)
      ; re enable the route dispatcher
      (reset! prevent-route-dispatch false)
      ; remove the onbeforeunload handler
      (set! (.-onbeforeunload js/window) nil)
      ; remove history change listener
      (events/unlistenByKey (om/get-state owner :history-listener-id))
      ; disable front of card editing
      (when-not (:placeholder topic-data)
        (utils/after 100 #(dis/dispatch! [:start-foce nil])))))

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (reset! prevent-route-dispatch true)
      (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
      (setup-medium-editor owner data)
      (when-not (om/get-state owner :body-click)
        (om/set-state! owner :body-click (setup-body-listener owner)))
      (let [win-location (.-location js/window)
            current-token (oc-urls/company-section-edit (router/current-company-slug) (name topic))
            listener (events/listen @router/history HistoryEventType/NAVIGATE
                       #(when-not (= (.-token %) current-token)
                          (if (om/get-state owner :has-changes)
                            (if (js/confirm (str before-unload-message " Are you sure you want to leave this page?"))
                              ; dispatch the current url
                              (@router/route-dispatcher (router/get-token))
                              ; go back to the previous token
                              (.setToken @router/history current-token))
                            ; dispatch the current url
                            (@router/route-dispatcher (router/get-token)))))]
        (om/set-state! owner :history-listener-id listener))))

  (did-update [_ _ prev-state]
    (when-not (om/get-state owner :body-medium-editor)
      (setup-medium-editor owner data)))

  (render-state [_ {:keys [has-changes
                           title
                           headline
                           image-url
                           image-width
                           image-height
                           body
                           char-count
                           char-count-alert
                           negative-headline-char-count
                           file-upload-state
                           file-upload-progress
                           upload-remote-url
                           hide-placeholder
                           ; finances states
                           finances-data
                           ; growth states
                           growth-focus
                           growth-data
                           growth-metrics
                           show-title-counter
                           growth-metric-slugs
                           growth-metadata-editing]}]
    (let [topic-kw (keyword topic)
          is-data-topic (#{:finances :growth} topic-kw)
          title-length-limit 20
          topic-body (utils/emojify (if-not (:placeholder topic-data) (:body topic-data) ""))
          topic-body-placeholder (or (:body-placeholder topic-data) "")
          win-height (.-clientHeight (.-body js/document))
          needs-fix? (< win-height utils/overlay-max-win-height)
          max-height (min (- 650 126) (- win-height 126))
          headline-length-limit (if (or (= topic-kw :finances)
                                        (= topic-kw :growth))
                                  80
                                  100)
          ww (.-clientWidth (sel1 js/document :body))
          fullscreen-width (responsive/fullscreen-topic-width card-width)]
      ; set the onbeforeunload handler only if there are changes
      (let [onbeforeunload-cb (when has-changes #(str before-unload-message))]
        (set! (.-onbeforeunload js/window) onbeforeunload-cb))
      
      ;; Save and cancel buttons
      (dom/div #js {:className "fullscreen-topic-edit group"
                    :ref "fullscreen-topic-edit"
                    :style #js {:width (str (- fullscreen-width 20) "px")}
                    :key (:updated-at topic-data)}
        (dom/div {:style {:opacity "1"
                          :margin "27px 0px"
                          :height "30px" ; so it keeps its height even if the buttons aren't there
                         }
                  :class "group"}
          (when-not growth-metadata-editing
            (dom/button {:class "btn-reset btn-outline left mr1 secondary-button"
                         :onClick #(do
                                    ;; reset growth section data from API since we may have preserved some local
                                    ;; state in the atom DB during metric add/archive operations
                                    (when (= topic "growth") (api/get-section (router/current-company-slug) "growth"))
                                    (reset-and-dismiss owner options)
                                    (utils/event-stop %))}
              "CANCEL"))
          (when-not growth-metadata-editing
            (dom/button {:class "btn-reset btn-solid left mr1 primary-button"
                         :disabled (or (not has-changes) negative-headline-char-count)
                         :onClick #(do
                                    (save-data owner options)
                                    (utils/event-stop %))}
              "SAVE")))
        (dom/div {:class "fullscreen-topic-internal group"
                  :on-click #(.stopPropagation %)}
          (dom/div {:class "fullscreen-topic-edit-top-box"}
            
            ;; Image
            (when image-url
              (dom/div {:class "topic-header-image"}
                (dom/img {:src image-url})
                (dom/button {:class "btn-reset remove-header"
                             :on-click #(om/set-state! owner (merge (om/get-state owner) {:image-url nil :image-width 0 :image-height 0 :has-changes true}))}
                  (icon :simple-remove {:size 15
                                          :stroke 4
                                          :color "white"
                                          :accent-color "white"}))))
            
            ;; Data table
            (when is-data-topic
              (dom/div {:class "topic-overlay-edit-data"}
              (when (= topic "finances")
                (om/build finances-edit {:finances-data finances-data
                                         :change-finances-cb (partial change-finances-data-cb owner)
                                         :currency currency
                                         :show-first-edit-tip show-first-edit-tip
                                         :first-edit-tip-cb #(focus-headline owner)}
                                        {:key (:updated-at topic-data)}))
              (when (= topic "growth")
                (dom/div {}
                  (om/build growth-edit {:growth-data growth-data
                                         :initial-focus growth-focus
                                         :metadata-edit-cb (partial growth-metadata-edit-cb owner)
                                         :metrics growth-metrics
                                         :growth-metric-slugs growth-metric-slugs
                                         :change-growth-cb (partial growth-change-data-cb owner)
                                         :delete-metric-cb (partial growth-delete-metric-cb owner data)
                                         :save-metadata-cb (partial growth-save-metrics-metadata-cb owner data)
                                         :cancel-cb #(growth-cancel-cb owner data)
                                         :change-growth-metric-cb (partial growth-change-metric-cb owner data)
                                         :new-growth-section (om/get-state owner :oc-editing)
                                         :show-first-edit-tip show-first-edit-tip
                                         :first-edit-tip-cb #(focus-headline owner)}
                                        {:opts {:currency currency} :key growth-data})))))
            
            ;; Title
            (dom/input {:class "topic-edit-title"
                        :id (str "topic-edit-title-" (name topic))
                        :type "text"
                        :placeholder "Title"
                        :on-blur #(om/set-state! owner :char-count nil)
                        :max-length title-length-limit
                        :value title
                        :on-change (fn [e]
                                      (om/set-state! owner :char-count (- title-length-limit (count title)))
                                      (om/set-state! owner :char-count-alert (< (- title-length-limit (count title)) title-alert-limit))
                                      (change-value owner :title e))})
            
            ;; Headline
            (dom/div #js {:className "topic-edit-headline emoji-autocomplete emojiable"
                          :ref "topic-edit-headline"
                          :contentEditable true
                          :id (str "topic-edit-headline-" (name topic))
                          :placeholder "Headline"
                          :onBlur #(do (check-headline-count owner headline-length-limit %)
                                        (om/set-state! owner :char-count nil))
                          :onKeyUp   #(check-headline-count owner headline-length-limit %)
                          :onKeyDown #(check-headline-count owner headline-length-limit %)
                          :dangerouslySetInnerHTML headline})
            
            ;; Body
            (dom/div #js {:className (str "topic-edit-body emoji-autocomplete emojiable" (when hide-placeholder " hide-placeholder"))
                          :id (str "topic-edit-body-" (name topic))
                          :contentEditable true
                          :placeholder topic-body-placeholder
                          :data-placeholder topic-body-placeholder
                          :dangerouslySetInnerHTML topic-body})
            
            ;; Icons
            (dom/div {:class "topc-edit-top-box-footer"}
              ;; Emoji
              (dom/div {:class "fullscreen-topic-emoji-picker left mr2"}
                (emoji-picker {:add-emoji-cb (fn [editor emoji]
                                               (when (= editor (sel1 (str "div#topic-edit-body-" (name topic))))
                                                 (force-hide-placeholder owner)))
                               :disabled (let [headline (sel1 (str "topic-edit-headline-" (name topic)))
                                               body     (sel1 (str "topic-edit-body-" (name topic)))]
                                                 (not (or (= (.-activeElement js/document) headline)
                                                          (= (.-activeElement js/document) body))))}))
              ;; Add image
              (when-not is-data-topic
                (dom/button {:class "btn-reset add-image"
                             :title (if (not image-url) "Add an image" "Replace image")
                             :type "button"
                             :data-toggle "tooltip"
                             :data-placement "top"
                             :style {:font-size "15px" :display (if (nil? file-upload-state) "block" "none")}
                             :on-click #(.click (sel1 [:input#topic-edit-upload-ui--select-trigger]))}
                  (dom/i {:class "fa fa-camera"})))
              ;; Embed image
              (when-not is-data-topic
                (dom/button {:class "btn-reset image-url"
                             :title "Provide an image link"
                             :type "button"
                             :data-toggle "tooltip"
                             :data-placement "top"
                             :style {:font-size "15px" :display (if (nil? file-upload-state) "block" "none")}
                             :on-click #(om/set-state! owner :file-upload-state :show-url-field)}
                  (dom/i {:class "fa fa-code"})))
              ;; Archive
              (dom/button #js {:className "btn-reset archive-button right"
                               :title "Archive this topic"
                               :type "button"
                               :data-toggle "tooltip"
                               :data-placement "top"
                               :style {:font-size "15px" :display (if (nil? file-upload-state) "block" "none")}
                               :onClick #(remove-topic-click owner options %)}
                (dom/i {:class "fa fa-archive"}))
              
              ;; Char counter
              (dom/div {:class (str "char-count" (when char-count-alert " red"))} char-count)
              ;; Image U/L form
              (dom/div {:class (str "upload-remote-url-container left" (when-not (= file-upload-state :show-url-field) " hidden"))}
                (dom/input {:type "text"
                            :style {:height "32px" :margin-top "1px" :outline "none" :border "1px solid rgba(78, 90, 107, 0.5)"}
                            :placeholder "http://site.com/img.png"
                            :on-change #(om/set-state! owner :upload-remote-url (-> % .-target .-value))
                            :value upload-remote-url})
                (dom/button {:style {:font-size "14px" :margin-left "5px" :padding "0.3rem"}
                             :class "btn-reset btn-solid"
                             :disabled (clojure.string/blank? upload-remote-url)
                             :on-click #(upload-file! owner (om/get-state owner :upload-remote-url))}
                  "add")
                (dom/button {:style {:font-size "14px" :margin-left "5px" :padding "0.3rem"}
                             :class "btn-reset btn-outline"
                             :on-click #(om/set-state! owner :file-upload-state nil)}
                  "cancel"))
              (dom/span {:class (str "file-upload-progress left" (when-not (= file-upload-state :show-progress) " hidden"))}
                (str file-upload-progress "%"))
              (dom/input {:id "topic-edit-upload-ui--select-trigger"
                          :style {:display "none"}
                          :type "file"
                          :on-change #(upload-file! owner (-> % .-target .-files (aget 0)))}))))))))