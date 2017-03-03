(ns oc.web.lib.image-upload
  (:require [oc.web.local-settings :as ls]
            [goog.object :as gobj]))

(def fs (atom nil))

(defn init-filestack []
  (when js/filestack
    (.init js/filestack ls/filestack-key)))

(defn upload!
  [success-cb progress-cb error-cb & [finished-cb selected-cb started-cb]]
  (when-not @fs
    (reset! fs (init-filestack)))
  (.then
    (.pick @fs
      (clj->js {
        :accept "image/*"
        :maxFiles 1
        :fromSources ["local_file_system" "imagesearch" "googledrive" "dropbox" "onedrive" "gmail" "clouddrive"]
        ;; Selected cb
        :onFileSelected (fn [res]
          (when (fn? selected-cb)
            (selected-cb res)))
        ;; Started cb
        :onFileUploadStarted (fn [res]
          (when (fn? started-cb)
            (started-cb res)))
        ;; Progress cb
        :onFileUploadProgress progress-cb
        ;; Finished cb
        :onFileUploadFinished (fn [res]
          (when (fn? finished-cb)
            (finished-cb res)))
        ;; Error cb
        :onFileUploadFailed error-cb}))
    (fn [res]
      (let [files-uploaded (gobj/get res "filesUploaded")]
        (when (= (count files-uploaded)1)
          (success-cb (get files-uploaded 0)))))))