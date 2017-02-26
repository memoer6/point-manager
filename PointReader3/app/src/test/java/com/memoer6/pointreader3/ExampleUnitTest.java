package com.memoer6.pointreader3;

import com.memoer6.pointreader3.model.User;
import com.memoer6.pointreader3.presenter.Presenter;

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

import static org.mockito.internal.verification.VerificationModeFactory.times;

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



//This annotation tells the Mockito test runner to validate that your usage of the framework is
// correct and simplifies the initialization of your mock objects.
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    //Class under test
    private Presenter.Service consumerService;


    //To create a mock object for an Android dependency, add the @Mock annotation before the
    // field declaration.
    @Mock
    private Presenter mockPresenter;

    @Captor
    private ArgumentCaptor<Presenter.Service<User>> callbackCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }


    //TO COMPLETE


    @Test
    public void test_get_user_list() {

        mockPresenter.getUserList();


        List<User> userListTest = new ArrayList<>();
        userListTest.add(new User("mockUser1", 10));
        userListTest.add(new User("mockUser2", 20));


        Mockito.verify(mockPresenter, times(1)).getUserList();




    }


}

