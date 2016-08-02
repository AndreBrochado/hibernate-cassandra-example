import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.ogm.cfg.OgmConfiguration;
import org.hibernate.ogm.cfg.OgmProperties;
import org.hibernate.ogm.datastore.impl.AvailableDatastoreProvider;
//import org.hibernate.result.Output;
//import org.hibernate.search.cfg.Environment;

import java.io.*;

public class HOGMDao {

    public OgmConfiguration hogmdao(String db) {

        OgmConfiguration cfgogm = new OgmConfiguration();

        try {
            cfgogm.setProperty( OgmProperties.DATASTORE_PROVIDER, AvailableDatastoreProvider.CASSANDRA_EXPERIMENTAL.name() );
            cfgogm.setProperty( OgmProperties.DATABASE, db );
            cfgogm.setProperty( OgmProperties.HOST, "192.168.0.211:9042" );
            cfgogm.setProperty( "hibernate.search.default.directory_provider ", "ram" );
            return cfgogm;
        }
        catch (Exception e) {
            throw new RuntimeException( e );
        }
    }

    public static void main(String[] args){
        HOGMDao dd = new HOGMDao();
        OgmConfiguration cfgogm = dd.hogmdao( "userbase" );
        cfgogm.addAnnotatedClass( User.class );
        cfgogm.addResource("user.hbm.xml");
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings( cfgogm.getProperties() ).build();
        //registry.
        File f = new File("test.xml");
        try {
            OutputStream os = new FileOutputStream(f);
            cfgogm.getProperties().storeToXML(os, "test");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}