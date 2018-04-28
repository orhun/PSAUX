package com.tht.psaux;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PService extends Service {
    public PService() {}

    public int onStartCommand(Intent intent, int flags, int startId) {
        autoKill();
        return Service.START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        autoKill();
        return null;
    }
    private void autoKill(){
        try {
            String procNames = new MainActivity().getDBInfo(getApplicationContext());
            String cmdResult = new CmdExec().executeCommand(CmdExec.processListCommand, "");
            String[] executedList = cmdResult.split("\n");
            for (int i = 0; i < executedList.length; i++){
                try {
                    String line = executedList[i];
                    if(line.contains("USER") && line.contains("NAME")){
                        continue;
                    }
                    String pName = line.split("[ ]+")[8].trim();
                    int pid = Integer.parseInt(line.split("[ ]+")[1].trim());
                    if (procNames.contains(pName)) {
                        new CmdExec().killProc(getApplicationContext(), pid, pName);
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
