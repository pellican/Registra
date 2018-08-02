package com.progetto.registra.database;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DbAdapterSet {
		public static final int COL_ID = 0;
		public static final int COL_INIZIO1 = 1;
	    public static final int COL_INIZIO2 = 2;
	    public static final int COL_FINEMA1 = 3;
	    public static final int COL_FINEMA2 = 4;
	    public static final int COL_INIZIOP1 = 5;
	    public static final int COL_INIZIOP2 = 6;
	    public static final int COL_FINE1 = 7;
	    public static final int COL_FINE2 = 8;
	   
	    private static final String DATABASE_TABLE = "contact";
	    public static final String KEY_CONTACTID = "_id";
	    public static final String KEY_INIZIO1 = "inizio1";
	    public static final String KEY_INIZIO2 = "inizio2";
	    public static final String KEY_FINEMA1 = "fine_m1";
	    public static final String KEY_FINEMA2 = "fine_m2";
	    public static final String KEY_INIZIOP1 = "inizio_p1";
	    public static final String KEY_INIZIOP2 = "inizio_p2";
	    public static final String KEY_FINE1 = "fine1";
	    public static final String KEY_FINE2 = "fine2";
	    private Context context;
	    private SQLiteDatabase database;
	    private DatabaseHelperSet dbHelper;

	    static {
	        DbAdapterSet.class.getSimpleName();
	    }

	    public DbAdapterSet(Context context) {
	        this.context = context;
	    }

	    public DbAdapterSet open() throws SQLException {
	        this.dbHelper = new DatabaseHelperSet(this.context);
	        this.database = this.dbHelper.getWritableDatabase();
	        return this;
	    }

	    public void close() {
	        this.dbHelper.close();
	    }

	    private ContentValues createContentValues(String in1, String in2, String fim1, String fim2, String inp1,String inp2,String fine1,String fine2) {
	        ContentValues values = new ContentValues();
	        values.put(KEY_INIZIO1, in1);
	        values.put(KEY_INIZIO2, in2);
	        values.put(KEY_FINEMA1, fim1);
	        values.put(KEY_FINEMA2, fim2);
	        values.put(KEY_INIZIOP1, inp1);
	        values.put(KEY_INIZIOP2, inp2);
	        values.put(KEY_FINE1, fine1);
	        values.put(KEY_FINE2, fine2);
	        return values;
	    }
	    public long createContact(String in1, String in2, String fim1, String fim2, String inp1,String inp2,String fine1,String fine2) {
	        return this.database.insertOrThrow(DATABASE_TABLE, null, createContentValues(in1, in2, fim1, fim2, inp1,inp2,fine1,fine2));
	    }

	    public boolean updateContact(long contactID,String in1, String in2, String fim1, String fim2, String inp1,String inp2,String fine1,String fine2) {
	        return this.database.update(DATABASE_TABLE, createContentValues(in1, in2, fim1, fim2, inp1,inp2,fine1,fine2), new StringBuilder("_id=").append(contactID).toString(), null) > 0;
	    }

	    public boolean deleteContact(long contactID) {
	        return this.database.delete(DATABASE_TABLE, new StringBuilder("_id=").append(contactID).toString(), null) > 0;
	    }

	    public void delete_byID(long id) {
	        this.database.delete(DATABASE_TABLE, "_id=" + id, null);
	    }

	    public void removeAll() {
	        this.database.delete(DATABASE_TABLE, null, null);
	    }

	    public Cursor fetchAllContacts() {
	        return this.database.query(DATABASE_TABLE, new String[]{KEY_CONTACTID, KEY_INIZIO1, KEY_INIZIO2, KEY_FINEMA1, KEY_FINEMA2, KEY_INIZIOP1,KEY_INIZIOP2,KEY_FINE1,KEY_FINE2}, null, null, null, null, null);
	    }

	    public Cursor fetchContactsByMese(String filter) {
	        String where = "mese=" + filter;
	        return this.database.query(true, DATABASE_TABLE, new String[]{KEY_CONTACTID, KEY_INIZIO1, KEY_INIZIO2, KEY_FINEMA1, KEY_FINEMA2, KEY_INIZIOP1,KEY_INIZIOP2,KEY_FINE1,KEY_FINE2}, where, null, null, null, null, null);
	    }

	    public Cursor getRow(long rowId) {
	        String where = "_id=" + rowId;
	        Cursor c = this.database.query(true, DATABASE_TABLE, new String[]{KEY_CONTACTID,KEY_INIZIO1, KEY_INIZIO2, KEY_FINEMA1, KEY_FINEMA2, KEY_INIZIOP1,KEY_INIZIOP2,KEY_FINE1,KEY_FINE2}, where, null, null, null, null, null);
	        if (c != null) {
	            c.moveToFirst();
	        }
	        return c;
	    }
}
