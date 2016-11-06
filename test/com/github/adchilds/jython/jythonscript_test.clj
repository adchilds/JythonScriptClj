(ns com.github.adchilds.jython.jythonscript-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [com.github.adchilds.jython.jythonscript :refer :all]))

(def base-path "test/resources/script/jython/")

(deftest test-execute-string
  (testing "Execute a Jython script from a file path."
    (execute (str base-path "testExecute.py"))))

(deftest test-execute-string-with-args
  (testing "Execute a Jython script from a file path."
    (execute (str base-path "testExecute.py") "A string.")))

(deftest test-execute-file
  (testing "Execute a Jython script from a file object."
    (execute (io/file (str base-path "testExecute.py")))))

(deftest test-execute-file-with-args
  (testing "Execute a Jython script from a file object."
    (execute (io/file (str base-path "testExecute.py")) "A file.")))

(deftest test-execute-stream
  (testing "Execute a Jython script from a stream."
    (execute (io/input-stream (str base-path "testExecute.py")))))

(deftest test-execute-stream-with-args
  (testing "Execute a Jython script from a stream."
    (execute (io/input-stream (str base-path "testExecute.py")) "A stream.")))

(deftest test-evaluate-string
  (testing "Evaluate a Jython script from a file path."
    (let [result (evaluate (str base-path "testEvaluate.py"))]
      (isa? Integer result)
      (is (= 25 result)))))