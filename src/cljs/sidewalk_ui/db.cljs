(ns sidewalk-ui.db)

(def default-db
  {:cursor-mode "hover"
   :active-color {:r 127 :g 127 :b 127}
   :pixel-vec (into [] (for [r (range 0 806)]
                         {:r 255
                          :g 255
                          :b 255
                          :key r}))})
