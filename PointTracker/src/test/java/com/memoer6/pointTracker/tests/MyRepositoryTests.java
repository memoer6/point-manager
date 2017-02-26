package com.memoer6.pointTracker.tests;

import java.util.Optional;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.test.context.junit4.SpringRunner;

import com.memoer6.pointTracker.model.User;
import com.memoer6.pointTracker.repository.UserRepository;

import static org.assertj.core.api.Assertions.*;

/*
//@DataJpaTest can be used if you want to test JPA applications. By default it will configure an in-memory embedded
//database, scan for @Entity classes and configure Spring Data JPA repositories. Regular @Component beans will
//not be loaded into the ApplicationContext.
//Data JPA tests may also inject a TestEntityManager bean which provides an alternative to the standard JPA
//EntityManager specifically designed for tests. If you want to use TestEntityManager outside of @DataJpaTests
//you can also use the @AutoConfigureTestEntityManager annotation.
//In-memory embedded databases generally work well for tests since they are fast and don’t require any developer
//installation. If, however, you prefer to run tests against a real database you can use the
//@AutoConfigureTestDatabase annotation

@RunWith(SpringRunner.class)
@DataJpaTest
public class MyRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @Test
    public void testFindByNameUserRepository() throws Exception {
        this.entityManager.persist(new User("Lina", 10D));
        Optional<User> user = this.repository.findByName("Lina");
        assertThat(user.get().getName()).isEqualTo("Lina");
        assertThat(user.get().getTotalPoints()).isEqualTo(10.0);
    }

}
*/

