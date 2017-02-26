package com.memoer6.pointreader.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.memoer6.pointreader.R;
import com.memoer6.pointreader.model.Transaction;
import com.memoer6.pointreader.model.User;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//This application runs with "PointTracker" server in Eclipse


public class MainActivity extends AppCompatActivity  {

    private TextView pointsText, nameText;
    private PopupMenu menu;
    private BroadcastReceiver mResponseReceiver;


    //tag that is saved in a bundle to retain the user name after screen orientation changes
    private static final String NAME = "com.memoer6.pointreader.MainActivity.name";
    //tag that is saved in a bundle to retain the userid after screen orientation changes
    private static final String USER_ID = "com.memoer6.pointreader.MainActivity.user_id";
    //tag that is saved in a bundle to retain the user points after screen orientation changes
    private static final String POINTS = "com.memoer6.pointreader.MainActivity.points";
    //tag that is saved in a bundle to retain the transaction list after screen orientation changes
    private static final String LIST_TRANSACTIONS =
            "com.memoer6.pointreader.MainActivity.list_transactions";
    private static final String LIST_USERS =
            "com.memoer6.pointreader.MainActivity.list_users";
    private static final String SHOW_PROGRESS = "com.memoer6.pointreader.MainActivity.showProgress";


    //The RecyclerView widget is a more advanced and flexible version of ListView.
    // This widget is a container for displaying large data sets that can be scrolled very
    // efficiently by maintaining a limited number of views. Use the RecyclerView widget
    // when you have data collections whose elements change at runtime based on
    // user action or network events.
    private TransactionAdapter mAdapter;
    private List<Transaction> mTransactionList = new ArrayList<>();
    private List<User> mUserList = new ArrayList<>();
    private long mUserId;
    private ProgressBar mProgressBar2;
    private RecyclerView mRecyclerView;
    private LinearLayout mHeaderLayout;
    //preferences to store application settings
    SharedPreferences sharedPref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameText = (TextView) findViewById(R.id.name);
        pointsText = (TextView) findViewById(R.id.points);
        mProgressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        mHeaderLayout = (LinearLayout) findViewById(R.id.headerLayout);



        //initialization of recycle view and layout manager
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //The positioning of the items is configured using the layout manager.
        assert mRecyclerView != null;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //RecyclerView supports custom animations for items as they enter, move, or get
        // deleted using ItemAnimator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        //specify an adapter
        mAdapter = new TransactionAdapter(mTransactionList);
        mRecyclerView.setAdapter(mAdapter);

        //read the setting preferences
       sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        //Log.d("Count", String.valueOf(sharedPref.getInt(SettingsActivity.KEY_PREF_NUMBER, 50)));


        // Restore state members from saved instance
        if (savedInstanceState != null) {

            if (savedInstanceState.getBoolean(SHOW_PROGRESS)) {
                showProgress(true);
            }

            if (savedInstanceState.getString(NAME) != null) {
                nameText.setText(savedInstanceState.getString(NAME));
            }

            if (savedInstanceState.getLong(USER_ID) != 0) {
                mUserId = savedInstanceState.getLong(USER_ID);
            }

            if (savedInstanceState.getString(POINTS) != null) {
                pointsText.setText(savedInstanceState.getString(POINTS));
            }

            if (savedInstanceState.getSerializable(LIST_USERS) != null) {

                mUserList.addAll((ArrayList<User>)
                        savedInstanceState.getSerializable(LIST_USERS));
            }

            if (savedInstanceState.getSerializable(LIST_TRANSACTIONS) != null) {

                mTransactionList.addAll((ArrayList<Transaction>)
                        savedInstanceState.getSerializable(LIST_TRANSACTIONS));
                mAdapter.notifyDataSetChanged();

            }
        //First time the activity run
        } else {

            Intent intent = getIntent();
            mUserId = intent.getLongExtra(FirstActivity.USER_ID, 0);

            Bundle bundle = intent.getExtras();
            mUserList = (List<User>) bundle.getSerializable(FirstActivity.LIST_USERS);

            showUserData();

        }



    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the the name and points

        savedInstanceState.putBoolean(SHOW_PROGRESS, mProgressBar2.isShown());
        savedInstanceState.putString(NAME, nameText.getText().toString());
        savedInstanceState.putLong(USER_ID, mUserId);
        savedInstanceState.putString(POINTS, pointsText.getText().toString());
        savedInstanceState.putSerializable(LIST_USERS,(ArrayList<User>) mUserList);
        savedInstanceState.putSerializable(LIST_TRANSACTIONS,(ArrayList<Transaction>) mTransactionList);


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

