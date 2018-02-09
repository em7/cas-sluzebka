(ns cas-sluzebka.time-test
  (:require [cas-sluzebka.time :refer :all]
            [clojure.test :refer :all]))

(deftest before-test
  (let [th (map->Time {:h 8 :m 0})]
    (testing "returns diff when time is before"
      (let [diff (before th {:h 4 :m 30})]
        (is (= 3 (:h diff)))
        (is (= 30 (:m diff)))))
    (testing "returns 0 when time is equal to threshold"
      (let [diff (before th th)]
        (is (= 0 (:h diff)))
        (is (= 0 (:m diff)))))
    (testing "returns 0 when time is after threshold"
      (let [diff (before th (map->Time {:h 9 :m 30}))]
        (is (= 0 (:h diff)))
        (is (= 0 (:m diff)))))))

(deftest after-test
  (let [th (map->Time {:h 8 :m 0})]
    (testing "returns diff when time is after"
      (let [diff (after th (map->Time {:h 9 :m 15}))]
        (is (= 1 (:h diff)))
        (is (= 15 (:m diff)))))
    (testing "returns 0 when time is equal to threshold"
      (let [diff (after th th)]
        (is (= 0 (:h diff)))
        (is (= 0 (:m diff)))))
    
    (testing "returns 0 when time is before threshold"
      (let [diff (after th (map->Time {:h 6 :m 30}))]
        (is (= 0 (:h diff)))
        (is (= 0 (:m diff)))))))

