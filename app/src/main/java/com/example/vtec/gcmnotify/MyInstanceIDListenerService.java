package com.example.vtec.gcmnotify;

import android.app.Service;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by VTEC on 5/9/2017.
 */

public class MyInstanceIDListenerService extends InstanceIDListenerService {
    private static  final String TAG = "IDListenerService";
    @Override
    public void onTokenRefresh() {
        System.out.println("onTokenRefresh");
        try{
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }catch (Exception e){
            Log.d(TAG, "Failed to complete token refresh", e);
        }

    }
}
