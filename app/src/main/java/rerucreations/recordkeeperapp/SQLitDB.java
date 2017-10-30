package rerucreations.recordkeeperapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sanwal Singh on 7/9/17.
 * Skype - sanwal.singh
 * Contact No. +91-774-252-6633
 */

public class SQLitDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "eventDB";
    private static final String TABLE_NAME = "event_table";
    private static final String TABLE_PROFILE = "profile_table";

    private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "event_title";
    private static final String KEY_ACTIVITY_TYPE = "event_activity_type";
    private static final String KEY_PLACE = "event_place";
    private static final String KEY_DURATION = "event_duration";
    private static final String KEY_COMMENT = "event_comment";
    private static final String KEY_LAT = "event_lat";
    private static final String KEY_LANG = "event_lang";
    private static final String KEY_DATE = "event_date";
    private static final String KEY_PHOTO_PATH = "event_photo_path";


    private static final String KEY_PRO_ID = "pro_id";
    private static final String KEY_PRO_NAME = "pro_name";
    private static final String KEY_PRO_EMAIL_ID = "pro_email_id";
    private static final String KEY_PRO_GENDER = "pro_gender";
    private static final String KEY_PRO_COMMENT = "pro_comment";
    private static final String KEY_IS_EDITED = "is_edited";


    private static final String CREATE_TABLE_RECORDS = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TITLE + " TEXT,"
            + KEY_ACTIVITY_TYPE + " TEXT,"
            + KEY_PLACE + " TEXT,"
            + KEY_DURATION + " TEXT,"
            + KEY_COMMENT + " TEXT,"
            + KEY_LAT + " TEXT,"
            + KEY_LANG + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_PHOTO_PATH + " TEXT" + ")";

    private static final String CREATE_TABLE_PROFILE = "CREATE TABLE " + TABLE_PROFILE + "(" + KEY_PRO_ID + " INTEGER PRIMARY KEY,"
            + KEY_PRO_NAME + " TEXT,"
            + KEY_PRO_EMAIL_ID + " TEXT,"
            + KEY_PRO_GENDER + " TEXT,"
            + KEY_PRO_COMMENT + " TEXT,"
            + KEY_IS_EDITED + " TEXT" + ")";

    public SQLitDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_RECORDS);
        sqLiteDatabase.execSQL(CREATE_TABLE_PROFILE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_RECORDS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_PROFILE);
    }


    public long addRecord(Records record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, record.getTitle());
        values.put(KEY_ACTIVITY_TYPE, record.getActivity_type());
        values.put(KEY_PLACE, record.getPlace());
        values.put(KEY_DURATION, record.getDuration());
        values.put(KEY_COMMENT, record.getComment());
        values.put(KEY_LAT, record.getLat());
        values.put(KEY_LANG, record.getLng());
        values.put(KEY_DATE, record.getDate());
        values.put(KEY_PHOTO_PATH, record.getPhoto());

        long id = db.insert(TABLE_NAME, null, values);
        return id;
    }


    public Records getRecordByID(int event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Records record = null;
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE "
                + KEY_ID + " = " + event_id;

        Log.e("DB", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        try {
            if (c != null && c.getCount() > 0) {
                Log.e("DB", "record not null");
                c.moveToFirst();
                record = new Records();
                record.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
                record.setActivity_type(c.getString(c.getColumnIndex(KEY_ACTIVITY_TYPE)));
                record.setPlace(c.getString(c.getColumnIndex(KEY_PLACE)));
                record.setDuration(c.getString(c.getColumnIndex(KEY_DURATION)));
                record.setComment(c.getString(c.getColumnIndex(KEY_COMMENT)));
                record.setLat(c.getString(c.getColumnIndex(KEY_LAT)));
                record.setLng(c.getString(c.getColumnIndex(KEY_LANG)));
                record.setDate(c.getString(c.getColumnIndex(KEY_DATE)));
                record.setPhoto(c.getString(c.getColumnIndex(KEY_PHOTO_PATH)));
                record.setId(c.getString(c.getColumnIndex(KEY_ID)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return record;
    }


    public List<Records> getRecordsList() {
        List<Records> Records = new ArrayList<Records>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " + KEY_DATE + " ASC";

        Log.e("DB", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        Log.e("DB", "no of Records : " + c.getCount());

        if (c.moveToFirst()) {
            do {
                Records record = new Records();
                record.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
                record.setActivity_type(c.getString(c.getColumnIndex(KEY_ACTIVITY_TYPE)));
                record.setPlace(c.getString(c.getColumnIndex(KEY_PLACE)));
                record.setDuration(c.getString(c.getColumnIndex(KEY_DURATION)));
                record.setComment(c.getString(c.getColumnIndex(KEY_COMMENT)));
                record.setLat(c.getString(c.getColumnIndex(KEY_LAT)));
                record.setLng(c.getString(c.getColumnIndex(KEY_LANG)));
                record.setDate(c.getString(c.getColumnIndex(KEY_DATE)));
                record.setPhoto(c.getString(c.getColumnIndex(KEY_PHOTO_PATH)));
                record.setId(c.getString(c.getColumnIndex(KEY_ID)));
                Records.add(record);
            } while (c.moveToNext());
        }
        return Records;
    }

    public int getRecordCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int updateRecords(String row_id, Records todo) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, todo.getTitle());
        values.put(KEY_ACTIVITY_TYPE, todo.getActivity_type());
        values.put(KEY_PLACE, todo.getPlace());
        values.put(KEY_DURATION, todo.getDuration());
        values.put(KEY_COMMENT, todo.getComment());
        values.put(KEY_LAT, todo.getLat());
        values.put(KEY_LANG, todo.getLng());
        values.put(KEY_DATE, todo.getDate());
        values.put(KEY_PHOTO_PATH, todo.getPhoto());

        Log.e("DB", "get title : " + todo.getTitle());

        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[]{row_id});
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public void deleteRow(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + KEY_ID + "='" + id + "'");
    }


    public long addUser(UserDetails details) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_PRO_NAME, details.getName());
        values.put(KEY_PRO_EMAIL_ID, details.getEmail());
        values.put(KEY_PRO_GENDER, details.getGender());
        values.put(KEY_PRO_COMMENT, details.getComment());
        values.put(KEY_IS_EDITED, details.getIsEdit());

        long id = db.insert(TABLE_PROFILE, null, values);
        return id;
    }

    public UserDetails getUserByID(int user_id) {

        SQLiteDatabase db = this.getReadableDatabase();
        UserDetails details = null;

        String selectQuery = "SELECT  * FROM " + TABLE_PROFILE + " WHERE "
                + KEY_PRO_ID + " = " + user_id;
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c != null && c.getCount() > 0) {
                Log.e("DB", "record not null");
                c.moveToFirst();
                details = new UserDetails();

                details.setName(c.getString(c.getColumnIndex(KEY_PRO_NAME)));
                details.setEmail(c.getString(c.getColumnIndex(KEY_PRO_EMAIL_ID)));
                details.setGender(c.getString(c.getColumnIndex(KEY_PRO_GENDER)));
                details.setComment(c.getString(c.getColumnIndex(KEY_PRO_COMMENT)));
                details.setIsEdit(c.getString(c.getColumnIndex(KEY_IS_EDITED)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return details;
    }

    public int updateProfile(String id, UserDetails details) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_PRO_NAME, details.getName());
        values.put(KEY_PRO_EMAIL_ID, details.getEmail());
        values.put(KEY_PRO_GENDER, details.getGender());
        values.put(KEY_PRO_COMMENT, details.getComment());
        values.put(KEY_IS_EDITED, details.getIsEdit());

        return db.update(TABLE_PROFILE, values, KEY_PRO_ID + " = ?",
                new String[]{id});
    }


    public int getUserCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PROFILE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
