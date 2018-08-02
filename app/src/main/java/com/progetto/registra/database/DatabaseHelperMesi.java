package com.progetto.registra.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DatabaseHelperMesi extends SQLiteOpenHelper {
    private static final String DATABASE_CREATE = "create table contact (_id integer primary key autoincrement, mese text not null, anno text not null, ore text not null, minuti text not null, pagato text not null, resto text not null);";
    private static final String DATABASE_NAME = "mesidatabase.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelperMesi(Context context) {
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
