package com.tht.psaux;

import android.app.ActivityManager;
import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CmdExec {
    public static String processListCommand = "ps";
    private ActivityManager amg;
    public String executeCommand(String command, String args){
        try {
            Process process = Runtime.getRuntime().exec(command + args);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                strBuilder.append(line).append('\n');
            }
            reader.close();
            process.waitFor();
            return strBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public void killProc(Context context, int pid, String pname){
        try {
            amg = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            android.os.Process.killProcess(pid);
            android.os.Process.sendSignal(pid, android.os.Process.SIGNAL_KILL);
            amg.killBackgroundProcesses(pname);
            Toast.makeText(context, context.getString(R.string.signalSent) + pname, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
