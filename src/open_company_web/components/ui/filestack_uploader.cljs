(ns open-company-web.components.ui.filestack-uploader
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.local-settings :as ls]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.ui.icon :as i]
            [goog.style :as gstyle]
            [goog.dom :as gdom]
            [goog.dom.classlist :as cl]
            [cljsjs.filestack] ; pulled in for cljsjs externs
            [clojure.string :as string]))

(def placeholder-id (str (random-uuid)))

(defn img-on-load [img]
  (set! (.-height (.-dataset img)) (.-clientHeight img))
  (set! (.-width (.-dataset img)) (.-clientWidth img)))

(defn upload-file! [editor owner file]
  (let [success-cb  (fn [success]
                      (let [url    (.-url success)
                            node   (gdom/createDom "img")
                            marker (gdom/getElement placeholder-id)]
                        (set! (.-onload node) #(img-on-load node))
                        (set! (.-src node) url)
                        (gdom/replaceNode node marker))
                      (gstyle/setStyle (gdom/getElement "file-upload-ui") #js {:display "none"})
                      (om/set-state! owner {}))
        error-cb    (fn [error] (js/console.log "error" error))
        progress-cb (fn [progress]
                      (om/set-state! owner {:state :show-progress
                                            :progress progress}))]
    (cond
      (and (string? file) (not (string/blank? file)))
      (js/filepicker.storeUrl file success-cb error-cb progress-cb)
      file
      (js/filepicker.store file #js {:name (.-name file)} success-cb error-cb progress-cb))))

(defn insert-marker! []
  (when-not (gdom/getElement placeholder-id)
    (js/MediumEditor.util.insertHTMLCommand
     js/document
     (str "<span id=" placeholder-id "></span>"))))

(defcomponent filestack-uploader [editor owner]
  (did-mount [_]
    (when-not (utils/is-test-env?)
      (assert ls/filestack-key "FileStack API Key required")
      (js/filepicker.setKey ls/filestack-key)))

  (did-update [_ _ prev-state]
    (when (and (not (utils/is-test-env?)) (= (om/get-state owner :state) :show-options))
      (.tooltip (js/$ "[data-toggle=\"tooltip\"]"))))

  (render-state [this {:keys [state url]}]
    (dom/div {:id "file-upload-ui"
              :style (merge {:transition ".2s"}
                            (when (:state (om/get-state owner))
                              {:right 0}))}
      (dom/div {:class "flex"}
        (dom/input {:id "file-upload-ui--select-trigger" :style {:display "none"} :type "file"
                    :on-change #(upload-file! editor owner (-> % .-target .-files (aget 0)))})
        (dom/button {:class "btn-reset p0 file-upload-btn"
                     :style {:margin-right "13px"
                             :transition ".2s"
                             :transform (if (om/get-state owner :state) "rotate(135deg)")}
                     :on-click (fn [e]
                                  (utils/event-stop e)
                                  (om/update-state! owner :state #(if % nil :show-options)))}
          (i/icon :circle-add {:size 24}))
          (dom/div {:style #js {:margin "1px 0 0 22px"
                                :display (if (= state :show-options) "block" "none")}}
            (dom/button {:class "btn-reset oc-gray-5"
                         :style {:font-size "15px" :opacity "0.5"}
                         :title "Add an image"
                         :type "button"
                         :data-toggle "tooltip"
                         :data-placement "top"
                         :on-click (fn [_]
                                     (insert-marker!)
                                     (.click (gdom/getElement "file-upload-ui--select-trigger")))}
            (dom/i {:class "fa fa-camera"}))
            (dom/button {:style {:font-size "15px" :opacity "0.5"}
                         :class "btn-reset oc-gray-5"
                         :title "Provide an image link"
                         :type "button"
                         :data-toggle "tooltip"
                         :data-placement "top"
                         :on-click (fn [_]
                                     (insert-marker!)
                                     (om/set-state! owner :state :show-url-field))}
                (dom/i {:class "fa fa-code"})))
          (dom/span {:style {:display (if (= state :show-progress) "block" "none")}}
            (str "Uploading... " (om/get-state owner :progress) "%"))
          (dom/div {:style {:display (if (= state :show-url-field) "block" "none")}}
            (dom/input {:type "text"
                               :style {:height "32px" :margin-top "1px" :outline "none" :border "1px solid rgba(78, 90, 107, 0.5)"}
                               :on-change #(do (om/set-state! owner :url (-> % .-target .-value)) true)
                               :value url})
            (dom/button {:style {:font-size "14px" :margin-left "5px" :padding "0.3rem"}
                         :class "btn-reset btn-solid"
                         :disabled (clojure.string/blank? url)
                         :on-click #(upload-file! editor owner url)}
              "add")
            (dom/button {:style {:font-size "14px" :margin-left "5px" :padding "0.3rem"}
                         :class "btn-reset btn-outline"
                         :on-click (fn [_]
                                     (gdom/removeNode (gdom/getElement placeholder-id))
                                     (om/set-state! owner {}))}
              "cancel"))))))