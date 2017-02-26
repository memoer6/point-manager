package com.memoer6.pointreader;


import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.memoer6.pointreader.view.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

//This rule provides functional testing of a single activity.The activity under test will be
// launched before each test annotated with @Test and before any method annotated with @Before.
// It will be terminated after the test is completed and all methods annotated with @After are
// finished. The Activity under Test can be accessed during your test by calling
// ActivityTestRule#getActivity().

@RunWith(AndroidJUnit4.class)
@SmallTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void progressBarIsVisible() {

        //mActivityRule.getActivity().showProgress(true);
        //assertEquals(mActivityRule.getActivity().mProgressBar2.getVisibility(), View.VISIBLE);

    }



}
