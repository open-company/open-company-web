(ns open-company-web.lib.oc-colors)

(def oc-colors {
  :green "#74E0B4" ;"#26C485"
  :red "#d72a46"
  :blue "#007A9D" ;"#004E64" ;"#109DB7"
  :gray "#D8D8D8" ;"#ADADAD"
  :black "#000000"
  ;; greens
  :oc-green-dark "#008C54"
  :oc-green-regular "#26C485"
  :oc-green-light "#74E0B4"
  ;; blues
  :oc-blue-dark "#003848"
  :oc-blue-regular "#004E64"
  :oc-blue-light "#007A9D"
  ;; Grays
  :oc-gray-0 "#FEFEFE"
  :oc-gray-1 "#F1F1F1"
  :oc-gray-2 "#D8D8D8"
  :oc-gray-3 "#8B8A8A"
  :oc-gray-4 "#5B5B5B"

  :oc-red-dark "#9E001A"
  :oc-red-regular "#D72A46"
  :oc-red-light "#EC7A8D"})

(defn get-color-by-kw [kw]
  (if (contains? oc-colors kw)
    (kw oc-colors)
    (:black oc-colors)))

(defn fill-color [color-key]
  (str "fill-color: " (get-color-by-kw color-key)))