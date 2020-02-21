(ns oc.web.components.ui.poll
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.poll :as poll-utils]
            [oc.web.actions.poll :as poll-actions]))

;; Poll test

; (poll-portal {:portal-selector "div.poll-test"
;               :editing? false
;               :can-add-reply? true
;               :current-user-id "1234-1234-1235"
;               :poll-data {:question "Blah blah blahas djnas jnda lknsadl knsadlk nlasdk nkasn kldn lksandl knalik ndpojdaop ndn jkldan lan ina ikn dloikna oiln lion"
;                           :total-votes-count 51
;                           :replies [{:body "Reply 1 asn djkan kjdaj kba bkadjb kjabd khjsabiuladbu bajkl bdliuab diuab iubvilay vdlyavuyavduyhj vauydviauyd biua bhdiua bnuia nuiadn iulabn luiab abilu "
;                                      :votes-count 3
;                                      :votes ["1234-1234-1234" "1234-1234-1235" "1234-1234-1236"]}
;                                     {:body "Reply 2"
;                              poll-data        :votes-count 1
;                                      :votes ["1234-1234-1237"]}
;                                     {:body "Reply 3"
;                                      :votes-count 45
;                                      :votes ["1234-1234-1234" "1234-1234-1234" "1234-1234-1234"]}]}})
; [:div.poll-test]

