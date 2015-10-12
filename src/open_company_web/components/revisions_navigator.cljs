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
  (let [section (:section data)
        notes (:notes data)]
    ((:navigate-cb data) read-only)
    (om/update-state! owner :as-of (fn [_] (:updated-at rev)))
    (api/load-revision rev (keyword (:slug @router/path)) section read-only notes)))

(defn date-string [rev]
  (str (:name (:author rev)) " about " (utils/time-since (:updated-at rev))))

(defcomponent revisions-navigator [data owner]
  (init-state [_]
    (let [revisions (:revisions data)
          sorted-revisions (utils/sort-revisions revisions)
          last-revision (last sorted-revisions)]
      {:as-of (:updated-at data)}))
  (render [_]
    (let [revisions (utils/sort-revisions (:revisions data))
          last-revision (last revisions)
          as-of (om/get-state owner :as-of)
          rev-first (revision-first revisions as-of)
          rev-prev  (revision-prev revisions as-of)
          rev-next  (revision-next revisions as-of)
          rev-last  (if rev-next (revision-last revisions as-of) nil)
          section (:section data)
          latest? (= (:updated-at last-revision) as-of)
          rev-first (if latest? nil rev-first)]
      (dom/div {:class "revisions-navigator"}
        (if (:loading data)
          (dom/div {:style {:text-align "center"}} "Loading...")
          (dom/div {}
            (dom/div {:class "revisions-navigator-left"}
              (when rev-first
                (dom/a {:title (date-string rev-first)
                        :on-click #(nav-revision! % rev-first owner data true)}
                  (dom/div {:class "double-prev"}
                    (dom/i {:class "fa fa-backward"}))))
              (when rev-prev
                (dom/a {:title (date-string rev-prev)
                        :on-click #(nav-revision! % rev-prev owner data true)}
                  (dom/div {:class "single-prev"}
                    (dom/i {:class "fa fa-caret-left"})))))
            (dom/div {:class "revisions-navigator-right"}
              (when rev-last
                (dom/a {:title (date-string rev-last)
                        :on-click #(nav-revision! % rev-last owner data false)}
                  (dom/div {:class "double-next"}
                    (dom/i {:class "fa fa-forward"}))))
              (when rev-next
                (dom/a {:title (date-string rev-next)
                        :on-click #(nav-revision! % rev-next owner data (not= (:updated-at rev-next) (:updated-at last-revision)))}
                  (dom/div {:class "single-next"}
                    (dom/i {:class "fa fa-caret-right"})))))))))))