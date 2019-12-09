package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper{
public MyHelper(Context context){
    super(context,"itcast105.db",null,1);
}
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE information(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "project VARVHAR(20),teacher VARCHAR(20),week VARCHAR(20),start INTEGER,ends INTEGER,location VARCHAR(20))");
        db.execSQL("CREATE TABLE myhomework(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "project VARCHAR(20),jobname VARCHAR(20),deadlineyear INREGER,deadlinemonth INTEGER,deadlineday INTEGER" +
                ",content VARCHAR(2000))");
        db.execSQL("CREATE TABLE classclock(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "num INTEGER)");
        db.execSQL("CREATE TABLE homeworkclock(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "num INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}