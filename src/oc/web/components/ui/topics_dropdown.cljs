(ns oc.web.components.ui.topics-dropdown
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn- unique-slug [topics topic-name]
  (let [slug (atom (s/slug topic-name))]
    (while (seq (filter #(= (:slug %) @slug) topics))
      (reset! slug (str (s/slug topic-name) "-" (int (rand 1000)))))
    @slug))

(defn- create-new-topic [s]
  (when (seq @(::new-topic s))
    (let [topics @(drv/get-ref s :entry-edit-topics)
          topic-name (s/trim @(::new-topic s))
          topic-slug (unique-slug topics topic-name)
          edit-key (second (:rum/args s))]
      (dis/dispatch! [:topic-add {:name topic-name :slug topic-slug} edit-key])
      (reset! (::new-topic s) "")
      (reset! (::showing-dropdown s) false))))

(rum/defcs topics-dropdown < (rum/local "" ::new-topic)
                             (rum/local false ::focusing-create-topic)
                             (rum/local false ::showing-dropdown)
                             (rum/local nil ::window-click)
                             rum/reactive
                             (drv/drv :entry-edit-topics)
                             {:will-mount (fn [s]
                               ;; Load board if it's not already
                               (let [entry-editing (first (:rum/args s))]
                                 (when-not @(drv/get-ref s :entry-edit-topics)
                                   (dis/dispatch! [:board-get (utils/link-for (:links entry-editing) "up")])))
                               s)
                              :did-mount (fn [s]
                                (reset! (::window-click s)
                                 (events/listen
                                  js/window
                                  EventType/CLICK
                                  #(when (not (utils/event-inside? % (rum/dom-node s)))
                                    (reset! (::showing-dropdown s) false))))
                                s)
                              :will-unmount (fn [s]
                                (when @(::window-click s)
                                  (events/unlistenByKey @(::window-click s))
                                  (reset! (::window-click s) nil))
                                s)}
  [s entry-editing edit-key]
  (let [topics (distinct (drv/react s :entry-edit-topics))]
    [:div.entry-card-dd-container
      [:button.mlb-reset.entry-card-dd-button
        {:class (utils/class-set {:has-topic (not (empty? (:topic-name entry-editing)))
                                  :active @(::showing-dropdown s)})
         :on-click #(reset! (::showing-dropdown s) (not @(::showing-dropdown s)))}
        (if (:topic-name entry-editing)
          [:div.activity-tag.on-gray
            {:class (when @(::showing-dropdown s) "active")}
            (:topic-name entry-editing)]
          "+ Add a topic")]
      (when @(::showing-dropdown s)
        [:div.entry-edit-topics-dd
          [:div.triangle]
          [:div.entry-dropdown-list-content
            [:ul
              (for [t (sort #(compare (:name %1) (:name %2)) topics)
                    :let [selected (= (:topic-name entry-editing) (:name t))]]
                [:li.selectable.group
                  {:key (str "entry-edit-dd-" (:slug t))
                   :on-click #(do
                               (reset! (::showing-dropdown s) false)
                               (dis/dispatch!
                                [:input
                                 [edit-key]
                                 (merge entry-editing {:topic-name (:name t) :has-changes true})]))
                   :class (when selected "select")}
                  [:button.mlb-reset
                    (:name t)]
                  (when selected
                    [:button.mlb-reset.mlb-link.remove
                      {:on-click (fn [e]
                                   (utils/event-stop e)
                                   (reset! (::showing-dropdown s) false)
                                   (dis/dispatch!
                                    [:input
                                     [edit-key]
                                     (merge entry-editing {:topic-slug nil :topic-name nil :has-changes true})]))}
                      "Remove"])])
              [:li.divider]
              [:li.entry-edit-new-topic.group
                [:div.entry-edit-new-topic-title "CREATE NEW TOPIC"]
                [:div.entry-edit-new-topic-container.group
                  [:input.entry-edit-new-topic-field
                    {:type "text"
                     :value @(::new-topic s)
                     :on-focus #(reset! (::focusing-create-topic s) true)
                     :on-blur (fn [e] (utils/after 100 #(reset! (::focusing-create-topic s) false)))
                     :on-key-up (fn [e]
                                  (cond
                                    (= "Enter" (.-key e))
                                    (create-new-topic s)))
                     :on-change #(reset! (::new-topic s) (.. % -target -value))
                     :placeholder "Create New Topic"}]
                  [:button.mlb-reset.entry-edit-new-topic-plus
                    {:on-click (fn [e]
                                 (utils/event-stop e)
                                 (create-new-topic s))
                     :class (utils/class-set {:empty (empty? @(::new-topic s))
                                              :active (not (empty? @(::new-topic s)))})
                     :title "Create a new topic"}]]]]]])]))