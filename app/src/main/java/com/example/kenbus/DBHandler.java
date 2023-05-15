package com.example.kenbus;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Booking";
    private static final String TABLE_Users = "Booking_details";
    private static final String KEY_ID = "Id";
    private static final String KEY_PHONE = "Phone_Number";

    private static final String KEY_NAME= " Full_names ";
    private static final String KEY_DEPART = "Departure";
    private static final String KEY_DEST = "Destination";
    private static final String KEY_DEPART_TIME= "Departure_Time";
    private static final String KEY_DATE = "Date";


    public DBHandler(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_Users + "("
                + KEY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_PHONE + " INTEGER,"
                + KEY_DEPART + " TEXT,"
                + KEY_DEST + " TEXT,"
                + KEY_DEPART_TIME + " INTEGER,"
                + KEY_DATE + " INTEGER"+ ")";
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Users);
        // Create tables again
        onCreate(db);
    }
    // **** CRUD (Create, Read, Update, Delete) Operations ***** //

    // Adding new User Details
    void insertUserDetails(String Full_names, String Departure, String Destination, String Departure_Time, String Date){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_NAME, Full_names);
        cValues.put(KEY_DEPART, Departure);
        cValues.put(KEY_DEST, Destination);
        cValues.put(KEY_DEPART_TIME, Departure_Time);
        cValues.put(KEY_DATE, Date);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_Users,null, cValues);
        db.close();
    }
    // Get User Details
    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> GetUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT Full_names, Departure,Departure_Time,Date, Destination FROM "+ TABLE_Users;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("Full_names",cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            user.put("Destination",cursor.getString(cursor.getColumnIndex(KEY_DEST)));
            user.put("Departure",cursor.getString(cursor.getColumnIndex(KEY_DEPART)));
            user.put("Departure_Time",cursor.getString(cursor.getColumnIndex(KEY_DEPART_TIME)));
            user.put("Date",cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            userList.add(user);
        }
        return  userList;
    }
    // Get User Details based on userid
    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> GetUserByUserId(int userid){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT Full_names, Destination, Departure,Departure_Time,Date FROM "+ TABLE_Users;
        Cursor cursor = db.query(TABLE_Users, new String[]{KEY_NAME, KEY_DEPART, KEY_DEST, KEY_DEPART_TIME,KEY_DATE}, KEY_ID+ "=?",new String[]{String.valueOf(userid)},null, null, null, null);
        if (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("Full_names",cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            user.put("Destination",cursor.getString(cursor.getColumnIndex(KEY_DEST)));
            user.put("Departure",cursor.getString(cursor.getColumnIndex(KEY_DEPART)));
            user.put("Departure_Time",cursor.getString(cursor.getColumnIndex(KEY_DEPART_TIME)));
            user.put("Date",cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            userList.add(user);
        }
        return  userList;
    }
    // Delete User Details
    public void DeleteUser(int userid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Users, KEY_ID+" = ?",new String[]{String.valueOf(userid)});
        db.close();
    }
    // Update User Details
    public int UpdateUserDetails(String Destination, String Departure, int id , int Departure_Time, int Date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put(KEY_DEST, Destination);
        cVals.put(KEY_DEPART, Departure);
        cVals.put(KEY_ID, id);
        cVals.put(KEY_DEPART_TIME, Departure_Time);
        cVals.put(KEY_DATE, Date);
        int count = db.update(TABLE_Users, cVals, KEY_ID+" = ?",new String[]{String.valueOf(id)});
        return  count;

    }
}



