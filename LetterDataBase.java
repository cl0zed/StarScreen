package com.example.orientationsensor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.content.ContentValues;

public class LetterDataBase extends SQLiteOpenHelper {

    static final String dbName = "LetterDataBase.db";
    static final String letters_table = "LettersTable";
    static final String col_ID = "id";
    static final String col_Name = "Name";
    static final String col_Point_X = "X";
    static final String col_Point_Y = "Y";
    static final String col_type = "Type";
    static final int dataBaseVersion = 1;

    static final float size = 1.0f;

    LetterDataBase(Context context)
    {
        super(context, dbName, null, dataBaseVersion);
    }

    public void onCreate(SQLiteDatabase database)
    {
        /*String createTable = "CREATE TABLE IF NOT EXISTS " + letters_table + " (" + col_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
            + col_Name + " TEXT, " + col_type + " INT, " + col_Point_X + " FLOAT, " + col_Point_Y + " FLOAT, "
            + col_Quad_X + " FLOAT, " + col_Quad_Y + " FLOAT, " +  col_Cube_X + " FLOAT, " + col_Cube_Y
            + " FLOAT);";*/
        String createTable = "CREATE TABLE IF NOT EXISTS " + letters_table + " (" + col_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + col_Name + " TEXT, " + col_type + " INTEGER, " + col_Point_X + " FLOAT, " + col_Point_Y + " FLOAT);";
        Log.i("Database", createTable);
        database.execSQL(createTable);
        insertValues(database);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + letters_table);
        onCreate(db);
    }

    //type 1 - use moveTo for this point
    //type 2 - use lineTo for this point
    //type 3 - use quadTo for this point
    //type 4 - use cubeTo for this point


