package com.progetto.registra.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperSet extends SQLiteOpenHelper{
	 private static final String DATABASE_CREATE = "create table contact (_id integer primary key autoincrement, inizio1 text not null, inizio2 text not null, fine_m1 text not null, fine_m2 text not null, inizio_p1 text not null, inizio_p2 text not null, fine1 text not null, fine2 text not null);";
	    private static final String DATABASE_NAME = "setdatabase.db";
	    private static final int DATABASE_VERSION = 1;

	    public DatabaseHelperSet(Context context) {
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
