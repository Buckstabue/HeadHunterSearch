package ru.hh.headhuntersearch.di.module;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.Converter;
import retrofit.converter.JacksonConverter;
import ru.hh.headhuntersearch.BuildConfig;
import ru.hh.headhuntersearch.data.network.ApiInterface;
import ru.hh.headhuntersearch.util.Const;


@Module
public class NetworkModule {

    private final String endPoint;

    public NetworkModule(String baseUrl) {
        this.endPoint = baseUrl;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        // TODO do we need it?
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    @Singleton
    ObjectMapper getObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Provides
    @Singleton
    Converter getConverter(ObjectMapper objectMapper) {
        return new JacksonConverter(objectMapper);
    }

    @Provides
    @Singleton
    RestAdapter provideRetrofit(Converter converter, OkHttpClient okHttpClient) {
        return new RestAdapter.Builder()
                .setClient(new OkClient(okHttpClient))
                .setConverter(converter)
                .setEndpoint(endPoint)
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .setRequestInterceptor(request -> {
                    request.addHeader("Content-Type", "application/vnd.api+json");
                    String userAgent = String.format("User-Agent: BestAppEver/3.0 (%s)", Const.DEVELOPER_EMAIL);
                    request.addHeader("User-Agent", userAgent);
                }).build();
    }

    @Provides
    @Singleton
    ApiInterface provideApiInterface(RestAdapter apiRestAdapter) {
        return apiRestAdapter.create(ApiInterface.class);
    }
}