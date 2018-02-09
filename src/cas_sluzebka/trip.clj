(ns cas-sluzebka.trip
  "Trip time calculations."
  (:require [cas-sluzebka.time :as time]))

(defn travel-times
  "Calculates travel times before and after business hours threshold for one trip.
  Returns a map of times with keys :before and :after. Expects times to instances of
  time/Time. thr is threshold between inside/outside business hours, dep 
  is departure and arr is arrival."
  [thr dep arr]
  (let [arr-after-t  (time/after thr arr)
        dep-before-t (time/before thr dep)]
    (condp time/equal? time/zero
      arr-after-t  {:before (time/after dep arr) ;arrived before threshold
                    :after  time/zero}
      dep-before-t {:before time/zero   ;departed after threshold
                    :after  (time/after dep arr)}
      {:before dep-before-t
       :after  arr-after-t})))

(defn project-time
  "Calculates time which needs to be billed to project.
  All parameters should be instances of time/Time.
  Breaks is the total sum of work-time breaks (e.g. lunch breaks).
  For start and end expects times when the business
  hours start and end. Travel-inside should be sum 
  of trave time inside business hours."
  [& {:keys [breaks start end travel-inside]}]
  (-> (time/subst end start)
      (time/subst travel-inside)
      (time/subst breaks)))
