(ns oc.web.components.topics-list
  "
  Display either a dashboard listing of topics in 1-3 columns.

  Handle topic selection, topic navigation, and share initiation.
  "
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel sel1)]
            [oc.web.api :as api]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.lib.raven :as sentry]
            [oc.web.dispatcher :as dispatcher]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.components.topics-columns :refer (topics-columns)]
            [oc.web.components.topics-mobile-layout :refer (topics-mobile-layout)]
            [cljsjs.hammer]))

;; ===== Topic List Component =====

(defn- get-state [owner data current-state]
  ; get internal component state
  (let [board-data (:board-data data)
        active-topics (apply merge (map #(hash-map (keyword %) (->> % keyword (get board-data))) (:topics board-data)))]
    {; initial active topics to check with the updated active topics
     :initial-active-topics active-topics
     ; actual active topics possibly changed by the user
     :active-topics active-topics
     ; card with
     :card-width (:card-width data)
     ; redirect the user to the updates preview page
     :redirect-to-preview (or (:redirect-to-preview current-state) false)}))

(defcomponent topics-list [data owner options]

  (init-state [_]
    (get-state owner data nil))

  (will-receive-props [_ next-props]
    (when-let* [new-topic-foce (om/get-state owner :new-topic-foce)
                new-topic-data (-> next-props :board-data new-topic-foce)]
      (dispatcher/dispatch! [:start-foce new-topic-foce new-topic-data]))
    (when (om/get-state owner :redirect-to-preview)
      (utils/after 100 #(router/nav! (oc-urls/update-preview))))
    (when-not (= (:board-data next-props) (:board-data data))
      (om/set-state! owner (get-state owner next-props (om/get-state owner)))))

  (render-state [_ {:keys [active-topics
                           redirect-to-preview
                           rerender]}]
    (let [board-slug    (router/current-board-slug)
          board-data    (:board-data data)
          board-topics  (vec (filter #(contains? board-data %) (vec (map keyword (:topics board-data)))))
          card-width    (:card-width data)
          columns-num   (:columns-num data)
          ww            (responsive/ww)
          total-width   (if (and (= columns-num 1)
                                (< ww responsive/c1-min-win-width))
                          "auto"
                          (str (responsive/total-layout-width-int card-width columns-num) "px"))
          can-edit-secs (utils/can-edit-topics? board-data)]
      (dom/div {:class (utils/class-set {:topic-list true
                                         :not-first-board (and (:dashboard-sharing data)
                                                               (not= (:slug (first (:boards (dispatcher/org-data)))) (:slug board-data)))
                                         :group true
                                         :editable can-edit-secs})
                :data-rerender rerender
                :key (str "topic-list-" rerender)}
        ;; Topics list columns
        (let [comp-data {:columns-num columns-num
                         :card-width card-width
                         :total-width total-width
                         :content-loaded (:content-loaded data)
                         :loading (:loading data)
                         :topics board-topics
                         :foce-data-editing? (:foce-data-editing? data)
                         :new-topics (:new-topics data)
                         :create-board (:create-board data)
                         :org-data (:org-data data)
                         :board-data board-data
                         :entries-data (:entries-data data)
                         :topics-data board-data
                         :foce-key (:foce-key data)
                         :foce-data (:foce-data data)
                         :show-add-topic (:show-add-topic data)
                         :dashboard-selected-topics (:dashboard-selected-topics data)
                         :dashboard-sharing (:dashboard-sharing data)
                         :prevent-topic-not-found-navigation (:prevent-topic-not-found-navigation data)
                         :is-dashboard (:is-dashboard data)
                         :show-top-menu (:show-top-menu data)}
              sub-component (if (responsive/is-mobile-size?) topics-mobile-layout topics-columns)]
          (om/build sub-component comp-data))))))