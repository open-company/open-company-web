(ns open-company-web.components.table-of-contents
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))

(defcomponent table-of-contents [data owner]
  ; (did-mount [_]
  ;   (if-let [toc-obj (om/get-ref owner "table-of-contents")]
  ;     (let [toc-node (.getDOMNode toc-obj)
  ;           $-toc (.$ js/window toc-node)
  ;           win-height (.height (.$ js/window js/window))]
  ;       (.css $-toc #js {"height" (str (- win-height 50) "px")}))))
  (render [_]
    (dom/div #js {:className "table-of-contents" :ref "table-of-contents"}
      (dom/div {:class "table-of-contents-inner"}
        (dom/div {:class "category"} (dom/h3 "CATEGORY"))
        (for [section (utils/get-section-keys data)]
          (let [section-data (section data)]
            (dom/div {:class "category-section"}
              (dom/a {:href "#"
                      :on-click (fn [e]
                                  (.preventDefault e)
                                  (let [section-el (.$ js/window (str "#section-" (name section)))
                                        section-offset (.offset section-el)
                                        top (- (.-top section-offset) 60)]
                                    (.scrollTo js/$ #js {"top" (str top "px") "left" "0px"} 500)))}
                (dom/p {:class "section-title"} (:title section-data))
                (dom/p {:class "section-date"} (utils/time-since (:udpated-at section-data)))))))))))

