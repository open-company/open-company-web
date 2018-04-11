(ns oc.web.components.ui.activity-attachments
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [clojure.contrib.humanize :refer (filesize)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(rum/defcs activity-attachments < (rum/local false ::attachments-dropdown)
  [s activity-data]
  (let [attachments (:attachments activity-data)
        attachments-num (count attachments)]
    (when (pos? attachments-num)
      [:div.activity-attachments
        {:on-mouse-enter #(reset! (::attachments-dropdown s) true)
         :on-mouse-leave #(reset! (::attachments-dropdown s) false)}
        [:div.attachments-summary
          (str attachments-num " attachment" (when (> attachments-num 1) "s"))]
        (when @(::attachments-dropdown s)
          [:div.attachments-dropdown
            (for [atch attachments
                  :let [author (:author atch)
                        createdat (:created-at atch)
                        size (:file-size atch)
                        subtitle (str "Uploaded "
                                    (when author
                                      (str "by " author " "))
                                    (when createdat
                                      (str (utils/time-since createdat) " "))
                                    (when size
                                      (str "- " (filesize size :binary false :format "%.2f"))))]]
              [:a.attachments-dropdown-item.group
                {:key (str "attachment-" size "-" (:file-url atch))
                 :href (:file-url atch)
                 :target "_blank"}
                [:div.file-icon]
                [:div.file-title
                  (:file-name atch)]
                [:div.file-subtitle
                  subtitle]
                [:div.file-download]])])])))