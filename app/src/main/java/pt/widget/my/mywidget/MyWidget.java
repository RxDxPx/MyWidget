package pt.widget.my.mywidget;

//import android.Manifest;
////import android.app.Activity;
//import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.preference.PreferenceManager;
//import android.telephony.SmsManager;
//import android.view.View;
//import android.widget.Button;
//import android.widget.RemoteViews;
//import android.widget.Toast;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
import android.content.ComponentName;
//import java.util.Random;

////////////////////////////////////////


import android.app.Activity;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.SEND_SMS;

/**
 * Created by x335405 on 31/12/2017.
 */

public class MyWidget extends AppWidgetProvider {

    private static final int REQUEST_SMS = 0;

    private static final String ACTION_UPDATE_CLICK =
            "com.example.myapp.action.UPDATE_CLICK";


    private static final String SMS_SENT =
            "SMS_SENT";

    private static final String SMS_DELIVERED =
            "SMS_DELIVERED";

    private static int mCount = 0;

    private static String getMessage() {
        return String.valueOf(mCount++);
    }

    private PendingIntent getPendingSelfIntent(Context context, String action) {
        // An explicit intent directed at the current class (the "self").
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private PendingIntent getPendingSelfIntent(Context context, String action, String phone) {
        // An explicit intent directed at the current class (the "self").
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        intent.putExtra("telefone", phone);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

//
//        sentStatusReceiver=new BroadcastReceiver() {
//
//            @Override
//            public void onReceive(Context arg0, Intent arg1) {
//                String s = "Unknown Error";
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        s = "Message Sent Successfully !!";
//                        break;
//                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        s = "Generic Failure Error";
//                        break;
//                    case SmsManager.RESULT_ERROR_NO_SERVICE:
//                        s = "Error : No Service Available";
//                        break;
//                    case SmsManager.RESULT_ERROR_NULL_PDU:
//                        s = "Error : Null PDU";
//                        break;
//                    case SmsManager.RESULT_ERROR_RADIO_OFF:
//                        s = "Error : Radio is off";
//                        break;
//                    default:
//                        break;
//                }
//                Toast.makeText(context, "DENTRO-" + s, Toast.LENGTH_SHORT).show();
//
//            }
//        };
//
//        deliveredStatusReceiver=new BroadcastReceiver() {
//
//            @Override
//            public void onReceive(Context arg0, Intent arg1) {
//                String s = "Message Not Delivered";
//                switch(getResultCode()) {
//                    case Activity.RESULT_OK:
//                        s = "Message Delivered Successfully";
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        break;
//                }
//                Toast.makeText(context, "DENTRO-" + s, Toast.LENGTH_SHORT).show();
//            }
//        };

        String message = getMessage();

        // Loop for every App Widget instance that belongs to this provider.
        // Noting, that is, a user might have multiple instances of the same
        // widget on
        // their home screen.
        for (int appWidgetID : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.my_widget);

            //remoteViews.setTextViewText(R.id.textView_output, message);
            //remoteViews.setTextViewText(R.id.refreshBtn, "TEST " + message);
            Toast.makeText(context,"update "+message,Toast.LENGTH_LONG).show();

            remoteViews.setTextViewText(R.id.text_sent1, "0");
            remoteViews.setTextViewText(R.id.text_sent2, "0");
            remoteViews.setTextViewText(R.id.text_deliver1, "0");
            remoteViews.setTextViewText(R.id.text_deliver2, "0");

            remoteViews.setOnClickPendingIntent(R.id.refreshBtn,
                    getPendingSelfIntent(context,
                            ACTION_UPDATE_CLICK)
            );

            appWidgetManager.updateAppWidget(appWidgetID, remoteViews);

        }
    }

    /**
     * A general technique for calling the onUpdate method,
     * requiring only the context parameter.
     *
     * @author John Bentley, based on Android-er code.
     * @see <a href="http://android-er.blogspot.com
     * .au/2010/10/update-widget-in-onreceive-method.html">
     * Android-er > 2010-10-19 > Update Widget in onReceive() method</a>
     */
    private void onUpdate(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance
                (context);

        // Uses getClass().getName() rather than MyWidget.class.getName() for
        // portability into any App Widget Provider Class
        ComponentName thisAppWidgetComponentName =
                new ComponentName(context.getPackageName(),getClass().getName()
                );
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                thisAppWidgetComponentName);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }

