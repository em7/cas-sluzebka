(ns cas-sluzebka.core-test
  (:require [clojure.test :refer :all]
            [cas-sluzebka.core :refer :all])
  (:import [cas_sluzebka CalculatedTime CalculatedTimes TimeToCalc AllTimesToCalc]))

(deftest time-before-test
  (let [th {:h 8 :m 0}]
    (testing "returns diff when time is before"
      (let [diff (time-before th {:h 4 :m 30})]
        (is (= 3 (:h diff)))
        (is (= 30 (:m diff)))))
    (testing "returns 0 when time is equal to threshold"
      (let [diff (time-before th th)]
        (is (= 0 (:h diff)))
        (is (= 0 (:m diff)))))
    (testing "returns 0 when time is after threshold"
      (let [diff (time-before th {:h 9 :m 30})]
        (is (= 0 (:h diff)))
        (is (= 0 (:m diff)))))))

(deftest time-after-test
  (let [th {:h 8 :m 0}]
    (testing "returns diff when time is after"
      (let [diff (time-after th {:h 9 :m 15})]
        (is (= 1 (:h diff)))
        (is (= 15 (:m diff)))))
    (testing "returns 0 when time is equal to threshold"
      (let [diff (time-after th th)]
        (is (= 0 (:h diff)))
        (is (= 0 (:m diff)))))
    (testing "returns 0 when time is before threshold"
      (let [diff (time-after th {:h 6 :m 30})]
        (is (= 0 (:h diff)))
        (is (= 0 (:m diff)))))))

(deftest calc-trip-test
  (let [th {:h 8 :m 30}]
    (testing "dep and arr before threshold"
      (let [calc   (calc-trip th {:h 6 :m 0} {:h 8 :m 15})
            before (:before calc)
            after  (:after calc)]
        (is (time-equal zero-time after))
        (is (time-equal {:h 2 :m 15} before))))
    (testing "dep and arr after threshold"
      (let [calc   (calc-trip th {:h 8 :m 45} {:h 9 :m 15})
            before (:before calc)
            after  (:after calc)]
        (is (time-equal zero-time before))
        (is (time-equal {:h 0 :m 30} after))))
    (testing "deb before threshold and arr after threshold"
      (let [calc   (calc-trip th {:h 6 :m 0} {:h 10 :m 30})
            before (:before calc)
            after  (:after calc)]
        (is (time-equal before {:h 2 :m 30}))
        (is (time-equal after {:h 2 :m 0}))))))

(deftest calc-times-test
  (let [time1 (TimeToCalc. 5 30
                           9 0
                           8 0)
        time2 (TimeToCalc. 15 0
                           18 30
                           16 30)
        times-same (AllTimesToCalc. time1
                                    time2
                                    false)
        times-other (AllTimesToCalc. time1
                                     time2
                                     true)]
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
