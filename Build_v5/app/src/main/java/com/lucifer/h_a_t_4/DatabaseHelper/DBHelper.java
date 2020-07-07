package com.lucifer.h_a_t_4.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper // Database for Address storing
{
    public static final String DATABASE_NAME="HomeAutomation.db";

    public static final String TABLE_NAME="Address";
    public static final String TABLE_NAME2 ="Alarm";


    public static final String Col4="Address";
    public static final String Col2="ID";

    public static final String Col3="Device";
    public static final String Col5="Hour";
    public static final String Col6="Minute";


    public boolean created , created2 , created3;
    private String TAG = DBHelper.class.getSimpleName();
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    DbResponse obj = new DbResponse();

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

        db.execSQL("create table " + TABLE_NAME + "( ID INTEGER PRIMARY KEY AUTOINCREMENT ,Address text  , Device text)");
        Log.d(TAG,"Table2 created");
        db.execSQL("create table " + TABLE_NAME2 + "( ID INTEGER PRIMARY KEY AUTOINCREMENT ,Hour Integer  , Minute text)");
        Log.d(TAG,"Table3 created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        Log.d(TAG,"Table address dropped");
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME2);
        Log.d(TAG,"Table Alarm dropped");
        onCreate(db);
        Log.d(TAG,"Database updated created");
    }

    public boolean check_db()
    {

        Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (mCursor.moveToFirst())
        {
            created = true;
            Log.d(TAG,"Table 1 Data true");
            mCursor.close();
            return  true;

        } else
        {
            created = false;
            Log.d(TAG,"Table 1 Data false");
            mCursor.close();
            return false;
        }

    }



    public boolean check_db3()
{

    Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME2, null);

    if (mCursor.moveToFirst())
    {
        created3 = true;
        Log.d(TAG,"Table 3 Data true");
        mCursor.close();
        return  true;

    } else
    {
        created3 = false;
        Log.d(TAG," Table 3 Data false");
        mCursor.close();
        return false;
    }

}

    public boolean insertAddressData(String name,String Device){
        db.delete(TABLE_NAME, null, null);
        Log.d(TAG,"Insert data called Table 2 (Address) ");
        //
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col4,name);
        contentValues.put(Col3,Device);
        long result = db.insert(TABLE_NAME,null,contentValues);


        Log.d(TAG,"Data Inserted Table 2 (Address)");

        Log.d(TAG,"Data deleted upon Insertion Table 2 (Address) ");
        if(result==-1)
            return false;
        else
            return true;

    }

    public boolean insertAlarm(int Hour,int Minute){
        db.delete(TABLE_NAME, null, null);
        Log.d(TAG,"Data deleted upon Insertion TAble 3(Alarm)");
        Log.d(TAG,"Insert data called Table 3(Alarm) ");

        ContentValues contentValues = new ContentValues();
        contentValues.put(Col5,Hour);
        contentValues.put(Col6,Minute);
        long result = db.insert(TABLE_NAME2,null,contentValues);

        Log.d(TAG,"Data Inserted in Table 3(Alarm)");


        if(result==-1)
            return false;
        else
            return true;

    }



    public DbResponse getLastAddressdata(){

        if(created) {
            String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + Col2 + " DESC LIMIT 1;";
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

    public boolean updatedata(String Id,String name){


        return true;
    }



    public Integer deletedata(String Id){

        return 1;

    }
    public Integer deleteAddressdata(String Address){

        //   SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG,"Deleted Data :"+db.delete(TABLE_NAME,"Address=?", new String[]{Address}));
        return db.delete(TABLE_NAME,"Address=?", new String[]{Address});

    }

}