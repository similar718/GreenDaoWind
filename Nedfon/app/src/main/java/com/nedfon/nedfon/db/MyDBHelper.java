package com.nedfon.nedfon.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nedfon.nedfon.bean.DbDean;

import java.util.ArrayList;
import java.util.List;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "nedfon"; // database name
    private static final int TABLE_VERSION = 1;// database version
    public static final String LOCAL_DBNAME = "nedfon.db";
    private SQLiteDatabase db = null;
    private Cursor cursor = null;

    public MyDBHelper(Context context) {
        super(context, LOCAL_DBNAME, null, TABLE_VERSION);
    }

    // create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL("create table "
                + TABLE_NAME
                + " (_id integer primary key autoincrement,"
                + "phone varchar(20),pwd vachar(20),"
                + "token varchar(20),time integer)");
    }

    // add   test
    public void insert(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateUser(String token,String phone){
        SQLiteDatabase db =  getWritableDatabase();
        db.execSQL(String.format("update %s set token=? where phone=?", TABLE_NAME), new Object[]{token,phone});
        db.close();
    }

    // add   test
    public void insert1(DbDean entity) {
        ContentValues values = new ContentValues();
        values.put("phone", entity.phone);
        values.put("pwd", entity.pwd);
        values.put("token", entity.token);
        values.put("time", entity.time);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // delete a line
    public void delete(String imageName) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, "phone=?", new String[] { String.valueOf(imageName) });
    }
    // according to imageName query
    public boolean query(String imageName) {
        boolean flag = false;
        SQLiteDatabase db = getWritableDatabase();
        cursor = db.query(TABLE_NAME, null, "phone=?",
                new String[] { String.valueOf(imageName) }, null, null, null);
        if (cursor.moveToFirst()) {
            flag = true;
        }
        return flag;
    }

    public List<DbDean> query() {
        List<DbDean> list = new ArrayList<DbDean>();
        SQLiteDatabase db = getWritableDatabase();
        cursor = db.query(TABLE_NAME, null, null, null, "_id", null, null);
        if (cursor.moveToFirst()) {
            do {
                DbDean info = new DbDean();
                info.phone = cursor.getString(cursor.getColumnIndex("phone"));
                info.pwd = cursor.getString(cursor.getColumnIndex("pwd"));
                info.token = cursor.getString(cursor.getColumnIndex("token"));
                info.time = cursor.getInt(cursor.getColumnIndex("time"));
                list.add(info);
            } while (cursor.moveToNext());
        }
        return list;
    }
    // operate the database by the SQL statement
    public void handleBySql(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    // close data base
    public void close() {
        if (db != null) {
            db.close();
            db = null;
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
