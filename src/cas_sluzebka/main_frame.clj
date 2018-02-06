(ns cas-sluzebka.main-frame
  (:require [seesaw.core :as ssw]
            [seesaw.font :as sswfnt]))

;;; debug
(ssw/native!) ;look native

(def pf "Proof of concept frame."
  (ssw/frame :title "Hello World"))

(-> pf ssw/pack! ssw/show!)

(ssw/config pf :title) ;query the window for property
(ssw/config! pf :content "Content!") ;set content, setting text will create label

(defn display
  "Sets content to `pf` window. Returns content."
  [content]
  (ssw/config! pf :content content)
  content)

(display (ssw/label "I am a label."))

;;; label
(let [lbl (ssw/label "I am restyled label.")]
  (ssw/config! lbl :font (sswfnt/font :name :monospaced
                                      :style #{:bold :italic}
                                      :size 18))
  (display lbl))

(ssw/input "What do you say?") ;input box

;;; button
(let [btn (ssw/button :text "Hello World"
                      :mnemonic \H
                      :listen [:action (fn [e] (ssw/alert pf
                                                          "Hello World!"
                                                          :type :info
                                                          :title "Hello Title"))])]
  ;; listen can be used outside button factory function
  (ssw/listen btn
              :mouse-entered #(ssw/config! % :foreground :blue)
              :mouse-exited #(ssw/config! % :foreground :red))
  (display btn))

;;; listbox
(let [lb         (ssw/listbox :model (-> 'seesaw.core ns-publics keys sort))
      scrollable (ssw/scrollable lb)]
  (ssw/listen lb :selection (fn [e] (println "Selection is " (ssw/selection e))))
  ;;selections should be unregistered. Listen function returns fn to unregister
  ;;selction! sets selection
  (ssw/selection lb {:multi? :true}) ;mutliselection
  (display scrollable))
