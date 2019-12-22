package com.twitter.challenge.network;

import com.twitter.challenge.model.Conditions;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MainApi {

    //https://twitter-code-challenge.s3.amazonaws.com/
    @GET("current.json")
    Single<Conditions> getWeather(
    );

    @GET("future_{daysAhead}.json")
    Observable<Conditions> getFutureWeather(
            @Path("daysAhead") int daysAhead
    );
}
