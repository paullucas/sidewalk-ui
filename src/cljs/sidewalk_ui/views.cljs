(ns sidewalk-ui.views
  (:require [re-frame.core :as re-frame]
            [cljs.core.async :refer [<! >!]]
            [websocket-client.core :refer [async-websocket]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn send-socket []
  (let [pixel-vec (re-frame/subscribe [:pixel-vec])
        url "ws://192.168.0.98:7890"
        aws (async-websocket url)
        packet (.from js/Uint8Array
                      (into [0]
                            (reduce
                             (fn [vec [pix]]
                               (conj vec pix))
                             (mapv
                              (fn [pix]
                                [(:g pix)
                                 (:r pix)
                                 (:b pix)])
                              @pixel-vec))))]
    (go (>! aws packet))
    (go (>! aws packet))
    (go (js/console.log (.stringify js/JSON (<! aws))))))

(defn pixel-vec
  ([color]
   (into [] (for [key (range 0 806)]
              {:r (:r color)
               :g (:g color)
               :b (:b color)
               :key key})))
  ([]
   (into [] (for [key (range 0 806)]
              {:r (rand-int 255)
               :g (rand-int 255)
               :b (rand-int 255)
               :key key}))))

(defn rand-btn []
  [:div.col-md-12.randPixelBtnContainer
   [:span.fa.fa-2x.fa-random.randPixelBtn
    {:on-click
     #(re-frame/dispatch
       [:set-pixel-vec
        (pixel-vec)])}]])

(defn full-fill-btn []
  (let [color (re-frame/subscribe [:active-color])]
    [:div.col-md-12.fullFillBtnContainer
     [:span.fa.fa-2x.fa-arrows-alt.fullFillBtn
      {:on-click
       #(re-frame/dispatch
        [:set-pixel-vec
         (pixel-vec @color)])}]]))

(defn change-color [key]
  (let [color (re-frame/subscribe [:active-color])]
    (re-frame/dispatch
     [:set-pixel {:r (:r @color)
                  :g (:g @color)
                  :b (:b @color)
                  :key key}])))

(defn parse-pixels [pixel-vec]
  (let [mode (re-frame/subscribe [:cursor-mode])]
    (doall
     (for [pixel pixel-vec]
       ^{:key pixel}
       [:div.pixelBox
        {:on-mouse-over (when (= @mode "hover") #(change-color (:key pixel)))
         :on-mouse-down (when (= @mode "click") #(change-color (:key pixel)))
         :style {:backgroundColor
                 (str "rgb(" (:r pixel) "," (:g pixel) "," (:b pixel) ")")}}]))))

(defn sidewalk-grid []
  (let [pixel-vec (re-frame/subscribe [:pixel-vec])]
    (send-socket)
    [:div.pixelContainer (parse-pixels @pixel-vec)]))

(defn color-slider [color value]
  [:input.colorSlider
   {:type "range"
    :min 0
    :max 255
    :value (case color
             "red" (:r value)
             "green" (:g value)
             "blue" (:b value))
    :on-change #(let [slider-value (.-value (.-target %))]
                  (re-frame/dispatch [:set-active-color
                                      (case color
                                        "red" {:r slider-value
                                               :g (:g value)
                                               :b (:b value)}
                                        "green" {:r (:r value)
                                                 :g slider-value
                                                 :b (:b value)}
                                        "blue" {:r (:r value)
                                                :g (:g value)
                                                :b slider-value})]))}])

(defn rand-color []
  [:div.col-md-12.randColorBtnContainer
   [:span.fa.fa-2x.fa-refresh.randColorBtn
    {:on-click #(re-frame/dispatch [:set-active-color
                                    {:r (rand-int 255)
                                     :g (rand-int 255)
                                     :b (rand-int 255)}])}]])

(defn hover-cursor-btn []
  [:div.col-md-12.toggleCursorBtnsContainer
   [:span.fa.fa-2x.fa-hand-paper-o.toggleCursorHoverBtn
    {:on-click #(re-frame/dispatch [:set-cursor-mode "hover"])}]])

(defn click-cursor-btn []
  [:div.col-md-12.toggleCursorBtnsContainer
   [:span.fa.fa-2x.fa-hand-pointer-o.toggleCursorClickBtn
    {:on-click #(re-frame/dispatch [:set-cursor-mode "click"])}]])

(defn color-preview [color]
  [:div.col-md-4.colorPreview
   {:style
    {:backgroundColor
     (str "rgb(" (:r color) "," (:g color) "," (:b color) ")")}}])

(defn color-picker []
  (let [color (re-frame/subscribe [:active-color])]
    [:div.col-md-7.colorPickerContainer
     [:div.col-md-6.sliderContainer
      [:div
       [color-slider "red" @color]
       [:h4 "R " (:r @color)]]
      [:div
       [color-slider "green" @color]
       [:h4 "G " (:g @color)]]
      [:div
       [color-slider "blue" @color]
       [:h4 "B " (:b @color)]]]
     [color-preview @color]]))

(defn toolbar []
  [:div.col-md-12.row.toolbarContainer
   [color-picker]
   [:div.col-md-3.randBtnRow
    [:div.col-md-4.brushSectionContainer
     [:span.fa.fa-3x.fa-eyedropper.brushSectionIcon]
     [:hr.col-md-12.toolbarHr]
     [rand-color]]
    [:div.col-md-4.gridSectionContainer
     [:span.fa.fa-3x.fa-th.gridSectionIcon]
     [:hr.col-md-12.toolbarHr]
     [full-fill-btn]
     [rand-btn]]
    [:div.col-md-4.cursorSectionContainer
     [:span.fa.fa-3x.fa-paint-brush.gridSectionIcon]
     [:hr.col-md-12.toolbarHr]
     [hover-cursor-btn]
     [click-cursor-btn]]]])

(defn main-panel []
  [:div
   [:h1.mainHeader "Sidewalk"]
   [sidewalk-grid]
   [toolbar]])
