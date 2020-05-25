(ns oc.web.components.cmail
  (:require [rum.core :as rum]
            [cuerdas.core :as str]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [taoensso.timbre :as timbre]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.router :as router]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.local-settings :as ls]
            [oc.web.utils.activity :as au]
            [oc.web.utils.ui :as ui-utils]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.lib.image-upload :as iu]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.actions.routing :as routing-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.actions.payments :as payments-actions]
            [oc.web.components.ui.poll :refer (polls-wrapper)]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.trial-expired-banner :refer (trial-expired-alert)]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.rich-body-editor :refer (rich-body-editor)]
            [oc.web.components.ui.sections-picker :refer (sections-picker)]
            [oc.web.components.ui.stream-attachments :refer (stream-attachments)]
            [oc.web.components.ui.post-to-button :refer (post-to-button)]
            [goog.dom :as gdom]
            [goog.Uri :as guri]
            [goog.object :as gobj]
            [clojure.contrib.humanize :refer (filesize)])
  (:import [goog.async Debouncer]))

(def self-board-name "All")
(def board-tooltip "Select a team")

;; Attachments handling

(defn media-attachment-dismiss-picker
  "Called every time the image picke close, reset to inital state."
  [s]
  (when-not @(::media-attachment-did-success s)
    (reset! (::media-attachment s) false)))

