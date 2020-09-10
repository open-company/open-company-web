(ns oc.web.components.ui.image-modal
  (:require [rum.core :as rum]
            [oc.web.dispatcher :as dis]))

(rum/defc image-modal
  [{:keys [src]}]
  (when src
    (let [close-cb #(dis/dispatch! [:input [:expand-image-src] nil])]
      [:div.image-modal-container
        {:on-click close-cb}
        [:span.image-modal-close
          {:on-click close-cb}]
        [:img.image-modal-content
          {:src src}]
        ])))