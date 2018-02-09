(ns cas-sluzebka.core
  "
  Calculates the trip time inside and outside the business hours.
  Example: (`calc-times` <AllTimesTocalc>), returns CalculatedTimes.  
  "
  (:require [cas-sluzebka.time :as time])
  (:import [java.time LocalTime]
           [cas_sluzebka CalculatedTime CalculatedTimes TimeToCalc AllTimesToCalc]))

(defn calc-trip
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

;; TODO
(defn project-time
  "Calculates time which needs to be billed to project.
  Breaks is the total sum of work-time breaks (e.g. lunch breaks)
  passed as instance of time/Time.
  For start and end expects map with keys :h :m when the business
  hours start and end. Travel-inside should be sum 
  of trave time inside business hours as a map with keys :h :m."
  [& {:keys [breaks start end travel-inside]}]
  
  
  )

(defn map->CalculatedTime
  "Creates an instance of CalculatedTime from map {:before time/Time 
  :after time/Time }."
  [m]
  (CalculatedTime. (get-in m [:before :h])
                   (get-in m [:before :m])
                   (get-in m [:after :h])
                   (get-in m [:after :m])
                   0 ; TODO
                   0))

(defn calc-time
  "Calculates times for one trip. Expects TimeToCalc, returns map
  with keys {:before time/Time :after time/Time}."
  [to-calc]
  (let [dep {:h (.-departureHour to-calc) :m (.-departureMinute to-calc)}
        arr {:h (.-arrivalHour to-calc) :m (.-arrivalMinute to-calc)}
        thr {:h (.-thresholdHour to-calc) :m (.-thresholdMinute to-calc)}]
    (calc-trip thr dep arr)))

(defn two-day-trip
  "Returns instance of CalculatedTimes which represents at least two days
  trip. trip and trip-back parameters should be maps of 
  {:before time/Time :after time/Time}."
  [trip trip-back]
  (CalculatedTimes. (map->CalculatedTime trip)
                    (map->CalculatedTime trip-back)))

(defn one-day-trip
  "Returns instance of CalculatedTimes which represents one day trip.
  The trip times are summed and returned in field day1.
  trip and trip-back parameters should be maps of
  {:before time/Time :after time/Time}."
  [trip trip-back]
  ;; need to swap before/after threshold for return trip
  ;; to correctly calculate inside/outside business hours
  (let [before-sum (time/+ (:before trip) (:after trip-back))
        after-sum  (time/+ (:after trip) (:before trip-back))]
    (CalculatedTimes. (map->CalculatedTime {:before before-sum :after after-sum})
                      nil)))

(comment
  ;; in this case the return value should be 3h before and 3h after 
  (one-day-trip {:before {:h 1 :m 30} :after {:h 2 :m 15}}
                {:before {:h 0 :m 45} :after {:h 1 :m 30}})
  )


(defn calc-times
  "
  Calculates trip time inside/outside business hours. Handles cases when
  return trip is done the same day or other day. If return trip is done the
  same day, returns sum of time before threshold on trip there + time after threshold
  on trip back in BeforeWorkingTime fields and sum of time after threshold on trip
  there + time before threshold on trip back in AfterWorkingTime fields.

  For one two day trips returns time before / after threshold.

  Expects an instance of AllTimesToCalc as a parameter, otherwise throws 
  IllegalArgumentException.
  Returns an instance of CalculatedTimes.
  "
  [all-times]
  
  (when-not (instance? AllTimesToCalc all-times)
    (throw (java.lang.IllegalArgumentException.
            "all-times must be instance of AllTimesToCalc.")))
  
  (let [trip       (.-trip all-times)
        trip-back  (.-tripBack all-times)
        break-time {:h (.-breakTimeHours all-times)
                    :m (.-breakTimeMins all-times)}
        calc-trip  (calc-time trip)
        calc-back  (calc-time trip-back)]
    (if (.-otherDay all-times)
      (two-day-trip calc-trip calc-back)
      (one-day-trip calc-trip calc-back))))

;;; DEBUG
(comment 
  (def start-thresh {:h 8 :m 0})
  (def end-thresh {:h 16 :m 30})

  )


