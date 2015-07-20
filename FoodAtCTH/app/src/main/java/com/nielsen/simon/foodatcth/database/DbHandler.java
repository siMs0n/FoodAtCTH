package com.nielsen.simon.foodatcth.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.nielsen.simon.foodatcth.Message;
import com.nielsen.simon.foodatcth.Pizza;
import com.nielsen.simon.foodatcth.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Simon on 2015-07-20.
 */
public class DbHandler extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/YOUR_PACKAGE/databases/";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "menusDB.db";
    public static final String TABLE_SANNE_GIBRALTAR = "Sanne_Gibraltar";
    public static final String TABLE_FAIJTAS = "faijtas";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "productname";
    public static final String COLUMN_PRODUCTDESCRIPTION = "productdescription";
    public static final String COLUMN_PRICE = "price";

    public static final String SANNE_GIB_COLUMN_ID = "_id";
    public static final String SANNE_GIB_COLUMN_NAME = "productname";
    public static final String SANNE_GIB_COLUMN_INGREDIENTS = "productdescription";
    public static final String SANNE_GIB_COLUMN_PRICE = "price";

    public static final String CREATE_SANNE_TABLE = "CREATE TABLE " +
            TABLE_SANNE_GIBRALTAR + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PRODUCTNAME
            + " TEXT," + COLUMN_PRODUCTDESCRIPTION
            + " TEXT," + COLUMN_PRICE + " INTEGER" + ")";

    private Context context;
    public Map<Menu, String> menuTables;
    public Map<PizzaMenu, String> pizzaTables;

    public static enum Menu {
        FAIJTAS
    }

    public static enum PizzaMenu {
        SANNE_GIBRALTAR
    }

    public DbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
    }

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            createDataBase();
        } catch (IOException e) {
            Message.simpleMessage(context, context.getResources().getString(R.string.menu_error));
        }
        pizzaTables.put(PizzaMenu.SANNE_GIBRALTAR, TABLE_SANNE_GIBRALTAR);
        menuTables.put(Menu.FAIJTAS, TABLE_FAIJTAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            context.deleteDatabase(DATABASE_NAME);
            onCreate(null);
        } catch (SQLException e) {
            Message.simpleMessage(context, context.getResources().getString(R.string.menu_error));
        }
    }

    private void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (!dbExist) {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                Message.simpleMessage(context, context.getResources().getString(R.string.menu_error));

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            //database does't exist yet.
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies the database from the local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DATABASE_NAME);

        //Path to the just created empty db
        String outFileName = DB_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //Transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public boolean addMenuItem(Menu menu, Product product) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, product.get_id());
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_PRODUCTDESCRIPTION, product.getProductDescription());
        values.put(COLUMN_PRICE, product.getPrice());

        SQLiteDatabase db = this.getWritableDatabase();

        long id = db.insert(menuTables.get(menu), null, values);
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
        Cursor c = this.getReadableDatabase().query(menuTables.get(menu), projection, null, null, null, null, sortOrder);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                products.add(new Product(c.getInt(c.getColumnIndex(COLUMN_ID)), c.getString(c.getColumnIndex(COLUMN_PRODUCTNAME)), c.getString(c.getColumnIndex(COLUMN_PRODUCTDESCRIPTION)), c.getInt(c.getColumnIndex(COLUMN_PRICE))));
                c.moveToNext();
            }
        }
        return products;
    }

    public ArrayList<Pizza> getPizzaMenu(PizzaMenu menu) {

        String[] projection = {
                COLUMN_ID,
                COLUMN_PRODUCTNAME,
                COLUMN_PRODUCTDESCRIPTION,
                COLUMN_PRICE
        };

        String sortOrder = COLUMN_ID + " DESC";

        ArrayList<Pizza> pizzas = new ArrayList<Pizza>();
        Cursor c = this.getReadableDatabase().query(pizzaTables.get(menu), projection, null, null, null, null, sortOrder);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                pizzas.add(new Pizza()); //TODO: Get correct values from db
                c.moveToNext();
            }
        }
        return pizzas;
    }

}
