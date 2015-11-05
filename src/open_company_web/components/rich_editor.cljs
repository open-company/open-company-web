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

(defn init-hallo! [owner data]
  (let [hallo-loaded (om/get-state owner :hallo-loaded)
        did-mount (om/get-state owner :did-mount)]
    (when (and hallo-loaded did-mount)
      (when-let [editor-ref (om/get-ref owner "rich-editor")]
        (let [editor-node (.getDOMNode editor-ref)
              hallo-opts (clj->js hallo-format)
              jquery-node (.$ js/window editor-node)]
          (.hallo jquery-node hallo-opts))))))

(defn set-state! [owner k v]
  (om/update-state! owner k (fn [_]v)))

(defn user-expanded! [owner v]
  (set-state! owner :user-expanded v))

(defn editing! [owner active]
  (set-state! owner :editing active))

(defn collapsed! [owner v]
  (set-state! owner :collapsed v))

(defn collapse-if-needed [owner data]
  (if-let [rich-editor-ref (if (:read-only data)
                             (om/get-ref owner "fake-rich-editor")
                             (om/get-ref owner "rich-editor"))]
    (let [rich-editor-node (.getDOMNode rich-editor-ref)
          $-rich-editor (.$ js/window rich-editor-node)
          height (.height $-rich-editor)]
      (om/update-state! owner :should-collapse (fn [_]false))
      (when (>= height 480)
        (collapsed! owner true)))))

(defcomponent rich-editor [data owner]
  (init-state [_]
    {:initial-body (:body (:section-data data))
     :hallo-loaded false
     :did-mount false
     :editing false
     :should-collapse true
     :collapsed false
     :user-expanded false})
  (will-mount [_]
    (when (not (:read-only data))
      ; add dependencies:
      ; jQuery UI
      (cdr/add-style! "/lib/jquery-ui/jquery-ui.theme.min.css")
      (cdr/add-style! "/lib/jquery-ui/jquery-ui.structure.min.css")
      (cdr/add-style! "/lib/jquery-ui/jquery-ui.min.css")
      ; Add js synchronously: jquery ui, rangy and hallo
      (cdr/add-scripts! [{:src "/lib/jquery-ui/jquery-ui.min.js"}
                         {:src "/lib/rangy/rangy-core.min.js"}
                         {:src "/lib/hallo/hallo.js"}]
                        (fn []
                          (om/update-state! owner :hallo-loaded (fn [_]true))
                          (init-hallo! owner data)))))
  (did-mount [_]
    (when (not (:read-only data))
      (om/update-state! owner :did-mount (fn [_]true))
      (init-hallo! owner data)))
  (will-update [_ next-props _]
    (when (not (= (:body (:section-data data)) (:body (:section-data next-props))))
      ; reset collapsed and should-collapse
      (collapsed! owner false)
      (user-expanded! owner false)
      (set-state! owner :should-collapse true)))
  (did-update [_ _ _]
    ; run only if needed and do not crash on tests
    (when (and (.-$ js/window) (om/get-state owner :should-collapse))
      ; collapse initially
      (collapse-if-needed owner data)
      ; rerun collapse calc after every image load
      (let [all-images (.$ js/window "div.rich-editor img")]
        (.each js/$ all-images (fn [idx item]
                               (.load (.$ js/window item) #(collapse-if-needed owner data)))))))
  (render [_]
    (let [section-data (:section-data data)
          section (:section data)
          read-only (:read-only data)
          editing (om/get-state owner :editing)
          no-data (nil? (:author section-data)) ; if we have no author means we had no data
          should-show-placeholder (and (not editing) no-data)
          placeholder "Finances notes here..."
          body (if should-show-placeholder placeholder (:body section-data))
          collapsed (om/get-state owner :collapsed)
          user-expanded (om/get-state owner :user-expanded)]
      (dom/div {:class "rich-editor-container"}
        ; (if read-only
        (dom/div #js {:className (utils/class-set {:fake-rich-editor true
                                                   :hidden (not read-only)
                                                   :collapsed collapsed})
                      :ref "fake-rich-editor"
                      :contenteditable false
                      :dangerouslySetInnerHTML (clj->js {"__html" body})})
        (dom/div #js {:className (utils/class-set {:rich-editor true
                                                   :hidden read-only
                                                   :no-data should-show-placeholder
                                                   :collapsed collapsed})
                      :ref "rich-editor"
                      :onFocus (fn [e]
                                 (when collapsed
                                  (collapsed! owner false)
                                  (user-expanded! owner true))
                                 (when-not (:read-only data)
                                   (editing! owner true)))
                      :onBlur (fn [e]
                                (if-let [editor-ref (om/get-ref owner "rich-editor")]
                                  (let [editor-el (.getDOMNode editor-ref)
                                        innerHTML (.-innerHTML editor-el)]
                                    (when (= innerHTML body)
                                      (editing! owner false)))))
                      :dangerouslySetInnerHTML (clj->js {"__html" body})})
        (if collapsed
          (dom/button {:class "btn btn-link expand-button"
                       :on-click (fn [e]
                                   (collapsed! owner false)
                                   (user-expanded! owner true))}
                      "Continue reading")
          (when user-expanded
            (dom/button {:class "btn btn-link collapse-button"
                         :on-click (fn [e]
                                    (collapsed! owner true)
                                    (user-expanded! owner false))} "Show less")))
        (if editing
          (dom/div {:class "rich-editor-save"}
            (dom/button {:class "oc-btn oc-cancel"
                 :on-click (fn[e]
                             (let [init-value (om/get-state owner :initial-body)
                                   el (.getDOMNode (om/get-ref owner "rich-editor"))]
                               (utils/handle-change section-data init-value :body)
                               (set! (.-innerHTML el) init-value))
                             (editing! owner false))
                 } "CANCEL")
            (dom/button {:class "oc-btn oc-success"
                         :on-click (fn [e]
                                     (let [value (.. (om/get-ref owner "rich-editor") getDOMNode -innerHTML)]
                                       (editing! owner false)
                                       (utils/handle-change section-data value :body)
                                       (utils/save-values (:save-channel data))))
                         } "SAVE"))
          (when (not no-data)
            (om/build update-footer {:author (:author section-data)
                                     :updated-at (:updated-at section-data)
                                     :section section})))))))