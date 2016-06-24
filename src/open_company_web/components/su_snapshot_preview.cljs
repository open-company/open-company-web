(ns open-company-web.components.su-snapshot-preview
  (:require-macros [cljs.core.async.macros :refer (go)])
  (:require [cljs.core.async :refer (chan <!)]
            [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [dommy.core :as dommy :refer-macros (sel1 sel)]
            [open-company-web.api :as api]
            [open-company-web.router :as router]
            [open-company-web.dispatcher :as dis]
            [open-company-web.lib.utils :as utils]
            [open-company-web.lib.responsive :as responsive]
            [open-company-web.components.menu :refer (menu)]
            [open-company-web.components.navbar :refer (navbar)]
            [open-company-web.components.ui.back-to-dashboard-btn :refer (back-to-dashboard-btn)]
            [open-company-web.components.ui.icon :as i]
            [open-company-web.components.topics-columns :refer (topics-columns)]
            [open-company-web.components.su-preview-dialog :refer (su-preview-dialog)]
            [cljs-dynamic-resources.core :as cdr]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.fx.Animation.EventType :as AnimationEventType]
            [goog.fx.dom :refer (Fade)]
            [cljsjs.hammer]
            [cljsjs.react.dom]))

(defn post-stakeholder-update [owner]
  (om/set-state! owner :link-posting true)
  (api/share-stakeholder-update))

(defn stakeholder-update-data [data]
  (:stakeholder-update (dis/company-data data)))

(defn patch-stakeholder-update [owner]
  (let [title  (om/get-state owner :title)
        topics (om/get-state owner :su-topics)]
    (api/patch-stakeholder-update {:title (or title "")
                                   :sections topics})))

(defn topic-click [owner topic selected-metric]
  (println "topic-click"))

(defn switch-topic [owner is-left?]
  (when (and (om/get-state owner :topic-navigation)
             (om/get-state owner :selected-topic)
             (nil? (om/get-state owner :tr-selected-topic)))
    (let [selected-topic (om/get-state owner :selected-topic)
          company-data   (dis/company-data (om/get-props owner))
          topics         (:sections (:stakeholder-update company-data))
          current-idx    (.indexOf (vec topics) selected-topic)]
      (if is-left?
        ;prev
        (let [prev-idx (mod (dec current-idx) (count topics))
              prev-topic (get (vec topics) prev-idx)]
          (om/set-state! owner :tr-selected-topic prev-topic))
        ;next
        (let [next-idx (mod (inc current-idx) (count topics))
              next-topic (get (vec topics) next-idx)]
          (om/set-state! owner :tr-selected-topic next-topic))))))

(defn kb-listener [owner e]
  (let [key-code (.-keyCode e)]
    (when (= key-code 39)
      ;next
      (switch-topic owner false))
    (when (= key-code 37)
      (switch-topic owner true))))

(defn focus-title [owner]
  (when-not (om/get-state owner :title-focused)
    (when-let [preview-title (.findDOMNode js/ReactDOM (om/get-ref owner "preview-title"))]
      (.focus preview-title)
      (set! (.-value preview-title) (.-value preview-title))
      (om/set-state! owner :title-focused true))))

(defn share-slack-clicked [owner]
  (om/set-state! owner :slack-loading true)
  (om/set-state! owner :show-su-dialog true))

(defn share-link-clicked [owner]
 (om/set-state! owner :link-loading true)
 (patch-stakeholder-update owner))

(defn dismiss-su-preview [owner]
  (om/set-state! owner (merge (om/get-state owner) {:show-su-dialog false
                                                    :slack-loading false
                                                    :link-loading false
                                                    :link-posting false
                                                    :link-posted false})))

(defn setup-sortable [owner options]
  (when (and (om/get-state owner :did-mount)
             (om/get-state owner :sortable-loaded))
    (when-let [list-node (sel1 [:div.topics-column])]
      (.create js/Sortable list-node #js {:handle ".topic-internal"
                                          :onStart #(dommy/add-class! list-node :dragging)
                                          :onEnd (fn [_]
                                                    (let [topic-nodes (sel [:div.topic-row])
                                                          topics      (vec (for [t topic-nodes] (.-topic (.-dataset t))))]
                                                      (om/set-state! owner :su-topics topics))
                                                    (dommy/remove-class! list-node :dragging))}))))

