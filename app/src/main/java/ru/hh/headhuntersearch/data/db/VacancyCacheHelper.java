package ru.hh.headhuntersearch.data.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import ru.hh.headhuntersearch.data.db.table.VacancyCacheTable;
import ru.hh.headhuntersearch.entity.vo.VacancyVO;

public final class VacancyCacheHelper {
    private VacancyCacheHelper() {
        // not intended to instantiate
    }

    public static void cacheInDatabase(List<VacancyVO> items, String request, SQLiteOpenHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(VacancyCacheTable.TABLE,
                    VacancyCacheTable.COLUMN_REQUEST + "=?",
                    new String[]{request});
            if (!items.isEmpty()) {
                for (VacancyVO vo : items) {
                    saveVacancy(vo, request, db);
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private static void saveVacancy(VacancyVO vacancy, String request, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(VacancyCacheTable.COLUMN_COMPANY_NAME, vacancy.getEmployerName());
        cv.put(VacancyCacheTable.COLUMN_REQUEST, request);
        cv.put(VacancyCacheTable.COLUMN_VACANCY_NAME, vacancy.getVacancyName());

        db.insert(VacancyCacheTable.TABLE, null, cv);
    }
}
