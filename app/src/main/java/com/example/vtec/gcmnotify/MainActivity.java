package com.example.vtec.gcmnotify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.DeleteEndpointRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String errorMessage = intent.getStringExtra(RegistrationIntentService.PARAM_ERROR_MESSAGE);
                String token = intent.getStringExtra(RegistrationIntentService.PARAM_RESULT_MESSAGE);
                Log.e("GCMTest", "Registration completed:" + errorMessage);
                System.out.println("Registration completed:" + errorMessage);
                if (errorMessage != null) {
                    System.out.println("Error push registration:" + errorMessage);
                    Toast.makeText(MainActivity.this, "Error push registration:" + errorMessage, Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("Succeeded push registration");
                    Toast.makeText(MainActivity.this, "Succeeded push registration", Toast.LENGTH_LONG).show();
                    if (token != null){
                        connectAWS(token);
                    }
                }
            }
        };

        if (!checkPlayServices()) {
            Toast.makeText(MainActivity.this, "This application needs Google Play services", Toast.LENGTH_LONG).show();
            return;
        }else{
            SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
            Boolean register = sharedPreferences.getBoolean("register", false);
            if (!register){
                Intent intent = new Intent(MainActivity.this, RegistrationIntentService.class);
                System.out.println("start service");
                startService(intent);
            }
            System.out.println(register);
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("PushTest", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(RegistrationIntentService.INTENT_PUSH_REGISTRATION_COMPLETED));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    public void connectAWS(String token) {
        String email = "email";
        String application = "platform application";
        if (!TextUtils.isEmpty(token)) {
            System.out.println("connectaws");
            new AWSCreateEndpointTask(this).execute(application, token, email);

        }
    }

    public class AWSCreateEndpointTask extends AsyncTask<String, Void, CreatePlatformEndpointResult> {

        Context context;

        public AWSCreateEndpointTask(Context context) {

            super();

            this.context = context;

        }

        @Override

        protected CreatePlatformEndpointResult doInBackground(String[] params) {

            if (params.length < 3) {

                return null;

            }

            String arn = params[0];

            String gcmToken = params[1];

            String userData = params[2];

            try {
                CreatePlatformEndpointRequest request = new CreatePlatformEndpointRequest();

                request.setCustomUserData(userData);

                request.setToken(gcmToken);

                request.setPlatformApplicationArn(arn);

                return AWSManager.getSNSClient().createPlatformEndpoint(request);

            } catch (Exception ex) {

                return null;

            }

        }

        @Override

        protected void onPostExecute(CreatePlatformEndpointResult result) {
            System.out.println("result " + result);

            if (result != null) {
                String endpointArn = result.getEndpointArn();

                System.out.println("endpoint " + endpointArn);
                String topArn = "my topArn";
                System.out.println("subscrip");
                new SubscribeTopicTask(MainActivity.this).execute(topArn, "application", endpointArn);
            }

        }

    }

    public class SubscribeTopicTask extends AsyncTask<String, Void, SubscribeResult> {

        Context context;

        public SubscribeTopicTask(Context context) {

            super();

            this.context = context;

        }

        @Override

        protected  SubscribeResult doInBackground(String[] params) {

            if (params.length < 3) {

                return null;

            }

            String topArn = params[0];

            String protocol = params[1];

            String endPoint = params[2];

            System.out.println("subscribe result");

            try {
                SubscribeRequest request = new SubscribeRequest();

                request.setTopicArn(topArn);

                request.setProtocol(protocol);

                request.setEndpoint(endPoint);


                return AWSManager.getSNSClient().subscribe(request);

            } catch (Exception ex) {

                return null;

            }

        }

        @Override

        protected void onPostExecute(SubscribeResult result) {
            System.out.println("result " + result);

            if (result != null) {
                SharedPreferences prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);

                String subscriptionArn = result.getSubscriptionArn();

                prefs.edit().putBoolean("register", true).commit(); //apply();
                System.out.println("subscriptionArn " + subscriptionArn);

            }

        }

    }


}
