(ns oc.web.utils.org
  (:require [cuerdas.core :as s]
            [defun.core :refer (defun)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.local-settings :as ls]
            [oc.web.dispatcher :as dis]
            [oc.web.actions.ui-theme :as theme]))

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

(defn disappearing-count-value [previous-val next-val]
  (if (and (integer? next-val)
           (pos? next-val))
    next-val
    previous-val))

(defn- rgb-string [rgb]
  (when (and (map? rgb)
             (map #(contains? rgb %) [:r :g :b]))
    (str (:r rgb) "," (:g rgb) "," (:b rgb))))

(defn primary-color-from-rgb [rgb]
  (when-let [rgbs (rgb-string rgb)]
    (str "rgb(" rgbs ")")))

(defn primary-light-color-from-rgb [rgb]
  (when-let [rgbs (rgb-string rgb)]
    (str "rgba(" rgbs ", 0.16)")))

(defn default-brand-color []
  (when-let [theme-kw (theme/computed-theme)]
    (get ls/default-brand-color theme-kw)))

(defun set-brand-color!
  ([nil])

  ([primary-color :guard string? primary-light-color :guard string? secondary-color :guard string?]
   (.. js/document -documentElement -style (setProperty "--primary-color" primary-color))
   (.. js/document -documentElement -style (setProperty "--primary-light-color" primary-light-color))
   (.. js/document -documentElement -style (setProperty "--secondary-color" secondary-color)))

  ([color-rgb :guard #(and (:r %) (:g %) (:b %)) button-color :guard string?]
   (recur (primary-color-from-rgb color-rgb) (primary-light-color-from-rgb color-rgb) button-color))

  ([color-map :guard #(and (:rgb %) (:button-color %))]
   (recur (:rgb color-map) (:button-color color-map)))

  ([brand-color-map :guard #(and (:dark %) (:light %))]
   (when-let [theme-key (theme/computed-theme)]
     (recur (get brand-color-map theme-key))))

  ([org-data :guard map?]
   (recur (or (:brand-color org-data) (default-brand-color)))))

(defn current-brand-color
  ([org-data]
   (when-let [theme-kw (theme/computed-theme)]
     (-> org-data :brand-color theme-kw)))
  ([] (current-brand-color (dis/org-data))))