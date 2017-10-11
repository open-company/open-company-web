(ns oc.web.components.ui.mixins
  (:require [rum.core :as rum]
            [dommy.core :as dommy :refer-macros (sel1)]))

(def -no-scroll-mixin-class :no-scroll)

(def no-scroll-mixin
  "Mixin used to check if the body has aleady the no-scroll class, if it does it's a no-op.
   If it doesn't it remember to remove it once the component is going to unmount."
  {:did-mount
    (fn [state]
      ;; Add no-scroll to the body if it doesn't has it already
      ;; to avoid scrolling while showing this modal
      (let [body (sel1 [:body])]
        (if (and body
                 (dommy/has-class? body -no-scroll-mixin-class))
          state
          (do ;; if no-scroll is not preset
            ;; add it
            (dommy/add-class! body -no-scroll-mixin-class)
            ;; remember to remove it on unmount
            (assoc state ::-no-scroll-mixin-remove-body-class true)))))
   :will-unmount
    (fn [state]
      (when (and (contains? state ::-no-scroll-mixin-remove-body-class)
                 (::-no-scroll-mixin-remove-body-class state))
        (dommy/remove-class! (sel1 [:body]) -no-scroll-mixin-class)
        (dissoc state ::-no-scroll-mixin-remove-body-class))
      state)})

(def first-render-mixin
  {:init (fn [state]
           (assoc state :first-render-done false))
   :after-render
    (fn [state]
      (if (:first-render-done state)
        state
        (assoc state :first-render-done true)))
   :will-unmount
    (fn [state]
      (assoc state :first-render-done false))})