(defn add-su-section [owner topic]
  (om/update-state! owner :su-topics #(conj % topic)))

(defn top-position [el]
  (loop [yPos 0
         element el]
    (if element
      (recur (+ yPos (.-offsetTop element))
             (.-offsetParent element))
      yPos)))

(defn mouse-moved [owner e]
  (let [y-pos (+ (.-clientY e) (.-scrollTop (.-body js/document)))
        all-topics (sel [:div.topic-row])
        hover-topic (loop [idx 0
                           topic (get all-topics idx)
                           last-topic nil]
                      ; (println "   - idx:" idx)
                      ; (println "   - topic:" (.-topic (.-dataset topic)) "-" (top-position topic))
                      ; (println "   - last-topic:" last-topic)
                      ; (println "   " (.-topic (.-dataset topic)) ": " y-pos "<" (top-position topic) "=" (< y-pos (top-position topic)))
                      (if (< y-pos (top-position topic))
                        last-topic
                        (if (>= (inc idx) (count all-topics))
                          nil
                          (recur (inc idx)
                                 (get all-topics (inc idx))
                                 topic))))]

    (println "hide all:" (count (sel [:div.share-remove-container])))
    (for [topic (sel [:div.share-remove-container])]
      (dommy/add-class! topic :hidden))
    (when hover-topic
      (println "show for:" (.-topic (.-dataset hover-topic)) "=>" (sel1 [(str "div#share-remove-" (.-topic (.-dataset hover-topic)))]))
      (dommy/remove-class! (sel1 [(str "div#share-remove-" (.-topic (.-dataset hover-topic)))]) :hidden)
      ; (om/set-state! owner :show-share-remove (.-topic (.-dataset hover-topic)))
      )))

(defcomponent su-snapshot-preview [data owner options]

  (init-state [_]
    (utils/add-channel "fullscreen-topic-save" (chan))
    (utils/add-channel "fullscreen-topic-cancel" (chan))
    (cdr/add-script! "/lib/Sortable.js/Sortable.js"
                     (fn []
                       (om/set-state! owner :sortable-loaded true)
                       (setup-sortable owner options)))
    (let [su-data (stakeholder-update-data data)]
      {:columns-num (responsive/columns-num)
       :su-topics (:sections su-data)
       :title-focused false
       :title (if (clojure.string/blank? (:title su-data))  (utils/su-default-title) (:title su-data))
       :show-su-dialog false
       :link-loading false
       :slack-loading false
       :link-posting false
       :link-posted false}))

  (did-mount [_]
    (om/set-state! owner :did-mount true)
    (setup-sortable owner options)
    (events/listen js/window EventType/RESIZE #(om/set-state! owner :columns-num (responsive/columns-num)))
    (focus-title owner)
    (when-not (utils/is-test-env?)
      (let [kb-listener (events/listen js/window EventType/KEYDOWN (partial kb-listener owner))
            swipe-listener (js/Hammer (sel1 [:div#app]))];(.-body js/document))]
        (om/set-state! owner :kb-listener kb-listener)
        (om/set-state! owner :swipe-listener swipe-listener)
        (.on swipe-listener "swipeleft" (fn [e] (switch-topic owner true)))
        (.on swipe-listener "swiperight" (fn [e] (switch-topic owner false))))
      (let [mouse-move-listener (events/listen js/window EventType/MOUSEMOVE (partial mouse-moved owner))]
        )))

  (will-unmount [_]
    (utils/remove-channel "fullscreen-topic-save")
    (utils/remove-channel "fullscreen-topic-cancel")
    (when-not (utils/is-test-env?)
      (events/unlistenByKey (om/get-state owner :kb-listener))
      (let [swipe-listener (om/get-state owner :swipe-listener)]
        (.off swipe-listener "swipeleft")
        (.off swipe-listener "swiperight"))))

  (will-receive-props [_ next-props]
    (when-not (= (dis/company-data data) (dis/company-data next-props))
      (om/set-state! owner :su-topics (:sections (stakeholder-update-data next-props))))
    ; share via link
    (when (om/get-state owner :link-loading)
      (if-not (om/get-state owner :link-posting)
        (post-stakeholder-update owner)
        ; show share url dialog
        (when (not= (dis/latest-stakeholder-update data) (dis/latest-stakeholder-update next-props))
          (om/set-state! owner :link-loading false)
          (om/set-state! owner :link-posted true)
          (om/set-state! owner :show-su-dialog true)))))

  (did-update [_ _ _]
    (focus-title owner)
    (setup-sortable owner options))

  (render-state [_ {:keys [columns-num
                           title-focused
                           title
                           show-su-dialog
                           link-loading
                           slack-loading
                           link-posting
                           link-posted
                           su-topics]}]
    (let [company-data (dis/company-data data)
          su-data      (stakeholder-update-data data)
          card-width   (responsive/calc-card-width)
          ww           (.-clientWidth (sel1 js/document :body))
          total-width  (if (> ww 413) (str card-width "px") "auto")
          su-subtitle  (str "— " (utils/date-string (js/Date.) true))
          topics-to-add (reduce utils/vec-dissoc (flatten (vals (:sections company-data))) su-topics)]
      (dom/div {:class (utils/class-set {:su-snapshot-preview true
                                         :main-scroll true})}
        (om/build menu data)
        (dom/div {:class "page snapshot-page"}
          (dom/div {:class "su-snapshot-header"}
            (om/build back-to-dashboard-btn {})
            (dom/div {:class "share-su"}
              (dom/label {} "SHARE TO")
              (dom/button {:class "share-su-button btn-reset share-slack"}
                (dom/img {:src "/img/Slack_Icon.png"}))
              (dom/button {:class "share-su-button btn-reset share-mail"}
                (i/icon :email-84 {:color "rgba(78,90,107,0.6)" :accent-color "rgba(78,90,107,0.6)" :size 20}))
              (dom/button {:class "share-su-button btn-reset share-link"}
                (i/icon :link-72 {:color "rgba(78,90,107,0.6)" :accent-color "rgba(78,90,107,0.6)" :size 20}))))
          ;; SU Snapshot Preview
          (when company-data
            (dom/div {:class "su-sp-content"}
              (dom/div {:class "su-sp-company-header"}
                (dom/img {:class "company-logo" :src (:logo company-data)})
                (dom/span {:class "company-name"} (:name company-data)))
              (when (:title su-data)
                (dom/div {:class "preview-title-container"}
                  (dom/input #js {:className "preview-title"
                                  :id "su-snapshot-preview-title"
                                  :type "text"
                                  :value title
                                  :ref "preview-title"
                                  :onChange #(om/set-state! owner :title (.. % -target -value))
                                  :style #js {:width total-width}})))
              (dom/div {:class "preview-subtitle"} su-subtitle)
              (when show-su-dialog
                (om/build su-preview-dialog {:selected-topics (:sections su-data)
                                             :company-data company-data
                                             :latest-su (dis/latest-stakeholder-update)
                                             :share-via-slack slack-loading
                                             :share-via-link (or link-loading link-posted)
                                             :su-title title}
                                            {:opts {:dismiss-su-preview #(dismiss-su-preview owner)}}))
              (om/build topics-columns {:columns-num 1
                                        :card-width card-width
                                        :total-width total-width
                                        :content-loaded (not (:loading data))
                                        :topics su-topics
                                        :company-data company-data
                                        :show-share-remove true
                                        :hide-add-topic true}
                                       {:opts {:topic-click (partial topic-click owner)
                                               :share-remove-click (fn [topic] (om/update-state! owner :su-topics #(utils/vec-dissoc % topic)))}})))
          ;; Add section container
          (when (pos? (count topics-to-add))
            (dom/div {:class "su-preview-add-section-container"}
              (dom/div {:class "su-preview-add-section"
                        :style #js {:width total-width}}
                (dom/div {:class "add-header"} "ADD TOPICS")
                (for [topic topics-to-add]
                  (dom/div {:class "add-section"
                            :on-click #(add-su-section owner topic)}
                    (i/icon :check-square-09 {:accent-color "transparent" :size 16 :color "black"})
                    (dom/div {:class "section-name"} (name topic))))))))))))