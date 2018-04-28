package com.tht.psaux;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {
    private TextView txvAbout, txvKey, txvKeyEmail, txvTHT;
    private ImageView imgKey, imgKeyTHT, imgKeyTw, imgKeyYt;
    private RelativeLayout rlMain;
    private Animation fadeIn;
    private static String profileLink = "https://www.turkhackteam.org/members/763537.html";
    private static String site = "https://www.turkhackteam.org";
    private void init(){
        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(500);
        txvAbout = (TextView) findViewById(R.id.txvAbout);
        txvAbout.startAnimation(fadeIn);
        txvKey = (TextView) findViewById(R.id.txvKey);
        txvKey.startAnimation(fadeIn);
        txvKeyEmail = (TextView) findViewById(R.id.txvKeyEmail);
        txvKeyEmail.startAnimation(fadeIn);
        txvTHT = (TextView) findViewById(R.id.txvTHT);
        txvTHT.startAnimation(fadeIn);
        imgKey = (ImageView) findViewById(R.id.imgKey);
        imgKey.startAnimation(fadeIn);
        imgKeyTHT = (ImageView) findViewById(R.id.imgKeyTHT);
        imgKeyTHT.startAnimation(fadeIn);
        imgKeyTw = (ImageView) findViewById(R.id.imgKeyTw);
        imgKeyTw.startAnimation(fadeIn);
        imgKeyYt = (ImageView) findViewById(R.id.imgKeyYt);
        imgKeyYt.startAnimation(fadeIn);
        rlMain = (RelativeLayout) findViewById(R.id.activity_about);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init();
        hideActionBar();
        setVersionText();
        txvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String version = "";
                try {
                    PackageManager manager = getPackageManager();
                    PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
                    version = info.versionName;
                }catch (Exception e){
                    e.printStackTrace();
                }
                Snackbar.make(rlMain, getResources().getString(R.string.app_name) + " v" + version, Snackbar.LENGTH_LONG).show();
            }
        });
        txvKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this, "Coded by KeyLo99", Toast.LENGTH_LONG).show();
            }
        });
        txvKeyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipBoard(txvKeyEmail.getText().toString());
            }
        });
        imgKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInBrowser(profileLink);
            }
        });
        imgKeyTHT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInBrowser(profileLink);
            }
        });
        imgKeyTw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTwitterIntent(getApplicationContext());
            }
        });
        imgKeyYt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openYoutubeIntent(getApplicationContext());
            }
        });
        txvTHT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInBrowser(site);
            }
        });
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
    private void setVersionText(){
        String version = "1.0";
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            version = info.versionName;
        }catch (Exception e){
            e.printStackTrace();
        }
        txvAbout.setText(getResources().getString(R.string.app_name) + " v" + version);
    }
    private void copyToClipBoard(String data){
        try{
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("PS-AUX", data);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), getString(R.string.copied), Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    private void openInBrowser(String uri) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void openTwitterIntent(Context context) {
        try {
            String id = "4745956696";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name="+id));
            startActivity(intent);
        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/KeyLo_99"));
            startActivity(intent);
        }
    }
    private void openYoutubeIntent(Context context) {
        try {
            String id = "/channel/UCBOaNBR9j5FtdXEuGItqYqw/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + id));
            startActivity(intent);
        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCBOaNBR9j5FtdXEuGItqYqw"));
            startActivity(intent);
        }
    }

}
