(ns oc.web.components.comments
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]))

(rum/defcs comments < (drv/drv :entry-data)
                      rum/reactive
  [s]
  (let [entry-data (drv/react s :entry-data)
        comments (:comments entry-data)]
    [:div.comments
      (when (pos? (count comments))
        [:div.comment])]))