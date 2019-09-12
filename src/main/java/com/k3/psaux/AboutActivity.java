package com.k3.psaux;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    private static String version = "1.0";
    // ** //
    private TextView txvProjectInfo;
    private void init(){
        txvProjectInfo = (TextView)findViewById(R.id.txvProjectInfo);
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            version = info.versionName;
        }catch (Exception e){
            e.printStackTrace();
        }
        txvProjectInfo.setText(getString(R.string.app_name) + " v" + version);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        hideActionBar();
        init();
    }
    private void hideActionBar(){
        try {
            getSupportActionBar().hide();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            getActionBar().hide();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
