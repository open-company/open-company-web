(ns oc.web.lib.oc-colors)

(def oc-colors {
  :yellow "#f9d748" ; rgb(249,215,72)
  :green "#26C485" ; rgb(53,188,46)
  :red "#E04B53"
  :blue "#007A9D" ;"#004E64" ;"#109DB7"
  :gray "#D8D8D8" ;"#ADADAD"
  :black "#000000"
  :white "#FCFCFC"
  ;; Greens
  :oc-green-dark-1 "#51d34a"
  :oc-green-dark "#35bc2e"
  :oc-green-regular "#26C485" ; :green
  :oc-green-light "#4DE249" ; rgb(77,226,73)
  ;; Blues
  :oc-blue-dark "#1E81C4"
  :oc-blue-regular "#004E64"
  :oc-blue-light "#007A9D"
  ;; Grays
  :oc-gray-0 "#FEFEFE"
  :oc-gray-1 "#F1F1F1"
  :oc-gray-6 "#E0E0E0"
  :oc-gray-2 "#D8D8D8"
  :oc-gray-3 "#8A8A8A"
  :oc-gray-4 "#5B5B5B"
  :oc-gray-5 "#4E5A6B" ;rgb(78,90,107)
  :oc-gray-5-half "rgba(78,90,107,0.5)"
  :oc-gray-5-3-quarter "rgba(78,90,107,0.75)"
  :oc-gray-7 "#637282"
  :oc-gray-8 "#FBFAF7"
  ;; Reds
  :oc-red-dark-1 "#d10022"
  :oc-red-dark "#9E001A"
  :oc-red-regular "#E04B53" ; :red
  :oc-red-light "#EC7A8D"

  :cr-gray "#333333"
  :cr-dark-bg-gray "#F3F2EE"
  :cr-text-blue "#4c67fc"
  :cr_orange "#FA6452"
  :cr-settings-background "#FCFBF7"
  :cr-dark-blue "#2e405a"
  :cr-green "#0F8E4C"
  :cr-green-1 "#0E8749"
  :cr-text-blue-2 "#3B3D40"
  :cr-light-blue "#DEE8FF"})

(defn get-color-by-kw [kw]
  (if (contains? oc-colors kw)
    (kw oc-colors)
    (:black oc-colors)))

(defn fill-color [color-key]
  (str "fill-color: " (get-color-by-kw color-key)))