(ns oc.web.components.cmail
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [oops.core :refer (oget ocall)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [org.martinklepsch.derivatives :as drv]
            [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.urls :as oc-urls]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.utils :as utils]
            [oc.web.mixins.ui :as mixins]
            [oc.web.local-settings :as ls]
            [oc.web.utils.activity :as au]
            [oc.web.utils.dom :as dom-utils]
            [oc.web.lib.image-upload :as iu]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.user :as user-actions]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.actions.nav-sidebar :as nav-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.poll :refer (polls-wrapper)]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.rich-body-editor :refer (rich-body-editor)]
            [oc.web.components.ui.sections-picker :refer (sections-picker)]
            [oc.web.components.ui.stream-attachments :refer (stream-attachments)]
            [oc.web.components.ui.post-to-button :refer (post-to-button)]
            [goog.object :as gobj]
            [clojure.contrib.humanize :refer (filesize)]
            [oc.web.lib.emoji-autocomplete :as emoji-autocomplete])
  (:import [goog.async Debouncer]))

(def self-board-name "All")
(def board-tooltip "Select a topic")

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
        (activity-actions/add-attachment (first dis/cmail-data-key) attachment-data)
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
    (utils/clean-body-html (oget body-el "innerHTML"))))

(defn real-close []
  (cmail-actions/cmail-hide))

(defn headline-element [s]
  (rum/ref-node s "headline"))

