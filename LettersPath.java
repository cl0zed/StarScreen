package com.example.orientationsensor;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Path;

public class LettersPath {

    public float letterWidth, letterHeight;

    public LettersPath(float width, float height)
    {
        letterHeight = height;
        letterWidth = width - 20.0f;
    }

    public Path drawMyPath(SQLiteDatabase db, String letter, float x, float y)
    {
        String [] columns = {
                LetterDataBase.col_Name,
                LetterDataBase.col_type,
                LetterDataBase.col_Point_X,
                LetterDataBase.col_Point_Y,
                /*LetterDataBase.col_Quad_X,
                LetterDataBase.col_Quad_Y,
                LetterDataBase.col_Cube_X,
                LetterDataBase.col_Cube_Y*/
        };
        Cursor cursor = db.query(LetterDataBase.letters_table, columns,
                LetterDataBase.col_Name + "= ?", new String[] {letter}, null, null, null, null);

        Path path = new Path();

        if (cursor.moveToFirst()) {
            do{
                OperationType operationType = OperationType.values()[Integer.parseInt(cursor.getString(1)) - 1];
                switch (operationType)
                {
                    case cursorMoveOperation:
                        path.moveTo(Float.parseFloat(cursor.getString(2)) * letterWidth + x,
                                Float.parseFloat(cursor.getString(3)) * letterHeight + y);
                        break;
                    case cursorLineOperation:
                        path.lineTo(Float.parseFloat(cursor.getString(2)) * letterWidth + x,
                                Float.parseFloat(cursor.getString(3)) * letterHeight + y);
                        break;
                    case cursorCircleOperation:
                        path.addCircle(Float.parseFloat(cursor.getString(2)) * letterWidth + x,
                                Float.parseFloat(cursor.getString(3)) * letterHeight + y, 4, Path.Direction.CCW);
                }
            }while (cursor.moveToNext());
            cursor.close();
        }
        return path;
    }

    public float [][]  getPoints(SQLiteDatabase db, String name)
    {
        String [] columns = {
                LetterDataBase.col_Name,
                LetterDataBase.col_Point_X,
                LetterDataBase.col_Point_Y,
        };
        Cursor cursor = db.query(LetterDataBase.letters_table, columns,
                LetterDataBase.col_Name + "= ?", new String[] {name}, null, null, null, null);
        float [][] points = new float[cursor.getCount() + 1][2];
        points[0][0] = cursor.getCount();
        cursor.moveToFirst();
        for (int i = 1; i < cursor.getCount() + 1; ++i)
        {
            points[i][0] = Float.parseFloat(cursor.getString(1));
            points[i][1] = Float.parseFloat(cursor.getString(2));
            cursor.moveToNext();
        }
        return points;
    }
}

enum OperationType{
    cursorMoveOperation{
        @Override
        public String toString() {
            return "Move Operation";
        }
    },
    cursorLineOperation{
        @Override
        public String toString() {
            return "Line Operation";
        }
    },
    cursorCircleOperation

}