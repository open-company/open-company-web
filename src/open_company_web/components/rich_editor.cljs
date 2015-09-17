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
      :halloreundo {}
      :hallolink {}
      :halloblacklist {}}
  ; :toolbar "halloToolbarFixed" ; uncomment for fixed toolbar
})

(defn init-hallo! [owner]
  (let [hallo-loaded (om/get-state owner :hallo-loaded)
        did-mount (om/get-state owner :did-mount)]
    (when (and hallo-loaded did-mount)
      (let [hallo-opts (clj->js hallo-format)
            editor-node (.getDOMNode (om/get-ref owner "rich-editor"))
            jquery-node (.$ js/window editor-node)
            init-editor (.hallo jquery-node hallo-opts)]
        (om/update-state! owner :editor (fn[_] init-editor))))))

(defcomponent rich-editor [data owner]
  (init-state [_]
    {:editor nil
     :hallo-loaded false
     :did-mount false})
  (will-mount [_]
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
                        (init-hallo! owner))))
  (did-mount [_]
    (om/update-state! owner :did-mount (fn [_]true))
    (init-hallo! owner))
  (render [_]
    (dom/div {:class "rich-editor-container"}
      (dom/div #js {:className "rich-editor"
                    :ref "rich-editor"
                    :onBlur (fn [e]
                                (let [value (.. e -target -innerHTML)]
                                  (utils/handle-change data value :body)))
                    :dangerouslySetInnerHTML (clj->js {"__html" (:body data)})})
      (om/build update-footer {:author (:author data) :updated-at (:updated-at data)}))))