(defn- fix-headline [headline]
  (utils/trim (string/replace (or headline "") #"\n" "")))

(defn- clean-body [s]
  (when (body-element)
    (dis/dispatch! [:input (conj dis/cmail-data-key :body) (cleaned-body)])))

;; Local cache for outstanding edits

(defn autosave
  ([s] (autosave s false))
  ([s reset-cmail?]
  (let [cmail-data @(drv/get-ref s :cmail-data)]
    (activity-actions/entry-save-on-exit (first dis/cmail-data-key) cmail-data (cleaned-body)
     (when reset-cmail? #(when % (cmail-actions/cmail-reset)))))))

(defn debounced-autosave!
  [s]
  (.fire ^js @(::debounced-autosave s)))

(defn cancel-autosave!
  [s]
  (.stop ^js @(::debounced-autosave s)))

;; Close dismiss handling

(defn cancel-clicked [s]
  (let [cmail-data @(drv/get-ref s :cmail-data)
        clean-fn (fn [dismiss-modal?]
                    (activity-actions/entry-clear-local-cache (:uuid cmail-data) (first dis/cmail-data-key) cmail-data)
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
  (dis/dispatch! [:input (conj dis/cmail-data-key :has-changes) true])
  (debounced-autosave! state)
  (when-let [body-el (body-element)]
    (reset! (::last-body state) (oget body-el "innerHTML"))))

(defn- setup-top-padding [s]
  (when-let [headline (headline-element s)]
    (when (-> s (drv/get-ref :cmail-state) deref :fullscreen)
      (reset! (::top-padding s) (.. headline -parentElement -scrollHeight)))))

(defn- headline-on-change [state]
  (when-let [headline (headline-element state)]
    (let [clean-headline (fix-headline (oget headline "innerText"))
          post-button-title (when-not (seq clean-headline) :title)]
      (dis/dispatch! [:update dis/cmail-data-key #(merge % {:headline clean-headline
                                                            :has-changes true})])
      (reset! (::post-tt-kw state) post-button-title)
      (debounced-autosave! state))
    (setup-top-padding state)))

;; Headline setup and paste handler

(defn- fullscreen-focus-headline [state]
  (let [headline-el  (headline-element state)]
    (when (and (-> state (drv/get-ref :cmail-state) deref :fullscreen)
               headline-el)
      (utils/to-end-of-content-editable headline-el))))

(defn- setup-headline [state headline-el]
  (when headline-el
    (reset! (::headline-input-listener state) (events/listen headline-el EventType/INPUT #(headline-on-change state)))
    (fullscreen-focus-headline state)))

(defn headline-on-paste
  "Avoid to paste rich text into headline, replace it with the plain text clipboard data."
  [state e]
  ; Prevent the normal paste behavior
  (utils/event-stop e)
  (let [clipboardData (or (oget e "clipboardData") (oget js/window "clipboardData"))
        pasted-data   (ocall clipboardData "getData" "text/plain")]
    ; replace the selected text of headline with the text/plain data of the clipboard
    (js/replaceSelectedText pasted-data)
    ; call the headline-on-change to check for content length
    (headline-on-change state)
    (when (= (oget js/document "activeElement") (oget js/document "body"))
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

(declare real-post-action)

(defn- maybe-publish [s retry]
  (let [latest-cmail-data @(drv/get-ref s :cmail-data)]
    (if (and (:auto-saving latest-cmail-data)
             (< retry 10))
      (utils/after 250 #(real-post-action s (inc retry)))
      (do
        (reset! (::publishing s) true)
        (activity-actions/entry-publish (dissoc latest-cmail-data :status) (first dis/cmail-data-key))))))

(defn- real-post-action
  ([s] (real-post-action s 0))
  ([s retry]
   (let [cmail-data @(drv/get-ref s :cmail-data)
         fixed-headline (fix-headline (:headline cmail-data))
         published? (= (:status cmail-data) "published")]
     (if (is-publishable? cmail-data)
       (let [_ (dis/dispatch! [:update dis/cmail-data-key #(merge % {:headline fixed-headline})])
             updated-cmail-data @(drv/get-ref s :cmail-data)]
         (if published?
           (do
             (reset! (::saving s) true)
             (activity-actions/entry-save (first dis/cmail-data-key) updated-cmail-data))
           (maybe-publish s retry)))
        (do
          (reset! (::show-post-tooltip s) true)
          (utils/after 3000 #(reset! (::show-post-tooltip s) false))
          (reset! (::disable-post s) false))))))

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
        (dis/dispatch! [:update dis/cmail-data-key #(dissoc % :has-changes)]))
      (cmail-actions/cmail-hide))))

(defn win-width []
  (or (oget js/document "documentElement.clientWidth")
      (oget js/window "innerWidth")))

(defn calc-video-height [s]
  (when (responsive/is-mobile-size?)
    (reset! (::mobile-video-height s) (utils/calc-video-height (win-width)))))

(defn- collapse-if-needed [s & [e]]
  (let [cmail-data @(drv/get-ref s :cmail-data)
        cmail-state @(drv/get-ref s :cmail-state)
        showing-section-picker? @(::show-section-picker s)
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

(defn close-cmail [s e]
  (let [cmail-data (-> s (drv/get-ref :cmail-data) deref)]
    (if (au/has-content? (assoc cmail-data :body (cleaned-body)))
      (autosave s true)
      (activity-actions/activity-delete cmail-data))
    (if (and (-> cmail-data :status keyword (= :published))
             (:has-changes cmail-data))
      (cancel-clicked s)
      (cmail-actions/cmail-hide))))

(defn- reset-cmail [s]
  (when @(::unlock-scroll s)
    (dom-utils/unlock-page-scroll))
  (when-let [tc @(::headline-autocomplete s)]
    (emoji-autocomplete/destroy tc))
  (let [cmail-data @(drv/get-ref s :cmail-data)
        cmail-state @(drv/get-ref s :cmail-state)
        initial-body (if (seq (:body cmail-data))
                       (:body cmail-data)
                       au/empty-body-html)
        initial-headline (utils/emojify
                           (if (seq (:headline cmail-data))
                             (:headline cmail-data)
                             ""))
        body-text (.text (.html (js/$ "<div/>") initial-body))
        scroll-lock? (or (responsive/is-mobile-size?)
                         (:fullscreen cmail-state))]
    (when-not (seq (:uuid cmail-data))
      (nux-actions/dismiss-add-post-tooltip))
    (reset! (::last-body s) initial-body)
    (reset! (::initial-body s) initial-body)
    (reset! (::initial-headline s) initial-headline)
    (reset! (::initial-uuid s) (:uuid cmail-data))
    (reset! (::publishing s) (:publishing cmail-data))
    (reset! (::show-placeholder s) (not (.match initial-body #"(?i).*(<iframe\s?.*>).*")))
    (reset! (::post-tt-kw s) (when-not (seq (:headline cmail-data)) :title))
    (reset! (::latest-key s) (:key cmail-state))
    (utils/after 300 (fn []
                      (when-let [headline-el (headline-element s)]
                        (setup-headline s headline-el)
                        (reset! (::headline-autocomplete s) (emoji-autocomplete/autocomplete headline-el)))))
    (reset! (::unlock-scroll s) scroll-lock?)
    (when scroll-lock?
      (dom-utils/lock-page-scroll))))

(defn- hide-section-picker! [s]
  (reset! (::show-section-picker s) false))

(defn- clear-delayed-show-section-picker [s]
  (let [delayed-show-section-picker (::delayed-show-section-picker s)]
    (when @delayed-show-section-picker
      (.clearTimeout js/window @delayed-show-section-picker)
      (reset! delayed-show-section-picker nil))))

(defn- maybe-hide-section-picker [s]
  (clear-delayed-show-section-picker s)
  (when (= @(::show-section-picker s) :hover)
    (hide-section-picker! s)))

(defn- show-section-picker! [s v]
  (reset! (::show-section-picker s) v)
  (reset! (::delayed-show-section-picker s) nil))

(defn- maybe-show-section-picker [s]
  (when-not @(::show-section-picker s)
    (clear-delayed-show-section-picker s)
    (reset! (::delayed-show-section-picker s) (utils/after 720 #(show-section-picker! s :hover)))))

(defn- toggle-section-picker [s]
  (if (= @(::show-section-picker s) :click)
    (hide-section-picker! s)
    (show-section-picker! s :click)))

(rum/defcs cmail < rum/reactive
                   ;; Derivatives
                   (drv/drv :cmail-state)
                   (drv/drv :cmail-data)
                   (drv/drv :show-edit-tooltip)
                   (drv/drv :current-user-data)
                   (drv/drv :follow-boards-list)
                   (drv/drv :editable-boards)
                   (drv/drv :board-slug)
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
                   (rum/local false ::show-section-picker)
                   (rum/local nil ::delayed-show-section-picker)
                   (rum/local nil ::last-body)
                   (rum/local nil ::post-tt-kw)
                   (rum/local 68 ::top-padding)
                   (rum/local false ::last-fullscreen-state)
                   (rum/local false ::unlock-scroll)
                   (rum/local nil ::headline-autocomplete)
                   ;; Mixins
                   (mixins/render-on-resize calc-video-height)
                   mixins/refresh-tooltips-mixin
                   ;; Go back to collapsed state on desktop if user didn't touch anything
                   (when-not (responsive/is-mobile-size?)
                     (mixins/on-window-click-mixin collapse-if-needed))
                   ;; Dismiss sectoins picker on window clicks, slightly delay it to avoid
                   ;; conflicts with the collapse cmail listener
                   (mixins/on-click-out :section-picker-container (fn [s e] (hide-section-picker! s)))

                   (mixins/on-click-out :cmail-container #(when (and (not (responsive/is-mobile-size?))
                                                                     (-> %1 (drv/get-ref :cmail-state) deref :fullscreen)
                                                                     (not (dom-utils/event-cotainer-has-class %2 "modal-wrapper")))
                                                            (close-cmail %1 %2)))

                   {:will-mount (fn [s]
                    (reset-cmail s)
                    (reset! (::last-fullscreen-state s) (-> s (drv/get-ref :cmail-state) deref :fullscreen))
                    s)
                   :did-mount (fn [s]
                    (calc-video-height s)
                    (reset! (::debounced-autosave s) (Debouncer. #(autosave s) 2000))
                    (setup-top-padding s)
                    s)
                   :will-update (fn [s]
                    (let [cmail-state @(drv/get-ref s :cmail-state)]
                      ;; If the state key changed let's reset the initial values
                      (when (not= @(::latest-key s) (:key cmail-state))
                        (when @(::latest-key s)
                          (reset-cmail s))
                        (when-not @(::latest-key s)
                          (reset! (::latest-key s) (:key cmail-state)))))
                    s)
                   :did-update (fn [s]
                    (when-let [cmail-state @(drv/get-ref s :cmail-state)]
                      (when-not (= (:fullscreen cmail-state) @(::last-fullscreen-state s))
                        (when (:fullscreen cmail-state)
                          (fullscreen-focus-headline s))
                        (reset! (::last-fullscreen-state s) (:fullscreen cmail-state))))
                    s)
                   :before-render (fn [s]
                    ;; Handle saving/publishing states to dismiss the component
                    (when-let [cmail-data @(drv/get-ref s :cmail-data)]
                      ;; Did activity get removed here or in another client?
                      (when (:delete cmail-data)
                        (real-close))
                      ;; Saving
                      (when (and @(::saving s)
                                 (not (:loading cmail-data)))
                        (reset! (::saving s) false)
                        (reset! (::disable-post s) false)
                        (when-not (:error cmail-data)
                          (utils/after 100 real-close)))
                      ;; Publish
                      (when (and @(::publishing s)
                                 (not (:publishing cmail-data)))
                        (reset! (::publishing s) false)
                        (reset! (::disable-post s) false)
                        (when-not (:error cmail-data)
                          (when (seq (:board-slug cmail-data))
                            ;; Redirect to the publishing board if the slug is available
                            (real-close)
                            (utils/after 180 (fn []
                             (let [follow-boards-list @(drv/get-ref s :follow-boards-list)
                                   following-board? (some #(when (= (:slug %) (:board-slug cmail-data)) %) follow-boards-list)
                                   current-board-slug @(drv/get-ref s :board-slug)
                                   posting-to-current-board? (= (keyword current-board-slug) (keyword (:board-slug cmail-data)))
                                   to-url (if-not posting-to-current-board?
                                            ;; If user is following the board they posted to
                                            ;; and they are in home
                                            {:slug "following"
                                             :url (oc-urls/following)
                                             :refresh false}
                                            ;; Redirect to the posting board in every other case
                                            {:slug (:board-slug cmail-data)
                                             :url (oc-urls/board (:board-slug cmail-data))
                                             :refresh true})]
                               (when-not following-board?
                                 (user-actions/toggle-board (:board-uuid cmail-data)))
                               (nav-actions/nav-to-url! nil (:slug to-url) (:url to-url) 0 (:refresh to-url)))))))))
                    s)
                   :after-render (fn [s]
                    (fix-tooltips s)
                    s)
                   :will-unmount (fn [s]
                    (nux-actions/dismiss-edit-tooltip)
                    (when @(::headline-input-listener s)
                      (events/unlistenByKey @(::headline-input-listener s))
                      (reset! (::headline-input-listener s) nil))
                    (when @(::unlock-scroll s)
                      (dom-utils/unlock-page-scroll))
                    (when-let [debounced-autosave @(::debounced-autosave s)]
                      (.dispose ^js debounced-autosave))
                    s)}
  [s]
  (let [is-mobile? (responsive/is-mobile-size?)
        _current-board-slug (drv/react s :board-slug)
        cmail-state (drv/react s :cmail-state)
        cmail-data* (drv/react s :cmail-data)
        cmail-data (update cmail-data* :board-name
                    #(if (:publisher-board cmail-data*)
                       self-board-name
                       %))
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
                      (not publishable?)
                      @(::publishing s)
                      @(::disable-post s))
        working? (or (and published?
                          @(::saving s))
                     (and (not published?)
                          @(::publishing s)))
        post-button-title (if (= (:status cmail-data) "published")
                            "Save"
                            "Share update")
        did-pick-section (fn [board-data note dismiss-action]
                           (hide-section-picker! s)
                           (when (and board-data
                                      (seq (:name board-data)))
                            (let [has-changes (or (:has-changes cmail-data)
                                                  (seq (:uuid cmail-data))
                                                  (:auto-saving cmail-data))]
                              (dis/dispatch! [:input dis/cmail-data-key
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
        show-section-picker? (or ;; Publisher board can still be created
                                 (and (not (some :publisher-board editable-boards))
                                      ls/publisher-board-enabled?
                                      (pos? (count editable-boards)))
                                 (> (count editable-boards) 1))
        expanded-state? (and (contains? cmail-state :collapsed)
                             (not (:collapsed cmail-state)))]
    [:div.cmail-outer
      {:class (utils/class-set {:quick-post-collapsed (not expanded-state?)
                                :fullscreen (and expanded-state?
                                                 (:fullscreen cmail-state))})
       :on-click (when (and (not is-mobile?)
                            (not expanded-state?)
                            (not (:fullscreen cmail-state)))
                   (fn [e]
                      (nux-actions/dismiss-add-post-tooltip)
                      (cmail-actions/cmail-expand cmail-data cmail-state)
                      (utils/after 280
                       #(when-let [el (headline-element s)]
                          (utils/to-end-of-content-editable el)))))}
      [:div.cmail-container
        {:ref :cmail-container}
        [:div.cmail-mobile-header
          [:div.cmail-mobile-header-left
            [:button.mlb-reset.mobile-attachment-button
              {:on-click #(add-attachment s)}]]
          [:div.cmail-mobile-header-title
            (if (:published? cmail-data)
              "Edit update"
              "New update")]
         [:button.mlb-reset.mobile-close-bt
           {:on-click (partial close-cmail s)}]]
        [:div.dismiss-inline-cmail-container
          {:class (when-not (:published? cmail-data) "long-tooltip")}
          [:button.mlb-reset.dismiss-inline-cmail
            {:on-click (partial close-cmail s)
             :data-toggle (when-not is-mobile? "tooltip")
             :data-placement "top"
             :title (if-not (:published? cmail-data)
                      "Save & Close"
                      "Close")}]]
        [:div.cmail-content-outer
          {:class (utils/class-set {:showing-edit-tooltip show-edit-tooltip})
           :style (when (and (not is-mobile?)
                             (:fullscreen cmail-state))
                    {:padding-top (str @(::top-padding s) "px")})}
          [:div.cmail-content
            {:class (utils/class-set {:has-section-button show-section-picker?
                                      :sections-dropdown-shown @(::show-section-picker s)})}
            (when is-mobile?
              [:div.section-picker-bt-container
                {:ref :section-picker-container}
                [:span.post-to "Post to"]
                [:button.mlb-reset.section-picker-bt
                  {:on-click #(toggle-section-picker s)}
                  [:span.section-picker-bt-copy
                    (:board-name cmail-data)]
                  [:div.dropdown-cog]]
                (when @(::show-section-picker s)
                  [:div.section-picker-container
                    (sections-picker {:active-slug (:board-slug cmail-data)
                                      :on-change did-pick-section
                                      :current-user-data current-user-data})])
                 [:div.post-button-container.group
                   (post-to-button {:on-submit #(post-clicked s)
                                    :disabled disabled?
                                    :title post-button-title
                                    :post-tt-kw post-tt-kw
                                    :force-show-tooltip @(::show-post-tooltip s)})]])
            ; Headline element
            [:div.cmail-content-headline-container.group
              [:div.cmail-content-headline.emojiable
                {:class utils/hide-class
                 :content-editable true
                 :key (str "cmail-headline-" (:key cmail-state))
                 :placeholder au/headline-placeholder
                 :ref "headline"
                 :on-paste    #(headline-on-paste s %)
                 :on-key-down (fn [e]
                                (utils/after 10 #(headline-on-change s))
                                (cond
                                  (and (oget e "metaKey")
                                       (= "Enter" (oget e "key")))
                                  (post-clicked s)
                                  (and (= (oget e "key") "Enter")
                                       (not (oget e "metaKey")))
                                  (do
                                    (utils/event-stop e)
                                    (utils/to-end-of-content-editable (body-element)))))
                 :dangerouslySetInnerHTML @(::initial-headline s)}]]
            (when-not is-mobile?
              [:div.cmail-content-collapsed-placeholder
                (str utils/default-body-placeholder "...")])
            (rich-body-editor {:on-change (partial body-on-change s)
                               :use-inline-media-picker true
                               :static-positioned-media-picker true
                               :media-picker-initially-visible false
                               :media-picker-container-selector "div.cmail-outer div.cmail-container div.cmail-footer div.cmail-footer-media-picker-container"
                               :initial-body @(::initial-body s)
                               :show-placeholder @(::show-placeholder s)
                               :show-h2 true
                               :placeholder (str utils/default-body-placeholder "...")
                               :dispatch-input-key (first dis/cmail-data-key)
                               :cmd-enter-cb #(post-clicked s)
                               :upload-progress-cb (fn [is-uploading?]
                                                     (reset! (::uploading-media s) is-uploading?))
                               :media-config ["poll" "code" "gif" "photo" "video"]
                               :classes (str "emojiable " utils/hide-class)
                               :cmail-key (:key cmail-state)
                               :attachments-enabled true})
            ; Attachments
            (stream-attachments (:attachments cmail-data) nil
             #(activity-actions/remove-attachment (first dis/cmail-data-key) %))
            (when (seq (:polls cmail-data))
              (polls-wrapper {:polls-data (:polls cmail-data)
                              :editing? true
                              :current-user-id (jwt/user-id)
                              :container-selector "div.cmail-content"
                              :dispatch-key (first dis/cmail-data-key)
                              :remove-poll-cb #(body-on-change s)
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
          {:class (when-not show-section-picker? "hidden")
           :on-mouse-leave #(maybe-hide-section-picker s)
           :on-mouse-enter #(maybe-show-section-picker s)
           :ref :section-picker-container}
          [:button.mlb-reset.section-picker-bt
            {:on-click #(toggle-section-picker s)
             :data-placement "top"
             :data-toggle "tooltip"
             :title board-tooltip}
            [:span.section-picker-bt-copy
             (:board-name cmail-data)]
            [:div.dropdown-cog]]
          (when @(::show-section-picker s)
            [:div.section-picker-container
              (sections-picker {:active-slug (:board-slug cmail-data)
                                :on-change did-pick-section
                                :current-user-data current-user-data
                                :direction (if (:fullscreen cmail-state) :up :down)})])]
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
        [:div.cmail-footer-media-picker-container.group]
        (when (:uuid cmail-data)
          [:div.delete-bt-container
           [:button.mlb-reset.delete-bt
            {:on-click #(delete-clicked s % cmail-data)
             :data-toggle (when-not is-mobile? "tooltip")
             :data-placement "top"
             :title "Delete"}]])
        ; (when-not (:fullscreen cmail-state)
        ;   [:div.fullscreen-bt-container
        ;     [:button.mlb-reset.fullscreen-bt
        ;       {:on-click #(cmail-actions/cmail-toggle-fullscreen)
        ;        :data-toggle (when-not is-mobile? "tooltip")
        ;        :data-placement "top"
        ;        :title "Fullscreen"}]])
        ]]]))
