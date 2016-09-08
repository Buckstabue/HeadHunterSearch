package ru.hh.headhuntersearch.data.db.table;

import ru.hh.headhuntersearch.provider.VacancyProviderContract;

public final class VacancyCacheTable {
    public static final String TABLE = "vacancy_cache";

    public static final String COLUMN_ID = VacancyProviderContract.COLUMN_ID;
    public static final String COLUMN_REQUEST = VacancyProviderContract.COLUMN_REQUEST;
    public static final String COLUMN_COMPANY_NAME = VacancyProviderContract.COLUMN_COMPANY_NAME;
    public static final String COLUMN_VACANCY_NAME = VacancyProviderContract.COLUMN_VACANCY_NAME;

    private VacancyCacheTable() {
        // not intended to instantiate
    }

    public static String createTable() {
        return "CREATE TABLE " + TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_REQUEST + " TEXT NOT NULL, " +
                COLUMN_COMPANY_NAME + " TEXT NOT NULL, " +
                COLUMN_VACANCY_NAME + " TEXT NOT NULL)";

    }

}
