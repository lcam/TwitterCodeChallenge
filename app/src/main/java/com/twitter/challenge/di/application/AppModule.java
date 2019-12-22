package com.twitter.challenge.di.application;

import com.twitter.challenge.util.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    //application level dependencies like retrofit, glide

    @Singleton
    @Provides
    static Retrofit provideRetrofitInstance(OkHttpClient client){
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    static OkHttpClient provideMainOkHttpInstance(HttpLoggingInterceptor httpLoggingInterceptor){
        return new OkHttpClient().newBuilder()
                //.addInterceptor(httpLoggingInterceptor) // log API requests
                .build();
    }


    @Singleton
    @Provides
    static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
    }
}
