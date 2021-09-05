package com.vinay.ssl_unpinning_demo_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;

import java.io.IOException;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpBrokenCertificatePinning();
    }

    private void setUpBrokenCertificatePinning(){


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
    }
}