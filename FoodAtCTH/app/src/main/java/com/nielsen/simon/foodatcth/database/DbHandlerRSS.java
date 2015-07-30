package com.nielsen.simon.foodatcth.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nielsen.simon.foodatcth.Message;
import com.nielsen.simon.foodatcth.Pizza;
import com.nielsen.simon.foodatcth.R;
import com.nielsen.simon.foodatcth.RssItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Simon on 2015-07-29.
 */
public class DbHandlerRSS extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "rssmenus.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_UNION = "tableunion";
    private static final String TABLE_LINSEN = "tablelinsen";
    private static final String TABLE_LSKITCHEN = "tablelskitchen";
    private static final String TABLE_KOKBOKEN = "tablekokboken";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";

    private Map<Restaurant,String> restaurantStringMap;

    private static DbHandlerRSS dbHandlerRSS;

    public static enum Restaurant{
        STUDENT_UNION_RESTAURANT, LINSEN, LSKITCHEN, KOKBOKEN
    }

    // Database creation sql statement
    private static final String DATABASE_CREATE_UNION = "create table "
            + TABLE_UNION + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TITLE
            + " text not null, " + COLUMN_DESCRIPTION + " text);";
    private static final String DATABASE_CREATE_LINSEN = "create table "
            + TABLE_LINSEN + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TITLE
            + " text not null, " + COLUMN_DESCRIPTION + " text);";
    private static final String DATABASE_CREATE_LSKITCHEN = "create table "
            + TABLE_LSKITCHEN + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TITLE
            + " text not null, " + COLUMN_DESCRIPTION + " text);";
    private static final String DATABASE_CREATE_KOKBOKEN = "create table "
            + TABLE_KOKBOKEN + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TITLE
            + " text not null, " + COLUMN_DESCRIPTION + " text);";

    public DbHandlerRSS(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        restaurantStringMap = new HashMap<>();
        restaurantStringMap.put(Restaurant.STUDENT_UNION_RESTAURANT, TABLE_UNION);
        restaurantStringMap.put(Restaurant.LINSEN, TABLE_LINSEN);
        restaurantStringMap.put(Restaurant.LSKITCHEN, TABLE_LSKITCHEN);
        restaurantStringMap.put(Restaurant.KOKBOKEN, TABLE_KOKBOKEN);
    }

    public static DbHandlerRSS getInstance(Context context){
        if(dbHandlerRSS==null){
            dbHandlerRSS = new DbHandlerRSS(context);
        }
        return dbHandlerRSS;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_UNION);
        db.execSQL(DATABASE_CREATE_LINSEN);
        db.execSQL(DATABASE_CREATE_LSKITCHEN);
        db.execSQL(DATABASE_CREATE_KOKBOKEN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LINSEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LSKITCHEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KOKBOKEN);
        onCreate(db);
    }

    public boolean addMenuItem(Restaurant restaurant, RssItem rssItem) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, rssItem.getTitle());
        values.put(COLUMN_DESCRIPTION, rssItem.getDescription());

        SQLiteDatabase db = this.getWritableDatabase();

        long id = db.insert(restaurantStringMap.get(restaurant), null, values);
        if (id < 0) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public void addMenuItems(Restaurant restaurant, ArrayList<RssItem> rssItems){
        SQLiteDatabase db = this.getWritableDatabase();

        for(int i = 0; i<rssItems.size(); i++){
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, rssItems.get(i).getTitle());
            values.put(COLUMN_DESCRIPTION, rssItems.get(i).getDescription());
            db.insert(restaurantStringMap.get(restaurant), null, values);
        }
        db.close();
    }

    public ArrayList<RssItem> getMenuForRestaurant(Restaurant restaurant){

        String[] projection = {
                COLUMN_TITLE,
                COLUMN_DESCRIPTION
        };

        String sortOrder = COLUMN_ID + " ASC";

        ArrayList<RssItem> rssItems = new ArrayList<RssItem>();
        try {
            Cursor c = this.getReadableDatabase().query(restaurantStringMap.get(restaurant), projection, null, null, null, null, sortOrder);
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    rssItems.add(new RssItem(c.getString(c.getColumnIndex(COLUMN_TITLE)), c.getString(c.getColumnIndex(COLUMN_DESCRIPTION))));
                    c.moveToNext();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rssItems;

    }

    public void replaceRestaurantMenu(Restaurant restaurant, List<RssItem> rssItems){
        SQLiteDatabase db = this.getWritableDatabase();
        String table = restaurantStringMap.get(restaurant);
        db.execSQL("DROP TABLE IF EXISTS " + table);
        db.execSQL("create table "
                + table + "(" + COLUMN_ID
                + " integer primary key autoincrement, " + COLUMN_TITLE
                + " text not null, " + COLUMN_DESCRIPTION + " text);");

        for(int i = 0; i<rssItems.size(); i++){
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, rssItems.get(i).getTitle());
            values.put(COLUMN_DESCRIPTION, rssItems.get(i).getDescription());
            db.insert(table, null, values);
        }
        db.close();
    }

}
