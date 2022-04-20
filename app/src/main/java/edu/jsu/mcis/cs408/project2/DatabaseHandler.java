package edu.jsu.mcis.cs408.project2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final String TABLE_GUESSES = "guesses";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_KEY = "boxKey";
    public static final String COLUMN_WORD = "word";

    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GUESSES_TABLE = "CREATE TABLE contacts (_id varchar primary key, boxKey text, word text)";
        db.execSQL(CREATE_GUESSES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GUESSES);
        onCreate(db);
    }

    public void addGuess(Word w){
        ContentValues values = new ContentValues();
        values.put(COLUMN_KEY, w.getBox()); /* needs to store key */
        values.put(COLUMN_WORD, w.getWord());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_GUESSES, null, values);
        db.close();
    }
}
