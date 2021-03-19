(ns oc.web.utils.color
  (:require [cuerdas.core :as string]
            [oc.lib.color :as lib-color]))

(def colors-presets-list
  [{:rgb {:r 251 :g 94 :b 72}
    :hex "#FB5E48"
    :name "Carrot orange"}
   {:rgb {:r 248 :g 155 :b 68}
    :hex "#F89A44"
    :name "Orange"}
   {:rgb {:r 33 :g 178 :b 104}
    :hex "#21B268"
    :name "Carrot green"}
   {:rgb {:r 105 :g 184 :b 171}
    :hex "#69B8AB"
    :name "Teal"}
   {:rgb {:r 97 :g 135 :b 248}
    :hex "#6187F8"
    :name "Blue"}
   {:rgb {:r 120 :g 83 :b 215}
    :hex "#7853D7"
    :name "Purple"}])