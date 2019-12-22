package com.twitter.challenge.di.main;

import com.twitter.challenge.network.MainApi;
import com.twitter.challenge.network.MainNetworkClient;
import com.twitter.challenge.network.RequestContent;
import com.twitter.challenge.ui.MainRecyclerAdapter;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MainModule {

    @Provides
    static MainRecyclerAdapter provideMainRecyclerAdapter() {
        return new MainRecyclerAdapter();
    }

    @Provides
    static MainApi provideMainApi(Retrofit retrofit) {
        return retrofit.create(MainApi.class);
    }

    @Provides
    static MainNetworkClient provideMainNetworkClient(MainApi mainApi) {
        return new MainNetworkClient(mainApi);
    }

    @Provides
    static RequestContent provideRequestContent(MainNetworkClient client) {
        return new RequestContent(client);
    }
}
