package ru.hh.headhuntersearch.di.component;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Singleton;

import dagger.Component;
import ru.hh.headhuntersearch.data.network.ApiInterface;
import ru.hh.headhuntersearch.di.module.AppModule;
import ru.hh.headhuntersearch.di.module.ConverterModule;
import ru.hh.headhuntersearch.di.module.DatabaseModule;
import ru.hh.headhuntersearch.di.module.NetworkModule;
import ru.hh.headhuntersearch.entity.converter.DtoToVoConverter;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, ConverterModule.class, DatabaseModule.class})
public interface AppComponent {

    Context getApplicationContext();

    ApiInterface getApiInterface();

    DtoToVoConverter getDtoToVoConverter();

    SQLiteOpenHelper getSqLiteOpenHelper();
}
