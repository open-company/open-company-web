(ns oc.pages.not-found
  (:require [oc.shared :as shared]))

(defn not-found
  "Not found page block."
  [{:keys [contact-mail-to :contact-mail-to contact-email :contact-email]}]
  [:div.not-found
   [:div
    [:div.error-page.not-found-page
     [:img {:src (shared/cdn "/img/ML/carrot_404.svg") :width 338 :height 189}]
     [:h2 "Page Not Found"]
     [:p "The page may have been moved or removed,"]
     [:p.not-logged-in.last "or you may need to " [:a.login {:href "/login"} "login"] "."]
     [:p.logged-in.last "or you may not have access with this account."]
     [:script {:src "/lib/set-path.js"}]]]])