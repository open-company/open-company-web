(ns oc.web.components.ui.follow-board-picker
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.utils :as utils]
            [oc.web.actions.user :as user-actions]
            [oc.web.mixins.ui :refer (strict-refresh-tooltips-mixin)]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.actions.notifications :as notification-actions]
            [oc.web.components.ui.carrot-checkbox :refer (carrot-checkbox)]))

(defn- sort-boards [boards]
  (sort-by :name boards))

(defn- toggle-board [s b]
  (compare-and-set! (::selecting-multiple s) false true)
  (swap! (::boards s)
   #(if (utils/in? % (:uuid b))
     (disj % (:uuid b))
     (conj % (:uuid b)))))

(defn- search-string [v q]
  (-> v string/lower (string/includes? q)))

(defn- search-board [board q]
  (or (-> board :name (search-string q))
      (-> board :slug (search-string q))))

(defn- filter-board [s board q]
  (and (not= (:slug board) utils/default-drafts-board-slug)
       (or (not (seq q))
           (search-board board q)
           (some (partial search-board board) (string/split q #"\s")))))

(defn- filter-sort-boards [s boards q]
  (sort-boards (filterv #(filter-board s % (string/lower q)) boards)))

(defn- follow! [s]
  (reset! (::saving s) true)
  (user-actions/follow-boards @(::boards s))
  (nav-actions/close-all-panels))

(defn- toggle-board-and-exit [s board]
  (let [boards (swap! (::boards s)
                #(if (utils/in? % (:uuid board))
                  (disj % (:uuid board))
                  (conj % (:uuid board))))]
    (follow! s)))

(defn- close-follow-board-picker [s]
  (if (not= @(::initial-boards s) @(::boards s))
    (let [alert-data {:icon "/img/ML/trash.svg"
                      :action "follow-board-picker-unsaved-exit"
                      :message "Leave without saving your changes?"
                      :link-button-title "Stay"
                      :link-button-cb #(alert-modal/hide-alert)
                      :solid-button-style :red
                      :solid-button-title "Lose changes"
                      :solid-button-cb #(do
                                          (alert-modal/hide-alert)
                                          (nav-actions/close-all-panels))}]
      (alert-modal/show-alert alert-data))
    (nav-actions/close-all-panels)))

(rum/defcs follow-board-picker < rum/reactive

 (drv/drv :org-data)
 (drv/drv :follow-boards-list)
 (rum/local #{} ::boards)
 (rum/local #{} ::initial-boards)
 (rum/local "" ::query)
 (rum/local false ::saving)
 (rum/local false ::selecting-multiple)
 strict-refresh-tooltips-mixin
 {:init (fn [s]
   ;; Refresh the following list
   (user-actions/load-follow-list)
   s)
  :will-mount (fn [s]
   ;; setup the currently followed boards
   (let [boards (set (map :uuid @(drv/get-ref s :follow-boards-list)))]
     (reset! (::boards s) boards)
     (reset! (::initial-boards s) boards))
   s)}

  [s]
  (let [org-data (drv/react s :org-data)
        follow-boards-list (drv/react s :follow-boards-list)
        all-boards (:boards org-data)
        sorted-boards (filter-sort-boards s all-boards @(::query s))
        is-mobile? (responsive/is-mobile-size?)]
    [:div.follow-board-picker
      [:div.follow-board-picker-modal
        ; [:div.follow-board-picker-header]
        [:button.mlb-reset.modal-close-bt
          {:on-click #(close-follow-board-picker s)}]
        [:div.follow-board-picker-body
          [:h3.follow-board-picker-title
            "Topics"]
          [:div.follow-board-picker-subtitle
            "Select some topics to follow the related posts more easily."]
          (if (zero? (count all-boards))
            [:div.follow-board-picker-empty-boards
              [:div.follow-board-picker-empty-icon]
              [:div.follow-board-picker-empty-copy
                "There are no topics to follow yet. "
                (when (utils/link-for (:links org-data) "create")
                  [:button.mlb-reset.follow-board-picker-empty-invite-bt
                    {:on-click #(nav-actions/show-org-settings :invite-picker)}
                    "Add a topic to get started."])]]
            [:div.follow-board-picker-body-inner.group
              [:input.follow-board-picker-search-field-input.oc-input
                {:class (when-not (seq @(::boards s)) "empty")
                 :value @(::query s)
                 :type "text"
                 :ref :query
                 :placeholder "Search for topics or make your selection below..."
                 :on-change #(reset! (::query s) (.. % -target -value))}]
              [:div.follow-board-picker-boards-list.group
                (for [b sorted-boards
                      :let [selected? (utils/in? @(::boards s) (:uuid b))]]
                  [:div.follow-board-picker-board-row.group
                    {:key (str "follow-board-picker-" (:uuid b))
                     :class (when (utils/in? @(::boards s) (:uuid b)) "selected")}
                    (carrot-checkbox {:selected selected?
                                      :did-change-cb #(toggle-board s b)})
                    [:button.mlb-reset.follow-board-bt
                      {:on-click #(if @(::selecting-multiple s)
                                    (toggle-board s b)
                                    (toggle-board-and-exit s b))}
                      [:span.follow-board-picker-board
                        (:name b)]]])]
              [:div.follow-board-picker-footer.group
                [:button.mlb-reset.create-board-bt
                  {:on-click #(nav-actions/show-section-add)}
                  "Create a new board"]
                [:button.mlb-reset.follow-board-picker-create-bt
                  {:on-click #(follow! s)
                   :disabled (or (= @(::boards s) @(::initial-boards s))
                                 @(::saving s))
                   :data-toggle (when-not is-mobile? "tooltip")
                   :data-container "body"
                   :data-placement "top"
                   :title "Save & close"}
                  "Follow"]]])]]]))
