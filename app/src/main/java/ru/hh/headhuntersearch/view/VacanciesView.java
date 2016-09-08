package ru.hh.headhuntersearch.view;

import java.util.List;

import ru.hh.headhuntersearch.entity.vo.VacancyVO;

public interface VacanciesView extends BaseView {

    void showVacancies(List<VacancyVO> vacancyPageVO);

    void showLoadingMore(boolean showLoading);

    void showSplashProgress(boolean show);

    void showRefreshProgress(boolean show);

    void showLoadMoreError();

    void showLoadFirstPageError(boolean show);

    void showRefreshError();

    void showEmptyView(boolean show);

    void blockRefreshing(boolean block);
}
