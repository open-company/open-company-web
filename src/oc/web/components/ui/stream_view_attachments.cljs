(ns oc.web.components.ui.stream-view-attachments
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.activity :as au]
            [oc.web.lib.responsive :as responsive]
            [clojure.contrib.humanize :refer (filesize)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(rum/defcs stream-view-attachments < (rum/local false ::attachments-dropdown)
  [s attachments remove-cb]
  (let [atc-num (count attachments)
        ww (responsive/ww)
        editable? (fn? remove-cb)]
    (when (pos? atc-num)
      [:div.stream-view-attachments
        [:div.stream-view-attachments-title
          (str atc-num " attachment" (when (> atc-num 1) "s"))]
        [:div.stream-view-attachments-content
          (for [atch attachments
                :let [created-at (:created-at atch)
                      size (:file-size atch)
                      subtitle (str
                                  (when created-at
                                    (utils/time-since created-at))
                                  (when (and created-at size)
                                    " - ")
                                  (when size
                                    (filesize size :binary false :format "%.2f")))]]
            [:a.stream-view-attachments-item.group
              {:class (utils/class-set {:double-line (> atc-num 1)
                                        :editable editable?})
               :key (str "attachment-" size "-" (:file-url atch))
               :href (:file-url atch)
               :target "_blank"}
              [:div.attachment-icon
                [:i.fa
                  {:class (au/icon-for-mimetype (:file-type atch))}]]
              [:div.attachment-content
                [:div.attachment-name
                  (:file-name atch)]
                [:div.attachment-description
                  subtitle]]
              (when editable?
                [:button.mlb-reset.remove-attachment-bt
                  {:data-toggle (when-not (responsive/is-tablet-or-mobile?) "" "tooltip")
                   :data-placement "top"
                   :data-container "body"
                   :title "Remove attachment"
                   :on-click #(do
                                (utils/event-stop %)
                                (utils/remove-tooltips)
                                (when (fn? remove-cb)
                                  (remove-cb atch)))}])])]])))