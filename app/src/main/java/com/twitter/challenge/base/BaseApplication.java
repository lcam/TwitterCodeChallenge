package com.twitter.challenge.base;

import com.twitter.challenge.di.application.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class BaseApplication extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        //binding an app instance to an app component
        return DaggerAppComponent.builder().application(this).build();
        //return null;
    }
}
