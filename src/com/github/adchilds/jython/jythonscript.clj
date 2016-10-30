(ns com.github.adchilds.jython.jythonscript
  (:require [clojure.java.io :as io])
  (:import (java.io File InputStream)
           (org.python.util PythonInterpreter)
           (org.python.core PySystemState Py)))

;; TODO: Rename?
(defn parse-arguments
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
    (if (or (nil? args) (empty? args))
      ;; Exit early if possible
      state

      ;; Update the current PySystemState
      (doseq [arg (into-array args)]
        (.append (.argv state) (Py/java2py arg))))

    state))

(defn update-interpreter-state
  "
  Updates the {@link PythonInterpreter}s {@link PySystemState} by adding the
  given {@code args}. These arguments may be accessed from within Jython
  scripts via the 'sys.argv' parameters, beginning at the second index (i.e.
  sys.argv[1]). Note: the first index is reserved.

  :param args the arguments to set on the {@link PySystemState} for the
              current {@link PythonInterpreter}
  "
  {:added "1.0"}
  ^PythonInterpreter
  [& args]
  (let [state (apply parse-arguments args)]
    (new PythonInterpreter nil state)))

(defmulti execute
  "
  Executes the given Jython script with optional arguments passed to the
  script at runtime.

  :param file
  :param args
  "
  {:added "1.0"}
  (fn ([file & args] (class file))))

(defmethod execute String [path & args]
  "

  "
  (let [file-stream (io/input-stream path)]
    (apply execute file-stream args)))

(defmethod execute File [file & args]
  "

  "
  (let [file-stream (io/input-stream file)]
    (apply execute file-stream args)))

(defmethod execute InputStream [stream & args]
  "

  "
  (let [interpreter (apply update-interpreter-state args)]
    (.execfile interpreter stream)))

