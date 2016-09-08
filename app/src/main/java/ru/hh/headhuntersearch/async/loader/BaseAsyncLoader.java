package ru.hh.headhuntersearch.async.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import ru.hh.headhuntersearch.async.AsyncResult;

public abstract class BaseAsyncLoader<T> extends AsyncTaskLoader<AsyncResult<T>> {

    public BaseAsyncLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    protected abstract T loadData() throws Exception;

    @Override
    public final AsyncResult<T> loadInBackground() {
        T data;
        try {
            data = loadData();
        } catch (Exception e) {
            return new AsyncResult<>(e);
        }

        return new AsyncResult<>(data);
    }
}
