(ns open-company-web.components.user-selector
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [cljs-dynamic-resources.core :as cdr]))

(defn get-name [user]
  (let [real-name (:real_name user)]
    (if (> (count real-name) 0)
      real-name
      (:name user))))

(defn format-state [state]
  (if-not (.-id state) (.-text state))
  (let [text (.-text state)
        el (.$ js/window (.-element state))
        img (.data el "icon")
        my-temp (.$ js/window (str "<span><img class=\"user-icon\" src=\"" img "\" />" (.-text state) "</span>"))]
    my-temp))

(defn init-select2 [owner]
  ; get needed states
  (let [req-libs-loaded (om/get-state owner :req-libs-loaded)
        did-mount (om/get-state owner :did-mount)
        select2-initialized (om/get-state owner :select2-initialized)]
    ; check if we are ready to initialize the widget and if we haven't aready done that
    (when (and req-libs-loaded did-mount (not select2-initialized))
      ; init the widget
      (let [us (.$ js/window ".user-selector")]
        (.select2 us (clj->js {"templateResult" format-state
                               "templateSelection" format-state})))
      ; save flag so we don't reinitialize the widget
      (om/update-state! owner :select2-initialized (fn [_]true)))))

(defcomponent user-selector [data owner]
  (init-state [_]
    {:req-libs-loaded false
     :did-mount false
     :select2-initialized false})
  (will-mount [_]
    ; load needed resources
    (cdr/add-style! "/lib/select2/css/select2.css")
    (cdr/add-scripts! [{:src "//ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"}
                       {:src "/lib/select2/js/select2.js"}]
                      (fn []
                        (om/update-state! owner :req-libs-loaded (fn [] true))
                        (init-select2 owner))))
  (did-mount [_]
    (om/update-state! owner :did-mount (fn [] true))
    (init-select2 owner))
  (render [_]
    (dom/div {:class "col-md-4"}
      (dom/select {:class "user-selector"
                   :value (:value data)
                   :style {"width" "100%"}}
        (for [user (:users data)]
          (when-not (:is_bot user)
            (dom/option {
                         :value (:id user)
                         :data-icon (:image_24 (:profile user))}
                        (get-name user))))))))
