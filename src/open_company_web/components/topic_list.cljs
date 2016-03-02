(ns open-company-web.components.topic-list
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [cljs.core.async :refer (chan <!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.caches :as caches]
            [open-company-web.api :as api]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.topic :refer (topic)]
            [open-company-web.components.topic-list-edit :refer (topic-list-edit)]
            [open-company-web.components.ui.manage-topics :refer (manage-topics)]
            [open-company-web.components.ui.edit-topic-button :refer (edit-topic-button)]
            [goog.fx.dom :refer (Fade)]
            [goog.fx.Animation.EventType :as AnimationEventType]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.style :refer (getStyle setStyle)]))

(defn get-new-sections-if-needed [owner]
  (when-not (om/get-state owner :new-sections-requested)
    (let [slug (keyword (:slug @router/path))
          company-data (slug @dispatcher/app-state)]
      (when (and (empty? (slug @caches/new-sections)) (seq company-data))
        (om/update-state! owner :new-sections-requested not)
        (api/get-new-sections)))))

(defn save-sections-cb [owner options]
  (api/patch-sections (om/get-state owner :active-topics))
  ((:navbar-editing-cb options) false)
  (om/set-state! owner :editing false))

(defn manage-topics-cb [owner options]
  (om/set-state! owner :editing true)
  ((:navbar-editing-cb options) true)
  (utils/scroll-to-y 0))

(defn toggle-edit-topic-button [owner & [section-name]]
  (om/set-state! owner :last-expanded-section section-name)
  ; avoid to do animate if it's not needed
  (when-let [edit-topic-button (om/get-ref owner "edit-topic-button")]
    (let [is-showing (om/get-state owner :show-topic-edit-button)
          current-display (getStyle edit-topic-button "display")]
      (when-not is-showing
        ; reset display rule if going to show
        (setStyle edit-topic-button "display" "inline"))
      (let [start (if (= current-display "none") 0 1)
            end   (if (= current-display "none") 1 0)
            fade-anim (new Fade
                          edit-topic-button
                          start
                          end
                          utils/oc-animation-duration)]
        (doto fade-anim
          (events/listen
            AnimationEventType/FINISH
            (fn [_]
              (om/update-state! owner :show-topic-edit-button not)))
          (.play))))))

(defn force-edit-button [owner show & [section-name]]
  (let [showing (om/get-state owner :show-topic-edit-button)]
    (if (= show showing)
      (when section-name
        (om/set-state! owner :last-expanded-section section-name))
      (toggle-edit-topic-button owner section-name))))

(defn get-active-topics [company-data category]
  (get-in company-data [:sections (keyword category)]))

(defn update-active-topics [owner options category new-active-topics]
  (let [old-active-categories (om/get-state owner :active-topics)
        new-active-categories (assoc old-active-categories category new-active-topics)]
    (om/set-state! owner :active-topics new-active-categories)
    ; enable/disable save button
    ((:save-bt-active-cb options) (not= new-active-topics (om/get-state owner :initial-active-topics)))))

