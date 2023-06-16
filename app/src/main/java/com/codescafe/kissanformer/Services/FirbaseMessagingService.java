package com.codescafe.kissanformer.Services;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.auth.FarmerManager;
import com.codescafe.kissanformer.auth.SupportTeamManager;
import com.codescafe.kissanformer.farmer.FarmerHome;
import com.codescafe.kissanformer.supportteam.SupportTeamHome;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.List;
import java.util.Random;

public class FirbaseMessagingService extends FirebaseMessagingService {

    FarmerManager farmerManager;
    SupportTeamManager supportTeamManager;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;

    private int count = 0;
    SharedPreferences sharedPreferences;
    Bitmap largeIcon;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String NotificationType = remoteMessage.getData().get("NotificationType");
        Log.e("mytag", "onMessageReceived");
        if (!(NotificationType == null)) {
            Log.e("mytag", "if");
            if (NotificationType.equals("MessageNotification")) {
                Log.e("mytag", "MessageNotification");
                UserNotificationHandler(remoteMessage);
            } else if (NotificationType.equals("AdminMessageNotification")) {
                Log.e("mytag", "AdminMessageNotification");
                AdminNotificationReciever(remoteMessage);
            } else if (NotificationType.equals("DiscountNotification")) {
                Log.e("mytag", "DiscountNotification");
                DiscountNotification(remoteMessage);
            }
        }
    }

    private void DiscountNotification(RemoteMessage remoteMessage) {
        String title = remoteMessage.getData().get("title");
        largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        String msg = remoteMessage.getData().get("msg");
        String product = remoteMessage.getData().get("ProductModel");
        Gson gson = new Gson();
//        ProductModel productModel = gson.fromJson(product, ProductModel.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("product", productModel);
//        Intent intent=new Intent(this, ProductPreview.class);
//        intent.putExtra("model",productModel);
//        intent.putExtra("title",title);
//        intent.putExtra("NotificationType","DiscountNotification");
//        intent.putExtra("bundle", bundle);
//        Log.e("mytag",product);
//        Log.e("mytag",productModel.getDiscount());
        // PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationchannel();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = new Random().nextInt(3000);
        notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(msg)
                .setSound(ringUri)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg))
                .setAutoCancel(true);

//        if (Utils.signUpModel!=null&&Utils.signUpModel.getName()!=null){
//            Log.e("mytag","if");
//            notificationManager.notify(notificationId, notificationBuilder.build());
//        }
//        else {
//            Log.e("mytag","else");
//            //   notificationManager.notify(notificationId, notificationBuilder.build());
//        }
    }

    private void AdminNotificationReciever(RemoteMessage remoteMessage) {
        String title = remoteMessage.getData().get("title");
        largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        String msg = remoteMessage.getData().get("msg");
        String hisId = remoteMessage.getData().get("to");
        Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent=new Intent(getApplicationContext(), SupportTeamHome.class);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.e("mytag","SDK_INT");
            pendingIntent = PendingIntent.getActivity(this,
                    0, intent,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_MUTABLE);
        }
        else {
            pendingIntent = PendingIntent.getActivity(this,
                    0,intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
        notificationchannel();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = new Random().nextInt(3000);
        notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(msg)
                .setSound(ringUri)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        supportTeamManager = new SupportTeamManager(getApplicationContext());
        try {
            if (!(supportTeamManager.getUserDetails("userDetail").getAdmin_id().equals(""))) {
                if (supportTeamManager.getUserDetails("userDetail").getAdmin_id().equals(hisId)) {
                    notificationManager.notify(notificationId, notificationBuilder.build());
                }
            }
        }catch (Exception e){

        }


    }

    private void UserNotificationHandler(RemoteMessage remoteMessage) {
        largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        String msg = remoteMessage.getData().get("msg");
        String title = remoteMessage.getData().get("title");
        String hisId = remoteMessage.getData().get("to");
        Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent=new Intent(getApplicationContext(), FarmerHome.class);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.e("mytag","SDK_INT");
            pendingIntent = PendingIntent.getActivity(this,
                    0, intent,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_MUTABLE);
        }
        else {
            pendingIntent = PendingIntent.getActivity(this,
                    0,intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
        notificationchannel();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = new Random().nextInt(3000);
        notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(msg)
                .setSound(ringUri)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        farmerManager = new FarmerManager(getApplicationContext());
        try {
            if (!(farmerManager.getUserDetails("userDetail").getId().equals(""))){
                if (farmerManager.getUserDetails("userDetail").getId().equals(hisId)){
                    notificationManager.notify(notificationId, notificationBuilder.build());
                }
            }

        }catch (Exception e){

        }

    }

    public static boolean isAppForground(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> l = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : l) {
            if (info.uid == context.getApplicationInfo().uid && info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    private void notificationDialog() {
        if (!isAppForground(getApplicationContext())) {

        }
    }

    public void notificationchannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "personal notificatiobn";
            String discription = "for All personal notificatiobn";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new
                    NotificationChannel("channel_id", name, importance);
            notificationChannel.setDescription(discription);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            if (notificationManager != null)
                notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d("TAG", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        //  sendRegistrationToServer(token);
    }
}
