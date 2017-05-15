package com.example.vtec.gcmnotify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

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
                    if (token != null) {
                        connectAWS(token);
                    }
                }
            }
        };

        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        Boolean register = sharedPreferences.getBoolean("register", false);

        if (!register) {
            if (!checkPlayServices()) {
                Toast.makeText(MainActivity.this, "This application needs Google Play services", Toast.LENGTH_LONG).show();
                return;
            } else {

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
        String application = "arn of application flatform";
        if (!TextUtils.isEmpty(token)) {
            System.out.println("connectaws");
            new AWSManager.AWSCreateEndpointTask(this).execute(application, token, email);

        }
    }

}
