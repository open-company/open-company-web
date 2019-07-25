(ns oc.web.components.ui.image-modal
  (:require [rum.core :as rum]))

(rum/defc image-modal
  [{:keys [src on-close]}]
  (when src
    [:div.image-modal-container
      {:on-click on-close}
      [:span.image-modal-close
        {:on-click on-close}]
      [:img.image-modal-content
        {:src src}]
      ]))
