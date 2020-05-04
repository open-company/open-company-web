(ns oc.web.components.carrot-abstract
  (:require [rum.core :as rum]
            [dommy.core :refer-macros (sel1)]
            [org.martinklepsch.derivatives :as drv]
            [oc.web.lib.utils :as utils]
            [oc.web.lib.responsive :as responsive]
            [oc.web.utils.mention :as mention-utils]
            [oc.web.mixins.mention :as mention-mixins]))

(defn- setup-editor [s]
  (let [users-list (:mention-users @(drv/get-ref s :team-roster))]
    (when (and (seq users-list)
               (compare-and-set! (::initializing-editor s) false true))
      (let [options (first (:rum/args s))
            mobile-editor (responsive/is-tablet-or-mobile?)
            body-el (rum/ref-node s "abstract")
            buttons ["italic" "anchor"]
            abstract-on-change (:on-change-cb (first (:rum/args s)))
            extensions #js {"autolist" (js/AutoList.)
                            "mention" (mention-utils/mention-ext users-list)}
            options {:toolbar (if mobile-editor false #js {:buttons (clj->js buttons)})
                     :buttonLabels "fontawesome"
                     :anchorPreview (if mobile-editor false #js {:hideDelay 500, :previewValueSelector "a"})
                     :extensions extensions
                     :imageDragging false
                     :targetBlank true
                     :autoLink true
                     :anchor #js {:customClassOption nil
                                  :customClassOptionText "Button"
                                  :linkValidation true
                                  :placeholderText "Paste or type a link"
                                  :targetCheckbox false
                                  :targetCheckboxText "Open in new window"}
                     :paste #js {:forcePlainText false
                                 :cleanPastedHTML true
                                 :cleanAttrs #js ["style" "alt" "dir" "size" "face" "color" "itemprop" "name" "id"]
                                 :cleanTags #js ["meta" "video" "audio" "img" "button" "svg" "canvas" "figure" "input"
                                                 "textarea" "style" "javascript" "br"]
                                 :unwrapTags (clj->js (remove nil? ["div" "label" "font" "h1" "h2" "h3" "h4" "h5"
                                                       "h6" "strong" "section" "time" "em" "main" "u" "form" "header" "footer"
                                                       "details" "summary" "nav" "abbr" "ol" "ul" "li"
                                                       "table" "thead" "tbody" "tr" "th" "td" "p" "div" "strong" "b"]))}
                     :placeholder #js {:text utils/default-abstract
                                       :hideOnClick false
                                       :hide-on-click false}
                     :keyboardCommands #js {:commands #js [
                                        #js {
                                          :command false
                                          :key "B"
                                          :meta true
                                          :shift false
                                          :alt false
                                        }
                                        #js {
                                          :command "italic"
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
            body-editor  (new js/MediumEditor body-el (clj->js options))]
        (.subscribe body-editor
                    "editableInput"
                    (fn [event editable]
                      (abstract-on-change s)))
        (.subscribe body-editor
                    "editableKeydown"
                    (fn [e editable]
                      (let [opts (first (:rum/args s))
                            cmd-enter-cb (:cmd-enter-cb opts)]
                        (when (and (fn? cmd-enter-cb)
                                   (.-metaKey e)
                                   (= "Enter" (.-key e)))
                          (cmd-enter-cb e)))))
        (reset! (::editor s) body-editor)
        ;; Setup autocomplete
        (js/emojiAutocomplete)))))

;; Abstract component

(def abstract-show-counter-from 200)

(rum/defcs carrot-abstract < rum/reactive
                             ;; Locals
                             (rum/local false ::focused)
                             (rum/local nil ::editor)
                             (rum/local false ::initializing-editor)
                             ;; Mixins
                             (mention-mixins/oc-mentions-hover)
                             ;; Derivatives
                             (drv/drv :team-roster)
                             (drv/drv :users-info-hover)
                             (drv/drv :current-user-data)
                             (drv/drv :follow-publishers-list)
                             (drv/drv :followers-publishers-count)
                             {:did-mount (fn [s]
                              (setup-editor s)
                              s)
                             :did-remount (fn [o s]
                              (setup-editor s)
                              (when (not= (:cmail-key (first (:rum/args o))) (:cmail-key (first (:rum/args s))))
                                (when @(::editor s)
                                  (.destroy @(::editor s)))
                                (reset! (::editor s) nil)
                                (reset! (::initializing-editor s) false)
                                (utils/after 10 #(setup-editor s)))
                              s)
                             :will-update (fn [s]
                              s)
                             :will-unmount (fn [s]
                              (when @(::editor s)
                                (.destroy @(::editor s)))
                              s)}
  [s {:keys [initial-value value on-change-cb post-clicked abstract-length exceeds-limit cmail-key]}]
  (let [_team-roster (drv/react s :team-roster)
        _users-info-hover (drv/react s :users-info-hover)
        _current-user-data (drv/react s :current-user-data)
        _follow-publishers-list (drv/react s :follow-publishers-list)
        _followers-publishers-count (drv/react s :followers-publishers-count)]
    [:div.cmail-content-abstract-container
      {:key (str "carrot-abstract-" cmail-key)}
      [:div.cmail-content-abstract.emoji-autocomplete.emojiable.group.oc-mentions.oc-mentions-hover.editing
        {:class utils/hide-class
         :content-editable true
         :ref "abstract"
         :max-length utils/max-abstract-length
         :on-focus #(reset! (::focused s) true)
         :on-blur #(do
                    (reset! (::focused s) false)
                    (on-change-cb))
         :dangerouslySetInnerHTML {:__html initial-value}
         :on-key-down (fn [e]
                        ;; Prevent new line from being insert
                        (when (= (.-key e) "Enter")
                          (.preventDefault e))
                        ;; Call post-clicjed callback if available on Meta+Enter
                        (when (and (= (.-key e) "Enter")
                                   (.-metaKey e)
                                   (fn? post-clicked))
                          (post-clicked)))}]
       [:div.cmail-content-abstract-counter
        {:class (when exceeds-limit "exceeds-max-length")}
        (str abstract-length "/" utils/max-abstract-length
         (when exceeds-limit
           " (character limit exceeded)"))]]))