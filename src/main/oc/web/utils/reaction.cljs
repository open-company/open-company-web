(ns oc.web.utils.reaction)

(defn can-pick-reaction?
  "Given an emoji and the list of the current reactions
   check if the user can react.
   A user can react if:
   - the reaction is NOT already in the reactions list
   - the reaction is already in the reactions list and its not reacted"
  [emoji reactions-data]
  (let [reaction-map (first (filter #(= (:reaction %) emoji) reactions-data))]
    (or (not reaction-map)
        (and (map? reaction-map)
             (not (:reacted reaction-map))))))