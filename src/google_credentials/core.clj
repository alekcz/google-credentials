(ns google-credentials.core
  (:require [environ.core :refer [env]]
            [clojure.string :as str]
            [cheshire.core :as json])
  (:import 	[com.google.auth.oauth2 GoogleCredentials ServiceAccountCredentials]
            [java.io IOException])
  (:gen-class))

(set! *warn-on-reflection* 1)

(def user-file-type "authorized_user")
(def service-account-type "service_account")

(defn- clean-env-var [env-var]
  (-> env-var (name) (str) (str/lower-case) (str/replace "_" "-") (str/replace "." "-") (keyword)))

(defn- build-service-account-credentials [creds]
  (let [credentials (ServiceAccountCredentials/fromPkcs8
                      ^String (-> creds :client_id str)
                      ^String (-> creds :client_email str)
                      ^String (-> creds :private_key str)
                      ^String (-> creds :private_key_id str)
                      [])]
    (-> (^ServiceAccountCredentials$Builder .toBuilder credentials)
        (.setProjectId (-> creds :project_id str))
        (^ServiceAccountCredentials .build))))

(defn ^GoogleCredentials load-service-credentials 
  "Load google application credentials from environment variable defaults to  GOOGLE_APPLICATION_CREDENTIALS"
  ([]
   (load-service-credentials "GOOGLE_APPLICATION_CREDENTIALS"))  
  ([^String env-var]
    (let [clean-var (clean-env-var env-var)
          creds (json/decode (env clean-var) true)]
      (cond
        (empty? creds)
          (throw (IOException. ^String (str "Environment variable " env-var " is empty or does not exist")))
        (empty? (:type creds))
          (throw (IOException. ^String (str "Error reading credentials from stream, 'type' field not specified.")))
        (= ^String service-account-type (:type creds))
          (build-service-account-credentials creds) 
        :else (throw (IOException. ^String (str "Error reading credentials from stream, 'type' value '" (:type creds) "' not recognized."
                                        "Expecting '" user-file-type "' or '" service-account-type "'.")))))))
