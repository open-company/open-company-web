(ns open-company-web.components.edit-topic
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [cljs.core.async :refer (chan <!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.lib.utils :as utils]
            [open-company-web.api :as api]
            [cljs-dynamic-resources.core :as cdr]))

(defn add-navbar-channels []
  (let [save-ch (chan)
        cancel-ch (chan)]
    (utils/add-channel "save-bt-navbar" save-ch)
    (utils/add-channel "cancel-bt-navbar" cancel-ch)))

(defn remove-navbar-channels []
  (utils/remove-channel "save-bt-navbar")
  (utils/remove-channel "cancel-bt-navbar"))

(defn medium-options [owner]
  (clj->js {
    "toolbar" #js {
      "buttons" #js ["bold"
                     "italic"
                     "underline"
                     "strikethrough"
                     "h1"
                     "unorderedlist"
                     "orderedlist"
                     "orderedlist"
                     "anchor"
                     "removeFormat"]
      "relativeContainer" (sel1 [:div.toolbar-container])
      "static" true
      "align" "left"
      "updateOnEmptySelection" true
      }}))

(defn setup-medium-editor [owner]
  (when (and (om/get-state owner :did-mount)
             (om/get-state owner :did-load-resources))
    (let [editor (new js/MediumEditor "div.body-editor" (medium-options owner))]
      (om/set-state! owner :medium-editor editor))))

(defcomponent edit-topic [{:keys [section section-data] :as data} owner options]

  (init-state [_]
    (cdr/add-style! "/lib/medium-editor/css/medium-editor.min.css")
    (cdr/add-style! "/lib/medium-editor/css/themes/tim.min.css")
    (cdr/add-script! "/lib/medium-editor/js/medium-editor.js"
                      (fn []
                        (om/set-state! owner :did-load-resources true)
                        (setup-medium-editor owner)))
    {:title (:title section-data)
     :headline (:headline section-data)
     :body (:body section-data)
     :medium-editor nil
     :did-mount false
     :did-load-resources false})

  (will-mount [_]
    ((:navbar-editing-cb options) true "Edit")
    (add-navbar-channels))

  (will-unmount [_]
    (remove-navbar-channels))

  (did-mount [_]
    (om/set-state! owner :did-mount true)
    (setup-medium-editor owner)
    (let [save-ch (utils/get-channel "save-bt-navbar")]
      (go (loop []
        (let [change (<! save-ch)]
          (let [section-data {:title (om/get-state owner :title)
                              :headline (om/get-state owner :headline)
                              :body (.-innerHTML (sel1 [:div.body-editor]))}]
            (api/partial-update-section section section-data)
            ((:dismiss-topic-editing-cb options) true))))))
    (let [cancel-ch (utils/get-channel "cancel-bt-navbar")]
      (go (loop []
        (let [change (<! cancel-ch)]
          ((:dismiss-topic-editing-cb options) false))))))

  (render-state [_ {:keys [title headline body]}]
    (dom/div {:class "edit-topic"}
      (dom/div {:class "edit-topic-title"}
        (dom/input #js {:ref "topic-title"
                        :maxLength 100
                        :value title
                        :onChange #(om/set-state! owner :title (.. % -target -value))}))
      (dom/div {:class "edit-topic-headline"}
        (dom/input #js {:ref "topic-headline"
                        :maxLength 100
                        :value headline
                        :onChange #(om/set-state! owner :headline (.. % -target -value))}))
      (dom/div {:class "edit-topic-body"}
        (dom/div #js {:ref "topic-body"
                      :className "body-editor"
                      :dangerouslySetInnerHTML (clj->js {"__html" body})
                      :onChange (fn [e]
                                  (om/set-state! owner :body (.. e -target -value)))})
        (dom/div #js {:className "toolbar-container"
                      :ref "toolbar-container"})))))