import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.List;


import testpkg.User;

import org.fest.assertions.Assertions;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.ogm.cfg.OgmConfiguration;
import org.hibernate.search.cfg.Environment;
import org.hibernate.search.cfg.SearchMapping;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    private static SessionFactory sessionFactory;


    @BeforeClass
    public static void setUpEntityManagerFactory() {
        try {
            SearchMapping mapping = new SearchMapping();
            mapping.entity(User.class).indexed().property("id", ElementType.FIELD).documentId();


            Configuration c = new OgmConfiguration();
            c.getProperties().put( Environment.MODEL_MAPPING, mapping );
            c.configure();
            sessionFactory = c.buildSessionFactory();
        }catch (Throwable e){
            e.printStackTrace();
        }


    }

    @AfterClass
    public static void closeEntityManagerFactory() {
        sessionFactory.close();
    }

    @Test
    public void userWriteTest() {

        Session session = sessionFactory.openSession();

        Transaction tx = session.beginTransaction();

        // create a testpkg.User - null id so
        User newUser = new User(null, "Twoflower", 7, "Octarine");
        // persist testpkg.User
        session.save(newUser);
        tx.commit();
        session.close();


        session = sessionFactory.openSession();
        tx = session.beginTransaction();

        User loadedUser = session.get(User.class, (Serializable) newUser.getId());
        assertNotEquals(null, loadedUser);
        assertEquals(loadedUser.getName(), newUser.getName());
        assertEquals(loadedUser.getFavouriteColour(), newUser.getFavouriteColour());
        assertEquals(loadedUser.getFavouriteNumber(), newUser.getFavouriteNumber());

        tx.commit();
        session.close();

    }

    @Test
    public void userReadTest(){

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        User newUser = new User( null, "Threeflower", 9, "Octarine 3" );
        session.persist( newUser );
        tx.commit();
        session.clear();

        tx = session.beginTransaction();
        Query q = session.createQuery("FROM User");
        List listUsers = q.list();
        tx.commit();
        session.close();

        assertFalse(listUsers.isEmpty());
        Assertions.assertThat( listUsers ).onProperty( "name" ).contains( newUser.getName() );
    }
}