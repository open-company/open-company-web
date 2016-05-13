(ns open-company-web.components.topic-overlay-edit
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.medium-editor-exts :as editor]
            [open-company-web.router :as router]
            [open-company-web.api :as api]
            [open-company-web.caches :as caches]
            [open-company-web.lib.prevent-route-dispatch :refer (prevent-route-dispatch)]
            [open-company-web.components.finances.finances-edit :refer (finances-edit)]
            [open-company-web.components.finances.utils :as finances-utils]
            [open-company-web.components.growth.growth-edit :refer (growth-edit)]
            [open-company-web.components.growth.utils :as growth-utils]
            [open-company-web.components.ui.icon :as i]
            [goog.events :as events]
            [goog.style :as gstyle]
            [goog.dom.classlist :as cl]
            [goog.history.EventType :as EventType]
            [cljs-dynamic-resources.core :as cdr]
            [cljsjs.medium-editor] ; pulled in for cljsjs externs
            [cljsjs.filestack] ; pulled in for cljsjs externs
            [cuerdas.core :as s]))

(defn change-value [owner k e]
  (let [target (.-target e)
        value (.-value target)]
    (om/set-state! owner :has-changes true)
    (om/set-state! owner k value)))

(defn focus-field [topic field]
  (let [topic-field (.getElementById js/document (str "topic-edit-" field "-" (name topic)))
        field-value (.-value topic-field)]
    (.focus topic-field)
    (when (or (= field "headline") (= field "title"))
      (set! (.-value topic-field) field-value))))

(def before-unload-message "You have unsaved changes to the topic.")

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

