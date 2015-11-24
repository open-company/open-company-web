(ns open-company-web.components.uncontrolled-content-editable
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [cljs-dynamic-resources.core :as cdr]
            [clojure.string]))

;; Inspired to https://github.com/lovasoa/react-contenteditable/issues/3

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
      ;; :toolbar "halloToolbarFixed" ; uncomment for fixed toolbar
      :halloblacklist {}}
})

(defn init-hallo! [owner data]
  (let [hallo-loaded (om/get-state owner :hallo-loaded)
        did-mount (om/get-state owner :did-mount)]
    (when (and hallo-loaded did-mount)
      (when-let [editor-ref (om/get-ref owner "div-content-editable")]
        (let [editor-node (.getDOMNode editor-ref)
              hallo-opts (clj->js hallo-format)
              jquery-node (.$ js/window editor-node)]
          (.hallo jquery-node hallo-opts))))))

(def last-html (atom nil))
(def change-counter (atom 0))

(defn div-node [owner]
  (when-let [div-ref (om/get-ref owner "div-content-editable")]
    (.getDOMNode div-ref)))

(defn div-inner-html [owner]
  (when-let [dnode (div-node owner)]
    (.-innerHTML dnode)))

(defn emit-change [owner data event]
  (when-not (:disable-change-listener data)
    (cond
      ; on focus
      (= event :on-focus)
      (when (contains? data :on-focus)
        (om/set-state! owner :focus true)
        ((:on-focus data)))
      ; on blur
      (= event :on-blur)
      (when (contains? data :on-blur)
        (om/set-state! owner :focus false)
        ((:on-blur data)))
      ; on change
      (= event :on-change)
      (let [html (div-inner-html owner)]
        (when (not= html @last-html)
          (swap! change-counter inc)
          ((:on-change data) html @change-counter)
          (reset! last-html html))))))

(defn trim [v]
  (when (not (nil? v))
    (clojure.string/trim v)))

(defn get-html [owner data]
  (let [html (:html data)
        no-data (empty? html)
        should-show-placeholder (and (not (om/get-state owner :focus)) no-data)
        placeholder (:placeholder data)]
    (if should-show-placeholder
      placeholder
      html)))

(defcomponent uncontrolled-content-editable [data owner]

  (init-state [_]
    {:hallo-loaded false
     :did-mount false
     :focus false})
  
(will-mount [_]
    (when-not (:read-only data)
      ; add dependencies:
      ; jQuery UI
      (cdr/add-style! "/lib/jquery-ui/jquery-ui.structure.min.css")
      (cdr/add-style! "/lib/jquery-ui/jquery-ui.min.css")
      ; Add js synchronously: jquery ui, rangy and hallo
      (cdr/add-scripts! [{:src "/lib/rangy/rangy-core.min.js"}
                         {:src "/lib/hallo/hallo.js"}]
                        (fn []
                          (om/update-state! owner :hallo-loaded (fn [_]true))
                          (init-hallo! owner data)))))

  (did-mount [_]
    (when-not (:read-only data)
      (om/update-state! owner :did-mount (fn [_]true))
      (init-hallo! owner data)))

  (should-update [_ next-props _]
    (let [last-change (= (:body-counter next-props) @change-counter)
          html-changed (not= (trim (:html next-props)) (trim (div-inner-html owner)))
          class-change (not= (:class next-props) (:class data))
          placeholder-change (not= (:placeholder next-props) (:placeholder data))
          should-change (or (and html-changed last-change) class-change placeholder-change)]
      should-change))

  (did-update [_ _ _]
    (when (not= (:html data) (div-inner-html owner))
      (let [html (get-html owner data)]
        (set! (.-innerHTML (div-node owner)) html))))

  (render [_]
    (let [html (get-html owner data)]
      (dom/div #js {:className (:class data)
                    :onInput #(emit-change owner data :on-change)
                    :onFocus #(emit-change owner data :on-focus)
                    :onBlur #(emit-change owner data :on-blur)
                    ; :contentEditable WARN: do not set contentEditable here or it breaks the load
                    ; event of the images needed to calculate the div size
                    :dangerouslySetInnerHTML (clj->js {"__html" html})
                    :ref "div-content-editable"}))))