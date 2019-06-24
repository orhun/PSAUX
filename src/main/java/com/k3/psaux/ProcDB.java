package com.k3.psaux;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProcDB extends SQLiteOpenHelper {
    private static final String dbName = "p_db";
    public String tableName = "proctable";
    public String dataName = "procnames";
    private static final int ver = 1;

    public ProcDB(Context c){
        super(c, dbName, null, ver);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+tableName+" ("+dataName+" TEXT);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        try {
            db.execSQL("DROP TABLE IF EXISTS " + tableName);
            onCreate(db);
        }catch(Exception e){e.printStackTrace();}
    }
}
