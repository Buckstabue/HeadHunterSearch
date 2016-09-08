package ru.hh.headhuntersearch.async.loader;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import ru.hh.headhuntersearch.data.db.VacancyCacheHelper;
import ru.hh.headhuntersearch.data.network.ApiInterface;
import ru.hh.headhuntersearch.entity.converter.DtoToVoConverter;
import ru.hh.headhuntersearch.entity.dto.VacancyPaginationDto;
import ru.hh.headhuntersearch.entity.vo.VacancyPageVO;

public class VacanciesAsyncLoader extends BaseAsyncLoader<VacancyPageVO> {
    private static final String TAG = VacanciesAsyncLoader.class.getSimpleName();

    public static final String ARG_SEARCH_TEXT = "search_text";
    public static final String ARG_PAGE_NUMBER = "page_number";
    public static final String ARG_ITEMS_PER_PAGE = "items_per_page";

    private final ApiInterface apiInterface;
    private final DtoToVoConverter converter;
    private final SQLiteOpenHelper dbHelper;
    private final String searchText;
    private final int pageNumber;
    private final int itemsPerPage;

    public VacanciesAsyncLoader(Context context,
                                ApiInterface apiInterface,
                                DtoToVoConverter converter,
                                SQLiteOpenHelper dbHelper,
                                Bundle args) {
        super(context);
        this.apiInterface = apiInterface;
        this.converter = converter;
        this.dbHelper = dbHelper;
        this.searchText = args.getString(ARG_SEARCH_TEXT);
        this.pageNumber = args.getInt(ARG_PAGE_NUMBER);
        this.itemsPerPage = args.getInt(ARG_ITEMS_PER_PAGE);
    }

    @Override
    protected VacancyPageVO loadData() throws Exception {
        VacancyPaginationDto dto = apiInterface.searchVacancy(searchText, itemsPerPage, pageNumber);
        final VacancyPageVO resultVO = converter.toVacancyPageVO(dto);
        if (pageNumber == 1 && !resultVO.getItems().isEmpty()) {
            try {
                VacancyCacheHelper.cacheInDatabase(resultVO.getItems(), searchText, dbHelper);
            } catch (Throwable e) {
                Log.e(TAG, "loadData: error while caching items", e);
            }
        }

        return resultVO;
    }
}
