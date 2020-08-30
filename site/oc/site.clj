(ns oc.site
  (:require [hiccup.core :as h]
            ;; [environ.core :as env]
            ))
  
(def doc-type "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">")

(defn- index []
  [:html {:xmlns "http://www.w3.org/1999/xhtml"}
   [:head
    [:meta {:http-equiv "Content-Type", :content "text/html; charset=utf-8"}]
    [:meta {:name "viewport", :content "width=device-width"}]
    [:title "Carrot"]]
   [:body
    [:div {:id "app"}]]])

(defn -main [& args]
  (println "DBG in -main" args)
  (let [idx (index)
        _ (println "DBG   idx:" idx)
        hic (h/html idx)
        _ (println "DBG   hic" hic)
        page (str doc-type hic)
        _ (println "DBG   page:" page)]
    page))