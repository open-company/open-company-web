(ns open-company-web.lib.react-utils)

(defn build [component props]
  (let [React (.-React js/window)]
    (.createElement React component (clj->js props))))