import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;

/**
 * Created by root on 7/29/16.
 */
public class MyInsertListener implements PostInsertEventListener {
    @Override
    public void onPostInsert(PostInsertEvent postInsertEvent) {
        if(postInsertEvent.getEntity() instanceof User){
            User u = (User) postInsertEvent.getEntity();
            System.out.println(u.getId() + " " + u.getName() + " " + u.getFavouriteColour() + " " + u.getFavouriteNumber());
        }
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
        return false;
    }
}
