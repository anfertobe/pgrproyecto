package com.example.andres.chatandroid;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMBroadcastReceiver extends WakefulBroadcastReceiver
{
    private static final String TAG = "GcmBroadcastReceiver";
    private Context ctx;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        ctx = context;
        PowerManager mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        mWakeLock.acquire();
        try {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
            String messageType = gcm.getMessageType(intent);
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error", false);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server", false);
            } else {
                if(intent.getStringExtra("tipo").equals("CrearGrupo")){
                    String carne = "grupo " + intent.getStringExtra("id");
                    String nombre= intent.getStringExtra("nombre");
                    ContentValues values = new ContentValues(2);
                    values.put(DataProvider.COL_NAME,nombre);
                    values.put(DataProvider.COL_CARNE,carne);
                    context.getContentResolver().insert(DataProvider.CONTENT_URI_PROFILE, values);
                    if (Common.isNotify()) {
                        sendNotification("New message", true);
                    }

                }else {

                    String msg = intent.getStringExtra(DataProvider.COL_MESSAGE);
                    Log.i("msg", msg);
                    String senderEmail = intent.getStringExtra(DataProvider.COL_SENDER_EMAIL);
                    Log.i("senderEmail", senderEmail);
                    String receiverEmail = intent.getStringExtra(DataProvider.COL_RECEIVER_EMAIL);
                    ContentValues values = new ContentValues(2);
                    Log.i("Type", Integer.toString(DataProvider.MessageType.INCOMING.ordinal()));
                    values.put(DataProvider.COL_TYPE, DataProvider.MessageType.INCOMING.ordinal());
                    values.put(DataProvider.COL_MESSAGE, msg);

                    if(intent.getStringExtra("tipo").equals("Mensaje")){
                        values.put(DataProvider.COL_SENDER_EMAIL, senderEmail);
                    }else if(intent.getStringExtra("tipo").equals("Grupo")){
                        values.put(DataProvider.COL_SENDER_EMAIL, "grupo " + senderEmail);
                        String remitente2 = intent.getStringExtra(DataProvider.COL_SENDER_EMAIL2);
                        values.put(DataProvider.COL_SENDER_EMAIL2, remitente2);
                    }

                    context.getContentResolver().insert(DataProvider.CONTENT_URI_MESSAGES, values);

                    if (Common.isNotify()) {
                        sendNotification("New message", true);
                    }
                }
            }
            setResultCode(Activity.RESULT_OK);
        } finally {
            mWakeLock.release();
        }
    }

    private void sendNotification(String text, boolean launchApp) {
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(ctx);
        notification.setContentTitle(ctx.getString(R.string.app_name));
        notification.setContentText(text);
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.drawable.abc_ab_share_pack_mtrl_alpha);
        if (!TextUtils.isEmpty(Common.getRingtone())) {
            notification.setSound(Uri.parse(Common.getRingtone()));
        }

        if (launchApp) {
            Intent intent = new Intent(ctx, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pi = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pi);
        }

        mNotificationManager.notify(1, notification.build());
    }
}

