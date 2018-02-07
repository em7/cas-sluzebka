(ns cas-sluzebka.core-test
  (:require [clojure.test :refer :all]
            [cas-sluzebka.core :refer :all]))

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
