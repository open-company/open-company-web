(ns open-company-web.components.su-preview-dialog
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [rum.core :as rum]
            [clojure.string :as string]
            [goog.format.EmailAddress :as email]
            [open-company-web.api :as api]
            [open-company-web.dispatcher :as dis]
            [open-company-web.router :as router]
            [open-company-web.urls :as oc-urls]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.icon :as i]
            [open-company-web.components.ui.small-loading :as loading]
            [org.martinklepsch.derivatives :as drv]
            [cljsjs.react.dom]
            [cljsjs.clipboard]))

(defn valid-email? [addr] (email/isValidAddress addr))

(defn send-clicked [type]
  (let [post-data (get-in @dis/app-state [:su-share type])
        emojied   (update post-data :note (fnil utils/unicode-emojis ""))]
    (dis/dispatch! [:su-share/reset])
    (api/share-stakeholder-update {type emojied})))

(defn select-share-link [event]
  (when-let [input (.-target event)]
    (.setSelectionRange input 0 (count (.-value input)))))

;; Rum Mixins

(def emoji-autocomplete
  {:did-mount (fn [s] (js/emojiAutocomplete) s)})

(defn clipboard-mixin [btn-selector]
  {:did-mount    (fn [s] (assoc s ::clipboard (js/Clipboard. btn-selector)))
   :will-unmount (fn [s] (.destroy (::clipboard s)) s)})

;; Modal components

(rum/defc modal-title < rum/static
  [title icon-id]
  [:h3.m0.px3.py25.gray5.domine
   {:style {:border-bottom  "solid 1px rgba(78, 90, 107, 0.1)"}}
   (if (= :slack icon-id)
     [:i {:class "fa fa-slack mr2"}]
     (i/icon icon-id {:class "inline mr2"
                      :color :oc-gray-3
                      :accent-color :oc-gray-3}))
   title])

