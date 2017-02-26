package com.memoer6.pointreader.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.memoer6.pointreader.R;
import com.memoer6.pointreader.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FirstActivity extends AppCompatActivity  {

    //tag that is saved in a bundle to retain the user list after screen orientation changes
    protected static final String LIST_USERS = "com.memoer6.pointreader.FirstActivity.list_users";
    protected static final String USER_ID = "com.memoer6.pointreader.FirstActivity.user_id";
    private static final String SHOW_PROGRESS = "com.memoer6.pointreader.FirstActivity.showProgress";

    private BroadcastReceiver mResponseReceiver;
    private List<User> mUserList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private Button mButton;
    private PopupMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        mButton = (Button) findViewById(R.id.button);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        //this instantiation avoids the crash when doing multiple orientation changes with
        //progress bar on
        menu = new PopupMenu(this, mButton);

        if (savedInstanceState != null) {

            if (savedInstanceState.getBoolean(SHOW_PROGRESS)) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }

        assert mButton != null;
        //Avoid all uppercase in button text
        mButton.setTransformationMethod(null);

        //Request list of users
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                menu = new PopupMenu(FirstActivity.this, mButton);

                mProgressBar.setVisibility(View.VISIBLE);
                GetDataIntentService.showUserList(FirstActivity.this);


                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        intent.putExtra(USER_ID, Long.valueOf(item.getItemId()));

                        Bundle bundle = new Bundle();
                        bundle.putSerializable(LIST_USERS, (ArrayList<User>) mUserList);
                        intent.putExtras(bundle);
                        startActivity(intent);

                        return true;
                    }
                });

            }
        });

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putBoolean(SHOW_PROGRESS, mProgressBar.isShown());

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }



    //Dinamically register an instance of Broadcast Receiver to receive data from Intent Service
    // It can also be registered via XML
    // A BroadcastReceiver object is only valid for the duration of the call to onReceive
    // Once the code returns from this function, the system considers the object to be
    // finished and no longer active. Anything that requires asynchronous operation is not
    // available, because you will need to return from the function to handle the asynchronous
    // operation, but at that point the BroadcastReceiver is no longer active and thus the system
    // is free to kill its process before the asynchronous operation completes.
    private void registerResponseReceiver() {

        // The filter's action is BROADCAST_USERLIST
        IntentFilter mUserListIntentFilter = new IntentFilter(
                GetDataIntentService.BROADCAST_USERLIST);

        // Instantiates a new BroadcastReceiver
        mResponseReceiver = new ResponseReceiver();

        // Registers the UserListReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mResponseReceiver, mUserListIntentFilter);

    }

    // Receive Status Broadcasts from an IntentService
    // To receive broadcast Intent objects, use a subclass of BroadcastReceiver. In the
    // subclass, implement the BroadcastReceiver.onReceive() callback method, which
    // LocalBroadcastManager invokes when it receives an Intent. LocalBroadcastManager
    // passes the incoming Intent to BroadcastReceiver.onReceive()
    // Broadcast receiver for receiving status updates from the IntentService
    private final class ResponseReceiver extends BroadcastReceiver
    {
        // Prevents instantiation
        private ResponseReceiver() {
        }
        // Called when the BroadcastReceiver gets an Intent it's registered to receive
        public void onReceive(Context context, Intent intent) {

            mProgressBar.setVisibility(View.INVISIBLE);

            if (Objects.equals(intent.getAction(), GetDataIntentService.BROADCAST_USERLIST)) {

                // if STATUS extra in internet is STATUS SUCCESS,
                // set STATUS_ERROR by default if status is not found
                if (intent.getIntExtra(GetDataIntentService.STATUS,
                        GetDataIntentService.STATUS_ERROR) ==
                        GetDataIntentService.STATUS_SUCCESS) {

                    mUserList = (List<User>) intent
                            .getSerializableExtra(GetDataIntentService.DATA);


                    if (mUserList != null) {

                        if (mUserList.isEmpty()) {
                            //Shows "no users available"
                            displayMessage(getString(R.string.emptyUserListMessage),
                                    Toast.LENGTH_SHORT);

                        //list is not empty
                        } else {


                            for (User user: mUserList) {
                                //groupId, itemId, order, titleRes
                                menu.getMenu().add(0, user.getId().intValue(),
                                        0, user.getName());
                            }
                            menu.show();

                        }


                    //list is null
                    } else {
                        displayMessage(getString(R.string.errorUserListMessage),
                                Toast.LENGTH_SHORT);
                    }

                //STATUS_ERROR
                } else {
                    displayMessage(getString(R.string.errorUserListMessage),
                            Toast.LENGTH_SHORT);

                }

            }

        }
    }

    public void displayMessage(String message, int duration) {

        Toast.makeText(this, message, duration).show();

    }


    @Override
    protected void onResume() {
        // Call up to the superclass.
        super.onResume();

        // Register BroadcastReceivers
        registerResponseReceiver();

    }

    @Override
    protected void onPause() {
        // Call onPause() in superclass.
        super.onPause();

        // Unregister BroadcastReceiver.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mResponseReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();


        //dismiss the menu if it's open and the screen is rotated
        //avoid message: FirstActivity has leaked window android.widget.PopupWindow after rotation
        if (menu != null) {
            menu.dismiss();
        }

    }

}
