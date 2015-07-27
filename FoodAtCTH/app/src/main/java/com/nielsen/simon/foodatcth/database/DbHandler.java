package com.nielsen.simon.foodatcth.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.nielsen.simon.foodatcth.Message;
import com.nielsen.simon.foodatcth.Pizza;
import com.nielsen.simon.foodatcth.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Simon on 2015-07-20.
 */
public class DbHandler extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com/nielsen/simon/foodatcth/databases/";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "menus.db";
    public static final String TABLE_SANNE_GIBRALTAR = "Sanne_Gibraltar_Menu_Complete";
    public static final String TABLE_FAIJTAS = "faijtas";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "productname";
    public static final String COLUMN_PRODUCTDESCRIPTION = "productdescription";
    public static final String COLUMN_PRICE = "price";

    public static final String SANNE_GIB_COLUMN_MENU_NR = "menu_nr";
    public static final String SANNE_GIB_COLUMN_NAME = "name";
    public static final String SANNE_GIB_COLUMN_INGREDIENTS = "ingredients";
    public static final String SANNE_GIB_COLUMN_PRICE = "price";
    public static final String SANNE_GIB_COLUMN_GROUP_ID = "group_id";

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
        pizzaTables = new HashMap<PizzaMenu, String>();
        menuTables = new HashMap<Menu, String>();
        pizzaTables.put(PizzaMenu.SANNE_GIBRALTAR, TABLE_SANNE_GIBRALTAR);
        menuTables.put(Menu.FAIJTAS, TABLE_FAIJTAS);
        try {
            createDataBase();
        } catch (IOException e) {
            Message.simpleMessage(context, context.getResources().getString(R.string.menu_error));
        }
    }

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        pizzaTables = new HashMap<>();
        menuTables = new HashMap<>();
        pizzaTables.put(PizzaMenu.SANNE_GIBRALTAR, TABLE_SANNE_GIBRALTAR);
        menuTables.put(Menu.FAIJTAS, TABLE_FAIJTAS);
        try {
            createDataBase();
        } catch (IOException e) {
            Message.simpleMessage(context, context.getResources().getString(R.string.menu_error));
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*try {
            context.deleteDatabase(DATABASE_NAME);
            onCreate(null);
        } catch (SQLException e) {
            Message.simpleMessage(context, context.getResources().getString(R.string.menu_error));
        }*/
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (!dbExist) {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {
                e.printStackTrace();
                Message.simpleMessage(context, context.getResources().getString(R.string.database_error));

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
            String myPath = context.getDatabasePath(DATABASE_NAME).getPath();
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            //database does't exist yet.
            e.printStackTrace();
            Message.simpleMessage(context, "Database doesn't exist yet");
        }

        if (checkDB != null) {
            checkDB.close();
        }
        return false;
        //return checkDB != null ? true : false;
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
        String outFileName = context.getDatabasePath(DATABASE_NAME).getPath();

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
        try {
            Cursor c = this.getReadableDatabase().query(menuTables.get(menu), projection, null, null, null, null, sortOrder);
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    products.add(new Product(c.getInt(c.getColumnIndex(COLUMN_ID)), c.getString(c.getColumnIndex(COLUMN_PRODUCTNAME)), c.getString(c.getColumnIndex(COLUMN_PRODUCTDESCRIPTION)), c.getInt(c.getColumnIndex(COLUMN_PRICE))));
                    c.moveToNext();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Message.simpleMessage(context, context.getResources().getString(R.string.menu_error));
        }
        return products;
    }

    public ArrayList<Pizza> getPizzaMenu(PizzaMenu menu) {

        String[] projection = {
                SANNE_GIB_COLUMN_MENU_NR,
                SANNE_GIB_COLUMN_NAME,
                SANNE_GIB_COLUMN_INGREDIENTS,
                SANNE_GIB_COLUMN_PRICE,
                SANNE_GIB_COLUMN_GROUP_ID
        };

        String sortOrder = SANNE_GIB_COLUMN_MENU_NR + " ASC";

        ArrayList<Pizza> pizzas = new ArrayList<Pizza>();
        try {
            Cursor c = this.getReadableDatabase().query(pizzaTables.get(menu), projection, null, null, null, null, sortOrder);
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    pizzas.add(new Pizza(c.getInt(c.getColumnIndex(SANNE_GIB_COLUMN_MENU_NR)), c.getString(c.getColumnIndex(SANNE_GIB_COLUMN_NAME)), c.getString(c.getColumnIndex(SANNE_GIB_COLUMN_INGREDIENTS)), c.getInt(c.getColumnIndex(SANNE_GIB_COLUMN_PRICE)), c.getInt(c.getColumnIndex(SANNE_GIB_COLUMN_GROUP_ID))));
                    c.moveToNext();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Message.simpleMessage(context, context.getResources().getString(R.string.menu_error));
        }
        return pizzas;
    }

    public ArrayList<Pizza> getPizzaMenuSearchResult(PizzaMenu menu, String searchQuery) {

        String[] projection = {
                SANNE_GIB_COLUMN_MENU_NR,
                SANNE_GIB_COLUMN_NAME,
                SANNE_GIB_COLUMN_INGREDIENTS,
                SANNE_GIB_COLUMN_PRICE,
                SANNE_GIB_COLUMN_GROUP_ID
        };

        String sortOrder = SANNE_GIB_COLUMN_MENU_NR + " ASC";

        ArrayList<Pizza> pizzas = new ArrayList<Pizza>();
        try {
            Cursor c = this.getReadableDatabase().query(pizzaTables.get(menu), projection, searchQuery, null, null, null, sortOrder);
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    pizzas.add(new Pizza(c.getInt(c.getColumnIndex(SANNE_GIB_COLUMN_MENU_NR)), c.getString(c.getColumnIndex(SANNE_GIB_COLUMN_NAME)), c.getString(c.getColumnIndex(SANNE_GIB_COLUMN_INGREDIENTS)), c.getInt(c.getColumnIndex(SANNE_GIB_COLUMN_PRICE)), c.getInt(c.getColumnIndex(SANNE_GIB_COLUMN_GROUP_ID))));
                    c.moveToNext();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Message.simpleMessage(context, context.getResources().getString(R.string.menu_error));
        }
        return pizzas;
    }

    public ArrayList<Pizza> getPizzas(){
        String[] projection = {
                "_name",
                "_ingredients",
                "_price"
        };

        String sortOrder = "_id DESC";

        ArrayList<Pizza> pizzas = new ArrayList<Pizza>();
        try {
            Cursor c = this.getReadableDatabase().query("Pizza", projection, null, null, null, null, sortOrder);
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    pizzas.add(new Pizza(c.getPosition(), c.getString(c.getColumnIndex("_name")), c.getString(c.getColumnIndex("_ingredients")), c.getInt(c.getColumnIndex("_price")), 0));
                    c.moveToNext();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Message.simpleMessage(context, context.getResources().getString(R.string.menu_error));
        }
        return pizzas;
    }

}
