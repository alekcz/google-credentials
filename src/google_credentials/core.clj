(ns google-credentials.core
  (:require [environ.core :refer [env]]
            [cheshire.core :as json])
  (:import 	com.google.auth.oauth2.GoogleCredentials
            com.google.auth.oauth2.GoogleCredentials$Builder)
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

(defn get-project-id [] 
  (let [cred (json/parse-string (env :google-application-credentials) true)]
    (:project_id cred)))