//    private boolean checkPermission() {
//        return ( ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS ) == PackageManager.PERMISSION_GRANTED);
//    }
//
//    private void requestPermission() {
//        ActivityCompat.requestPermissions(this, new String[]{SEND_SMS}, REQUEST_SMS);
//    }
//
//
//    private static final int PERMISSION_SEND_SMS = 123;
//    private void requestSmsPermission(Context context) {
//
//        // check permission is given
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
//            // request permission (see result in onRequestPermissionsResult() method)
//            ActivityCompat.requestPermissions((Activity)this,
//                    new String[]{Manifest.permission.SEND_SMS},
//                    PERMISSION_SEND_SMS);
//        } else {
//            // permission already granted run sms send
//            //sendSms(phone, message);
//        }
//    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        String s;
        String intentCode = intent.getAction();
        String telefone  = "";
        if(intent.getExtras() != null)
            telefone = intent.getExtras().getString("telefone");
        Toast.makeText(context, intentCode, Toast.LENGTH_SHORT).show();
        switch(intentCode){

            case ACTION_UPDATE_CLICK:

                onUpdate(context);

                //requestSmsPermission(context);
                /////////////////////////////////////////////

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    int hasSMSPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS);
                    //int hasSMSPermission = ActivityCompat.checkSelfPermission((Activity)context, Manifest.permission.SEND_SMS);
                    if (hasSMSPermission != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(context, "Sem permissãp " + hasSMSPermission, Toast.LENGTH_SHORT).show();
//                        if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, Manifest.permission.SEND_SMS)) {
//                            showMessageOKCancel(context, "You need to allow access to Send SMS",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                                ActivityCompat.requestPermissions((Activity)context, new String[] {Manifest.permission.SEND_SMS},
//                                                        REQUEST_SMS);
//                                            }
//                                        }
//                                    });
//                            return;
//                        }
//                        ActivityCompat.requestPermissions((Activity)context, new String[] {Manifest.permission.SEND_SMS},
//                                REQUEST_SMS);
                        return;
                    }
                    else
                        sendMySMS(context);
                }

                break;

            case SMS_SENT:

                s = "Unknown Error";
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        s = "Message Sent Successfully !!";

                        if(telefone.equals("969336649")) {

                            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.my_widget);
                            remoteViews.setTextViewText(R.id.text_sent1, "1");

                            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
                            ComponentName thisWidget = new ComponentName(context.getApplicationContext(), MyWidget.class);
                            int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
                            for (int i=0; i < allWidgetIds.length; i++) {
                                //Log.i(TAG, "WID ID: " + allWidgetIds[i]);
                                appWidgetManager.updateAppWidget(allWidgetIds[i], remoteViews);
                            }
                        }

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
                Toast.makeText(context, s + " " + telefone, Toast.LENGTH_SHORT).show();

                break;

            case SMS_DELIVERED:

                s = "Message Not Delivered";
                switch(getResultCode()) {
                    case Activity.RESULT_OK:
                        s = "Message Delivered Successfully";

                        if(telefone.equals("969336649")) {
                            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.my_widget);
                            remoteViews.setTextViewText(R.id.text_deliver1, "1");

                            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
                            ComponentName thisWidget = new ComponentName(context.getApplicationContext(), MyWidget.class);
                            int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
                            for (int i=0; i < allWidgetIds.length; i++) {
                                //Log.i(TAG, "WID ID: " + allWidgetIds[i]);
                                appWidgetManager.updateAppWidget(allWidgetIds[i], remoteViews);
                            }
                        }

                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                //deliveryStatusTextView.setText(s);
                //phoneEditText.setText("");
                //messageEditText.setText("");
                Toast.makeText(context, s + " " + telefone, Toast.LENGTH_SHORT).show();

                break;
        }
    }

    private void showMessageOKCancel(Context context, String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    public void sendMySMS(final Context context) {

        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(context);
        String mensagemPref = spf.getString("edit_text_preference_1","por definir");
        String telefonesPref = spf.getString("preference_telefones","por definir");

        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
        int oitoEquarenteEcinco = (8 * 60) + 45;
        Calendar date = Calendar.getInstance();
        Date afterAddingMins = new Date((date.getTimeInMillis()) + (oitoEquarenteEcinco * ONE_MINUTE_IN_MILLIS));
        String time = localDateFormat.format(afterAddingMins);

        String phones = telefonesPref;
        String message = mensagemPref + " " + time;

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
                    //PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT"), 0);
                    //PendingIntent deliveredIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_DELIVERED"), 0);

                    PendingIntent sentIntent = getPendingSelfIntent(context, "SMS_SENT", phone);
                    PendingIntent deliveredIntent = getPendingSelfIntent(context, "SMS_DELIVERED", phone);

                    sms.sendTextMessage(phone, null, msg, sentIntent, deliveredIntent);

                    //ContextWrapper cWreceiver = new ContextWrapper(context);
                    //cWreceiver.registerReceiver(sentStatusReceiver, new IntentFilter("SMS_SENT"));
                    //cWreceiver.registerReceiver(deliveredStatusReceiver, new IntentFilter("SMS_DELIVERED"));
                    //registerReceiver(sentStatusReceiver, new IntentFilter("SMS_SENT"));
                }
            }

            /////////////////////////////////////////

        }
    }

    private BroadcastReceiver sentStatusReceiver, deliveredStatusReceiver;
}