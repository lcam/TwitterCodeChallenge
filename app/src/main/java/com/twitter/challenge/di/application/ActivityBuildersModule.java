package com.twitter.challenge.di.application;

import com.twitter.challenge.di.main.MainModule;
import com.twitter.challenge.di.main.MainViewModelsModule;
import com.twitter.challenge.ui.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    //declare Activities here!

    //now MainActivity is a potential client that I can inject dependencies into
    //Automatically creates a subcomponent --> for SCOPING!!!
    @ContributesAndroidInjector(
            modules = {MainViewModelsModule.class, MainModule.class}
    )
    abstract MainActivity contributeMainActivity();
}
