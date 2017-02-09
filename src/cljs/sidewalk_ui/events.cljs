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
   (assoc db :pixel-vec
          (map (fn [p]
                 (if (= (:key p) (:key pixel))
                   pixel
                   p))
               (:pixel-vec db)))))

(re-frame/reg-event-db
 :set-active-color
 (fn [db [_ color]]
   (assoc db :active-color color)))
