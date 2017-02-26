package com.memoer6.pointreader;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.memoer6.pointreader.model.PointApiService;
import com.memoer6.pointreader.model.User;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class ExampleUnitTest2 {


    private final static String BASE_URL = "http://192.168.1.110:8080";
    private final static String USERNAME = "Guillermo";
    private final static Long USERID = 1L;
    private final PointApiService POINT_API_SERVICE_TEST = getInstance();

    private java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private java.util.Date date0, date1;
    private User user;



    private PointApiService getInstance() {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();


        return new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                //.setClient(new MockClient())
                .build()
                .create(PointApiService.class);
    }


    @Test
    public void test_get_user_list() throws Exception {

        List<User> userList = POINT_API_SERVICE_TEST.getUserList();
        Assert.assertEquals("check the name",userList.get(0).getName(), USERNAME);

        Assert.assertEquals("check the user id", userList.get(0).getId(), USERID);

    }

    @Test
    public void test_read_user_data1() throws Exception {


        user = POINT_API_SERVICE_TEST.readUserData(USERID, true, 2);
        Assert.assertEquals(user.getTransactionList().size(), 2);

        date0 = formatter.parse(user.getTransactionList().get(0).getDate());
        date1 = formatter.parse(user.getTransactionList().get(1).getDate());

        Assert.assertTrue( date0.after(date1));

        Assert.assertNotNull(user.getTotalPoints());


    }

    @Test
    public void test_read_user_data2() throws Exception {


        user = POINT_API_SERVICE_TEST.readUserData(USERID, false, 3);
        Assert.assertEquals(user.getTransactionList().size(), 3);

        date0 = formatter.parse(user.getTransactionList().get(0).getDate());
        date1 = formatter.parse(user.getTransactionList().get(1).getDate());

        //date0 > date1, returns greater than 0
        Assert.assertFalse( date0.compareTo(date1) > 0);


    }



}