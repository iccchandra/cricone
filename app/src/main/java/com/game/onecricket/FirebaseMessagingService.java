package com.game.onecricket;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.game.onecricket.activity.HomeActivity;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingService";





    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("ch01", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

      System.out.println("Message received "+remoteMessage.getNotification().getBody());

        // Create Notification
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410, intent,
                PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Message")
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }


    private void sendNotification(String title, String message) {
        Intent intent = null;
        if (title != null && !title.isEmpty()) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.clap);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_leadership).setTicker("1Cricket").setWhen(0)
                    .setContentTitle(title)
                    .setAutoCancel(true)
                    .setSound(sound)
                    .setContentIntent(pendingIntent)
                     .setStyle(new NotificationCompat.BigTextStyle().bigText(message)
                    .bigText("Much longer text that cannot fit one line..."))
                    .setPriority(NotificationCompat.PRIORITY_HIGH);


            //intent = new Intent(this, SplashScreen.class);
            //intent.putExtra("Title", title);
            //intent.putExtra("Message", message);
            final Uri NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.clap);

            String CHANNEL_ID = "ch01";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.round_logo)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(NOTIFICATION_SOUND_URI)
                    .setContentIntent(pendingIntent);






        }
        if(intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            String CHANNEL_ID = "ch01";



            final Uri NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.clap);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.round_logo)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID)
                    .setVibrate(new long[]{1000, 1000})
                    .setSound(NOTIFICATION_SOUND_URI)
                    .setContentIntent(pendingIntent);


            Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.clap);
            NotificationCompat.Builder notificationBuilder1 = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_leadership)
                    .setContentTitle(title)
                    .setAutoCancel(true)
                    .setSound(sound)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Much longer text that cannot fit one line..."))
                    .setPriority(NotificationCompat.PRIORITY_HIGH);


            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID,
                        getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(mChannel);
                }
            }
            if (notificationManager != null) {
                notificationManager.notify(0, notificationBuilder.build());
            }
        }
    }


    /**
     * Create and show a custom notification containing the received FCM message.
     *
     * @param notification FCM notification payload received.
     * @param data FCM data payload received.
     */
    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_leadership);
        System.out.println("Notoi:"+notification.getTitle() + notification.getBody());
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.sound);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                                                .build();
        // Notification Channel is required for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id",
                                                                "channel_name",
                                                                       NotificationManager.IMPORTANCE_HIGH);

            channel.setDescription(notification.getTitle());
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setSound(soundUri, audioAttributes);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                                                        .setContentTitle(notification.getTitle())
                                                        .setContentText(notification.getBody())
                                                        .setAutoCancel(true)
                                                        .setSound(soundUri)
                                                         .setSmallIcon(R.mipmap.ic_leadership).setTicker("1Cricket").setWhen(0)
                                                         .setContentIntent(pendingIntent)
                                                        .setContentInfo(notification.getTitle())
                                                        .setStyle(new NotificationCompat.BigTextStyle().bigText(notification.getBody()))
                                                        .setLargeIcon(icon)
                                                        .setColor(Color.RED)
                                                        .setLights(Color.RED, 1000, 300)
                                                        .setDefaults(Notification.DEFAULT_ALL)
                                                        .setPriority(NotificationCompat.PRIORITY_HIGH);



        if (notificationManager != null) {
            notificationManager.notify(0, notificationBuilder.build());
        }
    }


}