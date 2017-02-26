package com.memoer6.pointreader.presenter;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.memoer6.pointreader.model.PointApiService;
import com.memoer6.pointreader.model.User;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;


public class Presenter {

    private final static String BASE_URL = "http://192.168.1.110:8080";

    private PointApiService pointApiService = getInstance();

    private PointApiService getInstance() {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();


        return new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build()
                .create(PointApiService.class);
    }



    //Read User Data
    public User readUserData(long userId, boolean invert, int count) throws Exception {


        if (pointApiService != null) {
            return pointApiService.readUserData(userId, invert, count);
        } else return null;

    }

    //Get User List
    public List<User> getUserList() throws Exception {


        if (pointApiService != null) {
            return pointApiService.getUserList();
        } else return null;

    }


}
