(ns open-company-web.components.table-of-contents
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.api :as api]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.api :as api]
            [open-company-web.components.table-of-contents-item :refer (table-of-contents-item)]))

(def first-cat-placeholder "first-category")

(defn setup-plus-position [e]
  (let [target (.$ js/window (.-target e))
        offset (.position target)
        t-top (- (.-top offset) 5)
        t-left (+ (.-left offset) 195)]
    (-> (.$ js/window ".add-section")
        (.css #js {"left" t-left "top" t-top}))))

(defn show-popover [e owner]
  (when (.-$ js/window) ; avoid tests crash
    (om/update-state! owner :hover-new-section (fn [_]false))
    (om/update-state! owner :hover-add-section (fn [_]false))
    (let [section (or (om/get-state owner :hover-new-section)
                      (om/get-state owner :hover-add-section))
          plus-offset (.position (.$ js/window ".add-section"))
          popover (.$ js/window ".new-section-popover-container")
          window-scrolltop (.scrollTop (.$ js/window js/window))]
      (.click popover (fn [e]
                        (.fadeOut popover 400 #(.css popover #js {"display" "none"}))))
      (.css popover #js {"top" (str (+ (.-top plus-offset) 40 window-scrolltop) "px")
                         "left" (str (+ (.-left plus-offset) 100) "px")})
      (.setTimeout js/window #(.fadeIn popover 400) 0))))

(defn setup-hover-events [owner]
  (when (.-$ js/window) ; avoid tests crash
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

(defn add-popover-container []
  (when (.-$ js/window) ; avoid tests crash
    (let [popover (.$ js/window "<div class='new-section-popover-container'></div>")
          body (.$ js/window (.-body js/document))]
      (.append body popover))))

(defn get-section-form-id [section-id]
  (get (clojure.string/split section-id "--") 1))

(defn resort-category [category]
  {(keyword category) (vec (map #(get-section-form-id (.-id %))
                                (sel (str "div.category-sortable.category-" category))))})

(defn sort-end [event ui categories]
  (let [sections (apply merge (map #(resort-category %) categories))]
    (api/patch-sections sections)))

(defn setup-sortable [categories]
  (when (.-$ js/window)
    (.sortable (.$ js/window ".category-sections-container")
               #js {"axis" "y"
                    "stop" #(sort-end %1 %2 categories)})))

(defcomponent table-of-contents [data owner]

  (init-state [_]
    {:hover-add-section false
     :hover-new-section false})

  (did-mount [_]
    (setup-hover-events owner)
    (add-popover-container)
    (setup-sortable (:categories data)))

  (did-update [_ _ _]
    (setup-hover-events owner)
    (setup-sortable (:categories data)))
  
  (display-name [_] "ToC")

  (render [_]
    (let [sections (:sections data)
          categories (:categories data)]
      (dom/div #js {:className "table-of-contents"
                    :ref "table-of-contents"}
        (dom/div {:class (utils/class-set {:add-section true
                                           :show (or (om/get-state owner :hover-add-section)
                                                     (om/get-state owner :hover-new-section))})
                  :on-click #(show-popover % owner)}
          (dom/i {:class "fa fa-plus"}))
        (dom/div {:class "table-of-contents-inner"}
          (for [category categories]
            (let [sections ((keyword category) sections)
                  sections-key (if (empty? sections)
                                 (str (rand 10))
                                 (apply str sections))]
              (dom/div {:class "category-container"
                        :key sections-key}
                (dom/div {:class (utils/class-set {:category true
                                                   :empty (empty? sections)})}
                         (dom/h3 (utils/camel-case-str (name category))))
                (dom/div {:id (str "new-section-" first-cat-placeholder)
                          :class (utils/class-set {:new-section true
                                                   :hover (or (= (om/get-state owner :hover-new-section)
                                                                 (str "new-section-" first-cat-placeholder))
                                                              (= (om/get-state owner :hover-add-section)
                                                                 (str "new-section-" first-cat-placeholder)))})
                          :on-click #(show-popover % owner)}
                  (dom/div {:class "new-section-internal"}))
                (dom/div {:class "category-sections-container"}
                  (for [section sections]
                    (let [section-data ((keyword section) data)]
                      (om/build table-of-contents-item {
                                          :category category
                                          :section section
                                          :title (:title section-data)
                                          :updated-at (:updated-at section-data)
                                          :show-popover #(show-popover % owner)
                                          :hover (or (= (om/get-state owner :hover-new-section)
                                                        (str "new-section-" (name section)))
                                                     (= (om/get-state owner :hover-add-section)
                                                        (str "new-section-" (name section))))}))))))))))))