(defproject alekcz/google-credentials "3.0.1"
  :description "A Clojure library for loading gcloud credentials from an environment variable instead of a .json file."
  :url "https://github.com/alekcz/google-credentials"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [environ "1.1.0"]
                 [com.google.auth/google-auth-library-oauth2-http "0.20.0"]
                 [cheshire "5.8.1"]]
  :aot :all                
  :repl-options {:init-ns google-credentials.core}
  :plugins  [[lein-cloverage "1.1.2"]])
