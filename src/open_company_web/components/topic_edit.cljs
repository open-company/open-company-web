(ns open-company-web.components.topic-edit
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.oc-colors :as oc-colors]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.lib.medium-editor-exts :as editor]
            [open-company-web.components.growth.utils :as growth-utils]
            [open-company-web.components.growth.topic-growth :refer (topic-growth)]
            [open-company-web.components.finances.topic-finances :refer (topic-finances)]
            [open-company-web.components.ui.icon :as i]
            [open-company-web.components.ui.filestack-uploader :refer (filestack-uploader)]
            [cljsjs.medium-editor])) ; pulled in for cljsjs externs

(defn scroll-to-topic-top [topic]
  (let [body-scroll (.-scrollTop (.-body js/document))
        topic-scroll-top (utils/offset-top topic)]
    (utils/scroll-to-y (- (+ topic-scroll-top body-scroll) 90))))

(defn pencil-click [owner options e]
  (utils/event-stop e)
  (when-not (om/get-props owner :add-topic)
    (let [section (om/get-props owner :section)
          topic-click-cb (:topic-click options)]
      (topic-click-cb nil true))))

(defn setup-edit [owner]
  (let [section-name (name (om/get-props owner :section))
        topic-data (om/get-props owner :topic-data)
        topic (om/get-props owner :section)]
    (when-let [headline-el (sel1 [(str "div#foce-headline-" section-name)])]
      (new js/MediumEditor headline-el (clj->js (utils/medium-editor-options "Headline"))))
    (when-let [snippet-el (sel1 [(str "div#foce-snippet-" section-name)])]
      (let [snippet-placeholder (if (:placeholder topic-data) (utils/get-topic-body topic-data topic) "")]
        (new js/MediumEditor snippet-el (clj->js (utils/medium-editor-options snippet-placeholder)))))))

(defcomponent topic-edit [{:keys [topic-data
                                  section
                                  currency
                                  prev-rev
                                  next-rev
                                  add-topic
                                  sharing-mode
                                  show-fast-editing]} owner options]

  (did-mount [_]
    (utils/after 1000 #(setup-edit owner)))

  (render [_]
    (let [section-kw          (keyword section)
          chart-opts          {:chart-size {:width  260
                                            :height 196}
                               :hide-nav true
                               :pillboxes-first false
                               :topic-click (:topic-click options)}
          is-growth-finances? (#{:growth :finances} section-kw)
          gray-color          (oc-colors/get-color-by-kw :oc-gray-5)
          finances-row-data   (:data topic-data)
          growth-data         (growth-utils/growth-data-map (:data topic-data))
          no-data             (or (and (= section-kw :finances)
                                       (or (empty? finances-row-data)
                                        (utils/no-finances-data? finances-row-data)))
                                  (and (= section-kw :growth)
                                       (utils/no-growth-data? growth-data)))
          snippet             (:snippet topic-data)
          image-header        (:image-url topic-data)
          image-header-size   {:width (:image-width topic-data)
                               :height (:image-height topic-data)}
          topic-body          (utils/get-topic-body topic-data section)]
      (dom/div #js {:className "topic-foce group"
                    :ref "topic-internal"}
        (when image-header
          (dom/div {:class "card-header card-image"}
            (when image-header
              (dom/img {:src image-header
                        :class "topic-header-img"}))
            (when image-header
              (dom/button {:class "btn-reset remove-header"}
                (i/icon :simple-remove {:size 16
                                        :color (oc-colors/get-color-by-kw :oc-gray-5)
                                        :accent-color (oc-colors/get-color-by-kw :oc-gray-5)})))))
        ;; Topic title
        (dom/input {:class "topic-title"
                    :value (:title topic-data)
                    :type "text"})
        ;; Topic headline
        (dom/div {:class "topic-headline-inner"
                  :id (str "foce-headline-" (name section))
                  :dangerouslySetInnerHTML #js {"__html" (:headline topic-data)}})
        (dom/div #js {:className "topic-body topic-snippet"
                      :ref "topic-snippet"
                      :id (str "foce-snippet-" (name section))
                      :dangerouslySetInnerHTML #js {"__html" snippet}})
        (dom/div {:class "topic-foce-buttons"}
          (dom/button {:class "btn-reset camera"}
            (i/icon :camera-20 {:size 16
                                :color (oc-colors/get-color-by-kw :oc-gray-5)
                                :accent-color (oc-colors/get-color-by-kw :oc-gray-5)}))
          (dom/button {:class "btn-reset add-image"}
            (i/icon :simple-add {:size 16
                                 :color (oc-colors/get-color-by-kw :oc-gray-5)
                                 :accent-color (oc-colors/get-color-by-kw :oc-gray-5)}))
          (when (and show-fast-editing
                   (not add-topic)
                   (responsive/can-edit?)
                   (not (responsive/is-mobile))
                   (not (:read-only topic-data))
                   (not sharing-mode))
            (dom/button {:class "topic-pencil-button btn-reset"
                         :on-click (partial pencil-click owner options)}
              (i/icon :pencil {:size 16
                               :color gray-color
                               :accent-color gray-color}))))
        (dom/div {:class "topic-foce-footer"}
          (dom/button {:class "btn-reset btn-outline"
                       :on-click #(do (utils/event-stop %) (dis/dispatch! [:start-foce nil]))} "CANCEL")
          (dom/button {:class "btn-reset btn-solid"} "SAVE"))))))