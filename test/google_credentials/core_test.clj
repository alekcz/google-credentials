(ns google-credentials.core-test
  (:require [clojure.test :refer :all]
            [google-credentials.core :as g-cred]
            [cheshire.core :as json]
            [environ.core :refer [env]]))

(deftest test-loading-credential-from-env
  (testing "Testing whether the credential can be successfully loaded from environment"
    (let [credentials (g-cred/load-credentials)]
      (is (= com.google.auth.oauth2.ServiceAccountCredentials (-> credentials type))))))

(deftest test-credential-values-to-file-contents
  (testing "Testing whether data in the .json file is correctly read into the credential."
    (let [cred (g-cred/load-credentials) cred-file (json/parse-string (env :google-application-credentials) true)]
      (is (= (:project_id cred-file) (.getProjectId cred)))
      (is (= (:client_id cred-file) (.getClientId cred)))
      (is (= (:client_email cred-file) (.getClientEmail cred)))
      (is (= (:private_key_id cred-file) (.getPrivateKeyId cred))))))
