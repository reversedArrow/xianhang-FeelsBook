package com.sample.xianhang_feelsbook;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * super class of all Activity
 */
public abstract class BaseActivity extends AppCompatActivity{

    private final String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // request permission for system version greater Android 6.0
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for(String p: permissions){
                if(ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{p}, 100);
                }
            }
        }
    }
}
