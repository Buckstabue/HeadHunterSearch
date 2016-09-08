package ru.hh.headhuntersearch.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.hh.headhuntersearch.App;
import ru.hh.headhuntersearch.R;
import ru.hh.headhuntersearch.adapter.VacancyAdapter;
import ru.hh.headhuntersearch.di.component.DaggerVacanciesComponent;
import ru.hh.headhuntersearch.di.module.VacanciesModule;
import ru.hh.headhuntersearch.entity.vo.VacancyVO;
import ru.hh.headhuntersearch.presenter.VacanciesPresenter;
import ru.hh.headhuntersearch.view.VacanciesView;

public class VacanciesFragment
        extends BaseFragment<VacanciesView, VacanciesPresenter>
        implements VacanciesView {
    public static final String TAG = VacanciesFragment.class.getSimpleName();
    private static final int LOADING_THRESHOLD = 5;

    @Inject
    VacanciesPresenter vacanciesPresenter;

    @BindView(R.id.vacancies)
    RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private VacancyAdapter adapter;
    private LinearLayoutManager layoutManager;

    {
        DaggerVacanciesComponent.builder()
                .appComponent(App.getAppComponent())
                .vacanciesModule(new VacanciesModule(this))
                .build()
                .inject(this);
    }

    public static VacanciesFragment newInstance() {
        return new VacanciesFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_vacancies;
    }

    @NonNull
    @Override
    protected VacanciesPresenter getPresenter() {
        return vacanciesPresenter;
    }

    @Override
    void onViewCreatedImpl(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        adapter = new VacancyAdapter();
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(onScrollListener);
        swipeRefreshLayout.setOnRefreshListener(() -> getPresenter().onRefreshRequested());
        adapter.setCallback(getPresenter());
    }

    public void onNewSearchRequest(@NonNull String request) {
        getPresenter().onNewSearchRequest(request);
    }

    @Override
    public void showVacancies(List<VacancyVO> vacancies) {
        adapter.setVacancies(vacancies);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadingMore(boolean showLoading) {
        adapter.setLoadingMore(showLoading, recyclerView);
    }

    @Override
    public void showSplashProgress(boolean show) {
        adapter.setSplashScreenShown(show, recyclerView);
    }

    @Override
    public void showRefreshProgress(boolean show) {
        swipeRefreshLayout.setRefreshing(show);
    }

    @Override
    public void showLoadMoreError() {
        Toast.makeText(getContext(), R.string.error_could_not_load_data, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoadFirstPageError(boolean show) {
        adapter.setErrorViewShown(show, recyclerView);
    }

    @Override
    public void showRefreshError() {
        Toast.makeText(getContext(), R.string.error_could_not_refresh, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showEmptyView(boolean show) {
        adapter.setEmptyViewShown(show, recyclerView);
    }

    @Override
    public void blockRefreshing(boolean block) {
        swipeRefreshLayout.setEnabled(!block);
    }

    private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = recyclerView.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
            if (totalItemCount == 0) {
                return; // loading more feature is available if there is at least one item in list
            }

            if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + LOADING_THRESHOLD)) {
                getPresenter().onLoadMoreRequested();
            }
        }
    };
}
