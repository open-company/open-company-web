(ns oc.web.components.ui.abstract-field
  (:require [rum.core :as rum]
            [org.martinklepsch.derivatives :as drv]
            [cuerdas.core :as string]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.mention :as mention-utils]
            [oc.web.mixins.mention :as mention-mixins]
            [cljsjs.medium-editor]))

(defn abstract-on-change [state]
  (when-not @(::did-change state)
    (reset! (::did-change state) true))
  (let [dom-el (rum/ref-node state "abstract")
        max-length (:max-length (first (:rum/args state)))
        text (.-innerText dom-el)]
    (reset! (::exceeded-max-length state) (and (pos? max-length) (> (count text) max-length))))
  (let [options (first (:rum/args state))
        on-change (:on-change options)]
    (on-change)))

(defn- setup-editor [s]
  (let [options (first (:rum/args s))
        placeholder "Post preview: summarize why this post matters…"
        abstract-el (rum/ref-node s "abstract")
        users-list (:mention-users @(drv/get-ref s :team-roster))
        extensions #js {"mention" (mention-utils/mention-ext users-list)}
        options {:toolbar false
                 :anchorPreview false
                 :extensions extensions
                 :imageDragging false
                 :targetBlank true
                 :autoLink true
                 :disableDoubleReturn true
                 :anchor #js {:customClassOption nil
                              :customClassOptionText "Button"
                              :linkValidation true
                              :placeholderText "Paste or type a link"
                              :targetCheckbox false
                              :targetCheckboxText "Open in new window"}
                 :paste #js {:forcePlainText true}
                 :placeholder #js {:text placeholder
                                   :hideOnClick true}
                 :keyboardCommands #js {:commands #js [
                                    #js {
                                      :command false
                                      :key "B"
                                      :meta true
                                      :shift false
                                      :alt false
                                    }
                                    #js {
                                      :command false
                                      :key "I"
                                      :meta true
                                      :shift false
                                      :alt false
                                    }
                                    #js {
                                      :command false
                                      :key "U"
                                      :meta true
                                      :shift false
                                      :alt false
                                    }]}}
        abstract-editor  (new js/MediumEditor abstract-el (clj->js options))]
    (.subscribe abstract-editor
     "editableInput"
     (fn [event editable]
       (abstract-on-change s)))
    (when (fn? (:on-enter-press-cb options))
      (.subscribe abstract-editor
       "editableKeypress"
       (fn [event editable]
        (js/console.log "DBG editableKeypress" event "key" (.-key event) "Enter?")
        (when (= (.-key event) "Enter")
          (.preventDefault event)
          ((:on-enter-press-cb options) event)))))
    (reset! (::editor s) abstract-editor)))

(rum/defcs abstract-field  < rum/reactive
                            (rum/local false ::did-change)
                            (rum/local nil ::editor)
                            (rum/local false ::exceeded-max-length)
                            ;; Image upload lock
                            (drv/drv :team-roster)
                            (mention-mixins/oc-mentions-hover)
                            {:did-mount (fn [s]
                              (let [props (first (:rum/args s))]
                                (when-not (:nux props)
                                  (utils/after 300 #(do
                                   (setup-editor s)
                                   (let [classes (:classes (first (:rum/args s)))]
                                     (when (string/includes? classes "emoji-autocomplete")
                                       (js/emojiAutocomplete)))))))
                              s)
                             :will-unmount (fn [s]
                              (when @(::editor s)
                                (.destroy @(::editor s)))
                              s)}
  [s {:keys [initial-abstract
             on-change
             classes
             max-length
             on-enter-press-cb]}]
  [:div.abstract-editor-container
    [:div.abstract-editor.oc-mentions.oc-mentions-hover.editing
      {:ref "abstract"
       :content-editable true
       :class (str classes
               (utils/class-set {:medium-editor-placeholder-hidden @(::did-change s)
                                 :exceeded-max-length @(::exceeded-max-length s)}))
       :dangerouslySetInnerHTML (utils/emojify initial-abstract)}]])