(ns oc.web.components.ui.labels
  (:require [rum.core :as rum]
            [clojure.set :as clj-set]
            [taoensso.timbre :as timbre]
            [oops.core :refer (oget)]
            [goog.string :refer (format)]
            [cuerdas.core :as string]
            [oc.web.urls :as oc-urls]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.utils.color :as color-utils]
            [oc.lib.color :as lib-color]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.components.ui.colors-presets :refer (colors-presets)]
            [oc.web.lib.react-utils :as rutils]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.label :as label-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)]
            ["react-color" :as react-color :refer (ChromePicker)]))

(def refresh-labels-mixin
  {:will-mount (fn [s]
    (label-actions/get-labels)
    s)})

(def color-picker (partial rutils/build ChromePicker))

(defn- select-label [s label]
  (timbre/infof "Label %s selected" (:slug label)))

(rum/defcs org-labels-list <
  rum/static
  rum/reactive
  (drv/drv :org-labels)
  [s]
  (let [org-labels (drv/react s :org-labels)]
    [:div.oc-labels
     [:div.oc-labels-title
      "Add labels"]
     (if (seq org-labels)
       (for [label org-labels]
        [:button.mlb-reset.oc-label
          {:data-label-slug (:slug label)
           :class (when (:can-edit? label) "editable")
           :key (str "label-" (or (:uuid label) (rand 1000)))
           :on-click (when (:can-edit? label)
                       (fn [e]
                         (dom-utils/stop-propagation! e)
                         (label-actions/edit-label label)))}
          ;; (carrot-checkbox {:selected false})
          [:span.oc-label-dot
           {:style {:background-color (:color label)}}]
          [:span.oc-label-name
           (:name label)]
          [:span.oc-label-edit-pen]])
       [:div.oc-labels-empty
        "No labels yet"])
     [:button.mlb-reset.add-label-bt
      {:on-click #(label-actions/new-label)}
      [:span.add-label-plus]
      [:span.add-label-span
       "Add label"]]
     [:div.oc-labels-footer
      [:button.mlb-reset.cancel-bt
       {:on-click #(label-actions/hide-labels-manager)}
       "Close"]]]))

(defn- delete-confirm [e label]
  (dom-utils/stop-propagation! e)
  (let [alert-data {:icon "/img/ML/trash.svg"
                    :action "delete-label-confirm"
                    :message "Are you sure you want to delete this label label? This can’t be undone."
                    :link-button-title "Keep"
                    :link-button-cb #(alert-modal/hide-alert)
                    :solid-button-style :red
                    :solid-button-title "Yes"
                    :solid-button-cb (fn []
                                       (label-actions/delete-label label)
                                       (alert-modal/hide-alert))}]
    (alert-modal/show-alert alert-data)))

(rum/defcs label-editor <
  rum/static
  rum/reactive
  (drv/drv :editing-label)
  (rum/local false ::show-color-picker)
  ;; Mixins
  (ui-mixins/on-click-out :color-picker-container
                          (fn [s e]
                            (when (and @(::show-color-picker s)
                                       (not (dom-utils/event-cotainer-has-class e :oc-input)))
                              (reset! (::show-color-picker s) false))))
  (ui-mixins/on-key-press ["Esc" "Escape"]
                          (fn [s _]
                            (reset! (::show-color-picker s) false)))
  {:did-mount (fn [s]
                (.focus (rum/ref-node s :label-name-field))
                s)}
  [s]
  (let [editing-label (drv/react s :editing-label)]
    [:div.oc-label-edit.fields-modal.label-modal-view
     [:div.oc-label-edit-title
      (if (:uuid editing-label)
        "Edit label"
        "New label")]
     [:div.oc-label-edit-name-header
      "Name"]
     [:input.field-value.oc-input
      {:value (:name editing-label)
       :ref :label-name-field
       :max-length label-actions/max-label-name-length
       :class (when (:error editing-label)
                "error")
       :on-change #(dis/dispatch! [:input [:editing-label :name] (oget % "target.value")])
       :placeholder "Name your label"}]
     [:div.oc-label-edit-color-header
      "Button/link color"]
     [:div.oc-label-edit-picker-wrapper
      [:span.oc-label-picker-dot
       {:style {:background-color (:color editing-label)}}]
      [:input.field-value.oc-input
        {:type "text"
        :value (:color editing-label)
        :pattern lib-color/color-reg-ex
        :placeholder "Ie: red, green or #0000ff"
        :on-focus #(reset! (::show-color-picker s) true)
        :on-change (fn [e]
                      (let [v (string/lower (.. e -target -value))]
                        (when (.. e -target checkValidity)
                          (let [is-hex-color? (lib-color/valid-hex-color? v)
                                hex-color (if is-hex-color? v (-> v keyword lib-color/default-css-color-names))]
                            (label-actions/label-editor-update {:color hex-color})))))}]]
      (when @(::show-color-picker s)
        [:div.color-picker-container
         {:ref :color-picker-container}
         (color-picker {:color (:color editing-label) ;; (-> current-brand-color :primary :hex)
                        :onChangeComplete (fn [color]
                                            (when-let [hex-color (oget color "?hex")]
                                              ;; (reset! (::color-value s) hex-color)
                                              (label-actions/label-editor-update {:color hex-color})))})])
      (colors-presets {:color-list color-utils/colors-presets-list
                       :on-change-cb #(label-actions/label-editor-update {:color (:hex %)})
                       :current-selected (:color editing-label)})
      [:div.oc-label-footer
       (when (:can-delete? editing-label)
         [:button.mlb-reset.delete-bt
          {:on-click #(delete-confirm % editing-label)}
          "Delete"])
       [:button.mlb-reset.cancel-bt
        {:on-click #(label-actions/dismiss-label-editor)}
        "Cancel"]
       [:button.mlb-reset.save-bt
        {:on-click #(label-actions/save-label)}
        "Save"]]]))

(rum/defcs org-labels-manager <
  rum/static
  rum/reactive
  (drv/drv :show-label-editor)
  (ui-mixins/on-click-out :org-labels-manager-inner (fn [s e]
    (when (and (not (dom-utils/event-cotainer-has-class e "alert-modal"))
               (not @(drv/get-ref s :show-label-editor)))
      (label-actions/hide-labels-manager))))
  refresh-labels-mixin
  [s]
  [:div.org-labels-manager.label-modal-view
   [:div.org-labels-manager-inner
    {:ref :org-labels-manager-inner}
    [:button.mlb-reset.labels-modal-close-bt
     {:on-click #(label-actions/hide-labels-manager)}]
    (if (drv/react s :show-label-editor)
      (label-editor)
      (org-labels-list))]])

(rum/defcs labels-picker <
  rum/static
  rum/reactive
  refresh-labels-mixin
  (drv/drv :user-labels)
  (drv/drv :cmail-data)
  (ui-mixins/on-click-out :labels-picker-inner (fn [_ e]
    (when-not (dom-utils/event-cotainer-has-class e "alert-modal")
      (cmail-actions/toggle-cmail-labels-views false))))
  [s]
  (let [org-labels (drv/react s :user-labels)
        cmail-data (drv/react s :cmail-data)
        label-slugs (->> cmail-data
                          :labels
                          (map :slug)
                          set)]
    [:div.labels-picker.label-modal-view
     [:div.labels-picker-inner
      {:ref :labels-picker-inner}
      [:button.mlb-reset.labels-modal-close-bt
       {:on-click #(cmail-actions/toggle-cmail-labels-views false)}]
      [:div.oc-labels
       [:div.oc-labels-title
        "Add labels"]
       (if (seq org-labels)
         (for [label org-labels]
           [:div.oc-label
            {:data-label-slug (:slug label)
             :key (str "labels-picker-" (or (:uuid label) (rand 1000)))
             :class (when (:can-edit? label)
                      "editable")
             :on-click #(cmail-actions/toggle-cmail-label label)}
            (carrot-checkbox {:selected (label-slugs (:slug label))})
            [:span.oc-label-dot
             {:style {:background-color (:color label)}}]
            [:span.oc-label-name
             (:name label)]
            (when (:can-edit? label)
              [:button.mlb-reset.edit-bt
               {:on-click (fn [e]
                            (dom-utils/stop-propagation! e)
                            (label-actions/edit-label label))}])])
         [:div.oc-labels-empty
          "No labels yet"])
       [:button.mlb-reset.add-label-bt
        {:on-click #(label-actions/new-label)}
        [:span.add-label-plus]
        [:span.add-label-span
         "Add label"]]]]]))

(rum/defc cmail-label-item <
  rum/static
  [{on-click-cb :on-click-cb
    {label-color :color label-name :name :as label} :label
    class-name :class-name
    {tooltip-title :tooltip-title tooltip-placement :tooltip-placement :or {tooltip-placement "top"}} :tooltip}]
  [:button.mlb-reset.cmail-label
   {:data-uuid (:uuid label)
    :data-slug (:slug label)
    :class class-name
    :data-toggle (when tooltip-title
                   "tooltip")
    :data-placement tooltip-placement
    :title tooltip-title
    :on-click on-click-cb}
   (when label-color
     [:div.oc-label-bg
      {:style {:background-color label-color}}])
   [:span.oc-label-text
    {:style {:color label-color}}
    label-name]])

(defn- active-label? [label labels-list]
  (let [label-set-fn #(set [(:uuid %) (:slug %)])
        label-set (label-set-fn label)
        existing-labels-set (set (apply concat (mapv label-set-fn labels-list)))]
    (seq (clj-set/intersection existing-labels-set label-set))))

(defn- show-tt [s]
  (reset! (::show-tooltip s) true))

(defn- hide-tt [s]
  (reset! (::show-tooltip s) false))

(defn- focus [s]
  (when-let [input (rum/ref-node s :label-autocomplete-input)]
    (.focus input)
    (show-tt s)))

(defn- delay-focus [s]
  (utils/after 100 #(focus s)))

(defn- update-suggestion [s act]
  (let [idx @(::suggested-idx s)
        next-idx* (cond (= act :next) (inc idx)
                        (= act :prev) (dec idx))
        next-idx (mod next-idx* (count @(::suggested-labels s)))]
    (reset! (::suggested-idx s) next-idx)
    (delay-focus s)))

(defn- reset-suggestion [s]
  (reset! (::suggested-labels s) [])
  (reset! (::query s) "")
  (hide-tt s))

(defn- select-suggestion [s label]
  (if label
    (do
      (cmail-actions/toggle-cmail-label (dissoc label :suggested-name))
      (reset-suggestion s)
      (delay-focus s)
      (show-tt s))
    (label-actions/new-label @(::query s))))

(defn- maybe-delete-suggestion [s]
  (when-not (seq @(::query s))
    (cmail-actions/cmail-label-remove-last-label)
    (show-tt s)))

(defn- label-matches? [reg label]
  (re-matches reg label))

(def regex-char-esc-smap
  (let [esc-chars "()*&^%$#!."]
    (zipmap esc-chars
            (map #(str "\\" %) esc-chars))))

(defn- ^:export escape-query
  [s]
  (->> s
       (replace regex-char-esc-smap)
       (reduce str)))

(defn- ^:export re-pattern-from-query [query start-with?]
  (re-pattern (str "(?i)^" (escape-query query)
                   (if start-with? ".*" "$"))))

(defn- labels-matching-query
  ([query labels] (labels-matching-query query labels true))
  ([query labels start-with?]
   (let [re-query (re-pattern-from-query query start-with?)]
     (filterv #(label-matches? re-query (:name %)) labels))))

(defn- suggestions-for [s query labels]
  (if (seq query)
    (let [filtered-labels (labels-matching-query query labels)
          suggested-labels (mapv #(assoc % :suggested-name (str query (subs (:name %) (count query) (count (:name %))))) filtered-labels)]
      (reset! (::suggested-labels s) suggested-labels)
      (reset! (::suggested-idx s) 0))
    (reset! (::suggested-labels s) [])))

(def ^{:private true} keys-copy "use ↑, ↓, ␛ or ⮐.")

(def ^{:private true} no-matches-tooltip-title "⮐ to create a new label.")

(def ^{:private true} no-matches-duplicate-tooltip-title "Label already added.")

(def ^{:private true} one-match-tooltip-title "1 label matches. ⮐ to select.")

(def ^{:private true} multiple-matches-tooltip-title "%d labels match, %s.")

(def ^{:private true} empty-tooltip-title "Type to find a label.")

(def ^{:private true} empty-has-labels-tooltip-title "Start typing a label name, Backspace to delete the previous label.")

(defn- label-autocomplete-on-change [s e]
  (let [q (.. e -target -value)
        args (-> s :rum/args first)
        cmail-labels (-> args :cmail-data :labels)
        user-labels (:user-labels args)
        labels-suggestions (filterv #(not (active-label? % cmail-labels)) user-labels)]
    (reset! (::query s) q)
    (suggestions-for s q labels-suggestions)
    (delay-focus s)))

(rum/defcs label-autocomplete-field <
  rum/static
  (rum/local [] ::suggested-labels)
  (rum/local 0 ::suggested-idx)
  (rum/local false ::show-tooltip)
  (rum/local "" ::query)
  ;; ui-mixins/strict-refresh-tooltips-mixin
  ;; {:did-mount (fn [s] (init-tt s) s)
  ;;  :will-unmount (fn [s] (hide-tt s) s)}
  [s {:keys [cmail-data user-labels]}]
  (let [suggested-labels @(::suggested-labels s)
        idx @(::suggested-idx s)
        suggestion (get suggested-labels idx)
        query (::query s)]
    [:div.cmail-label.cmail-label-autocomplete
     (when suggestion
       [:div.cmail-label-autocomplete-suggestion
        [:div.oc-label-bg
          {:style {:background-color (:color suggestion)}}]
        [:span.oc-label-text
          {:style {:color (:color suggestion)}}
          (:suggested-name suggestion)]])
     [:div.label-autocomplete-field-container
      (let [no-query? (string/empty-or-nil? @query)
            suggestions-num (count suggested-labels)
            query-duplicate? (seq (labels-matching-query @query (:labels cmail-data) false))]
        [:div.label-autocomplete-tooltip
         {:class (utils/class-set {:visible @(::show-tooltip s)
                                   :duplicated (and (not no-query?)
                                                    (zero? suggestions-num)
                                                    query-duplicate?)})}
         [:div.tooltip-arrow]
         [:div.tooltip-inner
          (cond (and no-query?
                     (count (:labels cmail-data)))
                empty-has-labels-tooltip-title
                no-query?
                empty-tooltip-title
                (= 1 suggestions-num)
                one-match-tooltip-title
                (and (zero? suggestions-num)
                     query-duplicate?)
                no-matches-duplicate-tooltip-title
                (zero? suggestions-num)
                no-matches-tooltip-title
                :else
                (format multiple-matches-tooltip-title suggestions-num keys-copy))]])
      [:input.label-autocomplete-field
       {:value @query
        :placeholder "Type…"
        :ref :label-autocomplete-input
        :max-length label-actions/max-label-name-length
        :on-mouse-down (fn [e]
                         (dom-utils/stop-propagation! e)
                         (delay-focus s))
        :on-change  (partial label-autocomplete-on-change s)
        :on-focus #(show-tt s)
        :on-blur #(hide-tt s)
        :on-key-up (fn [e]
                     (case (.-key e)
                       "ArrowUp"
                       (do (dom-utils/prevent-default! e)
                           (update-suggestion s :prev))
                       "ArrowDown"
                       (do (dom-utils/prevent-default! e)
                           (update-suggestion s :next))
                       "Escape"
                       (do (dom-utils/prevent-default! e)
                           (reset-suggestion s))
                       "Enter"
                       (do (dom-utils/prevent-default! e)
                           (select-suggestion s suggestion))
                       "Backspace"
                       (maybe-delete-suggestion s)
                       ;; else
                       true))}]]]))

(rum/defcs cmail-labels-list <
  rum/static
  rum/reactive
  (drv/drv :cmail-state)
  (drv/drv :cmail-data)
  (drv/drv :user-labels)
  ui-mixins/strict-refresh-tooltips-mixin
  (ui-mixins/on-click-out (fn [s _]
                            (when (:labels-inline-view @(drv/get-ref s :cmail-state))
                              (cmail-actions/toggle-cmail-inline-labels-view false))))
  [s {:keys [inline-type? add-label-bt]}]
  (let [cmail-state (drv/react s :cmail-state)
        cmail-data (drv/react s :cmail-data)
        user-labels (drv/react s :user-labels)
        is-mobile? (responsive/is-mobile-size?)]
    [:div.cmail-labels-list
     (when (:labels-inline-view cmail-state)
       (labels-picker))
     (when add-label-bt
       [:div.cmail-add-label-container
        ;; (cmail-label-item {:label add-label-map
        ;;                    :tooltip (when-not is-mobile? {:title "Click to add a label"})})
        [:button.mlb-reset.add-label-bt
         {:on-click #(if (seq user-labels)
                       (cmail-actions/toggle-cmail-inline-labels-view)
                       (label-actions/new-label))
          :data-toggle (when-not is-mobile? "toggle")
          :data-placement "top"
          :data-container "body"
          :title "Click to add a label"}
         (if (seq user-labels)
           "+ Add label"
           "+ Create label")]])
     (for [label (:labels cmail-data)]
       [:div.cmail-labels-item
        {:key (str "cmail-label-item" (or (:uuid label) (:slug label)))}
        (cmail-label-item {:label label
                           :class-name "cmail-label-item active"
                           :tooltip (when-not is-mobile? {:title "Remove label"})
                           :on-click-cb #(cmail-actions/toggle-cmail-label label)})])
     (when (and inline-type?
                (> (count user-labels) (count (:labels cmail-data))))
       [:div.cmail-labels-item
        (label-autocomplete-field {:cmail-data cmail-data
                                   :user-labels user-labels})])]))

(rum/defc label-item <
  rum/static
  [label]
  [:div.oc-label
   {:data-uuid (:uuid label)
    :data-slug (:slug label)}
   [:a
    {:href (oc-urls/label (:slug label))
     :on-click #(nav-actions/nav-to-label! % (:slug label) (oc-urls/label (:slug label)))}
    [:div.oc-label-bg
     {:style {:background-color (:color label)}}]
    [:span.oc-label-text
     {:style {:color (:color label)}}
     (:name label)]]])

(rum/defc labels-list <
  rum/static
  [labels]
  [:div.oc-labels-list
   (for [label labels]
     [:div.oc-labels-item
      {:key (str "oc-labels-item-" (or (:uuid label) (:slug label)))}
      (label-item label)])])