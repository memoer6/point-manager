package com.memoer6.pointreader2.model;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PointApiService {


    //Read User Data
    @GET("/user/{userId}/data")
    Call<User> readUserData(@Path("userId") long userId,
                      @Query("invert") boolean invert,
                      @Query("count") int count);

    //Get User List
    @GET("/user")
    Call<List<User>> getUserList();


}
