(ns cas-sluzebka.core-test
  (:require [clojure.test :refer :all]
            [cas-sluzebka.core :refer :all]
            [cas-sluzebka.time :as time])
  (:import [cas_sluzebka CalculatedTime CalculatedTimes TimeToCalc AllTimesToCalc]))




(deftest calc-times-test
  (let [time1 (TimeToCalc. 5 30
                           9 0
                           8 0)
        time2 (TimeToCalc. 15 0
                           18 30
                           16 30)
        times-same (AllTimesToCalc. time1
                                    time2
                                    false
                                    1
                                    30)
        times-other (AllTimesToCalc. time1
                                     time2
                                     true
                                     1
                                     30)]
    (testing "times the same day"
      (let [times (calc-times times-same)
            t-d1 (.-day1 times)
            t-d2 (.-day2 times)
            hb1 (.-hoursBeforeWorkingTime t-d1)
            mb1 (.-minutesBeforeWorkingTime t-d1)
            ha1 (.-hoursAfterWorkingTime t-d1)
            ma1 (.-minutesAfterWorkingTime t-d1)
            hp1 (.-projectWorkingTimeHours t-d1)
            mp1 (.-projectWorkingTimeMinutes t-d1)]
        (is (= 4 hb1))
        (is (= 30 mb1))
        (is (= 2 ha1))
        (is (= 30 ma1))
        (is (= 4 hp1))
        (is (= 30 mp1))
        (is (nil? t-d2))))

    (testing "times other day"
      (let [times (calc-times times-other)
            t-d1 (.-day1 times)
            t-d2 (.-day2 times)
            hb1 (.-hoursBeforeWorkingTime t-d1)
            mb1 (.-minutesBeforeWorkingTime t-d1)
            ha1 (.-hoursAfterWorkingTime t-d1)
            ma1 (.-minutesAfterWorkingTime t-d1)
            hp1 (.-projectWorkingTimeHours t-d1)
            mp1 (.-projectWorkingTimeMinutes t-d1)
            hb2 (.-hoursBeforeWorkingTime t-d2)
            mb2 (.-minutesBeforeWorkingTime t-d2)
            ha2 (.-hoursAfterWorkingTime t-d2)
            ma2 (.-minutesAfterWorkingTime t-d2)
            hp2 (.-projectWorkingTimeHours t-d2)
            mp2 (.-projectWorkingTimeMinutes t-d2)]
        (is (= 2 hb1))
        (is (= 30 mb1))
        (is (= 1 ha1))
        (is (= 0 ma1))
        (is (= 6 hp1))
        (is (= 0 mp1))
        (is (= 1 hb2))
        (is (= 30 mb2))
        (is (= 2 ha2))
        (is (= 0 ma2))
        (is (= 5 hp2))
        (is (= 30 mp2))))

    (testing "for parameter should throw"
      (is (thrown? IllegalArgumentException (calc-times nil)))
      (is (thrown? IllegalArgumentException (calc-times ""))))))
