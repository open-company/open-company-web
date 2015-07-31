(ns open-company-web.components.sidebar
    (:require [om.core :as om :include-macros true]
              [om-tools.core :as om-core :refer-macros [defcomponent]]
              [om-tools.dom :as dom :include-macros true]
              [open-company-web.components.link :refer [link]]
              [open-company-web.router :as router]))

(defcomponent sidebar [data owner]
  (init-state [_]
    {:path @router/path})
  (render [_]
    (let [path (om/get-state owner :path)
          symbol (:symbol path)
          profile-url (str "/" symbol)
          organization-url (str "/" symbol "/organization")
          equity-url (str "/" symbol "/equity")
          agreements-url (str "/" symbol "/agreements")
          reports-url (str "/" symbol "/summary")
          is-profile (= (:active data) "profile")
          is-organization (= (:active data) "organization")
          is-equity (= (:active data) "equity")
          is-agreements (= (:active data) "agreements")
          is-report (= (:active data) "reports")]
      (dom/div {:class "col-mid-1 sidebar"}
        (dom/ul {:class "nav nav-sidebar"}
          ; profile
          (dom/li {:class (if is-profile "active" "")}
            (om/build link {:href profile-url :name "Profile"}))
          ; organization
          (dom/li {:class (if is-organization "active" "")}
            (om/build link {:href organization-url :name "Organization"}))
          ; equity
          (dom/li {:class (if is-equity "active" "")}
            (om/build link {:href equity-url :name "Equity"}))
          ; agreements
          (dom/li {:class (if is-agreements "active" "")}
            (om/build link {:href agreements-url :name "Agreements"}))
          ; reports
          (dom/li {:class (if is-report "active" "")}
            (om/build link {:href reports-url :name "Reports"})))))))
