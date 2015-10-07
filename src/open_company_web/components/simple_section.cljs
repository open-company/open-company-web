(ns open-company-web.components.simple-section
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [open-company-web.components.link :refer [link]]
            [open-company-web.router :as router]
            [open-company-web.components.update-footer :refer (update-footer)]
            [open-company-web.components.rich-editor :refer (rich-editor)]
            [open-company-web.components.editable-title :refer (editable-title)]
            [open-company-web.api :refer [save-or-create-section]]
            [open-company-web.lib.utils :as utils]
            [open-company-web.components.revisions-navigator :refer [revisions-navigator]]
            [cljs.core.async :refer [put! chan <!]]
            [open-company-web.dispatcher :as dispatcher]))

(defcomponent simple-section [data owner]
  (init-state [_]
    (let [save-channel (chan)
          section (:section data)]
      (utils/add-channel (str "save-section-" section) save-channel))
    {})
  (will-mount [_]
    (let [save-change (utils/get-channel (str "save-section-" (:section data)))]
        (go (loop []
          (let [change (<! save-change)
                cursor @dispatcher/app-state
                slug (:slug @router/path)
                company-data ((keyword slug) cursor)
                section (:section data)
                section-data (section company-data)]
            (save-or-create-section section-data)
            (recur))))))
  (render [_]
    (let [company-data (:company-data data)
          section (:section data)
          section-data (section company-data)
          read-only (:read-only data)]
      (if (:loading company-data)
        (dom/h4 {} "Loading data...")
        (dom/div {:class "simple-section section"}
          (om/build editable-title {:section-data section-data
                                    :section section
                                    :read-only read-only
                                    :save-channel (str "save-section-" section)})
          (dom/div {:class "simple-section-body"}
            (om/build rich-editor {:read-only read-only
                                   :section-data section-data
                                   :section section
                                   :save-channel (str "save-section-" section)}))
          (om/build revisions-navigator {:revisions (:revisions section-data)
                                         :actual-revision (:href (utils/link-for (:links section-data) "self" "GET"))}))))))