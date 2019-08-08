(ns oc.web.components.cmail
  (:require [rum.core :as rum]
            [cuerdas.core :as s]
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
            [oc.web.utils.activity :as au]
            [oc.web.utils.ui :as ui-utils]
            [oc.web.local-settings :as ls]
            [oc.web.lib.image-upload :as iu]
            [oc.web.actions.nux :as nux-actions]
            [oc.web.lib.responsive :as responsive]
            [oc.web.actions.cmail :as cmail-actions]
            [oc.web.actions.routing :as routing-actions]
            [oc.web.actions.activity :as activity-actions]
            [oc.web.components.ui.alert-modal :as alert-modal]
            [oc.web.components.ui.emoji-picker :refer (emoji-picker)]
            [oc.web.components.carrot-abstract :refer (carrot-abstract)]
            [oc.web.components.rich-body-editor :refer (rich-body-editor)]
            [oc.web.components.ui.sections-picker :refer (sections-picker)]
            [oc.web.components.ui.ziggeo :refer (ziggeo-player ziggeo-recorder)]
            [oc.web.components.ui.stream-attachments :refer (stream-attachments)]
            [goog.dom :as gdom]
            [goog.Uri :as guri]
            [goog.object :as gobj]
            [clojure.contrib.humanize :refer (filesize)]))

(def missing-title-tooltip "Please add a title")
(def abstract-max-length-exceeded-tooltip "Abstract too long")

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

;; Local cache for outstanding edits

(defn remove-autosave [s]
  (when @(::autosave-timer s)
    (.clearInterval js/window @(::autosave-timer s))
    (reset! (::autosave-timer s) nil)))

(defn autosave [s]
  (let [cmail-data @(drv/get-ref s :cmail-data)
        section-editing @(drv/get-ref s :section-editing)]
    (activity-actions/entry-save-on-exit :cmail-data cmail-data (cleaned-body) section-editing)))

;; Close dismiss handling

(defn cancel-clicked [s]
  (let [cmail-data @(drv/get-ref s :cmail-data)
        clean-fn (fn [dismiss-modal?]
                    (remove-autosave s)
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
  (dis/dispatch! [:input [:cmail-data :has-changes] true]))

(defn- check-limits [s]
  (let [headline (rum/ref-node s "headline")
        $abstract (js/$ "div.cmail-content-abstract" (rum/dom-node s))
        abstract-text (s/trim (.text $abstract))
        exceeds-limit (> (count abstract-text) utils/max-abstract-length)
        clean-headline (s/trim (s/replace (.-innerText headline) #"\n" ""))
        post-button-title (cond
                           (not (seq clean-headline)) :title
                           exceeds-limit :abstract
                           :else nil)]
    (reset! (::abstract-exceeds-limit s) exceeds-limit)
    (reset! (::abstract-length s) (count abstract-text))
    (reset! (::post-button-title s) post-button-title)))

(defn- headline-on-change [state]
  (when-let [headline (rum/ref-node state "headline")]
    (let [emojied-headline (.-innerText headline)]
      (dis/dispatch! [:update [:cmail-data] #(merge % {:headline emojied-headline
                                                       :has-changes true})])
      (check-limits state))))

(defn- abstract-on-change [state]
  (let [$abstract (js/$ "div.cmail-content-abstract" (rum/dom-node state))]
    (dis/dispatch! [:update [:cmail-data] #(merge % {:abstract (.html $abstract)
                                                     :has-changes true})])
    (check-limits state)))

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
    (when (= (.-activeElement js/document) (.-body js/document))
      (when-let [headline-el (rum/ref-node state "headline")]
        ; move cursor at the end
        (utils/to-end-of-content-editable headline-el)))))

(defn add-emoji-cb [s]
  (headline-on-change s)
  (abstract-on-change s)
  (body-on-change s))

(defn- clean-body [s]
  (when-let [body-el (sel1 [:div.rich-body-editor])]
    (dis/dispatch! [:input [:cmail-data :body] (utils/clean-body-html (.-innerHTML body-el))])))

(defn- fix-headline [cmail-data]
  (utils/trim (:headline cmail-data)))

(defn- fix-abstract [cmail-data]
  (utils/trim (:abstract cmail-data)))

(defn- is-publishable? [s cmail-data]
  (and (seq (:board-slug cmail-data))
       (not (zero? (count (fix-headline cmail-data))))
       (not @(::abstract-exceeds-limit s))))

