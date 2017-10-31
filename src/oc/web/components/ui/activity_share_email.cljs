(ns oc.web.components.ui.activity-share-email
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.mixins :as mixins]
            [oc.web.components.ui.item-input :refer (item-input email-item)]))

(defn dismiss []
  (dis/dispatch! [:activity-share-hide]))

(defn close-clicked [s]
  (reset! (::dismiss s) true)
  (utils/after 180 dismiss))

(rum/defcs activity-share-email < rum/reactive
                                  ;; Derivatives
                                  (drv/drv :activity-share)
                                  (drv/drv :activity-shared-data)
                                  ;; Locals
                                  (rum/local nil ::email-data)
                                  (rum/local false ::dismiss)
                                  mixins/no-scroll-mixin
                                  mixins/first-render-mixin
                                  {:will-mount (fn [s]
                                                 (let [activity-data (:share-data @(drv/get-ref s :activity-share))
                                                       subject (str (:name (dis/org-data))
                                                                    (when (not (empty? (:board-name activity-data))) (str " " (:board-name activity-data)))
                                                                    ": " (.text (.html (js/$ "<div />") (:headline activity-data))))]
                                                   (reset! (::email-data s) {:subject subject
                                                                             :note ""}))
                                                 s)}
  [s]
  (let [activity-data (:share-data (drv/react s :activity-share))
        email-data @(::email-data s)
        shared-data (drv/react s :activity-shared-data)
        shared? (not (empty? (:secure-uuid shared-data)))
        secure-uuid (if (:secure-uuid shared-data) (:secure-uuid shared-data) (:secure-uuid activity-data))]
    [:div.activity-share-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(:first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(:first-render-done s))})}
      [:div.modal-wrapper
        [:button.carrot-modal-close.mlb-reset
            {:on-click #(close-clicked s)}]
        [:div.activity-share-modal
          (when (not shared?)
            [:div.title "Share " [:span {:dangerouslySetInnerHTML (utils/emojify (:headline activity-data))}]])
          (when shared?
            [:div.activity-share-modal-shared
                [:img {:src (utils/cdn "/img/ML/caught_up.svg") :width 42 :height 42}]
                [:div.shared-headline "Your post has been shared!"]
                [:button.mlb-reset.mlb-default.done-btn
                  {:on-click #(close-clicked s)}
                  "Done"]])
          (when (not shared?)
            [:div.activity-share-share
              [:div.mediums-box
                [:div.medium
                  [:div.email-medium.group
                    [:div.medium-row.group
                      [:span.labels "To"]
                      [:div.fields
                        {:class (when (:to-error email-data) "error")}
                        (item-input {:item-render email-item
                                     :match-ptn #"(\S+)[,|\s]+"
                                     :split-ptn #"[,|\s]+"
                                     :container-node :div.email-field
                                     :valid-item? utils/valid-email?
                                     :on-intermediate-change #(reset! (::email-data s) (merge email-data {:to-error false}))
                                     :on-change (fn [v] (reset! (::email-data s) (merge email-data {:to v
                                                                                                    :to-error false})))})]]
                    [:div.medium-row.subject.group
                      [:span.labels "Subject"]
                      [:div.fields
                        {:class (when (:subject-error email-data) "error")}
                        [:input
                          {:type "text"
                           :value (:subject email-data)
                           :on-change #(reset! (::email-data s) (merge email-data {:subject (.. % -target -value)
                                                                                   :subject-error false}))}]]]
                    [:div.medium-row.note.group
                      [:span.labels "Add a note (optional)"]
                      [:div.fields
                        [:textarea
                          {:value (:note email-data)
                           :on-change #(reset! (::email-data s) (merge email-data {:note (.. % -target -value)}))}]]]
                    [:div.medium-row.group
                      [:div.shared-subheadline (str "Only team members can see and add comments.")]]]]]
              [:div.share-footer.group
                [:div.buttons
                  [:button.mlb-reset.mlb-black-link
                    {:on-click #(close-clicked s)}
                    "Cancel"]
                  [:button.mlb-reset.mlb-default
                    {:on-click #(let [email-share {:medium :email
                                                   :note (:note email-data)
                                                   :subject (:subject email-data)
                                                   :to (:to email-data)}]
                                  (if (empty? (:to email-data))
                                    (reset! (::email-data s) (merge email-data {:to-error true}))
                                    (dis/dispatch! [:activity-share [email-share]])))
                     :class (when (empty? (:to email-data)) "disabled")}
                    "Share"]]]])]]]))