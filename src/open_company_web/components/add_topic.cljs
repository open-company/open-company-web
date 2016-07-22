(ns open-company-web.components.add-topic
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.router :as router]
            [open-company-web.caches :as caches]
            [open-company-web.components.ui.add-topic-popover :refer (add-topic-popover)]))

(defn add-topic-click [owner]
  (om/set-state! owner :show-add-topic-popover true))

(defn show-popover-above? [owner]
  (let [scroll             (.-scrollTop (.-body js/document))
        win-height        (- (.-clientHeight (.-documentElement js/document)) (.-clientHeight (sel1 [:nav.oc-navbar])) 4)
        popover-offsettop (.-offsetTop (om/get-ref owner "topic"))
        add-topic-pos     (- popover-offsettop scroll)]
    (> add-topic-pos (/ win-height 2))))

(defn get-all-sections [slug]
  (let [categories-data (:categories (slug @caches/new-sections))
        all-category-sections (apply concat
                                     (for [category categories-data]
                                       (let [cat-name (:name category)
                                             sections (:sections category)]
                                         (map #(assoc % :category cat-name) sections))))]
    (apply merge
           (map #(hash-map (keyword (:section-name %)) %) all-category-sections))))

(defn dismiss-popover [owner]
  (om/set-state! owner :show-add-topic-popover false))

(defcomponent add-topic [{:keys [active-topics archived-topics column] :as data} owner options]

  (init-state [_]
    {:show-add-topic-popover false})

  (render-state [_ {:keys [show-add-topic-popover]}]
    (dom/div #js {:className (str "topic group add-topic" (when show-add-topic-popover " active"))
                  :onClick #(add-topic-click owner)
                  :ref "topic"}
      (dom/div {:class "topic-title"} "+ ADD TOPIC")
      (when show-add-topic-popover
        (let [slug (keyword (router/current-company-slug))
              all-sections (get-all-sections slug)
              update-active-topics (:update-active-topics options)
              list-data {:topic-order (:new-section-order (slug @caches/new-sections))
                         :all-topics all-sections
                         :active-topics-list active-topics
                         :archived-topics archived-topics
                         :show-above (show-popover-above? owner)
                         :column column}
              list-opts {:did-change-active-topics update-active-topics
                         :dismiss-popover #(dismiss-popover owner)}]
          (om/build add-topic-popover list-data {:opts list-opts}))))))