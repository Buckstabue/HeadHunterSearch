package ru.hh.headhuntersearch.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import ru.hh.headhuntersearch.adapter.VacancyAdapter;
import ru.hh.headhuntersearch.async.AsyncResult;
import ru.hh.headhuntersearch.async.loader.VacanciesAsyncLoader;
import ru.hh.headhuntersearch.data.network.ApiInterface;
import ru.hh.headhuntersearch.entity.converter.DtoToVoConverter;
import ru.hh.headhuntersearch.entity.vo.VacancyPageVO;
import ru.hh.headhuntersearch.entity.vo.VacancyVO;
import ru.hh.headhuntersearch.util.Const;
import ru.hh.headhuntersearch.view.VacanciesView;

public class VacanciesPresenter extends BasePresenter<VacanciesView>
        implements LoaderManager.LoaderCallbacks<AsyncResult<VacancyPageVO>>, VacancyAdapter.Callback {
    private static final String TAG = VacanciesPresenter.class.getSimpleName();

    private static final String KEY_DATA = "internal_data";

    private static final int GET_VACANCIES_LOADER_ID = 0;
    private final ApiInterface apiInterface;
    private final DtoToVoConverter converter;
    private final SQLiteOpenHelper dbHelper;
    private Data data;

    public VacanciesPresenter(Context context,
                              VacanciesView view,
                              ApiInterface apiInterface,
                              DtoToVoConverter converter,
                              SQLiteOpenHelper dbHelper) {
        super(context, view);
        this.apiInterface = apiInterface;
        this.converter = converter;
        this.dbHelper = dbHelper;
    }

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        if (savedInstanceState == null) {
            data = new Data();
            getView().blockRefreshing(true);
        } else {
            data = savedInstanceState.getParcelable(KEY_DATA);
            restoreViewState(data);
            reconnectLoaderIfNeeded();
        }
    }

    private void reconnectLoaderIfNeeded() {
        if (getLoaderManager() == null) {
            Log.w(TAG, "reconnectLoaderIfNeeded: loaderManager is null");
            return;
        }
        EnumSet<State> loadingStates =
                EnumSet.of(State.REFRESHING, State.LOADING_FIRST_PAGE, State.LOADING_ADDITIONAL_PAGE);
        if (loadingStates.contains(data.state)) {
            getLoaderManager().initLoader(GET_VACANCIES_LOADER_ID, null, this);
        }
    }

    private void restoreViewState(Data data) {
        if (data.isEmptyViewShown) {
            getView().showEmptyView(true);
        }
        switch (data.state) {
            case INTRO:
                getView().blockRefreshing(true);
                break;
            case LOADING_FIRST_PAGE:
                getView().blockRefreshing(true);
                getView().showSplashProgress(true);
                break;
            case LOADING_ADDITIONAL_PAGE:
                getView().showVacancies(data.vacancies);
                getView().showLoadingMore(true);
                break;
            case IDLE:
                if (!data.isEmptyViewShown) {
                    getView().showVacancies(data.vacancies);
                }
                break;
            case REFRESHING:
                if (!data.isEmptyViewShown) {
                    getView().showVacancies(data.vacancies);
                }
                getView().showRefreshProgress(true);
                break;
            case ERROR_COULD_NOT_LOAD_FIRST_PAGE:
                getView().showLoadFirstPageError(true);
                break;
        }
    }

    @Override
    public Loader<AsyncResult<VacancyPageVO>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case GET_VACANCIES_LOADER_ID:
                return new VacanciesAsyncLoader(getContext(), apiInterface, converter, dbHelper, args);
        }
        throw new IllegalStateException("Unknown loader id=" + id);
    }

    @Override
    public void onLoadFinished(Loader<AsyncResult<VacancyPageVO>> loader, AsyncResult<VacancyPageVO> result) {
        if (getView() == null) {
            Log.w(TAG, "onLoadFinished: view is null");
            return;
        }
        if (result.getException() != null) {
            handleLoadingError(result.getException());
            return;
        }
        // we are here if loaded successfully
        VacancyPageVO vacancyPageVO = result.getData();
        switch (data.state) {
            case LOADING_FIRST_PAGE:
                data.vacancies = new ArrayList<>(vacancyPageVO.getItems());
                getView().showLoadFirstPageError(false);
                getView().showSplashProgress(false);
                break;
            case REFRESHING:
                data.currentPage = 1;
                data.vacancies = new ArrayList<>(vacancyPageVO.getItems());
                getView().showLoadFirstPageError(false);
                getView().showRefreshProgress(false);
                break;
            case LOADING_ADDITIONAL_PAGE:
                data.currentPage = vacancyPageVO.getPageNumber();
                data.vacancies.addAll(vacancyPageVO.getItems());
                getView().showLoadingMore(false);
                break;
        }
        getView().blockRefreshing(false);
        data.state = State.IDLE;
        data.totalItems = vacancyPageVO.getTotalItemsCount();
        if (data.vacancies.isEmpty()) {
            data.isEmptyViewShown = true;
            getView().showEmptyView(true);
        } else {
            data.isEmptyViewShown = false;
            getView().showEmptyView(false);
            getView().showVacancies(data.vacancies);
        }
    }

    private void handleLoadingError(Exception e) {
        switch (data.state) {
            case LOADING_ADDITIONAL_PAGE:
                data.state = State.IDLE;
                getView().showLoadMoreError();
                break;
            case LOADING_FIRST_PAGE:
                getView().showVacancies(Collections.emptyList());
                getView().showSplashProgress(false);
                getView().showLoadFirstPageError(true);
                data.state = State.ERROR_COULD_NOT_LOAD_FIRST_PAGE;
                break;
            case REFRESHING:
                getView().showRefreshError();
                getView().showRefreshProgress(false);
                data.state = State.IDLE;
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<AsyncResult<VacancyPageVO>> loader) {
    }

    public void onNewSearchRequest(@NonNull String request) {
        if (request.equals(data.query) && data.state != State.ERROR_COULD_NOT_LOAD_FIRST_PAGE) {
            return; // the same request
        }
        // if request is new or it previously failed
        data.query = request;
        data.state = State.LOADING_FIRST_PAGE;
        data.currentPage = 1;
        getView().showSplashProgress(true);
        loadVacanciesPage(data.currentPage);
    }

    public void onLoadMoreRequested() {
        LoaderManager loaderManager = getLoaderManager();
        if (loaderManager == null) {
            Log.w(TAG, "onLoadMoreRequested: loaderManager is null");
            return;
        }
        if (data.state != State.IDLE) {
            return; // already doing another loading task
        }
        if (data.vacancies.size() == data.totalItems) {
            return; // assume that all items are loaded
        }
        data.state = State.LOADING_ADDITIONAL_PAGE;
        getView().showLoadingMore(true);
        loadVacanciesPage(data.currentPage + 1);
    }

    public void onRefreshRequested() {
        data.state = State.REFRESHING;
        loadVacanciesPage(1); // loading the first page only when refreshing
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putParcelable(KEY_DATA, data);
    }

    private void loadVacanciesPage(int pageNumber) {
        LoaderManager loaderManager = getLoaderManager();
        if (loaderManager == null) {
            Log.w(TAG, "loadVacanciesPage: loaderManager is null");
            return;
        }

        Bundle args = new Bundle();
        args.putString(VacanciesAsyncLoader.ARG_SEARCH_TEXT, data.query);
        args.putInt(VacanciesAsyncLoader.ARG_ITEMS_PER_PAGE, Const.ITEM_PER_PAGE);
        args.putInt(VacanciesAsyncLoader.ARG_PAGE_NUMBER, pageNumber);
        loaderManager.restartLoader(GET_VACANCIES_LOADER_ID, args, this);
    }

    @Override
    public void onRetryButtonClicked() {
        data.state = State.LOADING_FIRST_PAGE;
        getView().showLoadFirstPageError(false);
        getView().showSplashProgress(true);
        loadVacanciesPage(1);
    }

    private static class Data implements Parcelable {
        private int currentPage = 1;
        private String query = null;
        private List<VacancyVO> vacancies = Collections.emptyList();
        private State state = State.INTRO;
        private boolean isEmptyViewShown = false;
        private int totalItems = 0;

        public Data() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.currentPage);
            dest.writeString(this.query);
            dest.writeTypedList(this.vacancies);
            dest.writeInt(this.state == null ? -1 : this.state.ordinal());
            dest.writeByte(this.isEmptyViewShown ? (byte) 1 : (byte) 0);
            dest.writeInt(this.totalItems);
        }

        protected Data(Parcel in) {
            this.currentPage = in.readInt();
            this.query = in.readString();
            this.vacancies = in.createTypedArrayList(VacancyVO.CREATOR);
            int tmpState = in.readInt();
            this.state = tmpState == -1 ? null : State.values()[tmpState];
            this.isEmptyViewShown = in.readByte() != 0;
            this.totalItems = in.readInt();
        }

        public static final Creator<Data> CREATOR = new Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel source) {
                return new Data(source);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };
    }

    private enum State {
        INTRO, // first loaded screen
        LOADING_FIRST_PAGE,
        LOADING_ADDITIONAL_PAGE,
        IDLE,
        REFRESHING,
        ERROR_COULD_NOT_LOAD_FIRST_PAGE
    }
}
