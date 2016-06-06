(ns open-company-web.components.fullscreen-topic-edit
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [cljs.core.async :refer (chan <!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
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
            [open-company-web.components.ui.filestack-uploader :refer (filestack-uploader)]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [cljs-dynamic-resources.core :as cdr]
            [cljsjs.medium-editor] ; pulled in for cljsjs externs
            [clojure.string :as string]
            [cljsjs.react.dom]))

(defn set-end-of-content-editable [content-editable-element]
  (if (.-createRange js/document)
    (let [rg (.createRange js/document)]
      (.selectNodeContents rg content-editable-element)
      (.collapse rg false)
      (let [selection (.getSelection js/window)]
        (.removeAllRanges selection)
        (.addRange selection rg)))
    (let [rg (.createTextRange (.-body js/document))]
      (.moveToElementText rg content-editable-element)
      (.collapse rg false)
      (.select rg))))

(def before-unload-message "You have unsaved edits.")


(defn change-value [owner k e]
  (let [target (.-target e)
        value (.-value target)]
    (om/set-state! owner :has-changes true)
    (om/set-state! owner k value)))

(defn focus-headline [owner]
  (when-let [headline (sel1 [:div.topic-edit-headline])]
    (.focus headline)
    (set-end-of-content-editable headline)))

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

(defn finances-data-map [finances-data]
  (apply merge (map #(hash-map (:period %) %) finances-data)))

(defn finances-init-state [topic data]
  (when (= topic "finances")
    {:finances-data (finances-data-map data)}))

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
      (om/set-state! owner :has-changes (not= finances-data fixed-data))
      (om/set-state! owner :finances-data fixed-data))))

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
    (let [topic-data (:topic-data data)
          growth-metric-focus (:growth-metric-focus data)
          all-metrics (:metrics topic-data)
          focus-metric (or growth-metric-focus (:slug (first all-metrics)))]
      {:growth-focus (or (:growth-focus current-state) focus-metric growth-utils/new-metric-slug-placeholder)
       :growth-metadata-editing (:growth-metadata-editing current-state)
       :growth-new-metric (empty? all-metrics)
       :growth-data (growth-utils/growth-data-map (:data topic-data))
       :growth-metrics (growth-metrics-map all-metrics)
       :growth-metric-slugs (growth-metrics-order all-metrics)})))

(defn filter-growth-data [focus growth-data]
  (into {} (filter (fn [[k v]] (= (:slug v) focus)) growth-data)))

