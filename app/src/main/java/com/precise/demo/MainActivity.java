package com.precise.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private String[] PERMISSIONS;
    private SubscriptionManager subsManager;
    private SubscriptionInfo subsInfo1, subsInfo2;
    private SmsManager smsManager;
    private int maxSimCount, simCount, activeSim;
    private TextView display1;
    private TextView display2;
    private TextView display3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display1 = findViewById(R.id.display);
        display2 = findViewById(R.id.display2);
        display3 = findViewById(R.id.display3);

        PERMISSIONS = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_PHONE_NUMBERS,
                Manifest.permission.READ_SMS
        };


        getPermissions();
        startUp();
    }


    private void startUp() {

        subsManager = this.getSystemService(SubscriptionManager.class);
        maxSimCount = subsManager.getActiveSubscriptionInfoCountMax();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            getPermissions();
            return;
        }
        simCount = subsManager.getActiveSubscriptionInfoCount();
        fill_Display1("Sim count is " + simCount);
        fill_Display2("Maximum sim count is " + maxSimCount);

    }

    // Requests for permissions
    private void getPermissions(){

        if (!hasPermissions(MainActivity.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, 1);
        }
    }


    // Checks whether permissions have been granted
    private boolean hasPermissions(Context context, String... PERMISSIONS) {

        if (context != null && PERMISSIONS != null) {
            for (String permission: PERMISSIONS){
                if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    // Returns the responses from permission requests
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            int x = 0;
            String tempStr, comment;
            for (String s: PERMISSIONS) {
                int m = s.length();
                tempStr = s.substring(19, m);
                if (grantResults[x] == PackageManager.PERMISSION_GRANTED) {
                    comment = tempStr + " permission is granted";
                } else {
                    comment = tempStr + " permission is denied";
                }
                Toast.makeText(this, comment, Toast.LENGTH_LONG).show();
                x++;
            }
        }

    }

    private void fill_Display1(String content) {
        display1.setText(content);
    }

    private void fill_Display1(int content){
        String comment = Integer.toString(content);
        display1.setText(comment);

    }

    private void fill_Display2(String content) {
        display2.setText(content);
    }

    private void fill_Display2(int content){
        String comment = Integer.toString(content);
        display2.setText(comment);

    }

    private void fill_Display3(String content) {
        display3.setText(content);
    }

    private void fill_Display3(int content){
        String comment = Integer.toString(content);
        display3.setText(comment);

    }


}