(ns open-company-web.components.topic-list
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :refer-macros (sel1)]
            [open-company-web.api :as api]
            [open-company-web.urls :as oc-urls]
            [open-company-web.caches :as caches]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dispatcher]
            [open-company-web.lib.jwt :as jwt]
            [open-company-web.lib.oc-colors :as oc-colors]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.cookies :as cook]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.topic :refer (topic)]
            [open-company-web.components.fullscreen-topic :refer (fullscreen-topic)]
            [open-company-web.components.topics-columns :refer (topics-columns)]
            [open-company-web.components.tooltip :refer (tooltip)]
            [open-company-web.components.ui.icon :refer (icon)]
            [open-company-web.components.ui.small-loading :refer (small-loading)]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.fx.Animation.EventType :as AnimationEventType]
            [goog.fx.dom :refer (Fade Slide)]
            [cljsjs.hammer]))

(defn get-new-sections-if-needed [owner]
  (when-not (om/get-state owner :new-sections-requested)
    (let [slug (keyword (router/current-company-slug))
          company-data (dispatcher/company-data)]
      (when (and (empty? (slug @caches/new-sections))
                 (seq company-data))
        (om/update-state! owner :new-sections-requested not)
        (api/get-new-sections)))))

(defn get-active-topics [company-data category]
  (get-in company-data [:sections (keyword category)]))

