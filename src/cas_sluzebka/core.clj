(ns cas-sluzebka.core
  "
  Calculates the trip time inside and outside the business hours.
  Example: (`calc-times` <AllTimesTocalc>), returns CalculatedTimes.  
  "
  (:require [cas-sluzebka.time :as time]
            [cas-sluzebka.trip :as trip])
  (:import  [cas_sluzebka CalculatedTime CalculatedTimes TimeToCalc AllTimesToCalc]))


(defn- map->CalculatedTime
  "Creates an instance of CalculatedTime from map {:before time/Time 
  :after time/Time :project time/Time }."
  [{:keys [before after project]}]
  (CalculatedTime. (:h before)
                   (:m before)
                   (:h after)
                   (:m after)
                   (:h project)
                   (:m project)))

(defn- travel-times
  "Calculates travel times for one trip. Expects to-calc to be TimeToCalc, returns map
  with keys {:before time/Time :after time/Time}.
  Optional breaks should be time/Time with sum of all work breaks. If not passed,
  no breaks are assumed."
  [to-calc]
  (let [dep (time/->Time (.-departureHour to-calc) (.-departureMinute to-calc))
        arr (time/->Time (.-arrivalHour to-calc)   (.-arrivalMinute to-calc))
        thr (time/->Time (.-thresholdHour to-calc) (.-thresholdMinute to-calc))]
    (trip/travel-times thr dep arr)))

(defn- project-time
  "Calculates time to be billed to a project. 
  
  Expects
  :breaks time/Time sum of break times during work hours
  :travel-there TimeToCalc for trip there
  :travel-back TimeToCalc for trip back
  :inisde-hrs time/Time sum of hours spent travelling inside busines hours
  
  Returns time/Time how many hours/minutes should be billed to a project"
  [& {:keys [breaks travel-there travel-back inside-hrs]}]
  (let [work-start (time/->Time (.-thresholdHour travel-there)
                                (.-thresholdMinute travel-there))
        work-end   (time/->Time (.-thresholdHour travel-back)
                                (.-thresholdMinute travel-back))]
    (trip/project-time :breaks breaks
                       :start work-start
                       :end work-end
                       :travel-inside inside-hrs)))

(defn- two-day-calc-times
  "Returns instance of CalculatedTimes which represents at least two days
  trip. trip and trip-back parameters should be maps of 
  {:before time/Time :after time/Time}. Project and project-back should be
  instances of time/Time, sums of times needed to be billed to the project."
  [trip trip-back project project-back]
  (let [all-trip-there (assoc trip :project project)
        all-trip-back  (assoc trip-back :project project-back)]
    (CalculatedTimes. (map->CalculatedTime all-trip-there)
                      (map->CalculatedTime all-trip-back))))

(defn- one-day-calc-times
  "Returns instance of CalculatedTimes which represents one day trip.
  The trip times are summed and returned in field day1.
  trip and trip-back parameters should be maps of
  {:before time/Time :after time/Time}."
  [trip trip-back project]
  ;; need to swap before/after threshold for return trip
  ;; to correctly calculate inside/outside business hours
  (let [before-sum (time/add (:before trip) (:after trip-back))
        after-sum  (time/add (:after trip)  (:before trip-back))]
    (CalculatedTimes. (map->CalculatedTime {:before before-sum 
                                            :after after-sum
                                            :project project})
                      nil)))

(defn- two-day-trip
  "Calculates all times for two day trip.
  
  Expects
  break-time time/Time sum of breaks at work (e.g. lunch breaks)
  trip-there TimeToCalc trip there
  trip-back TimeToCalc trip back
  
  Returns CalculatedTimes for two day trip.
  "
  [break-time trip trip-back]
  (let [calc-trip  (travel-times trip)
        calc-back  (travel-times trip-back)
        proj-there (project-time :breaks break-time
                                 :travel-there trip
                                 :travel-back trip-back
                                 :inside-hrs (:after calc-trip))
        proj-back  (project-time :breaks break-time
                                 :travel-there trip
                                 :travel-back trip-back
                                 :inside-hrs (:before calc-back))]
    (two-day-calc-times calc-trip calc-back proj-there proj-back)))

(defn- one-day-trip
  "Calculates all times for one day trip.
  
  Expects
  break-time time/Time sum of breaks at work (e.g. lunch breaks)
  trip-there TimeToCalc trip there
  trip-back TimeToCalc trip back
  
  Returns CalculatedTimes for one day trip.
  "
  [break-time trip trip-back]
  (let [calc-trip    (travel-times trip)
        calc-back    (travel-times trip-back)
        inside-hours (time/add (:after calc-trip) (:before calc-back))
        proj         (project-time :breaks break-time
                                   :travel-there trip
                                   :travel-back trip-back
                                   :inside-hrs inside-hours)]
    (one-day-calc-times calc-trip calc-back proj)))

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
        break-time (time/->Time (.-breakTimeHours all-times)
                                (.-breakTimeMins all-times))]
    (if (.-otherDay all-times)
      (two-day-trip break-time trip trip-back)
      (one-day-trip break-time trip trip-back))))


