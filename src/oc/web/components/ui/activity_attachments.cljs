(ns oc.web.components.ui.activity-attachments
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]
            [clojure.contrib.humanize :refer (filesize)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(rum/defcs activity-attachments < (rum/local false ::attachments-dropdown)
                                  (rum/local nil ::window-click)
                                  {:did-mount (fn [s]
                                    (reset! (::window-click s)
                                     (events/listen js/window EventType/CLICK
                                      #(when-not (utils/event-inside? % (rum/ref-node s "attachments-button"))
                                         (reset! (::attachments-dropdown s) false))))
                                    s)
                                   :will-unmount (fn [s]
                                    (when @(::window-click s)
                                      (events/unlistenByKey @(::window-click s))
                                      (reset! (::window-click s) nil)))}
  [s activity-data]
  (let [attachments (utils/get-attachments-from-body (:body activity-data))]
    (when (pos? (count attachments))
      [:div.activity-attachments
        [:button.mlb-reset.attachments-button
          {:ref "attachments-button"
           :class (when @(::attachments-dropdown s) "expanded")
           :on-click #(do
                        (reset! (::attachments-dropdown s) (not @(::attachments-dropdown s))))}
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
                          createdat (:createdat atch)
                          size (:size atch)
                          subtitle (str "Uploaded "
                                      (when author
                                        (str "by " author " "))
                                      (when createdat
                                        (str (utils/time-since createdat) " "))
                                      (when size
                                        (str "- " (filesize size :binary false :format "%.2f"))))]]
                [:a.attachments-dropdown-item.group
                  {:key (str "attachment-" size "-" (:url atch))
                   :href (:url atch)
                   :target "_blank"}
                  [:div.file-icon
                    [:i.fa
                      {:class (utils/icon-for-mimetype (:mimetype atch))}]]
                  [:div.file-title
                    (:name atch)]
                  [:div.file-subtitle
                    subtitle]
                  [:div.file-download]])]])])))