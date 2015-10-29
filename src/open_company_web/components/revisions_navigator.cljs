(ns open-company-web.components.revisions-navigator
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.lib.utils :as utils]
            [open-company-web.router :as router]
            [open-company-web.api :as api]))

(defn revision-next
  "Return the first future revision"
  [revisions as-of]
  (if (> (count revisions) 0)
    (loop [idx (dec (count revisions))
           next-rev nil]
      (let [rev (get revisions idx)]
        (if (<= (compare (:updated-at rev) as-of) 0)
          next-rev
          (if (= idx 0)
            rev
            (recur (dec idx)
                   rev)))))
    nil))

(defn revision-last [revisions as-of]
  (let [last-revision (last revisions)]
    (if (and
          last-revision
          (not= last-revision (revision-next revisions as-of)))
      last-revision
      nil)))

(defn revision-prev
  "Return the first future revision"
  [revisions as-of]
  (if (> (count revisions) 0)
    (loop [idx 0
           prev-rev nil]
      (let [rev (get revisions idx)]
        (if (>= (compare (:updated-at rev) as-of) 0)
          prev-rev
          (if (= idx (dec (count revisions)))
            rev ;return the last possible value as it's past
            (recur (inc idx)
                   rev)))))
      nil))

(defn revision-first [revisions as-of]
  (let [rev (first revisions)]
    (if (and rev
             (< (compare (:updated-at rev) as-of) 0)
             (not= rev (revision-prev revisions as-of)))
      rev
      false)))

(defn nav-revision! [e rev owner data read-only]
  (.preventDefault e)
  (let [section (:section data)]
    ((:navigate-cb data) read-only)
    (utils/handle-change (:section-data data) (:updated-at rev) :as-of)
    (api/load-revision rev (keyword (:slug @router/path)) section read-only)))

(defn date-string [rev]
  (let [date (new js/Date (:updated-at rev))
        month (utils/month-string (utils/add-zero (inc (.getMonth date))))
        day (utils/add-zero (.getDate date))]
    (str month " " day)))

(defcomponent revisions-navigator [data owner]
  (render [_]
    (let [section-data (:section-data data)
          revisions (utils/sort-revisions (:revisions section-data))
          last-revision (last revisions)
          as-of (:as-of section-data)
          rev-first (revision-first revisions as-of)
          rev-prev  (revision-prev revisions as-of)
          rev-next  (revision-next revisions as-of)
          rev-last  (if rev-next (revision-last revisions as-of) nil)
          section (:section data)
          latest? (= (:updated-at last-revision) as-of)
          rev-first (if latest? nil rev-first)
          first-date (date-string rev-first)
          prev-date (date-string rev-prev)
          next-date (date-string rev-next)
          last-date (date-string rev-last)]
      (dom/div {:class "revisions-navigator"}
        (if (:loading data)
          (dom/div {:style {:text-align "center"}} "Loading...")
          (dom/div {}
            (dom/div {:class "revisions-navigator-left"}
              (when rev-first
                (dom/a {:class "rev-double-prev"
                        :on-click #(nav-revision! % rev-first owner data true)
                        :title first-date}
                  (dom/div {:class "double-prev"}
                    (dom/i {:class "fa fa-backward"}))))
              (when rev-prev
                (dom/a {:class "rev-single-prev"
                        :on-click #(nav-revision! % rev-prev owner data true)
                        :title prev-date}
                  (dom/div {:class "single-prev"}
                    (dom/i {:class "fa fa-caret-left"})
                    (dom/p {:class "rev-nav-label"} "Previous")))))
            (dom/div {:class "revisions-navigator-right"}
              (when rev-last
                (dom/a {:class "rev-double-next"
                        :on-click #(nav-revision! % rev-last owner data false)
                        :title last-date}
                  (dom/div {:class "double-next"}
                    (dom/i {:class "fa fa-forward"}))))
              (when rev-next
                (dom/a {:class "rev-single-next"
                        :on-click #(nav-revision! %
                                                  rev-next
                                                  owner
                                                  data
                                                  (not= (:updated-at rev-next) (:updated-at last-revision)))
                        :title next-date}
                  (dom/div {:class "single-next"}
                    (dom/i {:class "fa fa-caret-right"})
                    (dom/p {:class "rev-nav-label"} "Next")))))))))))
