(ns oc.web.lib.image-upload
  (:require [oc.web.local-settings :as ls]
            [goog.object :as gobj]))

(def fs (atom nil))

(defn init-filestack []
  (when js/filestack
    (.init js/filestack ls/filestack-key)))

(defn upload!
  [type success-cb progress-cb error-cb & [finished-cb selected-cb started-cb]]
  (let [from-sources (if (= type "image/*")
                        ["local_file_system" "imagesearch" "googledrive" "dropbox" "onedrive" "box"]
                        ["local_file_system" "googledrive" "dropbox" "onedrive" "box"])
        base-config   {:maxFiles 1
                       :maxSize (* 20 1024 1024) ; Limit the uploaded file to be at most 20MB
                       :storeTo {
                        :container ls/attachments-bucket
                        :location "s3"
                       }
                       :transformOptions {
                         :transformations {
                           :crop true
                           :rotate true
                           :circle true
                         }
                       }
                       :fromSources from-sources
                       ;; Selected cb
                       :onFileSelected (fn [res]
                         (when (fn? selected-cb)
                           (selected-cb res)))
                       ;; Started cb
                       :onFileUploadStarted (fn [res]
                         (when (fn? started-cb)
                           (started-cb res)))
                       ;; Progress cb
                       :onFileUploadProgress (fn [res progress]
                         (when (fn? progress-cb)
                            (progress-cb res progress)))
                       ;; Finished cb
                       :onFileUploadFinished (fn [res]
                         (when (fn? finished-cb)
                           (finished-cb res)))
                       ;; Error cb
                       :onFileUploadFailed error-cb}
        config        (if type
                        (merge base-config {:accept type})
                        base-config)]
    (when-not @fs
      (reset! fs (init-filestack)))
    (.then
      (.pick @fs
        (clj->js config))
      (fn [res]
        (let [files-uploaded (gobj/get res "filesUploaded")]
          (when (= (count files-uploaded)1)
            (success-cb (get files-uploaded 0))))))))