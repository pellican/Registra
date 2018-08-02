package com.progetto.registra.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DbAdapterDay {
	public static final int COL_ID = 0;
	public static final int COL_DATA=1;
	public static final int COL_INIZIO = 2;
    public static final int COL_PAUSAM = 3;
    public static final int COL_FINEMA = 4;
    public static final int COL_INIZIOP = 5;
    public static final int COL_PAUSAP = 6;
    public static final int COL_FINE = 7;
    public static final int COL_TOTALE = 8;
   
   
    private static final String DATABASE_TABLE = "contact";
    public static final String KEY_CONTACTID = "_id";
    public static final String KEY_DATA = "data";
    public static final String KEY_INIZIO = "inizio";
    public static final String KEY_PAUSAM = "pausa_m";
    public static final String KEY_FINEMA = "fine_m";
    public static final String KEY_INIZIOP = "inizio_p";
    public static final String KEY_PAUSAP = "pausa_p";
    public static final String KEY_FINE = "fine";
    public static final String KEY_TOTALE = "totale";
   
    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelperDay dbHelper;

    static {
        DbAdapterDay.class.getSimpleName();
    }

    public DbAdapterDay(Context context) {
        this.context = context;
    }

    public DbAdapterDay open() throws SQLException {
        this.dbHelper = new DatabaseHelperDay(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        this.dbHelper.close();
    }

    private ContentValues createContentValues(String data,String in, String pam, String fim, String inp,String pap,String fine,String totale) {
        ContentValues values = new ContentValues();
        values.put(KEY_DATA, data);
        values.put(KEY_INIZIO, in);
        values.put(KEY_PAUSAM, pam);
        values.put(KEY_FINEMA, fim);        
        values.put(KEY_INIZIOP, inp);
        values.put(KEY_PAUSAP, pap);
        values.put(KEY_FINE, fine);
        values.put(KEY_TOTALE, totale);
        return values;
    }
    public long createContact (String data,String in, String pam, String fim, String inp,String pap,String fine,String totale) {
        return this.database.insertOrThrow(DATABASE_TABLE, null, createContentValues(data,in, pam, fim,inp,pap,fine,totale));
    }

    public boolean updateContact(long contactID,String data,String in, String pam, String fim, String inp,String pap,String fine,String totale) {
        return this.database.update(DATABASE_TABLE, createContentValues(data,in, pam, fim,inp,pap,fine,totale), new StringBuilder("_id=").append(contactID).toString(), null) > 0;
    }

    public boolean deleteContact(long contactID) {
        return this.database.delete(DATABASE_TABLE, new StringBuilder("_id=").append(contactID).toString(), null) > 0;
    }

    public void delete_byID(long id) {
        this.database.delete(DATABASE_TABLE, "_id=" + id, null);
    }
    public void delete_byData(String data){
        this.database.delete(DATABASE_TABLE,"data = '"+data+"'",null);
    }

    public void removeAll() {
        this.database.delete(DATABASE_TABLE, null, null);
    }

    public Cursor fetchAllContacts() {
        return this.database.query(DATABASE_TABLE, new String[]{KEY_CONTACTID,KEY_DATA, KEY_INIZIO, KEY_PAUSAM, KEY_FINEMA, KEY_INIZIOP,KEY_PAUSAP,KEY_FINE,KEY_TOTALE}, null, null, null, null, null);
    }

    public Cursor fetchContactsByData(String filter) {
        String where = "data=?";
        return this.database.query( DATABASE_TABLE, new String[]{KEY_CONTACTID,KEY_DATA, KEY_INIZIO, KEY_PAUSAM, KEY_FINEMA, KEY_INIZIOP,KEY_PAUSAP,KEY_FINE,KEY_TOTALE}, where,new String[]{filter}, null, null,null);
    }

    public Cursor getRow(long rowId) {
        String where = "_id=" + rowId;
        Cursor c = this.database.query(true, DATABASE_TABLE, new String[]{KEY_CONTACTID,KEY_DATA, KEY_INIZIO, KEY_PAUSAM, KEY_FINEMA, KEY_INIZIOP,KEY_PAUSAP,KEY_FINE,KEY_TOTALE}, where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
}
