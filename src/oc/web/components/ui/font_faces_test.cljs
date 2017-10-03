(ns oc.web.components.ui.font-faces-test
  (:require [rum.core :as rum]))

(def statement "Quick wafting zephyrs vex bold Jim.")

(rum/defc font-faces-test
  []
  [:div.font-faces-test
    "Reference of Avenir weights: " [:a {:href "https://www.myfonts.com/fonts/linotype/avenir/" :target "_blank"} "here"]
    ;; Lighter
    [:div.font-faces-test-row
      [:div.font-faces-test-face.AvenirLTStd-Book.weight-100
        "This is the lighter font ever, AvenirLTStd-Book with font-weight of 100"]]
    ;; Book
    [:div.font-faces-test-row
      [:div.font-faces-test-face.AvenirLTStd-Book.group
        [:div.title "AvenirLTStd-Book:"]
        [:div.statement statement]]
      [:div.font-faces-test-face.AvenirLTStd-Medium.weight-400.group
        [:div.title "Medium weight 400:"]
        [:div.statement statement]]]
    ;; Roman
    [:div.font-faces-test-row
      [:div.font-faces-test-face.AvenirLTStd-Roman.group
        [:div.title "AvenirLTStd-Roman:"]
        [:div.statement statement]]
      [:div.font-faces-test-face.AvenirLTStd-Medium.weight-500.group
        [:div.title "Medium weight 500:"]
        [:div.statement statement]]]
    ;; Medium
    [:div.font-faces-test-row
      [:div.font-faces-test-face.AvenirLTStd-Medium.group
        [:div.title "AvenirLTStd-Medium:"]
        [:div.statement statement]]
      [:div.font-faces-test-face.AvenirLTStd-Medium.weight-600.group
        [:div.title "Medium weight 600:"]
        [:div.statement statement]]]
    ;; Heavy
    [:div.font-faces-test-row
      [:div.font-faces-test-face.AvenirLTStd-Heavy.group
        [:div.title "AvenirLTStd-heavy:"]
        [:div.statement statement]]
      [:div.font-faces-test-face.AvenirLTStd-Medium.weight-800.group
        [:div.title "Medium weight 800:"]
        [:div.statement statement]]]
    ;; Black
    [:div.font-faces-test-row
      [:div.font-faces-test-face.AvenirLTStd-Black.group
        [:div.title "AvenirLTStd-Black:"]
        [:div.statement statement]]
      [:div.font-faces-test-face.AvenirLTStd-Medium.weight-900.group
        [:div.title "Medium weight 900:"]
        [:div.statement statement]]]
    [:div.font-faces-test-row
      [:div.font-faces-test-face.AvenirLTStd-Black.weight-900
        "This is the darker font ever, AvenirLTStd-Black with font-weight of 900"]]])