(ns oc.web.utils.org
  (:require [cuerdas.core :as s]
            [defun.core :refer (defun)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.local-settings :as ls]
            [oc.web.dispatcher :as dis]
            [oc.web.utils.theme :as theme-utils]))

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

(defn color-from-rgb [rgb]
  (when-let [rgbs (rgb-string rgb)]
    (str "rgb(" rgbs ")")))

(defn rgb->rgba [rgb opacity]
  (when-let [rgbs (rgb-string rgb)]
    (str "rgba(" rgbs ", " opacity ")")))

(defn default-brand-color []
  (when-let [theme-kw (theme-utils/computed-value (get-in @dis/app-state dis/theme-key))]
    (get ls/default-brand-color theme-kw)))

(defn css-color [color-map]
  (if (:rgb color-map)
    (color-from-rgb (:rgb color-map))
    (:hex color-map)))

(defun set-brand-color!
  ([nil] nil)

  ([primary-color-strings :guard #(and (:rgb-string %)
                                       (:rgba-016 %)
                                       (:rgba-02 %)
                                       (:rgba-04 %)
                                       (:rgba-06 %)
                                       (:rgba-08 %))
    secondary-color-strings :guard #(and (:rgb-string %)
                                         (:rgba-016 %)
                                         (:rgba-02 %)
                                         (:rgba-04 %)
                                         (:rgba-06 %)
                                         (:rgba-08 %))]
   (.. js/document -documentElement -style (setProperty "--primary-color" (:rgb-string primary-color-strings)))
   (.. js/document -documentElement -style (setProperty "--primary-light-color" (:rgba-016 primary-color-strings)))
   (.. js/document -documentElement -style (setProperty "--primary-color-02" (:rgba-02 primary-color-strings)))
   (.. js/document -documentElement -style (setProperty "--primary-color-04" (:rgba-04 primary-color-strings)))
   (.. js/document -documentElement -style (setProperty "--primary-color-06" (:rgba-06 primary-color-strings)))
   (.. js/document -documentElement -style (setProperty "--primary-color-08" (:rgba-08 primary-color-strings)))
   (.. js/document -documentElement -style (setProperty "--secondary-color" (:rgb-string secondary-color-strings)))
   (.. js/document -documentElement -style (setProperty "--secondary-light-color" (:rgba-016 secondary-color-strings)))
   (.. js/document -documentElement -style (setProperty "--secondary-color-02" (:rgba-02 secondary-color-strings)))
   (.. js/document -documentElement -style (setProperty "--secondary-color-04" (:rgba-04 secondary-color-strings)))
   (.. js/document -documentElement -style (setProperty "--secondary-color-06" (:rgba-06 secondary-color-strings)))
   (.. js/document -documentElement -style (setProperty "--secondary-color-08" (:rgba-08 secondary-color-strings))))
  
  ([brand-color-map :guard #(and (:primary %) (:secondary %))]
   (recur {:rgb-string (css-color (:primary brand-color-map))
           :rgba-016 (rgb->rgba (:rgb (:primary brand-color-map)) "0.16")
           :rgba-02 (rgb->rgba (:rgb (:primary brand-color-map)) "0.2")
           :rgba-04 (rgb->rgba (:rgb (:primary brand-color-map)) "0.4")
           :rgba-06 (rgb->rgba (:rgb (:primary brand-color-map)) "0.6")
           :rgba-08 (rgb->rgba (:rgb (:primary brand-color-map)) "0.8")}
          {:rgb-string (css-color (:secondary brand-color-map))
           :rgba-016 (rgb->rgba (:rgb (:secondary brand-color-map)) "0.16")
           :rgba-02 (rgb->rgba (:rgb (:secondary brand-color-map)) "0.2")
           :rgba-04 (rgb->rgba (:rgb (:secondary brand-color-map)) "0.4")
           :rgba-06 (rgb->rgba (:rgb (:secondary brand-color-map)) "0.6")
           :rgba-08 (rgb->rgba (:rgb (:secondary brand-color-map)) "0.8")}))

  ([brand-colors-map :guard #(and (:dark %) (:light %))]
   (when-let [theme-key (theme-utils/computed-value (get-in @dis/app-state dis/theme-key))]
     (recur (get brand-colors-map theme-key))))

  ([org-data :guard map?]
   (recur (or (:brand-color org-data) (default-brand-color)))))

(defn current-brand-color
  ([org-data]
   (when-let [theme-kw (theme-utils/computed-value (get-in @dis/app-state dis/theme-key))]
     (-> org-data :brand-color theme-kw)))
  ([] (current-brand-color (dis/org-data))))