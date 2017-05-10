package com.example.vtec.gcmnotify;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.DeleteEndpointRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by VTEC on 5/10/2017.
 */

public class SNSTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        String gcmToken = sp.getString(getString(R.string.gcm_pref_token), null);

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getApplicationContext());

        if (TextUtils.isEmpty(gcmToken)) {

            new GCMRegisterTask(this, gcm).execute();

        }

        connectAWS();
    }

    public void connectAWS() {
        String token = PreferenceManager.getDefaultSharedPreferences(this).getString(

                this.getString(R.string.gcm_pref_token), null);
        String email = "email address";

        if (!TextUtils.isEmpty(token)) {
            new AWSCreateEndpointTask(this).execute("aws arn", token, email);

        }
    }

    public class GCMRegisterTask extends AsyncTask<String, Void, Boolean> {

        private Context context;

        private GoogleCloudMessaging gcm;

        public GCMRegisterTask(Context context, GoogleCloudMessaging gcm) {

            super();

            this.context = context;

            this.gcm = gcm;

        }

        @Override

        protected Boolean doInBackground(String... params) {

            String token;

            try {

                token = gcm.register(context.getString(R.string.gcm_project_number));

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

                sp.edit().putString(context.getString(R.string.gcm_pref_token), token).apply();

                return true;

            } catch (IOException e) {

                Log.i("Registration Error", e.getMessage());
                System.out.println("Registeration Error " + e.getMessage());

            }

            return false;

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
                SharedPreferences prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);

                String endpointArn = result.getEndpointArn();

                prefs.edit().putString(context.getString(R.string.endpoint_arn), endpointArn).apply();
                System.out.println("endpoint " + endpointArn);
            }

        }

    }

    public class AWSRemoveEndpointTask extends AsyncTask<String, Void, Boolean> {

        Context context;

        public AWSRemoveEndpointTask(Context context) {

            super();

            this.context = context;

        }

        @Override

        protected Boolean doInBackground(String[] params) {

            if (params.length < 1) {

                return false;

            }

            String arn = params[0];

            if (TextUtils.isEmpty(arn)) {

                return false;

            }

            try {

                DeleteEndpointRequest request = new DeleteEndpointRequest();

                request.setEndpointArn(arn);

                AWSManager.getSNSClient().deleteEndpoint(request);

                return true;

            } catch (Exception ex) {

                return false;

            }

        }

    }

}
