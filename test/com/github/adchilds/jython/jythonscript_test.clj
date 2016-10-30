(ns com.github.adchilds.jython.jythonscript-test
  (:require [clojure.test :refer :all]
            [com.github.adchilds.jython.jythonscript :refer :all])
  (:import  [clojure.java.io/input-stream]))

(def base-path "test/resources/script/jython/")

(deftest test-execute
  (testing "Execute a Jython script from a file path."
    (execute (str base-path "testExecute.py"))))

(deftest test-execute-with-args
  (testing "Execute a Jython script from a file path."
    (execute (str base-path "testExecute.py") "test")))

