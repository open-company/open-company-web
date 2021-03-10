(ns oc.web.actions.label
  (:require [oc.web.dispatcher :as dis]
            [oc.lib.hateoas :as hateoas]
            [taoensso.timbre :as timbre]
            [oc.web.utils.color :as color-utils]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.api :as api]))

;; Data parse

(defn parse-label [label-map]
  (-> label-map
      (assoc :can-edit? (hateoas/link-for (:links label-map) "partial-update"))
      (assoc :can-delete? (hateoas/link-for (:links label-map) "delete"))))

(defn parse-labels [labels-resp]
  (->> labels-resp
      :collection
      :items
      (mapv parse-label)))

(defn user-labels [org-labels-list]
   (reverse (sort-by :count org-labels-list)))

(defn org-labels [org-labels-list]
  (sort-by :name org-labels-list))

(defn get-labels-finished
  ([org-slug resp] (get-labels-finished org-slug resp nil))
  ([org-slug finish-cb {:keys [body success]}]
   (let [response-body (when success (json->cljs body))
         parsed-labels (parse-labels response-body)
         org-labels-data (-> response-body
                             (dissoc :items)
                             (assoc :org-labels (org-labels parsed-labels))
                             (assoc :user-labels (user-labels parsed-labels)))]
     (timbre/infof "Labels load success %s, loaded %d labels" success (count (:org-labels org-labels-data)))
     (dis/dispatch! [:labels-loaded org-slug org-labels-data])
      (when (fn? finish-cb)
        (finish-cb (:org-labels org-labels-data))))))

(defn get-labels
  ([] (get-labels (dis/org-data) nil))
  ([org-data] (get-labels org-data nil))
  ([org-data finish-cb]
   (timbre/info "Loading labels")
   (let [labels-link (hateoas/link-for (:links org-data) "labels")]
     (api/get-labels labels-link (partial get-labels-finished (:slug org-data) finish-cb)))))

(defn new-label []
  (let [org-data (dis/org-data)
        random-color (-> color-utils/default-css-color-names
                         vals
                         shuffle
                         first)]
    (timbre/infof "New label for org %s, random color %s" (:uuid org-data) random-color)
    (dis/dispatch! [:label-editor/replace {:name ""
                                           :color random-color
                                           :org-uuid (:uuid org-data)}])))

(defn dismiss-label-editor []
  (timbre/info "Dismiss label editor")
  (dis/dispatch! [:label-editor/dismiss]))

(defn save-label []
  (let [org-data (dis/org-data)
        create-label-link (hateoas/link-for (:links org-data) "create-label")
        editing-label (:editing-label @dis/app-state)]
    (timbre/infof "Saving label with name %s and color %s" (:name editing-label) (:color editing-label))
    (api/create-label create-label-link editing-label
                      (fn [{:keys [body success]}]
                         (dismiss-label-editor)
                         (get-labels)
                         (let [label (when success (parse-label (json->cljs body)))]
                           (dis/dispatch! [:label-saved (dis/current-org-slug) label]))))))

(defn edit-label [label]
  (timbre/infof "Editing label" (:uuid label))
  (dis/dispatch! [:label-editor/start label]))

(defn delete-label [label]
  (timbre/infof "Deleting label %s" (:uuid label))
  (let [delete-link (hateoas/link-for (:links label) "delete")]
    (dis/dispatch! [:delete-label (dis/current-org-slug) label])
    (api/delete-label delete-link #(get-labels))))

(defn show-labels-manager []
  (timbre/info "Open labels manager")
  (dis/dispatch! [:labels-manager/show]))

(defn hide-labels-manager []
  (timbre/info "Close labels manager")
  (dis/dispatch! [:labels-manager/hide]))

(defn label-editor-update [label-data]
  (dis/dispatch! [:label-editor/update label-data]))