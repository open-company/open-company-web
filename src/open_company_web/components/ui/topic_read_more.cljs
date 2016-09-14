(ns open-company-web.components.ui.topic-read-more
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]))

(defcomponent topic-read-more [{:keys [topic-data
                                       section
                                       read-more-cb] :as data} owner]
  (render [_]
    (let [topic-body (:body topic-data)]
      (when (utils/exceeds-topic-body-limit topic-body)
        (dom/button {:class "btn-reset topic-read-more"
                     :onClick read-more-cb} "READ MORE")))))