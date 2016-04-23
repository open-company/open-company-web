(ns open-company-web.components.ui.revisions-navigator
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros (defcomponent)]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [open-company-web.api :as api]
            [open-company-web.caches :as cache]))

(defn revision-next
  "Return the first future revision"
  [revisions as-of]
  (when (pos? (count revisions))
    (let [rev (first
                (remove
                  nil?
                  (map
                    (fn [r]
                      (when (= (:updated-at r) as-of)
                        (let [idx (.indexOf (to-array revisions) r)]
                          (get revisions (inc idx)))))
                    revisions)))]
      rev)))

(defn revision-last [revisions as-of]
  (let [last-revision (last revisions)]
    (when (and
            last-revision
            (not= last-revision (revision-next revisions as-of)))
      last-revision)))

(defn revision-prev
  "Return the first future revision"
  [revisions as-of]
  (when (pos? (count revisions))
    (let [rev (first
                (remove
                  nil?
                  (map
                    (fn [r]
                      (when (= (:updated-at r) as-of)
                        (let [idx (.indexOf (to-array revisions) r)]
                          (get revisions (dec idx)))))
                    revisions)))]
      rev)))

(defn revision-first [revisions as-of]
  (let [rev (first revisions)]
    (if (and rev
             (neg? (compare (:updated-at rev) as-of))
             (not= rev (revision-prev revisions as-of)))
      rev
      false)))

(defn nav-revision! [e rev navigate-cb]
  (.preventDefault e)
  (navigate-cb (:updated-at rev)))

(defcomponent revisions-navigator [data owner]
  (render [_]
    (let [section-data (:section-data data)
          revisions (utils/sort-revisions (:revisions section-data))
          last-revision (last revisions)
          as-of (:updated-at section-data)
          rev-first (revision-first revisions as-of)
          rev-prev  (revision-prev revisions as-of)
          rev-next  (revision-next revisions as-of)
          rev-last  (when rev-next (revision-last revisions as-of))
          section (:section data)
          latest? (= (:updated-at last-revision) as-of)
          rev-first (when-not latest? rev-first)
          first-date (utils/date-string (utils/js-date (:updated-at rev-first)))
          prev-date  (utils/date-string (utils/js-date (:updated-at rev-prev)))
          next-date  (utils/date-string (utils/js-date (:updated-at rev-next)))
          last-date  (utils/date-string (utils/js-date (:updated-at rev-last)))
          slug (keyword (router/current-company-slug))
          section (:section data)
          revisions-list (section (slug @cache/revisions))]
      ; preload previous revision
      (when (and rev-prev (not (contains? revisions-list (:updated-at rev-prev))))
        (api/load-revision rev-prev slug section))
      ; preload first revision
      (when (and rev-first (not (contains? revisions-list (:updated-at rev-first))))
        (api/load-revision rev-first slug section))
      ; preload next revision as it can be that it's missing (ie: user jumped to the first rev then went forward)
      (when (and (not= (:updated-at rev-next) (:actual-as-of data)) rev-next (not (contains? revisions-list (:updated-at rev-next))))
        (api/load-revision rev-next slug section))
      (when (or rev-first rev-prev rev-next rev-last)
        (dom/div {:class "revisions-navigator"}
          (if (:loading data)
            (dom/div {:style {:text-align "center"}} "Loading...")
            (dom/div {}
              (dom/div {:class "revisions-navigator-left"}
                (when rev-first
                  (dom/a {:class "rev-double-prev"
                          :on-click #(nav-revision! % rev-first (:revisions-navigation-cb data))
                          :title first-date}
                    (dom/div {:class "double-prev"}
                      (dom/i {:class "fa fa-backward"}))))
                (when rev-prev
                  (dom/a {:class "rev-single-prev"
                          :on-click #(nav-revision! % rev-prev (:revisions-navigation-cb data))
                          :title prev-date}
                    (dom/div {:class "single-prev"}
                      (dom/i {:class "fa fa-caret-left"})
                      (dom/p {:class "rev-nav-label"} "Previous")))))
              (dom/div {:class "revisions-navigator-right"}
                (when rev-last
                  (dom/a {:class "rev-double-next"
                          :on-click #(nav-revision! % rev-last (:revisions-navigation-cb data))
                          :title last-date}
                    (dom/div {:class "double-next"}
                      (dom/i {:class "fa fa-forward"}))))
                (when rev-next
                  (dom/a {:class "rev-single-next"
                          :on-click #(nav-revision! % rev-next (:revisions-navigation-cb data))
                          :title next-date}
                    (dom/div {:class "single-next"}
                      (dom/i {:class "fa fa-caret-right"})
                      (dom/p {:class "rev-nav-label"} "Next"))))))))))))