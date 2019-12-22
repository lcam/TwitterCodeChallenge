package com.twitter.challenge.network;

import android.util.Log;

import com.twitter.challenge.model.Conditions;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

public class MainNetworkClient {

    private static final String TAG = "MainNetworkClient";

    private final MainApi mainApi;

    @Inject
    public MainNetworkClient(MainApi mainApi) {
        this.mainApi = mainApi;
    }

    public Single<Conditions> requestConditions() {
        Log.d(TAG, "requestConditions: about to make api request");
        return mainApi.getWeather();
    }

    public Observable<Conditions> requestFutureConditions(int daysAhead) {
        Log.d(TAG, "requestFutureConditions: about to make api request - " + daysAhead + " days ahead");
        return mainApi.getFutureWeather(daysAhead);
    }
}
