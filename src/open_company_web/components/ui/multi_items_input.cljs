(ns open-company-web.components.ui.multi-items-input
  (:require [rum.core :as rum]
            [clojure.string :as string]))

;; TODO (mk) Revert the following commit to restore on-change behavior as described by docstring
;; https://github.com/open-company/open-company-web/pull/161/commits/eb4b29ead8e45388c8edb48d74801b2ba16c1765
(rum/defcs item-input
  "An input that accepts multiple items of things

  Options
  :item-render Should be a function receiving three arguments: the item,
               a function to delete the item and a boolean field to indicate
               if this item is valid or not
  :on-change is called with the list of items whenever it updates this may include
             invalid items which are currently being typed out
  :match-ptn regex-pattern will be used to extract a 'submitted' value from the input
  :split-ptn regex-pattern will be used to split a string which might contain multiple values
             used to match items in pasted strings
  :tab-index (default 0) sets tab-index on container node
  :valid-item? (optional) Takes a value returned by `submitted` and returns true
               if it is valid otherwise false
  :container-node (default :div) Provide a different node for the container
  :input-node (default :input) Provide a different node for the input field"
  < (rum/local true ::show-input?) (rum/local [] ::items) (rum/local "" ::input)
  [s {:keys [item-render on-change match-ptn split-ptn tab-index
             valid-item? container-node input-node]
      :or {valid-item? identity
           tab-index 0
           container-node :div
           input-node :input}}]
  (let [*items       (::items s) ; tracking items already entered
        *input       (::input s) ; tracking value of input field
        *show-input? (::show-input? s)
        submitted    (fn [v] (second (first (re-seq match-ptn v))))
        remove-item! (fn [v]
                       (on-change (swap! *items #(filterv (comp not #{v}) %))))
        clear-input! (fn [] (reset! *input "") (on-change @*items))
        submit!      (fn [v]
                       (when (valid-item? v)
                         (clear-input!)
                         (on-change (swap! *items #(vec (distinct (conj % v)))))))
        maybe-submit (fn [v]
                       (if-let [s' (submitted v)]
                         (submit! s')
                         (reset! *input v)))]
    [container-node
     {:on-click #(reset! *show-input? true)
      :on-focus #(reset! *show-input? true)
      :tab-index tab-index}
     (for [e @*items]
       (rum/with-key (item-render e #(remove-item! e) (valid-item? e)) e))
     (cond
       ;; Render the current input as invalid item
       (and (not @*show-input?) (not (string/blank? @*input)))
       (item-render @*input clear-input! false)

       ;; Render an input to maintain same spacing
       (and (not @*show-input?) (not (seq @*items)))
       [:div {:style {:visibility "hidden" :pointer-events "none"}} [input-node {:class "col-12"}]]

       ;; Render actual input to add new items
       @*show-input?
       [input-node
        {:type      "text"
         :class     (when-not (seq @*items) "col-12")
         :placeholder (when-not (seq @*items) "investor@vc.com,advisor@smart.com")
         :auto-focus true
         :value      @*input
         :on-paste   #(let [pasted (string/split (.getData (.-clipboardData %) "Text") split-ptn)]
                        (.stopPropagation %)
                        (on-change (swap! *items into pasted)))
         :on-key-down #(when (and (= 8 (.-keyCode %)) (empty? @*input))
                         (on-change (swap! *items (comp vec drop-last))))
         :on-blur   (fn [e]
                      (when-not (string/blank? (.. e -target -value))
                        (clear-input!)
                        (on-change (swap! *items #(vec (distinct (conj % (.. e -target -value)))))))
                      (when (seq @*items) (reset! *show-input? false))
                      nil)
         :on-change #(maybe-submit (.. % -target -value))}])]))

(rum/defc email-item [v delete! submitted?]
  [:div.inline-block.mr1.mb1.rounded.email-item
   {:class (if submitted? "submitted" "border b--red")}
   [:span.inline-block.p1 v
    [:button.btn-reset.p0.ml1
     {:on-click #(delete!)}
     "x"]]])