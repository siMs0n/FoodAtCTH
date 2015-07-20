package com.nielsen.simon.foodatcth.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nielsen.simon.foodatcth.Message;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by Simon on 2015-07-20.
 */
public class DbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "menusDB.db";
    public static final String TABLE_SANNE = "sanne";
    public static final String TABLE_FAIJTAS = "faijtas";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "productname";
    public static final String COLUMN_PRODUCTDESCRIPTION = "productdescription";
    public static final String COLUMN_PRICE = "price";
    public static final String CREATE_SANNE_TABLE = "CREATE TABLE " +
            TABLE_SANNE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PRODUCTNAME
            + " TEXT," + COLUMN_PRODUCTDESCRIPTION
            + " TEXT," + COLUMN_PRICE + " INTEGER" + ")";

    private Context context;
    public Map<Menu, String> tables;

    public static enum Menu {
        SANNE, FAIJTAS
    }

    public DbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_SANNE_TABLE);
        } catch (SQLException e) {
            Message.simpleMessage(context, "" + e);
        }
        tables.put(Menu.SANNE, TABLE_SANNE);
        tables.put(Menu.FAIJTAS, TABLE_FAIJTAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SANNE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAIJTAS);
            onCreate(db);
        } catch (SQLException e) {
            Message.simpleMessage(context, "" + e);
        }
    }

    public boolean addMenuItem(Menu menu, Product product) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, product.get_id());
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_PRODUCTDESCRIPTION, product.getProductDescription());
        values.put(COLUMN_PRICE, product.getPrice());

        SQLiteDatabase db = this.getWritableDatabase();

        long id = db.insert(tables.get(menu), null, values);
        if (id < 0) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public ArrayList<Product> getMenu(Menu menu) {

        String[] projection = {
                COLUMN_ID,
                COLUMN_PRODUCTNAME,
                COLUMN_PRODUCTDESCRIPTION,
                COLUMN_PRICE
        };

        String sortOrder = COLUMN_ID + " DESC";

        ArrayList<Product> products = new ArrayList<Product>();
        Cursor c = this.getReadableDatabase().query(tables.get(menu), projection, null, null, null, null, sortOrder);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                products.add(new Product(c.getInt(c.getColumnIndex(COLUMN_ID)), c.getString(c.getColumnIndex(COLUMN_PRODUCTNAME)), c.getString(c.getColumnIndex(COLUMN_PRODUCTDESCRIPTION)), c.getInt(c.getColumnIndex(COLUMN_PRICE))));
                c.moveToNext();
            }
        }
        return products;
    }

}
