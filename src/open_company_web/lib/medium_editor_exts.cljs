(ns open-company-web.lib.medium-editor-exts
  (:require [open-company-web.lib.utils :as utils]
            [goog.dom :as gdom]
            [goog.style :as gstyle]
            [cljsjs.medium-editor]
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
  (and (= 1 (-> el .-childNodes .-length))
       (= "BR" (some-> el .-childNodes (aget 0) .-tagName))))

(def file-upload
  (let [class "file-upload-btn"
        handle (fn [e]
                 (.preventDefault e))
        hide-btn (fn [] (let [el (js/document.getElementById "file-upload-ui")]
                          (gstyle/setStyle el #js {:opacity 0})
                          (utils/after 250 #(gstyle/setStyle el #js {:display "none"}))))
        pos-btn (fn [top-v]
                  (let [el (js/document.getElementById "file-upload-ui")]
                    (gstyle/setStyle el #js {:position "absolute"
                                             :display "block"
                                             :opacity 1
                                             :top (str top-v "px")
                                             :left "-35px"})))
        show-btn (fn [e]
                   (utils/after 100
                    (fn []
                      (let [sel (js/window.getSelection)
                            el  (when (pos? (.-rangeCount sel))
                                  (.-commonAncestorContainer (.getRangeAt sel 0)))]
                        (when (and sel el)
                          (if (empty-paragraph? el)
                            (pos-btn (.-top (.position (js/$ el))))
                            (hide-btn))))))
                   true)]
    {:name "file-upload"
     :init (fn []
             (this-as this
               (hide-btn)
               (doseq [el (.getEditorElements this)]
                 (.on (.-base this) el "click" (.bind show-btn this))
                 (.on (.-base this) el "keyup" (.bind show-btn this)))))}))

