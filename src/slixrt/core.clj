(ns slixrt.core
  (:gen-class)
  (:use (sliimp film))
  (:use slimath.core)
  (:use (sligeom core transform bounding))
  (:use (slitrace camera shape prim core)))
  
(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(def ^:dynamic *world-transform* (translate 0.0 0.2 -10))
;(def ^:dynamic *projection* (perspective (Math/toRadians 38.) 1.0 1.0 100.))
(def ^:dynamic *world* (instance (translate 0 0 0)
                                (sphere 1.0)))

(defn -main
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (println "slixrt\n"))

