(ns oc.web.actions.label
  (:require [oc.web.dispatcher :as dis]
            [oc.lib.hateoas :as hateoas]
            [taoensso.timbre :as timbre]
            [oc.web.utils.color :as color-utils]
            [oc.web.lib.json :refer (json->cljs)]
            [oc.web.api :as api]))

;; Data parse

(defn parse-labels [labels-resp]
  (->> labels-resp
      :collection
      :items
      (mapv #(-> %
                 (assoc :can-edit? (hateoas/link-for (:links %) "partial-update"))
                 (assoc :can-delete? (hateoas/link-for (:links %) "delete"))))))

(defn get-labels-finished
  ([org-slug resp] (get-labels-finished org-slug resp nil))
  ([org-slug finish-cb {:keys [body success]}]
   (let [response-body (when success (json->cljs body))
         labels (parse-labels response-body)]
     (timbre/infof "Labels load success %s, loaded %d labels" success (count labels))
     (dis/dispatch! [:labels-loaded org-slug labels])
      (when (fn? finish-cb)
        (finish-cb labels)))))

(defn get-labels
  ([] (get-labels (dis/org-data) nil))
  ([org-data] (get-labels org-data nil))
  ([org-data finish-cb]
   (timbre/info "Loading labels")
   (let [labels-link (hateoas/link-for (:links org-data) "labels")]
     (api/get-labels labels-link (partial get-labels-finished (:slug org-data) finish-cb)))))

(defn most-used-labels []
  (reverse (sort-by :count (dis/org-labels))))

(defn new-label []
  (let [org-data (dis/org-data)
        random-color (-> color-utils/default-css-color-names
                         vals
                         shuffle
                         first)]
    (timbre/infof "New labe for org %s, random color %s" (:uuid org-data) random-color)
    (dis/dispatch! [:editing-label {:name ""
                                    :color random-color
                                    :org-uuid (:uuid org-data)}])))

(defn dismiss-label-editor []
  (timbre/info "Dismiss label editor")
  (dis/dispatch! [:dismiss-editing-label]))

(defn save-label []
  (let [org-data (dis/org-data)
        create-label-link (hateoas/link-for (:links org-data) "create-label")
        editing-label (:editing-label @dis/app-state)]
    (timbre/infof "Saving label with name %s and color %s" (:name editing-label) (:color editing-label))
    (api/create-label create-label-link editing-label
                      #(do
                         (dismiss-label-editor)
                         (get-labels)))))

(defn edit-label [label]
  (timbre/infof "Editing label" (:uuid label))
  (dis/dispatch! [:editing-label label]))

(defn delete-label [label]
  (timbre/infof "Deleting label %s" (:uuid label))
  (let [delete-link (hateoas/link-for (:links label) "delete")]
    (dis/dispatch! [:delete-label (dis/current-org-slug) label])
    (api/delete-label delete-link
                      (fn []
                        (get-labels)))))

(defn show-labels-manager []
  (timbre/info "Open labels manager")
  (dis/dispatch! [:input [:show-labels-manager] true]))

(defn hide-labels-manager []
  (timbre/info "Close labels manager")
  (dis/dispatch! [:input [:show-labels-manager] false]))