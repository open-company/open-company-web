(ns oc.web.components.ui.stream-view-attachments
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [oc.web.lib.responsive :as responsive]
            [clojure.contrib.humanize :refer (filesize)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(rum/defcs stream-view-attachments < (rum/local false ::attachments-dropdown)
  [s activity-data]
  (let [attachments (au/get-attachments-from-body (:body activity-data))
        atc-num (count attachments)
        ww (responsive/ww)]
    (when (pos? atc-num)
      [:div.stream-view-attachments
        [:div.stream-view-attachments-title
          (str atc-num " attachment" (when (> atc-num 1) "s"))]
        [:div.stream-view-attachments-content
          (for [atch attachments
                :let [createdat (:createdat atch)
                      size (:size atch)
                      subtitle (str
                                  (when createdat
                                    (str (utils/time-since createdat) " "))
                                  (when size
                                    (str "- " (filesize size :binary false :format "%.2f"))))]]
            [:a.stream-view-attachments-item.group
              {:class (when (> (count attachments) 1) "double-line")
               :key (str "attachment-" size "-" (:url atch))
               :href (:url atch)
               :target "_blank"}
              [:div.attachment-icon
                [:i.fa
                  {:class (au/icon-for-mimetype (:mimetype atch))}]]
              [:div.attachment-content
                [:div.attachment-name
                  (:name atch)]
                [:div.attachment-description
                  subtitle]]])]])))