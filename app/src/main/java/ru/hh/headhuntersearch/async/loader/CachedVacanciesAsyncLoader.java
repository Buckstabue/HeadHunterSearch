package ru.hh.headhuntersearch.async.loader;

import android.content.Context;
import android.os.Bundle;

import java.util.List;

import ru.hh.headhuntersearch.data.db.VacancyCacheHelper;
import ru.hh.headhuntersearch.entity.vo.VacancyPageVO;
import ru.hh.headhuntersearch.entity.vo.VacancyVO;
import ru.hh.headhuntersearch.exception.EmptyCacheException;

public class CachedVacanciesAsyncLoader extends BaseAsyncLoader<VacancyPageVO> {
    public static final String ARG_SEARCH_TEXT = "search_text";

    private final String searchText;

    public CachedVacanciesAsyncLoader(Context context, Bundle args) {
        super(context);
        this.searchText = args.getString(ARG_SEARCH_TEXT);
    }

    @Override
    protected VacancyPageVO loadData() throws Exception {
        List<VacancyVO> vacancies = VacancyCacheHelper.getVacancies(getContext(), searchText);
        if (vacancies.isEmpty()) {
            throw new EmptyCacheException();
        }
        return new VacancyPageVO(1, 1, vacancies, vacancies.size());
    }

}
