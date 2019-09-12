package com.k3.psaux;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ArrayList<CProcess> processes, processesTemp;
    private RecyclerView pRecyclerView;
    private SwipeRefreshLayout swiper;
    private ProcessAdapter processAdapterRec;
    private Button btnProc, btnSettings;
    private ImageButton btnSearch, btnCloseSearch;
    private EditText edtxSearch;
    private static String version = "1.0";
    private void init(){
        processes = new ArrayList<>();
        processesTemp = processes;
        pRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swiper = (SwipeRefreshLayout) findViewById(R.id.swiper);
        btnProc = (Button) findViewById(R.id.btnProc);
        btnSettings = (Button) findViewById(R.id.btnSettings);
        btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        edtxSearch = (EditText) findViewById(R.id.edtxSearch);
        btnCloseSearch = (ImageButton) findViewById(R.id.btnCloseSearch);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        hideActionBar();
        showSplash();
        loadPList(null);
        startPService();
        swiper.setOnRefreshListener(new swiper_onRefresh());
        btnProc.setOnClickListener(new btnProc_onClick());
        btnSettings.setOnClickListener(new btnSettings_onClick());
        btnSearch.setOnClickListener(new btnSearch_onClick());
        edtxSearch.addTextChangedListener(new edtxSearch_onTextChanged());
        btnCloseSearch.setOnClickListener(new btnCloseSearch_onClick());
        setBtnProcText();
        getProcSettings();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.mnSettings){
            showSettings();
        }
        return true;
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
    private void showSplash(){
        try {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final Dialog splashDialog = new Dialog(this,
                    android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            splashDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            splashDialog.setContentView(inflater.inflate(R.layout.splash_layout, null));
            splashDialog.setCancelable(false);
            final ImageView imgLogo = splashDialog.findViewById(R.id.imgLogo);
            final TextView txvLogo = splashDialog.findViewById(R.id.txvLogo);
            final Animation zoomout = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.zoomout);
            final Animation fade = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.fade);
            imgLogo.startAnimation(zoomout);
            final String cmds = "> PS AUX";
            for (int s = 0; s < cmds.toCharArray().length; s++) {
                final int i = s;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txvLogo.append(String.valueOf(cmds.toCharArray()[i]));
                        if (i == cmds.toCharArray().length - 1) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    imgLogo.startAnimation(fade);
                                    txvLogo.startAnimation(fade);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            splashDialog.cancel();
                                        }
                                    }, 800);
                                }
                            }, 800);
                        }
                    }
                }, s * 200);
            }
            splashDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setBtnProcText(){
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            version = info.versionName;
        }catch (Exception e){
            e.printStackTrace();
        }
        btnProc.setText(getResources().getString(R.string.app_name) + " v" + version);
    }
    private class swiper_onRefresh implements SwipeRefreshLayout.OnRefreshListener{
        @Override
        public void onRefresh() {
            edtxSearch.setVisibility(View.GONE);
            btnCloseSearch.setVisibility(View.GONE);
            loadPList(null);
        }
    }
    private class btnProc_onClick implements Button.OnClickListener{
        @Override
        public void onClick(View view) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        }
    }
    private class btnSettings_onClick implements Button.OnClickListener{
        @Override
        public void onClick(View view) {
            showSettings();
        }
    }
    private class btnSearch_onClick implements ImageButton.OnClickListener{
        @Override
        public void onClick(View view) {
            processesTemp = processes;
            edtxSearch.setVisibility(View.VISIBLE);
            btnCloseSearch.setVisibility(View.VISIBLE);
            edtxSearch.setText("");
        }
    }
    private class btnCloseSearch_onClick implements ImageButton.OnClickListener{
        @Override
        public void onClick(View view) {
            edtxSearch.setVisibility(View.GONE);
            btnCloseSearch.setVisibility(View.GONE);
            loadPList(null);
        }
    }
    private class edtxSearch_onTextChanged implements TextWatcher{
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            ArrayList<CProcess> processesFiltered;
            String charString = charSequence.toString();
            if (charString.isEmpty()) {
                processesFiltered = processesTemp;
            } else {
                ArrayList<CProcess> filteredList = new ArrayList<>();
                for (CProcess row : processesTemp) {
                    if (row.getPName().toLowerCase().contains(charString.toLowerCase()) ||
                            String.valueOf(row.getPid()).contains(charSequence)) {
                        filteredList.add(row);
                    }
                }
                processesFiltered = filteredList;
            }
            processes = processesFiltered;
            pRecyclerView.setAdapter(new ProcessAdapter(getApplicationContext(), processesFiltered,
                    new ProcessAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(CProcess item, final int i) {
                    pRecyclerView_onItemClick(getApplicationContext(), item, i);
                }
            }));
        }
        @Override
        public void afterTextChanged(Editable editable) {}
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    }
    private void loadPList(final String compareProcess) {
        processes.clear();
        processesTemp.clear();
        edtxSearch.setVisibility(View.GONE);
        btnCloseSearch.setVisibility(View.GONE);
        edtxSearch.setText("");
        new processLoaderThread(compareProcess).execute();
    }
    private class processLoaderThread extends AsyncTask<Void, Void, Void>{
        private String compareProcess;
        private Boolean isRunning = false;
        private ArrayList<Integer> userColors, ppidColors;
        private String users = "", ppids = "";
        private processLoaderThread(String params){
            this.compareProcess = params;
            this.userColors = new ArrayList<>();
            this.ppidColors = new ArrayList<>();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            String cmdResult = new CmdExec().executeCommand(CmdExec.processListCommand, " "
                    + getProcSettings());
            Log.d(CmdExec.processListCommand + " " + getProcSettings() + ">", cmdResult);
            String[] executedList = cmdResult.split("\n");
            for (String line : executedList) {
                if (String.valueOf(line.charAt(0)).equals(" ")) {
                    line = line.substring(1);
                }
                String user = line.split("[ ]+")[0].trim();
                String ppid = line.split("[ ]+")[2].trim();
                if (user.equals("USER")) {
                    continue;
                }
                if (!users.contains(user)) {
                    users += user + "|";
                    int color = Color.argb(255, new Random().nextInt(256 - 56)
                            + 56, new Random().nextInt(256 - 56) + 56,
                            new Random().nextInt(256 - 56) + 56);
                    user = "<font color=\"" + color + "\">" + user + "</font>";
                    userColors.add(color);
                } else {
                    for (int a = 0; a < users.split("[|]").length; a++) {
                        if (users.split("[|]")[a].equals(user)) {
                            user = "<font color=\"" + userColors.get(a) + "\">" + user + "</font>";
                        }
                    }
                }
                if (ppid.equals("PPID")) {
                    continue;
                }
                if (!ppids.contains(ppid + "|")) {
                    ppids += ppid + "|";
                    int color = Color.argb(255, new Random().nextInt(256 - 56)
                            + 56, new Random().nextInt(256 - 56) + 56,
                            new Random().nextInt(256 - 56) + 56);
                    ppid = "<font color=\"" + color + "\">" + ppid + "</font>";
                    ppidColors.add(color);
                } else {
                    for (int a = 0; a < ppids.split("[|]").length; a++) {
                        if (ppids.split("[|]")[a].equals(ppid)) {
                            ppid = "<font color=\"" + ppidColors.get(a) + "\">" + ppid + "</font>";
                        }
                    }
                }
                try {
                    if (line.split("[ ]+").length == 10) {
                        processes.add(new CProcess(user,
                                Integer.parseInt(line.split("[ ]+")[1]),
                                ppid,
                                Integer.parseInt(line.split("[ ]+")[3]),
                                Integer.valueOf(line.split("[ ]+")[4]),
                                line.split("[ ]+")[5],
                                line.split("[ ]+")[6],
                                line.split("[ ]+")[7], //+ " " + line.split("[ ]+")[7],
                                line.substring(line.indexOf(line.split("[ ]+")[9]),
                                        line.length()),
                                line.split("[ ]+")[0],
                                Integer.valueOf(line.split("[ ]+")[2])));
                    } else {
                        processes.add(new CProcess(user,
                                Integer.parseInt(line.split("[ ]+")[1]),
                                ppid,
                                Integer.parseInt(line.split("[ ]+")[3]),
                                Integer.valueOf(line.split("[ ]+")[4]),
                                "", line.split("[ ]+")[5],
                                line.split("[ ]+")[6], //+ " " + line.split("[ ]+")[7],
                                line.substring(line.indexOf(line.split("[ ]+")[8]),
                                        line.length()),
                                line.split("[ ]+")[0],
                                Integer.valueOf(line.split("[ ]+")[2])));
                    }
                    try {
                        if (line.split("[ ]+")[8].contains(compareProcess)) {
                            isRunning = true;
                        }
                    } catch (Exception e) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Collections.reverse(processes);
            if (compareProcess != null && !isRunning) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), getString(R.string.processKilled) +
                                compareProcess, Toast.LENGTH_LONG).show();
                    }
                });
            }
            processAdapterRec = new ProcessAdapter(getApplicationContext(), processes, new ProcessAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(CProcess item, final int i) {
                    pRecyclerView_onItemClick(getApplicationContext(), item, i);
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                RecyclerView.LayoutManager mLayoutManager =
                        new LinearLayoutManager(getApplicationContext());
                pRecyclerView.setLayoutManager(mLayoutManager);
                pRecyclerView.setItemAnimator(new DefaultItemAnimator());
                pRecyclerView.setAdapter(processAdapterRec);
                swiper.setRefreshing(false);
            }catch (Exception e){e.printStackTrace();}
        }
    }
    private void pRecyclerView_onItemClick(Context context, CProcess item, final int i){
        final String[] options = new String[]{context.getString(R.string.killProcess), context.getString(R.string.processInfo)};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,
                android.R.style.Theme_DeviceDefault_Dialog);
        builder.setTitle(processes.get(i).getPName());
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(options[which].equals(options[0])){
                    new CmdExec().killProc(getApplicationContext(), processes.get(i).getPid(),
                            processes.get(i).getPName());
                    loadPList(processes.get(i).getPName());
                }else if(options[which].equals(options[1])){
                    showProcessInfo(i);
                }
            }
        });
        builder.show();
    }
    private void showProcessInfo(final int i){
        LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Dialog pDialog = new Dialog(this);
        pDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(inflater.inflate(R.layout.process_info_layout, null));
        ((TextView)(pDialog.findViewById(R.id.txvPUser))).setText(processes.get(i).getUSERSALT());
        ((TextView)(pDialog.findViewById(R.id.txvPPid))).setText(String.valueOf(processes.get(i)
                .getPid()));
        ((TextView)(pDialog.findViewById(R.id.txvPPpid))).setText(String.valueOf(processes.get(i)
                .getPPIDSALT()));
        ((TextView)(pDialog.findViewById(R.id.txvPVsize))).setText(String.valueOf(processes.get(i)
                .getVSIZE()));
        ((TextView)(pDialog.findViewById(R.id.txvPRss))).setText(String.valueOf(processes.get(i)
                .getRSS()));
        ((TextView)(pDialog.findViewById(R.id.txvPWchan))).setText(processes.get(i).getWCHAN());
        ((TextView)(pDialog.findViewById(R.id.txvPPc))).setText(processes.get(i).getPC());
        ((TextView)(pDialog.findViewById(R.id.txvPName))).setText(processes.get(i).getPName());
        if(!processes.get(i).getCPU().equals("")){
            ((TextView)(pDialog.findViewById(R.id.txvPCpu))).setVisibility(View.VISIBLE);
            ((TextView)(pDialog.findViewById(R.id.txvPCpuLabel))).setVisibility(View.VISIBLE);
            ((TextView)(pDialog.findViewById(R.id.txvPCpu))).setText(processes.get(i).getCPU());
        }
        try {
            Drawable icon = getPackageManager().getApplicationIcon(processes.get(i).getPName());
            ((ImageView)(pDialog.findViewById(R.id.imgPPic))).setImageDrawable(icon);
        }catch (PackageManager.NameNotFoundException e){}
        ((Button)(pDialog.findViewById(R.id.btnKillProcess))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CmdExec().killProc(getApplicationContext(), processes.get(i).getPid(),
                        processes.get(i).getPName());
                pDialog.cancel();
                loadPList(processes.get(i).getPName());
            }
        });
        ((Button)(pDialog.findViewById(R.id.btnSetService))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDBInfo(getApplicationContext(), getDBInfo(getApplicationContext())+
                        " | "+processes.get(i).getPName());
                pDialog.cancel();
                Toast.makeText(MainActivity.this, getString(R.string.autoKillSet) +
                        processes.get(i).getPName(), Toast.LENGTH_SHORT).show();
            }
        });
        pDialog.show();
    }
    public String getDBInfo(Context context){
        String info = " | ";
        try {
            ProcDB procDB = new ProcDB(context);
            SQLiteDatabase db = procDB.getWritableDatabase();
            String[] column = {procDB.dataName};
            Cursor read = db.query(procDB.tableName, column, null, null,
                    null, null, null);
            while (read.moveToNext()) {
                info = read.getString(read.getColumnIndex(procDB.dataName));
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return info;
    }
    public void updateDBInfo (Context context, String newInfo){
        try {
            ProcDB procDB = new ProcDB(context);
            SQLiteDatabase db = procDB.getWritableDatabase();
            db.execSQL("DELETE FROM "+procDB.tableName);
            ContentValues cv = new ContentValues();
            cv.put(procDB.dataName, newInfo);
            db.insertOrThrow(procDB.tableName, null, cv);
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void startPService(){
        Intent i = new Intent(this, PService.class);
        stopService(i);
        startService(i);
    }
    private void showSettings(){
        final String[] options = new String[]{getString(R.string.psCommandArgs), getString(R.string.autoKillSettings)};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,
                android.R.style.Theme_DeviceDefault_Dialog);
        builder.setTitle(getString(R.string.settings));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(options[which].equals(options[0])){
                    showSettings_PSCommand();            
                }else if(options[which].equals(options[1])){
                    showSettings_autoKill();
                }
            }
        });
        builder.show();
    }
    private void showSettings_PSCommand(){
        LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Dialog pDialog = new Dialog(this);
        pDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(inflater.inflate(R.layout.procargs_settings_layout, null));
        CheckBox cbxArgsT = pDialog.findViewById(R.id.cbxArgT),
                cbxArgsX = pDialog.findViewById(R.id.cbxArgX),
                cbxArgsP = pDialog.findViewById(R.id.cbxArgP),
                cbxArgsC = pDialog.findViewById(R.id.cbxArgC);
        try{
            String[] settings = getProcSettings().split("[&]")[0].split("[ ]");
            for(int i = 0; i < settings.length; i++){
                if(settings[i].equals("-t")){
                    cbxArgsT.setChecked(true);
                }else if(settings[i].equals("-x")){
                    cbxArgsX.setChecked(true);
                }else if(settings[i].equals("-p")){
                    cbxArgsP.setChecked(true);
                }else if(settings[i].equals("-c")){
                    cbxArgsC.setChecked(true);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        cbxArgsT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeSettings("-t", b);
            }
        });
        cbxArgsX.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeSettings("-x", b);
            }
        });
        cbxArgsP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeSettings("-p", b);
            }
        });
        cbxArgsC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeSettings("-c", b);
            }
        });
        (pDialog.findViewById(R.id.btnSettingsInfo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Snackbar.make(pDialog.findViewById(R.id.llProcArgsSettings),
                       getString(R.string.unstableWarning), Snackbar.LENGTH_LONG).show();
            }
        });
        pDialog.show();
    }
    private void changeSettings(String args, Boolean b){
        if(b){
            if(!getDBInfo(getApplicationContext()).contains(args) &&
                    getDBInfo(getApplicationContext()).contains("&")){
                updateDBInfo(getApplicationContext(), args + " " +
                        getDBInfo(getApplicationContext()));
            }else{
                getProcSettings();
            }
        }else{
            if(getDBInfo(getApplicationContext()).contains(args) &&
                    getDBInfo(getApplicationContext()).contains("&")){
                updateDBInfo(getApplicationContext(), " " +
                        getDBInfo(getApplicationContext()).replace(args, ""));
            }
        }
    }
    private void showSettings_autoKill(){
        startPService();
        LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Dialog pDialog = new Dialog(this);
        pDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(inflater.inflate(R.layout.autokill_settings_layout, null));
        ListView lstSettings = (ListView) pDialog.findViewById(R.id.lstSettings);
        final ArrayList<String> procNames = new ArrayList<>();
        try{
            String[] autoKillLst = getDBInfo(getApplicationContext()).split("[&]")[1]
                    .split("[|]");
            for(int i = 0; i < autoKillLst.length; i++){
                String procName = autoKillLst[i].trim();
                if(!procName.equals("") && !procName.equals(" ") && procName.length() > 2)
                    procNames.add(procName);
            }
            SettingsAdapter settingsAdapter = new SettingsAdapter(getApplicationContext(), procNames);
            lstSettings.setAdapter(settingsAdapter);
            lstSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    pDialog.cancel();
                    updateDBInfo(getApplicationContext(), getDBInfo(getApplicationContext())
                            .replaceAll("\\|[ ]*"+procNames.get(i)+"[ ]*", ""));
                    showSettings_autoKill();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        if(procNames.size()!=0){
            pDialog.show();
        }else{
            Toast.makeText(this, getString(R.string.noSettingsFound), Toast.LENGTH_LONG).show();
        }
    }
    public String getProcSettings(){
        String settings = "";
        if(!getDBInfo(getApplicationContext()).contains("&")){
            updateDBInfo(getApplicationContext(), " &"+getDBInfo(getApplicationContext()));
            settings = "";
        }else{
            settings = getDBInfo(getApplicationContext()).split("[&]")[0];
        }
        return settings.trim();
    }
}
