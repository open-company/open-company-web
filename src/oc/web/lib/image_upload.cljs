(ns oc.web.lib.image-upload
  (:require [oc.web.local-settings :as ls]
            [cljsjs.filestack]
            [goog.object :as gobj]
            [oc.web.lib.sentry :as sentry]))

(def _fs (atom nil))

(defn init-filestack []
  (or
    @_fs
    (let [new-fs (.init js/filestack ls/filestack-key)]
      (reset! _fs new-fs)
      new-fs)))

(def store-to
  {:container ls/attachments-bucket
   :region "us-east-1"
   :location "s3"})

(defn upload!
  [config success-cb progress-cb error-cb & [close-cb finished-cb selected-cb started-cb]]
  (let [from-sources (if (= (:accept config) "image/*")
                        ["local_file_system" "imagesearch" "googledrive" "dropbox" "onedrive" "box"]
                        ["local_file_system" "googledrive" "dropbox" "onedrive" "box"])
        base-config   {:maxFiles 1
                       :maxSize ls/file-upload-size ; Limit the uploaded file to be at most 20MB
                       :storeTo store-to
                       :transformations {
                         :crop {:circle true}
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
                       :onFileUploadFailed error-cb
                       ;; Close cb
                       :onClose (fn []
                          (when (fn? close-cb)
                            (close-cb)))}
        config        (merge base-config config)
        fs (init-filestack)]
    (.then
      (.pick fs
        (clj->js config))
      (fn [res]
        (let [files-uploaded (gobj/get res "filesUploaded")]
          (when (= (count files-uploaded)1)
            (success-cb (get files-uploaded 0))))))))

(defn upload-file! [file success-cb & [error-cb progress-cb]]
  (try
    (let [fs-client (init-filestack)]
      (.then
        (.upload fs-client file #js {:onProgress #(when (fn? progress-cb) (progress-cb (.-totalPercent %)))})
        (fn [res]
          (let [url (gobj/get res "url")]
            (when (fn? success-cb)
              (success-cb url))))))
    (catch :default e
      (sentry/capture-error! e)
      (when (fn? error-cb)
        (error-cb e)))))

(defn thumbnail! [fs-url & [success-cb progress-cb error-cb]]
  (let [fs-client (init-filestack)
        opts (clj->js {:resize {
                        :fit "crop"
                        :width 272
                        :height 204
                        :align "faces"}})
       transformed-url (.transform fs-client fs-url opts)
       fixed-success-cb (fn [res]
                          (when (fn? success-cb)
                            (success-cb res)))
       fixed-progress-cb (fn [res progress]
                           (when (fn? progress-cb)
                             (progress-cb res progress)))
       fixed-error-cb (fn [res err]
                        (when (fn? error-cb)
                          (error-cb res err)))
       storing-task (.storeURL fs-client
                     ;; transformed image
                     transformed-url
                     ;; store options
                     (clj->js store-to)
                     ;; onSuccess
                     fixed-success-cb
                     ;; onError
                     fixed-error-cb
                      ;; onProgress
                      fixed-progress-cb)]
    (try
      (.then
        storing-task
        (fn [res]
          (let [url (gobj/get res "url")]
            (when (fn? success-cb)
              (success-cb url)))))
      (catch :default e
        (sentry/capture-error! e)
        (when (fn? error-cb)
          (error-cb e))))))