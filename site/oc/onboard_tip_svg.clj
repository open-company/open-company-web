(ns oc.onboard-tip-svg)

;; top,left: 55,167
;; example: /img/ML/onboard_tip.svg?w=width&h=height&c=55,167

(def onboard-tip-image
  (str
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
    "<svg width=\"100%\" height=\"100%\" viewBox=\"0 0 100% 100%\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">"
    "    <!-- Generator: Sketch 46.2 (44496) - http://www.bohemiancoding.com/sketch -->"
    "    <title>BG Dim</title>"
    "    <desc>Created with Sketch.</desc>"
    "    <defs></defs>"
    "    <g id=\"F&amp;F-Flows\" stroke=\"none\" stroke-width=\"1\" fill=\"none\" fill-rule=\"evenodd\" fill-opacity=\"0.9\" opacity=\"0.303668478\">"
    "        <g id=\"Boards-Onboarding-Tip\" fill=\"#34414F\">"
    "            <g id=\"BG-Dim\">"
    "                <path"
    "                    d=\"M0,0 L1435,0 L1435,780 L0,780 L0,0 Z"
    "                       M163.5,167.916362"
    "                         C114.677264,167.894197 75.9839099,207.763401 65.4710739,255.912323"
    "                         C55.2729802,302.358977 99.1839635,350.091197 163.5,351.513079"
    "                         C227.816037,350.091197 271.72702,302.357868 261.528926,255.912323"
    "                         C251.01609,207.763401 212.322736,167.894197 163.5,167.916362 Z\"></path>"
    "            </g>"
    "        </g>"
    "    </g>"
    "</svg>"))

(defn get-onboard-image [width height px py]
  (let [first-line (str "M0,0 L" width ",0 L" width "," height " L0," height " L0,0 Z")
        second-line (str "M163.5,167.916362")]))