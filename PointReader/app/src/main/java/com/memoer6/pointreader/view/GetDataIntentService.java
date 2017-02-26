package com.memoer6.pointreader.view;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.memoer6.pointreader.model.User;
import com.memoer6.pointreader.presenter.Presenter;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 *
 */

//The best way to limit the lifespan of your service is to use an IntentService, which finishes
// itself as soon as it's done handling the intent that started it.Leaving a service running
// when it’s not needed is one of the worst memory-management mistakes an Android app can make.
public final class GetDataIntentService extends IntentService {


    //actions supported by this service via intent
    private static final String LIST_USERS = "com.memoer6.pointreader.view.action.LIST_USERS";
    private static final String SHOW_USER_DATA = "com.memoer6.pointreader.view.action.SHOW_USER_DATA";



    // This service needs to report the status of the request back to the activity
    // The recommended way to send and receive status is to use a LocalBroadcastManager,
    // which limits broadcast Intent objects to components in your own app.
    // Sending an broadcast Intent doesn't start or resume an Activity. The BroadcastReceiver
    // for an Activity receives and processes Intent objects even when your app is in the
    // background, but doesn't force your app to the foreground. If you want to notify the
    // user about an event that happened in the background while your app was not visible,
    // use a Notification. Never start an Activity in response to an incoming broadcast Intent
    // To send the status of a work request in an IntentService to other components,
    // first create an Intent that contains the status in its extended data.
    // Defines a custom Intent action
    public static final String BROADCAST_USERLIST =
            "com.memoer6.pointreader.view.action.BROADCASTUSERLIST";
    public static final String BROADCAST_USERDATA =
            "com.memoer6.pointreader.view.action.BROADCASTUSERDATA";


    // Defines the key for the Intent to broadcast
    public static final String DATA = "com.memoer6.pointreader.view.extra.DATA";
    public static final String STATUS = "com.memoer6.pointreader.view.extra.STATUS";

     // Define the key for the user
    public static final String USERID = "com.memoer6.pointreader.view.extra.USERID";

    // Define the key for the invert query parameter
    public static final String ORDER = "com.memoer6.pointreader.view.extra.ORDER";

    // Define the key for the number of transactions query parameter
    public static final String COUNT = "com.memoer6.pointreader.view.extra.COUNT";

    // define list of statuses to send back
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_ERROR = 2;

    private Presenter mPresenter;



    public GetDataIntentService() {
        super("GetDataIntentService");
        mPresenter = new Presenter();
    }

    //List user
    public static void showUserList(Context context) {
        Intent intent = new Intent(context, GetDataIntentService.class);
        intent.setAction(LIST_USERS);
        context.startService(intent);
    }

    //Get user data
    public static void showUserData(Context context, long userId, boolean invert, int count) {

        Intent intent = new Intent(context, GetDataIntentService.class);
        intent.setAction(SHOW_USER_DATA);
        intent.putExtra(USERID, userId);
        intent.putExtra(ORDER, invert);
        intent.putExtra(COUNT, count);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent)  {

        if (intent != null) {

            switch(intent.getAction()) {

                case(LIST_USERS):
                    handleActionListUsers();
                    break;

                case(SHOW_USER_DATA):
                    long userId = (long) intent.getSerializableExtra(USERID);
                    boolean invert = intent.getBooleanExtra(ORDER, true);
                    int count = intent.getIntExtra(COUNT, 50);
                    handleActionShowUserData(userId, invert, count);
                    break;
            }
        }
    }


    private void handleActionListUsers()  {

        try {

            //Thread.sleep(3000);

            List<User> userList = mPresenter.getUserList();
            broadcastUserList(STATUS_SUCCESS, userList);

        } catch (Exception e) {

            broadcastUserList(STATUS_ERROR, null);
            e.printStackTrace();
        }
    }


    private void handleActionShowUserData(long userId, boolean invert, int count)  {


        try {

            //Thread.sleep(3000);

            User user = mPresenter.readUserData(userId, invert, count);
            broadcastShowUserData(STATUS_SUCCESS, user);

        } catch (Exception e) {

            broadcastShowUserData(STATUS_ERROR, null);
            e.printStackTrace();
        }
    }

    // Send the Intent by calling LocalBroadcastManager.broadcastUserList(). This sends the Intent
    // to any component in your application that has registered to receive it.
    // Uses LocalBroadcastManager because it doesn't need to send broadcasts across applications,
    // (never go outside of the current process)
    private void broadcastUserList(int status, Collection<User> userList) {

        Intent localIntent = new Intent(BROADCAST_USERLIST)
                // Puts the status into the Intent
                .putExtra(DATA, (Serializable) userList)
                .putExtra(STATUS, status);
        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

    }

    private void broadcastShowUserData(int status, User user) {

        Log.d("ServiceIntent", "User= " + user);
        Log.d("ServiceIntent", "Status= " + status);

        Intent localIntent = new Intent(BROADCAST_USERDATA)
                // Puts the status into the Intent
                .putExtra(DATA, user)
                .putExtra(STATUS, status);
        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);


    }

}
