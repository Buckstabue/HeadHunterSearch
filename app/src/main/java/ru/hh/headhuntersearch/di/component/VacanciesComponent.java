package ru.hh.headhuntersearch.di.component;

import dagger.Component;
import ru.hh.headhuntersearch.di.PerFragment;
import ru.hh.headhuntersearch.di.module.VacanciesModule;
import ru.hh.headhuntersearch.fragment.VacanciesFragment;

@PerFragment
@Component(modules = VacanciesModule.class, dependencies = {AppComponent.class})
public interface VacanciesComponent {

    void inject(VacanciesFragment vacanciesFragment);
}
