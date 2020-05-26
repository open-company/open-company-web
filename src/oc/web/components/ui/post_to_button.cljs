(ns oc.web.components.ui.post-to-button
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]))

(def missing-title-tooltip "Please add a title before sharing")
(def abstract-max-length-exceeded-tooltip "Abstract too long")

(rum/defcs post-to-button < rum/reactive
  [s {:keys [on-submit disabled title post-tt-kw force-show-tooltip]}]
  [:div.post-to-button
    {:class (when disabled "disabled")}
    ;; Post button
    [:div.post-to-bt-container
      {:class (utils/class-set {:force-show-tooltip force-show-tooltip
                                (str "tt-" (when post-tt-kw (name post-tt-kw))) true})}
      [:button.mlb-reset.post-to-bt
        {:on-click on-submit}
        title]
      (when post-tt-kw
        [:div.post-bt-tooltip
          (cond
            (= post-tt-kw :title)
            missing-title-tooltip
            (= post-tt-kw :abstract)
            abstract-max-length-exceeded-tooltip)])]])
