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
      (when-let [editor-ref (om/get-ref owner "rich-editor")]
        (let [editor-node (.getDOMNode editor-ref)
              hallo-opts (clj->js hallo-format)
              jquery-node (.$ js/window editor-node)
              init-editor (.hallo jquery-node hallo-opts)]
          (om/update-state! owner :editor (fn[_] init-editor)))))))

(defn set-editing! [owner active]
  (om/update-state! owner :editing (fn [_]active)))

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
          read-only (:read-only data)
          editing (om/get-state owner :editing)
          no-data (nil? (:author section-data)) ; if we have no author means we had no data
          should-show-placeholder (and (not editing) no-data)
          placeholder "Finances notes here..."
          body (if should-show-placeholder placeholder (:body section-data))]
      (dom/div {:class "rich-editor-container"}
        (dom/div {:class (utils/class-set {:fake-rich-editor true
                                           :hidden (not read-only)})
                  :dangerouslySetInnerHTML (clj->js {"__html" body})})
        (dom/div #js {:className (utils/class-set {:rich-editor true
                                                   :hidden read-only
                                                   :no-data should-show-placeholder})
                      :ref "rich-editor"
                      :onClick (fn [e]
                                 (when-not (:read-only data)
                                   (set-editing! owner true)))
                      :onBlur (fn [e]
                                (if-let [editor-ref (om/get-ref owner "rich-editor")]
                                  (let [editor-el (.getDOMNode editor-ref)
                                        innerHTML (.-innerHTML editor-el)]
                                    (when (= innerHTML body)
                                      (set-editing! owner false)))))
                      :dangerouslySetInnerHTML (clj->js {"__html" body})})
        (if editing
          (dom/div {:class "rich-editor-save"}
            (dom/button {:class "btn btn-success"
                         :on-click (fn [e]
                                     (let [value (.. (om/get-ref owner "rich-editor") getDOMNode -innerHTML)]
                                       (set-editing! owner false)
                                       (utils/handle-change section-data value :body)
                                       (utils/save-values (:save-channel data))))
                         } "Save")
            (dom/button {:class "btn btn-default cancel-button"
                         :on-click (fn[e]
                                     (let [init-value (om/get-state owner :initial-body)
                                           el (.getDOMNode (om/get-ref owner "rich-editor"))]
                                       (utils/handle-change section-data init-value :body)
                                       (set! (.-innerHTML el) init-value))
                                     (set-editing! owner false))
                         } "Cancel"))
          (when (not no-data)
            (om/build update-footer {:author (:author section-data)
                                     :updated-at (:updated-at section-data)
                                     :section section})))))))