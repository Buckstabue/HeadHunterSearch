package ru.hh.headhuntersearch.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.hh.headhuntersearch.presenter.BasePresenter;
import ru.hh.headhuntersearch.view.BaseView;

public abstract class BaseFragment<V extends BaseView, P extends BasePresenter<V>>
        extends Fragment
        implements BaseView {

    @LayoutRes
    abstract protected int getLayoutResId();

    @NonNull
    abstract protected P getPresenter();

    abstract void onViewCreatedImpl(View view, @Nullable Bundle savedInstanceState);

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();
        getPresenter().onStart();
    }

    @CallSuper
    @Override
    public void onStop() {
        getPresenter().onStop();
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getPresenter().attachView((V) this);
        getPresenter().setLoaderManager(getLoaderManager());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getPresenter().onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getPresenter().detachView();
        getPresenter().setLoaderManager(null);
    }

    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        onViewCreatedImpl(view, savedInstanceState);
        super.onViewCreated(view, savedInstanceState);
        getPresenter().onViewCreated(savedInstanceState);
    }
}
