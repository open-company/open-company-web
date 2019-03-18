(ns oc.web.utils.org)

(def org-avatar-filestack-config
  {:accept "image/*"
   :fromSources ["local_file_system"]})

(def org-name-max-length 64)

(defn clean-email-domain [email-domain]
  (when email-domain
    (let [trailing-slash? (.endsWith email-domain "/")
          no-trailing-slash (if trailing-slash?
                              (subs email-domain 0 (dec (count email-domain)))
                              email-domain)]
      (-> no-trailing-slash
       (subs (if (.startsWith email-domain "@") 1 0))
       (subs (if (.startsWith email-domain "http://") 7 0))
       (subs (if (.startsWith email-domain "https://") 8 0))
       (subs (if (.startsWith email-domain "www.") 4 0))))))