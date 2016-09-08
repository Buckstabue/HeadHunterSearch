package ru.hh.headhuntersearch.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;

import ru.hh.headhuntersearch.view.BaseView;

public class BasePresenter<V extends BaseView> {
    private final Context context;
    private V view;
    private LoaderManager loaderManager;

    public BasePresenter(Context context, V view) {
        this.context = context;
        this.view = view;
    }

    public final void setLoaderManager(LoaderManager loaderManager) {
        this.loaderManager = loaderManager;
    }

    public final void attachView(V view) {
        this.view = view;
    }

    public final void detachView() {
        this.view = null;
    }

    @Nullable
    protected final LoaderManager getLoaderManager() {
        return loaderManager;
    }

    public void onStart() {

    }

    @CallSuper
    public void onStop() {
        setLoaderManager(null);
    }

    public void onViewCreated(Bundle savedInstanceState) {

    }

    protected final V getView() {
        return view;
    }

    protected final Context getContext() {
        return context;
    }

    public void onSaveInstanceState(Bundle state) {

    }

}
