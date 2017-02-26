package com.memoer6.pointreader4.model;


import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface PointApiService {


    //In case of RxJava, we’ll change Call<T> with Observable<T>.
    //Read User Data
    @GET("/user/{userId}/data")
    Observable<User> readUserData(@Path("userId") long userId,
                                  @Query("invert") boolean invert,
                                  @Query("count") int count);

    //Get User List
    @GET("/user")
    Observable<List<User>> getUserList();


}
