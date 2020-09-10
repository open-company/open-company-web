(ns oc.web.components.ui.poll
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.poll :as poll-utils]
            [oc.web.actions.poll :as poll-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.ui.alert-modal :as alert-modal]))

(defn- get-dispatch-key [poll-key]
  (subvec poll-key 0 (- (count poll-key) 2)))

(rum/defcs poll-read < rum/static
                       (rum/local false ::animate)
                       (rum/local "" ::new-reply)
                       (rum/local false ::adding-reply)
                       {:did-mount (fn [s]
                        (utils/after 180 #(reset! (::animate s) true))
                        s)}
  [s {:keys [poll-data poll-key current-user-id]}]
  (let [can-vote? (and (seq current-user-id)
                       (not (:preview poll-data)))
        user-voted? (and (not @(::adding-reply s))
                         (seq (map #(= current-user-id %) (mapcat :votes (:replies poll-data)))))
        total-votes-count (reduce #(-> %2 :votes count (+ %1)) 0 (-> poll-data :replies vals))]
    [:div.poll.poll-read
      {:class (utils/class-set {:voted user-voted?})}
      [:div.poll-question
        [:div.poll-question-body
          (:question poll-data)]
        [:div.poll-total-count
          (str total-votes-count " vote" (when (not= total-votes-count 1) "s"))]]
      [:div.poll-replies
        {:class (utils/class-set {:can-vote can-vote?
                                  :animate @(::animate s)})}
        (for [reply (poll-utils/sorted-replies poll-data)
              :when (seq (string/trim (:body reply)))
              :let [votes-percent (-> reply :votes count (/ total-votes-count) (* 100))
                    rounded-votes-percent (if (js/isNaN votes-percent)
                                            0
                                            (/ (.round js/Math (* 10 votes-percent)) 10))
                    reply-voted? (and can-vote?
                                      (some #(when (= % current-user-id) %) (:votes reply)))]]
          [:div.poll-reply-outer.group
            {:key (str "poll-" (:poll-uuid poll-data) "-reply-" (:reply-id reply))
             :class (utils/class-set {(str "percent-" (.round js/Math rounded-votes-percent)) true
                                      :voted (and (not @(::adding-reply s))
                                                  reply-voted?)})}
            [:button.mlb-reset.poll-reply
              {:type "button"
               :on-click (when can-vote?
                           #(if reply-voted?
                              (poll-actions/unvote-reply poll-data poll-key (:reply-id reply))
                              (poll-actions/vote-reply poll-data poll-key (:reply-id reply))))}
              [:div.poll-reply-body
                (:body reply)]]
            [:div.poll-reply-count
              (str rounded-votes-percent "%")]])]
      (when (or (and (:can-add-reply poll-data)
                     can-vote?)
                (:preview poll-data))
          (if @(::adding-reply s)
            [:div.poll-reply-new
              [:div.poll-reply.group
                [:input.poll-reply-body
                  {:type "text"
                   :ref :new-reply
                   :value @(::new-reply s)
                   :max-length poll-utils/max-reply-length
                   :placeholder "Press Enter to add or Escape to cancel"
                   :on-change #(reset! (::new-reply s) (.. % -target -value))
                   :on-key-up (fn [e]
                               (when (and (or (= (.-key e) "Enter")
                                              (= (.-keyCode e) 13))
                                          (seq @(::new-reply s)))
                                 (poll-actions/add-new-reply poll-data poll-key @(::new-reply s))
                                 (reset! (::new-reply s) "")
                                 (reset! (::adding-reply s) false))
                               (when (or (= (.-key e) "Escape")
                                         (= (.-keyCode e) 27))
                                 (reset! (::new-reply s) "")
                                 (reset! (::adding-reply s) false)))}]
                [:button.mlb-reset.delete-reply.read-poll
                  {:type "button"
                   :on-click #(reset! (::adding-reply s) false)}]]]
            [:div.poll-reply-new
              (when (:can-add-reply poll-data)
                [:button.mlb-reset.poll-reply-new
                  {:type "button"
                   :on-click (fn [e]
                              (reset! (::adding-reply s) true)
                              (utils/after 800
                               #(when-let [new-input (rum/ref-node s :new-reply)]
                                  (.focus new-input))))}
                  "Add option"])
              (when (:preview poll-data)
                [:button.mlb-reset.poll-preview-bt
                  {:type "button"
                   :on-click #(poll-actions/hide-preview poll-key)}
                  "Edit poll"])]))]))

(defn- add-option [s poll-key poll-data]
  (poll-actions/add-reply poll-key)
  (utils/after 400
   #(when-let [last-option (rum/ref-node s (str "choice-" (inc (count (:replies poll-data)))))]
      (.focus last-option))))

(rum/defcs poll-edit < rum/static
                       (rum/local "" ::new-reply)
                       (rum/local nil ::tab-index-base)
                       {:will-mount (fn [s]
                        (reset! (::tab-index-base s) (int (rand 1000)))
                        s)
                        :did-mount (fn [s]
                          (when-let [q-el (rum/ref-node s :question)]
                            (.focus q-el))
                        s)}
  [s {:keys [poll-data poll-key current-user-id] :as props}]
  (let [should-show-delete-reply? (-> poll-data :replies count (> poll-utils/min-poll-replies))
        is-mobile? (responsive/is-mobile-size?)
        tab-index-base @(::tab-index-base s)]
    (if (:preview poll-data)
      (poll-read props)
      [:div.poll
        [:form
          {:on-submit #(utils/event-stop %)
           :action "."}
          [:button.mlb-reset.delete-poll-bt
            {:type "button"
             :on-click (fn [e]
                         (let [alert-data {:icon "/img/ML/trash.svg"
                                           :action "delete-poll"
                                           :message "Are you sure you want to delete this poll? This can’t be undone."
                                           :link-button-title "Keep"
                                           :link-button-cb #(alert-modal/hide-alert)
                                           :solid-button-style :red
                                           :solid-button-title "Yes"
                                           :solid-button-cb (fn []
                                                             (poll-actions/remove-poll (get-dispatch-key poll-key) poll-data)
                                                             (alert-modal/hide-alert))}]
                           (alert-modal/show-alert alert-data)))
             :data-toggle (when-not is-mobile? "tooltip")
             :data-placement "top"
             :data-container "body"
             :title "Remove poll"}]
          [:div.poll-question-label
            "What would you like to ask?"]
          [:input.poll-question
            {:type "text"
             :ref :question
             :max-length poll-utils/max-question-length
             :tab-index tab-index-base
             :placeholder "Ask your question..."
             :value (:question poll-data)
             :on-change #(poll-actions/update-question poll-key poll-data (.. % -target -value))}]
          (let [idx (atom 0)]
            (for [reply (poll-utils/sorted-replies poll-data)
                  :let [idx (swap! idx inc)
                        last-option? (= idx (count (:replies poll-data)))]]
              [:div.poll-reply.group
                {:key (str "poll-" (:poll-uuid poll-data) "-reply-" (:reply-id reply))}
                [:div.poll-reply-label
                  (str "Choice " idx)]
                [:input.poll-reply-body
                  {:type "text"
                   :tab-index (+ tab-index-base idx)
                   :value (:body reply)
                   :ref (str "choice-" idx)
                   :max-length poll-utils/max-reply-length
                   :placeholder (str "Choice " idx)
                   :on-key-down (fn [e]
                                   (when (and last-option?
                                              (= (.-keyCode e) 9)
                                              (not (.-shiftKey e)))
                                     (utils/event-stop e)
                                     (add-option s poll-key poll-data)))
                   :on-key-press (fn [e]
                                   (when (or (= (.-key e) "Enter")
                                             (= (.-keyCode e) 13))
                                     (utils/event-stop e)))
                   :on-change #(poll-actions/update-reply poll-key (:reply-id reply) (.. % -target -value))}]
                (when should-show-delete-reply?
                  [:button.mlb-reset.delete-reply
                    {:type "button"
                     :on-click #(poll-actions/delete-reply poll-key (:reply-id reply))}])]))]
        [:div.poll-reply-new
          [:button.mlb-reset.poll-reply-new
            {:type "button"
             :on-click (partial add-option s poll-key poll-data)}
            "Add option"]
          [:button.mlb-reset.poll-preview-bt
            {:type "button"
             :on-click #(poll-actions/show-preview poll-key)}
            "Show preview"]]])))

(rum/defcs poll < rum/static
                  {:will-mount (fn [s]
                   ;; Remove every previous poll with this same ID
                   (let [args (-> s :rum/args first)]
                    (.remove (js/$ (str (:container-selector args) " .oc-poll-" (-> args :poll-data :poll-uuid)))))
                   s)}
  [s {:keys [editing? poll-data] :as props}]
  [:div.oc-poll-container
    {:class (str "oc-poll-" (:poll-uuid poll-data))
     :key (str "oc-poll-" (:poll-uuid poll-data))
     :content-editable false}
    (if editing?
      (poll-edit props)
      (poll-read props))])

(defonce max-mount-retry 5)

(rum/defcs poll-portal < rum/static
                         (rum/local 0 ::retry)
                         (rum/local false ::mounted)
                         {:will-mount (fn [s]
                           ;; Set mounted true in will-mount since the first
                           ;; after-render is called before did-mount
                           (reset! (::mounted s) true)
                           s)
                          :after-render (fn [s]
                          (let [retry @(::retry s)]
                            (utils/after (* 180 (inc retry))
                             #(let [retry? (when @(::mounted s)
                                             (not (rum/dom-node s)))]
                                (when (and retry?
                                           (< retry max-mount-retry))
                                  (swap! (::retry s) inc))
                                (when (= retry max-mount-retry)
                                  ;; Removing poll from activity
                                  (let [props (-> s :rum/args first)]
                                    ;; There is a poll in the activity but not an element to be mounted to...
                                    (poll-utils/report-unmounted-poll props)
                                    ;; Should remove poll from entry?
                                    ; (poll-actions/remove-poll-for-max-retry (:poll-key props) (:poll-data props))
                                    )))))
                          s)
                          :did-remount (fn [_ s]
                           (reset! (::retry s) 0)
                           s)
                          :will-unmount (fn [s]
                           (reset! (::mounted s) false)
                           s)}
  [s {:keys [poll-portal-selector poll-data poll-key container-selector activity-data] :as props}]
  (when-let [portal-element (sel1 [container-selector poll-portal-selector])]
    (rum/portal (poll props) portal-element)))

(rum/defc polls-wrapper < rum/static
  [{:keys [polls-data editing? dispatch-key current-user-id container-selector activity-data]}]
  (for [[poll-uuid-k poll] polls-data
        :let [poll-uuid (name poll-uuid-k)
              poll-selector (str "." poll-utils/poll-selector-prefix poll-uuid)]]
    (rum/with-key
     (poll-portal {:poll-data poll
                   :editing? editing?
                   :container-selector container-selector
                   :current-user-id current-user-id
                   :poll-key (vec (concat (if (coll? dispatch-key) dispatch-key [dispatch-key]) [:polls poll-uuid-k]))
                   :poll-portal-selector poll-selector
                   :activity-data activity-data})
     (str poll-utils/poll-selector-prefix poll-uuid))))
