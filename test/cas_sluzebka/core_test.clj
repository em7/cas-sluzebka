(ns cas-sluzebka.core-test
  (:require [clojure.test :refer :all]
            [cas-sluzebka.core :refer :all]
            [cas-sluzebka.time :as time])
  (:import [cas_sluzebka CalculatedTime CalculatedTimes TimeToCalc AllTimesToCalc]))


(deftest calc-trip-test
  (let [th (time/map->Time {:h 8 :m 30})]
    (testing "dep and arr before threshold"
      (let [calc   (calc-trip th
                              (time/map->Time {:h 6 :m 0})
                              (time/map->Time {:h 8 :m 15}))
            before (:before calc)
            after  (:after calc)]
        (is (time/equal? time/zero after))
        (is (time/equal? (time/map->Time {:h 2 :m 15}) before))))
    (testing "dep and arr after threshold"
      (let [calc   (calc-trip th 
                              (time/map->Time {:h 8 :m 45})
                              (time/map->Time {:h 9 :m 15}))
            before (:before calc)
            after  (:after calc)]
        (is (time/equal? time/zero before))
        (is (time/equal? (time/map->Time {:h 0 :m 30}) after))))
    (testing "deb before threshold and arr after threshold"
      (let [calc   (calc-trip th {:h 6 :m 0} {:h 10 :m 30})
            before (:before calc)
            after  (:after calc)]
        (is (time/equal? before (time/map->Time {:h 2 :m 30})))
        (is (time/equal? after (time/map->Time {:h 2 :m 0})))))))

(deftest project-time-test
  (let [breaks (time/map->Time {:h 1 :m 30})
        start  (time/map->Time {:h 8 :m 0})
        end    (time/map->Time {:h 16 :m 30})]
    (testing "no breaks and travel time should return whole time"
      (let [expected (time/map->Time {:h 8 :m 30})
            actual   (project-time time/zero start end time/zero)]))))

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
                                    0 ;TODO
                                    0)
        times-other (AllTimesToCalc. time1
                                     time2
                                     true
                                     0 ;TODO
                                     0)]
    (testing "times the same day"
      (let [times (calc-times times-same)
            t-d1 (.-day1 times)
            t-d2 (.-day2 times)
            hb1 (.-hoursBeforeWorkingTime t-d1)
            mb1 (.-minutesBeforeWorkingTime t-d1)
            ha1 (.-hoursAfterWorkingTime t-d1)
            ma1 (.-minutesAfterWorkingTime t-d1)]
        (is (= 4 hb1))
        (is (= 30 mb1))
        (is (= 2 ha1))
        (is (= 30 ma1))
        (is (nil? t-d2))))

    (testing "times other day"
      (let [times (calc-times times-other)
            t-d1 (.-day1 times)
            t-d2 (.-day2 times)
            hb1 (.-hoursBeforeWorkingTime t-d1)
            mb1 (.-minutesBeforeWorkingTime t-d1)
            ha1 (.-hoursAfterWorkingTime t-d1)
            ma1 (.-minutesAfterWorkingTime t-d1)
            hb2 (.-hoursBeforeWorkingTime t-d2)
            mb2 (.-minutesBeforeWorkingTime t-d2)
            ha2 (.-hoursAfterWorkingTime t-d2)
            ma2 (.-minutesAfterWorkingTime t-d2)]
        (is (= 2 hb1))
        (is (= 30 mb1))
        (is (= 1 ha1))
        (is (= 0 ma1))
        (is (= 1 hb2))
        (is (= 30 mb2))
        (is (= 2 ha2))
        (is (= 0 ma2))))

    (testing "for parameter should throw"
      (is (thrown? IllegalArgumentException (calc-times nil)))
      (is (thrown? IllegalArgumentException (calc-times ""))))))
