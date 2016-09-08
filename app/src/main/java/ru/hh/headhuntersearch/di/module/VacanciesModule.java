package ru.hh.headhuntersearch.di.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.hh.headhuntersearch.data.network.ApiInterface;
import ru.hh.headhuntersearch.di.PerFragment;
import ru.hh.headhuntersearch.entity.converter.DtoToVoConverter;
import ru.hh.headhuntersearch.presenter.VacanciesPresenter;
import ru.hh.headhuntersearch.view.VacanciesView;

@Module
public class VacanciesModule {

    private VacanciesView vacanciesView;

    public VacanciesModule(VacanciesView vacanciesView) {
        this.vacanciesView = vacanciesView;
    }

    @PerFragment
    @Provides
    VacanciesPresenter provideVacanciesPresenter(Context context,
                                                 ApiInterface apiInterface,
                                                 DtoToVoConverter converter) {
        return new VacanciesPresenter(context, vacanciesView, apiInterface, converter);
    }
}
