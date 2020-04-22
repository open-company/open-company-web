(ns oc.web.utils.org
  (:require [oc.web.lib.jwt :as jwt]))

(def org-avatar-filestack-config
  {:accept "image/*"
   :fromSources ["local_file_system"]})

(def org-name-max-length 50)

(defn clean-email-domain [email-domain]
  (when email-domain
    (let [lower-case-domain (clojure.string/lower-case email-domain)
          trailing-slash? (.endsWith lower-case-domain "/")
          no-trailing-slash (if trailing-slash?
                              (subs email-domain 0 (dec (count email-domain)))
                              email-domain)
          no-beginning-at (if (.startsWith no-trailing-slash "@")
                            (subs no-trailing-slash 1)
                            no-trailing-slash)
          no-beginning-http (if (.startsWith no-beginning-at "http://")
                              (subs no-beginning-at 7)
                              no-beginning-at)
          no-beginning-https (if (.startsWith no-beginning-http "https://")
                               (subs no-beginning-http 8)
                               no-beginning-http)]
      (if (.startsWith no-beginning-https "www.")
        (subs no-beginning-https 4)
        no-beginning-https))))

(defn is-org-creator? [org-data]
  (= (:user-id (:author org-data)) (jwt/user-id)))

(defn disappearing-count-value [previous-val next-val]
  (if (and (integer? next-val)
           (pos? next-val))
    next-val
    previous-val))