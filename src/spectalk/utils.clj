(ns spectalk.utils
  (:require [clojure.spec :as s]
            [clojure.spec.gen :as gen]
            [clojure.spec.test :as stest]))

(defn make
  ([spec] (gen/generate (s/gen spec)))
  ([spec & [num]] (gen/sample (s/gen spec) num) ))
