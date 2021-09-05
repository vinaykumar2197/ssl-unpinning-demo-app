# ssl-unpinning-demo

Let's talk about ssl unpinning in android. But before that we should be 
aware of what is ssl pinning?

SSL ( Secure Sockets Layer ) : 

This connection ensures that all data passed between the  server and app remain private and integral.
Mitm ( Man in the middle) attacks can be prevented by using SSL Pinning.

Basically in normal scenario you won't be able to intercept it using Charles or Fiddler or Burp Suite.

## SSL Unpinning
There are ways using which we can intercept the api requests.

Here we will use burp suite to intercept the network requests.



## Steps

```
To perform ssl unpinning, various steps are involved.

1. Find out sha256 pin used in the application.
   
   There are various ways, we can pin the certificate depending on what we are using. e.g. okhttp. 


        try {

//            hitting api on main thread ( enabled it using strictMode)
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

//            String hostname = "publicobject.com";
            String hostname = "badssl.com";
            CertificatePinner certificatePinner = new CertificatePinner.Builder()
                    .add(hostname, "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
                    .build();
            OkHttpClient client = new OkHttpClient.Builder()
                    .certificatePinner(certificatePinner)
                    .build();

            Request request = new Request.Builder()
                    .url("https://" + hostname)
                    .build();

            client.newCall(request).execute();
        }
         catch (Exception e) {
            e.printStackTrace();
        }

  using above code we can get the list of pinned certificates.



  Exception Data : 

  javax.net.ssl.SSLPeerUnverifiedException: Certificate pinning failure!
  Peer certificate chain:
    sha256/9SLklscvzMYj8f+52lp5ze/hY0CFHyLSPQzSpYYIBm8=: CN=*.badssl.com,O=Lucas Garron Torres,L=Walnut Creek,ST=California,C=US
    sha256/5kJvNEMw0KjrCAu7eXY5HZdvyCS13BbA0VJG1RSP91w=: CN=DigiCert SHA2 Secure Server CA,O=DigiCert Inc,C=US <================= certificate pinned in app
    sha256/r/mIkG3eEpVdm+u/ko/cwxzOMo1bk4TyHIlByibiA5E=: CN=DigiCert Global Root CA,OU=www.digicert.com,O=DigiCert Inc,C=US
  Pinned certificates for badssl.com:
    sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=

  In above exception, as you can see, by passing dummy sha256, we got the list of sha256 which is being used for  badssl.com .

  Now let's enable proxy ( using proxydroid app), and debug the same app again.

  we get different response. 

  javax.net.ssl.SSLPeerUnverifiedException: Certificate pinning failure!
  Peer certificate chain:
    sha256/aw2f0c8chnyyhYvpY+iyMuAJ5ufPfM26h+wh9C3zS1k=: CN=badssl.com,OU=PortSwigger CA,O=PortSwigger,C=PortSwigger
    sha256/aw2f0c8chnyyhYvpY+iyMuAJ5ufPfM26h+wh9C3zS1k=: CN=PortSwigger CA,OU=PortSwigger CA,O=PortSwigger,L=PortSwigger,ST=PortSwigger,C=PortSwigger
  Pinned certificates for badssl.com:
    sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=


  Let's replace certificate pinned in app ( 5kJvNEMw0KjrCAu7eXY5HZdvyCS13BbA0VJG1RSP91w= ) to proxy ( aw2f0c8chnyyhYvpY+iyMuAJ5ufPfM26h+wh9C3zS1k= ) certificate.

  Now if we check burp suite, we will be able to see api requests in http history.

```


 
![SSL Exceotion Screenshot](https://github.com/vinaykumar2197/ssl-unpinning-demo-app/sc-ssl-unpinning.png)
![Burp Suite Screenshot](https://github.com/vinaykumar2197/ssl-unpinning-demo-app/sc-burp-suite.png)


## Authors

- [@Vinaykumar](https://www.github.com/vinaykumar2197)


## License
Copyright 2021 Vinaykumar Mishra

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
