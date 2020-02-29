# google-credentials

A Clojure library for loading gcloud credentials from an environment variable instead of a .json file.

![Build Status](https://github.com/alekcz/google-credentials/workflows/Clojure%20CI/badge.svg) [![codecov](https://codecov.io/gh/alekcz/google-credentials/branch/master/graph/badge.svg)](https://codecov.io/gh/alekcz/google-credentials)  

[![Clojars Project](https://img.shields.io/clojars/v/alekcz/google-credentials.svg)](https://clojars.org/alekcz/google-credentials)

## Background

When interoping with the Google SDK the initialization process is more or less as follows:
1. Load credentials from file
2. Initialize credentials
3. Initialize the SDK by pass the credentials to it.
4. Access resource in the Google cloud

This library allows the credentials to be loaded from the environment variable: `GOOGLE_APPLICATION_CREDENTIALS`.
I've found this really useful when deploying applications or running CI/CD outside the Google cloud. 

You still need to perform steps 2 - 4 to get up and running. 

## Usage

`[alekcz/google-credentials "1.0.0"]`

1. Get the `json` file containing your service account creditials by following the instruction here [https://cloud.google.com/docs/authentication/getting-started](https://cloud.google.com/docs/authentication/getting-started)  
2. Copy the contents of your `.json` into the GOOGLE_APPLICATION_CREDENTIALS environment variable. In your `bash_profile` and in Travis CI you should escape your credentials using singe quotes.

```clojure
(require '[google-credentials.core :as g-cred])

    ;; Cloud Storage
    (-> (. StorageOptions newBuilder)
        (.setCredentials (g-cred/load-credentials)) 
        (.build) 
        (.getService))

    ;; Firebase
    (def firebase-options   (-> (new FirebaseOptions$Builder) 
                                (.setCredentials (g-cred/load-credentials)) 
                                (.build))   
    (.initializeApp FirebaseApp firebase-options)   

     ;; generic example
    (def cred   (-> (<options-builder>)
                    (.setCredentials (g-cred/load-credentials)) 
                    (.build))  
    (<cloud-library>/<initialisation-function> cred)

```

## License

Copyright © 2020 Alexander Oloo

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
