package com.example.dotnet.epomoload;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


// eSMS v1.2 = add dialog box
// eSMS v1.2.1 = change the number of dialog to 77200
// eSMS v1.2.2 = for testing add carrier + number in dialog + toast Message.

//Dialog,Hutch, => 77100, Mobitel => 2244, Tigo=> 4499,

public class MainActivity extends AppCompatActivity {
    private String Number = "";
    private String NumberTest = "723335500";
    private String SMSContent = "REG wasi";  //REG wasi //start jaya for ROBI SIM
    private String carrierName = "";

    EditText phnNumberET;
    EditText NumberET;

    String ussdCode = Uri.encode("#") + "775" + "*" + "855" + Uri.encode("#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phnNumberET = (EditText) findViewById(R.id.PhoneNumberED);
        NumberET = (EditText) findViewById(R.id.ammountED);

        TelephonyManager manager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        carrierName = manager.getNetworkOperatorName();

        //  phnNumberET.setText(carrierName);


        SetCarrierName();
      //  formatesSMSSent(SMSContent);

        //  NumberET.setText(Number);
        UssdFunc();
    }

    public void reloadNow(View view) {
        Message("Confirm", "Please take a screenshot & give send it to me for Checking the Operator Name & Number" + carrierName + " " + Number); //Please make sure you have entered correct number
    }

    // #775*855#
    public void GetSubsUSSD(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdCode)));
    }

    public void UssdFunc() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdCode)));
    }

    private void SetCarrierName(){
        if(carrierName.toUpperCase().equals("DIALOG".toUpperCase())){
           //Toast.makeText(getApplicationContext(),"Banglalink",Toast.LENGTH_LONG).show();
            Number = "77200";
        }else if(carrierName.toUpperCase().equals("Hutch".toUpperCase())){
           // Toast.makeText(getApplicationContext(),"Banglalink",Toast.LENGTH_LONG).show();
            Number = "77200";
        }else if(carrierName.toUpperCase().equals("Mobitel".toUpperCase())){
            //Toast.makeText(getApplicationContext(),"Teletalk",Toast.LENGTH_LONG).show();
            Number = "2244";
        }else if(carrierName.toUpperCase().equals("Etisalat".toUpperCase())){
            //Toast.makeText(getApplicationContext(),"Robi",Toast.LENGTH_LONG).show();
            Number = "4499";
        }else if(carrierName.toUpperCase().equals("Robi".toUpperCase())){
            //Toast.makeText(getApplicationContext(),"Robi",Toast.LENGTH_LONG).show();
            Number = "21213";
        }else{
            Toast.makeText(getApplicationContext(),"Current Carrier is: "+carrierName+" "+Number+" ",Toast.LENGTH_SHORT).show();
            Number = "77200";
        }
    }
    public void Message(String Title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Title);
        builder.setMessage(Message);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                SetCarrierName();
                formatesSMSSent(SMSContent);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
    //sending formated sms
    private void formatesSMSSent(String smsString) {
        try {
            sendSMS(Number, smsString);
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "Faild, please try again.--------------",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //sent sms
    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        // ---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {

                switch (getResultCode()) {

                    case Activity.RESULT_OK:

                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:

                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:

                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:

                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:

                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        // ---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {

                switch (getResultCode()) {

                    case Activity.RESULT_OK:

                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case Activity.RESULT_CANCELED:

                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }
}
