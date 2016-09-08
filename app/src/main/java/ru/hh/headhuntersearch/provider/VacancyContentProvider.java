package ru.hh.headhuntersearch.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import ru.hh.headhuntersearch.data.db.HhDataBaseHelper;
import ru.hh.headhuntersearch.data.db.table.VacancyCacheTable;

public class VacancyContentProvider extends ContentProvider {

    private static final int VACANCIES_BY_REQUEST_CODE = 1;
    private static final UriMatcher uriMatcher;

    private SQLiteOpenHelper dbHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(VacancyProviderContract.AUTHORITY,
                VacancyProviderContract.VACANCY_PATH,
                VACANCIES_BY_REQUEST_CODE);
    }

    public VacancyContentProvider() {

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(VacancyCacheTable.TABLE, selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case VACANCIES_BY_REQUEST_CODE:
                return VacancyProviderContract.VACANCIES_CONTENT_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(VacancyCacheTable.TABLE, null, values);
        return uri.withAppendedPath(uri, String.valueOf(id));
    }

    @Override
    public boolean onCreate() {
        dbHelper = new HhDataBaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(VacancyCacheTable.TABLE, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues cv, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.update(VacancyCacheTable.TABLE, cv, selection, selectionArgs);
    }
}
