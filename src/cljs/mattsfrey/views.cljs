(ns mattsfrey.views
  (:require [re-frame.core :as re-frame]
            [re-com.core :as re-com]
            [reagent.core :as reagent]))


;; home

(defn home-title []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [re-com/title
       :label (str "Hello from " @name ". This is the Home Page.")
       :level :level1])))

(defn link-to-about-page []
  [re-com/hyperlink-href
   :label "go to About Page"
   :href "#/about"])

(defn home-panel []
  [re-com/v-box
   :gap "1em"
   :children [[home-title] [link-to-about-page]]])


;; about

(defn about-title []
  [re-com/title
   :label "This is the About Page."
   :level :level1])

(defn link-to-home-page []
  [re-com/hyperlink-href
   :label "go to Home Page"
   :href "#/"])

(defn about-panel []
  [re-com/v-box
   :gap "1em"
   :children [[about-title] [link-to-home-page]]])


(def tabs-definition
  [{:id ::home-panel  :label "Home"}
   {:id ::about-panel  :label "About"}])


(defn- inspect-1 [expr]
  `(let [result# ~expr]
     (js/console.info (str (pr-str '~expr) " => " (pr-str result#)))
     result#))

(defmacro inspect [& exprs]
  `(do ~@(map inspect-1 exprs)))

;; main

(defmulti panels identity)
(defmethod panels :home-panel [] [home-panel])
(defmethod panels :about-panel [] [about-panel])
(defmethod panels :default [] [:div])


(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      (js/console.log "active-panel => " (pr-str @active-panel))
      [re-com/v-box
       :children [[re-com/h-box
                   :align :center
                   :children [
                              [re-com/horizontal-pill-tabs
                              :model @active-panel
                              :tabs [{:id :home-panel  :label "Home"}
                                     {:id :about-panel  :label "About"}]
                              :on-change #(re-frame/dispatch [:set-active-panel %])]]]
                  [re-com/h-box
                   :height "100px"
                   :children [[re-com/v-box :size "1" :class "Content"
                               :children [(panels @active-panel)]]]]]])))
