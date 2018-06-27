(ns oc.web.components.ui.wrt
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [oc.web.lib.utils :as utils]
            [oc.web.utils.section :as su]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn filter-by-query [user query]
  (or (string/includes? (string/lower (:email user)) query)
      (string/includes? (string/lower (:name user)) query)
      (string/includes? (string/lower (:first-name user)) query)
      (string/includes? (string/lower (:last-name user)) query)))

(defn under-middle-screen? [el]
  (let [el-offset-top (aget (.offset (js/$ el)) "top")
        fixed-top-position (- el-offset-top (.-scrollTop (.-scrollingElement js/document)))
        win-height (.-innerHeight js/window)]
    (>= fixed-top-position (/ win-height 2))))

(rum/defcs wrt < (rum/local "" ::query)
                 (rum/local false ::showing-popup)
                 (rum/local false ::under-middle-screen)
  [s item-id read-data team-users]
  (let [seen-users (into [] (sort-by utils/name-or-email (map #(assoc % :seen true) (:reads read-data))))
        seen-ids (set (map :user-id seen-users))
        all-ids (set (map :user-id team-users))
        unseen-ids (clojure.set/difference all-ids seen-ids)
        unseen-users (into [] (sort-by utils/name-or-email (map (fn [user-id] (first (filter #(= (:user-id %) user-id) team-users))) unseen-ids)))
        all-users (sort-by utils/name-or-email (concat seen-users unseen-users))
        read-count (:count read-data)
        query (::query s)
        lower-query (string/lower @query)
        filtered-users (if (seq @query)
                         (case lower-query
                           "seen" seen-users
                           "unseen" (filter #(not (:seen %)) unseen-users)
                           (filterv #(filter-by-query % (string/lower @query)) all-users))
                         all-users)]
    [:div.wrt-container
      {:class (when seen-users "has-read-list")
       :on-mouse-over #(when-not (:reads read-data)
                        (su/request-reads-data item-id))
       :on-mouse-enter #(do
                          (reset! (::showing-popup s) true)
                          (reset! (::under-middle-screen s) (under-middle-screen? (rum/ref-node s :wrt-count))))
       :on-mouse-leave #(reset! (::showing-popup s) false)}
      [:div.wrt-count
        {:ref :wrt-count}
        (if read-count
          (str read-count " view" (when (not= read-count 1) "s"))
          "Views")]
      (when (and @(::showing-popup s)
                 (:reads read-data))
        [:div.wrt-popup
          {:class (when @(::under-middle-screen s) "top")}
          [:div.wrt-popup-title
            "Who saw this"]
          [:input.wrt-popup-query
            {:value @query
             :type "text"
             :placeholder "Find a person or seen/unseen..."
             :on-change #(reset! query (.. % -target -value))}]
          [:div.wrt-popup-list
            (for [u filtered-users]
              [:div.wrt-popup-list-row
                {:key (str "wrt-popup-row-" (:user-id u))}
                [:div.wrt-popup-list-row-avatar
                  {:class (when (:seen u) "seen")}
                  (user-avatar-image u)]
                [:div.wrt-popup-list-row-name
                  (utils/name-or-email u)]
                [:div.wrt-popup-list-row-seen
                  {:class (when (:seen u) "seen")}
                  (if (:seen u)
                    "Viewed"
                    "Not seen")]])]])]))