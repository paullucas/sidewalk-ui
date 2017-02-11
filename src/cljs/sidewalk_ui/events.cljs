(ns sidewalk-ui.events
    (:require [re-frame.core :as re-frame]
              [sidewalk-ui.db :as db]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
 :set-pixel-vec
 (fn [db [_ pixel-vec]]
   (assoc db :pixel-vec pixel-vec)))

(re-frame/reg-event-db
 :set-pixel
 (fn [db [_ pixel]]
   (assoc (:pixel-vec db) :key pixel)))

(re-frame/reg-event-db
 :set-active-color
 (fn [db [_ color]]
   (assoc db :active-color color)))
