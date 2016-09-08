package ru.hh.headhuntersearch.di.module;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.hh.headhuntersearch.data.db.HhDataBaseHelper;

@Module
public class DatabaseModule {

    @Singleton
    @Provides
    SQLiteOpenHelper provideSqLiteOpenHelper(Context context) {
        return new HhDataBaseHelper(context);
    }
}
