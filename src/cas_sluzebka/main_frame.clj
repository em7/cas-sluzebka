(ns cas-sluzebka.main-frame
  (:import [org.eclipse.swt.widgets Shell Display]))


(def dsp "DBG main testing display"
  (Display.))

(def shl "DBG main shell" (Shell.))

(defn show-shl
  "Main event loop"
  []
  (loop [shell shl
         display dsp]
    (when-not (.isDisposed shell)
      (when-not (.readAndDispatch display)
        (.sleep display)))
    (when-not (.isDisposed shell)
      (recur shell display)))
  (.dispose dsp))

(show-shl)


