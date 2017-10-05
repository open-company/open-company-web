(ns oc.web.lib.medium-editor-exts
  (:require [oc.web.lib.utils :as utils]
            [goog.dom :as gdom]
            [goog.style :as gstyle]
            [goog.object :as gobj]
            [cljsjs.medium-editor]
            [cljsjs.rangy-selectionsaverestore]
            [org.martinklepsch.cljsjs-medium-button]))

(defn inject-extension [config-map ext-map]
  (let [n   (:name ext-map)
        ctr (js/MediumEditor.Extension.extend (clj->js ext-map))]
    (assoc-in config-map [:extensions n] (new ctr))))

(defn inject-button [config-map btn-name btn]
  (assoc-in config-map [:extensions btn-name] btn))

(def hl-btn
  (new js/MediumButton #js {:label "<i class=\"fa fa-paint-brush\"></i>" :start "<mark>" :end "</mark>"}))

(def highlight-btn
  {:name "highlight"
   :tagNames "mark"
   :contentDefault "<b>H</b>"
   :aria "Highlight"
   :action "highlight"
   :useQueryState true
   :init (fn []
           (js/rangy.init)
           (this-as this
             (js/MediumEditor.extensions.button.prototype.init.call this)
             (set! this -classApplier (js/rangy.createCssClassApplier
                                         "highlight"
                                         #js {:elementTagName "mark"
                                              :normalize true}))))
   :handleClick (fn [e] (this-as this (.. this -classApplier toggleSelection)))
})

(defn empty-paragraph? [el]
  (and (<= (-> el .-childNodes .-length) 1)
       (empty? (.-nodeValue el))
       (or (= "BR" (some-> el .-childNodes (aget 0) .-tagName))
           (empty? (some-> el .-childNodes (aget 0) .-nodeValue)))))

(defn media-upload [upload-ui-id bt-offset & [scrolling-el]]
  (let [hide-btn (fn []
                    (when-let [el (js/document.getElementById upload-ui-id)]
                      (gstyle/setStyle el #js {:opacity 0})
                      (.remove (.-classList el) "expanded")
                      (utils/after 250 #(gstyle/setStyle el #js {:display "none"}))))
        pos-btn (fn [top-v]
                  (when-let [el (js/document.getElementById upload-ui-id)]
                    (gstyle/setStyle el #js {:position "absolute"
                                             :display "block"
                                             :opacity 1
                                             :top (str (- (+ top-v (:top bt-offset)) (when scrolling-el (.-top (.offset (js/$ scrolling-el))))) "px")
                                             :left (str (+ 6 (:left bt-offset)) "px")})))
        show-btn (fn [event]
                   (this-as this
                     (utils/after 100
                      (fn []
                        (if-not (utils/event-inside? event (aget (.-elements (.-base this)) 0))
                          ;; Hide if the click is outside
                          (hide-btn)
                          ;; Check the current selection and decide if we need to show or hide the button
                          (let [sel (js/window.getSelection)
                                el  (when (pos? (.-rangeCount sel))
                                      (.-commonAncestorContainer (.getRangeAt sel 0)))
                                $el (js/$ el)
                                offset (when el (.offset $el))
                                offset-top (int (when offset (.-top offset)))]
                            (when (and sel el)
                              (if (and (empty-paragraph? el) offset-top)
                                (pos-btn offset-top)
                                (hide-btn))))))))
                   true)]
    {:name "media-upload"
     :init (fn []
             (this-as this
               (.on (.-base this) js/window "click" (.bind show-btn this))
               (doseq [el (.getEditorElements this)]
                 (.on (.-base this) el "keyup" (.bind show-btn this))
                 (.subscribe (.-base this) "editableInput" (.bind show-btn this)))))
     :destroy (fn []
                (this-as this
                  (doseq [el (.getEditorElements this)]
                    (.unsubscribe (.-base this) "editableInput" (.bind show-btn this)))))}))