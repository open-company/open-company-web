(ns oc.web.components.ui.wst
  (:require [rum.core :as rum]
            [cuerdas.core :as string]
            [oc.web.lib.utils :as utils]
            [oc.web.components.ui.user-avatar :refer (user-avatar-image)]))

(defn filter-by-query [user query]
  (or (string/includes? (string/lower (:email user)) query)
      (string/includes? (string/lower (:name user)) query)
      (string/includes? (string/lower (:first-name user)) query)
      (string/includes? (string/lower (:last-name user)) query)))

(rum/defcs wst < (rum/local "" ::query)
                 (rum/local false ::showing-popup)
  [s wst-list]
  (let [seen-users (filter #(:seen %) wst-list)
        seen-count (count seen-users)
        query (::query s)
        lower-query (string/lower @query)
        filtered-users (if (seq @query)
                         (case lower-query
                           "seen" seen-users
                           "unseen" (filter #(not (:seen %)) wst-list)
                           (filterv #(filter-by-query % (string/lower @query)) wst-list))
                         wst-list)]
    [:div.wst-container
      {:on-mouse-enter #(reset! (::showing-popup s) true)
       :on-mouse-leave #(reset! (::showing-popup s) false)}
      [:div.wst-count
        (str seen-count " view" (when (> seen-count 1) "s"))]
      [:div.wst-popup
        {:class (when-not @(::showing-popup s) "hidden")}
        [:div.wst-popup-title
          "Who read this post"]
        [:input.wst-popup-query
          {:value @query
           :type "text"
           :placeholder "Find a person or seen/unseen..."
           :on-change #(reset! query (.. % -target -value))}]
        [:div.wst-popup-list
          (for [u filtered-users]
            [:div.wst-popup-list-row
              {:key (str "wst-popup-row-" (:user-id u))}
              [:div.wst-popup-list-row-avatar
                {:class (when (:seen u) "seen")}
                (user-avatar-image u)]
              [:div.wst-popup-list-row-name
                (utils/name-or-email u)]
              [:div.wst-popup-list-row-seen
                {:class (when (:seen u) "seen")}
                (if (:seen u)
                  "Viewed"
                  "Not seen")]])]]]))