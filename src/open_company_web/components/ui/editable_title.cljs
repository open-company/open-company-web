(ns open-company-web.components.ui.editable-title
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [cljs-dynamic-resources.core :as cdr]))

(defn resize-autogrower [owner]
  (when-let [autogrower (om/get-state owner :autogrower)]
    (.resize autogrower)))

(defn setup-autogrow [owner data]
  (when (and (.-$ js/window)
             (om/get-state owner :mounted)
             (om/get-state owner :autogrow-loaded))
    ; (when-let [text-area (om/get-ref owner "editable-title-textarea")]
      (let [$textarea (.$ js/window (str "textarea#editable-title-" (name (:section data))))
            autogrower (.autogrow $textarea (clj->js {"cloneClass" "hidden-autogrow-div"
                                                      "speed" 100
                                                      "onInitialize" true
                                                      "fixMinHeight" false}))]
        (om/set-state! owner :autogrower autogrower))))

(defcomponent editable-title [data owner]

  (init-state [_]
    (cdr/add-script! "/lib/autogrow/autogrow.js"
                     (fn [e]
                       (om/set-state! owner :autogrow-loaded true)
                       (setup-autogrow owner data)))
    {:autogrow-loaded false
     :mounted false
     :autogrower nil})

  (did-update [_ _ _]
    (resize-autogrower owner))

  (did-mount [_]
    (om/set-state! owner :mounted true)
    (setup-autogrow owner data))

  (render [_]
    (let [title (:title data)]
      (dom/div {:class "editable-title-container"}
        (dom/textarea #js {:ref "editable-title-textarea"
                           :id (str "editable-title-" (name (:section data)))
                           :className (utils/class-set {:editable-title true
                                                        :edit (:editing data)})
                           :placeholder (:placeholder data)
                           :disabled (:read-only data)
                           :value title
                           :onFocus #((:start-editing-cb data))
                           :onChange #(let [value (.. % -target -value)]
                                        ((:change-cb data) value))
                           :onBlur (fn [e]
                                     ((:cancel-if-needed-cb data)))
                           :onKeyDown #(cond
                                         (= (.-key %) "Escape")
                                         ((:cancel-if-needed-cb data)))})))))