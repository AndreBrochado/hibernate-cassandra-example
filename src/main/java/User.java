import org.hibernate.annotations.GenericGenerator;

import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by root on 7/21/16.
 */

@Entity
//@Indexed
//@Table(name = "User")
public class User implements Serializable{


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "name")
//    @Field(index = Index.YES)
    private String name;
    @Column(name = "favourite_colour")
    private String favouriteColour;
    @Column(name = "favourite_number")
    private int favouriteNumber;

    /**
     * Default constructor.  Note that this does not initialize fields
     * to their default values from the schema.  If that is desired then
     * one should use {@link \#newBuilder()}.
     */
    public User() {
    }

    /**
     * All-args constructor.
     */
    public User(String name, int favouriteNumber, String favouriteColour) {
        this.name = name.toLowerCase();
        this.favouriteNumber = favouriteNumber;
        this.favouriteColour = favouriteColour.toLowerCase();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFavouriteColour() {
        return favouriteColour;
    }

    public void setFavouriteColour(String favouriteColour) {
        this.favouriteColour = favouriteColour;
    }

    public int getFavouriteNumber() {
        return favouriteNumber;
    }

    public void setFavouriteNumber(int favouriteNumber) {
        this.favouriteNumber = favouriteNumber;
    }
}
