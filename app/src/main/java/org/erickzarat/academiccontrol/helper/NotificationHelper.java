package org.erickzarat.academiccontrol.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import org.erickzarat.academiccontrol.R;

/**
 * Created by erick on 5/06/16.
 */
public class NotificationHelper extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent arg1) {
        showNotification(context);
    }
//usar intent para los datos con bundle
    private void showNotification(Context context) {
        Log.i("notification", "visible");

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, NotificationHelper.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_acount)
                        .setContentTitle("Academic Control")
                        .setContentText("Esto es un recordatorio")
                .setColor(Color.alpha(R.color.colorPrimaryDark));
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
