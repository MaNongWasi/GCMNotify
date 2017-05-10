package com.example.vtec.gcmnotify;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by VTEC on 5/9/2017.
 */

public class MyGcmListenerService extends GcmListenerService {
    private static final String TAG = "MyGcmListenerService";

    @Override
    public void onMessageReceived(String s, Bundle bundle) {
        System.out.println("bundler " + bundle);
//        System.out.println(bundle.get("default"));
        String message = bundle.getString("message"); //from GSM and sns json format Message:{"GCM": "{ \"data\": { \"message\": \"heelp\" } }"}
        String d_mes = bundle.getString("default");
        System.out.println("message " + message);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alarm")
                .setContentText(d_mes);
        notificationManager.notify(1, mBuilder.build());

//        ReceivedMessage message = PushMessageBundleHelper.parse(data);
//        KiiUser sender = message.getSender();
//        PushMessageBundleHelper.MessageType type = message.pushMessageType();
//        switch (type) {
//            case PUSH_TO_APP:
//                PushToAppMessage appMsg = (PushToAppMessage)message;
//                Log.d(TAG, "PUSH_TO_APP Received");
//                break;
//            case PUSH_TO_USER:
//                PushToUserMessage userMsg = (PushToUserMessage)message;
//                Log.d(TAG, "PUSH_TO_USER Received");
//                break;
//            case DIRECT_PUSH:
//                DirectPushMessage directMsg = (DirectPushMessage)message;
//                Log.d(TAG, "DIRECT_PUSH Received");
//                break;
//        }

    }


//  private void sendNotification(String message) {
//    Intent intent = new Intent(this, MainActivity.class);
//    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//        PendingIntent.FLAG_ONE_SHOT);
//
//    Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//        .setSmallIcon(R.drawable.ic_stat_ic_notification)
//        .setContentTitle("GCM Message")
//        .setContentText(message)
//        .setAutoCancel(true)
//        .setSound(defaultSoundUri)
//        .setContentIntent(pendingIntent);
//
//    NotificationManager notificationManager =
//        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//    notificationManager.notify(0, notificationBuilder.build());
//  }

}

