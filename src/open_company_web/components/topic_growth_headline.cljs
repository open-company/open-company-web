(ns open-company-web.components.topic-growth-headline
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.lib.utils :as utils]))

(defcomponent topic-growth-headline [data owner options]
  (render [_]
    (let [metrics (:metrics data)]
      (dom/div {:class (utils/class-set {:topic-headline-inner true
                                         :topic-growth-headline true
                                         :group true
                                         :collapse (:expanded data)})}
        (for [metric metrics]
          (dom/label {:class "pillbox"
                      :on-click #((:pillbox-click-cb options) (:slug metric))} (:name metric)))
        (dom/div {:class "topic-headline-inner"} (:headline data))))))