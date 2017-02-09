(ns sidewalk-ui.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :pixel-vec
 (fn [db _]
   (:pixel-vec db)))

(re-frame/reg-sub
 :active-color
 (fn [db _]
   (:active-color db)))
