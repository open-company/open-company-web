(ns open-company-web.lib.utils
    (:require [om.core :as om :include-macros true]
              [clojure.string]
              [open-company-web.lib.iso4217 :refer [iso4217]]
              [cljs.core.async :refer [put!]]))

(defn abs [n] (max n (- n)))

(defn thousands-separator [number]
  (let [parts (clojure.string/split (str number) "." 1)
        int-part (first parts)
        dec-part (get parts 1)
        integer-string (clojure.string/replace int-part #"\B(?=(\d{3})+(?!\d))" ",")]
    (if-not (= dec-part nil)
      (str integer-string "." dec-part)
      integer-string)))

(defn thousands-separator-strip [number]
  (let [num-str (str number)]
    (clojure.string/replace num-str "," "")))

(defn String->Number [str]
  (let [n (js/parseFloat str)]
    (if (js/isNaN n) 0 n)))

(defn display [show]
  (if show
    #js {}
    #js {:display "none"}))

(defn get-currency [currency-code]
  (let [kw (keyword currency-code)]
    (get iso4217 kw)))

(defn get-symbol-for-currency-code [currency-code]
  (let [currency (get-currency currency-code)
        symbol (if
                (and
                  (contains? currency :symbol)
                  (> (count (:symbol currency)) 0))
                (:symbol currency)
                currency-code)
        ret (or symbol (:code currency))]
  ret))

(def channel-coll (atom {}))

(defn add-channel [channel-name channel]
  (swap! channel-coll assoc channel-name channel))

(defn get-channel [channel-name]
  (@channel-coll channel-name))

(defn handle-change [cursor value key]
  (if (array? key)
    (om/transact! cursor assoc-in key (fn [_] value))
    (om/transact! cursor key (fn [_] value))))

(defn change-value [cursor e key]
  (handle-change cursor (.. e -target -value) key))

(defn save-values [channel-name]
  (let [save-channel (get-channel channel-name)]
    (put! save-channel 1)))

(defn in?
  "true if seq contains elm"
  [seq elm]
  (some #(= elm %) seq))

(defn get-period-string [period]
  (case period
    "M1" "January"
    "M2" "February"
    "M3" "March"
    "M4" "April"
    "M5" "May"
    "M6" "June"
    "M7" "July"
    "M8" "August"
    "M9" "September"
    "M10" "October"
    "M11" "November"
    "M12" "December"

    "Q1" "January - March"
    "Q2" "April - June"
    "Q3" "July - September"
    "Q4" "October - December"

    ""))

(defn get-periods [prefix n]
 (let [r (range 1 (+ n 1))]
    (into [] (for [a r] (str prefix a)))))

(def users [
  { :tz_label "Pacific Daylight Time"
    :deleted false
    :is_bot false
    :color "e7392d"
    :real_name "Iacopo Carraro"
    :name "iacopo"
    :has_files true
    :tz nil
    :is_primary_owner false
    :is_restricted false
    :is_admin false
    :is_ultra_restricted false
    :status nil
    :id "U06STCKLN"
    :is_owner false
    :has_2fa false
    :profile {
      :email "iacopo@opencompany.io"
      :first_name "Iacopo"
      :real_name_normalized "Iacopo Carraro"
      :image_48 "https://secure.gravatar.com/avatar/98b5456ea1c562024f41501ffd7bc3c6.jpg?s=48&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0022-48.png"
      :image_192 "https://secure.gravatar.com/avatar/98b5456ea1c562024f41501ffd7bc3c6.jpg?s=192&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0022.png"
      :real_name "Iacopo Carraro"
      :image_72 "https://secure.gravatar.com/avatar/98b5456ea1c562024f41501ffd7bc3c6.jpg?s=72&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0022-72.png"
      :image_24 "https://secure.gravatar.com/avatar/98b5456ea1c562024f41501ffd7bc3c6.jpg?s=24&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0022-24.png"
      :image_32 "https://secure.gravatar.com/avatar/98b5456ea1c562024f41501ffd7bc3c6.jpg?s=32&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0022-32.png"
      :last_name "Carraro"}
    :tz_offset -25200}
  { :tz_label "Pacific Daylight Time"
    :deleted false
    :is_bot false
    :color "3c989f"
    :real_name "Scott Johnson"
    :name "scott"
    :has_files false
    :tz nil
    :is_primary_owner false
    :is_restricted false
    :is_admin false
    :is_ultra_restricted false
    :status nil
    :id "U06SU26MD"
    :is_owner false
    :profile {
      :email "scott@opencompany.io"
      :first_name "Scott"
      :real_name_normalized "Scott Johnson"
      :image_48 "https://secure.gravatar.com/avatar/dd620d96970e0f7565cb4c12044eb7f9.jpg?s=48&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0018-48.png"
      :image_192 "https://secure.gravatar.com/avatar/dd620d96970e0f7565cb4c12044eb7f9.jpg?s=192&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0018.png"
      :real_name "Scott Johnson"
      :image_72 "https://secure.gravatar.com/avatar/dd620d96970e0f7565cb4c12044eb7f9.jpg?s=72&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F3654%2Fimg%2Favatars%2Fava_0018-72.png"
      :image_24 "https://secure.gravatar.com/avatar/dd620d96970e0f7565cb4c12044eb7f9.jpg?s=24&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0018-24.png"
      :image_32 "https://secure.gravatar.com/avatar/dd620d96970e0f7565cb4c12044eb7f9.jpg?s=32&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0018-32.png"
      :last_name "Johnson"
    }
    :tz_offset -25200}
  { :tz_label "Pacific Daylight Time"
    :deleted false
    :is_bot false
    :color "9f69e7"
    :real_name ""
    :name "sean"
    :has_files true
    :tz nil
    :is_primary_owner true
    :is_restricted false
    :is_admin true
    :is_ultra_restricted false
    :status nil
    :id "U06SBTXJR"
    :is_owner true
    :profile {
      :real_name ""
      :real_name_normalized ""
      :email "sean@opencompany.io"
      :image_24 "https://secure.gravatar.com/avatar/f5b8fc1affa266c8072068f811f63e04.jpg?s=24&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0020-24.png"
      :image_32 "https://secure.gravatar.com/avatar/f5b8fc1affa266c8072068f811f63e04.jpg?s=32&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0020-32.png"
      :image_48 "https://secure.gravatar.com/avatar/f5b8fc1affa266c8072068f811f63e04.jpg?s=48&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0020-48.png"
      :image_72 "https://secure.gravatar.com/avatar/f5b8fc1affa266c8072068f811f63e04.jpg?s=72&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0020-72.png"
      :image_192 "https://secure.gravatar.com/avatar/f5b8fc1affa266c8072068f811f63e04.jpg?s=192&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F3654%2Fimg%2Favatars%2Fava_0020.png"
    }
    :tz_offset -25200}
  { :tz_label "Pacific Daylight Time"
    :deleted false
    :is_bot false
    :color "4bbe2e"
    :real_name "Stuart Levinson"
    :name "stuart"
    :has_files true
    :tz nil
    :is_primary_owner false
    :is_restricted false
    :is_admin false
    :is_ultra_restricted false
    :status nil
    :id "U06SQLDFT"
    :is_owner false
    :profile {
      :email "stuart@opencompany.io"
      :first_name "Stuart"
      :real_name_normalized "Stuart Levinson"
      :image_48 "https://secure.gravatar.com/avatar/6ef85399c45b7affe7fc8fb361a3366f.jpg?s=48&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0015-48.png"
      :image_192 "https://secure.gravatar.com/avatar/6ef85399c45b7affe7fc8fb361a3366f.jpg?s=192&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0015.png"
      :real_name "Stuart Levinson"
      :image_72 "https://secure.gravatar.com/avatar/6ef85399c45b7affe7fc8fb361a3366f.jpg?s=72&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0015-72.png"
      :image_24 "https://secure.gravatar.com/avatar/6ef85399c45b7affe7fc8fb361a3366f.jpg?s=24&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0015-24.png"
      :image_32 "https://secure.gravatar.com/avatar/6ef85399c45b7affe7fc8fb361a3366f.jpg?s=32&d=https%3A%2F%2Fslack.global.ssl.fastly.net%2F66f9%2Fimg%2Favatars%2Fava_0015-32.png"
      :last_name "Levinson"
    }
    :tz_offset -25200}
  { :tz_label "Pacific Daylight Time"
    :deleted false
    :is_bot true
    :color "674b1b"
    :real_name "Transparency Bot"
    :name "transparencybot"
    :has_files false
    :tz nil
    :is_primary_owner false
    :is_restricted false
    :is_admin false
    :is_ultra_restricted false
    :status nil
    :id "U09GS25L4"
    :is_owner false
    :profile {
      :image_512 "https://avatars.slack-edge.com/2015-08-24/9570128512_e722b04e32ec23b1b6ec_192.jpg"
      :first_name "Transparency"
      :real_name_normalized "Transparency Bot"
      :image_48 "https://avatars.slack-edge.com/2015-08-24/9570128512_e722b04e32ec23b1b6ec_48.jpg"
      :image_192 "https://avatars.slack-edge.com/2015-08-24/9570128512_e722b04e32ec23b1b6ec_192.jpg"
      :real_name "Transparency Bot"
      :image_original "https://avatars.slack-edge.com/2015-08-24/9570128512_e722b04e32ec23b1b6ec_original.jpg"
      :image_72 "https://avatars.slack-edge.com/2015-08-24/9570128512_e722b04e32ec23b1b6ec_72.jpg"
      :image_24 "https://avatars.slack-edge.com/2015-08-24/9570128512_e722b04e32ec23b1b6ec_24.jpg"
      :title "Helps open your company"
      :image_32 "https://avatars.slack-edge.com/2015-08-24/9570128512_e722b04e32ec23b1b6ec_32.jpg"
      :last_name "Bot"
      :image_1024 "https://avatars.slack-edge.com/2015-08-24/9570128512_e722b04e32ec23b1b6ec_192.jpg"
      :bot_id "B09GS6LSW"
    }
    :tz_offset -25200
  }
])