package com.memoer6.pointreader;

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


import com.memoer6.pointreader.model.Transaction;
import com.memoer6.pointreader.model.User;
import com.memoer6.pointreader.presenter.Presenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.collection.IsIn.isIn;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

///To integrate Mockito into your JUnit test class you can use the provided Test Runner
//This tells Mockito to take any annotated mocks within the test class and initialise them for
// mocking. You can then simply annotate any instance variable with @Mock to use it as a mock.
//Mockito does have some limitations, however, including
//       You can’t mock final classes
//       You can’t mock static methods
//       You can’t mock final methods
//       You can’t mock equals() or hashCode()

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {


    //The @Mock annotation tells Mockito that mockList is to be treated as a mock and the
    // @RunWith(MockitoJUnitRunner.class) tells Mockito to go through all the @Mock annotated
    // members of MyTest and initialize them for Mocking. You don’t have to assign any new instance
    // to mockList, this is done under the hood for you by Mockito.
    @Mock
    private Presenter mockPresenter;

    @Captor
    private ArgumentCaptor<Integer> intCaptor;

    private User mockUser1, mockUser2;
    List<Transaction> transactionListUser1, reverseTransactionListUser1;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);


    //When writing tests, it is common to find that several tests need similar objects created
    // before they can run. Annotating a public void method with @Before causes that method to be
    // run before the Test method.
    @Before
    public void setUp() throws ParseException {
        MockitoAnnotations.initMocks(this);
        setupUsers();
    }

    //STUBBING
    @Test
    public void test_get_user_list() throws Exception {


        List<User> userListExpected = new ArrayList<>();
        userListExpected.add(mockUser1);
        userListExpected.add(mockUser2);

        //method to stub (provide ‘canned answers’)  behaviour of a given interface or class.
        when(mockPresenter.getUserList()).thenReturn(userListExpected);


        List<User> userListActual = mockPresenter.getUserList();
        assertEquals(userListExpected, userListActual);
        System.out.println(userListActual);

        // Expected  ,  actual
        Assert.assertEquals("check the name","mockUser1", userListActual.get(0).getName());
        Assert.assertEquals("check the name", "mockUser2", userListActual.get(1).getName());
        Assert.assertEquals("check the user id", Long.valueOf(1L), userListActual.get(0).getId());

    }

    @Test
    public void test_read_user_data() throws Exception {


        //given
        long expectedId = 1L;
        //eq - this is due to the fact that you can’t mix real values and matchers in Mock method
        // calls, you either have to have all parameters as Matchers or all parameters as real
        // values. eq() provides a Matcher which only matches when the runtime parameter is equal
        // to the specified parameter in the stub.
        when(mockPresenter.readUserData(eq(expectedId), eq(false), anyInt())).thenReturn(mockUser1);

        //when
        User actualUser1 = mockPresenter.readUserData(expectedId, false, 2);
        java.util.Date date0 = sdf.parse(actualUser1.getTransactionList().get(0).getDate());
        java.util.Date date1 = sdf.parse(actualUser1.getTransactionList().get(1).getDate());


        //Then
        Assert.assertTrue( actualUser1.getId() == expectedId);
        Assert.assertEquals(2, actualUser1.getTransactionList().size());
        Assert.assertFalse( date0.after(date1));
        Assert.assertNotNull(actualUser1.getTotalPoints());

    }

    //We saw above that we created a customer with certain expected values. If we wanted to create
    // a few known test users and return them base on their Id’s we could use an Answer which we
    // could return from our when() calls.Answer is a Generic type provided by Mockito for
    // providing ‘canned responses’.
    @Test
    public void test_read_user_data_with_answer() throws Exception {

        //Given
        Long[] expectedId = {1L,2L};

        //instead of doing when().thenReturn() we do when().thenAnswer() and provide our
        // userById Answer as the Answer to be given.
        //anyLong is a Matcher and it is used to get Mockito to fire the Answer without checking
        // that a particular Long value has been passed in. Matchers let us ignore the parameters
        // to the mock call and instead concentrate only on the return value.
        when(mockPresenter.readUserData(anyLong(), anyBoolean(), anyInt())).thenAnswer(userById);

        //when
        User actualUser1 = mockPresenter.readUserData(expectedId[0], false, 2);
        User actualUser2 = mockPresenter.readUserData(expectedId[1], true, 7);

        //then
        Assert.assertEquals("check the name", "mockUser1", actualUser1.getName());
        Assert.assertEquals("check the name", "mockUser2", actualUser2.getName());


    }

    //create an Answer to return an appropriate User based on the ID which was passed to the
    // readUserData() method passed to the mock Presenter at runtime
    private Answer<User> userById = new Answer<User>() {

        @Override
        public User answer(InvocationOnMock invocation) throws Throwable {

            Object[] args = invocation.getArguments();
            int id = ((Long) args[0]).intValue();
            switch (id) {
                case 1 : return mockUser1;
                case 2 : return mockUser2;
                default: return null;
            }

        }
    };

    @Test(expected = UserNotFoundException.class)
    public void test_user_not_found_exception() throws Exception {

        //given
        when(mockPresenter.readUserData(5, false, 2)).thenThrow(new UserNotFoundException());

        //when
        mockPresenter.readUserData(5, false, 2);

        //Then
        Assert.fail("Exception should be thrown.");


    }

    public class UserNotFoundException extends Exception {

        private static final long serialVersionUID = -6643301294924639178L;
    }


    //Mockito supports BDD (Behaviour Driven Development) out of the box in the org.mockito.
    // BDDMockito class. It replaces the normal stubbing methods – when(), thenReturn(),
    // thenThrow(), thenAnswer() etc with BDD doppelgangers – given(), willReturn(), willThrow(),
    // willAnswer(). This allows us to avoid using when() in the // Given section, as it may
    // be confusing.
    @Test
    public void test_read_user_data_bdd() throws Exception {

        //given
        long expectedId = 1L;
        given(mockPresenter.readUserData(expectedId, false, 2)).willReturn(mockUser1);

        //when
        User actualUser1 = mockPresenter.readUserData(expectedId, false, 2);

        //Then
        Assert.assertTrue( actualUser1.getId() == expectedId);
        Assert.assertEquals("mockUser1", actualUser1.getName());

    }

    //VERIFICATION
    //Verification is the process of confirming the behaviour of a Mock. It is useful in
    // determining that the class we are testing has interacted in an expected way with any of its
    // dependencies. Sometimes we aren’t interested in the values which are returned from a Mock,
    // but are instead interested in how the class under test interacted with it, what values
    // were sent in or how often it was called.
    @Test
    public void simple_interaction_verification() throws Exception {
        // Given

        // When
        mockPresenter.getUserList();
        mockPresenter.getUserList();

        // Then
        //This is simulating a possible interaction within a class under test, but to keep things
        // simple we are doing it out in the unit test class. The next call is the call to
        // verify(mockPresenter).getUserList(). This instructs Mockito to check if there has been
        // a single call to the getUserList() method of the Mock Presenter.
        verify(mockPresenter, times(2)).getUserList();
        verify(mockPresenter, atLeastOnce()).getUserList();   // at least one time
        verify(mockPresenter, atMost(3)).getUserList();   // maximum 3 times
        verify(mockPresenter,never()).readUserData(anyLong(), anyBoolean(), anyInt());

    }

    //Verification with Parameters lets us verify that not only was there an interaction with a
    // Mock, but what parameters were passed to the Mock. To perform verification with parameters
    // you simply pass the parameters of interest into the Mocked method on the verify call
    // on the Mock.
    @Test
    public void verification_with_actual_parameters() throws Exception {

        //Given
        long userId = 1L;
        boolean reverse = false;
        int count = 3;

        //When
        mockPresenter.readUserData(userId, reverse, count);

        //Then
        verify(mockPresenter).readUserData(userId, reverse, count);
        verify(mockPresenter, never()).readUserData(2, reverse, count); //never is called with these parameters
        verify(mockPresenter, times(1)).readUserData(anyLong(), anyBoolean(), anyInt());
    }

    @Test
    public void verification_with_argument_captor() throws Exception {


        //Given
        long userId = 1L;
        boolean reverse = false;
        int count = 3;

        //When
        mockPresenter.readUserData(userId, reverse, count);

        //Then
        verify(mockPresenter).readUserData(eq(userId), eq(reverse), intCaptor.capture());
        Assert.assertEquals(3, intCaptor.getValue().intValue());

    }

    @Test
    public void test_getUserList_Hamcrest() throws Exception {

        //Given
        List<User> userListExpected = new ArrayList<>();
        userListExpected.add(mockUser1);
        userListExpected.add(mockUser2);
        when(mockPresenter.getUserList()).thenReturn(userListExpected);

        //When
        List<User> userListActual = mockPresenter.getUserList();

        //Then
        assertThat("mockUser1", comparesEqualTo(userListActual.get(0).getName()));
        assertThat(userListActual, contains(mockUser1, mockUser2));
        assertThat(userListActual, is(any(List.class)));
        assertThat(userListActual.size(), equalTo(2));
        assertThat(userListActual.size(), is(greaterThan(1)));
        assertThat(userListActual.size(), is(greaterThanOrEqualTo(2)));
        assertThat(userListActual, hasItem(mockUser1));
        assertThat(userListActual, hasItems(mockUser1, mockUser2));
        assertThat(userListActual, hasSize(2));
        assertThat(userListActual.get(0), hasProperty("name"));
        assertThat(userListActual, instanceOf(List.class));
        assertThat(mockUser1, isIn(userListActual));
        assertThat(userListActual.get(0).getName(), not(isEmptyOrNullString()));
        assertThat(userListActual.get(0), notNullValue());
        assertThat(userListActual.get(0).getName(), startsWith("mockUser"));
        assertThat(userListActual.get(0).getName(), allOf(startsWith("mock"),
                containsString("User"),endsWith("1")));
        assertThat(userListActual.get(0).getName(), anyOf(startsWith("mock"),
                containsString("User"),endsWith("3")));
        assertThat(userListActual.get(0).getName(), both(startsWith("mock")).and(endsWith("User1")));
        assertThat(userListActual.get(0).getName(), either(startsWith("mock")).or(endsWith("User3")));


    }





    private void setupUsers() throws ParseException {

        //Date formatter
        Calendar c = Calendar.getInstance();

        //User1
        mockUser1 = new User("mockUser1", 10);
        mockUser1.setId(1L);

        c.setTime(sdf.parse("2010-10-01"));
        Transaction transacion1a = new Transaction(1f, c.getTime(), "transaction 1a");

        c.add(Calendar.DATE, 1);  // number of days to add
        Transaction transaction2a = new Transaction(2f, c.getTime(), "transaction 2a");

        transactionListUser1 = new ArrayList<>(Arrays.asList(transacion1a, transaction2a));

        reverseTransactionListUser1 = new ArrayList<>(Arrays.asList(transaction2a, transacion1a));

        mockUser1.setTransactionList(transactionListUser1);


        //User2
        mockUser2 = new User("mockUser2", 20);
        mockUser2.setId(2L);

        //Transaction List User 2
        List<Transaction> transactionListUser2 = new ArrayList<>();

        c.add(Calendar.DATE, 1);
        transactionListUser2.add(new Transaction(3f, c.getTime(), "transaction 1b"));

        c.add(Calendar.DATE, 1);  // number of days to add
        transactionListUser2.add(new Transaction(4f, c.getTime(), "transaction 2b"));

        mockUser2.setTransactionList(transactionListUser2);

    }


}

