package com.developersanjeev.notifyme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button button_notify;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final int NOTIFICATION_ID = 0;
    private NotificationManager mNotifyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        button_notify = findViewById(R.id.notify);
        button_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
    }

    private NotificationCompat.Builder getNotificationBuilder(){
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this,PRIMARY_CHANNEL_ID)
                .setContentTitle("You have been notified!")
                .setContentText("This is your notification text!")
                .setSmallIcon(R.drawable.ic_android);
        return notifyBuilder;
    }

    public void createNotificationChannel(){

        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // Create a notification channel
            NotificationChannel channel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setDescription("Notification form Mascot");
            mNotifyManager.createNotificationChannel(channel);
        }
    }

    public void sendNotification(){
        NotificationCompat.Builder builder = getNotificationBuilder();
        mNotifyManager.notify(NOTIFICATION_ID,builder.build());
    }
}
