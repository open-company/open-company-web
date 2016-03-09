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
            [open-company-web.components.expanded-topic :refer (expanded-topic)]
            [open-company-web.components.topic-list-edit :refer (topic-list-edit)]
            [open-company-web.components.ui.manage-topics :refer (manage-topics)]
            [open-company-web.components.ui.edit-topic-button :refer (edit-topic-button)]
            [goog.fx.dom :refer (Fade)]
            [goog.fx.dom :refer (Resize)]
            [goog.fx.Animation.EventType :as AnimationEventType]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.style :refer (getStyle setStyle)]))

(def topic-box-size {:width 290
                     :height 170})

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
  (if (= category "all")
    (apply concat (vals (:sections company-data)))
    (get-in company-data [:sections (keyword category)])))

(defn update-active-topics [owner options category new-active-topics]
  (let [old-active-categories (om/get-state owner :active-topics)
        new-active-categories (assoc old-active-categories category new-active-topics)]
    (om/set-state! owner :active-topics new-active-categories)
    ; enable/disable save button
    ((:save-bt-active-cb options) (not= new-active-categories (om/get-state owner :initial-active-topics)))))

(defn get-state [data current-state]
  (let [company-data (:company-data data)
        categories (:categories company-data)
        all-categories (if (or (utils/is-mobile) (:editing current-state)) categories (concat ["all"] categories))
        active-topics (apply merge (map #(hash-map (keyword %) (get-active-topics company-data %)) all-categories))]
    {:editing (or (:editing current-state) false)
     :initial-active-topics active-topics
     :active-topics active-topics
     :new-sections-requested (or (:new-sections-requested current-state) false)
     :save-bt-active (or (:save-bt-active current-state) false)
     :show-topic-edit-button (or (:show-topic-edit-button current-state) false)
     :last-expanded-section (or (:last-expanded-section current-state) nil)
     :bw-expanded-topic nil
     :bw-expand-animated nil}))

(def li-in-row 3)

(defn add-expanded-topic [category-topics owner]
  (if (or (om/get-state owner :bw-expanded-topic)
          (om/get-state owner :bw-expand-animated)
          (om/get-props owner :expanded-topic))
    (let [selected-topic (or (om/get-state owner :bw-expanded-topic)
                             (om/get-state owner :bw-expand-animated)
                             (om/get-props owner :expanded-topic))
          idx (.indexOf (to-array category-topics) selected-topic)
          cur-row (int (/ idx li-in-row))
          insert-at (+ li-in-row (* cur-row li-in-row))
          [before after] (split-at insert-at category-topics)]
      (concat before ["li-expander"] after))
  category-topics))

(defn same-line? [owner topic1 topic2]
  (let [props (om/get-props owner)
        active-category (keyword (:active-category props))
        active-topics (om/get-state owner :active-topics)
        category-topics (get active-topics active-category)
        idx1 (.indexOf (to-array category-topics) topic1)
        idx2 (.indexOf (to-array category-topics) topic2)]
    (= (int (/ idx1 li-in-row)) (int (/ idx2 li-in-row)))))

(defn topic-click [owner topic]
  (let [bw-expanded-topic (om/get-state owner :bw-expanded-topic)
        bw-expand-animated (om/get-state owner :bw-expand-animated)]
    (if (= bw-expanded-topic topic)
      ; close currently expanded topic
      (om/set-state! owner :bw-expanded-topic nil)
      (if (and (not (nil? bw-expanded-topic))
               (not (nil? bw-expand-animated))
               (not= bw-expanded-topic topic)
               (not (same-line? owner bw-expanded-topic topic)))
        ; expand another topic while one is already focused
        (do
          (om/set-state! owner :bw-expanded-topic nil)
          (.setTimeout js/window #(om/set-state! owner :bw-expanded-topic topic) (+ utils/oc-animation-duration 100)))
        ; expand new topic
        (do
          (om/set-state! owner :bw-expanded-topic topic)
          (when-not (nil? (om/get-state owner :bw-expand-animated))
            (om/set-state! owner :bw-expand-animated topic)))))))

(defn scroll-to-card [card]
 (utils/scroll-to-y (- (.-offsetTop card) 40)))

(defn animate-expand [owner expand]
  (when-let [topic (om/get-ref owner "li-expander")]
    (let [bw-expanded-topic (om/get-state owner :bw-expanded-topic)
          current-topic-height (.-offsetHeight topic)
          topic-width (.-offsetWidth topic)]
      (setStyle topic #js {:height "auto"})
      (when (and expand bw-expanded-topic)
        (when-let [topic-card (om/get-ref owner bw-expanded-topic)]
          (scroll-to-card topic-card)))
      (let [topic-height (.-offsetHeight topic)
            resize-animation (new Fade
                                  topic
                                  (if expand 0 1)
                                  (if expand 1 0)
                                  utils/oc-animation-duration)]
        (setStyle topic #js {:height (if expand "0" (str current-topic-height "px"))})
        (.play
          (new Resize
                topic
               (new js/Array topic-width (if expand current-topic-height topic-height))
               (new js/Array topic-width (if expand topic-height 0))
               utils/oc-animation-duration))
        (doto resize-animation
          (events/listen
            AnimationEventType/FINISH
            #(om/set-state! owner :bw-expand-animated (om/get-state owner :bw-expanded-topic)))
          (.play))))))

(def scrolled-to-top (atom false))

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
    (when-not @scrolled-to-top
      (set! (.-scrollTop (.-body js/document)) 0)
      (reset! scrolled-to-top true))
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

  (render-state [_ {:keys [show-topic-edit-button active-topics editing bw-expanded-topic bw-expand-animated last-expanded-section]}]
    (if (or (and bw-expanded-topic (not bw-expand-animated))
            (and (not bw-expanded-topic) bw-expand-animated))
      (.setTimeout js/window #(animate-expand owner (not bw-expand-animated)) 0)
      (when bw-expanded-topic
        (.setTimeout js/window #(scroll-to-card (om/get-ref owner bw-expanded-topic)) 0)))
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
        (let [selected-topic (or bw-expanded-topic bw-expand-animated (:expanded-topic data))
              company-data (:company-data data)
              active-category (keyword (:active-category data))
              category-topics (get active-topics active-category)
              fixed-category-topics (add-expanded-topic category-topics owner)
              idx (.indexOf (to-array category-topics) selected-topic)
              row-idx (mod idx li-in-row)]
          (dom/div {:class "topic-list fix-top-margin-scrolling"
                    :key "topic-list"}
            (dom/ul #js {:className (utils/class-set {:topic-list-internal true
                                                      :group true
                                                      :content-loaded (not (:loading data))})
                         :ref "topic-list-ul"}
              (for [section-name fixed-category-topics
                    :let [sd (->> section-name keyword (get company-data))
                          li-props (if-not (utils/is-mobile)
                                      #js {:className (utils/class-set {
                                                          :topic-row true
                                                          :full-width (= section-name "li-expander")
                                                          :expanded (= selected-topic section-name)
                                                          :unexpanded (and selected-topic
                                                                           (not= selected-topic section-name)
                                                                           (not= section-name "li-expander"))})
                                           :ref section-name
                                           :style #js {:opacity (if (= section-name "li-expander")
                                                                  (if (or bw-expand-animated (:expanded-topic data))
                                                                      1 0)
                                                                  1)
                                                       :height  (if (= section-name "li-expander")
                                                                  (if (or bw-expand-animated (:expanded-topic data))
                                                                    "auto" "0px")
                                                                  (str (:height topic-box-size) "px"))}
                                           :key (str "topic-row-" (name section-name))}
                                      #js {:className "topic-row"
                                           :ref section-name
                                           :key (str "topic-row-" (name section-name))})]]
                (dom/li li-props
                  (if (= section-name "li-expander")
                    (let [sec-data (->> selected-topic keyword (get company-data))]
                      (dom/div #js {:className (str "topic li-" row-idx)}
                        (om/build expanded-topic {:section-data sec-data
                                                  :section selected-topic
                                                  :expanded true
                                                  :currency (:currency company-data)}
                                                 {:opts {
                                                   :section-name section-name
                                                   :toggle-edit-topic-cb (partial toggle-edit-topic-button owner)
                                                   :topic-edit-cb (:topic-edit-cb options)}})))
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
                                               :bw-topic-click (partial topic-click owner)}}))))))
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
                           {:topic-edit-cb #((:topic-edit-cb options) last-expanded-section)}})))))))))
