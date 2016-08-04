(ns open-company-web.components.ui.topic-read-more
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]))

(defcomponent topic-read-more [{:keys [topic-data
                                       section
                                       read-more-cb] :as data} owner]
  (render [_]
    (let [section-kw (keyword section)
          topic-body (utils/get-topic-body topic-data section-kw)]
      (when (> (count (utils/strip-HTML-tags topic-body)) 500)
        (dom/button {:class "btn-reset topic-read-more"
                     :onClick read-more-cb} "READ MORE")))))