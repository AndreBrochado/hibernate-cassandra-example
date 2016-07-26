import java.io.Serializable;

public class User implements Serializable{

    private String id;

    private String name;
    private String favouriteColour;
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
