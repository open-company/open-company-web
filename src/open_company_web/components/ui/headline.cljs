(ns open-company-web.components.ui.headline
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [cljs-dynamic-resources.core :as cdr]))

(defcomponent headline [data owner]
  (init-state [_]
    {:char-count 100})

  (will-receive-props [_ next-props]
    (om/set-state! owner :char-count (- 100 (count (:headline next-props)))))

  (render [_]
    (let [headline (:headline data)]
      (dom/div {:class "headline"}
        (dom/label {:class (utils/class-set {:headline-counter true
                                             :edit (:editing data)})} (om/get-state owner :char-count))
        (dom/textarea #js {:ref "headline-textarea"
                           :id (str "headline-" (name (:section data)))
                           :className (utils/class-set {:headline-textarea true
                                                        :edit (:editing data)})
                           :placeholder (:placeholder data)
                           :maxLength 100
                           :disabled (:read-only data)
                           :value headline
                           :onFocus #((:start-editing-cb data))
                           :onChange #(let [value (.. % -target -value)]
                                        ((:change-cb data) value))
                           :onBlur (fn [e]
                                     ((:cancel-if-needed-cb data)))
                           :onKeyDown #(cond
                                         (= (.-key %) "Escape")
                                         ((:cancel-if-needed-cb data)))})))))