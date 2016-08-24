;; Declare namespace, pull in spec libraries and utils
(ns spectalk.core
  (:require [clojure.spec :as s]
            [clojure.spec.gen :as gen]
            [clojure.spec.test :as stest]
            [spectalk.utils :as u]))




























;; Define a spec
(s/def ::name string?)




















;; Check some data against the spec
(s/explain ::name "William Burroughs")

(s/explain ::name 73)

(s/explain ::name "")




















;; More complex spec
(s/def ::name (s/and string?
                     seq))

(s/explain ::name "John Dillinger")

(s/explain ::name "")














;; Some other things you can use as a parameter to s/def:
;; - any arbitrary predicate
;; - the name of another spec
;; - sequences and maps of those things (s/cat, s/keys)
;; - logical combinations of those things (s/and, s/or)
















(u/make ::name)

(u/make ::name 10)




















;; Spec firstname/lastname
(s/def ::firstname (s/and string? seq))

(s/def ::lastname (s/and string?))

(s/def ::name (s/keys :req [::firstname ::lastname]))

(u/make ::name)


















;; Define a function
(defn ranged-rand
  "Returns random int in range start <= rand < end"
  [start end]
  (+ start (long (rand (- end start)))))










;; Spec the function
(s/fdef ranged-rand
        :args (s/and (s/cat :start int? :end int?)
                     #(< (:start %) (:end %)))
        :ret int?
        :fn (s/and #(>= (:ret %) (-> % :args :start))
                   #(< (:ret %) (-> % :args :end))))





;; More detail in spectalk.fnspec

(stest/instrument `ranged-rand)

(ranged-rand 3 5)

(ranged-rand 8 :dog)

























;; Other features:

















;; Generative testing:
(stest/check `ranged-rand)




















;; Validation:
(defn welcome [name]
  (if (s/valid? ::name name)
    (println "Welcome, Ms or Mr"
             (::firstname name)
             (::lastname name))
    ;; (implicit else)
    (s/explain ::name name)))

(def me {::firstname "Egg"
         ::lastname "Syntax"})

(welcome me)


(def tom {::firstname "Tom"})

(welcome tom)


(def jon {::firstname "Jon"
          ::lastname 7})

(welcome jon)




















;; Destructuring

(def my-address
  [73 "Random Lane" "Asheville" :NC])

(defn welcome-address [address]
  (let [street-num  (nth address 0)
        street-name (nth address 1)
        city        (nth address 2)
        state       (nth address 3)]
    (println "Welcome to" street-num
             street-name city state "!")))

(welcome-address my-address)




(s/def ::address
  (s/cat :street-num int?
         :street-name string?
         :city string?
         :state keyword?))

(s/valid? ::address my-address)

(s/conform ::address my-address)





















;; The end!
;; More info:
;; - This code: https://github.com/eggsyntax/spectalk
;; - Spec overview: http://clojure.org/about/spec
;; - Spec guide: http://clojure.org/guides/spec
