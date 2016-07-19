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
            [cljsjs.react.dom]
            [cljsjs.clipboard]))

(defn valid-email? [addr] (email/isValidAddress addr))

(defn send-clicked [type]
  (let [post-data (get-in @dis/app-state [:stakeholder-update/share type])
        emojied   (update post-data :note (fnil utils/unicode-emojis ""))]
    (dis/dispatch! [:stakeholder-update/reset-share])
    (dis/dispatch! [:stakeholder-update/validate-share type])
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

(rum/defcs multi-email-input < (rum/local nil ::invalid)
                               (rum/local nil ::input)
  [{:keys [::invalid ::input]} on-change]
  (let [invalid?     (fn [emails] (seq (remove valid-email? (string/split emails #"[,;\s]+"))))
        update!      (fn [emails]
                       (reset! input emails)
                       (let [inv (invalid? emails)]
                         (reset! invalid (and @invalid inv))
                         (on-change emails)))]
    [:div.mb3
     [:label.block.small-caps.bold.mb2
      "To"
      (cond
        (= [""] @invalid) [:span.red.py1 " — Required"]
        (seq @invalid)    [:span.red.py1 " — Not a valid email address"]) ]
     [:input.domine.npt.p1.col-12
      {:type      "text"
       :class     (when @invalid "b--red")
       :value     @input
       :on-blur   #(do (reset! invalid (invalid? @input)) true)
       :on-change #(update! (.. % -target -value))
       :placeholder "investor@vc.com, advisor@smart.com"}]]))

(rum/defc email-dialog < rum/static emoji-autocomplete
  [{:keys [share-link initial-subject]}]
  (dis/dispatch! [:input [:stakeholder-update/share :email :subject] initial-subject])
  [:div
   (modal-title "Share by Email" :email-84)
   [:div.p3
    (multi-email-input (fn [val] (dis/dispatch! [:input [:stakeholder-update/share :email :to] val])))
    [:label.block.small-caps.bold.mb2 "Subject"]
    [:input.domine.npt.p1.col-12.mb3
     {:type "text"
      :on-change #(dis/dispatch! [:input [:stakeholder-update/share :email :subject] (.. % -target -value)])
      :default-value initial-subject}]
    [:label.block.small-caps.bold.mb2 "Your Note"]
    [:textarea.domine.npt.p1.col-12.emoji-autocomplete.ta-mh
     {:type "text"
      :on-change #(dis/dispatch! [:input [:stakeholder-update/share :email :note] (.. % -target -value)])
      :placeholder "Optional note to go with this update."}]]])

(rum/defc slack-dialog < rum/static emoji-autocomplete
  []
  [:div
   (modal-title "Share to Your Slack Team" :slack)
   [:div.p3
    [:label.block.small-caps.bold.mb2 "Your Note"]
    [:textarea.domine.npt.p1.col-12.emoji-autocomplete.ta-mh
     {:type "text"
      :on-change #(dis/dispatch! [:input [:stakeholder-update/share :slack :note] (.. % -target -value)])
      :placeholder "Optional note to go with this update."}]]])

(rum/defcs link-dialog < (rum/local false ::copied)
                         (rum/local false ::clipboard)
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

(rum/defcs prompt-dialog < rum/static
 [_ prompt-cb]
 [:div
  (modal-title "Share Update")
  [:div.p3
    [:div.group.pt1
      [:button.btn-reset {:on-click #(prompt-cb :slack)}
        [:div.circle35.left [:img {:src "/img/Slack_Icon.png" :style {:width "20px" :height "20px"}}]]
        [:span.left.ml1.gray5.h6 {:style {:opacity "0.5"}} "SHARE TO SLACK"]]]
    [:div.group.pt1
      [:button.btn-reset {:on-click #(prompt-cb :email)}
        [:div.circle35.left (i/icon :email-84 {:color "rgba(78,90,107,0.6)" :accent-color "rgba(78,90,107,0.6)" :size 20})]
        [:span.left.ml1.gray5.h6 {:style {:opacity "0.5"}} "SHARE BY EMAIL"]]]
    [:div.group.pt1
      [:button.btn-reset {:on-click #(prompt-cb :link)}
        [:div.circle35.left (i/icon :link-72 {:color "rgba(78,90,107,0.6)" :accent-color "rgba(78,90,107,0.6)" :size 20})]
        [:span.left.ml1.gray5.h6 {:style {:opacity "0.5"}} "SHARE A LINK"]]]]])

;; This is very hacky and should by replaced by a more
;; versatile/generic form validation system
(def email-field
  (rum/derived-atom
   [dis/app-state]
   ::email-field
   (fn [app-state]
     (get-in app-state [:stakeholder-update/share :email :to]))))

(rum/defc modal-actions < rum/reactive
  [send-fn cancel-fn type]
  [:div.px3.pb3.right-align
   [:button.btn-reset.btn-outline
    {:class (when-not (or (= :link type) (= :prompt type)) "mr1")
     :on-click cancel-fn}
    (if (= :link type) "DONE" "CANCEL")]
   (when-not (or (= :link type) (= :prompt type))
     [:button.btn-reset.btn-solid
      {:on-click send-fn
       :disabled (if (= :email type)
                   (->> (string/split (rum/react email-field) #"[,;\s]+")
                        (every? valid-email?) not))}
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
                      (:share-via-link data)  :link
                      :else                   :prompt)
     :share-link (:latest-su data)
     :sending false
     :sent false})

  (did-mount [_]
    (utils/disable-scroll))

  (will-unmount [_]
    (utils/enable-scroll))

  (will-receive-props [_ next-props]
    ; slack SU posted
    (when (and (= (om/get-state owner :share-via) :link)
               (:latest-su next-props))
      (om/set-state! owner :share-link (:latest-su next-props)))
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
                :prompt (prompt-dialog #(do
                                          (when (= % :link)
                                            (api/share-stakeholder-update {}))
                                          (om/set-state! owner :share-via %)))
                :link   (link-dialog share-link)
                :email  (email-dialog {:initial-subject (str (:name company-data) " "  (:su-title data))
                                       :share-link      share-link})
                :slack  (slack-dialog))
              (modal-actions
                (if sent
                  cancel-fn
                  #(do (om/set-state! owner :sending true)
                       (send-clicked share-via)))
                cancel-fn
                (cond (and sending (not sent)) :sending
                      (and sending sent)       :sent
                      :else                    share-via)))))))))