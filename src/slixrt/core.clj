(ns slixrt.core
  (:gen-class)
  (:use [slimath core]
        [sliimp core filter sampler film]
        [sligeom core transform bounding [intersect :only [ray]]]
        [slitrace core camera shape prim])
  (:import [sliimp.sampler Sample]
           [sligeom.transform Transform]))
  
(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(def ^:dynamic *world-transform* (translate 0.0 0.0 -3))
(def ^:dynamic *projection* (perspective :fov-rads (Math/toRadians 38.) 
                                         :aspect 1.0 
                                         :near 1.0 
                                         :far 100.0))
(def ^:dynamic *world* (instance (translate 0 0 0)
                                (sphere 1.0)))

(defn radiance [^Transform SP ^Sample s] 
  (let [^Ray r-camera (camera-ray (:x-film s) (:y-film s) SP)
        ^Ray r-world (transform r-camera (inverse *world-transform*))]
(if-let [[t p n] (trace *world* r-world) ]
      (sample s t t t)
      (sample s 0.0 0.0 0.0))))

(defn radiance2 [SP s]
  (sample s 1.0 1.0 0.0))
  

(defn -main
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))

  (when-let [^Film F (film :bounds (rect :width 1024 :height 1024) 
                      :filter (gaussian)
                      :finished-f #(spit-film! % "/tmp/slixrt.exr")
                      :sampler-f stratified-seq2
                      :samples-per-pixel 100)]
    (println "slixrt\n")
    (let [[x0 y0 x1 y1] (rect-vec (:bounds F))
          sf (partial (:sampler-f F) (:samples-per-pixel F))
          S (screen-transform (width F) (height F))
          P (perspective :fov-rads (Math/toRadians 38.) 
                         :aspect (double (/ (width F) (height F))) 
                         :near 1.0 
                         :far 100.0)
          SP (compose S P)
          radiance-f (partial radiance SP)]
      (doseq [y (range y0 y1) x (range x0 x1)]
        (let [^Sampler ss (sf x y)]
          (doseq [^Sample s (:samples ss)]        
            (splat! F (radiance-f s))))))
    (finish-film! F)))

