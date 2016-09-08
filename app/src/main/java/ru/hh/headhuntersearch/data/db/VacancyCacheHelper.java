package ru.hh.headhuntersearch.data.db;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.hh.headhuntersearch.data.db.table.VacancyCacheTable;
import ru.hh.headhuntersearch.entity.vo.VacancyVO;
import ru.hh.headhuntersearch.provider.VacancyProviderContract;

public final class VacancyCacheHelper {
    private static final String TAG = VacancyCacheHelper.class.getSimpleName();

    private VacancyCacheHelper() {
        // not intended to instantiate
    }

    public static void cacheInDatabase(Context context, List<VacancyVO> items, String request) {
        final Uri contentUri = Uri.parse(VacancyProviderContract.CONTENT_URI);
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.delete(contentUri,
                VacancyCacheTable.COLUMN_REQUEST + "=?",
                new String[]{request});
        if (items.isEmpty()) {
            return;
        }
        ArrayList<ContentProviderOperation> ops = new ArrayList<>(items.size());
        for (VacancyVO vo : items) {
            ops.add(saveVacancy(vo, request, contentUri));
        }
        try {
            contentResolver.applyBatch(VacancyProviderContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            Log.e(TAG, "cacheInDatabase", e);
        } catch (OperationApplicationException e) {
            Log.e(TAG, "cacheInDatabase", e);
        }
    }

    private static ContentProviderOperation saveVacancy(VacancyVO vacancy, String request, Uri uri) {
        ContentValues cv = new ContentValues(3);
        cv.put(VacancyCacheTable.COLUMN_COMPANY_NAME, vacancy.getEmployerName());
        cv.put(VacancyCacheTable.COLUMN_REQUEST, request);
        cv.put(VacancyCacheTable.COLUMN_VACANCY_NAME, vacancy.getVacancyName());

        return ContentProviderOperation.newInsert(uri).withValues(cv).build();
    }

    public static List<VacancyVO> getVacancies(Context context, String request) {
        Uri uri = Uri.parse(VacancyProviderContract.CONTENT_URI);
        String[] projection = {VacancyProviderContract.COLUMN_VACANCY_NAME, VacancyProviderContract.COLUMN_COMPANY_NAME};
        String where = VacancyProviderContract.COLUMN_REQUEST + "=?";
        String[] whereArgs = {request};
        Cursor cursor = context.getContentResolver().query(uri, projection, where, whereArgs, null);
        if (cursor.getCount() == 0) {
            return Collections.emptyList();
        }
        List<VacancyVO> vacancies;
        try {
            vacancies = extractVacancies(cursor);
        } finally {
            cursor.close();
        }
        return vacancies;
    }

    private static List<VacancyVO> extractVacancies(Cursor cursor) {
        List<VacancyVO> result = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            String vacancyName = cursor.getString(cursor.getColumnIndex(VacancyProviderContract.COLUMN_VACANCY_NAME));
            String employer = cursor.getString(cursor.getColumnIndex(VacancyProviderContract.COLUMN_COMPANY_NAME));
            result.add(new VacancyVO(vacancyName, employer));
        }
        return result;
    }

}
