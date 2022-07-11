package com.example.catbreed.di;

import com.example.catbreed.BuildConfig;
import com.example.catbreed.api.ApiService;
import com.example.catbreed.api.RxSingleSchedulers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {
    @Singleton
    @Provides
    Gson provideGsonBuilder() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }
    @Provides
    public RxSingleSchedulers providesScheduler() {
        return RxSingleSchedulers.DEFAULT;
    }


    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder().addInterceptor(interceptor);
        return client.build();
    }

    @Singleton
    @Provides
    Retrofit.Builder provideRetrofit(Gson gson, OkHttpClient client) {

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.STAGING_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));


    }
    @Singleton
    @Provides
    ApiService provideApiService(Retrofit.Builder retrofit) {
        return retrofit
                .build()
                .create(ApiService.class);
    }
}
