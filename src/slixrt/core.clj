(ns slixrt.core
  (:gen-class)
  (:use (sliimp film))
  (:use slimath.core)
  (:use (slitrace camera shape prim core)))
  

(defn -main
  "I don't do a whole lot."
  [& args]
  ;; work around dangerous default behaviour in CLojure
  (alter-var-root #'*read-eval* (constantly false))
  (println "slixrt\n"))

