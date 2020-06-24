package com.lucifer.h_a_t_3.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper //Database Helper
{
    public static final String DATABASE_NAME="HomeAutomation.db";
    public static final String TABLE_NAME="Room";
    public static final String TABLE_NAME2="Address";
    public static final String Col1="Room_name";
    public static final String Col4="Address";
    public static final String Col2="ID";
    public static final String Col3="Device";
    public boolean created , created2;
    private String TAG = DBHelper.class.getSimpleName();
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    DbResponse obj = new DbResponse();
    DBHelper mydb;
    public class DbResponse{
        public String name;
        public String device;

    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

        Log.d(TAG,"Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME  + "(Room_name text )" );
        Log.d(TAG,"Table1 created");
        db.execSQL("create table " + TABLE_NAME2 + "( ID INTEGER PRIMARY KEY AUTOINCREMENT ,Address text  , Device text)");
        Log.d(TAG,"Table2 created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        Log.d(TAG,"Table1 dropped");
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME2);
        Log.d(TAG,"Table2 dropped");
        onCreate(db);
        Log.d(TAG,"Database updated created");
    }

    public boolean check_db()
    {

        Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME2, null);

        if (mCursor.moveToFirst())
        {
            created = true;
            Log.d(TAG,"Data true");
            mCursor.close();
            return  true;

        } else
        {
            created = false;
            Log.d(TAG,"Data false");
            mCursor.close();
            return false;
        }

    }
    public boolean check_db2()
    {

        Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (mCursor.moveToFirst())
        {
            created2 = true;
            Log.d(TAG,"Data true");
            mCursor.close();
            return  true;

        } else
        {
            created2 = false;
            Log.d(TAG,"Data false");
            mCursor.close();
            return false;
        }

    }
    public boolean insertRoomData(String name)
    {
    Log.d(TAG,"Insert data called ");
    contentValues.put(Col1,name);
    long result = db.insert(TABLE_NAME,null,contentValues);
    Log.d(TAG,"Data Inserted");
    if(result==-1)
        return false;
    else
        return true;
}
    public boolean insertAddressData(String name,String Device){
        db.delete(TABLE_NAME2, null, null);
        Log.d(TAG,"Insert data called ");
        //
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col4,name);
        contentValues.put(Col3,Device);
        long result = db.insert(TABLE_NAME2,null,contentValues);


        Log.d(TAG,"Data Inserted");

        Log.d(TAG,"Data deleted upon Insertion");
        if(result==-1)
            return false;
        else
            return true;

    }

    public Cursor getallRoomdata(){
        Cursor res =db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return res;

    }
    public DbResponse getLastAddressdata(){

        if(created) {
            String selectQuery = "SELECT * FROM " + TABLE_NAME2 + " ORDER BY " + Col2 + " DESC LIMIT 1;";
            //   SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                obj.name = cursor.getString(cursor.getColumnIndex(Col4));
                obj.device = cursor.getString(cursor.getColumnIndex(Col3));
            }
            cursor.close();
            Log.d(TAG, "LastData :" + obj.name);

        }
        return obj;
    }

    public Integer deletedata(String Id){

        return db.delete(TABLE_NAME,"Room_name=?", new String[]{Id});

    }
    public Integer deleteAddressdata(String Address){

        //   SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG,"Deleted Data :"+db.delete(TABLE_NAME2,"Address=?", new String[]{Address}));
        return db.delete(TABLE_NAME2,"Address=?", new String[]{Address});

    }

}