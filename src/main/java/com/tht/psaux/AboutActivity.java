package com.tht.psaux;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    private Context context;
    private ViewGroup viewGroup;
    private static String version = "1.0",
            k3pwn = "http://www.k3pwn.me";
    // ** //
    private TextView txvK3pwn, txvProjectInfo;
    private void init(){
        txvK3pwn = viewGroup.findViewById(R.id.txvK3pwn);
        txvProjectInfo = viewGroup.findViewById(R.id.txvProjectInfo);
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        }catch (Exception e){
            e.printStackTrace();
        }
        txvProjectInfo.setText(context.getString(R.string.app_name) + " v" + version);
        txvK3pwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPage(k3pwn);
            }
        });
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
    private void openPage(String url){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
