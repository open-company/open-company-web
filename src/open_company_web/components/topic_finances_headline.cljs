(ns open-company-web.components.topic-finances-headline
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.finances.utils :as finances-utils]))

(defcomponent topic-finances-headline [data owner options]
  (render [_]
    (let [sort-pred (utils/sort-by-key-pred :period)
          finances-data (sort sort-pred (:data data))
          all-revenues (apply + (map :revenue finances-data))
          actual (last finances-data)
          currency (utils/get-symbol-for-currency-code (:currency options))
          runway (finances-utils/fix-runway (:runway actual))
          burn-label (if (pos? all-revenues)
                        "Cash Flow"
                        "Costs")
          burn-class  (if (pos? all-revenues)
                        (if (pos? (:burn-rate actual))
                          "cash-flow"
                          "burn-rate")
                        "costs")
          burn-value (utils/with-metric-prefix (if (pos? all-revenues)
                                                (utils/abs (:burn-rate actual))
                                                (:costs actual)))]
      (dom/div {:class (utils/class-set {:topic-headline-inner true
                                         :topic-finances-headline true
                                         :group true
                                         :collapse (:expanded data)})}
        (dom/div {:class "topic-headline-labels"}
          (dom/div {:class "finances-metric cash"}
            (dom/div {:class "label"} "Cash")
            (dom/div {:class "value cash"} (str currency (utils/with-metric-prefix (:cash actual)))))
          (dom/div {:class "finances-metric burn-rate"}
            (dom/div {:class "label"} burn-label)
            (dom/div {:class (str "value " burn-class)} (str currency burn-value)))
          (when (neg? runway)
            (dom/div {:class "finances-metric runway"}
              (dom/div {:class "label"} "Runway")
              (dom/div {:class "value"} (finances-utils/get-rounded-runway runway [:round :remove-trailing-zero])))))
        (dom/div {:class "topic-headline-inner"} (:headline data))))))