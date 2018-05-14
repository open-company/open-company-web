(ns oc.web.components.ui.activity-attachments
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [clojure.contrib.humanize :refer (filesize)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(rum/defcs activity-attachments
  [s activity-data]
  (let [attachments (take 1 (:attachments activity-data))
        attachments-num (count attachments)]
    (when (pos? attachments-num)
      [:div.activity-attachments
        [:div.activity-attachments-inner
          {:class (utils/class-set {(str "atch-" attachments-num) true})}
          (for [atch (take 2 attachments)
                :let [size (when (:file-size atch)
                             (filesize (:file-size atch) :binary false :format "%.2f"))]]
            [:div.activity-attachment
              {:key (str "act-" (:uuid activity-data) "-atch-" (:file-url atch))}
              (:file-name atch)
              ; [:span.ativity-attachment-title
              ;   ]
              ; (when size
              ;   [:span.activity-attachment-separator
              ;     " • "])
              ; (when size
              ;   [:span.activity-attachment-description
              ;     size])
              ])
          (when (> attachments-num 2)
            [:div.activity-attachment.remaining-attachments
              (str "+ " (- attachments-num 2) " more")])]])))