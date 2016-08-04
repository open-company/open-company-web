(ns open-company-web.components.topic-attribution
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]))

(defn time-ago
  [past-date]
  (let [past-js-date (utils/js-date past-date)
        past (.getTime past-js-date)
        now (.getTime (utils/js-date))
        seconds (.floor js/Math (/ (- now past) 1000))
        minutes-interval (.floor js/Math (/ seconds 60))
        hours-interval (.floor js/Math (/ seconds 3600))
        days-interval (.floor js/Math (/ seconds 86400))
        weeks-interval (.floor js/Math (/ seconds 604800))
        months-interval (.floor js/Math (/ seconds 2592000))
        years-interval (.floor js/Math (/ seconds 31536000))]
    (cond
      (> months-interval 24)
      (str years-interval " " (utils/pluralize "year" years-interval) " ago")

      (> weeks-interval 8)
      (str months-interval " " (utils/pluralize "month" months-interval) " ago")

      (> days-interval 14)
      (str weeks-interval " " (utils/pluralize "week" weeks-interval) " ago")

      (> hours-interval 24)
      (str days-interval " " (utils/pluralize "day" days-interval) " ago")

      (> minutes-interval 60)
      (str hours-interval " " (utils/pluralize "hour" hours-interval) " ago")

      (> minutes-interval 1)
      (str minutes-interval " " (utils/pluralize "minute" minutes-interval) " ago")

      :else
      "just now")))


(defcomponent topic-attribution [{:keys [topic-data
                                         show-read-more
                                         prev-rev
                                         next-rev] :as data} owner options]

  (init-state [_]
    {:force-update 0
     :self-update nil})

  (did-mount [_]
    (when-not (utils/is-test-env?)
      (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
      ; force a rerender every minute
      (om/set-state! owner :self-update (js/setInterval #(om/update-state! owner :force-update inc) (* 60 1000)))))

  (will-unmount [_]
    (when-not (utils/is-test-env?)
      ; clear self update
      (js/clearTimeout (om/get-state owner :self-update))))

  (render-state [_ {:keys [force-update]}]
    (let [topic-body (utils/get-topic-body topic-data section-kw)]
      (dom/div {:class "topic-attribution-container group"}
        (dom/div {:class "topic-navigation"}
          (when prev-rev
            (dom/button {:class "topic-navigation-button earlier-update"
                         :title "View prior update"
                         :type "button"
                         :data-toggle "tooltip"
                         :data-placement "top"
                         :on-click #(when prev-rev ((:rev-click options) % prev-rev))}
              (dom/i {:class "fa fa-caret-left"})))
          (dom/div {:class "topic-attribution"
                  :data-toggle "tooltip"
                  :data-placement "top"
                  :title (str "by " (:name (:author topic-data)) " on " (utils/date-string (utils/js-date (:updated-at topic-data)) [:year]))}
          (time-ago (:updated-at topic-data)))
          (when next-rev
            (dom/button {:class "topic-navigation-button later-update"
                         :title "View next update"
                         :type "button"
                         :data-toggle "tooltip"
                         :data-placement "top"
                         :on-click #(when next-rev ((:rev-click options) % next-rev))}
              (dom/i {:class "fa fa-caret-right"}))))
        (when (and read-more-cb (> (count (utils/strip-HTML-tags topic-body)) 500))
          (dom/button {:class "btn-reset topic-read-more"
                       :onClick read-more-cb} "READ MORE"))))))