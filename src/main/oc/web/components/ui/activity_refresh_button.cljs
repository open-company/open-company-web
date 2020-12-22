(ns oc.web.components.ui.activity-refresh-button
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.components.ui.refresh-button :refer (refresh-button)]))

(defn- count-unseen-comments [items]
  (reduce (fn [c item]
            (+ c (count (filter :unseen (:replies-data item)))))
          0
          items))

(rum/defcs activity-refresh-button < rum/reactive
  (drv/drv :replies-badge)
  (rum/local 0 ::initial-unseen-comments)
  {:will-mount (fn [s]
                 (let [props (-> s :rum/args first)]
                   (reset! (::initial-unseen-comments s) (count-unseen-comments (:items-to-render props))))
                 s)}
  [s {:keys [items-to-render]}]
  (let [replies-badge (drv/react s :replies-badge)
        delta-new-comments (- (count-unseen-comments items-to-render) @(::initial-unseen-comments s))
        show-refresh-button? (and replies-badge
                                  (pos? delta-new-comments))]
    (when show-refresh-button?
      (refresh-button {:message (if (pos? delta-new-comments)
                                  (str delta-new-comments " unread comment" (when-not (= delta-new-comments 1) "s"))
                                  "New replies available")
                       :visible show-refresh-button?
                       :class-name :activity-refresh-button-container}))))
