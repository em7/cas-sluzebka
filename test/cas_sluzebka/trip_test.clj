(ns cas-sluzebka.trip-test
  (:require [cas-sluzebka.trip :refer :all]
            [cas-sluzebka.time :as time]
            [clojure.test :refer :all]))

(deftest trip-times-test
  (let [th (time/map->Time {:h 8 :m 30})]
    (testing "dep and arr before threshold"
      (let [calc   (travel-times th
                              (time/map->Time {:h 6 :m 0})
                              (time/map->Time {:h 8 :m 15}))
            before (:before calc)
            after  (:after calc)]
        (is (time/equal? time/zero after))
        (is (time/equal? (time/map->Time {:h 2 :m 15}) before))))
    (testing "dep and arr after threshold"
      (let [calc   (travel-times th 
                              (time/map->Time {:h 8 :m 45})
                              (time/map->Time {:h 9 :m 15}))
            before (:before calc)
            after  (:after calc)]
        (is (time/equal? time/zero before))
        (is (time/equal? (time/map->Time {:h 0 :m 30}) after))))
    (testing "deb before threshold and arr after threshold"
      (let [calc   (travel-times th {:h 6 :m 0} {:h 10 :m 30})
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
            actual   (project-time :breaks time/zero
                                   :start start
                                   :end end
                                   :travel-inside time/zero)]))))
