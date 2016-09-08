package ru.hh.headhuntersearch.async.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import ru.hh.headhuntersearch.entity.vo.VacancyPageVO;
import ru.hh.headhuntersearch.entity.vo.VacancyVO;
import ru.hh.headhuntersearch.exception.EmptyCacheException;
import ru.hh.headhuntersearch.provider.VacancyProviderContract;

public class CachedVacanciesAsyncLoader extends BaseAsyncLoader<VacancyPageVO> {
    public static final String ARG_SEARCH_TEXT = "search_text";

    private final String searchText;

    public CachedVacanciesAsyncLoader(Context context, Bundle args) {
        super(context);
        this.searchText = args.getString(ARG_SEARCH_TEXT);
    }

    @Override
    protected VacancyPageVO loadData() throws Exception {
        Uri uri = Uri.parse(VacancyProviderContract.CONTENT_URI);
        String[] projection = {VacancyProviderContract.COLUMN_VACANCY_NAME, VacancyProviderContract.COLUMN_COMPANY_NAME};
        String where = VacancyProviderContract.COLUMN_REQUEST + "=?";
        String[] whereArgs = {searchText};
        Cursor cursor = getContext().getContentResolver().query(uri, projection, where, whereArgs, null);
        if (cursor.getCount() == 0) {
            throw new EmptyCacheException();
        }
        List<VacancyVO> vacancies;
        try {
            vacancies = extractVacancies(cursor);
        } finally {
            cursor.close();
        }
        return new VacancyPageVO(1, 1, vacancies, vacancies.size());
    }

    private List<VacancyVO> extractVacancies(Cursor cursor) {
        List<VacancyVO> result = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            String vacancyName = cursor.getString(cursor.getColumnIndex(VacancyProviderContract.COLUMN_VACANCY_NAME));
            String employer = cursor.getString(cursor.getColumnIndex(VacancyProviderContract.COLUMN_COMPANY_NAME));
            result.add(new VacancyVO(vacancyName, employer));
        }
        return result;
    }
}
