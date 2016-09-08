package ru.hh.headhuntersearch.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.List;

import ru.hh.headhuntersearch.data.db.table.VacancyCacheTable;
import ru.hh.headhuntersearch.data.db.table.VacancyRequestTable;
import ru.hh.headhuntersearch.entity.vo.VacancyVO;

public final class VacancyCacheHelper {
    private VacancyCacheHelper() {
        // not intended to instantiate
    }

    public static void cacheInDatabase(List<VacancyVO> items, String searchText, SQLiteOpenHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Long requestId = getExistingRequestId(searchText, db);
        db.beginTransaction();
        try {

            if (requestId != null) {
                deleteOldCache(requestId, db);
            } else {
                requestId = createRequestRecord(db, searchText);
            }
            for (VacancyVO vo : items) {
                saveVacancy(vo, requestId, db);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private static void saveVacancy(VacancyVO vacancy, long requestId, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(VacancyCacheTable.COLUMN_COMPANY_NAME, vacancy.getEmployerName());
        cv.put(VacancyCacheTable.COLUMN_REQUEST_ID, requestId);
        cv.put(VacancyCacheTable.COLUMN_VACANCY_NAME, vacancy.getVacancyName());

        db.insert(VacancyCacheTable.TABLE, null, cv);
    }


    private static long createRequestRecord(SQLiteDatabase db, String searchText) {
        ContentValues cv = new ContentValues(1);
        cv.put(VacancyRequestTable.COLUMN_REQUEST, searchText);
        // TODO add error handling
        return db.insert(VacancyRequestTable.TABLE, null, cv);
    }

    private static void deleteOldCache(long requestId, SQLiteDatabase db) {
        db.delete(VacancyCacheTable.TABLE,
                VacancyCacheTable.COLUMN_REQUEST_ID + "=?",
                new String[]{String.valueOf(requestId)});
    }

    @Nullable
    private static Long getExistingRequestId(String searchText, SQLiteDatabase db) {
        String[] projection = {VacancyRequestTable.COLUMN_ID};
        Cursor cursor = db.query(VacancyRequestTable.TABLE,
                projection,
                VacancyRequestTable.COLUMN_REQUEST + "=?",
                new String[]{searchText},
                null,
                null,
                null);
        if (cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToNext();
        long requestId = cursor.getLong(cursor.getColumnIndex(VacancyRequestTable.COLUMN_ID));
        cursor.close();
        return requestId;
    }
}
