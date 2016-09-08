package ru.hh.headhuntersearch.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.hh.headhuntersearch.data.db.table.VacancyCacheTable;
import ru.hh.headhuntersearch.data.db.table.VacancyRequestTable;
import ru.hh.headhuntersearch.util.Const;

public class HhDataBaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "hh";


    public HhDataBaseHelper(Context context) {
        super(context, DB_NAME, null, Const.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(VacancyRequestTable.createTable());
        db.execSQL(VacancyCacheTable.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTable(VacancyRequestTable.TABLE, db);
        deleteTable(VacancyCacheTable.TABLE, db);
        onCreate(db);
    }

    private void deleteTable(String tableName, SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }
}
