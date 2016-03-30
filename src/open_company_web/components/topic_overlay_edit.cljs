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
    :buttons #js ["bold" "italic" "underline" "strikethrough" "h2" "orderedlist" "unorderedlist"]
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
    (om/set-state! owner :finances-data fixed-data)))

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
    (let [first-metric (:slug (first (:metrics data)))
          all-metrics (:metrics data)]
      {:growth-focus (or first-metric growth-utils/new-metric-slug-placeholder)
       :growth-metadata-editing false
       :growth-new-metric false
       :growth-data (growth-map-metric-data (:data data))
       :growth-metrics (growth-metrics-map all-metrics)
       :growth-metric-slugs (growth-metrics-order all-metrics)})))

(defn filter-growth-data [focus growth-data]
  (vec (filter #(= (:slug %) focus) (vals growth-data))))

(defn growth-reset-metrics-cb [owner data]
  (let [state (growth-init-state owner data)]
    (om/set-state! owner :growth-metrics (:growth-metrics state))
    (om/set-state! owner :growth-metric-slugs (:growth-metric-slugs state))))

(defn growth-delete-metric-cb [owner data metric-slug]
  (let [all-metrics (vals (om/get-state owner :growth-metrics))
        new-metrics (vec (filter #(not= (:slug %) metric-slug) all-metrics))
        new-metrics-map (growth-map-metric-data new-metrics)
        all-data (vals (om/get-state owner :growth-data))
        filtered-data (vec (filter #(not= (:slug %) metric-slug) all-data))
        new-data (growth-metrics-map filtered-data)
        metrics-order (growth-metrics-order new-metrics)]
    (om/set-state! owner :growth-metrics new-metrics-map)
    (om/set-state! owner :growth-data new-data)
    (om/set-state! owner :growth-metric-slugs metrics-order)
    ; FIXME!!
    ; (save-cb owner data)
    ))

(defn growth-metadata-edit-cb [owner editing]
  (om/set-state! owner :growth-metadata-editing editing))

(defn growth-change-data-cb [owner row]
  (let [fixed-row (growth-fix-row row)
        period (:period fixed-row)
        slug (:slug fixed-row)
        growth-data (om/get-state owner :growth-data)
        fixed-data (if (and (not (:target fixed-row))
                            (not (:value fixed-row)))
                     (dissoc growth-data (str period slug))
                     (assoc growth-data (str period slug) fixed-row))]
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
        focus (om/get-state owner :focus)]
    (when change-slug
      (let [slugs (om/get-state owner :growth-metric-slugs)
            remove-slug (vec (remove #(= % slug) slugs))
            add-slug (conj remove-slug (:slug properties-map))]
        ; switch the focus to the new metric-slug
        (om/set-state! owner :focus (:slug properties-map))
        ; save the new metrics list
        (om/set-state! owner :growth-metric-slugs add-slug)))
    (om/set-state! owner :growth-metrics new-metrics)))

(defcomponent topic-overlay-edit [{:keys [topic topic-data currency focus] :as data} owner options]

  (init-state [_]
    (cdr/add-style! "/css/medium-editor/medium-editor.css")
    (cdr/add-style! "/css/medium-editor/beagle.css")
    (merge 
     {:has-changes false
      :title (:title topic-data)
      :headline (:headline topic-data)
      :body (utils/get-topic-body topic-data topic)
      :medium-editor nil
      :history-listener-id nil}
     (finances-init-state topic (:data topic-data))
     (growth-init-state topic topic-data)))

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
                          (if (js/confirm (str before-unload-message " Are you sure you want to leave this page?"))
                            ; dispatch the current url
                            (open-company-web.core/route-dispatch! (router/get-token))
                            ; go back to the previous token
                            (.setToken open-company-web.core/history current-token))
                          (.log js/console %)))]
        (om/set-state! owner :history-listener-id listener))))

  (render-state [_ {:keys [has-changes title headline body finances-data growth-focus growth-new-metric growth-data growth-metrics]}]
    (let [topic-kw (keyword topic)
          js-date-upat (utils/js-date (:updated-at topic-data))
          month-string (utils/month-string-int (inc (.getMonth js-date-upat)))
          topic-updated-at (str month-string " " (.getDate js-date-upat))
          subtitle-string (str (:name (:author topic-data)) " on " topic-updated-at)
          section-body (utils/get-topic-body topic-data topic-kw)
          win-height (.-clientHeight (.-body js/document))
          needs-fix? (< win-height utils/overlay-max-win-height)
          max-height (- win-height 126)
          ; growth
          focus-metric-data (filter-growth-data growth-focus growth-data)
          growth-data (when (= topic "growth") (growth-map-metric-data (:data topic-data)))]
      ; set the onbeforeunload handler only if there are changes
      (let [onbeforeunload-cb (when has-changes #(str before-unload-message))]
        (set! (.-onbeforeunload js/window) onbeforeunload-cb))
      (dom/div {:class "topic-overlay-edit"
                :on-click #(.stopPropagation %)}
        (when has-changes
          (dom/button {:class "save-topic"
                       :on-click #(let [section-data {:title (om/get-state owner :title)
                                                      :headline (om/get-state owner :headline)
                                                      :body (.-innerHTML (om/get-ref owner "topic-overlay-edit-body"))}]
                                    (.stopPropagation %)
                                    (api/partial-update-section topic topic-data)
                                    ((:dismiss-editing-cb options)))} "Save Topic"))
        (dom/button {:class "cancel"
                     :on-click #((:dismiss-editing-cb options))} "Cancel")
        (dom/div {:class "topic-overlay-edit-header"}
          (dom/input {:class "topic-overlay-edit-title"
                      :id (str "topic-edit-title-" (name topic))
                      :type "text"
                      :placeholder "Type your title here"
                      :max-length 100
                      :value title
                      :on-change #(change-value owner :headline %)})
          (dom/div {:class "topic-overlay-date"} subtitle-string))
        (dom/div #js {:className "topic-overlay-edit-content"
                      :ref "topic-overlay-edit-content"
                      :style #js {:maxHeight (str max-height "px")}}
          (dom/div {}
            (dom/div {:class "topic-overlay-edit-headline-count"}
              (dom/label {:class "bold"} (- 100 (count headline))) "/100"))
          (dom/input {:class "topic-overlay-edit-headline"
                      :id (str "topic-edit-headline-" (name topic))
                      :type "text"
                      :placeholder "Type your headline here"
                      :max-length 100
                      :value headline
                      :on-change #(change-value owner :headline %)})
          (dom/div {:class "topic-overlay-edit-data"}
            (when (= topic "finances")
              (om/build finances-edit {:finances-data finances-data
                                       :change-finances-cb (partial change-finances-data-cb owner)
                                       :currency currency}))
            (when (= topic "growth")
              (om/build growth-edit {:growth-data focus-metric-data
                                     :metric-slug growth-focus
                                     :metadata-edit-cb (partial growth-metadata-edit-cb owner)
                                     :new-metric growth-new-metric
                                     :metrics growth-metrics
                                     :metric-count (count focus-metric-data)
                                     :change-growth-cb (partial growth-change-data-cb owner)
                                     :delete-metric-cb (partial growth-delete-metric-cb owner data)
                                     :reset-metrics-cb #(growth-reset-metrics-cb owner data)
                                     :change-growth-metric-cb (partial growth-change-metric-cb owner data)
                                     :new-growth-section (om/get-state owner :oc-editing)})))
          (dom/div #js {:className "topic-overlay-edit-body"
                        :ref "topic-overlay-edit-body"
                        :id (str "topic-edit-body-" (name topic))
                        :dangerouslySetInnerHTML (clj->js {"__html" section-body})}))
        (dom/div {:class "gradient"})))))