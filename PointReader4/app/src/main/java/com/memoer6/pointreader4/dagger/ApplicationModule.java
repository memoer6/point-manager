package com.memoer6.pointreader4.dagger;

//Because we wish to setup caching, we need an Application context. This module, will be used
// to provide this reference. We will define a method annotated with @Provides that denotes
// to Dagger that this method is the constructor for the Application return type:

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {

        return mApplication;
    }
}