//    public void insertPoint(SQLiteDatabase db, String name, int type, float X, float Y, float quadX,
//                            float quadY, float cubeX, float cubeY)
    public void insertPoint(SQLiteDatabase db, String name, int type, float X, float Y)
    {
        ContentValues values = new ContentValues();

        values.put(col_Name, name);
        values.put(col_type, type);
        values.put(col_Point_X, X);
        values.put(col_Point_Y, Y);

        db.insert(letters_table, null, values);
    }


    public void insertValues(SQLiteDatabase database)
    {

        insertPoint(database, "A", 1, 0, size);
        insertPoint(database, "A", 2, size/2.0f, 0);
        insertPoint(database, "A", 2, size, size);
        insertPoint(database, "A", 1, size * 5.0f/ 6.0f, size * 2.0f/3.0f);
        insertPoint(database, "A", 2, size / 6.0f, size * 2.0f/3.0f);

        insertPoint(database, "B", 1, 0, 0);
        insertPoint(database, "B", 2, 0, size);
        insertPoint(database, "B", 2, size*3.0f/8.0f, size);
        insertPoint(database, "B", 2, size/2.0f, size * 7.0f/8.0f);
        insertPoint(database, "B", 2, size/2.0f, size * 5.0f/8.0f);
        insertPoint(database, "B", 2, size*3.0f/8.0f, size/2.0f);
        insertPoint(database, "B", 2, 0, size/2.0f);

        insertPoint(database, "C", 1, size * 3.0f/4.0f, size /4.0f);
        insertPoint(database, "C", 2, size / 2.0f, 0);
        insertPoint(database, "C", 2, size / 4.0f, 0);
        insertPoint(database, "C", 2, 0, size /4.0f);
        insertPoint(database, "C", 2, 0, size * 3.0f/4.0f);
        insertPoint(database, "C", 2, size / 4.0f, size);
        insertPoint(database, "C", 2, size / 2.0f, size);
        insertPoint(database, "C", 2, size * 3.0f/4.0f, size * 3.0f/4.0f);

        insertPoint(database, "D", 1, size, 0);
        insertPoint(database, "D", 2, size, size);
        insertPoint(database, "D", 2, size*5.0f/8.0f, size);
        insertPoint(database, "D", 2, size/2.0f, size * 7.0f/8.0f);
        insertPoint(database, "D", 2, size/2.0f, size * 5.0f/8.0f);
        insertPoint(database, "D", 2, size*5.0f/8.0f, size/2.0f);
        insertPoint(database, "D", 2, size, size/2.0f);

        insertPoint(database, "E", 1, size/2.0f, 0);
        insertPoint(database, "E", 2, 0, 0);
        insertPoint(database, "E", 2, 0, size);
        insertPoint(database, "E", 2, size/2.0f, size);
        insertPoint(database, "E", 1, size/2.0f, size/2.0f);
        insertPoint(database, "E", 2, 0, size/2.0f);

        insertPoint(database, "F", 1, size/2.0f, 0);
        insertPoint(database, "F", 2, 0, 0);
        insertPoint(database, "F", 2, 0, size);
        insertPoint(database, "F", 1, size/2.0f, size/2.0f);
        insertPoint(database, "F", 2, 0, size/2.0f);

        insertPoint(database, "G", 1, size * 3.0f/4.0f, size /4.0f);
        insertPoint(database, "G", 2, size / 2.0f, 0);
        insertPoint(database, "G", 2, size / 4.0f, 0);
        insertPoint(database, "G", 2, 0, size /4.0f);
        insertPoint(database, "G", 2, 0, size * 3.0f/4.0f);
        insertPoint(database, "G", 2, size / 4.0f, size);
        insertPoint(database, "G", 2, size / 2.0f, size);
        insertPoint(database, "G", 2, size * 3.0f/4.0f, size * 3.0f/4.0f);
        insertPoint(database, "G", 2, size * 3.0f/4.0f, size/2.0f);
        insertPoint(database, "G", 2, size/4.0f, size/2.0f);

        insertPoint(database, "H", 1, 0, 0);
        insertPoint(database, "H", 2, 0, size);
        insertPoint(database, "H", 1, size / 2.0f, 0);
        insertPoint(database, "H", 2, size / 2.0f, size);
        insertPoint(database, "H", 1, 0, size/2.0f);
        insertPoint(database, "H", 2, size / 2.0f, size/2.0f);

        insertPoint(database, "I", 1, size/2.0f, size/4.0f);
        insertPoint(database, "I", 2, size/2.0f, size);
        insertPoint(database, "I", 3, size/2.0f, 0);

        insertPoint(database, "J", 1, size*3.0f/4.0f, size / 4.0f);
        insertPoint(database, "J", 2, size*3.0f/4.0f, size*7.0f/8.0f);
        insertPoint(database, "J", 2, size*5.0f/8.0f, size);
        insertPoint(database, "J", 2, size/2.0f, size);
        insertPoint(database, "J", 2, size*3.0f/8.0f, size*7.0f/8.0f);
        insertPoint(database, "J", 3, size*3.0f/4.0f, 0);

        insertPoint(database, "K", 1, 0, 0);
        insertPoint(database, "K", 2, 0, size);
        insertPoint(database, "K", 1, 0, size/2.0f);
        insertPoint(database, "K", 2, size/2.0f, 0);
        insertPoint(database, "K", 1, 0, size/2.0f);
        insertPoint(database, "K", 2, size/2.0f, size);

        insertPoint(database, "L", 1, 0, 0);
        insertPoint(database, "L", 2, 0, size);
        insertPoint(database, "L", 2, size/2.0f, size);

        insertPoint(database, "M", 1, 0, size);
        insertPoint(database, "M", 2, 0, 0);
        insertPoint(database, "M", 2, size*3.0f/8.0f, size/2.0f);
        insertPoint(database, "M", 2, size*3.0f/4.0f, 0);
        insertPoint(database, "M", 2, size*3.0f/4.0f, size);

        insertPoint(database, "N", 1, size/4.0f, size);
        insertPoint(database, "N", 2, size/4.0f, 0);
        insertPoint(database, "N", 2, size*3.0f/4.0f, size);
        insertPoint(database, "N", 2, size*3.0f/4.0f, 0);

        insertPoint(database, "O", 1, size/4.0f, 0);
        insertPoint(database, "O", 2, size/2.0f, 0);
        insertPoint(database, "O", 2, size*3.0f/4.0f, size/4.0f);
        insertPoint(database, "O", 2, size*3.0f/4.0f, size*3.0f/4.0f);
        insertPoint(database, "O", 2, size/2.0f, size);
        insertPoint(database, "O", 2, size/4.0f, size);
        insertPoint(database, "O", 2, 0, size*3.0f/4.0f);
        insertPoint(database, "O", 2, 0, size/4.0f);
        insertPoint(database, "O", 2, size/4.0f, 0);
    }
}
