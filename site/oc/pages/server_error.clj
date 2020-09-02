(ns oc.pages.server-error)

(defn server-error
  "Internal server error page block."
  [{:keys [contact-mail-to :contact-mail-to contact-email :contact-email]}]
  [:div.server-error
   [:div
    [:div.error-page
     [:h1 "500"]
     [:h2 "Internal Server Error"]
     [:p "We are sorry for the inconvenience."]
     [:p.last "Please try again later."]
     [:script {:src "/lib/set-path.js"}]]]])