(ns open-company-web.components.ui.icon
  (:require [om-tools.dom :as dom]
            [open-company-web.lib.oc-colors :as occ]
            [open-company-web.lib.raven :as raven]
            [open-company-web.local-settings :as ls]))

(defn icon
  "Render an icon from our Nucleo icon set.

  The icon set should be saved at resources/public/img/oc-icons.svg.
  When exporting the icons from Nucleo make sure to use 'Export as <symbol>'.
  Screenshot of Download window: http://i.imgur.com/ltAj6X1.png

  Optional second argument can be used to pass a map of options:
  - :accent-color (accent color, default: oc-blue)
  - :color (main color, default: black)
  - :size (size of the icon, default: 30px)
  - :stroke (size of the stroke, default: 2px)"
  ([id] (icon id {}))
  ([id {:keys [color accent-color size stroke class] :as opts}]
   (when-not id
     (raven/capture-error-with-message "oc-icon/icon: missing ID"))
   (let [fixed-id      (or id "")
         outline-color (or color (occ/get-color-by-kw :black))
         accent-color  (or accent-color (occ/get-color-by-kw :black))
         stroke        (or stroke 2)
         size          (or size 30)]
     (dom/div {:class (str "svg-icon " class) :style {:width (str size "px") :height (str size "px")}}
       (dom/svg {:viewBox "0 0 16 16" :width (str size "px") :height (str size "px")
                 :style {:color accent-color :stroke outline-color :strokeWidth (str stroke "px")}
                 ;; use tag isn't supported by react 0.14.7 and 0.14.8 isn't on cljsjs
                 ;; Also their changelog doesn't mention it at all so I'm not sure if .8 would work
                 :dangerouslySetInnerHTML {:__html (str "<use xlink:href=/img/oc-icons.svg?" ls/deploy-key
                                                        "#nc-icon-" (name fixed-id) ">")}})))))