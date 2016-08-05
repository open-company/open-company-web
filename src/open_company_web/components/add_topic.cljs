(ns open-company-web.components.add-topic
  (:require [rum.core :as rum]
            [clojure.set :as cs]
            [clojure.string :as string]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.caches :as caches]
            [open-company-web.components.ui.icon :as i]))

;; This should be defined as a derivative specifically suited to rendering the
;; topic adding component
(defn get-all-sections
  "Return all sections for the current company no matter it's state (archived, active, inactive)
   e.g {:section a-string :title a-string :category a-string*}"
  []
  (let [company-data (dis/company-data)
        slug         (keyword (router/current-company-slug))
        categories   (:categories company-data)]
    (into
     (:archived company-data)
     (for [cat (:categories (get @caches/new-sections slug))
           sec (:sections cat)]
       (-> sec
           (assoc :section (:section-name sec))
           (select-keys [:title :section])
           (assoc :category (:name cat)))))))

(rum/defcs custom-topic-input
  < (rum/local "" ::topic-title)
  [s submit-fn]
  [:div.mt1.flex
   [:input.npt.mr1.p1.flex-auto
    {:type "text",
     :value @(::topic-title s)
     :on-change #(reset! (::topic-title s) (.. % -target -value))
     :style {:font-size "16px"}
     :placeholder "Custom topic"}]
   [:button.btn-reset.btn-outline
    {:disabled (string/blank? @(::topic-title s))
     :on-click #(let [topic-name     (str "custom-" (utils/my-uuid))
                      new-topic-data {:title @(::topic-title s)
                                      :section topic-name
                                      :placeholder true}]
                  (submit-fn :progress topic-name new-topic-data))} "Add"]])

(defn chunk-topics
  "Partition the provided sequences as if they were one with `::archived` inbetween"
  [inactive archived]
  (let [w-marker   (cond-> inactive (seq archived) (conj ::archived))
        items      (into w-marker archived)
        chunk-size (inc (quot (count items) 2))]
    (partition-all chunk-size items)))

(rum/defcs add-topic
  < (rum/local false ::expanded?)
  [s {:keys [active-topics archived-topics column update-active-topics]}]
  (if @(::expanded? s)
    (let [all-sections (into {} (for [s (get-all-sections)]
                                  [(keyword (:section s)) s]))
          inactive-not-archived (filterv (complement (cs/union (set active-topics) (set archived-topics)))
                                         (keys all-sections))
          chunked (chunk-topics inactive-not-archived archived-topics)]
      [:div.card.p--card
       [:div.open-sans.small-caps.bold.mb2.gray5
        [:span.mr1 "Suggested Topics"]
        [:span.dimmed-gray.btn-reset.right
         {:on-click #(reset! (::expanded? s) false)}
          (i/icon :simple-remove {:color "rgba(78, 90, 107, 0.8)" :size 16 :stroke 8 :accent-color "rgba(78, 90, 107, 1.0)"})]]
       [:div.mxn2.clearfix
        (for [col chunked]
          [:div.col.col-6.px2
           {:key (first col)}
           (for [topic col]
             (if (= ::archived topic)
               [:span.block.open-sans.small-caps.bold.mb1.mt2 {:key topic} "Archived"]
               (let [topic-full (get all-sections topic)]
                 [:div.mb1.btn-reset.yellow-line-hover-child
                  {:key topic
                   :on-click #(do (update-active-topics (or (:category topic-full) :progress) (:section topic-full))
                                  (reset! (::expanded? s) false))}
                  [:span.child
                   (:title topic-full)
                   (:section-name topic-full)]])))])]
       (custom-topic-input #(do (update-active-topics %1 %2 %3)
                                (reset! (::expanded? s) false)))])
    [:div.topic.group.add-topic
     {:on-click #(reset! (::expanded? s) true)}
     [:div.topic-title.small-caps "+ Add Topic"]]))