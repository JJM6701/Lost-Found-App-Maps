package com.example.lostfoundapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2; // Updated database version
    private static final String DATABASE_NAME = "LostFoundDB";
    private static final String TABLE_ITEMS = "items";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "date";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_STATUS = "status";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_STATUS + " INTEGER,"
                + KEY_LATITUDE + " REAL,"
                + KEY_LONGITUDE + " REAL" + ")";
        db.execSQL(CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_ITEMS + " ADD COLUMN " + KEY_LATITUDE + " REAL");
            db.execSQL("ALTER TABLE " + TABLE_ITEMS + " ADD COLUMN " + KEY_LONGITUDE + " REAL");
        }
    }

    long addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, item.getName());
        values.put(KEY_PHONE, item.getPhone());
        values.put(KEY_DESCRIPTION, item.getDescription());
        values.put(KEY_DATE, item.getDate());
        values.put(KEY_LOCATION, item.getLocation());
        values.put(KEY_STATUS, item.isLost() ? 0 : 1);
        values.put(KEY_LATITUDE, item.getLatitude());
        values.put(KEY_LONGITUDE, item.getLongitude());

        long id = db.insert(TABLE_ITEMS, null, values);
        db.close();
        return id;
    }

    Item getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ITEMS, new String[] { KEY_ID, KEY_NAME, KEY_PHONE, KEY_DESCRIPTION, KEY_DATE, KEY_LOCATION, KEY_STATUS, KEY_LATITUDE, KEY_LONGITUDE },
                KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Item item = new Item(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6) == 0, cursor.getDouble(7), cursor.getDouble(8));
        cursor.close();
        return item;
    }

    public List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ITEMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6) == 0, cursor.getDouble(7), cursor.getDouble(8));
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return itemList;
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, KEY_ID + " = ?", new String[] { String.valueOf(id) });
        db.close();
    }
}