package jedis;

/**
 * Created by Arthur Deschamps on 31.05.17.
 * This interface defines the required methods to be a redis object (to be saved in db)
 */
public interface JedisObject {

    // Validate that object conforms to the schema
    boolean validate();

    // Save object in db
    void save();

    // Delete object from db
    void delete();

    // Must have a unique ID
    String getId();

}
