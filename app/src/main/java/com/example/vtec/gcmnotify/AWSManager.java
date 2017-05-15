package com.example.vtec.gcmnotify;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;

/**
 * Created by VTEC on 5/10/2017.
 */

public class AWSManager {
    private static final String TAG = "mytag";
    public static final String _BUCKET = "bucket";
    private static final String _ACCESS_KEY_ID = "access key ID";
    private static final String _SECRET_KEY = "secret key";
    private static AmazonS3Client s3Client = null;
    private static AmazonSNSClient snsClient = null;

    /**
     * returns the instance of the amazon S3 Client for this app */
    public static AmazonS3Client getS3Client(){
        if(s3Client == null){
            s3Client = new AmazonS3Client( new BasicAWSCredentials( _ACCESS_KEY_ID, _SECRET_KEY ) );
        }
        return s3Client;
    }

    /**
     * returns the instance of the amazon SNS Client for this app */
    public static AmazonSNSClient getSNSClient(){
        if(snsClient == null){
            snsClient = new AmazonSNSClient( new BasicAWSCredentials( _ACCESS_KEY_ID, _SECRET_KEY ) );
            snsClient.setRegion(Region.getRegion(Regions.EU_CENTRAL_1));
        }
        return snsClient;
    }

    public static class AWSCreateEndpointTask extends AsyncTask<String, Void, CreatePlatformEndpointResult> {

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
//                SharedPreferences prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);

                String endpointArn = result.getEndpointArn();

//                prefs.edit().putString(context.getString(R.string.endpoint_arn), endpointArn).apply();
                System.out.println("endpoint " + endpointArn);
                System.out.println("subscrip");
                new SubscribeTopicTask(context).execute(topArn, "application", endpointArn);
            }

        }

    }

    public static class SubscribeTopicTask extends AsyncTask<String, Void, SubscribeResult> {

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
