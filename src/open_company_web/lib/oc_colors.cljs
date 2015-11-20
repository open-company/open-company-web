(ns open-company-web.lib.oc-colors)

(def green-color "#26C485")
(def red-color "#d72a46")
(def blue-color "#109DB7")
(def gray-color "#ADADAD")
(def black-color "#000000")

(defn get-color-by-kw [kw]
  (case (keyword kw)
    :green green-color
    :red red-color
    :blue blue-color
    :gray gray-color
    :black black-color
    black-color))

(defn fill-color [color-key]
  (str "fill-color: " (get-color-by-kw color-key)))