(defn finances-data-map [finances-data]
  (apply merge (map #(hash-map (:period %) %) finances-data)))

(defn finances-init-state [topic data]
  (when (= topic "finances")
    {:finances-data (finances-data-map data)}))

(defn change-finances-data-cb [owner row]
  (let [fixed-row (finances-fix-row row)
        period (:period fixed-row)
        finances-data (om/get-state owner :finances-data)
        fixed-data (assoc finances-data period fixed-row)]
    (om/set-state! owner :has-changes true)
    (om/set-state! owner :finances-data fixed-data)))

(defn finances-clean-row [data]
  ; a data entry is good if we have the period and one other value: cash, costs or revenue
  (when (and (not (nil? (:period data)))
             (or (not (nil? (:cash data)))
                 (not (nil? (:costs data)))
                 (not (nil? (:revenue data)))))
    (dissoc data :burn-rate :runway :avg-burn-rate :new :value)))

(defn finances-clean-data [finances-data]
  (remove nil? (vec (map (fn [[_ v]] (finances-clean-row v)) finances-data))))

;; Growth helpers

(defn growth-get-value [v]
  (if (s/blank? v)
    ""
    (if (js/isNaN v)
      0
      v)))

(defn growth-fix-row [row]
  (let [fixed-value (growth-get-value (:value row))
        with-fixed-value (if (s/blank? fixed-value)
                           (dissoc row :value)
                           (assoc row :value fixed-value))
        fixed-target (growth-get-value (:target with-fixed-value))
        with-fixed-target (if (s/blank? fixed-target)
                           (dissoc with-fixed-value :target)
                           (assoc with-fixed-value :target fixed-target))]
    with-fixed-target))

(defn growth-metrics-map [metrics-coll]
  (apply merge (map #(hash-map (:slug %) %) (reverse metrics-coll))))

(defn growth-metrics-order [metrics-coll]
  (map :slug metrics-coll))

(defn growth-init-state [topic data]
  (when (= topic "growth")
    (let [topic-data (:topic-data data)
          growth-metric-focus (:growth-metric-focus data)
          all-metrics (:metrics topic-data)
          focus-metric (or growth-metric-focus (:slug (first all-metrics)))]
      {:growth-focus (or focus-metric growth-utils/new-metric-slug-placeholder)
       :growth-metadata-editing false
       :growth-new-metric (empty? all-metrics)
       :growth-data (growth-utils/growth-data-map (:data topic-data))
       :growth-metrics (growth-metrics-map all-metrics)
       :growth-metric-slugs (growth-metrics-order all-metrics)})))

(defn filter-growth-data [focus growth-data]
  (into {} (filter (fn [[k v]] (= (:slug v) focus)) growth-data)))

(defn growth-reset-metrics-cb [topic owner data]
  (let [state (growth-init-state topic data)]
    (om/set-state! owner :growth-metrics (:growth-metrics state))
    (om/set-state! owner :growth-metric-slugs (:growth-metric-slugs state))))

(defn growth-delete-metric-cb [owner data metric-slug]
  (let [all-metrics (vals (om/get-state owner :growth-metrics))
        new-metrics (vec (filter #(not= (:slug %) metric-slug) all-metrics))
        new-metrics-map (growth-utils/growth-data-map new-metrics)
        all-data (vals (om/get-state owner :growth-data))
        filtered-data (vec (filter #(not= (:slug %) metric-slug) all-data))
        new-data (growth-utils/growth-data-map filtered-data)
        metrics-order (growth-metrics-order new-metrics)
        next-focus (if metrics-order
                      (first metrics-order)
                      growth-utils/new-metric-slug-placeholder)]
    (om/set-state! owner :growth-focus next-focus)
    (om/set-state! owner :growth-metrics new-metrics-map)
    (om/set-state! owner :growth-data new-data)
    (om/set-state! owner :growth-metric-slugs metrics-order)
    (om/set-state! owner :growth-metadata-editing false)
    (om/set-state! owner :has-changes true)))

(defn growth-save-metrics-metadata-cb [owner data metric-slug]
  (let [metrics (om/get-state owner :growth-metrics)
       metrics-order (om/get-state owner :growth-metric-slugs)
       new-metrics (vec (map #(metrics %) metrics-order))]
    (api/partial-update-section "growth" {:metrics new-metrics})
    (om/set-state! owner :growth-new-metric false)))

(defn growth-metadata-edit-cb [owner editing]
  (om/set-state! owner :growth-metadata-editing editing))

(defn growth-change-data-cb [owner row]
  (let [{:keys [period slug] :as fixed-row} (growth-fix-row row)
        growth-data (om/get-state owner :growth-data)
        fixed-data (if (and (not (:target fixed-row))
                            (not (:value fixed-row)))
                     (dissoc growth-data (str period slug))
                     (assoc growth-data (str period slug) fixed-row))]
    (om/set-state! owner :has-changes true)
    (om/set-state! owner :growth-data fixed-data)))

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
  (let [state (growth-init-state (:topic data) data)]
    ; reset the finances fields to the initial values
    (om/set-state! owner :growth-data (:growth-data state))
    (om/set-state! owner :growth-metrics (:growth-metrics state))
    (om/set-state! owner :growth-metric-slugs (:growth-metric-slugs state))
    (when (om/get-state owner :growth-new-metric)
      (let [section-data (:topic-data data)
            first-metric (:slug (first (:metrics section-data)))]
        (om/set-state! owner :growth-focus first-metric)))
    ; and the editing state flags
    (om/set-state! owner :growth-new-metric false)))

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
  (let [topic-kw (keyword topic)
       is-data-topic (#{:finances :growth} topic-kw)
       with-title {:title (om/get-state owner :title)}
       with-headline (merge with-title {:headline (om/get-state owner :headline)})
       body (.-innerHTML (om/get-ref owner "topic-overlay-edit-body"))
       with-body (merge with-headline (if is-data-topic {:notes {:body body}} {:body body}))
       with-finances-data (if (= topic-kw :finances)
                            (merge with-body {:data (finances-clean-data (om/get-state owner :finances-data))})
                            with-body)
       with-growth-data (if (= topic-kw :growth)
                          (merge with-finances-data (growth-save-data owner))
                          with-finances-data)]
    with-growth-data))

(defn upload-file! [editor owner file]
  (let [success-cb  (fn [success]
                      (js/console.log success)
                      (js/console.log editor)
                      ;; (.pasteHTML editor (str "<strong> URL " (.-url success) "</strong>"))
                      (.pasteHTML editor (str "<img src=\"" (.-url success) "\">"))
                      ;; (gstyle/setElementShown (om/get-node owner) false)
                      (om/set-state! owner {}))
        error-cb    (fn [error] (js/console.log "error" error))
        progress-cb (fn [progress]
                      (om/set-state! owner {:state :show-progress
                                            :progress progress}))]
    (if (string? file)
      (js/filepicker.storeUrl file success-cb error-cb progress-cb)
      (js/filepicker.store file #js {:name (.-name file)} success-cb error-cb progress-cb))))

(defcomponent uploader [editor owner]
  (did-mount [_]
    (assert ls/filestack-key "FileStack API Key required")
    (js/filepicker.setKey ls/filestack-key))

  (render-state [this _]
    (dom/div {:id "file-upload-ui"
              :style (merge {:transition ".2s"}
                            (when (:state (om/get-state owner))
                              {:background "white" :right 0}))}
      (dom/div {:class "flex"}
        (dom/input {:id "file-upload-ui--select-trigger" :style {:display "none"} :type "file"
                    :on-change #(upload-file! editor owner (-> % .-target .-files (aget 0)))})
        (dom/button {:style {:margin-right "13px"}
                     :on-click (fn [_] (om/set-state! owner :state :show-options))}
          (i/icon :circle-add {:size 24}))
        (case (:state (om/get-state owner))
          :show-options
          (dom/div (dom/button {:style {:font-size "14px"} :class "underline"
                                :on-click #(.click (js/document.getElementById "file-upload-ui--select-trigger"))}
                     "Select an image")
            #_(dom/span {:style {:font-size "14px"}} " or ")
            #_(dom/button {:style {:font-size "14px"} :class "underline"
                           :on-click #(om/set-state! owner :state :show-url-field)}
                "provide URL"))
          :show-progress
          (dom/span (str "Uploading... " (om/get-state owner :progress) "%"))
          :show-url-field
          (dom/div (dom/input {:type "text" :auto-focus true :on-change #(do (om/set-state! owner :url (-> % .-target .-value)) true)
                               :value (om/get-state owner :url)})
            (dom/button {:style {:font-size "14px" :margin-left "1rem"} :class "underline"
                         :on-click #(upload-file! editor owner (om/get-state owner :url))}
              "add"))
          (dom/span))))))

(defcomponent topic-overlay-edit [{:keys [topic topic-data currency focus] :as data} owner options]

  (init-state [_]
    (cdr/add-style! "/css/medium-editor/medium-editor.css")
    (cdr/add-style! "/css/medium-editor/default.css")
    (merge
     {:has-changes false
      :title (:title topic-data)
      :headline (:headline topic-data)
      :body (utils/get-topic-body topic-data topic)
      :note (:note topic-data)
      :show-headline-counter false
      :show-title-counter false
      :medium-editor nil
      :history-listener-id nil}
     (finances-init-state topic (:data topic-data))
     (growth-init-state topic data)))

  (will-unmount [_]
    (when-not (utils/is-test-env?)
      ; re enable the route dispatcher
      (reset! prevent-route-dispatch false)
      ; remove the onbeforeunload handler
      (set! (.-onbeforeunload js/window) nil)
      ; remove history change listener
      (events/unlistenByKey (om/get-state owner :history-listener-id))))

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (reset! prevent-route-dispatch true)
      ; save initial innerHTML and setup MediumEditor
      (let [body-el (om/get-ref owner "topic-overlay-edit-body")
            slug (keyword (router/current-company-slug))
            finances-placeholder-data (get (:sections (get (:categories (slug @caches/new-sections)) 2)) 0)
            med-ed (new js/MediumEditor body-el (clj->js
                                                 (->  (utils/medium-editor-options (:note finances-placeholder-data))
                                                      (editor/inject-extension editor/file-upload))))]
        (.subscribe med-ed "editableInput" (fn [event editable]
                                             (om/set-state! owner :has-changes true)))
        (om/set-state! owner :initial-body (.-innerHTML body-el))
        (om/set-state! owner :medium-editor med-ed))
      (when focus
        (focus-field topic focus))
      (let [win-location (.-location js/window)
            current-token (str (.-pathname win-location) (.-search win-location) (.-hash win-location))
            listener (events/listen @router/history EventType/NAVIGATE
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

  (render-state [_ {:keys [has-changes
                           title
                           headline
                           body
                           ; finances states
                           finances-data
                           ; growth states
                           growth-focus
                           growth-new-metric
                           growth-data
                           growth-metrics
                           show-headline-counter
                           show-title-counter
                           growth-metric-slugs]}]
    (let [topic-kw (keyword topic)
          title-length-limit 20
          section-body (utils/get-topic-body topic-data topic-kw)
          win-height (.-clientHeight (.-body js/document))
          needs-fix? (< win-height utils/overlay-max-win-height)
          max-height (min (- 650 126) (- win-height 126))
          ; growth
          focus-metric-data (filter-growth-data growth-focus growth-data)
          growth-data (when (= topic "growth") (growth-utils/growth-data-map (:data topic-data)))
          headline-length-limit (if (or (= topic-kw :finances)
                                        (= topic-kw :growth))
                                  80
                                  100)]
      ; set the onbeforeunload handler only if there are changes
      (let [onbeforeunload-cb (when has-changes #(str before-unload-message))]
        (set! (.-onbeforeunload js/window) onbeforeunload-cb))
      (dom/div {:class "topic-overlay-edit"
                :on-click #(.stopPropagation %)}
        (when has-changes
          (dom/button {:class "save-topic"
                       :on-click #(let [section-data (data-to-save owner topic)]
                                    (.stopPropagation %)
                                    (api/partial-update-section topic section-data)
                                    ((:dismiss-editing-cb options)))} "Save"))
        (dom/button {:class (utils/class-set {:cancel true
                                              :save-visible has-changes})
                     :on-click #((:dismiss-editing-cb options))} "Cancel")
        (dom/div {:class "topic-overlay-edit-header"}
          (dom/input {:class "topic-overlay-edit-title"
                      :id (str "topic-edit-title-" (name topic))
                      :type "text"
                      :placeholder "Title"
                      :on-blur #(om/set-state! owner :show-title-counter false)
                      :max-length title-length-limit
                      :value title
                      :on-change (fn [e]
                                    (when (not show-title-counter)
                                      (om/set-state! owner :show-title-counter true))
                                    (change-value owner :title e))})
          (dom/div {:class (utils/class-set {:topic-overlay-edit-title-count true
                                             :transparent (not show-title-counter)})}
            (dom/label {:class "bold"} (- title-length-limit (count title))))
          (dom/div {:class "topic-overlay-date"}))
        (dom/div {:class "topic-overlay-edit-content"
                  :ref "topic-overlay-edit-content"
                  :style #js {:maxHeight (str max-height "px")}}
          (dom/div {:class "relative"}
            (dom/div {:class "flex"}
              (dom/textarea {:class "flex-auto mb3 topic-overlay-edit-headline"
                             :resize false
                             :id (str "topic-edit-headline-" (name topic))
                             :type "text"
                             :placeholder "Headline"
                             :on-blur #(om/set-state! owner :show-headline-counter false)
                             :max-length headline-length-limit
                             :value headline
                             :on-change (fn [e]
                                          (when (not show-headline-counter)
                                            (om/set-state! owner :show-headline-counter true))
                                          (change-value owner :headline e))})
              (dom/div {:class (utils/class-set {:ml2 true
                                                 :mt1 true
                                                 :pr3 true
                                                 :transparent (not show-headline-counter)})}
                (dom/label {:class "bold"} (- headline-length-limit (count headline)))))
            (dom/div {:class "topic-overlay-edit-data"} ;
              (when (= topic "finances")
                (om/build finances-edit {:finances-data finances-data
                                         :change-finances-cb (partial change-finances-data-cb owner)
                                         :currency currency}))
              (when (= topic "growth")
                (dom/div {}
                  (om/build growth-edit {:growth-data focus-metric-data
                                         :metric-slug growth-focus
                                         :metadata-edit-cb (partial growth-metadata-edit-cb owner)
                                         :new-metric growth-new-metric
                                         :metrics growth-metrics
                                         :metric-count (count focus-metric-data)
                                         :change-growth-cb (partial growth-change-data-cb owner)
                                         :delete-metric-cb (partial growth-delete-metric-cb owner data)
                                         :save-metadata-cb (partial growth-save-metrics-metadata-cb owner data)
                                         :reset-metrics-cb #(growth-reset-metrics-cb topic owner data)
                                         :cancel-cb #(growth-cancel-cb owner data)
                                         :change-growth-metric-cb (partial growth-change-metric-cb owner data)
                                         :new-growth-section (om/get-state owner :oc-editing)}
                    {:key focus-metric-data})
                  (dom/div {:class "pillbox-container growth"}
                    (for [metric-slug growth-metric-slugs]
                      (let [metric (get growth-metrics metric-slug)
                            mname (:name metric)
                            metric-classes (utils/class-set {:pillbox true
                                                             metric-slug true
                                                             :active (= growth-focus metric-slug)})]
                        (dom/label {:class metric-classes
                                    :title (:description metric)
                                    :data-tab metric-slug
                                    :on-click (fn [e]
                                                (.stopPropagation e)
                                                (om/set-state! owner :growth-new-metric false)
                                                (om/set-state! owner :growth-focus metric-slug))} mname)))
                    (dom/label {:class (utils/class-set {:pillbox true
                                                         growth-utils/new-metric-slug-placeholder true
                                                         :active (= growth-focus growth-utils/new-metric-slug-placeholder)})
                                :title "Add a new metric"
                                :data-tab growth-utils/new-metric-slug-placeholder
                                :on-click (fn [e]
                                            (.stopPropagation e)
                                            (om/set-state! owner :growth-new-metric true)
                                            (om/set-state! owner :growth-focus growth-utils/new-metric-slug-placeholder))} "+ New metric")))))
            (dom/div #js {:className "topic-overlay-edit-body"
                          :ref "topic-overlay-edit-body"
                          :id (str "topic-edit-body-" (name topic))
                          :dangerouslySetInnerHTML (clj->js {"__html" section-body})})
            (om/build uploader (om/get-state owner :medium-editor))))
        (dom/div {:class "gradient"})))))