(defn remove-topic [owner topic]
  (let [company-data (om/get-props owner :company-data)
        old-categories (:sections company-data)
        new-categories (apply merge (map #(hash-map (first %) (utils/vec-dissoc (second %) topic)) old-categories))]
    (api/patch-sections new-categories)))

(defn update-active-topics [owner category-name new-topic & [section-data]]
  (let [company-data (om/get-props owner :company-data)
        old-categories (:sections company-data)
        old-topics (get-active-topics company-data category-name)
        new-topics (concat old-topics [new-topic])
        new-categories (assoc old-categories (keyword category-name) new-topics)]
    (dispatcher/set-force-edit-topic new-topic)
    (if section-data
      (api/patch-sections new-categories section-data new-topic)
      (api/patch-sections new-categories))))

(defn filter-placeholder-sections [topics company-data]
  (vec (filter #(not (:placeholder (->> % keyword (get company-data)))) topics)))

(defn get-category-topics [company-data active-topics]
  (let [topics-list   (flatten (vals active-topics))]
    (map keyword topics-list)))

(defn should-show-add-topic-tooltip [company-data active-topics]
  (let [category-topics (get-category-topics company-data active-topics)]
    (and (jwt/jwt)
         (not (:read-only company-data))
         (not (responsive/is-mobile))
         (or (empty? category-topics)
             (zero? (count (filter #(not (->> % keyword (get company-data) :placeholder)) category-topics)))))))

(defn get-state [owner data current-state]
  (let [company-data (:company-data data)
        categories (:categories company-data)
        active-topics (apply merge (map #(hash-map (keyword %) (get-active-topics company-data %)) categories))
        show-add-topic-tooltip (should-show-add-topic-tooltip company-data active-topics)
        selected-topic (if (nil? current-state) (router/current-section) (:selected-topic current-state))]
    {:initial-active-topics active-topics
     :active-topics active-topics
     :card-width (:card-width data)
     :new-sections-requested (or (:new-sections-requested current-state) false)
     :selected-topic selected-topic
     :tr-selected-topic nil
     :topic-navigation (or (:topic-navigation current-state) true)
     :share-selected-topics (:sections (:stakeholder-update company-data))
     :transitioning false
     :redirect-to-preview (or (:redirect-to-preview current-state) false)
     :fullscreen-force-edit (if (nil? current-state) (router/section-editing?) (:fullscreen-force-edit current-state))
     :add-topic-tooltip-dismissed (or (:add-topic-tooltip-dismissed current-state) false)
     :show-add-topic-tooltip show-add-topic-tooltip
     :show-second-add-topic-tooltip (or (:show-second-add-topic-tooltip current-state) false)
     :second-tooltip-dismissed (or (:second-tooltip-dismissed current-state) false)
     :show-share-su-tooltip (or (:show-share-su-tooltip current-state) false)
     :share-su-tooltip-dismissed (or (:share-su-tooltip-dismissed current-state) false)}))

(defn topic-click [owner topic selected-metric & [force-edit]]
  (if force-edit
        (.pushState js/history nil (str "Edit " (name topic)) (oc-urls/company-section-edit (router/current-company-slug) (name topic)))
        (.pushState js/history nil (name topic) (oc-urls/company-section (router/current-company-slug) (name topic))))
  (when force-edit
    (om/set-state! owner :fullscreen-force-edit true))
  (om/set-state! owner :selected-topic topic)
  (om/set-state! owner :selected-metric selected-metric)
  (utils/after 100 #(om/set-state! owner :fullscreen-force-edit false)))

(def scrolled-to-top (atom false))

(defn close-overlay-cb [owner]
  (.pushState js/history nil "Dashboard" (oc-urls/company (router/current-company-slug)))
  (om/set-state! owner :transitioning false)
  (om/set-state! owner :selected-topic nil)
  (om/set-state! owner :selected-metric nil)
  (om/set-state! owner :fullscreen-force-edit false))

(defn switch-topic [owner is-left?]
  (when (and (om/get-state owner :topic-navigation)
             (om/get-state owner :selected-topic)
             (nil? (om/get-state owner :tr-selected-topic)))
    (let [selected-topic (om/get-state owner :selected-topic)
          active-topics (om/get-state owner :active-topics)
          topics-list (flatten (vals active-topics))
          current-idx (.indexOf (vec topics-list) selected-topic)]
      (om/set-state! owner :animation-direction is-left?)
      (if is-left?
        ;prev
        (let [prev-idx (mod (dec current-idx) (count topics-list))
              prev-topic (get (vec topics-list) prev-idx)]
          (om/set-state! owner :tr-selected-topic prev-topic))
        ;next
        (let [next-idx (mod (inc current-idx) (count topics-list))
              next-topic (get (vec topics-list) next-idx)]
          (om/set-state! owner :tr-selected-topic next-topic))))))

(defn kb-listener [owner e]
  (let [key-code (.-keyCode e)]
    (when (= key-code 39)
      ;next
      (switch-topic owner false))
    (when (= key-code 37)
      (switch-topic owner true))))

(defn animation-finished [owner]
  (let [cur-state (om/get-state owner)]
    (.pushState js/history nil (name (:tr-selected-topic cur-state)) (oc-urls/company-section (router/current-company-slug) (:tr-selected-topic cur-state)))
    (om/set-state! owner (merge cur-state {:selected-topic (:tr-selected-topic cur-state)
                                           :transitioning true
                                           :tr-selected-topic nil}))))

(defn animate-selected-topic-transition [owner left?]
  (let [selected-topic (om/get-ref owner "selected-topic")
        tr-selected-topic (om/get-ref owner "tr-selected-topic")
        width (utils/fullscreen-topic-width (om/get-state owner :card-width))
        fade-anim (new Slide selected-topic #js [0 0] #js [(if left? width (* width -1)) 0] utils/oc-animation-duration)
        cur-state (om/get-state owner)]
    (doto fade-anim
      (.listen AnimationEventType/FINISH #(animation-finished owner))
      (.play))
    (.play (new Fade selected-topic 1 0 utils/oc-animation-duration))
    (.play (new Slide tr-selected-topic #js [(if left? (* width -1) width) 0] #js [0 0] utils/oc-animation-duration))
    (.play (new Fade tr-selected-topic 0 1 utils/oc-animation-duration))))

(defn preview-and-share-click [owner e]
  (utils/event-stop e)
  (let [props (om/get-props owner)
        company-data (:company-data props)
        su-data (:stakeholder-update company-data)
        share-selected-topics (om/get-state owner :share-selected-topics)
        title (if (clojure.string/blank? (:title su-data))
                (utils/su-default-title)
                (:title su-data))]
    (api/patch-stakeholder-update {:title title :sections share-selected-topics})
    (om/set-state! owner :redirect-to-preview true)))

(defn should-show-first-edit-tooltip [company-data category-topics]
  ; show first edit tooltip if there is only one section and is a placeholder section
  (and (= (count category-topics) 1)
       (->> category-topics first keyword (get company-data) :placeholder)))

(defcomponent topic-list [data owner options]

  (init-state [_]
    (get-state owner data nil))

  (did-mount [_]
    (when-not (:read-only (:company-data data))
      (get-new-sections-if-needed owner))
    ; scroll to top when the component is initially mounted to
    ; make sure the calculation for the fixed navbar are correct
    (when-not @scrolled-to-top
      (set! (.-scrollTop (.-body js/document)) 0)
      (reset! scrolled-to-top true))
    (when (and (not (utils/is-test-env?))
               (responsive/user-agent-mobile?))
      (let [kb-listener (events/listen js/window EventType/KEYDOWN (partial kb-listener owner))
            swipe-listener (js/Hammer (sel1 [:div#app]))];(.-body js/document))]
        (om/set-state! owner :kb-listener kb-listener)
        (om/set-state! owner :swipe-listener swipe-listener)
        (.on swipe-listener "swipeleft" (fn [e] (switch-topic owner false)))
        (.on swipe-listener "swiperight" (fn [e] (switch-topic owner true))))))

  (will-unmount [_]
    (when (and (not (utils/is-test-env?))
               (responsive/user-agent-mobile?))
      (events/unlistenByKey (om/get-state owner :kb-listener))
      (when-let [swipe-listener (om/get-state owner :swipe-listener)]
        (.off swipe-listener "swipeleft")
        (.off swipe-listener "swiperight"))))

  (will-receive-props [_ next-props]
    (when (om/get-state owner :redirect-to-preview)
      (utils/after 100 #(router/nav! (oc-urls/stakeholder-update-preview))))
    (when-not (= (:company-data next-props) (:company-data data))
      (om/set-state! owner (get-state owner next-props (om/get-state owner))))
    (when-not (:read-only (:company-data next-props))
      (get-new-sections-if-needed owner))
    (when (:force-edit-topic next-props)
      (let [company-data (:company-data next-props)
            topics (flatten (vals (:sections company-data)))
            no-placeholder-sections (filter-placeholder-sections topics company-data)]
        (when (contains? company-data (keyword (:force-edit-topic next-props)))
          (om/set-state! owner :fullscreen-force-edit true)
          (om/set-state! owner :selected-topic (dispatcher/force-edit-topic))
          ; show second tooltip of needed
          (when (= (count (flatten (vals (:sections company-data)))) 1)
            (om/set-state! owner :show-second-add-topic-tooltip true))
          (when (= (count no-placeholder-sections) 2)
            (om/set-state! owner :show-share-su-tooltip true))))))

  (did-update [_ _ _]
    (when (om/get-state owner :tr-selected-topic)
      (animate-selected-topic-transition owner (om/get-state owner :animation-direction))))

  (render-state [_ {:keys [active-topics
                           selected-topic
                           selected-metric
                           tr-selected-topic
                           transitioning
                           share-selected-topics
                           redirect-to-preview
                           fullscreen-force-edit
                           show-add-topic-tooltip
                           add-topic-tooltip-dismissed
                           show-second-add-topic-tooltip
                           second-tooltip-dismissed
                           show-share-su-tooltip
                           share-su-tooltip-dismissed]}]
    (let [company-data    (:company-data data)
          category-topics (get-category-topics company-data active-topics)
          card-width      (:card-width data)
          columns-num     (:columns-num data)
          ww              (.-clientWidth (sel1 js/document :body))
          total-width     (case columns-num
                            3 (str (+ (* card-width 3) 40 60) "px")
                            2 (str (+ (* card-width 2) 20 60) "px")
                            1 (if (> ww 413) (str card-width "px") "auto"))]
      (dom/div {:class "topic-list group"
                :key "topic-list"}
        ;; Activate sharing mode button
        (when (and (not (responsive/is-mobile))
                   (responsive/can-edit?)
                   (not (:read-only company-data))
                   (> (count (filter-placeholder-sections category-topics company-data)) 1))
          (dom/div {:class "sharing-button-container"
                    :style #js {:width total-width}}
            (dom/button {:class "sharing-button"
                         :on-click #(router/nav! (oc-urls/stakeholder-update-preview))} "SHARE A SNAPSHOT " (dom/i {:class "fa fa-share"}))))
        ;; Fullscreen topic
        (when selected-topic
          (dom/div {:class "selected-topic-container"
                    :style #js {:opacity (if selected-topic 1 0)}}
              (dom/div #js {:className "selected-topic"
                            :key (str "transition-" selected-topic)
                            :ref "selected-topic"
                            :style #js {:opacity 1 :backgroundColor "rgba(255, 255, 255, 0.98)"}}
                (om/build fullscreen-topic {:section selected-topic
                                            :section-data (->> selected-topic keyword (get company-data))
                                            :change-url true
                                            :fullscreen-force-edit fullscreen-force-edit
                                            :revision-updates (dispatcher/section-revisions (router/current-company-slug) (router/current-section))
                                            :selected-metric selected-metric
                                            :read-only (:read-only company-data)
                                            :card-width card-width
                                            :currency (:currency company-data)
                                            :animate (not transitioning)
                                            :show-first-edit-tooltip (should-show-first-edit-tooltip company-data category-topics)}
                                           {:opts {:close-overlay-cb #(close-overlay-cb owner)
                                                   :topic-edit-cb (:topic-edit-cb options)
                                                   :remove-topic (partial remove-topic owner)
                                                   :topic-navigation #(om/set-state! owner :topic-navigation %)}}))
            ;; Fullscreen topic for transition
            (when tr-selected-topic
              (dom/div #js {:className "tr-selected-topic"
                            :key (str "transition-" tr-selected-topic)
                            :ref "tr-selected-topic"
                            :style #js {:opacity (if tr-selected-topic 0 1)}}
              (om/build fullscreen-topic {:section tr-selected-topic
                                          :section-data (->> tr-selected-topic keyword (get company-data))
                                          :selected-metric selected-metric
                                          :read-only (:read-only company-data)
                                          :card-width card-width
                                          :currency (:currency company-data)
                                          :animate false}
                                         {:opts {:close-overlay-cb #(close-overlay-cb owner)
                                                 :topic-edit-cb (:topic-edit-cb options)
                                                 :remove-topic (partial remove-topic owner)
                                                 :topic-navigation #(om/set-state! owner :topic-navigation %)}})))))
        ;; Topics list columns
        (om/build topics-columns {:columns-num columns-num
                                  :card-width card-width
                                  :selected-metric selected-metric
                                  :show-fast-editing true
                                  :total-width total-width
                                  :content-loaded (not (:loading data))
                                  :topics category-topics
                                  :company-data company-data
                                  :topics-data company-data
                                  :share-selected-topics share-selected-topics}
                                 {:opts {:topic-click (partial topic-click owner)
                                         :update-active-topics (partial update-active-topics owner)}})
        (when (and (not add-topic-tooltip-dismissed)
                   show-add-topic-tooltip)
          (om/build tooltip
            {:cta (str "HI " (jwt/get-key :name) ", WELCOME TO OPENCOMPANY! TO GET STARTED, ADD A TOPIC.")}
            {:opts {:dismiss-tooltip #(om/set-state! owner :add-topic-tooltip-dismissed true)}}))
        (when (and show-second-add-topic-tooltip
                   (not selected-topic)
                   (not second-tooltip-dismissed))
          (om/build tooltip
            {:cta "GREAT! YOU ADDED A TOPIC. ADD ANOTHER AND YOU'LL START TO SEE THE BIG PICTURE."}
            {:opts {:dismiss-tooltip #(om/set-state! owner :second-tooltip-dismissed true)}}))
        (when (and show-share-su-tooltip
                   (not selected-topic)
                   (not share-su-tooltip-dismissed))
          (om/build tooltip
            {:cta "YOUR BIG PICTURE IS COMING TOGETHER. YOU CAN SHARE A SNAPSHOT OF SELECTED TOPICS WITH YOUR TEAM, INVESTORS OR THE CROWD WHEN YOU'RE READY."}
            {:opts {:class "large" :dismiss-tooltip #(om/set-state! owner :share-su-tooltip-dismissed true)}}))))))