(rum/defcs poll-edit < rum/static
                       (rum/local "" ::new-reply)
  [s {:keys [poll-data poll-key current-user-id]}]
  (let [should-show-delete-reply? (> (count (:replies poll-data)) poll-utils/min-poll-replies)]
    [:div.poll
      {:content-editable false}
      [:form
        {:on-submit #(utils/event-stop %)
         :action "."}
        [:div.poll-question-label
          "What would you like to ask?"]
        [:input.poll-question
          {:type "text"
           :tab-index "1"
           :placeholder "Ask your question..."
           :value (:question poll-data)
           :on-change #(poll-actions/update-question poll-key (.. % -target -value))}]
        (for [idx (range (count (:replies poll-data)))
              :let [reply (get (:replies poll-data) idx)]]
          [:div.poll-reply.group
            {:key (str "poll-" (:poll-uuid poll-data) "-reply-" (:reply-id reply))}
            [:div.poll-reply-label
              (str "Choice " (inc idx))]
            [:input.poll-reply-body
              {:type "text"
               :tab-index (+ idx 2)
               :value (:body reply)
               :placeholder (str "Choice " (inc idx))
               :on-change #(poll-actions/update-reply poll-key idx (.. % -target -value))}]
            (when should-show-delete-reply?
              [:button.mlb-reset.delete-reply
                {:on-click #(poll-actions/delete-reply poll-key (:reply-id reply))}])])]
      [:div.poll-reply-new
        [:button.mlb-reset.poll-reply-new
          {:on-click #(poll-actions/add-reply poll-key)}
          "Add option"]]]))

(rum/defcs poll-read < rum/static
                       (rum/local "" ::new-reply)
                       (rum/local false ::adding-reply)
  [s {:keys [poll-data poll-key current-user-id]}]
  (let [user-voted? (and (not @(::adding-reply s))
                         (seq (map #(= current-user-id %) (mapcat :votes (:replies poll-data)))))]
    [:div.poll
      {:content-editable false
       :class (when user-voted? "voted")}
      [:div.poll-question
        [:div.poll-question-body
          (:question poll-data)]
        [:div.poll-total-count
          (str (:total-votes-count poll-data) " vote" (when (not= (:total-votes-count poll-data) 1) "s"))]]
      [:div.poll-replies
        (for [reply (:replies poll-data)
              :let [votes-percent (* (/ (:votes-count reply) (:total-votes-count poll-data)) 100)
                    rounded-votes-percent (if (js/isNaN votes-percent)
                                            0
                                            (/ (.round js/Math (* 100 votes-percent)) 100))
                    reply-voted? (some #(when (= % current-user-id) %) (:votes reply))]]
          [:div.poll-reply-outer.group
            {:key (str "poll-" (:poll-uuid poll-data) "-reply-" (:reply-id reply))}
            [:button.mlb-reset.poll-reply
              {:class (when (and (not @(::adding-reply s))
                                 reply-voted?)
                        "voted")
               :on-click #(if reply-voted?
                            (poll-actions/unvote-reply poll-data poll-key (:reply-id reply))
                            (poll-actions/vote-reply poll-data poll-key (:reply-id reply)))}
              [:div.poll-reply-body
                (:body reply)]]
            [:div.poll-reply-count
              (str rounded-votes-percent "%")]])
        (when (:can-add-reply poll-data)
          [:div.poll-reply-new
            (if @(::adding-reply s)
              [:div.poll-reply.group
                [:input.poll-reply-body
                  {:type "text"
                   :value @(::new-reply s)
                   :placeholder (str "Choice " (-> poll-data :replies count inc))
                   :on-change #(reset! (::new-reply s) (.. % -target -value))
                   :on-blur #(when-not (seq @(::new-reply s))
                               (reset! (::adding-reply s) false))
                   :on-key-press (fn [e]
                                   (when (or (= (.-key e) "Enter")
                                             (= (.-keyCode e) 13))
                                     ;; Add reply!
                                     (poll-actions/add-reply poll-key))
                                   (when (or (= (.-key e) "Escape")
                                             (= (.-keyCode e) 27))
                                     (reset! (::adding-reply s) false)))}]
                [:button.mlb-reset.delete-reply.read-poll
                  {:on-click #(reset! (::adding-reply s) false)}]]
              [:button.mlb-reset.poll-reply-new
                {:on-click #(reset! (::adding-reply s) true)}
                "Add option"])])]]))

(rum/defcs poll < rum/static
                  {:will-mount (fn [s]
                   ;; Remove every previous poll with this same ID
                   (let [args (-> s :rum/args first)]
                    (.remove (js/$ (str (:container-selector args) " .oc-poll-" (-> args :poll-data :poll-uuid)))))
                   s)}
  [s {:keys [editing? poll-data] :as props}]
  [:div.oc-poll-container
    {:class (str "oc-poll-" (:poll-uuid poll-data))
     :key (str "oc-poll-" (:poll-uuid poll-data))}
    (if editing?
      (poll-edit props)
      (poll-read props))])

(defonce max-mount-retry 5)

(rum/defcs poll-portal < rum/static
                         (rum/local 0 ::retry)
                         {:after-render (fn [s]
                          (let [retry? (not (rum/dom-node s))
                                retry @(::retry s)]
                            (when (and retry?
                                       (< retry max-mount-retry))
                              (utils/after (* 180 (inc retry)) #(swap! (::retry s) inc)))
                            (when (= retry max-mount-retry)
                              ;; Removing poll from activity
                              (let [props (-> s :rum/args first)]
                                (poll-actions/remove-poll-for-max-retry (:poll-key props) (:poll-data props)))))
                          s)
                          :did-remount (fn [_ s]
                           (reset! (::retry s) 0)
                           s)}
  [s {:keys [poll-portal-selector poll-data poll-key container-selector] :as props}]
  (when-let [portal-element (sel1 [container-selector poll-portal-selector])]
    (rum/portal (poll props) portal-element)))

(rum/defc polls-wrapper < rum/static
  [{:keys [polls-data editing? dispatch-key current-user-id container-selector]}]
  (for [idx (range (count polls-data))
        :let [poll (get polls-data idx)
              poll-selector (str "." poll-utils/poll-selector-prefix (:poll-uuid poll))]]
    (rum/with-key
     (poll-portal {:poll-data poll
                   :editing? editing?
                   :container-selector container-selector
                   :current-user-id current-user-id
                   :poll-key (vec (concat (if (coll? dispatch-key) dispatch-key [dispatch-key]) [:polls idx]))
                   :poll-portal-selector poll-selector})
     (str "oc-poll-portal-" (:poll-uuid poll)))))
