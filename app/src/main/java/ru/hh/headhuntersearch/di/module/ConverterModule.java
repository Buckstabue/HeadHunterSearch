package ru.hh.headhuntersearch.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.hh.headhuntersearch.entity.converter.DtoToVoConverter;

@Module
public class ConverterModule {

    @Provides
    @Singleton
    DtoToVoConverter provideDtoToVoConverter() {
        return new DtoToVoConverter();
    }
}
