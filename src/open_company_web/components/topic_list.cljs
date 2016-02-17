(ns open-company-web.components.topic-list
  (:require [om.core :as om :include-macros true]
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
            [goog.fx.Animation.EventType :as EventType]
            [goog.events :as events]
            [goog.style :refer (getStyle setStyle)]))

(defn get-new-sections-if-needed [owner]
  (when-not (om/get-state owner :new-sections-requested)
    (let [slug (keyword (:slug @router/path))
          company-data (slug @dispatcher/app-state)]
      (when (and (empty? (slug @caches/new-sections)) (seq company-data))
        (om/update-state! owner :new-sections-requested not)
        (api/get-new-sections)))))

(defn save-sections-cb [owner data options new-sections]
  (let [company-data (:company-data data)
        categories (:categories company-data)
        active-category (keyword (:active-category data))
        old-active-sections (get-in company-data [:sections active-category])
        remaining-categories (utils/vec-dissoc categories (name active-category))
        remaining-sections (apply merge
                                  (map #(hash-map (keyword %) ((keyword %) (:sections company-data)))
                                       remaining-categories))
        all-sections (assoc remaining-sections active-category new-sections)]
    (api/patch-sections all-sections)
    ((:navbar-editing-cb options) false)
    (om/set-state! owner :editing false)))

(defn manage-topics-cb [owner options]
  (om/set-state! owner :editing true)
  ((:navbar-editing-cb options) true)
  (utils/scroll-to-y 0))

(defn toggle-edit-topic-button [owner show section-name]
  (om/set-state! owner :last-expanded-section (if show section-name nil))
  ; avoid to do animate if it's not needed
  (when-not (= show (om/get-state owner :show-topic-edit-button))
    (when-let [edit-topic-button (om/get-ref owner "edit-topic-button")]
      (let [current-display (getStyle edit-topic-button "display")]
        (when show
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
              EventType/FINISH
              (fn [_]
                (om/set-state! owner :show-topic-edit-button show)))
            (.play)))))))

(defcomponent topic-list [data owner {:keys [navbar-editing-cb] :as options}]

  (init-state [_]
    {:editing false
     :new-sections-requested false
     :show-topic-edit-button false
     :last-expanded-section nil})

  (did-mount [_]
    (when-not (:read-only (:company-data data))
      (get-new-sections-if-needed owner)))

  (did-update [_ _ _]
    (when-not (:read-only (:company-data data))
      (get-new-sections-if-needed owner)))

  (render-state [_ {:keys [show-topic-edit-button editing] :as state}]
    (let [slug (keyword (:slug @router/path))]
      (if editing
        (om/build topic-list-edit data {:opts {:new-sections (slug @caches/new-sections)
                                               :active-category (:active-category data)
                                               :save-sections-cb (partial save-sections-cb owner data options)
                                               :cancel-editing-cb (fn []
                                                                    (om/set-state! owner :editing false)
                                                                    (navbar-editing-cb false))}})
        (let [company-data (:company-data data)
              active-category (keyword (:active-category data))
              active-sections (get-in company-data [:sections active-category])]
          (dom/div {:class "topic-list fix-top-margin-scrolling"}
            (dom/div {:class "topic-list-internal"}
              (for [section-name active-sections]
                (dom/div {:class "topic-row"
                          :key (str "topic-row-" (name section-name))}
                  (om/build topic {:loading (:loading company-data)
                                   :company-data company-data
                                   :active-category active-category}
                                   {:opts {:section-name section-name
                                           :navbar-editing-cb navbar-editing-cb
                                           :toggle-edit-topic-cb (partial toggle-edit-topic-button owner)}}))))
            (when (and (not (:read-only company-data)) (pos? (count active-sections)))
              (om/build manage-topics {} {:opts {:manage-topics-cb #(manage-topics-cb owner options)}}))
            (when-not (:read-only company-data)
              (dom/div #js {:className "topic-row floating-edit-topic-button"
                            :ref "edit-topic-button"
                            :style #js {:opacity (if show-topic-edit-button "1" "0")
                                        :display (if show-topic-edit-button "inline" "none")}}
                (om/build edit-topic-button
                          nil
                          {:opts
                           {:edit-topic-cb #((:topic-edit-cb options) (om/get-state owner :last-expanded-section))}})))))))))