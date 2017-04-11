(ns oc.web.components.topic-attachments
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [clojure.contrib.humanize :refer (filesize)]))

(rum/defc topic-attachment < rum/static
  [attachment remove-fn]
  [:a.topic-attachment.group
    {:href (:file-url attachment)
     :target "_blank"}
    [:i.file-mimetype.fa {:class (utils/icon-for-mimetype (:file-type attachment))}]
    [:label.topic-attachment-title (:file-name attachment)]
    (let [prefix (if (:created-at attachment)
                    (str (utils/date-string (utils/js-date (:created-at topic-attachment))) " - ")
                    "Draft - ")]
      [:label.topic-attachment-subtitle (str prefix (filesize (:file-size attachment) :binary false :format "%.2f" ))])
    (when (fn? remove-fn)
      [:button.btn-reset.remove-attachment
        {:title "Remove attachment"
         :data-toggle "tooltip"
         :data-placement "top"
         :data-container "body"
         :on-click #(when (fn? remove-fn)
                      (remove-fn %))}
        [:i.fa.fa-times]])])

(rum/defc topic-attachments < rum/static
                              {:after-render (fn [s]
                                              (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))
                                              s)
                               :before-render (fn [s]
                                                (.tooltip (js/$ "[data-toggle=\"tooltip\"]") "hide")
                                                s)}
  [attachments remove-cb]
  (when (pos? (count attachments))
    (let [sorted-attachments (sort #(compare (:created-at %2) (:created-at %1)) attachments)]
      [:div.topic-attachments
        (for [atc sorted-attachments
              :let [remove-fn (when (fn? remove-cb)
                                (fn [e]
                                  (utils/event-stop e)
                                  (dis/dispatch! [:foce-input {:attachments (filter #(not= (:file-url %) (:file-url atc)) attachments)}])
                                  (remove-cb (:file-url atc))))]]
          (rum/with-key (topic-attachment atc remove-fn) (str "topic-attachment-" (:file-url atc))))])))