(ns oc.web.components.ui.wrt
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn filter-by-query [user query]
  (or (string/includes? (string/lower (:email user)) query)
      (string/includes? (string/lower (:name user)) query)
      (string/includes? (string/lower (:first-name user)) query)
      (string/includes? (string/lower (:last-name user)) query)))

(rum/defcs wrt < (rum/local "" ::query)
                 (rum/local false ::showing-popup)
  [s read-count wrt-list]
  (let [seen-users (filter #(:seen %) wrt-list)
        read-count (:count read-count)
        query (::query s)
        lower-query (string/lower @query)
        filtered-users (if (seq @query)
                         (case lower-query
                           "seen" seen-users
                           "unseen" (filter #(not (:seen %)) wrt-list)
                           (filterv #(filter-by-query % (string/lower @query)) wrt-list))
                         wrt-list)]
    [:div.wrt-container
      {:on-mouse-enter #(reset! (::showing-popup s) true)
       :on-mouse-leave #(reset! (::showing-popup s) false)}
      [:div.wrt-count
        (str read-count " view" (when (> read-count 1) "s"))]
      [:div.wrt-popup
        {:class (when-not @(::showing-popup s) "hidden")}
        [:div.wrt-popup-title
          "Who read this post"]
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
                  "Not seen")]])]]]))