(defn real-post-action [s]
  (let [cmail-data @(drv/get-ref s :cmail-data)
        fixed-headline (fix-headline cmail-data)
        fixed-abstract (fix-abstract cmail-data)
        published? (= (:status cmail-data) "published")]
      (if (is-publishable? s cmail-data)
        (let [_ (dis/dispatch! [:update [:cmail-data] #(merge % {:headline fixed-headline :abstract fixed-abstract})])
              updated-cmail-data @(drv/get-ref s :cmail-data)
              section-editing @(drv/get-ref s :section-editing)]
          (remove-autosave s)
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
  (let [cmail-data @(drv/get-ref s :cmail-data)]
    (if (:auto-saving cmail-data)
      (reset! (::publish-after-autosave s) true)
      (real-post-action s))))

(defn fix-tooltips
  "Fix the tooltips"
  [s]
  (doto (.find (js/$ (rum/dom-node s)) "[data-toggle=\"tooltip\"]")
    (.tooltip "hide")
    (.tooltip "fixTitle")))

;; Delete handling

(defn delete-clicked [s e activity-data]
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
                                       (reset! (::deleting s) true)
                                       (activity-actions/activity-delete activity-data)
                                       (alert-modal/hide-alert)
                                       (real-close))
                    }]
    (alert-modal/show-alert alert-data)))

(defn win-width []
  (or (.-clientWidth (.-documentElement js/document))
      (.-innerWidth js/window)))

(defn calc-video-height [s]
  (when (responsive/is-tablet-or-mobile?)
    (reset! (::mobile-video-height s) (utils/calc-video-height (win-width)))))

(rum/defcs cmail < rum/reactive
                   ;; Derivatives
                   (drv/drv :cmail-state)
                   (drv/drv :cmail-data)
                   (drv/drv :show-sections-picker)
                   (drv/drv :section-editing)
                   (drv/drv :show-edit-tooltip)
                   ;; Locals
                   (rum/local "" ::initial-body)
                   (rum/local "" ::initial-headline)
                   (rum/local "" ::initial-abstract)
                   (rum/local true ::show-placeholder)
                   (rum/local nil ::initial-uuid)
                   (rum/local nil ::headline-input-listener)
                   (rum/local nil ::abstract-input-listener)
                   (rum/local nil ::uploading-media)
                   (rum/local false ::saving)
                   (rum/local false ::publishing)
                   (rum/local false ::disable-post)
                   (rum/local false ::publish-after-autosave)
                   (rum/local nil ::autosave-timer)
                   (rum/local 0 ::mobile-video-height)
                   (rum/local false ::deleting)
                   (rum/local false ::media-attachment-did-success)
                   (rum/local nil ::media-attachment)
                   (rum/local nil ::latest-key)
                   (rum/local "" ::post-button-title)
                   (rum/local false ::abstract-exceeds-limit)
                   (rum/local 0 ::abstract-length)
                   (rum/local false ::show-post-tooltip)
                   ;; Mixins
                   (mixins/render-on-resize calc-video-height)
                   mixins/refresh-tooltips-mixin

                   {:will-mount (fn [s]
                    (let [cmail-data @(drv/get-ref s :cmail-data)
                          cmail-state @(drv/get-ref s :cmail-state)
                          initial-body (if (seq (:body cmail-data))
                                         (:body cmail-data)
                                         "")
                          initial-headline (utils/emojify
                                             (if (seq (:headline cmail-data))
                                               (:headline cmail-data)
                                               ""))
                          initial-abstract (if (seq (:abstract cmail-data))
                                             (:abstract cmail-data)
                                             "")
                          abstract-text (.text (js/$ (str "<div>" initial-abstract "</div>")))
                          abstract-exceeds (> (count abstract-text) utils/max-abstract-length)]
                      (when (and (not (seq (:uuid cmail-data)))
                                 (not (:collapsed cmail-state)))
                        (nux-actions/dismiss-add-post-tooltip))
                      (reset! (::initial-body s) initial-body)
                      (reset! (::initial-headline s) initial-headline)
                      (reset! (::initial-abstract s) initial-abstract)
                      (reset! (::initial-uuid s) (:uuid cmail-data))
                      (reset! (::abstract-length s) (count abstract-text))
                      (reset! (::abstract-exceeds-limit s) abstract-exceeds)
                      (reset! (::post-button-title s)
                        (cond
                          abstract-exceeds :abstract
                          (not (seq (:headline cmail-data))) :title
                          :else nil))
                      (reset! (::show-placeholder s) (not (.match initial-body #"(?i).*(<iframe\s?.*>).*")))
                      (reset! (::latest-key s) (:key cmail-state)))
                    s)
                   :did-mount (fn [s]
                    (calc-video-height s)
                    (utils/after 300 #(setup-headline s))
                    (reset! (::autosave-timer s) (utils/every 5000 #(autosave s)))
                    s)
                   :will-update (fn [s]
                    (let [cmail-state @(drv/get-ref s :cmail-state)]
                      ;; If the state key changed let's reset the initial values
                      (when (not= @(::latest-key s) (:key cmail-state))
                        (when @(::latest-key s)
                          (let [cmail-data @(drv/get-ref s :cmail-data)
                                initial-body (if (seq (:body cmail-data))
                                               (:body cmail-data)
                                               "")
                                initial-headline (utils/emojify
                                                   (if (seq (:headline cmail-data))
                                                     (:headline cmail-data)
                                                     ""))
                                initial-abstract (if (seq (:abstract cmail-data))
                                                   (:abstract cmail-data)
                                                   "")
                                abstract-text (.text (js/$ "<div>" initial-abstract "</div>"))
                                abstract-exceeds (> (count abstract-text) utils/max-abstract-length)]
                            (when (and (not (seq (:uuid cmail-data)))
                                       (not (:collapsed cmail-state)))
                              (nux-actions/dismiss-add-post-tooltip))
                            (reset! (::initial-body s) initial-body)
                            (reset! (::initial-headline s) initial-headline)
                            (reset! (::initial-abstract s) initial-abstract)
                            (reset! (::initial-uuid s) (:uuid cmail-data))
                            (reset! (::abstract-length s) (count abstract-text))
                            (reset! (::abstract-exceeds-limit s) abstract-exceeds)
                            (reset! (::post-button-title s)
                             (cond
                              abstract-exceeds :abstract
                              (not (seq (:headline cmail-data))) :title
                              :else nil))
                            (reset! (::show-placeholder s) (not (.match initial-body #"(?i).*(<iframe\s?.*>).*")))))
                        (reset! (::latest-key s) (:key cmail-state))))
                    s)
                   :before-render (fn [s]
                    ;; Handle saving/publishing states to dismiss the component
                    (let [cmail-data @(drv/get-ref s :cmail-data)]
                      ;; Did activity get removed in another client?
                      (when (and @(::deleting s)
                                 (:delete cmail-data))
                        (reset! (::deleting s) false)
                        (real-close))
                      ;; Entry is saving
                      ;: and save request finished
                      (when (and @(::publish-after-autosave s)
                                 (not (:auto-saving cmail-data)))
                        (reset! (::publish-after-autosave s) false)
                        (real-post-action s))
                      (when (and @(::saving s)
                                 (not (:loading cmail-data)))
                        (reset! (::saving s) false)
                        (reset! (::disable-post s) false)
                        (when-not (:error cmail-data)
                          (real-close)))
                      (when (and @(::publishing s)
                                 (not (:publishing cmail-data)))
                        (reset! (::publishing s) false)
                        (reset! (::disable-post s) false)
                        (when-not (:error cmail-data)
                          (when-let [redirect? (seq (:board-slug cmail-data))]
                            ;; Redirect to the publishing board if the slug is available
                            (real-close)
                            (utils/after
                             180
                             #(router/nav! (if (= (router/current-board-slug) "all-posts")
                                             (oc-urls/all-posts)
                                             (oc-urls/board (:board-slug cmail-data)))))))))
                    s)
                   :after-render (fn [s]
                    (fix-tooltips s)
                    s)
                   :will-unmount (fn [s]
                    (nux-actions/dismiss-edit-tooltip)
                    (when @(::headline-input-listener s)
                      (events/unlistenByKey @(::headline-input-listener s))
                      (reset! (::headline-input-listener s) nil))
                    (when @(::abstract-input-listener s)
                      (events/unlistenByKey @(::abstract-input-listener s))
                      (reset! (::abstract-input-listener s) nil))
                    (remove-autosave s)
                    s)}
  [s]
  (let [is-mobile? (responsive/is-tablet-or-mobile?)
        cmail-state (drv/react s :cmail-state)
        cmail-data (drv/react s :cmail-data)
        show-sections-picker (drv/react s :show-sections-picker)
        published? (= (:status cmail-data) "published")
        video-size (if is-mobile?
                     {:width (win-width)
                      :height @(::mobile-video-height s)}
                     {:width 548
                      :height (utils/calc-video-height 548)})
        show-edit-tooltip (and (drv/react s :show-edit-tooltip)
                               (not (seq @(::initial-uuid s))))
        show-post-bt-tooltip? (not (is-publishable? s cmail-data))
        post-button-title @(::post-button-title s)
        disabled? (or show-post-bt-tooltip?
                      @(::publishing s)
                      @(::disable-post s))
        working? (or (and published?
                          @(::saving s))
                     (and (not published?)
                          @(::publishing s)))
        is-fullscreen? (and (not is-mobile?)
                            (:fullscreen cmail-state))
        close-cb (fn [_]
                  (if (au/has-content? (assoc cmail-data
                                         :body
                                         (cleaned-body)))
                    (autosave s)
                    (do
                      (reset! (::deleting s) true)
                      (activity-actions/activity-delete cmail-data)))
                  (if (and (= (:status cmail-data) "published")
                           (:has-changes cmail-data))
                    (cancel-clicked s)
                    (cmail-actions/cmail-hide)))
        long-tooltip (not= (:status cmail-data) "published")]
    [:div.cmail-outer
      {:class (utils/class-set {:fullscreen is-fullscreen?
                                :quick-post-collapsed (:collapsed cmail-state)})
       :on-click #(when (:collapsed cmail-state)
                    (nux-actions/dismiss-add-post-tooltip)
                    (cmail-actions/cmail-show (cmail-actions/get-board-for-edit) {:collapsed false
                                                                                  :fullscreen false
                                                                                  :key (:key cmail-state)}))}
      [:div.cmail-container
        [:div.cmail-mobile-header
          [:button.mlb-reset.mobile-close-bt
            {:on-click close-cb}]
          [:div.cmail-mobile-header-title
            (if (= (:status cmail-data) "published")
              "Edit"
              "New")
            (when (not= (:status cmail-data) "published")
              (if (or (:has-changes cmail-data)
                      (:auto-saving cmail-data))
                [:span.saving-saved " (saving)"]
                (when (false? (:auto-saving cmail-data))
                  [:span.saving-saved " (saved)"])))]
          [:button.mlb-reset.mobile-post-button
            {:on-click #(post-clicked s)
             :class (utils/class-set {:disabled disabled?
                                      :force-show-tooltip @(::show-post-tooltip s)
                                      :loading working?
                                      (str "tt-" (when post-button-title (name post-button-title))) true})}
            (when post-button-title
              [:div.post-bt-tooltip
                (cond
                  (= post-button-title :title)
                  missing-title-tooltip
                  (= post-button-title :abstract)
                  abstract-max-length-exceeded-tooltip)])
            (if (= (:status cmail-data) "published")
              "Save"
              "Post")]
          [:div.cmail-mobile-header-bt-separator]
          [:button.mlb-reset.mobile-attachment-button
            {:on-click #(add-attachment s)}]]
        [:div.cmail-header.group
          [:div.close-bt-container
            {:class (when long-tooltip "long-tooltip")}
            [:button.mlb-reset.close-bt
              {:on-click close-cb
               :data-toggle (if is-mobile? "" "tooltip")
               :data-placement "auto"
               :title (if long-tooltip
                        "Save & Close"
                        "Close")}]]
          [:div.cmail-header-vertical-separator]
          [:div.cmail-header-board-must-see-container.group
            {:class (when (:must-see cmail-data) "must-see-on")}
            [:div.board-name.oc-input
              {:on-click #(when-not (utils/event-inside? % (rum/ref-node s :picker-container))
                            (dis/dispatch! [:input [:show-sections-picker] (not show-sections-picker)]))
               :class (when show-sections-picker "active")}
              [:div.board-name-inner
                (:board-name cmail-data)]]
            (when show-sections-picker
              [:div.sections-picker-container
                {:ref :picker-container}
                (sections-picker (:board-slug cmail-data)
                 (fn [board-data note dismiss-action]
                   (dis/dispatch! [:input [:show-sections-picker] false])
                   (when (and board-data
                              (seq (:name board-data)))
                    (dis/dispatch! [:input [:cmail-data]
                     (merge cmail-data {:board-slug (:slug board-data)
                                        :board-name (:name board-data)
                                        :has-changes (or (:has-changes cmail-data)
                                                         (seq (:uuid cmail-data))
                                                         (:auto-saving cmail-data))
                                        :invite-note note})])
                    (when (fn? dismiss-action)
                      (dismiss-action)))))])
            [:div.must-see-toggle-container
              {:class (when (:must-see cmail-data) "on")}
              [:div.must-see-toggle
                {:on-mouse-down #(cmail-actions/cmail-toggle-must-see)
                 :data-toggle "tooltip"
                 :data-placement "auto"
                 :data-delay "{\"show\":\"500\", \"hide\":\"0\"}"
                 :title "Must See"}
                [:span.must-see-toggle-circle]]]
            (when (:must-see cmail-data)
              [:div.must-see-tag
                {:class (when-not is-fullscreen? "white-bg")}])]
          (when is-fullscreen?
            [:div.cmail-header-right-buttons
              (emoji-picker {:add-emoji-cb (partial add-emoji-cb s)
                             :width 24
                             :height 24
                             :position "bottom"
                             :tooltip-position "bottom"
                             :default-field-selector "div.cmail-content div.rich-body-editor"
                             :container-selector "div.cmail-content"})
              [:button.mlb-reset.attachment-button
                {:on-click #(add-attachment s)
                 :data-toggle "tooltip"
                 :data-placement "auto"
                 :data-container "body"
                 :title "Add attachment"}]
              [:div.delete-button-container
                [:button.mlb-reset.delete-button
                  {:title (if (= (:status cmail-data) "published") "Delete post" "Delete draft")
                   :data-toggle "tooltip"
                   :data-placement "bottom"
                   :data-container "body"
                   :on-click #(delete-clicked s % cmail-data)}]]])
          (when is-fullscreen?
            [:button.mlb-reset.post-button
              {:on-click #(post-clicked s)
               :class (utils/class-set {:disabled disabled?
                                        :force-show-tooltip @(::show-post-tooltip s)
                                        :loading working?
                                        (str "tt-" (when post-button-title (name post-button-title))) true})}
              (when post-button-title
                [:div.post-bt-tooltip
                  (cond
                    (= post-button-title :title)
                    missing-title-tooltip
                    (= post-button-title :abstract)
                    abstract-max-length-exceeded-tooltip)])
              (if (= (:status cmail-data) "published")
                "Save"
                "Post")])]
        (when (and (not (:collapsed cmail-state))
                   (not is-fullscreen?))
          [:div.dismiss-inline-cmail-container
            {:class (when long-tooltip "long-tooltip")}
            [:button.mlb-reset.dismiss-inline-cmail
              {:on-click close-cb
               :data-toggle (if is-mobile? "" "tooltip")
               :data-placement "auto"
               :title (if long-tooltip
                        "Save & Close"
                        "Close")}]])
        [:div.cmail-content-outer
          {:class (utils/class-set {:showing-edit-tooltip show-edit-tooltip})}
          [:div.cmail-content
            ;; Video elements
            ; FIXME: disable video on mobile for now
            (when-not is-mobile?
              (when (:fixed-video-id cmail-data)
                (ziggeo-player {:video-id (:fixed-video-id cmail-data)
                                :remove-video-cb #(activity-actions/prompt-remove-video :cmail-data cmail-data)
                                :width (:width video-size)
                                :height (:height video-size)
                                :video-processed (:video-processed cmail-data)})))
            ; Headline element
            [:div.cmail-content-headline.emoji-autocomplete.emojiable.group
              {:class utils/hide-class
               :content-editable true
               :key (str "cmail-headline-" (:key cmail-state))
               :ref "headline"
               :placeholder utils/default-headline
               :on-paste    #(headline-on-paste s %)
               :on-click    #(headline-on-change s)
               :on-focus #(headline-on-change s)
               :on-blur #(headline-on-change s)
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
               :dangerouslySetInnerHTML @(::initial-headline s)}]
            ;; Abstract
            (when (or is-mobile?
                      is-fullscreen?)
              (carrot-abstract {:initial-value @(::initial-abstract s)
                                :value (:abstract cmail-data)
                                :exceeds-limit @(::abstract-exceeds-limit s)
                                :abstract-length @(::abstract-length s)
                                :on-change-cb #(abstract-on-change s)
                                :post-clicked #(post-clicked s)
                                :cmail-key (:key cmail-state)}))
            (when (and show-edit-tooltip
                       is-fullscreen?)
              [:div.edit-tooltip-outer-container
                [:div.edit-tooltip-container.group
                  [:div.edit-tooltip-title
                    "Quick summary"]
                  [:div.edit-tooltip
                    (str
                     "Help everyone know what your post is about. "
                     "This is what your team sees first.")]
                  [:button.mlb-reset.edit-tooltip-bt
                    {:on-click #(nux-actions/dismiss-edit-tooltip)}
                    "OK, got it"]]])
            (rich-body-editor {:on-change (partial body-on-change s)
                               :use-inline-media-picker true
                               :initial-body @(::initial-body s)
                               :show-placeholder @(::show-placeholder s)
                               :show-h2 true
                               :fullscreen is-fullscreen?
                               :dispatch-input-key :cmail-data
                               :cmd-enter-cb #(post-clicked s)
                               :upload-progress-cb (fn [is-uploading?]
                                                     (reset! (::uploading-media s) is-uploading?))
                               :media-config ["gif" "photo" "video"]
                               :classes (str "emoji-autocomplete emojiable " utils/hide-class)
                               :cmail-key (:key cmail-state)})
            ; Attachments
            (stream-attachments (:attachments cmail-data) nil
             #(activity-actions/remove-attachment :cmail-data %))]]
      (if is-fullscreen?
        [:div.cmail-footer
          (when (and (not= (:status cmail-data) "published")
                     (not is-mobile?))
            (if (or (:has-changes cmail-data)
                    (:auto-saving cmail-data))
              [:div.saving-saved "Saving..."]
              (when (false? (:auto-saving cmail-data))
                [:div.saving-saved "Saved"])))]
        [:div.cmail-footer
          [:div.fullscreen-bt-container
            [:button.mlb-reset.fullscreen-bt
              {:on-click #(cmail-actions/cmail-toggle-fullscreen)}
              "Full-screen"]]
          [:div.cmail-footer-right
            (when-not is-fullscreen?
              [:button.mlb-reset.post-button
                {:on-click #(post-clicked s)
                 :class (utils/class-set {:disabled disabled?
                                          :force-show-tooltip @(::show-post-tooltip s)
                                          :loading working?
                                          (str "tt-" (when post-button-title (name post-button-title))) true})}
                (when post-button-title
                  [:div.post-bt-tooltip
                    (cond
                      (= post-button-title :title)
                      missing-title-tooltip
                      (= post-button-title :abstract)
                      abstract-max-length-exceeded-tooltip)])
                (if (= (:status cmail-data) "published")
                  "Save"
                  "Post")])
            [:div.board-name.oc-input
              {:on-click #(when-not (utils/event-inside? % (rum/ref-node s :picker-container-footer))
                            (dis/dispatch! [:input [:show-sections-picker] (not show-sections-picker)]))
               :class (when show-sections-picker "active")}
              [:div.board-name-inner
                (:board-name cmail-data)]]
            (when show-sections-picker
              [:div.sections-picker-container
                {:ref :picker-container-footer}
                (sections-picker (:board-slug cmail-data)
                 (fn [board-data note dismiss-action]
                   (dis/dispatch! [:input [:show-sections-picker] false])
                   (when (and board-data
                              (seq (:name board-data)))
                    (dis/dispatch! [:input [:cmail-data]
                     (merge cmail-data {:board-slug (:slug board-data)
                                        :board-name (:name board-data)
                                        :has-changes (or (:has-changes cmail-data)
                                                         (seq (:uuid cmail-data))
                                                         (:auto-saving cmail-data))
                                        :invite-note note})])
                    (when (fn? dismiss-action)
                      (dismiss-action)))))])
            [:div.delete-button-container
              [:button.mlb-reset.delete-button
                {:title (if (= (:status cmail-data) "published") "Delete post" "Delete draft")
                 :data-toggle "tooltip"
                 :data-placement "top"
                 :on-click #(delete-clicked s % cmail-data)}]]
            [:button.mlb-reset.attachment-button
              {:on-click #(add-attachment s)
               :data-toggle "tooltip"
               :data-placement "top"
               :data-container "body"
               :title "Add attachment"}]
            (emoji-picker {:add-emoji-cb (partial add-emoji-cb s)
                           :width 24
                           :height 24
                           :position "bottom"
                           :default-field-selector "div.cmail-content div.rich-body-editor"
                           :container-selector "div.cmail-content"})]
          (when (and (not= (:status cmail-data) "published")
                     (not is-mobile?))
            (if (or (:has-changes cmail-data)
                    (:auto-saving cmail-data))
              [:div.saving-saved "Saving..."]
              (when (false? (:auto-saving cmail-data))
                [:div.saving-saved "Saved"])))])]]))
