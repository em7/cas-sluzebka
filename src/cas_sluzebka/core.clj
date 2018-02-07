(ns cas-sluzebka.core
  (:import [java.time LocalTime]
           [cas_sluzebka CalculatedTime CalculatedTimes TimeToCalc AllTimesToCalc]))

(def zero-time {:h 0 :m 0})

(defn- diff-local
  "Calculates difference between two LocalTimes. Return type is local time.
  Calculates only with minutes and seconds."
  [lt1 lt2]
  (-> (.minusHours lt1 (.getHour lt2))
      (.minusMinutes (.getMinute lt2))))

(defn time-before
  "Calculates time before threshold. Expects maps with keys :h :m, returns
  such map. If time is after threshold, returns {:h 0 :m 0}."
  [threshold time]
  (let [th-local (LocalTime/of (:h threshold) (:m threshold))
        ti-local (LocalTime/of (:h time) (:m time))]
    (if (.isBefore th-local ti-local)
      zero-time
      (let [diff (diff-local th-local ti-local)]
        {:h (.getHour diff) :m (.getMinute diff)}))))

(defn time-after
  "Calculates time after threshold. Expects maps with keys :h :m,
  returns such map. If time is before threshold, returns {:h 0 :m 0}."
  [threshold time]
  (time-before time threshold))

(defn time-equal
  "Returns true if times are equal. Expects times to be map with :h and :m keys."
  [t1 t2]
  (and (= (:h t1) (:h t2))
       (= (:m t1) (:m t2))))

(defn time-sum
  "Returns a sum of times. Expects times to be a map {:h :m}."
  [t1 t2]
  (let [loc (-> (LocalTime/of (:h t1) (:m t1))
                (.plusHours (:h t2))
                (.plusMinutes (:m t2)))]
    {:h (.getHour loc) :m (.getMinute loc)}))

(defn calc-trip
  "Calculates travel times before and after business hours threshold for one trip.
  Returns a map of times with keys :before and :after. Expects times to be map with
  :h and :m keys. thr is threshold between inside/outside business hours, dep 
  is departure and arr is arrival."
  [thr dep arr]
  (let [arr-after-t  (time-after thr arr)
        dep-before-t (time-before thr dep)]
    (condp time-equal zero-time
      arr-after-t  {:before (time-after dep arr) ;arrived before threshold
                    :after  zero-time}
      dep-before-t {:before zero-time   ;departed after threshold
                    :after  (time-after dep arr)}
      {:before dep-before-t
       :after  arr-after-t})))

;;; TODO differentiate whether its trip there or return!
(defn map->CalculatedTime
  "Creates an instance of CalculatedTime from map {:before {:h :m} :after {:h :m}}."
  [m]
  (CalculatedTime. (get-in m [:before :h])
                   (get-in m [:before :m])
                   (get-in m [:after :h])
                   (get-in m [:after :m])))

(defn calc-time
  "Calculates times for one trip. Expects TimeToCalc, returns map
  with keys {:before {:h :m} :after {:h :m}}."
  [to-calc]
  (let [dep {:h (.-departureHour to-calc) :m (.-departureMinute to-calc)}
        arr {:h (.-arrivalHour to-calc) :m (.-arrivalMinute to-calc)}
        thr {:h (.-thresholdHour to-calc) :m (.-thresholdMinute to-calc)}]
    (calc-trip thr dep arr)))

(defn two-day-trip
  "Returns instance of CalculatedTimes which represents at least two days
  trip. trip and trip-back parameters should be maps of 
  {:before {:h :m} :after {:h :m}}."
  [trip trip-back]
  (CalculatedTimes. (map->CalculatedTime trip)
                    (map->CalculatedTime trip-back)))

(defn one-day-trip
  "Returns instance of CalculatedTimes which represents one day trip.
  The trip times are summed and returned in field day1.
  trip and trip-back parameters should be maps of
  {:before {:h :m} :after {:h :m}}."
  [trip trip-back]
  (let [before-sum (time-sum (:before trip) (:before trip-back))
        after-sum ])
  (CalculatedTimes. (map->CalculatedTime trip)
                    nil))



;; (map->CalculatedTime  {:before {:h 5 :m 4} :after {:h 2 :m 1}})

;; (defn calc-times
;;   [all-times]
;;   (let [trip (.-trip all-times)
;;         trip-back (.-tripBack all-times)
;;         calc-trip (calc-time trip)
;;         calc-back (calc-time trip-back)]
;;     (if (.-otherDay all-times)
;;       )))


;;; DEBUG
(def start-thresh {:h 8 :m 0})
(def end-thresh {:h 16 :m 30})


