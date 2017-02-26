package com.memoer6.pointreader4.dagger;


import android.app.Application;

//An important aspect of Dagger 2 is that the library generates code for classes annotated with
// the @Component interface. You can use a class prefixed with Dagger
// (i.e. DaggerTwitterApiComponent.java) that will be responsible for instantiating an instance
// of our dependency graph and using it to perform the injection work for fields annotated
// with @Inject.
//We should do all this work within an Application class since these instances should be declared
// only once throughout the entire lifespan of the application. This class builds up the dependency
// injection context and gives access to it, via the getComponent method.
//Because we are overriding the default Application class, we also modify the application name
// in the manifest file to launch the App. This way your application will use this application
// class to handle the initial instantiation.

public class PointReader extends Application {


    private PresenterDependencyComponent mPresenterComponent;

    @Override
    public void onCreate() {

        super.onCreate();

        mPresenterComponent = DaggerPresenterDependencyComponent.builder()
                // list of modules that are part of this component need to be created here too
                // This also corresponds to the name of your module: %component_name%Module
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule("http://192.168.1.110:8080"))
                .build();

        // If a Dagger 2 component does not have any constructor arguments for any of its modules,
        // then we can use .create() as a shortcut instead:
        //mPresenterComponent = DaggerPresenterDependencyComponent.create();

    }

    public PresenterDependencyComponent getDependencyComponent() {
        return mPresenterComponent;
    }
}
