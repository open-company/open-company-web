(ns open-company-web.components.table-of-contents
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [cljs.core.async :refer (chan <!)]
            [open-company-web.router :as router]
            [open-company-web.lib.utils :as utils]
            [open-company-web.api :as api]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.components.new-section-popover :refer (new-section-popover)]
            [open-company-web.caches :as caches]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.api :as api]
            [open-company-web.components.table-of-contents-item :refer (table-of-contents-item)]))

(def first-sec-placeholder "firstsectionplaceholder")

(defn get-category-section-info [e]
  (let [target (.-target e)
        $el (.$ js/window target)
        category (.data $el "category")
        section (.data $el "section")]
    {:category category :section section}))

(defn hide-popover [e]
  (when (.-$ js/window)
    (try
      (.css (.$ js/window "body") #js {"overflow" ""})
      (let [popover (.$ js/window "#new-section-popover-container")]
        (.fadeOut popover 400 #(.css popover #js {"display" "none"})))
      (.setTimeout js/window #(try
                                (om/detach-root (.$ js/window "#new-section-popover-container"))
                                (catch :default e)) 1500)
      (catch :default e))))

(defn add-popover-container []
  (when (.-$ js/window) ; avoid tests crash
    (let [popover (.$ js/window "<div id='new-section-popover-container'></div>")
          body (.$ js/window (.-body js/document))
          slug (keyword (:slug @router/path))]
      ; if the new-section-popover div is not present add it
      (when-not (pos? (.-length (.$ js/window "body div#new-section-popover-container")))
        (.append body popover))
      ; if the new-section-popover component has not been mount, render it
      (when-not (pos? (.-length (.$ js/window "body div#new-section-popover-container div.new-section-popover")))
        (.setTimeout js/window
                     #(om/root new-section-popover
                               (slug @caches/new-sections)
                               {:target (.getElementById js/document "new-section-popover-container")})
                     1)))))

(defn show-popover [e category section]
  (when (.-$ js/window) ; avoid tests crash
    (add-popover-container)
    (.css (.$ js/window "body") #js {"overflow" "hidden"})
    (let [$info (.$ js/window "#last-add-section-info")]
      (.data $info "category" category)
      (.data $info "section" section))
    (let [popover (.$ js/window "#new-section-popover-container")]
      (.click popover hide-popover)
      (.setTimeout js/window #(.fadeIn popover 400) 0))))

(defn insert-section
  [category-into section-after category-to-insert section-to-insert sections]
  (let [category-kw (keyword category-to-insert)
        category (category-kw sections)
        fixed-section-after (if (= category-into category-to-insert)
                                  section-after
                                  first-sec-placeholder)]
    (cond
      ; category doesn't exist, create it with the new section
      (not (contains? sections category-kw))
      (merge sections {category-kw [section-to-insert]})
      ; category exists, section is placeholder for first place
      (= fixed-section-after first-sec-placeholder)
      (let [new-category (concat [section-to-insert] (category-kw sections))]
        (merge sections {category-kw (vec new-category)}))
      ; category exists, adding section
      :else
      (let [idx (inc (.indexOf (to-array category) section-after))
            [before after] (split-at idx category)
            new-category (vec (concat before [section-to-insert] after))]
        (merge sections {category-kw new-category})))))

(defn handle-add-section-change [change]
  (let [$info (.$ js/window "#last-add-section-info")
        last-section (.data $info "section")
        last-category (.data $info "category")
        slug (keyword (:slug @router/path))
        company-data (slug @dispatcher/app-state)
        sections (:sections company-data)
        new-sections (insert-section last-category last-section (:category change) (:section change) sections)
        section-defaults (utils/fix-section (merge (:section-defaults change) {:oc-editing true
                                                                               :updated-at (utils/as-of-now)})
                                            (name (:section change)))
        new-section-kw (keyword (:section change))
        new-category (:category change)
        new-categories (if (utils/in? (:categories company-data) new-category)
                         (:categories company-data)
                         (conj (:categories company-data) new-category))]
    (swap! dispatcher/app-state assoc-in [slug] (merge (slug @dispatcher/app-state) {new-section-kw section-defaults}))
    (swap! dispatcher/app-state assoc-in [slug :sections] new-sections)
    (swap! dispatcher/app-state assoc-in [slug :categories] new-categories)
    (.setTimeout js/window #(utils/scroll-to-section new-section-kw) 1000)))

(defcomponent add-section [data owner]

  (render [_]
    (let [section (name (:section data))
          category (name (:category data))]
      (dom/div {:id (str "new-section-*-" (name section))
                :class "new-section"
                :on-click #(show-popover % category section)}
        (dom/div {:class "new-section-internal"})
        (dom/div {:class "add-section"
                  :on-click #(show-popover % category section)}
          (dom/i {:class "fa fa-plus"}))))))

(defn get-new-sections-if-needed [owner]
  (when (not (om/get-state owner :new-sections-requested))
    (let [slug (keyword (:slug @router/path))
          company-data (slug @dispatcher/app-state)]
      (when (and (empty? (slug @caches/new-sections)) (not (empty? company-data)))
        (om/update-state! owner :new-sections-requested not)
        (api/get-new-sections)))))

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
    {:new-sections-requested false})

  (did-mount [_]
    (setup-sortable (:categories data))
    (get-new-sections-if-needed owner)
    (let [add-section-chan (chan)]
      (utils/add-channel "add-section" add-section-chan)
      (go (loop []
        (let [change (<! add-section-chan)]
          (handle-add-section-change change)
          (recur)))))
    (let [close-new-section-popover-chan (chan)]
      (utils/add-channel "close-new-section-popover" close-new-section-popover-chan)
      (go (loop []
        (let [change (<! close-new-section-popover-chan)]
          (hide-popover nil)
          (recur))))))

  (did-update [_ _ _]
    (get-new-sections-if-needed owner)
    (setup-sortable (:categories data)))

  (render [_]
    (let [sections (:sections data)
          categories (:categories data)]
      (dom/div #js {:className "table-of-contents" :ref "table-of-contents"}
        (dom/div {:id "last-add-section-info"
                  :data-category ""
                  :data-section ""})
        (dom/div {:class "table-of-contents-inner"}
          (for [category categories]
            (let [sections ((keyword category) sections)
                  sections-key (str (name category) (apply str sections))]
              (dom/div {:class "category-container"
                        :key sections-key}
                (dom/div {:class (utils/class-set {:category true
                                                   :empty (empty? sections)})}
                         (dom/h3 (utils/camel-case-str (name category))))
                (om/build add-section {:category category
                                       :section first-sec-placeholder})
                (dom/div {:class "category-sections-container"}
                  (for [section sections]
                    (let [section-data ((keyword section) data)]
                      (dom/div {}
                        (om/build table-of-contents-item {
                                            :category category
                                            :section section
                                            :title (:title section-data)
                                            :updated-at (:updated-at section-data)
                                            :show-popover #(show-popover % (name category) (:name section-data))})
                        (om/build add-section {:category (name category)
                                               :section (:section section-data)})))))))))))))