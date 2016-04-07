(ns open-company-web.components.icon
  (:require [om-tools.dom :as dom]
            [open-company-web.local-settings :as ls]))

(defn icon
  "Render an icon from our Nucleo icon set.

  The icon set should be saved at resources/public/img/oc-icons.svg.
  When exporting the icons from Nucleo make sure to use 'Export as <symbol>'.
  Screenshot of Download window: http://i.imgur.com/ltAj6X1.png

  Optional second argument can be used to pass a map of options:
  - :accent-color (oc-blue)
  - :size (30px)
  - :stroke (2px)"
  ([id] (icon id {}))
  ([id {:keys [accent-color size stroke] :as opts}]
   (assert id "Icon ID is required")
   (let [outline-color "#0f0f0f"
         accent-color  (or accent-color "#007A9D") ; oc-blue from scss/modules/_colors.scss
         stroke        (or stroke 2)
         size          (or size 30)]
     (dom/svg {:viewBox "0 0 16 16" :width (str size "px") :height (str size "px")
               :style {:color accent-color :stroke outline-color :strokeWidth (str stroke "px")}
               ;; use tag isn't supported by react 0.14.7 and 0.14.8 isn't on cljsjs
               ;; Also their changelog doesn't mention it at all so I'm not sure if .8 would work
               :dangerouslySetInnerHTML {:__html (str "<use xlink:href=/img/oc-icons.svg?" ls/deploy-key
                                                      "#nc-icon-" (name id) ">")}}))))