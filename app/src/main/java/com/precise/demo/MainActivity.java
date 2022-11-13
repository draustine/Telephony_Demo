package com.precise.demo;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String[] PERMISSIONS;
    private SubscriptionManager subsManager;
    private SubscriptionInfo subsInfo1, subsInfo2;
    private SmsManager smsManager;
    private int maxSimCount, simCount, activeSim, simSlot;
    private TextView display1, display2, display3;
    private RadioGroup simSelector;
    private RadioButton sim1, sim2, selectedSim;
    private String carrier, carrier1, carrier2, activeCarrier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display1 = findViewById(R.id.display);
        display2 = findViewById(R.id.display2);
        display3 = findViewById(R.id.display3);
        sim1 = findViewById(R.id.sim1);
        sim2 = findViewById(R.id.sim2);
        simSelector = findViewById(R.id.simSelector);

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

        simSelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                simChanged();
            }
        });
    }


    private void startUp() {
        simChanged();


    }

    private void simChanged() {

        initialiseSims();
        int simIndex = simSelector.getCheckedRadioButtonId();
        simSlot = 0;
        if (simCount > 1) {
            selectedSim = findViewById(simIndex);
            activeSim = parseInt((String) selectedSim.getTag());
            simSlot = activeSim + 1;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                getPermissions();;
            }
            switch (simSlot){
                case 1:
                    activeCarrier = carrier1;
                    break;
                case 2:
                    activeCarrier = carrier2;
                    break;
            }

            SubscriptionInfo localSubsInfo = subsManager.getActiveSubscriptionInfoForSimSlotIndex(activeSim);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                smsManager = getApplicationContext().getSystemService(SmsManager.class) .createForSubscriptionId(localSubsInfo.getSubscriptionId());
            } else {
                smsManager = SmsManager.getSmsManagerForSubscriptionId(activeSim);
            }
        } else {
            smsManager = SmsManager.getDefault();
            activeCarrier = carrier;

        }

        fill_Display1(activeCarrier);
    }

    private void initialiseSims() {

        subsManager = this.getSystemService(SubscriptionManager.class);
        maxSimCount = subsManager.getActiveSubscriptionInfoCountMax();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            getPermissions();
            return;
        }
        simCount = subsManager.getActiveSubscriptionInfoCount();
        //fill_Display1("Sim count is " + simCount);
        fill_Display2("Maximum sim count is " + maxSimCount);
        List list = subsManager.getActiveSubscriptionInfoList();
        if (simCount > 1){
            subsInfo1 = (SubscriptionInfo) list.get(0);
            subsInfo2 = (SubscriptionInfo) list.get(1);
            String phone1 = subsInfo1.getNumber();
            String phone2 = subsInfo2.getNumber();
            carrier1 = subsInfo1.getDisplayName().toString();
            carrier2 = subsInfo2.getDisplayName().toString();
            sim1.setText(carrier1);
            sim2.setText(carrier2);
            //fill_Display3("Sim 1 contains : " + phone1 + "\nSim 2 contains : " + phone2);

        } else {
            sim2.setVisibility(View.INVISIBLE);
            TelephonyManager tManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            carrier = tManager.getNetworkOperatorName();
            String phone = tManager.getLine1Number();
            sim1.setText(carrier);
            sim1.setChecked(true);
            activeCarrier = carrier;
            //fill_Display3("The default sim slot contains " + phone);
        }

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