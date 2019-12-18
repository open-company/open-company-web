(ns oc.web.actions.dark-mode
  (:require [dommy.core :as dommy :refer-macros (sel1)]
            [oc.web.lib.jwt :as jwt]
            [oc.web.dispatcher :as dis]
            [oc.web.lib.cookies :as cook]))

(def ^:private dark-mode-cookie-name-suffix :dark-mode)

(def ^:private dark-mode-class-name-prefix :theme-mode)

(def ^:private dark-mode-values #{:dark :light :auto})

(def ^:private dark-mode-default-value :auto)

(defn dark-mode-cookie-name []
  (str (jwt/user-id) "-" (name dark-mode-cookie-name-suffix)))

(defn save-dark-mode-cookie [v]
  (cook/set-cookie! (dark-mode-cookie-name) (name v) (* 60 60 24 365)))

(defn read-dark-mode-cookie []
  (let [cookie-val (cook/get-cookie (dark-mode-cookie-name))]
    (if (seq cookie-val)
      (keyword cookie-val)
      dark-mode-default-value)))

(defn- set-dark-mode-class [mode]
  (let [html-el (sel1 [:html])]
    (dommy/remove-class! (sel1 [:html]) (str (name dark-mode-class-name-prefix) "-dark"))
    (dommy/remove-class! (sel1 [:html]) (str (name dark-mode-class-name-prefix) "-light"))
    (dommy/add-class! (sel1 [:html]) (str (name dark-mode-class-name-prefix) "-" (name mode)))))

(defn get-dark-mode-setting []
  (let [current-mode (read-dark-mode-cookie)]
    (if (= current-mode :auto)
      (if (and js/window.-matchMedia
               (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)")))
        :dark
        :light)
      (or current-mode :light))))

(defn set-dark-mode [v]
  (save-dark-mode-cookie v)
  (set-dark-mode-class v)
  (dis/dispatch! [:input dis/dark-mode-key v]))

(defn setup-dark-mode []
  (let [cur-val (get-dark-mode-setting)]
    (set-dark-mode-class cur-val)
    (dis/dispatch! [:input dis/dark-mode-key cur-val])))

(setup-dark-mode)