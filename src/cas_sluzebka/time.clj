(ns cas-sluzebka.time
  "Holds abstraction over time in a day. Time is represented as a record which holds
  hours and minutes as a positive integer.
  Constant `zero` is provided for zero time."
  (:import [java.time LocalTime]))

(defrecord Time [h m])

(def zero
  "Zero time constant."
  (Time. 0 0))

(defn- diff-local
  "Calculates difference between two LocalTimes. Return type is local time.
  Calculates only with minutes and seconds."
  [lt1 lt2]
  (-> (.minusHours lt1 (.getHour lt2))
      (.minusMinutes (.getMinute lt2))))

(defn before
  "Calculates time before threshold. Expects instances of Time as parameters.
  If time is after threshold, returns `zero` time."
  [threshold time]
  (let [th-local (LocalTime/of (:h threshold) (:m threshold))
        ti-local (LocalTime/of (:h time) (:m time))]
    (if (.isBefore th-local ti-local)
      zero
      (let [diff (diff-local th-local ti-local)]
        (Time.(.getHour diff) (.getMinute diff))))))

(defn after
  "Calculates time after threshold. Expects instances of Time as parameters.
  If time is after threshold, returns `zero` time."
  [threshold time]
  (before time threshold))

(defn equal?
  "Returns true if times are equal. Expects times to be instances of Time."
  [t1 t2]
  (and (= (:h t1) (:h t2))
       (= (:m t1) (:m t2))))

(defn add
  "Returns a sum of times. Expects times to be instances of Time."
  [t1 t2]
  (let [loc (-> (LocalTime/of (:h t1) (:m t1))
                (.plusHours (:h t2))
                (.plusMinutes (:m t2)))]
    (Time.(.getHour loc) (.getMinute loc))))

(defn subst
  "Returns a substraction of times like t1 - t2.
  Expects times to be instances of Time."
  [t1 t2]
  (let [loc (-> (LocalTime/of (:h t1) (:m t1))
                (.minusHours (:h t2))
                (.minusMinutes (:m t2)))]
    (Time. (.getHour loc) (.getMinute loc))))


