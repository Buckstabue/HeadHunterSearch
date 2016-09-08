package ru.hh.headhuntersearch;

import android.app.Application;

import ru.hh.headhuntersearch.di.component.AppComponent;
import ru.hh.headhuntersearch.di.component.DaggerAppComponent;
import ru.hh.headhuntersearch.di.module.AppModule;
import ru.hh.headhuntersearch.di.module.NetworkModule;
import ru.hh.headhuntersearch.util.Const;

public class App extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        // TODO init lazily
        appComponent = buildAppComponent();
    }

    private AppComponent buildAppComponent() {
        return DaggerAppComponent.builder()
                .networkModule(new NetworkModule(Const.API_URL))
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
