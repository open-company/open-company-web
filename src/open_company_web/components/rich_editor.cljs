(ns open-company-web.components.rich-editor
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.update-footer :refer (update-footer)]
            [open-company-web.lib.utils :as utils]
            [cljs-dynamic-resources.core :as cdr]))

(def hallo-format {
  :editable true
  :plugins {
      :halloformat { "formattings" {"bold" true
                                    "italic" true
                                    "strikethrough" true
                                    "underline" true}}
      :halloheadings {"formatBlocks" ["p" "h1"]}
      :hallolists {}
      :hallolink {}
      :halloblacklist {}}
  ; :toolbar "halloToolbarFixed" ; uncomment for fixed toolbar
})

(defn init-hallo! [owner]
  (let [hallo-loaded (om/get-state owner :hallo-loaded)
        did-mount (om/get-state owner :did-mount)]
    (when (and hallo-loaded did-mount)
      (when-let [editor-node (.getDOMNode (om/get-ref owner "rich-editor"))]
        (let [hallo-opts (clj->js hallo-format)
              jquery-node (.$ js/window editor-node)
              init-editor (.hallo jquery-node hallo-opts)]
          (om/update-state! owner :editor (fn[_] init-editor)))))))

(defcomponent rich-editor [data owner]
  (init-state [_]
    {:editor nil
     :initial-body (:body (:section-data data))
     :hallo-loaded false
     :did-mount false
     :editing false})
  (will-mount [_]
    (when (not (:read-only data))
      ; add dependencies:
      ; jQuery UI
      (cdr/add-style! "/lib/jquery-ui/jquery-ui.theme.min.css")
      (cdr/add-style! "/lib/jquery-ui/jquery-ui.structure.min.css")
      (cdr/add-style! "/lib/jquery-ui/jquery-ui.min.css")
      ; Add js synchronously: jquery ui, rangy and hallo
      (cdr/add-scripts! [{:src "/lib/jquery-ui/jquery-ui.min.js"}
                         {:src "//rangy.googlecode.com/svn/trunk/currentrelease/rangy-core.js"}
                         {:src "/lib/hallo/hallo.js"}]
                        (fn []
                          (om/update-state! owner :hallo-loaded (fn [_]true))
                          (init-hallo! owner)))))
  (did-mount [_]
    (when (not (:read-only data))
      (om/update-state! owner :did-mount (fn [_]true))
      (init-hallo! owner)))
  (render [_]
    (let [section-data (:section-data data)
          section (:section data)
          read-only (:read-only data)]
      (dom/div {:class "rich-editor-container"}
        (dom/div {:class (utils/class-set {:fake-rich-editor true :hidden (not read-only)})
                  :dangerouslySetInnerHTML (clj->js {"__html" (:body section-data)})})
        (dom/div #js {:className (utils/class-set {:rich-editor true :hidden read-only})
                      :ref "rich-editor"
                      :onClick (fn [e]
                                 (when-not (:read-only data)
                                   (om/update-state! owner :editing (fn[_] true))))
                      :dangerouslySetInnerHTML (clj->js {"__html" (:body section-data)})})
        (if (om/get-state owner :editing)
          (dom/div {:class "rich-editor-save"}
            (dom/button {:class "btn btn-success"
                         :on-click (fn [e]
                                     (let [value (.. (om/get-ref owner "rich-editor") getDOMNode -innerHTML)]
                                       (om/update-state! owner :editing (fn[_]false))
                                       (utils/handle-change section-data value :body)
                                       (utils/save-values (:save-channel data))))
                         } "Save")
            (dom/button {:class "btn btn-default cancel-button"
                         :on-click (fn[e]
                                     (let [init-value (om/get-state owner :initial-body)
                                           el (.getDOMNode (om/get-ref owner "rich-editor"))]
                                       (utils/handle-change section-data init-value :body)
                                       (set! (.-innerHTML el) init-value))
                                     (om/update-state! owner :editing (fn[_]false)))
                         } "Cancel"))
          (om/build update-footer {:author (:author section-data)
                                   :updated-at (:updated-at section-data)
                                   :section section}))))))