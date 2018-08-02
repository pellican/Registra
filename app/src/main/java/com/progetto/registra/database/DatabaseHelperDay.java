package com.progetto.registra.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperDay extends SQLiteOpenHelper{
	private static final String DATABASE_CREATE = "create table contact (_id integer primary key autoincrement, data text not null, inizio text not null, pausa_m text not null, fine_m text not null, inizio_p text not null, pausa_p text not null, fine text not null, totale text not null);";
    private static final String DATABASE_NAME = "daydatabase.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelperDay(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(database);
    }
}
