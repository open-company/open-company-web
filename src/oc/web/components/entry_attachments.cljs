(ns oc.web.components.entry-attachments
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [clojure.contrib.humanize :refer (filesize)]))

(rum/defc entry-attachment < rum/static
  [attachment remove-fn]
  [:a.entry-attachment.group
    {:href (:file-url attachment)
     :target "_blank"}
    [:i.file-mimetype.fa {:class (utils/icon-for-mimetype (:file-type attachment))}]
    [:label.entry-attachment-title (:file-name attachment)]
    (let [prefix (if (:created-at attachment)
                    (str (utils/date-string (utils/js-date (:created-at entry-attachment))) " - ")
                    "Draft - ")]
      [:label.entry-attachment-subtitle (str prefix (filesize (:file-size attachment) :binary false :format "%.2f" ))])
    (when (fn? remove-fn)
      [:button.btn-reset.remove-attachment
        {:title "Remove attachment"
         :data-toggle "tooltip"
         :data-placement "top"
         :data-container "body"
         :on-click #(when (fn? remove-fn)
                      (remove-fn %))}
        [:i.fa.fa-times]])])

(rum/defc entry-attachments < rum/static
                              {:after-render (fn [s]
                                              (when-not (utils/is-test-env?)
                                                (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))
                                              s)
                               :before-render (fn [s]
                                                (when-not (utils/is-test-env?)
                                                  (.tooltip (js/$ "[data-toggle=\"tooltip\"]") "hide"))
                                                s)}
  [attachments remove-cb]
  (when (pos? (count attachments))
    (let [sorted-attachments (vec (reverse (sort-by :created-at attachments)))]
      [:div.entry-attachments
        (for [atc sorted-attachments
              :let [remove-fn (when (fn? remove-cb)
                                (fn [e]
                                  (utils/event-stop e)
                                  (dis/dispatch! [:input [:entry-editing :attachments] (vec (filter #(not= (:file-url %) (:file-url atc)) attachments))])
                                  (remove-cb (:file-url atc))))]]
          (rum/with-key (entry-attachment atc remove-fn) (str "entry-attachment-" (:file-url atc))))])))