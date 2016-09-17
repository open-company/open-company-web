(ns open-company-web.actions
  (:require [medley.core :as med]
            [clojure.string :as string]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.cookies :as cook]
            [open-company-web.urls :as oc-urls]
            [open-company-web.router :as router]
            [open-company-web.caches :as cache]
            [open-company-web.api :as api]))

;; ---- Generic Actions Dispatch
;; This is a small generic abstraction to handle "actions".
;; An `action` is a transformation on the app state.
;; The return value of an action will be used as the new app-state.

;; The extended multimethod `action` is defined in the dispatcher
;; namespace to avoid cyclical dependencies between namespaces

(defn- log [& args]
  (js/console.log (apply pr-str args)))

(defmethod dispatcher/action :default [db payload]
  (js/console.warn "No handler defined for" (str (first payload)))
  (js/console.log "Full event: " (pr-str payload))
  db)

(defmethod dispatcher/action :logout [db _]
  (cook/remove-cookie! :jwt)
  (router/redirect! "/")
  (dissoc db :jwt))

(defmethod dispatcher/action :entry [db [_ {:keys [links]}]]
  (let [create-link    (med/find-first #(= (:rel %) "company-create") links)
        slug           (fn [co] (last (string/split (:href co) #"/")))
        [first second] (filter #(= (:rel %) "company") links)]
    (let [login-redirect (cook/get-cookie :login-redirect)]
      (cond
        (and create-link (not first)) (router/nav! oc-urls/create-company)
        login-redirect                (do (cook/remove-cookie! :login-redirect)
                                          (router/redirect! login-redirect))
        (and first (not second))      (router/nav! (oc-urls/company (slug first)))
        (and first second)            (router/nav! oc-urls/companies)
        )) db))

(defmethod dispatcher/action :company-submit [db _]
  (api/post-company (:company-editor db))
  db)

(defmethod dispatcher/action :company-created [db [_ body]]
  (if (:links body)
    (let [updated (utils/fix-sections body)]
      (router/redirect! (oc-urls/company (:slug updated)))
      (assoc-in db (dispatcher/company-data-key (:slug updated)) updated))
    db))

(defmethod dispatcher/action :new-section [db [_ body]]
  (when body
    (let [slug (:slug body)
          response (:response body)]
      (swap! cache/new-sections assoc-in [(keyword slug) :new-sections] (:templates response))
      (swap! cache/new-sections assoc-in [(keyword slug) :new-section-order] (:sections response))
      ;; signal to the app-state that the new-sections have been loaded
      (-> db
        (assoc-in [(keyword slug) :new-sections] (:templates response))
        (dissoc :loading)))))

(defmethod dispatcher/action :auth-settings [db [_ body]]
  (when body
    (assoc db :auth-settings body)))

(defmethod dispatcher/action :revision [db [_ body]]
  (if body
    (let [fixed-section (utils/fix-section (:body body) (:section body) true)
          assoc-in-coll [(:slug body) (:section body) (:as-of body)]
          assoc-in-coll-2 (dispatcher/revision-key (:slug body) (:section body) (:as-of body))
          next-db (assoc-in db assoc-in-coll-2 true)]
      (swap! cache/revisions assoc-in assoc-in-coll fixed-section)
      next-db)
    db))

(defmethod dispatcher/action :section [db [_ body]]
  (if body
    (let [fixed-section (utils/fix-section (:body body) (:section body))]
      (assoc-in db (dispatcher/company-section-key (:slug body) (:section body)) fixed-section))
    db))

(defmethod dispatcher/action :company [db [_ {:keys [slug success status body]}]]
  (cond
    success
    ;; add section name inside each section
    (let [updated-body (utils/fix-sections body)
          with-company-data (assoc-in db (dispatcher/company-data-key (:slug updated-body)) updated-body)]
      (if (or (:read-only updated-body)
               (pos? (count (:sections updated-body))))
          (dissoc with-company-data :loading)
          with-company-data))
    (= 403 status)
    (-> db
        (assoc-in [(keyword slug) :error] :forbidden)
        (dissoc :loading))
    (= 404 status)
    (do
      (router/redirect-404!)
      db)
    ;; probably some default failure handling should be added here
    :else db))

(defmethod dispatcher/action :companies [db [_ body]]
  (if body
    (-> db
     (assoc-in dispatcher/companies-key (:companies (:collection body)))
     (dissoc :loading))
    db))

(defmethod dispatcher/action :su-list [db [_ {:keys [slug response]}]]
  (-> db
    (assoc-in (dispatcher/su-list-key slug) response)
    (dissoc :loading)))

(defmethod dispatcher/action :su-edit [db [_ {:keys [slug su-date su-slug]}]]
  (let [protocol (.. js/document -location -protocol)
        host     (.. js/document -location -host)
        su-url   (str protocol "//" host (oc-urls/stakeholder-update slug (utils/su-date-from-created-at su-date) su-slug))]
    (-> db
      (assoc-in (dispatcher/latest-stakeholder-update-key slug) su-url)
      (dissoc :loading))))

(defmethod dispatcher/action :stakeholder-update [db [_ {:keys [slug update-slug response]}]]
  (let [company-data-keys [:logo :logo-width :logo-height :name :slug :currency]
        company-data      (select-keys response company-data-keys)]
    (-> db
      (assoc-in (dispatcher/stakeholder-update-key slug update-slug) response)
      (assoc-in (dispatcher/company-data-key slug) (utils/fix-sections company-data))
      (dissoc :loading))))

;; Front of Card Edit section
(defmethod dispatcher/action :start-foce [db [_ section-key section-data]]
  (if section-key
    (-> db
        (assoc :foce-key section-key) ; which topic is being FoCE
        (assoc :foce-data section-data) ; map of the in progress edits of the topic data
        (assoc :foce-data-editing? false)) ; is the data portion of the topic (e.g. finance, growth) being edited
    (-> db
        (dissoc :foce-key)
        (dissoc :foce-data)
        (dissoc :foce-data-editing?))))

(defmethod dispatcher/action :foce-input [db [_ topic-data-map]]
  (let [old-data (:foce-data db)]
    (assoc db :foce-data (merge old-data topic-data-map))))

;; This should be turned into a proper form library
;; Lomakeets FormState ideas seem like a good start:
;; https://github.com/metosin/lomakkeet/blob/master/src/cljs/lomakkeet/core.cljs

(defmethod dispatcher/action :input [db [_ path value]]
  (assoc-in db path value))

(defmethod dispatcher/action :new-sections [db [_ new-sections]]
  (let [slug (keyword (router/current-company-slug))]
    (api/patch-sections new-sections)
    (assoc-in db (conj (dispatcher/company-data-key slug) :sections) new-sections)))

(defmethod dispatcher/action :topic-archive [db [_ topic]]
  (let [slug (keyword (router/current-company-slug))
        company-data (dispatcher/company-data)
        old-sections (:sections company-data)
        new-sections (utils/vec-dissoc old-sections (name topic))]
    (api/patch-sections new-sections)
    (-> db
      (dissoc :foce-key)
      (dissoc :foce-data)
      (dissoc :foce-data-editing?)
      (assoc-in (conj (dispatcher/company-data-key slug) :sections) new-sections))))

(defmethod dispatcher/action :foce-save [db [_ & [new-sections topic-data]]]
  (let [slug (keyword (router/current-company-slug))
        topic (:foce-key db)
        topic-data (merge (:foce-data db) (if (map? topic-data) topic-data {}))
        is-data-topic (#{:finances :growth} (keyword topic))
        body (:body topic-data)
        with-fixed-headline (assoc topic-data :headline (utils/emoji-images-to-unicode (:headline topic-data)))
        with-fixed-body (assoc with-fixed-headline :body (utils/emoji-images-to-unicode body))
        old-section-data (get (dispatcher/company-data db slug) (keyword topic))
        new-data (dissoc (merge old-section-data with-fixed-body) :placeholder)]
    (api/patch-sections new-sections new-data (:section (:foce-data db)))
    (-> db
        (dissoc :foce-key)
        (dissoc :foce-data)
        (dissoc :foce-data-editing?)
        (assoc-in (conj (dispatcher/company-data-key slug) (keyword topic)) new-data)
        (assoc-in (conj (dispatcher/company-data-key slug) :sections) new-sections))))

(defmethod dispatcher/action :force-fullscreen-edit [db [_ topic]]
  (if topic
    (assoc-in db [:force-edit-topic] topic)
    (dissoc db :force-edit-topic)))

(defn- save-topic [db topic topic-data]
  (let [slug (keyword (router/current-company-slug))
        old-section-data (get (dispatcher/company-data db slug) (keyword topic))
        new-data (dissoc (merge old-section-data topic-data) :placeholder)]
    (api/partial-update-section topic (dissoc topic-data :placeholder))
    (assoc-in db (conj (dispatcher/company-data-key slug) (keyword topic)) new-data)))

(defmethod dispatcher/action :save-topic [db [_ topic topic-data]]
  (save-topic db topic topic-data))

(defmethod dispatcher/action :save-topic-data [db [_ topic topic-data]]
  ;; save topic data for the company
  (save-topic db topic topic-data)
  ;; update topic data for the still in-progress FoCE
  (assoc db :foce-data (merge (:foce-data db) topic-data)))

(defmethod dispatcher/action :su-share/reset [db _]
  (dissoc db :su-share))

;; Store JWT in App DB so it can be easily accessed in actions etc.

(defmethod dispatcher/action :jwt
  [db [_ jwt-data]]
  (assoc db :jwt jwt-data))

;; Stripe Payment related actions

(defmethod dispatcher/action :subscription
  [db [_ {:keys [uuid] :as data}]]
  (if uuid
    (assoc-in db [:subscription uuid] data)
    (assoc db :subscription nil)))