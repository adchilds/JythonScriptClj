(ns com.github.adchilds.jython.jythonscript-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [com.github.adchilds.jython.jythonscript :refer :all])
  (:import (org.python.core PyCode)))

(def base-path "test/resources/script/jython/")

(deftest test-compile-script-filepath
  (testing "Compiling a script into a PyCode object from a filepath."
    (let [compiledscript (compile-script (str base-path "testExecute.py"))]
      (is (not (nil? compiledscript)))
      (is (instance? PyCode compiledscript)))))

(deftest test-compile-script-file
  (testing "Compiling a script into a PyCode object from a file object."
    (let [compiledscript (compile-script (io/file (str base-path "testExecute.py")))]
      (is (not (nil? compiledscript)))
      (is (instance? PyCode compiledscript)))))

(deftest test-execute-filepath
  (testing "Execute a Jython script from a file path without args."
    (execute (str base-path "testExecute.py")))

  (testing "Execute a Jython script from a file path with args."
    (execute (str base-path "testExecute.py") "A string.")))

(deftest test-execute-file
  (testing "Execute a Jython script from a file object without args."
    (execute (io/file (str base-path "testExecute.py"))))

  (testing "Execute a Jython script from a file object with args."
    (execute (io/file (str base-path "testExecute.py")) "A file.")))

(deftest test-execute-stream
  (testing "Execute a Jython script from a stream without args."
    (execute (io/input-stream (str base-path "testExecute.py"))))

  (testing "Execute a Jython script from a stream with args."
    (execute (io/input-stream (str base-path "testExecute.py")) "A stream.")))

(deftest test-evaluate-filepath
  (testing "Evaluate a Jython script from a file path without args."
    (let [result (evaluate (str base-path "testEvaluate.py"))]
      (is (integer? result))
      (is (= 25 result))))

  (testing "Evaluate a Jython script from a file path with args."
    (let [result (evaluate (str base-path "testEvaluate.py") 7)]
      (is (integer? result))
      (is (= 35 result)))

    (let [result (evaluate (str base-path "testEvaluate.py") 7 7)]
      (is (integer? result))
      (is (= 49 result)))))

(deftest test-evaluate-file
  (testing "Evaluate a Jython script from a file object without args."
    (let [result (evaluate (io/file (str base-path "testEvaluate.py")))]
      (is (integer? result))
      (is (= 25 result))))

  (testing "Evaluate a Jython script from a file object with args."
    (let [result (evaluate (io/file (str base-path "testEvaluate.py")) 2)]
      (is (integer? result))
      (is (= 10 result)))

    (let [result (evaluate (io/file (str base-path "testEvaluate.py")) 7 10)]
      (is (integer? result))
      (is (= 70 result)))))

(deftest test-evaluate-stream
  (testing "Evaluate a Jython script from a stream without args."
    (let [result (evaluate (io/input-stream (str base-path "testEvaluate.py")))]
      (is (integer? result))
      (is (= 25 result))))

  (testing "Evaluate a Jython script from a stream with args."
    (let [result (evaluate (io/input-stream (str base-path "testEvaluate.py")) 1)]
      (is (integer? result))
      (is (= 5 result)))

    (let [result (evaluate (io/input-stream (str base-path "testEvaluate.py")) 3 9)]
      (is (integer? result))
      (is (= 27 result)))))