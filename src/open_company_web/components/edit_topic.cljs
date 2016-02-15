(ns open-company-web.components.edit-topic
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [cljs.core.async :refer (chan <!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.lib.utils :as utils]))

(defn add-navbar-channels []
  (let [save-ch (chan)
        cancel-ch (chan)]
    (utils/add-channel "save-bt-navbar" save-ch)
    (utils/add-channel "cancel-bt-navbar" cancel-ch)))

(defn remove-navbar-channels []
  (utils/remove-channel "save-bt-navbar")
  (utils/remove-channel "cancel-bt-navbar"))

(defcomponent edit-topic [{:keys [section section-data] :as data} owner options]

  (init-state [_]
    {})

  (will-mount [_]
    ((:navbar-editing-cb options) true "Edit")
    (add-navbar-channels))

  (will-unmount [_]
    (remove-navbar-channels))

  (did-mount [_]
    (let [save-ch (utils/get-channel "save-bt-navbar")]
      (go (loop []
        (let [change (<! save-ch)]
          (println "save-ch change!!")))))
    (let [cancel-ch (utils/get-channel "cancel-bt-navbar")]
      (go (loop []
        (let [change (<! cancel-ch)]
          ((:dismiss-topic-editing-cb options)))))))

  (render [_]
    (dom/div {:class "edit-topic"}
      (dom/div {:class "edit-topic-title"}
        (dom/input #js {:ref "topic-title"
                        :maxLength 256
                        :value (:title section-data)}))
      (dom/div {:class "edit-topic-headline"}
        (dom/input #js {:ref "topic-headline"
                        :maxLength 256
                        :value (:headline section-data)}))
      (dom/div {:class "edit-topic-body"}
        (dom/div #js {:ref "topic-body"
                      :contentEditable true
                      :dangerouslySetInnerHTML (clj->js {"__html" (:body section-data)})})))))
