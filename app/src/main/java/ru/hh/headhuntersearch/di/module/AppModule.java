package ru.hh.headhuntersearch.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Context applicationContext;

    public AppModule(Context applicationContext) {
        this.applicationContext = applicationContext.getApplicationContext();
    }

    @Singleton
    @Provides
    Context provideApplicationContext() {
        return applicationContext;
    }
}
