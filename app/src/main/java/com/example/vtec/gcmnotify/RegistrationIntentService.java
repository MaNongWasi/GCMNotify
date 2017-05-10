package com.example.vtec.gcmnotify;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

/**
 * Created by VTEC on 5/9/2017.
 */

public class RegistrationIntentService extends IntentService{
    private static final String TAG = "RegIntentService";
    public static final String INTENT_PUSH_REGISTRATION_COMPLETED = "gcmNotify_COMPLETED";
    public static final String PARAM_ERROR_MESSAGE = "ErrorMessage";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String error = null;
        try {
            synchronized (TAG) {
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                System.out.println("GCMRegIntentService + token:" + token);
                //notify to UI that registration complete success //here also ok
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            System.out.println(TAG + " Failed to complete token refresh " + e);
            error = e.getLocalizedMessage();
        }
        //notify to UI that registration complete success
        Intent registrationComplete = new Intent(INTENT_PUSH_REGISTRATION_COMPLETED);
        registrationComplete.putExtra(PARAM_ERROR_MESSAGE, error);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
}
