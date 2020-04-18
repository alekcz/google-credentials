(ns google-credentials.core
  (:require [environ.core :refer [env]]
            [clojure.string :as str]
            [cheshire.core :as json])
  (:import 	[com.google.auth.oauth2 GoogleCredentials ServiceAccountCredentials ServiceAccountCredentials$Builder]
            [java.io IOException]
            [java.net URI]
            [java.security PrivateKey]
            [com.google.api.client.http.javanet NetHttpTransport]
            [com.google.api.client.http HttpTransport]
            [com.google.auth.http HttpTransportFactory])
  (:gen-class))

(set! *warn-on-reflection* 1)

(def user-file-type "authorized_user")
(def service-account-type "service_account")

(defn- string->stream
  ([^String s] (string->stream s "UTF-8"))
  ([^String s ^String encoding]
   (-> s
       (.getBytes encoding)
       (java.io.ByteArrayInputStream.))))

(defn- clean-env-var [env-var]
  (-> env-var (name) (str) (str/lower-case) (str/replace "_" "-") (str/replace "." "-") (keyword)))

(defn- build-user-credentials [creds]
  (println creds))

(defn- blanker [data]
  (if (nil? data) "" data))

(defn- build-service-account-credentials [creds]
  (let [credentials (ServiceAccountCredentials/fromPkcs8
                      ^String (-> creds :client_id blanker)
                      ^String (-> creds :client_email blanker)
                      ^String (-> creds :private_key blanker)
                      ^String (-> creds :private_key_id blanker)
                      [])]
                      ;^HttpTransportFactory (reify HttpTransportFactory (create ^HttpTransport [this] (NetHttpTransport.)))
                      ;^URI (URI. (-> creds :token_uri blanker)))]
    (-> (^ServiceAccountCredentials$Builder .toBuilder credentials)
        (.setProjectId (-> creds :project_id blanker))
        (^ServiceAccountCredentials .build))))

(defn ^GoogleCredentials load-credentials 
  "Load google application credentials from environment variable defaults to  GOOGLE_APPLICATION_CREDENTIALS"
  ([]
   (load-credentials "GOOGLE_APPLICATION_CREDENTIALS"))  
  ([^String env-var]
    (let [clean-var (clean-env-var env-var)
          creds (json/decode (env clean-var) true)]
      (cond
        (empty? creds)
          (throw (IOException. (str "Environment variable " env-var "is empty or does not exist")))
        (empty? (:type creds))
          (throw (IOException. (str "Error reading credentials from stream, 'type' field not specified.")))
        (= ^String user-file-type (:type creds))
          (build-user-credentials creds)
        (= ^String service-account-type (:type creds))
          (build-service-account-credentials creds) 
        :else (throw (IOException. (str "Error reading credentials from stream, 'type' value '" (:type creds) "' not recognized."
                                        "Expecting '" user-file-type "' or '" service-account-type "'.")))))))

(defn get-project-id 
  "Get the project ID from the specified google application credentials environment variable defaults to  GOOGLE_APPLICATION_CREDENTIALS"
  ([]
   (get-project-id "GOOGLE_APPLICATION_CREDENTIALS"))
  ([^String env-var]
    (let [clean-var (clean-env-var env-var)
          creds (json/decode (env clean-var) true)]
      (:project-id creds))))                                   