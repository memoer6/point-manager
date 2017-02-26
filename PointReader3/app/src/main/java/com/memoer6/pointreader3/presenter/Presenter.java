package com.memoer6.pointreader3.presenter;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.memoer6.pointreader3.model.PointApiService;
import com.memoer6.pointreader3.model.User;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class Presenter {

    private final static String BASE_URL = "http://192.168.1.110:8080";
    private final static PointApiService POINT_API_SERVICE = getServiceInstance();

    private Presenter.Service mView;


    //Constructor that register activities
    public Presenter(Presenter.Service view) {

        mView = view;
    }


    private static PointApiService getServiceInstance() {

        //Since logging isn’t integrated by default anymore in Retrofit 2, we need to add a
        // logging interceptor for OkHttp.
        //OkHttp’s logging interceptor has four log levels: NONE, BASIC, HEADERS, BODY
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Gson gson = new GsonBuilder()
               .setDateFormat("yyyy-MM-dd")
               .create();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)    //interceptor
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   //RxJava
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(PointApiService.class);
    }


    //Observables emits a stream of data or events
    //Subscriber (Observers), watch Observables by subscribing to them and acts upon the emitted
    // data. Subscribers are notified when an Observable emits a value. They are also notified
    // when an error occurred or if the observable sends the event that is has no more values
    // to emit. The corresponding functions are onNext, onError, and onCompleted() from
    // the Observer interface.A instance of Subscription represents the connection between an
    // observer and an observable. You can use the unsubscribe() method to remove this connection

    //The observeOn() and subscribeOn() methods can define the threads in which the observer and
    // subscriber should be executed.
    //The Observable.observeOn() method can define a thread that is used to monitor and check for
    // emitted data from the observable. The subscribers onNext, onCompleted and onError methods
    // are executed in this thread.
    //The Observable.subscribeOn() method can define the thread that is used to execute the
    // observable code. For example the observable might perform a network operation in this
    // thread and might therefore be a long running operation.



    //Read User Data
    public void readUserData(long userId, boolean invert, int count) {

        Observable<User> observableUserData = POINT_API_SERVICE.readUserData(userId, invert, count);

        observableUserData
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        // handle completed
                    }

                    @Override
                    public void onError(Throwable e) {
                        // handle error
                        mView.onFailure(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(User user) {
                        // handle response
                        mView.onSuccess(user);

                    }
                });



    }

    //Get User List
    public void getUserList() {

        Observable<List<User>> observableUserList = POINT_API_SERVICE.getUserList();

        observableUserList
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<User>>() {
                    @Override
                    public void onCompleted() {
                        // handle completed
                    }

                    @Override
                    public void onError(Throwable e) {
                        // handle error
                        mView.onFailure(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<User> userList) {
                        // handle response
                        mView.onSuccess(userList);

                    }
                });

    }


    //interface to be implemented by views registered with presenter
    // in order to receive callbacks
    //T is a generic type which accepts responses with List<User> and User
    public interface Service<T> {

        void onSuccess(T response);

        void onFailure(String message);

    }

}
