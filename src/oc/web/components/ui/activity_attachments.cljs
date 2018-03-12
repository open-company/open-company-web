(ns oc.web.components.ui.activity-attachments
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [clojure.contrib.humanize :refer (filesize)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(rum/defcs activity-attachments < (rum/local false ::attachments-dropdown)
  [s activity-data small-version?]
  (let [attachments (:attachments activity-data)]
    (when (pos? (count attachments))
      [:div.activity-attachments
        {:class (utils/class-set {:small-version small-version?})
         :on-mouse-enter #(reset! (::attachments-dropdown s) true)
         :on-mouse-leave #(reset! (::attachments-dropdown s) false)}
        [:button.mlb-reset.attachments-button
          {:ref "attachments-button"
           :class (when @(::attachments-dropdown s) "expanded")}
          (count attachments)]
        (when @(::attachments-dropdown s)
          [:div.attachments-dropdown
            [:div.triangle]
            [:div.attachments-dropdown-list
              [:div.attachments-dropdown-header.group
                [:div.title "Attachments"]
                [:div.subtitle (str (count attachments) " files")]]
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
                  (when-not small-version?
                    [:div.file-icon
                      [:i.fa
                        {:class (au/icon-for-mimetype (:mimetype atch))}]])
                  [:div.file-title
                    (:file-name atch)]
                  (when-not small-version?
                    [:div.file-subtitle
                      subtitle])
                  [:div.file-download]])]])])))