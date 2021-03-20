(ns oc.web.components.ui.label-autocomplete
  (:require [rum.core :as rum]
            [clojure.set :as clj-set]
            [goog.string :refer (format)]
            [cuerdas.core :as string]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.actions.label :as label-actions]))

(defn- active-label? [label labels-list]
  (let [label-set-fn #(set [(:uuid %) (:slug %)])
        label-set (label-set-fn label)
        existing-labels-set (set (apply concat (mapv label-set-fn labels-list)))]
    (seq (clj-set/intersection existing-labels-set label-set))))

(defn- on-focus [s]
  (reset! (::focused s) true))

(defn- on-blur [s]
  (reset! (::focused s) false))

(defn- focus [s]
  (when-let [input (rum/ref-node s :label-autocomplete-input)]
    (.focus input)))

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
  (reset! (::query s) ""))

(defn- select-suggestion [s label]
  (if label
    (do
      (cmail-actions/toggle-cmail-label (dissoc label :suggested-name))
      (reset-suggestion s)
      (delay-focus s))
    (label-actions/new-label @(::query s))))

(defn- maybe-delete-last-label [s]
  (when-not (seq @(::query s))
    (cmail-actions/cmail-label-remove-last-label)))

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

(rum/defcs label-autocomplete <
  rum/static
  (rum/local [] ::suggested-labels)
  (rum/local 0 ::suggested-idx)
  ;; (rum/local false ::autocomplete-focused)
  (rum/local "" ::query)
  (rum/local false ::focused)
  ;; ui-mixins/strict-refresh-tooltips-mixin
  [s {:keys [cmail-data user-labels]}]
  (let [suggested-labels @(::suggested-labels s)
        idx @(::suggested-idx s)
        suggestion (get suggested-labels idx)
        query (::query s)]
    [:div.cmail-label.cmail-label-autocomplete
     {:class (utils/class-set {:sticky (or (seq @(::query s))
                                           suggestion)
                               :focused  @(::focused s)})}
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
            query-duplicate? (seq (labels-matching-query @query (:labels cmail-data) false))
            tt-copy (cond (and no-query?
                               (seq (:labels cmail-data)))
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
                          (format multiple-matches-tooltip-title suggestions-num keys-copy))]
        [:div.label-autocomplete-tooltip
         {:class (utils/class-set {:duplicated (and (not no-query?)
                                                    (zero? suggestions-num)
                                                    query-duplicate?)})}
         [:div.tooltip-arrow]
         [:div.tooltip-inner
          tt-copy]])
      [:input.label-autocomplete-field
       {:value @query
        :placeholder "Find and add labels…"
        :ref :label-autocomplete-input
        :max-length label-actions/max-label-name-length
        :on-mouse-down (fn [e]
                         (dom-utils/stop-propagation! e)
                         (delay-focus s))
        :on-change (partial label-autocomplete-on-change s)
        :on-focus #(on-focus s)
        :on-blur #(on-blur s)
        :on-key-down (fn [e]
                       (case (.-key e)
                         "ArrowUp"
                         (do (dom-utils/prevent-default! e)
                             (update-suggestion s :prev)
                             false)
                         "ArrowDown"
                         (do (dom-utils/prevent-default! e)
                             (update-suggestion s :next)
                             false)
                         "Escape"
                         (do (dom-utils/prevent-default! e)
                             (reset-suggestion s)
                             false)
                         ("Tab" "Enter")
                         (do (dom-utils/prevent-default! e)
                             (select-suggestion s suggestion))
                         "Backspace"
                         (maybe-delete-last-label s)
                         ;; else
                         true))}]]]))
