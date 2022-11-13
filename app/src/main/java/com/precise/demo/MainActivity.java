package com.precise.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String[] PERMISSIONS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PERMISSIONS = new String[] {
                Manifest.permission.INTERNET,
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_PHONE_NUMBERS,
                Manifest.permission.READ_SMS
        };

        getPermissions();
    }


    private void getPermissions(){

        if (!hasPermissions(MainActivity.this, PERMISSIONS)) {

            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, 1);
        }

    }



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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            int x = 0;
            String[] line;
            String tempStr = "", comment = "";
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
}