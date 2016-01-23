(ns open-company-web.components.topic
  (:require [om.core :as om :include-macros true]
            [om-tools.core :as om-core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]))

(defcomponent topic [data owner]
  (render [_]
    (dom/div {:class "col-xs-12 topic"}
      (dom/div {:class "topic-title"} "Update")
      (dom/div {:class "topic-headline"} "October was an unusual month for us as a result of us moving from 7-day to 30- day trials.")
      (dom/div {:class "topic-date"} "November 20")
      )))

; <div class="container">
;     <div class="topic-list">
;         <div class="row">
;             <div class="col-xs-12 topic">
;                 <div class="topic-title">Update</div>
;                 <div class="topic-headline">October was an unusual month for us as a result of us moving from 7-day to 30- day trials.</div>
;                 <div class="topic-date">November 20</div>
;             </div>
;         </div>
;         <div class="row">
;             <div class="col-xs-12 topic">
;                 <div class="topic-title">Team and hiring</div>
;                 <div class="topic-headline">56 team members across the world in 40 cities.</div>
;                 <div class="topic-date">November 20</div>
;             </div>
;         </div>
;         <div class="row">
;             <div class="col-xs-12 topic">
;                 <div class="topic-title">Marketing</div>
;                 <div class="topic-headline">We shared how we have been losing social media traffic for quite some time.</div>
;                 <div class="topic-date">November 20</div>
;             </div>
;         </div>
;         <div class="row">
;             <div class="col-xs-12 topic">
;                 <div class="topic-title">Product</div>
;                 <div class="topic-headline">We launched Pablo 2.0! More than 500,000 Pablo images have been created so far.</div>
;                 <div class="topic-date">November 20</div>
;             </div>
;         </div>
;         <div class="row">
;             <div class="col-xs-12 topic">
;                 <div class="topic-title">Happiness</div>
;                 <div class="topic-headline">In October, the Happiness teams continued to focus on speed.</div>
;                 <div class="topic-date">November 20</div>
;             </div>
;         </div>
;         <div class="row">
;             <div class="col-xs-12 topic">
;                 <div class="topic-title">Asks</div>
;                 <div class="topic-headline">Spread the word on our new Social Media Calendar feature.</div>
;                 <div class="topic-date">November 20</div>
;             </div>
;         </div>
;     </div>
; </div>