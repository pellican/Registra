package com.progetto.registra.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
public class DbAdapterMesi {

    public static final int COL_ANNO = 2;
    public static final int COL_ID = 0;
    public static final int COL_MESE = 1;
    public static final int COL_MINUTI = 4;
    public static final int COL_ORE = 3;
    public static final int COL_PAGATO = 5;
    public static final int COL_RESTO = 6;
    private static final String DATABASE_TABLE = "contact";
    public static final String KEY_ANNO = "anno";
    public static final String KEY_CONTACTID = "_id";
    public static final String KEY_MESE = "mese";
    public static final String KEY_MINUTI = "minuti";
    public static final String KEY_ORE = "ore";
    public static final String KEY_PAGATO = "pagato";
    public static final String KEY_RESTO = "resto";
    private static final String LOG_TAG;
    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelperMesi dbHelper;

    static {
        LOG_TAG = DbAdapterMesi.class.getSimpleName();
    }

    public DbAdapterMesi(Context context) {
        this.context = context;
    }

    public DbAdapterMesi open() throws SQLException {
        this.dbHelper = new DatabaseHelperMesi(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        this.dbHelper.close();
    }

    private ContentValues createContentValues(String mese, String anno, String ore, String minuti, String pagato, String resto) {
        ContentValues values = new ContentValues();
        values.put(KEY_MESE, mese);
        values.put(KEY_ANNO, anno);
        values.put(KEY_ORE, ore);
        values.put(KEY_MINUTI, minuti);
        values.put(KEY_PAGATO, pagato);
        values.put(KEY_RESTO, resto);
        return values;
    }

    public long createContact(String mese, String anno, String ore, String minuti, String pagato, String resto) {
        return this.database.insertOrThrow(DATABASE_TABLE, null, createContentValues(mese, anno, ore, minuti, pagato, resto));
    }

    public boolean updateContact(long contactID, String mese, String anno, String ore, String minuti, String pagato, String resto) {
        return this.database.update(DATABASE_TABLE, createContentValues(mese, anno, ore, minuti, pagato, resto), new StringBuilder("_id=").append(contactID).toString(), null) > 0;
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
        return this.database.query(DATABASE_TABLE, new String[]{KEY_CONTACTID, KEY_MESE, KEY_ANNO, KEY_ORE, KEY_MINUTI, KEY_PAGATO, KEY_RESTO}, null, null, null, null, null);
    }
    public Cursor fetchContactsByMese(String mese,String anno) {
        String where = "mese=? and anno=?";
        return this.database.query( DATABASE_TABLE, new String[]{KEY_CONTACTID,KEY_MESE, KEY_ANNO, KEY_ORE, KEY_MINUTI, KEY_PAGATO,KEY_RESTO}, where,new String[]{mese,anno}, null, null,null);
    }
    public Cursor fetchContactsByAnno(String anno) {
        String where = "anno=?";
        return this.database.query( DATABASE_TABLE, new String[]{KEY_CONTACTID,KEY_MESE, KEY_ANNO, KEY_ORE, KEY_MINUTI, KEY_PAGATO,KEY_RESTO}, where,new String[]{anno}, null, null,null);
    }
    public Cursor fetchFinoAlMeseId(String id ){
        String where = "_id BETWEEN ? AND ?";
        return  this.database.query(DATABASE_TABLE,new String[]{KEY_CONTACTID,KEY_MESE, KEY_ANNO, KEY_ORE, KEY_MINUTI, KEY_PAGATO,KEY_RESTO},where,new String[]{"0",id},null,null,null);
    }
    public Cursor CercaFinoAlMeseId(long id){
        return  this.database.rawQuery("SELECT * FROM "+DATABASE_TABLE+" where _id BETWEEN 0 AND "+id,null);
    }

}
