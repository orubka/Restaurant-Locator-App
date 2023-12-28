package com.example.cw;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

        // database name and version
        private static final String DATABASE_NAME = "myapp.db";
        private static final int DATABASE_VERSION = 1;

        // table and columns for Users
        public static final String TABLE_USERS = "users";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";

        // create Users table query
        private static final String CREATE_TABLE_USERS =
                "CREATE TABLE " + TABLE_USERS + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, " +
                        COLUMN_PASSWORD + " TEXT NOT NULL);";

        //constructor of DB helper
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create the Users table
            db.execSQL(CREATE_TABLE_USERS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // drop existing tables if they exist
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

            // recreate the tables
            onCreate(db);
        }


        // insert a new user into the database
        public long insertUser(String username, String password) {
            SQLiteDatabase db = this.getWritableDatabase();    //calling method to open database connection
            long newRowId = -1;

            try {
                ContentValues values = new ContentValues();
                values.put(COLUMN_USERNAME, username);
                values.put(COLUMN_PASSWORD, password);

                // insert or ignore if a conflict occurs
                newRowId = db.insertWithOnConflict(TABLE_USERS, null, values, SQLiteDatabase.CONFLICT_IGNORE);

                if (newRowId == -1) {
                    // handle the case where a duplicate username is detected
                    Log.e("DatabaseHelper", "Duplicate username. Insertion failed.");
                } else {
                    // successful insertion
                    Log.d("DatabaseHelper", "User inserted successfully with ID: " + newRowId);
                }

            } catch (Exception e) {
                // other exceptions
                Log.e("DatabaseHelper", "Error inserting user: " + e.getMessage());
            } finally {
                db.close();   // closing connection to database
            }

            return newRowId;
        }

        // checking if a username already exists in the database
        public boolean isUsernameExists(String username) {
            SQLiteDatabase db = this.getReadableDatabase();

            String query = "SELECT * FROM " + TABLE_USERS +
                    " WHERE " + COLUMN_USERNAME + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{username});

            boolean exists = cursor.getCount() > 0;

            cursor.close();
            db.close();

            return exists;
        }

}
