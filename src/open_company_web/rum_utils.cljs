(ns open-company-web.rum-utils
  (:require [org.martinklepsch.derivatives :as drv]
            [rum.core :as rum]
            [om.core :as om]))

(let [get-k ":derivatives/get"
      release-k ":derivatives/release"]
  (defn om-derivatives [mngr]
    (->> {:childContextTypes {get-k js/React.PropTypes.func
                              release-k js/React.PropTypes.func}
          :getChildContext (fn [] (let [{:keys [release! get!]} mngr]
                                    (clj->js {get-k get!
                                              release-k release!})))}
         (merge om/pure-methods)
         clj->js
         om/specify-state-methods!)))

(defn drv-root [{:keys [state drv-spec target component]}]
  (om/root component
           state
           {:target target
            :descriptor (om-derivatives (drv/derivatives-manager drv-spec))}))