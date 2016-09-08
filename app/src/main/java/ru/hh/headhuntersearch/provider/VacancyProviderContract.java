package ru.hh.headhuntersearch.provider;


public final class VacancyProviderContract {
    private VacancyProviderContract() {
        // not intended to instantiate
    }

    public static final String AUTHORITY = "ru.hh.headhuntersearch.provider";
    public static final String VACANCY_PATH = "vacancy";
    public static final String VACANCIES_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + VACANCY_PATH;
    public static final String CONTENT_URI = "content://" + AUTHORITY + "/" + VACANCY_PATH;

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_REQUEST = "request";
    public static final String COLUMN_COMPANY_NAME = "company_name";
    public static final String COLUMN_VACANCY_NAME = "vacancy_name";
}
