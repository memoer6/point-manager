package com.memoer6.pointreader;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.memoer6.pointreader.model.PointApiService;
import com.memoer6.pointreader.model.User;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

//The Android Testing Support Library provides an extensive framework for testing Android apps.
// This library provides a set of APIs that allow you to quickly build and run test code for
// your apps, including JUnit 4 and functional user interface (UI) tests.


public class GetUserListTest_deprecated extends InstrumentationTestCase {
    public static final String TAG = "GetUserListServiceTest";
    public static final String BASE_API_URL = "http://192.168.1.110:8080";
    private final static String USERNAME = "Guillermo";
    private final static Long USERID = 1L;


    @SmallTest
    public void testGetUserList() throws Exception {
        String jsonResponseFileName = "get_user_list.json";
        int expectedHttpResponse = 200;
        String reason = "OK";
        //Create RetrofitMockClient with the expected JSON response and code.
        RetrofitMockClient retrofitMockClient = RestServiceMockUtils.getClient(getInstrumentation()
                .getContext(), expectedHttpResponse, reason, jsonResponseFileName);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_API_URL)
                .setClient(retrofitMockClient)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();

        PointApiService restServiceClient = restAdapter.create(PointApiService.class);

        //Run test code

        List<User> userList = restServiceClient.getUserList();
        assertEquals("check the name", userList.get(0).getName(), USERNAME);
        assertEquals("check the user id", userList.get(0).getId(), USERID);

    }

}