(ns google-credentials.core
  (:require [environ.core :refer [env]]
            [clojure.string :as str])
  (:import 	com.google.auth.oauth2.GoogleCredentials)
  (:gen-class))

(defn- string->stream
  ([s] (string->stream s "UTF-8"))
  ([s encoding]
   (-> s
       (.getBytes encoding)
       (java.io.ByteArrayInputStream.))))

(defn load-credentials ^GoogleCredentials 
  []
  (GoogleCredentials/fromStream (string->stream (env :google-application-credentials))))

(defn load-custom-credentials ^GoogleCredentials 
  [env-var]
  (GoogleCredentials/fromStream (string->stream (env (-> env-var 
                                                         (name)
                                                         (str)
                                                         (str/lower-case)
                                                         (str/replace "_" "-")
                                                         (str/replace "." "-")
                                                         (keyword))))))