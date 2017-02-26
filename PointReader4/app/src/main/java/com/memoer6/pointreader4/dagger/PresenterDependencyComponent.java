package com.memoer6.pointreader4.dagger;


import com.memoer6.pointreader4.view.FirstActivity;
import com.memoer6.pointreader4.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;


//@Component: Components basically are injectors, let’s say a bridge between @Inject and @Module,
// which its main responsibility is to put both together. They just give you instances of all the
// types you defined, for example, we must annotate an interface with @Component and list all
// the @Modules that will compose that component, and if any of them is missing, we get errors
// at compile time. All the components are aware of the scope of dependencies it provides through
// its modules.

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface PresenterDependencyComponent {

    // to update the fields in your activities
    //base classes are not sufficient as injection targets. Dagger 2 relies on strongly typed
    // classes, so you must specify explicitly which ones should be defined.
    void inject(FirstActivity activity);

    void inject(MainActivity activity);

}
