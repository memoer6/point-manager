package com.memoer6.pointreader2.presenter;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.memoer6.pointreader2.model.PointApiService;
import com.memoer6.pointreader2.model.User;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Presenter {

    private final static String BASE_URL = "http://192.168.1.110:8080";
    private final static PointApiService POINT_API_SERVICE = getServiceInstance();

    private Presenter.Service mView;


    //Constructor that register activities
    public Presenter(Presenter.Service view) {

        //Since the Presenter is not a static class, and its instances are created and destroyed
        // within the activity life-cycle, there is not need to declare the view with weak reference
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
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(PointApiService.class);
    }



    //Read User Data
    public void readUserData(long userId, boolean invert, int count) {

        Call<User> callUserData = POINT_API_SERVICE.readUserData(userId, invert, count);

        //asynchronus call (in UI thread)
        callUserData.enqueue(getCallback(callUserData));

        //synchronus call (must be executed in background thread)
        //callUserData.execute();
    }

    //Get User List
    public void getUserList() {

        Call<List<User>> callUserList = POINT_API_SERVICE.getUserList();
        callUserList.enqueue(getCallback(callUserList));

    }

    //return generic callback type with generic methods that are implemented by both http requests
    private <T> Callback<T> getCallback(Call<T> call) {

        return new Callback<T>() {

            @Override
            public void onResponse(Call<T> call, Response<T> response) {

                //Within Retrofit 2, the onResponse() method is called even though the request
                // wasn’t successful. The Response class has a convenience method isSuccessful()
                // to check yourself whether the request was handled successfully (returning status
                // code 2xx) and you can use the response object for further processing.
                if (response.isSuccessful()) {

                    mView.onSuccess(response.body());

                } else {

                    mView.onFailure(response.errorBody().toString());
                }

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {

                // handle execution failures like no internet connectivity
                mView.onFailure(t.getMessage());
                t.printStackTrace();

            }

        };
    }


    //interface to be implemented by views registered with presenter
    // in order to receive callbacks
    //T is a generic type which accepts responses with List<User> and User
    public interface Service<T> {

        void onSuccess(T response);

        void onFailure(String message);

    }


}
