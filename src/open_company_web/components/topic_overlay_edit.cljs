(ns open-company-web.components.topic-overlay-edit
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [open-company-web.api :as api]
            [open-company-web.components.finances.finances-edit :refer (finances-edit)]
            [open-company-web.components.finances.utils :as finances-utils]
            [open-company-web.components.growth.growth-edit :refer (growth-edit)]
            [open-company-web.components.growth.utils :as growth-utils]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [cljs-dynamic-resources.core :as cdr]
            [cljsjs.medium-editor]
            [cuerdas.core :as s]))

(defn change-value [owner k e]
  (let [target (.-target e)
        value (.-value target)]
    (om/set-state! owner :has-changes true)
    (om/set-state! owner k value)))

(def medium-editor-options {
  :toolbar {
    :buttons #js ["bold" "italic" "underline" "strikethrough" "h2" "orderedlist" "unorderedlist" "anchor" "image"]
  }
  :placeholder {
    :text "Add your notes here"
    :hideOnClick true
  }})

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

(defn finances-init-state [topic data]
  (when (= topic "finances")
    {:finances-data (finances-utils/map-placeholder-data data)}))

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

(defn growth-map-metric-data [metric-data]
  (apply merge (map #(hash-map (str (:period %) (:slug %)) %) metric-data)))

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
       :growth-new-metric false
       :growth-data (growth-map-metric-data (:data topic-data))
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
        new-metrics-map (growth-map-metric-data new-metrics)
        all-data (vals (om/get-state owner :growth-data))
        filtered-data (vec (filter #(not= (:slug %) metric-slug) all-data))
        new-data (growth-map-metric-data filtered-data)
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
    (om/set-state! owner :has-changes true)
    (om/set-state! owner :growth-metrics new-metrics)))

(defn growth-cancel-cb [owner data]
  (let [state (growth-init-state owner data)]
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

(defn growth-save-map [owner]
  (let [growth-data (om/get-state owner :growth-data)
        fixed-growth-data (growth-clean-data growth-data)
        growth-metrics (om/get-state owner :growth-metrics)
        growth-metric-slugs (om/get-state owner :growth-metric-slugs)
        metrics-vec (vec (map #(get growth-metrics %) growth-metric-slugs))]
    {:data fixed-growth-data
     :metrics metrics-vec}))

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
                          (merge with-finances-data (growth-save-map owner))
                          with-finances-data)]
  with-growth-data))

(defcomponent topic-overlay-edit [{:keys [topic topic-data currency focus] :as data} owner options]

  (init-state [_]
    (cdr/add-style! "/css/medium-editor/medium-editor.css")
    (cdr/add-style! "/css/medium-editor/beagle.css")
    (merge 
     {:has-changes false
      :title (:title topic-data)
      :headline (:headline topic-data)
      :body (utils/get-topic-body topic-data topic)
      :show-headline-counter false
      :show-title-counter false
      :medium-editor nil
      :history-listener-id nil}
     (finances-init-state topic (:data topic-data))
     (growth-init-state topic data)))

  (will-unmount [_]
    (when-not (utils/is-test-env?)
      ; re enable the route dispatcher
      (reset! open-company-web.core/prevent-route-dispatch false)
      ; remove the onbeforeunload handler
      (set! (.-onbeforeunload js/window) nil)
      ; remove history change listener
      (events/unlistenByKey (om/get-state owner :history-listener-id))))

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (reset! open-company-web.core/prevent-route-dispatch true)
      ; save initial innerHTML and setup MediumEditor
      (let [body-el (om/get-ref owner "topic-overlay-edit-body")
            med-ed (new js/MediumEditor body-el (clj->js medium-editor-options))]
        (.subscribe med-ed "editableInput" (fn [event editable]
                                             (om/set-state! owner :has-changes true)))
        (om/set-state! owner :initial-body (.-innerHTML body-el))
        (om/set-state! owner :medium-editor med-ed))
      (when focus
        (focus-field topic focus))
      (let [win-location (.-location js/window)
            current-token (str (.-pathname win-location) (.-search win-location) (.-hash win-location))
            listener (events/listen open-company-web.core/history EventType/NAVIGATE
                       #(when-not (= (.-token %) current-token)
                          (if (om/get-state owner :has-changes)
                            (if (js/confirm (str before-unload-message " Are you sure you want to leave this page?"))
                              ; dispatch the current url
                              (open-company-web.core/route-dispatch! (router/get-token))
                              ; go back to the previous token
                              (.setToken open-company-web.core/history current-token))
                            ; dispatch the current url
                            (open-company-web.core/route-dispatch! (router/get-token)))))]
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
          js-date-upat (utils/js-date (:updated-at topic-data))
          month-string (utils/month-string-int (inc (.getMonth js-date-upat)))
          topic-updated-at (str month-string " " (.getDate js-date-upat))
          subtitle-string (str (:name (:author topic-data)) " on " topic-updated-at)
          section-body (utils/get-topic-body topic-data topic-kw)
          win-height (.-clientHeight (.-body js/document))
          needs-fix? (< win-height utils/overlay-max-win-height)
          max-height (min (- 650 126) (- win-height 126))
          ; growth
          focus-metric-data (filter-growth-data growth-focus growth-data)
          growth-data (when (= topic "growth") (growth-map-metric-data (:data topic-data)))
          growth-cancel-fn #(growth-cancel-cb owner data)]
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
                                    ((:dismiss-editing-cb options)))} "Save Topic"))
        (dom/button {:class "cancel"
                     :on-click #((:dismiss-editing-cb options))} "Cancel")
        (dom/div {:class "topic-overlay-edit-header"}
          (dom/input {:class "topic-overlay-edit-title"
                      :id (str "topic-edit-title-" (name topic))
                      :type "text"
                      :placeholder "Type your title here"
                      :on-focus #(om/set-state! owner :show-title-counter true)
                      :on-blur #(om/set-state! owner :show-title-counter false)
                      :max-length 100
                      :value title
                      :on-change #(change-value owner :title %)})
          (dom/div {:class (utils/class-set {:topic-overlay-edit-title-count true
                                             :transparent (not show-title-counter)})}
            (dom/label {:class "bold"} (- 100 (count title))) "/100")
          (dom/div {:class "topic-overlay-date"} subtitle-string))
        (dom/div #js {:className "topic-overlay-edit-content"
                      :ref "topic-overlay-edit-content"
                      :style #js {:maxHeight (str max-height "px")}}
          (dom/input {:class "topic-overlay-edit-headline"
                      :id (str "topic-edit-headline-" (name topic))
                      :type "text"
                      :placeholder "Type your headline here"
                      :on-focus #(om/set-state! owner :show-headline-counter true)
                      :on-blur #(om/set-state! owner :show-headline-counter false)
                      :max-length 100
                      :value headline
                      :on-change #(change-value owner :headline %)})
          (dom/div {:class "topic-overlay-edit-headline-count"}
              (dom/label {:class "bold"} (- 100 (count headline))) "/100")
          (dom/div {:class "topic-overlay-edit-data"}
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
                                       :reset-metrics-cb #(growth-reset-metrics-cb topic owner data)
                                       :cancel-cb growth-cancel-fn
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
                                  :on-click #(om/set-state! owner :growth-focus metric-slug)} mname)))
                  (dom/label {:class (utils/class-set {:pillbox true
                                                       growth-utils/new-metric-slug-placeholder true
                                                       :active (= growth-focus growth-utils/new-metric-slug-placeholder)})
                              :title "New metric"
                              :data-tab growth-utils/new-metric-slug-placeholder
                              :on-click (fn []
                                         (om/set-state! owner :growth-new-metric true)
                                         (om/set-state! owner :growth-focus growth-utils/new-metric-slug-placeholder))} "New metric")))))
          (dom/div #js {:className "topic-overlay-edit-body"
                        :ref "topic-overlay-edit-body"
                        :id (str "topic-edit-body-" (name topic))
                        :dangerouslySetInnerHTML (clj->js {"__html" section-body})}))
        (dom/div {:class "gradient"})))))