        // The filter's action is BROADCAST_USERDATA
        IntentFilter mUserDataIntentFilter = new IntentFilter(
                GetDataIntentService.BROADCAST_USERDATA);

        // Instantiates a new BroadcastReceiver
        mResponseReceiver = new ResponseReceiver();

        // Registers the UserDataReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mResponseReceiver, mUserDataIntentFilter);

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

            showProgress(false);

            if (intent.getAction() != null) {

                if (Objects.equals(intent.getAction(), GetDataIntentService.BROADCAST_USERDATA)) {

                    // if STATUS extra in internet is STATUS SUCCESS,
                    // set STATUS_ERROR by default if status is not found
                    if (intent.getIntExtra(GetDataIntentService.STATUS,
                            GetDataIntentService.STATUS_ERROR) ==
                            GetDataIntentService.STATUS_SUCCESS) {

                        @SuppressWarnings("unchecked")
                        User user = (User) intent
                                .getSerializableExtra(GetDataIntentService.DATA);


                        if (user != null) {

                            //Set 2 decimal digits
                            DecimalFormat df = new DecimalFormat("#.00");
                            pointsText.setText(df.format(user.getTotalPoints()));
                            //pointsText.setText(String.format("%.2f", user.getTotalPoints()));

                            nameText.setText(user.getName());

                            //Unlike ListView, there is no way to add or remove items directly
                            // through the RecyclerView adapter. You need to make changes to
                            // the data source directly and notify the adapter of any changes.
                            // Also, whenever adding or removing elements, always make changes
                            // to the existing list. For instance, reinitializing the list
                            // of Transactions such as the following:
                            // mTransactionList = user.getTransactionList()
                            // will not affect the adapter, since it has a memory reference
                            // to the old list:
                            mTransactionList.clear();
                            mTransactionList.addAll(user.getTransactionList());
                            mAdapter.notifyDataSetChanged();

                        } else {
                            displayMessage(getString(R.string.errorUserDataMessage),
                                    Toast.LENGTH_SHORT);
                        }
                    }

                }

            }

        }
    }

    //retrieve user data from server
    public void showUserData() {

        GetDataIntentService.showUserData(this, mUserId,
                sharedPref.getBoolean(SettingsActivity.KEY_PREF_ORDER, true),
                sharedPref.getInt(SettingsActivity.KEY_PREF_NUMBER, 10));

        showProgress(true);

    }

    //Called when returning from SettingActivity
    //When declaring an activity in your manifest file, you can specify how the activity should
    // associate with a task using the <activity> element's launchMode attribute.
    //The launchMode attribute specifies an instruction on how the activity should be launched
    // into a task.   "singleTask"
    //The system creates a new task and instantiates the activity at the root of the new task.
    // However, if an instance of the activity already exists in a separate task, the system
    // routes the intent to the existing instance through a call to its onNewIntent() method,
    // rather than creating a new instance. Only one instance of the activity can exist at a time.
    @Override
    public void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        showUserData();

    }


    public void displayMessage(String message, int duration) {

        Toast.makeText(this, message, duration).show();

    }

    // inflate the data for the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    // selection of one item in the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Handle item selection
        switch (item.getItemId()) {
            case R.id.select_user:
                showUserMenu(findViewById(R.id.select_user));
                return true;
            case R.id.settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    //A PopupMenu is a modal menu anchored to a View. It appears below the anchor view if
    // there is room, or above the view otherwise.
    public void showUserMenu(View view) {

        //Instantiate a PopupMenu with its constructor, which takes the current application
        // Context and the View to which the menu should be anchored.
        menu = new PopupMenu(this, view);

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                mUserId = item.getItemId();
                showUserData();
                return true;
            }
        });


        for (User user: mUserList) {
            //groupId, itemId, order, titleRes
            menu.getMenu().add(0, user.getId().intValue(),
                    0, user.getName());
        }
        menu.show();

    }


    private void showProgress(final boolean show) {

        mProgressBar2.setVisibility(show ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        nameText.setVisibility(show ? View.GONE : View.VISIBLE);
        pointsText.setVisibility(show ? View.GONE : View.VISIBLE);
        mHeaderLayout.setVisibility(show ? View.GONE : View.VISIBLE);

    }


    @Override
    protected void onResume() {
        // Call up to the superclass.
        super.onResume();

        // Register BroadcastReceivers
        registerResponseReceiver();

        Log.d("MainActivity", "onResume()");

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

        Log.d("MainActivity", "onStop()");

        //dismiss the menu if it's open and the screen is rotated
        //avoid message: MainActivity has leaked window android.widget.PopupWindow after rotation
        if (menu != null) {
            menu.dismiss();
        }

    }

}
