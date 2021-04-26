(ns oc.web.components.ui.image-modal
  (:require [rum.core :as rum]
            [oc.web.mixins.ui :as ui-mixins]
            [oc.web.dispatcher :as dis]
            [oc.web.utils.dom :as dom-utils]))

(defn dismiss-image-modal
  ([e]
   (dom-utils/stop-propagation! e)
   (dismiss-image-modal))
  ([]
   (dis/dispatch! [:input [:expand-image-src] nil])))

(rum/defc image-modal <
  rum/static
  (ui-mixins/on-key-press ["Escape"] #(dismiss-image-modal))
  [{:keys [src]}]
  (when src
    [:div.image-modal-container
      {:on-click dismiss-image-modal}
      [:span.image-modal-close
        {:on-click dismiss-image-modal}]
      [:img.image-modal-content
        {:src src}]
      ]))