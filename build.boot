(set-env!
  :source-paths   #{"site"}
  :resource-paths #{"resources"}
  :dependencies '[[hiccup "1.0.5" :scope "test"]
                  [perun "0.3.0" :scope "test"]
                  [pandeiro/boot-http "0.7.3" :scope "test"]
                  [compojure "1.5.0" :scope "test"]])

(require '[pandeiro.boot-http  :refer [serve]]
         '[io.perun :as p])

;; We use a bunch of edn files in `resources/pages` to declare a "page"
;; these edn files can hold additional information about the page such
;; as it's permalink identifier (`:page` key) or the page's title etc.

(defn page? [f]
  (and (.startsWith (:path f) "pages/")
       (.endsWith (:path f) ".edn")))

(defn page->permalink [f]
  (-> (read-string (slurp (:full-path f)))
      :page name (str ".html")))

(deftask build []
  (comp (p/base)
        (p/permalink :permalink-fn page->permalink
                     :filterer page?)
        (p/render :renderer 'oc.core/static-page
                  :filterer page?)
        ;; We're not actually rendering a collection here but
        ;; using the collection task is often a handy hack
        ;; to render pages which are "unique"
        (p/collection :renderer 'oc.core/app-shell
                      :filterer page?
                      :page "app-shell.html")))

(deftask dev []
  (comp (serve :handler 'oc.server/handler
               :port 3559)
        (watch)
        (build)))