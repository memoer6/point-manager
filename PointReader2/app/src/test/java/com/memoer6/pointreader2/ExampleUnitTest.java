package com.memoer6.pointreader2;

import com.memoer6.pointreader2.model.User;
import com.memoer6.pointreader2.presenter.Presenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

//If your unit test has no dependencies or only has simple dependencies on Android, you should
// run your test on a local development machine. This testing approach is efficient because it
// helps you avoid the overhead of loading the target app and unit test code onto a physical device
// or emulator every time your test is run.

//Unit tests run on a local JVM on your development machine. Our gradle plugin will compile source
// code found in src/test/java and execute it using the usual Gradle testing mechanisms. At runtime,
// tests will be executed against a modified version of android.jar where all final modifiers have
// been stripped off. This lets you use popular mocking libraries, like Mockito.

//The android.jar file that is used to run unit tests does not contain any actual code - that is
// provided by the Android system image on real devices. Instead, all methods throw exceptions
// (by default). This is to make sure your unit tests only test your code and do not depend on
// any particular behaviour of the Android platform (that you have not explicitly mocked
// e.g. using Mockito)



//To integrate Mockito into your JUnit test class you can use the provided Test Runner
//This tells Mockito to take any annotated mocks within the test class and initialise them for
// mocking. You can then simply annotate any instance variable with @Mock to use it as a mock.
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    //Class under test
    private Presenter.Service view;


    //The @Mock annotation tells Mockito that mockList is to be treated as a mock and the
    // @RunWith(MockitoJUnitRunner.class) tells Mockito to go through all the @Mock annotated
    // members of MyTest and initialize them for Mocking. You don’t have to assign any new instance
    // to mockList, this is done under the hood for you by Mockito.
    @Mock
    private Presenter mockPresenter;

    @Captor
    private ArgumentCaptor<Presenter.Service<User>> callbackCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void test_get_user_list() {

        List<User> userListExpected = new ArrayList<>();
        userListExpected.add(new User("mockUser1", 10));
        userListExpected.add(new User("mockUser2", 20));

        when(mockPresenter.getUserList()).thenReturn(userListExpected);





        Mockito.verify(mockPresenter).getUserList();

    }



    /*
    @Test
    public void test_get_user_list() {

        mockPresenter.getUserList();


        List<User> userListTest = new ArrayList<>();
        userListTest.add(new User("mockUser1", 10));
        userListTest.add(new User("mockUser2", 20));


        Mockito.verify(mockPresenter).getUserList();

    }
    */


}

