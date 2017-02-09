(ns sidewalk-ui.views
    (:require [re-frame.core :as re-frame]))

(defn rand-btn []
  [:div.col-md-12.randPixelBtnContainer
   [:span.fa.fa-2x.fa-random.randPixelBtn
    {:on-click #(re-frame/dispatch
                 [:set-pixel-vec (for [r (range 0 806)]
                                   {:r (rand-int 255)
                                    :g (rand-int 255)
                                    :b (rand-int 255)
                                    :key r})])}]])

(defn change-color [k]
  (let [c (re-frame/subscribe [:active-color])]
    (re-frame/dispatch
     [:set-pixel {:r (:r @c)
                  :g (:g @c)
                  :b (:b @c)
                  :key k}])))

(defn full-fill-btn []
  (let [c (re-frame/subscribe [:active-color])]
    [:div.col-md-12.fullFillBtnContainer
     [:span.fa.fa-2x.fa-arrows-alt.fullFillBtn
      {:on-click #(re-frame/dispatch
                   [:set-pixel-vec (for [r (range 0 806)]
                                     {:r (:r @c) 
                                      :g (:g @c)
                                      :b (:b @c)
                                      :key r})])}]]))

(defn parse-pixels [v]
  (for [p v]
    ^{:key p}
    [:div.pixelBox
     {:on-mouse-over #(change-color (:key p))
      :style {:backgroundColor
              (str
               "rgb(" (:r p) "," (:g p) "," (:b p) ")")}}]))

(defn sidewalk-grid []
  (let [pixel-vec (re-frame/subscribe [:pixel-vec])]
    [:div.pixelContainer
     (parse-pixels @pixel-vec)]))

(defn color-slider [n v]
  [:input.colorSlider
   {:type "range"
    :min 0
    :max 255
    :value (case n
             "r" (:r v)
             "g" (:g v)
             "b" (:b v))
    :on-change #(let [sv (.-value (.-target %))]
                  (re-frame/dispatch
                   [:set-active-color (case n
                                        "r" {:r sv
                                             :g (:g v)
                                             :b (:b v)}
                                        "g" {:r (:r v)
                                             :g sv
                                             :b (:b v)}
                                        "b" {:r (:r v)
                                             :g (:g v)
                                             :b sv})]))}])

(defn rand-color []
  [:div.col-md-12.randColorBtnContainer
   [:span.fa.fa-2x.fa-refresh.randColorBtn
    {:on-click #(re-frame/dispatch
                 [:set-active-color
                  {:r (rand-int 255)
                   :g (rand-int 255)
                   :b (rand-int 255)}])}]])

(defn color-preview [c]
  [:div.col-md-4.colorPreview
   {:style {:backgroundColor
            (str
             "rgb(" (:r c) "," (:g c) "," (:b c) ")")}}])

(defn color-picker []
  (let [c (re-frame/subscribe [:active-color])]
    [:div.colorPickerContainer.col-md-7
     [:div.col-md-4.sliderContainer
      [:div
       [color-slider "r" @c]
       [:h4 "R " (:r @c)]]
      [:div
       [color-slider "g" @c]
       [:h4 "G " (:g @c)]]
      [:div
       [color-slider "b" @c]
       [:h4 "B " (:b @c)]]]
     [color-preview @c]]))

(defn toolbar []
  [:div.toolbarContainer.col-md-12
    [color-picker]
    [:div.randBtnRow.col-md-2
     [:div.brushSectionContainer.col-md-6
      [:span.col-md-12.fa.fa-3x.fa-paint-brush.brushSectionIcon]
      [:hr.col-md-12.toolbarHr]
      [rand-color]]
     [:div.gridSectionContainer.col-md-6
      [:span.col-md-12.fa.fa-3x.fa-th.gridSectionIcon]
      [:hr.col-md-12.toolbarHr]
      [full-fill-btn]
      [rand-btn]]]])

(defn main-panel []
  [:div
   [:h1.mainHeader "Sidewalk"]
   [sidewalk-grid]
   [toolbar]])
