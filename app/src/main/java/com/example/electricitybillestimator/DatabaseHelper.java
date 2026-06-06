package com.example.electricitybillestimator;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bill.db";
    public static final String TABLE_NAME = "bill";

    public DatabaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(
                "CREATE TABLE bill(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "month TEXT," +
                        "unit INTEGER," +
                        "rebate INTEGER," +
                        "totalCharge REAL," +
                        "finalCost REAL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS bill");
        onCreate(db);
    }

    public boolean insertData(
            String month,
            int unit,
            int rebate,
            double totalCharge,
            double finalCost)
    {
        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues cv =
                new ContentValues();

        cv.put("month", month);
        cv.put("unit", unit);
        cv.put("rebate", rebate);
        cv.put("totalCharge", totalCharge);
        cv.put("finalCost", finalCost);

        long result =
                db.insert(TABLE_NAME,
                        null,
                        cv);

        return result != -1;
    }

    public Cursor getAllData()
    {
        SQLiteDatabase db =
                this.getWritableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM bill",
                        null);

        return cursor;
    }

    public Cursor getOneData(String id)
    {
        SQLiteDatabase db =
                this.getWritableDatabase();

        return db.rawQuery(
                "SELECT * FROM bill WHERE id=?",
                new String[]{id});
    }

    public Integer deleteData(String id)
    {
        SQLiteDatabase db =
                this.getWritableDatabase();

        return db.delete(
                TABLE_NAME,
                "id=?",
                new String[]{id});
    }

    public boolean updateData(
            String id,
            String month,
            int unit,
            int rebate,
            double totalCharge,
            double finalCost)
    {
        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues cv =
                new ContentValues();

        cv.put("month",month);
        cv.put("unit",unit);
        cv.put("rebate",rebate);
        cv.put("totalCharge",totalCharge);
        cv.put("finalCost",finalCost);

        db.update(
                TABLE_NAME,
                cv,
                "id=?",
                new String[]{id});

        return true;
    }

}