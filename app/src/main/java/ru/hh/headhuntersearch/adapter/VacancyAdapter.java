package ru.hh.headhuntersearch.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.hh.headhuntersearch.R;
import ru.hh.headhuntersearch.entity.vo.VacancyVO;

public class VacancyAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VACANCY_VIEW_TYPE = 0;
    private static final int LOADING_MORE_VIEW_TYPE = 1;
    private static final int SPLASH_LOADER_VIEW_TYPE = 2;
    private static final int ERROR_VIEW_TYPE = 3;
    private static final int EMPTY_VIEW_TYPE = 4;

    private List<VacancyVO> vacancies = Collections.emptyList();
    private boolean isLoadingMore = false;
    private boolean isSplashScreenShown = false;
    private boolean isErrorViewShown = false;
    private boolean isEmptyViewShown = false;
    private Callback callback;

    public void setVacancies(List<VacancyVO> vacancies) {
        this.vacancies = vacancies;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VACANCY_VIEW_TYPE:
                return new VacancyViewHolder(parent);
            case LOADING_MORE_VIEW_TYPE:
                return new LoadMoreViewHolder(parent);
            case SPLASH_LOADER_VIEW_TYPE:
                return new SplashLoaderViewHolder(parent);
            case ERROR_VIEW_TYPE:
                return new ErrorViewViewHolder(parent);
            case EMPTY_VIEW_TYPE:
                return new EmptyViewHolder(parent);
        }
        throw new IllegalStateException("Unknown view type=" + viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder instanceof VacancyViewHolder) {
            ((VacancyViewHolder) holder).bind(vacancies.get(position), position);
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setSplashScreenShown(boolean showSplashScreen, RecyclerView recyclerView) {
        if (this.isSplashScreenShown == showSplashScreen) {
            return;
        }
        this.isSplashScreenShown = showSplashScreen;
        recyclerView.post(() -> notifyDataSetChanged());
    }

    public void setErrorViewShown(boolean show, RecyclerView recyclerView) {
        if (this.isSplashScreenShown == show) {
            return;
        }
        this.isErrorViewShown = show;
        recyclerView.post(() -> notifyDataSetChanged());
    }

    public void setEmptyViewShown(boolean emptyViewShown, RecyclerView recyclerView) {
        if (this.isEmptyViewShown == emptyViewShown) {
            return;
        }
        this.isEmptyViewShown = emptyViewShown;
        recyclerView.post(() -> notifyDataSetChanged());
    }

    @Override
    public int getItemViewType(int position) {
        if (isSplashScreenShown) {
            return SPLASH_LOADER_VIEW_TYPE;
        }
        if (isErrorViewShown) {
            return ERROR_VIEW_TYPE;
        }
        if (isEmptyViewShown) {
            return EMPTY_VIEW_TYPE;
        }
        if (isLoadingMore && position == vacancies.size()) {
            return LOADING_MORE_VIEW_TYPE;
        }
        return VACANCY_VIEW_TYPE;
    }

    @Override
    public int getItemCount() {
        if (isSplashScreenShown) {
            return 1;
        }
        if (isErrorViewShown) {
            return 1;
        }
        if (isEmptyViewShown) {
            return 1;
        }
        if (isLoadingMore) {
            return vacancies.size() + 1;
        }
        return vacancies.size();
    }

    public void setLoadingMore(boolean loadingMore, RecyclerView recyclerView) {
        if (this.isLoadingMore == loadingMore) {
            return;
        }
        this.isLoadingMore = loadingMore;
        recyclerView.post(() -> notifyDataSetChanged());
    }

    static class VacancyViewHolder extends BaseViewHolder {
        @BindView(R.id.title)
        TextView titleTextView;

        @BindView(R.id.employer)
        TextView employerTextView;

        public VacancyViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_vacancy);
            ButterKnife.bind(this, itemView);
        }

        public void bind(VacancyVO vacancy, int position) {
            titleTextView.setText(String.format("%d. %s", position + 1, vacancy.getVacancyName()));
            employerTextView.setText(vacancy.getEmployerName());
        }
    }

    static class LoadMoreViewHolder extends BaseViewHolder {
        public LoadMoreViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_load_more);
        }
    }

    static class SplashLoaderViewHolder extends BaseViewHolder {
        public SplashLoaderViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_splash_loader);
        }
    }

    class ErrorViewViewHolder extends BaseViewHolder {

        @BindView(R.id.retry)
        Button retryButton;

        public ErrorViewViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_error);
            ButterKnife.bind(this, itemView);
            retryButton.setOnClickListener(v -> {
                if (callback != null) {
                    callback.onRetryButtonClicked();
                }
            });
        }
    }

    static class EmptyViewHolder extends BaseViewHolder {

        public EmptyViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_empty);
        }
    }

    public interface Callback {
        void onRetryButtonClicked();
    }
}
