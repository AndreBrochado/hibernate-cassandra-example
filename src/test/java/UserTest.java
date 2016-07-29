import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.ogm.cfg.OgmConfiguration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    private static SessionFactory sessionFactory;


    @BeforeClass
    public static void setUpEntityManagerFactory() {
        try {
            Configuration c = new OgmConfiguration();
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

        // create a User - null id so
        User bob = new User(null, "Bob", 42, "Octarine");
        System.out.println(bob.getId());
        // persist User
        session.persist(bob);
        tx.commit();
        session.close();


        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        //System.out.println(bob.getId());

        User loadedUser = session.get(User.class, bob.getId());
        assertNotEquals(null, loadedUser);
        assertEquals(loadedUser.getName(), bob.getName());
        assertEquals(loadedUser.getFavouriteColour(), bob.getFavouriteColour());
        assertEquals(loadedUser.getFavouriteNumber(), bob.getFavouriteNumber());

        tx.commit();
        session.close();

    }

    @Test
    public void userReadTest(){

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query q = session.createSQLQuery("select  *  from \"User\"").addEntity(User.class);

        @SuppressWarnings("unchecked")
        List<User> listUsers = q.list();
        tx.commit();
        session.close();
        assertFalse(listUsers.isEmpty());

        Schema schema = null;
        try {
            schema = new Schema.Parser().parse(new File("user.avsc"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File("users.avro");
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);
        try {
            dataFileWriter.create(schema, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Object u: listUsers) {
            if(!(u instanceof User)){
                for(Field field : u.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    try {
                        System.out.println(field.getName() + " = " + field.get(u));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                System.exit(1);
            }
//            GenericRecord g = new GenericData.Record(schema);
//            g.put("id", ((User)u).getId());
//            g.put("name", ((User)u).getName());
//            g.put("favourite_number", ((User)u).getFavouriteNumber());
//            g.put("favourite_colour", ((User)u).getFavouriteColour());
//            try {
//                dataFileWriter.append(g);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        try {
            dataFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}