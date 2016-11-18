(ns com.github.adchilds.jython.jythonscript
  (:require [clojure.java.io :as io])
  (:import (java.io File InputStream)
           (org.python.util PythonInterpreter)
           (org.python.core PySystemState Py PyString PyBoolean PyInteger PyFloat PyLong)))

(def EVALUATION_RESULT_LOCAL_VARIABLE "result")

(defn- get-py-state
  "
  Given an arbitrary sequence of arguments, parses each individually into
  it's corresponding Jython type, before appending the value to the set of
  arguments to be passed to the executing script via the produced PySystemState
  object.

  :param args the arguments to parse before being sent to a Python script
  "
  {:added "1.0"}
  ^PySystemState
  [& args]
  (let [state (new PySystemState)]
    (if (or (nil? args) (empty? args) (every? nil? args))
      ;; Exit early if possible
      state

      ;; Update the current PySystemState
      (doseq [arg (into-array (flatten args))]
        (.append (.argv state) (Py/java2py arg))))

    state))

(defn- get-interpreter
  "
  Creates a new {@link PythonInterpreter} with {@link PySystemState} by initializing
  it with the given arguments. These arguments may be accessed from within Jython
  scripts via the 'sys.argv' parameters, beginning at the second index (i.e.
  sys.argv[1]). Note: the first index is reserved.

  :param args the arguments to set on the {@link PySystemState} for the
              current {@link PythonInterpreter}
  "
  {:added "1.0"}
  ^PythonInterpreter
  [& args]
  (let [state (apply get-py-state args)]
    (new PythonInterpreter nil state)))

(defmulti compile-script
  "
  Compiles the given Jython script into a PyCode object.

  :param file one of the supported file type arguments
  :param args arguments to be passed to the script
  "
  {:added "1.0"}
  (fn ([file & args] (class file))))

(defmethod compile-script String [path]
  (compile-script (io/file path)))

(defmethod compile-script File [file]
  (let [interpreter (get-interpreter)]
    (.compile interpreter ^String (slurp file))))

(defmulti parse-result
  "
  Given a PyObject attempts to convert to it's Java representation. If an
  equivalent java type cannot be found, the original PyObject is returned.

  Current supported type conversions:
  * PyBoolean to boolean
  * PyInteger to int
  * PyString to String
  * PyFloat to float
  * PyLong to long

  :param result the object to convert to it's equivalent Java type, if
  supported; otherwise, returns the unconverted PyObject
  "
  {:added "1.0"}
  (fn [result] (class result)))

(defmethod parse-result PyString [result]
  (.getString result))

(defmethod parse-result PyBoolean [result]
  (Py/py2boolean result))

(defmethod parse-result PyInteger [result]
  (Py/py2int result))

(defmethod parse-result PyFloat [result]
  (Py/py2float result))

(defmethod parse-result PyLong [result]
  (Py/py2long result))

(defmethod parse-result :default [result]
  result)

(defmulti execute
  "
  Executes the given Jython script with optional arguments passed to the
  script at runtime. This function, given a file argument will delegate
  to the corresponding function that handles that type of argument. Current
  supported file arguments are as follows:

  * String - a relative or full path of a file
  * File - a File object
  * InputStream - a File's stream

  :param file one of the supported file type arguments
  :param args arguments to be passed to the script
  "
  {:added "1.0"}
  (fn ([file & args] (class file))))

(defmethod execute String [path & args]
  (apply execute (io/input-stream path) args))

(defmethod execute File [file & args]
  (apply execute (io/input-stream file) args))

(defmethod execute InputStream [stream & args]
  (let [interpreter (apply get-interpreter args)]
    (.execfile interpreter stream)
    interpreter))

(defmulti evaluate
  "
  Evaluates the given Jython script, returning the result as it's equivalent
  Java type. Accepts optional arguments to be passed to the script at runtime.
  Current supported file arguments are as follows:

  * String - a relative or full path of a file
  * File - a File object
  * InputStream - a File's stream

  :param file one of the supported file type arguments
  :param args arguments to be passed to the script
  "
  {:added "1.0"}
  (fn ([file & args] (class file))))

(defmethod evaluate String [path & args]
  (apply evaluate (io/input-stream path) args))

(defmethod evaluate File [file & args]
  (apply evaluate (io/input-stream file) args))

(defmethod evaluate InputStream [stream & args]
  (parse-result (.get (execute stream args) EVALUATION_RESULT_LOCAL_VARIABLE)))
