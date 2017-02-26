package com.memoer6.pointreader4.presenter;


import com.memoer6.pointreader4.model.PointApiService;
import com.memoer6.pointreader4.model.User;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;



public class Presenter {


    private Presenter.Service mView;
    private PointApiService mServiceAPI;

    public Presenter(PointApiService service) {

        mServiceAPI = service;

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
    public void readUserData(Presenter.Service<User> view, long userId, boolean invert, int count) {

        mView = view;

        Observable<User> observableUserData = mServiceAPI.readUserData(userId, invert, count);

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
    public void getUserList(Presenter.Service<List<User>> view) {

        mView = view;

        Observable<List<User>> observableUserList = mServiceAPI.getUserList();

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
