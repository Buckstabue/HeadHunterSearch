package ru.hh.headhuntersearch.data.db.table;

public final class VacancyCacheTable {
    public static final String TABLE = "vacancy_cache";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_REQUEST_ID = "request_id";
    public static final String COLUMN_COMPANY_NAME = "company_name";
    public static final String COLUMN_VACANCY_NAME = "vacancy_name";

    private VacancyCacheTable() {
        // not intended to instantiate
    }

    public static String createTable() {
        return "CREATE TABLE " + TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_REQUEST_ID + " INTEGER NOT NULL, " +
                COLUMN_COMPANY_NAME + " TEXT NOT NULL, " +
                COLUMN_VACANCY_NAME + " TEXT NOT NULL)";

    }

}
