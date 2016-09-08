package ru.hh.headhuntersearch.data.db.table;

public class VacancyRequestTable {
    public static final String TABLE = "vacancy_request";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_REQUEST = "request_id";

    private VacancyRequestTable() {
        // not intended to instantiate
    }

    public static String createTable() {
        return "CREATE TABLE " + TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_REQUEST + " TEXT NOT NULL UNIQUE)";
    }
}