(rum/defcs item-input
  "An input that accepts multiple items of things

  Options
  :item-render Should be a function receiving three arguments: the item,
               a function to delete the item and a boolean field to indicate
               if this item is valid or not
  :on-change is called with the list of items whenever it updates this may include
             invalid items which are currently being typed out
  :submitted (optional) can be used to extract a 'submitted' value from the input
             by default a value is submitted if it ends with one or more whitespace
             characters, the return value should be sanitized (e.g. trimmed)
  :valid-item? (optional) Takes a value returned by `submitted` and returns true
              if it is valid otherwise false
  :container-node (default :div) Provide a different node for the container
  :input-node (default :input) Provide a different node for the input field"
  < (rum/local true ::show-input?) (rum/local [] ::items) (rum/local "" ::input)
  [s {:keys [item-render on-change submitted
             valid-item? container-node input-node]
      :or {valid-item? identity
           submitted (fn [v] (second (first (re-seq #"(\S+)\s+" v))))
           container-node :div
           input-node :input}}]
  (let [*items       (::items s)
        *input       (::input s)
        *show-input? (::show-input? s)
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
                         (do (reset! *input v)
                             (on-change (into @*items (when-not (string/blank? v) [v]))))))]
    [container-node
     {:on-click #(reset! *show-input? true)}
     (for [e @*items]
       (rum/with-key (item-render e #(remove-item! e) true) e))
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
         :placeholder (when-not (seq @*items) "investor@vc.com advisor@smart.com")
         :auto-focus true
         :value      @*input
         :on-key-down #(do
                         (when (and (= 8 (.-keyCode %)) (empty? @*input))
                           (on-change (swap! *items (comp vec drop-last)))))
         :on-blur   #(do (submit! (.. % -target -value))
                         (reset! *show-input? false)
                         nil)
         :on-change #(maybe-submit (.. % -target -value))}])]))

(rum/defc email-item [v delete! submitted?]
  [:div.inline-block.mr1.mb1.rounded
   {:class (when-not submitted? "border b--red")
    :style (when submitted? {:backgroundColor "rgba(78, 90, 107, 0.1)"})}
   [:span.inline-block.p1 v
    [:button.btn-reset.p0.ml1
     {:on-click #(delete!)}
     "x"]]])

(rum/defcs email-dialog < rum/static rum/reactive (dis/drv :su-share) emoji-autocomplete
  [s {:keys [share-link]}]
  [:div
   (modal-title "Share by Email" :email-84)
   [:div.p3
    [:div
     [:label.block.small-caps.bold.mb2
      "To"
      (let [to-field (->> (drv/react s :su-share) :email :to)]
        (cond
          (not (seq to-field))
          [:span.red.py1 " — Required"]
          (not (every? valid-email? to-field))
          [:span.red.py1 " — Not a valid email address"]))]
     (item-input {:item-render email-item
                  :container-node :div.npt.pt1.pr1.pl1.mb3.mh4.overflow-scroll
                  :input-node :input.border-none.outline-none.mr.mb1
                  :valid-item? valid-email?
                  :on-change (fn [val] (dis/dispatch! [:input [:su-share :email :to] val]))})]
    [:label.block.small-caps.bold.mb2 "Subject"]
    [:input.domine.npt.p1.col-12.mb3
     {:type "text"
      :on-change #(dis/dispatch! [:input [:su-share :email :subject] (.. % -target -value)])
      :value (-> (drv/react s :su-share) :email :subject)}]
    [:label.block.small-caps.bold.mb2 "Your Note"]
    [:textarea.domine.npt.p1.col-12.emoji-autocomplete.ta-mh
     {:type "text"
      :on-change #(dis/dispatch! [:input [:su-share :email :note] (.. % -target -value)])
      :value (-> (drv/react s :su-share) :email :note)
      :placeholder "Optional note to go with this update."}]]])

(rum/defc slack-dialog < rum/static emoji-autocomplete
  []
  [:div
   (modal-title "Share to Your Slack Team" :slack)
   [:div.p3
    [:label.block.small-caps.bold.mb2 "Your Note"]
    [:textarea.domine.npt.p1.col-12.emoji-autocomplete.ta-mh
     {:type "text"
      :on-change #(dis/dispatch! [:input [:su-share :slack :note] (.. % -target -value)])
      :placeholder "Optional note to go with this update."}]]])

(rum/defcs link-dialog < (rum/local false ::copied)
                         (clipboard-mixin ".js-copy-btn")
  [{:keys [::copied] :as _state} link]
  [:div
   (modal-title  "Share a Link" :link-72)
   [:div.p3
    [:label.block.small-caps.bold.mb2 "Share this private link"]
    [:div.flex
     [:input.domine.npt.p1.flex-auto
      {:type "text"
       :id "share-link-input"
       :on-focus select-share-link
       :on-key-up select-share-link
       :value link}]
     [:button {:class "btn-reset btn-solid js-copy-btn"
               :data-clipboard-target "#share-link-input"
               :on-click #(reset! copied true)}
      (if @copied "COPIED ✓" "COPY")]]
    [:div.block.mt2
     [:a.small-caps.underline.bold.dimmed-gray
      {:href link :target "_blank"}
      "Open in New Window"]]]])

;; This is very hacky and should by replaced by a more
;; versatile/generic form validation system
(def email-field
  (rum/derived-atom
   [dis/app-state]
   ::email-field
   (fn [app-state]
     (get-in app-state [:su-share :email :to]))))

(rum/defcs modal-actions < rum/reactive (dis/drv :su-share)
  [s send-fn cancel-fn type]
  [:div.px3.pb3.right-align
   [:button.btn-reset.btn-outline
    {:class (when-not (= :link type) "mr1")
     :on-click cancel-fn}
    (if (= :link type) "DONE" "CANCEL")]
   (when-not (= :link type)
     [:button.btn-reset.btn-solid
      {:on-click send-fn
       :disabled (when (= :email type)
                   (let [to (->> (drv/react s :su-share) :email :to)]
                     (not (and (seq to) (every? valid-email? to)))))}
      (case type
        :sent "SENT ✓"
        :sending (loading/small-loading {})
        "Send")])])

(rum/defc confirmation < rum/static
  [type]
  [:div
   (case type
     :email (modal-title "Email Sent!" :email-84)
     :slack (modal-title "Shared via Slack!" :link-72))
   [:div.p3
    [:p.domine
     (case type
       :email "Recipients will get your update by email."
       :slack "Members of your Slack organization will get your update.")]
    [:div.right-align.mt3
     [:button.btn-reset.btn-solid
      {:on-click #(router/nav! (oc-urls/company))}
      "Back to your dashboard →"]]]])


(defcomponent su-preview-dialog [data owner options]

  (init-state [_]
    {:share-via (cond (:share-via-email data) :email
                      (:share-via-slack data) :slack
                      (:share-via-link data)  :link)
     :share-link (:latest-su data)
     :sending false
     :sent false})

  (did-mount [_]
    (dis/dispatch! [:input [:su-share :email :subject]
                    (str "[" (:name (:company-data data)) "] " (:su-title data))])
    (utils/disable-scroll))

  (will-unmount [_]
    (utils/enable-scroll))

  (will-receive-props [_ next-props]
    ; slack SU posted
    (when (and (#{:email :slack} (om/get-state owner :share-via))
               (om/get-state owner :sending)
               (not (om/get-state owner :sent)))
      (om/set-state! owner :sent true)))

  (render-state [_ {:keys [share-via share-link sending sent] :as state}]
    (let [company-data (:company-data data)
          cancel-fn    (:dismiss-su-preview options)]
      (dom/div {:class "su-preview-dialog"}
        (dom/div {:class "su-preview-window"}
          (dom/button
              {:class "absolute top-0 btn-reset" :style {:left "100%"}
               :on-click #(cancel-fn)}
            (i/icon :simple-remove {:class "inline mr1" :stroke "4" :color "white" :accent-color "white"}))
          (if sent
            (confirmation share-via)
            (dom/div
              (case share-via
                :link  (link-dialog share-link)
                :email (email-dialog {:share-link share-link})
                :slack (slack-dialog))
              (modal-actions
               (if sent
                 cancel-fn
                 #(do (om/set-state! owner :sending true)
                      (send-clicked share-via)))
               cancel-fn
               (cond (and sending (not sent)) :sending
                     (and sending sent)       :sent
                     :else                    share-via)))))))))
