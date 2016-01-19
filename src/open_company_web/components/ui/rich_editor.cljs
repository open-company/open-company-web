(ns open-company-web.components.ui.rich-editor
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.uncontrolled-content-editable :refer (uncontrolled-content-editable)]))

(defn set-state! [owner k v]
  (om/update-state! owner k (fn [_]v)))

(defn user-expanded! [owner v]
  (set-state! owner :user-expanded v))

(defn collapsed! [owner v]
  (set-state! owner :collapsed v))

(defn collapse-if-needed [owner data]
  (let [selector (if (:read-only data)
                   (str "div#section-" (name (:section data)) " div.fake-rich-editor")
                   (str "div#section-" (name (:section data)) " div.rich-editor"))
        $-rich-editor (.$ js/window selector)
        height (.height $-rich-editor)]
    (om/update-state! owner :should-collapse (fn [_] false))
    (when (>= height 480)
      (collapsed! owner true))))

(defn calc-collapse-add-onload [owner data]
  ; run only if needed and do not crash on tests
  (when (and (.-$ js/window) (om/get-state owner :should-collapse))
    ; collapse initially
    (collapse-if-needed owner data)
    ; rerun collapse calc after every image load
    (let [all-images (.$ js/window "div.rich-editor img")]
      (.each js/$ all-images (fn [idx item]
                               (.load (.$ js/window item) #(collapse-if-needed owner data)))))))

(defcomponent rich-editor [data owner]

  (init-state [_]
    {:should-collapse false
     :collapsed false
     :user-expanded false})

  (did-mount [_]
    (calc-collapse-add-onload owner data))

  (will-update [_ next-props _]
    (when (not= (:body data) (:body next-props))
      ; reset collapsed and should-collapse
      (collapsed! owner false)
      (user-expanded! owner false)
      (set-state! owner :should-collapse false)))

  (did-update [_ prev-props _]
    (calc-collapse-add-onload owner data))

  (render [_]
    (let [read-only (:read-only data)
          editing (:editing data)
          no-data (empty? (:body data))
          should-show-placeholder (and (not editing) no-data)
          placeholder (:placeholder data)
          body (:body data)
          collapsed (om/get-state owner :collapsed)
          user-expanded (om/get-state owner :user-expanded)]
      (dom/div {:class "rich-editor-container group"}
        (dom/div #js {:className (utils/class-set {:fake-rich-editor true
                                                   :hidden (not read-only)
                                                   :collapsed collapsed})
                      :ref "fake-rich-editor"
                      :placeholder (:placeholder data)
                      :dangerouslySetInnerHTML (clj->js {"__html" body})})
        (om/build uncontrolled-content-editable {:html body
                                                 :on-focus (fn [e]
                                                             (when collapsed
                                                              (collapsed! owner false)
                                                              (user-expanded! owner true))
                                                             (when-not (:read-only data)
                                                               ((:start-editing-cb data))))
                                                 :on-blur (:cancel-if-needed-cb data)
                                                 :read-only read-only
                                                 :editing editing
                                                 :on-change (:change-cb data)
                                                 :body-counter (:body-counter data)
                                                 :class (utils/class-set {:rich-editor true
                                                                          :hidden read-only
                                                                          :editing editing
                                                                          :collapsed collapsed})
                                                 :placeholder (:placeholder data)})
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
                                    (user-expanded! owner false))} "Show less")))))))