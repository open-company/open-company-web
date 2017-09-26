(ns oc.web.components.media-picker-test
  (:require [rum.core :as rum]
            [oc.web.lib.utils :as utils]))

(defn- setup-editor [s]
  (let [body-el (rum/ref-node s "body-el")
        media-picker-opts {:buttons #js ["picture" "video" "chart" "attachment" "divider-line"]
                           :delegateMethods #js {:onPickerClick #(js/console.log "on-picker-click" %1 %2)}}
        media-picker-ext (js/MediaPicker. (clj->js media-picker-opts))
        options {:toolbar #js {:buttons #js ["bold" "italic" "unorderedlist" "anchor"]}
                 :buttonLabels "fontawesome"
                 :anchorPreview #js {:hideDelay 500, :previewValueSelector "a"}
                 :extensions #js {:autolist (js/AutoList.)
                                  :media-picker media-picker-ext}
                 :autoLink true
                 :anchor #js {:customClassOption nil
                              :customClassOptionText "Button"
                              :linkValidation true
                              :placeholderText "Paste or type a link"
                              :targetCheckbox false
                              :targetCheckboxText "Open in new window"}
                 :paste #js {:forcePlainText false
                             :cleanPastedHTML false}
                 :placeholder #js {:text "Placeholder text test"
                                   :hideOnClick true}}
        body-editor  (new js/MediumEditor body-el (clj->js options))]
    (.subscribe body-editor
                "editableInput"
                (fn [event editable]
                  (js/console.log "media-picker-test/editableInput")))
    (reset! (::editor s) body-editor)))

(rum/defcs media-picker-test < rum/static
                               (rum/local nil ::editor)
                               {:did-mount (fn [s]
                                             (utils/after 1000 #(setup-editor s))
                                             s)}
  [s]
  [:div.media-picker-test
    [:div.media-picker-inner
      [:div.media-picker-body
        {:ref "body-el"
         :dangerouslySetInnerHTML #js {"__html" ""}}]]])