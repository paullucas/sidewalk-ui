(ns sidewalk-ui.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [sidewalk-ui.events]
              [sidewalk-ui.subs]
              [sidewalk-ui.views :as views]
              [sidewalk-ui.config :as config]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root))
