package com.twitter.challenge.ui;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.twitter.challenge.model.Conditions;
import com.twitter.challenge.network.RequestContent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class MainViewModel extends ViewModel {

    private static final String TAG = "MainViewModel";

    private RequestContent requestContent;
    private Observable<Resource<Conditions>> conditions;
    private Observable<Resource<List<Conditions>>> futureConditions;


    @Inject
    public MainViewModel(RequestContent requestContent) {
        this.requestContent = requestContent;
    }

    public Observable<Resource<Conditions>> getConditions(){
        if(conditions == null){
            conditions = requestContent.queryConditions();
        }
        return conditions;
    }

    public Observable<Resource<List<Conditions>>> getFutureConditions(int daysAhead){
        if(futureConditions == null){
            futureConditions = requestContent.queryFutureConditions(daysAhead);
        }
        return futureConditions;
    }
}
