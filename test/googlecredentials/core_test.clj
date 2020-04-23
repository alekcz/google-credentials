(ns googlecredentials.core-test
  (:require [clojure.test :refer :all]
            [googlecredentials.core :as g-cred]
            [cheshire.core :as json]
            [environ.core :refer [env]]))

(deftest test-loading-credential-from-env
  (testing "Testing whether the google credentials can be successfully loaded from environment"
    (let [credentials (g-cred/load-service-credentials)]
      (is (= com.google.auth.oauth2.ServiceAccountCredentials (-> credentials type))))))

(deftest test-loading-custom-credential-from-env
  (testing "Testing whether the firebase credentials can be successfully loaded from environment"
    (let [credentials (g-cred/load-service-credentials "FIREBASE_CONFIG")]
      (is (= com.google.auth.oauth2.ServiceAccountCredentials (-> credentials type))))))

(deftest test-loading-custom-credential-from-env-2
  (testing "Testing whether the firebase credentials can be successfully loaded from environment"
    (let [credentials (g-cred/load-service-credentials :firebase-config)]
      (is (= com.google.auth.oauth2.ServiceAccountCredentials (-> credentials type))))))      

(defn exception? [f]
  (= "failed" (try (f) (catch Exception _ "failed"))))

(deftest test-loading-custom-credential-from-env-3
  (testing "Testing exception"
    (is (exception? #(g-cred/load-service-credentials :missing)))))

(deftest test-loading-custom-credential-from-env-4
  (testing "Testing exception"
    (is (exception? #(g-cred/load-service-credentials :fire2)))))

(deftest test-loading-custom-credential-from-env-5
  (testing "Testing exception"
    (is (exception? #(g-cred/load-service-credentials :fire3)))))

(deftest test-loading-custom-credential-from-env-6
  (testing "Testing exception"
    (is (exception? #(g-cred/load-service-credentials :fire4)))))

(deftest test-loading-custom-credential-from-env-7
  (testing "Testing exception"
    (is (exception? #(g-cred/load-service-credentials :empty)))))

(deftest test-credential-values-to-file-contents
  (testing "Testing whether data in the .json file is correctly read into the credential."
    (let [cred (g-cred/load-service-credentials) cred-file (json/parse-string (env :google-application-credentials) true)]
      (is (= (:project_id cred-file) (.getProjectId cred)))
      (is (= (:client_id cred-file) (.getClientId cred)))
      (is (= (:client_email cred-file) (.getClientEmail cred)))
      (is (= (:private_key_id cred-file) (.getPrivateKeyId cred))))))
