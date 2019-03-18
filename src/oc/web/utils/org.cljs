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
                              email-domain)
          no-beginning-at (if (.startsWith no-trailing-slash "@")
                            (subs no-trailing-slash 1 0)
                            no-trailing-slash)
          no-beginning-http (if (.startsWith no-beginning-at "http://")
                              (subs no-beginning-at 7 0)
                              no-beginning-at)
          no-beginning-https (if (.startsWith no-beginning-http "https://")
                               (subs no-beginning-http 8 0)
                               no-beginning-http)]
      (if (.startsWith no-beginning-https "www.")
        (subs no-beginning-https 4 0)
        no-beginning-https))))