(defn growth-reset-metrics-cb [topic owner data]
  (let [state (growth-init-state topic data (om/get-state owner))]
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

(defn growth-row-has-data [row]
  (or (data-check-value (:value row))
      (data-check-value (:target row))))

(defn growth-change-data-cb [owner row]
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
    (om/set-state! owner :growth-metric-slugs (:growth-metric-slugs state))
    (when (om/get-state owner :growth-new-metric)
      (let [topic-data (:topic-data data)
            first-metric (:slug (first (:metrics topic-data)))]
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
  (when-let [body-node (sel1 [(keyword (str "div#topic-edit-body-" (name topic)))])]
    (let [topic-kw (keyword topic)
         is-data-topic (#{:finances :growth} topic-kw)
         with-title {:title (om/get-state owner :title)}
         with-headline (merge with-title {:headline (om/get-state owner :headline)})
         body (.-innerHTML body-node)
         with-body (merge with-headline (if is-data-topic {:notes {:body body}} {:body body}))
         with-finances-data (if (= topic-kw :finances)
                              (merge with-body {:data (finances-clean-data (om/get-state owner :finances-data))})
                              with-body)
         with-growth-data (if (= topic-kw :growth)
                            (merge with-finances-data (growth-save-data owner))
                            with-finances-data)]
      with-growth-data)))

(defn headline-on-change [owner]
  (when-not (om/get-state owner :show-headline-counter)
    (om/set-state! owner :show-headline-counter true))
  (let [headline-innerHTML (.-innerHTML (sel1 [:div.topic-edit-headline]))]
    (when (not= (om/get-state owner :headline) headline-innerHTML)
      (om/set-state! owner :has-changes true)
      (om/set-state! owner :headline headline-innerHTML))))

(defn check-headline-count [owner headline-mex-length e]
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
                 (>= (count headline-value) headline-mex-length))
        (.preventDefault e))
      (headline-on-change owner))))

(defn count-chars
  "A special variant of `count` that will count emoji strings (:smile:)
   and html spaces (&nbsp;) as single characters."
  [s]
  (-> (js/emojione.shortnameToUnicode (or s ""))
      (string/replace #"&nbsp;" " ")
      count))

(defn get-state [owner data & [initial?]]
  (let [topic-data    (:topic-data data)
        topic         (:topic data)
        current-state (when-not initial? (om/get-state owner))]
    (merge
      {:has-changes false
       :title (:title topic-data)
       :headline (:headline topic-data)
       :body (utils/get-topic-body topic-data topic)
       :note (:note topic-data)
       :show-headline-counter (:show-headline-counter current-state)
       :show-title-counter (:show-title-counter current-state)
       :medium-editor (:medium-editor current-state)
       :history-listener-id (:history-listener-id current-state)}
      (finances-init-state topic (:data topic-data))
      (growth-init-state topic data current-state))))

(defn reset-and-dismiss [owner options]
  ((:dismiss-editing options))
  (om/set-state! owner (get-state owner (om/get-props owner) true)))

(defn setup-medium-editor [owner {:keys [topic-data topic] :as data}]
  ; save initial innerHTML and setup MediumEditor and Emoji autocomplete
  (let [body-el (sel1 (str "div#topic-edit-body-" (name topic)))
        slug (keyword (router/current-company-slug))
        placeholder-data (if (:placeholder topic-data) (utils/get-topic-body topic-data topic) "")
        med-ed (new js/MediumEditor body-el (clj->js
                                             (->  (utils/medium-editor-options placeholder-data)
                                                  (editor/inject-extension editor/file-upload))))]
    (.subscribe med-ed "editableInput" (fn [event editable]
                                         (om/set-state! owner :has-changes true)))
    (js/emojiAutocomplete)
    (om/set-state! owner :initial-body (.-innerHTML body-el))
    (om/set-state! owner :medium-editor med-ed))
  (utils/after 200 #(focus-headline owner)))

(defcomponent fullscreen-topic-edit [{:keys [card-width topic topic-data currency focus] :as data} owner options]

  (init-state [_]
    (cdr/add-style! "/css/medium-editor/medium-editor.css")
    (cdr/add-style! "/css/medium-editor/default.css")
    (get-state owner data true))

  (will-mount [_]
    (let [save-ch (utils/get-channel (str "fullscreen-topic-save-" (name topic)))]
      (go (loop []
        (let [change (<! save-ch)]
          (when-let [section-data (data-to-save owner topic)]
            (om/set-state! owner :has-changes false)
            (api/partial-update-section topic section-data)
            (recur))))))
    (let [cancel-ch (utils/get-channel (str "fullscreen-topic-cancel-" (name topic)))]
      (go (loop []
        (let [change (<! cancel-ch)]
          (if-not (om/get-state owner :has-changes)
            (reset-and-dismiss owner options)
            (when (js/confirm (str before-unload-message " Are you sure you want to proceed?"))
              ; discard changes
              (reset-and-dismiss owner options)))
          (recur))))))

  (will-receive-props [_ next-props]
    (when (and (:visible next-props)
               (not (:visible data)))
      (when-not (om/get-state owner :medium-editor)
        (setup-medium-editor owner data))
      (let [new-state (get-state owner next-props true)]
        (om/set-state! owner new-state))
      (utils/after 200 #(focus-headline owner)))
    (when (and (not (om/get-state owner :has-changes))
               (not= next-props data))
      (om/set-state! owner (get-state owner next-props true))))

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
      (setup-medium-editor owner data)
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

  (did-update [_ _ prev-state]
    (when-not (om/get-state owner :medium-editor)
      (setup-medium-editor owner data))
    (let [has-changes (om/get-state owner :has-changes)]
      (when-not (= (:has-changes prev-state) has-changes)
        ((:show-save-button options) has-changes))))

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
          topic-body (utils/get-topic-body topic-data topic-kw)
          win-height (.-clientHeight (.-body js/document))
          needs-fix? (< win-height utils/overlay-max-win-height)
          max-height (min (- 650 126) (- win-height 126))
          ; growth
          focus-metric-data (filter-growth-data growth-focus growth-data)
          growth-data (when (= topic "growth") (growth-utils/growth-data-map (:data topic-data)))
          headline-length-limit (if (or (= topic-kw :finances)
                                        (= topic-kw :growth))
                                  80
                                  100)
          ww (.-clientWidth (sel1 js/document :body))
          fullscreen-width (if (> ww 575)
                              575
                              (min card-width ww))]
      ; set the onbeforeunload handler only if there are changes
      (let [onbeforeunload-cb (when has-changes #(str before-unload-message))]
        (set! (.-onbeforeunload js/window) onbeforeunload-cb))
      (dom/div {:class "fullscreen-topic-edit group"
                :style #js {:width (str (- fullscreen-width 20) "px")}
                :on-click #(.stopPropagation %)
                :key (:updated-at topic-data)}
        (dom/input {:class "topic-edit-title"
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
          (dom/div {:class (utils/class-set {:topic-edit-title-count true
                                             :transparent (not show-title-counter)})}
            (dom/label {:class "bold"} (- title-length-limit (count title))))
        (dom/div {:className "topic-edit-headline emoji-autocomplete"
                  :ref "topic-edit-headline"
                  :contentEditable true
                  :id (str "topic-edit-headline-" (name topic))
                  :placeholder "Headline"
                  :on-blur #(do (check-headline-count owner headline-length-limit %)
                                (om/set-state! owner :show-headline-counter false))
                  :on-key-up   #(check-headline-count owner headline-length-limit %)
                  :on-key-down #(check-headline-count owner headline-length-limit %)
                  :on-focus    #(check-headline-count owner headline-length-limit %)
                  :dangerouslySetInnerHTML (clj->js {"__html" (:headline topic-data)})})
        (dom/div {:class (utils/class-set {:topic-edit-headline-count true
                                           :transparent (not show-headline-counter)})}
          (dom/label {:class "bold"} (- headline-length-limit (count-chars headline))))
        (dom/div {:class "separator"})
        (dom/div {:class "topic-overlay-edit-data"} ;
          (when (= topic "finances")
            (om/build finances-edit {:finances-data finances-data
                                     :change-finances-cb (partial change-finances-data-cb owner)
                                     :currency currency}
                                    {:key (:updated-at topic-data)}))
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
        (dom/div {:class "relative"}
          (dom/div {:className "topic-body emoji-autocomplete"
                    :ref "topic-overlay-edit-body"
                    :id (str "topic-edit-body-" (name topic))
                    :dangerouslySetInnerHTML (clj->js {"__html" topic-body})})
          (om/build filestack-uploader (om/get-state owner :medium-editor)))))))