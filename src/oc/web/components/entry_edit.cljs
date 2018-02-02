(ns oc.web.components.entry-edit
  (:require-macros [if-let.core :refer (when-let*)])
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.lib.image-upload :as iu]
            [oc.web.lib.responsive :as responsive]
            [oc.web.lib.medium-editor-exts :as editor]
            [oc.web.components.ui.carrot-tip :refer (carrot-tip)]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]
            [oc.web.components.rich-body-editor :refer (rich-body-editor)]
            [oc.web.components.ui.small-loading :refer (small-loading)]
            [oc.web.components.ui.dropdown-list :refer (dropdown-list)]
            [goog.object :as gobj]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn should-show-divider-line [s]
  (when @(:first-render-done s)
    (when-let [entry-edit-modal-body (rum/ref-node s "entry-edit-modal-body")]
      (let [container-height (+ (.-clientHeight entry-edit-modal-body) 11) ;; Remove padding
            next-show-divider-line (> (.-scrollHeight entry-edit-modal-body) container-height)]
        (when (not= next-show-divider-line @(::show-divider-line s))
          (reset! (::show-divider-line s) next-show-divider-line))))))

(defn calc-entry-edit-modal-height
  [s & [force-calc]]
  (when @(:first-render-done s)
    (when-let [entry-edit-modal (rum/ref-node s "entry-edit-modal")]
      (when (or force-calc
                (not= @(::entry-edit-modal-height s) (.-clientHeight entry-edit-modal)))
        (reset! (::entry-edit-modal-height s) (.-clientHeight entry-edit-modal))))))

(defn dismiss-modal []
  (dis/dispatch! [:entry-edit/dismiss]))

(defn real-close [s]
  (reset! (::dismiss s) true)
  (utils/after 180 dismiss-modal))

;; Local cache for outstanding edits

(defn autosave []
  (let [body-el (sel1 [:div.rich-body-editor])
        cleaned-body (when body-el
                      (utils/clean-body-html (.-innerHTML body-el)))]
    (dis/dispatch! [:entry-save-on-exit :entry-editing cleaned-body])))

(defn save-on-exit?
  "Locally save the current outstanding edits if needed."
  [s]
  (when @(drv/get-ref s :entry-save-on-exit)
    (autosave)))

(defn toggle-save-on-exit
  "Enable and disable save current edit."
  [s turn-on?]
  (dis/dispatch! [:entry-toggle-save-on-exit turn-on?]))

;; Close dismiss handling

(defn cancel-clicked [s]
  (if @(::uploading-media s)
    (let [alert-data {:icon "/img/ML/trash.svg"
                      :action "dismiss-edit-uploading-media"
                      :message (str "Leave before finishing upload?")
                      :link-button-title "Stay"
                      :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                      :solid-button-style :red
                      :solid-button-title "Cancel upload"
                      :solid-button-cb #(do
                                          (dis/dispatch! [:alert-modal-hide])
                                          (real-close s))
                      }]
      (dis/dispatch! [:alert-modal-show alert-data]))
    (if (:has-changes @(drv/get-ref s :entry-editing))
      (let [alert-data {:icon "/img/ML/trash.svg"
                        :action "dismiss-edit-dirty-data"
                        :message (str "Leave without saving your changes?")
                        :link-button-title "Stay"
                        :link-button-cb #(dis/dispatch! [:alert-modal-hide])
                        :solid-button-style :red
                        :solid-button-title "Lose changes"
                        :solid-button-cb #(do
                                            (dis/dispatch! [:entry-clear-local-cache :entry-editing])
                                            (dis/dispatch! [:alert-modal-hide])
                                            (real-close s))
                        }]
        (dis/dispatch! [:alert-modal-show alert-data]))
      (real-close s))))

;; Data change handling

(defn body-on-change [state]
  (toggle-save-on-exit state true)
  (dis/dispatch! [:input [:entry-editing :has-changes] true])
  (calc-entry-edit-modal-height state))

