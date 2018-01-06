package pt.widget.my.mywidget;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.content.ComponentName;
import java.util.Random;

/**
 * Created by x335405 on 28/12/2017.
 */

public class MyWidgetProvider extends AppWidgetProvider {

    int numero;
    private static final String SYNC_CLICKED    = "automaticWidgetSyncButtonClick";

//    private Button refreshButton;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        refreshButton = (Button) findViewById(R.id.button_settings);
//
//        refreshButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//                Intent intent = new Intent(getApplicationContext(), Settings.class);
//                startActivity(intent);
//            }
//        });

        numero = 0;

        RemoteViews remoteViews;
        ComponentName watchWidget;

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.my_widget);
        watchWidget = new ComponentName(context, MyWidgetProvider.class);

        remoteViews.setOnClickPendingIntent(R.id.refreshBtn, getPendingSelfIntent(context, SYNC_CLICKED));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);

//        final int nb = appWidgetIds.length;
//
//            for (int i = 0; i<nb;i++)
//            {
//                int widgetId = appWidgetIds[i];
//                //String number = String.format("%03d", (new Random().nextInt(900) + 100));
//
//                SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(context);
//                String mensagemPref = spf.getString("edit_text_preference_1","por definir");
//                String telefonesPref = spf.getString("preference_telefones","por definir");
//
//                SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
//                long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
//                int oitoEquarenteEcinco = (8 * 60) + 45;
//
//                Calendar date2 = Calendar.getInstance();
//                date2.add(Calendar.MINUTE, oitoEquarenteEcinco);
//                String time2 = localDateFormat.format(new Date(date2.getTimeInMillis()));
//
//                mensagemPref += " " + time2;
//
//                Toast.makeText(context,mensagemPref,Toast.LENGTH_LONG).show();
//
//                sendMySMS(context, telefonesPref, mensagemPref);

                ////////////////////////////////////////////////

//                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.my_widget);
//                //remoteViews.setTextViewText(R.id.text, mensagemPref);
//                Intent intent = new Intent(context, MyWidgetProvider.class);
//                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
//
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                //remoteViews.setOnClickPendingIntent(R.id.refreshBtn, pendingIntent);
//
//                appWidgetManager.updateAppWidget(widgetId, remoteViews);
//            }
    }


    public void sendMySMS(Context context, String telefonesPref, String mensagemPref) {

        String phones = telefonesPref;
        String message = mensagemPref;

        if (phones.isEmpty()) {//Check if the phoneNumber is empty
            Toast.makeText(context, "Please Enter a Valid Phone Number", Toast.LENGTH_SHORT).show();
        } else if (message.isEmpty()) {//Check if the phoneNumber is empty
            Toast.makeText(context, "Please Enter a Valid Message", Toast.LENGTH_SHORT).show();
        } else {
            SmsManager sms = SmsManager.getDefault();
            // if message length is too long messages are divided
            for (String phone : phones.split(",")) {
                List<String> messages = sms.divideMessage(message);
                for (String msg : messages) {
                    PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT"), 0);
                    PendingIntent deliveredIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_DELIVERED"), 0);
                    sms.sendTextMessage(phone, null, msg, sentIntent, deliveredIntent);
                }
            }
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onReceive(context, intent);
        numero++;
        if (SYNC_CLICKED.equals(intent.getAction())) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews remoteViews;
            ComponentName watchWidget;

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.my_widget);
            watchWidget = new ComponentName(context, MyWidgetProvider.class);

            remoteViews.setTextViewText(R.id.refreshBtn, "TEST " + numero);

            appWidgetManager.updateAppWidget(watchWidget, remoteViews);

        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private BroadcastReceiver sentStatusReceiver, deliveredStatusReceiver;

    public void onResume() {
        //super.onResume();
        sentStatusReceiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                String s = "Unknown Error";
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        s = "Message Sent Successfully !!";
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        s = "Generic Failure Error";
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        s = "Error : No Service Available";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        s = "Error : Null PDU";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        s = "Error : Radio is off";
                        break;
                    default:
                        break;
                }
                //sendStatusTextView.setText(s);
                //Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }
        };
        deliveredStatusReceiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                String s = "Message Not Delivered";
                switch(getResultCode()) {
                    case Activity.RESULT_OK:
                        s = "Message Delivered Successfully";
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                //deliveryStatusTextView.setText(s);
                //phoneEditText.setText("");
                //7messageEditText.setText("");
                //Toast.makeText(context, "Please Enter a Valid Phone Number", Toast.LENGTH_SHORT).show();
            }
        };
        //registerReceiver(sentStatusReceiver, new IntentFilter("SMS_SENT"));
        //registerReceiver(deliveredStatusReceiver, new IntentFilter("SMS_DELIVERED"));
    }

}