(defn get-state [data current-state]
  (let [company-data (:company-data data)
        categories (:categories company-data)
        active-topics (apply merge (map #(hash-map (keyword %) (get-active-topics company-data %)) categories))]
    {:editing (or (:editing current-state) false)
     :initial-active-topics active-topics
     :active-topics active-topics
     :new-sections-requested (or (:new-sections-requested current-state) false)
     :save-bt-active (or (:save-bt-active current-state) false)
     :show-topic-edit-button (or (:show-topic-edit-button current-state) false)
     :last-expanded-section (or (:last-expanded-section current-state) nil)
     :bw-expanded-topic nil}))

(defn calc-ul-width [owner]
  (let [win-width (.-clientWidth (.-body js/document))
        ul (om/get-ref owner "topic-list-ul")
        li-in-row (int (/ win-width 400))]
    (setStyle ul #js {:width (str (* 400 li-in-row) "px")})))

(defn add-expanded-topic [category-topics owner]
  (if (om/get-state owner :bw-expanded-topic)
    (let [win-width (.-clientWidth (.-body js/document))
          selected-topic (om/get-state owner :bw-expanded-topic)
          fix-category-topics (to-array category-topics)
          idx (.indexOf fix-category-topics selected-topic)
          li-in-row (int (/ win-width 400))
          cur-row (int (/ idx li-in-row))
          insert-at (+ li-in-row (* cur-row li-in-row))
          [before after] (split-at insert-at category-topics)]
      (concat before ["li-expander"] after))
  category-topics))

(defcomponent topic-list [data owner {:keys [navbar-editing-cb] :as options}]

  (init-state [_]
    (let [save-ch (chan)
          cancel-ch (chan)]
      (utils/add-channel "save-bt-navbar" save-ch)
      (utils/add-channel "cancel-bt-navbar" cancel-ch))
    (get-state data nil))

  (did-mount [_]
    ; scroll to top when the component is initially mounted to
    ; make sure the calculation for the fixed navbar are correct
    (set! (.-scrollTop (.-body js/document)) 0)
    (when-not (:read-only (:company-data data))
      (get-new-sections-if-needed owner))
    ; save all the changes....
    (let [save-ch (utils/get-channel "save-bt-navbar")]
      (go (while true
        (let [change (<! save-ch)]
          (save-sections-cb owner options)))))
    (let [cancel-ch (utils/get-channel "cancel-bt-navbar")]
      (go (while true
        (let [change (<! cancel-ch)]
          ((:navbar-editing-cb options) false)
          ; reset editing
          (om/set-state! owner :editing false)
          ; reset active topics changes
          (om/set-state! owner :active-topics (om/get-state owner :initial-active-topics)))))))

  (will-unmount [_]
    (utils/remove-channel "save-bt-navbar")
    (utils/remove-channel "cancel-bt-navbar"))

  (did-update [_ prev-props _]
    (when-not (= (:company-data prev-props) (:company-data data))
      (om/set-state! owner (get-state data (om/get-state owner))))
    (when-not (:read-only (:company-data data))
      (get-new-sections-if-needed owner)))

  (render-state [_ {:keys [show-topic-edit-button active-topics editing]}]
    (.setTimeout js/window #(calc-ul-width owner) 100)
    (let [slug (keyword (:slug @router/path))]
      (if editing
        (let [categories (map name (keys active-topics))]
          (dom/div {:class "topic-list-edit-container"
                    :key "topic-list-edit-container"}
            (for [cat categories]
              (om/build topic-list-edit
                        (merge data {:active (= cat (:active-category data))
                                     :category cat
                                     :active-topics (get active-topics (keyword cat))})
                        {:key cat
                         :opts {:active-category (:active-category data)
                                :new-sections (slug @caches/new-sections)
                                :did-change-sort (partial update-active-topics owner options (keyword cat))}}))))
        (let [company-data (:company-data data)
              active-category (keyword (:active-category data))
              category-topics (get active-topics active-category)
              fixed-category-topics (add-expanded-topic category-topics owner)]
          (dom/div {:class "topic-list fix-top-margin-scrolling"
                    :key "topic-list"}
            (dom/ul #js {:className (utils/class-set {:topic-list-internal true
                                                      :group true
                                                      :content-loaded (not (:loading data))})
                         :ref "topic-list-ul"}
              (for [section-name fixed-category-topics
                    :let [sd (->> section-name keyword (get company-data))]]
                (dom/li {:class (utils/class-set {:topic-row true
                                                  :full-width (= section-name "li-expander")})
                          :key (str "topic-row-" (name section-name))}
                  (if (= section-name "li-expander")
                    (let [sec-data (->> (om/get-state owner :bw-expanded-topic) keyword (get company-data))]
                     (dom/div {:class "topic"}
                      (dom/div {:class "topic-expanded-body"
                                :dangerouslySetInnerHTML (clj->js {"__html" (:body sec-data)})})))
                    (when-not (and (:read-only company-data) (:placeholder sd))
                      (om/build topic {:loading (:loading company-data)
                                       :section section-name
                                       :section-data sd
                                       :currency (:currency company-data)
                                       :active-category active-category}
                                       {:opts {:section-name section-name
                                               :navbar-editing-cb navbar-editing-cb
                                               :force-edit-cb (partial force-edit-button owner)
                                               :toggle-edit-topic-cb (partial toggle-edit-topic-button owner)
                                               :bw-topic-click #(om/set-state! owner :bw-expanded-topic %)}}))))))
            (when (and (not (:read-only company-data)) (seq company-data))
              (dom/div #js {:className "manage-topics-container"
                            :style #js {:opacity (if (om/get-state owner :show-topic-edit-button) "0" "1")}}
                (om/build manage-topics
                          nil
                          {:opts {:manage-topics-cb #(manage-topics-cb owner options)}})))
            (when-not (:read-only company-data)
              (dom/div #js {:className "topic-row floating-edit-topic-button"
                            :ref "edit-topic-button"
                            :style #js {:opacity (if show-topic-edit-button "1" "0")
                                        :display (if show-topic-edit-button "inline" "none")}}
                (om/build edit-topic-button
                          nil
                          {:opts
                           {:edit-topic-cb #((:topic-edit-cb options) (om/get-state owner :last-expanded-section))}})))))))))