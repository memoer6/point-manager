package com.memoer6.pointreader4.dagger;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.memoer6.pointreader4.model.PointApiService;
import com.memoer6.pointreader4.presenter.Presenter;
import com.memoer6.pointreader4.utils.Utils;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//When you have an object that needs or depends on another object to do its work, you have a
// dependency. Dependencies can be solved by letting the dependent object create the dependency
// or asking a factory object to make one. In the context of dependency injection, however,
// the dependencies are supplied to the class that needs the dependency to avoid the need for
// the class itself to create them. This way you create software that is loosely coupled
// and highly maintainable.

// Modules are classes whose methods provide dependencies, so we define a class and
// annotate it with @Module, thus, Dagger will know where to find the dependencies in order
// to satisfy them when constructing class instances

@Module
public class NetworkModule {


    private String mBaseUrl;


    public NetworkModule(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    //@Provide: Inside modules we define methods containing this annotation which tells Dagger
    // how we want to construct and provide those mentioned dependencies.
    //@Singleton. Single instance of this provided object is created and shared.

    @Provides
    @Singleton
    HttpLoggingInterceptor provideLoggingInterceptor() {

        //Since logging isn’t integrated by default anymore in Retrofit 2, we need to add a
        // logging interceptor for OkHttp.
        //OkHttp’s logging interceptor has four log levels: NONE, BASIC, HEADERS, BODY
        return new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

    }

    //If there is connectivity, we tell the request it can reuse the data for sixty seconds.
    //If there’s no connectivity, we ask to be given only (only-if-cached) ‘stale’ data
    // up to 7 days ago.
    @Provides
    @Singleton
    Interceptor cacheInterceptor(final Application application) {

        return new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (Utils.isNetworkAvailable(application)) {
                    request = request.newBuilder().header("Cache-Control",
                            "public, max-age=" + 60).build();
                } else {
                    request = request.newBuilder().header("Cache-Control",
                            "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                }
                return chain.proceed(request);
            }
        };

    }



    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; //10 MB
        return new Cache(application.getCacheDir(), cacheSize);

    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor loggingInterceptor, Cache cache,
                                    Application application) {

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(cacheInterceptor(application))
                .cache(cache)
                .build();

    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
    }



    @Provides
    @Singleton
    PointApiService providePointApiService(Gson gson, OkHttpClient okHttpClient) {

        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   //RxJava
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(PointApiService.class);

    }


    @Provides
    @Singleton
    public Presenter providesPresenter(PointApiService service) {
        return new Presenter(service);
    }


}