(defn attachment-upload-failed-cb [state]
  (let [alert-data {:icon "/img/ML/error_icon.png"
                    :action "attachment-upload-error"
                    :title "Sorry!"
                    :message "An error occurred with your file."
                    :solid-button-title "OK"
                    :solid-button-cb #(alert-modal/hide-alert)}]
    (alert-modal/show-alert alert-data)
    (utils/after 10 #(do
                       (reset! (::media-attachment-did-success state) false)
                       (media-attachment-dismiss-picker state)))))

(defn attachment-upload-success-cb [state res]
  (reset! (::media-attachment-did-success state) true)
  (let [url (gobj/get res "url")]
    (if-not url
      (attachment-upload-failed-cb state)
      (let [size (gobj/get res "size")
            mimetype (gobj/get res "mimetype")
            filename (gobj/get res "filename")
            createdat (utils/js-date)
            prefix (str "Uploaded by " (jwt/get-key :name) " on " (utils/date-string createdat [:year]) " - ")
            subtitle (str prefix (filesize size :binary false :format "%.2f" ))
            icon (au/icon-for-mimetype mimetype)
            attachment-data {:file-name filename
                             :file-type mimetype
                             :file-size size
                             :file-url url}]
        (reset! (::media-attachment state) false)
        (activity-actions/add-attachment :cmail-data attachment-data)
        (utils/after 1000 #(reset! (::media-attachment-did-success state) false))))))

(defn attachment-upload-error-cb [state res error]
  (attachment-upload-failed-cb state))

(defn add-attachment [s]
  (reset! (::media-attachment s) true)
  (iu/upload!
   nil
   (partial attachment-upload-success-cb s)
   nil
   (partial attachment-upload-error-cb s)
   (fn []
     (utils/after 400 #(media-attachment-dismiss-picker s)))))

;; Data handling

(defn- body-element []
  (sel1 [:div.rich-body-editor]))

(defn- cleaned-body []
  (when-let [body-el (body-element)]
    (utils/clean-body-html (.-innerHTML body-el))))

(defn real-close []
  (cmail-actions/cmail-hide))

(defn headline-element [s]
  (rum/ref-node s "headline"))

(defn- fix-headline [headline]
  (utils/trim (str/replace (or headline "") #"\n" "")))

(defn- clean-body [s]
  (when (body-element)
    (dis/dispatch! [:input [:cmail-data :body] (cleaned-body)])))

;; Local cache for outstanding edits

(defn autosave [s]
  (let [cmail-data @(drv/get-ref s :cmail-data)
        section-editing @(drv/get-ref s :section-editing)]
    (activity-actions/entry-save-on-exit :cmail-data cmail-data (cleaned-body) section-editing)))

(defn debounced-autosave!
  [s]
  (.fire @(::debounced-autosave s)))

(defn cancel-autosave!
  [s]
  (.stop @(::debounced-autosave s)))

;; Close dismiss handling

(defn cancel-clicked [s]
  (let [cmail-data @(drv/get-ref s :cmail-data)
        clean-fn (fn [dismiss-modal?]
                    (activity-actions/entry-clear-local-cache (:uuid cmail-data) :cmail-data cmail-data)
                    (when dismiss-modal?
                      (alert-modal/hide-alert))
                    (real-close))]
    (if @(::uploading-media s)
      (let [alert-data {:icon "/img/ML/trash.svg"
                        :action "dismiss-edit-uploading-media"
                        :message (str "Leave before finishing upload?")
                        :link-button-title "Stay"
                        :link-button-cb #(alert-modal/hide-alert)
                        :solid-button-style :red
                        :solid-button-title "Cancel upload"
                        :solid-button-cb #(clean-fn true)}]
        (alert-modal/show-alert alert-data))
      (if (:has-changes cmail-data)
        (let [alert-data {:icon "/img/ML/trash.svg"
                          :action "dismiss-edit-dirty-data"
                          :message (str "Leave without saving your changes?")
                          :link-button-title "Stay"
                          :link-button-cb #(alert-modal/hide-alert)
                          :solid-button-style :red
                          :solid-button-title "Lose changes"
                          :solid-button-cb #(clean-fn true)}]
          (alert-modal/show-alert alert-data))
        (clean-fn false)))))

;; Data change handling

(defn body-on-change [state]
  (dis/dispatch! [:input [:cmail-data :has-changes] true])
  (debounced-autosave! state)
  (when-let [body-el (body-element)]
    (reset! (::last-body state) (.-innerHTML body-el))))

(defn- headline-on-change [state]
  (when-let [headline (headline-element state)]
    (let [clean-headline (fix-headline (.-innerText headline))
          post-button-title (when-not (seq clean-headline) :title)]
      (dis/dispatch! [:update [:cmail-data] #(merge % {:headline clean-headline
                                                       :has-changes true})])
      (reset! (::post-tt-kw state) post-button-title)
      (debounced-autosave! state))))

;; Headline setup and paste handler

(defn- setup-headline [state]
  (when-let [headline-el  (headline-element state)]
    (reset! (::headline-input-listener state) (events/listen headline-el EventType/INPUT #(headline-on-change state))))
  (js/emojiAutocomplete))

(defn headline-on-paste
  "Avoid to paste rich text into headline, replace it with the plain text clipboard data."
  [state e]
  ; Prevent the normal paste behavior
  (utils/event-stop e)
  (let [clipboardData (or (.-clipboardData e) (.-clipboardData js/window))
        pasted-data   (.getData clipboardData "text/plain")]
    ; replace the selected text of headline with the text/plain data of the clipboard
    (js/replaceSelectedText pasted-data)
    ; call the headline-on-change to check for content length
    (headline-on-change state)
    (when (= (.-activeElement js/document) (.-body js/document))
      (when-let [headline-el (headline-element state)]
        ; move cursor at the end
        (utils/to-end-of-content-editable headline-el)))))

(defn add-emoji-cb [s]
  (utils/after 180
   #(do
     (headline-on-change s)
     (body-on-change s))))

(defn- is-publishable? [cmail-data]
  (and (seq (fix-headline (:headline cmail-data)))
       (seq (:board-slug cmail-data))))

(defn real-post-action [s]
  (let [cmail-data @(drv/get-ref s :cmail-data)
        fixed-headline (fix-headline (:headline cmail-data))
        published? (= (:status cmail-data) "published")]
      (if (is-publishable? cmail-data)
        (let [_ (dis/dispatch! [:update [:cmail-data] #(merge % {:headline fixed-headline})])
              updated-cmail-data @(drv/get-ref s :cmail-data)
              section-editing @(drv/get-ref s :section-editing)]
          (if published?
            (do
              (reset! (::saving s) true)
              (activity-actions/entry-save :cmail-data updated-cmail-data section-editing))
            (do
              (reset! (::publishing s) true)
              (activity-actions/entry-publish (dissoc updated-cmail-data :status) section-editing :cmail-data))))
        (do
          (reset! (::show-post-tooltip s) true)
          (utils/after 3000 #(reset! (::show-post-tooltip s) false))
          (reset! (::disable-post s) false)))))

(defn post-clicked [s]
  (clean-body s)
  (reset! (::disable-post s) true)
  (cancel-autosave! s)
  (real-post-action s))

(defn fix-tooltips
  "Fix the tooltips"
  [s]
  (doto (.find (js/$ (rum/dom-node s)) "[data-toggle=\"tooltip\"]")
    (.tooltip "hide")
    (.tooltip "fixTitle")))

;; Delete handling

(defn delete-clicked [s e activity-data]
  (if (or (:uuid activity-data)
           (:links activity-data)
           (:auto-saving activity-data))
    (let [post-type (if (= (:status activity-data) "published")
                      "post"
                      "draft")
          alert-data {:icon "/img/ML/trash.svg"
                      :action "delete-entry"
                      :message (str "Delete this " post-type "?")
                      :link-button-title "No"
                      :link-button-cb #(alert-modal/hide-alert)
                      :solid-button-style :red
                      :solid-button-title "Yes"
                      :solid-button-cb #(do
                                         (activity-actions/activity-delete activity-data)
                                         (alert-modal/hide-alert))
                      }]
      (alert-modal/show-alert alert-data))
    (do
      ;; In case the data are queued up to be saved but the request didn't started yet
      (when (:has-changes activity-data)
        ;; Remove them
        (dis/dispatch! [:update [:cmail-data] #(dissoc % :has-changes)]))
      (cmail-actions/cmail-hide))))

(defn win-width []
  (or (.-clientWidth (.-documentElement js/document))
      (.-innerWidth js/window)))

(defn calc-video-height [s]
  (when (responsive/is-tablet-or-mobile?)
    (reset! (::mobile-video-height s) (utils/calc-video-height (win-width)))))

(defn- collapse-if-needed [s & [e]]
  (let [cmail-data @(drv/get-ref s :cmail-data)
        cmail-state @(drv/get-ref s :cmail-state)
        showing-section-picker? @(::show-sections-picker s)
        event-in? (if e
                    (utils/event-inside? e (rum/dom-node s))
                    false)]
    (when-not (or (:fullscren cmail-state)
                  (:collapsed cmail-state)
                  event-in?
                  (:has-changes cmail-data)
                  (:auto-saving cmail-data)
                  (:uuid cmail-data)
                  showing-section-picker?)
      (real-close)
      (.blur (headline-element s)))))

(rum/defcs cmail < rum/reactive
                   ;; Derivatives
                   (drv/drv :cmail-state)
                   (drv/drv :cmail-data)
                   (drv/drv :section-editing)
                   (drv/drv :show-edit-tooltip)
                   (drv/drv :current-user-data)
                   (drv/drv :payments)
                   (drv/drv :follow-boards-list)
                   (drv/drv :editable-boards)
                   ;; Locals
                   (rum/local "" ::initial-body)
                   (rum/local "" ::initial-headline)
                   (rum/local true ::show-placeholder)
                   (rum/local nil ::initial-uuid)
                   (rum/local nil ::headline-input-listener)
                   (rum/local nil ::uploading-media)
                   (rum/local false ::saving)
                   (rum/local false ::publishing)
                   (rum/local false ::disable-post)
                   (rum/local nil ::debounced-autosave)
                   (rum/local 0 ::mobile-video-height)
                   (rum/local false ::media-attachment-did-success)
                   (rum/local nil ::media-attachment)
                   (rum/local nil ::latest-key)
                   (rum/local false ::show-post-tooltip)
                   (rum/local false ::show-sections-picker)
                   (rum/local nil ::last-body)
                   (rum/local nil ::post-tt-kw)
                   ;; Mixins
                   (mixins/render-on-resize calc-video-height)
                   mixins/refresh-tooltips-mixin
                   ;; Go back to collapsed state on desktop if user didn't touch anything
                   (when-not (responsive/is-mobile-size?)
                     (mixins/on-window-click-mixin collapse-if-needed))
                   ;; Dismiss sectoins picker on window clicks, slightly delay it to avoid
                   ;; conflicts with the collapse cmail listener
                   (mixins/on-window-click-mixin (fn [s e]
                    (let [showing-section-picker? @(::show-sections-picker s)
                          event-in? (utils/event-inside? e (rum/ref-node s :sections-picker-container))]
                      (utils/after 100
                       #(when (and showing-section-picker?
                                   (not event-in?))
                         (reset! (::show-sections-picker s) false))))))

                   {:will-mount (fn [s]
                    (let [cmail-data @(drv/get-ref s :cmail-data)
                          cmail-state @(drv/get-ref s :cmail-state)
                          initial-body (if (seq (:body cmail-data))
                                         (:body cmail-data)
                                         au/empty-body-html)
                          initial-headline (utils/emojify
                                             (if (seq (:headline cmail-data))
                                               (:headline cmail-data)
                                               ""))
                          body-text (.text (.html (js/$ "<div/>") initial-body))]
                      (when-not (seq (:uuid cmail-data))
                        (nux-actions/dismiss-add-post-tooltip))
                      (reset! (::last-body s) initial-body)
                      (reset! (::initial-body s) initial-body)
                      (reset! (::initial-headline s) initial-headline)
                      (reset! (::initial-uuid s) (:uuid cmail-data))
                      (reset! (::saving s) (:loading cmail-data))
                      (reset! (::publishing s) (:publishing cmail-data))
                      (reset! (::show-placeholder s) (not (.match initial-body #"(?i).*(<iframe\s?.*>).*")))
                      (reset! (::latest-key s) (:key cmail-state))
                      (reset! (::post-tt-kw s) (when-not (seq (:headline cmail-data)) :title)))
                    (when (responsive/is-mobile-size?)
                      (dom-utils/lock-page-scroll))
                    s)
                   :did-mount (fn [s]
                    (calc-video-height s)
                    (utils/after 300 #(setup-headline s))
                    (reset! (::debounced-autosave s) (Debouncer. (partial autosave s) 2000))
                    s)
                   :will-update (fn [s]
                    (let [cmail-state @(drv/get-ref s :cmail-state)]
                      ;; If the state key changed let's reset the initial values
                      (when (not= @(::latest-key s) (:key cmail-state))
                        (when @(::latest-key s)
                          (let [cmail-data @(drv/get-ref s :cmail-data)
                                cmail-state @(drv/get-ref s :cmail-state)
                                initial-body (if (seq (:body cmail-data))
                                               (:body cmail-data)
                                               au/empty-body-html)
                                initial-headline (utils/emojify
                                                   (if (seq (:headline cmail-data))
                                                     (:headline cmail-data)
                                                     ""))
                                body-text (.text (.html (js/$ "<div/>") initial-body))]
                            (when-not (seq (:uuid cmail-data))
                              (nux-actions/dismiss-add-post-tooltip))
                            (reset! (::last-body s) initial-body)
                            (reset! (::initial-body s) initial-body)
                            (reset! (::initial-headline s) initial-headline)
                            (reset! (::initial-uuid s) (:uuid cmail-data))
                            (reset! (::show-placeholder s) (not (.match initial-body #"(?i).*(<iframe\s?.*>).*")))
                            (reset! (::post-tt-kw s) (when-not (seq (:headline cmail-data)) :title))))
                        (reset! (::latest-key s) (:key cmail-state))))
                    s)
                   :before-render (fn [s]
                    ;; Handle saving/publishing states to dismiss the component
                    (when-let [cmail-data @(drv/get-ref s :cmail-data)]
                      ;; Did activity get removed here or in another client?
                      (when (:delete cmail-data)
                        (real-close))
                      (when (and @(::saving s)
                                 (not (:loading cmail-data)))
                        (reset! (::saving s) false)
                        (reset! (::disable-post s) false)
                        (when-not (:error cmail-data)
                          (utils/after 100 real-close)))
                      (when (and @(::publishing s)
                                 (not (:publishing cmail-data)))
                        (reset! (::publishing s) false)
                        (reset! (::disable-post s) false)
                        (when-not (:error cmail-data)
                          (when (seq (:board-slug cmail-data))
                            ;; Redirect to the publishing board if the slug is available
                            (real-close)
                            (utils/after
                             180
                             #(let [following-board-uuids (set (map :uuid @(drv/get-ref s :follow-boards-list)))
                                    following-cmail-board? (following-board-uuids (:board-uuid cmail-data))
                                    is-in-cmail-board? (= (router/current-board-slug) (:board-slug cmail-data))
                                    is-home? (= (router/current-board-slug) "following")]
                                (router/nav! (cond
                                               ;; If user posted from that board let's leave him there
                                               is-in-cmail-board?
                                               (oc-urls/board (:board-slug cmail-data))
                                               ;; If use is following the board they posted to
                                               ;; and they are in home
                                               (and following-cmail-board?
                                                    is-home?)
                                               (oc-urls/following)
                                               ;; If user is publishing to its own publisher board
                                               ;; redirect him there
                                               (:publisher-board cmail-data)
                                               (oc-urls/contributions (:user-id @(drv/get-ref s :current-user-data)))
                                               ;; Redirect to the posting board in every other case
                                               :else
                                               (oc-urls/board (:board-slug cmail-data))))))))))
                    s)
                   :after-render (fn [s]
                    (fix-tooltips s)
                    s)
                   :will-unmount (fn [s]
                    (nux-actions/dismiss-edit-tooltip)
                    (when @(::headline-input-listener s)
                      (events/unlistenByKey @(::headline-input-listener s))
                      (reset! (::headline-input-listener s) nil))
                    (when (responsive/is-mobile-size?)
                      (dom-utils/unlock-page-scroll))
                    (when-let [debounced-autosave @(::debounced-autosave s)]
                      (.dispose debounced-autosave))
                    s)}
  [s]
  (let [is-mobile? (responsive/is-tablet-or-mobile?)
        cmail-state (drv/react s :cmail-state)
        cmail-data* (drv/react s :cmail-data)
        cmail-data (update cmail-data* :board-name
                    #(if (:publisher-board cmail-data*)
                       self-board-name
                       %))
        payments-data (drv/react s :payments)
        show-paywall-alert? (payments-actions/show-paywall-alert? payments-data)
        published? (= (:status cmail-data) "published")
        video-size (if is-mobile?
                     {:width (win-width)
                      :height @(::mobile-video-height s)}
                     {:width 548
                      :height (utils/calc-video-height 548)})
        show-edit-tooltip (and (drv/react s :show-edit-tooltip)
                               (not (seq @(::initial-uuid s))))
        publishable? (is-publishable? cmail-data)
        show-post-bt-tooltip? (not publishable?)
        post-tt-kw @(::post-tt-kw s)
        disabled? (or show-post-bt-tooltip?
                      show-paywall-alert?
                      (au/empty-body? @(::last-body s))
                      (not publishable?)
                      @(::publishing s)
                      @(::disable-post s))
        working? (or (and published?
                          @(::saving s))
                     (and (not published?)
                          @(::publishing s)))
        close-cb (fn [_]
                  (if (au/has-content? (assoc cmail-data
                                         :body
                                         (cleaned-body)))
                    (autosave s)
                    (activity-actions/activity-delete cmail-data))
                  (if (and (= (:status cmail-data) "published")
                           (:has-changes cmail-data))
                    (cancel-clicked s)
                    (cmail-actions/cmail-hide)))
        unpublished? (not= (:status cmail-data) "published")
        post-button-title (if (= (:status cmail-data) "published")
                            "Save"
                            "Share")
        did-pick-section (fn [board-data note dismiss-action]
                           (reset! (::show-sections-picker s) false)
                           (dis/dispatch! [:input [:show-sections-picker] false])
                           (when (and board-data
                                      (seq (:name board-data)))
                            (let [has-changes (or (:has-changes cmail-data)
                                                  (seq (:uuid cmail-data))
                                                  (:auto-saving cmail-data))]
                              (dis/dispatch! [:input [:cmail-data]
                               (merge cmail-data {:board-slug (:slug board-data)
                                                  :board-name (:name board-data)
                                                  :board-access (:access board-data)
                                                  :publisher-board (:publisher-board board-data)
                                                  :has-changes has-changes
                                                  :invite-note note})])
                              (when has-changes
                                (debounced-autosave! s)))
                            (when (fn? dismiss-action)
                              (dismiss-action))))
        current-user-data (drv/react s :current-user-data)
        editable-boards (drv/react s :editable-boards)
        show-section-picker? (or ;; Publisher board can still be creaeted
                                 (and (not (some :publisher-board editable-boards))
                                      ls/publisher-board-enabled?
                                      (pos? (count editable-boards)))
                                 (> (count editable-boards) 1))]
    [:div.cmail-outer
      {:class (utils/class-set {:quick-post-collapsed (or (:collapsed cmail-state) show-paywall-alert?)
                                :show-trial-expired-alert show-paywall-alert?})
       :on-click (when (and (not is-mobile?)
                            (:collapsed cmail-state)
                            (not show-paywall-alert?))
                   (fn [e]
                      (nux-actions/dismiss-add-post-tooltip)
                      (cmail-actions/cmail-expand cmail-data cmail-state)
                      (utils/after 280
                       #(when-let [el (headline-element s)]
                          (.focus el)))))}
      (when (and show-paywall-alert?
                 (:collapsed cmail-state))
        (trial-expired-alert {:top "48px" :left "50%"}))
      [:div.cmail-container
        [:div.cmail-mobile-header
          [:button.mlb-reset.mobile-close-bt
            {:on-click close-cb}]
          [:div.cmail-mobile-header-right
            [:button.mlb-reset.mobile-attachment-button
              {:on-click #(add-attachment s)}]
            [:div.post-button-container.group
              (post-to-button {:on-submit #(post-clicked s)
                               :disabled disabled?
                               :title post-button-title
                               :post-tt-kw post-tt-kw
                               :force-show-tooltip @(::show-post-tooltip s)})]]]
        [:div.dismiss-inline-cmail-container
          {:class (when unpublished? "long-tooltip")}
          [:button.mlb-reset.dismiss-inline-cmail
            {:on-click close-cb
             :data-toggle (when-not is-mobile? "tooltip")
             :data-placement "right"
             :title (if unpublished?
                      "Save & Close"
                      "Close")}]]
        [:div.cmail-content-outer
          {:class (utils/class-set {:showing-edit-tooltip show-edit-tooltip})}
          [:div.cmail-content
            {:class (when show-section-picker? "section-picker-visible")}
            (when is-mobile?
              [:div.section-picker-bt-container
                [:span.post-to "Post to"]
                [:button.mlb-reset.section-picker-bt
                  {:on-click #(swap! (::show-sections-picker s) not)}
                  (:board-name cmail-data)]
                (when @(::show-sections-picker s)
                  [:div.sections-picker-container
                    {:ref :sections-picker-container}
                    (sections-picker {:active-slug (:board-slug cmail-data)
                                      :on-change did-pick-section
                                      :current-user-data current-user-data})])])
            ; Headline element
            [:div.cmail-content-headline-container.group
              [:div.cmail-content-headline.emoji-autocomplete.emojiable
                {:class utils/hide-class
                 :content-editable true
                 :key (str "cmail-headline-" (:key cmail-state))
                 :placeholder au/headline-placeholder
                 :ref "headline"
                 :on-paste    #(headline-on-paste s %)
                 :on-key-down (fn [e]
                                (utils/after 10 #(headline-on-change s))
                                (cond
                                  (and (.-metaKey e)
                                       (= "Enter" (.-key e)))
                                  (post-clicked s)
                                  (and (= (.-key e) "Enter")
                                       (not (.-metaKey e)))
                                  (do
                                    (utils/event-stop e)
                                    (utils/to-end-of-content-editable (body-element)))))
                 :dangerouslySetInnerHTML @(::initial-headline s)}]]
            (when-not is-mobile?
              [:div.cmail-content-collapsed-placeholder
                (str utils/default-body-placeholder "...")])
            ; Attachments
            (stream-attachments (:attachments cmail-data) nil
             #(activity-actions/remove-attachment :cmail-data %))
            (rich-body-editor {:on-change (partial body-on-change s)
                               :use-inline-media-picker true
                               :static-positioned-media-picker true
                               :media-picker-initially-visible false
                               :initial-body @(::initial-body s)
                               :show-placeholder @(::show-placeholder s)
                               :show-h2 true
                               ;; Block the rich-body-editor component when
                               ;; the current editing post has been created already
                               :paywall? show-paywall-alert?
                               :placeholder (str utils/default-body-placeholder "...")
                               :dispatch-input-key :cmail-data
                               :cmd-enter-cb #(post-clicked s)
                               :upload-progress-cb (fn [is-uploading?]
                                                     (reset! (::uploading-media s) is-uploading?))
                               :media-config ["poll" "code" "gif" "photo" "video"]
                               :classes (str (when-not show-paywall-alert? "emoji-autocomplete ") "emojiable " utils/hide-class)
                               :cmail-key (:key cmail-state)
                               :attachments-enabled true})
            (when (seq (:polls cmail-data))
              (polls-wrapper {:polls-data (:polls cmail-data)
                              :editing? true
                              :current-user-id (jwt/user-id)
                              :container-selector "div.cmail-content"
                              :dispatch-key :cmail-data
                              :activity-data cmail-data}))]]
      [:div.cmail-footer
        [:div.post-button-container.group
          (post-to-button {:on-submit #(post-clicked s)
                           :disabled disabled?
                           :title post-button-title
                           :post-tt-kw post-tt-kw
                           :force-show-tooltip @(::show-post-tooltip s)
                           :show-on-hover true})]
        [:div.section-picker-bt-container
          {:class (when-not show-section-picker? "hidden")}
          [:button.mlb-reset.section-picker-bt
            {:on-click #(swap! (::show-sections-picker s) not)
             :data-placement "top"
             :data-toggle "tooltip"
             :title board-tooltip}
            [:span.prefix "#"]
            (:board-name cmail-data)]
          (when @(::show-sections-picker s)
            [:div.sections-picker-container
              {:ref :sections-picker-container}
              (sections-picker {:active-slug (:board-slug cmail-data)
                                :on-change did-pick-section
                                :current-user-data current-user-data})])]
        (emoji-picker {:add-emoji-cb (partial add-emoji-cb s)
                       :width 24
                       :height 32
                       :position "bottom"
                       :default-field-selector "div.cmail-content div.rich-body-editor"
                       :container-selector "div.cmail-content"})
        [:button.mlb-reset.attachment-button
          {:on-click #(add-attachment s)
           :data-toggle "tooltip"
           :data-placement "top"
           :data-container "body"
           :title "Add attachment"}]
        [:div.cmail-footer-right
          (when (:uuid cmail-data)
            [:div.delete-bt-container
              [:button.mlb-reset.delete-bt
                {:on-click #(delete-clicked s % cmail-data)
                 :data-toggle (when-not is-mobile? "tooltip")
                 :data-placement "top"
                 :title "Delete"}]])]]]]))
