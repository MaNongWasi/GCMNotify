package com.example.vtec.gcmnotify;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.sns.AmazonSNSClient;

/**
 * Created by VTEC on 5/10/2017.
 */

public class AWSManager {
    private static final String TAG = "mytag";
    public static final String _BUCKET = "bucket name";
    private static final String _ACCESS_KEY_ID = "access ID";
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
            snsClient.setRegion(Region.getRegion(region));
        }
        return snsClient;
    }
}
