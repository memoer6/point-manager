package com.memoer6.pointreader.model;


import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface PointApiService {


    //Read User Data
    @GET("/user/{userId}/data")
    User readUserData(@Path("userId") long userId,
                      @Query("invert") boolean invert,
                      @Query("count") int count);

    //Get User List
    @GET("/user")
    List<User> getUserList();


}
