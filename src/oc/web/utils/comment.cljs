(ns oc.web.utils.comment
  (:require [cljsjs.medium-editor]
            [goog.object :as gobj]
            [cuerdas.core :as string]
            [oc.web.lib.jwt :as jwt]
            [oc.web.lib.utils :as utils]))

(defn setup-medium-editor [comment-node]
  (let [config {:toolbar false
                :anchorPreview false
                :imageDragging false
                :extensions #js []
                :autoLink true
                :anchor false
                :targetBlank true
                :paste #js {:forcePlainText true}
                :placeholder #js {:text "Add a comment..."
                                  :hideOnClick true}
               :keyboardCommands #js {:commands #js [
                                  #js {
                                    :command false
                                    :key "B"
                                    :meta true
                                    :shift false
                                    :alt false
                                  }
                                  #js {
                                    :command false
                                    :key "I"
                                    :meta true
                                    :shift false
                                    :alt false
                                  }
                                  #js {
                                    :command false
                                    :key "U"
                                    :meta true
                                    :shift false
                                    :alt false
                                  }]}}]
    (new js/MediumEditor comment-node (clj->js config))))

(defn add-comment-content [add-comment-div]
  (let [inner-html (.-innerHTML add-comment-div)
        replace-br (.replace inner-html (js/RegExp. "<br[ ]{0,}/?>" "ig") "\n")
        cleaned-text (.replace replace-br (js/RegExp. "<div?[^>]+(>|$)" "ig") "")
        cleaned-text-1 (.replace cleaned-text (js/RegExp. "</div?[^>]+(>|$)" "ig") "\n")
        final-node (.html (js/$ "<div/>") cleaned-text-1)
        final-text (.trim (.text final-node))]
    (when (pos? (count final-text))
      (string/trim (.html final-node)))))

(defn- is-own-comment?
  [comment-data]
  (= (jwt/user-id) (:user-id (:author comment-data))))