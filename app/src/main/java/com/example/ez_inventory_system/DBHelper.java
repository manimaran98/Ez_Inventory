package com.example.ez_inventory_system;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String IMAGE = "item_image";
    public static final String TABLE_ITEM = "item_table";
    public static final String COLUMN_ITEM_ID = "item_id";
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_ITEM_QUANTITY = "item_quantity";
    public static final String COLUMN_ITEM_CATEGORY = "item_category";
    public static final String COLUMN_ITEM_PRICE = "item_price";
    public static final String COLUMN_ITEM_IMAGE = "item_image";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //First time database is access
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table item_table " +
                        "(item_id integer primary key autoincrement, item_name text,item_quantity integer, item_category text,item_price double,item_image blob)"
        );

        db.execSQL("create Table users(name Text, phnumber Text, username Text Primary Key, password Text)");

        db.execSQL(
                "create table transactions_table " +
                        "(transactions_id integer primary key autoincrement, item_id integer,item_name text,item_quantity integer,item_category,transactions integer,item_image blob)"
        );


    }

    //prevent previous users from breaking when you change the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS item_table");
        db.execSQL("drop Table if exists users");
        onCreate(db);
    }

    public Boolean insertUserData(String name , String phnumber, String username, String password){


        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentvalues  = new ContentValues();
        contentvalues.put("Name", name);
        contentvalues.put("PhNumber", phnumber);
        contentvalues.put("Username", username);
        contentvalues.put("Password", password);
        long result= myDB.insert("users", null, contentvalues);

        if(result == -1){
            return false;
        }
        else{
            return true;
        }

    }

    public Boolean insertItem(String item_name, int item_quantity , String item_category,double item_price, byte[] item_image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("item_name", item_name);
        cv.put("item_quantity", item_quantity);
        cv.put("item_category", item_category);
        cv.put("item_price", item_price);
        cv.put(IMAGE, item_image);
        db.insert(TABLE_ITEM, null,cv);
        return true;
    }

    public boolean checkusername (String username)
    {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("select * from users where username= ?", new String[]{username});

        if(cursor.getCount()>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Boolean checkusernamePassword(String username, String password)
    {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("select * from users where username= ? and password= ?", new String[]{username,password});
        if(cursor.getCount()>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public ArrayList<String> getItem() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_ITEM, null );
        res.moveToFirst();

        while(!res.isAfterLast()){

            array_list.add(res.getString(res.getColumnIndex(COLUMN_ITEM_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public void deleteData(int COLUMN_ITEM_ID) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM item_table WHERE item_id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, String.valueOf(COLUMN_ITEM_ID));

        statement.execute();
        database.close();
    }
    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public Cursor getData1(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from item_table where item_id="+id+"", null );
        return res;
    }



    public void updateData(String COLUMN_ITEM_NAME, String COLUMN_ITEM_PRICE, byte[] COLUMN_ITEM_IMAGE,String COLUMN_ITEM_QUANTITY ,int COLUMN_ITEM_ID) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE item_table SET item_name = ?, item_price = ?, item_image = ?,item_quantity = ?  WHERE item_id = ?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindString(1, COLUMN_ITEM_NAME);
        statement.bindString(2, COLUMN_ITEM_PRICE);
        statement.bindBlob(3, COLUMN_ITEM_IMAGE);
        statement.bindString(4, String.valueOf(COLUMN_ITEM_QUANTITY));
        statement.bindString(5, String.valueOf(COLUMN_ITEM_ID));
        statement.execute();
        database.close();
    }

    public Boolean insertTransaction(int item_id , String item_name, String item_quantity,String item_category, String transactions , byte[] item_image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("item_id", item_id);
        cv.put("item_name", item_name);
        cv.put("item_quantity", item_quantity);
        cv.put("item_category", item_category);
        cv.put("transactions", transactions);
        cv.put(IMAGE, item_image);
        db.insert("transactions_table", null,cv);
        return true;
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

}
