(ns open-company-web.components.add-topic
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.caches :as caches]
            [open-company-web.components.ui.icon :as i]))

;; This should be defined as a derivative specifically suited to rendering the
;; topic adding component
(defn get-all-sections
  "Return all sections for the current company no matter it's state (archived, active, inactive)
   e.g {:section a-string :title a-string}"
  []
  (let [company-data (dis/company-data)
        slug         (keyword (router/current-company-slug))]
    (into
      (:archived company-data)
      (for [sec (:new-sections (get @caches/new-sections slug))]
        (-> sec
            (assoc :section (:section-name sec))
            (select-keys [:title :section]))))))

(defn get-categories []
  (let [slug       (keyword (router/current-company-slug))
        categories (-> @caches/new-sections slug :categories)]
    {:1 (sort #(compare (:order %1) (:order %2)) (filter #(= (:column %) 1) categories))
     :2 (sort #(compare (:order %1) (:order %2)) (filter #(= (:column %) 2) categories))}))

(rum/defcs custom-topic-input
  < (rum/local "" ::topic-title)
  [s submit-fn]
  (let [add-disabled (clojure.string/blank? @(::topic-title s))]
    [:div.mt1.flex
     [:input.npt.mr1.p1.flex-auto
      {:type "text",
       :value @(::topic-title s)
       :max-length 20
       :on-change #(reset! (::topic-title s) (.. % -target -value))
       :style {:font-size "16px"}
       :placeholder "Custom topic"}]
     [:button
      {:class (str "btn-reset" (if add-disabled " btn-outline" " btn-solid"))
       :disabled add-disabled
       :on-click #(let [topic-name     (str "custom-" (utils/my-uuid))
                        new-topic-data {:title @(::topic-title s)
                                        :section topic-name
                                        :placeholder true}]
                    (submit-fn topic-name new-topic-data))} "Add"]]))

(rum/defcs add-topic < (drv/drv :company-data)
                       rum/static
                       rum/reactive
  [s update-active-topics-cb]
  (let [company-data (drv/react s :company-data)
        active-topics (vec (map keyword (:sections company-data)))
        archived-topics (:archived company-data)
        all-sections (into {} (for [s (get-all-sections)]
                                [(keyword (:section s)) s]))
        categories (get-categories)
        show-archived (pos? (count (:archived company-data)))]
      [:div.add-topic.group
       [:span.dimmed-gray.btn-reset.right
         {:on-click #(dis/dispatch! [:show-add-topic false])}
         (i/icon :simple-remove {:color "rgba(78, 90, 107, 0.8)" :size 16 :stroke 8 :accent-color "rgba(78, 90, 107, 1.0)"})]
       [:div.mxn2.clearfix
        ;; column 1
        [:div.col.px2
          {:class (if show-archived "col-4" "col-6")}
          (for [cat (:1 categories)]
            [:div
              [:span.block.mb1.mt2.all-caps
                {:key (str "col-" (:name cat))}
                (str (:name cat) (when (:icon cat) " "))
                (when (:icon cat)
                  [:i.fa {:class (:icon cat)}])]
              (for [sec (:sections cat)
                    :let [section-data (get all-sections (keyword sec))
                          disabled? (or (utils/in? (:sections company-data) sec)
                                        (utils/in? (:archived company-data) sec))]]
                [:div.mb1.btn-reset.yellow-line-hover-child
                  {:key (:section section-data)
                   :class (when disabled? "disabled")
                   :on-click #(when-not disabled?
                               (update-active-topics-cb (:section section-data) {:title (:title section-data) :section (:section section-data) :placeholder true}))}
                  [:span.child (:title section-data)]])])]
        ;; column 2
        [:div.col.px2
          {:class (if show-archived "col-4" "col-5")}
          (for [cat (:2 categories)]
            [:div
              [:span.block.mb1.mt2.all-caps
                {:key (str "col-" (:name cat))}
                (str (:name cat) (when (:icon cat) " "))
                (when (:icon cat)
                  [:i.fa {:class (:icon cat)}])]
              (for [sec (:sections cat)
                    :let [section-data (get all-sections (keyword sec))
                          disabled? (or (utils/in? (:sections company-data) sec)
                                        (utils/in? (:archived company-data) sec))]]
                [:div.mb1.btn-reset.yellow-line-hover-child
                  {:key (:section section-data)
                   :class (when disabled? "disabled")
                   :on-click #(when-not disabled?
                               (update-active-topics-cb (:section section-data) {:title (:title section-data) :section (:section section-data) :placeholder true}))}
                  [:span.child (:title section-data)]])])]
        ;; column 3 - archived only
        (if show-archived
          [:div.col.col-4.px2
            {:style {:margin-top "-16px"}}
            [:span.block.mb1.mt2.all-caps
              {:key (str "col-archived")}
              "Archived"]
            (for [sec (:archived company-data)
                  :let [section-data (get all-sections (keyword (:section sec)))]]
              [:div.mb1.btn-reset.yellow-line-hover-child
                {:key (:section section-data)
                 :on-click #(update-active-topics-cb (:section section-data) {:title (:title section-data) :section (:section section-data)})}
                [:span.child
                  (:title section-data)]])])]
       (custom-topic-input #(update-active-topics-cb %1 %2))]))