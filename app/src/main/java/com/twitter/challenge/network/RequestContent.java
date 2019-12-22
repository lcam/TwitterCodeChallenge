package com.twitter.challenge.network;

import android.util.Log;

import com.twitter.challenge.model.Conditions;
import com.twitter.challenge.ui.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RequestContent {
    private static final String TAG = "RequestContent";

    private MainNetworkClient mainNetworkClient;

    @Inject
    public RequestContent(MainNetworkClient mainNetworkClient) {
        this.mainNetworkClient = mainNetworkClient;
    }

    public Observable<Resource<Conditions>> queryConditions() {
        Single<Conditions> source = mainNetworkClient.requestConditions();

        return source.toObservable()
                .onErrorReturn(throwable -> defaultConditions(throwable))
                .timeout(10, TimeUnit.SECONDS)
                .map(conditions -> mapToResource(conditions))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Resource<List<Conditions>>> queryFutureConditions(int daysAhead) {

        List<Observable<Conditions>> observables = new ArrayList<>();
        for(int i=1; i<=daysAhead; i++) {
            observables.add(mainNetworkClient.requestFutureConditions(i));
        }

        return Observable.concat(observables)
                .toList().toObservable()
                .onErrorReturn(throwable -> defaultConditionsList(throwable))
                .timeout(10, TimeUnit.SECONDS)
                .map(conditionsList -> mapToResourceList(conditionsList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Conditions defaultConditions(Throwable throwable) {
        Log.e(TAG, "defaultConditions: ERROR " + throwable.getLocalizedMessage());
        return new Conditions();
    }

    private Resource<Conditions> mapToResource(Conditions conditions) {
        if(conditions != null && conditions.getName() == null){
            return Resource.error("Could not retrieve weather conditions", (Conditions)null);
        }

        Log.d(TAG, "mapToResource: SUCCESS");
        return Resource.success(conditions);
    }

    private List<Conditions> defaultConditionsList(Throwable throwable) {
        Log.e(TAG, "defaultConditions: ERROR " + throwable.getLocalizedMessage());
        Conditions conditions = new Conditions();
        List<Conditions> conditionsList = new ArrayList<>();
        conditionsList.add(conditions);
        return conditionsList;
    }

    private Resource<List<Conditions>> mapToResourceList(List<Conditions> conditionsList) {
        if(conditionsList != null && conditionsList.get(0).getName() == null){
            return Resource.error("Could not retrieve future weather conditions", (List<Conditions>)null);
        }

        Log.d(TAG, "mapToResource: SUCCESS");
        return Resource.success(conditionsList);
    }

}
