(ns oc.web.components.ui.media-attachments
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [clojure.contrib.humanize :refer (filesize)]))

(rum/defc media-attachment < rum/static
  [attachment remove-fn]
  [:a.media-attachment.group
    {:href (:file-url attachment)
     :target "_blank"}
    [:i.file-mimetype.fa {:class (utils/icon-for-mimetype (:file-type attachment))}]
    [:label.media-attachment-title (:file-name attachment)]
    (let [prefix (if (:created-at attachment)
                    (str (utils/date-string (utils/js-date (:created-at attachment))) " - ")
                    "Draft - ")]
      [:label.media-attachment-subtitle (str prefix (filesize (:file-size attachment) :binary false :format "%.2f" ))])
    (when (fn? remove-fn)
      [:button.btn-reset.remove-attachment
        {:title "Remove attachment"
         :data-toggle "tooltip"
         :data-placement "top"
         :data-container "body"
         :on-click #(when (fn? remove-fn)
                      (remove-fn %))}
        [:i.fa.fa-times]])])

(rum/defc media-attachments < rum/static
                              {:after-render (fn [s]
                                              (when-not (utils/is-test-env?)
                                                (.tooltip (js/$ "[data-toggle=\"tooltip\"]")))
                                              s)
                               :before-render (fn [s]
                                                (when-not (utils/is-test-env?)
                                                  (.tooltip (js/$ "[data-toggle=\"tooltip\"]") "hide"))
                                                s)}
  [attachments dispatch-input-key remove-cb]
  (when (pos? (count attachments))
    (let [sorted-attachments (vec (reverse (sort-by :created-at attachments)))]
      [:div.media-attachments
        (for [atc sorted-attachments
              :let [remove-fn (when (and dispatch-input-key
                                         (fn? remove-cb))
                               (fn [e]
                                 (utils/event-stop e)
                                 (dis/dispatch!
                                  [:input
                                   [dispatch-input-key :attachments]
                                   (filterv #(not= (:file-url %) (:file-url atc)) attachments)])
                                 (when (fn? remove-cb)
                                   (remove-cb (:file-url atc)))))]]
          (rum/with-key (media-attachment atc remove-fn) (str "media-attachment-" (:file-url atc))))])))