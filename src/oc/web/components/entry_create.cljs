(ns oc.web.components.entry-create
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [cuerdas.core :as s]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn dismiss-modal []
  (dis/dispatch! [:new-entry-toggle false]))

(defn close-clicked [s]
  (dis/dispatch! [:input [:new-entry-edit] nil])
  (reset! (::dismiss s) true)
  (utils/after 180 dismiss-modal))

(defn unique-slug [topics topic-name]
  (let [slug (atom (s/slug topic-name))]
    (while (seq (filter #(= (:slug %) @slug) topics))
      (reset! slug (str (s/slug topic-name) "-" (int (rand 1000)))))
    @slug))

(defn toggle-topics-dd []
  (.dropdown (js/$ "div.entry-card-dd-container button.dropdown-toggle") "toggle"))

(defn add-topic [state]
  (let [topics @(drv/get-ref state :board-topics)
        topic-name @(::new-topic state)]
    (if (seq (filter #(= (s/lower (:name %)) (s/lower topic-name)) topics))
      (js/alert "Please choose another topic name.")
      (let [topic-slug (unique-slug topics topic-name)]
        (dis/dispatch! [:topic-add {:slug topic-slug :name topic-name} true])
        (reset! (::new-topic state) "")
        ;; Dismiss the dropdown:
        (toggle-topics-dd)))))

(rum/defcs entry-create < rum/reactive
                          (drv/drv :board-topics)
                          (drv/drv :current-user-data)
                          (drv/drv :new-entry-edit)
                          (drv/drv :board-filters)
                          (rum/local false ::first-render-done)
                          (rum/local false ::dismiss)
                          (rum/local "" ::new-topic)
                          {:will-mount (fn [s]
                                         (when (nil? (:topic-slug @(drv/get-ref s :new-entry-edit)))
                                            (let [board-filters @(drv/get-ref s :board-filters)
                                                  topics @(drv/get-ref s :board-topics)
                                                  topic (if (string? board-filters) (first (filter #(= (:slug %) board-filters) topics)) (first topics))]
                                              (dis/dispatch! [:input [:new-entry-edit :topic-slug] (:slug topic)])))
                                         s)
                           :did-mount (fn [s]
                                        ;; Add no-scroll to the body to avoid scrolling while showing this modal
                                        (dommy/add-class! (sel1 [:body]) :no-scroll)
                                        s)
                           :after-render (fn [s]
                                           (when (not @(::first-render-done s))
                                             (reset! (::first-render-done s) true))
                                           s)
                           :will-unmount (fn [s]
                                           ;; Remove no-scroll class from the body tag
                                           (dommy/remove-class! (sel1 [:body]) :no-scroll)
                                           s)}
  [s]
  (let [topics (drv/react s :board-topics)
        current-user-data (drv/react s :current-user-data)
        new-entry-edit (drv/react s :new-entry-edit)
        topic (first (filter #(= (:slug %) (:topic-slug new-entry-edit)) topics))]
    [:div.entry-create-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(::first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(::first-render-done s))})
       :on-click #(close-clicked s)}
      [:div.entry-create-modal.group
        {:on-click #(utils/event-stop %)}
        (user-avatar-image current-user-data)
        [:div.posting-in "Posting in " [:span (:name topic)]]
        [:div.arrow [:i.fa.fa-angle-right]]
        [:div.select-topic "Select a topic"]
        [:div.entry-card-dd-container
          [:button.mlb-reset.dropdown-toggle
            {:type "button"
             :id "entry-create-dd-btn"
             :data-toggle "dropdown"
             :aria-haspopup true
             :aria-expanded false}
            [:i.fa.fa-caret-down]]
          [:div.entry-create-topics-dd.dropdown-menu
            {:aria-labelledby "entry-create-dd-btn"}
            [:div.triangle]
            [:div.entry-dropdown-list-content
              [:ul
                (for [t topics]
                  [:li
                    {:data-topic-slug (:slug t)
                     :key (str "entry-create-dd-" (:slug t))
                     :on-click #(dis/dispatch! [:input [:new-entry-edit :topic-slug] (:slug t)])
                     :class (when (= (:topic-slug new-entry-edit) (:slug t)) "select")}
                    (:name t)])
                [:li.divider]
                [:li.entry-create-new-topic
                  {:on-click #(utils/event-stop %)}
                  [:button.mlb-reset.entry-create-new-topic-plus
                    {:on-click (fn [e]
                                 (if (empty? @(::new-topic s))
                                   (do
                                     (toggle-topics-dd)
                                     (.focus (sel1 [:input.entry-create-new-topic-field])))
                                   (add-topic s)))
                     :title "Create a new topic"}]
                  [:input.entry-create-new-topic-field
                    {:type "text"
                     :value @(::new-topic s)
                     :on-key-up (fn [e]
                                  (cond
                                    (= "Enter" (.-key e))
                                    (when-not (empty? @(::new-topic s))
                                      (add-topic s))))
                     :on-change #(reset! (::new-topic s) (.. % -target -value))
                     :placeholder "Create New Topic"}]]]]]]]]))

