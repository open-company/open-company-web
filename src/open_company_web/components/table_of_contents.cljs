(ns open-company-web.components.table-of-contents
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]))

(def first-cat-placeholder "first-category")

(defn setup-plus-position [e]
  (let [target (.$ js/window (.-target e))
        position (.offset target)]
    (-> (.$ js/window ".add-section")
        (.css #js {"left" (+ (.-left position) 90) "top" (- (.-top position) 56)}))))

(defn show-popover [e owner]
  (om/update-state! owner :hover-new-section (fn [_]false))
  (om/update-state! owner :hover-add-section (fn [_]false))
  (let [plus-offset (.offset (.$ js/window ".add-section"))
        popover (.$ js/window ".new-section-popover-container")]
    (.click popover (fn [e]
                      (.fadeOut popover 400 #(.css popover #js {"display" "none"}))))
    (.css popover #js {"top" (str (- (.-top plus-offset) 50) "px")
                       "left" (str (- (.-left plus-offset) 100) "px")})
    (.setTimeout js/window #(.fadeIn popover 400) 0)))

(defn setup-hover-events [owner]
  (when (.-$ js/window)
    (-> (.$ js/window ".new-section")
        (.hover (fn [e]
                  (setup-plus-position e)
                  (om/update-state! owner :hover-new-section (fn [_](.-id (.-target e)))))
                (fn [e]
                  (.setTimeout js/window
                    #(om/update-state! owner :hover-new-section (fn [_]false))
                    1))))
    (-> (.$ js/window ".add-section")
        (.hover (fn [e]
                  (om/update-state! owner :hover-add-section (fn [_](om/get-state owner :hover-new-section))))
                (fn [e]
                  (om/update-state! owner :hover-add-section (fn [_]false)))))))

(defcomponent table-of-contents [data owner]
  (init-state [_]
    {:hover-add-section false
     :hover-new-section false})
  (did-mount [_]
    (setup-hover-events owner))
  (did-update [_ _ _]
    (setup-hover-events owner))
  (render [_]
    (let [sections (:sections data)
          categories (:categories data)]
      (dom/div #js {:className "table-of-contents" :ref "table-of-contents"}
        (dom/div {:class "new-section-popover-container"})
        (dom/div {:class "table-of-contents-inner"}
          (for [category categories]
            (dom/div {:class "category-container"}
              (dom/div {:class (utils/class-set {:category true :empty (zero? (count ((keyword category) sections)))})} (dom/h3 (utils/camel-case-str (name category))))
              (dom/div {:class (utils/class-set {:add-section true
                                                 :show (or (om/get-state owner :hover-add-section)
                                                            (om/get-state owner :hover-new-section))})
                        :on-click #(show-popover % owner)}
                (dom/i {:class "fa fa-plus"}))
              (dom/div {:id (str "new-section-" first-cat-placeholder)
                        :class (utils/class-set {:new-section true
                                                 :hover (or (= (om/get-state owner :hover-new-section) (str "new-section-" first-cat-placeholder))
                                                            (= (om/get-state owner :hover-add-section) (str "new-section-" first-cat-placeholder)))})
                        :on-click #(show-popover % owner)}
                (dom/div {:class "new-section-internal"}))
              (for [section (into [] (get sections (keyword category)))]
                (let [section-data ((keyword section) data)]
                  (dom/div {}
                    (dom/div {:class "category-section"}
                      (dom/a {:href "#"
                              :on-click (fn [e]
                                          (.preventDefault e)
                                          (let [section-el (.$ js/window (str "#section-" (name section)))
                                                section-offset (.offset section-el)
                                                top (- (.-top section-offset) 60)]
                                            (.scrollTo js/$ #js {"top" (str top "px") "left" "0px"} 500)))}
                        (dom/p {:class "section-title"} (:title section-data))
                        (dom/p {:class "section-date"} (utils/time-since (:updated-at section-data)))))
                    (dom/div {:id (str "new-section-" (name section))
                              :class (utils/class-set {:new-section true
                                                       :hover (or (= (om/get-state owner :hover-new-section) (str "new-section-" (name section)))
                                                                  (= (om/get-state owner :hover-add-section) (str "new-section-" (name section))))})
                              :on-click #(show-popover % owner)}
                      (dom/div {:class "new-section-internal"}))))))))))))