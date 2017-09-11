(ns oc.onboard-tip-svg)

;; top,left: 55.2729802,167.894197
;; example: /img/ML/onboard_tip.svg?w=width&h=height&c=55,167
;; starting SVG image /img/ML/tip_circle.svg

(def onboard-tip-image-1
  (str
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
    "<svg width=\"100%\" height=\"100%\" viewBox=\"0 0 100% 100%\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">"
        "<!-- Generator: Sketch 46.2 (44496) - http://www.bohemiancoding.com/sketch -->"
        "<title>BG Dim</title>"
        "<desc>Created with Sketch.</desc>"
        "<defs></defs>"
        "<g id=\"F&amp;F-Flows\" stroke=\"none\" stroke-width=\"1\" fill=\"none\" fill-rule=\"evenodd\" fill-opacity=\"0.9\" opacity=\"0.303668478\">"
            "<g id=\"Boards-Onboarding-Tip\" fill=\"#34414F\">"
                "<g id=\"BG-Dim\">"
                    "<path"
                        " d=\""))

(def onboard-tip-image-2
 (str
    "\"></path>"
                "</g>"
            "</g>"
        "</g>"
    "</svg>"))

(defn get-onboard-image [width height px py]
  (let [width (read-string width)
        height (read-string height)
        px (read-string px)
        py (read-string py)
        first-line (str "M0,0 L" width ",0 L" width "," height " L0," height " L0,0 Z\n")
        offset-x (- px 61.2729802)
        offset-y (- py 167.894197)
        second-line (str "M" (+ 163.5 offset-x) "," (+ 167.916362 offset-y) "\n")
        nth-line-3 (str "C" (+ 114.677264 offset-x) "," (+ 167.894197 offset-y)
                        " " (+ 75.9839099 offset-x) "," (+ 207.763401 offset-y)
                        " " (+ 65.4710739 offset-x) "," (+ 255.912323 offset-y) "\n")
        nth-line-4 (str "C" (+ 55.2729802 offset-x) "," (+ 302.358977 offset-y)
                        " " (+ 99.1839635 offset-x) "," (+ 350.091197 offset-y)
                        " " (+ 163.5 offset-x) "," (+ 351.513079 offset-y) "\n")
        nth-line-5 (str "C" (+ 227.816037 offset-x) "," (+ 350.091197 offset-y)
                        " " (+ 271.72702 offset-x) "," (+ 302.357868 offset-y)
                        " " (+ 261.528926 offset-x) "," (+ 255.912323 offset-y) "\n")
        nth-line-6 (str "C" (+ 251.01609 offset-x) "," (+ 207.763401 offset-y)
                        " " (+ 212.322736 offset-x) "," (+ 167.894197 offset-y)
                        " " (+ 163.5 offset-x) "," (+ 167.916362 offset-y)
                        " Z ")]
    (str onboard-tip-image-1 first-line second-line nth-line-3 nth-line-4 nth-line-5 nth-line-6 onboard-tip-image-2)))