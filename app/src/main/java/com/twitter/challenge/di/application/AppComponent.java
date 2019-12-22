package com.twitter.challenge.di.application;

import android.app.Application;

import com.twitter.challenge.base.BaseApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton //this component is now associated with the Singleton scope
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ActivityBuildersModule.class,
                AppModule.class,
                ViewModelFactoryModule.class
        }
)
public interface AppComponent extends AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
            //available at the time this component is constructed
        Builder application(Application application); //used to bind an app instance to an app component

        AppComponent build();
    }
}
