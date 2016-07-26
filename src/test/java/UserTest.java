/*
 * Hibernate OGM, Domain model persistence for NoSQL datastores
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.context.spi.CurrentSessionContext;
import org.hibernate.ogm.cfg.OgmConfiguration;
import org.hibernate.ogm.exception.impl.Exceptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Shows how to persist and retrieve entities to/from NoSQL stores using Hibernate OGM.
 *
 * @author Gunnar Morling
 */
public class UserTest {

    private static SessionFactory sessionFactory;


    @BeforeClass
    public static void setUpEntityManagerFactory() {
        try {
//        HOGMDao dd = new HOGMDao();
//        OgmConfiguration cfgogm = dd.hogmdao( "userbase" );
//        cfgogm.addAnnotatedClass( User.class );
//        cfgogm.addResource("user.hbm.xml");

//        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings( cfgogm.getProperties() ).build();

            Configuration c = new Configuration();
            c.configure();
//            sessionFactory = cfgogm.buildSessionFactory( registry );
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

        // create a User
        User bob = new User("Bob", 42, "Octarine");
        // persist User
        session.persist(bob);
        tx.commit();
        session.close();


        session = sessionFactory.openSession();
        tx = session.beginTransaction();

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

   /* @Test
    public void simpleQuery(){
       Session cs = null;
       try {
           cs = sessionFactory.openSession();
           Transaction tx = cs.beginTransaction();

           User bob = new User("Bob", 7, "blue");
           System.out.println(bob);
           cs.persist(bob);

           //@SuppressWarnings("unchecked")
           //List<User> list = cs.createSQLQuery("SELECT * FROM \"User\"").list();

           tx.commit();

//           for ( User user : list ) {
//               System.out.println( "Student_Id : " + user.getId() );
//               System.out.println( "Student Name : " + user.getName() );
//           }
       }
       finally {
           cs.close();
       }
   }*/

}