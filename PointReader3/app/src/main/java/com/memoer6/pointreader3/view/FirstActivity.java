package com.memoer6.pointreader3.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.memoer6.pointreader3.R;
import com.memoer6.pointreader3.model.User;
import com.memoer6.pointreader3.presenter.Presenter;

import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends AppCompatActivity implements Presenter.Service<List<User>> {

    //tag that is saved in a bundle to retain the user list after screen orientation changes
    protected static final String LIST_USERS = "com.memoer6.pointreader2.FirstActivity.list_users";
    protected static final String USER_ID = "com.memoer6.pointreader2.FirstActivity.user_id";
    private static final String SHOW_PROGRESS = "com.memoer6.pointreader2.FirstActivity.showProgress";

    private List<User> mUserList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private Button mButton;
    private PopupMenu menu;
    private Presenter mPresenter;

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
                mPresenter.getUserList();


                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        //Send user list to MainActivity via intent
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


    //Callback from present to show user list in popup menu
    @Override
    public void onSuccess(List<User> userList) {

        mProgressBar.setVisibility(View.INVISIBLE);
        if (userList != null) {

            mUserList = userList;

            if (userList.isEmpty()) {
                //Shows "no users available"
                displayMessage(getString(R.string.emptyUserListMessage),
                        Toast.LENGTH_SHORT);

                //list is not empty
            } else {


                for (User user : userList) {
                    //groupId, itemId, order, titleRes
                    menu.getMenu().add(0, user.getId().intValue(),
                            0, user.getName());
                }
                menu.show();

            }
        }

    }


    //Callback from presenter to display error message
    @Override
    public void onFailure(String message) {

        mProgressBar.setVisibility(View.INVISIBLE);
        displayMessage(message, Toast.LENGTH_SHORT);


    }



    public void displayMessage(String message, int duration) {

        Toast.makeText(this, message, duration).show();

    }


    @Override
    protected void onResume() {
        // Call up to the superclass.
        super.onResume();

        // Register Presenter
        mPresenter = new Presenter(this);

    }

    @Override
    protected void onPause() {
        // Call onPause() in superclass.
        super.onPause();

        // Unregister
        mPresenter = null;
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