(defn- headline-on-change [state]
  (toggle-save-on-exit state true)
  (when-let [headline (rum/ref-node state "headline")]
    (let [emojied-headline  (utils/emoji-images-to-unicode (gobj/get (utils/emojify (.-innerHTML headline)) "__html"))]
      (dis/dispatch! [:update [:entry-editing] #(merge % {:headline emojied-headline
                                                          :has-changes true})]))))

;; Headline setup and paste handler

(defn- setup-headline [state]
  (when-let [headline-el  (rum/ref-node state "headline")]
    (reset! (::headline-input-listener state) (events/listen headline-el EventType/INPUT #(headline-on-change state))))
  (js/emojiAutocomplete))

(defn headline-on-paste
  "Avoid to paste rich text into headline, replace it with the plain text clipboard data."
  [state e]
  ; Prevent the normal paste behaviour
  (utils/event-stop e)
  (let [clipboardData (or (.-clipboardData e) (.-clipboardData js/window))
        pasted-data   (.getData clipboardData "text/plain")]
    ; replace the selected text of headline with the text/plain data of the clipboard
    (js/replaceSelectedText pasted-data)
    ; call the headline-on-change to check for content length
    (headline-on-change state)
    (when-let [headline-el (rum/ref-node state "headline")]
      ; move cursor at the end
      (utils/to-end-of-content-editable headline-el))))

(defn add-emoji-cb [s]
  (headline-on-change s)
  (body-on-change s))

(defn- clean-body []
  (when-let [body-el (sel1 [:div.rich-body-editor])]
    (dis/dispatch! [:input [:entry-editing :body] (utils/clean-body-html (.-innerHTML body-el))])))

(defn- is-publishable? [entry-editing]
  (seq (:board-slug entry-editing)))

(rum/defcs entry-edit < rum/reactive
                        ;; Derivatives
                        (drv/drv :org-data)
                        (drv/drv :current-user-data)
                        (drv/drv :entry-editing)
                        (drv/drv :editable-boards)
                        (drv/drv :alert-modal)
                        (drv/drv :media-input)
                        (drv/drv :nux)
                        (drv/drv :entry-save-on-exit)
                        ;; Locals
                        (rum/local false ::dismiss)
                        (rum/local nil ::body-editor)
                        (rum/local "" ::initial-body)
                        (rum/local "" ::initial-headline)
                        (rum/local 330 ::entry-edit-modal-height)
                        (rum/local nil ::headline-input-listener)
                        (rum/local nil ::uploading-media)
                        (rum/local false ::show-divider-line)
                        (rum/local false ::saving)
                        (rum/local false ::publishing)
                        (rum/local false ::show-boards-dropdown)
                        (rum/local false ::window-resize-listener)
                        (rum/local nil ::autosave-timer)
                        ;; Mixins
                        mixins/no-scroll-mixin
                        mixins/first-render-mixin

                        {:will-mount (fn [s]
                          (let [nux @(drv/get-ref s :nux)
                                entry-editing @(drv/get-ref s :entry-editing)
                                initial-body (if (seq (:body entry-editing))
                                               (:body entry-editing)
                                               utils/default-body)
                                initial-headline (utils/emojify
                                                   (if (seq (:headline entry-editing))
                                                     (:headline entry-editing)
                                                     ""))]
                            (reset! (::initial-body s) initial-body)
                            (reset! (::initial-headline s) initial-headline))
                          s)
                         :did-mount (fn [s]
                          (when-not @(drv/get-ref s :nux)
                            (utils/after 300 #(setup-headline s))
                            (when-let [headline-el (rum/ref-node s "headline")]
                              (utils/to-end-of-content-editable headline-el)))
                          (reset! (::window-resize-listener s)
                           (events/listen
                            js/window
                            EventType/RESIZE
                            #(calc-entry-edit-modal-height s true)))
                          (reset! (::autosave-timer s) (utils/every 5000 autosave))
                          s)
                         :before-render (fn [s]
                          (calc-entry-edit-modal-height s)
                          ;; Set or remove the onBeforeUnload prompt
                          (let [save-on-exit @(drv/get-ref s :entry-save-on-exit)]
                            (set! (.-onbeforeunload js/window)
                             (if save-on-exit
                              #(do
                                (save-on-exit? s)
                                "Do you want to save before leaving?")
                              nil)))
                          ;; Handle saving/publishing states to dismiss the component
                          (let [entry-editing @(drv/get-ref s :entry-editing)]
                            ;; Entry is saving
                            (when @(::saving s)
                              ;: Save request finished
                              (when (not (:loading entry-editing))
                                (reset! (::saving s) false)
                                (when-not (:error entry-editing)
                                  (let [redirect? (not= (:status entry-editing) "published")]
                                    ;; If it's not published already redirect to drafts board
                                    (when redirect?
                                      (real-close s)
                                      (utils/after 250
                                       #(router/nav! (oc-urls/drafts (router/current-org-slug)))))))))
                            (when @(::publishing s)
                              (when (not (:publishing entry-editing))
                                (reset! (::publishing s) false)
                                (when-not (:error entry-editing)
                                  (let [redirect? (seq (:board-slug entry-editing))]
                                    ;; Redirect to the publishing board if the slug is available
                                    (when redirect?
                                      (real-close s)
                                      (utils/after
                                       250
                                       #(router/nav!
                                          (oc-urls/board (router/current-org-slug) (:board-slug entry-editing))))))))))
                          s)
                         :after-render  (fn [s] (should-show-divider-line s) s)
                         :will-unmount (fn [s]
                          (when @(::body-editor s)
                            (.destroy @(::body-editor s))
                            (reset! (::body-editor s) nil))
                          (when @(::headline-input-listener s)
                            (events/unlistenByKey @(::headline-input-listener s))
                            (reset! (::headline-input-listener s) nil))
                          (when @(::window-resize-listener s)
                            (events/unlistenByKey @(::window-resize-listener s))
                            (reset! (::window-resize-listener s) nil))
                          (when @(::autosave-timer s)
                            (js/clearInterval @(::autosave-timer s))
                            (reset! (::autosave-timer s) nil))
                          (set! (.-onbeforeunload js/window) nil)
                          s)}
  [s]
  (let [nux               (drv/react s :nux)
        org-data          (drv/react s :org-data)
        current-user-data (drv/react s :current-user-data)
        entry-editing     (drv/react s :entry-editing)
        alert-modal       (drv/react s :alert-modal)
        new-entry?        (empty? (:uuid entry-editing))
        is-mobile? (responsive/is-tablet-or-mobile?)
        fixed-entry-edit-modal-height (max @(::entry-edit-modal-height s) 330)
        wh (.-innerHeight js/window)
        media-input (drv/react s :media-input)
        all-boards (drv/react s :editable-boards)
        entry-board (get all-boards (:board-slug entry-editing))
        published? (= (:status entry-editing) "published")]
    [:div.entry-edit-modal-container
      {:class (utils/class-set {:will-appear (or @(::dismiss s) (not @(:first-render-done s)))
                                :appear (and (not @(::dismiss s)) @(:first-render-done s))})
       :on-click #(when (and (not (:has-changes entry-editing))
                             (not (utils/event-inside? % (rum/ref-node s "entry-edit-modal"))))
                    (cancel-clicked s))}
      [:div.modal-wrapper
        {:style {:margin-top (if is-mobile? "0px" (str (max 0 (/ (- wh fixed-entry-edit-modal-height) 2)) "px"))}}
        ;; Show the close button only when there are no modals shown
        (when (and (not (:media-video media-input))
                   (not (:media-chart media-input))
                   (not alert-modal)
                   (not nux)
                   (not is-mobile?))
          [:button.carrot-modal-close.mlb-reset
            {:on-click #(cancel-clicked s)}])
        [:div.entry-edit-modal.group
          {:ref "entry-edit-modal"}
          (if is-mobile?
            [:div.entry-edit-modal-header-mobile
              [:div.mobile-header
                [:button.mlb-reset.mobile-modal-close-bt
                  {:on-click #(cancel-clicked s)}]
                [:div.mobile-header-right
                  [:button.mlb-reset
                    {:ref "mobile-post-btn"
                     :on-click (fn [_]
                                 (clean-body)
                                 (if (and (is-publishable? entry-editing)
                                          (not (zero? (count (:headline entry-editing)))))
                                   (if published?
                                     (do
                                       (reset! (::saving s) true)
                                       (dis/dispatch! [:entry-save]))
                                     (do
                                       (reset! (::publishing s) true)
                                       (dis/dispatch! [:entry-publish])))
                                   (when (zero? (count (:headline entry-editing)))
                                     (when-let [$post-btn (js/$ (rum/ref-node s "mobile-post-btn"))]
                                       (when-not (.data $post-btn "bs.tooltip")
                                         (.tooltip $post-btn
                                          (clj->js {:container "body"
                                                    :placement "bottom"
                                                    :trigger "manual"
                                                    :template (str "<div class=\"tooltip post-btn-tooltip\">"
                                                                     "<div class=\"tooltip-arrow\"></div>"
                                                                     "<div class=\"tooltip-inner\"></div>"
                                                                   "</div>")
                                                    :title "A title is required in order to save or share this post."})))
                                       (utils/after 10 #(.tooltip $post-btn "show"))
                                       (utils/after 5000 #(.tooltip $post-btn "hide"))))))
                     :class (when (or @(::publishing s)
                                      (not (is-publishable? entry-editing))
                                      (zero? (count (:headline entry-editing))))
                              "disabled")}
                    (when (or (and published?
                                   @(::saving s))
                              (and (not published?)
                                   @(::publishing s)))
                      (small-loading))
                    (if published?
                      "Save"
                      "Post")]
                  (when (and (not nux)
                             (not published?))
                    [:button.mlb-reset
                      {:disabled (or @(::saving s)
                                     (not (:has-changes entry-editing)))
                       :on-click (fn [_]
                                  (clean-body)
                                  (reset! (::saving s) true)
                                  (dis/dispatch! [:entry-save]))}
                      (when @(::saving s)
                        (small-loading))
                      "Save to draft"])]]
              [:div.mobile-second-header
                [:div.posting-in
                  [:span
                    (if (:uuid entry-editing)
                      "Draft for: "
                      "Posting in: ")]
                  [:div.boards-dropdown-caret
                    {:on-click #(reset! (::show-boards-dropdown s) (not @(::show-boards-dropdown s)))
                     :class (when (not nux) "no-nux")}
                    (:board-name entry-editing)
                    (when (and (not nux) @(::show-boards-dropdown s))
                      (dropdown-list
                       {:items (map
                                #(clojure.set/rename-keys % {:name :label :slug :value})
                                (vals all-boards))
                        :value (:board-slug entry-editing)
                        :on-blur #(reset! (::show-boards-dropdown s) false)
                        :on-change (fn [item]
                                     (toggle-save-on-exit s true)
                                     (dis/dispatch! [:input [:entry-editing :has-changes] true])
                                     (dis/dispatch! [:input [:entry-editing :board-slug] (:value item)])
                                     (dis/dispatch! [:input [:entry-editing :board-name] (:label item)]))}))]]]]
            [:div.entry-edit-modal-header.group
              (user-avatar-image current-user-data)
              [:div.posting-in
                [:span
                  (if (:uuid entry-editing)
                    "Draft for "
                    "Posting in ")]
                [:div.boards-dropdown-caret
                  {:on-click #(reset! (::show-boards-dropdown s) (not @(::show-boards-dropdown s)))
                   :class (when (not nux) "no-nux")}
                  (:board-name entry-editing)
                  (when (and (not nux) @(::show-boards-dropdown s))
                    (dropdown-list
                     {:items (map
                              #(clojure.set/rename-keys % {:name :label :slug :value})
                              (vals all-boards))
                      :value (:board-slug entry-editing)
                      :on-blur #(reset! (::show-boards-dropdown s) false)
                      :on-change (fn [item]
                                   (toggle-save-on-exit s true)
                                   (dis/dispatch! [:input [:entry-editing :has-changes] true])
                                   (dis/dispatch! [:input [:entry-editing :board-slug] (:value item)])
                                   (dis/dispatch! [:input [:entry-editing :board-name] (:label item)]))}))]]])
        [:div.entry-edit-modal-body
          {:ref "entry-edit-modal-body"}
          ; Headline element
          [:div.entry-edit-headline.emoji-autocomplete.emojiable
            {:content-editable (not nux)
             :ref "headline"
             :placeholder utils/default-headline
             :on-paste    #(headline-on-paste s %)
             :on-key-down #(headline-on-change s)
             :on-click    #(headline-on-change s)
             :on-key-press (fn [e]
                           (when (= (.-key e) "Enter")
                             (utils/event-stop e)
                             (utils/to-end-of-content-editable (sel1 [:div.rich-body-editor]))))
             :dangerouslySetInnerHTML @(::initial-headline s)}]
          (rich-body-editor {:on-change (partial body-on-change s)
                             :initial-body @(::initial-body s)
                             :show-placeholder (not (contains? entry-editing :links))
                             :show-h2 true
                             :nux nux
                             :dispatch-input-key :entry-editing
                             :upload-progress-cb (fn [is-uploading?]
                                                   (reset! (::uploading-media s) is-uploading?))
                             :media-config ["photo" "video" "chart" "attachment"]
                             :classes "emoji-autocomplete emojiable"})
          [:div.entry-edit-controls-right]]
          ; Bottom controls
          [:div.entry-edit-controls.group]
        [:div.entry-edit-modal-divider
          {:class (when-not @(::show-divider-line s) "not-visible")}]
        [:div.entry-edit-modal-footer.group
          (when (and (not nux)
                     (not (responsive/is-tablet-or-mobile?))
                     (not (js/isIE)))
            (emoji-picker {:add-emoji-cb (partial add-emoji-cb s)
                           :container-selector "div.entry-edit-modal"}))
          (when nux
            (when-let* [post-button (js/$ "div.entry-edit-modal button.post-btn")
                        post-button-offset (.offset post-button)]
              (carrot-tip {:step :2
                           :circle-offset {:top -90
                                           :left -550}
                           :x (- (aget post-button-offset "left") 522)
                           :y (- (aget post-button-offset "top") 20)
                           :title "Here’s a sample post"
                           :message "Click the green Post button to see how it works."
                           :width 494})))
          [:button.mlb-reset.mlb-default.form-action-bt.post-btn
            {:ref "post-btn"
             :on-click (fn [_]
                         (clean-body)
                         (if (and (is-publishable? entry-editing)
                                  (not (zero? (count (:headline entry-editing)))))
                           (if published?
                             (do
                               (reset! (::saving s) true)
                               (dis/dispatch! [:entry-save]))
                             (do
                               (reset! (::publishing s) true)
                               (dis/dispatch! [:entry-publish])))
                           (when (zero? (count (:headline entry-editing)))
                             (when-let [$post-btn (js/$ (rum/ref-node s "post-btn"))]
                               (when-not (.data $post-btn "bs.tooltip")
                                 (.tooltip $post-btn
                                  (clj->js {:container "body"
                                            :placement "top"
                                            :trigger "manual"
                                            :template (str "<div class=\"tooltip post-btn-tooltip\">"
                                                             "<div class=\"tooltip-arrow\"></div>"
                                                             "<div class=\"tooltip-inner\"></div>"
                                                           "</div>")
                                            :title "A title is required in order to save or share this post."})))
                               (utils/after 10 #(.tooltip $post-btn "show"))
                               (utils/after 5000 #(.tooltip $post-btn "hide"))))))
             :class (when (or @(::publishing s)
                              (not (is-publishable? entry-editing)))
                      "disabled")}
            (when (or (and published?
                           @(::saving s))
                      (and (not published?)
                           @(::publishing s)))
              (small-loading))
            (if published?
              "Save"
              "Post")]
          (when-not nux
            [:button.mlb-reset.mlb-link-black.form-action-bt
              {:disabled (or @(::saving s)
                             (not (:has-changes entry-editing)))
               :on-click #(if published?
                            (cancel-clicked s)
                            (do
                              (clean-body)
                              (reset! (::saving s) true)
                              (dis/dispatch! [:entry-save])))}
              (when (and (not published?)
                         @(::saving s))
                (small-loading))
              (if published?
                "Cancel"
                "Save draft")])]]]]))